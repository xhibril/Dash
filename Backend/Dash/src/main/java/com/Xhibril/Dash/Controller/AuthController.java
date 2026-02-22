package com.Xhibril.Dash.Controller;

import com.Xhibril.Dash.Service.AuthService;
import com.Xhibril.Dash.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("api/signup")
    private void Signup(@RequestBody User user) {
        authService.addUser(user.getEmail(), user.getPass());
    }

    @PostMapping("api/login")
    private String Login(@RequestBody User user, HttpServletResponse res){
        return authService.login(user.getEmail(), user.getPass(), res);
    }

}
