package com.ztesoft.test;

import java.util.Random;

//22. ʵ��һ���࣬��Ϊ Producer����ʵ�� Runnable �ӿڡ�
public class Producer implements Runnable {

	// 23. ����һ��˽�� MyPriorityTransferQueue ���ԣ����ղ������� Event �����ԣ���Ϊ
	// buffer����������������������ɵ��¼���
	private MyPriorityTransferQueue<Event> buffer;

	// 24. ʵ����Ĺ��캯������ʼ����������ֵ��
	public Producer(MyPriorityTransferQueue<Event> buffer) {
		this.buffer = buffer;
	}
	static Random random=new Random(47);
	// 25. ������ʵ�� run() ���������� 100 �� Event ���������Ǳ�������˳��������ȼ���Խ�ȴ��������ȼ�Խ�ߣ���ʹ�� put()
	// ���������ǲ���queue�С�
	public void run() {
		for (int i = 0; i < 100; i++) {
			Event event = new Event(Thread.currentThread().getName(), random.nextInt(10));
			buffer.put(event);
		}
	}
}