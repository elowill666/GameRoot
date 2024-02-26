package tw.eeit175groupone.finalproject.service;

import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.eeit175groupone.finalproject.dao.OrderdetailsRepository;
import tw.eeit175groupone.finalproject.dao.ProductCommentRepository;
import tw.eeit175groupone.finalproject.domain.ProductCommentBean;

@Service
@Transactional
public class ProductCommentService {

    @Autowired
    private ProductCommentRepository productCommentRepository;
    @Autowired
    private OrderdetailsRepository orderdetailsRepository;

    public boolean insertProductCommentAndOrderDetail(String body){
        try {
            
            JSONObject obj = new JSONObject(body);
            Integer productId = obj.isNull("productId")?null:obj.getInt("productId");
            Integer userId = obj.isNull("userId")?null:obj.getInt("userId");
            Double score = obj.isNull("score")?null:obj.getDouble("score");
            String text = obj.isNull("text")?null:obj.getString("text");
            Integer orderdetailId = obj.isNull("orderdetailId")?null:obj.getInt("orderdetailId");
            String productCommentStatus = obj.isNull("productCommentStatus")?null:obj.getString("productCommentStatus");
    
            ProductCommentBean productCommentBean = new ProductCommentBean();
            productCommentBean.setProductId(productId);
            productCommentBean.setUserId(userId);
            productCommentBean.setScore(score);
            productCommentBean.setText(text);
            productCommentBean.setCreateAt(new Date());

            productCommentRepository.save(productCommentBean);
            Integer newProductCommentId = productCommentBean.getProductCommentId();

            
            orderdetailsRepository.updateProductCommentInOrderDetail(productCommentStatus, newProductCommentId, orderdetailId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }
    
}
