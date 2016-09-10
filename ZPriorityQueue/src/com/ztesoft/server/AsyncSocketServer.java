package com.ztesoft.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.ztesoft.handler.QueueHandler;

/**
*类描述：
*@author: liao.baoping
*@date： 日期：2016-3-18 时间：上午10:19:37
*@version 1.0
*/
public class AsyncSocketServer implements Runnable {
	
	private AsynchronousChannelGroup asyncChannelGroup = null;
	private AsynchronousServerSocketChannel asyncServerSocketChannel = null;
	InetSocketAddress socketAddress = null;
	static String SERVER_NAME = "FileLogServer";
	static String WRITER_SEVER = "writer";
	static String READER_SEVER = "reader";

	private String severType = "";
	private String host = "";
	private int port;
	private int maxCon;
	private CompletionHandler<AsynchronousSocketChannel, Void> handler = null;

	Logger logger = Logger.getLogger(AsyncSocketServer.class.getName());

	AsyncSocketServer(String severType, String host, int port,int maxCon) {

		this.severType = severType;
		this.host = host;
		this.port = port;
		this.maxCon = maxCon;
		try {
			asyncChannelGroup = AsynchronousChannelGroup.withFixedThreadPool(maxCon, Executors.defaultThreadFactory());
//					.withThreadPool(Executors.newFixedThreadPool(100,
//							new NamedThreadFactory(SERVER_NAME
//									+ "_Thread_Group")));

			if (host == null) {
				socketAddress = new InetSocketAddress(InetAddress
						.getLocalHost().getHostName(), port);
			} else {
				socketAddress = new InetSocketAddress(host, port);
			}

			asyncServerSocketChannel = AsynchronousServerSocketChannel
					.open(asyncChannelGroup);
			asyncServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF,
					1024 * 1024 * 1024);
			asyncServerSocketChannel.setOption(
					StandardSocketOptions.SO_REUSEADDR, true);
			asyncServerSocketChannel.bind(socketAddress);

			// 接口类型选择 写日志or读日志
			if (severType.equals(AsyncSocketServer.WRITER_SEVER)) {
				handler = new QueueHandler(asyncServerSocketChannel);
			} else if (severType.equals(AsyncSocketServer.READER_SEVER)) {
				
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}

	}

	public void run() {
		try {
			if (asyncServerSocketChannel.isOpen()) {
				asyncServerSocketChannel.accept(null, handler);
			}
			logger.info("FileLogServer>>>" + severType + "[" + host + ":"
					+ port + "]" + "已启动，准备接收请求[允许的最大连接数为："+maxCon+"]>>>>>>>>>");
			System.in.read();
		} catch (IOException e) {
			logger.error(e);

		}
	}

}
