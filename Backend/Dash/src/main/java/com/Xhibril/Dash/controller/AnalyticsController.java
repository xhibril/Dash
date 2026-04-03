package com.Xhibril.Dash.controller;
import com.Xhibril.Dash.dto.analytics.ChartDataResponse;
import com.Xhibril.Dash.model.Url;
import com.Xhibril.Dash.service.AuthService;
import com.Xhibril.Dash.service.AnalyticsService;
import com.Xhibril.Dash.service.AnalyticsService.Period;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AnalyticsController {

    private final AuthService authService;
    private final AnalyticsService analyticsService;

    public AnalyticsController(AuthService authService,
                               AnalyticsService analyticsService){
        this.authService = authService;
        this.analyticsService = analyticsService;
    }


    @GetMapping("/analytics/popular")
    public Url getMostPopular(HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);
        return analyticsService.mostPopular(id);
    }



    @GetMapping("/analytics/visits")
    public Integer getVisits(HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);
        return analyticsService.getVisits(id);

    }


    @GetMapping("/analytics/trend")
    public Integer getTrend(HttpServletRequest req){
        Long id = authService.getAuthenticatedId(req);
        return analyticsService.getTrend(id);
    }

    @GetMapping("/analytics/chart")
    public List<ChartDataResponse> getChartData(@RequestParam Long id, @RequestParam Period period){
        return analyticsService.chartData(period, id);
    }
}
