package tw.eeit175groupone.finalproject.dao;

import java.util.List;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import tw.eeit175groupone.finalproject.domain.OrdersBean;


public interface OrderRepository extends JpaRepository<OrdersBean, Integer>{

    /**
     * 取得OrderDTO需要的欄位數值
     *
     * @return Object[]
     */
    @Query("SELECT c.cartmiddleId\r\n"+ //
            ",c.productId\r\n"+ //
            ",c.shoppingcartId\r\n"+ //
            ",c.quantity\r\n"+ //
            ",s.userId\r\n"+ //
            ",p.price\r\n"+ //
            ",p.productName\r\n"+ //
            ",p.productType\r\n"+ //
            ",p.productStatus\r\n"+ //
            ",p.discount\r\n"+ //
            "FROM CartmiddleBean c\r\n"+ //
            "JOIN ShoppingcartBean s\r\n"+ //
            "ON c.shoppingcartId = s.shoppingcartId\r\n"+ //
            "JOIN ProductBean p\r\n"+ //
            "ON c.productId = p.productId\r\n"+ //
            "WHERE s.userId = :userId")
    List<Object[]> findShoppingcartByUserId(@Param("userId") Integer userId);


    /**
     * 以userID找出該user的訂單
     *
     * @param userId
     * @return OrdersBean
     */
    List<OrdersBean> findByUserId(Integer userId);

    /**
     * 從資料庫計算總營收
     *
     * @return Integer--Revenue
     */
    @Query("SELECT SUM(historyPrice)\r\n"+
            "FROM OrdersBean o\r\n"+
            "JOIN OrderdetailsBean od ON o.orderId=od.orderId\r\n"+
            "WHERE o.paymentMethod!='CASH'")
    Integer sumRevenue();

    /**
     * 從資料庫以年做分組，計算每年的總營收
     *
     * @param year--Integer
     * @param month--Integer
     * @return Object[]--AnnualRevenue include [0]Year and [1]Revenue
     */

    @Query("SELECT DAY(orderDate),SUM(historyPrice)\r\n"+
            "FROM OrdersBean o\r\n"+
            "JOIN OrderdetailsBean od ON o.orderId=od.orderId\r\n"+
            "JOIN ProductBean p ON p.productId=od.productId\r\n"+
            "WHERE YEAR(orderDate)=:year AND MONTH(orderDate)=:month\r\n"+
            "GROUP BY DAY(orderDate)")
    List<Object[]> sumDailyRevenue(@Param("year") Integer year,@Param("month") Integer month);

    /**
     * 從資料庫以產品類別分類，計算個產品總營收
     *
     * @return Object[]--[0]ProductType and [1]Revenue
     */
    @Query("SELECT productType,SUM(historyPrice)\r\n"+
            "FROM OrdersBean o\r\n"+
            "JOIN OrderdetailsBean od ON o.orderId=od.orderId\r\n"+
            "JOIN ProductBean p ON p.productId=od.productId\r\n"+
            "GROUP BY productType")
    List<Object[]> sumRevenueGroupByProductType();

    /**
     * 輸入年份，從資料庫以產品類別做分組，計算每類產品的月營收
     *
     * @param year--Integer
     * @return Object[]--RevenueByProductType
     * include [0]ProductType and [1]Month and [2]Revenue
     */
    @Query("SELECT productType,Month(orderDate), SUM(historyPrice)\r\n"+
            "FROM OrdersBean o\r\n"+
            "JOIN OrderdetailsBean od ON o.orderId=od.orderId\r\n"+
            "JOIN ProductBean p ON p.productId=od.productId\r\n"+
            "WHERE YEAR(orderDate)=:year\r\n"+
            "GROUP BY p.productType,Month(orderDate)")
    List<Object[]> sumMonthlyRevenueGroupByProductType(@Param("year") Integer year);

    /**
     * 輸入年份及月份，從資料庫以產品類別做分組，計算每類產品在某年某月的日營收
     *
     * @param year--Integer
     * @param month--Integer
     * @return Object[]--RevenueByProductType
     * include [0]ProductType and [1]date and [2]Revenue
     */
    @Query("SELECT productType,DAY(orderDate), SUM(historyPrice)\r\n"+
            "FROM OrdersBean o\r\n"+
            "JOIN OrderdetailsBean od ON o.orderId=od.orderId\r\n"+
            "JOIN ProductBean p ON p.productId=od.productId\r\n"+
            "WHERE YEAR(orderDate)=:year AND Month(orderDate)=:month\r\n"+
            "GROUP BY p.productType,DAY(orderDate)")
    List<Object[]> sumDailyRevenueGroupByProductType(@Param("year") Integer year,@Param("month") Integer month);

