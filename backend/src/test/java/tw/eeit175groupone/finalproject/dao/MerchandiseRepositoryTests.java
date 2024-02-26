package tw.eeit175groupone.finalproject.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tw.eeit175groupone.finalproject.domain.MerchandiseBean;

import java.util.Optional;

@SpringBootTest
public class MerchandiseRepositoryTests{
    @Autowired
    private MerchandiseRepository merchandiseRepository;

    @Test
    public void findByProductIdAndColorAndSizeTest(){
        Optional<MerchandiseBean> temp=merchandiseRepository.findByProductIdAndColorAndSize(15,"單一","單一");
        if(temp.isPresent()){
            MerchandiseBean merchandiseBean=temp.get();
            System.out.println(merchandiseBean);
        }
    }
    @Test
    public void existsByProductIdTest(){
        System.out.println(merchandiseRepository.existsByProductId(20));
        
    }

    @Test
    public void  findByProductIdAndMerchandiseIdTest(){
        System.out.println(merchandiseRepository.findByProductIdAndMerchandiseId(19,13));

    }
    
    
}
    

