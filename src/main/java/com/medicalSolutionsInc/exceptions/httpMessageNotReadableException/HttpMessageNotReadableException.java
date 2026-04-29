package com.medicalSolutionsInc.exceptions.httpMessageNotReadableException;

public class HttpMessageNotReadableException extends Exception {
	public HttpMessageNotReadableException ( String message ) {
		super ( message );
	}
	public HttpMessageNotReadableException(String message, Throwable cause) {
		super ( message, cause );
	}
	public HttpMessageNotReadableException(Throwable cause) {
		super ( cause );
	}
}
