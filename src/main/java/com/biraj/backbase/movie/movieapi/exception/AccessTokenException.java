package com.biraj.backbase.movie.movieapi.exception;
/**
 * 
 * @author birajmishra
 *This exception must be thrown when
 *any issues in JWT token like expiry, malformed, etc., and 
 *if any data related to token or profile is not found in database.
 *
 */
public class AccessTokenException extends MovieApiException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5103834241136923369L;
	
	public AccessTokenException() {
		super("40001" , "Access token exception occurred.");
	}
	
	public AccessTokenException(String errorCode,String errorDescription) {
		super(errorCode , errorDescription);
	}
	

}
