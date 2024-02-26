package tw.eeit175groupone.finalproject.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.json.JSONObject;


import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import tw.eeit175groupone.finalproject.domain.ProductBean;

public class ProductDAOImpl implements ProductDAO{
    @PersistenceContext
    private Session session;

    public Session getSession(){
        return this.session;
    }
    /**
     * 實作找出特定條件產品的總數的抽象方法
     */
    @Override
    public long countProduct(JSONObject obj) {
        //select * from product 
        //where product_name(keyword) like '%?%' and product_type = ? and product_subtype = ? and minprice <= ? and maxprice >= ?

        String keyword = obj.isNull("keyword")?null:obj.getString("keyword");
        String productType = obj.isNull("productType")?null:obj.getString("productType");
        String productSubtype = obj.isNull("productSubtype")?null:obj.getString("productSubtype");
        String maxPrice = obj.isNull("maxPrice")?null:obj.getString("maxPrice");
        String minPrice = obj.isNull("minPrice")?null:obj.getString("minPrice");

        System.out.println("keyword = "+keyword+124);
        System.out.println(keyword.isEmpty());

        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        //from product
        Root<ProductBean> root = query.from(ProductBean.class);

        //select count(*)
        query = query.select(builder.count(root));

        //裝多個條件查詢語句(and、or)
        List<Predicate> predicates = new ArrayList<>();

        // product_name(keyword) like '%?%'
        if(keyword != null && !keyword.isEmpty()){
                predicates.add(builder.like(root.get("productName"),"%"+keyword+"%"));
        }
        //and product_type = ?
        if(productType != null && !productType.isEmpty()){
            predicates.add(builder.equal(root.get("productType"), productType));
        }
        //and product_subtype = ?
        if(productSubtype != null && !productSubtype.isEmpty()){
            predicates.add(builder.equal(root.get("productSubtype"), productSubtype));
        }
        //and minprice <= ?
        if(minPrice != null){
            if(minPrice.length()==0){
                predicates.add(builder.greaterThanOrEqualTo(root.get("price"), 0));

            } else {
                predicates.add(builder.greaterThanOrEqualTo(root.get("price"), Integer.parseInt(minPrice)));
            }
        }
        //and maxprice <= ?
        if(maxPrice != null){
            if(maxPrice.length()==0){
                predicates.add(builder.lessThanOrEqualTo(root.get("price"), 10000000));

            } else {
                predicates.add(builder.lessThanOrEqualTo(root.get("price"), Integer.parseInt(maxPrice)));
            }
        }

        //and status != 'normal'
        predicates.add(builder.notEqual(root.get("productStatus"), "normal"));

        //建構查詢語句
        if(predicates!=null && !predicates.isEmpty()){
            query = query.where(builder.and(predicates.toArray(new Predicate[0])));
        }


        //執行查詢
        TypedQuery<Long> typedQuery = this.session.createQuery(query);

        return typedQuery.getSingleResult();
        
    }
    /**
     * 實作找出特定條件產品的抽象方法
     */
    @Override
    public List<ProductBean> findProduct(JSONObject obj) {
        //select * from product 
        //where product_name(keyword) like '%?%' and product_type = ? and product_subtype = ? and minprice <= ? and maxprice >= ?

        String keyword = obj.isNull("keyword")?null:obj.getString("keyword");
        String productType = obj.isNull("productType")?null:obj.getString("productType");
        String productSubtype = obj.isNull("productSubtype")?null:obj.getString("productSubtype");
        Integer maxPrice = obj.isNull("maxPrice")?null:obj.getInt("maxPrice");
        Integer minPrice = obj.isNull("minPrice")?null:obj.getInt("minPrice");

        System.out.println("keyword = "+keyword+124);
        System.out.println(keyword.isEmpty());

        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<ProductBean> query = builder.createQuery(ProductBean.class);

        //from product
        Root<ProductBean> root = query.from(ProductBean.class);

        //裝多個條件查詢語句(and、or)
        List<Predicate> predicates = new ArrayList<>();

        // product_name(keyword) like '%?%'
        if(keyword != null && !keyword.isEmpty()){
                predicates.add(builder.like(root.get("productName"),"%"+keyword+"%"));
        }
        //and product_type = ?
        if(productType != null && !productType.isEmpty()){
            predicates.add(builder.equal(root.get("productType"), productType));
        }
        //and product_subtype = ?
        if(productSubtype != null && !productSubtype.isEmpty()){
            predicates.add(builder.equal(root.get("productSubtype"), productSubtype));
        }
        //and minprice <= ?
        if(minPrice != null){
            predicates.add(builder.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        //and maxprice <= ?
        if(maxPrice != null){
            predicates.add(builder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        //and status != 'normal'
        predicates.add(builder.notEqual(root.get("productStatus"), "normal"));

        //建構查詢語句
        if(predicates!=null && !predicates.isEmpty()){
            query = query.where(builder.and(predicates.toArray(new Predicate[0])));
        }


        //執行查詢
        TypedQuery<ProductBean> typedQuery = this.session.createQuery(query);

        return typedQuery.getResultList();
    }
    
}
