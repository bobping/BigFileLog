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
		// ���ӳ������������
		poolConf.maxActive = Configure.POOL_MAX_ACTIVE;
		// �����ӳ���Դ�ľ�ʱ�����������������ʱ��
		poolConf.maxWait = Configure.POOL_MAX_WAIT;
		// ���ӳ��������е�������
		poolConf.maxIdle = Configure.POOL_MAXIDLE;
		// ���������������ӡ���Դʱ���Ƿ���������Ч
		poolConf.testOnBorrow = Configure.POOL_TESTONBORROW;
		// �����ӳء��黹������ʱ���Ƿ��⡰���ӡ��������Ч��
		poolConf.testOnReturn = Configure.POOL_TESTONRETURN;
		// ���ӿ��е���Сʱ�䣬�ﵽ��ֵ��������ӽ����ܻᱻ�Ƴ�
		poolConf.minEvictableIdleTimeMillis = Configure.POOL_MINEVICTABLEIDLETIMEMILLIS;

	}

	// ��ȡдlog���ӳ�
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

	// ��ȡ��log���ӳ�
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
