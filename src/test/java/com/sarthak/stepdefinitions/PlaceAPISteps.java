package com.sarthak.stepdefinitions;

import static io.restassured.RestAssured.given;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

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

		addPlaceReq = given().spec(getPlaceRequestSpec()).body(pojoPayloads.addPlacePayload(getProperty("place.name"),
				getProperty("place.address"), getProperty("place.language")));

	}

	@Given("I have a valid get place request")
	public void i_have_a_valid_get_place_request() throws FileNotFoundException {

		getPlaceReq = given().spec(getPlaceRequestSpec()).queryParam("place_id", place_id);

	}

	@Given("I have a valid update place request with new address")
	public void i_have_a_valid_update_place_request_with_new_address() throws FileNotFoundException {

		updatePlaceReq = given().spec(getPlaceRequestSpec()).queryParam("key", "qaclick123").body(pojoPayloads
				.updatePlacePayload(place_id, getProperty("place.updated.address"), getProperty("api.key")));

	}

	@Given("I have a valid delete place request with  valid  stored placeId")
	public void i_have_a_valid_delete_place_request_with_valid_stored_place_id() throws FileNotFoundException {
		deletePlaceReq = given().spec(getPlaceRequestSpec()).body(pojoPayloads.deletePlacePayload(place_id));
		setProperty("stored.place_id", place_id);
	}

	@When("I send a {string} request to add place endpoint with {string}")
	public void i_send_a_request_to_add_place_endpoint_with(String httpMethod, String resource) {
		APIResources resourceAPI = APIResources.valueOf(resource);
		System.out.println(resourceAPI.getResource());

		if (httpMethod.equalsIgnoreCase("POST")) {
			addPlaceResponse = addPlaceReq.when().post(resourceAPI.getResource());
		} else if (httpMethod.equalsIgnoreCase("GET")) {
			getPlaceResponse = getPlaceReq.when().get(resourceAPI.getResource());
		} else if (httpMethod.equalsIgnoreCase("PUT")) {
			updatePlaceResponse = updatePlaceReq.when().put(resourceAPI.getResource());
		} else {
			deletePlaceResponse = deletePlaceReq.when().delete(resourceAPI.getResource());
		}

	}

	@Then("the {string} response status code should be {int}")
	public void the_response_status_code_should_be(String responseType, Integer code) {
		if (responseType.equalsIgnoreCase("addPlaceResponse")) {
			Assert.assertEquals("Status code mismatch!", code.intValue(), addPlaceResponse.getStatusCode());
		} else if (responseType.equalsIgnoreCase("getPlaceResponse")) {
			Assert.assertEquals("Status code mismatch!", code.intValue(), getPlaceResponse.getStatusCode());
		} else if (responseType.equalsIgnoreCase("updatePlaceResponse")) {
			Assert.assertEquals("Status code mismatch!", code.intValue(), updatePlaceResponse.getStatusCode());
		} else {
			Assert.assertEquals("Status code mismatch!", code.intValue(), deletePlaceResponse.getStatusCode());
		}

	}

	@Then("the {string} response body should contain {string} as {string}")
	public void the_response_body_should_contain_as(String responseType, String key, String value) {

		if (responseType.equalsIgnoreCase("addPlaceResponse")) {
			JsonPath js = new JsonPath(addPlaceResponse.then().extract().response().asString());
			String actualValue = js.getString(key);
			String expectedValue = getProperty(value);

			assertEquals("Mismatch on field: " + key, expectedValue, actualValue);
		} else if (responseType.equalsIgnoreCase("getPlaceResponse")) {
			JsonPath js = new JsonPath(getPlaceResponse.then().extract().response().asString());
			String actualValue = js.getString(key);
			String expectedValue = getProperty(value);

			assertEquals("Mismatch on field: " + key, expectedValue, actualValue);
		} else if (responseType.equalsIgnoreCase("updatePlaceResponse")) {
			JsonPath js = new JsonPath(updatePlaceResponse.then().extract().response().asString());
			String actualValue = js.getString(key);
			String expectedValue = getProperty(value);

			assertEquals("Mismatch on field: " + key, expectedValue, actualValue);
		}else
		{
			JsonPath js = new JsonPath(deletePlaceResponse.then().extract().response().asString());
			String actualValue = js.getString(key);
			String expectedValue = getProperty(value);

			assertEquals("Mismatch on field: " + key, expectedValue, actualValue);
			
		}

	}

	@Then("the {string} response body should contain a valid {string}")
	public void the_response_body_should_contain_a_valid(String responseType, String placeId) {

		if (responseType.equalsIgnoreCase("addPlaceResponse")) {

			JsonPath js = new JsonPath(addPlaceResponse.then().extract().response().asString());
			String actualValue = js.getString(placeId);
			Assert.assertNotNull("Field '" + placeId + "' should not be null", actualValue);
		} else if (responseType.equalsIgnoreCase("getPlaceResponse")) {
			JsonPath js = new JsonPath(getPlaceResponse.then().extract().response().asString());
			String actualValue = js.getString(placeId);
			Assert.assertNotNull("Field '" + placeId + "' should not be null", actualValue);
		}
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

	@Given("I have an invalid place_id")
	public void i_have_an_invalid_place_id() throws FileNotFoundException {
		place_id = getProperty("stored.place_id");
		System.out.println(place_id);
	}
	
	@When("I execute complete CRUD flow")
	public void i_execute_complete_crud_flow() {
     
	}



}
