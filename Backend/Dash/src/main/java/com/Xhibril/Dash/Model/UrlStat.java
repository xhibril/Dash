package com.Xhibril.Dash.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "url_stat")
public class UrlStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(name = "url_id")
    private Long urlId;

    private Integer visits;

    private LocalDateTime bucket;

    public void setVisits(Integer visits){
        this.visits = visits;
    }
    public Integer getVisits(){ return visits; }

    public void setBucket(LocalDateTime bucket){ this.bucket = bucket;}
    public LocalDateTime getBucket(){ return bucket; }


    public void setUrlId(Long urlId){
        this.urlId = urlId;
    }

    public Long getUrlId(){ return urlId; }



}
