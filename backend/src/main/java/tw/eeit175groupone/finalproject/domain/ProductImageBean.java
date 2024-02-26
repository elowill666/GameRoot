package tw.eeit175groupone.finalproject.domain;

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
@Table(name="product_image")
@Data
@NoArgsConstructor
public class ProductImageBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_image_id")
    private Integer productImageId;
    @Column(name="product_image")
    private String productImage;
    @Column(name="image_type")
    private String imageType;
    @Column(name="image_getfirst")
    private Integer imageGetFirst;
    @Column(name="product_id")
    private Integer productId;
    
    
}
