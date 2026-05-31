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
    Given I have a valid add product request payload
    When I send a "POST" request to endpoint with "AddProductAPI"
     And I store the "productId" from the "addProductResponse" response
    Then the "addProductResponse" response status code should be 201
    And the "addProductResponse" response body should contain a valid "productId"
    And the "addProductResponse" response body should contain "message" as "ecomm.addProduct.response.msg"

  # =====================================================================
  # GET PRODUCT DETAIL
  # =====================================================================
  @Regression @GetProduct @TC03
  Scenario: TC03 - Verify product is persisted correctly after adding
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
    Given I have a valid add product request payload
    When I send a "POST" request to endpoint with "AddProductAPI"
    And I store the "productId" from the "addProductResponse" response
    Given I have a valid create order request payload
    When I send a "POST" request to endpoint with "CreateOrderAPI"
    Then the "createOrderResponse" response status code should be 201
    And the "createOrderResponse" response body should contain a valid "orders"
    And the "createOrderResponse" response body should contain "message" as "ecomm.create.order.response"
    And the "createOrderResponse" response body should contain "productOrderId[0]" as "productId"

  # =====================================================================
  # GET ORDER
  # =====================================================================
  @Regression @GetOrder @TC06
  Scenario: TC06 - Verify order is persisted correctly after creation
    Given I have a valid add product request payload
    When I send a "POST" request to endpoint with "AddProductAPI"
    And I store the "productId" from the "addProductResponse" response
    Given I have a valid create order request payload
    When I send a "POST" request to endpoint with "CreateOrderAPI"
    Then the "createOrderResponse" response status code should be 201
    And I store the "orders[0]" from the "createOrderResponse" response
    Given I have a valid get order request
    When I send a "GET" request to endpoint with "GetOrderAPI"
    Then the "getOrderResponse" response status code should be 200
    And the "getOrderResponse" response body should contain "message" as "ecomm.get.order.response.msg"

  # =====================================================================
  # DELETE ORDER
  # =====================================================================
  @Regression @DeleteOrder @TC07
  Scenario: TC07 - Verify user can delete an existing order
    Given I have a valid add product request payload
    When I send a "POST" request to endpoint with "AddProductAPI"
    And I store the "productId" from the "addProductResponse" response
    Given I have a valid create order request payload
    When I send a "POST" request to endpoint with "CreateOrderAPI"
    Then the "createOrderResponse" response status code should be 201
    And I store the "orders[0]" from the "createOrderResponse" response
    Given I have a valid delete order request payload
    When I send a "DELETE" request to endpoint with "DeleteOrderAPI"
    Then the "deleteOrderResponse" response status code should be 200
    And the "deleteOrderResponse" response body should contain "message" as "ecomm.delete.order.response.msg"

  # =====================================================================
  # DELETE PRODUCT
  # =====================================================================
  @Regression @DeleteProduct @TC08
  Scenario: TC08 - Verify user can delete an existing product
    Given I have a valid add product request payload
    When I send a "POST" request to endpoint with "AddProductAPI"
    And I store the "productId" from the "addProductResponse" response
    Given I have a valid delete product request payload
    When I send a "DELETE" request to endpoint with "DeleteProductAPI"
    Then the "deleteProductResponse" response status code should be 200
    And the "deleteProductResponse" response body should contain "message" as "ecomm.delete.product.resposne.msg"
