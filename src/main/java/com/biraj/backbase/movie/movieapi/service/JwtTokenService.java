package com.biraj.backbase.movie.movieapi.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import com.biraj.backbase.movie.movieapi.bean.AccessToken;
import com.biraj.backbase.movie.movieapi.bean.AccessTokenPayload;
import com.biraj.backbase.movie.movieapi.constant.MovieConstant;
import com.biraj.backbase.movie.movieapi.constant.MovieErrorCodeConstant;
import com.biraj.backbase.movie.movieapi.exception.AccessTokenException;
import com.biraj.backbase.movie.movieapi.utils.DateUtil;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;


@Service
public class JwtTokenService {
	private static final Logger LOG = LoggerFactory.getLogger(JwtTokenService.class);

	public static final String JWT_TYPE = "typ";
	public static final String JWT_VALUE = "JWT";

	@Autowired
	private JwtHandler handler;

	@Value("${accesstoken.algorithm}")
	private String algorithm;

	@Value("${accesstoken.exp:30}")
	private Long exp;

	public AccessToken verifyAccessToken(String token, String secret) throws AccessTokenException {
		AccessToken accessToken = new AccessToken();
		try {
			LOG.info("JwtTokenUtil : verifyAccessToken : verifying access token with secret");
			accessToken =(AccessToken)Jwts.parser().setSigningKey(secret).parse(token, handler);
		}  catch (MalformedJwtException exp) {
			LOG.error("JwtTokenUtil : verifyAccessToken : MalformedJwtException - access token malformed", exp);
			throw new AccessTokenException(MovieErrorCodeConstant.ACCESS_TOKEN_MALFORMED, exp.getMessage());
		} catch (SignatureException exp) {
			LOG.error("JwtTokenUtil : verifyAccessToken : SignatureException - access token Signature", exp);
			throw new AccessTokenException(MovieErrorCodeConstant.ACCESS_TOKEN_ILLEGAL_ARGUMENT, exp.getMessage());
		} catch (IllegalArgumentException exp) {
			LOG.error("JwtTokenUtil : verifyAccessToken : IllegalArgumentException - access token Illegal Argument", exp);
			throw new AccessTokenException(MovieErrorCodeConstant.ACCESS_TOKEN_ILLEGAL_ARGUMENT, exp.getMessage());
		}
		return accessToken;
	}

	public String generateAccessToken(AccessTokenPayload accessTokenPayload, String secret) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("JwtTokenUtil : generateAccessToken : accessTokenPayload : "
					+ accessTokenPayload + "   secret : " + secret);
		}
		Claims claims = Jwts.claims();
		claims.setExpiration(java.util.Date.from(Instant.now().plus(exp, ChronoUnit.MINUTES)));
		claims.setIssuer(accessTokenPayload.getIssuer());
		claims.setIssuedAt(accessTokenPayload.getIssuedDate());
		claims.setSubject(accessTokenPayload.getPartyId());
		claims.setAudience(accessTokenPayload.getAudience());
		claims.put(MovieConstant.USERID,accessTokenPayload.getUserId());
		String accessToken = generateTokens(claims, secret);
		if (LOG.isTraceEnabled()) {
			LOG.trace("JwtTokenUtil : generateAccessToken : accessToken : " + accessToken);
		}
		return accessToken;
	}

	private String generateTokens(Map<String, Object> claims,String secret) {
		return Jwts.builder().setHeaderParam(JWT_TYPE, JWT_VALUE).setClaims(claims)
				.signWith(SignatureAlgorithm.valueOf(algorithm), secret).compact();
	}


	
}