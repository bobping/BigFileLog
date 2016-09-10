package com.ztesoft.test;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ztesoft.springBeanLocator.BeanLocator;

public class sql {
	
	public static JdbcTemplate jdbcTemplate = (JdbcTemplate) BeanLocator.lookup("jdbcTemplate");
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		
        byte[] xpdlContent = null;
        String res = "";
        String sqlStr = "select * from om_order";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlStr);

        
        if(list.size()>0){
        	xpdlContent = (byte[]) list.get(0).get("TEMPLATE_DEFINE_CONTENT");
        	res = new String(xpdlContent,"utf-8");
        	System.out.println(res);
        }else{
        	
        }
		
		
	}

}
