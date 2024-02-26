package tw.eeit175groupone.finalproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import tw.eeit175groupone.finalproject.domain.CashRecordBean;

import java.util.List;

@Repository
@Transactional
public interface CashRecordRepository extends JpaRepository<CashRecordBean, Integer>{

    
    void deleteAllByOrderdetailIdIn(List<Integer> orderdetailIds);
}
