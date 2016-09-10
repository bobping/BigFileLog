package com.ztesoft.queueObject;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import com.ztesoft.queueObject.QueueObject;
import com.ztesoft.utils.ZObjectUtils;


public class QueueOperate {

	//QueueInterface queueInterface;
	
	public void queueOperate(byte[] byteObj) throws IOException, InterruptedException{
		QueueObject obj = (QueueObject) ZObjectUtils.toObject(byteObj);
		
		if(obj.getInQueueFlag() == 1 ){
		//	Object res = queueInterface.put(obj.getQueueName(), null, obj.getObj());
			
			System.out.println("队列有消息进入："+obj.getQueueName());
			
		//	return ZObjectUtils.toByteArray(res);
		}else{
			
		//	Object res = queueInterface.pop(obj.getQueueName());
			
			System.out.println("队列有消息出去："+obj.getQueueName());
			
		//	return ZObjectUtils.toByteArray(res);
		}

	}
	
	

}
