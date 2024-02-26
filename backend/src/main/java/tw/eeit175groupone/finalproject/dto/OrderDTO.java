package tw.eeit175groupone.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//產品進入orderdetail的最後一道防線
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Integer cartmiddleId;
    private Integer productId;
    private Integer shoppingcartId;
    private Integer quantity;
    private Integer userId;
    private Integer price;
    private String productName;
    private String productType;
    private String productStatus;
    private Double discount;
}
