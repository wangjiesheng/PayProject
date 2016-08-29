/**
 * Copyright 2015 UENPAY, Inc. All rights reserved.
 * project : ams
 * package ：com.uenpay.framework.constant
 * file : Constant.java
 * date ：2015年3月26日
 */
package com.pay.entity;

import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @author SongBang
 * Specification : 文档说明 : 定义全局常亮
 */
public final class UenpayConstant {
	
	
	/**
	 * system properties
	 */
	public static final String FILE_SEPARATOR = "file.separator";
	
	/**
	 * url分隔符
	 */
	public static final String URL_SEPARATOR = "/";
	
	
	/**
	 * system properties
	 */
	public static final String USER_HOME = "user.home";
	
	/**
	 * 打款序列号当前值
	 */
	public static final String PAY_SERIAL_NO_CUR_VAL = "pay.serialno.curval";
	
	/**
	 * 交易序列号当前值
	 */
	public static final String TRADE_SERIAL_NO_CUR_VAL = "trade.serialno.curval";
	
	/**
	 * project name
	 */
	public static final String PROJECT_NAME = "etc/mms";
//	public static final String PROJECT_NAME = "mms";
	
	/**
	 * configure file name
	 */
	public static final String CONFIGURE_FILE_NAME="mms-configure.properties";
	
	/**
	 * 保存在会话的用户信息KEY
	 */
	public static final String SESSION_USER_KEY = "LEGAL_USER";

	/**
	 * 员工号前缀
	 */
	public static final String COMPANY_PERSONNEL_PREFIX = "emp";
	
	/**
	 * 编码格式
	 */
	public static final String CHARACTER_ENCODING="UTF-8";
	
	/**
	 * ajax请求成功,返回码
	 */
	public static final String SUCCESS_RETURN_CODE = "0";
	
	/**
	 * error-code
	 */
	public static final String ERROR_CODE = "errorCode";
	
	/**
	 * error-message
	 */
	public static final String ERROR_MESSAGE = "errorMessage";
	
	
	/**
	 * ajax请求错误信息
	 */
	public static final String ERR_RESPONSE_DATA = "responseData";
	
	/**
	 * 系统中使用的缓存名称
	 */
	public static final String DEFAULT_CACHE_NAME = "uenpayCache";
	
	/**
	 * 缓存中Menu菜单key的名称
	 */
	public static final String CACHE_MENU_KEY = "MenuRootNode";
	
	/**
	 * 加载银联卡信息的key
	 */
	public static final String CACHE_UNION_FIT_KEY = "crdInfUnionFitKey";
	
	/**
	 * 缓存角色
	 */
	public static final String CACHE_ROLE_KEY = "RoleRootNode";
	
	/**
	 * 缓存所有常量数据key名称
	 */
	public static final String CACHE_ALL_CONSTANT_KEY = "AllConstants";
	
	/**
	 * 缓存所有常量数据key名称
	 */
	public static final String CACHE_ALL_PERM = "allPerm";
	
	/**
	 * 缓存中的Permission菜单key的名称
	 */
	public static final String CACHE_PERMI_KEY = "permiRootNode";
	
	/**
	 * 日期模式
	 */
	public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
	
	/**
	 * 时间模式
	 */
	public static final String TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	public static final String MINUTE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
	
	/**
	 * 默认版本号
	 */
	public static final int DATA_VERSION = 1;
	
	/**
	 * 默认数据状态   数据有效位  0:无效, 1:有效
	 */
	public static final String DATA_STATUS = "1";
	
	/**
	 * 日期正则表达式
	 */
	public static final String REG_DATE = "\\d{4}-\\d{2}-\\d{2}";
	
	/**
	 * 时间正则表达式
	 */
	public static final String REG_TIME="\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
	
	/**
	 * 时间到分钟正则表达式
	 */
	public static final String REG_MINUTE_TIME="\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}";

	/**
	 * 获得权限信息的key
	 */
	public static final String PERMISSION_KEY = "permissionPowerInfo";
	
	/**
	 * 用于获得权限集合的key
	 */
	public static final String POWERINFO_KEY = "powerInfoMap";
	
