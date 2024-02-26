package tw.eeit175groupone.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyOrderDTO {
    private Integer productId;
    private Integer orderId;
    private Integer orderdetailId;
    private Integer userId;
    private String productName;
    private String historyProductName;
    private Integer historyPrice;
    private Integer totalAmount;
    private Integer quantity;
    private String orderStatus;
    private java.util.Date orderDate;
    private String paymentMethod;
    private Integer paymentStatus;
    private java.util.Date paymentDate;
    private java.util.Date shipmentDate;
    private String consigneeCity;
    private String consigneeArea;
    private String consigneeAddress;
    private Integer consigneeTelephonenumber;
    private String consigneeUsername;
    private String productCommentStatus;
    private String gamekey;
    private java.util.Date productCommentEndtime;
    private java.util.Date finishDate;
    private String refundStatus;
}
