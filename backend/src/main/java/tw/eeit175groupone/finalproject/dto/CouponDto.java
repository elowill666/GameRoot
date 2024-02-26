package tw.eeit175groupone.finalproject.dto;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="coupon")
public class CouponDto {
    @Id
	@Column(name="coupon_id")
	private Integer coupon_id;
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
	private List<Integer> userId;
}
