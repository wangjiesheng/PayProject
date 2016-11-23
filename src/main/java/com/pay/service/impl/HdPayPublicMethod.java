/**
 * Copyright 2015-2020 UENPAY ASLAN, Inc. All rights reserved.
 * project : mms
 * package ：com.uenpay.service.ams.impl
 * file : HdPayPublicMethod.java
 * date ：2016年2月23日
 */
package com.pay.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pay.entity.FundPay;
import com.pay.entity.HdPayConstant;
import com.pay.entity.MessageHandle;
import com.pay.framework.mongodb.AbstractParamsLog;
import com.pay.framework.socket.hd.SocketClient;
import com.pay.framework.socket.hd.Util;
import com.pay.framework.util.JsonMapper;
import com.pay.framework.util.PayUtil;
import com.pay.framework.util.PropertiesUtils;


/**
 * @author wangjiesheng
 * Specification : 文档说明:
 */
public class HdPayPublicMethod extends AbstractParamsLog {
	
	public static Logger logger = LogManager.getLogger(HdPayPublicMethod.class);
	
	private static List<String> list = Arrays.asList("00","A6");
	
	/**
	 * 
	 * @param fundPay
	 * @param operation 0：表示T+0业务1：表示T+1业务
	 * @return
	 */
	public static MessageHandle HdpayPubilc(FundPay fundPay, String operation,boolean isMessage){
		String appPayId = fundPay.getAppPayId();
		MessageHandle mess = new MessageHandle();
		Util util = new Util();
		
		Map<String, String> reqMap = new HashMap<String, String>();
		Map<String, String> mapResult = new HashMap<String, String>();
		reqMap.put(HdPayConstant.VERSION, "1.0");
		reqMap.put(HdPayConstant.TXNTYPE, "12");
		reqMap.put(HdPayConstant.ENTERPRISEID, getBeanValue("enterpriseID"));
		reqMap.put(HdPayConstant.SETTTYPE, operation);//0：表示T+0业务1：表示T+1业务
		reqMap.put(HdPayConstant.SIGNMETHOD, "01");
		reqMap.put(HdPayConstant.TXNTIME,fundPay.getTime());
		reqMap.put(HdPayConstant.ACCNO, fundPay.getCardNo());
		reqMap.put(HdPayConstant.TXNAMT, Integer.parseInt(fundPay.getPayAmount())+"");
		reqMap.put(HdPayConstant.ENTERPRISENO, getBeanValue("enterpriseNo"));
		reqMap.put(HdPayConstant.ORDERID,"N"+appPayId);
		reqMap.put(HdPayConstant.OLDMERID, "996381156510001");//T+0和T+1都要传
		reqMap.put("terminalNo", fundPay.getTerminalNo());//终端号
		reqMap.put(HdPayConstant.BANKNO, "");
		reqMap.put(HdPayConstant.BANKNAME,fundPay.getBankName());
		reqMap.put(HdPayConstant.PAYNAME, fundPay.getAccountName());
		reqMap.put(HdPayConstant.PPTYPE, "0");
		reqMap.put(HdPayConstant.NOTE, "尤恩代付newT+"+operation+"业务");
		reqMap.put(HdPayConstant.BACKURL, getBeanValue("backUrl"));
		
		String reqStr = "";
		String respResult = "";
		try {
			reqStr = util.jsonObject(reqMap);
			logger.info(appPayId+"_弘达代付请求报文："+reqStr);
			respResult = SocketClient.connServer(reqStr);
			logger.info(appPayId+"_弘达代付返回报文："+respResult);
			
			//处理比较奇葩的情况,
			if("null".equals(respResult)){//返回信息为自己定义的null字符串时
				logger.info(appPayId+"_发送报文异常,无响应!");
				mess.setErrorInfo("无响应");
				mess.setRespMsg("无响应");
				mess.setFlag(false);
				isMessage = true;
			}else{
				respResult = respResult.substring(4);
				mapResult = JsonMapper.readJsonMap(respResult);
				String queryId = mapResult.get(HdPayConstant.QUERYID);//查询ID保存起来,便于查询。
				String respMsg = mapResult.get(HdPayConstant.RESPMSG);//打款返回的信息
				mess.setFlag(true);
				mess.setPaySeqNo(queryId);
				mess.setRespMsg(respMsg);
			}
		} catch (Exception e) {
			logger.info(appPayId+"_发送报文异常",e);
			mess.setErrorInfo(PayUtil.exception2Str(e, 30));
			mess.setRespMsg(PayUtil.exception2Str(e, 30));
			mess.setFlag(false);
			isMessage = true;
		}
		PayUtil.insertMongoDbData(reqStr, respResult, fundPay, "PAY");
		return mess;
	}
	
	
	public static MessageHandle HdQueryPublic(FundPay fundPay, String operation, boolean isMessage){
		MessageHandle mess = new MessageHandle();
		String [] strArry = fundPay.getPaySeqNo().split(":");//time:queryId
		fundPay.setTime(strArry[0]);
		String appPayId = fundPay.getAppPayId();
		Util util=new Util();

		Map<String, String> mapResult = new HashMap<String, String>();
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put(HdPayConstant.VERSION, "1.0");
		reqMap.put(HdPayConstant.TXNTYPE, "00");
		reqMap.put(HdPayConstant.ENTERPRISEID, getBeanValue("enterpriseID"));
		reqMap.put(HdPayConstant.SIGNMETHOD, "01");
		reqMap.put(HdPayConstant.TXNTIME,strArry[0]);//被查询交易的交易时间
		reqMap.put(HdPayConstant.ENTERPRISENO, getBeanValue("enterpriseNo"));
		reqMap.put(HdPayConstant.ORDERID,"N"+appPayId);//被查询交易的订单号
		reqMap.put("settType", operation);//T+0 or T+1
		if("null".equals(strArry[1])){
			//没有queryId就去掉此参数进行代付确定查询
		}else{
			reqMap.put(HdPayConstant.QUERYID,strArry[1]);//交易查询流水号
		}
		
		String reqStr = "";
		String respResult = "";
		try {
			reqStr = util.jsonObject(reqMap);
			logger.info(appPayId + "_弘达代付确认请求报文：" + reqStr);
			respResult = SocketClient.connServer(reqStr);
			logger.info(appPayId + "_弘达代付确认返回报文：" + respResult);
			respResult = respResult.substring(4);
			mapResult = JsonMapper.readJsonMap(respResult);
			String respMsg = mapResult.get(HdPayConstant.ORIGRESPMSG).toString();
			if (mapResult.get(HdPayConstant.RESPCODE).toString().equals("00")
					&& mapResult.get(HdPayConstant.ORIGRESPCODE).equals("00")) {// 表示成功
				mess.setErrorInfo(respMsg);
				mess.setFlag(true);
			}else{
				mess.setErrorInfo(respMsg);
				mess.setFlag(false);
			}
		} catch (Exception e) {
			logger.info(appPayId + "_弘达代付查询失败", e);
			mess.setErrorInfo("query无响应");
			mess.setFlag(false);
			isMessage = true;
		}
		PayUtil.insertMongoDbData(reqStr, respResult, fundPay, "QUERY");
		return mess;
	}
	
