package tw.eeit175groupone.finalproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import tw.eeit175groupone.finalproject.domain.ProductBean;
import tw.eeit175groupone.finalproject.domain.UserBean;
import tw.eeit175groupone.finalproject.service.CashService;

@RestController
public class RootnodeController {
    @Autowired
    private CashService cashService;

    @GetMapping("cash/rootnodeproduct")
    public ResponseEntity<?> findRootNode() {
        List<ProductBean> beans = cashService.findRootNodeProduct();
        if (beans != null && !beans.isEmpty()) {
            return ResponseEntity.ok().body(beans);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * @param session
     * @param cash
     * @param productId
     *                  添加金額紀錄到order、orderdetail、cashRecord
     *                  並更新user的cash
     * 
     */
    @GetMapping("cash/getcash/{cash}/{productId}/{discount}")
    public ResponseEntity<?> getcash(HttpSession session, @PathVariable Integer cash, @PathVariable Integer productId,
            @PathVariable Integer discount) {
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        UserBean user = (UserBean) session.getAttribute("user");
        if (user == null) {
            // 如果找不到名為 "user" 的屬性，嘗試獲取 "googleuser"
            user = (UserBean) session.getAttribute("googleuser");
        }
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        Integer userId = user.getId();
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }
        if (cash == null || cash <= 0 || productId == null || discount == null) {
            return ResponseEntity.badRequest().build();
        }
        cashService.insertCash(cash, productId, userId, discount);
        String ecpayCheckout = cashService.ecpayCheckoutRootnode(cash);
        return ResponseEntity.ok().body(ecpayCheckout);
    }
}
