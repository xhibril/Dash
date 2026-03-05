package com.Xhibril.Dash.Controller;

import com.Xhibril.Dash.Service.AuthService;
import com.Xhibril.Dash.Model.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/signup")
    private void Signup(@RequestBody User user) throws Exception {
       authService.addUser(user.getEmail(), user.getPass());
    }

    @PostMapping("/login")
    private String Login(@RequestBody User user, HttpServletResponse res){
        return authService.login(user.getEmail(), user.getPass(), res);
    }

    @GetMapping("/email/verify/{token}")
    public boolean verifyUser(@PathVariable String token) {
        System.out.println("HITTTTTTTTTTTTINGGGGGGGGGGG");
        return authService.verifyUser(token);
    }
}
