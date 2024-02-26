package tw.eeit175groupone.finalproject.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "product")
public class ProductBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Integer productId;
	@Column(name = "product_name")
	private String productName;
	@Column(name = "price")
	private Integer price;
	@Column(name = "outlined")
	private String outlined;
	@Column(name = "description")
	private String description;
	@Column(name = "spec")
	private String spec;
	@Column(name = "discount")
	private Double discount;
	@Column(name = "discount_factor")
	private Integer discountFactor;
	@Column(name = "discount_starttime")
	private java.util.Date discountStarttime;
	@Column(name = "discount_endtime")
	private java.util.Date discountEndtime;
	@Column(name = "supplier")
	private String supplier;
	@Column(name = "release_date")
	private java.util.Date releaseDate;
	@Column(name = "product_status")
	private String productStatus;
	@Column(name = "product_type")
	private String productType;
	@Column(name = "product_subtype")
	private String productSubtype;
	@Column(name = "cover_image")
	@Transient
	private String coverImage;

	

	//重寫toString避免在呼叫時與Set<MerchandiseBean> merchandiseBean中重複呼叫形成無窮迴圈
	@Override
	public String toString(){
		return "ProductBean{"+
				"productId="+productId+
				", productName='"+productName+'\''+
				", price="+price+
				", outlined='"+outlined+'\''+
				", description='"+description+'\''+
				", spec='"+spec+'\''+
				", discount="+discount+
				", discountFactor="+discountFactor+
				", discountStarttime="+discountStarttime+
				", discountEndtime="+discountEndtime+
				", supplier='"+supplier+'\''+
				", releaseDate="+releaseDate+
				", productStatus='"+productStatus+'\''+
				", productType='"+productType+'\''+
				", productSubtype='"+productSubtype+'\''+
				", coverImage='"+coverImage+'\''+
				'}';
	}

}
