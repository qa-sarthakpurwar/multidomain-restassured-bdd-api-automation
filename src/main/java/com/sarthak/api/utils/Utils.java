package com.sarthak.api.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Properties;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
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

	public static RequestSpecification getPlaceRequestSpec() throws FileNotFoundException {
		if (requestSpec == null) {

			PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));

			requestSpec = new RequestSpecBuilder().setBaseUri(getProperty("base.url"))
					.addQueryParam("key", getProperty("api.key")).addFilter(RequestLoggingFilter.logRequestTo(log))
					.addFilter(ResponseLoggingFilter.logResponseTo(log)).setContentType(ContentType.JSON).build();
			return requestSpec;

		}

		return requestSpec;
	}

}
