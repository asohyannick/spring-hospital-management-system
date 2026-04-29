package com.medicalSolutionsInc.exceptions.forbiddenException;

public class ForbiddenException extends Exception {
	public ForbiddenException ( String message ) {
		super ( message );
	}
	public ForbiddenException(Throwable cause) {
		super ( cause );
	}
	public ForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super ( message, cause, enableSuppression, writableStackTrace );
	}
	public ForbiddenException() {
		super("Access Denied. You're are not authorize to access this resource");
	}
}
