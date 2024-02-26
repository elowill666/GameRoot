package tw.eeit175groupone.finalproject.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;

import tw.eeit175groupone.finalproject.dao.FriendRepository;
import tw.eeit175groupone.finalproject.dao.UserDashboardRepository;
import tw.eeit175groupone.finalproject.domain.UserBean;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private DashboardUserService dashboardUserService;

    @Autowired
    UserDashboardRepository userDashboardRepository;

    @Autowired
    FriendRepository friendRepository;

    @Test
    public void loginTest() {

        UserBean login = userService.login("Tom", "a123");
        System.err.println(login);
    }

    @Test
    public void decodedEmailTest() {
        String encodedEmail = "victor880824tor%40gmail.com";

        String decodedEmail;
        try {
            decodedEmail = URLDecoder.decode(encodedEmail, "UTF-8");

            System.err.println(decodedEmail);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    // @Test
    // public void countByOrderStatusAndUserIdTest() {
    // Integer count = userDashboardService.countByOrderStatusAndUserId("confirmed",
    // 1);
    // System.err.println("user完成的訂單有" + count + "筆");
    // }

    // @Test
    // public void getUserOrderCountsAndTotalAmount() {
    // Long count = userDashboardRepository.getUserOrderCounts(1);
    // System.err.println(count);
    // }

    @Test
    public void getUserOrderCountsandTotalAmountsTest() {
        List<Object[]> count = userDashboardRepository.getUserOrderCountsandTotalAmounts(1);
        for (Object[] objects : count) {
            System.err.println((Integer) objects[0]);
            System.err.println((Integer) objects[1]);
        }
    }

    @Test
    public void getfriendIdandNameAndPhotoTest() {
        List<Object[]> count = friendRepository.getfriendIdandNameAndPhoto(2);
        for (Object[] objects : count) {
            System.err.println((Integer) objects[0]);
            System.err.println((String) objects[1]);
            // System.err.println((String) objects[2]);
        }
    }

    @Test
    public void bcryptPasswordTest() {
        String pwd = "test12345";
        String pw_hash = BCrypt.hashpw(pwd, BCrypt.gensalt());
        System.err.println(pw_hash);
        // $2a$10$GPTchupdKBFKNdWgJTVCRuNiQLBaj6M4T1ldGcrxpbGcmwwi6BhFe

        // boolean checkpw = BCrypt.checkpw(pwd.getBytes(), pw_hash);
        // System.out.println(checkpw);
    }
}