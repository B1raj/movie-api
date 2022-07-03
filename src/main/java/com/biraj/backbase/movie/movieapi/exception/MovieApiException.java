package com.biraj.backbase.movie.movieapi.exception;

import lombok.Getter;

/**
 * 
 * @author birajmishra
 * Global Exception handler
 */

@Getter
public abstract class MovieApiException extends RuntimeException  {
	private static final long serialVersionUID = -5103834241136923370L;
	
	private String errorCode;
	
	public MovieApiException(String errorCode, String errorDescription) {
		super(errorDescription);
		this.errorCode = errorCode;
	}
	
	public MovieApiException(String errorCode, String errorDescription , Exception exception) {
		super(errorDescription , exception);
		this.errorCode = errorCode;
	}
	
}
