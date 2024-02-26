package tw.eeit175groupone.finalproject.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tw.eeit175groupone.finalproject.domain.OrderdetailsBean;

import java.util.*;

@SpringBootTest
public class OrderdetailsDAOImplTests{

    @Autowired
    private OrderdetailsRepository orderdetailsRepository;

    @Test
    public void findRevenueTest(){
        Map<String, String> temp=new HashMap<>();
//        temp.put("productType","digitalGames");
//        temp.put("productSubtype","action");

        System.out.println(orderdetailsRepository.findsumRevenue(temp));

    }

    //    @Test
//    public void findsumDailyRevenueTest() {
//        Map<String, String> temp = new HashMap<>();
//        //1706676445000 2024/02/02
//        temp.put("productType","cash");
//        temp.put("date","1706803200000");
////        temp.put("productSubtype","action");
//        List<Object[]> objects = orderdetailsRepository.findsumDailyRevenue(temp);
//        for (Object[] oo : objects) {
//            System.out.println(oo[0]);
//            System.out.println(oo[1]);
//        }
//    }
    @Test
    public void findAllByOrderIdTest(){
        List<Integer> testId=new ArrayList<>();
        testId.add(1);
        testId.add(2);
        List<OrderdetailsBean> allByOrderId=orderdetailsRepository.findAllByOrderIdIn(testId);
for(OrderdetailsBean temp:allByOrderId){
    temp.setProductCommentEndtime(new Date());
}
        OrderdetailsBean orderdetailsBean=orderdetailsRepository.findById(1).orElse(null);
orderdetailsBean.setProductCommentEndtime(new Date());
        System.out.println(allByOrderId);

    }

}
