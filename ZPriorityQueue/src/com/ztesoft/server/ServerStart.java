package com.ztesoft.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.ztesoft.handler.QueueHandler;
import com.ztesoft.server.config.Configure;
import com.ztesoft.springHelper.ApplicationContextUtils;

/**
*��������
*@author: liao.baoping
*@date�� ���ڣ�2016-3-18 ʱ�䣺����10:19:45
*@version 1.0
*/
public class ServerStart  {

	static Logger logger = Logger.getLogger(ServerStart.class);
	
	public static void main(String[] args) {
		
		ApplicationContextUtils.initContext();
		
		ExecutorService exe = Executors.newFixedThreadPool(Configure.BACKUP_THREAD_NUM+2);
		
		// ע��writer����
		AsyncSocketServer writerSev = new AsyncSocketServer(
				AsyncSocketServer.WRITER_SEVER, Configure.FILE_LOG_HOST,
				Configure.WRITER_PORT,Configure.WRITE_CON_NUM); 
		exe.execute(writerSev);
		logger.info("д���ļ���־����˿ڴ���....");

	}


}