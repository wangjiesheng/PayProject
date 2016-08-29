/**
 * 
 */
package com.pay.framework.mongodb;

/**
 * @author dell
 *
 */
public abstract class AbstractParamsLog {
	/**
	 * mongodb dbname
	 */
	protected static final String MONGODB_NAME = "logdb";

	/**
	 * dbcollection name
	 */
	private static final String COLLECTION_NAME = "mms_requestparams";

	/**
	 * document request url key
	 */
	protected static final String REQUEST_URL_KEY = "requesturl";

	/**
	 * document uuid key
	 */
	protected static final String UUID_KEY = "uuid";

	/**
	 * document request params key
	 */
	protected static final String REQUEST_PARAMS_KEY = "requestparams";
	
	/**
	 * document request params key
	 */
	protected static final String RESPONSE_PARAMS_KEY = "responseparams";

	/**
	 * document emp no key
	 */
	protected static final String EMP_NO_KEY = "empno";

	/**
	 * document request time
	 */
	protected static final String REQUEST_TIME_KEY = "requesttime";
	
	/**
	 * document request time
	 */
	protected static final String REQUEST_TYPE = "request_type";
	
	protected static final String APP_PAY_ID = "app_pay_id";
	
	protected static final String PARAM = "param";
	
	protected static final String DF_COLLECTION_NAME = "hddf_log";//代付打款的集合名称
	
}
