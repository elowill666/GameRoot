package tw.eeit175groupone.finalproject.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.minidev.json.JSONObject;

@SpringBootTest
public class LikesServiceTests {
    @Autowired
    private LikesService likesService;

    @Test
    public void testAddLike() {
        JSONObject obj = new JSONObject();
        obj.put("userId", 3);
        obj.put("articlesId", 1);
        obj.put("commentsId", null);
        boolean result = likesService.addLike(obj.toString());
        System.err.println(result);
    }

    @Test
    public void testDeleteLike() {
        boolean result = likesService.deleteLike(3, null, 3);
        System.err.println(result);
    }

    @Test
    public void testConfirmLikesExist() {
        JSONObject obj = new JSONObject();
        obj.put("userId", 3);
        obj.put("articlesId", 1);
        obj.put("commentsId", null);
        boolean result = likesService.confirmLikesExist(obj.toString());
        System.err.println(result);
    }

}
