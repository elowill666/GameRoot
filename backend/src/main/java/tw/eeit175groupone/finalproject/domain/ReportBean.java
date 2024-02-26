package tw.eeit175groupone.finalproject.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="report")
public class ReportBean{
    @Id
    @Column(name="report_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer reportId;
    @Column(name="reason",columnDefinition="nvarchar")
    private String reason;
    @Column(name="status",columnDefinition="nvarchar")
    private String status;
    @Column(name="timestamp")
    private java.util.Date timestamp;
    @Column(name="reported_content_type",columnDefinition="nvarchar")
    private String reportedContentType;
    @Column(name="reporter_user_id")
    private Integer reporterUserId;
    @Column(name="reported_articles_id")
    private Integer reportedArticlesId;
    @Column(name="reported_comments_id")
    private Integer reportedCommentsId;
    @Column(name="created_at")
    private java.util.Date createdAt;
    @Column(name="updated_at")
    private java.util.Date updatedAt;

}
