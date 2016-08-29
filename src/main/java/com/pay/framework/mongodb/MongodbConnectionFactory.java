/**
 * 
 */
package com.pay.framework.mongodb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.pay.framework.util.PropertiesUtils;

/**
 * @author dell
 *
 */
public class MongodbConnectionFactory {

	/**
	 * logger
	 */
	private static Logger logger = LogManager.getLogger(MongodbConnectionFactory.class);

	/**
	 * mongoClient
	 */
	private static MongoClient client = null;

	/**
	 * mongodb url
	 */
	private static final String MONGODB_LOGDB_URL_KEY = "mongodb.logdb.url";

	/**
	 * 根据名称获取DB，相当于是连接
	 * 
	 * @param dbName
	 * @return
	 */
	public static MongoClient getMongoClient() {
		return client;
	}

	public static void initMongo(String urlKey){
		try {
			String urlStr = PropertiesUtils.getPropertiesVal(urlKey);
			MongoClientOptions.Builder build = new MongoClientOptions.Builder().connectionsPerHost(400) // 与目标数据库能够建立的最大connection数量为50
					.threadsAllowedToBlockForConnectionMultiplier(400) // 如果当前所有的connection都在使用中，则每个connection上可以有50个线程排队等待
					.maxWaitTime(1000 * 30).connectTimeout(1000 * 60 * 1).cursorFinalizerEnabled(false);
			MongoClientURI uri = new MongoClientURI(urlStr, build);
			client = new MongoClient(uri);

		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	/**
	 * 初始化连接池，设置参数。
	 */
	static {
		initMongo(MONGODB_LOGDB_URL_KEY);
	}
}
