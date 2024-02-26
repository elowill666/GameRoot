package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tw.eeit175groupone.finalproject.domain.ArticlesBean;
import tw.eeit175groupone.finalproject.domain.CommentsBean;
import tw.eeit175groupone.finalproject.domain.UserBean;

@SpringBootTest
public class CommentsRepositoryTests {
    @Autowired
    private CommentsRepository commentsRepository;

    @Test
    public void testFindCommentsByArticlesId() {
        List<CommentsBean> result = commentsRepository.findCommentsByArticlesId(1);
        for (CommentsBean comment : result) {
            System.err.println("comment = " + comment);
        }
    }

    @Test
    public void testFindArticleById() {
        Integer times = commentsRepository.countCommentsNumberByArticleId(1);
        System.err.println("article = " + times);
    }

    @Test
    public void testCountCommentsNumberGroupByArticlesId() {
        List<Object[]> temp = commentsRepository.countCommentsNumberGroupByArticlesId();
        if (temp != null) {
            for (Object[] o : temp) {
                System.out.println(o[0]);
                System.out.println(o[1]);
            }
        }

    }

    @Test
    public void testcountCommentsGroupByArticlesIdAndUserID() {
        List<Object[]> countTemp = commentsRepository.countCommentsGroupByArticlesIdAndUserID(1);
        for (Object[] test : countTemp) {
            System.err.println("allArticles1=" + test[0]);
            System.err.println("allArticles2=" + test[1]);

        }
    }

    @Test
    public void testFindCommentsDetailByArticlesId() {
        List<Object[]> result = commentsRepository.findCommentsDetailByArticlesId(1);
        for (Object[] comment : result) {
            System.err.println("comment = " + comment);
        }
    }

    @Test
    public void testCountCommentsNumberEachArticle() {
        List<Object[]> result = commentsRepository.countCommentsNumberEachArticle();
        for (Object[] obj : result) {
            System.err.println("obj=" + obj);
        }
    }

    @Test
    public void testCountSearchCommentsNumber() {
        List<Object[]> result = commentsRepository.countSearchCommentsNumber("問題");
        for (Object[] obj : result) {
            System.err.println("obj=" + obj);
        }
    }

    @Test
    public void testHideComment() {
        commentsRepository.hideComment(34);
        System.err.println("true");
    }

}
