package com.ztesoft.queue.fqueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.google.code.fqueue.util.Config;
import com.ztesoft.config.Configure;

public class FQueueManager {

	private static final Logger log = Logger.getLogger(FQueueManager.class);

	private Map<String, FileFQueue> queueMap = new HashMap<String, FileFQueue>();

	private FileFQueue fqueue = null;
	
	public FQueueManager() throws Exception {
		this.init();
	}

	private void init() throws Exception {
		List<FileFQueue> queues = new ArrayList<FileFQueue>();
		//队列地址		
		String path = "";//Configure.ASYNC_QUEUE_PATH; 
		//初始化队列
		fqueue = new FileFQueue(path, 1024 * 1024 * 200);
	}
	
	public FileFQueue getQueue(){	
		return fqueue;
	}

	public List<FileFQueue> getQueues(String queueName) {
		return null;
		
	}

	public Queue<byte[]> getQueueWithBuild(Config config) {
		throw new RuntimeException("Method not implement yet.");
	}

	public void addQueue(Config config) throws Exception {
		throw new RuntimeException("Method not implement yet.");
	}

	public void removeQueue(Config config) {
		if (config == null)
			return;
		throw new RuntimeException("Method not implement yet.");
	}

	public Set<String> getAllQueueNames() {
		return queueMap.keySet();
	}
	
	public static void main(String[] args) throws Exception {

		
		
		ExecutorService exe = Executors.newFixedThreadPool(2);
		ExecutorService exe2 = Executors.newFixedThreadPool(2);
		
		
		
		final FileFQueue queue = new FQueueManager().getQueue();
//		final FQueue queue2 = new FQueue("../lbp_ping",1024*1024);
//		FQueueMasterBakTransport fmbt = new FQueueMasterBakTransport("../lbp_ping",1024*1024,queue);
//		fmbt.execute();
		
		
		exe.execute(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
							for(int i = 0; i<10000;i++){
					boolean flag =queue.offer("lbp is a handsome boy!!!".getBytes());
					if(flag){
						synchronized (queue) {
							queue.notifyAll();
						}
					}
//			System.out.println(new String(queue.poll()));		
			}
			}
			
		});
		exe.execute(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
							for(int i = 0; i<10000;i++){
					queue.offer("+++++++++++++++++++++".getBytes());
//			System.out.println(new String(queue.poll()));		
			}
			}
			
		});
	
		exe.execute(new Runnable() {

			@Override
			public void run() {
				
				
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (int i = 0; i < 20000; i++) {

					System.out.println(new String(queue.poll()));

				

				}
			}

		});
			

			

	
	}
	

}
