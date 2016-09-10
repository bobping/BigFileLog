package com.ztesoft.queueObject;

import java.io.Serializable;


public class QueueObject implements Comparable<QueueObject>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1862320048180616055L;
	private String queueName ;
	//��Ϣ�����Ƿ����
	private boolean existQueue = true;
	//��Ϣ���л��ǳ��б�־ 1���У�0���У�2����
	private int inQueueFlag;
	
	private long queueSize;
	
	private int prority;
	
	private Object obj;

	public int getInQueueFlag() {
		return inQueueFlag;
	}

	public void setInQueueFlag(int inQueueFlag) {
		this.inQueueFlag = inQueueFlag;
	}
	
	public long getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(long queueSize) {
		this.queueSize = queueSize;
	}
	
	public boolean isExistQueue() {
		return existQueue;
	}

	public void setExistQueue(boolean existQueue) {
		this.existQueue = existQueue;
	}
	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public int getPrority() {
		return prority;
	}

	public void setPrority(int prority) {
		this.prority = prority;
	}

	@Override
	public int compareTo(QueueObject o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
