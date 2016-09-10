package com.ztesoft.handler;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import com.ztesoft.file.FileLogReader;
import com.ztesoft.file.FileLogWriter;

/**
 * 类描述：
 * 
 * @author: liao.baoping
 * @date： 日期：2016-3-16 时间：下午7:05:00
 * @version 1.0
 */
public class FileLogReadHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

	private final static Logger logger = Logger.getLogger(FileLogReadHandler.class);
	private static FileLogReader fileLogReader = new FileLogReader();

	AsynchronousServerSocketChannel asynchronousServerSocketChannel = null;

	public FileLogReadHandler(AsynchronousServerSocketChannel asynchronousServerSocketChannel) {
		this.asynchronousServerSocketChannel = asynchronousServerSocketChannel;
	}

	@Override
	public void completed(AsynchronousSocketChannel asynchSocketChannel, Void attachment) {
		// TODO Auto-generated method stub
		// 为下一条新请求准备
		asynchronousServerSocketChannel.accept(null, this);
		ByteBuffer inbuffer = ByteBuffer.allocate(256);
		ByteBuffer outbuffer = ByteBuffer.allocate(1024 * 640);
		StringBuilder strBuilder = new StringBuilder();
		try {
			logger.info("Incoming connection from: " + asynchSocketChannel.getRemoteAddress());
			// 数据处理
			while (asynchSocketChannel.read(inbuffer).get() != -1) {
				inbuffer.flip();

				byte[] in = new byte[inbuffer.limit()];

				inbuffer.get(in);

				strBuilder= strBuilder.append(new String(in, "utf-8"));
				//读到结束标志后处理
				if(strBuilder.toString().endsWith("D_E_L")){
					
					strBuilder.setLength(strBuilder.length()-5);
					String element = strBuilder.toString();
					String[] elements = element.split("\\|");
					String msg = fileLogReader.readMsg(elements[0], elements[1]);
					outbuffer.put(msg.getBytes("UTF-8"));
					outbuffer.flip();
					
					while(asynchSocketChannel.write(outbuffer).get()>0){
						
						logger.debug("报文写入情况：未完待续...........");
					}
					strBuilder.setLength(0);
				}


				inbuffer.clear();
				outbuffer.clear();

			}
		} catch (IOException | InterruptedException | ExecutionException ex) {
			logger.error(ex);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			logger.error(e);
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

}
