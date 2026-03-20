package com.Xhibril.Dash.dto.account;

public class UpdateEmailResponse {
    private String message;
    private String resetToken;
    private String oldEmail;


    public void setResetToken(String resetToken){ this.resetToken = resetToken;}
    public String getResetToken(){ return resetToken;}

    public String getMessage(){ return message;}
    public void setMessage(String message){this.message = message;}


    public String getOldEmail(){ return oldEmail;}
    public void setOldEmail(String oldEmail){ this.oldEmail = oldEmail;}

    public UpdateEmailResponse(){}
    public UpdateEmailResponse(String message){ this.message = message;}
}
