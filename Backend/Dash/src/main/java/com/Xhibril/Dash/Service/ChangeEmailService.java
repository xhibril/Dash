package com.Xhibril.Dash.Service;

import com.Xhibril.Dash.Dto.ChangeEmailRequest;
import com.Xhibril.Dash.Dto.ChangeEmailResponse;
import com.Xhibril.Dash.Model.ChangeEmail;
import com.Xhibril.Dash.Model.User;
import com.Xhibril.Dash.Repository.ChangeEmailRepository;
import com.Xhibril.Dash.Repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChangeEmailService {

    private final static SecureRandom random = new SecureRandom();

    @Autowired EmailService emailService;


    @Autowired AuthService authService;
    @Autowired
    UserRepository userRepo;
    @Autowired
    ChangeEmailRepository changeEmailRepo;

    @Transactional
    public ResponseEntity<ChangeEmailResponse> initEmailChange(Long id, @RequestBody ChangeEmailRequest request) throws MessagingException {
        Optional<User> user = userRepo.findByEmail(request.getPendingEmail());

        // check if user already has made a req
       boolean hasPendingRequest = changeEmailRepo.existsByUserId(id);
        if(hasPendingRequest){
            changeEmailRepo.deleteAllByUserId(id);
        }

        if(user.isPresent()){
            return ResponseEntity.badRequest().body(new ChangeEmailResponse("Email is already registered"));
        }

        user = userRepo.findById(id);
        User u = user.get();
        if(!request.getPassword().equals(u.getPass())){
            return ResponseEntity.badRequest().body(new ChangeEmailResponse("Incorrect password"));
        }

        ChangeEmail changeEmail = new ChangeEmail();

        String code = String.valueOf(100000 + random.nextInt(900000));
        emailService.sendVerificationCode(request.getPendingEmail(), code);


        changeEmail.setVerificationCode(code);
        changeEmail.setUserId(id);
        changeEmail.setPendingEmail(request.getPendingEmail());
        changeEmail.setExpiresAt(Instant.now().plusSeconds(600));
        changeEmailRepo.save(changeEmail);

        return ResponseEntity.ok().build();
    }



    @Transactional
    public ResponseEntity<ChangeEmailResponse> verifyChangeEmailRequest(Long id, @RequestBody ChangeEmailRequest changeEmailRequest){


        Optional<ChangeEmail> request = changeEmailRepo.findByUserId(id);

        if(request.isPresent()){
            ChangeEmail changeEmail = request.get();

            Instant current = Instant.now();

            // check if code has expired
            if(current.isAfter(changeEmail.getExpiresAt())){
                return ResponseEntity.badRequest().body(new ChangeEmailResponse("Code has expired"));
            }

            if(!changeEmailRequest.getCode().equals(changeEmail.getVerificationCode())){
                return ResponseEntity.badRequest().body(new ChangeEmailResponse("Incorrect code"));
            }


            String resetToken = UUID.randomUUID().toString();
            Instant resetTokenExpiration = Instant.now().plusSeconds(600);

            changeEmailRepo.saveResetToken(resetToken, resetTokenExpiration, id);
            changeEmailRepo.deleteVerificationCode(id);

            ChangeEmailResponse response = new ChangeEmailResponse();
            response.setMessage("Verification successful");
            response.setResetToken(resetToken);
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.badRequest().build();
    }



    @Transactional
    public ResponseEntity<ChangeEmailResponse> changeEmail(Long id, @RequestBody ChangeEmailRequest changeEmailRequest){
        Optional<ChangeEmail> request = changeEmailRepo.findByUserId(id);


        if(request.isPresent()){

            ChangeEmail saved = request.get();


            Instant current = Instant.now();


            if(current.isAfter(saved.getResetTokenExpiration())){
                return ResponseEntity.badRequest().body(new ChangeEmailResponse("Email reset expired"));
            }

            if(!changeEmailRequest.getResetToken().equals(saved.getResetToken())){
                return ResponseEntity.badRequest().build();
            }



            String oldEmail = userRepo.findEmailById(id);

            userRepo.updateEmail(changeEmailRequest.getPendingEmail(), id);

            // delete request(s) after email succ changed
            changeEmailRepo.deleteAllByUserId(id);

            ChangeEmailResponse response = new ChangeEmailResponse();
            response.setOldEmail(oldEmail);

            return ResponseEntity.ok().body(response);


        }
        return ResponseEntity.badRequest().build();
    }
}
