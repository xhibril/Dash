package com.Xhibril.Dash.service;
import com.Xhibril.Dash.dto.auth.PasswordResetResponse;
import com.Xhibril.Dash.model.PasswordReset;
import com.Xhibril.Dash.model.User;
import com.Xhibril.Dash.repository.PasswordResetRepository;
import com.Xhibril.Dash.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final static SecureRandom random = new SecureRandom();

    private final PasswordResetRepository passwordResetRepo;
    private final UserRepository userRepo;
    private final EmailService emailService;
    private final PasswordEncoder encoder;
    private final AuthService authService;

    public PasswordResetService(PasswordResetRepository passwordResetRepo,
                                UserRepository userRepo,
                                EmailService emailService,
                                PasswordEncoder encoder,
                                AuthService authService){
        this.passwordResetRepo = passwordResetRepo;
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.encoder = encoder;
        this.authService = authService;
    }


    // generate code
    @Transactional
    public ResponseEntity<PasswordResetResponse> initPasswordReset(String email){

        Optional<PasswordReset> isFound = passwordResetRepo.findByEmail(email);
        PasswordReset passwordReset = new PasswordReset();

        if(isFound.isPresent()){
            // delete if a reset req already exists
            passwordResetRepo.deleteByEmail(email);
        }

        Optional<User> userOpt = userRepo.findByEmail(email);

        // return early if user is not found
        if(userOpt.isEmpty()){
            return ResponseEntity.ok(new PasswordResetResponse("Code sent"));
        }

        String code = String.valueOf(100000 + random.nextInt(900000));

        passwordReset.setEmail(email);
        passwordReset.setCode(encoder.encode(code));
        passwordReset.setExpiresAt(Instant.now().plusSeconds(600));
        passwordReset.setAttempts(6);

        passwordResetRepo.save(passwordReset);

        emailService.sendVerificationCode(email, code);

        return ResponseEntity.ok(new PasswordResetResponse("Verification code sent"));
    }




    @Transactional
    public ResponseEntity<PasswordResetResponse> verifyCode(String email, String clientSideCode){

        Optional<PasswordReset> isFound = passwordResetRepo.findByEmail(email);
        PasswordResetResponse response = new PasswordResetResponse();

        if(isFound.isPresent()){
           PasswordReset passwordReset = isFound.get();

           if(passwordReset.getAttempts() <= 0){
               // delete request if no more attempts remaining
               passwordResetRepo.deleteByEmail(email);
               return ResponseEntity.badRequest().
                       body(new PasswordResetResponse("No more attempts remaining"));
           }

           Instant current = Instant.now();

           // check if code has expired
           if(current.isAfter(passwordReset.getExpiresAt())){
               passwordResetRepo.deleteByEmail(email);
               return ResponseEntity.badRequest().
                       body(new PasswordResetResponse("Code has expired"));
           }

           // check if valid
           if(!encoder.matches(clientSideCode, passwordReset.getCode())){
               int remainingAttempts = passwordReset.getAttempts();
               remainingAttempts -= 1;
               passwordResetRepo.updateRemainingAttempts(remainingAttempts, email);

               return ResponseEntity.badRequest().
                       body(new PasswordResetResponse("Invalid code"));
           }

           String resetToken = saveResetToken(email);
           response.setResetToken(resetToken);
           response.setMessage("Verification successful");

           passwordResetRepo.deleteConfirmationCode(email);
        } else {
            return ResponseEntity.badRequest().body(new PasswordResetResponse(("Invalid request")));
        }
        return ResponseEntity.ok(response);

    }



    @Transactional
    public ResponseEntity<PasswordResetResponse> resetPassword(String email, String newPassword, String confirmPassword, String resetToken){
        Optional<PasswordReset> isFound = passwordResetRepo.findByEmail(email);

        if(isFound.isPresent()){
            PasswordReset passwordReset = isFound.get();
            Instant current = Instant.now();

            // check if reset token has expired
            if(current.isAfter((passwordReset.getTokenExpiresAt()))){
                passwordResetRepo.deleteByEmail(email);
                return ResponseEntity.badRequest().body(new PasswordResetResponse("Password reset expired"));
            }

            // check if reset token is valid
            if(!resetToken.equals(passwordReset.getResetToken())){
                return ResponseEntity.badRequest().body(new PasswordResetResponse(("Invalid request")));
            }

            if(!newPassword.equals(confirmPassword)){
                return ResponseEntity.badRequest().body(new PasswordResetResponse("Passwords do not match"));
            }

            if(!authService.isPasswordNew(newPassword, email)){
                return ResponseEntity.badRequest().body(new PasswordResetResponse("New password cannot be the same as old"));
            }

            userRepo.updatePassword(encoder.encode(newPassword), email);

            passwordResetRepo.deleteByEmail(email);
            return ResponseEntity.ok(new PasswordResetResponse("Password successfully changed"));

        } else {
            return ResponseEntity.badRequest().body(new PasswordResetResponse(("Invalid request")));
        }

    }

    public String saveResetToken(String email) {
        String resetToken = UUID.randomUUID().toString();
        Instant resetTokenExpiration = Instant.now().plusSeconds(600);
        passwordResetRepo.addResetToken(resetToken, resetTokenExpiration, email);
        return resetToken;
    }
}
