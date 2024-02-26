package tw.eeit175groupone.finalproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "images")
public class ImagesBean {

    @Id
    @Column(name = "image_id")
    private Integer imagesId;
    @Column(name = "image")
    private String image;
    @Column(name = "type")
    private String type;
    @Column(name = "articles_id")
    private Integer articlesId;
    @Column(name = "comments_id")
    private Integer commentsId;
}
