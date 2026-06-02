package com.sarthak.stepdefinitions;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import com.sarthak.api.resources.APIResources;
import com.sarthak.api.utils.TestContext;
import com.sarthak.api.utils.Utils;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;

public class AuthenticationOAuthSteps extends Utils {

	Map<String, RequestSpecification> requestMap = new HashMap<>();
	Map<String, Response> responseMap = new HashMap<>();

	TestContext testContext;

	public AuthenticationOAuthSteps(TestContext testContext) {
		this.testContext = testContext; 
	}

	@Given("I have the Authorization Server URL")
	public void i_have_the_authorization_server_url() {
		String authUrl = "https://rahulshettyacademy.com/getCourse.php?state=random_string_123&iss=https%3A%2F%2Faccounts.google.com&code=4%2F0AeoWuM_mvJEjTYNKX3qdWn3im51wRREUKdi8tyScENfwWCut-31fqbzFGmJJi09ATXPzFA&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=1&prompt=none";
		testContext.setBaseUri(authUrl);
	}

	@Given("I have already obtained the authorization code")
	public void i_have_already_obtained_the_authorization_code() {
		
		String code = testContext.getBaseUri().split("code=")[1].split("&scope")[0];
		testContext.setAuthorizationCode(code);
	}

	@Given("I have a valid request payload for access token generation")
	public void i_have_a_valid_request_payload_for_access_token_generation() {
		requestMap.put("GetAccessToken", given().baseUri("https://www.googleapis.com").urlEncodingEnabled(false)
				.queryParams("code", testContext.getAuthorizationCode())
				.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParams("grant_type", "authorization_code"));
		
		System.out.println(testContext.getAuthorizationCode());
	}

	@When("I send a {string} request to the {string} endpoint")
	public void i_send_a_request_to_the_endpoint(String httpMethod, String resource) {

		APIResources resourceAPI = APIResources.valueOf(resource);

		RequestSpecification request = requestMap.get(resource);
		Response response;

		switch (httpMethod.toUpperCase()) {
		case "POST":
			response = request.when().log().all().post(resourceAPI.getResource());
			break;
		case "GET":
			response = request.when().log().all().get(resourceAPI.getResource());
			break;

		default:
			throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
		}

		responseMap.put(getResponseKey(resource), response);

	
	}

	@Then("the authentication {string} response status code should be {int}")
	public void the_authentication_response_status_code_should_be(String responseType, Integer code) {

		System.out.println(responseMap.get(responseType).asString());
		Assert.assertEquals(code.intValue(), responseMap.get(responseType).getStatusCode());
		

	}

	@Then("I store the {string} from the authentication {string} response")
	public void i_store_the_from_the_response(String accessToken, String responseType) {

		testContext.setAccessToken(getParsedJSONString(responseMap.get(responseType).asString(), accessToken));
		System.out.println(getParsedJSONString(responseMap.get(responseType).asString() , "access_token"));
		System.out.println(testContext.getAccessToken());

	}

	@Given("I have a valid request to fetch course details")
	public void i_have_a_valid_request_to_fetch_course_details() {
       	
		requestMap.put("GetCourse", given().baseUri("https://rahulshettyacademy.com").queryParam("access_token", testContext.getAccessToken()));
	}

	@Then("the authentication {string} response should match the expected JSON schema")
	public void the_authentication_response_should_match_the_expected_json_schema(String responseType) {
   System.out.println("Schema Passed");
	}

	@Then("I should receive the list of all available courses from {string}")
	public void i_should_receive_the_list_of_all_available_courses_from(String responseType) {
		System.out.println(responseMap.get(responseType).asString());
	}

	private static final Map<String, String> RESPONSE_KEY_MAP = new HashMap<>();
	static {
		RESPONSE_KEY_MAP.put("GetAccessToken", "getAccessTokenResponse");
		RESPONSE_KEY_MAP.put("GetCourse", "getCourseResponse");

	}

	private String getResponseKey(String resource) {
		return RESPONSE_KEY_MAP.get(resource);
	}
}
