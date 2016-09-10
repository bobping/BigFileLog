package com.ztesoft.test;

import java.util.Random;

//22. 实现一个类，名为 Producer，它实现 Runnable 接口。
public class Producer implements Runnable {

	// 23. 声明一个私有 MyPriorityTransferQueue 属性，接收参数化的 Event 类属性，名为
	// buffer，用来储存这个生产者生成的事件。
	private MyPriorityTransferQueue<Event> buffer;

	// 24. 实现类的构造函数，初始化它的属性值。
	public Producer(MyPriorityTransferQueue<Event> buffer) {
		this.buffer = buffer;
	}
	static Random random=new Random(47);
	// 25. 这个类的实现 run() 方法。创建 100 个 Event 对象，用他们被创建的顺序决定优先级（越先创建的优先级越高）并使用 put()
	// 方法把他们插入queue中。
	public void run() {
		for (int i = 0; i < 100; i++) {
			Event event = new Event(Thread.currentThread().getName(), random.nextInt(10));
			buffer.put(event);
		}
	}
}