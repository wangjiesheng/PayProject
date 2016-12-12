/**
 * Copyright 2015-2020 UENPAY ASLAN, Inc. All rights reserved.
 * project : mms
 * package ：com.uenpay.service.ams.impl
 * file : HdPayPublicMethod.java
 * date ：2016年2月23日
 */
package com.pay.service.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pay.entity.HdPayConstant;
import com.pay.framework.socket.hd.SocketClient;
import com.pay.framework.socket.hd.Util;
import com.pay.framework.util.PayUtil;


/**
 * @author wangjiesheng
 * Specification : 文档说明:
 */
public class HdPayJsonPublicMethod extends PayUtil{
	
	public static Logger logger = LogManager.getLogger(HdPayJsonPublicMethod.class);
	
	private static List<String> list = Arrays.asList("00","A6");//00表示成功,06表示有缺陷的成功
	
	/*public static void main(String[] args) {
		
		{"txnType":"12","enterpriseNo":"11005","payName":"何星","terminalNo":"10847653","ppType":"0","BankName":"中信银行股份有限公司",
			"version":"1.0","txnAmt":"999","enterpriseID":"09B9E7F4FC64CFB5E504CFDE61D0F356","oldMerid":"996381156510001",
			"accNo":"6226960200902047","signMethod":"01","backUrl":"211.147.87.18:9000","settType":"0",
			"signature":"UERWSUpLbUoydjJqM09YT21nU3FyMzRTVDJNUE5COWdOeis4MnM2bVBoRTFpeVIxNVU4VGFDRERa\nSXdob1ZRbEpGMTM1cUJ5"
					+ "THhmSzVsa3BLM25YU2pKTUVkTlNnRjl0OHFQNHpKbW5tQ1YwRVJmQmho\naC9MNVFjc3JYOWdnTWhlNXY1eVVaVVBRYVBOOXpJa"
					+ "zNJcFp4UEIxL2tjeld4Q3NZZ2NrbmFVc3pS\naVUvTTVySVpLZXBkQ2tQcTVFaGtrdVA1SWdIWHBFMWhYbnQ0NjBPTUI4b25lWlJ"
					+ "ucDE1OUFSRXgv\neHFpeXgvVjNYOFZwK2V2OEQvTGFqVEZ5WDk0Wm4rRDJKYzF0Z0xsYkJTQXE1M0hOVnp4SGt5elRQ\nTjlxaGFw"
					+ "cGl5MzdjT1QwVVZvNVY2aVF5cURaQVFmN1dYejJCakxXTlBRendNNlhGb0JGdnN1WEJB\nPT0=","note":"尤恩代付newT+0业务20000",
					"orderId":"N16120600015829","txnTime":"20161206223331"}
		
		JSONObject js = new JSONObject();
		js.put("settType", "0");
		js.put("appPayId", "161206009990005");
		js.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		js.put("accNo", "6226960200902047");
		js.put("txnAmt", "1");
		js.put("terminalNo", "0");
		js.put("bankName", "中信银行股份有限公司");
		js.put("accountName", "何星");
		
		System.out.println(HdpayJsonPubilc(js.toString()));
	}*/
	
	public static String HdpayJsonPubilc(String data){
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
			resultJson.put("respMsg", "pay未知错误");
			respResult = resultJson.toString();
		}
		return respResult;
	}
	 
	/**
	 * 直接根据订单号查询代付打款结果
	 * @param appPayId
	 * @return
	 */
	public static String HdQueryJsonOrderId(String data){
		JSONObject json = JSONObject.fromObject(data);
		String appPayId = json.getString("appPayId");
		String settType = json.getString("settType");
		
		Util util=new Util();

		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put(HdPayConstant.VERSION, "1.0");
		reqMap.put(HdPayConstant.TXNTYPE, "00");
		reqMap.put(HdPayConstant.ENTERPRISEID, getBeanValue("enterpriseID"));
		reqMap.put(HdPayConstant.SIGNMETHOD, "01");
		reqMap.put(HdPayConstant.ENTERPRISENO, getBeanValue("enterpriseNo"));
		reqMap.put(HdPayConstant.ORDERID,"N"+appPayId);//被查询交易的订单号
		reqMap.put(HdPayConstant.SETTTYPE, settType);//T+0 or T+1
		
		String reqStr = "";
		String respResult = "";
		try {
			reqStr = util.jsonObject(reqMap);
			logger.info(appPayId + "_弘达代付确认请求报文：" + reqStr);
			respResult = SocketClient.connServer(reqStr);
			logger.info(appPayId + "_弘达代付确认返回报文：" + respResult);
			respResult = respResult.substring(4);
		
		} catch (Exception e) {
			logger.info(appPayId + "_弘达代付查询失败", e);
			e.printStackTrace();
			JSONObject resultJson = new JSONObject();
			resultJson.put("respCode", "99");
			resultJson.put("respMsg", "query未知错误");
			respResult = resultJson.toString();
		}
		return respResult;
	}
	

	/**
	 * 查询余额接口
	 * @param settType 清算类型 0表示T+0 ; 1表示T+1
	 * @return
	 */
	public static String queryHdBalance(String settType){
		Util util=new Util();
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put(HdPayConstant.VERSION, "1.0");
		reqMap.put(HdPayConstant.TXNTYPE, "01");
		reqMap.put(HdPayConstant.ENTERPRISEID, getBeanValue("enterpriseID"));
		reqMap.put(HdPayConstant.SIGNMETHOD, "01");
		reqMap.put(HdPayConstant.ENTERPRISENO, getBeanValue("enterpriseNo"));
		reqMap.put(HdPayConstant.SETTTYPE, settType);//T+0 or T+1
		String reqStr = util.jsonObject(reqMap);
		String respResult = SocketClient.connServer(reqStr);
		respResult = respResult.substring(4);
		JSONObject mapResult = JSONObject.fromObject(respResult);
		return mapResult.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(queryHdBalance("0"));;
	}
	
}
