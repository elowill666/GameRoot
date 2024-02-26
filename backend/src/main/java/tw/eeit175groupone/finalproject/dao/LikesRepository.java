package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.eeit175groupone.finalproject.domain.CollectionsBean;
import tw.eeit175groupone.finalproject.domain.LikesBean;

@Repository
public interface LikesRepository extends JpaRepository<LikesBean, Integer> {

        @Query("SELECT COUNT(l) FROM LikesBean l WHERE l.articlesId = :articlesId")
        Integer countLikesNumberByArticleId(@Param("articlesId") Integer articlesId);

        @Query("SELECT c.commentId, COUNT(l) " +
                        "FROM CommentsBean c " +
                        "JOIN LikesBean l ON l.commentsId = c.commentId " +
                        "JOIN ArticlesBean a ON c.articlesId = a.articlesId " +
                        "WHERE a.articlesId = :articleId " +
                        "GROUP BY c.commentId")
        List<Object[]> countCommentLikesNumberByArticlesId(@Param("articleId") Integer articleId);

        @Query("SELECT a.articlesId, COUNT(l) " +
                        "FROM ArticlesBean a " +
                        "JOIN LikesBean l ON a.articlesId = l.articlesId " +
                        "GROUP BY a.articlesId")
        List<Object[]> countLikesNumberEachArticle();

        @Query("SELECT a.articlesId, COUNT(l) " +
                        "FROM ArticlesBean a " +
                        "JOIN LikesBean l ON a.articlesId = l.articlesId " +
                        "WHERE a.articleHead LIKE %:keyword% OR a.articleText LIKE %:keyword% " +
                        "GROUP BY a.articlesId")
        List<Object[]> countSearchLikesNumber(@Param("keyword") String keyword);

        @Modifying
        @Query("DELETE FROM LikesBean l WHERE (l.userId = :userId AND l.articlesId = :articlesId) OR (l.userId = :userId AND l.commentsId = :commentsId) ")
        void deleteLike(@Param("userId") Integer userId,
                        @Param("articlesId") Integer articlesId,
                        @Param("commentsId") Integer commentsId);

        @Query("SELECT l FROM LikesBean l WHERE (l.userId = :userId AND l.articlesId = :articlesId) OR (l.userId = :userId AND l.commentsId = :commentsId) ")
        List<LikesBean> confirmLikesExist(@Param("userId") Integer userId,
                        @Param("articlesId") Integer articlesId,
                        @Param("commentsId") Integer commentsId);


        //                 @Query("SELECT a.articlesId, COUNT(l) " +
        //                 "FROM ArticlesBean a " +
        //                 "JOIN LikesBean l ON a.articlesId = l.articlesId "+
        //                 "WHERE a.articlesId IN :articlesIds" +
        //                 "GROUP BY a.articlesId")
        // List<Object[]> countLikesNumberArticle(@Param("articlesIds") List<Integer> articlesIds);

}
