package com.Devex.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.Devex.DTO.CartDetailDTo;
import com.Devex.Entity.CartDetail;

@EnableJpaRepositories
@Repository("cartDetailRepository")
public interface CartDetailRespository extends JpaRepository<CartDetail, Integer>{
	@Query("SELECT new com.Devex.DTO.CartDetailDTo(cd.id, cd.productCart.price, cd.cart.id, cd.quantity, cd.productCart.product.name,"
			+ "cd.productCart.color ,cd.productCart.size,cd.productCart.product.sellerProduct.shopName ,"
			+ "cd.productCart.product.sellerProduct.username,"
			+ "cd.productCart.product.id,"
			+ "cd.productCart.product.sellerProduct.avatar,"
			+ "ip.name) FROM CartDetail cd JOIN cd.productCart.product.imageProducts ip\r\n"
			+ "WHERE ip.product = cd.productCart.product")
    List<CartDetailDTo> findAllCartDTO();
	


}
