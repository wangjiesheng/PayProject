package com.pay.framework.socket;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Socket操作接口
 * @author OJH
 * 2015年6月9日
 */
public interface ISocketHandler {
	
	/**
	 * 接收socket请求的数据
	 * @param input
	 * @return 处理后的数据
	 */
	public Object receiveData(InputStream input);
	
	/**
	 * 响应socket的数据
	 * @param out 响应的数据
	 * @param obj 接收socket请求的数据返回的对象
	 */
	public void responseData(OutputStream out, Object obj);
	
}
