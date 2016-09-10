package com.ztesoft.queue.fqueue;

import java.util.Iterator;
import org.apache.log4j.Logger;
import com.google.code.fqueue.FQueue;

/**
 * 
 * 
 */

public class FileFQueue  {
	
	private Logger logger = Logger.getLogger(FileFQueue.class);	
	private FQueue fQueue;
	
	
	public FileFQueue( String path, int size) throws Exception {
		fQueue = new FQueue(path, size);
	}
	
	public boolean offer(byte[] e) {
		return fQueue.offer(e);
	}
		
	public byte[] poll() {
		return fQueue.poll();
	}
	
	public byte[][] poll(int psize) {
		try {
			byte[][] result = new byte[psize][];
			for (int i = 0; i < psize; i++) {
				result[i] = fQueue.poll();
//				if (result[i] == null) {
//					break;
//				}
			}
			return result;
		} finally {
			
		}
	}
	
	public byte[] peek() {
		throw new RuntimeException("Method not implement yet.");
	}

	
	public Iterator<byte[]> iterator() {
		throw new RuntimeException("Method not implement yet.");
	}
	
	/**
	 * 生产者线程切换
	 * @param index
	 */
	public void producerSwitchMonitor(int index) {
		synchronized (this) {
			
		}
	}
	
	public int size() {
		return fQueue.size();
	}
	

}
