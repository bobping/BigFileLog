package com.ztesoft.socketClt;

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
	private static final byte[] endFlag = {68, 95, 69, 95, 76};
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
			asynchSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, false);
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
					logger.debug("未完待续........");
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
				close();

				return null;
			}
		}
		logger.error("客户端没有连接上服务端！");
		return null;

	}
	
	public synchronized String sendByte(byte[] msg) throws UnsupportedEncodingException {
		if (isOpen()) {
			//给报文增加结束标志
			msg = byteMerger(msg,endFlag);
			ByteBuffer msgByteBuffer = ByteBuffer.wrap(msg);
			ByteBuffer returnMsg = ByteBuffer.allocate(1024 * 64);
			byte[] temByte = new byte[0];
			try {
				while(asynchSocketChannel.write(msgByteBuffer).get()>0){
					logger.debug("未完待续........");
				}
				
				while(asynchSocketChannel.read(returnMsg).get()!=-1){
					
					returnMsg.flip();
					byte[] returnByte = new byte[returnMsg.limit()];
					returnMsg.get(returnByte);
					
					if (returnByte.length == 0) {
						logger.error("文件数据读取异常！");
						return null;
					}
					
					temByte = byteMerger(temByte,returnByte);
					
					//读到结束标记后返回
					if(isEndOfMsg(temByte)){
						
						temByte = wipeEndFlag(temByte);		
						String tem = new String(temByte,"utf-8");
						return tem;
						
					}
					msgByteBuffer.clear();	
					returnMsg.clear();
					
					
				};

			} catch (Exception e) {
				logger.error(e);
				//释放连接
				close();

				return null;
			}
		}

		throw new RuntimeException("释放连接出现异常！");

	}
	
	public synchronized String getMsg(String msg) {
		if (isOpen()) {
			ByteBuffer msgByteBuffer = ByteBuffer.wrap((msg+"D_E_L").getBytes(Charset.forName("utf-8")));
			ByteBuffer returnMsg = ByteBuffer.allocate(1024 * 64);
			StringBuilder returnbuf = new StringBuilder() ;
			try {
				
				
				while(asynchSocketChannel.write(msgByteBuffer).get()>0){
					logger.debug("未完待续.......");
				};

				while(asynchSocketChannel.read(returnMsg).get()!=-1){
				
					returnMsg.flip();
					byte[] returnByte = new byte[returnMsg.limit()];
					returnMsg.get(returnByte);
					
					if (returnByte.length == 0) {
						logger.error("文件数据读取异常！");
						return null;
					}
					
					returnbuf =  returnbuf.append(new String(returnByte,"utf-8"));
					
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
				close();

				return null;
			}
		}
		
		close();

		throw new RuntimeException("释放连接出现异常！");

	}
	
	
	public boolean isOpen() {
		if (null != asynchSocketChannel) {
			return asynchSocketChannel.isOpen();
		}
		return false;
	}

	public void close() {
		if (null != asynchSocketChannel) {
			if (asynchSocketChannel.isOpen()) {
				
					try {
						asynchSocketChannel.close();
					} catch (IOException e) {
						throw new RuntimeException("释放连接出现异常！",e);

					}
					asynchSocketChannel = null;
					logger.info("client has been served!");
				
			}
		}
	}
	
	boolean isEndOfMsg(byte[] temByte){
		int length = temByte.length;
		if(length>0&&temByte[length-5]==endFlag[0]&&
				temByte[length-4]==endFlag[1]&&
				temByte[length-3]==endFlag[2]&&
				temByte[length-2]==endFlag[3]&&
				temByte[length-1]==endFlag[4]){
			return true;
		}else{
			return false;
		}
	}
	//java 合并两个byte数组  
    public static byte[] byteMerger(byte[] byte1, byte[] byte2){  
        byte[] byte3 = new byte[byte1.length+byte2.length];  
        System.arraycopy(byte1, 0, byte3, 0, byte1.length);  
        System.arraycopy(byte2, 0, byte3, byte1.length, byte2.length);  
        return byte3;  
    }  
	
    public byte[] wipeEndFlag(byte[] byte1){
    	byte[] byte3 = new byte[byte1.length-5];
    	System.arraycopy(byte1, 0, byte3, 0, byte1.length-5); 
    	return byte3;
    }
	

}
