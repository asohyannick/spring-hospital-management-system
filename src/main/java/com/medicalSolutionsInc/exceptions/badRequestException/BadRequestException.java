package com.medicalSolutionsInc.exceptions.badRequestException;

public class BadRequestException extends Exception {
	public BadRequestException ( String message ) {
		super ( message );
	}
	public BadRequestException(String message, Throwable cause ) {
		super(message, cause);
	}
	public BadRequestException(Throwable cause) {
		super ( cause );
	}
	public BadRequestException() {
		super("Bad Request Exception. Please,check your request and try again later");
	}
}
