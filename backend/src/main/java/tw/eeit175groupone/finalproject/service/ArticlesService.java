package tw.eeit175groupone.finalproject.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.hibernate.id.IntegralDataTypeHolder;
import org.hibernate.mapping.Collection;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.eeit175groupone.finalproject.dao.ArticlesRepository;
import tw.eeit175groupone.finalproject.dao.CollectionsRepository;
import tw.eeit175groupone.finalproject.dao.CommentsRepository;
import tw.eeit175groupone.finalproject.dao.ImagesRepository;
import tw.eeit175groupone.finalproject.dao.LikesRepository;
import tw.eeit175groupone.finalproject.dao.ProductArticlesRepository;
import tw.eeit175groupone.finalproject.domain.ArticlesBean;
import tw.eeit175groupone.finalproject.domain.CollectionsBean;
import tw.eeit175groupone.finalproject.domain.ImagesBean;
import tw.eeit175groupone.finalproject.domain.ProductArticlesBean;
import tw.eeit175groupone.finalproject.domain.UserBean;
import tw.eeit175groupone.finalproject.dto.ArticleDetailDto;
import tw.eeit175groupone.finalproject.dto.ArticleListDto;
import tw.eeit175groupone.finalproject.dto.ArticleSearchDto;
import tw.eeit175groupone.finalproject.dto.DashboardArticle;

@Service
@Transactional
public class ArticlesService{
    @Autowired
    private ArticlesRepository articlesRepository;
    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private ImagesRepository imagesRepository;
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private CollectionsRepository collectionsRepository;
    @Autowired
    private ProductArticlesRepository productArticlesRepository;
    
    @Autowired
    private DashboardForumService dashboardForumService;

    /**
     * 列出所有文章(文章列表)
     */
    public List<ArticlesBean> findAll(){
        List<ArticlesBean> beans=articlesRepository.findAll();
        if(beans!=null && !beans.isEmpty()){
            return beans;
        } else{
            return null;
        }
    }

    /**
     * 列出所有文章(文章列表)(join UserBean)
     *
     * @return
     */
    public List<Object[]> findAllArticlesWithUsers(){
        List<Object[]> allArticles=articlesRepository.findAllArticlesWithUsers();
        if(allArticles!=null && !allArticles.isEmpty()){
            return allArticles;
        } else{
            return null;
        }
    }

    /**
     * 列出文章列表(含使用者資料,按讚數,留言數)
     *
     * @return
     */
    public ArticleListDto findArticlesList(){
        ArticleListDto result=new ArticleListDto();
        // 文章列表(含使用者資料)
        List<Object[]> articleList=articlesRepository.findAllArticlesWithUsers();
        if(articleList!=null){
            result.setArticleList(articleList);
            // 每篇文章的留言數量
            List<Object[]> articleCommentsNumber=commentsRepository.countCommentsNumberEachArticle();
            Map<Integer, Integer> commentsnumberMap=new HashMap<>();
            for(Object[] detail : articleCommentsNumber){
                Integer articleId=(Integer) detail[0]; // 文章的id
                Integer commentNumber=((Number) detail[1]).intValue(); // 留言數量
                commentsnumberMap.put(articleId,commentNumber);
            }
            // 檢查是否有文章沒有留言，並將其留言數量設為0
            List<Integer> allArticleIds=articlesRepository.findAllArticlesId();
            for(Integer articleId : allArticleIds){
                if(!commentsnumberMap.containsKey(articleId)){
                    commentsnumberMap.put(articleId,0);
                }
            }
            result.setCommentsnumberMap(commentsnumberMap);
            // 每篇文章的按讚數量
            List<Object[]> articleLikesNumber=likesRepository.countLikesNumberEachArticle();
            Map<Integer, Integer> likesnumberMap=new HashMap<>();
            for(Object[] detail : articleLikesNumber){
                Integer articleId=(Integer) detail[0]; // 文章的id
                Integer likeNumber=((Number) detail[1]).intValue(); // 按讚數量
                likesnumberMap.put(articleId,likeNumber);
            }
            result.setLikesnumberMap(likesnumberMap);

        }
        return result;
    }

    /**
     * 搜尋關鍵字,搜尋範圍包含文章標題與內文
     *
     * @param keyword
     * @return List<ArticlesBean>
     */
    public List<ArticlesBean> searchKeyword(String keyword){
        List<ArticlesBean> beans=articlesRepository.searchKeyword(keyword);
        return beans.isEmpty() ? null: beans;
    }

