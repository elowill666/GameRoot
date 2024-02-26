package tw.eeit175groupone.finalproject.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardReport{
    private Integer reportId;
    private String reason;
    private String status;
    private java.util.Date timestamp;
    private String reportedContentType;
    private Integer reporterUserId;
    private java.util.Date createdAt;
    private java.util.Date updatedAt;
    private Integer numberOfReport;
    //被檢舉文章ID或是留言ID
    private Integer reportedTextId;
}
