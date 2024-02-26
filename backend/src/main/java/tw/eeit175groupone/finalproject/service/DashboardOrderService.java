package tw.eeit175groupone.finalproject.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.eeit175groupone.finalproject.dao.*;
import tw.eeit175groupone.finalproject.domain.CashRecordBean;
import tw.eeit175groupone.finalproject.domain.OrderdetailsBean;
import tw.eeit175groupone.finalproject.domain.OrdersBean;
import tw.eeit175groupone.finalproject.domain.UserBean;
import tw.eeit175groupone.finalproject.dto.DashboardOrder;
import tw.eeit175groupone.finalproject.dto.DashboardOrderFind;
import tw.eeit175groupone.finalproject.dto.OrderDTO;
import tw.eeit175groupone.finalproject.dto.OrderOrderDTO;

import java.util.*;
import java.util.stream.Collectors;

@Transactional(rollbackFor={Exception.class})
@Service
public class DashboardOrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderdetailsRepository orderdetailsRepository;
    @Autowired
    private CashRecordRepository cashRecordRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * 找出所有的order
     *
     * @param request--Map<String, String> 前端傳來的找尋條件應至少有三個key 1.start起始數字、2.rows需要資料數和3.排序方式
     * @return List<OrdersBean>--所有的訂單資料
     */
    public List<OrderOrderDTO> findAllOrder(DashboardOrderFind request){

        List<Integer> orderIds=new ArrayList<>();
        if(request.getRefundStatus()!=null && request.getRefundStatus().length()!=0){
            if(request.getRefundStatus().equals("allRefund")){
                orderIds=orderdetailsRepository.findOrderIdByAllRefundStatus();
            }else{
                orderIds=orderdetailsRepository.findOrderIdByRefundStatus(request.getRefundStatus());
            }
            if(orderIds.isEmpty()){
                return null;
            }
        }
        List<OrdersBean> all=this.findOrdersWithCriteria(request,orderIds);
        System.out.println(all);
        if(all==null || all.isEmpty()){
            System.out.println("Empty");
            return null;
        }
        //每次取出會去更新繳費沒，沒繳費把orderStatus改成cancel;
        all=this.checkStatus(all);
        all=orderRepository.saveAll(all);
        if(all==null || all.isEmpty()){
            return null;
        }
        List<OrderOrderDTO> orderWithDetails=this.combineOrderAndDetail(all);
        if(orderWithDetails!=null && !orderWithDetails.isEmpty()){
            return orderWithDetails;
        }
        return null;
    }


    /**
     * 用會員ID找某個使用者的訂單
     *
     * @param userId--Integer
     * @return List<OrdersBean>--所有的訂單資料
     */
    public List<OrderOrderDTO> findOrderById(Integer userId,Map<String, String> request){
        Pageable orderPageable=this.orderPageable(request);
        if(orderPageable==null){
            return null;
        }
        List<OrdersBean> userOrders=orderRepository.findByUserId(userId,orderPageable);
        if(userOrders==null || userOrders.isEmpty()){
            return null;
        }
        //每次取出會去更新繳費沒，沒繳費把orderStatus改成cancel;
        userOrders=this.checkStatus(userOrders);
        userOrders=orderRepository.saveAll(userOrders);
        if(userOrders==null || userOrders.isEmpty()){
            return null;
        }
        List<OrderOrderDTO> orderWithDetails=this.combineOrderAndDetail(userOrders);
        if(orderWithDetails!=null && !orderWithDetails.isEmpty()){
            return orderWithDetails;
        }
        return null;
    }

    /**
     * 把訂單詳細資料合併到訂單資料
     *
     * @param ordersBeans
     * @return
     */

    public List<OrderOrderDTO> combineOrderAndDetail(List<OrdersBean> ordersBeans){
        List<Integer> orderIds=new ArrayList<>();
        for(OrdersBean temp : ordersBeans){
            orderIds.add(temp.getOrderId());
        }
        List<OrderdetailsBean> orderDetails=orderdetailsRepository.findAllByOrderIdIn(orderIds);
        if(orderDetails.isEmpty() || orderDetails==null){
            return null;
        }
        List<OrderOrderDTO> orderWithDetails=new ArrayList<>();
        for(OrdersBean temp : ordersBeans){
            List<OrderdetailsBean> tempDetail=new ArrayList<>();
            OrderOrderDTO tempDTO=new OrderOrderDTO();
            //如果太久沒繳費就把Order狀態變成

            tempDTO.setOrdersBean(temp);
            for(OrderdetailsBean tempod : orderDetails){
                if(tempod.getOrderId().equals(temp.getOrderId())){
                    tempDetail.add(tempod);
                }
            }
            tempDTO.setOrderdetailsBean(tempDetail);
            orderWithDetails.add(tempDTO);
        }
        return orderWithDetails;
    }

    /**
     * 把拿出來的資料先更新改變狀態用
     *
     * @param beans--List<OrdersBean>
     * @return List<OrdersBean>
     */
    public List<OrdersBean> checkStatus(List<OrdersBean> beans){
        //如果pay狀態==-1，且new Date-orderdate>30*24*60*60*1000
        //就把orderStatus調成cancel
        List<OrdersBean> list=new ArrayList<>();
        for(OrdersBean temp : beans){
            Long passedtime=System.currentTimeMillis()-temp.getOrderDate().getTime();
            if(passedtime>2592000000L && temp.getPaymentStatus().equals(-1)){
                temp.setOrderStatus("cancel");
            }
            list.add(temp);
        }
        return list;
    }


    /**
     * 提供資料起始、資料數、及排序方式
     *
     * @param request--Map<String, String>應有三個key 1.start起始數字、2.rows需要資料數和3.排序方式
     */
    public Pageable orderPageable(Map<String, String> request){
        if(!request.containsKey("start") || !request.containsKey("rows")){
            return null;
        }
        try{
            //去出Map的資料
            Integer start=Integer.parseInt(request.getOrDefault("start",null));
            Integer rows=Integer.parseInt(request.getOrDefault("rows",null));
            String sort=request.getOrDefault("sort",null);
            if(sort==null && sort.length()==0){
                return null;
            }
            //取的排序方式
            Sort orderSort=this.orderSort(sort);
            if(orderSort==null){
                return null;
            }
            //回傳找資料需要的Pageable
            rows=rows;
            start=(start-1);
            return PageRequest.of(start,rows,orderSort);

        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 將辨識排序的字串丟入回傳排序方式
     *
     * @param sort--String排序方式
     * @return Sort
     */


    public Sort orderSort(String sort){
        if("createdDESC".equals(sort)){
            return Sort.by(Sort.Order.desc("orderDate"),Sort.Order.desc("updateDate"));
        } else if
        ("createdASC".equals(sort)){
            return Sort.by(Sort.Order.asc("orderDate"),Sort.Order.desc("updateDate"));
        } else if
        ("totalDESC".equals(sort)){
            return Sort.by(Sort.Order.desc("totalAmount"),Sort.Order.desc("orderDate"));
        } else if
        ("totalASC".equals(sort)){
            return Sort.by(Sort.Order.asc("totalAmount"),Sort.Order.desc("orderDate"));
        } else if
        ("updateDESC".equals(sort)){
            return Sort.by(Sort.Order.desc("updateDate"),Sort.Order.desc("orderDate"));
        } else if
        ("updateASC".equals(sort)){
            return Sort.by(Sort.Order.asc("updateDate"),Sort.Order.desc("orderDate"));
        } else if
        ("idasc".equals(sort)){
            return Sort.by(Sort.Direction.ASC,"orderId");
        }
        return Sort.by(Sort.Direction.DESC,"orderDate");
    }

    /**
     * 用訂單編號找更改某個訂單
     *
     * @param orderId--Integer
     * @return OrdersBean--所有的訂單資料
     */
    public OrderOrderDTO modifyByOrderId(Integer orderId,OrderOrderDTO request){
        Integer paymentStatus=request.getOrdersBean().getPaymentStatus();
        String orderStatus=request.getOrdersBean().getOrderStatus();
        List<OrderdetailsBean> requestOrderdetailsBean=request.getOrderdetailsBean();
        OrderOrderDTO refundResult=new OrderOrderDTO();
        List<OrderdetailsBean> before=orderdetailsRepository.findByOrderId(orderId);
        Map<Integer,String> tempRefund=new HashMap<>();
        if(paymentStatus==null || orderStatus==null || orderStatus.length()==0){
            return null;
        }
        if(!orderRepository.existsById(orderId)){
            return null;
        }
        OrdersBean ordersBean=orderRepository.findById(orderId).orElse(null);
        if(ordersBean==null){
            return null;
        }
        //跑去更新訂單及付款狀態
        OrdersBean afterModifyStatus=modifyOrderAndCheckPayment(ordersBean,paymentStatus,orderStatus);
        
        if(afterModifyStatus==null){
            return null;
        }
        OrdersBean save=orderRepository.save(ordersBean);
        if(save==null){
            return null;
        }
        refundResult.setOrdersBean(save);
        
        if(requestOrderdetailsBean!=null && !requestOrderdetailsBean.isEmpty()){
            for(OrderdetailsBean temp:before){
                tempRefund.put(temp.getOrderdetailId(),temp.getRefundStatus());
            }
            refundResult.setOrderdetailsBean(this.modifyOrderdetailRefund(before,requestOrderdetailsBean));
            if(refundResult.getOrderdetailsBean()==null){
                return null;
            }
        }
        
        if(refundResult.getOrdersBean().getPaymentStatus().equals(1)){
            this.modifyRefundAndCash(tempRefund,refundResult.getOrderdetailsBean());
        }
        
        
        if(refundResult!=null){
            refundResult.getOrdersBean().setUpdateDate(new Date(System.currentTimeMillis()));

            refundResult.setOrdersBean(orderRepository.save(refundResult.getOrdersBean()));
            
            return refundResult;
        }
        return null;
    }

    /**
     * 更新訂單明細的退款狀態及原因
     *
     * @param before--List<OrderdetailsBean>原本的訂單明細
     * @param after--List<OrderdetailsBean>前端傳回來更改的訂單明細
     * @return Map<Integer,String>
     */
    public List<OrderdetailsBean> modifyOrderdetailRefund(List<OrderdetailsBean> before,List<OrderdetailsBean> after){
        for(OrderdetailsBean temp : before){
            for(OrderdetailsBean temp2 : after){
                if(temp.getOrderdetailId().equals(temp2.getOrderdetailId())){
                    temp.setRefundStatus(temp2.getRefundStatus());
                    temp.setReviewReason(temp2.getReviewReason());
                }
            }
        }
        
        return orderdetailsRepository.saveAll(before);
    }

    /**
     * 更新資料用的付款狀態
     *
     * @param ordersBean--OrdersBean訂單資料
     * @param paymentStatus--Integer
     * @param orderStatus--String
     * @return Map<Integer,String>
     */
    public OrdersBean modifyOrderAndCheckPayment(OrdersBean ordersBean,Integer paymentStatus,String orderStatus){
        System.err.println("paymentStatus="+paymentStatus+",orderStatus="+orderStatus);
        //如果payment要改已取消orderStatus也只能是已取消
        //如果orderStatus改已取消payment只能改已取消，paymentDate、shipmentDate還finishDate=null，update更新
        if(!paymentStatus.equals(ordersBean.getPaymentStatus()) || !orderStatus.equals(ordersBean.getOrderStatus())){
            if("cancel".equals(orderStatus)){
                System.err.println("訂單取消");
                ordersBean.setOrderStatus("cancel");
                ordersBean.setPaymentStatus(-1);
                ordersBean.setFinishDate(null);
                ordersBean.setShipmentDate(null);
                ordersBean.setUpdateDate(new Date(System.currentTimeMillis()));
                return ordersBean;
                //如果payment要改待確認orderStatus也只能改成待確認
                //如果orderStatus改已待確認payment只能改已待確認，paymentDate、shipmentDate還finishDate=null，update更新
            } else if("ToBeConfirmed".equals(orderStatus)){
                System.err.println("訂單未知");
                ordersBean.setOrderStatus("ToBeConfirmed");
                ordersBean.setPaymentStatus(999);
                ordersBean.setFinishDate(null);
                ordersBean.setShipmentDate(null);
                ordersBean.setUpdateDate(new Date(System.currentTimeMillis()));
                return ordersBean;
                //如果原本是payment已取消
                //改成未付款orderStatus改成已成立
                //改成已付款orderStatus改成已成立
            } else if(-1==paymentStatus){
                System.err.println("訂單取消");
                ordersBean.setOrderStatus("cancel");
                ordersBean.setPaymentStatus(-1);
                ordersBean.setFinishDate(null);
                ordersBean.setShipmentDate(null);
                ordersBean.setUpdateDate(new Date(System.currentTimeMillis()));
                return ordersBean;

            } else if(999==paymentStatus){
                System.err.println("訂單未知");
                ordersBean.setOrderStatus("ToBeConfirmed");
                ordersBean.setPaymentStatus(999);
                ordersBean.setFinishDate(null);
                ordersBean.setShipmentDate(null);
                ordersBean.setUpdateDate(new Date(System.currentTimeMillis()));
                return ordersBean;
            } else if(0==paymentStatus || 1==paymentStatus){
                System.err.println("訂單已付款");
                ordersBean.setPaymentStatus(paymentStatus);
                ordersBean.setOrderStatus("confirmed");
                ordersBean.setUpdateDate(new Date(System.currentTimeMillis()));
                ordersBean.setShipmentDate(null);
                ordersBean.setFinishDate(null);
            } else{
                return null;
            }
        }
        if(!orderStatus.equals(ordersBean.getOrderStatus()) && 1==ordersBean.getPaymentStatus()){
            //如果orderStatus改成已送出shipmentDate和update更新
            if("sent".equals(orderStatus)){
                System.err.println("訂單已送出");
                ordersBean.setOrderStatus("sent");
                ordersBean.setUpdateDate(new Date(System.currentTimeMillis()));
                ordersBean.setShipmentDate(new Date(System.currentTimeMillis()));
                ordersBean.setFinishDate(null);
                return ordersBean;
                //如果orderStatus改成已送出finishDate和update更新,假如shipmentDate=null一起更新
            } else if("finish".equals(orderStatus)){
                System.err.println("訂單已完成");
                if(ordersBean.getShipmentDate()==null){
                    ordersBean.setShipmentDate(new Date(System.currentTimeMillis()));
                }
                ordersBean.setOrderStatus("finish");
                ordersBean.setUpdateDate(new Date(System.currentTimeMillis()));
                ordersBean.setFinishDate(new Date(System.currentTimeMillis()));
                return ordersBean;
            }
        }
        return ordersBean;
    }


    public boolean modifyRefundAndCash(Map<Integer,String> before,List<OrderdetailsBean> after){
        List<Integer> tempCancel=new ArrayList<>();
        int totalCancel=0;
        int totalApprove=0;
        int totalFinal=0;
        List<CashRecordBean> tempApprove=new ArrayList<>();
        Set<Integer> allIds=before.keySet();
        List<OrderdetailsBean> afterTemp=after;
        List<CashRecordBean> resultCash=null;
        OrdersBean tempOrder=orderRepository.findById(afterTemp.get(0).getOrderId()).orElse(null);
        if(tempOrder==null){
            return false;
        }
        for(Integer temp1 : allIds){
            for(OrderdetailsBean temp2 : afterTemp){
                if(temp1.equals(temp2.getOrderdetailId())){
                    if(before.get(temp1)==null && temp2.getRefundStatus()!=null){
                        if("approve".equals(temp2.getRefundStatus())){
                            CashRecordBean tempcash=new CashRecordBean();
                            tempcash.setUserId(tempOrder.getUserId());
                            tempcash.setPurchaseAmount(temp2.getHistoryPrice()*temp2.getQuantity());
                            tempcash.setCashRecordStatus("refund");
                            tempcash.setOrderdetailId(temp2.getOrderdetailId());
                            tempcash.setRefundDate(new Date(System.currentTimeMillis()));
                            tempApprove.add(tempcash);
                            totalApprove+=temp2.getHistoryPrice()*temp2.getQuantity();
                        }
                    } else if(before.get(temp1)!=null && temp2.getRefundStatus()!=null){
                        if(!temp2.getRefundStatus().equals(before.get(temp1))){
                            if("approve".equals(temp2.getRefundStatus())){
                                CashRecordBean tempcash=new CashRecordBean();
                                tempcash.setUserId(tempOrder.getUserId());
                                tempcash.setPurchaseAmount(temp2.getHistoryPrice()*temp2.getQuantity());
                                tempcash.setCashRecordStatus("refund");
                                tempcash.setOrderdetailId(temp2.getOrderdetailId());
                                tempcash.setRefundDate(new Date(System.currentTimeMillis()));
                                tempApprove.add(tempcash);
                                totalApprove+=temp2.getHistoryPrice()*temp2.getQuantity();
                                // 如果本來通過改成拒絕或待定
                            } else if("approve".equals(before.get(temp1))){
                                tempCancel.add(temp2.getOrderdetailId());
                                totalCancel+=temp2.getHistoryPrice()*temp2.getQuantity();
                            }
                        }
                    }
                }
            }
        }
        System.err.println("ap="+totalApprove+",cp="+totalCancel);
        if(!tempApprove.isEmpty()){
            resultCash=cashRecordRepository.saveAll(tempApprove);
        }
        if(!tempCancel.isEmpty()){
            cashRecordRepository.deleteAllByOrderdetailIdIn(tempCancel);
        }
        if(totalApprove!=0 || totalCancel!=0){
            totalFinal=totalApprove-totalCancel;
        }
        UserBean userBean=userRepository.findById(tempOrder.getUserId()).orElse(null);
        if(userBean!=null){
            if(userBean.getCash()==null){
                userBean.setCash(totalFinal);
            } else{
                userBean.setCash(userBean.getCash()+totalFinal);
            }
            userBean=userRepository.save(userBean);
            if(userBean!=null){
                return true;
            }
        }


        return false;
    }


    /**
     * 用來數出總訂單數
     *
     * @return long--總訂單數
     */

    public Long countOrder(){
        Long count=orderRepository.count();
        return count;
    }

    /**
     * 用userID數出該user的總訂單數
     *
     * @param userId--Integer
     * @return Long--該user的總訂單數
     */
    public Long countOrderByUserId(Integer userId){
        Long countByUserId=orderRepository.countByUserId(userId);
        return countByUserId;
    }

    /**
     * 用訂單編號找更改某個訂單的詳細資料
     *
     * @param orderId--Integer
     * @return List<OrderdetailsBean>--某個訂單的詳細資料
     */
    public List<OrderdetailsBean> findOrderdetailById(Integer orderId){
        List<OrderdetailsBean> orderdetails=orderdetailsRepository.findByOrderId(orderId);
        if(orderdetails!=null && !orderdetails.isEmpty()){
            return orderdetails;
        }
        return null;
    }


    //    舊方法
    public List<OrderOrderDTO> findAllOrder(Map<String, String> request){
        Pageable orderPageable=this.orderPageable(request);
        if(orderPageable==null){
            return null;
        }
        List<OrdersBean> all=orderRepository.findOrderWithLimit(orderPageable);
        if(all==null || all.isEmpty()){
            return null;
        }
        //每次取出會去更新繳費沒，沒繳費把orderStatus改成cancel;
        all=this.checkStatus(all);
        all=orderRepository.saveAll(all);
        if(all==null || all.isEmpty()){
            return null;
        }
        List<OrderOrderDTO> orderWithDetails=this.combineOrderAndDetail(all);
        if(orderWithDetails!=null && !orderWithDetails.isEmpty()){
            return orderWithDetails;
        }
        return null;
    }

    public Long countOrder(DashboardOrderFind request){
        List<Integer> orderIds=new ArrayList<>();
        if(request.getRefundStatus()!=null && request.getRefundStatus().length()!=0){
            orderIds=orderdetailsRepository.findOrderIdByRefundStatus(request.getRefundStatus());
        }
        Specification<OrdersBean> spec=buildSpecification(request,orderIds);
        System.err.println("spec="+spec);
        return orderRepository.count(spec);
    }


    public List<OrdersBean> findOrdersWithCriteria(DashboardOrderFind request,List<Integer> orderIds){
        Specification<OrdersBean> spec=buildSpecification(request,orderIds);
        Pageable pageable=this.orderPageable(request.getOrderPage());
        return orderRepository.findAll(spec,pageable);
    }

    private Specification<OrdersBean> buildSpecification(DashboardOrderFind dashboardOrderFind,List<Integer> orderIds){
        Specification<OrdersBean> spec=Specification.where(null);
        DashboardOrderFind request=new DashboardOrderFind();
        request=dashboardOrderFind;
        if(request.getUserId()!=null){
            spec=spec.and(OrderSpecifications.hasUserId(request.getUserId()));
        }

        if(request.getOrderId()!=null){
            spec=spec.and(OrderSpecifications.hasOrderId(request.getOrderId()));
        }
        if(orderIds!=null && !orderIds.isEmpty()){
            System.out.println("specid="+orderIds);
            spec=spec.and(OrderSpecifications.hasOrderIds(orderIds));
        }


        if(request.getOrderStatus()!=null){
            spec=spec.and(OrderSpecifications.hasOrderStatus(request.getOrderStatus()));
        }
        if(request.getPaymentStatus()!=null){
            System.err.println("ps");
            spec=spec.and(OrderSpecifications.hasPaymentStatus(request.getPaymentStatus()));
        }
        if(request.getPaymentMethod()!=null){
            System.err.println("pm");
            spec=spec.and(OrderSpecifications.hasPaymentMethod(request.getPaymentMethod()));
        }
        if(request.getOrderDateStart()!=null){
            spec=spec.and(OrderSpecifications.hasStartOrderDate(request.getOrderDateStart()));
        }

        if(request.getOrderDateEnd()!=null){
            spec=spec.and(OrderSpecifications.hasEndOrderDate(request.getOrderDateEnd()));
        }

        if(request.getTotalAmountMin()!=null){
            spec=spec.and(OrderSpecifications.hasTotalAmountMin(request.getTotalAmountMin()));
        }
        if(request.getTotalAmountMax()!=null){
            spec=spec.and(OrderSpecifications.hasTotalAmountMax(request.getTotalAmountMax()));
        }

        return spec;
    }


}
