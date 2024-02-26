package tw.eeit175groupone.finalproject.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardArticle{
    
    private Integer articlesId;
    private Integer userId;
    private String articleGameType;
    private String articleHead;
    private String articleText;
    private Date updateAt;
    private Date createdAt;
    private Integer clicktimes;
    private String status;
    private String articleType;
    private Integer commentsNumber;
    private Integer reportsNumber;
    private Date minCreatedAt;
    private Date maxCreatedAt;
    private Integer start;
    private Integer rows;
    private String sort;
    
    
}
