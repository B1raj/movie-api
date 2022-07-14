package com.biraj.backbase.movie.movieapi.service;

import com.biraj.backbase.movie.movieapi.bean.AuthenticatorResponse;
import com.biraj.backbase.movie.movieapi.bean.ErrorInfo;
import com.biraj.backbase.movie.movieapi.bean.UserInfo;
import com.biraj.backbase.movie.movieapi.constant.MovieConstant;
import com.biraj.backbase.movie.movieapi.constant.MovieErrorCodeConstant;
import com.biraj.backbase.movie.movieapi.entity.Users;
import com.biraj.backbase.movie.movieapi.repository.UserRepository;
import com.biraj.backbase.movie.movieapi.utils.CrypticUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * @author birajmishra
 */

@Service
@Slf4j
public class AuthenticatorService {

	@Autowired
	UserRepository userRepository;

	/**
	 * validate user from db
	 * @param authorization : Base64 Encoded Username and password
	 * @return AuthenticatorResponse : AuthenticatorResponse
	 */

	public AuthenticatorResponse authenticate(String authorization) {
		if (log.isTraceEnabled()) {
			log.trace("AuthenticatorServiceImpl : authenticate : Start");
		}
		String[] authParts = authorization.split("\\s+");
		if (authParts.length != 2) {
			//throw new BadRequestException(MovieErrorCodeConstant.BAD_REQUEST, MovieConstant.BAD_REQUEST);
			return AuthenticatorResponse.builder().authenticated(false).errorInfo(ErrorInfo.builder().errorCode(MovieErrorCodeConstant.BAD_REQUEST).errorMessage(MovieConstant.BAD_REQUEST).build()).build();

		}
		String decodedAuth = CrypticUtil.decrypt(authParts[1]);
		AuthenticatorResponse response;

		String userid = decodedAuth.split(":")[0];
		String password = decodedAuth.split(":")[1];

		Optional<Users> userObject = userRepository.findByUserId(userid);

		if(userObject.isEmpty()){
			log.error("Authentication failed for provided {}", userid);
			//throw new AuthenticationException(MovieErrorCodeConstant.UNABLE_TO_AUTHENTICATE,
			//		MovieConstant.UNABLE_TO_AUTHENTICATE);
			return AuthenticatorResponse.builder().authenticated(false).errorInfo(ErrorInfo.builder().errorCode(MovieErrorCodeConstant.UNABLE_TO_AUTHENTICATE).errorMessage(MovieConstant.UNABLE_TO_AUTHENTICATE).build()).build();


		}
		Users user = userObject.get();
		if (userid.equals(user.getUserId()) && password.equals(user.getPassword())) {
			// valid user
			if (log.isTraceEnabled()) log.trace("****LOGIN SUCCESSFUL **** for user{}", userid);
			return AuthenticatorResponse.builder().authenticated(true).userInfo(UserInfo.builder().username(user.getUserId()).build()).build();

		} else {
			// invalid user
			log.error("Authentication failed for provided {}", userid);
			//throw new AuthenticationException(MovieErrorCodeConstant.UNABLE_TO_AUTHENTICATE,
			//		MovieConstant.UNABLE_TO_AUTHENTICATE);
			return AuthenticatorResponse.builder().authenticated(false).errorInfo(ErrorInfo.builder().errorCode(MovieErrorCodeConstant.UNABLE_TO_AUTHENTICATE).errorMessage(MovieConstant.UNABLE_TO_AUTHENTICATE).build()).build();

		}

	}

}
