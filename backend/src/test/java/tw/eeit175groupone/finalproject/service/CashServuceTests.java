package tw.eeit175groupone.finalproject.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tw.eeit175groupone.finalproject.domain.ProductBean;

@SpringBootTest
public class CashServuceTests {
    @Autowired
    private CashService cashService;
    
    @Test
    public void TestfindRootNode(){
        // List<ProductBean> rootNode = cashService.findRootNode();
        // System.err.println(rootNode);

    }
}
