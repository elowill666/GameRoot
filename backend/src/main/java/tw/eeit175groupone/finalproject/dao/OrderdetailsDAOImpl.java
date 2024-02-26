package tw.eeit175groupone.finalproject.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tw.eeit175groupone.finalproject.domain.OrderdetailsBean;
import tw.eeit175groupone.finalproject.domain.OrdersBean;
import tw.eeit175groupone.finalproject.domain.ProductBean;
import tw.eeit175groupone.finalproject.util.DatetimeConverter;

import javax.swing.text.html.parser.Entity;
import java.util.*;

public class OrderdetailsDAOImpl implements OrderdetailsDAO{

    @Autowired
    private EntityManager entityManager;

    /**
     * 算開站至今總營收可以依據填入選項找出
     * 只有日期或沒參數找出全品項總營收，
     * 帶入productType產品類別就是類別總營收
     * 帶入productSubtype產品子類別就是子類別總營收
     *
     * @param request--date日期，productType產品類別，productSubtype產品子類別，productName產品名字 此方法只會用到productType產品類別，productSubtype產品子類別
     * @return 總營收--Integer
     */
    public Integer findsumRevenue(Map<String, String> request){
        //如果不為null加where
        String productType=request.getOrDefault("productType",null);
        //如果不為null where
        String productSubtype=request.getOrDefault("productSubtype",null);


        //這裡沒差
        String productName=request.getOrDefault("productName",null);

        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery=criteriaBuilder.createQuery(Integer.class);

        Root<OrderdetailsBean> root=criteriaQuery.from(OrderdetailsBean.class);
        Root<ProductBean> productRoot=criteriaQuery.from(ProductBean.class);
        Root<OrdersBean> ordersBeanRoot=criteriaQuery.from(OrdersBean.class);

        List<Predicate> predicates=new ArrayList<>();

        // Join productId
        predicates.add(criteriaBuilder.and(
                criteriaBuilder.equal(root.get("productId"),productRoot.get("productId")),
                criteriaBuilder.equal(root.get("orderId"),ordersBeanRoot.get("orderId"))
        ));

        if(productType!=null && productType.length()!=0){
            predicates.add(criteriaBuilder.equal(productRoot.get("productType"),productType));
        }

        if(productSubtype!=null && productSubtype.length()!=0){
            predicates.add(criteriaBuilder.equal(productRoot.get("productSubtype"),productSubtype));
        }
//        criteriaBuilder.notEqual(root.get("productType"),"CASH");
        if(predicates!=null && !predicates.isEmpty()){
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }
        criteriaQuery.multiselect(
                criteriaBuilder.coalesce(
                        criteriaBuilder.sum(root.get("historyPrice"))
                        ,criteriaBuilder.literal(0)));
//        criteriaQuery.groupBy(productRoot.get("productType"));

        TypedQuery<Integer> query=entityManager.createQuery(criteriaQuery);
        Integer resultList=query.getSingleResult();
        if(resultList!=null){
            return resultList;
        }
        return null;
    }

    //    @Query("SELECT Month(orderDate),SUM(historyPrice)\r\n"+
//            "FROM OrdersBean o\r\n" +
//            "JOIN OrderdetailsBean od ON o.orderId=od.orderId\r\n" +
//            "JOIN ProductBean p ON p.productId=od.productId\r\n"+
//            "WHERE YEAR(orderDate)=:year\r\n"+
//            "GROUP BY Month(orderDate)")
//    List<Object[]> sumMonthlyRevenue(@Param("year") Integer year); 
//
    public List<Object[]> findsumMonthlyRevenue(Map<String, String> request,Integer requestYear){
        
        //如果不為null加where
        String productType=request.getOrDefault("productType",null);
        //如果不為null where
        String productSubtype=request.getOrDefault("productSubtype",null);
        //如果不為null where
        String productName=request.getOrDefault("productName",null);

        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query=criteriaBuilder.createQuery(Object[].class);

        Root<OrderdetailsBean> orderdetailsBeanRoot=query.from(OrderdetailsBean.class);
        Root<ProductBean> productBeanRoot=query.from(ProductBean.class);
        Root<OrdersBean> ordersBeanRoot=query.from(OrdersBean.class);

        List<Predicate> predicates=new ArrayList<>();

        predicates.add(criteriaBuilder.and(
                criteriaBuilder.equal(
                        orderdetailsBeanRoot.get("orderId"),ordersBeanRoot.get("orderId")),
                criteriaBuilder.equal(
                        orderdetailsBeanRoot.get("productId"),productBeanRoot.get("productId"))));

        if(requestYear!=null){
            predicates.add(
                    criteriaBuilder.equal(
                            criteriaBuilder.function("YEAR",Integer.class,ordersBeanRoot.get("orderDate")),requestYear));
        }

        if(productType!=null && productType.length()!=0){
            predicates.add(criteriaBuilder.equal(productBeanRoot.get("productType"),productType));
        }

        if(productSubtype!=null && productSubtype.length()!=0){
            predicates.add(criteriaBuilder.equal(productBeanRoot.get("productSubtype"),productSubtype));
        }

        if(predicates!=null || predicates.isEmpty()){
            query.where(predicates.toArray(new Predicate[0]));
        }

        query.multiselect(
                criteriaBuilder.function("MONTH",Integer.class,ordersBeanRoot.get("orderDate")),
                criteriaBuilder.coalesce(criteriaBuilder.sum(orderdetailsBeanRoot.get("historyPrice")),
                        criteriaBuilder.literal(0)));

        query.groupBy(criteriaBuilder.function("MONTH",Integer.class,ordersBeanRoot.get("orderDate")));


        TypedQuery<Object[]> typedQuery=entityManager.createQuery(query);
        List<Object[]> resultList=typedQuery.getResultList();
        if(resultList!=null){
            return resultList;
        }
        return null;
    }


