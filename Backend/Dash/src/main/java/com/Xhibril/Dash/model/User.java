package com.Xhibril.Dash.model;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private Boolean verified;

    public Long getId(){
        return id;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }

    public void setPassword(String pass){
        this.password = pass;
    }
    public String getPassword(){
        return password;
    }

    public void setVerified(Boolean verified){ this.verified = verified;}
    public Boolean getVerified(){ return verified;}
}
