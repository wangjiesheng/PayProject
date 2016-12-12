package com.pay.entity;
/**
 *@author lb
 *2016年1月27日
 */
public class HdPayConstant {
	
	/**--------------------------------------------------------------*/
	public static final String VERSION="version";  // 版本号                    
	public static final String TXNTYPE="txnType";  // 交易类型 
	public static final String ENTERPRISEID="enterpriseID";  //企业ID
	public static final String SETTTYPE="settType";  //清算时效标志
	public static final String OLDMERID="oldMerid";  //原消费交易商户号
	public static final String SIGNATURE="signature";  //签名
	public static final String SIGNMETHOD="signMethod";  //签名方法
	public static final String TXNTIME="txnTime";  //订单发送时间
	public static final String ACCNO="accNo";  //收款人账号
	public static final String BACKURL="backUrl";  //后台通知地址
	public static final String TXNAMT="txnAmt";  //交易金额
	public static final String ENTERPRISENO="enterpriseNo";  //企业编号
	public static final String ORDERID="orderId";  //商户订单号
	public static final String BANKNO="bankNo";  //收款人开户行行号
	public static final String BANKNAME="BankName";  //收款人银行中文名称
	public static final String PAYNAME="payName";  //收款人名称
	public static final String PPTYPE="ppType";  //公/私标识
	public static final String NOTE="note";  //备注
	public static final String TERMINALNO="terminalNo";  //终端号
	
	
	
	/**--------------------------------------------------------------*/
	public static final String RESPCODE="respCode";  //代付返回码
	public static final String RESPMSG="respMsg";  //代付返回信息
	public static final String QUERYID="queryId";  //查询ID
	
	public static final String ORIGRESPCODE="origRespCode";  //确认返回码
	public static final String ORIGRESPMSG="origRespMsg";  //确认返回信息
	
	public static String PAYTYPE="0"; //0代表走T+0，1代表走T+1
	

}
