package com.ztesoft.springBeanLocator;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanLocator {
	

	private static Logger log = Logger.getLogger(BeanLocator.class.getName());
	private static final String cp = "classpath:spring.xml";
	//private static final String cp = "config"+File.separator+"spring"+File.separator+"spring.xml";
	private static ApplicationContext ctx = null;
	
	public BeanLocator()throws Exception{
		try {
			ctx = new ClassPathXmlApplicationContext(cp);;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	public BeanLocator(String source)throws Exception{
		try {
			ctx = new ClassPathXmlApplicationContext(source);;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	public static Object lookup(String name){
		if(ctx == null){
			
			log.info("=======================lookup spring.xml------------------------");
			ctx = new ClassPathXmlApplicationContext("classpath:spring.xml");;
			log.info("=======================loaded spring------------------------");

		}
		Object obj = ctx.getBean(name);
		if(obj == null){
			log.error("<frame>bean "+name+" not found!");
		}
		return obj;
	}


}
