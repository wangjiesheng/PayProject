/**
 * Copyright 2015-2020 UENPAY ASLAN, Inc. All rights reserved.
 * project : payment
 * package ：com.pay.controller
 * file : AppPayPortController.java
 * date ：2016年8月5日
 */
package com.pay.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pay.entity.FundPay;
import com.pay.entity.MessageHandle;
import com.pay.entity.QuickPayRequest;
import com.pay.framework.appcrypt.AES;
import com.pay.framework.appcrypt.CryptUtils;
import com.pay.framework.util.HttpUtils;
import com.pay.framework.util.PayUtil;
import com.pay.framework.util.ThreadPoolFactory;
import com.pay.service.impl.HdPayJsonPublicMethod;
import com.pay.service.impl.HdPayPublicMethod;
import com.pay.service.impl.HdWaitHandle;

/**
 * @author wangjiesheng
 * Specification : 文档说明:
 */
@Controller
@RequestMapping("/appPort")
public class AppPayPortController {
	
	private Logger logger = LogManager.getLogger(AppPayPortController.class);
	
	
	/**
	 * AES seed
	 */
	public static  String password = "dynamicode";
	
	/**
	 * 弘达代付T+0和闪付调用
	 * @param requestParam
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="hdPayAction", method=RequestMethod.POST)
	public void hdPayAction(@RequestParam(value="requestParam") String requestParam, HttpServletResponse response)throws Exception{
		String transDataXml = AES.decryptString(requestParam, CryptUtils.encryptToMD5(password));
		QuickPayRequest quickPayRequest = com.pay.framework.util.XmlConvert.convertToJavaBean(transDataXml, QuickPayRequest.class);
		String sonPoolType = quickPayRequest.getFundPoolType();
		String appPayId = quickPayRequest.getAppPayId();
		String accountName = quickPayRequest.getAccountName();
		String cardNo = quickPayRequest.getCardNo();
		String payAmount = quickPayRequest.getPayAmount();
		String time = quickPayRequest.getTime();
		String bankName = quickPayRequest.getBankName();
		String paymentChannel = quickPayRequest.getPaymentChannel();
		String amsPayHandleUrl = quickPayRequest.getUpdateResultUrl();
		String terminalNo = quickPayRequest.getTerminalNo();

		MessageHandle mess = null;
		
		FundPay fundPay = new FundPay(appPayId, accountName, cardNo, payAmount, time, paymentChannel, bankName, terminalNo);
		
		boolean isMessage = false;//是否需要通知
		//1、-------------------------------弘达代付打款-------------------------------------------
		String operation = "";
		if("4".equals(paymentChannel)){//4 弘达T+N代付,5 弘达T+0代付
			operation = "1";
		}else if("5".equals(paymentChannel)){
			operation = "0";
		}
		mess = HdPayPublicMethod.HdpayPubilc(fundPay, operation, isMessage);//0：表示T+0业务1：表示T+1业务
		fundPay.setPaySeqNo(fundPay.getTime() + ":" + mess.getPaySeqNo());// time+queryId
		
		//4-------------------->针对这样的错误不需要再次查询：错误信息(监管资金不足)等                         S1结算卡号不符  Z7监管资金不足
		if(("Z7".equals(mess.getErrorCode()) || "S1".equals(mess.getErrorCode())
				|| "系统异常".equals(mess.getRespMsg()) || "不予承兑".equals(mess.getRespMsg())) && mess.isFlag()){//直接失败,不用查询了,这些情况需要清算自己去核实款项
			mess.setFlag(false);
			mess.setErrorInfo(mess.getRespMsg());
		}
		
		String payMsg = mess.getRespMsg();//发送打款返回的信息
		
		//5、------------------>代付如果发送成功了,就执行查询操作
		if(mess.isFlag()){
			// (1)、--------------->弘达代付发起查询交易，等待2s
			Thread.sleep(2000);
			mess = HdPayPublicMethod.HdQueryPublic(fundPay,operation, isMessage);
			mess.setRespMsg(payMsg);
			
			// (2)、--------------->第一次不成功时 -->再次查询一遍
			if (!mess.isFlag()) {
				if("交易已受理，请稍后查询交易结果".equals(mess.getErrorInfo())){
					isMessage = true;//遇到这样的错误表示需要用线程进行等待
				}else{
					Thread.sleep(3000);// 等待3s
					mess = HdPayPublicMethod.HdQueryPublic(fundPay,operation, isMessage);
					mess.setRespMsg(payMsg);
				}
			}
		}
		
		//6、如果有需要走通知的就走通知流出,否则走正常的修改状态逻辑.
		if(isMessage){//走异步通知流程
			mess.setResultCode(appPayId);//如果是走异步通知的，resultCode等于
			mess.setPaySeqNo(fundPay.getPaySeqNo());
			ThreadPoolFactory.execute(new HdWaitHandle(sonPoolType, fundPay, amsPayHandleUrl, operation));//采用线程池等待方式处理,通知方法已注释
		}else{
			String resultCode = "404";// 默认请求异常标志
			try {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("appPayId", appPayId);
				params.put("sonPoolType", sonPoolType);
				params.put("paymentChannel", paymentChannel);
				logger.info("ams请求修改状态http地址------------>" + amsPayHandleUrl + ",参数:" + params.toString());
				if (mess.isFlag()) {
					resultCode = HttpUtils.postRequestByString(amsPayHandleUrl + "/successAction", params);
				} else {// 代付不成功时记录原因
					params.put("errorInfo", mess.getRespMsg()+"-->"+mess.getErrorInfo());//代付不成功原因:打款返回的信息-->加上查询返回的信息
					resultCode = HttpUtils.postRequestByString(amsPayHandleUrl + "/failingAction", params);
				}
				logger.info("ams修改状态返回结果------"+appPayId+"------>" + resultCode);
			} catch (Exception e) {
				logger.info("弘达代付更新数据异常......", e);
			}
			mess.setResultCode(mess.isFlag() + resultCode);// ams的处理情况也返回给rms,这个数据要存到fund_shop_pre_pay表中
			mess.setPaySeqNo(fundPay.getPaySeqNo());
		}
		PayUtil.repData(response, JSONObject.wrap(mess).toString());
	}
	
	/** 公开的接口给内部使用
	 * @param requestParam
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="hdPayJsonAction", method=RequestMethod.POST)
	public void hdPayJsonAction(@RequestParam(value="requestParam") String requestParam,HttpServletResponse response) throws Exception{
		try {
			String data = AES.decryptString(requestParam, CryptUtils.encryptToMD5(password));//请求加密,返回不需要加密
			String resultJson = HdPayJsonPublicMethod.HdpayJsonPubilc(data);
			PayUtil.repData(response, resultJson);
		} catch (Exception e) {
			JSONObject resultJson = new JSONObject();
			resultJson.put("respCode", "99");
			resultJson.put("respMsg", "未知错误");
			PayUtil.repData(response, resultJson.toString());
		}
	}
	
	/** 公开查询的接口给内部使用
	 * @param requestParam
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="hdQueryJsonAction", method=RequestMethod.POST)
	public void hdQueryJsonAction(@RequestParam(value="requestParam") String requestParam,HttpServletResponse response) throws Exception{
		try {
			String data = AES.decryptString(requestParam, CryptUtils.encryptToMD5(password));//请求加密,返回不需要加密
			String resultJson = HdPayJsonPublicMethod.HdQueryJsonOrderId(data);
			PayUtil.repData(response, resultJson);
		} catch (Exception e) {
			JSONObject resultJson = new JSONObject();
			resultJson.put("respCode", "99");
			resultJson.put("respMsg", "未知错误");
			PayUtil.repData(response, resultJson.toString());
		}
	}
	
	/**
	 * 弘达代付查询结果
	 * @param requestParam
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="queryHdPayAction", method=RequestMethod.POST)
	public void queryHdPayAction(@RequestParam(value="requestParam") String requestParam,HttpServletResponse response)throws Exception{
		String transDataXml = AES.decryptString(requestParam, CryptUtils.encryptToMD5(password));
		logger.info("上送报文:"+transDataXml);
		JSONObject rspJsonObject = new JSONObject(transDataXml);
		String appPayId = rspJsonObject.getString("appPayId");
		String paymentChannel = rspJsonObject.getString("paymentChannel");
		String operation = "4".equals(paymentChannel) ? "1" : "5".equals(paymentChannel) ? "0" : "99";//4 弘达T+N代付,5 弘达T+0代付
		
		MessageHandle mess = HdPayPublicMethod.HdQueryOrderId(appPayId,operation);
		String jsonStr = JSONObject.wrap(mess).toString();
		logger.info("查询代付返回结果"+appPayId+"--------->"+jsonStr);
		String encryptJsonStr = AES.encryptString(jsonStr, CryptUtils.encryptToMD5(password));//json字符串加密
		PayUtil.repData(response, encryptJsonStr);
	}
}
