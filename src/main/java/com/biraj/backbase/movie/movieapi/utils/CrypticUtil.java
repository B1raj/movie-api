package com.biraj.backbase.movie.movieapi.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

@Component
public class CrypticUtil {

	public static String decrypt(String cipherText) {
		return new String(Base64Utils.decode(cipherText.getBytes()));
	}

}
