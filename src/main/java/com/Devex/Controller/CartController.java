package com.Devex.Controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Devex.Entity.CartDetail;
import com.Devex.Entity.CartProdcut;
import com.Devex.Entity.Customer;
import com.Devex.Entity.Product;
import com.Devex.Entity.ProductVariant;
import com.Devex.Entity.User;
import com.Devex.Repository.CartDetailRespository;
import com.Devex.Repository.ProductRepository;
import com.Devex.Repository.ProductVariantRepository;
import com.Devex.Sevice.CartDetailService;
import com.Devex.Sevice.RecommendationSystem;
import com.Devex.Sevice.SessionService;
import com.Devex.Sevice.ShoppingCartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CartController {

	@Autowired
	SessionService session;
	@Autowired
	ProductRepository daop;
	@Autowired
	HttpServletResponse resp;
	@Autowired
	HttpServletRequest req;
	@Autowired
	ShoppingCartService cart;
	@Autowired
	RecommendationSystem recomendationService;
	@Autowired
	ProductVariantRepository pv;
	@Autowired
	CartDetailService cartsv;
	ObjectMapper objectMapper = new ObjectMapper();
	Cookie cookie = null;

	@RequestMapping("/devex/cart")
	public String showcart(Model model, @CookieValue(value = "myCart", required = false) String cartValue) {
		cart.clear();
		User user = new User();
		List<Product> listProducts = new ArrayList<>();
		user = session.get("user");
		// Giỏ hàng
		if (session.get("user") != null) {
			session.set("cartCount", cart.getCount());
			listProducts.addAll(recomendationService.recomendProduct(user.getUsername()));
			// fix tạm
			if (listProducts.size() <= 0) {
				listProducts.addAll(recomendationService.recomendProduct("baolh"));
			}
			// end fix tạm
		} else {
			// fix tạm
			listProducts.addAll(recomendationService.recomendProduct("baolh"));
			// end fix tạm
			session.set("cartCount", 0);
		}
		// Trộn ví trí sản phẩm
		Collections.shuffle(listProducts);
		model.addAttribute("products", listProducts);
		
		if (cartValue != null && !cartValue.isEmpty()) {
			// Giải mã chuỗi Base64
			byte[] decodedBytes = Base64.decodeBase64(cartValue);
			String decodedValue = new String(decodedBytes, StandardCharsets.UTF_8);
			try {
				Map<String, CartProdcut> map = objectMapper.readValue(decodedValue,
						new com.fasterxml.jackson.core.type.TypeReference<Map<String, CartProdcut>>() {
						});
				cart.setItems(map);
			} catch (JsonProcessingException e) {
				// Xử lý lỗi khi chuyển đổi chuỗi JSON
			}

			model.addAttribute("cart", cart);
			session.set("cartCount", cart.getCount());
			model.addAttribute("total", cart.getAmount());
			List<Product> list = daop.findAll();
			model.addAttribute("test", list);

		}
		return "user/cartproduct";
		
	}
	@RequestMapping("/cartproduct/add/{idProduct}")
	public String addCart(@PathVariable("idProduct") String id, Model model,
			@RequestParam(name = "flexRadio", required = false) String size,
			@RequestParam(name = "flexRadioDefault", required = false) String cloer,
			@RequestParam(name = "soluong") int soLuong) throws JsonProcessingException {
		int idProductVariant = 	pv.findIdProductVaVariantbySizeandColor(cloer, size, id);
		ProductVariant pv2 = pv.findById(idProductVariant).get();
		CartDetail cart = new CartDetail();
		cart.setProductCart(pv2);
		cart.setQuantity(soLuong);
		Customer user = session.get("user");
		cart.setCart(user.getCart());
		cartsv.save(cart);
		return "redirect:/details/{idProduct}";
	}
	
	
//
//	@RequestMapping("/devex/cartproduct/add/{idProduct}")
//	public String addCart(@PathVariable("idProduct") String id, Model model,
//			@RequestParam(name = "soluong") int soLuong,
//			@RequestParam(name = "flexRadio", required = false) String size,
//			@RequestParam(name = "coler", required = false) String cloer) throws JsonProcessingException {
//
//		cart.add(id, soLuong, cloer, size);
//		System.out.println("size" + size);
//		Map<String, CartProdcut> itemsMap = cart.getItems().stream()
//				.collect(Collectors.toMap(CartProdcut::getId, Function.identity()));
//		String cartValue = objectMapper.writeValueAsString(itemsMap);
//		session.set("cartCount", cart.getCount());
//		cart.clear();
//		// Mã hóa chuỗi JSON thành chuỗi Base64
//		byte[] encodedBytes = Base64.encodeBase64(cartValue.getBytes(StandardCharsets.UTF_8));
//		String encodedCartValue = new String(encodedBytes, StandardCharsets.UTF_8);
//		cookie = new Cookie("myCart", encodedCartValue);
//		cookie.setMaxAge(86400);
//		cookie.setPath("/");
//		resp.addCookie(cookie);
//
//		return "redirect:/details/{idProduct}";
//	}
//
//	@RequestMapping("/devex/cartproduct/remove/{idProduct}")
//	public String remove(@PathVariable("idProduct") String id) throws JsonProcessingException {
//		cart.remove(id);
//		Map<String, CartProdcut> itemsMap = cart.getItems().stream()
//				.collect(Collectors.toMap(CartProdcut::getId, Function.identity()));
//		String cartValue = objectMapper.writeValueAsString(itemsMap);
//		cart.clear();
//		// Giải mã chuỗi Base64
//		byte[] encodedBytes = Base64.encodeBase64(cartValue.getBytes(StandardCharsets.UTF_8));
//		String encodedCartValue = new String(encodedBytes, StandardCharsets.UTF_8);
//		cookie = new Cookie("myCart", encodedCartValue);
//		cookie.setMaxAge(86400);
//		cookie.setPath("/");
//		resp.addCookie(cookie);
//
//		return "redirect:/devex/cart";
//	}

}
