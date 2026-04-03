package com.Xhibril.Dash.dto.url;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class GenerateUrlRequest {
    private Long id;

    @NotBlank (message = "URL is required")
    @Pattern(
            regexp = "^$|^[^\\s]+\\.[^\\s]+$",
            message = "Invalid URL"
    )
    private String originalUrl;
    private String shortUrl;

    @Size(min = 3, max = 10, message = "Alias must be 3–10 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Alias must contain only letters and numbers")
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
