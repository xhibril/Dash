package com.Xhibril.Dash.Service;
import com.Xhibril.Dash.Model.UrlStat;
import com.Xhibril.Dash.Repository.UrlRepository;
import com.Xhibril.Dash.Model.Url;
import com.Xhibril.Dash.Repository.UrlStatRepository;
import jdk.jfr.Percentage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public boolean addUrl(Long userId, String originalUrl, String shortUrl){

        if(!urlRepo.existsByShortUrl(shortUrl)) {

            Url url = new Url();


            LocalDate createdDate = LocalDate.now();

            url.setUserId(userId);
            url.setOriginalUrl(originalUrl);
            url.setShortUrl(shortUrl);
            url.setCreatedDate(createdDate);
            url.setVisits(0);

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
    public void incrementVist(String shortUrl){
        LocalDateTime now = LocalDateTime.now();

        Url url = urlRepo.findByShortUrl(shortUrl);
        Long urlId = url.getId();

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
            urlStatRepo.updateVisits(visits, urlId);
        } else {
            urlStat = new UrlStat();
            urlStat.setUrlId(urlId);
            urlStat.setBucket(bucket);
            urlStat.setVisits(1);

            urlStatRepo.save(urlStat);
        }

        // update visits in main url table

        if(url.getVisits() == null){
            visits = 1;
        } else {
            visits = url.getVisits() + 1;
        }
        urlRepo.updateVisits(visits, shortUrl);
    }


    public List<Url> getUrls(Long id){
        List<Url> urlList = urlRepo.findByUserId(id);



        for(Url u : urlList){
            System.out.println(u.getShortUrl() + "/n");
            System.out.println(u.getOriginalUrl() + "/n");
        }

        return urlList;
    }



    public Integer getVisits(Long id){
        LocalDate date = LocalDate.now();

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        return visitsHelper(start, end, id);
    }


    public Integer getTrend(Long id){
        LocalDate date = LocalDate.now();
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        Integer todayVisits = visitsHelper(start,end, id);
        Integer yesterdayVisits = visitsHelper(start.minusDays(1),end.minusDays(1), id);

       Integer diff = todayVisits - yesterdayVisits;
        System.out.println("today: " + todayVisits);
        System.out.println("yesterday: " + yesterdayVisits);
        System.out.println("diff: " + diff);

       if(yesterdayVisits != 0){
           Integer percentage = (diff / yesterdayVisits) * 100;

           System.out.println("PERCENTAGE: " + percentage);

           if(diff >= 0){
               return Math.round(percentage);
           } else {
               return -Math.round(percentage);
           }

       } else {
           return 0;
       }
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








}
