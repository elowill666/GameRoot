package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.eeit175groupone.finalproject.domain.ProductArticlesBean;

public interface ProductArticlesRepository extends JpaRepository<ProductArticlesBean,Integer>{

    @Query("select pa, a from ProductArticlesBean pa join ArticlesBean a on pa.articlesId = a.articlesId where pa.productId = :productId and a.status = 'normal' order by a.createdAt desc")
    List<Object[]> findProductArticlesByProductId(@Param("productId") Integer productId); 
    
} 