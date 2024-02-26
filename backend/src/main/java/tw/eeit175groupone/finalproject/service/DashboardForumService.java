package tw.eeit175groupone.finalproject.service;

import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.eeit175groupone.finalproject.dao.*;
import tw.eeit175groupone.finalproject.domain.ArticlesBean;
import tw.eeit175groupone.finalproject.domain.CommentsBean;
import tw.eeit175groupone.finalproject.domain.ReportBean;
import tw.eeit175groupone.finalproject.dto.DashboardArticle;
import tw.eeit175groupone.finalproject.dto.DashboardComment;
import tw.eeit175groupone.finalproject.dto.DashboardReport;
import tw.eeit175groupone.finalproject.util.DatetimeConverter;

import java.util.*;

@Transactional(rollbackFor={Exception.class})
@Service
public class DashboardForumService{

    @Autowired
    private ArticlesRepository articlesRepository;
    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private DashboardUserService dashboardUserService;

    /**
     * 找出所有的Articles
     */
    public Map<String, Object> findAllArticles(DashboardArticle request){
        List<DashboardArticle> allart=new ArrayList<>();
        Map<String, Object> result=new HashMap<>();
        List<ArticlesBean> temp=this.dynamicFindArticle(request);
        int startRow=1;
        int totalRows=10;
        result.put("totalcount",temp.size());
        if(temp==null){
            return null;
        } else if(temp.isEmpty()){
            result.put("data","nodata");
            return result;
        }
        Map<Integer, Integer> commentsmap=countCommentsNumberGroupByArticlesId();
//        Map<Integer, Integer> reportsmap=countReportsNumberForArticle();
        //這裡是將檢舉數跟留言數塞進dto
        for(ArticlesBean tempbean : temp){
            DashboardArticle dashboardArticle=new DashboardArticle();
            BeanUtils.copyProperties(tempbean,dashboardArticle);
            dashboardArticle.setCommentsNumber(
                    (commentsmap.getOrDefault(dashboardArticle.getArticlesId(),0)));
//            dashboardArticle.setReportsNumber(
//                    (reportsmap.getOrDefault(dashboardArticle.getArticlesId(),0)));
            allart.add(dashboardArticle);
        }

        if(request.getCommentsNumber()!=null){
            allart=allart.stream()
                    .filter(all -> all.getCommentsNumber()>=request.getCommentsNumber())
                    .toList();
        }
        allart=this.articleSort(request,allart);

        startRow=(request.getStart()-1)*request.getRows();
        totalRows=request.getStart()*request.getRows();
        if(totalRows>temp.size()){
            totalRows=temp.size();
        }
        allart=allart.stream()
                .skip(startRow)
                .limit(totalRows-startRow)
                .toList();


        if(allart!=null && !allart.isEmpty()){
            result.put("data",allart);
            return result;
        }

        return null;
    }

    /**
     * 找出某個會員發的文章
     *
     * @param userId
     * @return
     */
    public List<DashboardArticle> findArticleByUserId(Integer userId){
        List<ArticlesBean> temp=articlesRepository.findByUserId(userId);
        List<DashboardArticle> allart=new ArrayList<>();
        if(temp!=null && !temp.isEmpty()){
            Map<Integer, Integer> commentsmap=countCommentsNumberGroupByArticlesId();
            Map<Integer, Integer> reportsmap=countReportsNumberForArticle();
            for(ArticlesBean tempbean : temp){
                DashboardArticle dashboardArticle=new DashboardArticle();
                BeanUtils.copyProperties(tempbean,dashboardArticle);
                dashboardArticle.setCommentsNumber(
                        (commentsmap.getOrDefault(dashboardArticle.getArticlesId(),0)));
                dashboardArticle.setReportsNumber(
                        (reportsmap.getOrDefault(dashboardArticle.getArticlesId(),0)));
                allart.add(dashboardArticle);
            }
            if(allart!=null && !allart.isEmpty()){
                return allart;
            }
        }
        return null;
    }


    /**
     * 查詢各文章的留言數
     *
     * @return Map<Integer,Integer>--前面裝articleId，後面裝留言數
     */
    public Map<Integer, Integer> countCommentsNumberGroupByArticlesId(){
        List<Object[]> tempcommentNumber=commentsRepository.countCommentsNumberGroupByArticlesId();
        Map<Integer, Integer> commentNumber=new HashMap<>();
        if(tempcommentNumber!=null){
            for(Object[] temp : tempcommentNumber){
                commentNumber.put((Integer) temp[0],((Long) temp[1]).intValue());
            }
            return commentNumber;
        }
        return null;
    }

    /**
     * 查詢各文章的留言數
     *
     * @return Map<Integer,Integer>--前面裝articleId，後面裝留言數
     */
    public Map<Integer, Integer> countReportsNumberForArticle(){
        List<Object[]> tempreportNumber=reportRepository.countReportsNumberForArticle();
        Map<Integer, Integer> reportNumber=new HashMap<>();
        if(tempreportNumber!=null){
            for(Object[] temp : tempreportNumber){
                reportNumber.put((Integer) temp[0],((Long) temp[1]).intValue());
            }
            return reportNumber;
        }
        return null;
    }


