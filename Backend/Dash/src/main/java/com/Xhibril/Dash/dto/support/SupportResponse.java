package com.Xhibril.Dash.dto.support;

public class SupportResponse {
    private String message;

    public SupportResponse(String message){
        this.message = message;
    }

    public String getMessage(){ return message; }
}
