package tw.eeit175groupone.finalproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.eeit175groupone.finalproject.dao.CartmiddleRepository;
import tw.eeit175groupone.finalproject.dao.ProductRepository;
import tw.eeit175groupone.finalproject.dao.ShoppingcartRepository;
import tw.eeit175groupone.finalproject.domain.CartmiddleBean;
import tw.eeit175groupone.finalproject.domain.MerchandiseBean;
import tw.eeit175groupone.finalproject.domain.ProductBean;
import tw.eeit175groupone.finalproject.domain.ShoppingcartBean;
import tw.eeit175groupone.finalproject.dto.ShoppingcartDTO;
import tw.eeit175groupone.finalproject.dto.ShoppingcartPaydata;

@Service
@Transactional
public class ShoppingcartService {
    @Autowired
    private CartmiddleRepository cartmiddleRepository;
    @Autowired
    private ShoppingcartRepository shoppingcartRepository;
    @Autowired
    private ProductRepository productRepository;

    public void remove(Integer cartmiddleId) {
        if (cartmiddleId != null) {
            cartmiddleRepository.deleteById(cartmiddleId);
        }
    }

    public void addToCart(Integer productId, Integer id) {
        ShoppingcartBean shoppingcartBean = shoppingcartRepository.findByUserId(id);
        if (shoppingcartBean == null) {
            shoppingcartBean = new ShoppingcartBean();
            shoppingcartBean.setUserId(id);
            shoppingcartBean.setAlltotalprice(0);
            shoppingcartRepository.save(shoppingcartBean);
        }
        Integer shoppingcartId = shoppingcartBean.getShoppingcartId();
        List<CartmiddleBean> byShoppingcartId = cartmiddleRepository.findByShoppingcartId(shoppingcartId);
        for (CartmiddleBean row : byShoppingcartId) {
            if (row.getProductId().equals(productId)) {
                return;
            }
        }
        if (productId != null) {
            CartmiddleBean cartmiddleBean = new CartmiddleBean();
            cartmiddleBean.setProductId(productId);
            cartmiddleBean.setQuantity(1);
            cartmiddleBean.setTotalprice(0);
            cartmiddleBean.setShoppingcartId(shoppingcartBean.getShoppingcartId());
            cartmiddleRepository.save(cartmiddleBean);
        }

    }

    public List<ShoppingcartDTO> findcart(Integer userId) {
        List<Object[]> shoppingcart = shoppingcartRepository.findShoppingcart(userId);
        ArrayList<ShoppingcartDTO> array = new ArrayList<ShoppingcartDTO>();
        for (Object[] row : shoppingcart) {
            ShoppingcartDTO shoppingcartDTO = new ShoppingcartDTO();
            ShoppingcartBean shoppingcartBean = (ShoppingcartBean) row[0];
            CartmiddleBean cartmiddleBean = (CartmiddleBean) row[1];
            ProductBean productBean = (ProductBean) row[2];
            MerchandiseBean merchandiseBean = (MerchandiseBean) row[3];
            BeanUtils.copyProperties(shoppingcartBean, shoppingcartDTO);
            BeanUtils.copyProperties(cartmiddleBean, shoppingcartDTO);
            BeanUtils.copyProperties(productBean, shoppingcartDTO);
            if (merchandiseBean != null) {
                shoppingcartDTO.setInventoryQuantity(merchandiseBean.getInventoryQuantity());
            }
            Integer quantity = cartmiddleBean.getQuantity();
            if (productBean.getProductStatus().equals("discount")) {
                shoppingcartDTO.setDiscountPrice(productBean.getDiscount() * quantity);
            }
            shoppingcartDTO.setTotalprice(productBean.getPrice() * quantity);
            array.add(shoppingcartDTO);
        }
        return array;
    }

    public void increaseQuantity(Integer cartmiddleId) {

        List<Object[]> byCartmiddleIdandPrice = cartmiddleRepository.findByCartmiddleIdandPrice(cartmiddleId);
        if (!byCartmiddleIdandPrice.isEmpty()) {
            for (Object[] row : byCartmiddleIdandPrice) {
                CartmiddleBean cartmiddleBean = (CartmiddleBean) row[0];
                Integer quantity = cartmiddleBean.getQuantity() + 1;
                cartmiddleBean.setQuantity(quantity);
                cartmiddleRepository.save(cartmiddleBean);
            }
        }

    }

    public void decreaseQuantity(Integer cartmiddleId) {
        List<Object[]> byCartmiddleIdandPrice = cartmiddleRepository.findByCartmiddleIdandPrice(cartmiddleId);
        if (!byCartmiddleIdandPrice.isEmpty()) {
            for (Object[] row : byCartmiddleIdandPrice) {
                CartmiddleBean cartmiddleBean = (CartmiddleBean) row[0];
                Integer quantity = cartmiddleBean.getQuantity() - 1;
                cartmiddleBean.setQuantity(quantity);
                cartmiddleRepository.save(cartmiddleBean);
            }
        }
    }

    public void updateallTotalPrice(Integer totalprice, Integer id, Integer couponId) {
        if (totalprice != null && id != null) {
            ShoppingcartBean byUserId = shoppingcartRepository.findByUserId(id);
            byUserId.setAlltotalprice(totalprice);
            byUserId.setCouponId(couponId);
            shoppingcartRepository.save(byUserId);
        }
    }

    public ShoppingcartPaydata findPayData(Integer id) {
        if (id != null) {
            List<Object[]> payData = shoppingcartRepository.findPayData(id);
            ShoppingcartPaydata shoppingcartPaydata = new ShoppingcartPaydata();
            for (Object[] row : payData) {
                BeanUtils.copyProperties(row[0], shoppingcartPaydata);
                BeanUtils.copyProperties(row[1], shoppingcartPaydata);
            }
            return shoppingcartPaydata;
        }
        return null;
    }

    public void cleanShoppingcart(Integer userId) {
        if (userId != null) {
            ShoppingcartBean shoppingcartBean = shoppingcartRepository.findByUserId(userId);
            cartmiddleRepository.deleteByShoppingcartId(shoppingcartBean.getShoppingcartId());
        }
    }

    public Integer countShoppingcart(Integer id) {
        if(id!=null){
            return shoppingcartRepository.findShoppingcartCount(id);

        }
        return 0;
    }
}
