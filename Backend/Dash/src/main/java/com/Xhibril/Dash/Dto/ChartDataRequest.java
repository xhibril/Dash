package com.Xhibril.Dash.Dto;

public class ChartDataRequest {

    private Long id;
    private String period;

    public void setId(Long id){ this.id = id;}
    public Long getId(){ return id;}

    public void setPeriod(String period) { this.period = period;}
    public String getPeriod(){ return period;}
}
