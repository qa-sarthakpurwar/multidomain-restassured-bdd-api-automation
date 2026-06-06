@Regression @ECommerce
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
    Then ecommerce "loginResponse" response status code should be 200
    And ecommerce "loginResponse" response body should contain a valid "token"
    And ecommerce "loginResponse" response body should contain a valid "userId"
    And ecommerce "loginResponse" response body should contain "message" as "ecomm.login.response.msg"

  # =====================================================================
  # ADD PRODUCT
  # =====================================================================
  @Regression @AddProduct @TC02
  Scenario: TC02 - Verify user can add a product with valid payload
    Given I have a valid add product request payload
    When I send a "POST" request to endpoint with "AddProductAPI"
     And I store the "productId" from the "addProductResponse" response
    Then ecommerce "addProductResponse" response status code should be 201
    And ecommerce "addProductResponse" response body should contain a valid "productId"
    And ecommerce "addProductResponse" response body should contain "message" as "ecomm.addProduct.response.msg"

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
    Then ecommerce "getProductDetailResponse" response status code should be 200
    And ecommerce "getProductDetailResponse" response body should contain "message" as "ecomm.getProductDetail.response.msg"
    And ecommerce "getProductDetailResponse" response body should contains valid product Details
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
    Then ecommerce "getProductDetailResponse" response status code should be 200
    And I store the "data.productImage" from the "getProductDetailResponse" response
    Given I have a valid add to cart request payload
    When I send a "POST" request to endpoint with "AddToCartAPI"
    Then ecommerce "addToCartResponse" response status code should be 200
    And ecommerce "addToCartResponse" response body should contain "message" as "ecomm.addToCart.response.msg"

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
    Then ecommerce "createOrderResponse" response status code should be 201
    And ecommerce "createOrderResponse" response body should contain a valid "orders"
    And ecommerce "createOrderResponse" response body should contain "message" as "ecomm.create.order.response"
    And ecommerce "createOrderResponse" response body should contain "productOrderId[0]" as "productId"

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
    Then ecommerce "createOrderResponse" response status code should be 201
    And I store the "orders[0]" from the "createOrderResponse" response
    Given I have a valid get order request
    When I send a "GET" request to endpoint with "GetOrderAPI"
    Then ecommerce "getOrderResponse" response status code should be 200
    And ecommerce "getOrderResponse" response body should contain "message" as "ecomm.get.order.response.msg"

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
    Then ecommerce "createOrderResponse" response status code should be 201
    And I store the "orders[0]" from the "createOrderResponse" response
    Given I have a valid delete order request payload
    When I send a "DELETE" request to endpoint with "DeleteOrderAPI"
    Then ecommerce "deleteOrderResponse" response status code should be 200
    And ecommerce "deleteOrderResponse" response body should contain "message" as "ecomm.delete.order.response.msg"

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
    Then ecommerce "deleteProductResponse" response status code should be 200
    And ecommerce "deleteProductResponse" response body should contain "message" as "ecomm.delete.product.resposne.msg"