    //    @Query("SELECT DAY(orderDate),SUM(historyPrice)\r\n"+
//            "FROM OrdersBean o\r\n" +
//            "JOIN OrderdetailsBean od ON o.orderId=od.orderId\n"+
//            "WHERE YEAR(orderDate)=:year AND MONTH(orderDate)=:month\r\n"+
//            "GROUP BY DAY(orderDate)")
//    List<Object[]> sumDailyRevenue(@Param("year") Integer year, @Param("month") Integer month);

    /**
     * 算開站帶入日期的當月每日可以依據填入選項找出
     * 只有日期或沒參數找出日期的當月每日營收，
     * 帶入productType產品類別就是類別當月每日營收
     * 帶入productSubtype產品子類別就是子類別當月每日營收
     * 帶入productName產品名字就是產品當月每日營收
     *
     * @param request--date日期，productType產品類別，productSubtype產品子類別，productName產品名字 此方法只會用到productType產品類別，productSubtype產品子類別
     * @return 當月每日營收--List<Object[]>--Object[0]為日，Object[1]為日營收
     */
    public List<Object[]> findsumDailyRevenue(Map<String, String> request,Integer requestYear,Integer requestMonth){
        //如果不為null加where
        String productType=request.getOrDefault("productType",null);
        //如果不為null where
        String productSubtype=request.getOrDefault("productSubtype",null);
   

        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query=criteriaBuilder.createQuery(Object[].class);

        Root<OrderdetailsBean> orderdetailsBeanRoot=query.from(OrderdetailsBean.class);
        Root<ProductBean> productRoot=query.from(ProductBean.class);
        Root<OrdersBean> ordersBeanRoot=query.from(OrdersBean.class);
        List<Predicate> predicates=new ArrayList<>();

        predicates.add(criteriaBuilder.and(
                criteriaBuilder.equal(orderdetailsBeanRoot.get("productId"),productRoot.get("productId")),
                criteriaBuilder.equal(orderdetailsBeanRoot.get("orderId"),ordersBeanRoot.get("orderId"))
        ));

        if(requestYear!=null ){
            predicates.add(criteriaBuilder.equal(
                    criteriaBuilder.function("YEAR",Integer.class,ordersBeanRoot.get("orderDate")),requestYear));
        } 
        if(requestMonth!=null){
            predicates.add(criteriaBuilder.equal(
                    criteriaBuilder.function("MONTH",Integer.class,ordersBeanRoot.get("orderDate")),requestMonth));
        }
       

        if(productType!=null && productType.length()!=0){
            predicates.add(criteriaBuilder.equal(productRoot.get("productType"),productType));
        }

        if(productSubtype!=null && productSubtype.length()!=0){
            predicates.add(criteriaBuilder.equal(productRoot.get("productSubtype"),productSubtype));
        }

        if(!predicates.isEmpty() && predicates!=null){
            query.where(predicates.toArray(new Predicate[0]));
        }

        query.multiselect(
                criteriaBuilder.function("DAY",Integer.class,ordersBeanRoot.get("orderDate")),
                criteriaBuilder.coalesce(
                        criteriaBuilder.sum(orderdetailsBeanRoot.get("historyPrice"))
                        ,criteriaBuilder.literal(0)));

        query.groupBy(criteriaBuilder.function("DAY",Integer.class,ordersBeanRoot.get("orderDate")));

        TypedQuery<Object[]> typedQuery=entityManager.createQuery(query);
        List<Object[]> resultList=typedQuery.getResultList();
        if(resultList!=null){
            return resultList;
        }

        return null;
    }

