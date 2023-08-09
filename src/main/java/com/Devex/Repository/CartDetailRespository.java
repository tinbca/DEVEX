package com.Devex.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Devex.Entity.CartDetail;
import com.Devex.Sevice.CartDetailDTo;




@EnableJpaRepositories
@Repository("cartDetailRepository")
public interface CartDetailRespository extends JpaRepository<CartDetail, Integer>{
	
	
		@Query("SELECT new com.Devex.Sevice.CartDetailDTo(cd.id, cd.productCart.price, cd.cart.id, cd.quantity, cd.productCart.product.name,"
			+ "cd.productCart.color ,cd.productCart.size,cd.productCart.product.sellerProduct.shopName ,"
			+ "cd.productCart.product.sellerProduct.username,"
			+ "cd.productCart.product.id,"
			+ "cd.productCart.product.sellerProduct.avatar,"
			+ "(SELECT ip.name FROM cd.productCart.product.imageProducts ip ORDER BY ip.id ASC LIMIT 1) AS img) FROM CartDetail cd JOIN cd.productCart.product.imageProducts ip\r\n"
			+ "WHERE ip.product = cd.productCart.product and cd.cart.person.username = ?1 ")
    	List<CartDetailDTo> findAllCartDTO(String username );
	
//		@Query("DELETE FROM CartDetail c WHERE c.productCart.product.sellerProduct.username = ?1")
//		void deleteByShopId(String idShop);
		@Query("SELECT c FROM CartDetail c WHERE c.productCart.product.sellerProduct.username = :idShop")
	    List<CartDetail> findCartDetailsByShopId(@Param("idShop") String idShop);


}
