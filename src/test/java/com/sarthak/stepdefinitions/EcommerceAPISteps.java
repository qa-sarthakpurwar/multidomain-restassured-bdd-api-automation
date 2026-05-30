package com.sarthak.stepdefinitions;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.sarthak.api.resources.APIResources;
import com.sarthak.api.utils.Utils;
import com.sarthak.ecommapi.pojo.AddToCart;
import com.sarthak.ecommapi.pojo.EcommPojoUtilities;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EcommerceAPISteps extends Utils {

	RequestSpecification loginReq;;
	Response loginResponse;

	RequestSpecification addProductReq;
	Response addProductResponse;

	RequestSpecification getProductDetailReq;
	Response getProductDetailResponse;

	RequestSpecification addToCartReq;
	Response addToCartResponse;

	RequestSpecification getProductReq;
	Response getProductResponse;

	RequestSpecification createOrderReq;
	Response createOrderResponse;

	EcommPojoUtilities pojoPayloads = new EcommPojoUtilities();

	@Given("I have a valid login request payload")
	public void i_have_a_valid_login_request_payload() throws FileNotFoundException {

		loginReq = given().spec(getEcommRequestSpec()).contentType(ContentType.JSON)
				.body(pojoPayloads.loginPayload(getProperty("ecomm.username"), getProperty("ecomm.password")));

	}

	@When("I send a {string} request to endpoint with {string}")
	public void i_send_a_request_to_endpoint_with(String httpMethod, String resource) {
		APIResources resourceAPI = APIResources.valueOf(resource);
		System.out.println(resourceAPI.getResource());

		if (resource.equalsIgnoreCase("LoginAPI")) {
			loginResponse = loginReq.when().post(resourceAPI.getResource());
		} else if (resource.equalsIgnoreCase("AddProductAPI")) {
			addProductResponse = addProductReq.when().post(resourceAPI.getResource());
		} else if (resource.equalsIgnoreCase("AddToCartAPI")) {
			addToCartResponse = addToCartReq.when().post(resourceAPI.getResource());
		} else if (resource.equalsIgnoreCase("GetProductDetailAPI")) {
			getProductDetailResponse = getProductDetailReq.when().get(resourceAPI.getResource());
		} else if (resource.equalsIgnoreCase("CreateOrderAPI")) {
			createOrderResponse = createOrderReq.when().post(resourceAPI.getResource());
		}
	}

	@Then("the {string} response status code should be {int}")
	public void the_response_status_code_should_be(String responseType, Integer code) {
		if (responseType.equalsIgnoreCase("addPlaceResponse")) {
			Assert.assertEquals("Status code mismatch!", code.intValue(), loginResponse.getStatusCode());
		} else if (responseType.equalsIgnoreCase("addProductResponse")) {
			Assert.assertEquals("Status code mismatch!", code.intValue(), addProductResponse.getStatusCode());
		} else if (responseType.equalsIgnoreCase("addToCartResponse")) {
			System.out.println(addToCartResponse.getStatusCode());
			Assert.assertEquals("Status code mismatch!", code.intValue(), addToCartResponse.getStatusCode());
		} else if (responseType.equalsIgnoreCase("getProductDetailResponse")) {
			System.out.println(getProductDetailResponse.getStatusCode());
			Assert.assertEquals("Status code mismatch!", code.intValue(), getProductDetailResponse.getStatusCode());
		} else if (responseType.equalsIgnoreCase("createOrderResponse")) {
			System.out.println(createOrderResponse.getStatusCode());
			Assert.assertEquals("Status code mismatch!", code.intValue(), createOrderResponse.getStatusCode());
		}
	}

	@Then("the {string} response body should contain a valid {string}")
	public void the_response_body_should_contain_a_valid(String responseType, String value) {
		String actualValue = "";

		if (responseType.equalsIgnoreCase("loginResponse")) {
			actualValue = getParsedJSONString(loginResponse.then().extract().response().asString(), value);
			Assert.assertNotNull(actualValue, "" + value + "  should not be null");

		} else if (responseType.equalsIgnoreCase("addProductResponse")) {
			actualValue = getParsedJSONString(addProductResponse.then().extract().response().asString(), value);
			Assert.assertNotNull(actualValue, "" + value + "  should not be null");

		} else if (responseType.equalsIgnoreCase("createOrderResponse")) {
			actualValue = getParsedJSONString(createOrderResponse.then().extract().response().asString(), value);
			Assert.assertNotNull(actualValue, "" + value + "  should not be null");

		}
	}

	@Then("the {string} response body should contain {string} as {string}")
	public void the_response_body_should_contain_as(String responseType, String key, String value) {
		String actualValue = "";
		String expectedValue = getProperty(value);

		if (responseType.equalsIgnoreCase("loginResponse")) {
			actualValue = getParsedJSONString(loginResponse.then().extract().response().asString(), key);
			Assert.assertEquals("Mismatch on field: " + key, expectedValue, actualValue);

		} else if (responseType.equalsIgnoreCase("addProductResponse")) {
			actualValue = getParsedJSONString(addProductResponse.then().extract().response().asString(), key);
			Assert.assertEquals("Mismatch on field: " + key, expectedValue, actualValue);

		} else if (responseType.equalsIgnoreCase("addToCartResponse")) {
			actualValue = getParsedJSONString(addToCartResponse.then().extract().response().asString(), key);
			Assert.assertEquals("Mismatch on field: " + key, expectedValue, actualValue);

		} else if (responseType.equalsIgnoreCase("getProductDetailResponse")) {
			actualValue = getParsedJSONString(getProductDetailResponse.then().extract().response().asString(), key);
			Assert.assertEquals("Mismatch on field: " + key, expectedValue, actualValue);
		} else if (responseType.equalsIgnoreCase("createOrderResponse")) {
			actualValue = getParsedJSONString(createOrderResponse.then().extract().response().asString(), key);
			Assert.assertEquals("Mismatch on field: " + key, expectedValue, actualValue);
		}

	}

	@When("I store the {string} from the {string} response")
	public void i_store_the_from_the_response(String key, String responseType) {
		if (responseType.equalsIgnoreCase("loginResponse")) {
			String loginResponseKey = getParsedJSONString(loginResponse.then().extract().response().asString(), key);
			setProperty(key, loginResponseKey);
		} else if (responseType.equalsIgnoreCase("addProductResponse")) {
			String addProductResponseKey = getParsedJSONString(
					addProductResponse.then().extract().response().asString(), key);
			setProperty(key, addProductResponseKey);
			System.out.println(addProductResponseKey);
		} else if (responseType.equalsIgnoreCase("getProductDetailResponse")) {
			String addProductResponseKey = getParsedJSONString(
					getProductDetailResponse.then().extract().response().asString(), key);
			setProperty(key, addProductResponseKey);
			System.out.println(addProductResponseKey);
		}
	}

	@Given("I have a valid add product request payload")
	public void i_have_a_valid_add_product_request_payload() throws FileNotFoundException {

		addProductReq = given().spec(getEcommRequestSpec()).param("productName", getProperty("productName"))
				.param("productAddedBy", getProperty("userId")).param("productCategory", getProperty("productCategory"))
				.param("productSubCategory", "watch").param("productPrice", getProperty("productPrice"))
				.param("productDescription", getProperty("productDescription"))
				.param("productFor", getProperty("productFor"))
				.multiPart("productImage", new File(getProperty("productImage")));
	}

	@Given("I have a valid get product detail request payload")
	public void i_have_a_valid_get_product_detail_request_payload() throws FileNotFoundException {
		getProductDetailReq = given().spec(getEcommRequestSpec()).pathParam("productId", getProperty("productId"));
	}

	@Given("I have a valid add to cart request payload")
	public void i_have_a_valid_add_to_cart_request_payload() throws FileNotFoundException {

		List<String> data = new ArrayList<>();
		data.add(getProperty("productId"));
		data.add(getProperty("productName"));
		data.add(getProperty("productCategory"));
		data.add("watch");
		data.add(getProperty("productPrice"));
		data.add(getProperty("productDescription"));
		data.add(getProperty("data.productImage"));
		data.add("0");
		data.add("0");
		data.add("true");
		data.add(getProperty("productFor"));
		data.add(getProperty("userId"));
		data.add("0");
		data.add(getProperty("userId"));

		addToCartReq = given().spec(getEcommRequestSpec()).contentType(ContentType.JSON)
				.body(pojoPayloads.addToCartPayload(data));

	}

	@Given("I have a valid create order request payload")
	public void i_have_a_valid_create_order_request_payload() throws FileNotFoundException {
		createOrderReq = given().spec(getEcommRequestSpec()).contentType(ContentType.JSON)
				.body(pojoPayloads.createOrderPayload(getProperty("country"), getProperty("productId")));
	}

}
