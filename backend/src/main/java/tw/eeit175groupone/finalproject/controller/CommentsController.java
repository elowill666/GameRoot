package tw.eeit175groupone.finalproject.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.eeit175groupone.finalproject.service.CommentsService;

@RestController

@RequestMapping("/comments")
public class CommentsController {
    @Autowired
    private CommentsService commentsService;

    // 新增留言
    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestBody String body) {

        boolean isAdded = commentsService.addComment(body);
        JSONObject response = new JSONObject();
        if (isAdded) {
            response.put("success", true);
            response.put("text", "留言成功");
            return ResponseEntity.ok().body(response.toString());
        } else {
            response.put("success", false);
            response.put("text", "留言失敗");
            return ResponseEntity.ok().body(response.toString());
        }
    }

    // 隱藏文章banned
    @PutMapping("/hide/{commentId}")
    public ResponseEntity<?> hideArticle(@PathVariable Integer commentId) {
        boolean ishidden = commentsService.hideComment(commentId);
        if (ishidden) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

}
