package com.biraj.backbase.movie.movieapi.controller;

import com.biraj.backbase.movie.movieapi.bean.*;
import com.biraj.backbase.movie.movieapi.constant.MovieConstant;
import com.biraj.backbase.movie.movieapi.service.AccessFactory;
import com.biraj.backbase.movie.movieapi.service.AuthenticatorService;
import com.biraj.backbase.movie.movieapi.service.MovieService;
import com.biraj.backbase.movie.movieapi.service.RatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


import static com.biraj.backbase.movie.movieapi.constant.MovieConstant.RATING_VALIDATION;
import static com.biraj.backbase.movie.movieapi.constant.MovieErrorCodeConstant.BAD_REQUEST;

/**
 * @author birajmishra
 * Controller, this is where request lands.
 */

@SuppressWarnings("rawtypes")
@RestController
@Slf4j
@RequestMapping(value = "/v1/api")
public class MovieApiController {

    @Autowired
    private AccessFactory tokenFactory;
    @Autowired
    AuthenticatorService authenticatorService;
    @Autowired
    MovieService movieService;

    @Autowired
    RatingService ratingService;

    @PostMapping(value = "/login")
    private Mono<ResponseEntity<LoginResponse>> login(HttpServletRequest request,
                                                @RequestHeader(value = MovieConstant.UUID) String uuid,
                                                @RequestHeader(value = MovieConstant.AUTHORIZATION) String authorization) {

        // 1. verify credentials
        AuthenticatorResponse response = authenticatorService.authenticate(authorization);
        if(response.isAuthenticated()){
            // 2. generate JWT
            UserTokens userToken = tokenFactory.createToken(response.getUserInfo());
            log.info("User Token's successfully created.");
            if (log.isTraceEnabled()) log.trace("MovieApiController :  login : userToken : " + userToken);
            return  Mono.just(new ResponseEntity<>(LoginResponse.builder().accessToken(userToken.getAccessToken()).build(), HttpStatus.CREATED));
        }
        return  Mono.just(new ResponseEntity<>(LoginResponse.builder().errorInfo(response.getErrorInfo()).build(), HttpStatus.UNAUTHORIZED));
    }


    @GetMapping(value = "/movie")
    private Mono<ResponseEntity> movie(HttpServletRequest request,
                                       @RequestHeader(value = MovieConstant.UUID) String uuid,
                                       @RequestHeader(value = MovieConstant.ACCESS_TOKEN) String accessTokenString,
                                       @RequestHeader(value = MovieConstant.MOVIE) String movie,
                                       @RequestHeader(value = MovieConstant.YEAR) int year) {
        return movieService.getMovieInfo(movie, year).map(m -> {
            if (null != m.getErrorInfo()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(m);
            }
            return ResponseEntity.status(HttpStatus.OK).body(m);
        }).cast(ResponseEntity.class);
    }

    @PostMapping(value = "/rating")
    private Mono<ResponseEntity> saveRating(HttpServletRequest request,
                                            @RequestHeader(value = MovieConstant.UUID) String uuid,
                                            @RequestHeader(value = MovieConstant.ACCESS_TOKEN) String accessTokenString,
                                            @RequestBody RatingRequest rating) {
        AccessToken accessToken = (AccessToken) request.getAttribute(MovieConstant.ACCESS_TOKEN);

        Mono<ResponseEntity> validated = validate(rating);
        if (validated != null) return validated;
        Mono<RatingResponse> ratingResponseMono = ratingService.saveRating(rating, accessToken.getPayload().getUserId());

        return ratingResponseMono.map(m -> {
            if (null != m.getErrorInfo()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(m);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(m);
        }).cast(ResponseEntity.class);
    }

    @PutMapping(value = "/rating")
    private Mono<ResponseEntity> updateRating(HttpServletRequest request,
                                            @RequestHeader(value = MovieConstant.UUID) String uuid,
                                            @RequestHeader(value = MovieConstant.ACCESS_TOKEN) String accessTokenString,
                                            @RequestBody RatingRequest rating) {
        AccessToken accessToken = (AccessToken) request.getAttribute(MovieConstant.ACCESS_TOKEN);

        Mono<ResponseEntity> validated = validate(rating);
        if (validated != null) return validated;
        Mono<RatingResponse> ratingResponseMono = ratingService.updateRating(rating, accessToken.getPayload().getUserId());

        return ratingResponseMono.map(m -> {
            if (null != m.getErrorInfo()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(m);
            }
            return ResponseEntity.status(HttpStatus.OK).body(m);
        }).cast(ResponseEntity.class);
    }

    private Mono<ResponseEntity> validate(RatingRequest rating) {
        if(rating.getRating()>10){
            ErrorInfo e =  ErrorInfo.builder().errorMessage(RATING_VALIDATION).errorCode(BAD_REQUEST).build();
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e));
        }
        return null;
    }

    @GetMapping(value = "/top10")
    private Mono<ResponseEntity> top10Movies(HttpServletRequest request,
                                       @RequestHeader(value = MovieConstant.UUID) String uuid,
                                       @RequestHeader(value = MovieConstant.ACCESS_TOKEN) String accessTokenString) {


        Mono<List<TopMovies>> movieInfo = Mono.just(ratingService.getTop10Movies());

        return movieInfo.map(m -> ResponseEntity.status(HttpStatus.OK).body(m))
                .cast(ResponseEntity.class);
    }
}
