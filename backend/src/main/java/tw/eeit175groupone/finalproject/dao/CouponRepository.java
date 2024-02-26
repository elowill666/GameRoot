package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.eeit175groupone.finalproject.domain.CouponBean;
import tw.eeit175groupone.finalproject.domain.OrdersBean;



@Repository
public interface CouponRepository extends JpaRepository<CouponBean, Integer>  {
   
    List<CouponBean> findByUsedAndUserId(String used, Integer userId);

    List<CouponBean> findAll();

    @Modifying
    @Query("Delete from CouponBean c where c.couponId = :couponId")
    void deleteCouponByCouponId(@Param("couponId")Integer couponId);

    // List<CouponBean> findByUserIdAndFindByUsed(Integer userId, String used); 
    
}
