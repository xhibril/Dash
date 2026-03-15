package com.Xhibril.Dash.Model;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "support")
public class Support {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(name = "user_id")
    private Long userId;

    private String subject;

    @Column(name = "created_at")

    private LocalDate createdAt;

    private String message;

    private String status;


    public void setEmail(String email){ this.email = email; }
    public String getEmail(){ return email; }

    public void setSubject(String subject){ this.subject = subject;}
    public String getSubject(){return subject;}

    public void setCreatedAt(LocalDate createdAt){ this.createdAt = createdAt;}
    public LocalDate getCreatedAt(){ return createdAt; }

    public void setMessage(String message){ this.message = message;}
    public String getMessage(){ return message; }

    public void setStatus(String status){ this.status = status;}
    public String getStatus(){ return status;}

    public void setUserId(Long userId){ this.userId = userId;}
    public Long getUserId(){ return userId; }
}
