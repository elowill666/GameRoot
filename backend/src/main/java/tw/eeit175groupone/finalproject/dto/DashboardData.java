package tw.eeit175groupone.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardData{
    private Integer revenune;
    private Map<Integer,Integer> annualRevenune;
    private Map<Integer,Integer> monthlyRevenune;
    private Map<String,Integer> revenueByProductType;
    private Map<String, Map<Integer, Integer>>  annualRevenuneByProductType;
    private Map<String, Map<Integer, Integer>> monthlyRevenuneByProductType;
    private String dataYear;
    private String dataMonth;
    
 
    
    
    
    
}
