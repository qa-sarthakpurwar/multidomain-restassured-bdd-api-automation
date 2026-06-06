package com.sarthak.hooks;

import java.io.FileNotFoundException;

import org.junit.Assert;

import com.sarthak.api.resources.APIResources;
import com.sarthak.api.utils.TestContext;
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

        System.out.println(
                "THREAD: " + Thread.currentThread().getId()
                        + " | SCENARIO: " + scenario.getName()
        );

        RequestSpecification request = authService.login();

        Response response = request.when()
                .post(APIResources.LoginAPI.getResource());

        String token = response.jsonPath().getString("token");
        String userId = response.jsonPath().getString("userId");

        // ✅ THREAD-SAFE STORAGE (IMPORTANT FIX)
        context.setAccessToken(token);
        context.setAuthorizationCode(userId);

        context.getResponseMap().put("loginResponse", response);

        System.out.println("Login completed for thread: " + Thread.currentThread().getId());
    }

    @After
    public void afterScenario(Scenario scenario) {

        if (scenario.isFailed()) {
            ReportUtil.log("❌ Scenario Failed: " + scenario.getName());
        } else {
            ReportUtil.log("✅ Scenario Passed: " + scenario.getName());
        }

        // IMPORTANT: cleanup thread-safe context
        context.clear();
    }

    @After("@ECommerce and not @DeleteProduct and not @Login")
    public void runCleanupScenario(Scenario scenario) throws FileNotFoundException {

        System.out.println("Running cleanup after: " + scenario.getName());

        String productId = context.getResponseMap()
                .get("addProductResponse")
                .jsonPath()
                .getString("productId");

        if (productId != null) {

            RequestSpecification request = productService.deleteProduct(productId);

            Response response = request.when()
                    .delete(APIResources.DeleteProductAPI.getResource());

            Assert.assertEquals(200, response.getStatusCode());
        }
    }
}