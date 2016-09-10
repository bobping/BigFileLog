package com.ztesoft.asynSocketClet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;


/**
 * 文件模块客户端
 * 
 * @author: liao.baoping
 * @date: 2016-3-24 14:36:28
 */
public class AsyncSocketClient {

	private static final Logger logger = Logger.getLogger(AsyncSocketClient.class);

	private AsynchronousSocketChannel asynchSocketChannel = null;
	private String host;
	private int port;

	public AsyncSocketClient(String host, int port) {
		this.host = host;
		this.port = port;
		try {
			open();
		} catch (Exception e) {
			logger.error(e);
		}

	}

	public void open() throws Exception {
		if (!isOpen()) {
			asynchSocketChannel = AsynchronousSocketChannel.open();
			asynchSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
			asynchSocketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
			asynchSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
			asynchSocketChannel.connect(new InetSocketAddress(host, port)).get();
		}
	}

	public synchronized String sendMsg(String msg) throws UnsupportedEncodingException {
		if (isOpen()) {
			//给报文增加结束标志
			ByteBuffer msgByteBuffer = ByteBuffer.wrap((msg+"D_E_L").getBytes("utf-8"));
			ByteBuffer returnMsg = ByteBuffer.allocate(1024 * 64);
			StringBuilder returnStrbuf = new StringBuilder();
			try {
				while(asynchSocketChannel.write(msgByteBuffer).get()>0){
					logger.debug("消息写入情况：未完待续.......");
				}
				
				while(asynchSocketChannel.read(returnMsg).get()!=-1){
					
					returnMsg.flip();
					byte[] returnByte = new byte[returnMsg.limit()];
					returnMsg.get(returnByte);
					
					if (returnByte.length == 0) {
						logger.error("文件数据读取异常！");
						return null;
					}

					returnStrbuf = returnStrbuf.append(new String(returnByte, "utf-8"));
					
					//读到结束标记后返回
					if(returnStrbuf.toString().endsWith("D_E_L")){
						
						returnStrbuf.setLength(returnStrbuf.length()-5);				
						String tem = returnStrbuf.toString();

						return tem;
						
					}

					msgByteBuffer.clear();	
					returnMsg.clear();
					
					
				};

			} catch (Exception e) {
				logger.error(e);
				//释放连接
				try {
					close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return null;
			}
		}
		logger.error("客户端没有连接上服务端！");
		return null;

	}
	
	public synchronized String getMsg(String msg) throws UnsupportedEncodingException {
		if (isOpen()) {
			ByteBuffer msgByteBuffer = ByteBuffer.wrap((msg+"D_E_L").getBytes("utf-8"));
			ByteBuffer returnMsg = ByteBuffer.allocate(1024 * 64);
			StringBuilder returnbuf = new StringBuilder() ;
			try {
				
				
				asynchSocketChannel.write(msgByteBuffer).get();
				
				while(asynchSocketChannel.read(returnMsg).get()!=-1){
					
					returnMsg.flip();
					byte[] returnByte = new byte[returnMsg.limit()];
					returnMsg.get(returnByte);
					
					if (returnByte.length == 0) {
						logger.error("文件数据读取异常！");
						return null;
					}
					
					returnbuf =  returnbuf.append(new String(returnByte));
					
					
					//读到结束标记后返回
					if(returnbuf.toString().endsWith("D_E_L")){
						
						returnbuf.setLength(returnbuf.length()-5);				
						String tem = returnbuf.toString();
						return tem;
						
					}
					msgByteBuffer.clear();	
					returnMsg.clear();

							
				};
			
			} catch (Exception e) {
				logger.error(e);
				//释放连接
				try {
					close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return null;
			}
		}
		
		try {
			close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.error("没有从服务端获取到正确信息！");
		return null;

	}
	
	
	public boolean isOpen() {
		if (null != asynchSocketChannel) {
			return asynchSocketChannel.isOpen();
		}
		return false;
	}

	public void close() throws IOException {
		if (null != asynchSocketChannel) {
			if (asynchSocketChannel.isOpen()) {
				
					asynchSocketChannel.close();
					asynchSocketChannel = null;
					logger.info("client has been served!");
				
			}
		}
	}

}
