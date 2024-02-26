package tw.eeit175groupone.finalproject.dao;

import org.springframework.data.jpa.domain.Specification;
import tw.eeit175groupone.finalproject.domain.OrdersBean;

import java.util.Date;
import java.util.List;

public class OrderSpecifications{

    /**
     * WHERE userId=?
     * @param userId
     * @return
     */
    public static Specification<OrdersBean> hasUserId(Integer userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId != null) {
                return criteriaBuilder.equal(root.get("userId"), userId);
            }
            return null;
        };
    }
    
    
    
    /**
     * WHERE orderId=?
     * @param orderId
     * @return
     */
    public static Specification<OrdersBean> hasOrderId(Integer orderId) {
        return (root,query,criteriaBuilder) ->{
            if(orderId!=null){
                return criteriaBuilder.equal(root.get("orderId"),orderId);
            }
            return null;
        };
    }


    public static Specification<OrdersBean> hasOrderIds(List<Integer> orderIds) {
        return (root,query,criteriaBuilder) ->{
            if(orderIds!=null&& !orderIds.isEmpty()){
                return root.get("orderId").in(orderIds);
            }
            return null;
        };
    }
    /**
     * WHERE orderStatus=?
     * @param orderStatus
     * @return
     */
    public static Specification<OrdersBean> hasOrderStatus(String orderStatus) {
        return (root,query,criteriaBuilder) ->{
            if(orderStatus!=null&&orderStatus.length()!=0){
                return criteriaBuilder.equal(root.get("orderStatus"),orderStatus);
            }
            return null;
        };
    }

    /**
     * WHERE paymentStatus=?
     * @param paymentStatus
     * @return
     */
    public static Specification<OrdersBean> hasPaymentStatus(Integer paymentStatus) {
        return (root,query,criteriaBuilder) ->{
            if(paymentStatus!=null){
                return criteriaBuilder.equal(root.get("paymentStatus"),paymentStatus);
            }
            return null;
        };
    }
    
    /**
     * 起始日期
     * WHERE orderDate>=?
     * @param orderDate
     * @return
     */
    public static Specification<OrdersBean> hasStartOrderDate(Date orderDate) {
        return (root,query,criteriaBuilder) ->{
            if(orderDate!=null){
                return criteriaBuilder.greaterThanOrEqualTo(root.get("orderDate"),orderDate);
            }
            return null;
        };
    }
    /**
     * 結束日期
     * WHERE orderDate<=?
     * @param orderDate
     * @return
     */
    public static Specification<OrdersBean> hasEndOrderDate(Date orderDate) {
        return (root,query,criteriaBuilder) ->{
            if(orderDate!=null){
                return criteriaBuilder.lessThanOrEqualTo(root.get("orderDate"),orderDate);
            }
            return null;
        };
    }

    /**
     * 付款方式
     * WHERE paymentMethod=?
     * @param paymentMethod
     * @return
     */
    public static Specification<OrdersBean> hasPaymentMethod(String paymentMethod) {
        return (root,query,criteriaBuilder) ->{
            if(paymentMethod!=null){
                return criteriaBuilder.equal(root.get("paymentMethod"),paymentMethod);
            }
            return null;
        };
    }

    /**
     *訂單總額
     * WHERE totalAmountMin=?
     * @param totalAmountMin
     * @return
     */
    public static Specification<OrdersBean> hasTotalAmountMin(Integer totalAmountMin) {
        return (root,query,criteriaBuilder) ->{
            if(totalAmountMin!=null){
                return criteriaBuilder.greaterThanOrEqualTo(root.get("totalAmount"),totalAmountMin);
            }
            return null;
        };
    }

    /**
     *訂單總額
     * WHERE totalAmountMax=?
     * @param totalAmountMax
     * @return
     */
    public static Specification<OrdersBean> hasTotalAmountMax(Integer totalAmountMax) {
        return (root,query,criteriaBuilder) ->{
            if(totalAmountMax!=null){
                return criteriaBuilder.lessThanOrEqualTo(root.get("totalAmount"),totalAmountMax);
            }
            return null;
        };
    }
    
    
}

