package com.Xhibril.Dash.service;
import com.Xhibril.Dash.dto.account.UpdateEmailResponse;
import com.Xhibril.Dash.model.UpdateEmail;
import com.Xhibril.Dash.model.User;
import com.Xhibril.Dash.repository.ChangeEmailRepository;
import com.Xhibril.Dash.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class UpdateEmailService {
    private final static SecureRandom random = new SecureRandom();

    private final EmailService emailService;
    private final UserRepository userRepo;
    private final ChangeEmailRepository changeEmailRepo;

    public UpdateEmailService(EmailService emailService,
                              UserRepository userRepo,
                              ChangeEmailRepository changeEmailRepo){
        this.emailService = emailService;
        this.userRepo = userRepo;
        this.changeEmailRepo = changeEmailRepo;
    }

    @Transactional
    public ResponseEntity<UpdateEmailResponse> initEmailChange(Long id, String pendingEmail, String password) throws MessagingException {
        Optional<User> user = userRepo.findByEmail(pendingEmail);

        // check if user already has made a req
       boolean hasPendingRequest = changeEmailRepo.existsByUserId(id);
        if(hasPendingRequest){
            changeEmailRepo.deleteAllByUserId(id);
        }

        // check if email already exists
        if(user.isPresent()){
            return ResponseEntity.badRequest().body(new UpdateEmailResponse("Email is already registered"));
        }

        user = userRepo.findById(id);
        User u = user.get();
        if(!password.equals(u.getPassword())){
            return ResponseEntity.badRequest().body(new UpdateEmailResponse("Incorrect password"));
        }

        UpdateEmail changeEmail = new UpdateEmail();

        String code = String.valueOf(100000 + random.nextInt(900000));
        emailService.sendVerificationCode(pendingEmail, code);


        changeEmail.setVerificationCode(code);
        changeEmail.setUserId(id);
        changeEmail.setPendingEmail(pendingEmail);
        changeEmail.setExpiresAt(Instant.now().plusSeconds(600));
        changeEmailRepo.save(changeEmail);

        return ResponseEntity.ok().body(new UpdateEmailResponse("Verification code sent"));
    }



    @Transactional
    public ResponseEntity<UpdateEmailResponse> verifyChangeEmailRequest(Long id, String code){
        Optional<UpdateEmail> request = changeEmailRepo.findByUserId(id);

        if(request.isPresent()){
            UpdateEmail changeEmail = request.get();

            Instant current = Instant.now();

            // check if code has expired
            if(current.isAfter(changeEmail.getExpiresAt())){
                return ResponseEntity.badRequest().body(new UpdateEmailResponse("Code has expired"));
            }


            if(!code.equals(changeEmail.getVerificationCode())){
                return ResponseEntity.badRequest().body(new UpdateEmailResponse("Incorrect code"));
            }

            // save reset token n return it
            String resetToken = UUID.randomUUID().toString();
            Instant resetTokenExpiration = Instant.now().plusSeconds(600);

            changeEmailRepo.saveResetToken(resetToken, resetTokenExpiration, id);
            changeEmailRepo.deleteVerificationCode(id);

            UpdateEmailResponse response = new UpdateEmailResponse();
            response.setMessage("Verification successful");
            response.setResetToken(resetToken);
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.badRequest().body(new UpdateEmailResponse("Something went wrong, please try again"));
    }



    @Transactional
    public ResponseEntity<UpdateEmailResponse> changeEmail(Long id, String pendingEmail, String resetToken){
        Optional<UpdateEmail> request = changeEmailRepo.findByUserId(id);

        if(request.isPresent()){
            UpdateEmail saved = request.get();
            Instant current = Instant.now();

            // check if token has expired
            if(current.isAfter(saved.getResetTokenExpiration())){
                return ResponseEntity.badRequest().body(new UpdateEmailResponse("Email reset expired"));
            }

            // check if reset token is valid
            if(!resetToken.equals(saved.getResetToken())){
                return ResponseEntity.badRequest().body(new UpdateEmailResponse("Something went wrong, please try again"));
            }

            User savedUser = userRepo.findEmailById(id);
            userRepo.updateEmail(pendingEmail, id);

            // delete request(s)
            changeEmailRepo.deleteAllByUserId(id);

            UpdateEmailResponse response = new UpdateEmailResponse();
            response.setOldEmail(savedUser.getEmail());

            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.badRequest().body(new UpdateEmailResponse("Something went wrong, please try again"));
    }
}
