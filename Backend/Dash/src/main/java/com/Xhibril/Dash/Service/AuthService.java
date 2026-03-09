package com.Xhibril.Dash.Service;
import io.jsonwebtoken.JwtBuilder;
import jakarta.mail.internet.MimeMessage;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import com.Xhibril.Dash.Repository.UserRepository;
import com.Xhibril.Dash.Model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepo;
    @Autowired
    JwtService jwtService;

    @Autowired EmailService emailService;

    public ResponseEntity<String> addUser(String email, String password) throws Exception {

        if(userRepo.findByEmail(email).isEmpty()){
            User user = new User();
            user.setEmail(email);
            user.setPass(password);
            user.setVerified(false);

            userRepo.save(user);
            emailService.sendVerificationEmail(email);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Account already exists");
        }
    }



    public ResponseEntity<String> login(String email, String pass, boolean rememberMe, HttpServletResponse res) throws Exception {
        Optional<User> cred = userRepo.findByEmail(email);

        if(cred.isPresent()){
            User user = cred.get();

            if(pass.equals(user.getPass())){

                // user is not verified
                if(!accountStatus(email)){
                    emailService.sendVerificationEmail(email);
                    return ResponseEntity.status(403).build();
                }

             Map<String, Object> claims = new HashMap<>();
             claims.put("id", user.getId());

             int time = (rememberMe ? 604800 : 7200);


             System.out.println("TIMEEEE" + time);
                String token = jwtService.generateToken("authToken", claims, time);
                jwtService.saveToken("authToken",token,time, res);
            } else {
                return ResponseEntity.badRequest().body("Incorrect credentials");
            }
        } else {
            return ResponseEntity.badRequest().body("Incorrect credentials");
        }
        return ResponseEntity.ok().build();
    }



    @Transactional
    public boolean verifyUser(String token){
        String email = jwtService.extractFromToken(token, "email", String.class);

        Optional<User> isFound = userRepo.findByEmail(email);

        if(isFound.isPresent()){
            userRepo.verifyUser(email);
            return true;
        }

        return false;
    }


    public Long getAuthenticatedId(HttpServletRequest req){
        String token = jwtService.getTokenFromCookie("authToken", req);
        return jwtService.extractFromToken(token, "id", Long.class);
    }


    public boolean accountStatus(String email){
        Optional<User> isFound = userRepo.findByEmail(email);


        if(isFound.isPresent()){
            User user = isFound.get();

            return user.getVerified();
        }
        return false;
    }



    public boolean isAuthenticated(HttpServletRequest req){
        Long id = getAuthenticatedId(req);
        return id != null;
    }



}