    /**
     * 搜尋關鍵字,範圍包含文章標題與內文,搜尋結果資訊較詳盡
     *
     * @param keyword
     * @return
     */
    public ArticleSearchDto searchArticleList(String keyword){
        ArticleSearchDto result=new ArticleSearchDto();
        // 搜尋結果:文章列表(含使用者資料)
        List<Object[]> searchResult=articlesRepository.searchKeywordList(keyword);
        // 確保searchResult不為null
        if(searchResult==null){
            searchResult=new ArrayList<>(); // 初始化為一個空的列表
        }
        result.setSearchResult(searchResult);
        if(!searchResult.isEmpty()){
            // 每篇文章的留言數量
            List<Object[]> articleCommentsNumber=commentsRepository.countSearchCommentsNumber(keyword);
            if(articleCommentsNumber!=null && !articleCommentsNumber.isEmpty()){
                Map<Integer, Integer> searchcommentsnumberMap=new HashMap<>();
                for(Object[] detail : articleCommentsNumber){
                    Integer articleId=(Integer) detail[0]; // 文章的id
                    Integer commentNumber=((Number) detail[1]).intValue(); // 留言數量
                    searchcommentsnumberMap.put(articleId,commentNumber);
                }
                result.setSearchcommentsnumberMap(searchcommentsnumberMap);
                // 檢查是否有文章沒有留言，並將其留言數量設為0
                List<Integer> allArticleIds=articlesRepository.findKeywordArticlesId(keyword);
                for(Integer articleId : allArticleIds){
                    if(!searchcommentsnumberMap.containsKey(articleId)){
                        searchcommentsnumberMap.put(articleId,0);
                    }
                }
                result.setSearchcommentsnumberMap(searchcommentsnumberMap);
            } else{
                result.setSearchcommentsnumberMap(new HashMap<>());
            }
            // 每篇文章的按讚數量
            List<Object[]> articleLikesNumber=likesRepository.countSearchLikesNumber(keyword);
            if(articleLikesNumber!=null && !articleLikesNumber.isEmpty()){
                Map<Integer, Integer> searchlikesnumberMap=new HashMap<>();
                for(Object[] detail : articleLikesNumber){
                    Integer articleId=(Integer) detail[0]; // 文章的id
                    Integer likeNumber=((Number) detail[1]).intValue(); // 按讚數量
                    searchlikesnumberMap.put(articleId,likeNumber);
                }
                result.setSearchlikesnumberMap(searchlikesnumberMap);
            } else{
                result.setSearchlikesnumberMap(new HashMap<>());
            }
        }
        return result;
    }

    /**
     * 根據文章(articleId),找出文章內容+留言
     *
     * @param articleId
     */
    public ArticleDetailDto getArticleDetail(Integer articleId){
        ArticleDetailDto result=new ArticleDetailDto();

        // 查詢文章
        ArticlesBean article=articlesRepository.findArticleByArticlesId(articleId);
        if(article!=null){
            result.setArticle(article);
            // 查詢文章圖片
            List<ImagesBean> imgs=imagesRepository.findByArticlesId(articleId);
            result.setImages(imgs);
            // 查詢發文者資料
            UserBean articleuser=articlesRepository.findUserByArticlesId(articleId);
            result.setArticleuser(articleuser);
            // 查詢留言數
            Integer commentsnumber=commentsRepository.countCommentsNumberByArticleId(articleId);
            result.setCommentsnumber(commentsnumber);
            // 查詢按讚數
            Integer likesnumber=likesRepository.countLikesNumberByArticleId(articleId);
            result.setLikesnumber(likesnumber);
            // 查詢留言詳細訊息
            List<Object[]> commentsdetail=commentsRepository.findCommentsDetailByArticlesId(articleId);
            result.setCommentsdetail(commentsdetail);
            // 查詢留言的圖片
            List<ImagesBean> commentsimgs=imagesRepository.findAllCommentsImagesByArticlesId(articleId);
            result.setCommentsimages(commentsimgs);
            // 查詢留言的按讚數(使用Map來儲存留言id和對應的按讚數)
            List<Object[]> commentlikesdetail=likesRepository.countCommentLikesNumberByArticlesId(articleId);
            result.setCommentlikesdetail(commentlikesdetail);
            Map<Integer, Integer> commentLikesMap=new HashMap<>();
            for(Object[] detail : commentlikesdetail){
                Integer commentId=(Integer) detail[0]; // 留言的id
                Integer likesNumber=((Number) detail[1]).intValue(); // 按讚數量
                commentLikesMap.put(commentId,likesNumber);
            }
            result.setCommentLikesMap(commentLikesMap);
        }
        return result;
    }

