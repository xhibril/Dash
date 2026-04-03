package com.Xhibril.Dash.controller;
import com.Xhibril.Dash.dto.support.SupportRequest;
import com.Xhibril.Dash.dto.support.SupportResponse;
import com.Xhibril.Dash.service.AuthService;
import com.Xhibril.Dash.service.SupportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<SupportResponse> sendSupportMessage(@Valid  @RequestBody SupportRequest request, HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);
        return supportService.saveSupportMessage(id, request.getEmail(), request.getSubject(), request.getMessage());
    }
}
