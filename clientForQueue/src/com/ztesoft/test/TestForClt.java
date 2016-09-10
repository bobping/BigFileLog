package com.ztesoft.test;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ztesoft.fileManagerClt.FilelogHostGet;
import com.ztesoft.fileManagerClt.ScheduleApp;
import com.ztesoft.queueObject.QueueObject;
import com.ztesoft.socketClt.AsynSocketTemplate;
import com.ztesoft.springBeanLocator.BeanLocator;
import com.ztesoft.utils.ZObjectUtils;

public class TestForClt {
	
    public static byte[] byteMerger(byte[] byte1, byte[] byte2){  
        byte[] byte3 = new byte[byte1.length+byte2.length];  
        System.arraycopy(byte1, 0, byte3, 0, byte1.length);  
        System.arraycopy(byte2, 0, byte3, byte1.length, byte2.length);  
        return byte3;  
    }  

	public static AsynSocketTemplate asynSocketTemplate = (AsynSocketTemplate) BeanLocator.lookup("asynSocketTemplate");

	// public static FilelogHostGet filelogHostGet = (FilelogHostGet)
	// BeanLocator.lookup("filelogHostGet");

	// public static AsynSocketTemplate asynSocketTemplate = new
	// AsynSocketTemplate();

	// public static JdbcTemplate jdbcTemplate = (JdbcTemplate)
	// BeanLocator.lookup("jdbcTemplate");

	public static void main(String[] args) throws IOException, InterruptedException {

//		byte[] r = {5, 3, 1, 32, 1, 44, 34, 34, 22, 45, 11, 33 ,81, 95, 69, 95, 68 };
		
//		byte[] e = { 5, 3, 1, 32, 1, 44, 34, 34, 22, 45, 11, 33 };
//
//		byte[] a = "Q_E_D".getBytes("utf-8");
//		
//		r  = byteMerger(r,a);
//		
//		r = byteMerger(r,e);
//		
//		
//		
//		System.out.println("====================="+(r[0]==e[0]));
//		
//		
//		ByteBuffer buffer = ByteBuffer.wrap("jdlkjfadffk".getBytes("utf-8"));
//		ByteBuffer buffer2 = ByteBuffer.wrap("你好".getBytes("utf-8"));
//		buffer.put(buffer2);
//		
//		//buffer.flip();
//		System.out.println(buffer.position()+"-----"+buffer.limit());
//		buffer.flip();
//		
//		System.out.println(new String(buffer.array(),"utf-8"));
//		
//		System.out.println(buffer.position()+"-----"+buffer.limit());
		
	//	String content = new String(r, "utf-8");
		
		QueueObject obj = new QueueObject();
		
		obj.setObj("你好啊");
		obj.setInQueueFlag(1);
		obj.setQueueName("the first queue!");
		
		byte[] res = asynSocketTemplate.sendMsg(ZObjectUtils.toByteArray(obj));

//		for (int i = 0; i < 1; i++) {
//			String msg = asynSocketTemplate.getMsg(res);
//			byte[] a = msg.getBytes("utf-8");
//			System.out.println(msg);
//
//		}

	}
}
