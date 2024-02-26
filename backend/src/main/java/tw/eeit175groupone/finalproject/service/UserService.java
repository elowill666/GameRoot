package tw.eeit175groupone.finalproject.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.eeit175groupone.finalproject.dao.FriendRepository;
import tw.eeit175groupone.finalproject.dao.UserRepository;
import tw.eeit175groupone.finalproject.domain.CouponDetail;
import tw.eeit175groupone.finalproject.domain.FriendBean;
import tw.eeit175groupone.finalproject.domain.Status;
import tw.eeit175groupone.finalproject.domain.UserBean;
import tw.eeit175groupone.finalproject.dto.Friend;
import tw.eeit175groupone.finalproject.dto.User;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository repository;

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    DashboardUserService dashboardUserService;

    public UserBean findUserById(Integer id) {
        if (id != null) {
            Optional<UserBean> bean = repository.findById(id);
            if (bean != null && bean.isPresent()) {
                return bean.get();
            }
        }
        return null;
    }

    public boolean insertUser(UserBean user) {
        UserBean uname = repository.findByUsername(user.getUsername());
        if (uname == null) {
            user.setAllowchat(0);
            user.setPermissions(0);// 0是user 1是管理員
            user.setMembership("lv1");// 初始等級
            user.setCreated_at(new Date(System.currentTimeMillis()));
            user.setCash(0);
            user.setBantimecount(0);
            if (user.getStatus() == null) {
                user.setStatus("OFF");
            }
            // 進行加密
            if (user.getPassword() != null || user.getPassword().length() != 0) {
                String pw_hash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                // 加密完的塞進Bean裡
                user.setPassword(pw_hash);
            }
            repository.save(user);

            dashboardUserService.forNewUser(user.getId());
            return true;
        }
        return false;
    }

    public UserBean emailAuthenticate(UserBean user) {
        user.setStatus("ON");
        repository.save(user);
        UserBean userById = findUserById(user.getId());
        if (userById != null) {
            return userById;
        }
        return null;
    }

    public UserBean login(String username, String password) {
        UserBean select = repository.findByUsername(username);
        if (select != null) {
            String sqlpass = select.getPassword(); // 資料庫取出
            boolean checkpw = BCrypt.checkpw(password.getBytes(), sqlpass);
            System.out.println(checkpw);
            if (checkpw) {
                return select;
            }
        }
        return null;
    }

    public UserBean findUserByName(String username) {
        UserBean user = repository.findByUsername(username);
        if (user != null) {
            return user;
        }
        return null;
    }

    public UserBean changePassword(String username, String newpassword) {
        String pw_hash = BCrypt.hashpw(newpassword, BCrypt.gensalt());
        repository.changePassword(pw_hash, username);
        UserBean user = repository.findByUsername(username);
        return user;
    }

    public boolean updateUser(User user) {
        Optional<UserBean> byId = repository.findById(user.getId());
        if (byId.isPresent()) {
            repository
                    .updateProfile(user.getId(),
                            user.getUsername(),
                            user.getGender(),
                            user.getPhone_number(),
                            user.getEmail(),
                            user.getAddress());
            return true;
        }
        return false;
    }

    public boolean updatePhoto(String encodeToString, String username) {
        Integer success = repository.updatePhoto(encodeToString, username);
        return success > 0;
    }

    // OnetoOne
    // 標記為上線

    public List<UserBean> findConnectedUsers() {

        return repository.findAllByStatus(Status.ONLINE);
    }

    public List<Friend> addFriend(Integer user1, Integer user2) {
        System.out.println(user1);
        System.out.println(user2);
        UserBean byUseId1 = repository.findByUseId(user1);
        UserBean byUseId2 = repository.findByUseId(user2);
        if (byUseId1 != null && byUseId2 != null) {
            FriendBean save = friendRepository.save(new FriendBean(user1, user2));
            if (save != null) {
                List<Object[]> allFriends = friendRepository.getfriendIdandNameAndPhoto(user1);
                ArrayList<Friend> friendList = new ArrayList<>();
                for (Object[] friend : allFriends) {
                    Friend myfriend = Friend
                            .builder()
                            .firendId((Integer) friend[0])
                            .username((String) friend[1])
                            .userphoto((String) friend[2])
                            .build();

                    friendList.add(myfriend);
                }
                return friendList;
            }
        }
        return null;
    }

    public List<Friend> getAllFriend(Integer user) {
        if (user != null) {
            List<Object[]> allFriends = friendRepository.getfriendIdandNameAndPhoto(user);
            ArrayList<Friend> friendList = new ArrayList<>();
            for (Object[] friend : allFriends) {
                Friend myfriend = Friend
                        .builder()
                        .firendId((Integer) friend[0])
                        .username((String) friend[1])
                        .userphoto((String) friend[2])
                        .build();
                friendList.add(myfriend);
            }
            return friendList;
        }
        return null;
    }

}
