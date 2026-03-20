package com.Xhibril.Dash.dto.analytics;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;

public class ChartDataResponse {

    private Long visits;
    private String period;
    private Long urlId;
    private String shortUrl;

    @JsonIgnore
    private LocalDate bucket;

    @JsonIgnore
    private Integer hour;


    public ChartDataResponse(){}

    public ChartDataResponse(LocalDate bucket, Long visits) {
        this.bucket = bucket;
        this.visits = visits;

    }

    public ChartDataResponse(Integer hour, Long visits){
        this.hour = hour;
        this.visits = visits;

    }


    public void setUrlId(Long urlId){ this.urlId = urlId; }
    public Long getUrlId(){ return urlId; }

    public void setBucket(LocalDate bucket) { this.bucket = bucket;}
    public LocalDate getBucket(){ return bucket;}

    public void setVisits(Long visits){ this.visits = visits; }

    public Long getVisits(){ return visits; }

    public void setPeriod(String period){ this.period = period; }
    public String getPeriod(){ return period; }

    public void setHour(Integer hour){this.hour = hour;}
    public Integer getHour(){return hour;}


    public void setShortUrl(String shortUrl){ this.shortUrl = shortUrl;}
    public String getShortUrl(){ return shortUrl;}

}