    //    @Query("SELECT productType,SUM(historyPrice)\r\n"+
//            "FROM OrdersBean o\r\n"+
//            "JOIN OrderdetailsBean od ON o.orderId=od.orderId\r\n"+
//            "JOIN ProductBean p ON p.productId=od.productId\r\n"+
//            "GROUP BY productType")
//    List<Object[]> sumRevenueGroupByProductType();
    public List<Object[]> findsumRevenueGroupByProductType(Map<String, String> request){

        String productType=request.getOrDefault("productType",null);

        String productSubtype=request.getOrDefault("productSubtype",null);

        String productName=request.getOrDefault("productName",null);


        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query=criteriaBuilder.createQuery(Object[].class);

        Root<OrderdetailsBean> orderdetailsBeanRoot=query.from(OrderdetailsBean.class);
        Root<ProductBean> productRoot=query.from(ProductBean.class);
        Root<OrdersBean> ordersBeanRoot=query.from(OrdersBean.class);
        List<Predicate> predicates=new ArrayList<>();

        Path<Object> productGroupBy=productRoot.get("productType");


        predicates.add(criteriaBuilder.and(
                criteriaBuilder.equal(orderdetailsBeanRoot.get("productId"),productRoot.get("productId")),
                criteriaBuilder.equal(orderdetailsBeanRoot.get("orderId"),ordersBeanRoot.get("orderId"))
        ));

        if(productType!=null && productType.length()!=0){
            predicates.add(criteriaBuilder.equal(productRoot.get("productType"),productType));
            productGroupBy=productRoot.get("productSubtype");
        }

        if(productSubtype!=null && productSubtype.length()!=0){
            predicates.add(criteriaBuilder.equal(productRoot.get("productSubtype"),productSubtype));
            productGroupBy=productRoot.get("productName");
        }

        if(productName!=null && productName.length()!=0){
            predicates.add(criteriaBuilder.equal(productRoot.get("productName"),productName));
        }

        if(predicates!=null && !predicates.isEmpty()){
            query.where(predicates.toArray(new Predicate[0]));
        }

        query.groupBy(productGroupBy);

        query.multiselect(productGroupBy,
                criteriaBuilder.sum(orderdetailsBeanRoot.get("historyPrice")));

        TypedQuery<Object[]> typedQuery=entityManager.createQuery(query);
        List<Object[]> resultList=typedQuery.getResultList();

        return resultList;
    }

