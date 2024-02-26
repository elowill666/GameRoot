package tw.eeit175groupone.finalproject.service;

import java.util.*;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import tw.eeit175groupone.finalproject.dao.*;
import tw.eeit175groupone.finalproject.domain.CouponBean;
import tw.eeit175groupone.finalproject.domain.CouponDetail;
import tw.eeit175groupone.finalproject.domain.UserBean;
import tw.eeit175groupone.finalproject.dto.DashboardUser;
import tw.eeit175groupone.finalproject.util.DatetimeConverter;

@Service
@Transactional(rollbackFor={Exception.class})
public class DashboardUserService{

    @Autowired
    UserDashboardRepository userDashboardRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ArticlesRepository articlesRepository;
    @Autowired
    private CouponRepository couponRepository;

    public DashboardUser getDashboardUserById(Integer userId){
        if(userId==null){
            return null;
        }

        Optional<UserBean> optionalUser=userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            return null;
        }

        UserBean userBean=optionalUser.get();
        if(userBean.getBantimecount()==null || userBean.getBantimecount()==0){
            userBean.setBantimecount(0);
        }
        DashboardUser dashboardUser=new DashboardUser();
        BeanUtils.copyProperties(userBean,dashboardUser);

        // 發文總數
        Integer allArticles=userDashboardRepository.countUserArticles(userId);
        dashboardUser.setCountUserArticles(allArticles);

        // 留言總數
        Integer allComments=userDashboardRepository.countUserComments(userId);
        dashboardUser.setCountUserComments(allComments);


        // 未完成的訂單
        Integer countConfirmedOrder=userDashboardRepository.countByOrderStatusAndUserId("confirmed",userId);
        dashboardUser.setUnconfirmedOrder(countConfirmedOrder);

        // 完成的訂單
        Integer countUnConfirmedOrder=userDashboardRepository.countByOrderStatusAndUserId("finish",userId);
        dashboardUser.setConfirmedOrder(countUnConfirmedOrder);


        List<Object[]> userOrderCountsandTotalAmounts=userDashboardRepository
                .getUserOrderCountsandTotalAmounts(userId);
        if(userOrderCountsandTotalAmounts!=null && !userOrderCountsandTotalAmounts.isEmpty()){
            for(Object[] objects : userOrderCountsandTotalAmounts){
                dashboardUser.setTotalOrders(objects[0]!=null ? (Integer) objects[0]: 0);
                dashboardUser.setTotalAmount(objects[1]!=null ? (Integer) objects[1]: 0);
            }
        }

