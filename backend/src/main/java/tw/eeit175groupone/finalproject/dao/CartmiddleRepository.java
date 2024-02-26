package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.eeit175groupone.finalproject.domain.CartmiddleBean;

@Repository
public interface CartmiddleRepository extends JpaRepository<CartmiddleBean, Integer> {
    @Query("SELECT c,p.price FROM CartmiddleBean c\n\r"
            + "JOIN ProductBean p\n\r"
            + "ON c.productId = p.productId\n\r"
            + "WHERE c.cartmiddleId = :cartmiddleId")
    List<Object[]> findByCartmiddleIdandPrice(@Param("cartmiddleId") Integer cartmiddleId);

    List<CartmiddleBean> findByShoppingcartId(Integer shoppingcartId);

    @Modifying
    @Query("DELETE FROM CartmiddleBean WHERE shoppingcartId = :shoppingcartId")
    void deleteByShoppingcartId(@Param("shoppingcartId") Integer shoppingcartId);
}
