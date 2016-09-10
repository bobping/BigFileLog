package com.ztesoft.schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.code.fqueue.util.Config;
import com.ztesoft.handler.FileLogWriteHandler;
import com.ztesoft.server.SocketStart;
import com.ztesoft.config.Configure;
import com.ztesoft.zkClient.ZkClient;

/**
*类描述：心跳定时任务
*@author: liao.baoping
*@date： 日期：2016-9-7 时间：下午2:01:12
*@version 1.0
*/
public class BeatJob {

	private final static Logger logger = Logger.getLogger(BeatJob.class);

	private static boolean isBeat = Configure.IS_BEAT;
	
	private ZkClient zk;

	@Autowired
	private SocketStart socketStart;
	// managerID
	private String managerId = "1";//Configure.MANAGER_ID;

	private static double getCpuRateForLinux() {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader brStat = null;
		StringTokenizer tokenStat = null;
		try {
			// 使用Linux的top指令获取系统cpu使用率
			Process process = Runtime.getRuntime().exec("top -b -n 1");
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			brStat = new BufferedReader(isr);

			brStat.readLine();
			brStat.readLine();

			tokenStat = new StringTokenizer(brStat.readLine());
			tokenStat.nextToken();
			String usrUsage = tokenStat.nextToken();

			Float usage = new Float(usrUsage.substring(0, usrUsage.indexOf("%")));

			return (1 - usage.floatValue() / 100);

		} catch (IOException ioe) {
			try {
				if (is != null)
					is.close();
				if (isr != null)
					isr.close();
				if (brStat != null)
					brStat.close();
			} catch (IOException io) {
				System.out.println(io.getMessage());
			}
			return 1;
		} finally {
			try {
				if (is != null)
					is.close();
				if (isr != null)
					isr.close();
				if (brStat != null)
					brStat.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

	}

	public ZkClient getZk() {
		return zk;
	}

	public void setZk(ZkClient zk) {
		this.zk = zk;
	}

	public void dealForCron() {

		//判断是否要更新心跳数据
		if (isBeat) {
			int writeCounter = FileLogWriteHandler.getWriterCounter();
			// 已用磁盘空间占比
//			long freeDisk = new File(Configure.IOM_LOG_PATH).getFreeSpace() / 1024 / 1024;
			int usedDisk =(int) ((1- new File("./").getFreeSpace() / (float)new File("./").getTotalSpace())*100);
			

			String freeCpu = "0";
			if (System.getProperty("os.name").equals("Linux")) {
				// 获取cpu空闲值
				freeCpu = new java.text.DecimalFormat("0.000").format(getCpuRateForLinux());
			}
			String sql = "update service_file_manage set create_date=?,enabled_cpu=?,enabled_disc=?,conn_number=? where manage_id=?";

			Date date = new Date();
			// 更新数据为 ：日期，cpu空闲值，磁盘剩余，请求数量 [根据managerId更新]
			Object[] params = new Object[] { date, freeCpu, usedDisk, writeCounter, managerId };

//			jdbcTemplate.update(sql, params);	
			logger.info("心跳更新：时间[" + date + "]" + "单位时间请求量[" + writeCounter + "]");

			// System.out.println("date:" + new Date().toString());
		}else{
//			logger.info("______不更新心跳");
			
			try {
				List<String> sevList = zk.getPath();
				
				for(String sev : sevList){
					logger.info("文件服务器节点："+sev);
				}
				
			} catch (Exception e) {
				logger.error("连接zk异常，尝试重连...");
				zk.init();
			}

			

			
			zk.setData("假装有数据......".getBytes());
			logger.info("socket状态："+socketStart.getWriteSev().getAsyncServerSocketChannel().isOpen());
			
		}

	}
}
