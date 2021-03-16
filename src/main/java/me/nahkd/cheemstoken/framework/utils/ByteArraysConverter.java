package me.nahkd.cheemstoken.framework.utils;

import java.util.Base64;

public class ByteArraysConverter {
	
	public static final String toBase64(byte[] in) {
		return new String(Base64.getEncoder().encode(in));
	}
	
	public static final byte[] fromBase64(String in) {
		return Base64.getDecoder().decode(in);
	}
	
}
