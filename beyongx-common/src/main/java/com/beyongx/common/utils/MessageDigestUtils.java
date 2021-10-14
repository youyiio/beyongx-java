package com.beyongx.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 消息摘要工具
 */
public class MessageDigestUtils {
    private static MessageDigest MD5_DIGEST = null;
    private static MessageDigest SHA_DIGEST = null;

    static {
        try {
            MD5_DIGEST = MessageDigest.getInstance("MD5");
            SHA_DIGEST = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /*
     * 摘要算法: MD5
     */
    public static synchronized String getMD5(String srcTxt) {
        if (srcTxt == null) {
            return null;
        }

        MD5_DIGEST.update(srcTxt.getBytes());

        byte[] byteRes = MD5_DIGEST.digest();

        return new String(Hex.encode(byteRes));
    }

    /*
     * 摘要算法: SHA
     */
    public static String getSHA(String srcTxt) {
        if (srcTxt == null) {
            return null;
        }

        SHA_DIGEST.update(srcTxt.getBytes());

        byte[] byteRes = SHA_DIGEST.digest();

        return new String(Hex.encode(byteRes));

    }
}
