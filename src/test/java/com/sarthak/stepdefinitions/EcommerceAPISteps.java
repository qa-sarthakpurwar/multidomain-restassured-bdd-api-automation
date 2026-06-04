package com.sarthak.stepdefinitions;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.sarthak.api.resources.APIResources;
import com.sarthak.api.utils.TestContext;
import com.sarthak.api.utils.Utils;
import com.sarthak.ecommapi.pojo.EcommPojoUtilities;
import com.sarthak.ecommapi.pojo.ProductResponse;
import com.sarthak.services.AuthService;
import com.sarthak.services.CartService;
import com.sarthak.services.OrderService;
import com.sarthak.services.ProductService;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EcommerceAPISteps {

	private TestContext context;

	public EcommerceAPISteps(TestContext context) {
		this.context = context;
	}

	EcommPojoUtilities pojoPayloads = new EcommPojoUtilities();
	AuthService authService = new AuthService();
	ProductService productService = new ProductService();
	CartService cartService = new CartService();
	OrderService orderService = new OrderService();

	@Given("I have a valid login request payload")
	public void i_have_a_valid_login_request_payload() throws FileNotFoundException {

		context.getRequestMap().put("LoginAPI", authService.login());

	}

	@When("I send a {string} request to endpoint with {string}")
	public void i_send_a_request_to_endpoint_with(String httpMethod, String resource) {
		APIResources resourceAPI = APIResources.valueOf(resource);

		RequestSpecification request = context.getRequestMap().get(resource);
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
		context.getResponseMap().put(getResponseKey(resource), response);
	}

	@Then("ecommerce {string} response status code should be {int}")
	public void ecommerce_response_status_code_should_be(String responseType, Integer code) {
		Assert.assertEquals(code.intValue(), context.getResponseMap().get(responseType).getStatusCode());
	}

	@Then("ecommerce {string} response body should contain a valid {string}")
	public void ecommerce_response_body_should_contain_a_valid(String responseType, String value) {

		String actualValue = Utils.getParsedJSONString(context.getResponseMap().get(responseType).asString(), value);
		Assert.assertNotNull(value + " should not be null", actualValue);
	}

	@Then("ecommerce {string} response body should contain {string} as {string}")
	public void ecommerce_response_body_should_contain_as(String responseType, String key, String value) {

		String actualValue = Utils.getParsedJSONString(context.getResponseMap().get(responseType).asString(), key);
		Assert.assertEquals("Mismatch on field: " + key, Utils.getProperty(value), actualValue);
	}

	@Then("ecommerce {string} response body should contains valid product Details")
	public void ecommerce_response_body_should_contains_valid_product_Details(String responseType) {

		ProductResponse productResponse = context.getResponseMap().get(responseType).as(ProductResponse.class);
		Assert.assertEquals(productResponse.getData().getProductAddedBy(), Utils.getProperty("userId"));
		Assert.assertEquals(productResponse.getData().getProductCategory(), Utils.productData.getProductCategory());
		Assert.assertEquals(productResponse.getData().getProductDescription(),
				Utils.productData.getProductDescription());
		Assert.assertEquals(productResponse.getData().getProductFor(), Utils.productData.getProductFor());
		Assert.assertEquals(productResponse.getData().getProductName(), Utils.productData.getProductName());
		Assert.assertEquals(productResponse.getData().getProductPrice(), Utils.productData.getProductPrice());

	}

	@When("I store the {string} from the {string} response")
	public void i_store_the_from_the_response(String key, String responseType) {
		Utils.setProperty(key, Utils.getParsedJSONString(context.getResponseMap().get(responseType).asString(), key));
	}

	@Given("I have a valid add product request payload")
	public void i_have_a_valid_add_product_request_payload() throws FileNotFoundException {

		context.getRequestMap().put("AddProductAPI", productService.addProduct());
	}

	@Given("I have a valid get product detail request payload")
	public void i_have_a_valid_get_product_detail_request_payload() throws FileNotFoundException {

		context.getRequestMap().put("GetProductDetailAPI", productService.getProduct(Utils.getProperty("productId")));

	}

	@Given("I have a valid add to cart request payload")
	public void i_have_a_valid_add_to_cart_request_payload() throws FileNotFoundException {

		List<String> data = Utils.getProductDataList();
		context.getRequestMap().put("AddToCartAPI", cartService.addToCart(data));

	}

	@Given("I have a valid create order request payload")
	public void i_have_a_valid_create_order_request_payload() throws FileNotFoundException {

		context.getRequestMap().put("CreateOrderAPI",
				orderService.createOrder(Utils.getProperty("country"), Utils.getProperty("productId")));

	}

	@Given("I have a valid get order request")
	public void i_have_a_valid_get_order_request() throws FileNotFoundException {

		context.getRequestMap().put("GetOrderAPI", orderService.getOrder(Utils.getProperty("orders[0]")));

	}

	@Given("I have a valid delete order request payload")
	public void i_have_a_valid_delete_order_request_payload() throws FileNotFoundException {

		context.getRequestMap().put("DeleteOrderAPI", orderService.deleteOrder(Utils.getProperty("orders[0]")));

	}

	@Given("I have a valid delete product request payload")
	public void i_have_a_valid_delete_product_request_payload() throws FileNotFoundException {

		context.getRequestMap().put("DeleteProductAPI", productService.deleteProduct(Utils.getProperty("productId")));

	}

	private static final Map<String, String> RESPONSE_KEY_MAP = new HashMap<>();
	static {
		RESPONSE_KEY_MAP.put("LoginAPI", "loginResponse");
		RESPONSE_KEY_MAP.put("AddProductAPI", "addProductResponse");
		RESPONSE_KEY_MAP.put("GetProductDetailAPI", "getProductDetailResponse");
		RESPONSE_KEY_MAP.put("AddToCartAPI", "addToCartResponse");
		RESPONSE_KEY_MAP.put("CreateOrderAPI", "createOrderResponse");
		RESPONSE_KEY_MAP.put("GetOrderAPI", "getOrderResponse");
		RESPONSE_KEY_MAP.put("DeleteOrderAPI", "deleteOrderResponse");
		RESPONSE_KEY_MAP.put("DeleteProductAPI", "deleteProductResponse");
	}

	private String getResponseKey(String resource) {
		return RESPONSE_KEY_MAP.get(resource);
	}
}
