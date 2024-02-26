package tw.eeit175groupone.finalproject.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tw.eeit175groupone.finalproject.domain.ProductBean;

@Setter
@Getter
@Data
@NoArgsConstructor
public class ProductMonthlySaleDTO {

    private ProductBean product;
    private Long productMonthlySales;
    
}
