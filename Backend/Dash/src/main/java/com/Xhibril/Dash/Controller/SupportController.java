package com.Xhibril.Dash.Controller;

import com.Xhibril.Dash.Dto.SupportRequest;
import com.Xhibril.Dash.Model.Support;
import com.Xhibril.Dash.Service.AuthService;
import com.Xhibril.Dash.Service.SupportService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SupportController {

    @Autowired
    AuthService authService;
    @Autowired
    private SupportService supportService;

    @PostMapping("/support")
    public void sendSupportMessage(@RequestBody SupportRequest supportRequest){
        supportService.saveSupportMessage(supportRequest);
    }
}
