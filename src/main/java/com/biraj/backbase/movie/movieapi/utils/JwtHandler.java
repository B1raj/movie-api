package com.biraj.backbase.movie.movieapi.utils;

import com.biraj.backbase.movie.movieapi.bean.AccessToken;
import com.biraj.backbase.movie.movieapi.bean.AccessTokenPayload;
import com.biraj.backbase.movie.movieapi.constant.MovieConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtHandlerAdapter;
/**
 * @author birajmishra
 */
@Component
@Slf4j
public class JwtHandler extends JwtHandlerAdapter<AccessToken> {

	protected static final String PARTY_ID = "ptyid";
	
	@Override
	public AccessToken onClaimsJws(Jws<Claims> jws) {
		if(log.isTraceEnabled()){
			log.trace("AccessTokenJwtHandler : onClaimsJws : start ");
		}
		AccessTokenPayload payload = AccessTokenPayload.builder().issuer(jws.getBody().getIssuer())
				.issuedDate(jws.getBody().getIssuedAt())
				.audience(jws.getBody().getAudience())
				.userId((String)jws.getBody().get(MovieConstant.USERID))
				.build();
		return AccessToken.builder().payload(payload).headers(jws.getHeader()).build();
	}
	
}