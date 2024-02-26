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
@Table(name = "orderdetails")
public class OrderdetailsBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "orderdetail_id")
	private Integer orderdetailId;
	@Column(name = "quantity")
	private Integer quantity;
	@Column(name = "gamekey")
	private String gamekey;
	@Column(name = "history_product_name")
	private String historyProductName;
	@Column(name = "history_price")
	private Integer historyPrice;
	@Column(name = "order_id")
	private Integer orderId;
	@Column(name = "product_id")
	private Integer productId;
	@Column(name = "reason")
	private String reason;
	@Column(name = "refund_status")
	private String refundStatus;
	@Column(name = "refund_date")
	private java.util.Date refundDate;
	@Column(name = "product_comment_id")
	private Integer productCommentId;
	@Column(name = "product_comment_status")
	private String productCommentStatus;
	@Column(name = "product_comment_endtime")
	private java.util.Date productCommentEndtime;
	@Column(name = "reason_type")
	private String reasonType;
	@Column(name="review_reason",columnDefinition="nvarchar")
	private String reviewReason;
}
