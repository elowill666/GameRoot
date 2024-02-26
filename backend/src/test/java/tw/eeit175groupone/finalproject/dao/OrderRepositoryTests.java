package tw.eeit175groupone.finalproject.dao;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import tw.eeit175groupone.finalproject.domain.OrderdetailsBean;
import tw.eeit175groupone.finalproject.domain.OrdersBean;
import tw.eeit175groupone.finalproject.dto.DashboardOrderFind;
import tw.eeit175groupone.finalproject.dto.OrderDTO;
import tw.eeit175groupone.finalproject.util.DatetimeConverter;

@SpringBootTest
public class OrderRepositoryTests{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    // @Test
    // public void testfindOrder(){
    // List<Object[]> result = orderRepository.findOrderdetailByOrderId(1);
    // List<MyOrderDTO> wish=new ArrayList();
    // for (Object[] temp : result) {
    // MyOrderDTO dto=new MyOrderDTO();
    // BeanUtils.copyProperties((OrdersBean) temp[0],dto);
    // BeanUtils.copyProperties((OrderdetailsBean) temp[1],dto);
    // System.err.println("WishlistId: " + dto);
    // wish.add(dto);

    // }
    // System.err.println( wish);
    // }

    // }
    // System.err.println( wish);
    // }

    @Test

    public void testfindShoppingcartByUserId(){
        Integer id=1;
        List<Object[]> shoppingcartByUserId=orderRepository.findShoppingcartByUserId(id);
        List<OrderDTO> array=new ArrayList<OrderDTO>();
        for(Object[] objects : shoppingcartByUserId){
            OrderDTO orderDTO=new OrderDTO();
            orderDTO.setCartmiddleId((Integer) objects[0]);
            orderDTO.setProductId((Integer) objects[1]);
            orderDTO.setShoppingcartId((Integer) objects[2]);
            orderDTO.setQuantity((Integer) objects[3]);
            orderDTO.setUserId((Integer) objects[4]);
            orderDTO.setPrice((Integer) objects[5]);
            orderDTO.setProductName((String) objects[6]);
            orderDTO.setProductType((String) objects[7]);
            array.add(orderDTO);
        }
        System.err.println(array);
    }

    @Test
    public void testsumRevenue(){
        Integer integer=orderRepository.sumRevenue();
        System.out.println(integer);
    }

    @Test
    public void testsumMonthlyRevenue(){
        Integer getonlyYear=DatetimeConverter.getOnlyYear(new Date());
        List<Object[]> integers=orderRepository.sumMonthlyRevenue(getonlyYear);
        for(Object[] temp : integers){
            System.out.println(temp[0]);
            System.out.println(temp[1]);
        }
    }

    @Test
    public void testsumDailyRevenue(){
        List<Object[]> objects=orderRepository.sumDailyRevenue(2024,1);
        for(Object[] temp : objects){
            System.out.println(temp[0]);
            System.out.println(temp[1]);
        }
        System.out.println(DatetimeConverter.getOnlyMonth(new Date()));
        System.out.println(DatetimeConverter.getDayOfMonth("2024-01"));
    }

    @Test
    public void testsumRevenueGroupByProductType(){
        List<Object[]> integers=orderRepository.sumRevenueGroupByProductType();

        for(Object[] temp : integers){
            System.out.println(temp[0]);
            System.out.println(temp[1]);
        }
    }

    @Test
    public void testsumMonthlyRevenueByProductType(){
        List<Object[]> integers=orderRepository.sumMonthlyRevenueGroupByProductType(2024);
        for(Object[] temp : integers){
            System.out.println(temp[0]);
            System.out.println(temp[1]);
            System.out.println(temp[2]);
        }
    }

    @Test
    public void testsumDailyRevenueByProductType(){
        List<Object[]> integers=orderRepository.sumDailyRevenueGroupByProductType(2024,1);
        for(Object[] temp : integers){
            System.out.println(temp[0]);
            System.out.println(temp[1]);
            System.out.println(temp[2]);
        }
    }

    @Test
    public void findProductTypeTest(){
        List<String> productType=productRepository.findProductType();
        System.out.println(productType);
    }

    @Test
    public void findByUserId(){
        List<Integer> ids=new ArrayList<>();
        ids.add(1);
        ids.add(2);
        ids.add(3);
        List<OrdersBean> orders=orderRepository.findAllById(ids);
        for(OrdersBean o : orders){
            o.setConsigneeTelephonenumber(123);
            System.out.println(o);
        }

    }

    @Test
    public void findOrderWithLimitTest(){
        Sort sort=Sort.by(Sort.Direction.DESC,"orderId");
        sort=sort.reverse();
        Pageable pageable=PageRequest.of(1,10,sort);
        List<OrdersBean> allord=orderRepository.findOrderWithLimit(pageable);
        Map<String, List<OrderdetailsBean>> allorder=new HashMap();
        for(OrdersBean temp : allord){
            List<OrderdetailsBean> tempdetail=new ArrayList<>();
            System.out.println(temp);

        }
    }

    @Test
    public void findOrderWithOrderdetailTest(){
        Sort sort=Sort.by(Sort.Direction.DESC,"orderId");
        sort=sort.reverse();
        Pageable pageable=PageRequest.of(1,10,sort);
        List<Object[]> allord=orderRepository.findOrderWithOrderdetail(pageable);
        Map<String, List<OrderdetailsBean>> allorder=new HashMap();
        for(Object[] temp : allord){

            System.out.println(temp[0]);
            System.out.println(temp[1]);

        }
    }

    @Test
    public void countByUserIdTest(){
        Long aLong=orderRepository.countByUserId(1);
        System.out.println(aLong);

    }

    @Test
    public void smallTest(){
        OrdersBean ob=orderRepository.findById(1).orElse(null);
        Long now=new Date().getTime();
        Long l=ob.getOrderDate().getTime()-new Date().getTime();
        System.out.println(l);

    }

    @Test
    public void findAllByIdTest(){

        List<Integer> okok=new ArrayList<>();
        okok.add(1);
        okok.add(2);
        okok.add(3);
        okok.add(4);

        List<OrdersBean> allById=orderRepository.findAllById(okok);
        for(OrdersBean temp : allById){
            System.out.println(temp);
        }
    }

    @Test
    public void findTest(){

        List<Integer> okok=new ArrayList<>();
        okok.add(1);
        okok.add(2);
        okok.add(3);
        okok.add(4);

        List<OrdersBean> allById=orderRepository.findAllById(okok);
        for(OrdersBean temp : allById){
            System.out.println(temp);
        }
    }

    @Test
    public void sumPersonalComsumeTest(){
        List<Integer> okok=new ArrayList<>();
        okok.add(1);
        okok.add(2);
        okok.add(3);
        okok.add(4);
        okok.add(5);
        okok.add(6);
        okok.add(7);
        okok.add(8);
        List<Object[]> allById=orderRepository.sumPersonalComsume(okok);
        for(Object[] temp : allById){
            System.out.println(temp[0]);
            System.out.println(temp[1]);
        }
    }

}
