package com.sarthak.hooks;

import java.io.FileNotFoundException;

import com.sarthak.stepdefinitions.EcommerceAPISteps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

	@Before("not @Login")
	public void runLoginBeforeScenario(Scenario scenario) throws FileNotFoundException {
		System.out.println("Running login before: " + scenario.getName());

		EcommerceAPISteps stepDefinition = new EcommerceAPISteps();
		stepDefinition.i_have_a_valid_login_request_payload();
		stepDefinition.i_send_a_request_to_endpoint_with("POST", "LoginAPI");
		stepDefinition.i_store_the_from_the_response("token", "loginResponse");
		stepDefinition.i_store_the_from_the_response("userId", "loginResponse");
	}

	@After("not @Login and not @DeleteProduct")
	public void runCleanupScenario(Scenario scenario) throws FileNotFoundException {

		System.out.println("Running cleanup after: " + scenario.getName());

		EcommerceAPISteps stepDefinition = new EcommerceAPISteps();
		stepDefinition.i_have_a_valid_delete_product_request_payload();
		stepDefinition.i_send_a_request_to_endpoint_with("DELETE", "DeleteProductAPI");
		stepDefinition.the_response_status_code_should_be("deleteProductResponse", 200);

	}

}
