package com.ztesoft.server;


import org.apache.log4j.Logger;

import com.ztesoft.springHelper.ApplicationContextUtils;

/**
*��������
*@author: liao.baoping
*@date�� ���ڣ�2016-3-18 ʱ�䣺����10:19:45
*@version 1.0
*/
public class ServerStart  {

	static Logger logger = Logger.getLogger(ServerStart.class);
	
	public static void main(String[] args) {
		
			ApplicationContextUtils.initContext();
	}


}