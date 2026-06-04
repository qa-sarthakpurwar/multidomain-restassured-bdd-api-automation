package com.sarthak.services;

import java.io.FileNotFoundException;

import com.sarthak.api.utils.Utils;
import com.sarthak.ecommapi.pojo.EcommPojoUtilities;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class OrderService extends Utils {

	EcommPojoUtilities pojo = new EcommPojoUtilities();

	public RequestSpecification createOrder(String country, String productId) throws FileNotFoundException {

		return given().spec(getEcommRequestSpec()).contentType(ContentType.JSON)
				.body(pojo.createOrderPayload(country, productId));
	}

	public RequestSpecification getOrder(String orderId) throws FileNotFoundException {

		return given().spec(getEcommRequestSpec()).queryParam("id", orderId);
	}

	public RequestSpecification deleteOrder(String orderId) throws FileNotFoundException {

		return given().spec(getEcommRequestSpec()).pathParam("orderId", orderId);
	}
}