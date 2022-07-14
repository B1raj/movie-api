/**
 * 
 */
package com.biraj.backbase.movie.movieapi.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author birajmishra
 *To be used as an input for authenticator.
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticatorResponse  {
	private boolean authenticated;
	private UserInfo userInfo;
	private ErrorInfo errorInfo;
}
