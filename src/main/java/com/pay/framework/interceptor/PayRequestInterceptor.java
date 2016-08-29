/**
 * Copyright 2015-2020 UENPAY ASLAN, Inc. All rights reserved.
 * project : mavenProjectDemo
 * package ：com.wang.framework.interceptor
 * file : LoginInterceptor.java
 * date ：2016年4月12日
 */
package com.pay.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.pay.framework.appcrypt.AES;
import com.pay.framework.appcrypt.CryptUtils;

/**
 * @author wangjiesheng
 * Specification : 文档说明:
 */
public class PayRequestInterceptor implements HandlerInterceptor{

	public static Logger logger = LogManager.getLogger(PayRequestInterceptor.class);
	
	/**
	 * AES seed
	 */
	public static  String password = "dynamicode";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestParam = "";
		try {
			requestParam = request.getParameter("requestParam");
			String transDataXml = AES.decryptString(requestParam, CryptUtils.encryptToMD5(password));
			request.setAttribute("requestParam", transDataXml);
		} catch (Exception e) {
			logger.info("数据转换异常!拦截器阻止进行......"+requestParam);
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
