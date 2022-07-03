/**
 * 
 */
package com.biraj.backbase.movie.movieapi.bean;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author birajmishra
 *
 */
@Component
@Scope("prototype")
@Data
@Builder
public class UserTokens {
	private String accessToken;
}
