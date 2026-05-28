package com.sarthak.stepdefinitions;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import com.sarthak.api.resources.APIResources;
import com.sarthak.api.utils.Utils;
import com.sarthak.placeapi.pojo.PojoUtilities;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;

public class PlaceAPISteps extends Utils {

	RequestSpecification addPlaceReq;;
	Response addPlaceResponse;

	RequestSpecification getPlaceReq;;
	Response getPlaceResponse;

	RequestSpecification updatePlaceReq;;
	Response updatePlaceResponse;

	RequestSpecification deletePlaceReq;
	Response deletePlaceResponse;

	String place_id;

	@Given("the Place API is up and running")
	public void the_place_api_is_up_and_running() {
		int statusCode = given().queryParam("key", getProperty("api.key")).when().get(getProperty("base.url"))
				.getStatusCode();
		Assert.assertTrue("API is down!", statusCode < 500);
		Assert.assertTrue("API is down!", statusCode < 500);
		Assert.assertTrue("API is down!", statusCode < 500);

	}

	@Given("I have a valid add place request payload")
	public void i_have_a_valid_add_place_request_payload() {

		RequestSpecification requestSpec = new RequestSpecBuilder().setBaseUri(getProperty("base.url"))
				.addQueryParam("key", getProperty("api.key")).setContentType(ContentType.JSON).build();

		PojoUtilities pojoPayloads = new PojoUtilities();

		addPlaceReq = given().spec(requestSpec).body(pojoPayloads.addPlacePayload(getProperty("place.name"),
				getProperty("place.address"), getProperty("place.language")));

	}

	@When("I send a {string} request to add place endpoint with {string}")
	public void i_send_a_request_to_add_place_endpoint_with(String httpMethod, String resource) {
		APIResources resourceAPI = APIResources.valueOf(resource);
		if (httpMethod.equalsIgnoreCase("POST")) {
			addPlaceResponse = addPlaceReq.when().post(resourceAPI.getResource());
		} else if (httpMethod.equalsIgnoreCase("GET")) {
			getPlaceResponse = addPlaceReq.when().get(resourceAPI.getResource());
		} else if (httpMethod.equalsIgnoreCase("PUT")) {
			updatePlaceResponse = updatePlaceReq.when().get(resourceAPI.getResource());
		} else {
			deletePlaceResponse = deletePlaceReq.when().get(resourceAPI.getResource());
		}
	}

	@Then("the response status code should be {int}")
	public void the_response_status_code_should_be(Integer int1) {
		assertEquals(addPlaceResponse.getStatusCode(), 200);
	}

	@Then("the response body should contain {string} as {string}")
	public void the_response_body_should_contain_as(String key, String value) {

		JsonPath js = new JsonPath(addPlaceResponse.then().extract().response().asString());
		String actualValue = js.getString(key);
		String expectedValue = getProperty(value);
	
		assertEquals("Mismatch on field: " + key, expectedValue, actualValue);
		System.out.println(expectedValue + "Matched");

	}

	@Then("the response body should contain a valid {string}")
	public void the_response_body_should_contain_a_valid(String placeId) {
		JsonPath js = new JsonPath(addPlaceResponse.then().extract().response().asString());
		String actualValue = js.getString(placeId);
		Assert.assertNotNull("Field '" + placeId + "' should not be null", actualValue);
	}

	@When("I store the {string} from the response")
	public void i_store_the_from_the_response(String placeId) {
		JsonPath js = new JsonPath(addPlaceResponse.then().extract().response().asString());
		String placeIdValue = js.getString(placeId);
		if (placeId != null) {
			place_id = placeIdValue;
		} else {
			System.out.println("place_id is null");
		}
	}

}
