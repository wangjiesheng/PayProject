/**
 * Copyright 2015-2020 UENPAY ASLAN, Inc. All rights reserved.
 * project : mms
 * package ：com.uenpay.framework.util
 * file : amsHttpUtils.java
 * date ：2015年12月30日
 */
package com.pay.framework.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

/**
 * http请求封装
 * 
 * @author OJH
 * 2015年7月31日
 */
public class HttpUtils {
	
	//默认编码
	private static String defaultEncoding = "UTF-8";
	
	public static String password = "dynamicode";
	
	/**
	 * http客户端组件
	 */
	private static HttpClient httpClient =  null;
	
	static{
		// 设置组件参数, HTTP协议的版本,1.1/1.0/0.9   
	    HttpParams params = new BasicHttpParams();   
	    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);   
	    HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");   
	    HttpProtocolParams.setUseExpectContinue(params, true);      
	  
	    //设置连接超时时间   
	    int REQUEST_TIMEOUT = 2*60*1000;  //设置请求超时10秒钟   
	    int SO_TIMEOUT = 2*60*1000;       //设置等待数据超时时间10秒钟   
	    //HttpConnectionParams.setConnectionTimeout(params, REQUEST_TIMEOUT);  
	    //HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);  
	    params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIMEOUT);    
	    params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);   
	    
	    //设置访问协议   
	    SchemeRegistry schreg = new SchemeRegistry();    
	    schreg.register(new Scheme("http",80,PlainSocketFactory.getSocketFactory()));   
	    //schreg.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));         
	      
	    //多连接的线程安全的管理器   
	    PoolingClientConnectionManager pccm = new PoolingClientConnectionManager(schreg);  
	    pccm.setDefaultMaxPerRoute(50); //每个主机的最大并行链接数   
	    pccm.setMaxTotal(200);          //客户端总并行链接最大数      
	      
	    httpClient = new DefaultHttpClient(pccm, params);  
	}
	
	
	public static String getDefaultEncoding() {
		return defaultEncoding;
	}

	/**
	 * post请求
	 * @param url
	 * @param params
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static byte[] postRequest(URI uri, Map<String,Object> params) throws ClientProtocolException, IOException{		
		HttpPost postMethod = new HttpPost(uri);
		List<BasicNameValuePair>  paramsList = new ArrayList<BasicNameValuePair>();
		byte[] data = null;
		if(params != null){
			for(String key : params.keySet()){
				Object value = params.get(key);
				if(value instanceof List){
					List<?> valueList = (List<?>)value;
					for(Object val : valueList){
						paramsList.add(new BasicNameValuePair(key, (String)val));
					}
					
				}else{
					paramsList.add(new BasicNameValuePair(key, (String)params.get(key)));
				}
			}
		}
		
		try{
			UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(paramsList, defaultEncoding);
			postMethod.setEntity(requestEntity);
			HttpResponse response = httpClient.execute(postMethod);
			data = EntityUtils.toByteArray(response.getEntity());
		}finally{
			postMethod.releaseConnection();
		}
		
		return	data;
	}
	
	/**
	 * post请求
	 * @param uriStr
	 * @param params
	 * @return
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static byte[] postRequest(String uriStr, Map<String, Object> params) throws ClientProtocolException, IOException, URISyntaxException{
		return postRequest(new URI(uriStr), params);
	}
	
	
	/**
	 * post请求
	 * @param uri
	 * @param params
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String postRequestByString(URI uri, Map<String,Object> params) throws ClientProtocolException, IOException{
		byte[] data = postRequest(uri, params);
		return new String(data, defaultEncoding);
	}
	
	
	/**
	 * post请求
	 * @param uri
	 * @param params
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws URISyntaxException 
	 */
	public static String postRequestByString(String uriStr, Map<String,Object> params) throws ClientProtocolException, IOException, URISyntaxException{
		byte[] data = postRequest(uriStr, params);
		return new String(data, defaultEncoding);
	}
	
	public static HttpClient getHttpClient(){
		return httpClient;
	}
	/*public static void main(String[] args) throws Exception, IOException, URISyntaxException {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("appPayId", "16042400017317");
		String str = postRequestByString("http://192.168.1.57:8080/ams/appPort/log", params);
		System.out.println(str);
	}*/
	
	public static void main(String[] args) throws Exception {
		addHKShop();
	}
	
	public static void addHKShop() throws Exception{
		Map<String,Object> sendMaps = new HashMap<String, Object>();
		sendMaps.put("param", "王结胜");
		postRequestByString("http://192.168.1.57:8899/mavenProjectDemo/appPort/appInterface", sendMaps);
	}
	
}
