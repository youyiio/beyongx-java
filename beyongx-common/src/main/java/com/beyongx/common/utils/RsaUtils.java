package com.beyongx.common.utils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RsaUtils {
	private static Logger logger = LoggerFactory.getLogger(RsaUtils.class);
	
	private static final String CIPHER_TRANSFORMATION = "RSA/ECB/PKCS1Padding";
	private static final String KEY_ALGORITHM = "RSA";

	public static String encrypt(String plain, Key key) {
		String cipherText = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encryptedBytes = cipher.doFinal(plain.getBytes());
			
			cipherText = new String(Base64.encodeBase64(encryptedBytes));
		} catch (NoSuchAlgorithmException e) {
			logger.debug("error", e);
		} catch (NoSuchPaddingException e) {
			logger.debug("error", e);
		} catch (InvalidKeyException e) {
			logger.debug("error", e);
		} catch (IllegalBlockSizeException e) {
			logger.debug("error", e);
		} catch (BadPaddingException e) {
			logger.debug("error", e);
		}
		
		return cipherText;
	}
	
	public static String decrypt(String cipherText, Key key) {
		String plain = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, key);
			
			byte[] plainBytes = cipher.doFinal(Base64.decodeBase64(cipherText.getBytes()));
			
			plain = new String(plainBytes);
		} catch (NoSuchAlgorithmException e) {
			logger.debug("error", e);
		} catch (NoSuchPaddingException e) {
			logger.debug("error", e);
		} catch (InvalidKeyException e) {
			logger.debug("error", e);
		} catch (IllegalBlockSizeException e) {
			logger.debug("error", e);
		} catch (BadPaddingException e) {
			logger.debug("error", e);
		}

		return plain;
	}
	
	public static String decryptWithPrivateKey(String cipherText, byte[] privateKeyBytes) {
		String plain = null;
		if (StringUtils.isEmpty(cipherText) || privateKeyBytes == null) {
			return plain;
		}
		try {
			plain = decrypt(cipherText, toPrivateKey(privateKeyBytes));
		} catch (Exception e) {
			logger.debug("error", e);
		}
		
		return plain;
	}
	
	public static String decryptWithPublicKey(String cipherText,
			byte[] publicKeyBytes) {
		String plain = null;
		if (StringUtils.isEmpty(cipherText) || publicKeyBytes == null) {
			return plain;
		}
		
		try {
			plain = decrypt(cipherText, toPublicKey(publicKeyBytes));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return plain;
	}

	public static String encryptWithPublicKey(String plain,
			byte[] publicKeyBytes) {
		String cipherText = null;
		if (StringUtils.isEmpty(plain) || publicKeyBytes == null) {
			return cipherText;
		}
		
		try {
			cipherText = encrypt(plain, toPublicKey(publicKeyBytes));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return cipherText;
	}

	public static String encryptWithPrivateKey(String plain,
			byte[] privateKeyBytes) {
		String cipherText = null;
		if (StringUtils.isEmpty(plain) || privateKeyBytes == null) {
			return cipherText;
		}
		
		try {
			cipherText = encrypt(plain, toPrivateKey(privateKeyBytes));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return cipherText;		
	}

	public static PrivateKey toPrivateKey(byte[] keyBytes) {
		PrivateKey key = null;
		if (keyBytes == null) {
			return key;
		}
		
		byte[] decodedKeyBytes = Base64.decodeBase64(keyBytes);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(decodedKeyBytes);
		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);			
			key = keyFactory.generatePrivate(pkcs8KeySpec);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		} catch (InvalidKeySpecException e) {
			logger.error(e.getMessage(), e);
		}
		
		return key;
	}
	
	public static PublicKey toPublicKey(byte[] keyBytes) {
		PublicKey key = null;
		if (keyBytes == null) {
			return key;
		}
		
		byte[] decodedKeyBytes = Base64.decodeBase64(keyBytes);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(
				decodedKeyBytes);
		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			key = keyFactory.generatePublic(x509KeySpec);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		} catch (InvalidKeySpecException e) {
			logger.error(e.getMessage(), e);
		}
		
		return key;
	}

}
