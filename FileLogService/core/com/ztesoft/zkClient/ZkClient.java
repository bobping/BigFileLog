package com.ztesoft.zkClient;


import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.ztesoft.config.Configure;



public class ZkClient {

	private static Logger logger = Logger.getLogger(ZkClient.class);

	private String zkAddress = "127.0.0.1:2181";
	private Integer sessionTimeOutMs = 10000;
	private Integer conectTimeOut = 10000;
	
	private static final String zkPathParent = "/fileServers";
	private static final String zkPath = zkPathParent + "/" +Configure.FILE_LOG_HOST+":"+Configure.WRITER_PORT;
	
	private CuratorFramework client;
	
	public String getZkAddress() {
		return zkAddress;
	}


	public void setZkAddress(String zkAddress) {
		this.zkAddress = zkAddress;
	}



	public Integer getSessionTimeOutMs() {
		return sessionTimeOutMs;
	}


	public void setSessionTimeOutMs(Integer sessionTimeOutMs) {
		this.sessionTimeOutMs = sessionTimeOutMs;
	}


	public Integer getConectTimeOut() {
		return conectTimeOut;
	}


	public void setConectTimeOut(Integer conectTimeOut) {
		this.conectTimeOut = conectTimeOut;
	}


	public CuratorFramework getClient() {
		return client;
	}


	public void setClient(CuratorFramework client) {
		this.client = client;
	}


	
	public void init() {

		//TODO:��������
		
		try {
			
			client = CuratorFrameworkFactory.builder().connectString(zkAddress)
					.sessionTimeoutMs(sessionTimeOutMs).connectionTimeoutMs(conectTimeOut)
					.canBeReadOnly(false)
					.retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
					.namespace("fileZk").build();
			client.start();
			
			Stat stat = client.checkExists().forPath(zkPath);
			if(stat==null){
				logger.info("�½�zk·����"+zkPath );
				client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
				.forPath(zkPath);
			}else{
				logger.info("ɾ��ԭ��zk·�����½�zk·����"+zkPath);
				client.delete().forPath(zkPath);
				client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
				.forPath(zkPath);
			}

		} catch (Exception e) {
			logger.error("ע��zk��ַʧ�ܣ�");
			throw new RuntimeException(e);
		}
		
		
	}
	
	public void close(){
		if(client!=null){
			client.close();
		}
		
	}
	
	public List<String> getPath(){
		try {

			return this.client.getChildren().forPath(zkPathParent);
			
		} catch (Exception e) {
			logger.error("��ȡ������zk·��ʧ�ܣ�",e);
			throw new RuntimeException(e);
		}
	}
	
	public void setData(byte[] data){
		try {
			client.setData().forPath(zkPath, data);
		} catch (Exception e) {
			logger.error("��·������"+zkPath +"������dataʧ�ܣ�",e);
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		ZkClient zk = new ZkClient();
		zk.init();
		zk.getPath();
		Thread.sleep(100000);
		
	}
	

	
}
