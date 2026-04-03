package com.Xhibril.Dash.dto.account;

public class UpdatePasswordResponse {
    private String message;

    public UpdatePasswordResponse(String message){
        this.message = message;
    }

    public String getMessage(){ return message; }
}
