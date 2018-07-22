package com.bht.gw.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MicrositeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorcode;
	private String errorMessage;
	private String errorName;
	
	
	public MicrositeException(String errorcode, String errorMessage, String errorName) {
		super();
		this.errorcode = errorcode;
		this.errorMessage = errorMessage;
		this.errorName = errorName;
	}
	
	public MicrositeException(String errorcode, String errorMessage) {
		super();
		this.errorcode = errorcode;
		this.errorMessage = errorMessage;
	}
	
	public MicrositeException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	
}
