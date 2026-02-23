package com.Xhibril.Dash.Controller;

import com.Xhibril.Dash.Service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class Redirecter {


    @Autowired
    private UrlService urlService = new UrlService();

    @GetMapping("/{alias}")
    public String redirect(@PathVariable String alias){

        String originalUrl = urlService.redirect(alias);

        if (originalUrl != null){
            urlService.incrementVist(alias);
            return "redirect:" + originalUrl;
        }
        return "redirect:http://localhost:5173/dashboard";
    }

}
