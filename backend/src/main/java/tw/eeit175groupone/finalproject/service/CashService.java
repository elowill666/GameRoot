package tw.eeit175groupone.finalproject.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import jakarta.transaction.Transactional;
import tw.eeit175groupone.finalproject.dao.CashRecordRepository;
import tw.eeit175groupone.finalproject.dao.OrderRepository;
import tw.eeit175groupone.finalproject.dao.OrderdetailsRepository;
import tw.eeit175groupone.finalproject.dao.ProductRepository;
import tw.eeit175groupone.finalproject.dao.UserRepository;
import tw.eeit175groupone.finalproject.domain.CashRecordBean;
import tw.eeit175groupone.finalproject.domain.OrderdetailsBean;
import tw.eeit175groupone.finalproject.domain.OrdersBean;
import tw.eeit175groupone.finalproject.domain.ProductBean;
import tw.eeit175groupone.finalproject.domain.UserBean;
import tw.eeit175groupone.finalproject.util.CreateDatetime;

@Service
@Transactional
public class CashService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CashRecordRepository cashRecordRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderdetailsRepository orderdetailsRepository;

    public String ecpayCheckoutRootnode(Integer cash) {
        AllInOne all = new AllInOne("");
        AioCheckOutALL obj = new AioCheckOutALL();
        String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
        obj.setMerchantTradeNo(uuId);
        CreateDatetime time = new CreateDatetime();
        obj.setMerchantTradeDate(time.stringTime());
        obj.setTotalAmount(String.valueOf(cash));
        obj.setTradeDesc("test Description");
        obj.setItemName("根節點");
        // 交易結果回傳網址，只接受 https 開頭的網站，可以使用 ngrok
        obj.setReturnURL("http://localhost");
        obj.setNeedExtraPaidInfo("N");
        // 商店轉跳網址 (Optional)
        obj.setClientBackURL("http://localhost:5173/home");
        // obj.setOrderResultURL("http://localhost:5173/home");
        String form = all.aioCheckOut(obj, null);
        return form;
    }

    /**
     * 做出根節點儲值網頁
     * 
     * @return List<ProductBean>
     */
    public List<ProductBean> findRootNodeProduct() {
        String cash = "cash";
        List<ProductBean> productbeans = productRepository.findByProductType(cash);
        for (ProductBean productBean : productbeans) {
            productBean.setCoverImage(productRepository.findCoverImageById(productBean.getProductId()).get());
        }
        return productRepository.findByProductType(cash);
    }

    public void insertCash(Integer cash, Integer productId, Integer id, Integer discount) {
        if (cash != null && id != null && productId != null) {
            CashRecordBean bean = new CashRecordBean();
            bean.setUserId(id);
            if (discount != 0) {
                bean.setPurchaseAmount(discount);
            } else {
                bean.setPurchaseAmount(cash);
            }
            CreateDatetime createDatetime = new CreateDatetime();
            Date date = createDatetime.creataTime();
            bean.setPurchaseDate(date);
            bean.setTransactionId(null);
            bean.setCashRecordStatus("recharge");
            cashRecordRepository.save(bean);
            OrdersBean ordersBean = new OrdersBean();
            ordersBean.setUserId(id);
            ordersBean.setTotalAmount(cash);
            ordersBean.setOrderDate(date);
            ordersBean.setPaymentDate(date);
            ordersBean.setFinishDate(date);
            ordersBean.setOrderStatus("finish");
            ordersBean.setPaymentMethod("ECPAY");
            ordersBean.setPaymentStatus(1);
            orderRepository.save(ordersBean);
            ProductBean productBean = productRepository.findById(productId).get();
            OrderdetailsBean orderdetailsBean = new OrderdetailsBean();
            orderdetailsBean.setQuantity(1);
            orderdetailsBean.setHistoryPrice(cash);
            orderdetailsBean.setHistoryProductName(productBean.getProductName());
            orderdetailsBean.setOrderId(ordersBean.getOrderId());
            orderdetailsBean.setProductId(productId);
            orderdetailsRepository.save(orderdetailsBean);
            UserBean userBean = userRepository.findById(id).get();
            if (discount != 0) {
                userBean.setCash(userRepository.findById(id).get().getCash() + discount);
            } else {
                userBean.setCash(userRepository.findById(id).get().getCash() + cash);
            }
            userRepository.save(userBean);
        }
    }

}