    /**
     * 找出某個文章有多少留言
     *
     * @param articleId--Integer
     * @return CommentNumber of Article--Integer
     */
    public Integer findCommentNumberOfArticle(Integer articleId){
        Integer commentNumber=commentsRepository.countCommentsNumberByArticleId(articleId);
        if(commentNumber!=null){
            return commentNumber;
        }
        return null;
    }

    /**
     * 找出某個文章被檢舉幾次
     *
     * @param articleId--Integer
     * @return ReportsNumber of Article--Integer
     */
    public Integer findReportNumberOfArticle(Integer articleId){
        Integer reportsNumber=reportRepository.countReportsNumberById(articleId);
        if(reportsNumber!=null){
            return reportsNumber;
        }
        return null;
    }

    /**
     * 用文章ID找對應的文章內容
     *
     * @param articlesId
     * @return ArticleTest--String 文章內容
     */

    public String findArticleByArticlesId(Integer articlesId){
        ArticlesBean temp=articlesRepository.findById(articlesId).orElse(null);
        if(temp!=null){
            String articleText=temp.getArticleText();
            return articleText;
        }
        return null;
    }

    /**
     * 取得前端改動文章的文章id及文章狀態拿來更新文章的資訊
     *
     * @param request--List<DashboardArticle> 第一項Integer為ArticleId 第二項為改變後的文章狀態
     * @return Map<Integer,String>--把更新後的數據回傳
     */
    public List<ArticlesBean> saveAllArticle(List<DashboardArticle> request){

        List<ArticlesBean> result=new ArrayList<>();
        for(DashboardArticle temprequest : request){
            ArticlesBean temp=new ArticlesBean();
            temp=this.saveArticleById(temprequest);
            result.add(temp);
        }
        if(result!=null && !result.isEmpty()){
            return result;
        }
        return null;
    }

//    public String saveAllArticle(Map<Integer, String> allArticle){
//        //取出所有的KEY
//        Set<Integer> allKeys=allArticle.keySet();
//        if(allKeys!=null && !allKeys.isEmpty()){
//            //將取出的keys找資料庫
//            List<ArticlesBean> findAllById=articlesRepository.findAllById(allKeys);
//            if(findAllById!=null && !findAllById.isEmpty()){
//                //確認有取出資料，將取出的資料更新
//                //用Map的getOrDefault()方法，如果value為零把原本的值塞回去
//                for(int i=0; i<findAllById.size(); i++){
//                    findAllById.get(i).setStatus(
//                            allArticle.getOrDefault(
//                                    findAllById.get(i).getArticlesId(),findAllById.get(i).getStatus()
//                            ));
//                    findAllById.get(i).setUpdateAt(new Date());
//                }
//                //全部更新完成，存入資料庫
//                List<ArticlesBean> tempresult=articlesRepository.saveAll(findAllById);
//                if(tempresult!=null && !tempresult.isEmpty()){
//                    //前端會重載資料所以直接回傳字串
//                    return "success";
//                }
//            }
//
//        }
//        return null;
//    }

    /**
     * 取得前端改動文章的文章資訊拿來更新文章的資訊
     *
     * @param request--DashboardArticle
     * @return String--把更新後的狀態回傳及更新時間
     */
    public ArticlesBean saveArticleById(DashboardArticle request){
        String changeStatus=request.getStatus();
        Integer articleId=request.getArticlesId();
        //確認在資料庫有此文章，且回傳的狀態文字長度不為0
        if(articlesRepository.existsById(articleId) && changeStatus.length()!=0){
            //取出來不為null值，直接將資料用ArticlesBean接
            ArticlesBean temp=articlesRepository.findById(articleId).orElse(null);
            if(temp==null){
                return null;
            }
            if(changeStatus.equals(temp.getStatus())){
                return null;
            }
            temp.setStatus(changeStatus);
            temp.setUpdateAt(new Date());
            //改完資料存回去
            ArticlesBean save=articlesRepository.save(temp);
            if(save!=null){
                if("banned".equals(save.getStatus())){
                    if(this.allBanReportArticle(save.getArticlesId())==null){
                        return null;
                    }
                    if(dashboardUserService.banUserBecauseForum(save.getUserId(),"文章違規")){
                        return save;
                    }
                    //因為此處是管理去改狀態，所以如果改成normal時要幫使用者解ban
                } else if("normal".equals(save.getStatus())){
                    if(this.allRejectReportArticle(save.getArticlesId())==null){
                        return null;
                    }
                    if(dashboardUserService.unbanUser(save.getUserId())){
                        return save;
                    }
                }
            }
        }
        return null;
    }

    private List<ArticlesBean> dynamicFindArticle(DashboardArticle request){
        Specification<ArticlesBean> spec=this.findArticleSpec(request);
        return articlesRepository.findAll(spec);
    }

