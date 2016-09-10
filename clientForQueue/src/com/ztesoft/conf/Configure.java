package com.ztesoft.conf;

import java.util.ResourceBundle;

public class Configure {

	
	
	//读配置信息socketClt.properties
	public static final ResourceBundle resource = ResourceBundle.getBundle("socketClt");
	
	//预设文件服务器ip
	public static final String DEFAULT_FILE_HOST = resource.getString("default.file.host");
	//存报文端口
	public static final int SAVE_FILE_PORT = Integer.valueOf(resource.getString("save.port"));
	//取报文端口
	public static final int GET_FILE_PORT = Integer.valueOf(resource.getString("get.port"));
	//链接池中最大连接数
	public static final int POOL_MAX_ACTIVE = Integer.valueOf(resource.getString("pool.maxActive"));
	//当连接池资源耗尽时，调用者最大阻塞的时间
	public static final int POOL_MAX_WAIT = Integer.valueOf(resource.getString("pool.maxWait"));
	//链接池中最大空闲的连接数
	public static final int POOL_MAXIDLE = Integer.valueOf(resource.getString("pool.maxIdle"));
	//向调用者输出“链接”资源时，是否检测是有有效
	public static final boolean POOL_TESTONBORROW = (resource.getString("is.testOnBorrow").equals("true")?true:false);
	// 向连接池“归还”链接时，是否检测“链接”对象的有效性
	public static final boolean POOL_TESTONRETURN = (resource.getString("is.testOnReturn").equals("true")?true:false);	
	//连接空闲的最小时间，达到此值后空闲连接将可能会被移除
	public static final int POOL_MINEVICTABLEIDLETIMEMILLIS = Integer.valueOf(resource.getString("pool.minEvictableIdleTimeMillis"));
	
	public static void main(String[] args) {
		System.out.println(DEFAULT_FILE_HOST);
		System.out.println(SAVE_FILE_PORT);
		System.out.println(GET_FILE_PORT);
		System.out.println(POOL_MAX_ACTIVE);
		System.out.println(POOL_MAX_WAIT);
		System.out.println(POOL_MAXIDLE);
		System.out.println(POOL_TESTONBORROW);
		System.out.println(POOL_TESTONRETURN);
		System.out.println(POOL_MINEVICTABLEIDLETIMEMILLIS);
		
	}
	

}
