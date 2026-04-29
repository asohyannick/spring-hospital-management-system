package com.medicalSolutionsInc.exceptions.notFoundException;
public class NotFoundException extends Exception {
	public NotFoundException ( String message ) {
		super ( message );
	}
	public NotFoundException ( String message, Throwable cause ) {
		super ( message, cause );
	}
	public NotFoundException() {
		super("The resource is not available. Please,try again later");
	}
	public NotFoundException(Throwable cause) {
		super(cause);
	}
}