	/**
	 * 用于获得角色集合的key
	 */
	public static final String ROLES_KEY = "rolesMap";
	
	/**
	 * 尤恩和通道缓存中的key
	 */
	public static final String CACHE_UEN_CHANNEL_KEY = "cacheUenRelateChannel";
	
	/**
	 * 超级管理员
	 */
	public static final String SUPPER_MANAGER = "超级管理员";

	/**
	 * timestamp拼接时间格式  开始时间格式后缀
	 */
	public static final String START_TIME_POSTFIX = "00:00:00.000001";
	public static final String START_DATE_POSTFIX = "00:00:00";

	/**
	 * timestamp拼接时间格式  结束时间格式后缀
	 */
	public static final String END_TIME_POSTFIX = "23:59:59.999999";
	public static final String END_DATE_POSTFIX = "23:59:59";
	
	/**
	 * 默认的客户密码
	 */
	public static final String DEFAULT_CUSTOMER_PASSWORD = "123456";
	
	/**
	 * 主池
	 */
	public static final String MAIN_POOL_BUSI_TYPE = "10";
	
	/**
	 * T+0IC
	 */
	public static final String TA_POOL_BUSI_TYPE = "11";
	
	/**
	 * T+0磁条
	 */
	public static final String TB_POOL_BUSI_TYPE = "12";
	
	/**
	 * T+N
	 */
	public static final String TN_POOL_BUSI_TYPE = "13";
	
	/**
	 * 机构资金池
	 */
	public static final String ORG_POOL_BUSI_TYPE = "14";
	
	/**
	 * 退款子池
	 */
	public static final String RF_POOL_BUSI_TYPE = "15";
	
	/**
	 * 闪付子池
	 */
	public static final String QP_POOL_BUSI_TYPE = "16";
	
	/**
	 * 0待打款
	 */
	public static final String PAY_STATUS_PREPARE ="0";
	
	/**
	 * 0待处理
	 */
	public static final String DISPOSE_STATUS_PREPARE ="0";
	
	/**
	 * 1已打款
	 */
	public static final String PAY_STATUS_ALREADY ="1";
	
	/**
	 * 2打款失败
	 */
	public static final String PAY_STATUS_FAIL ="2";
	
	/**
	 * 3差错帐处理
	 */
	public static final String PAY_STATUS_MISTAKE ="3";
	
	/**
	 * 4暂不打款
	 */
	public static final String PAY_STATUS_TRANSIENCE = "4";
	
	/**
	 * 0系统自动打款
	 */
	public static final String OPRT_TYPE_AUTO_PAY = "0";
	
	/**
	 * 1人工手动打款
	 */
	public static final String OPRT_TYPE_HANDTO_PAY = "1";
	
	/**
	 * 2重打款
	 */
	public static final String OPRT_TYPE_REPEAT_PAY = "2";
	
	/**
	 * 3转差错帐处理
	 */
	public static final String OPRT_TYPE_MISTAKE_PAY = "3";
	
	/**
	 * 主池Id
	 */
	public static final int MAIN_POOL_ID = 1000;
	
	/**
	 * 公司员工
	 */
	public static final String USER_TYPE = "0";
	
	
	/**
	 * 通道交易码
	 */
	public static final String CHANNLE_TRADE_TXNCOD = "P10190";
	
	/**
	 * 通道查询码
	 */
	public static final String CHANNLE_QUERY_TXNCOD = "P10191";
	
	/**
	 * 实名认证类型 实名认证
	 */
	public static final String CERTIFIC_TYPE_SM="0";
	
	/**
	 * 实名认证类型 一级认证
	 */
	public static final String CERTIFIC_TYPE_CJ="1";
	
	/**
	 * 实名认证类型 二级认证
	 */
	public static final String CERTIFIC_TYPE_CC="2";
	
	/**
	 * 实名认证类型 三级认证
	 */
	public static final String CERTIFIC_TYPE_CCC="3";
	
	/**
	 * 认证等级 查询
	 */
	public static final String CERTIFIC_LEVEL_ZERO="0";
	
