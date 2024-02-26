package tw.eeit175groupone.finalproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "cash_record")
public class CashRecordBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cash_record_id")
    private Integer cashRecordId;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "purchase_amount")
    private Integer purchaseAmount;
    @Column(name = "purchase_date")
    private java.util.Date purchaseDate;
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "cash_record_status")
    private String cashRecordStatus;
    @Column(name = "orderdetail_id")
    private Integer orderdetailId;
    @Column(name = "refund_date")
    private Date refundDate;
    @Column(name = "order_id")
    private Integer orderId;
}
