package com.ztesoft.handler;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;


import org.apache.log4j.Logger;

import com.ztesoft.queue.QueueInterface;
import com.ztesoft.queue.QueueOperate;
import com.ztesoft.springHelper.ApplicationContextUtils;
import com.ztesoft.utils.ZObjectUtils;

/**
 * 类描述：
 * 
 * @author: liao.baoping
 * @date： 日期：2016-3-15 时间：下午6:19:00
 * @version 1.0
 */
public class QueueHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

	private static final Logger logger = Logger.getLogger(QueueHandler.class);
	
	private static final byte[] endFlag = {81, 95, 69, 95, 68};
	
	//需要备份的时候取bean
	private static QueueOperate queueOperate =(QueueOperate) ApplicationContextUtils.getBean("queueOperate");
	
	AsynchronousServerSocketChannel asynchronousServerSocketChannel = null;

	public QueueHandler(AsynchronousServerSocketChannel asynchronousServerSocketChannel) {
		this.asynchronousServerSocketChannel = asynchronousServerSocketChannel;
	}

	@Override
	public void completed(AsynchronousSocketChannel asynchSocketChannel, Void attachment) {
		// TODO Auto-generated method stub

		// 为下一条新连接请求准备
		asynchronousServerSocketChannel.accept(null, this);
		ByteBuffer inbuffer = ByteBuffer.allocate(1024 * 128);
		ByteBuffer outbuffer = ByteBuffer.allocate(1024 * 1024);
		byte[] temByte = new byte[0];

		try {
			logger.info("Incoming connection from: " + asynchSocketChannel.getRemoteAddress());
			// 数据处理
			int readFlag = 0;
			while (readFlag!=-1) {
				
				readFlag = asynchSocketChannel.read(inbuffer).get();
				inbuffer.flip();
				byte[] in = new byte[inbuffer.limit()];

				inbuffer.get(in);
				
				temByte = byteMerger(temByte, in);
				
				if(isEndOfMsg(temByte)){
				
					temByte = wipeEndFlag(temByte);
					
					byte[] outByte = queueOperate.queueOperate(temByte);
					
					outbuffer = ByteBuffer.wrap(byteMerger(outByte, endFlag));
					
					while(asynchSocketChannel.write(outbuffer).get()>0){
						logger.debug("消息写入情况：未完待续...........");
					}
					temByte = new byte[0];

				}
				inbuffer.clear();
				outbuffer.clear();
			}
		} catch (IOException | InterruptedException | ExecutionException ex) {
			logger.error(ex);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				SocketAddress remote = asynchSocketChannel.getRemoteAddress();
				asynchSocketChannel.close();
				logger.warn("远程服务器:" + remote + "连接关闭<<<<<<<<");
			} catch (IOException e) {
				logger.error(e);
			}
		}

	}

	@Override
	public void failed(Throwable exc, Void attachment) {
		// TODO Auto-generated method stub
		asynchronousServerSocketChannel.accept(null, this);

		throw new UnsupportedOperationException("Cannot accept connections!");
	}
	

	//java 合并两个byte数组  
    public byte[] byteMerger(byte[] byte1, byte[] byte2){  
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
	

}