	/**
	 * 直接根据订单号查询代付打款结果
	 * @param appPayId
	 * @return
	 */
	public static MessageHandle HdQueryOrderId(String appPayId, String operation){
		MessageHandle mess = new MessageHandle();
		Util util=new Util();

		Map<String, String> mapResult = new HashMap<String, String>();
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put(HdPayConstant.VERSION, "1.0");
		reqMap.put(HdPayConstant.TXNTYPE, "00");
		reqMap.put(HdPayConstant.ENTERPRISEID, getBeanValue("enterpriseID"));
		reqMap.put(HdPayConstant.SIGNMETHOD, "01");
		reqMap.put(HdPayConstant.ENTERPRISENO, getBeanValue("enterpriseNo"));
		reqMap.put(HdPayConstant.ORDERID,"N"+appPayId);//被查询交易的订单号
		reqMap.put("settType", operation);//T+0 or T+1
		
		String reqStr = "";
		String respResult = "";
		try {
			reqStr = util.jsonObject(reqMap);
			logger.info(appPayId + "_弘达代付确认请求报文：" + reqStr);
			respResult = SocketClient.connServer(reqStr);
			logger.info(appPayId + "_弘达代付确认返回报文：" + respResult);

			respResult = respResult.substring(4);
			mapResult = JsonMapper.readJsonMap(respResult);
			String respMsg = mapResult.get(HdPayConstant.ORIGRESPMSG).toString();
			if (mapResult.get(HdPayConstant.RESPCODE).toString().equals("00") && list.contains(mapResult.get(HdPayConstant.ORIGRESPCODE))) {// 表示成功
				mess.setErrorInfo(respMsg);
				mess.setFlag(true);
			}else{
				mess.setErrorInfo(respMsg);
				mess.setFlag(false);
			}
		} catch (Exception e) {
			logger.info(appPayId + "_弘达代付查询失败", e);
			mess.setErrorInfo("query无响应");
			mess.setFlag(false);
		}
		PayUtil.insertMongoDbData(reqStr, respResult, appPayId, "QUERY");
		return mess;
	}
	
	private static String getBeanValue(String name){
		return PropertiesUtils.getPropertiesVal("pay.param."+name);
	}
	
	/**
	 * 查询余额方法
	 * @param args
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Util util=new Util();
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put(HdPayConstant.VERSION, "1.0");
		reqMap.put(HdPayConstant.TXNTYPE, "01");
		reqMap.put(HdPayConstant.ENTERPRISEID, getBeanValue("enterpriseID"));
		reqMap.put(HdPayConstant.SIGNMETHOD, "01");
		reqMap.put(HdPayConstant.ENTERPRISENO, getBeanValue("enterpriseNo"));
		reqMap.put("settType", "0");//T+0 or T+1
		String reqStr = util.jsonObject(reqMap);
		String respResult = SocketClient.connServer(reqStr);
		System.out.println(respResult);
		System.out.println(System.currentTimeMillis() - start);
	}
}
