package tw.eeit175groupone.finalproject.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CreateDatetime {
    //取得Date型別
    public Date creataTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 定義 DateTimeFormatter 並將 LocalDateTime 格式化為字串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        // 將格式化後的字串轉換為 Date
        Date date = parseStringToDate(formattedDateTime, "yyyy/MM/dd HH:mm:ss");
        return date;
    }
    //取得String
    public String stringTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 定義 DateTimeFormatter 並將 LocalDateTime 格式化為字串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        return currentDateTime.format(formatter);
    }

    // 取得時間
    public static Date parseStringToDate(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
