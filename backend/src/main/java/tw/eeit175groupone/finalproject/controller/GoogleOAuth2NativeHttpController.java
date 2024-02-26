package tw.eeit175groupone.finalproject.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
// import tw.eeit175groupone.finalproject.GoogleOauthConfig;
import tw.eeit175groupone.finalproject.domain.UserBean;
import tw.eeit175groupone.finalproject.service.UserService;

@CrossOrigin
@Controller
public class GoogleOAuth2NativeHttpController {

        @Autowired
        UserService userService;

        private final static String clientId = "820611805488-kb82h3pqrjr5hccvjlo2esjf4jmrqb2t.apps.googleusercontent.com";

        private final static String clientSecret = "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";

        private final static String redirectUri = "http://localhost:8080/google-callback";

        @Value("${redirect.home.url}")
        private String redirectHomeUrl;

        // @Autowired
        // private GoogleOauthConfig googleOauth2Config;

        @Autowired
        private RestTemplate restTemplate;

        private final String scope = "https://www.googleapis.com/auth/userinfo.email";

        @GetMapping("/google-login")
        public String googleLogin(HttpServletResponse response) {

                // 直接 redirect 導向 Google OAuth2 API
                String authUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                                "client_id=" + clientId +
                                "&response_type=code" +
                                "&scope=openid%20email%20profile" +
                                "&redirect_uri=" + redirectUri +
                                "&state=state";
                return "redirect:" + authUrl;
        }

        @GetMapping("/google-callback")
        public String oauth2Callback(@RequestParam(required = false) String code,
                        HttpSession session)
                        throws IOException {
                UserBean googleuser;
                if (code == null) {

                        String authUri = "https://accounts.google.com/o/oauth2/v2/auth?response_type=code" +
                                        "&client_id=" + clientId +
                                        "&redirect_uri=" + redirectUri +
                                        "&scope=" + scope;
                        return "redirect:" + authUri;
                } else {
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                        MultiValueMap<String, String> map = new LinkedMultiValueMap<>(); // key
                        // 可能重複再用
                        map.add("code", code);
                        map.add("client_id", clientId);
                        map.add("client_secret", clientSecret);
                        map.add("redirect_uri", redirectUri);
                        map.add("grant_type", "authorization_code");

                        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,
                                        headers);

                        ResponseEntity<String> response = restTemplate.postForEntity(
                                        "https://oauth2.googleapis.com/token", request,
                                        String.class);
                        String credentials = response.getBody();
                        // System.out.println("credentials" + credentials);

                        JsonNode jsonNode = new ObjectMapper().readTree(credentials);
                        String accessToken = jsonNode.get("access_token").asText();
                        // String idToken = jsonNode.get("id_token").asText();

                        HttpHeaders headers2 = new HttpHeaders();
                        headers2.setBearerAuth(accessToken);

                        HttpEntity<String> entity = new HttpEntity<>(headers2);
                        ResponseEntity<String> response2 = restTemplate.exchange(
                                        "https://www.googleapis.com/oauth2/v1/userinfo?alt=json",
                                        HttpMethod.GET,
                                        entity,
                                        String.class);

                        String payloadResponse = response2.getBody();

                        JsonNode payloadJsonNode = new ObjectMapper().readTree(payloadResponse);

                        // Extract data from payloadJsonNode and process it
                        // String payloadGoogleId = payloadJsonNode.get("id").asText();
                        String payloadEmail = payloadJsonNode.get("email").asText();
                        String payloadName = payloadJsonNode.get("name").asText();
                        String payloadPicture = payloadJsonNode.get("picture").asText();

                        // 先用google名字找有沒有註冊過
                        googleuser = userService.findUserByName(payloadName);

                        UserBean user = new UserBean();
                        user.setUsername(payloadName);
                        user.setEmail(payloadEmail);
                        user.setUserphoto(payloadPicture);
                        // 如果他第一次用估狗登入 就幫他註冊
                        if (googleuser == null) {
                                user.setStatus("ON");
                                userService.insertUser(user);
                                session.setAttribute("googleuser", user);
                        } else {
                                session.setAttribute("googleuser", googleuser);
                        }

                }

                return "redirect:" + redirectHomeUrl + "/" + googleuser.getUsername();
        }

}
