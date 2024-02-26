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
@Table(name = "articles")
public class ArticlesBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
