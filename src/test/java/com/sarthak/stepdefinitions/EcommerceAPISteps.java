package com.sarthak.stepdefinitions;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.sarthak.api.resources.APIResources;
import com.sarthak.api.utils.Utils;
import com.sarthak.ecommapi.pojo.EcommPojoUtilities;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EcommerceAPISteps extends Utils {

	Map<String, Response> responseMap = new HashMap<>();
	Map<String, RequestSpecification> requestMap = new HashMap<>();

	EcommPojoUtilities pojoPayloads = new EcommPojoUtilities();

	@Given("I have a valid login request payload")
	public void i_have_a_valid_login_request_payload() throws FileNotFoundException {

		requestMap.put("LoginAPI", given().spec(getEcommRequestSpec()).contentType(ContentType.JSON)
				.body(pojoPayloads.loginPayload(getProperty("ecomm.username"), getProperty("ecomm.password"))));

	}

	@When("I send a {string} request to endpoint with {string}")
	public void i_send_a_request_to_endpoint_with(String httpMethod, String resource) {
		APIResources resourceAPI = APIResources.valueOf(resource);

		RequestSpecification request = requestMap.get(resource);
		Response response;

		switch (httpMethod.toUpperCase()) {
		case "POST":
			response = request.when().post(resourceAPI.getResource());
			break;
		case "GET":
			response = request.when().get(resourceAPI.getResource());
			break;
		case "PUT":
			response = request.when().put(resourceAPI.getResource());
			break;
		case "DELETE":
			response = request.when().delete(resourceAPI.getResource());
			break;
		default:
			throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
		}
		responseMap.put(getResponseKey(resource), response);
	}

	@Then("ecommerce {string} response status code should be {int}")
	public void ecommerce_response_status_code_should_be(String responseType, Integer code) {
		Assert.assertEquals(code.intValue(), responseMap.get(responseType).getStatusCode());
	}

	@Then("ecommerce {string} response body should contain a valid {string}")
	public void ecommerce_response_body_should_contain_a_valid(String responseType, String value) {

		String actualValue = getParsedJSONString(responseMap.get(responseType).asString(), value);
		Assert.assertNotNull(value + " should not be null", actualValue);
	}

	@Then("ecommerce {string} response body should contain {string} as {string}")
	public void ecommerce_response_body_should_contain_as(String responseType, String key, String value) {

		String actualValue = getParsedJSONString(responseMap.get(responseType).asString(), key);
		Assert.assertEquals("Mismatch on field: " + key, getProperty(value), actualValue);
	}

	@When("I store the {string} from the {string} response")
	public void i_store_the_from_the_response(String key, String responseType) {
		setProperty(key, getParsedJSONString(responseMap.get(responseType).asString(), key));
	}

	@Given("I have a valid add product request payload")
	public void i_have_a_valid_add_product_request_payload() throws FileNotFoundException {

		requestMap.put("AddProductAPI",
				given().spec(getEcommRequestSpec()).param("productName", getProperty("productName"))
						.param("productAddedBy", getProperty("userId"))
						.param("productCategory", getProperty("productCategory"))
						.param("productSubCategory", getProperty("productSubCategory"))
						.param("productPrice", getProperty("productPrice"))
						.param("productDescription", getProperty("productDescription"))
						.param("productFor", getProperty("productFor"))
						.multiPart("productImage", new File(getProperty("productImage"))));
	}

	@Given("I have a valid get product detail request payload")
	public void i_have_a_valid_get_product_detail_request_payload() throws FileNotFoundException {
		requestMap.put("GetProductDetailAPI",
				given().spec(getEcommRequestSpec()).pathParam("productId", getProperty("productId")));
	}

	@Given("I have a valid add to cart request payload")
	public void i_have_a_valid_add_to_cart_request_payload() throws FileNotFoundException {

		List<String> data = getProductData();

		requestMap.put("AddToCartAPI", given().spec(getEcommRequestSpec()).contentType(ContentType.JSON)
				.body(pojoPayloads.addToCartPayload(data)));

	}

	@Given("I have a valid create order request payload")
	public void i_have_a_valid_create_order_request_payload() throws FileNotFoundException {
		requestMap.put("CreateOrderAPI", given().spec(getEcommRequestSpec()).contentType(ContentType.JSON)
				.body(pojoPayloads.createOrderPayload(getProperty("country"), getProperty("productId"))));
	}

	@Given("I have a valid get order request")
	public void i_have_a_valid_get_order_request() throws FileNotFoundException {
		requestMap.put("GetOrderAPI", given().spec(getEcommRequestSpec()).queryParam("id", getProperty("orders[0]")));
	}

	@Given("I have a valid delete order request payload")
	public void i_have_a_valid_delete_order_request_payload() throws FileNotFoundException {

		requestMap.put("DeleteOrderAPI",
				given().spec(getEcommRequestSpec()).pathParam("orderId", getProperty("orders[0]")));

	}

	@Given("I have a valid delete product request payload")
	public void i_have_a_valid_delete_product_request_payload() throws FileNotFoundException {
		requestMap.put("DeleteProductAPI",
				given().spec(getEcommRequestSpec()).pathParam("productId", getProperty("productId")));
	}

	private static final Map<String, String> RESPONSE_KEY_MAP = new HashMap<>();
	static {
	    RESPONSE_KEY_MAP.put("LoginAPI",            "loginResponse");
	    RESPONSE_KEY_MAP.put("AddProductAPI",       "addProductResponse");
	    RESPONSE_KEY_MAP.put("GetProductDetailAPI", "getProductDetailResponse");
	    RESPONSE_KEY_MAP.put("AddToCartAPI",        "addToCartResponse");
	    RESPONSE_KEY_MAP.put("CreateOrderAPI",      "createOrderResponse");
	    RESPONSE_KEY_MAP.put("GetOrderAPI",         "getOrderResponse");
	    RESPONSE_KEY_MAP.put("DeleteOrderAPI",      "deleteOrderResponse");
	    RESPONSE_KEY_MAP.put("DeleteProductAPI",    "deleteProductResponse");
	}

	private String getResponseKey(String resource) {
	    return RESPONSE_KEY_MAP.get(resource);  
	}
}
