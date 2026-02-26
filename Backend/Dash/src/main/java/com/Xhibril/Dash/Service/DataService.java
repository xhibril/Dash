package com.Xhibril.Dash.Service;

import com.Xhibril.Dash.Dto.ChartDataRequest;
import com.Xhibril.Dash.Dto.ChartDataResponse;
import com.Xhibril.Dash.Model.UrlStat;
import com.Xhibril.Dash.Repository.UrlStatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class DataService {

    @Autowired
    UrlStatRepository urlStatRepo;


    @GetMapping("/chart")
    public List<ChartDataResponse> chartData(String period, Long urlId) {
        LocalDate date = LocalDate.now();
        LocalDateTime start = null, end = null;
        System.out.println("PERIODDDDDDDDDD" + period);

        switch (period) {
            case "DAILY":
                start = date.atStartOfDay();
                end = date.atTime(LocalTime.MAX);
                return transformData(urlStatRepo.getStats(urlId, start, end), "WEEKLY");



            case "WEEKLY":
                date = date.with(DayOfWeek.MONDAY);
                start = date.atStartOfDay();
                end = date.plusDays(6).atTime(LocalTime.MAX);
                System.out.println("\nSTART: " + start);
                System.out.println("\nEND: " + end);
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
            case "WEEKLY":
                for (ChartDataResponse cdr : data) {

                    LocalDate date = cdr.getBucket();
                    String dayName = date.getDayOfWeek()
                            .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

                    System.out.println("PRINTING CHART DATA:"+cdr.getBucket() + cdr.getVisits() + "\n");


                    cdr.setPeriod(dayName);
                    transformedData.add(cdr);
                }
                break;


            case "MONTHLY":
                LocalDate date = LocalDate.now().withDayOfMonth(1);
                int lastDay = date.lengthOfMonth();
                Long sum = 0L;

                int[][] ranges = {{1, 7}, {8, 14}, {15, 21}, {22, lastDay}};

                int i = 0;


                ChartDataResponse tempData = new ChartDataResponse();


                for (ChartDataResponse cdr : data) {

                    if (cdr.getVisits() >= ranges[i][0] && cdr.getVisits() <= ranges[i][1]) {
                        sum += cdr.getVisits();
                        i++;
                    } else {

                        tempData.setVisits(sum);
                        tempData.setPeriod(Arrays.toString(ranges[i-1]));
                        transformedData.add(tempData);
                    }
                }
                break;

        }


        return transformedData;
    }


}
