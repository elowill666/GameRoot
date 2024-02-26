package tw.eeit175groupone.finalproject.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import tw.eeit175groupone.finalproject.domain.CouponBean;
import tw.eeit175groupone.finalproject.domain.UserBean;
import tw.eeit175groupone.finalproject.domain.WishlistBean;
import tw.eeit175groupone.finalproject.dto.ShoppingcartDTO;
import tw.eeit175groupone.finalproject.dto.WishlistDTO;
import tw.eeit175groupone.finalproject.service.WishlistService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;


@Controller
@CrossOrigin(origins = { "http://localhost:5173/", "http://127.0.0.1:5173/" }, allowedHeaders = {
    "*" }, allowCredentials = "true")
public class WishlistController {

@Autowired
private WishlistService wishlistService;




@GetMapping("/wishlist/findWishlistById")
    public ResponseEntity<?> findWishlist(HttpSession session) {
        if (session != null) {
            UserBean user = (UserBean) session.getAttribute("user");
            Integer userId = user.getId();
            if (userId != null) {
                List<WishlistDTO> array = wishlistService.findWishlist(userId);
                return ResponseEntity.ok().body(array);
            }
        }
        return ResponseEntity.notFound().build();
    }
    
@PostMapping("/wishlist/insert")
public ResponseEntity<?> insertWishlist(@RequestBody String json){
    JSONObject response= new JSONObject();
    if(wishlistService.insertWishlist(json)){
        // System.err.println("hihihihihihiii");
        response.put("scuccess", true);
        response.put("text", "成功加入願望清單");
        return ResponseEntity.ok().body(response.toString());    
    } else {
        response.put("scuccess",false);
        response.put("text", "已經在願望清單囉");
        return ResponseEntity.ok(response.toString());
    }
    
}
@DeleteMapping("/wishlist/delete")
public ResponseEntity<?> deleteWishlist(@RequestParam(name = "userId")Integer userId,@RequestParam(name = "productId")Integer productId){
    JSONObject response= new JSONObject();
    if(wishlistService.deleteWishlist(userId,productId)){
        // System.err.println("hihihihihihiii");
        response.put("scuccess", true);
        response.put("text", "刪除願望清單");
        return ResponseEntity.ok().body(response.toString());    
    } else {
        response.put("scuccess",false);
        response.put("text", "不想讓你移除商品");
        return ResponseEntity.ok(response.toString());
    }
    
}
@PostMapping("/wishlist/check")
public ResponseEntity<?> confirmByProductIdInUserWishlist(@RequestBody String body) {
    JSONObject response= new JSONObject();
    //TODO: process POST request
    if(wishlistService.confirmByProductIdInUserWishlist(body)){
        response.put("input", true);
        return ResponseEntity.ok().body(response.toString()); 
    } else {
        response.put("input", false);
        return ResponseEntity.ok().body(response.toString()); 
    }
}

    


}
