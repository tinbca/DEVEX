package com.Devex.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Devex.Entity.Order;


public interface OrderRepository extends JpaRepository<Order, String>{

	@Query("SELECT DISTINCT o FROM Order o " +
	           "JOIN FETCH o.orderDetails od " +
	           "JOIN FETCH od.productVariant pv " +
	           "JOIN FETCH pv.product p " +
	           "JOIN FETCH p.sellerProduct s " +
	           "WHERE s.username = ?1")
	    List<Order> findOrdersBySellerUsername(String sellerUsername);
	
	@Query("SELECT o FROM Order o WHERE o.id like ?1")
	Order findOrderById(String id);
	
	@Modifying
	@Query("UPDATE Order o SET o.orderStatus.id = ?1 WHERE o.id = ?2")
	void updateIdOrderStatus(int ido, String id);
	
	@Query("SELECT MONTH(o.createdDay) as month, SUM(o.total) as total FROM Order o WHERE YEAR(o.createdDay) = :year GROUP BY MONTH(o.createdDay)")
    List<Object[]> getTotalByMonthAndYear(@Param("year") int year);
    
    @Query("SELECT o FROM Order o WHERE o.createdDay = (SELECT MAX(o2.createdDay) FROM Order o2)")
    Order findLatestOrder();
	
}
