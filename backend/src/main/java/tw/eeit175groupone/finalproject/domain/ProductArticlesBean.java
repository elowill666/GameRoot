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
@Table(name="product_articles")
@Data
@NoArgsConstructor
public class ProductArticlesBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_articles_id")
    private Integer productArticlesId;
    @Column(name="product_id")
    private Integer productId;
    @Column(name="articles_id")
    private Integer articlesId;
    @Column(name="shortinfo")
    private String shortInfo;
    
}
