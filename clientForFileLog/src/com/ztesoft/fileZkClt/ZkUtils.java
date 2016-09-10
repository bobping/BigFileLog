package com.ztesoft.fileZkClt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;

import com.ztesoft.socketClt.AsynSocketTemplate;
import com.ztesoft.springBeanLocator.BeanLocator;


public class ZkUtils {

	private static Logger logger = Logger.getLogger(ZkUtils.class);

	private String zkAddress = "127.0.0.1:2181";
	private String sessionTimeOutMs;
	private String conectTimeOut;
	
	
	private Map<String,String> currentHost = new HashMap<>();
	
	private static ConcurrentHashMap<String, ChildData> hostMap = new ConcurrentHashMap<String, ChildData>();
	
	private CuratorFramework client;
	
	public String getZkAddress() {
		return zkAddress;
	}


	public void setZkAddress(String zkAddress) {
		this.zkAddress = zkAddress;
	}



	public String getSessionTimeOutMs() {
		return sessionTimeOutMs;
	}


	public void setSessionTimeOutMs(String sessionTimeOutMs) {
		this.sessionTimeOutMs = sessionTimeOutMs;
	}


	public String getConectTimeOut() {
		return conectTimeOut;
	}


	public void setConectTimeOut(String conectTimeOut) {
		this.conectTimeOut = conectTimeOut;
	}


	public CuratorFramework getClient() {
		return client;
	}


	public void setClient(CuratorFramework client) {
		this.client = client;
	}


	
	public void init() {
		logger.info(zkAddress);
		client = CuratorFrameworkFactory.builder().connectString(zkAddress)
				.sessionTimeoutMs(10000).connectionTimeoutMs(10000)
				.canBeReadOnly(false)
				.retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
				.namespace("fileZk").build();
		client.start();
		
		
		/**
	     * 监听子节点的变化情况
	     */
	    final PathChildrenCache childrenCache = new PathChildrenCache(client, "/zk-huey", true);
	    try {
			childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
		} catch (Exception e) {
			logger.error(e);
		}
	    childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework arg0, PathChildrenCacheEvent event) throws Exception {

//	            switch (event.getType()) {
//	            case CHILD_ADDED:
//	            	
//	              System.out.println("CHILD_ADDED: " + event.getData().getPath());
//	              List<ChildData> childList = childrenCache.getCurrentData();
//	              for(ChildData childe : childList){
//	            	  System.out.println(childe.getPath());
//	              }
//	              break;
//	            case CHILD_REMOVED:
//	              System.out.println("CHILD_REMOVED: " + event.getData().getPath());
//
//	              for(ChildData childe : childrenCache.getCurrentData()){
//	            	  System.out.println(childe.getPath());
//	              }
//	              break;
//	            case CHILD_UPDATED:
//	              System.out.println("CHILD_UPDATED: " + event.getData().getPath());
//	              break;
//	            default:
//	              break;
//			}
				
				List<ChildData> childList = childrenCache.getCurrentData();
			
				childDatacalculateHost(childList, currentHost);
				
				System.out.println(currentHost.get("IP")+":"+currentHost.get("PORT"));

				
				
				
			}  
		});
		
		
	}
	
	public void close(){
		if(client!=null){
			client.close();
			
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		

		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("134.96.188.150:9181,134.96.188.151:9181,134.96.188.152:9181")
				.sessionTimeoutMs(10000).connectionTimeoutMs(10000)
				.canBeReadOnly(false)
				.retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
				.namespace("fileZk").build();
		
		client.start();
		
		try {
			System.out.println(client.blockUntilConnected(10, TimeUnit.SECONDS));
			
			System.out.println("阻塞。。。");
			for(String str : client.getChildren().forPath("/fileServers")){
				System.out.println(str);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("结束");
		
//		AsynSocketTemplate asynSocketTemplate = (AsynSocketTemplate) BeanLocator.lookup("asynSocketTemplate");
//		
//		String info = asynSocketTemplate.saveMsg("dkljflakjdflk");
//		System.out.println(info);
//		
//		String res = asynSocketTemplate.getMsg(info);
//		System.out.println(res);

		
		
		Thread.sleep(100000);
	}
	
	public static void childDatacalculateHost(List<ChildData> childList, Map<String,String> currentHost){
		
		if(childList == null || childList.size()==0){
			throw new RuntimeException("zk上无可用的文件服务器！");
		}
		Random rd = new Random(System.currentTimeMillis());//时间为种子获取
		int seed = rd.nextInt(childList.size());
		
		System.out.println("获取的随机数："+seed);
		ChildData targetChild = childList.get(seed);
		String hostInfo = targetChild.getPath().substring(9, targetChild.getPath().length());
		String [] infos = hostInfo.split(":");
		System.out.println(targetChild.getPath());
		currentHost.put("IP", infos[0]);
		currentHost.put("PORT", infos[1]);
		
		
	}
	
}
