/**
 * Copyright 2015 UENPAY, Inc. All rights reserved. project : ams package ：com.uenpay.framework.servlet file :
 * InitServlet.java date ：2015年4月2日
 */
package com.pay.framework.servlet;

import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author SongBang Specification : 文档说明
 */
public class InitServlet extends HttpServlet {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4917445416276110428L;

	/**
	 * log
	 */
	private static final Logger log = LogManager.getLogger(InitServlet.class);

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		//设置应用程序的local
		Locale.setDefault(Locale.CHINA);
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		for (String name : context.getBeanDefinitionNames()) {
			System.out.println("spring加载的资源--->"+name);
		};
	}
	

}
