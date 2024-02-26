package tw.eeit175groupone.finalproject.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tw.eeit175groupone.finalproject.domain.CouponBean;

@SpringBootTest
public class CouponRepositoryTests {
    @Autowired
    private CouponRepository couponRepository;
    
    @Test
	public void testfindAll(){
		List<CouponBean> couponBean=couponRepository.findAll();
		for(CouponBean temp:couponBean){
			System.err.println(temp);
		}
		
	}

    // @Test
	// public void testfindUnusedCoupon(){
	// 	CouponBean unused = couponRepository.findUnusedCoupon("unused");
    //     System.err.println("unused = " + unused);
	// }
    @Test
	public void testfindUnusedCoupon(){
    List<CouponBean> unusedCoupons = couponRepository.findByUsedAndUserId("unused", 1);
        System.err.println(unusedCoupons);
    }

    // @Test
	// public void testdeleteCouponByCouponId(){
    //     void unusedCoupons = couponRepository.deleteCouponByCouponId(768);
    //     System.err.println(unusedCoupons);
    // }

	// @Test
	// public void testfindCouponById(){
    // List<CouponBean> userCoupons = couponRepository.findByUserIdAndFindByUsed(2, "unused");
    //     System.err.println(userCoupons);
    // }
}

