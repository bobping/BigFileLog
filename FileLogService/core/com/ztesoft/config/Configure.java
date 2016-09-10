package com.ztesoft.config;

import java.util.ResourceBundle;

public class Configure {
	
	
	public static final ResourceBundle resource = ResourceBundle.getBundle("config");
	
	public static final String FILE_LOG_HOST = resource.getString("fileLog.host");
	
	public static final int WRITER_PORT = Integer.valueOf(resource.getString("writer.port"));
	
	public static final int READER_PORT = Integer.valueOf(resource.getString("reader.port"));
	//日志文件存放路径
	public static final String IOM_LOG_PATH = (!resource.getString("iom.fileLog.path").equals("")?resource.getString("iom.fileLog.path"):"./LinuxFileLog");
	//最大读报文连接数
	public static final int READ_CON_NUM = Integer.valueOf(resource.getString("read.con.num"));
	//最大写报文连接数
	public static final int WRITE_CON_NUM = Integer.valueOf(resource.getString("write.con.num"));
	//是否备份日志
//	public static final boolean IS_SLAVE = (resource.getString("is.slave").equals("true")?true:false);
	//是否心跳
	public static final boolean IS_BEAT = (resource.getString("is.beat").equals("true")?true:false);
	
//	public static final boolean IS_MASTER = (resource.getString("is.master").equals("true")?true:false);	
//	
//	public static final String ASYNC_QUEUE_PATH = resource.getString("fqueue.path");
//	
//	public static final String SLAVE_IP = resource.getString("slave.host");
//	
//	public static final int SLAVE_PORT = Integer.valueOf(resource.getString("slave.port"));
//	
//	public static final String MANAGER_ID = resource.getString("manager.id");
//	//备份线程数
//	public static final int BACKUP_THREAD_NUM = Integer.valueOf(resource.getString("backup.thread.num"));

	
	public static void main(String[] args) {
		System.out.println(READER_PORT);
	}
	

}
