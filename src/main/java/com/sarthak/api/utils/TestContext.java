package com.sarthak.api.utils;

import java.util.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.sarthak.ecommapi.pojo.ProductData;

public class TestContext {

    private static ThreadLocal<String> baseUri = new ThreadLocal<>();
    private static ThreadLocal<String> authorizationCode = new ThreadLocal<>();
    private static ThreadLocal<String> accessToken = new ThreadLocal<>();

    private static ThreadLocal<Map<String, Response>> responseMap =
            ThreadLocal.withInitial(HashMap::new);

    private static ThreadLocal<Map<String, RequestSpecification>> requestMap =
            ThreadLocal.withInitial(HashMap::new);

    private static ThreadLocal<ProductData> productData = new ThreadLocal<>();

    // BaseUri
    public String getBaseUri() {
        return baseUri.get();
    }

    public void setBaseUri(String value) {
        baseUri.set(value);
    }

    // Access Token
    public String getAccessToken() {
        return accessToken.get();
    }

    public void setAccessToken(String value) {
        accessToken.set(value);
    }

    // Authorization Code
    public String getAuthorizationCode() {
        return authorizationCode.get();
    }

    public void setAuthorizationCode(String value) {
        authorizationCode.set(value);
    }

    // Response Map
    public Map<String, Response> getResponseMap() {
        return responseMap.get();
    }

    // Request Map
    public Map<String, RequestSpecification> getRequestMap() {
        return requestMap.get();
    }

    // Product Data
    public ProductData getProductData() {
        return productData.get();
    }

    public void setProductData(ProductData data) {
        productData.set(data);
    }

    // IMPORTANT CLEANUP (optional but good)
    public void clear() {
        baseUri.remove();
        authorizationCode.remove();
        accessToken.remove();
        responseMap.remove();
        requestMap.remove();
        productData.remove();
    }
}