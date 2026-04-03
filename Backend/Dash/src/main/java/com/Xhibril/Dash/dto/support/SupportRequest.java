package com.Xhibril.Dash.dto.support;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SupportRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;

    @NotBlank(message = "Subject is required")
    @Size(max = 100, message = "Subject must be at most 100 characters")
    private String subject;

    @NotBlank(message = "Message is required")
    @Size(max = 2000, message = "Message must be at most 2000 characters")
    private String message;

    public void setMessage(String message){ this.message = message;}
    public String getMessage(){ return message; }

    public void setEmail(String email){ this.email = email; }
    public String getEmail(){ return email; }

    public void setSubject(String subject){ this.subject = subject;}
    public String getSubject(){return subject;}
}
