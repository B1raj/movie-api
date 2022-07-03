/**
 * 
 */
package com.biraj.backbase.movie.movieapi.constant;

import org.springframework.stereotype.Component;

/**
 * @author birajmishra
 * Constants used across application
 */
@Component
public interface MovieConstant {
	
	String UNABLE_TO_AUTHENTICATE = "Authentication Failed.";
	String ACCESS_TOKEN = "AccessToken";
	String AUTHORIZATION = "Authorization";
	String UUID = "uuid";
	String INVALID_MOVIE_REQUEST = "Invalid request, please try with valid movie name.";
	String FORBIDDEN = "You don't have access to the requested resource.";
	String BAD_REQUEST = "Bad Request, Please recheck the request.";
	String RELOGIN = "Unauthorized Access, Please relogin.";


	String MOVIE = "movie";
}
