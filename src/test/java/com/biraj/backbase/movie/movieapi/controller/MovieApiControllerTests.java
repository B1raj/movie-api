package com.biraj.backbase.movie.movieapi.controller;

import com.biraj.backbase.movie.movieapi.bean.LoginResponse;
import com.biraj.backbase.movie.movieapi.constant.MovieConstant;
import com.biraj.backbase.movie.movieapi.exception.AuthenticationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class MovieApiControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private static final String BASE_URL = "http://localhost:";

    @Test
    void givenValidUserDetails_shouldPerformSuccessfulLogin(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/v1/api/login"));
        HttpHeaders headers = new HttpHeaders();
        headers.set(MovieConstant.UUID, UUID.randomUUID().toString());
        headers.set(MovieConstant.AUTHORIZATION,"Basic am9obkBkb2UuY29tOmpvaG4=");
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(url, request, LoginResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody().getAccessToken()));
    }

}
