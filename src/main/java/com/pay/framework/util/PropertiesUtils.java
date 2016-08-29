/**
 *   上海尤恩信息技术有限公司
 *   (c) Copyright 2015-2020 uenpay
 *	 http://www.uenpay.com/
 *   FILENAME     : PropertiesUtils.java
 *   PACKAGE      : com.uenpay.porter.utils
 *   CREATE DATE  : 2016年3月7日
 *   AUTHOR       :  chenbing
 *   MODIFIED BY  :
 *	 DESCRIPTION  :
 */
package com.pay.framework.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pay.entity.UenpayConstant;

/**
 * @author chenbing
 * Specification : 文档说明
 */
public class PropertiesUtils {
	/**
	 * logger
	 */
	private static Logger log = LogManager.getLogger(PropertiesUtils.class);

	private static Properties properties = new Properties();

	static {
		InputStream fis = null;
		String configPath = null;
		try {
			String fileSeparator = System.getProperty(UenpayConstant.FILE_SEPARATOR);
			String userHomedir = System.getProperty(UenpayConstant.USER_HOME);
			configPath = userHomedir + fileSeparator + "payment-configure.properties";
			log.debug("configure.properties配置文件的路径:"+configPath);
			fis = new FileInputStream(configPath);
			properties.load(fis);
		} catch (Exception e) {
			log.error("请检查payment-configure.properties是否在"+configPath+"路径下");
			throw new Error(e);
		}finally{
			if(null != fis){
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 获得属性里面的值
	 * 
	 * @param key
	 * @return
	 */
	public static String getPropertiesVal(String key) {
		return properties.getProperty(key);
	}

}
