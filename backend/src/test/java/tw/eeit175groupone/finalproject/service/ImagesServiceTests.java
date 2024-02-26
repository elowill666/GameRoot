package tw.eeit175groupone.finalproject.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tw.eeit175groupone.finalproject.domain.ImagesBean;

@SpringBootTest
public class ImagesServiceTests {
    @Autowired
    private ImagesService imagesService;

    @Test
    public void testFindAllCommentsImagesByArticlesId() {
        List<ImagesBean> commentsImages = imagesService.findAllCommentsImagesByArticlesId(2);
        for (ImagesBean beans : commentsImages) {
            System.err.println("beans = " + beans);
        }
    }
}
