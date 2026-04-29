package com.medicalSolutionsInc.exceptions.conflictRequestException;

public class ConflictRequestException extends RuntimeException {
	public ConflictRequestException ( String message ) {
		super ( message );
	}
	public ConflictRequestException(String message, Throwable cause) {
		super(message, cause);
	}
	public ConflictRequestException(Throwable cause) {
		super(cause);
	}
	public ConflictRequestException() {
		super("Duplicate Exception Occurred. Please, check your request and try again later.");
	}
}
