package com.ztesoft.test;

//26. 实现一个类，名为 Consumer，它要实现 Runnable 接口。
public class Consumer implements Runnable {

	// 27. 声明一个私有 MyPriorityTransferQueue 属性，参数化 Event 类属性，名为
	// buffer，用来获取这个类的事件消费者。
	private MyPriorityTransferQueue<Event> buffer;

	// 28. 实现类的构造函数，初始化它的属性值。
	public Consumer(MyPriorityTransferQueue<Event> buffer) {
		this.buffer = buffer;
	}

	// 29. 实现 run() 方法。它使用 take() 方法消耗1002 Events
	// (这个例子实现的全部事件）并把生成事件的线程数量和它的优先级别写入操控台。
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