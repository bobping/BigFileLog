package com.ztesoft.queue;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ztesoft.queueObject.QueueObject;
import com.ztesoft.utils.ZObjectUtils;

@Component
public class QueueOperate {
	@Autowired
	QueueInterface queueInterface;
	
	public byte[] queueOperate(byte[] byteObj) throws IOException, InterruptedException{
		QueueObject obj = (QueueObject) ZObjectUtils.toObject(byteObj);
		
		if(obj.getInQueueFlag() == 1 ){
			Object res = queueInterface.put(obj.getQueueName(), null, obj);
			
			System.out.println("队列有消息进入："+obj.getQueueName());
			
			return ZObjectUtils.toByteArray(res);
		}else if(obj.getInQueueFlag() == 0 ){
			
			Object res = queueInterface.pop(obj.getQueueName());
			
			System.out.println("队列有消息出去："+obj.getQueueName());
			
			return ZObjectUtils.toByteArray(res);
		} else{
			return null;
		}

	}
	
	

}
