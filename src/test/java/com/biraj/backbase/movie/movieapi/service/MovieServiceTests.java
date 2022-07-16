package com.biraj.backbase.movie.movieapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class MovieServiceTests {

    @Autowired
    MovieService movieService;

    @Test
    public void Test_toNumber(){
        assertEquals( movieService.toNumber("$659,363,944"),659363944L );
    }



}
