package com.beyongx.common.utils;

import org.apache.commons.lang3.StringUtils;

public class Hex {
	private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	public static String encode(String rawText) {
		if (StringUtils.isEmpty(rawText)) {
			return "";
		}
		
		byte[] bytes = rawText.getBytes();
		final int nBytes = bytes.length;
		char[] result = new char[2 * nBytes];

		int j = 0;
		for (int i = 0; i < nBytes; i++) {
			// Char for top 4 bits
			result[j++] = HEX[(0xF0 & bytes[i]) >>> 4];
			// Bottom 4
			result[j++] = HEX[(0x0F & bytes[i])];
		}

		return new String(result);
	}
	
	public static char[] encode(byte[] bytes) {
		final int nBytes = bytes.length;
		char[] result = new char[2 * nBytes];

		int j = 0;
		for (int i = 0; i < nBytes; i++) {
			// Char for top 4 bits
			result[j++] = HEX[(0xF0 & bytes[i]) >>> 4];
			// Bottom 4
			result[j++] = HEX[(0x0F & bytes[i])];
		}

		return result;
	}

}
