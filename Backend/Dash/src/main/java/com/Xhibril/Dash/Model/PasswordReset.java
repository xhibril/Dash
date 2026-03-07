package com.Xhibril.Dash.Model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "password_reset_requests")
public class PasswordReset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String code;

    @Column(name = "expires_at")
    private Instant expiresAt;

    private Integer attempts;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expires_at")
    private  Instant tokenExpiresAt;


    public void setEmail(String email){ this.email = email; }
    public String getEmail(){ return email; }

    public void setCode(String code) { this.code = code;}
    public String getCode(){ return code;}

    public void setExpiresAt(Instant expiresAt){ this.expiresAt = expiresAt;}
    public Instant getExpiresAt(){ return expiresAt;}

    public void setAttempts (Integer attempts){ this.attempts = attempts;}
    public Integer getAttempts(){ return attempts;}

    public void setResetToken(String resetToken){ this.resetToken = resetToken;}
    public String getResetToken(){ return resetToken;}

    public void setTokenExpiresAt(Instant tokenExpiresAt){ this.tokenExpiresAt = tokenExpiresAt;}
    public Instant getTokenExpiresAt(){ return tokenExpiresAt;}


}
