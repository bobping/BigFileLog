package com.ztesoft.jdbc;


import java.util.Map;


import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;




/**
*��������dao������ĳ�����
*@author: liao.baoping
*@date�� ���ڣ�2016-3-22 ʱ�䣺����8:09:06
*@version 1.0
*/
public abstract class AbstractDAO {

	
	@Resource(name="jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;
    
	

}
