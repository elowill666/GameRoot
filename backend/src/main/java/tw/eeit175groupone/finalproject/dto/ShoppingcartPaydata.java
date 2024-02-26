package tw.eeit175groupone.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingcartPaydata {
    private Integer alltotalprice;
    private String username;
    private String email;
    private String phone_number;
    private String membership;
    private Integer cash;
    private Integer couponId;
    private Integer UserId;
}
