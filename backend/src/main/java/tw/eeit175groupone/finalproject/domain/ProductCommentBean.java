package tw.eeit175groupone.finalproject.domain;

import java.util.Date;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="product_comment")
@Data
@NoArgsConstructor
public class ProductCommentBean {
    
    @Id
    @Column(name="product_comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productCommentId;
    @Column(name="text")
    private String text;
    @Column(name="score")
    private Double score;
    @Column(name="product_id")
    private Integer productId;
    @Column(name="user_id")
    private Integer userId;
    @Column(name="created_at")
    private Date createAt;


    
}