    /**
     * 從資料庫以月做分組，計算某年的每月總營收
     *
     * @return Object[]--AnnualRevenue include [0]Year and [1]Revenue
     */
    @Query("SELECT Month(orderDate),SUM(historyPrice)\r\n"+
            "FROM OrdersBean o\r\n"+
            "JOIN OrderdetailsBean od ON o.orderId=od.orderId\r\n"+
            "JOIN ProductBean p ON p.productId=od.productId\r\n"+
            "WHERE YEAR(orderDate)=:year\r\n"+
            "GROUP BY Month(orderDate)")
    List<Object[]> sumMonthlyRevenue(@Param("year") Integer year);

    // 尋找user的訂單列表
    @Query("SELECT o \r\n"+
            "FROM OrdersBean o\r\n"+
            // "JOIN OrderdetailsBean od ON o.orderId=od.orderId\r\n"+
            "WHERE o.userId = :userId")
    List<OrdersBean> findOrder(@Param("userId") Integer userId);

    @Query("SELECT o,od \r\n"+
            "FROM OrdersBean o\r\n"+
            "JOIN OrderdetailsBean od ON o.orderId=od.orderId\r\n"+
            "WHERE o.orderId = :orderId")
    List<Object[]> findOrderdetail(@Param("orderId") Integer orderId);

    /**
     * 依照前端需求取出資料，
     *
     * @param pageable--PageRequest.of(pagesize,pagenum,sort); 取出資料數而且取出資料如果超出資料庫的資料筆數也沒關係
     * @return OrdersBean
     */
    @Query("SELECT o\r\n"+
            "FROM OrdersBean o\r\n")
    List<OrdersBean> findOrderWithLimit(
            @Param("rowsNumber") Pageable pageable);


    /**
     * 依照前端需求取出資料，
     *
     * @param pageable--PageRequest.of(pagesize,pagenum,sort); 取出資料數而且取出資料如果超出資料庫的資料筆數也沒關係
     * @return List<Object [ ]>--Object[0] OrderBean Object[1] OrderdetailBean
     */
    @Query("SELECT o,od\r\n"+
            "FROM OrdersBean o\r\n"+
            "JOIN OrderdetailsBean od ON o.orderId=od.orderId")
    List<Object[]> findOrderWithOrderdetail(
            @Param("rowsNumber") Pageable pageable);


    // @Query("SELECT o,od \r\n"+
    // "ROW_NUMBER() OVER (PARTITION BY o.order_id ORDER BY od.orderdetail_id) AS
    // RowNum"+
    // "FROM OrdersBean o\r\n"+
    // "JOIN OrderdetailsBean od ON o.orderId=od.orderId\r\n"+
    // "WHERE o.orderId = :orderId and RowNum = 1")
    // List<Object[]> findOrderdetaila(@Param("orderId")Integer orderId);

    /**
     * 以根據userID及排序條件找出該user的訂單並排序
     *
     * @param userId
     * @param pageable--排序條件
     * @return OrdersBean
     */
    List<OrdersBean> findByUserId(Integer userId,Pageable pageable);

    /**
     * 根據userID計算該user的總訂單數
     *
     * @param userId
     * @return Long--user的總訂單數
     */
    Long countByUserId(Integer userId);

    /**
     * 根據條件找資料
     *
     * @param spec--限制的參數=where ??????
     * @param pageable--分頁及排序的參數
     * @return Long--user的總訂單數
     */
    List<OrdersBean> findAll(Specification<OrdersBean> spec,Pageable pageable);

    long count(Specification<OrdersBean> spec);
    
    
    @Query("SELECT userId,COALESCE(SUM(totalAmount),0) FROM OrdersBean\r\n" +
            "WHERE userId IN :userIds\r\n" +
            "GROUP BY userId")
    List<Object[]> sumPersonalComsume(@Param("userIds")List<Integer> userIds);

}
