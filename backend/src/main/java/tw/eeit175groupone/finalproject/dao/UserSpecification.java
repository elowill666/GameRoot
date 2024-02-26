package tw.eeit175groupone.finalproject.dao;

import org.springframework.data.jpa.domain.Specification;
import tw.eeit175groupone.finalproject.domain.UserBean;

public class UserSpecification{
    
    
    public static Specification<UserBean> hasId(Integer id){
        return (root,query,criteriaBuilder) -> {
            if(id!=null){
                return criteriaBuilder.equal(root.get("id"),id);
            }
            return null;
        };
    }
    public static Specification<UserBean> hasUsername(String username){
        return (root,query,criteriaBuilder) -> {
            if(username!=null&&username.length()!=0){
                return criteriaBuilder.like(root.get("username"),"%"+username+"%");
            }
            return null;
        };
    }
    
    public static Specification<UserBean> hasPermissions(Integer permissions){
        return (root,query,criteriaBuilder) -> {
            if(permissions!=null){
                return criteriaBuilder.equal(root.get("permissions"),permissions);
            }
            return null;
        };
    }
    public static Specification<UserBean> hasGreaterCash(Integer cash){
        return (root,query,criteriaBuilder) -> {
            if(cash!=null){
                return criteriaBuilder.greaterThanOrEqualTo(root.get("cash"),cash);
            }
            return null;
        };
    }
    public static Specification<UserBean> hasGreaterBantimecount(Integer bantimecount){
        return (root,query,criteriaBuilder) -> {
            if(bantimecount!=null){
                return criteriaBuilder.greaterThanOrEqualTo(root.get("bantimecount"),bantimecount);
            }
            return null;
        };
    }
}
