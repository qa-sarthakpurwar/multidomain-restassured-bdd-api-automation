package com.sarthak.services;

import java.io.FileNotFoundException;

import static io.restassured.RestAssured.given;

import com.sarthak.api.utils.Utils;
import com.sarthak.ecommapi.pojo.EcommPojoUtilities;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class AuthService extends Utils {

	EcommPojoUtilities loginPayloads = new EcommPojoUtilities();

	public RequestSpecification login() throws FileNotFoundException {

		return given().spec(getEcommRequestSpec()).contentType(ContentType.JSON)
				.body(loginPayloads.loginPayload(System.getenv("ECOMM_USERNAME"), System.getenv("ECOMM_PASSWORD")));
	}

}
