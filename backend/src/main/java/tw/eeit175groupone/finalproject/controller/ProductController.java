package tw.eeit175groupone.finalproject.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.eeit175groupone.finalproject.domain.ProductBean;
import tw.eeit175groupone.finalproject.domain.ProductImageBean;
import tw.eeit175groupone.finalproject.dto.ProductDTO;
import tw.eeit175groupone.finalproject.dto.ProductDetailDTO;
import tw.eeit175groupone.finalproject.dto.ProductSearchDTO;
import tw.eeit175groupone.finalproject.service.ProductCommentService;
import tw.eeit175groupone.finalproject.service.ProductImageService;
import tw.eeit175groupone.finalproject.service.ProductService;

@RestController
@RequestMapping("/gameshop")
@CrossOrigin
public class ProductController {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductImageService productImageService;
	@Autowired
	private ProductCommentService productCommentService;

	/**
	 * 無條件找出全部商品
	 * 
	 * @return 全部商品
	 */
	@GetMapping("/product")
	public ResponseEntity<List<ProductBean>> findAllWithoutNotSale() {
		List<ProductBean> beans = productService.findAllWithoutNotSale();
		// Iterator<ProductBean> iterator = beans.iterator();
		// while(iterator.hasNext()) {
		// System.err.println(iterator.next());
		// }

		return ResponseEntity.ok().body(beans);
	}

	/**
	 * 有條件找出全部商品(關鍵字、金額上限及下限)
	 * 
	 * @return 條件內全部商品
	 */
	@PostMapping("/product/find")
	public ResponseEntity<?> findProduct(@RequestBody String body) {

		long count = productService.countProduct(body);
		List<ProductBean> beans = productService.findProduct(body);

		// 創建DTO方便抓取Product col值
		ProductSearchDTO response = new ProductSearchDTO();
		response.setBeans(beans);
		response.setCount(count);

		if (beans != null && !beans.isEmpty()) {
			return ResponseEntity.ok().body(response);
		}
		return ResponseEntity.ok().body(response);
	}

