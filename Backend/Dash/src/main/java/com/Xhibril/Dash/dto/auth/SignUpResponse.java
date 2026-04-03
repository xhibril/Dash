package com.Xhibril.Dash.dto.auth;

public class SignUpResponse {
    private String message;

    public SignUpResponse(String message){
        this.message = message;
    }

    public String getMessage(){ return message; }
}
