package com.ztesoft.socketClt;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.springframework.stereotype.Component;




/**
 * @author bobping
 *
 */

public class AsyncSocketPoolManager {

	private static Map<String, SocketCltPool> poolsForSave = new HashMap<String, SocketCltPool>();
	private static Map<String, SocketCltPool> poolsForGet = new HashMap<String, SocketCltPool>();;


	private static Config poolConf = new Config();

	// 获取写log连接池
	public SocketCltPool getPoolForSave(String host, String port) {

		SocketCltPool pool = poolsForSave.get(host);

		synchronized (this) {
			if (pool == null) {
				pool = new SocketCltPool(poolConf, host, Integer.valueOf(port));
				poolsForSave.put(host, pool);
			}
		}

		return pool;

	}

	// 获取读log连接池
	public SocketCltPool getPoolForGet(String host, String port) {

		SocketCltPool pool = poolsForGet.get(host);

		synchronized (this) {
			if (pool == null) {
				pool = new SocketCltPool(poolConf, host, Integer.valueOf(port));
				poolsForGet.put(host, pool);
			}
		}

		return pool;

	}

}
