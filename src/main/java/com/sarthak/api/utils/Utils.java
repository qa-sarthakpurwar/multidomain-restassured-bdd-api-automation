package com.sarthak.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarthak.ecommapi.pojo.ProductData;
import com.sarthak.oauth.pojo.TestData;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

public class Utils {

	public static RequestSpecification requestSpec;

	private static Properties properties = new Properties();

	static {
		try {
			FileInputStream file = new FileInputStream("src\\test\\resources\\TestData\\TestData.properties");
			properties.load(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	public static void setProperty(String key, String value) {
		try {
			properties.setProperty(key, value);
			FileOutputStream output = new FileOutputStream("src\\test\\resources\\TestData\\TestData.properties");
			properties.store(output, null);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static RequestSpecification getPlaceRequestSpec() throws FileNotFoundException {

		String fileName = "logging-" + Thread.currentThread().getId() + ".txt";

		PrintStream log = new PrintStream(new FileOutputStream(fileName, true));

		return new RequestSpecBuilder().setBaseUri(getProperty("base.url")).addQueryParam("key", getProperty("api.key"))
				.addFilter(RequestLoggingFilter.logRequestTo(log)).addFilter(ResponseLoggingFilter.logResponseTo(log))
				.setContentType(ContentType.JSON).build();
	}

	// ── Parse JSON response and get value by key ───────────────────

	public static String getParsedJSONString(String response, String key) {
		try {
			if (response == null || response.isEmpty()) {
				throw new IllegalArgumentException("Response body is null or empty");
			}
			if (key == null || key.isEmpty()) {
				throw new IllegalArgumentException("Key is null or empty");
			}
			JsonPath js = new JsonPath(response);
			String actualValue = js.getString(key);
			if (actualValue == null) {
				throw new RuntimeException("Key '" + key + "' not found in response");
			}
			return actualValue;
		} catch (IllegalArgumentException e) {
			System.err.println("[VALIDATION ERROR] " + e.getMessage());
			throw e;
		} catch (RuntimeException e) {
			System.err.println("[PARSING ERROR] Failed to parse key '" + key + "' from response: " + e.getMessage());
			throw e;
		}
	}

	public static RequestSpecification getEcommRequestSpec() throws FileNotFoundException {
		if (requestSpec == null) {

			PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));

			requestSpec = new RequestSpecBuilder().setBaseUri(getProperty("base.url"))
					.addHeader("Authorization", getProperty("token"))
					// .addFilter(new RequestLoggingFilter(LogDetail.ALL, true, log))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL, true, log)).build();
			return requestSpec;

		}

		return requestSpec;
	}

	// ── Read Json File───────────────────

	public static TestData testData;

	static {
		try {
			ObjectMapper mapper = new ObjectMapper();
			testData = mapper.readValue(new File("src\\test\\resources\\TestData\\OAuthCourse.json"), TestData.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static TestData getTestData() {
		return testData;
	}

	public static ProductData productData;

	static {
		try {
			ObjectMapper mapper = new ObjectMapper();
			productData = mapper.readValue(new File("src\\test\\resources\\TestData\\ProductDetails.json"),
					ProductData.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static ProductData getProductData() {
		return productData;
	}

	public static List<String> getProductDataList() {
		List<String> data = new ArrayList<>();
		data.add(getProperty("productId"));
		data.add(getProperty("productName"));
		data.add(getProperty("productCategory"));
		data.add("watch");
		data.add(getProperty("productPrice"));
		data.add(getProperty("productDescription"));
		data.add(getProperty("data.productImage"));
		data.add("0");
		data.add("0");
		data.add("true");
		data.add(getProperty("productFor"));
		data.add(getProperty("userId"));
		data.add("0");
		data.add(getProperty("userId"));
		return data;
	}

}
