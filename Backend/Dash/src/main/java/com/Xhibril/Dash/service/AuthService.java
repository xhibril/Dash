package com.Xhibril.Dash.service;
import com.Xhibril.Dash.dto.auth.LoginResponse;
import com.Xhibril.Dash.dto.auth.SignUpResponse;
import org.springframework.http.ResponseEntity;
import com.Xhibril.Dash.repository.UserRepository;
import com.Xhibril.Dash.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository userRepo,
                       JwtService jwtService,
                       EmailService emailService,
                       PasswordEncoder encoder){
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.encoder = encoder;
    }

    enum TIME {
        WEEK(604800),
        TWO_HOURS(7200);

        private final int seconds;

        TIME(int seconds){
            this.seconds = seconds;
        }

        public int getSeconds(){
            return seconds;
        }
    }


    public ResponseEntity<SignUpResponse> registerUser(String email, String password){
        if(userRepo.findByEmail(email).isEmpty()){
            User user = new User();
            user.setEmail(email);
            user.setPassword(encoder.encode(password));
            user.setVerified(false);

            userRepo.save(user);
            emailService.sendVerificationEmail(email);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(new SignUpResponse("Account already exists"));
        }
    }


    public ResponseEntity<LoginResponse> login(String email, String password, boolean rememberMe, HttpServletResponse res) throws Exception {
        Optional<User> userOpt = userRepo.findByEmail(email);

        if(userOpt.isPresent()){
            User user = userOpt.get();

            if(encoder.matches(password, user.getPassword())){
                // user is not verified
                if(!user.getVerified()){
                    emailService.sendVerificationEmail(email);
                    return ResponseEntity.status(403).build();
                }

             Map<String, Object> claims = new HashMap<>();
             claims.put("id", user.getId());

             int time = (rememberMe ? TIME.WEEK.getSeconds() : TIME.TWO_HOURS.getSeconds());

                String token = jwtService.generateToken("authToken", claims, time);
                jwtService.saveToken("authToken",token,time, res);
            } else {
                return ResponseEntity.badRequest().body(new LoginResponse("Incorrect credentials"));
            }
        } else {
            return ResponseEntity.badRequest().body(new LoginResponse("Incorrect credentials"));
        }
        return ResponseEntity.ok().build();
    }


    // email hardcoded cuz we verify user through it
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

    public boolean isAuthenticated(HttpServletRequest req){
        Long id = getAuthenticatedId(req);
        return id != null;
    }

    public ResponseEntity<Void> logout(HttpServletResponse res){
        Cookie cookie = new Cookie("authToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        res.addCookie(cookie);
        return ResponseEntity.ok().build();
    }


    // check if old pass matches new one
    public boolean isPasswordNew(String newPassword, String email){
        String oldPassword = userRepo.getStoredPassword(email);

        if(encoder.matches(newPassword, oldPassword)){
            return false;
        }
        return true;
    }



}
