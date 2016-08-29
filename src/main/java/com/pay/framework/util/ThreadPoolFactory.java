/**
 * Copyright 2015-2020 UENPAY ASLAN, Inc. All rights reserved.
 * project : ams-wjs
 * package ：com.uenpay.framework.util
 * file : ThreadPoolFactory.java
 * date ：2015年12月7日
 */
package com.pay.framework.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author wangjiesheng
 * Specification : 文档说明:线程池
 */
public class ThreadPoolFactory {
	
	public static ExecutorService ThreadPool = null;
	
	static{
		ThreadPool = Executors.newFixedThreadPool (100);
	}
	
	public static void execute(Runnable command){
		ThreadPool.execute(command);
	}
	
	public static void shutdown(){
		ThreadPool.shutdown();
	}
	
	public static boolean isTerminated(){
		return ThreadPool.isTerminated();
	}
	
	public static boolean awaitTermination() throws Exception{
		return ThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
	}
	
}
