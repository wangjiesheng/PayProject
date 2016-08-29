/**
 * 
 */
package com.pay.framework.mongodb;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * @author dell
 *
 */
public class MongoDbUtil {
	
	/**
	 * logger
	 */
	private static Logger loger = LogManager.getLogger(MongoDbUtil.class);
	
	/**
	 * 添加请求数据
	 * @param dbName
	 * @param collectionName
	 * @param datas
	 */
	public static void insertData(String dbName, String collectionName, Map<String, Object> datas) {
		MongoClient client = MongodbConnectionFactory.getMongoClient();
		DB db = null;
		try {
			db = client.getDB(dbName);
			DBCollection collection = db.getCollection(collectionName);
			DBObject dbObject = new BasicDBObject(datas);
			collection.insert(dbObject);
		} catch (Exception e) {
			loger.error(e);
		} finally {
			if (db != null) {
				try {
					db.requestDone();
					db = null;
				} catch (Exception e) {
					// nothing to do
				}
			}
		}

	}
	
	
	
	/**
	 * 添加请求数据
	 * @param dbName
	 * @param collectionName
	 * @param datas
	 */
	public static void insertStringData(String dbName, String collectionName, Map<String, String> datas) {
		MongoClient client = MongodbConnectionFactory.getMongoClient();
		DB db = null;
		try {
			db = client.getDB(dbName);
			DBCollection collection = db.getCollection(collectionName);
			DBObject dbObject = new BasicDBObject(datas);
			collection.insert(dbObject);
		} catch (Exception e) {
			loger.error(e);
		} finally {
			if (db != null) {
				try {
					db.requestDone();
					db = null;
				} catch (Exception e) {
					// nothing to do
				}
			}
		}

	}
	
}
