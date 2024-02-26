package tw.eeit175groupone.finalproject.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.transaction.Transactional;
import tw.eeit175groupone.finalproject.dao.ArticlesRepository;
import tw.eeit175groupone.finalproject.dao.CollectionsRepository;
import tw.eeit175groupone.finalproject.dao.CommentsRepository;
import tw.eeit175groupone.finalproject.dao.LikesRepository;
import tw.eeit175groupone.finalproject.domain.CollectionsBean;
import tw.eeit175groupone.finalproject.domain.WishlistBean;
import tw.eeit175groupone.finalproject.dto.CollectionsLikesCommentsDTO;

@Service
@Transactional
public class CollectionsService {

    @Autowired
    private CollectionsRepository collectionsRepository;
    @Autowired
    private ArticlesRepository articlesRepository;
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private CommentsRepository commentsRepository;
    private CollectionsLikesCommentsDTO next;

    // 收藏文章清單++
    public boolean insertCollection(String body) {
        JSONObject obj = new JSONObject(body);
        Integer articleId = obj.isNull("articleId") ? null : obj.getInt("articleId");
        Integer userId = obj.isNull("userId") ? null : obj.getInt("userId");
        // System.err.println("hooooooooo");

        if (articleId != null && userId != null) {
            List<CollectionsBean> collections = collectionsRepository.confirmByProductIdInUserCollections(articleId,
                    userId);
            if (collections.isEmpty() && collections.size() == 0) {
                CollectionsBean insert = new CollectionsBean();
                insert.setArticleId(articleId);
                insert.setUserId(userId);
                collectionsRepository.saveAndFlush(insert);
                return true;
            }
        }
        return false;

    }

    // 收藏文章清單--
    public boolean deleteCollection(Integer userId, Integer articleId) {
        // System.err.println("hooooooooo");

        if (articleId != null && userId != null) {

            collectionsRepository.deleteByProductIdInUserCollections(articleId, userId);
            // System.out.println("666666666666666666666");
            return true;
        }
        return false;
    }

    /**
     * 確認登入者有沒有加入過收藏文章
     * 
     * @param userId
     * @param productId
     * @return 是否
     */
    public boolean confirmByProductIdInUserCollection(String body) {
        JSONObject obj = new JSONObject(body);
        Integer articleId = obj.isNull("articleId") ? null : obj.getInt("articleId");
        Integer userId = obj.isNull("userId") ? null : obj.getInt("userId");
        if (articleId != null && userId != null) {
            List<CollectionsBean> beans = collectionsRepository.confirmByProductIdInUserCollections(articleId, userId);
            // System.err.println("111111111111111111"+beans);
            if (beans.size() != 0 || !beans.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public Long countByUserId(Integer userId) {
        return collectionsRepository.countByUserId(userId);
    }

    /**
     * 顯示使用者收藏清單用
     */
    public List<CollectionsLikesCommentsDTO> findArticles(Integer userId, Map<String, String> request) {
        System.out.println("0000000000000000000000000000000000000000000");
        System.out.println("userid = " + userId + ",request = " + request);
        if (userId != null) {

            Pageable pageable = this.collectionsPageable(request);

            List<CollectionsLikesCommentsDTO> productByUserIdInCollections = collectionsRepository
                    .findProductByUserIdInCollections(userId, pageable);
            
            List<Integer> articlesIds = new ArrayList<>();
            List<Object[]> articlesLike = likesRepository.countLikesNumberEachArticle();
            List<Object[]> articlesComment = commentsRepository.countCommentsNumberEachArticle();

            for (CollectionsLikesCommentsDTO temp : productByUserIdInCollections) {
                for (Object[] temp2 : articlesLike) {
                    Integer artiId = (Integer) temp2[0];
                    if (artiId.equals(temp.getArticlesId())) {
                        Long countArti = (Long) temp2[1];
                        temp.setLikes(countArti);
                    }
                }
                for (Object[] temp3 : articlesComment){
                    Integer artiId = (Integer)temp3[0];
                    if(artiId.equals(temp.getArticlesId())){
                        Long countArti = (Long) temp3[1];
                        temp.setComments(countArti);
                    }
                }
            }
            // Iterator<CollectionsLikesCommentsDTO> iterator = productByUserIdInCollections.iterator();
            // while (iterator.hasNext()) {
            // CollectionsLikesCommentsDTO next = iterator.next();
            // articlesIds.add(next.getArticlesId());
            // }

            // likesRepository.countCommentLikesNumberByArticlesId(userId);

            if (!productByUserIdInCollections.isEmpty() || productByUserIdInCollections.size() != 0) {
                return productByUserIdInCollections;
            }
        }
        return null;
    }

    /**
     * 提供資料起始、資料數、及排序方式
     *
     * @param request--Map<String, String>應有三個key 1.start起始數字、2.rows需要資料數和3.排序方式
     */
    public Pageable collectionsPageable(Map<String, String> request) {
        if (!request.containsKey("start") || !request.containsKey("rows")) {
            return null;
        }
        try {
            // 去出Map的資料
            Integer start = Integer.parseInt(request.getOrDefault("start", null));
            Integer rows = Integer.parseInt(request.getOrDefault("rows", null));
            String sort = request.getOrDefault("sort", null);
            if (sort == null && sort.length() == 0) {
                return null;
            }
            // 取的排序方式
            Sort collectionsSort = this.collectionsSort(sort);
            if (collectionsSort == null) {
                return null;
            }
            // 回傳找資料需要的Pageable
            start = (start - 1);

            return PageRequest.of(start, rows, collectionsSort);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Sort collectionsSort(String sort) {
        if ("createdASC".equals(sort)) {
            return Sort.by(Sort.Order.asc("a.createdAt"));
        } else if ("createdDESC".equals(sort)) {
            return Sort.by(Sort.Order.desc("a.createdAt"));
        } else if ("commentsASC".equals(sort)) {
            return Sort.by(Sort.Order.asc("comments"));
        } else if ("commentsDESC".equals(sort)) {
            return Sort.by(Sort.Order.desc("comments"));
        } else if ("likesASC".equals(sort)) {
            return Sort.by(Sort.Order.asc("likes"));
        } else if ("likesDESC".equals(sort)) {
            return Sort.by(Sort.Order.desc("likes"));
        }
        return null;
    }
}
