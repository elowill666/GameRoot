package tw.eeit175groupone.finalproject.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.id.IntegralDataTypeHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import tw.eeit175groupone.finalproject.domain.ArticlesBean;
import tw.eeit175groupone.finalproject.domain.UserBean;

@Repository
public interface ArticlesRepository extends JpaRepository<ArticlesBean, Integer> {

        @Query("SELECT a FROM ArticlesBean a WHERE (a.articleHead LIKE %:keyword% OR a.articleText LIKE %:keyword%) AND (a.status != 'banned') ")
        List<ArticlesBean> searchKeyword(@Param("keyword") String keyword);

        @Query("SELECT a,u FROM ArticlesBean a JOIN UserBean u ON a.userId = u.id WHERE (a.articleHead LIKE %:keyword% OR a.articleText LIKE %:keyword%) AND (a.status != 'banned') ")
        List<Object[]> searchKeywordList(@Param("keyword") String keyword);

        @Query("SELECT a FROM ArticlesBean a WHERE (a.articlesId = :articlesId) AND (a.status != 'banned') ")
        ArticlesBean findArticleByArticlesId(@Param("articlesId") Integer articlesId);

        @Query("SELECT u FROM UserBean u JOIN ArticlesBean a ON u.id = a.userId WHERE (a.articlesId = :articlesId) AND (a.status != 'banned') ")
        UserBean findUserByArticlesId(@Param("articlesId") Integer articlesId);

        @Query("SELECT a,u FROM ArticlesBean a JOIN UserBean u ON a.userId = u.id WHERE a.status != 'banned'")
        List<Object[]> findAllArticlesWithUsers();

        @Query("SELECT a.articlesId FROM ArticlesBean a WHERE a.status != 'banned' ")
        List<Integer> findAllArticlesId();

        List<ArticlesBean> findByUserId(Integer userId);

        @Query("SELECT a.articlesId FROM ArticlesBean a WHERE (a.articleHead LIKE %:keyword% OR a.articleText LIKE %:keyword%) AND (a.status != 'banned') ")
        List<Integer> findKeywordArticlesId(@Param("keyword") String keyword);

        @Modifying
        @Query("UPDATE ArticlesBean a SET " +
                        "a.articleHead = :articleHead, " +
                        "a.articleText = :articleText, " +
                        "a.updateAt = :updateAt " +
                        "WHERE a.articlesId = :articlesId")
        void updateArticle(
                        @Param("articlesId") Integer articlesId,
                        @Param("articleHead") String articleHead,
                        @Param("articleText") String articleText,
                        @Param("updateAt") java.util.Date updateAt);

        @Transactional
        @Modifying
        @Query("DELETE ArticlesBean a WHERE a.articlesId=:articlesId")
        void deleteArticle(@Param("articlesId") Integer articlesId);

        @Query("SELECT p.productId FROM ArticlesBean a JOIN ProductBean p ON a.articleGameType = p.productName WHERE a.articleGameType = :articleGameType")
        Integer findProductIdByProductName(@Param("articleGameType") String articleGameType);

        List<ArticlesBean> findAll(Specification<ArticlesBean> spec);

        long count(Specification<ArticlesBean> spec);

        @Query("SELECT userId,COUNT(a) FROM ArticlesBean a\r\n" +
                        "WHERE userId IN :userIds\r\n" +
                        "GROUP BY userId")
        List<Object[]> countPersonalPost(@Param("userIds") List<Integer> userIds);

        @Query("SELECT a,u FROM ArticlesBean a JOIN UserBean u ON a.userId = u.id WHERE articleGameType = :articleGameType AND a.status != 'banned'")
        List<Object[]> findAllArticlesWithUsersFiltered(@Param("articleGameType") String articleGameType);

}
