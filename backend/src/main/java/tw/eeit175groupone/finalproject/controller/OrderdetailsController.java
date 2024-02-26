package tw.eeit175groupone.finalproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.eeit175groupone.finalproject.domain.OrderdetailsBean;
import tw.eeit175groupone.finalproject.service.OrderdetailsService;

@RestController
@RequestMapping("/Orderdetails")
@CrossOrigin
public class OrderdetailsController {
  @Autowired
  private OrderdetailsService orderdetailsService;

  @PostMapping("/redfund")
  public ResponseEntity<?> redfund(@RequestBody OrderdetailsBean orderdetailsBean) {
    if (orderdetailsBean != null) {
      orderdetailsService.redfundOrderdetail(orderdetailsBean);
      return ResponseEntity.ok().body(true);
    }
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/findall")
  public List<OrderdetailsBean> findAll() {
    return orderdetailsService.findAll();
  }

  @GetMapping("/find/{id}")
  public ResponseEntity<?> findByOrderId(@PathVariable("id") Integer orderId) {
    if (orderId != null) {
      List<OrderdetailsBean> beans = orderdetailsService.findByOrderId(orderId);
      return ResponseEntity.ok().body(beans);
    }
    return ResponseEntity.notFound().build();
  }

}
