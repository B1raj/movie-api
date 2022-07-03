package com.biraj.backbase.movie.movieapi.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

import java.util.List;

/**
 * @author birajmishra
 *Response of login 
 */
@Scope("prototype")
@Slf4j
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Top10MovieResponse {
	private List<String> name;
	}
