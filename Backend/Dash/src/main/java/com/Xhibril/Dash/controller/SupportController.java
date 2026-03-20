package com.Xhibril.Dash.controller;
import com.Xhibril.Dash.dto.support.SupportRequest;
import com.Xhibril.Dash.service.AuthService;
import com.Xhibril.Dash.service.SupportService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SupportController {

    private final AuthService authService;
    private final SupportService supportService;

    public SupportController(AuthService authService,
                             SupportService supportService){
        this.authService = authService;
        this.supportService = supportService;
    }

    @PostMapping("/support/tickets")
    public void sendSupportMessage(@RequestBody SupportRequest supportRequest, HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);
        supportService.saveSupportMessage(id, supportRequest);
    }
}
