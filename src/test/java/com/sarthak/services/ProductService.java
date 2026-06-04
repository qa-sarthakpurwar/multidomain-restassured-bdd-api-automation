package com.sarthak.services;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileNotFoundException;

import com.sarthak.api.utils.Utils;

import io.restassured.specification.RequestSpecification;


public class ProductService extends Utils{
	
    public RequestSpecification addProduct() throws FileNotFoundException {

        return given()
                .spec(getEcommRequestSpec())
                .param("productName", productData.getProductName())
                .param("productAddedBy", getProperty("userId"))
                .param("productCategory", productData.getProductCategory())
                .param("productSubCategory", productData.getProductSubCategory())
                .param("productPrice", productData.getProductPrice())
                .param("productDescription", productData.getProductDescription())
                .param("productFor", productData.getProductFor())
                .multiPart("productImage", new File(getProperty("productImage")));
    }

    public RequestSpecification getProduct(String productId) throws FileNotFoundException {

        return given()
                .spec(getEcommRequestSpec())
                .pathParam("productId", productId);
    }

    public RequestSpecification deleteProduct(String productId) throws FileNotFoundException {

        return given()
                .spec(getEcommRequestSpec())
                .pathParam("productId", productId);
    }

}