    /**
     * 新增文章
     *
     * @param body
     * @return 新文章的id
     */
    public Integer addArticle(String body){
        JSONObject obj=new JSONObject(body);
        Integer userId=obj.isNull("userId") ? null: obj.getInt("userId");
        String articleGameType=obj.isNull("subCategory") ? null: obj.getString("subCategory");
        String articleHead=obj.isNull("title") ? "default": obj.getString("title");
        String articleText=obj.isNull("content") ? null: obj.getString("content");
        String articleType=obj.isNull("mainCategory") ? null: obj.getString("mainCategory");
        String articleStatus=obj.isNull("status") ? "normal": obj.getString("status");

        Date createdAt=new Date();
        ArticlesBean artbean=new ArticlesBean();
        artbean.setUserId(userId);
        artbean.setArticleGameType(articleGameType);
        artbean.setArticleHead(articleHead);
        artbean.setArticleText(articleText);
        artbean.setCreatedAt(createdAt);
        artbean.setArticleType(articleType);
        artbean.setStatus(articleStatus);
        ArticlesBean saveArticle=articlesRepository.save(artbean);
        // 也存入ProductArticleBean
        ProductArticlesBean partbean=new ProductArticlesBean();
        Integer articlesid=saveArticle.getArticlesId();
        Integer productId=articlesRepository.findProductIdByProductName(articleGameType);
        partbean.setProductId(productId);
        partbean.setArticlesId(articlesid);
        partbean.setShortInfo(articleText);
        productArticlesRepository.save(partbean);

        // 新增文章後返回文章的articlesId
        return saveArticle.getArticlesId();
    }

    /**
     * 編輯文章
     *
     * @param article
     * @return
     */
    public boolean updateArticle(ArticlesBean article){
        Optional<ArticlesBean> articleById=articlesRepository.findById(article.getArticlesId());
        if(articleById.isPresent()){
            article.setUpdateAt(new Date());
            articlesRepository.updateArticle(
                    article.getArticlesId(),
                    article.getArticleHead(),
                    article.getArticleText(),
                    article.getUpdateAt());
            return true;
        }
        return false;
    }

    /**
     * 刪除文章
     *
     * @param articlesId
     * @return
     */
    public boolean deleteArticle(Integer articlesId,Map<String, String> request){
        Optional<ArticlesBean> article=articlesRepository.findById(articlesId);
        if(article.isPresent()){
            if(request.containsKey("member") && "admin".equals(request.get("member"))){
                DashboardArticle temp =new DashboardArticle();
                temp.setArticlesId(articlesId);
                temp.setStatus("banned");
              dashboardForumService.saveArticleById(temp);
            } else{
                articlesRepository.deleteArticle(articlesId);
            }
            return true;
        } else{
            return false;
        }
    }

    /**
     * 收藏文章
     *
     * @param body
     * @return
     */
    public boolean collectArticle(String body){
        JSONObject obj=new JSONObject(body);
        Integer articlesId=obj.isNull("articlesId") ? null: obj.getInt("articlesId");
        Integer userId=obj.isNull("userId") ? null: obj.getInt("userId");

        CollectionsBean collection=new CollectionsBean();
        collection.setArticleId(articlesId);
        collection.setUserId(userId);
        CollectionsBean collectArticle=collectionsRepository.save(collection);
        if(collectArticle!=null && collectArticle.getCollectionId()!=null){
            return true;
        }
        return false;
    }

    /**
     * 取消收藏
     *
     * @param articleId
     * @param userId
     * @return
     */
    public boolean deleteCollectedArticle(Integer articleId,Integer userId){
        if(articleId!=null && userId!=null){
            collectionsRepository.deleteCollection(articleId,userId);
            return true;
        }
        return false;
    }

    /**
     * 列出文章列表(含使用者資料,按讚數,留言數)
     *
     * @return
     */
    public ArticleListDto findArticlesListFiltered(String articleGameType){
        ArticleListDto result=new ArticleListDto();
        // 文章列表(含使用者資料)
        List<Object[]> articleList=articlesRepository.findAllArticlesWithUsersFiltered(articleGameType);
        if(articleList!=null){
            result.setArticleList(articleList);
            // 每篇文章的留言數量
            List<Object[]> articleCommentsNumber=commentsRepository.countCommentsNumberEachArticle();
            Map<Integer, Integer> commentsnumberMap=new HashMap<>();
            for(Object[] detail : articleCommentsNumber){
                Integer articleId=(Integer) detail[0]; // 文章的id
                Integer commentNumber=((Number) detail[1]).intValue(); // 留言數量
                commentsnumberMap.put(articleId,commentNumber);
            }
            // 檢查是否有文章沒有留言，並將其留言數量設為0
            List<Integer> allArticleIds=articlesRepository.findAllArticlesId();
            for(Integer articleId : allArticleIds){
                if(!commentsnumberMap.containsKey(articleId)){
                    commentsnumberMap.put(articleId,0);
                }
            }
            result.setCommentsnumberMap(commentsnumberMap);
            // 每篇文章的按讚數量
            List<Object[]> articleLikesNumber=likesRepository.countLikesNumberEachArticle();
            Map<Integer, Integer> likesnumberMap=new HashMap<>();
            for(Object[] detail : articleLikesNumber){
                Integer articleId=(Integer) detail[0]; // 文章的id
                Integer likeNumber=((Number) detail[1]).intValue(); // 按讚數量
                likesnumberMap.put(articleId,likeNumber);
            }
            result.setLikesnumberMap(likesnumberMap);

        }
        return result;
    }

}
