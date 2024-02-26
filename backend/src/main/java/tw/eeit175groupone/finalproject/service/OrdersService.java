package tw.eeit175groupone.finalproject.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import tw.eeit175groupone.finalproject.dao.CashRecordRepository;
import tw.eeit175groupone.finalproject.dao.CouponRepository;
import tw.eeit175groupone.finalproject.dao.MerchandiseRepository;
import tw.eeit175groupone.finalproject.dao.OrderRepository;
import tw.eeit175groupone.finalproject.dao.OrderdetailsRepository;
import tw.eeit175groupone.finalproject.dao.ProductRepository;
import tw.eeit175groupone.finalproject.dao.UserRepository;
import tw.eeit175groupone.finalproject.domain.CashRecordBean;
import tw.eeit175groupone.finalproject.domain.CouponBean;
import tw.eeit175groupone.finalproject.domain.MerchandiseBean;
import tw.eeit175groupone.finalproject.domain.OrderdetailsBean;
import tw.eeit175groupone.finalproject.domain.OrdersBean;
import tw.eeit175groupone.finalproject.domain.ProductBean;
import tw.eeit175groupone.finalproject.domain.UserBean;
import tw.eeit175groupone.finalproject.dto.MyOrderDTO;
import tw.eeit175groupone.finalproject.dto.OrderDTO;
import tw.eeit175groupone.finalproject.dto.OrderOrderDTO;
import tw.eeit175groupone.finalproject.util.CreateDatetime;

