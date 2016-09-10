package com.ztesoft.socketClt;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.springframework.stereotype.Component;

import com.ztesoft.conf.Configure;




/**
 * @author bobping
 *
 */
@Component
public class AsyncSocketPoolManager {

	private static Map<String, SocketCltPool> poolsForSave = new HashMap<String, SocketCltPool>();
	private static Map<String, SocketCltPool> poolsForGet = new HashMap<String, SocketCltPool>();;

	private static int save_port = Configure.SAVE_FILE_PORT;
	
	private static int get_port = Configure.GET_FILE_PORT;

	private static Config poolConf = new Config();
	
	static {
		// 链接池中最大连接数
		poolConf.maxActive = Configure.POOL_MAX_ACTIVE;
		// 当连接池资源耗尽时，调用者最大阻塞的时间
		poolConf.maxWait = Configure.POOL_MAX_WAIT;
		// 链接池中最大空闲的连接数
		poolConf.maxIdle = Configure.POOL_MAXIDLE;
		// 向调用者输出“链接”资源时，是否检测是有有效
		poolConf.testOnBorrow = Configure.POOL_TESTONBORROW;
		// 向连接池“归还”链接时，是否检测“链接”对象的有效性
		poolConf.testOnReturn = Configure.POOL_TESTONRETURN;
		// 连接空闲的最小时间，达到此值后空闲连接将可能会被移除
		poolConf.minEvictableIdleTimeMillis = Configure.POOL_MINEVICTABLEIDLETIMEMILLIS;

	}

	// 获取写log连接池
	public SocketCltPool getPoolForSave(String host) {

		SocketCltPool pool = poolsForSave.get(host);

		synchronized (this) {
			if (pool == null) {
				pool = new SocketCltPool(poolConf, host, save_port);
				poolsForSave.put(host, pool);
			}
		}

		return pool;

	}

	// 获取读log连接池
	public SocketCltPool getPoolForGet(String host) {

		SocketCltPool pool = poolsForGet.get(host);

		synchronized (this) {
			if (pool == null) {
				pool = new SocketCltPool(poolConf, host, get_port);
				poolsForGet.put(host, pool);
			}
		}

		return pool;

	}

}
