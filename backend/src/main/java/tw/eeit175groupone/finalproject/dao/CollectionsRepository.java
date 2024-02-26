package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.eeit175groupone.finalproject.domain.CollectionsBean;
import tw.eeit175groupone.finalproject.domain.WishlistBean;
import tw.eeit175groupone.finalproject.dto.CollectionsLikesCommentsDTO;

@Repository
public interface CollectionsRepository extends JpaRepository<CollectionsBean, Integer> {

    @Query("SELECT NEW tw.eeit175groupone.finalproject.dto.CollectionsLikesCommentsDTO( a.articlesId, a.userId, a.articleGameType, a.articleHead, a.articleText, a.updateAt, a.createdAt, a.clicktimes, a.status, a.articleType, COUNT(l) as likes, COUNT(cm) as comments) FROM CollectionsBean c JOIN ArticlesBean a ON c.articleId = a.articlesId LEFT JOIN LikesBean l ON l.articlesId = a.articlesId LEFT JOIN CommentsBean cm ON cm.articlesId = a.articlesId WHERE c.userId = :userId and a.status = 'normal' GROUP BY  a.articlesId, a.userId, a.articleGameType, a.articleHead, a.articleText, a.updateAt, a.createdAt, a.clicktimes, a.status, a.articleType")
    List<CollectionsLikesCommentsDTO> findProductByUserIdInCollections(@Param("userId") Integer userId, Pageable pageable);

    Long countByUserId(Integer userId);

    @Query("select c from CollectionsBean c where c.articleId = :articleId and c.userId = :userId")
    List<CollectionsBean> confirmByProductIdInUserCollections(@Param("articleId")Integer articleId,@Param("userId")Integer userId);

    @Modifying
    @Query("Delete from CollectionsBean c where c.articleId = :articleId and c.userId = :userId")
    void deleteByProductIdInUserCollections(@Param("articleId")Integer articleId,@Param("userId")Integer userId);

    
    @Query("SELECT c FROM CollectionsBean c\n\r"
    +"JOIN ArticlesBean a\n\r"
    +"ON c.articleId = a.articlesId\n\r"
    +"WHERE c.userId = :userId")
    List<Object[]> findArticles(@Param("userId")Integer userId);

    @Modifying
    @Query("DELETE from CollectionsBean ct WHERE ct.articleId = :articleId AND ct.userId = :userId")
    void deleteCollection(@Param("articleId") Integer articleId, @Param("userId") Integer userId);

}
