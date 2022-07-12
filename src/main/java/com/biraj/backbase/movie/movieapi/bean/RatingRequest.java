package com.biraj.backbase.movie.movieapi.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {
    private  double rating;
    private  int year;
    private String movie;
}
