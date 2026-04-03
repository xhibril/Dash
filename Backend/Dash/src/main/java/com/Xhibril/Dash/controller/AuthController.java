package com.Xhibril.Dash.controller;
import com.Xhibril.Dash.dto.auth.LoginRequest;
import com.Xhibril.Dash.dto.auth.LoginResponse;
import com.Xhibril.Dash.dto.auth.SignUpRequest;
import com.Xhibril.Dash.dto.auth.SignUpResponse;
import com.Xhibril.Dash.service.AuthService;
import com.Xhibril.Dash.model.User;
import com.Xhibril.Dash.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
    public ResponseEntity<SignUpResponse> Signup(@Valid  @RequestBody SignUpRequest signUpRequest){
       return authService.registerUser(signUpRequest.getEmail(), signUpRequest.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> Login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse res) throws Exception {
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
    public void resendVerificationToken(@RequestBody User user){
        emailService.sendVerificationEmail(user.getEmail());
    }
 }
