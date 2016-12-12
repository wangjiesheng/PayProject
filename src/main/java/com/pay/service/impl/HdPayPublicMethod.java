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

import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pay.entity.FundPay;
import com.pay.entity.HdPayConstant;
import com.pay.entity.MessageHandle;
import com.pay.framework.socket.hd.SocketClient;
import com.pay.framework.socket.hd.Util;
import com.pay.framework.util.PayUtil;


/**
 * @author wangjiesheng
 * Specification : 文档说明:
 */
public class HdPayPublicMethod extends PayUtil{
	
	public static Logger logger = LogManager.getLogger(HdPayPublicMethod.class);
	
	private static List<String> list = Arrays.asList("00","A6");//00表示成功,06表示有缺陷的成功
	
	public static String HdpayPubilc(String data){
		JSONObject json = JSONObject.fromObject(data);
		String settType = json.getString("settType");
		String appPayId = json.getString("appPayId");
		Util util = new Util();
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put(HdPayConstant.VERSION, "1.0");
		reqMap.put(HdPayConstant.TXNTYPE, "12");
		reqMap.put(HdPayConstant.ENTERPRISEID, getBeanValue(HdPayConstant.ENTERPRISEID));
		reqMap.put(HdPayConstant.SETTTYPE, settType);//0：表示T+0业务1：表示T+1业务
		reqMap.put(HdPayConstant.SIGNMETHOD, "01");
		reqMap.put(HdPayConstant.TXNTIME,json.getString("txnTime"));//20161206155147
		reqMap.put(HdPayConstant.ACCNO, json.getString("accNo"));//账号
		reqMap.put(HdPayConstant.TXNAMT, json.getString("txnAmt"));//单位分 551796
		reqMap.put(HdPayConstant.ENTERPRISENO, getBeanValue(HdPayConstant.ENTERPRISENO));
		reqMap.put(HdPayConstant.ORDERID,"N"+appPayId);
		reqMap.put(HdPayConstant.OLDMERID, "996381156510001");//T+0和T+1都要传
		reqMap.put(HdPayConstant.TERMINALNO, json.getString("terminalNo"));//终端号
		reqMap.put(HdPayConstant.BANKNO, "");
		reqMap.put(HdPayConstant.BANKNAME,json.getString("bankName"));
		reqMap.put(HdPayConstant.PAYNAME, json.getString("accountName"));
		reqMap.put(HdPayConstant.PPTYPE, "0");
		reqMap.put(HdPayConstant.NOTE, "尤恩代付newT+"+settType+"业务"+PayUtil.getCommonBeanValue("pay.socket.port"));
		reqMap.put(HdPayConstant.BACKURL, getBeanValue(HdPayConstant.BACKURL));
		String reqStr = "";
		String respResult = "";
		try {
			reqStr = util.jsonObject(reqMap);
			logger.info(appPayId+"_弘达代付请求报文："+reqStr);
			respResult = SocketClient.connServer(reqStr);
			logger.info(appPayId+"_弘达代付返回报文："+respResult);
			respResult = respResult.substring(4);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//"respCode":"00","respMsg":"成功"
			JSONObject resultJson = new JSONObject();
			resultJson.put("respCode", "99");
			resultJson.put("respMsg", "未知错误");
			respResult = resultJson.toString();
		}
		return respResult;
	}
	
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
		reqMap.put(HdPayConstant.VERSION, "1.0");
		reqMap.put(HdPayConstant.TXNTYPE, "12");
		reqMap.put(HdPayConstant.ENTERPRISEID, getBeanValue(HdPayConstant.ENTERPRISEID));
		reqMap.put(HdPayConstant.SETTTYPE, operation);//0：表示T+0业务1：表示T+1业务
		reqMap.put(HdPayConstant.SIGNMETHOD, "01");
		reqMap.put(HdPayConstant.TXNTIME,fundPay.getTime());
		reqMap.put(HdPayConstant.ACCNO, fundPay.getCardNo());
		reqMap.put(HdPayConstant.TXNAMT, Integer.parseInt(fundPay.getPayAmount())+"");
		reqMap.put(HdPayConstant.ENTERPRISENO, getBeanValue(HdPayConstant.ENTERPRISENO));
		reqMap.put(HdPayConstant.ORDERID,"N"+appPayId);
		reqMap.put(HdPayConstant.OLDMERID, "996381156510001");//T+0和T+1都要传
		reqMap.put(HdPayConstant.TERMINALNO, fundPay.getTerminalNo());//终端号
		reqMap.put(HdPayConstant.BANKNO, "");
		reqMap.put(HdPayConstant.BANKNAME,fundPay.getBankName());
		reqMap.put(HdPayConstant.PAYNAME, fundPay.getAccountName());
		reqMap.put(HdPayConstant.PPTYPE, "0");
		reqMap.put(HdPayConstant.NOTE, "尤恩代付newT+"+operation+"业务"+PayUtil.getCommonBeanValue("pay.socket.port"));
		reqMap.put(HdPayConstant.BACKURL, getBeanValue(HdPayConstant.BACKURL));
		
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
				mess.setErrorCode("99");
				mess.setFlag(false);
				isMessage = true;
			}else{
				respResult = respResult.substring(4);
				JSONObject jsonResult = JSONObject.fromObject(respResult);
				String queryId = "";
				if(jsonResult.containsKey(HdPayConstant.QUERYID)){
					queryId = jsonResult.getString(HdPayConstant.QUERYID);//查询ID保存起来,便于查询。
				}
				String respMsg = jsonResult.getString(HdPayConstant.RESPMSG);//打款返回的信息
				String respCode = jsonResult.getString(HdPayConstant.RESPCODE);//打款返回的信息
				
				mess.setFlag(true);
				mess.setPaySeqNo(queryId);
				mess.setRespMsg(respMsg);
				mess.setErrorCode(respCode);
			}
		} catch (Exception e) {
			logger.info(appPayId+"_发送报文异常",e);
			e.printStackTrace();
			mess.setErrorCode("99");
			mess.setErrorInfo(exception2Str(e, 30));
			mess.setRespMsg(exception2Str(e, 30));
			mess.setFlag(false);
			isMessage = true;
		}
		insertMongoDbData(reqStr, respResult, fundPay, "PAY");
		return mess;
	}
	
	
	public static MessageHandle HdQueryPublic(FundPay fundPay, String operation, boolean isMessage){
		MessageHandle mess = new MessageHandle();
		String [] strArry = fundPay.getPaySeqNo().split(":");//time:queryId
		fundPay.setTime(strArry[0]);
		String appPayId = fundPay.getAppPayId();
		Util util=new Util();

		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put(HdPayConstant.VERSION, "1.0");
		reqMap.put(HdPayConstant.TXNTYPE, "00");
		reqMap.put(HdPayConstant.ENTERPRISEID, getBeanValue("enterpriseID"));
		reqMap.put(HdPayConstant.SIGNMETHOD, "01");
		reqMap.put(HdPayConstant.TXNTIME,strArry[0]);//被查询交易的交易时间
		reqMap.put(HdPayConstant.ENTERPRISENO, getBeanValue("enterpriseNo"));
		reqMap.put(HdPayConstant.ORDERID,"N"+appPayId);//被查询交易的订单号
		reqMap.put(HdPayConstant.SETTTYPE, operation);//T+0 or T+1
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
			
			JSONObject mapResult = JSONObject.fromObject(respResult);
			String respMsg = mapResult.getString(HdPayConstant.ORIGRESPMSG);
			if (mapResult.getString(HdPayConstant.RESPCODE).equals("00") 
					&& mapResult.getString(HdPayConstant.ORIGRESPCODE).equals("00")) {// 表示成功
				mess.setErrorInfo(respMsg);
				mess.setFlag(true);
			}else{
				mess.setErrorInfo(respMsg);
				mess.setFlag(false);
			}
		} catch (Exception e) {
			logger.info(appPayId + "_弘达代付查询失败", e);
			e.printStackTrace();
			mess.setErrorInfo("query无响应");
			mess.setFlag(false);
			isMessage = true;
		}
		insertMongoDbData(reqStr, respResult, fundPay, "QUERY");
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

		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put(HdPayConstant.VERSION, "1.0");
		reqMap.put(HdPayConstant.TXNTYPE, "00");
		reqMap.put(HdPayConstant.ENTERPRISEID, getBeanValue("enterpriseID"));
		reqMap.put(HdPayConstant.SIGNMETHOD, "01");
		reqMap.put(HdPayConstant.ENTERPRISENO, getBeanValue("enterpriseNo"));
		reqMap.put(HdPayConstant.ORDERID,"N"+appPayId);//被查询交易的订单号
		reqMap.put(HdPayConstant.SETTTYPE, operation);//T+0 or T+1
		
		String reqStr = "";
		String respResult = "";
		try {
			reqStr = util.jsonObject(reqMap);
			logger.info(appPayId + "_弘达代付确认请求报文：" + reqStr);
			respResult = SocketClient.connServer(reqStr);
			logger.info(appPayId + "_弘达代付确认返回报文：" + respResult);

			respResult = respResult.substring(4);

			JSONObject mapResult = JSONObject.fromObject(respResult);
			String respMsg = mapResult.getString(HdPayConstant.ORIGRESPMSG);
			if (mapResult.getString(HdPayConstant.RESPCODE).equals("00") && list.contains(mapResult.get(HdPayConstant.ORIGRESPCODE))) {// 表示成功
				mess.setErrorInfo(respMsg);
				mess.setFlag(true);
			}else{
				mess.setErrorInfo(respMsg);
				mess.setFlag(false);
			}
		} catch (Exception e) {
			logger.info(appPayId + "_弘达代付查询失败", e);
			e.printStackTrace();
			mess.setErrorInfo("query无响应");
			mess.setFlag(false);
		}
		insertMongoDbData(reqStr, respResult, appPayId, "QUERY");
		return mess;
	}
	
}
