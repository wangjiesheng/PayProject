/**
 * Copyright 2015-2020 UENPAY ASLAN, Inc. All rights reserved.
 * project : mms
 * package ：com.uenpay.entity.ams
 * file : FundPay.java
 * date ：2015年12月29日
 */
package com.pay.entity;

/**
 * @author wangjiesheng
 * Specification : 文档说明:打款实体类
 */
public class FundPay {

	private String appPayId;
	/**
	 * 持卡人姓名
	 */
	private String accountName;
	
	/**
	 * 卡号
	 */
	private String cardNo;
	
	/**
	 * 总行名称
	 */
	private String bankName;
	
	/**
	 * 打款金额
	 */
	private String payAmount;

	/**
	 * 打款通道
	 */
	private String paymentChannel;
	
	private String payStatus;
	
	private Integer version;
	
	private String time;
	
	private String paySeqNo;
	
	private String terminalNo;
	
	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getAppPayId() {
		return appPayId;
	}

	public void setAppPayId(String appPayId) {
		this.appPayId = appPayId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public FundPay() {
		super();
	}

	public FundPay(String appPayId, String accountName, String cardNo,
			String payAmount, String time, String paymentChannel) {
		super();
		this.appPayId = appPayId;
		this.accountName = accountName;
		this.cardNo = cardNo;
		this.payAmount = payAmount;
		this.time = time;
		this.paymentChannel = paymentChannel;
	}
	
	public FundPay(String appPayId, String accountName, String cardNo,
			String payAmount, String time, String paymentChannel,String bankName,String terminalNo) {
		super();
		this.appPayId = appPayId;
		this.accountName = accountName;
		this.cardNo = cardNo;
		this.payAmount = payAmount;
		this.time = time;
		this.paymentChannel = paymentChannel;
		this.bankName = bankName;
		this.terminalNo = terminalNo;
	}

	public FundPay(String appPayId, String paymentChannel, String time) {
		super();
		this.appPayId = appPayId;
		this.paymentChannel = paymentChannel;
		this.time = time;
	}
	
	
	public FundPay(String appPayId, String paySeqNo) {
		super();
		this.appPayId = appPayId;
		this.paySeqNo = paySeqNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getPaySeqNo() {
		return paySeqNo;
	}

	public void setPaySeqNo(String paySeqNo) {
		this.paySeqNo = paySeqNo;
	}

	@Override
	public String toString() {
		return "FundPay [appPayId=" + appPayId + ", accountName=" + accountName
				+ ", cardNo=" + cardNo + ", bankName=" + bankName
				+ ", payAmount=" + payAmount + ", paymentChannel="
				+ paymentChannel + ", version=" + version + ", time=" + time
				+ "]";
	}

}
