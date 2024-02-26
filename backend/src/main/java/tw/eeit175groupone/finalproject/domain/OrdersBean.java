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
@Table(name = "orders")
public class OrdersBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Integer orderId;
	@Column(name = "user_id")
	private Integer userId;
	@Column(name = "total_amount")
	private Integer totalAmount;
	@Column(name = "order_status")
	private String orderStatus;
	@Column(name = "order_date")
	private java.util.Date orderDate;
	@Column(name = "payment_method")
	private String paymentMethod;
	@Column(name = "payment_status")
	private Integer paymentStatus;
	@Column(name = "payment_date")
	private java.util.Date paymentDate;
	@Column(name = "shipment_date")
	private java.util.Date shipmentDate;
	@Column(name = "update_date")
	private java.util.Date updateDate;
	@Column(name = "billing_username")
	private String billingUsername;
	@Column(name = "billing_city")
	private String billingCity;
	@Column(name = "billing_area")
	private String billingArea;
	@Column(name = "billing_address")
	private String billingAddress;
	@Column(name = "consignee_username")
	private String consigneeUsername;
	@Column(name = "consignee_phonenumber")
	private Integer consigneePhonenumber;
	@Column(name = "consignee_telephonenumber")
	private Integer consigneeTelephonenumber;
	@Column(name = "consignee_email")
	private String consigneeEmail;
	@Column(name = "consignee_city")
	private String consigneeCity;
	@Column(name = "consignee_area")
	private String consigneeArea;
	@Column(name = "consignee_address")
	private String consigneeAddress;
	@Column(name = "finish_date")
	private java.util.Date finishDate;
	@Column(name = "coupon_id")
	private Integer couponId;

}
