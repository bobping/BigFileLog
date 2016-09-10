package com.ztesoft.test;

//15. 实现一个类，名为 Event，扩展 Comparable 接口，把 Event 类参数化。
public class Event implements Comparable<Event> {

	// 16. 声明一个私有 String 属性，名为 thread，用来储存创建事件的线程的名字。
	private String thread;

	// 17. 声明一个私有 int 属性，名为 priority，用来储存事件的优先级。
	private int priority;

	// 18. 实现类的构造函数，初始化它的属性值。
	public Event(String thread, int priority) {
		this.thread = thread;
		this.priority = priority;
	}

	// 19. 实现一个方法，返回 thread 属性值。
	public String getThread() {
		return thread;
	}

	// 20. 实现一个方法，返回 priority 属性值。
	public int getPriority() {
		return priority;
	}

	// 21. 实现 compareTo() 方法。此方法把当前事件与接收到的参数事件进行对比。返回 -1，如果当前事件的优先级的级别高于参数；返回
	// 1，如果当前事件的优先级低于参数；如果相等，则返回 0。你将获得一个按优先级递减顺序排列的list。有高等级的事件就会被排到queue的最前面。
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