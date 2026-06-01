package com.sarthak.stepdefinitions;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.sarthak.api.resources.APIResources;
import com.sarthak.api.utils.Utils;
import com.sarthak.placeapi.pojo.PojoUtilities;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;

public class PlaceAPISteps extends Utils {

	Map<String, RequestSpecification> requestMap = new HashMap<>();
	Map<String, Response> responseMap = new HashMap<>();

	static String place_id;
	PojoUtilities pojoPayloads = new PojoUtilities();

	@Given("the Place API is up and running")
	public void the_place_api_is_up_and_running() {
		int statusCode = given().queryParam("key", getProperty("api.key")).when().get(getProperty("base.url"))
				.getStatusCode();
		Assert.assertTrue("API is down!", statusCode < 500);

	}

	@Given("I have a valid add place request payload")
	public void i_have_a_valid_add_place_request_payload() throws FileNotFoundException {
		;

		requestMap.put("AddPlaceAPI",
				given().spec(getPlaceRequestSpec()).body(pojoPayloads.addPlacePayload(getProperty("place.name"),
						getProperty("place.address"), getProperty("place.language"))));

	}

	@Given("I have a valid get place request")
	public void i_have_a_valid_get_place_request() throws FileNotFoundException {

		requestMap.put("GetPlaceAPI", given().spec(getPlaceRequestSpec()).queryParam("place_id", place_id));

	}

	@Given("I have a valid update place request with new address")
	public void i_have_a_valid_update_place_request_with_new_address() throws FileNotFoundException {

		requestMap.put("UpdatePlaceAPI",
				given().spec(getPlaceRequestSpec()).queryParam("key", "qaclick123").body(pojoPayloads
						.updatePlacePayload(place_id, getProperty("place.updated.address"), getProperty("api.key"))));

	}

	@Given("I have a valid delete place request with  valid  stored placeId")
	public void i_have_a_valid_delete_place_request_with_valid_stored_place_id() throws FileNotFoundException {
		requestMap.put("DeletePlaceAPI",
				given().spec(getPlaceRequestSpec()).body(pojoPayloads.deletePlacePayload(place_id)));
		setProperty("stored.place_id", place_id);
	}

	@When("I send a {string} request to add place endpoint with {string}")
	public void i_send_a_request_to_add_place_endpoint_with(String httpMethod, String resource) {
		APIResources resourceAPI = APIResources.valueOf(resource);

		RequestSpecification request = requestMap.get(resourceAPI);
		Response response;

		switch (httpMethod.toUpperCase()) {
		case "POST":
			response = requestMap.get(resource).when().post(resourceAPI.getResource());
			break;
		case "GET":
			response = requestMap.get(resource).when().get(resourceAPI.getResource());
			break;
		case "PUT":
			response = requestMap.get(resource).when().put(resourceAPI.getResource());
			break;
		case "DELETE":
			response = requestMap.get(resource).when().delete(resourceAPI.getResource());
			break;

		default:
			throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);

		}

		responseMap.put(getResponseKey(resource), response);

	}

	@Then("the {string} response status code should be {int}")
	public void the_response_status_code_should_be(String responseType, Integer code) {

		Assert.assertEquals("Status code mismatch!", code.intValue(), responseMap.get(responseType).getStatusCode());

	}

	@Then("the {string} response body should contain {string} as {string}")
	public void the_response_body_should_contain_as(String responseType, String key, String value) {

		String actualValue = getParsedJSONString(responseMap.get(responseType).then().extract().response().asString(),
				key);
		assertEquals("Mismatch on field: " + key, getProperty(value), actualValue);

	}

	@Then("the {string} response body should contain a valid {string}")
	public void the_response_body_should_contain_a_valid(String responseType, String placeId) {

		String actualValue = getParsedJSONString(responseMap.get(responseType).then().extract().response().asString(),
				placeId);
		Assert.assertNotNull("Field '" + placeId + "' should not be null", actualValue);
	}

	@When("I store the {string} from the {string} place response")
	public void i_store_the_from_the_place_response(String placeId, String responseType) {
		String placeIdValue = getParsedJSONString(responseMap.get(responseType).then().extract().response().asString(),
				placeId);
		if (placeId != null) {
			place_id = placeIdValue;
		} else {
			System.out.println("place_id is null");
		}
	}

	@Given("I have an invalid place_id")
	public void i_have_an_invalid_place_id() throws FileNotFoundException {
		place_id = getProperty("stored.place_id");
		System.out.println(place_id);
	}

	private static final Map<String, String> RESPONSE_KEY_MAP = new HashMap<>();
	static {
		RESPONSE_KEY_MAP.put("AddPlaceAPI", "addPlaceResponse");
		RESPONSE_KEY_MAP.put("GetPlaceAPI", "getPlaceResponse");
		RESPONSE_KEY_MAP.put("UpdatePlaceAPI", "updatePlaceResponse");
		RESPONSE_KEY_MAP.put("DeletePlaceAPI", "deletePlaceResponse");
	}

	private String getResponseKey(String resource) {
		return RESPONSE_KEY_MAP.get(resource);
	}

}
