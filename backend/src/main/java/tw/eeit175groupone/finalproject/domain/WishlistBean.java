package tw.eeit175groupone.finalproject.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="wishlist")
@Data
public class WishlistBean {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="wishlist_id")
	private Integer wishlistId;
	@Column(name="product_id")
	private Integer productId;
	@Column(name="user_id")
	private Integer userId;
	@Column(name="created_at")
	private Date createAt;

}
