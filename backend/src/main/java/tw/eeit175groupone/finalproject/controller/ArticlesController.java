package tw.eeit175groupone.finalproject.controller;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.eeit175groupone.finalproject.domain.ArticlesBean;
import tw.eeit175groupone.finalproject.dto.ArticleDetailDto;
import tw.eeit175groupone.finalproject.dto.ArticleListDto;
import tw.eeit175groupone.finalproject.dto.ArticleSearchDto;
import tw.eeit175groupone.finalproject.service.ArticlesService;
import tw.eeit175groupone.finalproject.service.ImagesService;

@RestController
@RequestMapping("/articles")
@CrossOrigin
public class ArticlesController {
    @Autowired
    private ArticlesService articlesService;
    @Autowired
    private ImagesService imagesService;

    // 文章列表
    // @GetMapping("/find")
    // public ResponseEntity<?> findAll() {
    // List<ArticlesBean> findall = articlesService.findAll();
    // if (findall != null & !findall.isEmpty()) {
    // return ResponseEntity.ok().body(findall);
    // } else {
    // return ResponseEntity.notFound().build();
    // }
    // }

    // 文章列表
    // @GetMapping("/find")
    // public ResponseEntity<?> findAllArticlesWithUsers() {
    // List<Object[]> findall = articlesService.findAllArticlesWithUsers();
    // if (findall != null & !findall.isEmpty()) {
    // return ResponseEntity.ok().body(findall);
    // } else {
    // return ResponseEntity.notFound().build();
    // }
    // }

    // 文章列表
    @GetMapping("/find")
    public ResponseEntity<?> getArticleList() {
        ArticleListDto articleList = articlesService.findArticlesList();
        return ResponseEntity.ok(articleList);

    }

    // 關鍵字搜尋
    // @GetMapping("/search")
    // public ResponseEntity<?> searchKeyword(@RequestParam(required = false) String
    // keyword) {
    // List<ArticlesBean> search = articlesService.searchKeyword(keyword);
    // if (keyword != null && !keyword.isEmpty()) {
    // if (search != null && !search.isEmpty()) {
    // return ResponseEntity.ok().body(search);
    // } else {
    // return ResponseEntity.notFound().build();
    // }
    // } else {
    // return null;
    // }
    // }

    // 關鍵字搜尋
    @GetMapping("/search")
    public ResponseEntity<?> searchKeyword(@RequestParam(required = false) String keyword) {
        ArticleSearchDto search = articlesService.searchArticleList(keyword);
        if (keyword != null && !keyword.isEmpty()) {
            if (search != null && !search.getSearchResult().isEmpty()) {
                return ResponseEntity.ok().body(search);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.badRequest().body("請提供有效的搜尋關鍵字");
        }
    }

    // 文章頁面詳細內容
    @GetMapping("/detail/{articleId}")
    public ResponseEntity<?> getArticleDetail(@PathVariable Integer articleId) {
        ArticleDetailDto articleDetail = articlesService.getArticleDetail(articleId);
        return ResponseEntity.ok(articleDetail);

    }

    // 新增文章
    @PostMapping("/add")
    public ResponseEntity<?> addArticle(@RequestBody String body) {

        Integer newArticleId = articlesService.addArticle(body);
        JSONObject response = new JSONObject();
        if (newArticleId != null) {
            response.put("success", true);
            response.put("text", "文章已發布");
            response.put("articleId", newArticleId); // 將文章 ID 放入 JSON 對象中
            return ResponseEntity.ok().body(response.toString());
        } else {
            response.put("success", false);
            response.put("text", "發布失敗");
            return ResponseEntity.ok().body(response.toString());
        }
    }

    // 編輯文章
    @PutMapping("/edit")
    public ResponseEntity<?> editArticle(@RequestBody ArticlesBean article) {
        boolean updateArticle = articlesService.updateArticle(article);
        if (updateArticle) {
            return ResponseEntity.ok().body(article);
        } else {
            return ResponseEntity.ok().body("修改失敗");
        }
    }

    // 刪除文章
    @PutMapping("/hide/{articleId}")
    public ResponseEntity<?> deleteArticle(@PathVariable Integer articleId,@RequestBody Map<String,String> request) {
        System.out.println(request);
        boolean ishidden = articlesService.deleteArticle(articleId,request);
        if (ishidden) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    // 收藏文章
    @PostMapping("/collect")
    public ResponseEntity<?> collectArticle(@RequestBody String body) {
        boolean isCollected = articlesService.collectArticle(body);
        JSONObject response = new JSONObject();
        if (isCollected) {
            response.put("success", true);
            response.put("text", "收藏成功");
            return ResponseEntity.ok().body(response.toString());
        } else {
            response.put("success", false);
            response.put("text", "收藏失敗");
            return ResponseEntity.ok().body(response.toString());
        }
    }

    // 移除收藏
    @DeleteMapping("/deletecollection")
    public ResponseEntity<?> deleteCollectedArticle(@RequestParam(name = "articleId") Integer articleId,
            @RequestParam(name = "userId") Integer userId) {
        JSONObject response = new JSONObject();
        if (articlesService.deleteCollectedArticle(articleId, userId)) {
            response.put("scuccess", true);
            response.put("text", "取消收藏成功");
            return ResponseEntity.ok().body(response.toString());
        } else {
            response.put("scuccess", false);
            response.put("text", "取消收藏失敗");
            return ResponseEntity.ok(response.toString());
        }
    }

    // 文章列表
    @GetMapping("/findfiltered/{articleGameType}")
    public ResponseEntity<?> getArticleListFiltered(@PathVariable String articleGameType) {
        ArticleListDto articleList = articlesService.findArticlesListFiltered(articleGameType);
        return ResponseEntity.ok(articleList);

    }

}
