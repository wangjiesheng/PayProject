package com.pay.framework.socket.hd;



import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pay.framework.util.PropertiesUtils;

public class ServerClent {
	
	static Logger logger = LogManager.getLogger(ServerClent.class);
	
	/*  新模式测试环境
	 *  测试地址：211.103.172.38:8838
		测试企业编号:11023
		测试企业ID：D9FB5353EE9D8649AB8FE87535FE06CC
	*/
	
	/*	新模式正式环境
	 	公网   210.74.5.40   8839
		内网   172.12.10.50  8839
		11005
		09B9E7F4FC64CFB5E504CFDE61D0F356
	*/	
	
	/*  旧模式的正式环境
	 *  正式地址：190.159.6.31:8838
		正式企业编号:11005
		正式企业ID：FAC52ADFB1D19EB9CD34FA4DCBC9D3DA
	 */
	/**
	 * ********************************************************
	 * 
	 * @Title: connServer
	 * @Description:
	 * @return byte[]
	 * @throws AshException
	 * @throws AshException
	 * @date 2013-5-9 上午01:11:37
	 ******************************************************** 
	 */
	public static byte[] connServer(byte[] reqPack,String character) throws Exception {
		String ipAddress = getBeanValue("ip");
		Integer port = Integer.valueOf(getBeanValue("port"));
		Integer timeOut = Integer.valueOf(getBeanValue("timeout"));
		
		byte[] respPack = null;
		Socket socket = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			socket = new Socket(); // 建立与服务器端的链接
			socket.setSoTimeout(timeOut); // 读写超时设置
			socket.connect(new InetSocketAddress(ipAddress, port), timeOut); // 连接超时设置
			//写入
			os = socket.getOutputStream();
			os.write(reqPack);
			os.flush();
			socket.shutdownOutput();
			
			//读取响应流
			is = socket.getInputStream();
			// 接收服务器的响应
			byte[] lenByte = new byte[4];
			is.read(lenByte, 0, 4);
			int len = Integer.parseInt(new String(lenByte, character));//(LoUtils.asciiToString(lenByte)
			byte[] bb = new byte[len];
			@SuppressWarnings("unused")
			int size = is.read(bb, 0, len);
			respPack = new byte[lenByte.length + bb.length];
			System.arraycopy(lenByte, 0, respPack, 0, 4);
			System.arraycopy(bb, 0, respPack, lenByte.length, bb.length);
			//正常执行到此处，证明已取到响应数据，连接正常
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("发送弘达通道socket异常!");
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
				if (socket != null) {
					socket.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return respPack;
	}
	
	private static String getBeanValue(String name){
		return PropertiesUtils.getPropertiesVal("pay.socket."+name);
	}
}

	