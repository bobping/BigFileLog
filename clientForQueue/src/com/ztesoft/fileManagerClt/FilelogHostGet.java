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
	
	
	//�ı���������updateHostIp��ʱ����ά������������ʱ�����μ�spring����
	private static String currentIp = Configure.DEFAULT_FILE_HOST;
	
	
	
	public String getCurrentIp(){
		
		return currentIp;
	}
	
	//��ʱ10���Ӹ���һ��
	@Scheduled(fixedRate=5000)
	public void updateHostIp(){
//		FileManageService fileManageService ;
//		try {
//			fileManageService = (FileManageService) BeanLocator.lookup("filelogHostGet");
//		} catch (Exception e) {
//			// TODO: handle exception
//			logger.error("�޷�ʵ�����ļ�����dubbo�ӿ�");
//			logger.error(e);
//			fileManageService=null;
//		}
//		
//		
//		try{
//			//����dubbo�ӿڻ�ȡ����ip
//			if(fileManageService!=null){
//				FileManageBean bean = fileManageService.queryFullAddressList();
//				currentIp = bean.getIp_service();
//				logger.info("�����ļ�����ڵ�ip��Ϣ["+currentIp+"]....");
//			}else{
//				currentIp=Configure.DEFAULT_FILE_HOST;
//				logger.warn("�����ļ�����ڵ�ʧ�ܣ�ʹ��Ĭ�Ͻڵ�["+Configure.DEFAULT_FILE_HOST+"]....");
//			}						
//		
//			//����dubbo�ӿڻ�ȡ����ip
//			FileManageBean bean = fileManageService.queryFullAddressList();
//			currentIp = bean.getIp_service();			
//			logger.info("10���Ӹ���һ���ļ�����ڵ�ip��Ϣ["+currentIp+"]....");
//		}catch(Exception e){
//			logger.error(e);
//		}
//	
    }

	
	
	
}
