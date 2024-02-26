package tw.eeit175groupone.finalproject.domain;

import org.hibernate.annotations.DialectOverride.GeneratedColumn;

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
@Table(name = "shoppingcart")
public class ShoppingcartBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	@Column(name = "shoppingcart_id")
	private Integer shoppingcartId;
	@Column(name = "user_id")
	private Integer userId;
	@Column(name = "alltotalprice")
	private Integer alltotalprice;
	@Column(name = "coupon_id")
	private Integer couponId;



}
