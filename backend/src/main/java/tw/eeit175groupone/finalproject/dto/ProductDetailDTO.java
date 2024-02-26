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

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductDetailDTO {
    private ProductBean product;
    private List<ProductImageBean> productImages;
    private List<ProductImageBean> descriptionImages;
    private List<Object[]> productComments;
    private List<MerchandiseBean> merchandises;
    private List<GameInforBean> gameInfor;
    private List<Object[]> productArticles;

}
