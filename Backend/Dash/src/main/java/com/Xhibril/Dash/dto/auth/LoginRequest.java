package com.Xhibril.Dash.dto.auth;

public class LoginRequest {

    private String email;
    private String password;
    private boolean rememberMe;


    public void setEmail(String email){ this.email = email;}
    public String getEmail(){ return email;}

    public void setPassword(String password){ this.password = password;}
    public String getPassword(){ return password;}

    public void setRememberMe(boolean rememberMe){ this.rememberMe = rememberMe;}
    public boolean getRememberMe(){ return rememberMe;}

}
