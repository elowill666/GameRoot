package tw.eeit175groupone.finalproject.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardComment{


    private Integer commentId;

    private String commentText;

    private Date createdAt;

    private Integer articlesId;

    private Integer userId;

    private String status;

    private Integer reportedNumber;
    private Date minCreatedAt;
    private Date maxCreatedAt;
    private Integer start;
    private Integer rows;
    private String sort;
}
