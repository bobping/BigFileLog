package com.ztesoft.queue;



public interface QueueInterface {
	
	/**
	 * ����put����������Ϣ���������
	 * 
	 * @param queueName
	 * @param key
	 * @param value
	 * @
	 */
	public Boolean put(String queueName,String key,  Object value) ;


    /**
     * ����pop����������һ����Ϣ
     * @param queueName
     * @throws InterruptedException 
     * @
     */
    public Object pop(String queueName) throws InterruptedException ;

	/**
	 * ���г��Ȳ���
	 * 
	 * @param queueName
	 * @param key
	 * @return
	 * @
	 */
	public Object count(String queueName);

}
