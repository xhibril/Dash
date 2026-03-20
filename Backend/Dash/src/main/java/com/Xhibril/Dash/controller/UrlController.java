package com.Xhibril.Dash.controller;
import com.Xhibril.Dash.dto.url.GenerateUrlRequest;
import com.Xhibril.Dash.dto.url.GenerateUrlResponse;
import com.Xhibril.Dash.service.AuthService;
import com.Xhibril.Dash.service.UrlService;
import com.Xhibril.Dash.model.Url;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UrlController {
    private final UrlService urlService;
    private final AuthService authService;

    public UrlController(UrlService urlService,
                         AuthService authService) {
        this.urlService = urlService;
        this.authService = authService;
    }

    @PostMapping("/urls")
    public ResponseEntity<GenerateUrlResponse> generateUrl(@RequestBody GenerateUrlRequest request, HttpServletRequest req) {
        Long id = authService.getAuthenticatedId(req);
        return urlService.addUrl(id, request.getOriginalUrl(), request.getAlias());
    }

    @GetMapping("/urls")
    public List<Url> getUrls(HttpServletRequest req) {
        Long id = authService.getAuthenticatedId(req);
        return urlService.getUrls(id);
    }


    @DeleteMapping("/urls/{urlId}")
    public void deleteUrl(@PathVariable Long urlId, HttpServletRequest req) {
        Long id = authService.getAuthenticatedId(req);
        urlService.deleteUrl(id, urlId);
    }
}
