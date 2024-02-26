package tw.eeit175groupone.finalproject.dao;

import java.util.List;
import java.util.Map;

public interface OrderdetailsDAO{


    Integer findsumRevenue(Map<String,String> request);

    List<Object[]> findsumMonthlyRevenue(Map<String, String> request,Integer requestYear);
    
    List<Object[]> findsumDailyRevenue(Map<String, String> request,Integer requestYear,Integer requestMonth);

    List<Object[]> findsumRevenueGroupByProductType(Map<String, String> request);

    List<Object[]> findsumMonthlyRevenueGroupByProductType(Map<String, String> request,Integer requestYear);

    List<Object[]> findsumDailyRevenueGroupByProductType(Map<String, String> request,Integer requestYear,Integer requestMonth);
}