    /**
     * 以前端傳來的搜尋條件完成Specification(給後端的sql where)，再去資料庫找資料
     *
     * @param request--DashboardArticle前端傳完來的搜尋條件
     * @return Specification<ArticlesBean>完成後的Specification提供去資料庫找資料
     */
    private Specification<ArticlesBean> findArticleSpec(DashboardArticle request){
        Specification<ArticlesBean> spec=Specification.where(null);
        DashboardArticle articleRequest=request;

        if(articleRequest.getArticlesId()!=null){
            spec=spec.and(ArticlesSpecification.hasArticleId(articleRequest.getArticlesId()));
        }

        if(articleRequest.getArticlesId()!=null){
            spec=spec.and(ArticlesSpecification.hasArticleId(articleRequest.getArticlesId()));
        }

        if(articleRequest.getUserId()!=null){
            spec=spec.and(ArticlesSpecification.hasUserId(articleRequest.getUserId()));
        }

        if(articleRequest.getArticleGameType()!=null){
            spec=spec.and(ArticlesSpecification.hasLikeArticleGameType(articleRequest.getArticleGameType()));
        }

        if(articleRequest.getArticleHead()!=null){
            spec=spec.and(ArticlesSpecification.hasLikeArticleHead(articleRequest.getArticleHead()));
        }

        if(articleRequest.getArticleText()!=null){
            spec=spec.and(ArticlesSpecification.hasLikeArticleText(articleRequest.getArticleText()));
        }

        if(articleRequest.getMinCreatedAt()!=null){
            spec=spec.and(ArticlesSpecification.hasMinCreatedAt(articleRequest.getMinCreatedAt()));
        }

        if(articleRequest.getMaxCreatedAt()!=null){
            spec=spec.and(ArticlesSpecification.hasMaxCreatedAt(articleRequest.getMaxCreatedAt()));
        }

        if(articleRequest.getClicktimes()!=null){
            spec=spec.and(ArticlesSpecification.hasClicktimes(articleRequest.getClicktimes()));
        }


        if(articleRequest.getStatus()!=null){
            spec=spec.and(ArticlesSpecification.hasStatus(articleRequest.getStatus()));
        }

        if(articleRequest.getArticleType()!=null){
            spec=spec.and(ArticlesSpecification.hasArticleType(articleRequest.getArticleType()));
        }


        return spec;
    }

    /**
     * 將從資料庫取出的資料以前端請求的條件進行分頁及排序
     *
     * @param request--DashboardArticle       前端回傳的搜索條件
     * @param results--List<DashboardArticle> 已經先找出來的資料
     * @return
     */
    private List<DashboardArticle> articleSort(DashboardArticle request,List<DashboardArticle> results){
        List<DashboardArticle> temp=results;
        if("idacs".equals(request.getSort())){
            temp=temp.stream()
                    .sorted(Comparator.comparing(DashboardArticle::getArticlesId))
                    .toList();
        } else if("iddesc".equals(request.getSort())){
            temp=temp.stream()
                    .sorted(Comparator.comparing(DashboardArticle::getArticlesId).reversed())
                    .toList();
        } else if("userIdasc".equals(request.getSort())){
            temp=temp.stream()
                    .sorted(Comparator.comparing(DashboardArticle::getUserId)
                            .thenComparing(DashboardArticle::getCreatedAt).reversed())
                    .toList();
        } else if("userIddesc".equals(request.getSort())){
            temp=temp.stream()
                    .sorted(Comparator.comparing(DashboardArticle::getUserId).reversed()
                            .thenComparing(DashboardArticle::getCreatedAt).reversed())
                    .toList();
        } else if("createdatdesc".equals(request.getSort())){
            temp=temp.stream()
                    .sorted(Comparator.comparing(DashboardArticle::getCreatedAt).reversed()
                            .thenComparing(DashboardArticle::getUserId))
                    .toList();
        } else if("createdatasc".equals(request.getSort())){
            temp=temp.stream()
                    .sorted(Comparator.comparing(DashboardArticle::getCreatedAt)
                            .thenComparing(DashboardArticle::getUserId))
                    .toList();
        } else if("updatedatdesc".equals(request.getSort())){
            temp=temp.stream()
                    .sorted(Comparator.comparing(DashboardArticle::getUpdateAt,Comparator.nullsLast(Date::compareTo)).reversed()
                            .thenComparing(DashboardArticle::getCreatedAt,Comparator.nullsLast(Date::compareTo)).reversed())
                    .toList();
        } else if("updatedateasc".equals(request.getSort())){
            temp=temp.stream()
                    .sorted(Comparator.comparing(DashboardArticle::getUpdateAt,Comparator.nullsLast(Date::compareTo))
                            .thenComparing(DashboardArticle::getCreatedAt,Comparator.nullsLast(Date::compareTo)).reversed())
                    .toList();
        } else{
            temp=temp.stream()
                    .sorted(Comparator.comparing(DashboardArticle::getCreatedAt,Comparator.nullsLast(Date::compareTo)).reversed())
                    .sorted(Comparator.comparing(DashboardArticle::getUserId))
                    .toList();
        }
        return temp;
    }


//------------------------------------comment--------------------------------------------------

