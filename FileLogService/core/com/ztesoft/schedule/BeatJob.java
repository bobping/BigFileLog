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
*��������������ʱ����
*@author: liao.baoping
*@date�� ���ڣ�2016-9-7 ʱ�䣺����2:01:12
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
			// ʹ��Linux��topָ���ȡϵͳcpuʹ����
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

		//�ж��Ƿ�Ҫ������������
		if (isBeat) {
			int writeCounter = FileLogWriteHandler.getWriterCounter();
			// ���ô��̿ռ�ռ��
//			long freeDisk = new File(Configure.IOM_LOG_PATH).getFreeSpace() / 1024 / 1024;
			int usedDisk =(int) ((1- new File("./").getFreeSpace() / (float)new File("./").getTotalSpace())*100);
			

			String freeCpu = "0";
			if (System.getProperty("os.name").equals("Linux")) {
				// ��ȡcpu����ֵ
				freeCpu = new java.text.DecimalFormat("0.000").format(getCpuRateForLinux());
			}
			String sql = "update service_file_manage set create_date=?,enabled_cpu=?,enabled_disc=?,conn_number=? where manage_id=?";

			Date date = new Date();
			// ��������Ϊ �����ڣ�cpu����ֵ������ʣ�࣬�������� [����managerId����]
			Object[] params = new Object[] { date, freeCpu, usedDisk, writeCounter, managerId };

//			jdbcTemplate.update(sql, params);	
			logger.info("�������£�ʱ��[" + date + "]" + "��λʱ��������[" + writeCounter + "]");

			// System.out.println("date:" + new Date().toString());
		}else{
//			logger.info("______����������");
			
			try {
				List<String> sevList = zk.getPath();
				
				for(String sev : sevList){
					logger.info("�ļ��������ڵ㣺"+sev);
				}
				
			} catch (Exception e) {
				logger.error("����zk�쳣����������...");
				zk.init();
			}

			

			
			zk.setData("��װ������......".getBytes());
			logger.info("socket״̬��"+socketStart.getWriteSev().getAsyncServerSocketChannel().isOpen());
			
		}

	}
}
