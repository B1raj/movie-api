package com.biraj.backbase.movie.movieapi.controller;

import com.biraj.backbase.movie.movieapi.bean.*;
import com.biraj.backbase.movie.movieapi.constant.MovieConstant;
import com.biraj.backbase.movie.movieapi.service.AccessFactory;
import com.biraj.backbase.movie.movieapi.service.AuthenticatorService;
import com.biraj.backbase.movie.movieapi.service.MovieService;
import com.biraj.backbase.movie.movieapi.service.RatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author birajmishra
 * Controller, this is where request lands.
 */


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
    private ResponseEntity<LoginResponse> login(HttpServletRequest request,
                                                @RequestHeader(value = MovieConstant.UUID) String uuid,
                                                @RequestHeader(value = MovieConstant.AUTHORIZATION) String authorization) {

        // 1. verify credentials
        AuthenticatorResponse response = authenticatorService.authenticate(authorization);
        // 2. generate JWT
        UserTokens userToken = tokenFactory.createToken(response.getUserInfo());
        log.info("User Token's successfully created.");
        if (log.isTraceEnabled()) log.trace("MovieApiController :  login : userToken : " + userToken);
        // 3. return
        return new ResponseEntity(LoginResponse.builder().accessToken(userToken.getAccessToken()).build(), HttpStatus.CREATED);
    }


    @GetMapping(value = "/movie")
    private Mono<ResponseEntity> movie(HttpServletRequest request,
                                       @RequestHeader(value = MovieConstant.UUID) String uuid,
                                       @RequestHeader(value = MovieConstant.ACCESS_TOKEN) String accessTokenString,
                                       @RequestHeader(value = MovieConstant.MOVIE) String movie,
                                       @RequestHeader(value = MovieConstant.YEAR) int year) {
        return movieService.getMovieInfo(movie, year).map(m -> ResponseEntity.status(HttpStatus.OK).body(m))
                .cast(ResponseEntity.class);
    }

    @PostMapping(value = "/rating")
    private Mono<ResponseEntity> saveRating(HttpServletRequest request,
                                            @RequestHeader(value = MovieConstant.UUID) String uuid,
                                            @RequestHeader(value = MovieConstant.ACCESS_TOKEN) String accessTokenString,
                                            @RequestBody RatingRequest rating) {
        AccessToken accessToken = (AccessToken) request.getAttribute(MovieConstant.ACCESS_TOKEN);
        Mono<RatingResponse> ratingResponseMono = ratingService.saveRating(rating, accessToken.getPayload().getUserId());

        return ratingResponseMono.map(m -> ResponseEntity.status(HttpStatus.OK).body(m))
                .cast(ResponseEntity.class);
    }

    @GetMapping(value = "/top10")
    private Mono<ResponseEntity> top10(HttpServletRequest request,
                                       @RequestHeader(value = MovieConstant.UUID) String uuid,
                                       @RequestHeader(value = MovieConstant.ACCESS_TOKEN) String accessTokenString) {
        Mono<List<TopMovies>> movieInfo = Mono.just(ratingService.getTop10Movies());

        return movieInfo.map(m -> ResponseEntity.status(HttpStatus.OK).body(m))
                .cast(ResponseEntity.class);
    }
}
