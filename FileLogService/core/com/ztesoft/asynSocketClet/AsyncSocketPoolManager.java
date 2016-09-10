package com.ztesoft.asynSocketClet;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.springframework.stereotype.Component;

import com.ztesoft.config.Configure;


@Component
public class AsyncSocketPoolManager {
	
	private static Map<String, SocketCltPool> poolsForSave = new HashMap<String, SocketCltPool>();
	private static Map<String, SocketCltPool> poolsForGet = new HashMap<String, SocketCltPool>();;

	private static int writePort = 0;//Configure.SLAVE_PORT;
	
	//不需要获取报文，次变量未定义
	private static int readPort ;
//	public static SocketCltPool poolForSave  ;
//	public static  SocketCltPool poolForGet  ;
	
	private static Config poolConf =new Config();
	static{		
		poolConf.maxActive = 16;
		poolConf.maxWait = 30000;
		poolConf.maxIdle = 16;
		poolConf.whenExhaustedAction = 1;
		poolConf.testOnBorrow = true;
		poolConf.testOnReturn = true;
	
//		poolForSave = new SocketCltPool(poolConf, "127.0.0.1", 8881);
//		poolForGet  = new SocketCltPool(poolConf, "127.0.0.1", 9999);		
	}
	
	//获取写log连接池
	public SocketCltPool getPoolForSave(String host){
		
		SocketCltPool pool = poolsForSave.get(host);
		
		
		
		synchronized (this) {
			if(pool==null){
				pool = new SocketCltPool(poolConf, host, writePort);
				poolsForSave.put(host, pool);
			}
		}
	
		return pool;
		
		
		
	}
	
	//获取读log连接池
	public SocketCltPool getPoolForGet(String host){
		
		SocketCltPool pool = poolsForGet.get(host);
		
		synchronized (this) {
			if(pool==null){
				pool = new SocketCltPool(poolConf, host, readPort);
				poolsForGet.put(host, pool);
			}
		}

		
		return pool;
		
		
		
	}
	
	

}
 