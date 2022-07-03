package com.biraj.backbase.movie.movieapi.utils;

import com.biraj.backbase.movie.movieapi.bean.AccessToken;
import com.biraj.backbase.movie.movieapi.bean.AccessTokenPayload;
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
		AccessToken accessToken = new AccessToken();
		accessToken.setHeaders(jws.getHeader());
		AccessTokenPayload payload =  new AccessTokenPayload();
		payload.setIssuer(jws.getBody().getIssuer());
		payload.setIssuedDate(jws.getBody().getIssuedAt());
		payload.setPartyId(jws.getBody().getSubject());
		payload.setAudience(jws.getBody().getAudience());
		payload.setUserId((String)jws.getBody().get("userid"));
		
		accessToken.setPayload(payload);
		if(log.isTraceEnabled()){
			log.trace("AccessTokenJwtHandler : onClaimsJws : accessToken : "+ accessToken);
		}
		
		return accessToken;
	}
	
}