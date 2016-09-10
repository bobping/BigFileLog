package com.ztesoft.test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

//1.   创建一个类，名为 MyPriorityTransferQueue，扩展 PriorityBlockingQueue 类并实现 TransferQueue 接口。
public class MyPriorityTransferQueue<E> extends PriorityBlockingQueue<E> implements TransferQueue<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1134683753793784830L;

	// 2. 声明一个私有 AtomicInteger 属性，名为 counter，用来储存正在等待元素的消费者的数量。
	private AtomicInteger counter;

	// 3. 声明一个私有 LinkedBlockingQueue 属性，名为 transferred。
	private LinkedBlockingQueue<E> transfered;

	// 4. 声明一个私有 ReentrantLock 属性，名为 lock。
	private ReentrantLock lock;

	// 5. 实现类的构造函数，初始化它的属性值。
	public MyPriorityTransferQueue() {
		counter = new AtomicInteger(0);
		lock = new ReentrantLock();
		transfered = new LinkedBlockingQueue<E>();
	}

	// 6. 实现 tryTransfer() 方法。此方法尝试立刻发送元素给正在等待的消费者（如果可能）。如果没有任何消费者在等待，此方法返回
	// false 值。
	@Override
	public boolean tryTransfer(E e) {
		lock.lock();
		boolean value;
		if (counter.get() == 0) {
			value = false;
		} else {
			put(e);
			value = true;
		}
		lock.unlock();
		return value;
	}

	// 7. 实现 transfer() 方法。此方法尝试立刻发送元素给正在等待的消费者（如果可能）。如果没有任何消费者在等待，
	// 此方法把元素存入一个特殊queue，为了发送给第一个尝试获取一个元素的消费者并阻塞线程直到元素被消耗。

	@Override
	public void transfer(E e) throws InterruptedException {
		lock.lock();
		if (counter.get() != 0) {
			put(e);
			lock.unlock();
		} else {
			transfered.add(e);
			lock.unlock();
			synchronized (e) {
				e.wait();
			}
		}
	}

	// 8. 实现 tryTransfer() 方法，它接收3个参数：
	// 元素，和需要等待消费者的时间（如果没有消费者的话），和用来注明时间的单位。如果有消费者在等待，立刻发送元素。否则，转化时间到毫秒并使用
	// wait() 方法让线程进入休眠。当消费者取走元素时，如果线程在 wait() 方法里休眠，你将使用 notify() 方法唤醒它。
	@Override
	public boolean tryTransfer(E e, long timeout, TimeUnit unit) throws InterruptedException {
		lock.lock();
		if (counter.get() != 0) {
			put(e);
			lock.unlock();
			return true;
		} else {
			transfered.add(e);
			long newTimeout = TimeUnit.MILLISECONDS.convert(timeout, unit);
			lock.unlock();
			e.wait(newTimeout);
			lock.lock();

			if (transfered.contains(e)) {
				transfered.remove(e);
				lock.unlock();
				return false;
			} else {
				lock.unlock();
				return true;
			}
		}
	}

	// 9. 实现 hasWaitingConsumer() 方法。使用 counter 属性值来计算此方法的返回值。如果counter 的值大于0，放回
	// true。不然，返回 false。
	@Override
	public boolean hasWaitingConsumer() {
		return (counter.get() != 0);
	}

	// 10. 实现 getWaitingConsumerCount() 方法。返回counter 属性值。
	@Override
	public int getWaitingConsumerCount() {
		return counter.get();
	}

	// 11.实现 take() 方法。此方法是当消费者需要元素时被消费者调用的。首先，获取之前定义的锁并增加在等待的消费者数量。
	@Override
	public E take() throws InterruptedException {
		lock.lock();
		counter.incrementAndGet();

		// 12.如果在 transferred queue 中无任何元素。释放锁并使用 take()
		// 方法尝试从queue中获取元素，此方法将让线程进入睡眠直到有元素可以消耗。
		E value = transfered.poll();
		if (value == null) {
			lock.unlock();
			value = super.take();
			lock.lock();

			// 13. 否则，从transferred queue 中取走元素并唤醒正在等待要消耗元素的线程（如果有的话）。
		} else {
			synchronized (value) {
				value.notify();
			}
		}

		// 14. 最后，增加正在等待的消费者的数量并释放锁。
		counter.decrementAndGet();
		lock.unlock();
		return value;
	}
}
