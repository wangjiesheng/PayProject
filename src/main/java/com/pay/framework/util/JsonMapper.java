package com.pay.framework.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pay.entity.HdPayConstant;

/**
 * @author lb 2015年9月10日
 */
public class JsonMapper extends ObjectMapper {

	public static ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * JsonMapper
	 */
	private static final long serialVersionUID = 1L;

	public JsonMapper() {
		super();
	}

	public static String resultJson(Object obj) {
		String resultStr = "";
		try {
			resultStr = objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return resultStr;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, String> readJsonMap(String json) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			map = objectMapper.readValue(json, HashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> readJson2Map(String json) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		try {
			map = objectMapper.readValue(json, HashMap.class);
			if (map.containsKey("responsedata")) {
				map2 = (Map<String, Object>) map.get("responsedata");
				Set<String> key2 = map2.keySet();
				Iterator<String> iter2 = key2.iterator();
				while (iter2.hasNext()) {
					String field = iter2.next();
					map.put(field, map2.get(field));
				}
				map.remove("responsedata");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static void main(String[] args) {
		Map<String, String> resultMap = new HashMap<String, String>();
		Map<String, String> mapResult = new HashMap<String, String>();
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put(HdPayConstant.VERSION, "1.0");
		reqMap.put(HdPayConstant.TXNTYPE, "12");
		reqMap.put(HdPayConstant.SIGNMETHOD, "01");
//		reqMap.put(HdPayConstant.TXNTIME,hdPayData.getCreateDateTime());
//		reqMap.put(HdPayConstant.ACCNO, hdPayData.getActNo());
//		reqMap.put(HdPayConstant.TXNAMT, hdPayData.getPayAmt());
		reqMap.put(HdPayConstant.ENTERPRISENO, "11201");
		reqMap.put(HdPayConstant.ENTERPRISEID, "sadfasfa");
//		reqMap.put(HdPayConstant.ORDERID,hdPayData.getSumId());
		reqMap.put(HdPayConstant.BANKNO, "");
//		reqMap.put(HdPayConstant.BANKNAME,hdPayData.getBankName());
//		reqMap.put(HdPayConstant.PAYNAME, hdPayData.getActName());
		reqMap.put(HdPayConstant.PPTYPE, "0");
		reqMap.put(HdPayConstant.NOTE, "尤恩代付");
		reqMap.put(HdPayConstant.SETTTYPE, "1");//0：表示T+0业务1：表示T+1业务
		String reqStr=JsonMapper.resultJson(reqMap);
		
		System.out.println(reqStr);
	}



}
