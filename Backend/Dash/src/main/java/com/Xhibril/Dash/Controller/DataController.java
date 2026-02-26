package com.Xhibril.Dash.Controller;

import com.Xhibril.Dash.Dto.ChartDataRequest;
import com.Xhibril.Dash.Dto.ChartDataResponse;
import com.Xhibril.Dash.Service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    DataService dataService;

    @GetMapping("/chart")
    private List<ChartDataResponse> getChartData(@RequestParam Long id, @RequestParam String period){
        return dataService.chartData(period, id);
    }


}
