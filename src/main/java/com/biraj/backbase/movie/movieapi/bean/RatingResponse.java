package com.biraj.backbase.movie.movieapi.bean;

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
    private com.biraj.backbase.movie.movieapi.entity.Rating rating;
    private ErrorInfo errorInfo;
}
