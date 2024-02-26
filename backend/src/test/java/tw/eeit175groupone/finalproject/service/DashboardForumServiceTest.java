package tw.eeit175groupone.finalproject.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tw.eeit175groupone.finalproject.domain.ArticlesBean;
import tw.eeit175groupone.finalproject.domain.ReportBean;
import tw.eeit175groupone.finalproject.dto.DashboardArticle;
import tw.eeit175groupone.finalproject.dto.DashboardComment;
import tw.eeit175groupone.finalproject.dto.DashboardReport;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class DashboardForumServiceTest{
    @Autowired
    private DashboardForumService dashboardForumService;

    @Test
    public void findAllArticlesTest(){
        DashboardArticle request=new DashboardArticle();
        request.setUserId(1);
        request.setStart(1);
        request.setRows(10);
        Map<String,Object> tempmap=dashboardForumService.findAllArticles(request);
        List<Object> data=Arrays.asList(tempmap.get("data"));

        for(Object temp : data){
            System.out.println(temp);
        }
    }




    @Test
    public void findCommentNumberOfArticleTest(){
        Integer commentNumber=dashboardForumService.findCommentNumberOfArticle(1);
        System.out.println(commentNumber);
    }

    @Test
    public void findReportNumberOfArticleTest(){
        Integer reportsNumber=dashboardForumService.findReportNumberOfArticle(1);
        System.out.println(reportsNumber);
    }

    @Test
    public void findArticleByArticlesIdTest(){
        String text=dashboardForumService.findArticleByArticlesId(1);
        System.out.println(text);
    }


    @Test
    public void saveArticleByIdTest(){
       DashboardArticle temp=new DashboardArticle();
       temp.setStatus("banned");
temp.setArticlesId(11);
       ArticlesBean s=dashboardForumService.saveArticleById(temp);
        System.out.println(s);

    }


    //-----------------Comment------------------------------


    @Test
    public void findAllCommentTest(){
        DashboardComment temp=new DashboardComment();
        temp.setRows(10);
        temp.setStart(1);
        temp.setUserId(1);
        
        Map<String,Object> allComments=dashboardForumService.findAllComments(temp);
        System.out.println(allComments.get("totalcount"));
    }

    @Test
    public void countCommentsNumberGroupByArticlesIdTest(){
        Map<Integer, Integer> map=dashboardForumService.countCommentsNumberGroupByArticlesId();
        System.out.println(map);
    }

    @Test
    public void findCommentTextByIdTest(){
        String commentTextById=dashboardForumService.findCommentTextById(1);
        System.out.println(commentTextById);
    }

//-------------------------------report--------------------------------------
    @Test
    public void findAllReportsTest(){
        List<DashboardReport> allReports=dashboardForumService.findAllReports();
        System.out.println(allReports);

    }

    @Test
    public void checkForReportComfireTest(){
        ReportBean temp=dashboardForumService.checkForReportComfired(15,3,"comment");
        System.out.println(temp);

    }


    @Test
    public void cancelReportBannedTest(){
        ReportBean temp=dashboardForumService.cancelReportBanned(1,2,"article");
        System.out.println(temp);

    }

}
