package com.ztesoft.server;


import org.apache.log4j.Logger;

import com.ztesoft.springHelper.ApplicationContextUtils;

/**
*类描述：
*@author: liao.baoping
*@date： 日期：2016-3-18 时间：上午10:19:45
*@version 1.0
*/
public class ServerStart  {

	static Logger logger = Logger.getLogger(ServerStart.class);
	
	public static void main(String[] args) {
		
			ApplicationContextUtils.initContext();
	}


}