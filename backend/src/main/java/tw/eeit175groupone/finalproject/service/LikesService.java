package tw.eeit175groupone.finalproject.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.eeit175groupone.finalproject.dao.LikesRepository;
import tw.eeit175groupone.finalproject.domain.CollectionsBean;
import tw.eeit175groupone.finalproject.domain.LikesBean;

@Service
@Transactional
public class LikesService {
    @Autowired
    private LikesRepository likesRepository;

    /**
     * 按讚
     * 
     * @param body
     * @return
     */
    public boolean addLike(String body) {
        JSONObject obj = new JSONObject(body);
        Integer userId = obj.isNull("userId") ? null : obj.getInt("userId");
        Integer articlesId = obj.isNull("articlesId") ? null : obj.getInt("articlesId");
        Integer commentsId = obj.isNull("commentId") ? null : obj.getInt("commentId");

        if (userId != null) {
            List<LikesBean> likes = likesRepository.confirmLikesExist(userId, articlesId, commentsId);
            if (likes.isEmpty() && likes.size() == 0) {
                LikesBean likesBean = new LikesBean();
                likesBean.setUserId(userId);
                likesBean.setArticlesId(articlesId);
                likesBean.setCommentsId(commentsId);
                likesRepository.save(likesBean);
                return true;
            }
        }
        return false;
    }

    /**
     * 取消讚
     * 
     * @param userId
     * @param articlesId
     * @param commentsId
     * @return
     */
    public boolean deleteLike(Integer userId, Integer articlesId, Integer commentsId) {
        if (userId != null) {
            likesRepository.deleteLike(userId, articlesId, commentsId);
            return true;
        }
        return false;
    }

    /**
     * 確認登入者有沒有按過讚
     * 
     * @param body
     * @return
     */
    public boolean confirmLikesExist(String body) {
        JSONObject obj = new JSONObject(body);
        Integer userId = obj.isNull("userId") ? null : obj.getInt("userId");
        Integer articlesId = obj.isNull("articlesId") ? null : obj.getInt("articlesId");
        Integer commentsId = obj.isNull("commentId") ? null : obj.getInt("commentId");
        if (userId != null) {
            List<LikesBean> beans = likesRepository.confirmLikesExist(userId, articlesId, commentsId);
            if (beans.size() != 0 || !beans.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
}
