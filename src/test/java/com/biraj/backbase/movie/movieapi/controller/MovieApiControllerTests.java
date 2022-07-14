package com.biraj.backbase.movie.movieapi.controller;

import com.biraj.backbase.movie.movieapi.bean.*;
import com.biraj.backbase.movie.movieapi.constant.MovieConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles({"test"})
public class MovieApiControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private static final String BASE_URL = "http://localhost:";
    private static final String AT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTc4MTE1MzEsImlzcyI6Imh0dHBzOi8vYmlyYWouYXV0aC5zZXJ2aWNlLmNvbSIsImlhdCI6MTY1NzgwOTczMSwiYXVkIjoiaHR0cHM6Ly9iaXJhai5tb3ZpZS5hcGkuY29tL2FjY2VzcyIsInVzZXJJZCI6Im1pa2VAZG9lLmNvbSJ9.w_f3iKlB1kX99etgt2mKUp3vBfGYC25Q71POFKXJtVc";


    @Test
    void login_givenValidUserDetails_shouldPerformSuccessfulLogin() {
        ResponseEntity<LoginResponse> response = performLogin();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNotNull(Objects.requireNonNull(response.getBody()));
        assertNotNull(Objects.requireNonNull(response.getBody().getAccessToken()));
    }


    ResponseEntity<LoginResponse> performLogin() {
        String url = BASE_URL.concat((String.valueOf(port)).concat("/v1/api/login"));
        HttpHeaders headers = new HttpHeaders();
        headers.set(MovieConstant.UUID, UUID.randomUUID().toString());
        headers.set(MovieConstant.AUTHORIZATION, "Basic am9obkBkb2UuY29tOmpvaG4=");
        HttpEntity request = new HttpEntity<>(headers);
        return restTemplate.postForEntity(url, request, LoginResponse.class);
    }

    @Test
    void movie_givenValidMovieNameAndYear_ShouldReturnIfMovieGotOscar() {
        String url = BASE_URL.concat((String.valueOf(port)).concat("/v1/api/movie"));
        HttpHeaders headers = new HttpHeaders();
        headers.set(MovieConstant.UUID, UUID.randomUUID().toString());
        ResponseEntity<LoginResponse> response1 = performLogin();
        assertNotNull(response1.getBody());
        headers.set(MovieConstant.ACCESS_TOKEN, response1.getBody().getAccessToken());
        headers.set(MovieConstant.MOVIE, "Titanic");
        headers.set(MovieConstant.YEAR, "1997");
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<MovieResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, MovieResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getAwards());
        assertTrue(response.getBody().getAwards().length > 0);
        Arrays.stream(response.getBody().getAwards()).forEach((e) -> {
            if (e.getCategory().equals("Best Picture")) {
                assertTrue(e.getIsAwarded());
            }
        });
    }

    @Test
    void movie_givenValidAccessTokenAndInvalidMovieNameOrYear_ShouldReturnValidationError() {
        String url = BASE_URL.concat((String.valueOf(port)).concat("/v1/api/movie"));
        HttpHeaders headers = new HttpHeaders();
        headers.set(MovieConstant.UUID, UUID.randomUUID().toString());
        ResponseEntity<LoginResponse> response1 = performLogin();
        assertNotNull(response1.getBody());
        headers.set(MovieConstant.ACCESS_TOKEN, response1.getBody().getAccessToken());
        headers.set(MovieConstant.MOVIE, "Titanic");
        headers.set(MovieConstant.YEAR, "1700");
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<MovieResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, MovieResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getErrorInfo());
        assertNotNull(response.getBody().getErrorInfo().getErrorCode());
        assertEquals(response.getBody().getErrorInfo().getErrorCode(), "40001");
    }

    @Test
    void movie_givenInValidAccessToken_ShouldReturnError() {
        String url = BASE_URL.concat((String.valueOf(port)).concat("/v1/api/movie"));
        HttpHeaders headers = new HttpHeaders();
        headers.set(MovieConstant.UUID, UUID.randomUUID().toString());
        headers.set(MovieConstant.ACCESS_TOKEN, "Invalid AT");
        headers.set(MovieConstant.MOVIE, "Titanic");
        headers.set(MovieConstant.YEAR, "1997");
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<MovieResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, MovieResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void saveRating_givenValidAccessTokenAndValidMovieNameAndYearAndRating_ShouldDaveRatingSuccessfully() {
        String url = BASE_URL.concat((String.valueOf(port)).concat("/v1/api/rating"));
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<LoginResponse> response = performLogin();
        assertNotNull(response.getBody());
        headers.set(MovieConstant.ACCESS_TOKEN, response.getBody().getAccessToken());
        headers.set(MovieConstant.UUID, UUID.randomUUID().toString());
        HttpEntity request = new HttpEntity<>(RatingRequest.builder().rating(100.2).movie("Milk").year(2008).build(), headers);
        ResponseEntity<RatingResponse> ratingResponse = restTemplate.exchange(url, HttpMethod.POST, request, RatingResponse.class);
        assertThat(ratingResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNotNull(ratingResponse.getBody());
        assertNotNull(ratingResponse.getBody().getId());
    }

    @Test
    void saveRating_givenValidAccessTokenAndInvalidMovieNameOrYearAndValidRating_ShouldReturnValidationError() {
        String url = BASE_URL.concat((String.valueOf(port)).concat("/v1/api/movie"));
        HttpHeaders headers = new HttpHeaders();
        headers.set(MovieConstant.UUID, UUID.randomUUID().toString());
        ResponseEntity<LoginResponse> response1 = performLogin();
        assertNotNull(response1.getBody());
        headers.set(MovieConstant.ACCESS_TOKEN, response1.getBody().getAccessToken());
        headers.set(MovieConstant.MOVIE, "Titanic");
        headers.set(MovieConstant.YEAR, "1700");
        HttpEntity request = new HttpEntity<>(RatingRequest.builder().rating(100.2).movie("Milk").year(2000).build(), headers);
        ResponseEntity<MovieResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, MovieResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getErrorInfo());
        assertNotNull(response.getBody().getErrorInfo().getErrorCode());
        assertEquals(response.getBody().getErrorInfo().getErrorCode(), "40001");
    }

    @Test
    void top10movies_givenValidAccessToken_ShouldReturnTop10Movies() {
        String url = BASE_URL.concat((String.valueOf(port)).concat("/v1/api/top10"));
        HttpHeaders headers = new HttpHeaders();
        headers.set(MovieConstant.UUID, UUID.randomUUID().toString());
        ResponseEntity<LoginResponse> tokenResponse = performLogin();
        assertNotNull(tokenResponse.getBody());
        headers.set(MovieConstant.ACCESS_TOKEN, tokenResponse.getBody().getAccessToken());
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<List> responseEntityResponseEntity = restTemplate.exchange(url, HttpMethod.GET, request, List.class);
        assertEquals(responseEntityResponseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntityResponseEntity.getBody());
        assertFalse(responseEntityResponseEntity.getBody().isEmpty());
        assertEquals(responseEntityResponseEntity.getBody().size(), 10);
    }
}
