package com.Xhibril.Dash.Dto;

import jakarta.persistence.criteria.CriteriaBuilder;

public class PasswordResetResponse {

    private String message;
    private String resetToken;
    private Integer attempts;


    public PasswordResetResponse(){}

    public PasswordResetResponse(String message){
        this.message = message;
    }


    public void setMessage(String message){ this.message = message;}
    public String getMessage(){ return message;}

    public void setResetToken(String resetToken){ this.resetToken = resetToken;}
    public String getResetToken(){ return resetToken;}

    public void setAttempts(Integer attempts){ this.attempts = attempts;}
    public Integer getAttempts(){ return attempts;}
}
