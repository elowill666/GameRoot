package tw.eeit175groupone.finalproject.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tw.eeit175groupone.finalproject.domain.GameInforBean;
import tw.eeit175groupone.finalproject.domain.MerchandiseBean;
import tw.eeit175groupone.finalproject.domain.ProductBean;
import tw.eeit175groupone.finalproject.domain.ProductImageBean;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class ProductDTO {

    private ProductBean product;
    private GameInforBean gameInfor;
    private MerchandiseBean merchandise;
    private List<ProductImageBean> productImages;
    
}
