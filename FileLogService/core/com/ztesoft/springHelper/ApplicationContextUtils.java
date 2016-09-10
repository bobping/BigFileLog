/**
 * @author zhang.qiaoxian
 * @date 2015年4月20日
 * @project 0_JSKT_Base
 *
 */
package com.ztesoft.springHelper;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * 全局上下文
 * zhong.kaijie	  2016年1月5日
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {
	private static Logger logger = Logger
			.getLogger(ApplicationContextUtils.class);
	private static final String DEFAULT_CONTEXT_XML = "/spring.xml";
	private static ApplicationContext applicationContext = null;

	/**
	 * 初始化spring上下文
	 * zhong.kaijie	 2016年1月6日
	 */
	public static void initContext() {
		initContext(DEFAULT_CONTEXT_XML);
	}

	/**
	 * 根据指定的配置文件初始化Spring上下文
	 * @param configLocation
	 * zhong.kaijie	 2016年1月6日
	 */
	public static void initContext(String configLocation) {
		if (applicationContext == null) {
			synchronized (ApplicationContextUtils.class) {
				if (applicationContext == null) {
					logger.info("Building factory init config location:"
							+ configLocation);

					applicationContext = new ClassPathXmlApplicationContext(
							configLocation);

					logger.info("Building factory success!");

				}
			}
		}
	}

	public static Object getBeanByName(String beanName) {
		return applicationContext.getBean(beanName);
	}


	public static <T> Map<String, T> getBeansByClass(Class<T> c) {
		// org.apache.commons.dbcp.BasicDataSource.class
		return applicationContext.getBeansOfType(c);
	}

	public static <T> String[] getBeanNamesForType(Class<T> c) {
		// org.apache.commons.dbcp.BasicDataSource.class
		return applicationContext.getBeanNamesForType(c);
	}

	public static <T> T getBean(String beanName, Class<T> type) {
		return applicationContext.getBean(beanName, type);
	}

	public static Object getBean(String beanName) {
		return getApplicationContext().getBean(beanName);
	}

	/**
	 * 根据类型获取bean
	 * @param type
	 * @return
	 * zhong.kaijie	 2016年1月5日
	 */
	public static <T> T getBean(Class<T> type) {
		return getApplicationContext().getBean(type);
	}

	/**
	 * 根据类型获取bean工厂中所有bean的Map集合
	 * @param type
	 * @return Map
	 * zhong.kaijie	 2016年1月5日
	 */
	public static <T> Map<String, T> getBeansMap(Class<T> type) {
		return getApplicationContext().getBeansOfType(type);
	}

	/**
	 * 根据类型获取bean工厂中所有bean的集合
	 * @param type
	 * @return	Collection
	 * zhong.kaijie	 2016年1月5日
	 */
	public static <T> Collection<T> getBeans(Class<T> type) {
		Map<String, T> map = getBeansMap(type);
		if (map == null || map.size() == 0) {
			return null;
		} else {
			return map.values();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		ApplicationContextUtils.applicationContext = applicationContext;
	}

	/**
	 * @return the beanFactory
	 */
	private  static ApplicationContext getApplicationContext() {
		return ApplicationContextUtils.applicationContext;
	}
	
	/**
	 * @return the beanFactory
	 */
	public  static DefaultListableBeanFactory getAutowireCapableBeanFactory() {
		
		return (DefaultListableBeanFactory)ApplicationContextUtils.applicationContext.getAutowireCapableBeanFactory();
	}
	
	public static void close() {
		((AbstractApplicationContext)ApplicationContextUtils.getApplicationContext()).close();
	}
}
