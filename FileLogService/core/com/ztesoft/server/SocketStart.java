package com.ztesoft.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.ztesoft.config.Configure;

@Component
public class SocketStart {
	
	static Logger logger = Logger.getLogger(SocketStart.class);

	private AsyncSocketServer writerSev;
	
	private AsyncSocketServer readerSev;

	public AsyncSocketServer getWriteSev() {
		return writerSev;
	}


	public void setWriteSev(AsyncSocketServer writerSev) {
		this.writerSev = writerSev;
	}


	public AsyncSocketServer getReaderSev() {
		return readerSev;
	}


	public void setReaderSev(AsyncSocketServer readerSev) {
		this.readerSev = readerSev;
	}
	
	@PostConstruct
	public void init(){

		ExecutorService exe = Executors.newFixedThreadPool(2);
		
		// 注册writer服务
		writerSev = new AsyncSocketServer( AsyncSocketServer.WRITER_SEVER, Configure.FILE_LOG_HOST,
				Configure.WRITER_PORT,Configure.WRITE_CON_NUM); 
		// 注册reader服务
		readerSev = new AsyncSocketServer( AsyncSocketServer.READER_SEVER, Configure.FILE_LOG_HOST,
				Configure.READER_PORT,Configure.READ_CON_NUM); 

		exe.execute(writerSev);
		logger.info("写大文件日志服务端口打开中....");
		exe.execute(readerSev);
		logger.info("读大文件日志服务端口打开中....");
		
	}


	
	

}
