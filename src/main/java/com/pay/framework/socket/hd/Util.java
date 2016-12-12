package com.pay.framework.socket.hd;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import cfca.sadk.algorithm.common.Mechanism;
import cfca.sadk.algorithm.common.PKIException;
import cfca.sadk.lib.crypto.JCrypto;
import cfca.sadk.lib.crypto.Session;
import cfca.sadk.util.KeyUtil;
import cfca.sadk.util.Signature;
import cfca.sadk.x509.certificate.X509Cert;



/**
 *@author lb
 *2016年2月16日
 */
public class Util {
	
	/**
	 * 对map排序
	 * @param contentData
	 * @return
	 */
	public  Map<String, String> signData(Map<String, ?> contentData) {
		Map<String, String> submitFromData = new TreeMap<String, String>();
		Object[] key_arr = contentData.keySet().toArray();  
		Arrays.sort(key_arr);  
		for  (Object key : key_arr) {  
			Object value = contentData.get(key);  
			if(value!=null&&StringUtils.isNotBlank(value.toString())){
				if(!key.equals("signature")){
					submitFromData.put(key.toString().trim(), value.toString().trim());
				}
			}
		}  
		return submitFromData;
	}
	
	/**
	 * 
	 *********************************************************.<br>
	 * [方法] checkCert <br>
	 * [描述] (校验证书) <br>
	 * [参数] sourceData(原数据),cmbcCertPath(校验证书路径), signature(待校验的签名)<br>
	 * [返回] boolean <br>
	 * [时间] 2015-11-12 上午10:35:55 <br>
	 *********************************************************.<br>
	 */
	public   boolean checkCert( byte[] sourceData,String cmbcCertPath, byte[] signature){
		boolean bool=false;
		try {
			Signature engine = new Signature();
			String deviceType = JCrypto.JSOFT_LIB;
			X509Cert cert = new X509Cert(new FileInputStream(cmbcCertPath));
			JCrypto.getInstance().initialize(deviceType, null);
			Session session = JCrypto.getInstance().openSession(deviceType);
			bool= engine.p1VerifyMessage(Mechanism.SHA1_RSA, sourceData, signature, cert.getPublicKey(), session);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bool;
	}
	
	/**
	 * ********************************************************
	 * 
	 * @Title: jsonObject
	 * @Description:将对象转换成json字符串
	 * @param obj
	 * @return String
	 * @date 2013-12-12 上午11:11:20
	 ******************************************************** 
	 */
	public  String jsonObject(Map<String, String> map) {
		String filePath = Util.class.getResource("/hongfupay.pfx").getPath();
		Map<String, String> nmap = submitDate(map,filePath, "123456");
		//Map<String, String> nmap = submitDate(map,"E:\\ye\\project\\mms\\src\\main\\resources\\test1024.pfx", "123456");
		String json = "";
		try {
			json = JSONObject.wrap(nmap).toString();
			return json;
		} catch (Exception e) {
			json = "{\"err\":\"" + "JsonObject is wrong" + "\"}";
			return json;
		} 
	
	}
	
	/**
	 * 
	 * @param sourceData 签名数据
	 * @param path签名证书q
	 * @param pass证书密码
	 * @return
	 */
	public   Map<String, String> submitDate(Map<String, String> sourceData, String path, String pass) {
		Map<String, String> data=signData(sourceData);
		try {
			Signature engine = new Signature();
			String deviceType = JCrypto.JSOFT_LIB;
			JCrypto.getInstance().initialize(deviceType, null);
			Session session = JCrypto.getInstance().openSession(deviceType);
			PrivateKey priKey = KeyUtil.getPrivateKeyFromPFX(path, pass);
			byte[] signature = engine.p1SignMessage(Mechanism.SHA1_RSA,data.toString().getBytes("UTF8"), priKey,session);
			data.put("signature", Base64.getBase64(new String(signature)));
		} catch (PKIException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return data;
	}
}
