package com.Xhibril.Dash.Controller;

import com.Xhibril.Dash.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class EmailVerificationController {

    @Autowired
    AuthService authService;


    @GetMapping("/email/verify/{token}")
    public String verifyUser(@PathVariable String token) {
        if( authService.verifyUser(token)){
            System.out.println("WORKED");
            return "redirect:http://localhost:5173/login?verified=true";
        } else {
            System.out.println("DID NOT WORK WORKED");
            return "redirect:http://localhost:5173/login?verified=false";
        }
    }
}
