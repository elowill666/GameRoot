package tw.eeit175groupone.finalproject.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.eeit175groupone.finalproject.domain.OrderdetailsBean;
import tw.eeit175groupone.finalproject.domain.OrdersBean;
import tw.eeit175groupone.finalproject.dto.DashboardOrder;
import tw.eeit175groupone.finalproject.dto.DashboardOrderFind;
import tw.eeit175groupone.finalproject.dto.OrderOrderDTO;
import tw.eeit175groupone.finalproject.service.DashboardOrderService;
import tw.eeit175groupone.finalproject.service.OrdersService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin
public class DashBoardOrderController{

    @Autowired
    private DashboardOrderService dashboardOrderService;

    @Autowired
    private OrdersService ordersService;

    //接收請求後丟出全部的訂單資訊給前端管理者
    @PostMapping("/order/find")
    public ResponseEntity<?> findAllOrder(@RequestBody DashboardOrderFind request){
        System.out.println("find"+request);
        if(request!=null){
            List<OrderOrderDTO> allOrder=dashboardOrderService.findAllOrder(request);
            if(allOrder!=null && !allOrder.isEmpty()){
                System.out.println(allOrder);
                return ResponseEntity.ok().body(allOrder);
            }
            return ResponseEntity.ok().body("nodata");
        }
        return ResponseEntity.noContent().build();

    }

    //接收請求後丟出某個使用者的訂單資訊給前端管理者
    @PostMapping("/order/{userId}")
    public ResponseEntity<?> findOrderByUserId(@PathVariable("userId") Integer userId,@RequestBody Map<String, String> request){
        if(!request.isEmpty() && request!=null){
            System.out.println(request);
            List<OrderOrderDTO> UserOrders=dashboardOrderService.findOrderById(userId,request);
            if(UserOrders!=null && !UserOrders.isEmpty()){
                return ResponseEntity.ok().body(UserOrders);
            }
        }
        return ResponseEntity.noContent().build();

    }


    @PutMapping("/order/{orderId}")
    public ResponseEntity<?> modifyOrderByOrderId(@PathVariable("orderId") Integer orderId,@RequestBody OrderOrderDTO request){
        System.err.println("updateorder = "+request+",orderId = "+orderId);
        if(request!=null && orderId!=null){
            OrderOrderDTO afterModify=dashboardOrderService.modifyByOrderId(orderId,request);
            if(afterModify!=null){
                return ResponseEntity.ok().body(afterModify);
            }
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/order/orderdetail/{orderId}")
    public ResponseEntity<?> findOrderDetailByOrderId(@PathVariable("orderId") Integer orderId){
        if(orderId!=null){
            System.out.println(orderId);
            List<OrderdetailsBean> orderdetails=dashboardOrderService.findOrderdetailById(orderId);
            return ResponseEntity.ok().body(orderdetails);
        }
        return ResponseEntity.noContent().build();
    }

    //回傳總訂單數
    @PostMapping ("/order/ordercount")
    public ResponseEntity<Long> countOrder(@RequestBody DashboardOrderFind request){
        System.err.println("count="+request);
        if (request!=null) {
            Long countOrder = dashboardOrderService.countOrder(request);
            System.out.println("fin"+countOrder);
            if (countOrder != null) {
                return ResponseEntity.ok().body(countOrder);
            }

        }
        return ResponseEntity.noContent().build();
    }

    //以前端請求的userId回傳該user的總訂單數
    @GetMapping("/order/ordercount/{userId}")
    public ResponseEntity<Long> countOrderByUserId(@PathVariable("userId") Integer userId){
        Long countOrder=dashboardOrderService.countOrderByUserId(userId);
        if(countOrder!=null){
            return ResponseEntity.ok().body(countOrder);
        }
        return ResponseEntity.noContent().build();
    }

}
