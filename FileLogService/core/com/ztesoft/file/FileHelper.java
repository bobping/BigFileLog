package com.ztesoft.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import com.ztesoft.file.ByteUtil;

public class FileHelper {

	public static final String VERSION_1 = "v1.0";
	public static final String VERSION_2 = "v2.0";
	public static final int MSGSIZE_START_IDX = 34;// 消息开始的长度
	public static final int MSGSIZE_LENGTH = 10;// 消息長度的字節數
	public static final int HEADER_SIZE = 64;
	public static final int VERSION_START_IDX = 30;
	public static final int VERSION_LENGTH = 4;

	private static SimpleDateFormat formatDate = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	
	//该方法将log长度记录写入文件,_funbackup后缀的方法用于代码更改备份记录
	public static byte[] createBytes_funbackup(String requestMsg, String version) throws UnsupportedEncodingException {

		String str = formatDate.format(new Date());

		// version=V1.0
		byte[] startFlag = ("--START=" + str + "--\n" + version).getBytes("utf-8");
		// System.out.println(startFlag.length);

		byte[] msgBody = null;
		byte[] msgLen = null;

		byte[] endFlag = "\n--END--\n".getBytes("utf-8");
		byte[] header = new byte[64];
		byte[] msg = null;

		// header
		ByteUtil.copyto(header, startFlag, 0, startFlag.length);

		// header = 3+4+10+1+1
		if (VERSION_1.equals(version)) {
			msg = requestMsg.getBytes("utf-8");
		} else if (VERSION_2.equals(version)) {
			msg = zip(requestMsg.getBytes("utf-8"));
		}

		// msg size
		msgLen = new byte[MSGSIZE_LENGTH];
		byte[] tempBodyLen = (msg.length + "").getBytes("utf-8");
		ByteUtil.copyto(msgLen, tempBodyLen, MSGSIZE_LENGTH
				- tempBodyLen.length, tempBodyLen.length);
		ByteUtil.copyto(header, msgLen, startFlag.length, 10);

		int length = header.length + msg.length + endFlag.length;

		msgBody = new byte[length];
		ByteUtil.copyto(msgBody, header, 0, HEADER_SIZE);
		ByteUtil.copyto(msgBody, msg, HEADER_SIZE, msg.length);
		ByteUtil.copyto(msgBody, endFlag, HEADER_SIZE + msg.length,
				endFlag.length);

		return msgBody;
	}

	
	static byte[] endFlag = "\n--END--\n".getBytes(Charset.forName("utf-8"));;
	static byte[] header = new byte[64];
	
	public static byte[] createBytes(String requestMsg, String version) throws UnsupportedEncodingException {

		String str = formatDate.format(new Date());

		// version=V1.0
		byte[] startFlag = ("--START=" + str + "--\n" + version).getBytes("utf-8");
		// System.out.println(startFlag.length);

		byte[] msgBody = null;
		byte[] msgLen = null;

		//byte[] endFlag = "\n--END--\n".getBytes("utf-8");
		//byte[] header = new byte[64];
		byte[] msg = null;

		// header
		ByteUtil.copyto(header, startFlag, 0, startFlag.length);

		// header = 3+4+10+1+1
		if (VERSION_1.equals(version)) {
			msg = requestMsg.getBytes("utf-8");
		} else if (VERSION_2.equals(version)) {
			msg = zip(requestMsg.getBytes("utf-8"));
		}

		int length = HEADER_SIZE + msg.length + 9;

		msgBody = new byte[length];
		ByteUtil.copyto(msgBody, header, 0, HEADER_SIZE);
		ByteUtil.copyto(msgBody, msg, HEADER_SIZE, msg.length);
		ByteUtil.copyto(msgBody, endFlag, HEADER_SIZE + msg.length,9);

		return msgBody;
	}

	
	public static byte[] zip(byte[] bytes) {

		ByteArrayOutputStream out = null;
		CompressorOutputStream cos = null;
		try {
			out = new ByteArrayOutputStream();
			cos = new GzipCompressorOutputStream(out);
			cos.write(bytes);
			cos.close();

			byte[] rs = out.toByteArray();
			out.close();
			return rs;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

		return null;
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[] uzip(byte[] bytes) {

		CompressorInputStream cis = null;
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		try {
			bais = new ByteArrayInputStream(bytes);
			cis = new GzipCompressorInputStream(bais);
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int size = 0;
			while ((size = cis.read(buffer)) > 0) {
				baos.write(buffer, 0, size);
			}
			byte[] rs = baos.toByteArray();
			return rs;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (cis != null) {
				try {
					cis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (bais != null) {
				try {
					bais.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		return null;
	}

	/**
	*描述：MappedByteBuffer专属释放方法
	*@author: liao.baoping
	*@date： 日期：2016-3-10 时间：下午6:01:55
	*@param buffer
	*@throws Exception
	*/
	public static void clean(final Object buffer) throws Exception {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				try {
					Method getCleanerMethod = buffer.getClass().getMethod(
							"cleaner", new Class[0]);
					getCleanerMethod.setAccessible(true);
					sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod
							.invoke(buffer, new Object[0]);
					cleaner.clean();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});

	}
	
	public static final SimpleDateFormat sFormat = new SimpleDateFormat("yyMMddHHmmssS");
	
	/**
	*描述：生成报文的唯一id
	*@author: liao.baoping
	*@date： 日期：2016-3-24 时间：下午1:25:40
	*@param position
	*@return
	*/
	public static String generateLogId(long position){
		
		Date now = new Date();
		
		return sFormat.format(now)+"."+position;
		
	}
	
	// header = 3+4+10+1+1
	public static void main(String[] args) {
//		String str = "xmj524 is hansome boy...lbp is also handsome boy!!!";
//		System.out.println(str.getBytes().length);
//		byte[] b = FileHelper.createBytes(str, "v2.0");
////		ByteUtil.showBytes(b);
//		System.out.println(new String(b));
//
//		byte[] a = new byte[10];
//		System.arraycopy(b, 34, a, 0, 10);
//		System.out.println("length >>>" + new String(a) + " & " + a.length);
//		byte[] c = new byte[60];
//		System.arraycopy(b, 64, c, 0, 60);
//		System.out.println(new String(FileHelper.uzip(c)).trim());
//		System.out.println();

		long p1 = System.currentTimeMillis();
		System.out.println(generateLogId(99999));
		
//		UUID.randomUUID();
		System.out.println(System.currentTimeMillis()-p1);
		
	}

}
