package com.biraj.backbase.movie.movieapi.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author birajmishra
 *
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfo {
	private String username;
}
