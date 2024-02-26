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
@Table(name="coupon")
@Data
public class CouponBean {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="coupon_id")
	private Integer couponId;
    @Column(name="discount")
	private Float discount;
    @Column(name="used")
	private String used;
    @Column(name="info")
	private String info;
    @Column(name="begin_of_Valid_date")
	private Date beginDate;
    @Column(name="end_of_valid_date")
	private Date endDate;
    @Column(name="user_id")
	private Integer userId;
	@Column(name="coupon_detail_id")
	private Integer couponDetailId;

}
