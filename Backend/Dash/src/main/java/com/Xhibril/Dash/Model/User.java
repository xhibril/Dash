package com.Xhibril.Dash.Model;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(name = "password")
    private String pass;




    public Long getId(){
        return id;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public void setPass(String pass){
        this.pass = pass;
    }

    public String getPass(){
        return pass;
    }
}
