package tw.eeit175groupone.finalproject.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DashboardProduct{
    
    private Integer productId;
    private String productName;
    private Integer price;
    private Double discount;
    private Integer discountFactor;
    private java.util.Date discountStarttime;
    private java.util.Date discountEndtime;
    private Map<String, Date> disconutTime;
    private String supplier;
    private String productStatus;
    private String productType;
    private String productSubtype;
    private String coverImage;
    private Integer minPrice;
    private Integer maxPrice;
    private Integer start;
    private Integer rows;
    private String sort;
    private Integer totalItem;
    private Double minDiscount;
    private Double maxDiscount;
    private Integer minDiscountFactor;
    private Integer maxDiscountFactor;
}
