package tw.eeit175groupone.finalproject.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.minidev.json.JSONObject;

@SpringBootTest
public class CommentsServiceTests {
    @Autowired
    private CommentsService commentsService;

    @Test
    public void testAddComment() {
        JSONObject obj = new JSONObject();
        obj.put("content", "測試新增留言");
        obj.put("articlesId", 7);
        obj.put("userId", 7);
        boolean result = commentsService.addComment(obj.toString());
        System.err.println(result);
    }

    @Test
    public void testHideComment() {
        boolean result = commentsService.hideComment(41);
        System.err.println(result);
    }

}
