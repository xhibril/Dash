package com.Xhibril.Dash.Controller;

import com.Xhibril.Dash.Service.AuthService;
import com.Xhibril.Dash.Service.UrlService;
import com.Xhibril.Dash.model.Url;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UrlController {

    @Autowired
    UrlService urlService;

    @Autowired
    AuthService authService;

    @PostMapping("/url")
    private ResponseEntity<Void> addUrl(@RequestBody Url url, HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);

        if(id != null){
            urlService.addUrl(id, url.getOriginalUrl(), url.getShortUrl());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
