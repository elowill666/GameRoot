package tw.eeit175groupone.finalproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.eeit175groupone.finalproject.domain.OrderdetailsBean;

import java.util.List;


@Repository
public interface OrderdetailsRepository extends JpaRepository<OrderdetailsBean, Integer>, OrderdetailsDAO{
    @Query("SELECT o FROM OrderdetailsBean o WHERE orderId=:orderId")
    List<OrderdetailsBean> findByOrderId(@Param("orderId") Integer orderId);


    @Modifying
    @Query("update OrderdetailsBean set productCommentStatus = :productCommentStatus , productCommentId = :productCommentId where orderdetailId = :orderdetailId")
    void updateProductCommentInOrderDetail(String productCommentStatus,Integer productCommentId,Integer orderdetailId);

    List<OrderdetailsBean> findAllByOrderIdIn(List<Integer> orderIds);

    @Query("SELECT orderId From OrderdetailsBean WHERE refundStatus=:refundStatus GROUP BY orderId")
    List<Integer> findOrderIdByRefundStatus(@Param("refundStatus") String refundStatus);
    @Query("SELECT orderId From OrderdetailsBean WHERE refundStatus IS NOT NULL GROUP BY orderId")
    List<Integer> findOrderIdByAllRefundStatus();



}
