package tw.eeit175groupone.finalproject.domain;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "coupon_detail")
@Entity
@NoArgsConstructor
public class CouponDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="coupon_detail_id")
    private Integer couponDetailId;
    @Column(name="coupon_name",columnDefinition="nvarchar")
    private String couponName;
    @Column(name="discount")
    private Integer discount;
    @Column(name="info",columnDefinition="nvarchar")
    private String info;
    @Column(name="begin_of_receive_date")
    private Date beginOfReceiveDate;
    @Column(name="end_of_receive_date")
    private Date endOfReceiveDate;
    @Column(name="receive_status")
    private String receiveStatus;
    @Column(name="begin_of_valid_date")
    private Date beginOfValidDate;
    @Column(name="end_of_valid_date")
    private Date endOfValidDate;
    @Column(name="target_user_membership")
    private String targetUserMembership;
    @Column(name="receive_method")
    private String receiveMethod;
    
}
