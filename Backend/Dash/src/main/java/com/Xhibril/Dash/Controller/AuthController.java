package com.Xhibril.Dash.Controller;

import com.Xhibril.Dash.Dto.LoginRequest;
import com.Xhibril.Dash.Service.AuthService;
import com.Xhibril.Dash.Model.User;
import com.Xhibril.Dash.Service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    AuthService authService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/signup")
    private ResponseEntity<String> Signup(@RequestBody User user) throws Exception {
       return authService.addUser(user.getEmail(), user.getPass());
    }

    @PostMapping("/login")
    private ResponseEntity<String> Login(@RequestBody LoginRequest loginRequest, HttpServletResponse res) throws Exception {
        return authService.login(loginRequest.getEmail(), loginRequest.getPass(), loginRequest.getRememberMe(), res);
    }


    @PostMapping("/email/resend")
    public void resendVerificationToken(@RequestBody User user) throws Exception {
        emailService.sendVerificationEmail(user.getEmail());
    }

    @GetMapping("/auth/status")
    public boolean isAuthenticated(HttpServletRequest req){
        return authService.isAuthenticated(req);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse res){
        return authService.logout(res);
    }
}
