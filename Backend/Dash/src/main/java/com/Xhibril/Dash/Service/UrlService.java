package com.Xhibril.Dash.Service;
import com.Xhibril.Dash.Repository.UrlRepository;
import com.Xhibril.Dash.model.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

    @Autowired UrlRepository urlRepo;

    public void addUrl(Long userId, String originalUrl, String shortUrl){

        if(!urlRepo.existsByShortUrl(shortUrl)) {
            Url url = new Url();

            url.setUserId(userId);
            url.setOriginalUrl(originalUrl);
            url.setShortUrl(shortUrl);

            urlRepo.save(url);
        }
    }




}
