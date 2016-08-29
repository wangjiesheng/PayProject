package com.pay.framework.socket.hd;

import java.io.UnsupportedEncodingException;

import com.pay.framework.util.PropertiesUtils;

/**
 *@author lb
 *2016年2月15日
 */
public class SocketClient {
	
	public static String connServer(String info) {
		byte[] reqpack = null;
		byte[] resppack = null;
		String rtuInfo = "";
		String character = getBeanValue("character");
		try {
			byte[] reqmsg = info.getBytes(character);
			int reqlen = reqmsg.length;
			String len = "0000" + reqlen;
			len = len.substring(len.length() - 4);
			reqpack = new byte[4 + reqlen];
			System.arraycopy(len.getBytes(character), 0, reqpack, 0, 4);
			System.arraycopy(reqmsg, 0, reqpack, 4, reqlen);
			resppack = ServerClent.connServer(reqpack,character);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(resppack == null){
			rtuInfo =  "null";
		}else if (resppack.length > 0){
			try {
				rtuInfo = new String(resppack, character);//LoUtils.asciiToString(resppack);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return rtuInfo;
	}
	
	private static String getBeanValue(String name){
		return PropertiesUtils.getPropertiesVal(name);
	}
}
