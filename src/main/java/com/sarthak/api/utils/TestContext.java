package com.sarthak.api.utils;

public class TestContext {

	private static String baseUri;
	private static String authorizationCode;
	private static String accessToken;

	
	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}

	
	public String getBaseUri() {
		return baseUri;
	}


	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken =accessToken;
	}

}
