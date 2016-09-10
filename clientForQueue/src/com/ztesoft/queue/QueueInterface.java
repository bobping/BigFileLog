package com.ztesoft.queue;



public interface QueueInterface {
	
	/**
	 * 队列put操作，将消息放入队列中
	 * 
	 * @param queueName
	 * @param key
	 * @param value
	 * @
	 */
	public Boolean put(String queueName,String key,  Object value) ;


    /**
     * 队列pop操作，弹出一条消息
     * @param queueName
     * @
     */
    public Object pop(String queueName) ;

	/**
	 * 队列长度操作
	 * 
	 * @param queueName
	 * @param key
	 * @return
	 * @
	 */
	public long count(String queueName);

}
