package tw.eeit175groupone.finalproject.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.eeit175groupone.finalproject.dao.WishlistRepository;
import tw.eeit175groupone.finalproject.domain.ProductBean;
import tw.eeit175groupone.finalproject.domain.WishlistBean;
import tw.eeit175groupone.finalproject.dto.ShoppingcartDTO;
import tw.eeit175groupone.finalproject.dto.WishlistDTO;

@Service
@Transactional
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    //用userid查詢wishlist
    public List<WishlistDTO> findWishlist(Integer userId){
        List<Object[]> result = wishlistRepository.findWishlist(userId);
        ArrayList<WishlistDTO> array = new ArrayList<WishlistDTO>();
        for (Object[] temp : result) {
            WishlistDTO dto=new WishlistDTO();
                BeanUtils.copyProperties((WishlistBean) temp[0],dto);
                BeanUtils.copyProperties((ProductBean) temp[1],dto);
            array.add(dto);
        }
        return array;
        }


    //願望清單++
    public boolean insertWishlist(String body) {
        JSONObject obj = new JSONObject(body);
        Integer productId = obj.isNull("productId")?null:obj.getInt("productId");
        Integer userId = obj.isNull("userId")?null:obj.getInt("userId");
        // System.err.println("hooooooooo");

        if(productId!=null && userId!= null){
            List<WishlistBean> confirmByProductIdInUserWishlist = wishlistRepository.confirmByProductIdInUserWishlist(productId, userId);
            if(confirmByProductIdInUserWishlist.isEmpty() && confirmByProductIdInUserWishlist.size()==0){
                WishlistBean insert = new WishlistBean(); 
                insert.setProductId(productId);
                insert.setUserId(userId);
                insert.setCreateAt(new Date());
                wishlistRepository.saveAndFlush(insert);
                return true;

            }
        }
        return false;
        
    }
    //願望清單--
    public boolean deleteWishlist(Integer userId,Integer productId) {
        // System.err.println("hooooooooo");
        
        if(productId!=null && userId!= null){
            wishlistRepository.deleteByProductIdInUserWishlist(productId, userId);
            return true;
        }     
        return false;
    }
    /**確認登入者有沒有加入過wishlist
     * @param userId
     * @param productId
     * @return 是否
     */
    public boolean confirmByProductIdInUserWishlist(String body){
        JSONObject obj = new JSONObject(body);
        Integer productId = obj.isNull("productId")?null:obj.getInt("productId");
        Integer userId = obj.isNull("userId")?null:obj.getInt("userId");
        if(productId!=null&&userId!=null){
            List<WishlistBean> beans = wishlistRepository.confirmByProductIdInUserWishlist(productId, userId);
            // System.err.println("111111111111111111"+beans);
            if(beans.size()!=0||!beans.isEmpty()){
                 return true;
            }
        }
        return false;
    }
}   
