package tw.eeit175groupone.finalproject.service;


import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.eeit175groupone.finalproject.dao.ProductImageRepository;
import tw.eeit175groupone.finalproject.dao.ProductRepository;
import tw.eeit175groupone.finalproject.domain.ProductImageBean;

@Service
@Transactional
public class ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ProductRepository productRepository;

    public boolean insertProductImage(String json){
        JSONObject obj = new JSONObject(json);
        String productImage = obj.isNull("productImage")?null:obj.getString("productImage");
        Integer imageGetFirst = obj.isNull("imageGetFirst")?null:obj.getInt("imageGetFirst");
        // Integer productId = obj.isNull("productId")?null:obj.getInt("productId");

        ProductImageBean bean = new ProductImageBean();
        bean.setImageType("productimg");
        bean.setProductImage(productImage);
        bean.setImageGetFirst(imageGetFirst);
        bean.setProductId(productRepository.lastProductId());
        System.err.println("bean = "+bean);
        if(productImage != null){
            System.err.println("ssssssssssssssssssssss");
            productImageRepository.save(bean);
            return true;
        }
        return false;
        
    }
    public List<ProductImageBean> findFirstProductImage(Integer productId){
        List<ProductImageBean> image = productImageRepository.findFirstProductImagesByProductId(productId);
        if(image.size()!=0 || !image.isEmpty()){
            return image;
        } 
        return null;
    }
    

}
