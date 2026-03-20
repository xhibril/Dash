package com.Xhibril.Dash.model;
import jakarta.persistence.*;
import java.time.Instant;


@Entity
@Table(name = "change_email_requests")
public class UpdateEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "pending_email")
    private String pendingEmail;


    @Column(name = "verification_code")
    private String verificationCode;


    @Column(name = "expires_at")
    private Instant expiresAt;


    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expires_at")
    private Instant resetTokenExpiration;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPendingEmail() {
        return pendingEmail;
    }

    public void setPendingEmail(String pendingEmail) {
        this.pendingEmail = pendingEmail;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setResetToken(String resetToken){
        this.resetToken = resetToken;
    }

    public String getResetToken(){ return resetToken;}

    public void setResetTokenExpiration(Instant resetTokenExpiration){ this.resetTokenExpiration = resetTokenExpiration;}
    public Instant getResetTokenExpiration(){ return resetTokenExpiration;}

}
