package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.eeit175groupone.finalproject.domain.ImagesBean;

@Repository
public interface ImagesRepository extends JpaRepository<ImagesBean, Integer> {

    @Query("SELECT i FROM ImagesBean i WHERE i.articlesId = :articlesId")
    List<ImagesBean> findByArticlesId(@Param("articlesId") Integer articlesId);

    /**
     * 根據文章Id尋找該篇文章留言的圖片
     * 
     * @param articleId
     */
    @Query("SELECT i,a,c " +
            "FROM ArticlesBean a " +
            "JOIN CommentsBean c ON a.articlesId = c.articlesId " +
            "JOIN ImagesBean i ON c.commentId = i.commentsId " +
            "WHERE a.articlesId = :articleId ")
    List<ImagesBean> findAllCommentsImagesByArticlesId(@Param("articleId") Integer articleId);

}
