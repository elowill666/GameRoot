package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.eeit175groupone.finalproject.domain.ShoppingcartBean;

@Repository
public interface ShoppingcartRepository extends JpaRepository<ShoppingcartBean, Integer> {
        @Query("SELECT s,c,p,m FROM ShoppingcartBean s\r\n"
                        + "JOIN CartmiddleBean c\r\n"
                        + "ON s.shoppingcartId = c.shoppingcartId\r\n"
                        + "JOIN ProductBean p\r\n"
                        + "ON c.productId = p.productId\r\n"
                        + "LEFT JOIN MerchandiseBean m\n\r"
                        + "ON p.productId = m.productId\n\r"
                        + "WHERE s.userId = :userId\n\r"
                        + "ORDER BY p.productId")
        List<Object[]> findShoppingcart(@Param("userId") Integer userId);

        ShoppingcartBean findByUserId(Integer userId);

        @Query("SELECT s,u FROM ShoppingcartBean s\r\n"
                        + "JOIN UserBean u\r\n"
                        + "ON s.userId = u.id\r\n"
                        + "WHERE s.userId = :userId")
        List<Object[]> findPayData(@Param("userId") Integer userId);

        @Query("SELECT count(c.productId) FROM CartmiddleBean c\r\n"
        +"JOIN ShoppingcartBean s\r\n"
        +"ON c.shoppingcartId = s.shoppingcartId\r\n"
        +"WHERE s.userId = :userId\r\n")
        Integer findShoppingcartCount(@Param("userId") Integer userId);
}
