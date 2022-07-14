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
import org.springframework.cache.annotation.Cacheable;
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

    @Autowired
    MovieRepository movieRepository;

    /**
     * Gets movie info i.e. if it won oscar for Best picture category or not.
     *
     * @param name
     * @param year
     * @return
     */

    @Cacheable(value = "movieResponse", key = "{#name, #year}")
    public Mono<MovieResponse> getMovieInfo(String name, int year) {
        log.info("getting movie details from omdb for " + name);
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
                    Optional<Movies> movie = movieRepository.findByNameIgnoreCaseAndReleaseYear(name, year);
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

    /**
     * Get box office collection by calling OMDB API
     *
     * @param name
     * @param year
     * @return
     */
    @Cacheable(value = "collection", key = "{#movie, #year}")
    public Long getBoxOfficeCollection(String name, int year) {
     log.info("Getting box office collection for name " + name);
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
        return null != OmdbResponse && null != OmdbResponse.getBody() && OmdbResponse.getBody()
                .getResponse()
                .equalsIgnoreCase("True")
                && OmdbResponse.getBody().getTitle().equalsIgnoreCase(name)
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
