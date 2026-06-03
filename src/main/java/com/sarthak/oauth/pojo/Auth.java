package com.sarthak.oauth.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Auth {

	@JsonProperty("grant_type")
	private String grantType;

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

}
