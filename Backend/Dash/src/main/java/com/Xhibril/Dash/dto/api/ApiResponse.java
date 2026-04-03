package com.Xhibril.Dash.dto.api;

public class ApiResponse {
    private String message;

    public ApiResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
}
