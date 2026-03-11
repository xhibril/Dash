package com.Xhibril.Dash.Controller;

import com.Xhibril.Dash.Service.AuthService;
import com.Xhibril.Dash.Service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class Redirecter {


    @Autowired
    private UrlService urlService = new UrlService();

    @Autowired
    AuthService authService;

    @GetMapping("/{shortUrl}")
    public String redirect(@PathVariable String shortUrl){

        String originalUrl = urlService.redirect(shortUrl);

        if (originalUrl != null){
            urlService.incrementVist(shortUrl);
            return "redirect:" + originalUrl;
        }
        return "redirect:http://localhost:5173/dashboard";
    }


}
