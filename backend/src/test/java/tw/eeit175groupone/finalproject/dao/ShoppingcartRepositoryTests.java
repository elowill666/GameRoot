package tw.eeit175groupone.finalproject.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tw.eeit175groupone.finalproject.domain.CartmiddleBean;
import tw.eeit175groupone.finalproject.domain.ProductBean;
import tw.eeit175groupone.finalproject.domain.ShoppingcartBean;
import tw.eeit175groupone.finalproject.domain.UserBean;
import tw.eeit175groupone.finalproject.dto.ShoppingcartDTO;
import tw.eeit175groupone.finalproject.dto.ShoppingcartPaydata;
import tw.eeit175groupone.finalproject.service.ShoppingcartService;

@SpringBootTest
public class ShoppingcartRepositoryTests {
    @Autowired
    private ShoppingcartRepository shoppingcartRepository;

    @Autowired
    private ShoppingcartService shoppingcartService;

    @Autowired
    private CartmiddleRepository cartmiddleRepository;

    @Test
    public void TestFindShoppingcartCount(){
        Integer id = 2;
        Integer countShoppingcart = shoppingcartService.countShoppingcart(id);
        System.err.println(countShoppingcart);
    }

    @Test
    public void TestcleanShoppingcart() {
        Integer id = 1;
        shoppingcartService.cleanShoppingcart(id);

    }

    @Test
    public void TestFindPayData() {
        Integer id = 1;
        ShoppingcartPaydata shoppingcartPaydata = new ShoppingcartPaydata();
        List<Object[]> payData = shoppingcartRepository.findPayData(id);
        for (Object[] row : payData) {
            System.out.println(row[0]);
            BeanUtils.copyProperties(row[0], shoppingcartPaydata);
            BeanUtils.copyProperties(row[1], shoppingcartPaydata);
        }
        System.err.println(shoppingcartPaydata);
    }

    // @Test
    // public void TestUpdateallTotalPrice() {
    //     Integer price = 100;
    //     Integer id = 1;
    //     shoppingcartService.updateallTotalPrice(price, id);
    // }

    @Test
    public void TestFindShoppingcart() {
        Integer userId = 1;

        List<Object[]> shoppingcart = shoppingcartRepository.findShoppingcart(userId);
        ArrayList<ShoppingcartDTO> array = new ArrayList<ShoppingcartDTO>();
        for (Object[] row : shoppingcart) {
            System.out.println(row[0]);
            ShoppingcartDTO shoppingcartDTO = new ShoppingcartDTO();
            ShoppingcartBean shoppingcartBean = (ShoppingcartBean) row[0];
            CartmiddleBean cartmiddleBean = (CartmiddleBean) row[1];
            ProductBean productBean = (ProductBean) row[2];
            BeanUtils.copyProperties(shoppingcartBean, shoppingcartDTO);
            BeanUtils.copyProperties(cartmiddleBean, shoppingcartDTO);
            BeanUtils.copyProperties(productBean, shoppingcartDTO);
            array.add(shoppingcartDTO);
        }
        // System.err.println(array);
    }

    @Test
    public void TestFindcart() {
        Integer id = 1;
        List<ShoppingcartDTO> findcart = shoppingcartService.findcart(id);
        System.out.println(findcart);
    }

    @Test
    public void TestDecreaseQuantity() {
        Integer id = 3;
        List<Object[]> byCartmiddleIdandPrice = cartmiddleRepository.findByCartmiddleIdandPrice(id);
        for (Object[] row : byCartmiddleIdandPrice) {
            CartmiddleBean cartmiddleBean = (CartmiddleBean) row[0];
            Integer productPrice = (Integer) row[1];
            Integer quantity = cartmiddleBean.getQuantity();
            Integer totalprice = cartmiddleBean.getTotalprice();
            quantity--;
            totalprice = productPrice * quantity;
            cartmiddleBean.setQuantity(quantity);
            cartmiddleBean.setTotalprice(totalprice);
            cartmiddleRepository.save(cartmiddleBean);
            System.err.println(cartmiddleBean);
        }
    }

    @Test
    public void TestAddToCart() {
        Integer id = 1;
        Integer productid = 1;
        // shoppingcartService.addToCart(productid, id);
        ShoppingcartBean shoppingcartBean = shoppingcartRepository.findByUserId(id);
        Integer shoppingcartId = shoppingcartBean.getShoppingcartId();
        List<CartmiddleBean> byShoppingcartId = cartmiddleRepository.findByShoppingcartId(shoppingcartId);
        for (CartmiddleBean row : byShoppingcartId) {
            if (row.getProductId() == shoppingcartId) {
                return;
            }
        }
    }

    @Test
    public void TestRemove() {
        Integer id = 11;
        shoppingcartService.remove(id);

    }
}
