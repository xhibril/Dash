package com.Xhibril.Dash.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import com.Xhibril.Dash.Repository.AuthRepository;
import com.Xhibril.Dash.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired AuthRepository authRepo;


    public void addUser(String email, String password){

        if(authRepo.findByEmail(email).isEmpty()){
            User user = new User();
            user.setEmail(email);
            user.setPass(password);
            authRepo.save(user);
        }
    }


    public String login(String email, String pass, HttpServletResponse res){
        Optional<User> cred = authRepo.findByEmail(email);

        if(cred.isPresent()){
            User user = cred.get();

            if(pass.equals(user.getPass())){
                String token = generateToken(user.getId());
                saveToken(token, res);
            } else {
                return "Password is incorrect";
            }
        } else {
            return "User does not exist";
        }
        return "SUCCESS";
    }




    public String generateToken(Long id){
        String secret = System.getenv("JWT_SECRET");
        Instant now = Instant.now();

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        String token = Jwts.builder()
                .setSubject("authToken")
                .claim("id", id)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(3600)))
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
        System.out.println("IDDDDDDDDDDDDDDD: " + id);
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








}
