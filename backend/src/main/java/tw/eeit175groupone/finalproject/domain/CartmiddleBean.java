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
@Table(name = "cartmiddle")
public class CartmiddleBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cartmiddle_id")
	private Integer cartmiddleId;
	@Column(name = "product_id")
	private Integer productId;
	@Column(name = "shoppingcart_id")
	private Integer shoppingcartId;
	@Column(name = "quantity")
	private Integer quantity;
	@Column(name = "totalprice")
	private Integer totalprice;
	@Override
	public String toString() {
		return "CartmiddleBean [cartmiddleId=" + cartmiddleId + ", productId=" + productId + ", shoppingcartId="
				+ shoppingcartId + ", quantity=" + quantity + ", totalprice=" + totalprice + "]";
	}

	

}