        return dashboardUser;
    }

    public Boolean updateDashboardProfile(DashboardUser user){
        if(user.getPermissions()!=null && user.getId()!=null){
            UserBean byUseId=userRepository.findByUseId(user.getId());
            // 如果原本是一般權限 但這次更新是被ban 而不是解ban 被B次數就+
            if(byUseId.getPermissions()==0 || user.getPermissions()==-1){
                byUseId.setBanReason(user.getBanReason());
                byUseId.setBantimecount(byUseId.getBantimecount()+1);
                byUseId.setPermissions(user.getPermissions());
                Long startBanTime=DatetimeConverter.dateParsetoDate(new Date(System.currentTimeMillis())).getTime();
                Long EndBanTime=startBanTime+byUseId.getBantimecount()*3L*24*60*60*1000;
                byUseId.setBantimestart(new java.sql.Date(startBanTime));
                byUseId.setBantimeend(new java.sql.Date(EndBanTime));
                byUseId=userRepository.save(byUseId);
                // 更新後再取得一次
                if(byUseId!=null){
                    return true;
                }
            }
            byUseId.setBanReason(null);
            byUseId.setPermissions(user.getPermissions());
            byUseId.setBantimestart(null);
            byUseId.setBantimeend(null);
            byUseId=userRepository.save(byUseId);
            if(byUseId!=null){
                return true;
            }
        }
        return false;
    }

    /**
     * 依照前端搜尋資訊提供使用者資訊
     *
     * @param request--DashboardUser前端提供的搜尋資訊
     * @return List<DashboardUser>--回傳對應的資訊
     */
    public Map<String, Object> findAllUser(DashboardUser request){
        Specification<UserBean> spec=this.findCondition(request);
        List<UserBean> allUserTemp=userRepository.findAll(spec);
        List<Integer> allUserId=null;
        List<Object[]> allTotalAmount=null;
        List<Object[]> allPost=null;
        List<DashboardUser> tempResult=new ArrayList<>();
        Map<String, Object> result=new HashMap<>();
        if(allUserTemp==null){
            return null;
        } else if(allUserTemp.isEmpty()){
            result.put("data","nodata");
            result.put("totalcount",0);
            return result;
        }
        //所有的userId
        allUserId=allUserTemp.stream()
                .map(UserBean::getId)
                .toList();
        if(allUserId.isEmpty()){
            return null;
        }
        allTotalAmount=orderRepository.sumPersonalComsume(allUserId);
        allPost=articlesRepository.countPersonalPost(allUserId);
        //把allUserTemp、allTotalAmount和allPost整合
        tempResult=this.combineUserData(allUserTemp,allTotalAmount,allPost);
        if(request.getTotalAmount()!=null){
            tempResult=tempResult.stream()
                    .filter(find -> find.getTotalAmount()>=request.getTotalAmount())
                    .toList();
        }
        if(request.getTotalOrders()!=null){
            tempResult=tempResult.stream()
                    .filter(find -> find.getTotalOrders()>=request.getTotalOrders())
                    .toList();
        }
        if(tempResult.isEmpty()){
            return null;
        }
        result.put("totalcount",tempResult.size());

        if(tempResult!=null && !tempResult.isEmpty()){
            result.put("data",this.resultSort(tempResult,request));

            return result;
        }
        return null;
    }

    /**
     * 假如論壇的文章或留言被ban了觸發此方法無情開ban
     *
     * @param reporterUserId--Integer
     * @return boolean--true更新成功 false更新失敗
     */
    public boolean banUserBecauseForum(Integer reporterUserId,String banReason){
        if(userRepository.existsById(reporterUserId)){
            UserBean userBean=userRepository.findById(reporterUserId).orElse(null);
            if(userBean!=null){
                userBean.setPermissions(-1);
                userBean.setBantimecount(userBean.getBantimecount()+1);
                Long startBanTime=DatetimeConverter.dateParsetoDate(new Date(System.currentTimeMillis())).getTime();
                Long EndBanTime=startBanTime+userBean.getBantimecount()*3L*24*60*60*1000;
                userBean.setBantimestart(new java.sql.Date(startBanTime));
                userBean.setBantimeend(new java.sql.Date(EndBanTime));
                userBean.setBanReason(banReason);
                if(userRepository.save(userBean)!=null){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 假如論壇的文章或留言被解ban了觸發此方法有情解ban
     *
     * @param userId--Integer
     * @return boolean--true更新成功 false更新失敗
     */
    public boolean unbanUser(Integer userId){
        if(userRepository.existsById(userId)){
            UserBean userBean=userRepository.findById(userId).orElse(null);
            if(userBean!=null){
                userBean.setPermissions(0);
                if(userBean.getBantimecount()==null){
                    userBean.setBantimecount(0);
                    userBean.setBantimestart(null);
                    userBean.setBantimeend(null);
                } else{
                    userBean.setBantimecount(userBean.getBantimecount()-1);
                    userBean.setBantimestart(null);
                    userBean.setBantimeend(null);
                    userBean.setBanReason(null);
                }
                if(userRepository.save(userBean)!=null){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 帶入userId檢查是否可以解ban 如果可以回傳true
     *
     * @param user
     * @return boolean--true可以解 false被ban
     */
    public UserBean userBanCheck(UserBean user){
        if(user.getBantimeend().getTime()<=System.currentTimeMillis()){
            user.setBantimestart(null);
            user.setBantimeend(null);
            user.setBanReason(null);
            user.setPermissions(0);
            UserBean save=userRepository.save(user);
            if(save!=null){
                return save;
            }
        }
            return null;
    }


    /**
     * 將資料合併
     *
     * @param request--List<>--UserBean--已經初步塞選的使用者資料
     * @param sumComsume--List<>--Object[]--初步塞選的使用者資料再去資料庫找每個人的消費
     * @param countPost--List<>--Object[]--用初步塞選的使用者資料再去資料庫找每個人的發文數
     * @return List<>--DashboardUser--將三個三數整合後的資料
     */
    private List<DashboardUser> combineUserData(List<UserBean> request,List<Object[]> sumComsume,List<Object[]> countPost){
        List<DashboardUser> result=new ArrayList<>();
        for(UserBean temp : request){
            DashboardUser dashboardtemp=new DashboardUser();
            BeanUtils.copyProperties(temp,dashboardtemp);
            //找到對應的id給他總消費
            dashboardtemp.setTotalAmount(
                    sumComsume.stream()
                            .filter(find -> ((Integer) find[0]).equals(dashboardtemp.getId()))
                            .findFirst()
                            .map(find -> ((Long) find[1]).intValue())
                            .orElse(0));
            //找到對應的id給他總發文數
            dashboardtemp.setCountUserArticles(
                    countPost.stream()
                            .filter(find -> ((Integer) find[0]).equals(dashboardtemp.getId()))
                            .findFirst()
                            .map(find -> ((Long) find[1]).intValue())
                            .orElse(0));
            result.add(dashboardtemp);
        }
        return result;
    }


    /**
     * 生成給jpa去資料庫找資料的搜尋條件
     *
     * @param request--DashboardUser--前端傳來的搜尋條件
     * @return List<>--DashboardUser--將三個三數整合後的資料
     */
    public Specification<UserBean> findCondition(DashboardUser request){
        Specification<UserBean> spec=Specification.where(null);

        if(request.getId()!=null){
            spec=spec.and(UserSpecification.hasId(request.getId()));
        }
        if(request.getUsername()!=null){
            spec=spec.and(UserSpecification.hasUsername(request.getUsername()));
        }
        if(request.getPermissions()!=null){
            spec=spec.and(UserSpecification.hasPermissions(request.getPermissions()));
        }
        if(request.getCash()!=null){
            spec=spec.and(UserSpecification.hasGreaterCash(request.getCash()));
        }
        if(request.getBantimecount()!=null){
            spec=spec.and(UserSpecification.hasGreaterBantimecount(request.getBantimecount()));
        }

        return spec;
    }

    /**
     * 進行排序分頁
     *
     * @param request--DashboardUser--前端傳來的搜尋條件
     * @return List<>--DashboardUser--排序分頁後的資料
     */
    public List<DashboardUser> resultSort(List<DashboardUser> request,DashboardUser condition){
        String sort=condition.getSort();
        int start=1;
        int rows=10;
        if(condition.getStart()!=null){
            start=condition.getStart();
        }
        if(condition.getRows()!=null){
            rows=condition.getRows();
        }
        int startRows=(start-1)*rows;
        int totalRows=rows*start;
        if(totalRows>request.size()){
            totalRows=request.size();
        }
        if(sort==null || sort.length()==0){
            request=request.stream()
                    .sorted(Comparator.comparing(DashboardUser::getId))
                    .skip(startRows)
                    .limit(totalRows)
                    .toList();
        } else if("idasc".equals(sort)){
            request=request.stream()
                    .sorted(Comparator.comparing(DashboardUser::getId))
                    .skip(startRows)
                    .limit(totalRows)
                    .toList();
        } else if("iddesc".equals(sort)){
            request=request.stream()
                    .sorted(Comparator.comparing(DashboardUser::getId).reversed())
                    .skip(startRows)
                    .limit(totalRows)
                    .toList();
        } else if("totalamountdesc".equals(sort)){
            request=request.stream()
                    .sorted(Comparator.comparing(DashboardUser::getTotalAmount).reversed())
                    .skip(startRows)
                    .limit(totalRows)
                    .toList();
        } else if("totalamountasc".equals(sort)){
            request=request.stream()
                    .sorted(Comparator.comparing(DashboardUser::getTotalAmount))
                    .skip(startRows)
                    .limit(totalRows)
                    .toList();
        } else if("articlesamountdesc".equals(sort)){
            request=request.stream()
                    .sorted(Comparator.comparing(DashboardUser::getCountUserArticles))
                    .skip(startRows)
                    .limit(totalRows)
                    .toList();
        } else if("articlesamountasc".equals(sort)){
            request=request.stream()
                    .sorted(Comparator.comparing(DashboardUser::getCountUserArticles))
                    .skip(startRows)
                    .limit(totalRows)
                    .toList();
        }
        return request;
    }
    
    
    public Boolean forNewUser(Integer userId){
        List<CouponBean> couponList=new ArrayList<>();
        CouponBean one=new CouponBean();
        one.setUserId(userId);
        one.setInfo("新用戶95折特價卷");
        one.setDiscount(0.95f);
        one.setUsed("unused");
        one.setBeginDate(new Date(System.currentTimeMillis()));
        one.setEndDate(new Date(System.currentTimeMillis()+30L*24*60*60));
        couponRepository.save(one);
        CouponBean two=new CouponBean();
        two.setUserId(userId);
        two.setInfo("新用戶75折特價卷");
        two.setDiscount(0.75f);
        two.setUsed("unused");
        two.setBeginDate(new Date(System.currentTimeMillis()));
        two.setEndDate(new Date(System.currentTimeMillis()+30L*24*60*60));
        couponRepository.save(two);
        CouponBean three=new CouponBean();
        three.setUserId(userId);
        three.setInfo("新用戶55折特價卷");
        three.setDiscount(0.55f);
        three.setUsed("unused");
        three.setBeginDate(new Date(System.currentTimeMillis()));
        three.setEndDate(new Date(System.currentTimeMillis()+30L*24*60*60));
        couponList.add(three);
        couponRepository.save(three);
        return true;
    }
}
