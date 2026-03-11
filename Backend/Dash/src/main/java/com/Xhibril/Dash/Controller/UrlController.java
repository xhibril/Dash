package com.Xhibril.Dash.Controller;

import com.Xhibril.Dash.Dto.GenerateUrlRequest;
import com.Xhibril.Dash.Dto.GenerateUrlResponse;
import com.Xhibril.Dash.Service.AuthService;
import com.Xhibril.Dash.Service.UrlService;
import com.Xhibril.Dash.Model.Url;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private ResponseEntity<GenerateUrlResponse> generateUrl(@RequestBody GenerateUrlRequest request, HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);

        return urlService.addUrl(id, request.getOriginalUrl(), request.getAlias());
    }





    @GetMapping("/urls")
    private List<Url> getUrls(HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);
            return urlService.getUrls(id);
    }


    @GetMapping("/popular")
    private Url getMostPopular(HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);
            return urlService.mostPopular(id);
    }



    @GetMapping("/visits")
    private Integer getVisits(HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);
          return urlService.getVisits(id);

    }


    @GetMapping("/trend")
    private Integer getTrend(HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);
            return urlService.getTrend(id);

    }



    @PostMapping("/delete/url")
    private void deleteUrl(@RequestParam Long urlId, HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);
            urlService.deleteUrl(id, urlId);
    }




}
