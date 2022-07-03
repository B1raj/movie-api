package com.biraj.backbase.movie.movieapi.service;

import com.biraj.backbase.movie.movieapi.bean.ErrorInfo;
import com.biraj.backbase.movie.movieapi.bean.MovieResponse;
import com.biraj.backbase.movie.movieapi.bean.OmdbResponse;
import com.biraj.backbase.movie.movieapi.constant.MovieConstant;
import com.biraj.backbase.movie.movieapi.constant.MovieErrorCodeConstant;
import com.biraj.backbase.movie.movieapi.entity.Movies;
import com.biraj.backbase.movie.movieapi.exception.BadRequestException;
import com.biraj.backbase.movie.movieapi.repository.MovieRepository;
import com.biraj.backbase.movie.movieapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;


@Service
@Slf4j
public class RatingService {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserRepository userRepository;

    public Object saveRating(double rating) {

      //  userRepository.findByUserId()
        return null;
    }


}
