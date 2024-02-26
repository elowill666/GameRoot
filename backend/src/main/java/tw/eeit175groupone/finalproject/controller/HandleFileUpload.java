package tw.eeit175groupone.finalproject.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
public class HandleFileUpload {


    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        
        if (file.isEmpty()) {
            System.err.println("ssssssssssssssssss");
            return "Please select a file to upload.";
        }

        try {
            //獲取根目錄
            String projRoot = System.getProperty("user.dir");
            String targetFilePath = projRoot + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "images" + File.separator +StringUtils.cleanPath(file.getOriginalFilename());


            

            //下面這個會導到target底下的static
            // Resource resource = new ClassPathResource("/static/images/");
            // String filePath = resource.getFile().getAbsolutePath() + File.separator + StringUtils.cleanPath(file.getOriginalFilename());
        
            // 構建上傳文件的路徑
            // System.err.println("filePath = "+filePath);
            File fileInput = new File(targetFilePath);

            // if(!fileInput.getParentFile().exists()){
            //     fileInput.getParentFile().mkdirs();

          
                // 將文件寫入目標路徑
                file.transferTo(fileInput);
                return "http://192.168.24.151:8080/images/"+file.getOriginalFilename();
            // }
        } catch (IOException e) {
            return "File upload failed: " + e.getMessage();
        }
    }
}
