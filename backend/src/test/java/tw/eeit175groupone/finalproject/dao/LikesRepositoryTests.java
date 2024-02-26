package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import tw.eeit175groupone.finalproject.domain.LikesBean;

@SpringBootTest
public class LikesRepositoryTests {
    @Autowired
    private LikesRepository likesRepository;

    @Test
    public void testCountLikesNumberByArticleId() {
        Integer times = likesRepository.countLikesNumberByArticleId(4);
        System.err.println("likesnumber=" + times);
    }

    @Test
    public void testCountCommentLikesNumberByArticlesId() {
        List<Object[]> times = likesRepository.countCommentLikesNumberByArticlesId(2);
        for (Object[] time : times) {
            for (Object obj : time)
                System.err.println("obj=" + obj);
        }

    }

    @Test
    public void testCountLikesNumberEachArticle() {
        List<Object[]> result = likesRepository.countLikesNumberEachArticle();
        for (Object[] obj : result) {
            System.err.println("obj=" + obj);
        }
    }

    @Test
    public void testCountSearchLikesNumber() {
        List<Object[]> result = likesRepository.countSearchLikesNumber("問題");
        for (Object[] obj : result) {
            System.err.println("obj=" + obj);
        }
    }

}
