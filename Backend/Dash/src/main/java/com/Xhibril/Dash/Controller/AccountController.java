package com.Xhibril.Dash.Controller;

import com.Xhibril.Dash.Service.AccountService;
import com.Xhibril.Dash.Service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    AuthService authService;

    @Autowired
    AccountService accountService;

    @PostMapping("/delete/account")
    private ResponseEntity<Void> deleteAccount(HttpServletRequest req, HttpServletResponse res){
        Long id = authService.getAuthenticatedId(req);
        return accountService.deleteAccount(id, res);
    }
}