@Service
@Transactional(rollbackFor = { Exception.class })
public class OrdersService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderdetailsRepository orderdetailsRepository;
    @Autowired
    private MerchandiseRepository merchandiseRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private CashRecordRepository cashRecordRepository;
    @Autowired
    private UserRepository userRepository;

    public String ecpayCheckout(OrdersBean bean) {
        AllInOne all = new AllInOne("");
        AioCheckOutALL obj = new AioCheckOutALL();
        String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
        obj.setMerchantTradeNo(uuId);
        CreateDatetime time = new CreateDatetime();
        obj.setMerchantTradeDate(time.stringTime());
        obj.setTotalAmount(String.valueOf(bean.getTotalAmount()));
        obj.setTradeDesc("test Description");
        obj.setItemName("TestItem");
        // 交易結果回傳網址，只接受 https 開頭的網站，可以使用 ngrok
        obj.setReturnURL("http://localhost:8080/order/ecpayreturn");
        obj.setNeedExtraPaidInfo("N");
        // 商店轉跳網址 (Optional)
        obj.setClientBackURL("http://localhost:5173/home");
        // obj.setOrderResultURL("http://localhost:5173/home");
        String form = all.aioCheckOut(obj, null);

        return form;
    }

    public List<ProductBean> findall() {
        List<ProductBean> beans = productRepository.findAll();
        if (beans != null && !beans.isEmpty()) {
            return beans;
        }
        return null;
    }

    public List<OrdersBean> findOrdersByUserId(Integer userId) {
        if (userId != null) {
            List<OrdersBean> beans = orderRepository.findByUserId(userId);
            if (beans != null && !beans.isEmpty()) {
                return beans;
            }
        }
        return null;
    }

    public OrdersBean findByOrderId(Integer orderId) {
        if (orderId != null) {
            Optional<OrdersBean> bean = orderRepository.findById(orderId);
            if (bean.isPresent()) {
                OrdersBean ordersBean = bean.get();
                return ordersBean;
            }
        }
        return null;
    }

    // 加入orders跟orderdetails資料庫,扣除庫存數量若庫存歸0下架商品
    public OrdersBean findShoppingcartByUserId(Integer userId, OrdersBean ordersBean) {
        if (userId != null && ordersBean != null) {
            try {
                Integer couponId = ordersBean.getCouponId();
                if (couponId != 0) {
                    CouponBean couponBean = couponRepository.findById(couponId).get();
                    couponBean.setUsed("used");
                }
                String paymentMethod = ordersBean.getPaymentMethod();
                ordersBean.setUserId(userId);
                Date date = new CreateDatetime().creataTime();
                ordersBean.setOrderDate(date);
                ordersBean.setOrderStatus("confirmed");
                ordersBean.setPaymentDate(date);
                ordersBean.setPaymentStatus(1);
                orderRepository.save(ordersBean);
                if (paymentMethod.equals("Rootnode")) {
                    this.insertByRootnode(userId, ordersBean);
                }
                List<OrderDTO> orderDTO = this.getOrderDTO(userId);
                for (OrderDTO row : orderDTO) {
                    if (row.getProductStatus().equals("notsale")) {
                        continue;
                    }
                    for (int i = 0; i < row.getQuantity(); i++) {
                        OrderdetailsBean orderdetailsBean = new OrderdetailsBean();
                        orderdetailsBean.setHistoryProductName(row.getProductName());
                        if (row.getProductStatus().equals("discount")) {
                            orderdetailsBean.setHistoryPrice(row.getDiscount().intValue());
                        } else {
                            orderdetailsBean.setHistoryPrice(row.getPrice());
                        }
                        orderdetailsBean.setProductId(row.getProductId());
                        orderdetailsBean.setQuantity(1);
                        orderdetailsBean.setOrderId(ordersBean.getOrderId());

                        orderdetailsBean.setProductCommentStatus("noComment");
                        // 設定留言時間截止是下訂單後一個月
                        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate plusMonths = localDate.plusMonths(1);
                        Date endDate = Date.from(plusMonths.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        orderdetailsBean.setProductCommentEndtime(endDate);

                        if (row.getProductType().equals("digitalGames")) {
                            Random random = new Random();
                            String key = "EEIT-" + random.nextInt(10) + random.nextInt(10)
                                    + random.nextInt(10) + random
                                            .nextInt(10)
                                    + "-" + random.nextInt(10) + random.nextInt(10)
                                    + random.nextInt(10) + random
                                            .nextInt(10)
                                    + "-" + random.nextInt(10) + random.nextInt(10)
                                    + random.nextInt(10) + random
                                            .nextInt(10);
                            orderdetailsBean.setGamekey(key);
                        }
                        orderdetailsRepository.save(orderdetailsBean);
                    }
                    if (row.getProductType().equals("physical")) {
                        List<MerchandiseBean> merchandiseByProductId = merchandiseRepository
                                .findMerchandiseByProductId(row.getProductId());
                        MerchandiseBean merchandiseBean = merchandiseByProductId.get(0);
                        Integer merchandiseQuantity = merchandiseBean.getInventoryQuantity();
                        merchandiseQuantity = merchandiseQuantity - row.getQuantity();
                        merchandiseBean.setInventoryQuantity(merchandiseQuantity);
                        merchandiseRepository.save(merchandiseBean);
                        if (merchandiseQuantity == 0) {
                            Optional<ProductBean> product = productRepository.findById(row.getProductId());
                            ProductBean productBean = product.get();
                            productBean.setProductStatus("notsale");
                            productRepository.save(productBean);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ordersBean;
    }

    // 扣根節點跟紀錄到cash record裡面
    private void insertByRootnode(Integer userId, OrdersBean ordersBean) {
        Integer rootnode = ordersBean.getTotalAmount();
        CashRecordBean cashRecordBean = new CashRecordBean();
        cashRecordBean.setUserId(userId);
        cashRecordBean.setPurchaseAmount(rootnode);
        cashRecordBean.setPurchaseDate(new CreateDatetime().creataTime());
        cashRecordBean.setCashRecordStatus("purchase");
        cashRecordBean.setOrderId(ordersBean.getOrderId());
        cashRecordRepository.save(cashRecordBean);
        Optional<UserBean> byId = userRepository.findById(userId);
        UserBean userBean = byId.get();
        userBean.setCash(userBean.getCash() - rootnode);
        userRepository.save(userBean);

    }

    // 取得OrderDTO
    public List<OrderDTO> getOrderDTO(Integer userId) {
        if (userId != null) {
            List<Object[]> shoppingcartByUserId = orderRepository.findShoppingcartByUserId(userId);
            List<OrderDTO> array = new ArrayList<OrderDTO>();
            for (Object[] objects : shoppingcartByUserId) {
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setCartmiddleId((Integer) objects[0]);
                orderDTO.setProductId((Integer) objects[1]);
                orderDTO.setShoppingcartId((Integer) objects[2]);
                orderDTO.setQuantity((Integer) objects[3]);
                orderDTO.setUserId((Integer) objects[4]);
                orderDTO.setPrice((Integer) objects[5]);
                orderDTO.setProductName((String) objects[6]);
                orderDTO.setProductType((String) objects[7]);
                orderDTO.setProductStatus((String) objects[8]);
                orderDTO.setDiscount((Double) objects[9]);
                array.add(orderDTO);
            }
            return array;
        }
        return null;
    }

    // 取得MyOrderDTO
    public List<MyOrderDTO> findOrderdetail(Integer orderId) {
        List<Object[]> result = orderRepository.findOrderdetail(orderId);
        ArrayList<MyOrderDTO> array = new ArrayList<MyOrderDTO>();
        for (Object[] temp : result) {
            MyOrderDTO dto = new MyOrderDTO();
            BeanUtils.copyProperties((OrdersBean) temp[0], dto);
            BeanUtils.copyProperties((OrderdetailsBean) temp[1], dto);
            array.add(dto);
        }
        return array;
    }

    // 用userId找order
    public List<OrdersBean> findOrder(Integer userId) {
        List<OrdersBean> result = orderRepository.findOrder(userId);
        if (result != null && !result.isEmpty()) {
            return result;
        }
        return null;
    }

    // test
    public List<OrderOrderDTO> findOrderOrder(Integer userId) {
        List<OrderOrderDTO> orderOrderDTOs = new ArrayList();
        List<OrdersBean> orders = orderRepository.findOrder(userId);
        // int count = 0;
        // 將orders這個orderBean裡面的orderId取出收集成list
        List<Integer> ordersIds = orders.stream()
                .map(OrdersBean::getOrderId)
                .toList();
        List<OrderdetailsBean> allOrderdetails = orderdetailsRepository.findAllByOrderIdIn(ordersIds);
        if (allOrderdetails.isEmpty()) {
            return null;
        }
        for (OrdersBean ordertemp : orders) {
            OrderOrderDTO orderOrderDTO = new OrderOrderDTO();
            List<OrderdetailsBean> details = new ArrayList<>();
            orderOrderDTO.setOrdersBean(ordertemp);
            for (OrderdetailsBean orderdetailstemp : allOrderdetails) {
                if (orderdetailstemp.getOrderId().equals(ordertemp.getOrderId())) {
                    details.add(orderdetailstemp);
                }
            }
            orderOrderDTO.setOrderdetailsBean(details);
            orderOrderDTOs.add(orderOrderDTO);
        }
        // Iterator<OrdersBean> iterator = orders.iterator();
        // // System.out.println("orders = "+orders);
        // while (iterator.hasNext()) {
        // OrderOrderDTO orderOrderDTO = new OrderOrderDTO();
        // OrdersBean next = iterator.next();
        // orderOrderDTO.setOrdersBean(next);
        // List<OrderdetailsBean> orderdetails =
        // orderdetailsRepository.findByOrderId(next.getOrderId());
        // orderOrderDTO.setOrderdetailsBean(orderdetails);
        // orderOrderDTOs.add(orderOrderDTO);
        // // count++;
        // }
        orderOrderDTOs=orderOrderDTOs.stream()
                .sorted(Comparator.comparing(all->all.getOrdersBean().getOrderDate(), Comparator.reverseOrder()))
                .toList();


        if (orderOrderDTOs.size() != 0) {
            return orderOrderDTOs;
        }
        return null;
    }

}
