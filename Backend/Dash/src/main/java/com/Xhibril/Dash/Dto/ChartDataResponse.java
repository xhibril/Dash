package com.Xhibril.Dash.Dto;


import java.time.LocalDate;

public class ChartDataResponse {

    private Long visits;
    private String period;


    private Long urlId;


    private LocalDate bucket;

    public ChartDataResponse(LocalDate bucket, Long visits) {
        this.bucket = bucket;
        this.visits = visits;
    }

    public ChartDataResponse() {

    }

    public void setUrlId(Long urlId){ this.urlId = urlId; }
    public Long getUrlId(){ return urlId; }

    public void setBucket(LocalDate bucket) { this.bucket = bucket;}
    public LocalDate getBucket(){ return bucket;}



    public void setVisits(Long visits){ this.visits = visits; }

    public Long getVisits(){ return visits; }

    public void setPeriod(String period){ this.period = period; }
    public String getPeriod(){ return period; }
}
