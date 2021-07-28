package com.careydevelopment.ecosystem.customer.service;

import org.springframework.http.HttpStatus;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -7661881974219233311L;

	private int statusCode;
	
	public ServiceException (String message, HttpStatus httpStatus) {
	    super(message);
	    
	    if (httpStatus != null) this.statusCode = httpStatus.value();
	    else this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
	}
	
	public ServiceException (String message, int statusCode) {
		super(message);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
