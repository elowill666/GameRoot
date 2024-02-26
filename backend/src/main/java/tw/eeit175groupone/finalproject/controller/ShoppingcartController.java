package tw.eeit175groupone.finalproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import tw.eeit175groupone.finalproject.domain.UserBean;
import tw.eeit175groupone.finalproject.dto.ShoppingcartDTO;
import tw.eeit175groupone.finalproject.dto.ShoppingcartPaydata;
import tw.eeit175groupone.finalproject.service.ShoppingcartService;

@RestController
@RequestMapping("/shoppingcart")
public class ShoppingcartController {
    @Autowired
    private ShoppingcartService shoppingcartService;

    @GetMapping("/countshoppingcart")
    public ResponseEntity<?> countShoppingcart(HttpSession session) {
        if (session == null) {
            return ResponseEntity.ok(false);
        }
        UserBean user = (UserBean) session.getAttribute("user");
        if (user == null) {
            // 如果找不到名為 "user" 的屬性，嘗試獲取 "googleuser"
            user = (UserBean) session.getAttribute("googleuser");
        }
        if (user == null) {
            return ResponseEntity.ok(false);
        }
        Integer userId = user.getId();
        if (userId == null) {
            return ResponseEntity.ok(false);
        }
        Integer countShoppingcart = shoppingcartService.countShoppingcart(userId);
        if (countShoppingcart == 0) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);
    }

    @GetMapping("/paydata")
    public ResponseEntity<?> getMethodName(HttpSession session) {
        Integer userId = this.findUserId(session);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }
        ShoppingcartPaydata paydata = shoppingcartService.findPayData(userId);
        return ResponseEntity.ok().body(paydata);
    }

    @GetMapping("lastprice/{totalprice}/{couponId}")
    public ResponseEntity<?> allTotalPrice(@PathVariable Integer totalprice, @PathVariable Integer couponId,
            HttpSession session) {
        Integer userId = this.findUserId(session);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }
        if (totalprice < 0 || totalprice == null || couponId == null) {
            return ResponseEntity.notFound().build();
        }
        shoppingcartService.updateallTotalPrice(totalprice, userId, couponId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/removecart/{cartmiddleId}")
    public ResponseEntity<?> remove(@PathVariable Integer cartmiddleId, HttpSession session) {
        Integer userId = this.findUserId(session);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }
        if (cartmiddleId == null) {
            return ResponseEntity.notFound().build();
        }
        shoppingcartService.remove(cartmiddleId);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/addtocart/{productId}")
    public ResponseEntity<?> addToCart(@PathVariable Integer productId, HttpSession session) {
        Integer userId = this.findUserId(session);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }
        if (productId == null) {
            return ResponseEntity.notFound().build();
        }
        shoppingcartService.addToCart(productId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/findcart")
    public ResponseEntity<?> findCart(HttpSession session) {
        Integer userId = this.findUserId(session);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }
        List<ShoppingcartDTO> array = shoppingcartService.findcart(userId);
        return ResponseEntity.ok().body(array);
    }

    @PutMapping("increase/{cartmiddleId}")
    public ResponseEntity<?> increaseQuantity(@PathVariable Integer cartmiddleId) {
        if (cartmiddleId != null) {
            shoppingcartService.increaseQuantity(cartmiddleId);
            return ResponseEntity.ok("Quantity increased successfully.");
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("decrease/{cartmiddleId}")
    public ResponseEntity<?> decreaseQuantity(@PathVariable Integer cartmiddleId) {
        if (cartmiddleId != null) {
            shoppingcartService.decreaseQuantity(cartmiddleId);
            return ResponseEntity.ok("Quantity decreased successfully.");
        }

        return ResponseEntity.notFound().build();
    }

    public Integer findUserId(HttpSession session) {
        if (session == null) {
            return null;
        }
        UserBean user = (UserBean) session.getAttribute("user");
        if (user == null) {
            // 如果找不到名為 "user" 的屬性，嘗試獲取 "googleuser"
            user = (UserBean) session.getAttribute("googleuser");
        }
        if (user == null) {
            return null;
        }
        Integer userId = user.getId();
        if (userId == null) {
            return null;
        }
        return userId;
    }
}
