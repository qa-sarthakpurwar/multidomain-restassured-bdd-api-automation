package com.sarthak.ecommapi.pojo;

import java.util.List;

public class CreateOrder {

	private List<CreateOrderDetail> orders;

	public List<CreateOrderDetail> getOrders() {
		return orders;
	}

	public void setOrders(List<CreateOrderDetail> ordersl) {
		this.orders = ordersl;
	}

}
