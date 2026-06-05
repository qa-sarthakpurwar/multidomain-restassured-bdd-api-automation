package com.sarthak.hooks;

import java.io.FileNotFoundException;

import org.junit.Assert;

import com.sarthak.api.resources.APIResources;
import com.sarthak.api.utils.TestContext;
import com.sarthak.api.utils.Utils;
import com.sarthak.services.AuthService;
import com.sarthak.services.ProductService;
import com.sarthak.utils.ReportUtil;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Hooks {

	private TestContext context;
	private AuthService authService;
	private ProductService productService;

	public Hooks(TestContext context) {
		this.context = context;
		this.authService = new AuthService();
		this.productService = new ProductService();
	}

	@Before("@ECommerceAPI and not @Login")
	public void runLoginBeforeScenario(Scenario scenario) throws FileNotFoundException {

		  String featureName = scenario.getUri().toString();

		    System.out.println(
		        "THREAD: " + Thread.currentThread().getId() +
		        " | FEATURE: " + featureName +
		        " | SCENARIO: " + scenario.getName()
		    );
		System.out.println("Running login before: " + scenario.getName());

		RequestSpecification request = authService.login();

		Response response = request.when().post(APIResources.LoginAPI.getResource());

		String token = Utils.getParsedJSONString(response.asString(), "token");
		String userId = Utils.getParsedJSONString(response.asString(), "userId");

		// Store in context (IMPORTANT)
		context.getResponseMap().put("loginResponse", response);

		Utils.setProperty("token", token);
		Utils.setProperty("userId", userId);
	}
	
	  @After
	    public void afterScenario(Scenario scenario) {

	        if (scenario.isFailed()) {
	            ReportUtil.log("❌ Scenario Failed: " + scenario.getName());
	        } else {
	            ReportUtil.log("✅ Scenario Passed: " + scenario.getName());
	        }
	    }

	@After("@ECommerce and not @DeleteProduct and not @Login")
	public void runCleanupScenario(Scenario scenario) throws FileNotFoundException {

		System.out.println("Running cleanup after: " + scenario.getName());

		RequestSpecification request = productService.deleteProduct(Utils.getProperty("productId"));

		Response response = request.when().delete(APIResources.DeleteProductAPI.getResource());

		Assert.assertEquals(200, response.getStatusCode());
	}

}
