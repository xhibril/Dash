package com.Xhibril.Dash.Service;
import io.jsonwebtoken.JwtBuilder;
import jakarta.mail.internet.MimeMessage;
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

    public void addUser(String email, String password) throws Exception {

        if(userRepo.findByEmail(email).isEmpty()){
            User user = new User();
            user.setEmail(email);
            user.setPass(password);
            user.setVerified(false);
            User savedUser = userRepo.save(user);

            emailService.sendVerificationEmail(email);
        }
    }



    public String login(String email, String pass, HttpServletResponse res){
        Optional<User> cred = userRepo.findByEmail(email);

        if(cred.isPresent()){
            User user = cred.get();

            if(pass.equals(user.getPass())){


             Map<String, Object> claims = new HashMap<>();
             claims.put("id", user.getId());


                String token = jwtService.generateToken("authToken", claims, 604800);
                jwtService.saveToken("authToken",token, res);
            } else {
                return "Password is incorrect";
            }
        } else {
            return "User does not exist";
        }
        return "SUCCESS";
    }



    @Transactional
    public boolean verifyUser(String token){
        Long id = jwtService.extractFromToken(token, "id", Long.class);
        System.out.println("Extracted ID: " + id);

        Optional<User> isFound = userRepo.findById(id);

        if(isFound.isPresent()){
            userRepo.verifyUser(id);
            return true;
        }

        return false;
    }


    public Long getAuthenticatedId(HttpServletRequest req){
        String token = jwtService.getTokenFromCookie("authToken", req);
        return jwtService.extractFromToken(token, "id", Long.class);
    }






}
