/**
 * Copyright 2015-2020 UENPAY ASLAN, Inc. All rights reserved.
 * project : mms
 * package ：com.uenpay.entity.ams
 * file : MessageHandle.java
 * date ：2015年12月29日
 */
package com.pay.entity;

/**
 * @author wangjiesheng
 * Specification : 文档说明:
 */
public class MessageHandle {

	/**
	 * 是否成功
	 */
	private boolean flag;

	/**
	 * 返回信息
	 */
	private String errorCode;
	
	/**
	 * 返回信息
	 */
	private String errorInfo;//查询返回信息
	
	private String respMsg;//打款返回信息

	/**
	 * ams处理状态返回的信息
	 */
	private String resultCode;
	
	/**
	 * 打款查询id
	 */
	private String paySeqNo;
	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getPaySeqNo() {
		return paySeqNo;
	}

	public void setPaySeqNo(String paySeqNo) {
		this.paySeqNo = paySeqNo;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public MessageHandle(boolean flag, String errorCode, String errorInfo,
			String respMsg) {
		super();
		this.flag = flag;
		this.errorCode = errorCode;
		this.errorInfo = errorInfo;
		this.respMsg = respMsg;
	}
	
	public MessageHandle() {
		super();
	}

	@Override
	public String toString() {
		return "MessageHandle [flag=" + flag + ", errorCode=" + errorCode
				+ ", errorInfo=" + errorInfo + ", resultCode=" + resultCode
				+ ", paySeqNo=" + paySeqNo + "]";
	}

}
