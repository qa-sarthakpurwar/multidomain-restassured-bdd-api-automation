@ECommerceAPI
Feature: E-Commerce API - CRUD Operations
# =============================================================================
# Author      : qa-sarthakpurwar
# Created On  : 2026-05-29
# Feature     : E-Commerce API Automation
# Description : Covers positive CRUD and E2E scenarios
# =============================================================================

  # =====================================================================
  # LOGIN
  # =====================================================================
  @Regression @Login @TC01
  Scenario: TC01 - Verify user can login with valid credentials
    Given I have a valid login request payload
    When I send a "POST" request to endpoint with "LoginAPI"
    Then the "loginResponse" response status code should be 200
    And the "loginResponse" response body should contain a valid "token"
    And the "loginResponse" response body should contain a valid "userId"
    And the "loginResponse" response body should contain "message" as "ecomm.login.response.msg"

  # =====================================================================
  # ADD PRODUCT
  # =====================================================================
  @Regression @AddProduct @TC02
  Scenario: TC02 - Verify user can add a product with valid payload
    Given I have a valid login request payload
    When I send a "POST" request to endpoint with "LoginAPI"
    And I store the "token" from the "loginResponse" response
    And I store the "userId" from the "loginResponse" response
    Given I have a valid add product request payload
    When I send a "POST" request to endpoint with "AddProductAPI"
    Then the "addProductResponse" response status code should be 201
    And the "addProductResponse" response body should contain a valid "productId"
    And the "addProductResponse" response body should contain "message" as "ecomm.addProduct.response.msg"

  # =====================================================================
  # GET PRODUCT DETAIL
  # =====================================================================
  @Regression @AddProduct @TC03
  Scenario: TC02 - Verify user can add a product with valid payload
    Given I have a valid login request payload
    When I send a "POST" request to endpoint with "LoginAPI"
    And I store the "token" from the "loginResponse" response
    Given I have a valid add product request payload
    When I send a "POST" request to endpoint with "AddProductAPI"
    And I store the "productId" from the "addProductResponse" response
    Given I have a valid get product detail request payload
    When I send a "GET" request to endpoint with "GetProductDetailAPI"
    Then the "getProductDetailResponse" response status code should be 200
    And the "getProductDetailResponse" response body should contain "message" as "ecomm.getProductDetail.response.msg"
# =====================================================================
# ADD PRODUCT  TO CART
# =====================================================================

  @Regression @AddToCart @TC04
  Scenario: TC04 - Verify user can add product to cart
    Given I have a valid login request payload
    When I send a "POST" request to endpoint with "LoginAPI"
    And I store the "token" from the "loginResponse" response
    Given I have a valid add product request payload
    When I send a "POST" request to endpoint with "AddProductAPI"
    And I store the "productId" from the "addProductResponse" response
    Given I have a valid get product detail request payload
    When I send a "GET" request to endpoint with "GetProductDetailAPI"
    Then the "getProductDetailResponse" response status code should be 200
    And I store the "data.productImage" from the "getProductDetailResponse" response
    Given I have a valid add to cart request payload
    When I send a "POST" request to endpoint with "AddToCartAPI"
    Then the "addToCartResponse" response status code should be 200
    And the "addToCartResponse" response body should contain "message" as "ecomm.addToCart.response.msg"

  # =====================================================================
  # CREATE ORDER
  # =====================================================================
  @Regression @CreateOrder @TC05
  Scenario: TC05 - Verify user can create an order successfully
    Given I have a valid login request payload
    When I send a "POST" request to endpoint with "LoginAPI"
    And I store the "token" from the "loginResponse" response
    Given I have a valid add product request payload
    When I send a "POST" request to endpoint with "AddProductAPI"
    And I store the "productId" from the "addProductResponse" response
    Given I have a valid create order request payload
    When I send a "POST" request to endpoint with "CreateOrderAPI"
    Then the "createOrderResponse" response status code should be 201
    And the "createOrderResponse" response body should contain a valid "orders"
    And the "createOrderResponse" response body should contain "message" as "ecomm.create.order.response"
    And the "createOrderResponse" response body should contain "productOrderId[0]" as "productId"
    
    
    
