package com.beyongx.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {
	private static MessageDigest messageDigest;

	static {
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm [SHA-256]");
		}
	}

	/**
	 * 密码加密
	 * 
	 * @param plainPassword
	 *            明文密码
	 * @param salt
	 *            盐值，允许为null
	 * @return 加密后的密码
	 */
	public static String encodePassword(String plainPassword, Object salt) {
		String saltedPass = mergePasswordAndSalt(plainPassword, salt);

		byte[] digest;

		try {
			digest = messageDigest.digest(saltedPass.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("UTF-8 not supported!");
		}

		String shaStr = new String(Hex.encode(digest));

		return MD5Utils.md5(shaStr);
	}

	private static String mergePasswordAndSalt(String password, Object salt) {
		if (password == null) {
			password = "";
		}

		if (salt == null) {
			salt = "";
		}

		return password.toLowerCase() + "{" + salt.toString() + "}";
		
	}
}
