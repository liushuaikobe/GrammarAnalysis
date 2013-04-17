package org.ls.entity;

public class Token {
	private String token;
	private String value;
	public Token(String token, String value) {
		super();
		this.token = token;
		this.value = value;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
