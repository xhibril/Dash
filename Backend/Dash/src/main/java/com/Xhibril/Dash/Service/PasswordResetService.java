package com.Xhibril.Dash.Service;

import com.Xhibril.Dash.Dto.PasswordResetRequest;
import com.Xhibril.Dash.Dto.PasswordResetResponse;
import com.Xhibril.Dash.Model.PasswordReset;
import com.Xhibril.Dash.Model.User;
import com.Xhibril.Dash.Repository.PasswordResetRepository;
import com.Xhibril.Dash.Repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

@Autowired
    PasswordResetRepository passwordResetRepo;
@Autowired
    UserRepository userRepo;

private final static SecureRandom random = new SecureRandom();
    @Autowired
    private EmailService emailService;

    // generate code
    @Transactional
    public ResponseEntity<PasswordResetResponse> initPasswordReset(String email) throws MessagingException {

        Optional<PasswordReset> isFound = passwordResetRepo.findByEmail(email);
        PasswordReset passwordReset = new PasswordReset();

        if(isFound.isPresent()){
            // delete if a reset req already exists
            passwordResetRepo.deleteByEmail(email);
        }

        Optional<User> user = userRepo.findByEmail(email);

        // return early if user is not found
        if(user.isEmpty()){
            return ResponseEntity.ok(new PasswordResetResponse("Code sent"));
        }

        String code = String.valueOf(100000 + random.nextInt(900000));

        passwordReset.setEmail(email);
        passwordReset.setCode(code);
        passwordReset.setExpiresAt(Instant.now().plusSeconds(600));
        passwordReset.setAttempts(6);

        passwordResetRepo.save(passwordReset);

        emailService.sendVerificationCode(email, code);

        return ResponseEntity.ok(new PasswordResetResponse("Code sent"));
    }




    @Transactional
    public ResponseEntity<PasswordResetResponse> verifyCode(String email, String clientSideCode){

        Optional<PasswordReset> isFound = passwordResetRepo.findByEmail(email);
        PasswordResetResponse response = new PasswordResetResponse();

        if(isFound.isPresent()){
           PasswordReset passwordReset = isFound.get();

           if(passwordReset.getAttempts() <= 0){
               return ResponseEntity.badRequest().
                       body(new PasswordResetResponse("No more attempts remaining"));
           }

           Instant current = Instant.now();

           if(current.isAfter(passwordReset.getExpiresAt())){
               return ResponseEntity.badRequest().
                       body(new PasswordResetResponse("Code has expired"));
           }

           if(!passwordReset.getCode().equals(clientSideCode)){
               return ResponseEntity.badRequest().
                       body(new PasswordResetResponse("Invalid code"));
           }

           String resetToken = saveResetToken(email);
           response.setResetToken(resetToken);

           passwordResetRepo.deleteConfirmationCode(email);
        } else {
            return ResponseEntity.badRequest().body(new PasswordResetResponse(("Something went wrong, please try again")));
        }
        return ResponseEntity.ok(response);

    }



    @Transactional
    public ResponseEntity<PasswordResetResponse> resetPassword(String email, String newPassword, String clientSideToken){
        Optional<PasswordReset> isFound = passwordResetRepo.findByEmail(email);

        if(isFound.isPresent()){

            PasswordReset passwordReset = isFound.get();
            Instant current = Instant.now();


            if(current.isAfter((passwordReset.getTokenExpiresAt()))){
                return ResponseEntity.badRequest().body(new PasswordResetResponse("Password reset expired"));
            }

            if(!clientSideToken.equals(passwordReset.getResetToken())){
                return ResponseEntity.badRequest().body(new PasswordResetResponse(("Something went wrong, please try again")));
            }

            userRepo.updatePassword(newPassword, email);

            passwordResetRepo.deleteByEmail(email);
            return ResponseEntity.ok(new PasswordResetResponse("Password successfully changed"));

        } else {
            return ResponseEntity.badRequest().body(new PasswordResetResponse(("Something went wrong, please try again")));
        }

    }


    public String saveResetToken(String email) {
        String resetToken = UUID.randomUUID().toString();
        Instant resetTokenExpiration = Instant.now().plusSeconds(600);
        passwordResetRepo.addResetToken(resetToken, resetTokenExpiration, email);
        return resetToken;
    }

}
