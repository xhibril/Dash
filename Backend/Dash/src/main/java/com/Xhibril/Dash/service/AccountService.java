package com.Xhibril.Dash.service;
import com.Xhibril.Dash.model.User;
import com.Xhibril.Dash.repository.SupportRepository;
import com.Xhibril.Dash.repository.UrlRepository;
import com.Xhibril.Dash.repository.UrlStatRepository;
import com.Xhibril.Dash.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
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

    public AccountService(UrlRepository urlRepo,
                          SupportRepository supportRepo,
                          UrlStatRepository urlStatRepo,
                          UserRepository userRepo,
                          AuthService authService){
        this.urlRepo = urlRepo;
        this.supportRepo= supportRepo;
        this.urlStatRepo = urlStatRepo;
        this.userRepo = userRepo;
        this.authService = authService;
    }


    @Transactional
    public ResponseEntity<String> deleteAccount(Long id, String password, HttpServletResponse res) {
        Optional<User> user = userRepo.findById(id);

        if (user.isPresent()) {
            User u = user.get();
            if (!(password.equals(u.getPassword()))) {
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
        Optional<User> user = userRepo.findById(id);

        if(user.isPresent()){
            User u = user.get();

            if(!oldPassword.equals(u.getPassword())){
                return ResponseEntity.badRequest().body("Incorrect password");
            }

            userRepo.updatePassword(newPassword, u.getEmail());
            return ResponseEntity.ok().body("Password successfully changed");
        }
        return ResponseEntity.badRequest().body("Something went wrong, please try again");
    }
}
