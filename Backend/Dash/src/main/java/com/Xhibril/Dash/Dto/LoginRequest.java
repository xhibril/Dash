package com.Xhibril.Dash.Dto;

public class LoginRequest {

    private String email;
    private String pass;
    private boolean rememberMe;


    public void setEmail(String email){ this.email = email;}
    public String getEmail(){ return email;}

    public void setPass(String pass){ this.pass = pass;}
    public String getPass(){ return pass;}

    public void setRememberMe(boolean rememberMe){ this.rememberMe = rememberMe;}
    public boolean getRememberMe(){ return rememberMe;}

}
