package tw.eeit175groupone.finalproject.dto;



import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tw.eeit175groupone.finalproject.domain.OrderdetailsBean;
import tw.eeit175groupone.finalproject.domain.OrdersBean;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class OrderOrderDTO {
    
    private OrdersBean ordersBean;
    private List<OrderdetailsBean> orderdetailsBean;
}
