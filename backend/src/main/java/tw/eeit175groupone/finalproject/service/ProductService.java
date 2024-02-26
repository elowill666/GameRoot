package tw.eeit175groupone.finalproject.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.annotation.MultipartConfig;
import tw.eeit175groupone.finalproject.dao.GameInforRepository;
import tw.eeit175groupone.finalproject.dao.MerchandiseRepository;
import tw.eeit175groupone.finalproject.dao.ProductArticlesRepository;
import tw.eeit175groupone.finalproject.dao.ProductCommentRepository;
import tw.eeit175groupone.finalproject.dao.ProductImageRepository;
import tw.eeit175groupone.finalproject.dao.ProductRepository;
import tw.eeit175groupone.finalproject.domain.GameInforBean;
import tw.eeit175groupone.finalproject.domain.MerchandiseBean;
import tw.eeit175groupone.finalproject.domain.ProductBean;
import tw.eeit175groupone.finalproject.domain.ProductImageBean;
import tw.eeit175groupone.finalproject.dto.ProductDTO;
import tw.eeit175groupone.finalproject.dto.ProductDetailDTO;
import tw.eeit175groupone.finalproject.util.DatetimeConverter;

@Service
@Transactional
@MultipartConfig
public class ProductService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private MerchandiseRepository merchandiseRepository;

	@Autowired
	private ProductImageRepository productImageRepository;

	@Autowired
	private ProductCommentRepository productCommentRepository;

	@Autowired
	private GameInforRepository gameInforRepository;

	@Autowired
	private ProductArticlesRepository productArticlesRepository;

	/**
	 * 找出全部商品
	 * 
	 * @return 全部商品
	 */
	public List<ProductBean> findAllWithoutNotSale() {
		return productRepository.findAllWithoutNotSale();
	}

	/**
	 * 有條件找出全部商品總數
	 * 
	 * @return 條件內全部商品總數
	 */
	public long countProduct(String json) {
		try {

			JSONObject obj = new JSONObject(json);
			return productRepository.countProduct(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 有條件找出全部商品
	 * 
	 * @return 條件內全部商品
	 */
	public List<ProductBean> findProduct(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return productRepository.findProduct(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 產品Detail頁面(product+product_image+product_comment(留言評分)+merchandise(周邊資訊)+gameinfor(遊戲規格)+product_articles(產品文章))
	 */
	public ProductDetailDTO findProductDetail(Integer productId) {
		ProductDetailDTO productDetailList = new ProductDetailDTO();

		try {

			if (productId != null && productRepository.existsById(productId)) {

				Optional<ProductBean> optional = productRepository.findById(productId);
				if (optional.isPresent()) {
					ProductBean bean = optional.get();
					productDetailList.setProduct(bean);
				}

				List<ProductImageBean> descriptionImages = productImageRepository
						.findDescriptionImagesByProductId(productId);
				productDetailList.setDescriptionImages(descriptionImages);
				List<ProductImageBean> productImages = productImageRepository.findProductImagesByProductId(productId);
				productDetailList.setProductImages(productImages);

				List<Object[]> comments = productCommentRepository.findProductCommentByProductId(productId);
				productDetailList.setProductComments(comments);

				List<MerchandiseBean> merchandises = merchandiseRepository.findMerchandiseByProductId(productId);
				productDetailList.setMerchandises(merchandises);

				List<GameInforBean> gameInfor = gameInforRepository.findGameInforByProductId(productId);
				productDetailList.setGameInfor(gameInfor);

				List<Object[]> articles = productArticlesRepository.findProductArticlesByProductId(productId);
				productDetailList.setProductArticles(articles);

				if (productDetailList != null) {
					return productDetailList;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 找出遊戲或周邊的產品
	 * 
	 * @param productType
	 * @return 找出遊戲或周邊的產品
	 */
	public List<ProductBean> findAllByProductType(String productType) {
		return productRepository.findAllByProductType(productType);
	}

	/**
	 * 找出遊戲或周邊子類別的產品
	 * 
	 * @param productType
	 * @return 找出遊戲或周邊子類別的產品
	 */
	public List<ProductBean> findAllByProductSubType(String productSubType) {
		return productRepository.findAllByProductSubType(productSubType);
	}

	public ProductBean findByProductId(Integer productId) {
		Optional<ProductBean> opt = productRepository.findById(productId);
		if (opt.isPresent()) {
			return opt.get();
		}
		return null;
	}

	/**
	 * 創建產品
	 */
	public boolean createProduct(String Json) {
		JSONObject obj = new JSONObject(Json);
		// Table product
		// System.err.println("pALL = "+obj);
		try {

			String productName = obj.isNull("productName") ? "DefaultProductName" : obj.getString("productName");
			Integer price = obj.isNull("price") ? null : obj.getInt("price");
			String outlined = obj.isNull("outlined") ? null : obj.getString("outlined");
			String description = obj.isNull("description") ? null : obj.getString("description");
			String spec = obj.isNull("spec") ? null : obj.getString("spec");
			String supplier = obj.isNull("supplier") ? null : obj.getString("supplier");
			// String releaseDate =
			// obj.isNull("releaseDate")?null:obj.getString("releaseDate");
			String productStatus = obj.isNull("productStatus") ? null : obj.getString("productStatus");
			String productType = obj.isNull("productType") ? null : obj.getString("productType");
			String productSubtype = obj.isNull("productSubtype") ? null : obj.getString("productSubtype");
			// coverImage要轉成base64
			String coverImage = obj.isNull("coverImage") ? null : obj.getString("coverImage");

			ProductBean pbean = new ProductBean();
			pbean.setProductName(productName);
			pbean.setPrice(price);
			pbean.setOutlined(outlined);
			pbean.setDescription(description);
			pbean.setSupplier(supplier);
			// pbean.setReleaseDate(DatetimeConverter.parse(releaseDate, "yyyy-mm-dd"));
			pbean.setReleaseDate(new Date());
			pbean.setSpec(spec);
			pbean.setProductStatus(productStatus);
			pbean.setProductType(productType);
			pbean.setProductSubtype(productSubtype);
			// pbean.setCoverImage(coverImage);
			productRepository.save(pbean);
			Integer pbeanId = pbean.getProductId();
			// System.err.println("pId = "+pbeanId);
			// 另外塞入coverImage
			productRepository.updateCoverImage(pbeanId, coverImage);

			// Table merchanise
			// Integer merchandiseId =
			// obj.isNull("merchandiseId")?null:obj.getInt("merchandiseId");
			// Integer productId = obj.isNull("productId")?null:obj.getInt("merchandiseId");
			Integer inventoryQuantity = obj.isNull("inventoryQuantity") ? null : obj.getInt("inventoryQuantity");
			String gameName = obj.isNull("gameName") ? null : obj.getString("gameName");
			String color = obj.isNull("color") ? null : obj.getString("color");
			String size = obj.isNull("size") ? null : obj.getString("size");
			MerchandiseBean mBean = new MerchandiseBean();
			// mBean.setProductId(productId);
			mBean.setInventoryQuantity(inventoryQuantity);
			mBean.setGameName(gameName);
			mBean.setColor(color);
			mBean.setSize(size);

			// 隨便找出一個值來測
			if (gameName != null && gameName.length() != 0) {
				// 找出最新上傳的商品id
				mBean.setMerchandiseId(pbeanId);
				mBean.setProductId(pbeanId);
				merchandiseRepository.save(mBean);
				return true;
			}

			// Table gameInfor
			// Integer gameInforId =
			// obj.isNull("gameInforId")?null:obj.getInt("gameInforId");
			Double rating = obj.isNull("rating") ? null : obj.getDouble("rating");
			String os = obj.isNull("os") ? null : obj.getString("os");
			String processor = obj.isNull("processor") ? null : obj.getString("processor");
			String memory = obj.isNull("memory") ? null : obj.getString("memory");
			String graphics = obj.isNull("graphics") ? null : obj.getString("graphics");
			String storage = obj.isNull("storage") ? null : obj.getString("storage");
			GameInforBean gBean = new GameInforBean();
			gBean.setRating(rating);
			gBean.setOs(os);
			gBean.setMemory(memory);
			gBean.setGraphics(graphics);
			gBean.setStorage(storage);
			gBean.setProcessor(processor);
			if (os != null && os.length() != 0) {
				gBean.setGameInforId(pbeanId);
				gameInforRepository.save(gBean);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 找出最新產品編號
	public Integer findLastProductId() {
		return productRepository.lastProductId();
	}

	// 修改產品細項
	public boolean updateProductDTO(ProductDTO productDTO) {

		ProductBean productBean = productDTO.getProduct();
		productRepository.save(productBean);
		if (productBean.getCoverImage().length() != 0 || !productBean.getCoverImage().isEmpty()) {
			productRepository.updateCoverImage(productBean.getProductId(), productBean.getCoverImage());
		}
		List<ProductImageBean> productImageBeans = productDTO.getProductImages();
		if (productImageBeans.size() != 0 && !productImageBeans.isEmpty()) {
			// 先刪後新增
			productImageRepository.deleteProductImageByProductId(productBean.getProductId());
			Iterator<ProductImageBean> iterator = productImageBeans.iterator();

			while (iterator.hasNext()) {
				ProductImageBean next = iterator.next();
				next.setProductId(productBean.getProductId());
				if (next.getProductImage().length() != 0 && !next.getProductImage().isEmpty()) {
					next.setImageType("productimg");
					productImageRepository.save(next);
				}
			}
		}
		if (("physical").equals(productBean.getProductType())) {
			MerchandiseBean merchandiseBean = productDTO.getMerchandise();
			if (merchandiseBean != null && !("").equals(merchandiseBean.getGameName())) {
				gameInforRepository.deleteById(productBean.getProductId());
				merchandiseBean.setMerchandiseId(productBean.getProductId());
				merchandiseBean.setProductId(productBean.getProductId());
				merchandiseRepository.save(merchandiseBean);
				return true;
			}
		} else if (("digitalGames").equals(productBean.getProductType())) {
			GameInforBean gameInforBean = productDTO.getGameInfor();
			if (gameInforBean != null && !("").equals(gameInforBean.getRating())) {
				merchandiseRepository.deleteById(productBean.getProductId());
				gameInforBean.setGameInforId(productBean.getProductId());
				gameInforRepository.save(gameInforBean);
				return true;
			}
		}

		return false;
	}

	// 提供給產品更新時的所有資料
	public ProductDTO findProductDTO(Integer productId) {
		ProductDTO productDTO = new ProductDTO();

		if (productId != null && productRepository.existsById(productId)) {
			Optional<ProductBean> optional = productRepository.findById(productId);
			if (optional.isPresent()) {
				ProductBean bean = optional.get();
				bean.setCoverImage(productRepository.findCoverImageById(productId).get());
				productDTO.setProduct(bean);
			}
			List<ProductImageBean> productImages = productImageRepository.findProductImagesByProductId(productId);
			productDTO.setProductImages(productImages);

		}

		if (productId != null && gameInforRepository.existsById(productId)) {
			Optional<GameInforBean> optGameInfor = gameInforRepository.findById(productId);
			if (optGameInfor.isPresent()) {
				GameInforBean bean = optGameInfor.get();
				productDTO.setGameInfor(bean);
			}
			return productDTO;
		}
		if (productId != null && merchandiseRepository.existsById(productId)) {
			Optional<MerchandiseBean> optMerchandise = merchandiseRepository.findById(productId);
			if (optMerchandise.isPresent()) {
				MerchandiseBean bean = optMerchandise.get();
				productDTO.setMerchandise(bean);
			}
			return productDTO;
		}
		if (productDTO.getProduct() != null || productDTO.getMerchandise() != null
				|| productDTO.getGameInfor() != null) {
			return productDTO;
		}

		return null;
	}

	// 回傳月銷量產品排名
	/**
	 * @param body
	 * @return
	 */
	public List<Object[]> MonthlySalesOfProduct() {

		try {

			List<Object[]> beans = new ArrayList<>();
			List<ProductBean> products = this.findAllWithoutNotSale();
			// 獲取當前日期
			Calendar instance = Calendar.getInstance();
			// 取的當前上個月分
			Integer year = instance.get(Calendar.MONTH) == 0 ? instance.get(Calendar.YEAR) - 1
					: instance.get(Calendar.YEAR);
			Integer month = instance.get(Calendar.MONTH) == 0 ? 12 : instance.get(Calendar.MONTH);

			Iterator<ProductBean> iterator = products.iterator();

			while (iterator.hasNext()) {
				Object[] bean = new Object[2];
				ProductBean next = iterator.next();
				bean[0] = next;
				Long monthlySalesOfProduct = productRepository.MonthlySalesOfProduct(month, year, next.getProductId());
				// 如果 salesData 為非空，將其放入 bean 中；否則放入一個預設值
				if (monthlySalesOfProduct != null) {
					bean[1] = monthlySalesOfProduct;
				} else {
					bean[1] = 0;
				}
				beans.add(bean);
			}
			if (beans != null && !beans.isEmpty()) {

				return beans;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

}
