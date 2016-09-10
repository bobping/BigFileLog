package com.ztesoft.test;

//26. ʵ��һ���࣬��Ϊ Consumer����Ҫʵ�� Runnable �ӿڡ�
public class Consumer implements Runnable {

	// 27. ����һ��˽�� MyPriorityTransferQueue ���ԣ������� Event �����ԣ���Ϊ
	// buffer��������ȡ�������¼������ߡ�
	private MyPriorityTransferQueue<Event> buffer;

	// 28. ʵ����Ĺ��캯������ʼ����������ֵ��
	public Consumer(MyPriorityTransferQueue<Event> buffer) {
		this.buffer = buffer;
	}

	// 29. ʵ�� run() ��������ʹ�� take() ��������1002 Events
	// (�������ʵ�ֵ�ȫ���¼������������¼����߳��������������ȼ���д��ٿ�̨��
	@Override
	public void run() {
		for (int i = 0; i < 1002; i++) {
			try {
				Event value = buffer.take();
				System.out.printf("Consumer: %s: %d\n", value.getThread(), value.getPriority());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}