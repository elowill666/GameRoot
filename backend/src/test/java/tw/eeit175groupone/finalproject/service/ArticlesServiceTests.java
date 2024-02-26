package tw.eeit175groupone.finalproject.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.minidev.json.JSONObject;
import tw.eeit175groupone.finalproject.domain.ArticlesBean;
import tw.eeit175groupone.finalproject.dto.ArticleDetailDto;
import tw.eeit175groupone.finalproject.dto.ArticleListDto;
import tw.eeit175groupone.finalproject.dto.ArticleSearchDto;

@SpringBootTest
public class ArticlesServiceTests {
    @Autowired
    private ArticlesService articlesService;

    @Test
    public void testFindAll() {
        List<ArticlesBean> allArticles = articlesService.findAll();
        for (ArticlesBean article : allArticles) {
            System.err.println("article = " + article);
        }
    }

    @Test
    public void testSearchKeyword() {
        List<ArticlesBean> results = articlesService.searchKeyword("新手");
        for (ArticlesBean result : results) {
            System.err.println("result = " + result);
        }
    }

    @Test
    public void testGetArticleWithComments() {
        ArticleDetailDto result = articlesService.getArticleDetail(1);
        System.err.println("result = " + result);
    }

    // @Test
    // public void testAddArticle() {
    // JSONObject obj = new JSONObject();
    // obj.put("userId", 1);
    // obj.put("articleGameType", "神魔之塔");
    // obj.put("articleHead", "測試好玩遊戲");
    // obj.put("articleText", "好玩遊戲");
    // obj.put("articleType", "product");
    // Integer result = articlesService.addArticle(obj.toString());
    // System.err.println(result);
    // }

    @Test
    public void testGetArticleDetail() {
        ArticleDetailDto result = articlesService.getArticleDetail(2);
        System.err.println("result=" + result);
    }

    @Test
    public void testFindArticlesList() {
        ArticleListDto allArticles = articlesService.findArticlesList();
        System.err.println("allArticles=" + allArticles);
    }

    @Test
    public void testSearchArticleList() {
        ArticleSearchDto search = articlesService.searchArticleList("問題");
        System.err.println("search=" + search);
    }

    @Test
    public void testUpdateArticle() {
        ArticlesBean bean = new ArticlesBean();
        bean.setArticlesId(52);
        bean.setArticleHead("早安哈囉");
        bean.setArticleText("再次測試更新");
        boolean result = articlesService.updateArticle(bean);
        System.err.println("result=" + result);
    }

    @Test
    public void testHideArticle() {
        Map<String, String> test = new HashMap<>();
        test.put("member", "admin");
        boolean result = articlesService.deleteArticle(45, test);
        System.err.println(result);
    }

    @Test
    public void testCollectArticle() {
        JSONObject obj = new JSONObject();
        obj.put("articlesId", 1);
        obj.put("userId", 3);
        boolean result = articlesService.collectArticle(obj.toString());
        System.err.println(result);
    }

    @Test
    public void testDeleteCollectedArticle() {
        boolean result = articlesService.deleteCollectedArticle(2, 3);
        System.err.println(result);
    }

}
