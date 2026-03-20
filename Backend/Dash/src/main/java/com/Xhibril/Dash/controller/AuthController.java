package com.Xhibril.Dash.controller;
import com.Xhibril.Dash.dto.auth.LoginRequest;
import com.Xhibril.Dash.service.AuthService;
import com.Xhibril.Dash.model.User;
import com.Xhibril.Dash.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

 private final AuthService authService;
 private final EmailService emailService;

 public AuthController(AuthService authService,
                       EmailService emailService){

     this.authService = authService;
     this.emailService = emailService;
 }

    @PostMapping("/signup")
    public ResponseEntity<String> Signup(@RequestBody User user){
       return authService.registerUser(user.getEmail(), user.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<String> Login(@RequestBody LoginRequest loginRequest, HttpServletResponse res) throws Exception {
        return authService.login(loginRequest.getEmail(), loginRequest.getPassword(), loginRequest.getRememberMe(), res);
    }

    @GetMapping("/auth/status")
    public boolean isAuthenticated(HttpServletRequest req){
        return authService.isAuthenticated(req);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse res) {
        return authService.logout(res);
    }


    @PostMapping("/email/resend")
    public void resendVerificationToken(@RequestBody User user) throws Exception {
        emailService.sendVerificationEmail(user.getEmail());
    }
 }
