package com.biraj.backbase.movie.movieapi.service;

import com.biraj.backbase.movie.movieapi.bean.*;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
@Slf4j
public class RatingService {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    MovieService movieService;

    @Value("${api.top:10}")
    int top;

    /**
     * Save Rating or update the Rating if already exists
     *
     * @param rating
     * @param userId
     * @return
     */


    public Mono<RatingResponse> saveRating(RatingRequest rating, String userId) {
        Optional<Users> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) {
            return Mono.just(RatingResponse.builder().errorInfo(ErrorInfo.builder()
                    .errorMessage(MovieConstant.CANNOT_SAVE_RATING_USER_DOESNOT_EXIST)
                    .errorCode(MovieErrorCodeConstant.BAD_REQUEST_FOR_RATING).build()).build());
        }
        Optional<Movies> movie = movieRepository.findByNameIgnoreCaseAndReleaseYear(rating.getMovie(), rating.getYear());
        if (movie.isEmpty()) {
            return Mono.just(RatingResponse.builder().errorInfo(ErrorInfo.builder()
                    .errorMessage(MovieConstant.CANNOT_SAVE_RATING_MOVIE_NAME_INCORRECT)
                    .errorCode(MovieErrorCodeConstant.BAD_REQUEST_FOR_RATING).build()).build());
        }
        Movies m = movie.get();
        Users u = user.get();
        Optional<MovieRating> ratedMovieOptional = ratingRepository.findByMovieAndUser(m, u);
        Mono<MovieRating> obj;
        if (ratedMovieOptional.isEmpty()) {
            MovieRating save = ratingRepository.save(MovieRating.builder().user(u).movie(m).rating(rating.getRating()).build());
            obj = Mono.just(save);
        } else {
            MovieRating ratedMovie = ratedMovieOptional.get();
            ratedMovie.setRating(rating.getRating());
            obj = Mono.just(ratingRepository.save(ratedMovie));
        }
        return obj.map(o -> RatingResponse.builder().id(o.getId()).rating(o.getRating()).movie(rating.getMovie()).year(rating.getYear()).build());
    }

    /**
     * Get top ten movies
     * Get collection for movie by name and release year
     *
     * @return top ten movies list
     */

    public List<TopMovies> getTop10Movies() {
        Pageable firstPageWithTenElements = PageRequest.of(0, top);
        Optional<List<TopMovies>> objectStringMap = ratingRepository.findTopNByRating(firstPageWithTenElements);
        if (objectStringMap.isPresent()) {
            List<TopMovies> movies = objectStringMap.get();
            movies.forEach(movie -> {
                movie.setBoxOfficeCollection(movieService.getBoxOfficeCollection(movie.getName(), movie.getReleaseYear()));
            });
            return movies.stream().sorted((o1, o2) -> o2.getBoxOfficeCollection().compareTo(o1.getBoxOfficeCollection())).collect(Collectors.toList());
        }
        log.error("Empty top 10 movies returned from db.");
        return List.of();
    }


}
