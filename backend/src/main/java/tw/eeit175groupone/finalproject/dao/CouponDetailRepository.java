package tw.eeit175groupone.finalproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.eeit175groupone.finalproject.domain.CouponDetail;
import tw.eeit175groupone.finalproject.dto.CouponDto;

public interface CouponDetailRepository extends JpaRepository<CouponDto,Integer>{
}