    /**
     * 找出所有留言
     *
     * @return comment--List<commentBean>
     */
    public Map<String, Object> findAllComments(DashboardComment request){
        Pageable pageable=this.commentPageable(request);
        Specification<CommentsBean> spec=this.findCommentCondition(request);
        List<CommentsBean> all=commentsRepository.findAll(spec,pageable);
        Long totalcount=commentsRepository.count(spec);
        Map<String, Object> result=new HashMap<>();
        if(all==null){
            return null;
        } else if(all.isEmpty()){
            result.put("data","nodata");
            result.put("totalcount",0);
            return result;
        }

//            Map<Integer, Integer> tempmap=countReportsNumberForComment();
//        for(CommentsBean temp : all){
//            DashboardComment bean=new DashboardComment();
//            BeanUtils.copyProperties(temp,bean);
////                bean.setReportedNumber(
////                        (tempmap.getOrDefault(bean.getCommentId(),0)));
//            allComments.add(bean);
//        }
        if(totalcount!=null){
            result.put("data",all);
            result.put("totalcount",totalcount);
            return result;
        }
        return null;
    }

    /**
     * 取得前端改動留言的留言資訊拿來更新留言的資訊
     *
     * @param request--CommentsBean前端改動的留言資訊
     * @return CommentsBean--把更新後的留言資訊
     */
    public CommentsBean modifyCommentById(CommentsBean request){
        String changeStatus=request.getStatus();
        Integer commentId=request.getCommentId();
        //確認在資料庫有此文章，且回傳的狀態文字長度不為0
        if(!commentsRepository.existsById(commentId) || changeStatus.length()==0){
            return null;
        }
        CommentsBean save=commentsRepository.findById(commentId).orElse(null);
        //確認取出的資料不為null
        if(save==null){
            return null;
        }
        //用前端更改的狀態更變資料庫本來的留言資訊
        save.setStatus(changeStatus);

        //改完資料存回去
        save=commentsRepository.save(save);
        if(save==null){
            return null;
        }
        if("banned".equals(save.getStatus())){
            if(dashboardUserService.banUserBecauseForum(save.getUserId(),"留言違規")){
                return save;
            }
        } else if("normal".equals(save.getStatus())){
            if(dashboardUserService.unbanUser(save.getUserId())){
                return save;
            }
        }
        return null;
    }
    /**
     * 取得前端改動留言的大量留言資訊拿來更新留言的資訊
     *
     * @param request--List<CommentsBean>前端改動的留言資訊
     * @return List<CommentsBean>--把更新後的留言資訊
     */
    public List<CommentsBean> modifyAllComment(List<CommentsBean> request){
        List<CommentsBean> result=new ArrayList<>();
        for(CommentsBean temprequest : request){
            CommentsBean temp=new CommentsBean();
            temp=this.modifyCommentById(temprequest);
            result.add(temp);
        }
        if(result!=null && !result.isEmpty()){
            return result;
        }
        return null;
    }

    /**
     * 輸入留言ID回傳留言內容
     *
     * @param commentId--Integer
     * @return commentText--String 文章內容
     */
    public String findCommentTextById(Integer commentId){
        CommentsBean temp=commentsRepository.findById(commentId).orElse(null);
        if(temp!=null){
            String commentText=temp.getCommentText();
            return commentText;
        }
        return null;
    }

    /**
     * 輸入留言ID回傳留言內容
     *
     * @return Map<Integer,Integer>--第一個Integer為留言ID 第二個Integer是被檢舉數
     */
    public Map<Integer, Integer> countReportsNumberForComment(){
        List<Object[]> temp=reportRepository.countReportsNumberForComment();
        if(temp!=null){
            Map<Integer, Integer> map=new HashMap<>();
            for(Object[] otemp : temp){
                map.put((Integer) otemp[0],((Long) otemp[1]).intValue());
            }
            return map;
        }
        return null;
    }

    /**
     * 用來生成給JPA轉成SQL的排序分頁參數Pageabel
     *
     * @param request--DashboardComment 裡面已包含起始頁面幾每次需要幾筆數據
     * @return Pageable--將生成的用於分頁的物件送回
     */
    private Pageable commentPageable(DashboardComment request){
        Integer start=0;
        Integer rows=10;
        if(request.getStart()!=null){
            start=request.getStart()-1;
        }
        if(request.getRows()!=null){
            rows=request.getRows();
        }
        Sort sort=Sort.by(Sort.Order.desc("createdAt"));
        if("idacs".equals(request.getSort())){
            sort=Sort.by(Sort.Order.asc("commentId"));
        } else if("iddesc".equals(request.getSort())){
            sort=Sort.by(Sort.Order.desc("commentId"));
        } else if("articlesIdasc".equals(request.getSort())){
            sort=Sort.by(Sort.Order.asc("articlesId"));
        } else if("articlesIddesc".equals(request.getSort())){
            sort=Sort.by(Sort.Order.desc("articlesId"));
        } else if("userIdasc".equals(request.getSort())){
            sort=Sort.by(Sort.Order.asc("userId"));
        } else if("userIdesc".equals(request.getSort())){
            sort=Sort.by(Sort.Order.desc("userId"));
        } else if("createdatdesc".equals(request.getSort())){
            sort=Sort.by(Sort.Order.desc("createdAt"));
        } else if("createdatasc".equals(request.getSort())){
            sort=Sort.by(Sort.Order.asc("createdAt"));
        }
        Pageable pageable=PageRequest.of(start,rows,sort);
        return pageable;
    }

