package tw.eeit175groupone.finalproject.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient.ResponseSpec;

import tw.eeit175groupone.finalproject.service.LikesService;

@RestController
@CrossOrigin
@RequestMapping("/likes")
public class LikesController {
    @Autowired
    private LikesService likesService;

    // 按讚
    @PostMapping("/add")
    public ResponseEntity<?> addLike(@RequestBody String body) {
        boolean isAdded = likesService.addLike(body);
        JSONObject response = new JSONObject();
        if (isAdded) {
            response.put("success", true);
            response.put("text", "已按讚");
            return ResponseEntity.ok().body(response.toString());
        } else {
            response.put("success", false);
            response.put("text", "已經按過讚囉");
            return ResponseEntity.ok().body(response.toString());
        }
    }

    // 取消讚
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCollectedArticle(@RequestParam(name = "userId") Integer userId,
            @RequestParam(name = "articlesId", required = false) Integer articlesId,
            @RequestParam(name = "commentId", required = false) Integer commentsId) {
        JSONObject response = new JSONObject();
        if (likesService.deleteLike(userId, articlesId, commentsId)) {
            response.put("scuccess", true);
            response.put("text", "已收回讚");
            return ResponseEntity.ok().body(response.toString());
        } else {
            response.put("scuccess", false);
            response.put("text", "取消讚失敗");
            return ResponseEntity.ok(response.toString());
        }
    }

    // 確認登入者有沒有按過讚
    @PostMapping("/check")
    public ResponseEntity<?> confirmLikesExist(@RequestBody String body) {
        JSONObject response = new JSONObject();
        if (likesService.confirmLikesExist(body)) {
            response.put("input", true);
            return ResponseEntity.ok().body(response.toString());
        } else {
            response.put("input", false);
            return ResponseEntity.ok().body(response.toString());
        }
    }

}
