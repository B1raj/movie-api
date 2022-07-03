package com.biraj.backbase.movie.movieapi.service;

import com.biraj.backbase.movie.movieapi.bean.ErrorInfo;
import com.biraj.backbase.movie.movieapi.bean.RatingResponse;
import com.biraj.backbase.movie.movieapi.constant.MovieConstant;
import com.biraj.backbase.movie.movieapi.constant.MovieErrorCodeConstant;
import com.biraj.backbase.movie.movieapi.entity.Movies;
import com.biraj.backbase.movie.movieapi.entity.MovieRating;
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

    public Mono<RatingResponse> saveRating(double rating, String userId, String name) {
        Users user = userRepository.findByUserId(userId).get();
        Optional<Movies> movie = movieRepository.findByName(name);
        if (movie.isEmpty()) {
           return Mono.just(RatingResponse.builder().errorInfo(ErrorInfo.builder()
                    .errorMessage(MovieConstant.CANNOT_SAVE_RATING)
                    .errorCode(MovieErrorCodeConstant.BAD_REQUEST).build()).build());
        }
        Mono<MovieRating> obj = Mono.just(ratingRepository.save(MovieRating.builder().user(user).movie(movie.get()).rating(rating).build()));
        return obj.map(o -> RatingResponse.builder().movieRating(MovieRating.builder().rating(o.getRating()).build()).build());
    }

}
