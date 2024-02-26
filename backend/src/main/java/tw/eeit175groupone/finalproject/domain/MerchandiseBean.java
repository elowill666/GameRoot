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
@Table(name = "merchandise")
@NoArgsConstructor
@Data
public class MerchandiseBean {

	@Id
	@Column(name = "merchandise_id")
	private Integer merchandiseId;
	@Column(name = "product_id")
	private Integer productId;
	@Column(name = "inventory_quantity")
	private Integer inventoryQuantity;
	@Column(name = "gamename")
	private String gameName;
	@Column(name = "color")
	private String color;
	@Column(name = "size")
	private String size;


}
