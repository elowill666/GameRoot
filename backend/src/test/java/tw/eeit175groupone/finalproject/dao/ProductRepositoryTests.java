package tw.eeit175groupone.finalproject.dao;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tw.eeit175groupone.finalproject.domain.GameInforBean;
import tw.eeit175groupone.finalproject.domain.MerchandiseBean;
import tw.eeit175groupone.finalproject.domain.ProductBean;
import tw.eeit175groupone.finalproject.dto.CompleteProductInfo;

@SpringBootTest
public class ProductRepositoryTests{
    @Autowired
    private ProductRepository productRepository;


    @Test
    public void testProduct(){
        List<Object[]> selectAll=productRepository.selectIdNamePrice();
        for(Object[] row : selectAll){
            Integer id=(Integer) row[0];
            String name=(String) row[1];
            Integer price=(Integer) row[2];

            System.out.println("ID: "+id+", Name: "+name+", Price: "+price);
        }
    }

    @Test
    public void testFindDiscountProduct(){
        List<ProductBean> productBeans=productRepository.findDiscountProduct();
        for(ProductBean temp : productBeans){
            System.out.println(temp);
        }

    }

    @Test
    public void testFindProductOutoffDiscount(){
        List<ProductBean> productBeans=productRepository.findProductOutoffDiscount();
        for(ProductBean temp : productBeans){
            System.out.println(temp);
        }

    }

    @Test
    public void testfindErrorDiscount(){
        List<ProductBean> productBeans=productRepository.findErrorDiscount();
        for(ProductBean temp : productBeans){
            System.out.println(temp);
        }
    }

    @Test
    public void findAllTest(){
        List<ProductBean> findall=productRepository.findAll();
        for(ProductBean temp : findall){
            System.out.println(findall);
        }

    }

    @Test
    public void findAllGameProduct(){
        List<Object[]> find=productRepository.findAllGameProduct();
        for(Object[] temp : find){
            CompleteProductInfo dto=new CompleteProductInfo();
            BeanUtils.copyProperties((ProductBean) temp[0],dto);
            if(temp[1]!=null){
                BeanUtils.copyProperties((GameInforBean) temp[1],dto);
            }
            System.out.println("dto="+dto);
        }

    }

    @Test
    public void findAllMerchandiseProduct(){
        List<Object[]> find=productRepository.findAllMerchdiseProduct();
        for(Object[] temp : find){
            CompleteProductInfo dto=new CompleteProductInfo();

            BeanUtils.copyProperties((ProductBean) temp[0],dto);
            if(temp[1]!=null){
                BeanUtils.copyProperties((MerchandiseBean) temp[1],dto);
            }
            System.out.println("dto="+dto);
        }
    }
    
    @Test
    public void findProductByCash(){
        List<ProductBean> temp=productRepository.findProductByCash();
        for(ProductBean p : temp){
            System.out.println(p);
        }
    }
    
    
    
    @Test
    public void testfinddiscountProduct(){
        List<ProductBean> temp=productRepository.findDiscountProductAndEndtimeNotOver();
        for(ProductBean p : temp){
            System.out.println(p);
        }
    }

    @Test
    public void findCoverImageById(){
        Optional<String> coverImageById=productRepository.findCoverImageById(1);

        String p=coverImageById.get();
        System.out.println(p);
    }


    //    @Test
//    public void findCoverImageByIdByJPA(){
//        Optional<String> coverImageById=productRepository.findCoverImageByIdByJPA(1);
//
//        String p=coverImageById.get();
//        System.out.println(p);
//    }
    @Test
    public void findSubTypeTest(){
        List<String> temp=productRepository.findSubtype("physical");

        System.out.println(temp);
    }

    @Test
    public void findTopProductTest(){
        List<ProductBean> temp=productRepository.findTopProduct(10);

        System.out.println(temp);
    }

    @Test
    public void findTopProductwithNativeQueryTest(){
        List<ProductBean> temp=productRepository.findTopProductwithNativeQuery(10);

        System.out.println(temp);
    }

    @Test
    public void findProductwithPageableTest(){
        List<ProductBean> temp=productRepository.findProductwithPageable(10,10);

        System.out.println(temp);
    }

    @Test
    public void findWithLimitAndOffsetTest(){
        List<ProductBean> temp=productRepository.findWithLimitAndOffset(10,10);
        for(ProductBean p : temp){
            System.out.println(p.getProductId());
        }
    }

    @Test
    public void countByProductIdTest(){
        Long temp=productRepository.count();
        System.out.println(temp.intValue());
    }

    @Test
    public void findproductNameTest(){
        List<String> adventure=productRepository.findProductName("adventure");
        System.out.println(adventure);
    }
    
    
}
