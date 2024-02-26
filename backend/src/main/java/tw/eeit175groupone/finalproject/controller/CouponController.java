package tw.eeit175groupone.finalproject.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import tw.eeit175groupone.finalproject.domain.CouponBean;
import tw.eeit175groupone.finalproject.domain.CouponDetail;
import tw.eeit175groupone.finalproject.domain.UserBean;
import tw.eeit175groupone.finalproject.service.CouponService;

@RestController
@RequestMapping("/coupon")

public class CouponController {
    
    @Autowired
    private CouponService couponService;

    //顯示使用者自己未使用的優惠券
    @GetMapping("/findById")
    public ResponseEntity<?> findById(HttpSession session) {
        if (session != null) {
            UserBean attribute = (UserBean) session.getAttribute("user");
            if (attribute != null) {
            Integer userId = attribute.getId();
            if (userId != null) {
                List<CouponBean> data = couponService.findByUsedAndUserId("unused",userId);
                return ResponseEntity.ok().body(data);
                }
            }
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/createCoupon")             
    public ResponseEntity<?> createdNewCoupon(@RequestBody String body) {

        boolean isAdded = couponService.addCoupon(body);
                JSONObject response = new JSONObject();
                if (isAdded) {
                    response.put("success", true);
                    response.put("text", "成功");
                    return ResponseEntity.ok().body(response.toString());
                } else {
                    response.put("success", false);
                    response.put("text", "失敗");
                    return ResponseEntity.ok().body(response.toString());
                }
            }

            // @GetMapping("/findall")
            // public List<CouponBean> findAll(){
                
            //     return couponService.findAll();
            //     }

            @GetMapping("/findall")
            public ResponseEntity<?> findAll() {
                
                    // if (userId != null) {
                        List<CouponBean> data = couponService.findAll();
                        return ResponseEntity.ok().body(data);
                        // }
                    
                
                // return ResponseEntity.notFound().build();
            }


            @DeleteMapping("/delete/{couponId}")
            public ResponseEntity<?> deleteCouponByCouponId(@PathVariable Integer couponId){
                JSONObject response= new JSONObject();
                if(couponService.deleteCouponByCouponId(couponId)){
                    // System.err.println("666666");
                    response.put("success", true);
                    response.put("text", "已刪除coupon");
                    return ResponseEntity.ok().body(response.toString());    
                } else {
                    response.put("success",false);
                    response.put("text", "沒辦法刪喔");
                    return ResponseEntity.ok(response.toString());
                }
                
            }




        // System.err.println("折價卷="+couponData);
    //     if (couponData != null) {
            
    //         CouponBean coupon=couponService.addCoupon(couponData);
    //         if(coupon!=null){
    //             return ResponseEntity.ok().body("success");
    //         }
    //     }
    //     return ResponseEntity.notFound().build();
    // }
}


