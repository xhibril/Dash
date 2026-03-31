package com.Xhibril.Dash.service;
import com.Xhibril.Dash.model.User;
import com.Xhibril.Dash.repository.SupportRepository;
import com.Xhibril.Dash.repository.UrlRepository;
import com.Xhibril.Dash.repository.UrlStatRepository;
import com.Xhibril.Dash.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountService {

    private final UrlRepository urlRepo;
    private final SupportRepository supportRepo;
    private final UrlStatRepository urlStatRepo;
    private final UserRepository userRepo;
    private final AuthService authService;
    private final PasswordEncoder encoder;

    public AccountService(UrlRepository urlRepo,
                          SupportRepository supportRepo,
                          UrlStatRepository urlStatRepo,
                          UserRepository userRepo,
                          AuthService authService,
                          PasswordEncoder encoder){
        this.urlRepo = urlRepo;
        this.supportRepo= supportRepo;
        this.urlStatRepo = urlStatRepo;
        this.userRepo = userRepo;
        this.authService = authService;
        this.encoder = encoder;
    }


    @Transactional
    public ResponseEntity<String> deleteAccount(Long id, String password, HttpServletResponse res) {
        Optional<User> userOpt = userRepo.findById(id);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!(encoder.matches(password, user.getPassword()))){
                return ResponseEntity.badRequest().body("Incorrect password");
            }

            // delete support tickets
            supportRepo.deleteAllTickets(id);

            // delete urls / urls stats
            urlStatRepo.deleteAllByUserUrls(id);
            urlRepo.deleteAllByUserId(id);

            // delete account
            userRepo.deleteById(id);

            authService.logout(res);
        } else{
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }


    @Transactional
    public ResponseEntity<String> updatePassword(Long id, String oldPassword, String newPassword){
        Optional<User> userOpt = userRepo.findById(id);

        if(userOpt.isPresent()){
            User user = userOpt.get();

            if(!encoder.matches(oldPassword, user.getPassword())){
                return ResponseEntity.badRequest().body("Incorrect password");
            }


            if(!authService.isPasswordNew(newPassword, user.getEmail())){
                return ResponseEntity.badRequest().body("New password cannot be the same as old");
            }

            userRepo.updatePassword(encoder.encode(newPassword), user.getEmail());
            return ResponseEntity.ok().body("Password successfully changed");
        }
        return ResponseEntity.badRequest().body("Something went wrong, please try again");
    }
}
