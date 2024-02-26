package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.eeit175groupone.finalproject.domain.GameInforBean;





@Repository
public interface GameInforRepository extends JpaRepository<GameInforBean, Integer>{
    @Query("select g from GameInforBean g where g.gameInforId = :productId")
    public List<GameInforBean> findGameInforByProductId(@Param("productId")Integer productId);
    
    
}
