package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import tw.eeit175groupone.finalproject.domain.CommentsBean;

@Repository
public interface CommentsRepository extends JpaRepository<CommentsBean, Integer>{

    @Query("SELECT c FROM CommentsBean c WHERE (c.articlesId = :articlesId) AND (c.status != 'banned') ")
    List<CommentsBean> findCommentsByArticlesId(@Param("articlesId") Integer articlesId);

    @Query("SELECT c,u,a "+
            "FROM CommentsBean c "+
            "JOIN UserBean u ON c.userId = u.id "+
            "JOIN ArticlesBean a ON a.articlesId = c.articlesId "+
            "WHERE (a.articlesId = :articleId) "+
            "AND (c.status != 'banned')")
    List<Object[]> findCommentsDetailByArticlesId(@Param("articleId") Integer articlesId);

    /**
     * 用文章Id找文章有幾則留言
     *
     * @param articlesId
     * @return 此文章有多少留言數
     */
    @Query("SELECT COUNT(c) FROM CommentsBean c WHERE (c.articlesId = :articlesId) AND (c.status != 'banned')")
    Integer countCommentsNumberByArticleId(@Param("articlesId") Integer articlesId);

    /**
     * 用groupby找留言數
     *
     * @return 文章有多少留言數
     */
    @Query("SELECT articlesId, COUNT(c) FROM CommentsBean c GROUP BY articlesId")
    List<Object[]> countCommentsNumberGroupByArticlesId();

    @Query("SELECT c.articlesId, COUNT(c)\r\n"+
            "FROM CommentsBean c\r\n"+
            "JOIN ArticlesBean a ON c.articlesId=a.articlesId\r\n"+
            "WHERE a.userId=:userId GROUP BY c.articlesId")
    List<Object[]> countCommentsGroupByArticlesIdAndUserID(@Param("userId") Integer userId);

    @Query("SELECT a.articlesId, COUNT(c) "+
            "FROM ArticlesBean a "+
            "JOIN CommentsBean c ON a.articlesId = c.articlesId "+
            "WHERE c.status != 'banned' "+
            "GROUP BY a.articlesId")
    List<Object[]> countCommentsNumberEachArticle();

    @Query("SELECT a.articlesId, COUNT(c) "+
            "FROM ArticlesBean a "+
            "JOIN CommentsBean c ON a.articlesId = c.articlesId "+
            "WHERE (a.articleHead LIKE %:keyword% OR a.articleText LIKE %:keyword%) "+
            "AND (c.status != 'banned') "+
            "GROUP BY a.articlesId")
    List<Object[]> countSearchCommentsNumber(@Param("keyword") String keyword);

    @Transactional
    @Modifying
    @Query("UPDATE CommentsBean c SET c.status = 'banned' WHERE c.commentId=:commentId")
    void hideComment(@Param("commentId") Integer commentId);
    
    List<CommentsBean> findAll();

    List<CommentsBean> findAll(Specification<CommentsBean> spec,Pageable pageable);

    long count(Specification<CommentsBean> spec);

    
}
