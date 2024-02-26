
package tw.eeit175groupone.finalproject.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.eeit175groupone.finalproject.service.UserMailService;

@RestController
@RequestMapping("/email")
public class UserEmailController {

    @Autowired
    private UserMailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestBody String userEmail) {
        JSONObject obj = new JSONObject(userEmail);
        String email = obj.isNull("email") ? null : obj.getString("email");
        try {
            // href=\"http://localhost:5173/user/userChangePassword\"
            // href=\"http://192.168.24.151:5173/user/userChangePassword\"
            String to = email;
            String subject = "重置密碼驗證信";
            String htmlContent = "<div style=\"background-color: #204974; padding: 10px 0; text-align: center;width: 800px;\">\n"
                    + //
                    "\n" + //
                    "        <span style=\"font-size: 20px; color: #fff; font-weight: bold;\">Game Root</span>\n"
                    + //
                    "    </div>\n" + //
                    "\n" + //
                    "    <div style=\"margin-top: 20px; margin-bottom: 15px; font-size: 18px; color: #333;\">點擊以下按鈕以重設密碼：</div>\n"
                    + //
                    "\n" + //
                    "    <a href=\"http://localhost:5173/user/userChangePassword\" style=\"text-decoration: none;\">\n"
                    + //
                    "        <button\n" + //
                    "            style=\"padding: 10px 20px; background-color: #204974; color: #fff; border: none; border-radius: 5px; font-size: 18px; cursor: pointer;\">重設密碼</button>\n"
                    + //
                    "    </a>\n" + //
                    "\n" + //
                    "    <div style=\"margin-top: 20px; margin-bottom: 15px; font-size: 15px; color: #333;\">此為系統自動發送郵件，請勿直接回覆本郵件。</div>\n"
                    + //
                    "    <div style=\"margin-top: 20px; margin-bottom: 15px; font-size: 12px; color: #333;\">Copyright Game Root © 2024. All\n"
                    + //
                    "        rights reserved.</div>";

            emailService.sendHtmlEmail(to, subject, htmlContent);
            return "郵件已成功發送";
        } catch (Exception e) {
            e.printStackTrace();
            return "發送郵件時出現錯誤";
        }
    }

    @PostMapping("/loginAuthentication")
    public String loginAuthentication(@RequestBody String userEmail) {
        JSONObject obj = new JSONObject(userEmail);
        String email = obj.isNull("email") ? null : obj.getString("email");
        String code = obj.isNull("code") ? null : obj.getString("code");
        try {

            System.out.println("userEmail = " + email);
            String to = email;
            String subject = "驗證帳號";
            String htmlContent = "   <div style=\"background-color: #204974; padding: 10px 0; text-align: center;width: 800px;\">\n"
                    + //
                    "\n" + //
                    "        <span style=\"font-size: 20px; color: #fff; font-weight: bold;\">Game Root</span>\n"
                    + //
                    "    </div>\n" + //
                    "\n" + //
                    "    <div style=\"margin-top: 20px; margin-bottom: 45px; font-size: 18px; color: #333;\">驗證碼："
                    + code + "</div>\n"
                    + //
                    "    <div style=\"margin-top: 10px; margin-bottom: 10px; font-size: 17px; color: #333;\">歡迎加入！</div>\n"
                    + //
                    "    <div style=\"margin-top: 10px; margin-bottom: 30px; font-size: 17px; color: #333;\">Game Root 團隊敬上</div>\n"
                    + //
                    "\n" + //
                    "    <div style=\"margin-top: 10px; margin-bottom: 15px; font-size: 15px; color: #333;\">此為系統自動發送郵件，請勿直接回覆本郵件。</div>\n"
                    + //
                    "    <div style=\"margin-top: 20px; margin-bottom: 15px; font-size: 12px; color: #333;\">Copyright Game Root © 2024. All\n"
                    + //
                    "        rights reserved.</div>";

            emailService.sendHtmlEmail(to, subject, htmlContent);
            return "郵件已成功發送";
        } catch (Exception e) {
            e.printStackTrace();
            return "發送郵件時出現錯誤";
        }
    }
}
