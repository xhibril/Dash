package com.Xhibril.Dash.dto.url;

public class GenerateUrlRequest {
    private Long id;
    private String originalUrl;
    private String shortUrl;
    private String alias;



    public void setId(Long id) { this.id = id;}
    public Long getId(){ return id;}

    public void setOriginalUrl(String originalUrl){ this.originalUrl = originalUrl;}
    public String getOriginalUrl(){ return originalUrl;}

    public void setShortUrl(String shortUrl){ this.shortUrl = shortUrl;}
    public String getShortUrl(){ return shortUrl;}

    public void setAlias(String alias) { this.alias = alias;}
    public String getAlias(){ return alias;}
}
