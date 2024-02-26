package tw.eeit175groupone.finalproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tw.eeit175groupone.finalproject.domain.ReportBean;

import java.util.List;

public interface ReportRepository extends JpaRepository<ReportBean,Integer>{


    /**
     * 
     * @param articleId--Integer
     * @return 某篇文章被檢舉幾次--Integer
     */
    @Query("SELECT COUNT(r) FROM ReportBean r WHERE reportedArticlesId=:articleId")
    Integer countReportsNumberById(@Param("articleId") Integer articleId);

    /**
     *用GROUPBY 分類找尋各文章被檢舉的次數
     *  
     * @return 各文章被檢舉幾次--Integer
     */
    @Query("SELECT reportedArticlesId,COUNT(r) FROM ReportBean r\r\n" +
            "GROUP BY reportedArticlesId")
    List<Object[]> countReportsNumberForArticle();

    /**
     *用GROUPBY 分類找尋各留言被檢舉的次數
     *
     * @return 各文章被檢舉幾次--Integer
     */
    @Query("SELECT reportedCommentsId,COUNT(r) FROM ReportBean r\r\n" +
            "GROUP BY reportedCommentsId")
    List<Object[]> countReportsNumberForComment();

    /**
     *以文章ID計算該文章被檢舉的次數
     * @param articleId--Integer
     * @return 文章被檢舉幾次--Integer
     */
    @Query("SELECT COUNT(r) FROM ReportBean r\r\n" +
            "WHERE status='confirmed' AND reportedArticlesId=:articleId")
    Integer countByReportedArticlesId(@Param("articleId") Integer articleId);


    /**
     *以留言ID計算該留言被檢舉的次數
     * @param commentId--Integer
     * @return 文章被檢舉幾次--Integer
     */
    @Query("SELECT COUNT(r) FROM ReportBean r\r\n" +
            "WHERE status='confirmed' AND reportedCommentsId=:commentId")
    Integer countByReportedCommentsId(@Param("commentId") Integer commentId);


    /**
     *以文章ID抓取該文章被檢舉的全部次數
     * @param articleId--Integer
     * @return 文章被檢舉幾次--Integer
     */
    List<ReportBean> findByReportedArticlesId(Integer articleId);


    /**
     *以留言ID抓取該留言被檢舉的全部次數
     * @param commentId--Integer
     * @return 留言被檢舉幾次--Integer
     */
    List<ReportBean> findByReportedCommentsId(Integer commentId);
    
    /**
     *以會員ID抓取此會員被檢舉的紀錄
     * @param reporterUserId--Integer
     * @return 留言被檢舉幾次--List<ReportBean>
     */
    List<ReportBean> findByReporterUserId(Integer reporterUserId);
    
}
