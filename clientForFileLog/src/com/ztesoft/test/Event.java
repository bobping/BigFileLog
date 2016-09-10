package com.ztesoft.test;

//15. ʵ��һ���࣬��Ϊ Event����չ Comparable �ӿڣ��� Event ���������
public class Event implements Comparable<Event> {

	// 16. ����һ��˽�� String ���ԣ���Ϊ thread���������洴���¼����̵߳����֡�
	private String thread;

	// 17. ����һ��˽�� int ���ԣ���Ϊ priority�����������¼������ȼ���
	private int priority;

	// 18. ʵ����Ĺ��캯������ʼ����������ֵ��
	public Event(String thread, int priority) {
		this.thread = thread;
		this.priority = priority;
	}

	// 19. ʵ��һ������������ thread ����ֵ��
	public String getThread() {
		return thread;
	}

	// 20. ʵ��һ������������ priority ����ֵ��
	public int getPriority() {
		return priority;
	}

	// 21. ʵ�� compareTo() �������˷����ѵ�ǰ�¼�����յ��Ĳ����¼����жԱȡ����� -1�������ǰ�¼������ȼ��ļ�����ڲ���������
	// 1�������ǰ�¼������ȼ����ڲ����������ȣ��򷵻� 0���㽫���һ�������ȼ��ݼ�˳�����е�list���иߵȼ����¼��ͻᱻ�ŵ�queue����ǰ�档
	public int compareTo(Event e) {
		if (this.priority > e.getPriority()) {
			return -1;
		} else if (this.priority < e.getPriority()) {
			return 1;
		} else {
			return 0;
		}
	}
}