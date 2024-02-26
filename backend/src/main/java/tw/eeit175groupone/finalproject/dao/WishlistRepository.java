package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.eeit175groupone.finalproject.domain.CouponBean;
import tw.eeit175groupone.finalproject.domain.WishlistBean;
import tw.eeit175groupone.finalproject.dto.WishlistDTO;




@Repository
public interface WishlistRepository extends JpaRepository<WishlistBean, Integer>{
	
	@Query("select w.productId from WishlistBean w where w.userId = :userId")
    List<Integer[]> findProductByUserIdInWishlist(@Param("userId")Integer userId);

    @Query("select w from WishlistBean w where w.productId = :productId and w.userId = :userId")
    List<WishlistBean> confirmByProductIdInUserWishlist(@Param("productId")Integer productId,@Param("userId")Integer userId);

    @Modifying
    @Query("Delete from WishlistBean w where w.productId = :productId and w.userId = :userId")
    void deleteByProductIdInUserWishlist(@Param("productId")Integer productId,@Param("userId")Integer userId);

    
    @Query("SELECT w,p FROM WishlistBean w\n\r"
    +"JOIN ProductBean p\n\r"
    +"ON w.productId = p.productId\n\r"
    +"WHERE w.userId = :userId")
    List<Object[]> findWishlist(@Param("userId")Integer userId);
}
