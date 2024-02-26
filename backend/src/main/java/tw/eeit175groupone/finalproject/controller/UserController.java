package tw.eeit175groupone.finalproject.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import tw.eeit175groupone.finalproject.domain.UserBean;
import tw.eeit175groupone.finalproject.dto.DashboardUser;
import tw.eeit175groupone.finalproject.dto.Friend;
import tw.eeit175groupone.finalproject.dto.User;
import tw.eeit175groupone.finalproject.service.DashboardUserService;
import tw.eeit175groupone.finalproject.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;
import tw.eeit175groupone.finalproject.util.DatetimeConverter;

@RestController
@RequestMapping("/user")
// @SessionAttributes("user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    DashboardUserService dashboardUserService;

    // 用id拿使用者資訊
    @GetMapping("/{id}")
    public ResponseEntity<UserBean> getUserById(@PathVariable Integer id) {
        UserBean userById = userService.findUserById(id);

        return ResponseEntity.ok().body(userById);
    }

    // 用name拿使用者資訊
    @GetMapping("getuser/{username}")
    public ResponseEntity<?> getUserByName(@PathVariable String username) {
        UserBean userByName = userService.findUserByName(username);
        if (userByName != null) {
            return ResponseEntity.ok().body(userByName);
        }
        return ResponseEntity.ok().body("使用者不存在");
    }

    // 新增 註冊
    @PostMapping("/insert")
    public ResponseEntity<String> addNewUser(@RequestBody UserBean bean) {
        boolean insertUser = userService.insertUser(bean);
        if (insertUser) {
            return ResponseEntity.ok().body("新增成功");
        }
        return ResponseEntity.ok().body("新增失敗");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserBean user, HttpSession session) {
        System.out.println("Login!!!");
        JSONObject rejson = new JSONObject();
        UserBean bean = userService.login(user.getUsername(), user.getPassword());
        if (bean == null) {
            rejson.put("message", "帳號或密碼錯誤");
            return ResponseEntity.ok().body(rejson.toString());
        } else {
            Integer permissions = bean.getPermissions();
            if (permissions == -1) {
                UserBean banCheck = dashboardUserService.userBanCheck(bean);
                if (banCheck == null) {
                    rejson.put("message", "因" + bean.getBanReason() + "被停權至"
                            + DatetimeConverter.convertForECPay(bean.getBantimeend()));
                    rejson.put("ban", "ban");
                    return ResponseEntity.ok().body(rejson.toString());
                } else {
                    rejson.put("message", "登入成功");
                    session.setAttribute("user", banCheck);
                }
            }
            if (permissions == 1) {
                rejson.put("message", "管理員登入");
                session.setAttribute("user", bean);
            } else {

                String status = bean.getStatus();
                System.err.println(status);
                if ("OFF".equals(status)) {
                    rejson.put("message", "進行信箱驗證");
                    session.setAttribute("loginAuthentication", bean);
                } else {
                    rejson.put("message", "登入成功");
                    session.setAttribute("user", bean);
                }
            }
            return ResponseEntity.ok().body(rejson.toString());
        }
    }

    @GetMapping("/emailAuthenticate")
    public ResponseEntity<?> emailAuthenticate(HttpSession session) {
        UserBean user = (UserBean) session.getAttribute("loginAuthentication");
        if (user != null) {
            UserBean emailAuthenticate = userService.emailAuthenticate(user);
            if (emailAuthenticate != null) {
                session.setAttribute("user", emailAuthenticate);
                return ResponseEntity.ok().body(emailAuthenticate);
            }

        }
        return ResponseEntity.ok().body("驗證失敗");
    }

    // 驗證登入狀態
    @GetMapping("/authenticate")
    public ResponseEntity<?> authenticate(HttpSession session) {
        UserBean uuu = (UserBean) session.getAttribute("googleuser");
        if (session != null) {
            UserBean user = (UserBean) session.getAttribute("user");
            if (user != null) {
                Integer permissions = user.getPermissions();
                if (permissions == 1) {
                    return ResponseEntity.ok("管理員");
                }
                return ResponseEntity.ok(true);
            } else {
                UserBean googleuser = (UserBean) session.getAttribute("googleuser");
                if (googleuser != null) {
                    return ResponseEntity.ok("googleuser");
                }
            }

        }
        return ResponseEntity.ok(false);
    }

    // 驗證登入者是否為管理員
    @GetMapping("/adminAuthenticate")
    public ResponseEntity<Boolean> adminAuthenticate(HttpSession session) {
        if (session != null) {
            UserBean user = (UserBean) session.getAttribute("user");
            if (user != null) {
                Integer permissions = user.getPermissions();
                if (permissions == 1) {
                    return ResponseEntity.ok(true);
                }
            }
        }
        return ResponseEntity.ok(false);
    }

    // 直接取得登入使用者的資料 (檢驗使用者是用一般登入還是google並傳回前端)
    @GetMapping("/profile")
    public ResponseEntity<?> getProfileBySession(HttpSession session) {
        if (session != null) {
            UserBean user = (UserBean) session.getAttribute("user");
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                UserBean googleuser = (UserBean) session.getAttribute("googleuser");
                if (googleuser != null) {
                    // 拿最新的googleuser給前端
                    UserBean googleuserById = userService.findUserById(googleuser.getId());
                    return ResponseEntity.ok(googleuserById);
                } else {
                    return ResponseEntity.ok("無此用戶");
                }
            }
        }
        return ResponseEntity.ok("無此用戶");
    }

    // 登出
    @GetMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpSession session) {
        if ((session != null && session.getAttribute("user") != null)
                || (session != null && session.getAttribute("googleuser") != null)) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    session.removeAttribute("user");
                    session.removeAttribute("googleuser");
                    session.removeAttribute("loginAuthentication");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        return ResponseEntity.ok(true);
    }

    // 改密碼
    // @PostMapping("/changepassword")
    // public ResponseEntity<?> changePassword(@RequestBody String json) {

    // JSONObject obj = new JSONObject(json);
    // String username = obj.isNull("username") ? null : obj.getString("username");
    // String oldpassword = obj.isNull("oldpassword") ? null :
    // obj.getString("oldpassword");
    // String newpassword = obj.isNull("newpassword") ? null :
    // obj.getString("newpassword");
    // System.err.println(username);
    // UserBean userByName = userService.findUserByName(username);
    // if (userByName != null) {
    // if (userByName.getPassword().equals(oldpassword)) {
    // userService.changePassword(username, newpassword);
    // } else {
    // return ResponseEntity.ok().body("密碼錯誤");
    // }
    // } else {
    // return ResponseEntity.ok().body("用戶不存在");
    // }
    // return ResponseEntity.ok().body("更新成功");
    // }
    @PostMapping("/changepassword")
    public ResponseEntity<?> changePassword(@RequestBody String json) {

        JSONObject obj = new JSONObject(json);
        String username = obj.isNull("username") ? null : obj.getString("username");
        String newpassword = obj.isNull("newpassword") ? null : obj.getString("newpassword");
        System.err.println(username);
        UserBean userByName = userService.findUserByName(username);
        if (userByName != null) {
            userService.changePassword(username, newpassword);
        } else {
            return ResponseEntity.ok().body("用戶不存在");
        }
        return ResponseEntity.ok().body("密碼更新成功");
    }

    // google
    // 拿到 session 裡面的東西，然後傳到前端去
    @GetMapping("/api/users/map")
    public ResponseEntity<?> testSessionValue(HttpSession httpSession) {

        // System.out.println("檢查登入 controller");

        UserBean user = (UserBean) httpSession.getAttribute("user");

        if (user == null) {
            System.out.println("session attribute 空的");
            return new ResponseEntity<String>("session attribute null", HttpStatus.UNAUTHORIZED); // 401
        }

        JSONObject rejson = new JSONObject();

        rejson.put("userId", user.getId());
        rejson.put("userName", user.getUsername());

        return new ResponseEntity<String>(rejson.toString(), HttpStatus.OK);
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<?> usersLogout(HttpSession httpSession) {

        System.out.println("登出 controller");

        httpSession.removeAttribute("user");

        return new ResponseEntity<String>("登出ok", HttpStatusCode.valueOf(200));
    }

    @PostMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody User user) {
        UserBean userById = userService.findUserById(user.getId());
        if (userById != null) {
            Boolean updatBoolean = userService.updateUser(user);
            return ResponseEntity.ok().body(updatBoolean);
        }
        return ResponseEntity.ok().body("使用者不存在");
    }

    @PostMapping("/uploadphoto/{username}")
    public ResponseEntity<String> uploadPhoto(@RequestPart("file") MultipartFile file, @PathVariable String username) {
        try {
            Encoder encoder = Base64.getEncoder();
            String encodeToString = encoder.encodeToString(file.getBytes());
            String phototodata = "data:image/jpeg;base64," + encodeToString;
            boolean updatePhoto = userService.updatePhoto(phototodata, username);
            if (updatePhoto) {
                return ResponseEntity.ok("Photo uploaded successfully.");
            } else {
                return ResponseEntity.ok("Failed to upload photo.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.ok("Failed to upload photo.");
        }
    }

    // onetooneChat
    @GetMapping("/users")
    public ResponseEntity<List<UserBean>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }

    // 增加私訊列表功能
    @GetMapping("/addfirend/{user2}")
    public ResponseEntity<?> addChatFriend(@PathVariable Integer user2, HttpSession session) {
        UserBean user = (UserBean) session.getAttribute("user");
        if (user == null) {
            UserBean googleuser = (UserBean) session.getAttribute("googleuser");
            List<Friend> friend = userService.addFriend(googleuser.getId(), user2);
            if (friend != null && !friend.isEmpty()) {
                return ResponseEntity.ok().body(friend);
            }
        } else {
            List<Friend> friend = userService.addFriend(user.getId(), user2);
            if (friend != null && !friend.isEmpty()) {
                return ResponseEntity.ok().body(true);
            }
        }
        return ResponseEntity.ok("訪問失敗");
    }

    @GetMapping("/getAllFriend/{userId}")
    public ResponseEntity<?> getAllFriend(@PathVariable Integer userId) {
        List<Friend> friend = userService.getAllFriend(userId);
        if (friend != null && !friend.isEmpty()) {
            return ResponseEntity.ok().body(friend);
        }
        return ResponseEntity.ok("訪問失敗");
    }

}
