package com.biraj.backbase.movie.movieapi.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
/**
 * @author birajmishra
 */
public class DateUtil {

	public static Date currentDate() {
		return new Date();
	}

	public static Timestamp getTimestamp() {
		return Timestamp.from(Instant.now());
	}

}
