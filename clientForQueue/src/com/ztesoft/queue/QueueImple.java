package com.ztesoft.queue;

import com.ztesoft.queueObject.QueueObject;

public class QueueImple implements QueueInterface {

	
	
	@Override
	public Boolean put(String queueName, String key, Object value) {

		QueueObject obj = new QueueObject();
		obj.setInQueueFlag(1);
		obj.setObj(value);
		
		
		
		return null;
	}

	@Override
	public Object pop(String queueName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count(String queueName) {
		// TODO Auto-generated method stub
		return 0;
	}

}
