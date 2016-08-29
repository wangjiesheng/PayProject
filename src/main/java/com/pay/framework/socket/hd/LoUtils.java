package com.pay.framework.socket.hd;


import java.io.UnsupportedEncodingException;





public class LoUtils {
	public static String byte2HexStr(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}
	public static String asciiToString(byte[] value) {
		try {
			return new String(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	public static void main(String[] args) {
		byte[] value={0X48,0X50};
		System.out.println(LoUtils.asciiToString(value)+"|啊啊啊");
	}

}
