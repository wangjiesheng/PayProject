package com.pay.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="EPOSPROTOCOL")
@XmlAccessorType(XmlAccessType.FIELD)
public class QuickPayRequest {
	@XmlElement(name="APPPAYID")
	private String appPayId;
	
	@XmlElement(name="ACCOUNTNAME")
	private String accountName;
	
	@XmlElement(name="CARDNO")
	private String cardNo;
	
	@XmlElement(name="PAYAMOUNT")
	private String payAmount;
	
	@XmlElement(name="TIME")
	private String time;
	
	@XmlElement(name="BANKNAME")
	private String bankName;
	
	@XmlElement(name="TERMINALNO")
	private String terminalNo;
	
	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}
	/**
	 * 调用哪个接口修改打款状态
	 */
	@XmlElement(name="UPDATERESULTURL")
	private String updateResultUrl;
	
	/**
	 * 1:表示银企直连 ​
	 * 2:表示代付垫支 
	 * 3:表示代付T+0
	 */
	@XmlElement(name="PAYMENTCHANNEL")
	private String paymentChannel;
	
	@XmlElement(name="FUNDPOOLTYPE")
	private String fundPoolType;
	
	public String getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public String getAppPayId() {
		return appPayId;
	}

	public void setAppPayId(String appPayId) {
		this.appPayId = appPayId;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getFundPoolType() {
		return fundPoolType;
	}

	public void setFundPoolType(String fundPoolType) {
		this.fundPoolType = fundPoolType;
	}

	public String getUpdateResultUrl() {
		return updateResultUrl;
	}

	public void setUpdateResultUrl(String updateResultUrl) {
		this.updateResultUrl = updateResultUrl;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Override
	public String toString() {
		return "QuickPayRequest [appPayId=" + appPayId + ", accountName="
				+ accountName + ", cardNo=" + cardNo + ", payAmount="
				+ payAmount + ", time=" + time + ", bankName=" + bankName
				+ ", updateResultUrl=" + updateResultUrl + ", paymentChannel="
				+ paymentChannel + ", fundPoolType=" + fundPoolType + "]";
	}
	
}
