package com.ztesoft.socketClt;

 
import java.io.IOException;

import org.apache.commons.pool.impl.GenericObjectPool;  
import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.apache.log4j.Logger;  

/**
 * @author bobping
 *
 */
public class SocketCltPool {
	
	private static final Logger logger = Logger.getLogger(SocketCltPool.class); 
	private GenericObjectPool<AsyncSocketClient> pool ;
	
	
	public SocketCltPool(Config config,String ip,int port){
		SocketCltFactory factory = new SocketCltFactory( ip, port);
		pool = new GenericObjectPool<AsyncSocketClient>(factory,config);
		
	}
	
	//获取socket对象
	public AsyncSocketClient getAsySocketClt() throws Exception{
		return (AsyncSocketClient) pool.borrowObject();
	}
	
	//归还socket对象
	public void releasAsySocketClt (AsyncSocketClient asySocketClt){
		
		try {
			pool.returnObject(asySocketClt);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e);
			if(asySocketClt!=null){
				try {
					asySocketClt.close();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					logger.error(e);
				}
			}
		}
		
		
	}
	
}
