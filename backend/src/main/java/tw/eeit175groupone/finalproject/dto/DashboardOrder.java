package tw.eeit175groupone.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardOrder{

    private Integer orderId;
    private String orderStatus;
    private Integer paymentStatus;
    private Map<Integer, String> refundStatus;

}


