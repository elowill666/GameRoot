package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tw.eeit175groupone.finalproject.domain.ImagesBean;

@SpringBootTest
public class ImagesRepositoryTests {

    @Autowired
    private ImagesRepository imagesRepository;

    @Test
    public void testFindAllCommentsImagesByArticlesId() {

        List<ImagesBean> allCommentsImages = imagesRepository.findAllCommentsImagesByArticlesId(2);
        for (ImagesBean bean : allCommentsImages) {
            System.err.println("image = " + bean);
        }
    }

}
