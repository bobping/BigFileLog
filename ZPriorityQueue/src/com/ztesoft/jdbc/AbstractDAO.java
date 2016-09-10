package com.ztesoft.jdbc;


import java.util.Map;


import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;




/**
*类描述：dao操作类的抽象父类
*@author: liao.baoping
*@date： 日期：2016-3-22 时间：下午8:09:06
*@version 1.0
*/
public abstract class AbstractDAO {

	
	@Resource(name="jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;
    
	

}
