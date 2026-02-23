package com.Xhibril.Dash.Model;
import jakarta.persistence.Entity;
import jakarta.persistence.*;


@Entity
@Table(name = "urls")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "original_url")
    private String originalUrl;

    @Column(name = "short_url")
    private String shortUrl;

    private Integer visits;


    public Long getId(){
        return id;
    }

    public void setUserId(Long userId){ this.userId = userId;}

    public Long getUserId(){
        return userId;
    }

    public void setOriginalUrl(String url){
        this.originalUrl = url;
    }

    public String getOriginalUrl(){
        return originalUrl;
    }

    public void setShortUrl(String url){
        this.shortUrl = url;
    }

    public String getShortUrl(){
        return shortUrl;
    }

    public void setVisits(Integer visits){
        this.visits = visits;
    }

    public Integer getVisits(){
        return visits;
    }





}
