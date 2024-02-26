package tw.eeit175groupone.finalproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.eeit175groupone.finalproject.dto.DashboardUser;
import tw.eeit175groupone.finalproject.service.DashboardUserService;
import tw.eeit175groupone.finalproject.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard/user")
@CrossOrigin
public class DashboardUserController {

    @Autowired
    DashboardUserService dashboardUserService;

    @Autowired
    UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getDashboardUser(@PathVariable Integer userId) {
        if (userId != null) {
            DashboardUser dashboardUserById = dashboardUserService.getDashboardUserById(userId);
            if (dashboardUserById != null) {
                return ResponseEntity.ok().body(dashboardUserById);
            }
        }
        return ResponseEntity.ok().body("使用者不存在");
    }

    /**
     * @param user
     * @return
     */
    @PostMapping("/updateUserprofile")
    public ResponseEntity<?> updateUserprofile(@RequestBody DashboardUser user) {
        if (user != null) {
            Integer id = user.getId();
            String membership = user.getMembership();
            Integer permissions = user.getPermissions();
            Boolean updateDashboardProfile = dashboardUserService.updateDashboardProfile(user);
            if (updateDashboardProfile) {
                return ResponseEntity.ok().body("更新成功");
            }
        }

        return ResponseEntity.ok().body("更新失敗");
    }

    // 接收請求後丟出全部的使用者資訊給前端管理者
    @PostMapping("/find")
    public ResponseEntity<?> findAllUser(@RequestBody DashboardUser request) {
        if(request!=null){
            Map<String,Object> allUser=dashboardUserService.findAllUser(request);
            if(allUser!=null && !allUser.isEmpty()){
                return ResponseEntity.ok().body(allUser);
            }
        }
        return null;

    }

}
