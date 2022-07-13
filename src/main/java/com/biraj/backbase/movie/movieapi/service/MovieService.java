package com.biraj.backbase.movie.movieapi.service;

import com.biraj.backbase.movie.movieapi.bean.Award;
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

    @Value("${api.key:12ea989a}")
    String apikey;
    @Value("${omdb.url:https://omdbapi.com}")
    String url;

    @Value("${omdb.api.timeout:10}")
    int timeout;

    @Autowired
    MovieRepository movieRepository;

    public Mono<MovieResponse> getMovieInfo(String name, int year) {
        System.out.println("---------------------------------- name " + name);
        return WebClient.create(url)
                .method(HttpMethod.GET)
                .uri(builder -> builder
                        .queryParam("t", name)
                        .queryParam("y", year)
                        .queryParam("apikey", apikey)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::isError, response -> {
                    log.error("OMDB api call resulted in error");
                    return Mono.error(new BadRequestException(MovieErrorCodeConstant.BAD_REQUEST, MovieConstant.BAD_REQUEST));
                }).toEntity(OmdbResponse.class)
                .map(OmdbResponse -> {
                    if (!isAValidMovie(name, year, OmdbResponse)) {
                        return getInvalidMovieResponse(name);
                    }
                    //valid movie since it is present in omdb,check in DB now
                    Optional<Movies> movie = movieRepository.findByNameAndReleaseYear(name, year);
                    if (movie.isPresent()) {
                        return MovieResponse.builder()
                                .name(name).awards(new Award[]{Award.builder().isAwarded(movie.get().isAwarded()).category(movie.get().getCategory()).build()})
                                .boxOfficeCollection(toNumber((OmdbResponse.getBody()).getBoxOffice()))
                                .year(year)
                                .build();
                    }
                    return getInvalidMovieResponse(name);
                });
    }


    public Long getBoxOfficeCollection(String name, int year) {
        System.out.println("---------------------------------- name " + name);
        return WebClient.create(url)
                .method(HttpMethod.GET)
                .uri(builder -> builder
                        .queryParam("t", name)
                        .queryParam("y", year)
                        .queryParam("apikey", apikey)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::isError, response -> {
                    log.error("OMDB api call resulted in error");
                    return Mono.error(new BadRequestException(MovieErrorCodeConstant.BAD_REQUEST, MovieConstant.BAD_REQUEST));
                }).toEntity(OmdbResponse.class)
                .map(OmdbResponse -> {
                    return toNumber(OmdbResponse.getBody().getBoxOffice());
                }).block();
    }

    public Long toNumber(String boxOffice) {
        if ((null != boxOffice && boxOffice.trim().length() > 0)) {
            if (boxOffice.trim().equals("N/A")) return 0L;
            long value = Long.parseLong(boxOffice.replaceAll(",", "").replaceFirst("\\$", ""));
            return value;
        }
        return 0L;
    }


    private boolean isAValidMovie(String name, int year, ResponseEntity<OmdbResponse> OmdbResponse) {
        return Objects.requireNonNull(OmdbResponse.getBody())
                .getResponse()
                .equalsIgnoreCase("True")
                && OmdbResponse.getBody().getTitle().equals(name)
                && OmdbResponse.getBody().getType().equals("movie")
                && (String.valueOf(year).equals(OmdbResponse.getBody().getYear()));
    }

    private MovieResponse getInvalidMovieResponse(String name) {
        return MovieResponse.builder().name(name).errorInfo(ErrorInfo.builder()
                        .errorMessage(MovieConstant.INVALID_MOVIE_REQUEST)
                        .errorCode(MovieErrorCodeConstant.BAD_REQUEST_EXCEPTION)
                        .build())
                .build();
    }
}
