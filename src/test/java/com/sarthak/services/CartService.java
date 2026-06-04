package com.sarthak.services;

import java.io.FileNotFoundException;
import java.util.List;

import com.sarthak.api.utils.Utils;
import com.sarthak.ecommapi.pojo.EcommPojoUtilities;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class CartService extends Utils{
	
	 EcommPojoUtilities pojo = new EcommPojoUtilities();

	    public RequestSpecification addToCart(List<String> productIds) throws FileNotFoundException {

	        return given()
	                .spec(getEcommRequestSpec())
	                .contentType(ContentType.JSON)
	                .body(pojo.addToCartPayload(productIds));
	    }

}
