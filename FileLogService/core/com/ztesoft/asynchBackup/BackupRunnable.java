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

//	// �Ƿ��첽����
//	private static boolean isSlave = Configure.IS_SLAVE;
//
//	private FQueueManager queue = Configure.IS_SLAVE ? (FQueueManager) ApplicationContextUtils.getBeanByName("fQueueManager") : null;
//	// ��������
//	private JdbcTemplate jdbcTemplate = Configure.IS_SLAVE ? (JdbcTemplate) ApplicationContextUtils.getBeanByName("jdbcTemplate") : null;
//	//socket�ͻ���
//	private AsynSocketTemplate asynSocketTemplate = Configure.IS_SLAVE ? (AsynSocketTemplate) ApplicationContextUtils
//			.getBeanByName("asynSocketTemplate") : null;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// �ж��Ƿ���Ҫ����
//		while (isSlave) {
//			byte[] bytes = null;
//			try {
//				// ��ȡ�Ķ��и�ʽΪ ����1|����2|����3|����4
//				// ����1Ϊ�ļ�λ�ã�����2Ϊ�ļ�ָ�룬����3Ϊ�ļ�Ψһid������4Ϊ�ļ�����
//				bytes = queue.getQueue().poll();
//
//				if (bytes == null) {
//					// �������Ϊ����Ϣ50ms
//					// System.out.println("���пգ���Ϣ50ms");
//					Thread.sleep(50);
//				} else {
//					String msg = new String(bytes, Charset.forName("utf-8"));
//					String[] msgs = msg.split("\\|");
//					String res = "";
//					// ���ر�����Ϣ
//					res = asynSocketTemplate.saveMsg(msgs[3]);
//
//					if (res != null && !res.equals("")) {
////						logger.info("slave ���أ�" + res);
//
//						// ƴװ��¼�����ݿ�
//						String sql = "insert into file_log_backup (id,path) values (?, ?)";
//						// ��־id,����·��
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
