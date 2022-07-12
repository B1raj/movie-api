package com.biraj.backbase.movie.movieapi.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * @author birajmishra
 *Defines the structure of error message to be returned.
 */

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorInfo {
	private String errorCode;
	private String errorMessage;

	public String toString() {
	return "{ \"errorCode\": " + this.getErrorCode()+", \"errorMessage\": " + "\""+ this.getErrorMessage() +"\"}";

	}
}
