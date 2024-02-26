package tw.eeit175groupone.finalproject.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.eeit175groupone.finalproject.dao.CouponDetailRepository;
import tw.eeit175groupone.finalproject.dao.CouponRepository;
import tw.eeit175groupone.finalproject.dao.UserRepository;
import tw.eeit175groupone.finalproject.domain.CouponBean;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CouponDetailRepository couponDetailRepository;
    
  public List<CouponBean> findByUsedAndUserId(String used, Integer userId){
        if(userId!=null){
            List<CouponBean> beans = couponRepository.findByUsedAndUserId(used,userId);
            if (beans != null && !beans.isEmpty()) {
                List<CouponBean> result = new ArrayList<>();
                List<CouponBean> update = new ArrayList<>();
    
                for (CouponBean temp : beans) {
                    //只選可以使用的時間區間
                    if (temp.getEndDate().getTime()>System.currentTimeMillis() && temp.getBeginDate().getTime()<System.currentTimeMillis()) {
                        result.add(temp);
                    } else {
                        // temp.setUsed("used");
                        update.add(temp);
                    }
                }
    
                // 如果有使用卷的日期小於現在日期才會有update的List
                if (!update.isEmpty()) {
                    couponRepository.saveAll(update);
                }
    
                return result;
            }
        }
        return null;
    }

     
/**
     * 新增coupon
     * 
     * @param body
     * @return
     */

    public boolean addCoupon(String body) {
        JSONObject obj = new JSONObject(body);
        String discountStr = obj.isNull("discount") ? null : obj.getString("discount");
        List<Integer> userId = userRepository.findAllId();
        String info = obj.isNull("info") ? null : obj.getString("info");
        String used = obj.isNull("used") ? "unused" : obj.getString("used");
        String beginDateStr = obj.isNull("beginDate") ? null : obj.getString("beginDate");
        String endDateStr = obj.isNull("endDate") ? null : obj.getString("endDate");

       

        //把string轉float
        float discount = 0.0f; // 默认值为 0.0
            if (discountStr != null) {
                try {
                    discount = Float.parseFloat(discountStr);
                } catch (NumberFormatException e) {
                    // 处理无法解析为浮点数的情况
                    e.printStackTrace();
                }
            }   

        List<CouponBean> newCoupons=new ArrayList<>();
        for (Integer id : userId) {
            CouponBean temp = new CouponBean();
            temp.setDiscount(discount);
            temp.setInfo(info);
            temp.setUserId(id);
            temp.setUsed(used);
            
            // 检查日期时间字符串是否为 null
            if (beginDateStr != null && endDateStr != null) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date beginDate = dateFormat.parse(beginDateStr);
                    Date endDate = dateFormat.parse(endDateStr);
                    temp.setBeginDate(beginDate);
                    temp.setEndDate(endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    System.out.println("日期时间格式不正确！");
                }
            }else{
                return false;
            }
            
            newCoupons.add(temp);
        }
        List<CouponBean> couponBeans=couponRepository.saveAll(newCoupons);
        if(couponBeans!=null && !couponBeans.isEmpty()){
            return true;
        }
        return false;
    }

    public List<CouponBean> findAll(){
		return couponRepository.findAll();
    }
    
    @Transactional
    public boolean deleteCouponByCouponId(Integer couponId) {
        // System.err.println("66666");
        
        if(couponId!= null){
            couponRepository.deleteCouponByCouponId(couponId);
            return true;
        }     
        return false;
    }

//  /**
//      * 將折價卷新增到資料庫
//      * @param infoOfCreate---CouponDetail
//      * @return CouponDetail--寫入資料庫的數據
//      *                   
//      */
//     public CouponDetail createCoupon(CouponDetail infoOfCreate){
//         CouponDetail save=couponDetailRepository.save(infoOfCreate);
//         if(save!=null){
//             return save;
//         }
//         return null;
//     }


}