    private Specification<CommentsBean> findCommentCondition(DashboardComment request){
        Specification<CommentsBean> spec=Specification.where(null);
        if(request.getCommentId()!=null){
            spec=spec.and(CommentsSpecification.hasCommentId(request.getCommentId()));
        }
        if(request.getUserId()!=null){
            spec=spec.and(CommentsSpecification.hasUserId(request.getUserId()));
        }
        if(request.getArticlesId()!=null){
            spec=spec.and(CommentsSpecification.hasArticlesId(request.getArticlesId()));
        }
        if(request.getCommentText()!=null){
            spec=spec.and(CommentsSpecification.hasLikeCommentText(request.getCommentText()));
        }
        if(request.getStatus()!=null){
            spec=spec.and(CommentsSpecification.hasStatus(request.getStatus()));
        }
        if(request.getMinCreatedAt()!=null){
            spec=spec.and(CommentsSpecification.hasMinCreatedAt(request.getMinCreatedAt()));
        }
        if(request.getMaxCreatedAt()!=null){
            spec=spec.and(CommentsSpecification.hasMaxCreatedAt(request.getMaxCreatedAt()));
        }

        return spec;
    }


//----------------------------------report--------------------------------------

    /**
     * 找出所有檢舉的資訊
     *
     * @return List<DashboardReport>
     */

    public List<DashboardReport> findAllReports(){
        List<ReportBean> allReport=reportRepository.findAll();
        List<DashboardReport> results=new ArrayList<>();
        if(allReport==null || allReport.isEmpty()){
            return null;
        }
        //取得各別文章被檢舉幾次
        Map<Integer, Integer> countArticle=countReportsNumberForArticle();
        if(countArticle==null || countArticle.isEmpty()){
            return null;
        }
        //取得各別留言被檢舉幾次
        Map<Integer, Integer> countComment=countReportsNumberForComment();
        if(countComment!=null && !countComment.isEmpty()){
            for(ReportBean bean : allReport){
                DashboardReport temp=new DashboardReport();
                BeanUtils.copyProperties(bean,temp);
                //如果是文章就給文章ID，並從countArticle以articleID取得被檢舉數
                if(temp.getReportedContentType().equals("article")){
                    temp.setReportedTextId(bean.getReportedArticlesId());
                    temp.setNumberOfReport(
                            countArticle.getOrDefault(temp.getReportedTextId(),0));
                    //如果是留言就給留言ID，並從countComment以commentID取得被檢舉數
                } else if(temp.getReportedContentType().equals("comment")){
                    temp.setReportedTextId(bean.getReportedCommentsId());
                    temp.setNumberOfReport(
                            countComment.getOrDefault(temp.getReportedTextId(),0));
                }
                results.add(temp);
            }
            return results;
        }
        return null;
    }

    /**
     * 取得某人的檢舉文章
     * <p>
     * 暫時先偷懶count不分開
     *
     * @param userId--Integer
     * @return List<DashboardReport>
     */

    public List<DashboardReport> findReportByUserId(Integer userId){
        List<ReportBean> allReport=reportRepository.findByReporterUserId(userId);
        List<DashboardReport> results=new ArrayList<>();
        if(allReport==null || allReport.isEmpty()){
            return null;
        }
        //取得各別文章被檢舉幾次
        Map<Integer, Integer> countArticle=countReportsNumberForArticle();
        if(countArticle==null || countArticle.isEmpty()){
            return null;
        }
        //取得各別留言被檢舉幾次
        Map<Integer, Integer> countComment=countReportsNumberForComment();
        if(countComment!=null && !countComment.isEmpty()){
            for(ReportBean bean : allReport){
                DashboardReport temp=new DashboardReport();
                BeanUtils.copyProperties(bean,temp);
                //如果是文章就給文章ID，並從countArticle以articleID取得被檢舉數
                if(temp.getReportedContentType().equals("article")){
                    temp.setReportedTextId(bean.getReportedArticlesId());
                    temp.setNumberOfReport(
                            countArticle.getOrDefault(temp.getReportedTextId(),0));
                    //如果是留言就給留言ID，並從countComment以commentID取得被檢舉數
                } else if(temp.getReportedContentType().equals("comment")){
                    temp.setReportedTextId(bean.getReportedCommentsId());
                    temp.setNumberOfReport(
                            countComment.getOrDefault(temp.getReportedTextId(),0));
                }
                results.add(temp);
            }
            return results;
        }
        return null;
    }

