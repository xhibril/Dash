package com.Xhibril.Dash.controller;
import com.Xhibril.Dash.dto.auth.PasswordResetRequest;
import com.Xhibril.Dash.dto.auth.PasswordResetResponse;
import com.Xhibril.Dash.service.PasswordResetService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    public PasswordResetController(PasswordResetService passwordResetService){
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/password/reset-request")
    public ResponseEntity<PasswordResetResponse> initResetRequest(@RequestBody PasswordResetRequest resetRequest) throws MessagingException {
        return passwordResetService.initPasswordReset(resetRequest.getEmail());
    }

    @PostMapping("/password/reset/verify")
    public ResponseEntity<PasswordResetResponse> verifyCode(@RequestBody PasswordResetRequest resetRequest){
        return passwordResetService.verifyCode(resetRequest.getEmail(), resetRequest.getCode());
    }


    @PostMapping("/password/reset/reset")
    public ResponseEntity<PasswordResetResponse> resetPassword(@RequestBody PasswordResetRequest resetRequest){
        return passwordResetService.resetPassword(resetRequest.getEmail(), resetRequest.getNewPassword(), resetRequest.getResetToken());
    }
}
