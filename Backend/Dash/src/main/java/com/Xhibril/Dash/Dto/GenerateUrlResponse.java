package com.Xhibril.Dash.Dto;

public class GenerateUrlResponse {

    private Long id;
    private String shortUrl;
    private String originalUrl;
    private String message;


    public GenerateUrlResponse(){}

    public GenerateUrlResponse(String message){
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortUrl() {
        return this.shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
