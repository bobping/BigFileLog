package com.ztesoft.fileManagerClt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;



@Configuration
@EnableScheduling
public class ScheduleApp {

	
	//专属文件日志系统客户端的定时任务配置类
	@Autowired
	FilelogHostGet filelogHostGet;
//    @Bean
//    public FilelogHostGet bean() {
//        return new FilelogHostGet();
//    }
	
	
}
