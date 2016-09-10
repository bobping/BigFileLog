package com.ztesoft.socketClt;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.ztesoft.fileManagerClt.FilelogHostGet;

/**
 * @author bobping
 *
 */
@Component
public class AsynSocketTemplate {

	private static final Logger logger = Logger.getLogger(AsynSocketTemplate.class); 

	@Autowired
	private AsyncSocketPoolManager asyncSocketPoolManager;
	
//	@Autowired
	private FilelogHostGet filelogHostGet = new FilelogHostGet();
//	
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
	
	
	public byte[] sendMsg (byte[] msg) {
	 
		//从文件日志管理器获取ip地址
		String host = filelogHostGet.getCurrentIp();
//		String host = "127.0.0.1";
		
		byte[] returnMsg = null;
		AsyncSocketClient socket = null;
		SocketCltPool socketCltPool = null;
		try {
			socketCltPool = asyncSocketPoolManager.getPoolForSave(host);
			socket = socketCltPool.getAsySocketClt();
			
			returnMsg = socket.sendObjSer(msg);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e);

		} finally {
			socketCltPool.releasAsySocketClt(socket);
		}
		
		return returnMsg;
		
	}
	
	public String getMsg (String msg) {
       		
		//msg格式：【路径 | 指针.长度 | 报文唯一id | 主ip |备用 ip】
		String[] arg = msg.split("\\|");
		
		//主ip
		String host = arg[3];
		//备用ip
		String bkHost = arg[4];
		//logId
		String logId = arg[2];
		
		String returnMsg = null;
		AsyncSocketClient socket = null;
		SocketCltPool socketCltPool = null;
		try {
			socketCltPool = asyncSocketPoolManager.getPoolForGet(host);
			socket = socketCltPool.getAsySocketClt();			
			
			returnMsg = socket.getMsg(msg);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e);
			logger.warn("尝试从文件日志备用节点["+bkHost+"]获取日志");
			returnMsg = null;//getMsgFromBackupHost(bkHost,logId);

		} finally {
			socketCltPool.releasAsySocketClt(socket);
		}
		
		return returnMsg;
		
	}
	
//	public String getMsgFromBackupHost(String host,String logId) {
//		
//		AsyncSocketClient socket = null;
//		SocketCltPool socketCltPool = null;
//		String backupMsg = null;
//		String sql = "SELECT * FROM FILE_LOG_BACKUP WHERE ID = ?";
//		Map<String, Object> resmap = jdbcTemplate.queryForMap(sql, logId);
//		String backupFileInfo = (String) resmap.get("path");
//		//System.out.println(backupFileInfo);
//		
//		try{
//			socketCltPool = asyncSocketPoolManager.getPoolForGet(host);
//			socket = socketCltPool.getAsySocketClt();	
//			backupMsg = 	socket.getMsg(backupFileInfo);
//		}catch(Exception e){
//			logger.error("备用节点也未能获取日志。!");
//		}finally{
//			socketCltPool.releasAsySocketClt(socket);
//		}
//		
//		return backupMsg;
//		
//		
//		
//	}
	
}
