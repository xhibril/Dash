package com.Xhibril.Dash.Service;

import com.Xhibril.Dash.Dto.ChartDataRequest;
import com.Xhibril.Dash.Dto.ChartDataResponse;
import com.Xhibril.Dash.Model.UrlStat;
import com.Xhibril.Dash.Repository.UrlStatRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;

import java.util.List;


@Service
public class DataService {

    @Autowired
    UrlStatRepository urlStatRepo;

    public List<ChartDataResponse> chartData(String period, Long urlId) {
        LocalDate date = LocalDate.now();
        LocalDateTime start, end;

        switch (period) {
            case "DAILY":
                start = date.atStartOfDay();
                end = date.atTime(LocalTime.MAX);
                return transformData(urlStatRepo.getDaily(urlId, start, end), "DAILY");


            case "WEEKLY":
                date = date.with(DayOfWeek.MONDAY);
                start = date.atStartOfDay();
                end = date.plusDays(6).atTime(LocalTime.MAX);
                return transformData(urlStatRepo.getStats(urlId, start, end), "WEEKLY");


            case "MONTHLY":
                date = date.withDayOfMonth(1);
                LocalDate lastDay = date.withDayOfMonth(date.lengthOfMonth());

                start = date.atStartOfDay();
                end = lastDay.atTime(LocalTime.MAX);
                return transformData(urlStatRepo.getStats(urlId, start, end), "MONTHLY");

        }
        return null;
    }


    public List<ChartDataResponse> transformData(List<ChartDataResponse> data, String period) {

        List<ChartDataResponse> transformedData = new ArrayList<>();

        switch (period) {
            case "DAILY":

                for (ChartDataResponse cdr : data) {
                    String formattedHour = cdr.getHour() + ":00";
                    cdr.setPeriod(formattedHour);
                    transformedData.add(cdr);
                }
                return transformedData;


            case "WEEKLY":
                for (ChartDataResponse cdr : data) {
                    LocalDate date = cdr.getBucket();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");
                    String dateStr = date.format(formatter);

                    cdr.setPeriod(dateStr);
                    transformedData.add(cdr);
                }
                break;


            case "MONTHLY":
                Long sum = 0L;
                int bucketIndex = 0, prev = 0;

                for (int i = 0; i < data.size(); ) {
                    ChartDataResponse cdr = data.get(i);
                    int currentDate = cdr.getBucket().getDayOfMonth();
                    bucketIndex = (currentDate - 1) / 7;
                    if (bucketIndex >= 4) bucketIndex = 3;

                    if (bucketIndex == prev) {
                        sum += cdr.getVisits();
                        i++;

                    } else {
                        ChartDataResponse finalData = getFinalData(prev, sum);
                        transformedData.add(finalData);
                        sum = 0L;
                    }
                    prev = bucketIndex;
                }

                ChartDataResponse finalData = getFinalData(bucketIndex, sum);
                transformedData.add(finalData);
                break;
        }
        return transformedData;
    }


    private ChartDataResponse getFinalData(int bucketIndex, Long sum) {

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


}
