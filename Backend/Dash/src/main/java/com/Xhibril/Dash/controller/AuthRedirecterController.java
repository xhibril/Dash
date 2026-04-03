package com.Xhibril.Dash.controller;
import com.Xhibril.Dash.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class AuthRedirecterController {

    private final AuthService authService;

    public AuthRedirecterController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/email/verify/{token}")
    public String verifyUser(@PathVariable String token) {
        if (authService.verifyUser(token)) {
            return "redirect:https://api.xhibril.dev/login?verified=true";
        } else {
            return "redirect:https://api.xhibril.dev/login?verified=false";
        }
    }
}