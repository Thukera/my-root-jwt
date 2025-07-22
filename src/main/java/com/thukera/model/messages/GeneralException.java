package com.thukera.model.messages;

import com.google.gson.Gson;

import lombok.Data;

@Data
public class GeneralException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private String message;
	private Object error;
	private String RennerApiStatuCode;
	
	public GeneralException(String message, Object exceptionError) {
		super();
		this.message = message;
		this.error = exceptionError;
	}
	
	public GeneralException(String message, Object exceptionError, String apiStatuCode) {
		super();
		this.message = message;
		this.error = exceptionError;
		this.RennerApiStatuCode = apiStatuCode;
	}

	
	@Override
	public String toString() {
		try {
			return new Gson().toJson(this);
		} catch(Exception e) {
			return "{\"GeneralIvrException\":\"message="+ message + ", error=" + error + ", RennerApiStatuCode="
					+ RennerApiStatuCode + "\"}";	
		}
	}
	
}
