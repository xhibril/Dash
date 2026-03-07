package com.Xhibril.Dash.Controller;

import com.Xhibril.Dash.Dto.PasswordResetRequest;
import com.Xhibril.Dash.Dto.PasswordResetResponse;
import com.Xhibril.Dash.Model.PasswordReset;
import com.Xhibril.Dash.Repository.PasswordResetRepository;
import com.Xhibril.Dash.Service.PasswordResetService;
import com.Xhibril.Dash.Service.UrlService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PasswordResetController {

    @Autowired
    PasswordResetService passwordResetService;

    @PostMapping("/password/reset")
    public ResponseEntity<PasswordResetResponse> initResetRequest(@RequestBody PasswordResetRequest resetRequest) throws MessagingException {
        return passwordResetService.initPasswordReset(resetRequest.getEmail());
    }


    @PostMapping("/password/reset/verify")
    public ResponseEntity<PasswordResetResponse> verifyCode(@RequestBody PasswordResetRequest resetRequest){
        return passwordResetService.verifyCode(resetRequest.getEmail(), resetRequest.getCode());
    }


    @PostMapping("/password/reset/new")
    public ResponseEntity<PasswordResetResponse> resetPassword(@RequestBody PasswordResetRequest resetRequest){
        return passwordResetService.resetPassword(resetRequest.getEmail(), resetRequest.getNewPassword(), resetRequest.getResetToken());
    }

}
