package com.ztesoft.conf;

import java.util.ResourceBundle;

public class Configure {

	
	
	//��������ϢsocketClt.properties
	public static final ResourceBundle resource = ResourceBundle.getBundle("socketClt");
	
	//Ԥ���ļ�������ip
	public static final String DEFAULT_FILE_HOST = resource.getString("default.file.host");
	//�汨�Ķ˿�
	public static final int SAVE_FILE_PORT = Integer.valueOf(resource.getString("save.port"));
	//ȡ���Ķ˿�
	public static final int GET_FILE_PORT = Integer.valueOf(resource.getString("get.port"));
	//���ӳ������������
	public static final int POOL_MAX_ACTIVE = Integer.valueOf(resource.getString("pool.maxActive"));
	//�����ӳ���Դ�ľ�ʱ�����������������ʱ��
	public static final int POOL_MAX_WAIT = Integer.valueOf(resource.getString("pool.maxWait"));
	//���ӳ��������е�������
	public static final int POOL_MAXIDLE = Integer.valueOf(resource.getString("pool.maxIdle"));
	//���������������ӡ���Դʱ���Ƿ���������Ч
	public static final boolean POOL_TESTONBORROW = (resource.getString("is.testOnBorrow").equals("true")?true:false);
	// �����ӳء��黹������ʱ���Ƿ��⡰���ӡ��������Ч��
	public static final boolean POOL_TESTONRETURN = (resource.getString("is.testOnReturn").equals("true")?true:false);	
	//���ӿ��е���Сʱ�䣬�ﵽ��ֵ��������ӽ����ܻᱻ�Ƴ�
	public static final int POOL_MINEVICTABLEIDLETIMEMILLIS = Integer.valueOf(resource.getString("pool.minEvictableIdleTimeMillis"));
	
	public static void main(String[] args) {
		System.out.println(DEFAULT_FILE_HOST);
		System.out.println(SAVE_FILE_PORT);
		System.out.println(GET_FILE_PORT);
		System.out.println(POOL_MAX_ACTIVE);
		System.out.println(POOL_MAX_WAIT);
		System.out.println(POOL_MAXIDLE);
		System.out.println(POOL_TESTONBORROW);
		System.out.println(POOL_TESTONRETURN);
		System.out.println(POOL_MINEVICTABLEIDLETIMEMILLIS);
		
	}
	

}
