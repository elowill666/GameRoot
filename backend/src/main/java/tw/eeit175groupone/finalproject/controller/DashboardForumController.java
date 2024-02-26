package tw.eeit175groupone.finalproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.eeit175groupone.finalproject.domain.ArticlesBean;
import tw.eeit175groupone.finalproject.domain.CommentsBean;
import tw.eeit175groupone.finalproject.dto.DashboardArticle;
import tw.eeit175groupone.finalproject.dto.DashboardComment;
import tw.eeit175groupone.finalproject.dto.DashboardReport;
import tw.eeit175groupone.finalproject.service.DashboardForumService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/dashboard/forum")
@CrossOrigin
public class DashboardForumController{
    @Autowired
    private DashboardForumService dashboardForumService;

    //接收請求後丟出全部的文章資訊給前端管理者
    @PostMapping("/article/find")
    public ResponseEntity<?> findAllArticles(@RequestBody DashboardArticle request){
        System.err.println(request);
        if(request!=null){
            Map<String, Object> allOrder=dashboardForumService.findAllArticles(request);
            if(allOrder!=null && !allOrder.isEmpty()){
                return ResponseEntity.ok().body(allOrder);
            }
        }
        return null;
    }

    @GetMapping("/article/{userId}")
    public ResponseEntity<?> findArticlesByUserId(@PathVariable("userId") Integer userId){
        List<DashboardArticle> allArticle=dashboardForumService.findArticleByUserId(userId);
        if(allArticle!=null && !allArticle.isEmpty()){
            System.out.println(allArticle);
            return ResponseEntity.ok().body(allArticle);
        }
        return null;
    }


    //接收請求後丟出某個文章的留言數給前端管理者
    //之後再做修改
    @GetMapping("/article/find/commentnumber/{articleId}")
    public ResponseEntity<?> findAllCommentnumber(@PathVariable(name="articleId") Integer articleId){
        Integer commentNumber=dashboardForumService.findCommentNumberOfArticle(articleId);
        if(commentNumber!=null){
            return ResponseEntity.ok().body(commentNumber);
        }
        return ResponseEntity.notFound().build();
    }

    //接收請求後丟出某個文章的被檢舉數給前端管理者
    @GetMapping("/article/find/reportnumber/{articleId}")
    public ResponseEntity<?> findAllReportnumber(@PathVariable(name="articleId") Integer articleId){
        Integer reportNumber=dashboardForumService.findReportNumberOfArticle(articleId);
        if(reportNumber!=null){
            return ResponseEntity.ok().body(reportNumber);
        }
        return ResponseEntity.notFound().build();
    }

    //接收請求得到文章ID後取得文章內容給前端管理者
    @GetMapping("/article/text/{articleId}")
    public ResponseEntity<String> findArticleByArticlesId(@PathVariable(name="articleId") Integer articleId){
        String text=dashboardForumService.findArticleByArticlesId(articleId);
        if(text!=null && text.length()!=0){
            return ResponseEntity.ok().body(text);
        }
        return ResponseEntity.notFound().build();
    }

    //收到前端批次的儲存請求更新文章狀態，因前端收到ResponseEntity.ok()直接重載所有資料
    //所以傳success字串就好
    @PutMapping("/article/saveall")
    public ResponseEntity<?> modifyAllArticle(@RequestBody List<DashboardArticle> request){
        if(request!=null && !request.isEmpty()){
            List<ArticlesBean> result=dashboardForumService.saveAllArticle(request);
            if(result!=null && !result.isEmpty()){
                return ResponseEntity.ok().body(result);
            }
        }
        return ResponseEntity.notFound().build();
    }

    //收到前端傳來的某個文章id及更改狀態，以此資料更新文章狀態
    //回傳更改後的狀態給前端
    @PutMapping("/article/{articleId}")
    public ResponseEntity<?> modifyArticlebyId(@PathVariable Integer articleId,@RequestBody DashboardArticle request){
        System.err.println("artmod="+request);
        if(request!=null){
            ArticlesBean result=dashboardForumService.saveArticleById(request);
            if(result!=null){
                System.out.println(result);
                return ResponseEntity.ok().body(result);
            }
        }
        return ResponseEntity.notFound().build();
    }


    //-------------------------------comment------------------------------------
    //收到前端的搜尋請求回傳前端請求的留言資料
    @PostMapping("/comment/find")
    public ResponseEntity<?> findAllComment(@RequestBody DashboardComment request){
        if(request!=null){
            Map<String, Object> allComments=dashboardForumService.findAllComments(request);
            if(allComments!=null && !allComments.isEmpty()){
                return ResponseEntity.ok().body(allComments);
            }
        }
        return ResponseEntity.noContent().build();
    }
    //收到前端的留言狀態更新請求，進行留言更新
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<?> findAllComment(@PathVariable("commentId") Integer commentId,@RequestBody CommentsBean request){
        if(request!=null){
            CommentsBean result=dashboardForumService.modifyCommentById(request);
            if(result!=null){
                return ResponseEntity.ok().body(result);
            }
        }
        return ResponseEntity.noContent().build();
    }
    //收到前端的批次留言狀態更新請求，進行留言更新
    @PutMapping("/comment/saveall")
    public ResponseEntity<?> modifyAllComment(@RequestBody List<CommentsBean> request){
        if(request!=null && !request.isEmpty()){
            List<CommentsBean> result=dashboardForumService.modifyAllComment(request);
            if(result!=null && !result.isEmpty()){
                return ResponseEntity.ok().body(result);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/comment/text/{commentId}")
    public ResponseEntity<?> findCommentTextById(@PathVariable(name="commentId") Integer commentId){
        String text=dashboardForumService.findCommentTextById(commentId);
        if(text!=null && text.length()!=0){
            return ResponseEntity.ok().body(text);
        }
        return ResponseEntity.notFound().build();
    }

    //-----------------------------report-----------------------------------------
    @PostMapping("/report/find")
    public ResponseEntity<?> findAllReports(){
        List<DashboardReport> allReports=dashboardForumService.findAllReports();
        if(allReports!=null && !allReports.isEmpty()){
            return ResponseEntity.ok().body(allReports);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/report/{userId}")
    public ResponseEntity<?> findAllReports(@PathVariable("userId") Integer userId){
        List<DashboardReport> allReports=dashboardForumService.findReportByUserId(userId);
        if(allReports!=null && !allReports.isEmpty()){
            return ResponseEntity.ok().body(allReports);
        }
        return ResponseEntity.notFound().build();
    }


    //收到前端批次的儲存請求更新檢舉狀態，因前端收到ResponseEntity.ok()直接重載所有資料
    //所以傳success字串就好
    @PutMapping("/report/saveall")
    public ResponseEntity<String> modifyAllReport(@RequestBody List<DashboardReport> request){
        if(request!=null && !request.isEmpty()){
            String result=dashboardForumService.modifyAllReport(request);
            if(result!=null && result.length()!=0){
                return ResponseEntity.ok().body("success");
            }
        }
        return ResponseEntity.notFound().build();
    }

    //收到前端傳來的某個文章id及更改狀態，以此資料更新文章狀態
    //回傳更改後的狀態給前端
    @PutMapping("/report/{reportId}")
    public ResponseEntity<String> modifyReportbyId(@PathVariable Integer reportId,@RequestBody DashboardReport request){
        System.out.println(request);
        if(reportId!=null && request!=null){
            String result=dashboardForumService.moodifyReportById(reportId,request);
            if(result!=null && result.length()!=0){
                return ResponseEntity.ok().body(result);
            }
        }
        return ResponseEntity.notFound().build();
    }


}
