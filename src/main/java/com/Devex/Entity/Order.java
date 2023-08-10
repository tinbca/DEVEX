package com.Devex.Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Orders")
public class Order implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID", updatable = false)
	private String id;
	@Column(name = "Note")
	private String note;

	@Column(name = "Createdday")
	private Date createdDay;
	@Column(name = "Total")
	private Double total;
	@Column(name = "Pricediscount")
	private Double priceDiscount;
	@Column(name = "Address")
	private String address;
	@Column(name = "Phone")
	private String phone;
	
	@ManyToOne
	@JoinColumn(name = "Payment_ID")
	private Payment payment;
	
	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "Status_ID")
	private OrderStatus orderStatus;
	
	@ManyToOne
	@JoinColumn(name = "Customer_ID")
	private Customer customerOrder;
	
	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "Voucher_ID")
	private Voucher voucherOrder;
	
	@JsonIgnore
	@OneToMany(mappedBy = "order")
	private List<OrderDetails> orderDetails;
	

}
