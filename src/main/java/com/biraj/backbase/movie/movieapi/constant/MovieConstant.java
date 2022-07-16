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
	String USERID = "userId";
	String INVALID_MOVIE_REQUEST = "Invalid request, please try with valid movie name and year.";
	String BAD_REQUEST = "Bad Request, Please recheck the request.";
	String RELOGIN = "Unauthorized Access, Please re-login.";
	String MOVIE = "movie";
	String RATING_VALIDATION = "rating value has to be a number below 10";
	String YEAR = "year";
	String CANNOT_SAVE_RATING_MOVIE_NAME_INCORRECT = "Bad Request, Cannot Save Rating: Movie name or year is incorrect";
	String CANNOT_SAVE_RATING_ALREADY_EXISTS = "Bad Request, Movie is already rated by the user";

	String CANNOT_UPDATE_RATING_DOES_NOT_EXISTS = "Bad Request, Movie is not yet rated by the user";
	String CANNOT_SAVE_RATING_USER_DOES_NOT_EXIST = "Bad Request, Cannot Save Rating: User does not exist";
}
