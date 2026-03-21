package com.Xhibril.Dash.service;
import com.Xhibril.Dash.dto.analytics.ChartDataResponse;
import com.Xhibril.Dash.model.Url;
import com.Xhibril.Dash.model.UrlStat;
import com.Xhibril.Dash.repository.UrlRepository;
import com.Xhibril.Dash.repository.UrlStatRepository;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {
    private final UrlStatRepository urlStatRepo;
    private final UrlRepository urlRepo;

    public AnalyticsService(UrlStatRepository urlStatRepo,
                            UrlRepository urlRepo){
        this.urlStatRepo = urlStatRepo;
        this.urlRepo = urlRepo;
    }

    public enum Period {
        DAILY,
        WEEKLY,
        MONTHLY
    }

    public List<ChartDataResponse> chartData(Period period, Long urlId) {
        LocalDate date = LocalDate.now();
        LocalDateTime start, end;

        switch (period) {
            case DAILY:
                start = date.atStartOfDay();
                end = date.atTime(LocalTime.MAX);
                return transformData(urlStatRepo.getDaily(urlId, start, end), Period.DAILY);


            case WEEKLY:
                date = date.with(DayOfWeek.MONDAY);
                start = date.atStartOfDay();
                end = date.plusDays(6).atTime(LocalTime.MAX);
                return transformData(urlStatRepo.getStats(urlId, start, end), Period.WEEKLY);


            case MONTHLY:
                date = date.withDayOfMonth(1);
                LocalDate lastDay = date.withDayOfMonth(date.lengthOfMonth());

                start = date.atStartOfDay();
                end = lastDay.atTime(LocalTime.MAX);
                return transformData(urlStatRepo.getStats(urlId, start, end), Period.MONTHLY);

        }
        return new ArrayList<>();
    }


    private List<ChartDataResponse> transformData(List<ChartDataResponse> data, Period period) {
        List<ChartDataResponse> transformedData = new ArrayList<>();

        switch (period) {
            case DAILY:

                for (ChartDataResponse cdr : data) {
                    String formattedHour = cdr.getHour() + ":00";
                    cdr.setPeriod(formattedHour);
                    transformedData.add(cdr);
                }
                return transformedData;

            case WEEKLY:
                for (ChartDataResponse cdr : data) {
                    LocalDate date = cdr.getBucket();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");
                    String dateStr = date.format(formatter);

                    cdr.setPeriod(dateStr);
                    transformedData.add(cdr);
                }
                break;

            case MONTHLY:
                Long sum = 0L;
                int bucketIndex = 0, prev = 0;

                for (int i = 0; i < data.size(); ) {
                    ChartDataResponse cdr = data.get(i);
                    int currentDate = cdr.getBucket().getDayOfMonth();
                    bucketIndex = (currentDate - 1) / 7;
                    if (bucketIndex >= 4) bucketIndex = 3;

                    // add visits if inside of same week period
                    if (bucketIndex == prev) {
                        sum += cdr.getVisits();
                        i++;

                    } else {
                        // store those visits in the corresponding week
                        ChartDataResponse finalData = getFinalData(prev, sum);
                        if(finalData != null){
                            transformedData.add(finalData);
                        }
                        sum = 0L;
                    }
                    prev = bucketIndex;
                }

                ChartDataResponse finalData = getFinalData(bucketIndex, sum);
                if(finalData != null){
                    transformedData.add(finalData);
                }
                break;
        }
        return transformedData;
    }


    private ChartDataResponse getFinalData(int bucketIndex, Long sum) {
        if(sum == 0) return null;

        LocalDate date = LocalDate.now().withDayOfMonth(1);
        int lastDay = date.lengthOfMonth();

        ChartDataResponse finalData = new ChartDataResponse();
        switch (bucketIndex) {
            case 0 -> finalData.setPeriod("1-7");
            case 1 -> finalData.setPeriod("8-14");
            case 2 -> finalData.setPeriod("15-21");
            case 3 -> finalData.setPeriod("22-" + lastDay);
            default -> finalData.setPeriod("No date");
        }
        finalData.setVisits(sum);
        return finalData;
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
        }
        return visits;
    }



    public Url mostPopular(Long id){
        return urlRepo.findTopByUserIdOrderByVisitsDesc(id);
    }

}

