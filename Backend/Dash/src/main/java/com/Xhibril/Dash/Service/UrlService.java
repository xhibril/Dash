package com.Xhibril.Dash.Service;
import com.Xhibril.Dash.Repository.UrlRepository;
import com.Xhibril.Dash.Model.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {

    private static final Random random = new Random();
    private static final String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Autowired UrlRepository urlRepo;

    public boolean addUrl(Long userId, String originalUrl, String shortUrl){

        if(!urlRepo.existsByShortUrl(shortUrl)) {
            Url url = new Url();

            url.setUserId(userId);
            url.setOriginalUrl(originalUrl);
            url.setShortUrl(shortUrl);

            urlRepo.save(url);
            return true;
        }
        return false;
    }


    public String redirect(String url){
         Url u = urlRepo.findByShortUrl(url);
         if (u != null){
             return u.getOriginalUrl();
         } else {
             return null;
         }
    }


    public String generateUrl(Long id, String originalUrl){

        int length = 5;
        StringBuilder shortUrl;

        while(true){

            shortUrl = new StringBuilder();
            for(int i = 0; i < length; i++) {
                int index = random.nextInt(letters.length());
                shortUrl.append(letters.charAt(index));
            }

            if(addUrl(id, originalUrl, shortUrl.toString())) break;

        }

        return shortUrl.toString();
    }



    @Transactional
    public void incrementVist(String url){
        Url u = urlRepo.findByShortUrl(url);
        Integer visits = 0;
        if(u.getVisits() == null){
             visits = 1;
        } else {
             visits = u.getVisits() + 1;
        }
        urlRepo.updateVisits(visits, u.getShortUrl());
    }


    public List<Url> getUrls(Long id){
        List<Url> urlList = urlRepo.findByUserId(id);



        for(Url u : urlList){
            System.out.println(u.getShortUrl() + "/n");
            System.out.println(u.getOriginalUrl() + "/n");
        }

        return urlList;
    }






}
