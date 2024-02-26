package tw.eeit175groupone.finalproject.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tw.eeit175groupone.finalproject.dao.CommentsRepository;
import tw.eeit175groupone.finalproject.dao.MerchandiseRepository;
import tw.eeit175groupone.finalproject.dao.ProductRepository;
import tw.eeit175groupone.finalproject.domain.*;
import tw.eeit175groupone.finalproject.dto.*;
import tw.eeit175groupone.finalproject.util.DatetimeConverter;

import java.util.*;

@SpringBootTest
public class DashboardServiceTest {
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MerchandiseRepository merchandiseRepository;
    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private DashboardOrderService dashboardOrderService;

    @Test
    public void findAllProductTest() {
        DashboardProduct test = new DashboardProduct();
        test.setStart(1);
        test.setRows(10);
        test.setProductName("霍");
        Map<String, Object> tempData = dashboardService.findAllProduct(test);
        Object tempDataList = tempData.getOrDefault("data", null);
        List<CompleteProductInfo> allProduct = (List<CompleteProductInfo>) tempDataList;

        System.out.println("資料數=" + tempData.get("countOfData"));
        if (allProduct != null) {
            for (CompleteProductInfo temp : allProduct) {
                System.out.println("test" + temp.getProductId());
            }
        }
    }

    @Test
    public void productFindAndSortTest() {
        DashboardProduct test = new DashboardProduct();
        test.setStart(1);
        test.setRows(100);
        // Map<String,Object> allProduct=dashboardService.findAllProduct(test);
        // List<CompleteProductInfo>
        // productInfoList=dashboardService.productFindAndSort(allProduct,test);
        // for(CompleteProductInfo temp : productInfoList){
        // System.out.println("test"+temp);
        // }
    }

    // @Test
    // public void modifyProductTest(){
    // Optional<ProductBean> temp=productRepository.findById(20);
    // DashboardProduct testproduct=new DashboardProduct();
    // if(temp!=null && temp.isPresent()){
    // ProductBean bean=temp.get();
    // BeanUtils.copyProperties(bean,testproduct);
    // testproduct.setDiscountFactor(10);
    // DashboardProduct dboardProduct=dashboardService.modifyProduct(testproduct);
    // System.out.println("new"+dboardProduct);
    // }
    // System.out.println("old"+temp.get());
    // }

    // @Test
    // public void modifyProductTest(){
    // Optional<ProductBean> temp=productRepository.findById(19);
    // CompleteProductInfo testproduct=new CompleteProductInfo();
    // if(temp!=null && temp.isPresent()){
    // Optional<MerchandiseBean>
    // mtemp=merchandiseRepository.findByProductIdAndMerchandiseId(19,13);
    // ProductBean bean=temp.get();
    // MerchandiseBean mbean=mtemp.get();
    // BeanUtils.copyProperties(bean,testproduct);
    // BeanUtils.copyProperties(mbean,testproduct);
    // testproduct.setColor("七彩");
    // CompleteProductInfo
    // dboardProduct=dashboardService.modifyProduct(testproduct);
    // System.out.println("new"+dboardProduct);
    // }
    // System.out.println("old"+temp.get());
    // }

    @Test
    public void modifyDiscountProductTest() {
        dashboardService.modityDiscountProduct();
    }

    @Test
    public void findAllOrderTest() {
        // Map<String,String> request=new HashMap<>();

        // List<OrderOrderDTO> allOrder=dashboardOrderService.findAllOrder(newfind);
        // for(OrderOrderDTO temp : allOrder){
        // System.out.println(temp);
        // }
    }

    // @Test
    // public void getsumMonthlyRevenueTest(){
    // Integer onlyYear=DatetimeConverter.getonlyYear(new Date());
    //
    // Map<Integer, Integer> trytry=dashboardService.getsumMonthlyRevenue(onlyYear);
    //
    // System.out.println(trytry);
    // }

    // @Test
    // public void getsumDailyRevenueTest(){
    // Integer thisYear=DatetimeConverter.getOnlyYear(new Date());
    // Integer thisMonth=DatetimeConverter.getOnlyMonth(new Date());
    // Integer dayOFMonth=DatetimeConverter.getOnlyDay(new Date());
    // Map<Integer, Integer>
    // trytry=dashboardService.getsumDailyRevenue(thisYear,thisMonth,dayOFMonth);
    // System.out.println(trytry);
    // }

    // @Test
    // public void getsumRevenueByProductTpyeTest(){
    // Map<String, Integer> temp=dashboardService.getsumRevenueByProductTpye();
    // System.out.println(temp);
    //
    //
    // }
    //
    // @Test
    // public void getsumMonthlyRevenueGroupByProductTypeTest(){
    // Integer onlyYear=DatetimeConverter.getOnlyYear(new Date());
    // Map<String, Map<Integer, Integer>>
    // stringMapMap=dashboardService.getsumMonthlyRevenueGroupByProductType(onlyYear);
    // System.out.println(stringMapMap);
    //
    // }

    // @Test
    // public void getsumDailyRevenueGroupByProductTypeTest(){
    // Integer thisYear=DatetimeConverter.getOnlyYear(new Date());
    // Integer thisMonth=DatetimeConverter.getOnlyMonth(new Date());
    // Integer dayOFMonth=DatetimeConverter.getOnlyDay(new Date());
    //
    // Map<String, Map<Integer, Integer>> stringMapMap
    // =dashboardService.getsumDailyRevenueGroupByProductType(thisYear,thisMonth,dayOFMonth);
    // System.out.println(stringMapMap);
    //
    // }

    @Test
    public void findProductwithPageableTest() {
        List<ProductBean> temp = dashboardService.findProductwithPageable(10, 10);
        for (ProductBean p : temp) {
            System.out.println(p.getProductId());
        }
    }

    @Test
    public void countProductTest() {
        Integer temp = dashboardService.countProduct();

        System.out.println(temp);

    }

}
