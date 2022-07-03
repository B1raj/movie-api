package com.biraj.backbase.movie.movieapi.bean;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author birajmishra
 * Access token class defining structure of token, which secures API's
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessToken {
	private Map<String, Object> headers;
	private AccessTokenPayload payload;
}
