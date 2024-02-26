package tw.eeit175groupone.finalproject.service;

import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.eeit175groupone.finalproject.dao.*;
import tw.eeit175groupone.finalproject.domain.*;
import tw.eeit175groupone.finalproject.dto.*;


import java.util.*;

@Transactional(rollbackFor={Exception.class})
@Service
public class DashboardService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GameInforRepository gameInforRepository;
    @Autowired
    private MerchandiseRepository merchandiseRepository;


    /**
     * 用於找出全部商品提供前端管理者查看所有商品的資料
     */
    public Map<String,Object> findAllProduct(DashboardProduct request){
        Integer start=request.getStart();
        Integer rows=request.getRows();
        Map<String,Object> result=new HashMap<>();
        if(rows==null || start==null){
            return null;
        }
        //因為找尋資訊時遊戲商品跟周邊商品是分開找的所以順序會亂掉
        List<Object[]> gamesTemp=productRepository.findAllGameProduct();
        List<CompleteProductInfo> allProduct=new ArrayList<>();
        if(!gamesTemp.isEmpty() && gamesTemp!=null){
            for(Object[] temp : gamesTemp){
                CompleteProductInfo dto=new CompleteProductInfo();
                BeanUtils.copyProperties(temp[0],dto);
                if(temp[1]!=null){
                    BeanUtils.copyProperties(temp[1],dto);
                }
                dto.setInventoryQuantity(9999);
                allProduct.add(dto);
            }
        }
        List<Object[]> merchandiseTemp=productRepository.findAllMerchdiseProduct();
        if(merchandiseTemp!=null && !merchandiseTemp.isEmpty()){
            for(Object[] temp : merchandiseTemp){
                CompleteProductInfo mdto=new CompleteProductInfo();
                BeanUtils.copyProperties(temp[0],mdto);
                if(temp[1]!=null){
                    BeanUtils.copyProperties(temp[1],mdto);
                }
                mdto.setSpec(mdto.getSize()+"/"+mdto.getColor());
                allProduct.add(mdto);
            }
        }
        List<ProductBean> cashTemp=productRepository.findProductByCash();
        if(cashTemp!=null && !cashTemp.isEmpty()){
            for(ProductBean temp : cashTemp){
                CompleteProductInfo cdto=new CompleteProductInfo();
                BeanUtils.copyProperties(temp,cdto);
                cdto.setInventoryQuantity(9999);
                allProduct.add(cdto);
            }
        }
        if(allProduct.isEmpty() || allProduct==null){
            return null;
        }
        allProduct=this.productDynamicFind(allProduct,request);
        if(allProduct.isEmpty()){
            result.put("data","nodata");
            return result;
        }
        Integer sizeOfProduct=allProduct.size();
        
        //又因為是分開所以不知道到底不能直接用sql指令分頁，以愚蠢的方式取出大量的資料手動分頁

        allProduct=this.productDynamicSort(allProduct,request);
        List<CompleteProductInfo> productInfoList=new ArrayList<>();
        
        int totalrows=rows*start;
        if(totalrows>sizeOfProduct){
            totalrows=sizeOfProduct;
        }
        for(int i=(start-1)*rows; i<totalrows; i++){
            productInfoList.add(allProduct.get(i));
        }
        if(!productInfoList.isEmpty()){
            result.put("data",productInfoList);
            result.put("countOfData",sizeOfProduct);
            return result;
        }
        return null;
    } 
    
    public List<CompleteProductInfo> productDynamicSort(List<CompleteProductInfo> allProduct,DashboardProduct request){
        
        if("pricedesc".equals(request.getSort())){
            allProduct=allProduct.stream()
                    .sorted(Comparator.comparing(CompleteProductInfo::getPrice).reversed())
                    .toList();
        }else if("priceasc".equals(request.getSort())){
            allProduct=allProduct.stream()
                    .sorted(Comparator.comparing(CompleteProductInfo::getPrice))
                    .toList();
        }else if("discountdesc".equals(request.getSort())){
            allProduct=allProduct.stream()
                    .sorted(Comparator.comparing(CompleteProductInfo::getDiscount).reversed())
                    .toList();
        }
        else if("discountasc".equals(request.getSort())){
            allProduct=allProduct.stream()
                    .sorted(Comparator.comparing(CompleteProductInfo::getDiscount))
                    .toList();
        }else if("discountFactordesc".equals(request.getSort())){
            allProduct=allProduct.stream()
                    .sorted(Comparator.comparing(CompleteProductInfo::getDiscountFactor).reversed())
                    .toList();
        }else if("discountFactorasc".equals(request.getSort())){
            allProduct=allProduct.stream()
                    .sorted(Comparator.comparing(CompleteProductInfo::getDiscountFactor))
                    .toList();
        }else if("iddesc".equals(request.getSort())){
            allProduct=allProduct.stream()
                    .sorted(Comparator.comparing(CompleteProductInfo::getProductId).reversed())
                    .toList();
        }else {
            allProduct=allProduct.stream()
                    .sorted(Comparator.comparing(CompleteProductInfo::getProductId))
                    .toList();
        }
        return allProduct;
    }


    /**
     * 用stream 的filter實現動態找資料
     *
     * @param allProduct--List<CompleteProductInfo> 找出來的全部數據
     * @param request--DashboardProduct 搜索條件
     * @return List<CompleteProductInfo>篩選後的商品
     */
    public List<CompleteProductInfo> productDynamicFind(List<CompleteProductInfo> allProduct,DashboardProduct request){
        List<CompleteProductInfo> result=allProduct;
        
        if(request.getProductId()!=null){
            result=result.stream()
                    .filter(all -> request.getProductId().equals(all.getProductId()))
                    .toList();
        }
        if(request.getProductName()!=null && request.getProductName().length()!=0){
            result=result.stream()
                    .filter(all -> all.getProductName().toLowerCase().contains(request.getProductName().toLowerCase()))
                    .toList();
        }
        if(request.getMinDiscount()!=null){
            result=result.stream()
                    .filter(all -> all.getDiscount()!=null&&
                            request.getMinDiscount()<=(all.getDiscount()))
                    .toList();
        }
        if(request.getMaxDiscount()!=null){
            result=result.stream()
                    .filter(all -> all.getDiscount()!=null&&
                            request.getMaxDiscount()>=(all.getDiscount()))
                    .toList();
        }
        if(request.getMinDiscountFactor()!=null){
            result=result.stream()
                    .filter(all ->all.getDiscountFactor()!=null&&
                            request.getMinDiscountFactor()<=(all.getDiscountFactor()))
                    .toList();
        }
        if(request.getMaxDiscountFactor()!=null){
            result=result.stream()
                    .filter(all ->all.getDiscountFactor()!=null&&
                            request.getMaxDiscountFactor()>=(all.getDiscountFactor()))
                    .toList();
        }
        if(request.getDiscountStarttime()!=null){
            result=result.stream()
                    .filter(all -> (all.getDiscountStarttime()!=null&&request.getDiscountStarttime().getTime()<=all.getDiscountStarttime().getTime()))
                    .toList();
        }
        if(request.getDiscountEndtime()!=null){
            result=result.stream()
                    .filter(all -> (all.getDiscountEndtime()!=null&&request.getDiscountEndtime().getTime()>=all.getDiscountEndtime().getTime()))
                    .toList();
        }
        if(request.getSupplier()!=null){
            result=result.stream()
                    .filter(all -> (request.getSupplier().equals(all.getSupplier())))
                    .toList();
        }
        if(request.getProductStatus()!=null){
            result=result.stream()
                    .filter(all -> (request.getProductStatus().equals(all.getProductStatus())))
                    .toList();
        }
        if(request.getProductType()!=null){
            result=result.stream()
                    .filter(all -> (request.getProductType().equals(all.getProductType())))
                    .toList();
        }
        if(request.getProductSubtype()!=null){
            result=result.stream()
                    .filter(all -> (request.getProductSubtype().equals(all.getProductSubtype())))
                    .toList();
        }
        if(request.getMinPrice()!=null){
            result=result.stream()
                    .filter(all -> (request.getMinPrice()<=(all.getPrice())))
                    .toList();
        }
        if(request.getMaxPrice()!=null){
            result=result.stream()
                    .filter(all -> (request.getMaxPrice()>=(all.getPrice())))
                    .toList();
        }
        System.out.println("test="+result);
        return result;
    }

