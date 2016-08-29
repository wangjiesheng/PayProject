package com.pay.framework.socket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SocketServer implements InitializingBean {
	
	/**
	 *记录日志 
	 */
	private static Logger log = LogManager.getLogger(SocketServer.class);
	
	/**
	 *默认为非阻塞模式 
	 */
	private static boolean  BLOCK_MODE = false;
	
	/**
	 * 读取数据的缓存大小
	 */
	private static int READ_BUFFER_SIZE = 1024;
	
	/**
	 * 写入数据的缓存大小
	 */
	private static int WRITE_BUFFER_SIZE = 1024;
	
	/**
	 * SelectableChannel对象的多路复用器
	 */
	private Selector selector;
	
	/**
	 * 默认绑定的主机地址
	 */
	@Value("#{globalConfigureProperties['convenience.advice.socket.ip']}")
	private String bindHost ;
	
	/**
	 * 默认绑定的端口
	 */
	/**
	 * 默认绑定的主机地址
	 */
	@Value("#{globalConfigureProperties['convenience.advice.socket.port']}")
	private int bindPort;
	
	/**
	 * socket服务处理
	 */
	@Autowired
	private ISocketHandler socketHandler;
	
	/**
	 * 超时时间
	 * 
	 */
	private int  timeout = 10000;
	

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		if(timeout >= 0){
			this.timeout = timeout;
		}else{
			log.warn("perform setTimeout failed");
		}
		
	}

	/**
	 * 默认构造方法
	 */
	public SocketServer(){
		super();
		
	}
	
	/**
	 * 带参数的构造方法
	 * @param bindHost 绑定的主机
	 * @param bindPort 绑定的端口
	 */
	public SocketServer(String bindHost, int bindPort){
		this.bindHost = bindHost;
		this.bindPort = bindPort;
	}
	
	/**
	 * 初始化操作，创建一个服务通道，并与一个选择器关联
	 * @throws IOException 
	 */
	private void initServer() throws IOException{
		//向选择器的键集中添加一个键。在选择操作期间从键集中移除已取消的键。键集本身是不可直接修改的
		ServerSocketChannel  server = ServerSocketChannel.open();
		this.selector = Selector.open();
		//绑定端口
		InetSocketAddress address = new InetSocketAddress(this.bindHost, this.bindPort);
		server.socket().bind(address);
		server.socket().setSoTimeout(timeout);
		server.configureBlocking(BLOCK_MODE);
		//由通道服务注册一个选择键
		server.register(selector, SelectionKey.OP_ACCEPT);
		
	}
	
	/**
	 * 操作流程控制
	 * @param selector  选择器
	 * @param selectionKey 选择的键
	 * @throws IOException 
	 */
	private void nextFlowController(Selector selector, SelectionKey selectionKey) throws IOException{
		//获取注册的键关联的通道
		if(selectionKey.isAcceptable()){
			ServerSocketChannel server = (ServerSocketChannel)selectionKey.channel();
			//下一步获取一个socket通道，注册读取的选择键;不管此通道的阻塞模式如何，此方法返回的套接字通道（如果有）将处于阻塞模式。
			SocketChannel socketChannel = server.accept();
			if(socketChannel == null){
				log.info("accept invoke success, but socketChannel is null");
				return;
			}
			socketChannel.configureBlocking(BLOCK_MODE);
			socketChannel.register(selector, SelectionKey.OP_READ);
		}else if(selectionKey.isReadable()){
			//下一步注册写入的选择键
			SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
			Object receiveObj = selectionKey.attachment();//读取的数据
			SelectionKey writeKey = socketChannel.register(selector, SelectionKey.OP_WRITE);
			writeKey.attach(receiveObj);//传递读取的对象
		}else if(selectionKey.isWritable()){
			//下一步关闭通道,只有关闭通道，下一次选择器才不会重新获取
			SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
			socketChannel.close();
			log.info("channel close done...");
		}else{
			log.info("not next flow path");
		}
	}
	
	/**
	 * 向通道读取数据
	 * @param selectionKey 选择的键
	 * @throws IOException 
	 */
	private void processRead(SelectionKey selectionKey) throws IOException{
		//通过选择键关联的通道获取数据
		SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
		ByteBuffer byteBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int readLen = -1;
		while((readLen = socketChannel.read(byteBuffer)) > 0){
			out.write(byteBuffer.array(),0,readLen);
			byteBuffer.clear();//还原位置，以便下一次写入数据
		}
		ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());
		if(socketHandler != null){
			Object receiveObj = socketHandler.receiveData(input);
			selectionKey.attach(receiveObj);//保存读取的对象
		}else{
			log.warn("socketHandler is null");
		}
		log.info("processRead perform done");
	}
	
	/**
	 * 向通道写入数据,响应内容
	 * @param selectionKey
	 * @throws IOException 
	 */
	private void processWrite(SelectionKey selectionKey) throws IOException{
		SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Object receiveObj = selectionKey.attachment();//读取的数据
		if(socketHandler != null){
			socketHandler.responseData(out, receiveObj);
			//写入到目标
			ByteArrayInputStream input  = new ByteArrayInputStream(out.toByteArray());
			byte[]  buffer = new byte[WRITE_BUFFER_SIZE];
			int readLen = -1;
			while((readLen = input.read(buffer)) != -1){
				ByteBuffer byteBuffer = ByteBuffer.allocate(readLen);
				byteBuffer.put(buffer, 0, readLen);
				byteBuffer.clear();//还原位置，以便写入数据
				socketChannel.write(byteBuffer);
			}
			
		}else{
			log.warn("socketHandler is null");
		}
		log.info("processWrite perform done");
	}
	
	
	/**
	 * 获取选择的键集合，运行任务
	 * @throws IOException 
	 */
	private void performTask() throws IOException{
		//此方法执行处于阻塞模式的选择操作
		int count = selector.select();
		log.info("got selected count " + count);
		//已选择的
		Iterator<SelectionKey>  iterator = selector.selectedKeys().iterator();
		while(iterator.hasNext()){
			SelectionKey selectionKey = iterator.next();
			//api:每次向选择器注册通道时就会创建一个选择键。通过调用某个键的 cancel 方法、关闭其通道，或者通过关闭其选择器来取消 该键之前，它一直保持有效。
			try{
				if(selectionKey.isAcceptable()){
					//可进行连接操作，暂时不需要进行连接处理，直接注册读取的选择键
				}else if(selectionKey.isReadable()){
					//可读取内容
					processRead(selectionKey);
				}else if(selectionKey.isWritable()){
					//可写入内容，响应数据
					processWrite(selectionKey);
				}
				
				nextFlowController(selector, selectionKey);
				
			}catch(Exception e){
				log.error("occurrent channel damage", e);
				//必须关闭通道，否则通道一直存在，注册的键就有效；因为报错，后续代码不会再次往通道注册选择键，所以此通道可以 关闭。
				selectionKey.channel().close();
			}finally{
				// 移除已选择的,重要
				iterator.remove();
			}
			
		}
		
	}

	
	/**
	 * 
	 * 服务启动
	 * 
	 */
	public void startServer(){
		//初始化操作
		try {
			this.initServer();
		} catch (IOException e) {
			log.error("initServe failed", e);
			return;
		}
		//循环检测
		log.info("socketServer launch...");
		while(true){
			try {
				performTask();
			} catch (Exception e) {
				log.error("socket perform failed",e);
			}
		}
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Runnable runnable = new Runnable() {
			public void run() {
				startServer();
			}
		}; 
		Thread thread = new Thread(runnable);
		thread.start();
		
	}        
	
	
}
