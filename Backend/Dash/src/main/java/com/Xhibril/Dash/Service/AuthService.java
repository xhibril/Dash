package com.Xhibril.Dash.Service;
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
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepo;
    @Autowired
    private JavaMailSender mailSender;


    public void addUser(String email, String password) throws Exception {

        if(userRepo.findByEmail(email).isEmpty()){
            User user = new User();
            user.setEmail(email);
            user.setPass(password);
            user.setVerified(false);
            User savedUser = userRepo.save(user);

            sendVerificationEmail(savedUser.getId(), email);
        }
    }


    @Async
    public void sendVerificationEmail(Long id, String to) throws Exception {

        String baseUrl = "http://localhost:8080/api";
        String encodedToken = URLEncoder.encode(generateToken(id, 600), StandardCharsets.UTF_8);
        String link = baseUrl + "/email/verify/" + encodedToken;

        String html = """
        <div style="font-family: Arial; line-height:1.6;">
            <h2>Verify your email</h2>
            <p>Click the button below to verify your account:</p>
            <a href="%s"
               style="
                 display:inline-block;
                 padding:12px 20px;
                 background: #667085;
                 color:white;
                 text-decoration:none;
                 border-radius:6px;
                 font-weight:bold;">
               Verify Email
            </a>
            <p style="margin-top:20px;font-size:12px;color:#666;">
                If you didn’t request this, ignore this email.
            </p>
        </div>
    """.formatted(link);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Verify your email");
        helper.setText(html, true);
        helper.setFrom("noreply@xhibril.dev");
        mailSender.send(message);
    }



    public String login(String email, String pass, HttpServletResponse res){
        Optional<User> cred = userRepo.findByEmail(email);

        if(cred.isPresent()){
            User user = cred.get();

            if(pass.equals(user.getPass())){
                String token = generateToken(user.getId(), 604800);
                saveToken(token, res);
            } else {
                return "Password is incorrect";
            }
        } else {
            return "User does not exist";
        }
        return "SUCCESS";
    }



    public String generateToken(Long id, int time){
        String secret = System.getenv("JWT_SECRET");
        Instant now = Instant.now();

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        String token = Jwts.builder()
                .setSubject("authToken")
                .claim("id", id)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(time)))
                .signWith(key)
                .compact();

        return token;
    }


    public void saveToken(String token, HttpServletResponse res){
        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);
        //  cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);  // 7 days
        res.addCookie(cookie);
    }


    public Long getAuthenticatedId(HttpServletRequest req){
        String token = getCookie(req);
        Long id = extractId(token);
        return id;
    }


    public String getCookie(HttpServletRequest req){
        Cookie[] cookies = req.getCookies();
        String token = null;

        if(cookies != null){
            for(Cookie cookie : cookies){
                if("authToken".equals(cookie.getName())){
                    token = cookie.getValue();
                }
            }
        }
        return token;
    }


    public Long extractId(String token){
        String secretKey = System.getenv("JWT_SECRET");

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long id = claims.get("id", Long.class);
            return id;

        } catch (Exception e) {
            return null;
        }
    }



    @Transactional
    public boolean verifyUser(String token){
        Long id = extractId(token);
        System.out.println("Extracted ID: " + id);

        Optional<User> isFound = userRepo.findById(id);

        if(isFound.isPresent()){
            userRepo.verifyUser(id);
            return true;
        }

        return false;
    }






}