    //    @Query("SELECT productType,Month(orderDate), SUM(historyPrice)\r\n"+
//            "FROM OrdersBean o\r\n"+
//            "JOIN OrderdetailsBean od ON o.orderId=od.orderId\r\n"+
//            "JOIN ProductBean p ON p.productId=od.productId\r\n"+
//            "WHERE YEAR(orderDate)=:year\r\n"+
//            "GROUP BY p.productType,Month(orderDate)")
//    List<Object[]> sumMonthlyRevenueGroupByProductType(@Param("year") Integer year);
    public List<Object[]> findsumMonthlyRevenueGroupByProductType(Map<String, String> request,Integer requestYear){
     
        String productType=request.getOrDefault("productType",null);
        String productSubtype=request.getOrDefault("productSubtype",null);
        String productName=request.getOrDefault("productName",null);


        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query=criteriaBuilder.createQuery(Object[].class);

        Root<OrderdetailsBean> orderdetailsBeanRoot=query.from(OrderdetailsBean.class);
        Root<ProductBean> productRoot=query.from(ProductBean.class);
        Root<OrdersBean> ordersBeanRoot=query.from(OrdersBean.class);
        List<Predicate> predicates=new ArrayList<>();

        Path<Object> productGroupBy=productRoot.get("productType");

        predicates.add(
                criteriaBuilder.and(
                        criteriaBuilder.equal(orderdetailsBeanRoot.get("productId"),productRoot.get("productId")),
                        criteriaBuilder.equal(orderdetailsBeanRoot.get("orderId"),ordersBeanRoot.get("orderId"))));

        if(requestYear!=null ){
            predicates.add(criteriaBuilder.equal(
                    criteriaBuilder.function("YEAR",Integer.class,ordersBeanRoot.get("orderDate")),requestYear));
        } 
        
        if(productType!=null && productType.length()!=0){
            predicates.add(criteriaBuilder.equal(productRoot.get("productType"),productType));
            productGroupBy=productRoot.get("productSubtype");
        }


        if(productSubtype!=null && productSubtype.length()!=0){
            predicates.add(criteriaBuilder.equal(productRoot.get("productSubtype"),productSubtype));
            productGroupBy=productRoot.get("productName");
        }

        if(productName!=null && productName.length()!=0){
            predicates.add(criteriaBuilder.equal(productRoot.get("productName"),productName));
        }

        if(predicates!=null && !predicates.isEmpty()){
            query.where(predicates.toArray(new Predicate[0]));
        }

        query.groupBy(productGroupBy,criteriaBuilder.function("MONTH",Integer.class,ordersBeanRoot.get("orderDate")));

        query.multiselect(productGroupBy,
                criteriaBuilder.function("MONTH",Integer.class,ordersBeanRoot.get("orderDate")),
                criteriaBuilder.sum(orderdetailsBeanRoot.get("historyPrice")));


        TypedQuery<Object[]> typedQuery=entityManager.createQuery(query);
        List<Object[]> resultList=typedQuery.getResultList();


        return resultList;
    }

//    @Query("SELECT productType,DAY(orderDate), SUM(historyPrice)\r\n"+
//            "FROM OrdersBean o\r\n"+
//            "JOIN OrderdetailsBean od ON o.orderId=od.orderId\r\n"+
//            "JOIN ProductBean p ON p.productId=od.productId\r\n"+
//            "WHERE YEAR(orderDate)=:year AND Month(orderDate)=:month\r\n"+
//            "GROUP BY p.productType,DAY(orderDate)")
//    List<Object[]> sumDailyRevenueGroupByProductType(@Param("year") Integer year,@Param("month") Integer month);

    public List<Object[]> findsumDailyRevenueGroupByProductType(Map<String, String> request,Integer requestYear,Integer requestMonth){
        String productType=request.getOrDefault("productType",null);
        String productSubtype=request.getOrDefault("productSubtype",null);
        String productName=request.getOrDefault("productName",null);


        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query=criteriaBuilder.createQuery(Object[].class);

        Root<OrderdetailsBean> orderdetailsBeanRoot=query.from(OrderdetailsBean.class);
        Root<ProductBean> productRoot=query.from(ProductBean.class);
        Root<OrdersBean> ordersBeanRoot=query.from(OrdersBean.class);
        List<Predicate> predicates=new ArrayList<>();

        Path<Object> productGroupBy=productRoot.get("productType");

        predicates.add(
                criteriaBuilder.and(
                        criteriaBuilder.equal(orderdetailsBeanRoot.get("productId"),productRoot.get("productId")),
                        criteriaBuilder.equal(orderdetailsBeanRoot.get("orderId"),ordersBeanRoot.get("orderId"))));

        if(requestYear!=null){
            predicates.add(criteriaBuilder.equal(
                    criteriaBuilder.function("YEAR",Integer.class,ordersBeanRoot.get("orderDate")),requestYear));
        }
        if(requestMonth!=null){
            predicates.add(criteriaBuilder.equal(
                    criteriaBuilder.function("MONTH",Integer.class,ordersBeanRoot.get("orderDate")),requestMonth));
        }
        

        if(productType!=null && productType.length()!=0){
            predicates.add(criteriaBuilder.equal(productRoot.get("productType"),productType));
            productGroupBy=productRoot.get("productSubtype");
        }


        if(productSubtype!=null && productSubtype.length()!=0){
            predicates.add(criteriaBuilder.equal(productRoot.get("productSubtype"),productSubtype));
            productGroupBy=productRoot.get("productName");
        }

        if(productName!=null && productName.length()!=0){
            predicates.add(criteriaBuilder.equal(productRoot.get("productName"),productName));
        }

        if(predicates!=null && !predicates.isEmpty()){
            query.where(predicates.toArray(new Predicate[0]));
        }

        query.groupBy(productGroupBy,criteriaBuilder.function("DAY",Integer.class,ordersBeanRoot.get("orderDate")));

        query.multiselect(productGroupBy,
                criteriaBuilder.function("DAY",Integer.class,ordersBeanRoot.get("orderDate")),
                criteriaBuilder.sum(orderdetailsBeanRoot.get("historyPrice")));


        TypedQuery<Object[]> typedQuery=entityManager.createQuery(query);
        List<Object[]> resultList=typedQuery.getResultList();


        return resultList;
    }


}
