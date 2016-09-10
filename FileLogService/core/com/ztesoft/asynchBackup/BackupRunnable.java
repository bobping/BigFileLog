package com.ztesoft.asynchBackup;

import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ztesoft.asynSocketClet.AsynSocketTemplate;
import com.ztesoft.queue.fqueue.FQueueManager;
import com.ztesoft.config.Configure;
import com.ztesoft.springHelper.ApplicationContextUtils;

public class BackupRunnable implements Runnable {

	private static final Logger logger = Logger.getLogger(BackupRunnable.class);

//	// 是否异步备份
//	private static boolean isSlave = Configure.IS_SLAVE;
//
//	private FQueueManager queue = Configure.IS_SLAVE ? (FQueueManager) ApplicationContextUtils.getBeanByName("fQueueManager") : null;
//	// 数据链接
//	private JdbcTemplate jdbcTemplate = Configure.IS_SLAVE ? (JdbcTemplate) ApplicationContextUtils.getBeanByName("jdbcTemplate") : null;
//	//socket客户端
//	private AsynSocketTemplate asynSocketTemplate = Configure.IS_SLAVE ? (AsynSocketTemplate) ApplicationContextUtils
//			.getBeanByName("asynSocketTemplate") : null;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// 判断是否需要备份
//		while (isSlave) {
//			byte[] bytes = null;
//			try {
//				// 获取的队列格式为 参数1|参数2|参数3|参数4
//				// 参数1为文件位置，参数2为文件指针，参数3为文件唯一id，参数4为文件内容
//				bytes = queue.getQueue().poll();
//
//				if (bytes == null) {
//					// 如果队列为空休息50ms
//					// System.out.println("队列空，休息50ms");
//					Thread.sleep(50);
//				} else {
//					String msg = new String(bytes, Charset.forName("utf-8"));
//					String[] msgs = msg.split("\\|");
//					String res = "";
//					// 返回备份信息
//					res = asynSocketTemplate.saveMsg(msgs[3]);
//
//					if (res != null && !res.equals("")) {
////						logger.info("slave 返回：" + res);
//
//						// 拼装记录到数据库
//						String sql = "insert into file_log_backup (id,path) values (?, ?)";
//						// 日志id,备用路径
//
//						Object[] params = new Object[] { msgs[2], res };
//						jdbcTemplate.update(sql, params);
//
//					}
//
//				}
//			} catch (Exception e) {
//				
//
//			}
//
//		}

	}

}
