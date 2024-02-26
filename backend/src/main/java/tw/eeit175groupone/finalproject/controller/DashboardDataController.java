package tw.eeit175groupone.finalproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.eeit175groupone.finalproject.dto.DashboardData;
import tw.eeit175groupone.finalproject.service.DashboardDataService;

import java.util.Map;

@RestController
@RequestMapping("/dashboard/data")
public class DashboardDataController{

    @Autowired
    private DashboardDataService dashboardDataService;
    

    @PostMapping("/find")
    public ResponseEntity<?> findAllData(@RequestBody Map<String,String> request){
        System.out.println(request);
        if(request!=null&&!request.isEmpty()){
           DashboardData allData=dashboardDataService.findAllData(request);
            if(allData!=null){
                return ResponseEntity.ok().body(allData);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
