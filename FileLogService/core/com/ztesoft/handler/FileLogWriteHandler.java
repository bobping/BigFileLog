package com.ztesoft.handler;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.ztesoft.file.FileHelper;
import com.ztesoft.file.FileInfo;
import com.ztesoft.file.FileLogWriter;
import com.ztesoft.file.FileLogWriterOld;
import com.ztesoft.gidClient.service.SequenceService;
import com.ztesoft.queue.fqueue.FQueueManager;
import com.ztesoft.queue.fqueue.FileFQueue;
import com.ztesoft.config.Configure;
import com.ztesoft.springHelper.ApplicationContextUtils;

/**
 * ��������
 * 
 * @author: liao.baoping
 * @date�� ���ڣ�2016-3-15 ʱ�䣺����6:19:00
 * @version 1.0
 */
public class FileLogWriteHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

	private static FileLogWriterOld writer = new FileLogWriterOld();
	//�ж��Ƿ񱸷�
//	private static boolean isSlave = Configure.IS_SLAVE;

	private static final Logger logger = Logger.getLogger(FileLogWriteHandler.class);
	
	//ͳ��writer������ �ϴμ��ʱ��������ܼ�
	private static final AtomicInteger lastCount = new AtomicInteger(0);
	//ͳ��writer������ ����ʱ�������ܼ�
	private static final AtomicInteger currentCount = new AtomicInteger(0);
	//��Ҫ���ݵ�ʱ��ȡbean
//	private static final FQueueManager fqManager =Configure.IS_SLAVE ? (FQueueManager)ApplicationContextUtils.getBeanByName("fQueueManager"):null;
	//��Ҫ���ݵ�ʱ��ȡbean
//	private static SequenceService sequenceService =Configure.IS_SLAVE ? (SequenceService) ApplicationContextUtils.getBean("sequenceService"):null;

	//ip��Ϣ
	String ipInfo = "|"+Configure.FILE_LOG_HOST + ":" + Configure.READER_PORT + "D_E_L";
	
	AsynchronousServerSocketChannel asynchronousServerSocketChannel = null;

	public FileLogWriteHandler(AsynchronousServerSocketChannel asynchronousServerSocketChannel) {
		this.asynchronousServerSocketChannel = asynchronousServerSocketChannel;
	}

	@Override
	public void completed(AsynchronousSocketChannel asynchSocketChannel, Void attachment) {

		// Ϊ��һ������������׼��
		asynchronousServerSocketChannel.accept(null, this);
		ByteBuffer inbuffer = ByteBuffer.allocate(1024 * 64);
		ByteBuffer outbuffer = ByteBuffer.allocate(256);
		StringBuilder msgStr = new StringBuilder();
		FileInfo finfo = null;

		try {
			logger.info("Incoming connection from: " + asynchSocketChannel.getRemoteAddress());
			// ���ݴ���
			while (asynchSocketChannel.read(inbuffer).get() != -1) {
				
				inbuffer.flip();

				byte[] in = new byte[inbuffer.limit()];

				inbuffer.get(in);

				// logger.info(new String(in)); //����>>>��ʾд����־����
				
				msgStr = msgStr.append(new String(in,"utf-8"));

				//�ж��Ƿ������ȫ
				if (msgStr.toString().endsWith("D_E_L")) {

					currentCount.incrementAndGet();//ͳ��������
					
					finfo = writer.writeFile(FileHelper.createBytes(msgStr.toString(), "v1.0"));
					
//					//�ж��Ƿ񱸷�
//					if (isSlave) {
//
//						// ��gidservice��ȡlogΨһid
//						finfo.setLog_id(sequenceService.getSequence("file_log_id", "IOM"));
//
//						msgStr.setLength(msgStr.length()-5);
//						
//						// �����ĺͶ�Ӧ�Ĵ洢��Ϣ������У�������ʹ��
//						 fqManager.getQueue().offer((finfo.toString()+"|"+msgStr).getBytes("utf-8"));
//
//					}
					
					String finfoStr = finfo.toString() + ipInfo;
					outbuffer = ByteBuffer.wrap(finfoStr.getBytes("utf-8"));

					// logger.info(finfoStr);
					while(asynchSocketChannel.write(outbuffer).get()>0){
						logger.debug("����д�������δ�����...........");
					}
					msgStr.setLength(0);

				}
				
				inbuffer.clear();
				outbuffer.clear();

				

			}
		} catch (IOException | InterruptedException | ExecutionException ex) {
			logger.error(ex);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				SocketAddress remote = asynchSocketChannel.getRemoteAddress();
				asynchSocketChannel.close();
				logger.warn("Զ�̷�����:" + remote + "���ӹر�<<<<<<<<");
			} catch (IOException e) {
				logger.error(e);
			}
		}

	}

	@Override
	public void failed(Throwable exc, Void attachment) {
		// TODO Auto-generated method stub
		asynchronousServerSocketChannel.accept(null, this);

		throw new UnsupportedOperationException("Cannot accept connections!");
	}
	
	
	/**
	*������ͳ�����ε��ø÷���ʱ����ڵ�������
	*@author: liao.baoping
	*@date�� ���ڣ�2016-3-18 ʱ�䣺����1:44:04
	*@return
	*/
	public static int getWriterCounter(){
		int last = FileLogWriteHandler.lastCount.get();
		int current = FileLogWriteHandler.currentCount.get();
		if(last>current){
			FileLogWriteHandler.lastCount.set(0);
			FileLogWriteHandler.currentCount.set(0);
			
			return (current+Integer.MAX_VALUE)+(Integer.MAX_VALUE-last);			
		}else{
			FileLogWriteHandler.lastCount.set(current);
			
			return current-last;
		}
		
		
	}

}
