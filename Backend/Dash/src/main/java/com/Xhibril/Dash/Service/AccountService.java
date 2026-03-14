package com.Xhibril.Dash.Service;

import com.Xhibril.Dash.Model.Url;
import com.Xhibril.Dash.Model.User;
import com.Xhibril.Dash.Repository.SupportRepository;
import com.Xhibril.Dash.Repository.UrlRepository;
import com.Xhibril.Dash.Repository.UrlStatRepository;
import com.Xhibril.Dash.Repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    UrlRepository urlRepo;
    @Autowired
    SupportRepository supportRepo;

    @Autowired
    UrlStatRepository urlStatRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired AuthService authService;

    @Transactional
    public ResponseEntity<Void> deleteAccount(Long id, HttpServletResponse res){

        // delete support tickets
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent()){
            User u = user.get();
            supportRepo.deleteAllByEmail(u.getEmail());
        }

        // delete urls / urls stats
        urlStatRepo.deleteAllByUserUrls(id);
        urlRepo.deleteAllByUserId(id);

        // delete account
        userRepo.deleteById(id);

        authService.logout(res);

        return ResponseEntity.ok().build();
    }
}
