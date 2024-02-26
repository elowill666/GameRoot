package tw.eeit175groupone.finalproject.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;
import tw.eeit175groupone.finalproject.domain.ProductCommentBean;
@Repository
public interface ProductCommentRepository extends JpaRepository<ProductCommentBean, Integer>{
    

    @Query("select pc, u.username,u.userphoto from ProductCommentBean pc join UserBean u on pc.userId = u.id where pc.productId = :productId order by pc.createAt DESC")
    List<Object[]> findProductCommentByProductId(@Param("productId") Integer productId); 
}
