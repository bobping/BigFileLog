package com.ztesoft.test;

import java.util.concurrent.TimeUnit;

//30. 创建例子的主类通过创建一个类，名为 Main 并添加 main()方法。
public class Main {

	public static void main(String[] args) throws Exception {

		// 31. 创建一个 MyPriorityTransferQueue 对象，名为 buffer。
		MyPriorityTransferQueue<Event> buffer = new MyPriorityTransferQueue<Event>();

		// 32. 创建一个 Producer 任务并运行 10 线程来执行任务。
		Producer producer = new Producer(buffer);
		Thread producerThreads[] = new Thread[10];
		for (int i = 0; i < producerThreads.length; i++) {
			producerThreads[i] = new Thread(producer);
			producerThreads[i].start();
		}

		
		Thread.sleep(1000);
		
		// 33.创建并运行一个 Consumer 任务。
		Consumer consumer = new Consumer(buffer);
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();

		// 34. 写入当前的消费者数量。
		System.out.printf("Main: Buffer: Consumer count: %d\n", buffer.getWaitingConsumerCount());

		// 35. 使用 transfer() 方法传输一个事件给消费者。
		Event myEvent = new Event("Core Event", 0);
		buffer.transfer(myEvent);
		System.out.printf("Main: My Event has ben transfered.\n");

		// 36. 使用 join() 方法等待生产者的完结。
		for (int i = 0; i < producerThreads.length; i++) {
			try {
				producerThreads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// 37. 让线程休眠1秒。
		TimeUnit.SECONDS.sleep(1);

		// 38.写入当前的消费者数量。
		System.out.printf("Main: Buffer: Consumer count: %d\n", buffer.getWaitingConsumerCount());

		// 39. 使用 transfer() 方法传输另一个事件。
		myEvent = new Event("Core Event 2", 0);
		buffer.transfer(myEvent);

		// 40. 使用 join() 方法等待消费者完结。
		consumerThread.join();

		// 41. 写信息表明程序结束。
		System.out.printf("Main: End of the program\n");
	}
}
