package com.Xhibril.Dash.dto.auth;

public class PasswordResetRequest {

    private String email;
    private String code;
    private String newPassword;
    private String resetToken;

    public void setEmail(String email){ this.email = email; }
    public String getEmail(){ return email; }

    public void setCode(String code) { this.code = code;}
    public String getCode(){ return code;}

    public void setNewPassword(String newPassword){ this.newPassword = newPassword;}
    public String getNewPassword(){ return newPassword;}


    public void setResetToken(String resetToken){ this.resetToken = resetToken;}
    public String getResetToken(){ return resetToken;}
}
