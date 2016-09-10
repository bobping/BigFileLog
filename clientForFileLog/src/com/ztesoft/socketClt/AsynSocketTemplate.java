package com.ztesoft.socketClt;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;


/**
 * @author bobping
 *
 */

public class AsynSocketTemplate {

	private static final Logger logger = Logger.getLogger(AsynSocketTemplate.class); 

	private AsyncSocketPoolManager asyncSocketPoolManager;
	
	private String zkAddress;
	
	private static final String zkPath = "/fileServers";
	
	private Map<String,String> currentHost = new HashMap<>();
	
	//���¸�ֵΪĬ��ֵ
	private Config poolConf;
	
	private int maxActive = 16;
	
	private int maxWait = 3000;
	
	private int maxIdle = 8;
	
	private boolean testOnBorrow = true;
	
	private boolean testOnReturn = true;
	
	private int minEvictableIdleTimeMillis = 6000;
	
	private int sessionTimeOutMs = 10000;
	
	private int conectTimeOut = 10000;
	
	public Map<String, String> getCurrentHost() {
		return currentHost;
	}

	public void setCurrentHost(Map<String, String> currentHost) {
		this.currentHost = currentHost;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public int getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public String getZkAddress() {
		return zkAddress;
	}

	public void setZkAddress(String zkAddress) {
		this.zkAddress = zkAddress;
	}

	public int getSessionTimeOutMs() {
		return sessionTimeOutMs;
	}

	public void setSessionTimeOutMs(int sessionTimeOutMs) {
		this.sessionTimeOutMs = sessionTimeOutMs;
	}

	public int getConectTimeOut() {
		return conectTimeOut;
	}

	public void setConectTimeOut(int conectTimeOut) {
		this.conectTimeOut = conectTimeOut;
	}

	public CuratorFramework getClient() {
		return client;
	}

	public void setClient(CuratorFramework client) {
		this.client = client;
	}

	private CuratorFramework client; 
	
	private PathChildrenCache childrenCache;
	
	public void init(){
		this.initPoolConfig();
		this.initZkInfo();

	}

	private void initZkInfo() {
		
		logger.info(zkAddress);
		client = CuratorFrameworkFactory.builder().connectString(zkAddress)
				.sessionTimeoutMs(sessionTimeOutMs).connectionTimeoutMs(conectTimeOut)
				.canBeReadOnly(false)
				.retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
				.namespace("fileZk").build();
		client.start();
	    try {	
	    	//��ʼ����zk��������ļ�������
	    	calculateHost(client.getChildren().forPath(zkPath), currentHost);
			
			/**
		     * �����ӽڵ�ı仯�����������ŷ���
		     */
		    childrenCache = new PathChildrenCache(client, zkPath, true);
			childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
		    childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
				
				@Override
				public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {

//		            switch (event.getType()) {
//		            case CHILD_ADDED:
//		              break;
//		            case CHILD_REMOVED:
//		              break;
//		            case CHILD_UPDATED:
//		              break;
//		            default:
//		              break;
//				 	}
					
					if(event.getType() == Type.CHILD_ADDED||event.getType() == Type.CHILD_REMOVED||event.getType() == Type.CHILD_UPDATED){
						calculateHost(client.getChildren().forPath(zkPath), currentHost);
					}
				}  
			});
		} catch (Exception e) {
			logger.error("����zk�����쳣��",e);
			this.close();
		}
	    

		
	}

	public void close(){
		if(client!=null){
			try {
				childrenCache.close();
			} catch (IOException e) {
				throw new RuntimeException("�ر�zk���������쳣��", e);
			}
			client.close();
		}
	}
	
	private void initPoolConfig(){
		poolConf = new Config();
		/**
		 * ��ʼ�����ӳ�����
		 */
		// ���ӳ������������
		poolConf.maxActive = maxActive;
		// �����ӳ���Դ�ľ�ʱ�����������������ʱ��
		poolConf.maxWait = maxWait;
		// ���ӳ��������е�������
		poolConf.maxIdle = maxIdle;
		// ���������������ӡ���Դʱ���Ƿ���������Ч
		poolConf.testOnBorrow = testOnBorrow;
		// �����ӳء��黹������ʱ���Ƿ��⡰���ӡ��������Ч��
		poolConf.testOnReturn = testOnReturn;
		// ���ӿ��е���Сʱ�䣬�ﵽ��ֵ��������ӽ����ܻᱻ�Ƴ�
		poolConf.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
		// ����صĶ���
		asyncSocketPoolManager = new AsyncSocketPoolManager();
	}

	
	
	public String saveMsg (String msg) {
	 
		String returnMsg = null;
		AsyncSocketClient socket = null;
		SocketCltPool socketCltPool = null;
		try {
			socketCltPool = asyncSocketPoolManager.getPoolForSave(currentHost.get("IP"), currentHost.get("PORT"));
			socket = socketCltPool.getAsySocketClt();
			
			returnMsg = socket.sendMsg(msg);

		} catch (Exception e) {

			logger.error(e);
			if(currentHost.get("IP")==null){
				throw new RuntimeException("�޷���zk��ȡ�������ļ�����ڵ㣡",e);
			}
			throw new RuntimeException("������־�����쳣��",e);

		} finally {
			socketCltPool.releasAsySocketClt(socket);
		}
		
		return returnMsg;
		
	}
	public String saveByte (byte[] msg) {
		
		String returnMsg = null;
		AsyncSocketClient socket = null;
		SocketCltPool socketCltPool = null;
		try {
			socketCltPool = asyncSocketPoolManager.getPoolForSave(currentHost.get("IP"), currentHost.get("PORT"));
			socket = socketCltPool.getAsySocketClt();
			
			returnMsg = socket.sendByte(msg);
			
		} catch (Exception e) {
			logger.error(e);
			if(currentHost.get("IP")==null){
				throw new RuntimeException("�޷���zk��ȡ�������ļ�����ڵ㣡",e);
			}
			throw new RuntimeException("������־�����쳣��",e);
			
		} finally {
			socketCltPool.releasAsySocketClt(socket);
		}
		
		return returnMsg;
		
	}
	
	public String getMsg (String msg) {
       		
		//msg��ʽ����·�� | ָ��.���� |  | ip : port��
		String[] arg = msg.split("\\|");
		//ip
		String[] host = arg[2].split(":");
		String ip = host[0];
		String port = host[1];
		String returnMsg = null;
		AsyncSocketClient socket = null;
		SocketCltPool socketCltPool = null;
		try {
			socketCltPool = asyncSocketPoolManager.getPoolForGet(ip, port);
			socket = socketCltPool.getAsySocketClt();			
			
			returnMsg = socket.getMsg(msg);

		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException("��ȡ��־�����쳣��",e);

		} finally {
			socketCltPool.releasAsySocketClt(socket);
		}
		
		return returnMsg;
		
	}
	
	
	public static void calculateHost(List<String> childList, Map<String,String> currentHost){
		
		if(childList == null || childList.size()==0){
			throw new RuntimeException("zk���޿��õ��ļ���������");
		}
		Random rd = new Random(System.currentTimeMillis());//ʱ��Ϊ���ӻ�ȡ
		int seed = rd.nextInt(childList.size());
		String targetChild = childList.get(seed);
		String [] infos = targetChild.split(":");
		logger.info("��������ļ�����ڵ㣺"+targetChild);
		currentHost.put("IP", infos[0]);
		currentHost.put("PORT", infos[1]);
		
		
	}
	
	
	
}
