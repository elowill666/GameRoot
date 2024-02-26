package tw.eeit175groupone.finalproject.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.eeit175groupone.finalproject.domain.MerchandiseBean;


@Repository
public interface MerchandiseRepository extends JpaRepository<MerchandiseBean, Integer>{
	
	@Query("select m from MerchandiseBean m where m.productId = :productId")
	List<MerchandiseBean> findMerchandiseByProductId(@Param("productId")Integer productId);
	
	Optional<MerchandiseBean> findByProductIdAndColorAndSize(Integer productId,String color,String size);
	
	boolean existsByProductId(Integer productId);


	Optional<MerchandiseBean> findByProductIdAndMerchandiseId(Integer productId,Integer merchandiseId);
	
}
