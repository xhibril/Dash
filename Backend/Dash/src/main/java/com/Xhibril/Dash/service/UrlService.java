package com.Xhibril.Dash.service;
import com.Xhibril.Dash.dto.url.GenerateUrlResponse;
import com.Xhibril.Dash.model.UrlStat;
import com.Xhibril.Dash.repository.UrlRepository;
import com.Xhibril.Dash.model.Url;
import com.Xhibril.Dash.repository.UrlStatRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UrlService {
    private static final Random random = new Random();
    private static final String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final UrlRepository urlRepo;
    private final UrlStatRepository urlStatRepo;

    public UrlService(UrlRepository urlRepo,
                      UrlStatRepository urlStatRepo){
        this.urlRepo = urlRepo;
        this.urlStatRepo = urlStatRepo;
    }


    public ResponseEntity<GenerateUrlResponse> addUrl(Long userId, String originalUrl, String alias){
        originalUrl = normalizeUrl(originalUrl);
        // generate url if user didnt input alias
        if(alias == null || alias.trim().isEmpty()){
            alias = generateShortUrl();
        }

        if(!urlRepo.existsByShortUrl(alias)) {

            Url url = new Url();
            GenerateUrlResponse urlResponse = new GenerateUrlResponse();
            LocalDate createdDate = LocalDate.now();

            url.setUserId(userId);
            url.setOriginalUrl(originalUrl);
            url.setShortUrl(alias);
            url.setCreatedDate(createdDate);
            url.setVisits(0);


            // save so we can extract id
            Url saved = urlRepo.save(url);

            urlResponse.setId(saved.getId());
            urlResponse.setShortUrl(saved.getShortUrl());
            urlResponse.setOriginalUrl(originalUrl);
            urlResponse.setVisits(0);
            urlResponse.setMessage("URL Successfully created");

            return ResponseEntity.ok().body(urlResponse);
        }
        return ResponseEntity.badRequest().body(new GenerateUrlResponse("URL already exists"));
    }


    public String redirect(String shortUrl){
         Optional<Url> url = urlRepo.findByShortUrl(shortUrl);

       if(url.isPresent()){
           Url u = url.get();
           return u.getOriginalUrl();
       }
       return null;
    }


    private String generateShortUrl(){
        int length = 5;
        StringBuilder shortUrl;

        while(true){

            shortUrl = new StringBuilder();
            for(int i = 0; i < length; i++) {
                int index = random.nextInt(letters.length());
                shortUrl.append(letters.charAt(index));
            }

            // check to make sure url doesnt exist
            Optional<Url> url = urlRepo.findByShortUrl(shortUrl.toString());

            if(url.isEmpty()){
                break;
            }
        }
        return shortUrl.toString();
    }


    private String normalizeUrl(String originalUrl){
        originalUrl = originalUrl.trim();

        try {
            URI uri = new URI(originalUrl);

            if(uri.getScheme() == null){
                originalUrl = "http://" + originalUrl;
                uri = new URI(originalUrl);
            }

            return uri.toString();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL");
        }
    }


    @Transactional
    public void incrementVisit(String shortUrl){
        LocalDateTime now = LocalDateTime.now();

        Optional<Url> urlOpt = urlRepo.findByShortUrl(shortUrl);

        if(urlOpt.isEmpty()) return;

        Url url = urlOpt.get();
        Long urlId = url.getId();

        LocalDateTime bucket = now
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .withHour((now.getHour() / 2) * 2);


        // check if bucket exists
        Optional<UrlStat> urlStatOpt = urlStatRepo.findByUrlIdAndBucket(urlId, bucket);

        UrlStat urlStat;
        Integer visits;

        if(urlStatOpt.isPresent()){
            urlStat = urlStatOpt.get();

            visits = urlStat.getVisits() + 1;
            urlStatRepo.updateVisits(visits, urlId, bucket);
        } else {
            // make a new bucket if it doesnt exist
            urlStat = new UrlStat();
            urlStat.setUrlId(urlId);
            urlStat.setBucket(bucket);
            urlStat.setVisits(1);
            urlStat.setShortUrl(shortUrl);

            urlStatRepo.save(urlStat);
        }

        // update visits total in main url table
        visits = url.getVisits() + 1;
        urlRepo.updateVisits(visits, shortUrl);
    }


    public List<Url> getUrls(Long id){
        List<Url> urlList = urlRepo.findByUserId(id);
        return urlList;
    }


    @Transactional
    public void deleteUrl(Long id, Long urlId){
        if(urlRepo.existsByIdAndUserId(urlId, id)){
            urlStatRepo.deleteAllByUrlId(urlId);
            urlRepo.deleteUrlById(urlId);

        }
    }
}
