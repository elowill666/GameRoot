package tw.eeit175groupone.finalproject.controller;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tw.eeit175groupone.finalproject.dao.CommentsRepository;
import tw.eeit175groupone.finalproject.dao.ProductRepository;

import tw.eeit175groupone.finalproject.domain.CommentsBean;
import tw.eeit175groupone.finalproject.domain.OrdersBean;

import tw.eeit175groupone.finalproject.dto.*;

import tw.eeit175groupone.finalproject.service.DashboardService;


import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/dashboard")

public class DashboardController{
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private ProductRepository productRepository;


//    -------------------------產品用請求起始---------------------------    

    //    接收請求後丟出某個商品封面照片給前端管理者
    @PostMapping("/productimage/{productId}")
    public ResponseEntity<?> findAllProduct(@PathVariable Integer productId){
        byte[] productImage=dashboardService.findCoverImageById(productId);
        if(productImage!=null){
            return ResponseEntity.ok().body(productImage);
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping("/productimage/find")
    public ResponseEntity<?> findAllProductImage(@RequestBody List<Integer> productIds){
        if(!productIds.isEmpty()&&productIds!=null){
            Map<Integer, String> allCoverImageByIdIn=dashboardService.findAllCoverImageByIdIn(productIds);
            if(allCoverImageByIdIn!=null){
                return ResponseEntity.ok().body(allCoverImageByIdIn);
            }
       
        }
        return ResponseEntity.notFound().build();
    }
    

    //更新商品狀態用
    @GetMapping("/product/update")
    public ResponseEntity<?> modityDiscountProduct(){
        dashboardService.modityDiscountProduct();
        return ResponseEntity.ok().body("success");
    }


    //接收請求後丟出全部的商品資訊給前端管理者
    @PostMapping("/product/find")
    public ResponseEntity<?> findProduct(@RequestBody DashboardProduct request){
        System.err.println("pfind="+request);
    
        if(request!=null){
            Map<String,Object> allProduct=dashboardService.findAllProduct(request);
            if(allProduct!=null ){
                if(allProduct.isEmpty()){
                    return ResponseEntity.ok().body("nodata");
                }
                return ResponseEntity.ok().body(allProduct);
            }
        }
        return ResponseEntity.notFound().build();

    }

    //1.接收請求，並接收資料
    //2.更改商品資料
    @PutMapping("/product")
    public ResponseEntity<?> modifyProduct(@RequestBody CompleteProductInfo requset){
        System.out.println("in");
        if(requset!=null){
            CompleteProductInfo newProductInfo=dashboardService.modifyProduct(requset);
            if(newProductInfo!=null){
                return ResponseEntity.ok().body(newProductInfo);
            }
        }
        return ResponseEntity.notFound().build();
    }

    //1.接收請求，並接收資料
    //2.批次更改商品資料
    @PutMapping("/product/all")
    public ResponseEntity<?> modifyAllProduct(@RequestBody List<CompleteProductInfo> request){
        if(request!=null&&!request.isEmpty()){
            List<CompleteProductInfo> results=dashboardService.modifyAllProduct(request);
            return ResponseEntity.ok().body(results);
            }
        
        return ResponseEntity.notFound().build();
    }
    
    

    @GetMapping("/product/count")
    public ResponseEntity<?> countProduct(){
        Integer countProduct=dashboardService.countProduct();

        if(countProduct!=null){
                return ResponseEntity.ok().body(countProduct);
            }
        
        return ResponseEntity.notFound().build();
    }
    
    

    //1.接收請求，並接收資料
    //2.更改商品資料
//    @PutMapping("/product")
//    public ResponseEntity<?> modifyProduct(@RequestBody DashboardProduct requset){
//        System.out.println(123);
//        if(requset!=null){
//            System.out.println(456);
//            DashboardProduct updatedproduct=dashboardService.modifyProduct(requset);
//            if(updatedproduct!=null){
//                return ResponseEntity.ok().body(requset);
//            }
//        }
//        return ResponseEntity.noContent().build();
//    }
//    
    //接收到請求回傳商品類別
    @GetMapping("/product/producttype")
    public ResponseEntity<?> findProductType(){
        List<String> productType=dashboardService.findProductType();
        if(productType!=null && !productType.isEmpty()){
            return ResponseEntity.ok().body(productType);
        }
        return null;
    }
    //接收到請求回傳商品子類別
    @PostMapping("/product/subtype")
    public ResponseEntity<?> findProductType(@RequestBody String productType){
        System.out.println(productType);
        if(productType!=null){
            JSONObject json=new JSONObject(productType);

            List<String> subtype=dashboardService.findSubtype(json);
            if(subtype!=null && !subtype.isEmpty()){
                System.out.println(subtype);
                return ResponseEntity.ok().body(subtype);
            }
        }
        return null;
    }
    //接收到請求回傳商品名稱
    @PostMapping("/product/productname")
    public ResponseEntity<?> findProductName(@RequestBody String productSubtype){
        System.out.println(productSubtype);
        if(productSubtype!=null){
            JSONObject json=new JSONObject(productSubtype);

            List<String> productName=dashboardService.findProductName(json);
            if(productName!=null && !productName.isEmpty()){
                System.out.println(productName);
                return ResponseEntity.ok().body(productName);
            }
        }
        return null;
    }
    
    
    
//    -------------------------產品用請求結束--------------------------- 

//    -------------------------使用者用請求開起始--------------------------- 

  

//    -------------------------使用者用請求開結束---------------------------      

//    -------------------------訂單用請求開始---------------------------  



//    -------------------------訂單用請求開結束---------------------------      

//    -------------------------文章用請求開始---------------------------  

   



//    -------------------------文章用請求結束---------------------------  

//    -------------------------數據用請求結束DATA---------------------------  



    @PostMapping("/photo/download")
    public ResponseEntity<?> findAllData(@RequestParam("file") MultipartFile file){
        if(file!=null){
            try{
                //直接回傳byte[]掛掉?為啥
//                return ResponseEntity.ok().body(file.getBytes());

                String img="data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(file.getBytes());
                //要麻轉base64回傳
//                return ResponseEntity.ok().body(img);

                //要麻轉baes64再轉byte[]傳
                byte[] bytes=img.getBytes();
                return ResponseEntity.ok().body(bytes);


            } catch(IOException e){
                throw new RuntimeException(e);
            }
        }
        return ResponseEntity.notFound().build();
    }
    
    
    
    
}
