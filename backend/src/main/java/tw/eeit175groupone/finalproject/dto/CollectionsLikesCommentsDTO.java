package tw.eeit175groupone.finalproject.dto;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tw.eeit175groupone.finalproject.domain.ArticlesBean;

@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class CollectionsLikesCommentsDTO {


    @Column(name = "articles_id")
    private Integer articlesId;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "article_gametype")
    private String articleGameType;
    @Column(name = "article_head")
    private String articleHead;
    @Column(name = "article_text")
    private String articleText;
    @Column(name = "update_at")
    private Date updateAt;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "clicktimes")
    private Integer clicktimes;
    @Column(name = "status")
    private String status;
    @Column(name = "article_type")
    private String articleType;
    @Column(name = "likes")
    private Long likes;
    @Column(name = "comments")
    private Long comments;
    
}
