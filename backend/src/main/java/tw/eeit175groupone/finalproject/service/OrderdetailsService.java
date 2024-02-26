package tw.eeit175groupone.finalproject.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.eeit175groupone.finalproject.dao.OrderdetailsRepository;
import tw.eeit175groupone.finalproject.domain.OrderdetailsBean;
import tw.eeit175groupone.finalproject.util.CreateDatetime;

@Service
public class OrderdetailsService {

  @Autowired
  private OrderdetailsRepository orderdetailsRepository;

  public List<OrderdetailsBean> findAll() {
    return orderdetailsRepository.findAll();
  }

  public List<OrderdetailsBean> findByOrderId(Integer id) {
    return orderdetailsRepository.findByOrderId(id);
  }

  public boolean redfundOrderdetail(OrderdetailsBean orderdetailsBean) {
    try {
      if (orderdetailsBean != null) {
        Date date = new CreateDatetime().creataTime();
        orderdetailsBean.setRefundDate(date);
        Optional<OrderdetailsBean> old = orderdetailsRepository.findById(orderdetailsBean.getOrderdetailId());
        OrderdetailsBean oldOrderdetailsBean = old.get();
        oldOrderdetailsBean.setReason(orderdetailsBean.getReason());
        oldOrderdetailsBean.setReasonType(orderdetailsBean.getReasonType());
        oldOrderdetailsBean.setRefundStatus("pending");
        oldOrderdetailsBean.setRefundDate(date);
        orderdetailsRepository.save(oldOrderdetailsBean);
        return true;
      }
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
