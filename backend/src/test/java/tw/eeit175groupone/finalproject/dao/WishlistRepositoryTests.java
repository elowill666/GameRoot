package tw.eeit175groupone.finalproject.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tw.eeit175groupone.finalproject.domain.CommentsBean;
import tw.eeit175groupone.finalproject.domain.ProductBean;
import tw.eeit175groupone.finalproject.domain.WishlistBean;
import tw.eeit175groupone.finalproject.dto.WishlistDTO;


@SpringBootTest
public class WishlistRepositoryTests {
    @Autowired
    private WishlistRepository wishlistRepository;

    // @Test
    // public void testfindAllWishlist(){
		// List<WishlistBean> wishlistBean=wishlistRepository.findAllWishlist(1);
		// System.err.println(wishlistBean);
		// 	}
			
// @Test
// 	public void testfindByUserId(){
//     List<Object[]> findByUserId = wishlistRepository.findWishlist(1);
//     for(Object[] temp : findByUserId){
//             WishlistDTO dto=new WishlistDTO();
//             BeanUtils.copyProperties((WishlistBean) temp[0],dto);
//             BeanUtils.copyProperties((ProductBean) temp[1],dto);
//             System.err.println("dto="+dto);
//         }
        
    // }


  @Test
    public void testfindWishlist(){
      List<Object[]> result = wishlistRepository.findWishlist(5);
      List<WishlistDTO> wish=new ArrayList();
      for (Object[] temp : result) {
        WishlistDTO dto=new WishlistDTO();
            BeanUtils.copyProperties((WishlistBean) temp[0],dto);
            BeanUtils.copyProperties((ProductBean) temp[1],dto);
        // System.err.println("WishlistId: " + dto);
        wish.add(dto);
        
      }
      System.err.println("WishlistId: " + wish);
    }

// @Test
//     public void testfindWishlist() {
//         Integer userId = 1;

//         List<Object[]> result = wishlistRepository.findWishlist(userId);
//         ArrayList<WishlistDTO> array = new ArrayList<WishlistDTO>();
//         for (Object[] row : result) {
//             System.out.println(row[0]);
//             WishlistDTO WishlistDTO = new WishlistDTO();
//             WishlistBean wishlistBean = (WishlistBean) row[0];
//             ProductBean productBean = (ProductBean) row[1];
//             BeanUtils.copyProperties(wishlistBean, WishlistDTO);
//             BeanUtils.copyProperties(productBean, WishlistDTO);
//             array.add(WishlistDTO);
//         }
//         System.err.println(array);
//     }
  // @Test
  //   public void testFindCommentsByArticlesId() {
  //       List<WishlistBean> result = wishlistRepository.findWishlist(1);
  //       for (WishlistBean Wishlist : result) {
  //           System.err.println("Wishlist = " + Wishlist);
  //       }
  //   }



}