	/**
	 * 產品頁詳細內容
	 * 
	 * @param id 抓取productId
	 * @return product+product_image+product_comment(留言評分)+merchandise(周邊資訊)+gameinfor(遊戲規格)
	 */
	@GetMapping("/product/detail/{id}")
	public ResponseEntity<?> getProductDetail(@PathVariable(name = "id") Integer id) {
		ProductDetailDTO productDetail = productService.findProductDetail(id);
		if (productDetail != null) {
			return ResponseEntity.ok().body(productDetail);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	/**
	 * 找出遊戲或周邊的產品
	 * 
	 * @param productType
	 * @return 找出遊戲或周邊的產品
	 */
	@PostMapping("/product/find/type/{type}")
	public ResponseEntity<?> findAllByProductType(@PathVariable(name = "type") String type) {
		List<ProductBean> findAllByProductType = productService.findAllByProductType(type);

		return ResponseEntity.ok().body(findAllByProductType);
	}

	/**
	 * 找出遊戲或周邊的產品
	 * 
	 * @param productType
	 * @return 找出遊戲或周邊的產品
	 */
	@PostMapping("/product/find/subtype/{subtype}")
	public ResponseEntity<?> findAllByProductSubType(@PathVariable(name = "subtype") String subtype) {
		List<ProductBean> findAllByProductSubType = productService.findAllByProductSubType(subtype);

		return ResponseEntity.ok().body(findAllByProductSubType);
	}

	/**
	 * 創建產品
	 * 
	 * @param body 含product、merchandise、gameInfor表(尚未加入productImage)
	 * @return 是否創建成功
	 */
	@PostMapping("/product/create")
	public ResponseEntity<?> createProduct(@RequestBody String body) {
		// System.err.println("body = "+body);
		boolean isCreated = productService.createProduct(body);
		JSONObject response = new JSONObject();
		if (isCreated) {
			response.put("success", true);
			response.put("text", "成功上架產品");
			return ResponseEntity.ok().body(response.toString());
		} else {
			response.put("success", false);
			response.put("text", "OOPS!請確認必填欄位是否為空");
			return ResponseEntity.ok().body(response.toString());
		}
	}

	/**
	 * 創建產品照片
	 * 
	 * @param body product_img
	 * @return 是否創建成功
	 */
	@PostMapping("/product/createproductimg")
	public ResponseEntity<?> createProductImage(@RequestBody String body) {
		System.err.println("body = " + body);
		boolean isCreated = productImageService.insertProductImage(body);
		JSONObject response = new JSONObject();
		if (isCreated) {
			response.put("success", true);
			response.put("text", "成功上傳商品照片");
			return ResponseEntity.ok().body(response.toString());
		} else {
			response.put("success", false);
			response.put("text", "OOPS! YOUR PHOTO GOT Something wrong");
			return ResponseEntity.ok().body(response.toString());
		}
	}

	/**
	 * 找出第一張產品照片
	 */
	@PostMapping("/product/firstproductimg/{id}")
	public ResponseEntity<?> findFirstProductImg(@PathVariable("id") Integer productId) {
		List<ProductImageBean> image = productImageService.findFirstProductImage(productId);
		if (image != null) {
			return ResponseEntity.ok().body(image);
		} else {
			return ResponseEntity.ok().body(null);
		}
	}

	/**
	 * 找出最新插入產品的id
	 * 
	 * @return 最新產品id
	 */
	@GetMapping("/product/lastproductid")
	public ResponseEntity<?> findLastProductId() {
		Integer lastProductId = productService.findLastProductId();
		return ResponseEntity.ok().body(lastProductId);
	}

	/**
	 * 創建產品留言 更新評論狀態
	 * 
	 * @param body
	 * @return 成功失敗
	 */
	@PostMapping("/product/createcomment")
	public ResponseEntity<?> insertProductComment(@RequestBody String body) {
		JSONObject res = new JSONObject();
		boolean isCreated = productCommentService.insertProductCommentAndOrderDetail(body);
		if (isCreated) {
			res.put("success", true);
			res.put("text", "評論成功");
			return ResponseEntity.ok().body(res.toString());
		} else {
			res.put("success", false);
			res.put("text", "評論失敗");
			return ResponseEntity.ok().body(res.toString());
		}

	}

	/**
	 * 找出特定編號產品
	 * 
	 * @param param 產品編號
	 * @return 產品內容
	 */
	@GetMapping("/product/find/{id}")
	public ResponseEntity<?> findByProductId(@PathVariable("id") Integer productId) {
		if (productId != null) {
			ProductBean bean = productService.findByProductId(productId);
			if (bean != null) {
				return ResponseEntity.ok().body(bean);
			}
		}
		return ResponseEntity.ok().body(null);
	}

	@PutMapping("/product/update")
	public ResponseEntity<?> updateProdcutDetail(@RequestBody ProductDTO productDTO) {

		JSONObject response = new JSONObject();
		// System.out.println("85858555588888888888 = "+productDTO);
		if (productDTO != null) {
			boolean isUpdated = productService.updateProductDTO(productDTO);
			if (isUpdated) {
				response.put("success", true);
				response.put("text", "商品已更新");
				return ResponseEntity.ok().body(response.toString());
			}
		}
		response.put("success", false);
		response.put("text", "商品更新失敗");
		return ResponseEntity.ok().body(response.toString());

	}

	@GetMapping("/product/detailforupdate/{id}")
	public ResponseEntity<?> findProductDTO(@PathVariable("id") Integer productId) {
		if (productId != null) {
			ProductDTO bean = productService.findProductDTO(productId);
			if (bean != null) {
				return ResponseEntity.ok().body(bean);
			}
		}
		return ResponseEntity.ok().body(null);
	}

	@GetMapping("/product/monthlysales")
	public ResponseEntity<?> MonthlySalesOfProduct() {
		List<Object[]> beans = productService.MonthlySalesOfProduct();
		if (beans != null) {
			System.err.println("hihihhihihihihihhi");
			return ResponseEntity.ok().body(beans);
		}
		return ResponseEntity.ok().body(null);
	}

}
