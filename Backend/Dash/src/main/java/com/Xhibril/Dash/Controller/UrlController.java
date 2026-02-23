package com.Xhibril.Dash.Controller;

import com.Xhibril.Dash.Service.AuthService;
import com.Xhibril.Dash.Service.UrlService;
import com.Xhibril.Dash.Model.Url;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UrlController {

    @Autowired
    UrlService urlService;

    @Autowired
    AuthService authService;


    @PostMapping("/generate/url")
    private String generateUrl(@RequestBody Url url, HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);

        if(id != null){
            return urlService.generateUrl(id, url.getOriginalUrl());
        } else {
            return "Could not generate a url at the moment";
        }
    }


    @PostMapping("/url")
    private ResponseEntity<Void> addUrl(@RequestBody Url url, HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);

        if(id != null){
            urlService.addUrl(id, url.getOriginalUrl(), url.getShortUrl());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }



    @GetMapping("/urls")
    private List<Url> getUrls(HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);

        if(id != null){
            return urlService.getUrls(id);
        }

        return null;
    }



}
