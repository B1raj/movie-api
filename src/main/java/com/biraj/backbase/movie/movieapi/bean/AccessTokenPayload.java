package com.biraj.backbase.movie.movieapi.bean;
/**
 * @author birajmishra
 * Basic entries to be present in JWT, as per JWT standard. Can have more entries if required
 */
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessTokenPayload {
	private String issuer;
	private Date issuedDate;
	private String audience;
	private String userId;
}