    /**
     * 取得前端改動檢舉的檢舉id及檢舉狀態拿來更新檢舉的資訊
     *
     * @param allReport--Map<Integer,String> 第一項Integer為reportId 第二項為改變後的檢舉狀態
     * @return Map<Integer,String>--把更新後的數據回傳
     */

//    public String modifyAllReport(Map<Integer, String> allReport){
//        //取出所有的KEY
//        Set<Integer> allKeys=allReport.keySet();
//        if(allKeys!=null && !allKeys.isEmpty()){
//            //將取出的keys找資料庫
//            List<ReportBean> findAllById=reportRepository.findAllById(allKeys);
//            if(findAllById!=null && !findAllById.isEmpty()){
//                //確認有取出資料，將取出的資料更新
//                //用Map的getOrDefault()方法，如果value為零把原本的值塞回去
//                for(int i=0; i<findAllById.size(); i++){
//                    findAllById.get(i).setStatus(
//                            allReport.getOrDefault(
//                                    findAllById.get(i).getReportId(),findAllById.get(i).getStatus()
//                            ));
//                    findAllById.get(i).setUpdatedAt(new Date());
//                }
//                //全部更新完成，存入資料庫
//                List<ReportBean> tempresult=reportRepository.saveAll(findAllById);
//                if(tempresult!=null && !tempresult.isEmpty()){
//                    //前端會重載資料所以直接回傳字串
//                    return "success";
//                }
//            }
//
//        }
//        return null;
//    }
    public String modifyAllReport(List<DashboardReport> allReport){
        String result=null;
        for(DashboardReport temp : allReport){
            result=this.moodifyReportById(temp.getReportId(),temp);
            if(result==null && result.length()==0){
                return null;
            }
        }
        if(result!=null && result.length()!=0){
            return result;
        }
        return null;
    }


    /**
     * 取得前端改動檢舉的檢舉id及檢舉狀態拿來更新檢舉的資訊
     *
     * @param reportId--Integer
     * @param request--Map<String,Object> 1.key為"status" 2.value為被更改的狀態
     * @return String--把更新後的狀態回傳及更新時間
     */
    public String moodifyReportById(Integer reportId,DashboardReport request){
        ReportBean result=null;
        Integer reportedTextId=request.getReportedTextId();
        if(request.getStatus().length()==0){
            return null;
        }
        if(!reportRepository.existsById(reportId)){
            return null;
        }
        //如果想更改的狀態為comfired的話
        if("confirmed".equals(request.getStatus())){
            result=this.checkForReportComfired(reportId,reportedTextId,request.getReportedContentType());
            //如果不等於null 就順便去更新文章 article也要變banned，且status等於ban
            if(result!=null && "banned".equals(result.getStatus())){
                if("article".equals(request.getReportedContentType())){
                    ArticlesBean bean=articlesRepository.findById(reportedTextId).orElse(null);
                    if(bean!=null){
                        bean.setStatus("banned");
                        bean.setUpdateAt(new Date());
                        if(articlesRepository.save(bean)!=null){
                            if(!dashboardUserService.banUserBecauseForum(request.getReporterUserId(),"檢舉通過")){
                                return null;
                            }
                        }
                    }
                } else if("comment".equals(request.getReportedContentType())){
                    CommentsBean bean=commentsRepository.findById(reportedTextId).orElse(null);
                    if(bean!=null){
                        bean.setStatus("banned");
                        if(commentsRepository.save(bean)!=null){
                            if(!dashboardUserService.banUserBecauseForum(request.getReporterUserId(),"檢舉通過")){
                                return null;
                            }
                        }
                    }
                }
            }
        } else if("banned".equals(request.getStatus())){
            result=this.allban(reportId,reportedTextId,request.getReportedContentType());
            if(result!=null){
                if("article".equals(request.getReportedContentType())){
                    if(!this.banArticle(reportedTextId)){
                        return null;
                    }
                } else if("comment".equals(request.getReportedContentType())){
                    if(!this.banComment(reportedTextId)){
                        return null;
                    }
                }
                if(!dashboardUserService.banUserBecauseForum(request.getReporterUserId(),"檢舉通過")){
                    return null;
                }
            }
        } else{
            result=reportRepository.findById(reportId).orElse(null);
            if(result!=null){
                if("banned".equals(result.getStatus())){
                    result=cancelReportBanned(reportId,reportedTextId,request.getReportedContentType());
                    if(result!=null){
                        if(!dashboardUserService.unbanUser(request.getReporterUserId())){
                            return null;
                        }
                    }
                } else{
                    System.err.println("good");
                    result.setStatus(request.getStatus());
                    result.setUpdatedAt(new Date());
                    result=reportRepository.save(result);
                    System.err.println(result);
                }
            }
        }
        if(result!=null){
            JSONObject json=new JSONObject()
                    .put("status",result.getStatus())
                    .put("updated",result.getUpdatedAt());
            return json.toString();
        }
        return null;
    }

