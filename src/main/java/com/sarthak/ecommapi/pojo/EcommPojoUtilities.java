package com.sarthak.ecommapi.pojo;

import java.util.ArrayList;
import java.util.List;

public class EcommPojoUtilities {

	public LoginAPI loginPayload(String username, String password) {

		LoginAPI loginObject = new LoginAPI();
		loginObject.setUserEmail(username);
		loginObject.setUserPassword(password);

		return loginObject;

	}

	public AddToCart addToCartPayload(List<String> values) {
		AddToCart addToCartObject = new AddToCart();

		Product productObject = new Product();
		productObject.set_id(values.get(0));
		productObject.setProductName(values.get(1));
		productObject.setProductCategory(values.get(2));
		productObject.setProductSubCategory(values.get(3));
		productObject.setProductPrice(Integer.parseInt(values.get(4)));
		productObject.setProductDescription(values.get(5));
		productObject.setProductImage(values.get(6));
		productObject.setProductRating(values.get(7));
		productObject.setProductTotalOrders(values.get(8));
		productObject.setProductStatus(Boolean.parseBoolean(values.get(9)));
		productObject.setProductFor(values.get(10));
		productObject.setProductAddedBy(values.get(11));
		productObject.set__v(Integer.parseInt(values.get(12)));

		addToCartObject.set_id(values.get(13));
		addToCartObject.setProduct(productObject);

		return addToCartObject;
	}

	public CreateOrder createOrderPayload(String country, String productId) {

		CreateOrderDetail createOrderDetail = new CreateOrderDetail();
		createOrderDetail.setCountry(country);
		createOrderDetail.setProductOrderedId(productId);

		List<CreateOrderDetail> orderList = new ArrayList<CreateOrderDetail>();
		orderList.add(createOrderDetail);

		CreateOrder createOrderPayload = new CreateOrder();
		createOrderPayload.setOrders(orderList);
		return createOrderPayload;
	}

}
