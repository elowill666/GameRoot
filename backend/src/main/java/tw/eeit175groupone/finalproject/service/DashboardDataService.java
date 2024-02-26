package tw.eeit175groupone.finalproject.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.eeit175groupone.finalproject.dao.OrderRepository;
import tw.eeit175groupone.finalproject.dao.OrderdetailsRepository;
import tw.eeit175groupone.finalproject.dao.ProductRepository;
import tw.eeit175groupone.finalproject.dto.DashboardData;
import tw.eeit175groupone.finalproject.util.DatetimeConverter;

import java.util.*;

@Transactional(rollbackFor={Exception.class})
@Service
public class DashboardDataService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderdetailsRepository orderdetailsRepository;


    public DashboardData findAllData(Map<String,String> request ){
        DashboardData result=new DashboardData();
        Integer thisYear=null;
        Integer thisMonth=null;
        Integer dayOFMonth=null;
        long ms=0;
        String date=request.getOrDefault("date",null);
        if(date!=null&&date.length()!=0){
            date=DatetimeConverter.parseToLocalTime(date);
            thisYear=Integer.parseInt(date.substring(0,4));
            thisMonth=Integer.parseInt(date.substring(5,7));
            dayOFMonth=DatetimeConverter.getDayOfMonth(date.substring(0,7));
        }else {
            return null;
        }
        System.out.println("year"+thisYear);
        System.out.println("thisMonth"+thisMonth);
        System.err.println("dayOFMonth"+dayOFMonth);
        
        
        result.setDataMonth(thisMonth+"");
        result.setDataYear(thisYear+"");
        //取得總營收
        result.setRevenune(this.getsumRevenue(request));
        //取得某年每月總營收
        result.setAnnualRevenune(this.getsumMonthlyRevenue(request,thisYear));
        //取得某月每日總營收
        result.setMonthlyRevenune(this.getsumDailyRevenue(request,thisYear,thisMonth,dayOFMonth));
        //各類總營收
        result.setRevenueByProductType(this.getsumRevenueByProductTpye(request));
        //各類年營收
        result.setAnnualRevenuneByProductType(this.getsumMonthlyRevenueGroupByProductType(request,thisYear));
        //各類月營收
        result.setMonthlyRevenuneByProductType(this.getsumDailyRevenueGroupByProductType(request,thisYear,thisMonth,dayOFMonth));
        System.out.println(result);
        if(result!=null){
            return result;
        }
        return null;
    }

    /**
     * 一個取得總營收的方法
     */
    private Integer getsumRevenue(Map<String,String> request){
        Integer revenue=orderdetailsRepository.findsumRevenue(request);
        if(revenue!=null){
            return revenue;
        }
        return null;
    }

    /**
     * 一個取得某年的每月營收的方法
     *
     * @param request--date日期，productType產品類別，productSubtype產品子類別，productName產品名字 此方法只會用到productType產品類別，productSubtype產品子類別
     * @param requestYear-Integer欲找尋年份
     * @return Map--key:月份,value:當月營收
     */
    private Map<Integer, Integer> getsumMonthlyRevenue(Map<String,String> request,Integer requestYear){
        List<Object[]> monthlyRevenue=orderdetailsRepository.findsumMonthlyRevenue(request,requestYear);
        Map<Integer, Integer> tempMonthlyRevenue=new HashMap<>();
        for(int i=1; i<=12; i++){
            tempMonthlyRevenue.put(i,0);
        }
        if(monthlyRevenue!=null){
            for(Object[] temp : monthlyRevenue){
                Integer tempsum=((Integer) temp[1]);
                tempMonthlyRevenue.put((Integer) temp[0],tempsum);
            }
            return tempMonthlyRevenue;
        }
        return null;
    }

    /**
     * 一個取得某月的每日營收的方法getsumRevenueByProductTpye
     *
     * @param request--date日期，productType產品類別，productSubtype產品子類別，productName產品名字 此方法只會用到productType產品類別，productSubtype產品子類別
     * @param day--Integer(day是用來得出輸入的月有幾天)
     * @return Map--key:日,value:當日營收
     */
    private Map<Integer, Integer> getsumDailyRevenue(Map<String,String> request,Integer requestYear,Integer requestMonth,Integer day){
        List<Object[]> monthlyRevenue=orderdetailsRepository.findsumDailyRevenue(request,requestYear,requestMonth);
        Map<Integer, Integer> tempDailyRevenue=new HashMap<>();
        for(int i=1; i<=day; i++){
            tempDailyRevenue.put(i,0);
        }
        if(monthlyRevenue!=null){
            for(Object[] temp : monthlyRevenue){
                Integer tempsum=((Integer) temp[1]);
                tempDailyRevenue.put((Integer) temp[0],tempsum);
            }
            return tempDailyRevenue;
        }
        return null;
    }


    private Map<String, Integer> getsumRevenueByProductTpye(Map<String,String> request){
        List<Object[]> tempobjects=orderdetailsRepository.findsumRevenueGroupByProductType(request);
        List<String> productType=this.detectProductData(request);
        Map<String, Integer> tempRevenueByProductType=new HashMap<>();
        if(tempobjects!=null){
            for(Object[] temp : tempobjects){
                tempRevenueByProductType.put((String) temp[0],(Integer) temp[1]);
            }
            for(String temp:productType){
                if(!tempRevenueByProductType.containsKey(temp)){
                    tempRevenueByProductType.put(temp,0);
                }
            }
            return tempRevenueByProductType;
        }
        return null;
    }

    /**
     * 一個傳入年份回傳某年各類別商品每月總營收的方法
     *
     * @param request--date日期，productType產品類別，productSubtype產品子類別，productName產品名字 此方法只會用到productType產品類別，productSubtype產品子類別
     * @return Map--Map<String,Map<Integer,Integer> 1.String為商品類別 2.Map<Integer,Integer>第一個為月份，第二個為當月總營收
     */
    private Map<String, Map<Integer, Integer>> getsumMonthlyRevenueGroupByProductType(Map<String,String> request,Integer requestYear){
        //先找商品有幾種類別
        List<String> productType=this.detectProductData(request);
        if(productType!=null && !productType.isEmpty()){
            //找出個月營收
            List<Object[]> tempobjects=orderdetailsRepository.findsumMonthlyRevenueGroupByProductType(request,requestYear);
            if(tempobjects!=null ){
                //此Map只要想要String塞分類，後面的Map可以按月份塞值
                Map<String, Map<Integer, Integer>> tempRevenueByProductType=new HashMap<>();
                for(String type : productType){
                    Map<Integer, Integer> tempMap=new HashMap<>();
                    //先將空的map塞入0(一年１２個月)
                    for(int i=1; i<=12; i++){
                        tempMap.put(i,0);
                    }
                    //這裡很沒效率的用字串比對，去分數據是屬於哪個產品分類
                    for(Object[] temp : tempobjects){
                        String prodcutType=(String) temp[0];
                        if(type.equals(prodcutType)){
                            Integer m=(Integer) temp[1];
                            Integer r=(Integer)temp[2];
                            tempMap.put(m,r);
                        }
                    }
                    tempRevenueByProductType.put(type,tempMap);
                }
                return tempRevenueByProductType;
            }
        }
        return null;
    }


    /**
     * 一個傳入年份及月份回傳某年某月各類別商品每日總營收的方法
     *
     * @param request--date日期，productType產品類別，productSubtype產品子類別，productName產品名字 此方法只會用到productType產品類別，productSubtype產品子類別
     * @return Map--Map<String,Map<Integer,Integer> 1.String為商品類別 2.Map<Integer,Integer>第一個為月份，第二個為當月總營收
     */
    private Map<String, Map<Integer, Integer>> getsumDailyRevenueGroupByProductType(Map<String,String> request,Integer requestYear,Integer requestMonth,Integer day){
        //先找商品有幾種類別
        List<String> productType=this.detectProductData(request);
        if(productType!=null && !productType.isEmpty()){
            //找出個月營收
            List<Object[]> tempobjects=orderdetailsRepository.findsumDailyRevenueGroupByProductType(request,requestYear,requestMonth);
            if(tempobjects!=null){
                //此Map只要想要String塞分類，後面的Map可以按月份塞值
                Map<String, Map<Integer, Integer>> tempRevenueByProductType=new HashMap<>();
                for(String type : productType){
                    Map<Integer, Integer> tempMap=new HashMap<>();
                    //先將空的map塞入0(一個月有幾天day就是多少)
                    System.out.println("test"+day);
                    for(int i=1; i<=day; i++){
                        tempMap.put(i,0);
                    }
                    //這裡很沒效率的用字串比對，去分數據是屬於哪個產品分類
                    for(Object[] temp : tempobjects){
                        String prodcutType=(String) temp[0];
                        if(type.equals(prodcutType)){
                            Integer d=(Integer) temp[1];
                            Integer r=(Integer) temp[2];
                            tempMap.put(d,r);
                        }
                    }
                    tempRevenueByProductType.put(type,tempMap);
                }
                return tempRevenueByProductType;
            }
        }
        return null;
    }

    /**
     * 用來判斷打包的成什麼，如果輸入名字就單一名字，輸入子類別打包成商品名字，類別打包成子類別，沒有就打包成類別
     * 
     * @param request
     * @return List<String>
     */
    public List<String> detectProductData(Map<String,String> request){
        List<String> productType=new ArrayList<>();
        if(request.containsKey("productName")&&request.get("productName").length()!=0){
            productType.add(request.getOrDefault("productName",null));
        }else if(request.containsKey("productSubtype")&&request.get("productSubtype").length()!=0){
            productType=productRepository.findProductName(request.getOrDefault("productSubtype",null));
        }else if(request.containsKey("productType")&&request.get("productType").length()!=0){
            productType=productRepository.findSubtype(request.getOrDefault("productType",null));
        } else if(!request.containsKey("productType")||request.get("productName").length()==0){
            productType=productRepository.findProductType();
        }
        return productType;
    }
    
}
