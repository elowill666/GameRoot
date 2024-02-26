package tw.eeit175groupone.finalproject.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;
import tw.eeit175groupone.finalproject.domain.OrdersBean;
import tw.eeit175groupone.finalproject.domain.ProductBean;
import tw.eeit175groupone.finalproject.domain.UserBean;
import tw.eeit175groupone.finalproject.dto.MyOrderDTO;
import tw.eeit175groupone.finalproject.dto.OrderOrderDTO;
import tw.eeit175groupone.finalproject.service.OrdersService;
import tw.eeit175groupone.finalproject.service.ShoppingcartService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private ShoppingcartService shoppingcartService;

    // 接收綠界API回傳
    @PostMapping("order/ecpayreturn")
    public Integer ecpayReturn(@RequestBody String body) {
        System.err.println(body);
        System.err.println("1111111---------------");

        return 1;
    }

    // 要找退款的歷史訂單
    @GetMapping("order/findall")
    public ResponseEntity<?> findAllOrder(HttpSession session) {
        Integer userId = this.findUserId(session);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }
        List<OrdersBean> data = ordersService.findOrdersByUserId(userId);
        return ResponseEntity.ok().body(data);
    }

    // 訂購後產生訂單並清空購物車
    @PostMapping("order/insert")
    public ResponseEntity<?> orderInsert(@RequestBody OrdersBean bean, HttpSession session) {
        Integer userId = this.findUserId(session);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }
        if (bean == null) {
            return ResponseEntity.notFound().build();
        }
        OrdersBean returnbean = ordersService.findShoppingcartByUserId(userId, bean);
        shoppingcartService.cleanShoppingcart(userId);
        if (returnbean.getPaymentMethod().equals("ECPAY")) {
            String checkoutForm = ordersService.ecpayCheckout(returnbean);
            return ResponseEntity.ok().body(checkoutForm);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 找出用戶的訂單
     * 
     * @param userId 用戶編號
     * @return 用戶的訂單
     */
    @PostMapping("order/userorder/{id}")
    public ResponseEntity<?> findOrdersByUserId(@PathVariable("id") Integer userId) {
        // TODO: process POST request
        if (userId != null) {
            List<OrdersBean> beans = ordersService.findOrdersByUserId(userId);
            return ResponseEntity.ok().body(beans);
        } else {
            return ResponseEntity.ok().body("nothing");
        }
    }

    /**
     * 找出特定訂單編號
     * 
     * @param orderId 訂單編號
     * @return 訂單內容
     */
    @GetMapping("/order/find/{id}")
    public ResponseEntity<?> findAllProducts(@PathVariable("id") Integer orderId) {
        OrdersBean bean = ordersService.findByOrderId(orderId);
        if (bean != null) {
            return ResponseEntity.ok().body(bean);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/order/findOrderdetail/{id}")
    // public ResponseEntity<?> findOrderdetail(Integer orderId) {
    // List<MyOrderDTO> bean = ordersService.findOrderdetail(orderId);
    // if (bean != null) {
    // return ResponseEntity.ok().body(bean);
    // }
    // return ResponseEntity.notFound().build();
    // }

    public ResponseEntity<?> findOrderdetail(@PathVariable("id") Integer orderId) {

        List<MyOrderDTO> array = ordersService.findOrderdetail(orderId);
        if (array != null) {
            return ResponseEntity.ok().body(array);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/order/findOrder")
    public ResponseEntity<?> findOrder(HttpSession session) {
        if (session != null) {
            UserBean user = (UserBean) session.getAttribute("user");
            Integer userId = user.getId();
            if (userId != null) {
                List<OrdersBean> data = ordersService.findOrder(userId);
                return ResponseEntity.ok().body(data);
            }
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * testtest123
     * 
     * @param userId 用戶編號
     * @return 用戶的訂單
     */
    @PostMapping("order/userordertest/{id}")
    public ResponseEntity<?> findOrderOrderTest(@PathVariable("id") Integer userId) {
        // TODO: process POST request
        if (userId != null) {
            List<OrderOrderDTO> beans = ordersService.findOrderOrder(userId);
            return ResponseEntity.ok().body(beans);
        } else {
            return ResponseEntity.ok().body("nothing");
        }
    }

    public Integer findUserId(HttpSession session) {
        if (session == null) {
            return null;
        }
        UserBean user = (UserBean) session.getAttribute("user");
        if (user == null) {
            // 如果找不到名為 "user" 的屬性，嘗試獲取 "googleuser"
            user = (UserBean) session.getAttribute("googleuser");
        }
        if (user == null) {
            return null;
        }
        Integer userId = user.getId();
        if (userId == null) {
            return null;
        }
        return userId;
    }
}
