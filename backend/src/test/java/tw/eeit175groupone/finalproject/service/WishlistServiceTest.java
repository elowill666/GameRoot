package tw.eeit175groupone.finalproject.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tw.eeit175groupone.finalproject.domain.CouponBean;
import tw.eeit175groupone.finalproject.domain.ProductBean;
import tw.eeit175groupone.finalproject.domain.WishlistBean;
import tw.eeit175groupone.finalproject.dto.WishlistDTO;

@SpringBootTest
public class WishlistServiceTest {
   
    @Autowired
    private WishlistService wishlistService;

    // @Test
    //     public void testfindById(){
    //         List<Object[]> wushlist=wishlistService.findByUserId( 2);
    //         for(Object[] temp : wushlist){
    //         WishlistDTO dto=new WishlistDTO();
    //         BeanUtils.copyProperties((WishlistBean) temp[0],dto);
    //         BeanUtils.copyProperties((ProductBean) temp[1],dto);
    //         System.err.println("dto="+dto);
    //     }
    //     }

//    @Test
//        public void testfindById(){
//            List<Object[]> wishlist=wishlistService.findByUserId(2);
//            for (Object[] temp : wishlist) {
//                Integer wishlistId = (Integer) temp[0];
//                Integer userId = (Integer) temp[1];
//                String productName = (String) temp[2];
//                Integer price = (Integer) temp[3];
//        
//                System.err.println("WishlistId: " + wishlistId);
//                System.err.println("UserId: " + userId);
//                System.err.println("ProductName: " + productName);
//                System.err.println("Price: " + price);
//              }
//            }
            
        }
