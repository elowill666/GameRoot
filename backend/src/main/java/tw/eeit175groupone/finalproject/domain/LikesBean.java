package tw.eeit175groupone.finalproject.domain;

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
@Table(name = "likes")
public class LikesBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Integer likeId;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "type")
    private String type;
    @Column(name = "articles_id")
    private Integer articlesId;
    @Column(name = "comments_id")
    private Integer commentsId;
}
