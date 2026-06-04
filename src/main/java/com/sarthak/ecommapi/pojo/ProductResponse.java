package com.sarthak.ecommapi.pojo;

public class ProductResponse {
	private Product data;
	private String message;

	public Product getData() {
		return data;
	}

	public void setData(Product data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
