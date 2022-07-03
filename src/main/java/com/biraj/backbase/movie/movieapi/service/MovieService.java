package com.biraj.backbase.movie.movieapi.service;

import com.biraj.backbase.movie.movieapi.bean.ErrorInfo;
import com.biraj.backbase.movie.movieapi.bean.MovieResponse;
import com.biraj.backbase.movie.movieapi.bean.OmdbResponse;
import com.biraj.backbase.movie.movieapi.constant.MovieConstant;
import com.biraj.backbase.movie.movieapi.constant.MovieErrorCodeConstant;
import com.biraj.backbase.movie.movieapi.entity.Movies;
import com.biraj.backbase.movie.movieapi.exception.BadRequestException;
import com.biraj.backbase.movie.movieapi.repository.MovieRepository;
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
public class MovieService {

    @Value("${api.key}")
    String apikey;
    @Value("${omdb.url}")
    String url;

    @Value("${omdb.api.timeout:10}")
    int timeout;

    @Autowired
    MovieRepository movieRepository;

    public Mono<MovieResponse> getMovieInfo(String name) {
        return WebClient.create(url)
                .method(HttpMethod.GET)
                .uri(builder -> builder
                        .queryParam("t", name)
                        .queryParam("apikey", apikey)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::isError, response -> {
                    log.error("OMDB api call resulted in error");
                    return Mono.error(new
                            BadRequestException(MovieErrorCodeConstant.BAD_REQUEST, MovieConstant.BAD_REQUEST));
                }).toEntity(OmdbResponse.class)
                .map(OmdbResponse -> {
                    if (!isAMovie(name, OmdbResponse)) {
                        return getInvalidMovieResponse(name);
                    }
                    //valid movie since it is present in omdb,check in DB now
                    Optional<Movies> movie = movieRepository.findByName(name);
                    if (movie.isPresent()) {
                        return MovieResponse.builder()
                                .name(name)
                                .isOscarAwarded(movie.get().isAwarded())
                                .category(movie.get().getCategory())
                                .build();
                    }
                    return getInvalidMovieResponse(name);
                });
    }

    private boolean isAMovie(String name, ResponseEntity<OmdbResponse> OmdbResponse) {
        return Objects.requireNonNull(OmdbResponse.getBody())
                .getResponse()
                .equalsIgnoreCase("True") && OmdbResponse.getBody().getTitle().equals(name);
    }

    private MovieResponse getInvalidMovieResponse(String name) {
        return MovieResponse.builder().name(name).errorInfo(ErrorInfo.builder()
                        .errorMessage(MovieConstant.INVALID_MOVIE_REQUEST)
                        .errorCode(MovieErrorCodeConstant.BAD_REQUEST_EXCEPTION)
                        .build())
                .build();
    }
}
