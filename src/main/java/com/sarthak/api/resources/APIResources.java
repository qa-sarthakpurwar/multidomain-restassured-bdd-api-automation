package com.sarthak.api.resources;

public enum APIResources {

	AddPlaceAPI("/maps/api/place/add/json"), 
	GetPlaceAPI("/maps/api/place/get/json"),
	DeletePlaceAPI("/maps/api/place/delete/json"), 
	UpdatePlaceAPI("/maps/api/place/update/json"),

	LoginAPI("/api/ecom/auth/login"),
	AddProductAPI("/api/ecom/product/add-product"),
	AddToCartAPI("/api/ecom/user/add-to-cart"),
	GetProductDetailAPI("/api/ecom/product/get-product-detail/{productId}"),
	CreateOrderAPI("/api/ecom/order/create-order");

	private String resource;

	private APIResources(String resource) {
		this.resource = resource;
	}

	public String getResource() {
		return resource;
	}

}