	/**
	 * 认证等级 实名
	 */
	public static final String CERTIFIC_LEVEL_ONE="1";
	
	/**
	 * 认证等级 一级
	 */
	public static final String CERTIFIC_LEVEL_TWO="2";
	
	/**
	 * 认证等级 二级
	 */
	public static final String CERTIFIC_LEVEL_THREE="3";
	
	/**
	 * 认证等级 三级
	 */
	public static final String CERTIFIC_LEVEL_FOUR="4";
	
	/**
	 * 认证状态 实名认证初始状态
	 */
	public static final String RN_CERTIFIC_STATUS_ONE="00";
	
	/**
	 * 认证状态 实名认证待审核
	 */
	public static final String RN_CERTIFIC_STATUS_TWO="01";
	
	/**
	 * 认证状态 实名认证已通过
	 */
	public static final String RN_CERTIFIC_STATUS_THREE="02";
	
	/**
	 * 认证状态 实名认证未通过
	 */
	public static final String RN_CERTIFIC_STATUS_FOUR="03";
	
	/**
	 * 认证状态 一级认证待审核
	 */
	public static final String SUPER_CERTIFIC_STATUS_ONE="04";
	
	/**
	 * 认证状态 一级认证已通过
	 */
	public static final String SUPER_CERTIFIC_STATUS_TWO="05";
	
	/**
	 * 认证状态 一级认证未通过
	 */
	public static final String SUPER_CERTIFIC_STATUS_THREE="06";
	
	/**
	 * 认证状态 二级认证待审核
	 */
	public static final String SSUPER_CERTIFIC_STATUS_ONE="07";
	
	/**
	 * 认证状态 二级认证已通过
	 */
	public static final String SSUPER_CERTIFIC_STATUS_TWO="08";
	
	/**
	 * 认证状态 二级认证未通过
	 */
	public static final String SSUPER_CERTIFIC_STATUS_THREE="09";
	
	/**
	 * 认证状态 三级认证待审核
	 */
	public static final String SSSUPER_CERTIFIC_STATUS_ONE="10";
	
	/**
	 * 认证状态 三级认证已通过
	 */
	public static final String SSSUPER_CERTIFIC_STATUS_TWO="11";
	
	/**
	 * 认证状态 三级认证未通过
	 */
	public static final String SSSUPER_CERTIFIC_STATUS_THREE="12";
	
	/**
	 * 鉴权状态 待鉴权
	 */
	public static final String AUTHENTICATION_STATUS_N="0";
	
	/**
	 * 鉴权状态 鉴权成功
	 */
	public static final String AUTHENTICATION_STATUS_Y="1";
	
	/**
	 * 鉴权状态 鉴权失败
	 */
	public static final String AUTHENTICATION_STATUS_F="2";
	
	/**
	 * 鸿达的通道机构编号  弘达移动线上
	 */
	public static final String HONG_DA_ORGNO = "01030104";
	
	/**
	 * 北京银商
	 */
	public static final String BJ_YIN_SHANG_ORGNO = "01030104";
	
	/**
	 * 提现限额
	 */
	public static final String QUOTA_TYPE_DEPOSIT = "1";
	
	/**
	 * 交易限额
	 */
	public static final String QUOTA_TYPE_TRADE = "0";
	
	/**
	 * 限额浮动金额
	 */
	public static final String QUOTA_AMOUNT="20000";
	
	/**
	 * IC卡
	 */
	public static final String OP_CARD_TYPE_IC="0";
	
	/**
	 * 磁条卡
	 */
	public static final String OP_CARD_TYPE_MAG="1";
	
	/**
	 * 系统添加记录设置的用户Id
	 */
	public static final Integer SYSTEM_USER_ID = 899;
	
	/**
	 * 退款到虚拟账户
	 */
	public static final String REFUND_TYPE_ACCOUNT="0";
	
	/**
	 * 退款到个人
	 */
	public static final String REFUND_TYPE_PERSON="1";
	
	/**
	 * 计算金额需要精确的精度
	 */
	public static final MathContext ROUNDING_MODE_CEILING = new MathContext(4, RoundingMode.CEILING);

}
