package com.beyongx.common.utils;

public class HexUtils {
	
	public static byte[] hex2Bytes(String str) {//a1dfd
	    if (str == null || str.length() == 0) {
	        return null;
	    }
	    
	    byte[] ret = new byte[str.length() / 2];
	    byte[] tmp = str.getBytes();
	    for (int i = 0; i < (tmp.length / 2); i++) {
	        ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
	    }
	    
	    return ret;
	}
	
	public static byte uniteBytes(byte src0, byte src1) { 
		byte _b0 = Byte.decode("0x" + new String(new byte[] {src0})).byteValue();
		_b0 = (byte) (_b0 << 4); 
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
		byte ret = (byte) (_b0 ^ _b1); 
		return ret; 
	}
	
	public static String bytesToHex(byte[] bytes) {
		if (bytes == null || bytes.length <= 0) {
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) { 
		    String hex = Integer.toHexString(bytes[i] & 0xFF);
		    if (hex.length() == 1) { 
		        hex = '0' + hex; 
		    } 
		     
		    sb.append(hex);
		} 
		
		return sb.toString();
	}
	
	public static long bytesToLong(byte[] bytes) {
		if (bytes == null || bytes.length <= 0) {
			return 0l;
		}
		
		long result = 0l;
		int length = bytes.length;
		for (int i = 0; i < bytes.length; i++) {
			result += bytes[i] << (8 * (length - i - 1));
		}
		
		return result;
	}
	
	public static byte[] intToBytes(int n) {
		byte[] datas = new byte[4];
		for (int i = 0; i < datas.length; i++) {
			datas[3 - i] = (byte) (n >>> (i * 8));
		}
		
		return datas;
	}
	
}
