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
	CreateOrderAPI("/api/ecom/order/create-order"),
	GetOrderAPI("/api/ecom/order/get-orders-details"),
	DeleteOrderAPI("/api/ecom/order/delete-order/{orderId}"),
	DeleteProductAPI("/api/ecom/product/delete-product/{productId}"),
	
	GetAccessToken("/oauth2/v4/token"),
	GetCourse("/getCourse.php");
	

	private String resource;

	private APIResources(String resource) {
		this.resource = resource;
	}

	public String getResource() {
		return resource;
	}

}
