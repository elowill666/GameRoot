package tw.eeit175groupone.finalproject.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import tw.eeit175groupone.finalproject.dao.OrderRepository;
import tw.eeit175groupone.finalproject.dao.UserDashboardRepository;
import tw.eeit175groupone.finalproject.domain.OrdersBean;
import tw.eeit175groupone.finalproject.dto.DashboardOrderFind;
import tw.eeit175groupone.finalproject.dto.OrderOrderDTO;

import java.util.*;

@SpringBootTest
public class DashboardOrderTests{
    @Autowired
    private DashboardOrderService dashboardOrderService;
    @Autowired
    private OrderRepository orderRepository;


    @Test
    public void findOrderByIdTest(){
        Sort sort=Sort.by(Sort.Direction.DESC,"orderId");
        sort=sort.reverse();
        Pageable pageable=PageRequest.of(1,10,sort);
        Map<String, String> test=new HashMap<>();
        test.put("start","1");
        test.put("rows","10");
        test.put("sort","orderdatedesc");
        List<OrderOrderDTO> orderById=dashboardOrderService.findOrderById(1,test);
        System.out.println(orderById);
    }

    @Test
    public void modifyOrderdetailRefundTest(){

//        Map<Integer, String> test=new HashMap<>();
//        test.put(1,"pending");
//        Map<Integer, String> orderById=dashboardOrderService.modifyOrderdetailRefund(test);
//        System.out.println(orderById);
    }

    @Test
    public void checkStatusTest(){
        OrdersBean bean=new OrdersBean();
        bean.setOrderDate(new Date(System.currentTimeMillis()-2593000000L));
        bean.setPaymentStatus(-1);
        List<OrdersBean> lisb=new ArrayList<>();
        lisb.add(bean);
        List<OrdersBean> ordersBeans=dashboardOrderService.checkStatus(lisb);
        System.out.println(ordersBeans);
    }

    @Test
    public void findOrdersWithCriteriaTest(){
        HashMap<String, String> tempmap=new HashMap<>();
        tempmap.put("start","1");
        tempmap.put("rows","1");
        tempmap.put("sort","createdDESC");
        DashboardOrderFind dashboardOrderFind=new DashboardOrderFind();
        dashboardOrderFind.setOrderPage(tempmap);
       List<Integer> tempInt=new ArrayList<>();
       tempInt.add(2);
       tempInt.add(5);
        List<OrdersBean> ordersWithCriteria=dashboardOrderService.findOrdersWithCriteria(dashboardOrderFind,tempInt);
        for(OrdersBean temp : ordersWithCriteria){
            System.out.println(temp);
        }

    }

}
