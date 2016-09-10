package com.ztesoft.queue;

import java.util.concurrent.PriorityBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ztesoft.queueManage.QueueManage;
import com.ztesoft.queueObject.QueueObject;

@Component
public class PriorityQueue implements QueueInterface{

	@Autowired
	QueueManage queueManage;
	
	@Override
	public Boolean put(String queueName, String key, Object value) {
		
		PriorityBlockingQueue<QueueObject> pQueue = queueManage.getQueueByQName(queueName);
		if(pQueue==null){
			pQueue = queueManage.create(queueName);
		}
		return pQueue.add((QueueObject) value);
		
	}

	@Override
	public Object pop(String queueName) throws InterruptedException {
		PriorityBlockingQueue<QueueObject> pQueue = queueManage.getQueueByQName(queueName);
		if(pQueue!=null){
			return pQueue.take();
		}else{
			QueueObject obj = new QueueObject();
			obj.setExistQueue(false);
			return obj;
		}
		
	}

	@Override
	public Object count(String queueName) {
		PriorityBlockingQueue<QueueObject> pQueue = queueManage.getQueueByQName(queueName);
		QueueObject obj = new QueueObject();
		if(pQueue!=null){
			obj.setQueueSize(pQueue.size());
			return obj;
		}else{
			obj.setExistQueue(false);
			return obj;
		}
		
	}

}
