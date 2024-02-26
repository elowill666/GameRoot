package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.eeit175groupone.finalproject.domain.OrdersBean;

@Repository
public interface UserDashboardRepository extends JpaRepository<OrdersBean, Integer> {

    // 查進行中或完成的訂單
    Integer countByOrderStatusAndUserId(String orderStatus, Integer userId);

    // 查使用者發文數
    @Query("SELECT COUNT(a) FROM ArticlesBean a WHERE a.userId = :userId")
    Integer countUserArticles(@Param(value = "userId") Integer userId);

    // 查使用者留言數
    @Query(value = "SELECT count(*) FROM comments WHERE user_id=:userId", nativeQuery = true)
    Integer countUserComments(@Param(value = "userId") Integer userId);

    // 查詢使用者訂單總數與消費總金額
    @Query(value = "SELECT  COUNT(DISTINCT o.order_id), COALESCE(SUM(o.total_amount), 0) FROM  orders o WHERE user_id = :userId", nativeQuery = true)
    List<Object[]> getUserOrderCountsandTotalAmounts(@Param(value = "userId") Integer userId);

    @Modifying
    @Query(value = "UPDATE user_detail SET membership = :membership,permissions =:permissions,bantimecount= bantimecount + :bantimecount  WHERE user_id=:userId", nativeQuery = true)
    Integer updateMembershipAndPermissions(String membership, Integer permissions, Integer bantimecount,
            Integer userId);
    
    
    
}