package com.ztesoft.test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

//1.   ����һ���࣬��Ϊ MyPriorityTransferQueue����չ PriorityBlockingQueue �ಢʵ�� TransferQueue �ӿڡ�
public class MyPriorityTransferQueue<E> extends PriorityBlockingQueue<E> implements TransferQueue<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1134683753793784830L;

	// 2. ����һ��˽�� AtomicInteger ���ԣ���Ϊ counter�������������ڵȴ�Ԫ�ص������ߵ�������
	private AtomicInteger counter;

	// 3. ����һ��˽�� LinkedBlockingQueue ���ԣ���Ϊ transferred��
	private LinkedBlockingQueue<E> transfered;

	// 4. ����һ��˽�� ReentrantLock ���ԣ���Ϊ lock��
	private ReentrantLock lock;

	// 5. ʵ����Ĺ��캯������ʼ����������ֵ��
	public MyPriorityTransferQueue() {
		counter = new AtomicInteger(0);
		lock = new ReentrantLock();
		transfered = new LinkedBlockingQueue<E>();
	}

	// 6. ʵ�� tryTransfer() �������˷����������̷���Ԫ�ظ����ڵȴ��������ߣ�������ܣ������û���κ��������ڵȴ����˷�������
	// false ֵ��
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

	// 7. ʵ�� transfer() �������˷����������̷���Ԫ�ظ����ڵȴ��������ߣ�������ܣ������û���κ��������ڵȴ���
	// �˷�����Ԫ�ش���һ������queue��Ϊ�˷��͸���һ�����Ի�ȡһ��Ԫ�ص������߲������߳�ֱ��Ԫ�ر����ġ�

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

	// 8. ʵ�� tryTransfer() ������������3��������
	// Ԫ�أ�����Ҫ�ȴ������ߵ�ʱ�䣨���û�������ߵĻ�����������ע��ʱ��ĵ�λ��������������ڵȴ������̷���Ԫ�ء�����ת��ʱ�䵽���벢ʹ��
	// wait() �������߳̽������ߡ���������ȡ��Ԫ��ʱ������߳��� wait() ���������ߣ��㽫ʹ�� notify() ������������
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

	// 9. ʵ�� hasWaitingConsumer() ������ʹ�� counter ����ֵ������˷����ķ���ֵ�����counter ��ֵ����0���Ż�
	// true����Ȼ������ false��
	@Override
	public boolean hasWaitingConsumer() {
		return (counter.get() != 0);
	}

	// 10. ʵ�� getWaitingConsumerCount() ����������counter ����ֵ��
	@Override
	public int getWaitingConsumerCount() {
		return counter.get();
	}

	// 11.ʵ�� take() �������˷����ǵ���������ҪԪ��ʱ�������ߵ��õġ����ȣ���ȡ֮ǰ��������������ڵȴ���������������
	@Override
	public E take() throws InterruptedException {
		lock.lock();
		counter.incrementAndGet();

		// 12.����� transferred queue �����κ�Ԫ�ء��ͷ�����ʹ�� take()
		// �������Դ�queue�л�ȡԪ�أ��˷��������߳̽���˯��ֱ����Ԫ�ؿ������ġ�
		E value = transfered.poll();
		if (value == null) {
			lock.unlock();
			value = super.take();
			lock.lock();

			// 13. ���򣬴�transferred queue ��ȡ��Ԫ�ز��������ڵȴ�Ҫ����Ԫ�ص��̣߳�����еĻ�����
		} else {
			synchronized (value) {
				value.notify();
			}
		}

		// 14. ����������ڵȴ��������ߵ��������ͷ�����
		counter.decrementAndGet();
		lock.unlock();
		return value;
	}
}
