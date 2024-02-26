package tw.eeit175groupone.finalproject.dto;

import java.util.Date;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tw.eeit175groupone.finalproject.domain.ProductBean;
import tw.eeit175groupone.finalproject.domain.WishlistBean;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistDTO {
    private Integer wishlistId;
	private Integer userId;
	private Date createAt;
	private String productName;
	private Integer price;
	private Integer productId;
    // 省略构造函数和Getter/Setter方法


}