    /**
     * 無情驗證如果被檢舉數等於2，且又送comfire的情況下直接banned
     *
     * @param reportId--Integer           檢舉編號
     * @param reportedTextId--Integer     檢舉的文章ID/留言ID
     * @param reportedContentType--String 檢舉類別文章/留言
     * @return ReportBean--回傳更新後的bean null就是掛惹
     */
    public ReportBean checkForReportComfired(Integer reportId,Integer reportedTextId,String reportedContentType){
        Integer reportNumber=null;
        ReportBean reportBean=null;
        System.err.println("incomfired");
        //如果是文章走這裡
        if("article".equals(reportedContentType)){
            System.err.println("equal");
            reportNumber=reportRepository.countByReportedArticlesId(reportedTextId);
            System.err.println("rep"+reportNumber);
            if(reportNumber==null){
                System.out.println(reportNumber+"null");
                return null;
                //如果儲存已經等於2，下一次會等於3，3就會被banned
            } else if(reportNumber==2){
                System.err.println(reportNumber+"2");
                //準備被banned，所以取出全部資料更改
                List<ReportBean> tempreport=reportRepository.findByReportedArticlesId(reportedTextId);
                if(tempreport==null || tempreport.isEmpty()){
                    return null;
                }
                for(int i=0; i<tempreport.size(); i++){
                    tempreport.get(i).setStatus("banned");
                    tempreport.get(i).setUpdatedAt(new Date());
                }
                tempreport=reportRepository.saveAll(tempreport);
                if(tempreport!=null && !tempreport.isEmpty()){
                    reportBean=reportRepository.findById(reportId).orElse(null);
                }
                //如果大於三代表已經被Ban了
            } else if(reportNumber>2){
                System.out.println(reportNumber+"dsa");
                reportBean=reportRepository.findById(reportId).orElse(null);
                if(reportBean==null){
                    return null;
                }
                reportBean.setStatus("banned");
                reportBean.setUpdatedAt(new Date());
                reportBean=reportRepository.save(reportBean);
            } else if(reportNumber>=0){
                System.out.println("befor");
                reportBean=reportRepository.findById(reportId).orElse(null);
                if(reportBean==null){
                    return null;
                }
                reportBean.setStatus("confirmed");
                reportBean.setUpdatedAt(new Date());
                System.err.println(reportBean);
                reportBean=reportRepository.save(reportBean);
                System.out.println("0"+reportBean);
            }
        } else if("comment".equals(reportedContentType)){
            reportNumber=reportRepository.countByReportedCommentsId(reportedTextId);
            if(reportNumber==null){
                return null;
                //如果儲存已經等於2，下一次會等於3，3就會被banned
            } else if(reportNumber==2){
                //準備被banned，所以取出全部資料更改
                System.out.println("ccc222");
                List<ReportBean> tempreport=reportRepository.findByReportedCommentsId(reportedTextId);
                if(tempreport==null || tempreport.isEmpty()){
                    return null;
                }
                for(int i=0; i<tempreport.size(); i++){
                    tempreport.get(i).setStatus("banned");
                    tempreport.get(i).setUpdatedAt(new Date());
                }
                tempreport=reportRepository.saveAll(tempreport);
                if(tempreport!=null && !tempreport.isEmpty()){
                    reportBean=reportRepository.findById(reportId).orElse(null);
                }
                //如果大於三代表已經被Ban了
            } else if(reportNumber>2){
                reportBean=reportRepository.findById(reportId).orElse(null);
                if(reportBean==null){
                    return null;
                }
                reportBean.setStatus("banned");
                reportBean.setUpdatedAt(new Date());
                reportBean=reportRepository.save(reportBean);
            } else if(reportNumber>=0){
                reportBean=reportRepository.findById(reportId).orElse(null);
                if(reportBean==null){
                    return null;
                }
                reportBean.setStatus("confirmed");
                reportBean.setUpdatedAt(new Date());
                reportBean=reportRepository.save(reportBean);
                System.out.println("0"+reportBean);
            }
        } else{
            return null;
        }
        if(reportBean!=null){
            return reportBean;
        }
        return null;
    }

    /**
     * 如果管理者要強制改從banned改reject會全改
     *
     * @param reportedId--Integer         檢舉編號
     * @param reportedTextId--Integer     檢舉的文章ID/留言ID
     * @param reportedContentType--String 檢舉類別文章/留言
     * @return Boolean--true是更新成功 false更新失敗
     */
    public ReportBean cancelReportBanned(Integer reportedId,Integer reportedTextId,String reportedContentType){
        List<ReportBean> temp=new ArrayList<>();
        ReportBean result=null;
        //取出文章/留言的檢舉
        if("article".equals(reportedContentType)){
            temp=reportRepository.findByReportedArticlesId(reportedTextId);
            result=this.changeBanToReject(temp,reportedId);
            if(result!=null){
                ArticlesBean bean=articlesRepository.findById(reportedTextId).orElse(null);
                if(bean!=null){
                    bean.setStatus("normal");
                    bean.setUpdateAt(new Date());
                    bean=articlesRepository.save(bean);
                    if(bean==null){
                        return null;
                    }
                }
            }
        } else if("comment".equals(reportedContentType)){
            temp=reportRepository.findByReportedCommentsId(reportedTextId);
            result=this.changeBanToReject(temp,reportedId);
            if(result!=null){
                CommentsBean bean=commentsRepository.findById(reportedTextId).orElse(null);
                if(bean!=null){
                    bean.setStatus("normal");
                    bean=commentsRepository.save(bean);
                    if(bean==null){
                        return null;
                    }
                }
            }
        } else{
            return null;
        }
        if(result!=null){
            return result;
        }


        return null;
    }

