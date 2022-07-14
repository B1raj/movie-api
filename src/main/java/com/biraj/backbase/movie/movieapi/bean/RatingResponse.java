package com.biraj.backbase.movie.movieapi.bean;

import com.biraj.backbase.movie.movieapi.entity.MovieRating;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class RatingResponse {
    private Long id;
    private String movie;
    private int year;
    private double rating;
    private ErrorInfo errorInfo;
}
