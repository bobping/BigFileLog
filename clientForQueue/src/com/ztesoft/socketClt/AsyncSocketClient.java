package com.ztesoft.socketClt;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;


/**
 * ���ȼ����пͻ���
 * 
 * @author: liao.baoping
 * @date: 2016-3-24 14:36:28
 */
public class AsyncSocketClient {

	private static final Logger logger = Logger.getLogger(AsyncSocketClient.class);
	private static final byte[] endFlag = {81, 95, 69, 95, 68};
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

	public synchronized byte[] sendObjSer(byte[] objSer) {
		if (isOpen()) {
			//���������ӽ�����־
			objSer = byteMerger(objSer,endFlag);
			ByteBuffer objByteBuffer = ByteBuffer.wrap(objSer);
			ByteBuffer returnMsg = ByteBuffer.allocate(1024 * 1024);
			byte[] temByte = new byte[0];
			try {
				while(asynchSocketChannel.write(objByteBuffer).get()>0){
					logger.debug("δ�����........");
				}
				
				int readFlag = 0;
				while(readFlag!=-1){
					
					readFlag = asynchSocketChannel.read(returnMsg).get();
					
					returnMsg.flip();
					
					byte[] returnByte = new byte[returnMsg.limit()];
					
					returnMsg.get(returnByte);
					if (returnByte.length == 0) {
						logger.error("socket���ݶ�ȡ�쳣��");
						return null;
					}
					
					temByte = byteMerger(temByte,returnByte);
					
					if(isEndOfMsg(temByte)){
						System.out.println("========================"+new String(returnByte,"utf-8"));
						objByteBuffer.clear();	
						returnMsg.clear();
						
					}
					objByteBuffer.clear();
					returnMsg.clear();
					
				};

			} catch (Exception e) {
				logger.error(e);
				//�ͷ�����
				try {
					close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return null;
			}
		}
		logger.error("�ͻ���û�������Ϸ���ˣ�");
		return null;

	}
	
	public synchronized String getMsg(String msg) {
		if (isOpen()) {
			ByteBuffer msgByteBuffer = ByteBuffer.wrap((msg+"D_E_L").getBytes(Charset.forName("utf-8")));
			ByteBuffer returnMsg = ByteBuffer.allocate(1024 * 64);
			StringBuilder returnbuf = new StringBuilder() ;
			try {
				
				
				while(asynchSocketChannel.write(msgByteBuffer).get()>0){
					logger.debug("δ�����.......");
				};

				while(asynchSocketChannel.read(returnMsg).get()!=-1){
				
					returnMsg.flip();
					byte[] returnByte = new byte[returnMsg.limit()];
					returnMsg.get(returnByte);
					
					if (returnByte.length == 0) {
						logger.error("�ļ����ݶ�ȡ�쳣��");
						return null;
					}
					
					returnbuf =  returnbuf.append(new String(returnByte,"utf-8"));
					
					//����������Ǻ󷵻�
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
				//�ͷ�����
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
		logger.error("û�дӷ���˻�ȡ����ȷ��Ϣ��");
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
	//java �ϲ�����byte����  
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
