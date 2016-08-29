/**
 * Copyright 2015-2020 UENPAY ASLAN, Inc. All rights reserved.
 * project : payment
 * package ：com.pay.framework
 * file : package-info.java
 * date ：2016年8月23日
 */
/**
 * @author wangjiesheng
 * Specification : 文档说明:
 */
package com.pay.framework.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class TestSocket implements ISocketHandler{

	private Logger logger = LogManager.getLogger(TestSocket.class);
	
	@Override
	public Object receiveData(InputStream input) {
		byte[] buffer = new byte[4096];
		StringBuilder sb = new StringBuilder();
		try {
			int len = -1;
			while((len = input.read(buffer)) > 0){
				sb.append(new String(buffer, 0, len, "GBK"));
			}
			logger.info("接受到的数据-->"+sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Override
	public void responseData(OutputStream out, Object obj) {
		try {
			String val = (String)obj;
			out.write(val.getBytes("GBK"));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}