package tw.eeit175groupone.finalproject.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "comments")
public class CommentsBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;
    @Column(name = "comment_text")
    private String commentText;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "articles_id")
    private Integer articlesId;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "image_id")
    private Integer imageId;
    @Column(name = "status")
    private String status;
}
