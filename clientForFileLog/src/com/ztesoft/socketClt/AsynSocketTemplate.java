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
	
	//以下赋值为默认值
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
	    	//初始化从zk计算可用文件服务器
	    	calculateHost(client.getChildren().forPath(zkPath), currentHost);
			
			/**
		     * 监听子节点的变化情况计算出最优服务
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
			logger.error("启动zk监听异常！",e);
			this.close();
		}
	    

		
	}

	public void close(){
		if(client!=null){
			try {
				childrenCache.close();
			} catch (IOException e) {
				throw new RuntimeException("关闭zk监听发生异常！", e);
			}
			client.close();
		}
	}
	
	private void initPoolConfig(){
		poolConf = new Config();
		/**
		 * 初始化连接池配置
		 */
		// 链接池中最大连接数
		poolConf.maxActive = maxActive;
		// 当连接池资源耗尽时，调用者最大阻塞的时间
		poolConf.maxWait = maxWait;
		// 链接池中最大空闲的连接数
		poolConf.maxIdle = maxIdle;
		// 向调用者输出“链接”资源时，是否检测是有有效
		poolConf.testOnBorrow = testOnBorrow;
		// 向连接池“归还”链接时，是否检测“链接”对象的有效性
		poolConf.testOnReturn = testOnReturn;
		// 连接空闲的最小时间，达到此值后空闲连接将可能会被移除
		poolConf.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
		// 管理池的对象
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
				throw new RuntimeException("无法从zk获取到可用文件服务节点！",e);
			}
			throw new RuntimeException("保存日志出现异常！",e);

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
				throw new RuntimeException("无法从zk获取到可用文件服务节点！",e);
			}
			throw new RuntimeException("保存日志出现异常！",e);
			
		} finally {
			socketCltPool.releasAsySocketClt(socket);
		}
		
		return returnMsg;
		
	}
	
	public String getMsg (String msg) {
       		
		//msg格式：【路径 | 指针.长度 |  | ip : port】
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
			throw new RuntimeException("读取日志出现异常！",e);

		} finally {
			socketCltPool.releasAsySocketClt(socket);
		}
		
		return returnMsg;
		
	}
	
	
	public static void calculateHost(List<String> childList, Map<String,String> currentHost){
		
		if(childList == null || childList.size()==0){
			throw new RuntimeException("zk上无可用的文件服务器！");
		}
		Random rd = new Random(System.currentTimeMillis());//时间为种子获取
		int seed = rd.nextInt(childList.size());
		String targetChild = childList.get(seed);
		String [] infos = targetChild.split(":");
		logger.info("随机更新文件服务节点："+targetChild);
		currentHost.put("IP", infos[0]);
		currentHost.put("PORT", infos[1]);
		
		
	}
	
	
	
}