    /**
     * 提供前端把banned改成reject
     * 如果順利取出把全部的狀態改為reject
     *
     * @param temp--List<ReportBean> 編輯過的ReportBean
     * @param reportedId--Integer    檢舉ID
     * @return ReportBean--更改的狀態
     */
    public ReportBean changeBanToReject(List<ReportBean> temp,Integer reportedId){
        if(!temp.isEmpty() && temp!=null){
            for(int i=0; i<temp.size(); i++){
                temp.get(i).setStatus("reject");
                temp.get(i).setUpdatedAt(new Date());
            }
            List<ReportBean> result=reportRepository.saveAll(temp);
            if(result!=null && !result.isEmpty()){
                ReportBean bean=reportRepository.findById(reportedId).orElse(null);
                if(bean!=null)
                    return bean;
            }
        }
        return null;
    }

    /**
     * 如果管理者直接選banned會全改ban
     *
     * @param reportedId--Integer         檢舉編號
     * @param reportedTextId--Integer     檢舉的文章ID/留言ID
     * @param reportedContentType--String 檢舉類別文章/留言
     * @return Boolean--true是更新成功 false更新失敗
     */
    public ReportBean allban(Integer reportedId,Integer reportedTextId,String reportedContentType){
        List<ReportBean> tempresultlist=null;
        ReportBean result=null;

        if("article".equals(reportedContentType)){
            tempresultlist=this.allBanReportArticle(reportedTextId);
        } else if("comment".equals(reportedContentType)){
            List<ReportBean> reporttemp=reportRepository.findByReportedCommentsId(reportedTextId);
            if(reporttemp!=null&!reporttemp.isEmpty()){
                for(int i=0; i<reporttemp.size(); i++){
                    reporttemp.get(i).setStatus("banned");
                    reporttemp.get(i).setUpdatedAt(new Date());
                }
                tempresultlist=reportRepository.saveAll(reporttemp);
            }
        }
        if(tempresultlist!=null && !tempresultlist.isEmpty()){
            result=reportRepository.findById(reportedId).orElse(null);
            if(result!=null){
                return result;
            }
        }
        return null;
    }

    /**
     * "解掉被ban"掉所有ReportArticle
     *
     * @param reportedTextId--Integer
     * @return List<ReportBean>--ReportBean
     */
    public List<ReportBean> allRejectReportArticle(Integer reportedTextId){
        List<ReportBean> reporttemp=reportRepository.findByReportedArticlesId(reportedTextId);
        if(reporttemp!=null&!reporttemp.isEmpty()){
            for(int i=0; i<reporttemp.size(); i++){
                reporttemp.get(i).setStatus("reject");
                reporttemp.get(i).setUpdatedAt(new Date());
            }
            reporttemp=reportRepository.saveAll(reporttemp);
        }
        return reporttemp;
    }


    /**
     * ban掉所有ReportArticle
     *
     * @param reportedTextId--Integer
     * @return List<ReportBean>--ReportBean
     */
    public List<ReportBean> allBanReportArticle(Integer reportedTextId){
        List<ReportBean> reporttemp=reportRepository.findByReportedArticlesId(reportedTextId);
        if(reporttemp!=null&!reporttemp.isEmpty()){
            for(int i=0; i<reporttemp.size(); i++){
                reporttemp.get(i).setStatus("banned");
                reporttemp.get(i).setUpdatedAt(new Date());
            }
            reporttemp=reportRepository.saveAll(reporttemp);
        }
        System.out.println("rrr"+reporttemp);
        return reporttemp;
    }


    /**
     * 用來更新article變ban
     *
     * @param reportedTextId--Integer
     * @return boolean--true 更新成功
     */
    public boolean banArticle(Integer reportedTextId){
        ArticlesBean bean=articlesRepository.findById(reportedTextId).orElse(null);
        if(bean!=null){
            bean.setStatus("banned");
            bean.setUpdateAt(new Date());
            if(articlesRepository.save(bean)!=null){
                return true;
            }
        }
        return false;
    }

    /**
     * 用來更新Comment變ban
     *
     * @param reportedTextId--Integer
     * @return boolean--true 更新成功
     */
    public boolean banComment(Integer reportedTextId){
        CommentsBean bean=commentsRepository.findById(reportedTextId).orElse(null);
        if(bean!=null){
            bean.setStatus("banned");
            if(commentsRepository.save(bean)!=null){
                return true;
            }
        }
        return false;
    }

}
