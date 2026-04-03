package com.Xhibril.Dash.dto.account;

public class DeleteAccountResponse {
    private String message;

    public DeleteAccountResponse(String message){
        this.message = message;
    }
    public String getMessage(){ return message; }
}
