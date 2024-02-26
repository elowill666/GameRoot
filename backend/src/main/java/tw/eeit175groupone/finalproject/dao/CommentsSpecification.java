package tw.eeit175groupone.finalproject.dao;

import org.springframework.data.jpa.domain.Specification;
import tw.eeit175groupone.finalproject.domain.CommentsBean;

import java.util.Date;

public class CommentsSpecification{
    
    public static Specification<CommentsBean> hasCommentId(Integer commentId){
        return (root,query,criteriaBuilder) -> {
            if(commentId!=null){
                return criteriaBuilder.equal(root.get("commentId"),commentId);
            }
            return null;
        };
    }

    public static Specification<CommentsBean> hasUserId(Integer userId){
        return (root,query,criteriaBuilder) -> {
            if(userId!=null){
                return criteriaBuilder.equal(root.get("userId"),userId);
            }
            return null;
        };
    }

    public static Specification<CommentsBean> hasArticlesId(Integer articlesId){
        return (root,query,criteriaBuilder) -> {
            if(articlesId!=null){
                return criteriaBuilder.equal(root.get("articlesId"),articlesId);
            }
            return null;
        };
    }

    public static Specification<CommentsBean> hasLikeCommentText(String commentText){
        return (root,query,criteriaBuilder) -> {
            if(commentText!=null&&commentText.length()!=0){
                return criteriaBuilder.like(root.get("commentText"),"%"+commentText+"%");
            }
            return null;
        };
    }

    public static Specification<CommentsBean> hasStatus(String status){
        return (root,query,criteriaBuilder) -> {
            if(status!=null){
                return criteriaBuilder.equal(root.get("status"),status);
            }
            return null;
        };
    }
    public static Specification<CommentsBean> hasMinCreatedAt(Date minCreatedAt){
        return (root,query,criteriaBuilder) -> {
            if(minCreatedAt!=null){
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"),minCreatedAt);
            }
            return null;
        };
    }
    public static Specification<CommentsBean> hasMaxCreatedAt(Date maxCreatedAt){
        return (root,query,criteriaBuilder) -> {
            if(maxCreatedAt!=null){
                return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"),maxCreatedAt);
            }
            return null;
        };
    }


}
