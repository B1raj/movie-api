package com.biraj.backbase.movie.movieapi.bean;

import com.biraj.backbase.movie.movieapi.entity.Movies;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public
class TopMovies{
    private String name;
    private double rating;
    private Long collection;

    public TopMovies(String name, double rating) {
        this.name = name;
        this.rating = rating;
    }





}
