package tw.eeit175groupone.finalproject.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tw.eeit175groupone.finalproject.domain.ProductBean;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductSearchDTO {
    
    private List<ProductBean> beans;
    private Long count;
}
