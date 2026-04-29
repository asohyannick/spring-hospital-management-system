package com.medicalSolutionsInc.exceptions.unAuthorizedException;
public class UnAuthorizedException extends Exception {
	public UnAuthorizedException(String message, Throwable cause) {
		super(message, cause);
	}
	public UnAuthorizedException ( String message ) {
		super ( message );
	}
	public UnAuthorizedException(Throwable cause) {
		super(cause);
	}
	public UnAuthorizedException() {
		super("You're not authorized to access the resource");
	}
}
