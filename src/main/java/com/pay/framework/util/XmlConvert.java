package com.pay.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 使用jaxb2转换
 * 
 * @author OJH
 * 2015年7月20日
 */
public class XmlConvert {
	/**
	 *记录日志 
	 */
	private static Logger log = LogManager.getLogger(XmlConvert.class);
	
	private static String defaultEncoding = "UTF-8";
	

	public static String getDefaultEncoding() {
		return defaultEncoding;
	}


	/**
	 * 对象转成xml
	 * 
	 * @param obj
	 * @param encoding
	 * @return
	 * @throws JAXBException 
	 */
	public static byte[] convertToXml(Object obj, String encoding) throws Exception{
		JAXBContext context = JAXBContext.newInstance(obj.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newFactory().createXMLStreamWriter(baos, (String) marshaller.getProperty(Marshaller.JAXB_ENCODING));
        xmlStreamWriter.writeStartDocument((String) marshaller.getProperty(Marshaller.JAXB_ENCODING), "1.0");
		xmlStreamWriter.flush();
		marshaller.marshal(obj, baos);
		String xmlStr = new String(baos.toByteArray(), encoding);
		log.debug("convertToXml:" + xmlStr + ";");
		return baos.toByteArray();
	}
	
	
	/**
	 * 对象转换成xml
	 * @param obj
	 * @return
	 * @throws JAXBException
	 */
	public static byte[] convertToXml(Object obj) throws Exception{
		return convertToXml(obj, defaultEncoding);
	}
	
	
	/**
	 * xml数据转换成对象
	 * 
	 * @param xmlStr
	 * @param clazz
	 * @return
	 * @throws JAXBException
	 */
	public static <T> T convertToJavaBean(String xmlStr, Class<T> clazz) throws JAXBException{
		log.info("convertToJavaBean:" + xmlStr + ";");
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Reader reader = new StringReader(xmlStr);
		T t = (T)unmarshaller.unmarshal(reader);
		return t;
	}
	
	/**
	 * xml数据转换成对象
	 * @param in
	 * @param clazz
	 * @return
	 * @throws JAXBException
	 * @throws IOException 
	 * @throws FactoryConfigurationError 
	 * @throws XMLStreamException 
	 */
	public static <T> T convertToJavaBean(InputStream in, Class<T> clazz) throws Exception{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int len = -1;
		String xmlStatement = "";
		while((len = in.read(buffer)) > 0){
			out.write(buffer, 0, len);
			if(xmlStatement.length() == 0){
				xmlStatement = new String(buffer);
			}
		}
		String encoding = "GBK";
		int firstIndex = xmlStatement.indexOf("<?");
		int lastIndex = xmlStatement.indexOf("?>");
		if(firstIndex != -1 && lastIndex != -1 && lastIndex > firstIndex){
			String firstState = xmlStatement.substring(firstIndex,lastIndex);
			if(firstState.indexOf("UTF-8") != -1){
				encoding = "UTF-8";
			}else if(firstState.indexOf("ISO-8859-1") != -1){
				encoding = "ISO-8859-1";
			}else if(firstState.indexOf("ASCII") != -1){
				encoding = "ASCII";
			}
		}
 		log.debug("convertToJavaBean:" + new String(out.toByteArray(), encoding) + ";");
		//in不能在此关闭
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		T t = (T)unmarshaller.unmarshal(new ByteArrayInputStream(out.toByteArray()));
		return t;
		
	}
	
	/**
	 * 对象转成xml
	 * 
	 * @param obj
	 * @param encoding
	 * @return
	 * @throws JAXBException 
	 */
	public static String convertToXmlByString(Object obj, String encoding) throws Exception{
		return new String(convertToXml(obj, encoding), encoding);
	}
	
	/**
	 * 对象转换成xml
	 * @param obj
	 * @return
	 * @throws JAXBException
	 */
	public static String convertToXmlByString(Object obj) throws Exception{
		return convertToXmlByString(obj, defaultEncoding);
	}
}
