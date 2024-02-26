package tw.eeit175groupone.finalproject.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteProductInfo{
    private Integer productId;
    private String productName;
    private Integer price;
    private Double discount;
    private Integer discountFactor;
    private java.util.Date discountStarttime;
    private java.util.Date discountEndtime;
    private String supplier;
    private String productStatus;
    private String productType;
    private String productSubtype;
    private String coverImage;
    private Double rating;
    private Integer inventoryQuantity;
    private String gameName;
    private String color;
    private String size;
    private String spec;
    private Integer merchandiseId;
    
}
