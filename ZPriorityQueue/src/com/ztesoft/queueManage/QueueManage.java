package com.ztesoft.queueManage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;
import org.springframework.stereotype.Component;

import com.ztesoft.queueObject.QueueObject;

@Component
public class QueueManage {

	private Map<String, PriorityBlockingQueue<QueueObject>> qNameMap = new HashMap<String, PriorityBlockingQueue<QueueObject>>();
	
	public synchronized PriorityBlockingQueue<QueueObject> create(String queueName) {

			if(!qNameMap.containsKey(queueName)){
				//创建新队列
				PriorityBlockingQueue<QueueObject> newQueue = new PriorityBlockingQueue<QueueObject>();
				qNameMap.put(queueName, newQueue);
				//System.out.println("创建================queue："+queueName);
				return newQueue;
			}else{
				return qNameMap.get(queueName);
			}
			
		
	}

	
	public PriorityBlockingQueue<QueueObject> getQueueByQName(String queueName) {

		if(qNameMap!=null){
			return qNameMap.get(queueName);
		}else{
			return null;
		}

	} 

	public static void main(String[] args) {
		
		Map<String,String> testMap = new HashMap<>();
		testMap.put("gent", "ddd");
		testMap.put("gent", "dddeeee");
		testMap.put("a", null);
		
		System.out.println(testMap.get("gent"));
		System.out.println(testMap.get("gent"));
		System.out.println(testMap.get("a"));
		System.out.println(testMap.get("adqqqqq"));
		
		
	}
	
	
	
}
