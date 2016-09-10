package com.ztesoft.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class FileLogReader {
	
	//���ļ��л�ȡlog���ȷ�������
	public  String readMsg_back(String fileName, long position)
			throws Exception {
		File file = new File(fileName);
		if (!file.exists()) {
			file = new File(fileName + "_temp");
			if (!file.exists()) {
				throw new Exception("���Ҳ�����Ӧ���ļ�");
			}
		}

		FileInputStream fis = null;
		FileChannel fc = null;
		String result = "ϵͳ�쳣����ѯ����ָ������";
		byte[] arr = null;
		try {
			fis = new FileInputStream(file);
			fc = fis.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(FileHelper.HEADER_SIZE);
			fc.read(buffer,position);
			
			arr = buffer.array();
			buffer.clear();

			// msg size
			byte[] size = new byte[FileHelper.MSGSIZE_LENGTH];
			System.arraycopy(arr, FileHelper.MSGSIZE_START_IDX, size, 0,
					FileHelper.MSGSIZE_LENGTH);
			// version
			byte[] version = new byte[FileHelper.VERSION_LENGTH];
			System.arraycopy(arr, FileHelper.VERSION_START_IDX, version, 0,
					FileHelper.VERSION_LENGTH);

			buffer = ByteBuffer.allocate(Integer.parseInt(new String(size)
					.trim()));
			fc.read(buffer,position+FileHelper.HEADER_SIZE);  //header�ĳ���Ϊ64����header���濪ʼ��
			arr = buffer.array();
			buffer.clear();

			String versionStr = new String(version);
			if (FileHelper.VERSION_2.equals(versionStr)) {
				arr = FileHelper.uzip(arr);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("�ļ���������ʧ���쳣.");

		} finally {
			if (fc != null) {
				try {
					fc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		result = Base64.encode(arr);
		return result;
	}

	public  String readMsg(String fileName, String info)
			throws Exception {
		//��ʽ  ָ��.����
		String[] positions = info.split("\\.");
		long position = Long.valueOf(positions[0]);
		int length = Integer.valueOf(positions[1]);
		
		File file = new File(fileName);
		if (!file.exists()) {
			file = new File(fileName + "_temp");
			if (!file.exists()) {
				throw new Exception("���Ҳ�����Ӧ���ļ�"+fileName);
			}
		}

		FileInputStream fis = null;
		FileChannel fc = null;
		String result = "ϵͳ�쳣����ѯ����ָ������";
		byte[] arr = null;
		try {
			fis = new FileInputStream(file);
			fc = fis.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(FileHelper.HEADER_SIZE);
			fc.read(buffer,position);
			
			arr = buffer.array();
			buffer.clear();

			// version
			byte[] version = new byte[FileHelper.VERSION_LENGTH];
			System.arraycopy(arr, FileHelper.VERSION_START_IDX, version, 0,
					FileHelper.VERSION_LENGTH);

			buffer = ByteBuffer.allocate(length);
			fc.read(buffer,position+FileHelper.HEADER_SIZE);  //header�ĳ���Ϊ64����header���濪ʼ��
			arr = buffer.array();
			buffer.clear();

			String versionStr = new String(version);
			if (FileHelper.VERSION_2.equals(versionStr)) {
				arr = FileHelper.uzip(arr);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("�ļ���������ʧ���쳣.");

		} finally {
			if (fc != null) {
				try {
					fc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		result = new String(arr,"utf-8");
		return result;
	}
	
	public  String readMsgByMappedBuffer(String fileName, long position)
			throws Exception {
		File file = new File(fileName);
		if (!file.exists()) {
			file = new File(fileName + "_temp");
			if (!file.exists()) {
				throw new Exception("���Ҳ�����Ӧ���ļ�");
			}
		}
		FileInputStream fis = null;
		FileChannel fc = null;
		String result = "ϵͳ�쳣����ѯ����ָ������";
		byte[] arr = new byte[FileHelper.HEADER_SIZE];
		
		MappedByteBuffer mbb1 = null;
		MappedByteBuffer mbb2 = null;
		
		try {
			fis = new FileInputStream(file);
			fc = fis.getChannel();
//			long p1 = System.currentTimeMillis();
			mbb1 = fc.map(MapMode.READ_ONLY, position, FileHelper.HEADER_SIZE);
						
			mbb1.get(arr);			
//			FileHelper.clean(mbb);//�ͷ�mbb��������
//			long p2 = System.currentTimeMillis();
//			System.out.println(p2-p1);
			
			// msg size
			byte[] sizeByte = new byte[FileHelper.MSGSIZE_LENGTH];
			System.arraycopy(arr, FileHelper.MSGSIZE_START_IDX, sizeByte, 0,
					FileHelper.MSGSIZE_LENGTH);
			int size = Integer.parseInt(new String(sizeByte).trim());
			// version
			byte[] version = new byte[FileHelper.VERSION_LENGTH];
			System.arraycopy(arr, FileHelper.VERSION_START_IDX, version, 0,
					FileHelper.VERSION_LENGTH);

			
			mbb2 = fc.map(MapMode.READ_ONLY, position+FileHelper.HEADER_SIZE, size); //header�ĳ���Ϊ64����header���濪ʼ��
			
			arr = new byte[size];
		
			mbb2.get(arr);
	
//			FileHelper.clean(mbb);//�ͷ�mdd��������
			
			String versionStr = new String(version);
			if (FileHelper.VERSION_2.equals(versionStr)) {
				arr = FileHelper.uzip(arr);
			}

		} catch (Exception e) {
			e.printStackTrace();
			//��ֹmbb��ʱ��ռ���ļ���������Ǻ����޷����ļ����в���
			FileHelper.clean(mbb1);
			FileHelper.clean(mbb2);
			throw new Exception("�ļ���������ʧ���쳣.");

		} finally {
			if (fc != null) {
				try {
					fc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//��ֹmbb��ʱ��ռ���ļ���������Ǻ����޷����ļ����в���
			FileHelper.clean(mbb1);
			FileHelper.clean(mbb2);
		}
		result = Base64.encode(arr);
		return result;
	}
	
	
	public static void main(String[] args) throws IOException {
//		for (int i = 0; i < 100; i++) {
//			long s = System.currentTimeMillis();
//			try {
//				String result = FileLogReader
//						.readMsg(
//								"./data/default/2013-11-07/default-default-2013-11-07-192519732.dat",
//								12665835);
//				System.out.println("encode >> " + result);
//				System.out.println("decode >> "
//						+ new String(Base64.decode(result)) + " & "
//						+ (System.currentTimeMillis() - s) + "ms");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
//		FileInputStream fis = new FileInputStream(new File("D:\\LinuxFileLog\\odc\\2016-03-09\\odc-default-20160309221832815.dat"));
//		FileChannel fc = fis.getChannel();
//		long a = fc.size();
//		System.out.println(a);
//		System.out.println(fc.position());
		
		FileLogReader fileLogReader = new FileLogReader();
//		String fileName = "I:\\HelloOdc\\odc\\2016-01-25\\odc-default-20160125111139566.dat_temp";
//		long position = 117;
		try {

			long p2 = System.currentTimeMillis();
			String msg2 = fileLogReader.readMsg(".\\LinuxFileLog\\iom\\2016-04-13\\iom-default-20160413160748991.dat", "10370.10297");

			long p3 = System.currentTimeMillis();
			byte[] res2 = Base64.decode(msg2);
			
			System.out.println(new String(res2)+"\n"+"\n"+(p3-p2));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
