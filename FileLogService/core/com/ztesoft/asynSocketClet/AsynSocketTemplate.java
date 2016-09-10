package com.ztesoft.asynSocketClet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ztesoft.config.Configure;

public class AsynSocketTemplate {

	private static final Logger logger = Logger.getLogger(AsynSocketTemplate.class); 

	@Autowired
	AsyncSocketPoolManager asyncSocketPoolManager;
	
	public String saveMsg (String msg) {
		
		//此处ip先写死
		String host = "";// Configure.SLAVE_IP;
		
		String returnMsg = null;
		AsyncSocketClient socket = null;
		SocketCltPool socketCltPool = null;
		try {
			socketCltPool = asyncSocketPoolManager.getPoolForSave(host);
			socket = socketCltPool.getAsySocketClt();
			
			returnMsg = socket.sendMsg(msg);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e);

		} finally {
			socketCltPool.releasAsySocketClt(socket);
		}
		
		return returnMsg;
		
	}
	
	public String getMsg (String msg) {
		
		//此处ip先写死
		String host = "";//Configure.SLAVE_IP;
		
		String returnMsg = null;
		AsyncSocketClient socket = null;
		SocketCltPool socketCltPool = null;
		try {
			socketCltPool = asyncSocketPoolManager.getPoolForGet(host);
			socket = socketCltPool.getAsySocketClt();
			
			returnMsg = socket.getMsg(msg);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e);

		} finally {
			socketCltPool.releasAsySocketClt(socket);
		}
		
		return returnMsg;
		
	}
	
	
	
}
