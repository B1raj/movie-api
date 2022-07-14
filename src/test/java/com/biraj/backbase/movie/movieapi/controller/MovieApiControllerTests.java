package com.biraj.backbase.movie.movieapi.controller;

import com.biraj.backbase.movie.movieapi.bean.LoginResponse;
import com.biraj.backbase.movie.movieapi.bean.MovieResponse;
import com.biraj.backbase.movie.movieapi.constant.MovieConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles({ "test" })
public class MovieApiControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private static final String BASE_URL = "http://localhost:";
    private static final String AT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTc4MTE1MzEsImlzcyI6Imh0dHBzOi8vYmlyYWouYXV0aC5zZXJ2aWNlLmNvbSIsImlhdCI6MTY1NzgwOTczMSwiYXVkIjoiaHR0cHM6Ly9iaXJhai5tb3ZpZS5hcGkuY29tL2FjY2VzcyIsInVzZXJJZCI6Im1pa2VAZG9lLmNvbSJ9.w_f3iKlB1kX99etgt2mKUp3vBfGYC25Q71POFKXJtVc";


    @Test
    void givenValidUserDetails_shouldPerformSuccessfulLogin() {
        String url = BASE_URL.concat((String.valueOf(port)).concat("/v1/api/login"));
        HttpHeaders headers = new HttpHeaders();
        headers.set(MovieConstant.UUID, UUID.randomUUID().toString());
        headers.set(MovieConstant.AUTHORIZATION, "Basic am9obkBkb2UuY29tOmpvaG4=");
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(url, request, LoginResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody().getAccessToken()));
    }


    String performLogin() {
        String url = BASE_URL.concat((String.valueOf(port)).concat("/v1/api/login"));
        HttpHeaders headers = new HttpHeaders();
        headers.set(MovieConstant.UUID, UUID.randomUUID().toString());
        headers.set(MovieConstant.AUTHORIZATION, "Basic am9obkBkb2UuY29tOmpvaG4=");
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(url, request, LoginResponse.class);
        return response.getBody().getAccessToken();
    }

    @Test
    void givenValidMovieNameAndYear_ShouldReturnIfMovieGotOscar() {
        String url = BASE_URL.concat((String.valueOf(port)).concat("/v1/api/movie"));
        HttpHeaders headers = new HttpHeaders();
        headers.set(MovieConstant.UUID, UUID.randomUUID().toString());
        headers.set(MovieConstant.ACCESS_TOKEN, performLogin());
        headers.set(MovieConstant.MOVIE, "Titanic");
        headers.set(MovieConstant.YEAR, "1997");
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<MovieResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, MovieResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getAwards());
        assertThat(response.getBody().getAwards().length>0);
        Arrays.stream(response.getBody().getAwards()).forEach((e)->{
            if(e.getCategory().equals("Best Picture")){
                assertThat(e.getIsAwarded().equals(Boolean.TRUE));
            }
        });
    }

    @Test
    void givenValidAccessTokenAndInvalidMovieNameOrYear_ShouldReturnValidationError() {
        String url = BASE_URL.concat((String.valueOf(port)).concat("/v1/api/movie"));
        HttpHeaders headers = new HttpHeaders();
        headers.set(MovieConstant.UUID, UUID.randomUUID().toString());
        headers.set(MovieConstant.ACCESS_TOKEN, performLogin());
        headers.set(MovieConstant.MOVIE, "Titanic");
        headers.set(MovieConstant.YEAR, "1700");
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<MovieResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, MovieResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getErrorInfo());
        assertNotNull(response.getBody().getErrorInfo().getErrorCode());
    }

    @Test
    void givenInValidAccessToken_ShouldReturnError() {
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

}
