package tw.eeit175groupone.finalproject.dao;

import org.springframework.data.jpa.domain.Specification;
import tw.eeit175groupone.finalproject.domain.ArticlesBean;

import java.util.Date;


public class ArticlesSpecification{

    public static Specification<ArticlesBean> hasArticleId(Integer articleId){
        return (root,query,criteriaBuilder) -> {
            if(articleId!=null){
                return criteriaBuilder.equal(root.get("articlesId"),articleId);
            }
            return null;
        };
    }

    public static Specification<ArticlesBean> hasUserId(Integer userId){
        return (root,query,criteriaBuilder) -> {
            if(userId!=null){
                return criteriaBuilder.equal(root.get("userId"),userId);
            }
            return null;
        };
    }

    public static Specification<ArticlesBean> hasLikeArticleGameType(String articleGameType){
        return (root,query,criteriaBuilder) -> {
            if(articleGameType!=null && articleGameType.length()!=0){
                return criteriaBuilder.like(root.get("articleGameType"),"%"+articleGameType+"%");
            }
            return null;
        };
    }

    public static Specification<ArticlesBean> hasLikeArticleHead(String articleHead){
        return (root,query,criteriaBuilder) -> {
            if(articleHead!=null && articleHead.length()!=0){
                return criteriaBuilder.like(root.get("articleHead"),"%"+articleHead+"%");
            }
            return null;
        };
    }

    public static Specification<ArticlesBean> hasLikeArticleText(String articleText){
        return (root,query,criteriaBuilder) -> {
            if(articleText!=null && articleText.length()!=0){
                return criteriaBuilder.like(root.get("articleText"),"%"+articleText+"%");
            }
            return null;
        };
    }

    public static Specification<ArticlesBean> hasMinCreatedAt(Date minCreatedAt){
        return (root,query,criteriaBuilder) -> {
            if(minCreatedAt!=null){
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"),minCreatedAt);
            }
            return null;
        };
    }

    public static Specification<ArticlesBean> hasMaxCreatedAt(Date maxCreatedAt){
        return (root,query,criteriaBuilder) -> {
            if(maxCreatedAt!=null){
                return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"),maxCreatedAt);
            }
            return null;
        };
    }

    public static Specification<ArticlesBean> hasClicktimes(Integer clicktimes){
        return (root,query,criteriaBuilder) -> {
            if(clicktimes!=null){
                return criteriaBuilder.greaterThanOrEqualTo(root.get("clicktimes"),clicktimes);
            }
            return null;
        };
    }

    public static Specification<ArticlesBean> hasStatus(String status){
        return (root,query,criteriaBuilder) -> {
            if(status!=null && status.length()!=0){
                return criteriaBuilder.equal(root.get("status"),status);
            }
            return null;
        };
    }

    public static Specification<ArticlesBean> hasArticleType(String articleType){
        return (root,query,criteriaBuilder) -> {
            if(articleType!=null && articleType.length()!=0){
                return criteriaBuilder.equal(root.get("articleType"),articleType);
            }
            return null;
        };
    }
    
}
