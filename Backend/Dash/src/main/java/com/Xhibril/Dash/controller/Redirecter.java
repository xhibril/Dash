package com.Xhibril.Dash.controller;
import com.Xhibril.Dash.service.UrlService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class Redirecter {
    private final UrlService urlService;

    public Redirecter(UrlService urlService){
        this.urlService = urlService;
    }

    @GetMapping("/{shortUrl}")
    public String redirect(@PathVariable String shortUrl){
        String originalUrl = urlService.redirect(shortUrl);

        if (originalUrl != null){
            urlService.incrementVisit(shortUrl);
            return "redirect:" + originalUrl;
        }
        return "redirect:https://dash.xhibril.dev/dashboard";
    }
}
