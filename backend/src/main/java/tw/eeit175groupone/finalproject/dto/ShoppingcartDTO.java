package tw.eeit175groupone.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingcartDTO {
    private String productName;
    private Integer quantity;
    private Integer totalprice;
    private String coverImage;
    private Integer alltotalprice;
    private Integer cartmiddleId;
    private Integer productId;
    private Integer price;
    private Double discountPrice;
    private String productStatus;
    private Integer inventoryQuantity;
}
