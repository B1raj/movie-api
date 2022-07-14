package com.biraj.backbase.movie.movieapi.service;

import javax.servlet.http.HttpServletRequest;

import com.biraj.backbase.movie.movieapi.bean.AccessToken;
import com.biraj.backbase.movie.movieapi.bean.AccessTokenPayload;
import com.biraj.backbase.movie.movieapi.bean.UserInfo;
import com.biraj.backbase.movie.movieapi.bean.UserTokens;
import com.biraj.backbase.movie.movieapi.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * @author birajmishra
 */
@Service
@Slf4j
public class AccessFactory {
    @Autowired
    private JwtTokenService tokenUtil;

    @Autowired
    HttpServletRequest request;

    @Value("${accesstoken.issuer}")
    private String accessTokenIssuer;

    @Value("${accesstoken.audience}")
    private String audience;

    @Value("${jwt.secret}")
    private String secret;

    public UserTokens createToken(UserInfo userInfo) {
        // prepare token payload
        AccessTokenPayload payload = computeAccessTokenPayload(userInfo, accessTokenIssuer);
		return UserTokens.builder().accessToken(tokenUtil.generateAccessToken(payload, secret)).build();
    }

    public AccessToken verifyAccessToken(String accessToken) {
        log.info("AccessTokenFactory : verifyAccessToken : verifying access token {}", accessToken);
        return tokenUtil.verifyAccessToken(accessToken, secret);
    }


    private AccessTokenPayload computeAccessTokenPayload(UserInfo userInfo, String iss) {
        return AccessTokenPayload.builder().issuer(iss).issuedDate(DateUtil.currentDate()).audience(audience).userId(userInfo.getUsername()).build();
    }

}