//    /**
//     * 用來將前端更改後的product資料傳到資料庫更新
//     *
//     * @return DashboardProduct
//     */
//    public DashboardProduct modifyProduct(DashboardProduct product){
//        //確認數據不為空及商品在資料庫已存在
//        if(product!=null && productRepository.existsById(product.getProductId())){
//            //從資料庫取出資料
//            Optional<ProductBean> temp=productRepository.findById(product.getProductId());
//            //確認有取出資料
//            if(temp!=null && temp.isPresent()){
//                ProductBean productIndb=temp.get();
//                //用前端的數據覆蓋原本的商品的資訊
//                BeanUtils.copyProperties(product,productIndb);
//                //將資料回傳資料庫更新商品數據
//                ProductBean productupdated=productRepository.save(productIndb);
//                //確認取回數據不為空
//                if(productupdated!=null){
//                    DashboardProduct modifyProduct=new DashboardProduct();
//                    BeanUtils.copyProperties(productupdated,modifyProduct);
//                    if(product.equals(modifyProduct)){
//                        return modifyProduct;
//                    }
//                }
//                //return更新後的商品數據
//            }
//        }
//        return null;
//    }

    /**
     * 用來將前端更改後的product資料傳到資料庫更新
     *
     * @param product--CompleteProductInfo
     * @return CompleteProductInfo
     */
    public CompleteProductInfo modifyProduct(CompleteProductInfo product){
        //確認數據不為空及商品在資料庫已存在
        if(product!=null && productRepository.existsById(product.getProductId())){
            ProductBean productBean=new ProductBean();
            GameInforBean gameInforBean=new GameInforBean();
            MerchandiseBean merchandiseBean=new MerchandiseBean();
            //確認是哪個類型的產品
            if("physical".equals(product.getProductType())){
                //如果是周邊用這裡
                if(merchandiseRepository.existsByProductId(product.getProductId())){
                    //取出原本的商品資訊
                    Optional<ProductBean> tempProduct=productRepository.findById(product.getProductId());
                    Optional<MerchandiseBean> tempMerchandise=merchandiseRepository.findById(product.getProductId());
                    if(tempProduct!=null && tempProduct.isPresent()
                            && tempMerchandise!=null && tempMerchandise.isPresent()){
                        productBean=tempProduct.get();
                        merchandiseBean=tempMerchandise.get();
                        //將傳入的商品資訊塞入原本的
                        BeanUtils.copyProperties(product,productBean);
                        BeanUtils.copyProperties(product,merchandiseBean);
                        //更新資訊
                        MerchandiseBean saveMerchandise=merchandiseRepository.save(merchandiseBean);
                        ProductBean saveProduct=productRepository.save(productBean);
                        this.modityDiscountProduct();
                        saveProduct=productRepository.findById(saveProduct.getProductId()).orElse(null);
                        if(saveProduct!=null && saveMerchandise!=null){
                            //塞入新的dto
                            CompleteProductInfo newProductInfo=new CompleteProductInfo();
                            BeanUtils.copyProperties(saveProduct,newProductInfo);
                            BeanUtils.copyProperties(saveMerchandise,newProductInfo);
                            if(newProductInfo!=null){
                                this.modityDiscountProduct();

                                return newProductInfo;
                            }
                        }
                    }
                }
            } else if("digitalGames".equals(product.getProductType())){
                //如果是遊戲用這裡
                if(!gameInforRepository.existsById(product.getProductId())){
                    return null;
                }
                //取出原本的商品資訊
                Optional<ProductBean> tempProduct=productRepository.findById(product.getProductId());

                Optional<GameInforBean> tempGameInfo=gameInforRepository.findById(
                        product.getProductId());
                if(tempProduct==null || !tempProduct.isPresent()
                        || tempGameInfo==null || !tempGameInfo.isPresent()){
                    return null;
                }
                productBean=tempProduct.get();
                gameInforBean=tempGameInfo.get();
                //將傳入的商品資訊塞入原本的
                BeanUtils.copyProperties(product,productBean);
                BeanUtils.copyProperties(product,gameInforBean);
                ProductBean saveProduct=productRepository.save(productBean);
                GameInforBean saveGame=gameInforRepository.save(gameInforBean);
                this.modityDiscountProduct();
                saveProduct=productRepository.findById(saveProduct.getProductId()).orElse(null);
                if(saveGame!=null && saveProduct!=null){
                    CompleteProductInfo newProductInfo=new CompleteProductInfo();
                    BeanUtils.copyProperties(saveProduct,newProductInfo);
                    BeanUtils.copyProperties(saveGame,newProductInfo);
                    if(newProductInfo!=null){
                        newProductInfo.setInventoryQuantity(9999);
                        return newProductInfo;
                    }
                }
            }
        } else if("cash".equals(product.getProductType())){
            Optional<ProductBean> tempProduct=productRepository.findById(product.getProductId());
            if(tempProduct.isPresent()){
                ProductBean cashBean=tempProduct.get();
                BeanUtils.copyProperties(product,cashBean);
                ProductBean saveProduct=productRepository.save(cashBean);
                this.modityDiscountProduct();
                saveProduct=productRepository.findById(saveProduct.getProductId()).orElse(null);
                if(saveProduct!=null){
                    CompleteProductInfo newProductInfo=new CompleteProductInfo();
                    BeanUtils.copyProperties(saveProduct,newProductInfo);
                    this.modityDiscountProduct();
                    newProductInfo.setInventoryQuantity(9999);
                    return newProductInfo;
                }
            }

        }
        //叫出對應產品的產品資訊
        //把資料塞進去
        //更新資料
        //return更新後的商品數據

        return null;
    }

    /**
     * 用來將前端更改後的product資料傳到資料庫更新
     * 此方法批次更新
     *
     * @param allProduct--List<CompleteProductInfo>
     * @return CompleteProductInfo
     */
    public List<CompleteProductInfo> modifyAllProduct(List<CompleteProductInfo> allProduct){
        List<CompleteProductInfo> results=new ArrayList<>();
        for(CompleteProductInfo temp : allProduct){
            CompleteProductInfo modify=this.modifyProduct(temp);
            if(modify!=null){
                results.add(modify);
            }
        }
        if(!results.isEmpty() && results!=null){
            return results;
        }
        return null;
    }

    /**
     * 用於更新商品狀態，
     * 將已進入折扣時間，但狀態不在折扣狀態的商品調整成折扣狀態(discount)，
     * 和已不在折扣時間，但狀態還在折扣狀態的商品調整成一般狀態(onsale)，
     * 和根本沒有設定折扣時間，但狀態在折扣狀態的商品調整成一般狀態(onsale)
     *
     * @return List<ProductBean>
     */
    public void modityDiscountProduct(){
        //將根本沒有折扣時間，但狀態是discount的商品，將狀態改為onsale
        List<ProductBean> producterror=productRepository.findErrorDiscount();
        if(producterror!=null && !producterror.isEmpty()){
            for(int i=0; i<producterror.size(); i++){
                producterror.get(i).setProductStatus("onsale");
            }
            productRepository.saveAll(producterror);
        }
        //將已不在折扣時間，但狀態還是discount的商品，將狀態改為onsale
        List<ProductBean> productout=productRepository.findProductOutoffDiscount();
        if(productout!=null && !productout.isEmpty()){
            for(int i=0; i<productout.size(); i++){
                productout.get(i).setProductStatus("onsale");
            }
            productRepository.saveAll(productout);
        }

        //將已進入折扣時間，但狀態尚未變成discount的上架商品，狀態改為disconut
        List<ProductBean> discountProduct=productRepository.findDiscountProduct();
        if(discountProduct!=null && !discountProduct.isEmpty()){
            for(int i=0; i<discountProduct.size(); i++){
                discountProduct.get(i).setProductStatus("discount");
            }
            productRepository.saveAll(discountProduct);
        }
//        //將已沒有設定進入折扣時間，但有終止時間的商品進行驗證，如狀態尚未變成discount的商品，狀態改為disconut
//        List<ProductBean> discountProductwithStartimeNull=productRepository.findDiscountProductAndEndtimeNotOver();
//        if(discountProductwithStartimeNull!=null && !discountProductwithStartimeNull.isEmpty()){
//            for(int i=0; i<discountProductwithStartimeNull.size(); i++){
//                discountProductwithStartimeNull.get(i).setProductStatus("discount");
//            }
//            productRepository.saveAll(discountProductwithStartimeNull);
//        }

    }

    /**
     * @param productid
     * @return 產品圖片的Byte陣列
     */

    public byte[] findCoverImageById(Integer productid){
        Optional<String> temp=productRepository.findCoverImageById(productid);
        if(temp!=null && temp.isPresent()){
            return temp.get().getBytes();
        }

        return null;
    }

    /**
     * 以多筆productId批次取出商品封面照片
     *
     * @param productIds--List<Integer>
     * @return Map<Integer,byte [ ]>--key為productId,value為byte陣列
     */
    public Map<Integer, String> findAllCoverImageByIdIn(List<Integer> productIds){
        List<Object[]> temp=productRepository.findAllCoverImageById(productIds);
        Map<Integer, String> result=new HashMap<>();
        if(temp!=null && !temp.isEmpty()){
            for(Object[] obj : temp){
                Integer productId=(Integer) obj[0];
                String image=((String) obj[1]);
                result.put(productId,image);
            }
            return result;
        }
        return null;
    }


    /**
     * 按需求從start找rowsNumber數量的資料
     *
     * @param start--Integer
     * @param rowsNumber--Integer
     * @return List--ProductBean
     */
    public List<ProductBean> findProductwithPageable(Integer start,Integer rowsNumber){
        List<ProductBean> all=productRepository.findWithLimitAndOffset(start,rowsNumber);
        if(all!=null && !all.isEmpty()){
            return all;
        }
        return null;
    }

    /**
     * 用JPA提供的方法count產品總數
     * return Integer--產品總數
     */
    public Integer countProduct(){
        Long temp=productRepository.count();
        if(temp!=null){
            Integer count=temp.intValue();
            return count;
        }
        return null;
    }


//    -------------------------找產品用結束---------------------------

//    -------------------------找使用者開始---------------------------


//-----------------------------找使用者結束開始---------------------------


//--------------------------------找產品開始--------------------------------


//--------------------------------找產品結束--------------------------------


//--------------------------------找文章起始--------------------------------


//--------------------------------找文章結束--------------------------------

//-------------------------------找數據開始Data---------------------------

    public List<String> findProductType(){
        List<String> productType=productRepository.findProductType();
        if(productType!=null && !productType.isEmpty()){
            return productType;
        }
        return null;
    }

    public List<String> findSubtype(JSONObject json){
        String productType=json.isNull("productType") ? null: json.getString("productType");
        List<String> subtype=null;
        if("all".equals(productType)){
            subtype=productRepository.findAllSubtype();
        }else{
            subtype=productRepository.findSubtype(productType);
        }
        if(subtype!=null && !subtype.isEmpty()){
            return subtype;
        }
        return null;
    }

    public List<String> findProductName(JSONObject json){
        String productSubtype=json.isNull("productSubtype") ? null: json.getString("productSubtype");
        List<String> productName=productRepository.findProductName(productSubtype);
        if(productName!=null && !productName.isEmpty()){
            return productName;
        }
        return null;
    }


}
    
