package com.Xhibril.Dash.controller;
import com.Xhibril.Dash.dto.account.*;
import com.Xhibril.Dash.service.AccountService;
import com.Xhibril.Dash.service.AuthService;
import com.Xhibril.Dash.service.UpdateEmailService;
import com.Xhibril.Dash.validation.ValidationGroups;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final AuthService authService;
    private final AccountService accountService;
    private final UpdateEmailService changeEmailService;

    public AccountController(AuthService authService,
                             AccountService accountService,
                             UpdateEmailService changeEmailService){
        this.authService = authService;
        this.accountService = accountService;
        this.changeEmailService = changeEmailService;
    }

    @PostMapping("/delete/account")
    public ResponseEntity<DeleteAccountResponse> deleteAccount(@Valid @RequestBody DeleteAccountRequest request , HttpServletRequest req, HttpServletResponse res){
        Long id = authService.getAuthenticatedId(req);
        return accountService.deleteAccount(id, request.getPassword(), res);
    }

    @PostMapping("/update/password")
    public ResponseEntity<UpdatePasswordResponse> updatePassword(@Valid @RequestBody UpdatePasswordRequest request, HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);
        return accountService.updatePassword(id, request.getOldPassword(), request.getNewPassword(), request.getConfirmPassword());
    }


    @PostMapping("/update-email/request")
    public ResponseEntity<UpdateEmailResponse> initEmailUpdate(@Validated(ValidationGroups.Step1.class)
                                                                   @RequestBody UpdateEmailRequest request, HttpServletRequest req) throws MessagingException {
        Long id = authService.getAuthenticatedId(req);
        return changeEmailService.initEmailChange(id, request.getPendingEmail(), request.getPassword());
    }


    @PostMapping("/update-email/verify")
    public ResponseEntity<UpdateEmailResponse> verifyUpdateEmailRequest(@Validated(ValidationGroups.Step2.class)
                                                                            @RequestBody UpdateEmailRequest request, HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);
        return changeEmailService.verifyChangeEmailRequest(id, request.getCode());
    }

    @PostMapping("/update-email/change")
    public ResponseEntity<UpdateEmailResponse> updateEmail(@Validated(ValidationGroups.Step3.class)
                                                               @RequestBody UpdateEmailRequest request, HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);
        return changeEmailService.changeEmail(id, request.getPendingEmail(), request.getResetToken());
    }
}
