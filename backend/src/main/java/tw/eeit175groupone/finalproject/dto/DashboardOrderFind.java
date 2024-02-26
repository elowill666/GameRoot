package tw.eeit175groupone.finalproject.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardOrderFind {

    private Integer orderId;
    private Integer userId;

    private Integer totalAmountMin;
    private Integer totalAmountMax;
    private String orderStatus;
    private java.util.Date orderDateStart;
    private java.util.Date orderDateEnd;
    private String paymentMethod;
    private Integer paymentStatus;
    private java.util.Date paymentDate;
    private java.util.Date shipmentDate;
    private java.util.Date updateDate;
    private java.util.Date finishDate;
    
    private String refundStatus;
    
    private Map<String,String> orderPage;
    private Integer start;
    private Integer rows;
    private String sort;

}
