package tw.eeit175groupone.finalproject.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.eeit175groupone.finalproject.domain.ProductImageBean;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImageBean,Integer>{

    @Query("select pi from ProductImageBean pi where pi.productId = :productId and pi.imageType = 'description' order by pi.imageGetFirst")
    List<ProductImageBean> findDescriptionImagesByProductId(@Param("productId")Integer productId); 
    @Query("select pi from ProductImageBean pi where pi.productId = :productId and pi.imageType = 'productimg' order by pi.imageGetFirst")
    List<ProductImageBean> findProductImagesByProductId(@Param("productId")Integer productId); 
    @Query("select pi from ProductImageBean pi where pi.productId = :productId and pi.imageType = 'productimg' and pi.imageGetFirst = 1")
    List<ProductImageBean> findFirstProductImagesByProductId(@Param("productId")Integer productId);

    @Modifying
    @Query("Delete from ProductImageBean where productId = :productId")
    void deleteProductImageByProductId(@Param("productId") Integer productId);
}