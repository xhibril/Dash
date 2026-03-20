package com.Xhibril.Dash.dto.support;

public class SupportRequest {

    private String email;
    private String subject;
    private String message;

    public void setMessage(String message){ this.message = message;}
    public String getMessage(){ return message; }

    public void setEmail(String email){ this.email = email; }
    public String getEmail(){ return email; }

    public void setSubject(String subject){ this.subject = subject;}
    public String getSubject(){return subject;}
}
