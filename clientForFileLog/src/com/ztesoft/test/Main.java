package com.ztesoft.test;

import java.util.concurrent.TimeUnit;

//30. �������ӵ�����ͨ������һ���࣬��Ϊ Main ����� main()������
public class Main {

	public static void main(String[] args) throws Exception {

		// 31. ����һ�� MyPriorityTransferQueue ������Ϊ buffer��
		MyPriorityTransferQueue<Event> buffer = new MyPriorityTransferQueue<Event>();

		// 32. ����һ�� Producer �������� 10 �߳���ִ������
		Producer producer = new Producer(buffer);
		Thread producerThreads[] = new Thread[10];
		for (int i = 0; i < producerThreads.length; i++) {
			producerThreads[i] = new Thread(producer);
			producerThreads[i].start();
		}

		
		Thread.sleep(1000);
		
		// 33.����������һ�� Consumer ����
		Consumer consumer = new Consumer(buffer);
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();

		// 34. д�뵱ǰ��������������
		System.out.printf("Main: Buffer: Consumer count: %d\n", buffer.getWaitingConsumerCount());

		// 35. ʹ�� transfer() ��������һ���¼��������ߡ�
		Event myEvent = new Event("Core Event", 0);
		buffer.transfer(myEvent);
		System.out.printf("Main: My Event has ben transfered.\n");

		// 36. ʹ�� join() �����ȴ������ߵ���ᡣ
		for (int i = 0; i < producerThreads.length; i++) {
			try {
				producerThreads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// 37. ���߳�����1�롣
		TimeUnit.SECONDS.sleep(1);

		// 38.д�뵱ǰ��������������
		System.out.printf("Main: Buffer: Consumer count: %d\n", buffer.getWaitingConsumerCount());

		// 39. ʹ�� transfer() ����������һ���¼���
		myEvent = new Event("Core Event 2", 0);
		buffer.transfer(myEvent);

		// 40. ʹ�� join() �����ȴ���������ᡣ
		consumerThread.join();

		// 41. д��Ϣ�������������
		System.out.printf("Main: End of the program\n");
	}
}
