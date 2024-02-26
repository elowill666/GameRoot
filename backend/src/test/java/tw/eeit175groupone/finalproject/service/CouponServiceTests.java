package tw.eeit175groupone.finalproject.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.minidev.json.JSONObject;
import tw.eeit175groupone.finalproject.dao.CouponRepository;
import tw.eeit175groupone.finalproject.domain.CouponBean;

@SpringBootTest
public class CouponServiceTests {
    
    @Autowired
    private CouponService couponService;
    // @Autowired
    // private CouponRepository couponRepository;

    @Test
        public void testfindAll(){
            List<CouponBean> couponBean=couponService.findAll();
            for(CouponBean temp:couponBean){
                System.err.println(temp);
            }
            
        }

        @Test
        public void testfindById(){
            List<CouponBean> couponBean=couponService.findByUsedAndUserId("unused", 2);
            for(CouponBean temp:couponBean){
                System.err.println(temp);
            }
            
        }


        @Test
        public void testaddCoupon() {
            JSONObject obj = new JSONObject();
            obj.put("info", "測試");
            obj.put("discount", 0.7);
            obj.put("beginDate", "2024-02-15 00:00:00");
            obj.put("endDate", "2024-02-28 00:00:00");
            boolean result = couponService.addCoupon(obj.toString());
            System.err.println(result);
    }
    }

