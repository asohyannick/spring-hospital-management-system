package com.medicalSolutionsInc.exceptions.serverError;

public class ServerError extends Exception {
	public ServerError ( String message ) {
		super ( message );
	}
	public ServerError ( String message, Throwable cause ) {
		super ( message, cause );
	}
	public ServerError(Throwable cause) {
		super(cause);
	}
	public ServerError() {
		super("The server is down. Please, try again later");
	}
}
