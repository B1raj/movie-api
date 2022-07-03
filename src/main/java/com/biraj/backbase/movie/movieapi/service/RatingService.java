package com.biraj.backbase.movie.movieapi.service;

import com.biraj.backbase.movie.movieapi.bean.ErrorInfo;
import com.biraj.backbase.movie.movieapi.bean.RatingResponse;
import com.biraj.backbase.movie.movieapi.constant.MovieConstant;
import com.biraj.backbase.movie.movieapi.constant.MovieErrorCodeConstant;
import com.biraj.backbase.movie.movieapi.entity.Movies;
import com.biraj.backbase.movie.movieapi.entity.Rating;
import com.biraj.backbase.movie.movieapi.entity.Users;
import com.biraj.backbase.movie.movieapi.repository.MovieRepository;
import com.biraj.backbase.movie.movieapi.repository.RatingRepository;
import com.biraj.backbase.movie.movieapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Service
@Slf4j
public class RatingService {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RatingRepository ratingRepository;

    public Rating saveRating(double rating, String userId, String name) {
        Users user = userRepository.findByUserId(userId).get();
        Optional<Movies> movie = movieRepository.findByName(name);
        return movie.map(movies -> ratingRepository.save(Rating.builder().user(user).movie(movies).rating(rating).build())).orElseGet(() -> Rating.builder().build());
    }

    public Mono<RatingResponse> createRatingResponse(Rating rating) {

        if (null == rating.getId()) {
            return Mono.just(RatingResponse.builder().errorInfo(ErrorInfo.builder()
                    .errorMessage(MovieConstant.CANNOT_SAVE_RATING)
                    .errorCode(MovieErrorCodeConstant.BAD_REQUEST).build()).build());
        }
        return Mono.just(RatingResponse.builder().rating(rating).build());
    }
}
