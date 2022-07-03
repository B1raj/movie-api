package com.biraj.backbase.movie.movieapi.exception;

/**
 * @author birajmishra
 * FOr invalid data
 */
public class InvalidDataException extends MovieApiException {

	
	private static final long serialVersionUID = 1L;
	
	public InvalidDataException() {
		super("40000" , "Data exception occured.");
	}
	
	public InvalidDataException(String errorCode,String errorDescription) {
		super(errorCode , errorDescription);
	}
	
	public InvalidDataException(String errorCode,String errorDescription , Exception exception) {
		super(errorCode , errorDescription , exception);
	}
	
	
}
