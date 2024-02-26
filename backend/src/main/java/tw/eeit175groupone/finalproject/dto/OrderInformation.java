package tw.eeit175groupone.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInformation {
    private String billingUsername;
    private String billingCity;
    private String billingArea;
    private String billingAddress;
    private String consigneeUsername;
    private Integer consigneePhonenumber;
    private String consigneeCity;
    private String consigneeArea;
    private String consigneeEmail;
    private String consigneeAddress;
    private String paymentMethod;
    private Integer totalAmount;

}
