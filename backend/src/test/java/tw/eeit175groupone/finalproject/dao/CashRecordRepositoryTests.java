package tw.eeit175groupone.finalproject.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CashRecordRepositoryTests{
    @Autowired
    private CashRecordRepository cashRecordRepository;
    
    @Test
    public void deleteByOrderdetailIdInTest(){
        List<Integer> deleteids=new ArrayList<>();
        deleteids.add(1);
        deleteids.add(2);
        deleteids.add(3);
        deleteids.add(4);
        deleteids.add(5);
        
        cashRecordRepository.deleteAllByOrderdetailIdIn(deleteids);
        
    }
}
