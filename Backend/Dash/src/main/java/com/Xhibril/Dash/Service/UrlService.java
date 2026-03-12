package com.Xhibril.Dash.Service;
import com.Xhibril.Dash.Dto.GenerateUrlResponse;
import com.Xhibril.Dash.Model.UrlStat;
import com.Xhibril.Dash.Repository.UrlRepository;
import com.Xhibril.Dash.Model.Url;
import com.Xhibril.Dash.Repository.UrlStatRepository;
import jdk.jfr.Percentage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class UrlService {

    private static final Random random = new Random();
    private static final String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Autowired UrlRepository urlRepo;
    @Autowired
    UrlStatRepository urlStatRepo;

    public ResponseEntity<GenerateUrlResponse> addUrl(Long userId, String originalUrl, String alias){

        originalUrl = normalizeUrl(originalUrl);
        // generate url if user didnt input alias
        if(alias.isEmpty()){
            alias = generateShortUrl(userId, originalUrl);
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


    private String generateShortUrl(Long id, String originalUrl){

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
    public void incrementVist(String shortUrl){
        LocalDateTime now = LocalDateTime.now();

        Optional<Url> url = urlRepo.findByShortUrl(shortUrl);

        if(url.isEmpty()) return;
        Url u = url.get();

        Long urlId = u.getId();

        LocalDateTime bucket = now
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .withHour((now.getHour() / 2) * 2);


        Optional<UrlStat> stat = urlStatRepo.findByUrlIdAndBucket(urlId, bucket);

        UrlStat urlStat;
        Integer visits = 0;

        if(stat.isPresent()){
            urlStat = stat.get();

            visits = urlStat.getVisits() + 1;
            urlStatRepo.updateVisits(visits, urlId, bucket);
        } else {
            urlStat = new UrlStat();
            urlStat.setUrlId(urlId);
            urlStat.setBucket(bucket);
            urlStat.setVisits(1);
            urlStat.setShortUrl(shortUrl);

            urlStatRepo.save(urlStat);
        }

        // update visits in main url table

        if(u.getVisits() == null){
            visits = 1;
        } else {
            visits = u.getVisits() + 1;
        }
        urlRepo.updateVisits(visits, shortUrl);
    }


    public List<Url> getUrls(Long id){
        List<Url> urlList = urlRepo.findByUserId(id);
        return urlList;
    }



    public Integer getVisits(Long id){
        LocalDate date = LocalDate.now();

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        return visitsHelper(start, end, id);
    }


    public Integer getTrend(Long id) {
        LocalDate date = LocalDate.now();
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        Integer todayVisits = visitsHelper(start, end, id);
        Integer yesterdayVisits = visitsHelper(start.minusDays(1), end.minusDays(1), id);

        Integer diff = todayVisits - yesterdayVisits;
        System.out.println("today: " + todayVisits);
        System.out.println("yesterday: " + yesterdayVisits);
        System.out.println("diff: " + diff);

        Float percentage;

        if (yesterdayVisits != 0) {
             percentage = (float) diff / yesterdayVisits * 100;
        } else {
            percentage = (float) diff / 1 * 100;
        }

       return Math.round(percentage);
    }



    public Integer visitsHelper(LocalDateTime start,LocalDateTime end, Long id){

        List<Url> urls = urlRepo.findByUserId(id);
        Integer visits = 0;

        for(Url u : urls){
            List<UrlStat> stats = urlStatRepo.findByBucketBetweenAndUrlId(start,end, u.getId());


              visits += stats.stream()
                    .mapToInt(UrlStat::getVisits)
                    .sum();


              System.out.println("VISITS: "+ u.getVisits());
        }

        System.out.println("FINAL SUM: "+ visits);
        return visits;
    }



    public Url mostPopular(Long id){
        return urlRepo.findTopByUserIdOrderByVisitsDesc(id);
    }



    @Transactional
    public void deleteUrl(Long id, Long urlId){
        System.out.println("deleting" + urlId + " " + id);
        if(urlRepo.existsByIdAndUserId(urlId, id)){
            urlStatRepo.deleteAllByUrlId(urlId);
            urlRepo.deleteUrlById(urlId);

        }
    }

}
