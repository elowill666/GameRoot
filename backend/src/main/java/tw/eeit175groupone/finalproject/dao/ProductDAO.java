package tw.eeit175groupone.finalproject.dao;


import java.util.List;

import org.json.JSONObject;

import tw.eeit175groupone.finalproject.domain.ProductBean;

public interface ProductDAO {
    //找出特定條件產品的總數的抽象方法
    public abstract long countProduct(JSONObject obj);
    //找出特定條件產品的抽象方法
    public abstract List<ProductBean> findProduct(JSONObject obj);

    
}