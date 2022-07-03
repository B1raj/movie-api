/**
 * 
 */
package com.biraj.backbase.movie.movieapi.bean;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author birajmishra
 *To be used as an input for authenticator.
 */
@Slf4j
@Data
@AllArgsConstructor
@Scope(scopeName="prototype")
public class AuthenticatorRequest {
	private Map<String, String> userCredentials;
}
