package tw.eeit175groupone.finalproject.controller;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import tw.eeit175groupone.finalproject.domain.UserBean;
import tw.eeit175groupone.finalproject.dto.CollectionsLikesCommentsDTO;
import tw.eeit175groupone.finalproject.dto.WishlistDTO;
import tw.eeit175groupone.finalproject.service.CollectionsService;

@Controller

public class CollectionsController {

    @Autowired
    private CollectionsService collectionsService;

    @PostMapping("/collections/findArticleById")
    public ResponseEntity<?> findArticle(HttpSession session,@RequestBody Map<String, String> request) {
        System.out.println("request = "+request);
        if (session != null) {
            UserBean user = (UserBean) session.getAttribute("user");
            Integer userId = user.getId();
            if (userId != null) {
                List<CollectionsLikesCommentsDTO> articles = collectionsService.findArticles(userId,request);
                Long countByUserId = collectionsService.countByUserId(userId);
                Object[] beans = new Object[2];
                beans[0] = articles;
                beans[1] = countByUserId;
                return ResponseEntity.ok().body(beans);
            }
        }
        return ResponseEntity.notFound().build();
    }
    
@PostMapping("/collections/insert")
public ResponseEntity<?> insertCollection(@RequestBody String json){
    JSONObject response= new JSONObject();
    if(collectionsService.insertCollection(json)){
        // System.err.println("hihihihihihiii");
        response.put("scuccess", true);
        response.put("text", "成功加入收藏文章");
        return ResponseEntity.ok().body(response.toString());    
    } else {
        response.put("scuccess",false);
        response.put("text", "已經在收藏文章囉");
        return ResponseEntity.ok(response.toString());
    }
    
}
@DeleteMapping("/collections/delete")
public ResponseEntity<?> deleteCollection(@RequestParam(name = "userId")Integer userId,@RequestParam(name = "articleId")Integer articleId){
    JSONObject response= new JSONObject();
    if(collectionsService.deleteCollection(userId, articleId)){
        // System.err.println("hihihihihihiii");
        response.put("scuccess", true);
        response.put("text", "刪除文章收藏");
        return ResponseEntity.ok().body(response.toString());    
    } else {
        response.put("scuccess",false);
        response.put("text", "不想讓你移除文章");
        return ResponseEntity.ok(response.toString());
    }
    
}
@PostMapping("/collections/check")
public ResponseEntity<?> confirmByProductIdInUserCollection(@RequestBody String body) {
    JSONObject response= new JSONObject();
    //TODO: process POST request
    if(collectionsService.confirmByProductIdInUserCollection(body)){
        response.put("input", true);
        return ResponseEntity.ok().body(response.toString()); 
    } else {
        response.put("input", false);
        return ResponseEntity.ok().body(response.toString()); 
    }
}
    
}
