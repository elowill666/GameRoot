package tw.eeit175groupone.finalproject.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.eeit175groupone.finalproject.domain.OrdersBean;
import tw.eeit175groupone.finalproject.domain.ProductBean;

@Repository
public interface ProductRepository extends JpaRepository<ProductBean, Integer>, ProductDAO {
        @Query(value = "SELECT productId,productName,price FROM ProductBean")
        List<Object[]> selectIdNamePrice();

        /**
         * 用於找尋已進入折扣時間，
         * 但狀態不是discount的上架商品
         *
         * @return A ProductBean List
         */
        @Query(value = "select*from product\r\n"
                        + "where discount_starttime<=GETDATE()\r\n"
                        + "and discount_endtime>=GETDATE()\r\n"
                        + "and product_status='onsale'", nativeQuery = true)
        List<ProductBean> findDiscountProduct();

        /**
         * 用於找尋不在折扣時間，
         * 但狀態為discount的商品
         *
         * @return A ProductBean List
         */
        @Query(value = "SELECT*FROM product\r\n"
                        + "WHERE discount_starttime>GETDATE()\r\n"
                        + "or discount_endtime<GETDATE() and product_status='discount'", nativeQuery = true)
        List<ProductBean> findProductOutoffDiscount();

        /**
         * 用於找尋沒有折扣時間，
         * 但狀態為discount的商品
         *
         * @return A ProductBean List
         */
        @Query(value = "select*from product\r\n"
                        + "where discount_endtime is null\r\n"
                        + "and product_status='discount'", nativeQuery = true)
        List<ProductBean> findErrorDiscount();

        /**
         * 找出不設起始時間，但有終止時間的商品
         *
         * @return A Object[ ] List
         */
        @Query(value = "select p from ProductBean p\r\n"
                        + "where p.discountEndtime>=current_date\r\n"
                        + "and p.productStatus='onsale'")
        List<ProductBean> findDiscountProductAndEndtimeNotOver();

        /**
         * 找出全部遊戲商品的完整產品資訊
         *
         * @return A Object[ ] List
         */
        @Query(value = "select p,g from ProductBean p\r\n"
                        + "LEFT JOIN GameInforBean g on p.productId=g.gameInforId\r\n" +
                        "WHERE productType='digitalGames'")
        List<Object[]> findAllGameProduct();

        /**
         * 找出全部周邊商品的完整產品資訊
         *
         * @return A Object[ ] List
         */
        @Query(value = "select p,m from ProductBean p\r\n"
                        + "LEFT JOIN MerchandiseBean m on p.productId=m.productId\r\n" +
                        "WHERE productType='physical'")
        List<Object[]> findAllMerchdiseProduct();

        // @Query(value = "select p.coverImage from ProductBean p\r\n"
        // +"WHERE p.productId=:id")
        // Optional<String> findCoverImageByIdByJPA(@Param("id") Integer productId);

        /**
         * 輸入產品id回傳此產品的封面照片
         *
         * @param productId--Integer
         * @return String--回傳接收到的產品id的封面照片
         */
        @Query(value = "select p.cover_image from product p\r\n"
                        + "WHERE p.product_id=:id", nativeQuery = true)
        Optional<String> findCoverImageById(@Param("id") Integer productId);

        @Query(value = "select product_id,p.cover_image from product p\r\n"
                        + "WHERE p.product_id In :ids", nativeQuery = true)
        List<Object[]> findAllCoverImageById(@Param("ids") List<Integer> productId);

        /**
         * 找出上架中或特價中的產品
         *
         * @return 上架中或特價中的產品
         */
        @Query("select p from ProductBean p Where p.productStatus in ('discount','onsale')")
        List<ProductBean> findAllWithoutNotSale();

        /**
         * 找出遊戲或周邊的產品
         *
         * @param productType--String
         * @return 找出遊戲或周邊的產品
         */
        @Query("select p from ProductBean p Where p.productType = :productType and p.productStatus in ('discount','onsale')")
        List<ProductBean> findAllByProductType(@Param("productType") String productType);

        /**
         * 找出遊戲或周邊子類別的產品
         *
         * @param productSubtype--String
         * @return 找出遊戲或周邊子類別的產品
         */
        @Query("select p from ProductBean p Where p.productSubtype = :productSubtype and p.productStatus in ('discount','onsale')")
        List<ProductBean> findAllByProductSubType(@Param("productSubtype") String productSubtype);

        /**
         * coverImage被transient掉所以另外寫native SQL塞入
         */
        @Modifying
        @Query(value = "update product set  cover_image = :coverImage WHERE product_id=:id", nativeQuery = true)
        void updateCoverImage(@Param("id") Integer productId, @Param("coverImage") String coverImage);

