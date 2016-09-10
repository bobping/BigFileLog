/**
 * 
 */
package com.ztesoft.utils;


import java.io.*;

/**
 * @author lebronye
 * @说明 对象序列化与反序列的操作
 */
public class ZObjectUtils {
	
	/**
	 * 对象转byte数组
	 * 
	 * @param object
	 * @return
	 * @throws OssException 
	 */
	public static byte[] toByteArray(Object object) throws IOException {
		if(object == null) {
			return null;
		}
		byte[] bytes = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
			oos.flush();
			bytes = bos.toByteArray();
		} catch (IOException ex) {
			throw new IOException("object to byte array IOException.");
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				throw new IOException("object to byte array IOException.");
			}
		}
		return bytes;
	}

	/**
	 * byte数组转对象
	 * 
	 * @param bytes
	 * @return
	 * @throws OssException 
	 */
	public static Object toObject(byte[] bytes) throws IOException {
		if(bytes == null) {
			return null;
		}
		Object obj = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			obj = ois.readObject();
		} catch (IOException ex) {
			throw new IOException( "byte to object array IOException.");
		} catch (ClassNotFoundException e) {
			throw new IOException("class not found exception.");
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				throw new IOException("object to byte array IOException.");
			}
		}
		return obj;
	}
	
	


	
	public static void main(String[] args) throws IOException {
		byte[] test=ZObjectUtils.toByteArray("Q_E_D");
		System.out.println(test);
		System.out.println(ZObjectUtils.toObject(test));
		
	}
	
}
