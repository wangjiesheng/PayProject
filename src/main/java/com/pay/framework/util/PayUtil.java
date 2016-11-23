/**
 * Copyright 2015-2020 UENPAY ASLAN, Inc. All rights reserved.
 * project : mms
 * package ：com.uenpay.service.ams.impl
 * file : PayUtil.java
 * date ：2016年4月15日
 */
package com.pay.framework.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.pay.entity.FundPay;
import com.pay.entity.UenpayConstant;
import com.pay.framework.mongodb.AbstractParamsLog;
import com.pay.framework.mongodb.MongoDbUtil;

/**
 * @author wangjiesheng
 * Specification : 文档说明:
 */
public class PayUtil extends AbstractParamsLog{
	
	/**
	 * 响应数据处理
	 * @param response
	 * @param mess
	 * @throws IOException
	 */
	public static void repData(HttpServletResponse response, String jsonStr) throws IOException{
		PrintWriter pw = response.getWriter();
		pw.write(jsonStr);
		pw.flush();
		pw.close();
	}
	
	/**
	 * 异常字符串截取
	 * @param e Exception
	 * @param len 截取字符串的长度
	 * @return
	 */
	public static String exception2Str(Exception e,int len){
		return e.toString().length() >= len ? e.toString().substring(0, len) : e.toString();
	}
	
	/**
	 * 把代付的请求数据和响应数据存入mongoDB,便于查日志
	 * @param requestparams 请求数据
	 * @param responseparams 响应数据
	 * @param fundPay 打款信息
	 * @param type 类型
	 */
	public static void insertMongoDbData(String requestparams, String responseparams ,FundPay fundPay, String type){
		Map<String, Object> mapReq = new HashMap<String, Object>();
		mapReq.put(REQUEST_TYPE, type);
		mapReq.put(APP_PAY_ID, fundPay.getAppPayId());
		mapReq.put(PARAM, fundPay.toString());
		mapReq.put(REQUEST_PARAMS_KEY, requestparams);
		mapReq.put(RESPONSE_PARAMS_KEY, responseparams);
		mapReq.put(REQUEST_TIME_KEY, getTimeStr(new Date()));
		MongoDbUtil.insertData(MONGODB_NAME, DF_COLLECTION_NAME, mapReq);
	}
	
	/**
	 * 把代付的请求数据和响应数据存入mongoDB,便于查日志
	 * @param requestparams 请求数据
	 * @param responseparams 响应数据
	 * @param appPayId 打款流水号
	 * @param type 类型
	 */
	public static void insertMongoDbData(String requestparams, String responseparams ,String appPayId, String type){
		Map<String, Object> mapReq = new HashMap<String, Object>();
		mapReq.put(REQUEST_TYPE, type);
		mapReq.put(APP_PAY_ID, appPayId);
		mapReq.put(REQUEST_PARAMS_KEY, requestparams);
		mapReq.put(RESPONSE_PARAMS_KEY, responseparams);
		mapReq.put(REQUEST_TIME_KEY, getTimeStr(new Date()));
		MongoDbUtil.insertData(MONGODB_NAME, DF_COLLECTION_NAME, mapReq);
	}
	
	/**
	 * 时间格式
	 * @param date
	 * @return
	 */
	public static String getTimeStr(Date date){
		SimpleDateFormat timeFormat = new SimpleDateFormat(UenpayConstant.TIME_FORMAT_PATTERN);
		return timeFormat.format(date);
	}
}
