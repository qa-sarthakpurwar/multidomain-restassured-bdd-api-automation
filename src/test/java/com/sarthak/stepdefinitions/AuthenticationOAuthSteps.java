package com.sarthak.stepdefinitions;

import static io.restassured.RestAssured.given;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertTrue;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sarthak.api.resources.APIResources;
import com.sarthak.api.utils.TestContext;
import com.sarthak.api.utils.Utils;
import com.sarthak.oauth.pojo.GetCoursesDetails;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AuthenticationOAuthSteps {

	Map<String, RequestSpecification> requestMap = new HashMap<>();
	Map<String, Response> responseMap = new HashMap<>();

	TestContext testContext;

	public AuthenticationOAuthSteps(TestContext testContext) {
		this.testContext = testContext;
	}

	@Given("I have the Authorization Server URL")
	public void i_have_the_authorization_server_url() {
		String authUrl = Utils.getProperty("authUrl");
		testContext.setBaseUri(authUrl);
	}

	@Given("I have already obtained the authorization code")
	public void i_have_already_obtained_the_authorization_code() {

		String code = testContext.getBaseUri().split("code=")[1].split("&scope")[0];
		testContext.setAuthorizationCode(code);
	}

	@Given("I have a valid request payload for access token generation")
	public void i_have_a_valid_request_payload_for_access_token_generation() {
		requestMap.put("GetAccessToken",
				given().baseUri(Utils.getProperty("accessTokenUrl")).urlEncodingEnabled(false)
						.queryParams("code", testContext.getAuthorizationCode())
						.queryParams("client_id", System.getenv("CLIENT_ID"))
						.queryParams("client_secret", System.getenv("CLIENT_SECRET"))
						.queryParams("redirect_uri", Utils.getProperty("redirectUri"))
						.queryParams("grant_type", Utils.testData.getAuth().getGrantType()));
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

		Assert.assertEquals(code.intValue(), responseMap.get(responseType).getStatusCode());

	}

	@Then("I store the {string} from the authentication {string} response")
	public void i_store_the_from_the_response(String accessToken, String responseType) {

		testContext.setAccessToken(Utils.getParsedJSONString(responseMap.get(responseType).asString(), accessToken));

	}

	@Given("I have a valid request to fetch course details")
	public void i_have_a_valid_request_to_fetch_course_details() {

		requestMap.put("GetCourse",
				given().baseUri(Utils.getProperty("baseUri")).queryParam("access_token", testContext.getAccessToken()));
	}

	@Then("the authentication {string} response should match the expected JSON schema")
	public void the_authentication_response_should_match_the_expected_json_schema(String responseType) {
		System.out.println(responseMap.get(responseType).asString());
		responseMap.get(responseType).then().assertThat()
				.body(matchesJsonSchemaInClasspath("JsonSchema/CourseSchema.json"));
	}

	@Then("I should receive the list of all available courses from {string}")
	public void i_should_receive_the_list_of_all_available_courses_from(String responseType) {
		GetCoursesDetails courseDetails = responseMap.get(responseType).as(GetCoursesDetails.class,
				io.restassured.mapper.ObjectMapperType.JACKSON_2);

		Assert.assertEquals(courseDetails.getInstructor(), Utils.testData.getExpectedData().getInstructor());
		Assert.assertEquals(courseDetails.getServices(), Utils.testData.getExpectedData().getServices());

		Assert.assertTrue(courseDetails.getCourses().getWebAutomation().size() > 0);
		Assert.assertTrue(courseDetails.getLinkedIn().contains(Utils.testData.getExpectedData().getLinkedIn()));

		List<String> allTitles = new ArrayList<>();

		courseDetails.getCourses().getWebAutomation().forEach(c -> allTitles.add(c.getCourseTitle()));
		courseDetails.getCourses().getApi().forEach(c -> allTitles.add(c.getCourseTitle()));
		courseDetails.getCourses().getMobile().forEach(c -> allTitles.add(c.getCourseTitle()));

		assertTrue(allTitles.containsAll(TestContext.EXPECTED_TITLES));

		List<String> allPrices = new ArrayList<>();
		courseDetails.getCourses().getWebAutomation().forEach(c -> allPrices.add(c.getPrice()));
		courseDetails.getCourses().getApi().forEach(c -> allPrices.add(c.getPrice()));
		courseDetails.getCourses().getMobile().forEach(c -> allPrices.add(c.getPrice()));

		for (String price : allPrices) {
			int p = Integer.parseInt(price);
			Assert.assertTrue("Price should be greater than 0 but was: " + p, p > 0);
		}

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
