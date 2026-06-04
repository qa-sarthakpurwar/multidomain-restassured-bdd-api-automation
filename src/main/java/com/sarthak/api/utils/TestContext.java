package com.sarthak.api.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sarthak.ecommapi.pojo.ProductData;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestContext {

	private static String baseUri;
	private static String authorizationCode;
	private static String accessToken;

	private Map<String, Response> responseMap = new HashMap<>();
	private Map<String, RequestSpecification> requestMap = new HashMap<>();
	private ProductData productData;

	public Map<String, Response> getResponseMap() {
		return responseMap;
	}

	public Map<String, RequestSpecification> getRequestMap() {
		return requestMap;
	}

	public ProductData getProductData() {
		return productData;
	}

	public void setProductData(ProductData productData) {
		this.productData = productData;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}

	public String getBaseUri() {
		return baseUri;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public static final List<String> EXPECTED_TITLES = Arrays.asList("Selenium Webdriver Java", "Cypress", "Protractor",
			"Rest Assured Automation using Java", "SoapUI Webservices testing", "Appium-Mobile Automation using Java");

}
