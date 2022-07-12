package com.biraj.backbase.movie.movieapi.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author birajmishra
 * Response of login
 */


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieResponse {
    private String name;
    private int year;
	private Award[] awards;
    private ErrorInfo errorInfo;
    private Long boxOfficeCollection;
}
