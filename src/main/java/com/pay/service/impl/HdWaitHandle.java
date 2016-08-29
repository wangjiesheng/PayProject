/**
 * Copyright 2015-2020 UENPAY ASLAN, Inc. All rights reserved.
 * project : mms
 * package ：com.uenpay.service.ams.impl
 * file : HdWaitHandle.java
 * date ：2016年4月1日
 */
package com.pay.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pay.entity.FundPay;
import com.pay.entity.MessageHandle;
import com.pay.framework.util.HttpUtils;

/**
 * @author wangjiesheng
 * Specification : 文档说明:处理弘达代付查询返回结果:交易已受理，请稍后查询交易结果  线程处理类
 */
public class HdWaitHandle implements Runnable{
	
	private Logger logger = LogManager.getLogger(HdWaitHandle.class);

	private String sonPoolType;
	
	private FundPay fundPay;
	
	private String amsPayHandleUrl;
	
	private String operation;
	
	public HdWaitHandle(String sonPoolType, FundPay fundPay,
			String amsPayHandleUrl,String operation) {
		super();
		this.sonPoolType = sonPoolType;
		this.fundPay = fundPay;
		this.amsPayHandleUrl = amsPayHandleUrl;
		this.operation = operation;
	}

	@Override
	public void run() {
		String appPayId = fundPay.getAppPayId();
		logger.info("出现查询结果-->交易已受理,请稍后查询交易 或者查询socket异常结果,正在等待1分钟后查询结果......"+appPayId);
		try {
			Thread.sleep(60000);//等待1分钟再次查询
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("等待结束,正在查询结果......"+appPayId);
		
		MessageHandle mess = null;
		try {
			mess = HdPayPublicMethod.HdQueryPublic(fundPay,operation , false);
		} catch (Exception e1) {
			mess = HdPayPublicMethod.HdQueryOrderId(appPayId, operation);
		}
		logger.info("查询返回结果----------->"+mess.toString());
		String resultCode = "404";// 默认请求异常标志
		
		//3、根据查询结果调用ams修改打款状态
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("appPayId", appPayId);
			params.put("sonPoolType", sonPoolType);
			params.put("paymentChannel", fundPay.getPaymentChannel());
			logger.info("ams请求修改状态http地址------------>" + amsPayHandleUrl + ",参数:" + params.toString());
			if (mess.isFlag()) {
				resultCode = HttpUtils.postRequestByString(amsPayHandleUrl + "/successAction", params);
			} else {// 代付不成功时记录原因
				params.put("errorInfo", mess.getErrorInfo());//代付不成功原因:打款返回的信息-->加上查询返回的信息
				resultCode = HttpUtils.postRequestByString(amsPayHandleUrl + "/failingAction", params);
			}
			logger.info("ams修改状态返回结果------"+appPayId+"------>" + resultCode);
		} catch (Exception e) {
			logger.info("弘达代付更新数据异常......"+appPayId, e);
		}
	}

}
