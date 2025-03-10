package tw.eeit175groupone.finalproject.service;

import java.util.Date;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.eeit175groupone.finalproject.dao.CommentsRepository;
import tw.eeit175groupone.finalproject.domain.CommentsBean;

@Service
@Transactional
public class CommentsService {
    @Autowired
    private CommentsRepository commentsRepository;

    /**
     * 新增留言
     * 
     * @param body
     * @return
     */
    public boolean addComment(String body) {

        JSONObject obj = new JSONObject(body);
        String commentText = obj.isNull("content") ? null : obj.getString("content");
        Integer articlesId = obj.isNull("articlesId") ? null : obj.getInt("articlesId");
        Integer userId = obj.isNull("userId") ? null : obj.getInt("userId");
        String status = obj.isNull("status") ? "normal" : obj.getString("status");

        Date createdAt = new Date();
        CommentsBean commentbean = new CommentsBean();
        commentbean.setCommentText(commentText);
        commentbean.setCreatedAt(createdAt);
        commentbean.setArticlesId(articlesId);
        commentbean.setUserId(userId);
        commentbean.setStatus(status);
        commentsRepository.save(commentbean);

        return true;
    }

    /**
     * 隱藏留言(將留言status改為'banned',不會顯示出來)
     * 
     * @param commentId
     * @return
     */
    public boolean hideComment(Integer commentId) {
        Optional<CommentsBean> comment = commentsRepository.findById(commentId);
        if (comment.isPresent()) {
            commentsRepository.hideComment(commentId);
            return true;
        } else {
            return false;
        }
    }

}
