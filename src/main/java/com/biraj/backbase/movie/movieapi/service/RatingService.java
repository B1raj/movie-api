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

    /*
    Save Rating or update the Rating if already exists
     */
    public Mono<RatingResponse> saveRating(RatingRequest rating, String userId) {
        Optional<Users> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) {
            return Mono.just(RatingResponse.builder().errorInfo(ErrorInfo.builder()
                    .errorMessage(MovieConstant.CANNOT_SAVE_RATING_USER_DOESNOT_EXIST)
                    .errorCode(MovieErrorCodeConstant.BAD_REQUEST_FOR_RATING).build()).build());
        }
        Optional<Movies> movie = movieRepository.findByNameAndReleaseYear(rating.getMovie(),rating.getYear());
        if (movie.isEmpty()) {
            return Mono.just(RatingResponse.builder().errorInfo(ErrorInfo.builder()
                    .errorMessage(MovieConstant.CANNOT_SAVE_RATING_MOVIE_NAME_INCORRECT)
                    .errorCode(MovieErrorCodeConstant.BAD_REQUEST_FOR_RATING).build()).build());
        }
        Movies m = movie.get();
        Users u = user.get();
        Optional<MovieRating> ratedMovieOptional = ratingExists(m, u);
        Mono<MovieRating> obj;
        if (ratedMovieOptional.isEmpty()) {
            obj = Mono.just(ratingRepository.save(MovieRating.builder().user(u).movie(m).rating(rating.getRating()).build()));
        } else {
            MovieRating ratedMovie = ratedMovieOptional.get();
            ratedMovie.setRating(rating.getRating());
            obj = Mono.just(ratingRepository.save(ratedMovie));
        }
        return obj.map(o -> RatingResponse.builder().rating(o.getRating()).movie(rating.getMovie()).year(rating.getYear()).build());
    }

    private Optional<MovieRating> ratingExists(Movies movie, Users userId) {
        return ratingRepository.findByMovieAndUser(movie, userId);
    }

    public List<TopMovies> getTop10Movies() {
        Pageable firstPageWithTenElements = PageRequest.of(0, top);
        Optional<List<TopMovies>> objectStringMap = ratingRepository.findTopNByRating(firstPageWithTenElements);
        if (objectStringMap.isPresent()) {
            List<TopMovies> movies = objectStringMap.get();
            movies.forEach(movie->{
                movie.setBoxOfficeCollection(getCollection(movie.getName(),movie.getReleaseYear()));
            });
          return movies.stream().sorted((o1, o2) -> o2.getBoxOfficeCollection().compareTo(o1.getBoxOfficeCollection())).collect(Collectors.toList());
        }
        log.error("Empty top 10 movies returned from db.");
        return List.of();
    }

    private static <T> Predicate<T> distinctByName(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private Long getCollection(String movie, int year) {
        return movieService.getBoxOfficeCollection(movie,year);
    }
}
