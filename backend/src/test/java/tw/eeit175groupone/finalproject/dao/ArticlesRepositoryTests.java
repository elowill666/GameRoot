package tw.eeit175groupone.finalproject.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tw.eeit175groupone.finalproject.domain.ArticlesBean;
import tw.eeit175groupone.finalproject.domain.CommentsBean;
import tw.eeit175groupone.finalproject.domain.UserBean;

@SpringBootTest
public class ArticlesRepositoryTests{
    @Autowired
    private ArticlesRepository articlesRepository;

    @Test
    public void testFindAll(){
        List<ArticlesBean> findAll=articlesRepository.findAll();
        findAll.stream()
                .filter(find -> find.getCreatedAt()==null)
                .forEach(find -> {
                    find.setCreatedAt(new Date(System.currentTimeMillis()-10L*60*60*24*1000));
                });
        articlesRepository.saveAll(findAll);
        for(ArticlesBean aaa:findAll){
            System.out.println(aaa);
        }

    }

    @Test
    public void testSearchKeyword(){
        List<ArticlesBean> searchResult=articlesRepository.searchKeyword("神魔");
        for(ArticlesBean search : searchResult){
            System.err.println("search = "+search);
        }
    }

    @Test
    public void testSearchKeywordList(){
        List<Object[]> searchResult=articlesRepository.searchKeywordList("神魔");
        for(Object[] search : searchResult){
            System.err.println("search = "+search);
        }
    }

    @Test
    public void testFindArticleById(){
        ArticlesBean article=articlesRepository.findArticleByArticlesId(1);
        System.err.println("article = "+article);
    }

    @Test
    public void testFindUserByArticlesId(){
        UserBean user=articlesRepository.findUserByArticlesId(1);
        System.err.println("user = "+user);
    }

    @Test
    public void testFindAllArticlesWithUsers(){
        List<Object[]> allArticles=articlesRepository.findAllArticlesWithUsers();
        System.err.println("allArticles="+allArticles);
    }

    @Test
    public void testHideArticle(){
        articlesRepository.deleteArticle(43);
    }

    @Test
    public void testFindProductIdByProductName(){
        Integer result=articlesRepository.findProductIdByProductName("霍格華茲的傳承");
        System.err.println(result);
    }


    @Test
    public void testcountPersonalPost(){
        List<Integer> okok=new ArrayList<>();
        okok.add(1);
        okok.add(2);
        okok.add(3);
        okok.add(4);
        okok.add(5);
        okok.add(6);
        okok.add(7);
        okok.add(8);
        
        List<Object[]> tempArti=articlesRepository.countPersonalPost(okok);
        for(Object[] temp:tempArti){
            System.err.println(temp[0]);
            System.err.println(temp[1]);
        }
    }

}
