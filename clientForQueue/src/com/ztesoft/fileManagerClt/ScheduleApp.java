package com.ztesoft.fileManagerClt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;



@Configuration
@EnableScheduling
public class ScheduleApp {

	
	//ר���ļ���־ϵͳ�ͻ��˵Ķ�ʱ����������
	@Autowired
	FilelogHostGet filelogHostGet;
//    @Bean
//    public FilelogHostGet bean() {
//        return new FilelogHostGet();
//    }
	
	
}