        /**
         * 找出最新插入的商品編號
         * 
         * @return
         */
        @Query("select max(p.productId) from ProductBean p")
        Integer lastProductId();

        /**
         * 找出儲值金商品
         *
         * @param cash
         * @return List<ProductBean>
         */
        List<ProductBean> findByProductType(String cash);

        /**
         * 用產品子類別找尋產品名字
         *
         * @param productSubtype--String 產品類別
         * @return productName--List<String> 子產品類別
         */
        @Query("SELECT productName\r\n" +
                        "from ProductBean\r\n" +
                        "WHERE productSubtype=:type\r\n" +
                        "GROUP BY productName")
        List<String> findProductName(@Param("type") String productSubtype);

        /**
         * 用產品類別找尋產品子類別
         *
         * @param productype--String 產品類別
         * @return productSubtype--List<String> 子產品類別
         */
        @Query("SELECT productSubtype\r\n" +
                        "from ProductBean\r\n" +
                        "WHERE productType=:type\r\n" +
                        "GROUP BY productSubtype")
        List<String> findSubtype(@Param("type") String productype);

        @Query("SELECT productSubtype\r\n" +
                        "from ProductBean\r\n" +
                        "GROUP BY productSubtype")
        List<String> findAllSubtype();

        /**
         * 直接group找尋全部商品的productType
         *
         * @return productType--List<String>
         */
        @Query("SELECT productType\r\n" +
                        "FROM ProductBean o\r\n" +
                        "GROUP BY productType")
        List<String> findProductType();

        /**
         * 找出全部類別為cash的商品
         *
         * @return ProductBean--List<>
         */
        @Query("SELECT p From ProductBean p WHERE productType='cash'")
        List<ProductBean> findProductByCash();

        /**
         * 輸入數字取出相應的資料筆數
         *
         * @param topNumber--Integer
         * @return ProductBean--List<String>
         */
        @Query("SELECT p\r\n" +
                        "FROM ProductBean p\r\n" +
                        "Order By productId\r\n" +
                        "Limit :number")
        List<ProductBean> findTopProduct(@Param("number") Integer topNumber);

        /**
         * 輸入數字取出相應的資料筆數(nativeQuery)
         *
         * @param topNumber--Integer
         * @return ProductBean--List<String>
         */
        @Query(value = "SELECT TOP(:number)* FROM product\r\n" +
                        "Order By product_id", nativeQuery = true)
        List<ProductBean> findTopProductwithNativeQuery(@Param("number") Integer topNumber);

        /**
         * 輸入想取出資料的起始數字及想取出的資料筆數(x)
         * 得到從輸入的起始數字開始往後算X筆的資料筆數
         * (nativeQuery)
         *
         * @param startNumber--Integer 起始資料的數字
         * @param rowsNumber--Integer  想抓到的資料數量
         * @return ProductBean--List<String>
         */
        @Query(value = "SELECT * FROM product\r\n" +
                        "ORDER BY product_id\r\n" +
                        "OFFSET :number ROWS\r\n" +
                        "FETCH FIRST :rowsNumber ROWS ONLY", nativeQuery = true)
        List<ProductBean> findProductwithPageable(@Param("number") Integer startNumber,
                        @Param("rowsNumber") Integer rowsNumber);

        /**
         * 輸入想取出資料的起始數字及想取出的資料筆數(x)
         * 得到從輸入的起始數字開始往後算X筆的資料筆數
         * 順帶一提JPA的速度比nativequery更快
         *
         * @param startNumber--Integer 起始資料的數字
         * @param rowsNumber--Integer  想抓到的資料數量
         * @return ProductBean--List<String>
         * 
         */
        @Query(value = "SELECT p FROM ProductBean p\r\n" +
                        "ORDER BY productId\r\n" +
                        "LIMIT :rowsNumber\r\n" +
                        "OFFSET :number")
        List<ProductBean> findWithLimitAndOffset(@Param("number") Integer startNumber,
                        @Param("rowsNumber") Integer rowsNumber);

        /**
         * JPA提供的神奇方法直接count
         * 
         * @return 統計後的值--Long
         */
        @Override
        long count();

        // 查詢月銷量
        @Query("SELECT COUNT(od) FROM OrderdetailsBean od JOIN OrdersBean o ON o.orderId = od.orderId WHERE MONTH(o.orderDate) = :month  AND YEAR(o.orderDate) = :year AND od.productId = :productId GROUP BY od.productId")
        Long MonthlySalesOfProduct(@Param("month") Integer month, @Param("year") Integer year,
                        @Param("productId") Integer productId);

}
