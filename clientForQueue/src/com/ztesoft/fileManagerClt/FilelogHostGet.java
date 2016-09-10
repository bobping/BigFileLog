package com.ztesoft.fileManagerClt;



import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ztesoft.conf.Configure;
import com.ztesoft.dubbo.fileManage.bean.FileManageBean;
import com.ztesoft.dubbo.fileManage.service.FileManageService;
import com.ztesoft.springBeanLocator.BeanLocator;
@Component
public class FilelogHostGet {
	
	private static final Logger logger = Logger.getLogger(FilelogHostGet.class);
	
	
	//改变量由下面updateHostIp定时任务维护，方法调用时间间隔参见spring配置
	private static String currentIp = Configure.DEFAULT_FILE_HOST;
	
	
	
	public String getCurrentIp(){
		
		return currentIp;
	}
	
	//定时10分钟更新一次
	@Scheduled(fixedRate=5000)
	public void updateHostIp(){
//		FileManageService fileManageService ;
//		try {
//			fileManageService = (FileManageService) BeanLocator.lookup("filelogHostGet");
//		} catch (Exception e) {
//			// TODO: handle exception
//			logger.error("无法实例化文件管理dubbo接口");
//			logger.error(e);
//			fileManageService=null;
//		}
//		
//		
//		try{
//			//调用dubbo接口获取最新ip
//			if(fileManageService!=null){
//				FileManageBean bean = fileManageService.queryFullAddressList();
//				currentIp = bean.getIp_service();
//				logger.info("更新文件服务节点ip信息["+currentIp+"]....");
//			}else{
//				currentIp=Configure.DEFAULT_FILE_HOST;
//				logger.warn("更新文件服务节点失败，使用默认节点["+Configure.DEFAULT_FILE_HOST+"]....");
//			}						
//		
//			//调用dubbo接口获取最新ip
//			FileManageBean bean = fileManageService.queryFullAddressList();
//			currentIp = bean.getIp_service();			
//			logger.info("10分钟更新一次文件服务节点ip信息["+currentIp+"]....");
//		}catch(Exception e){
//			logger.error(e);
//		}
//	
    }

	
	
	
}
