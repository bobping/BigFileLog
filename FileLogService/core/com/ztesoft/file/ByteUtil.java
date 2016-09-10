package com.ztesoft.file;

/**
 * 
 * <p>
 * Title: ByteUtil
 * </p>
 * <p>
 * Description: 一些字节处理的工具函数，用于一些比较底层的基于字节的协议转换
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author xioayuer
 * @version 1.0
 */
public class ByteUtil {
	/**
	 * 
	 */
	private ByteUtil() {

	}

	/**
	 * 初始化内存字节
	 * 
	 * @param src
	 *            byte[]
	 * @param c
	 *            byte
	 * @param pos
	 *            int
	 * @param count
	 *            int
	 * @return byte[]
	 */
	public static byte[] memset(byte[] src, byte c, int pos, int count) {
		if (pos < 0 || pos > src.length || count <= 0) {
			return src;
		}

		if (src == null) {
			src = new byte[pos + count]; // 也必须通过返回参数来获取值
		}

		for (int i = 0; i < count && pos + i < src.length; i++) {
			src[pos + i] = c;
		}

		return src;
	}

	/**
	 * 内存字节拷贝
	 * 
	 * @param dest
	 *            byte[]
	 * @param pos
	 *            int
	 * @param src
	 *            byte[]
	 * @param size
	 *            int
	 * @return byte[]
	 */
	public static byte[] memcpy(byte[] dest, int pos, byte[] src, int size) {
		if (size >= 0) {
			size = size > dest.length ? dest.length : size;
			System.arraycopy(src, 0, dest, pos, size);
		}
		return dest;
	}

	/**
	 * 字节头部填充
	 * 
	 * @param src
	 *            byte[]
	 * @param length
	 *            int
	 * @param fill
	 *            byte
	 * @return byte[]
	 */
	public static byte[] fillHead(byte[] src, int length, byte fill) {
		if (src.length >= length) {
			return src;
		}

		byte[] dest = new byte[length];
		ByteUtil.memset(dest, fill, 0, length);
		ByteUtil.memcpy(dest, length > src.length ? length - src.length : 0,
				src, src.length);
		return dest;
	}

	/**
	 * 字节尾部填充
	 * 
	 * @param src
	 *            byte[]
	 * @param length
	 *            int
	 * @param fill
	 *            byte
	 * @return byte[]
	 */
	public static byte[] fillTail(byte[] src, int length, byte fill) {
		if (src.length >= length) {
			return src;
		}

		byte[] dest = new byte[length];
		ByteUtil.memset(dest, fill, 0, length);
		ByteUtil.memcpy(dest, 0, src, src.length);
		return dest;
	}

	/**
	 * 插入字节到目标字节数组，使用者须保证目标空间足够，后面的字节被覆盖
	 * 
	 * @param dest
	 *            byte[]
	 * @param src
	 *            byte[]
	 * @param pos
	 *            int
	 * @param length
	 *            int
	 * @return byte[]
	 */
	public static byte[] insert(byte[] dest, byte[] src, int pos, int length) {
		if (length > dest.length) {
			length = dest.length;
		}

		for (int i = 0; i < pos + length; i++) {
			if (i >= pos && i < pos + length) {
				if (i + length < dest.length) {
					dest[i + length] = dest[i];
				}
				if (i - pos < src.length) {
					dest[i] = src[i - pos];
				}
			}
		}

		return dest;
	}

	/**
	 * 复制字节到目标字节数组，使用者须保证目标空间足够
	 * 
	 * @param dest
	 *            byte[]
	 * @param src
	 *            byte[]
	 * @param pos
	 *            int
	 * @param length
	 *            int
	 * @return byte[]
	 */
	public static byte[] copyto(byte[] dest, byte[] src, int pos, int length) {
		if (length > src.length) {
			length = src.length;
		}

		if (dest == null || dest.length < pos + length) {
			byte[] buf = new byte[pos + length];
			memcpy(buf, 0, dest, pos);
			dest = buf;
		}

		for (int i = 0; i < length; i++) {
			dest[pos + i] = src[i];
		}

		return dest;
	}

	/**
	 * 从源字节数组指定位置复制字节到目标字节数组，使用者须保证目标空间足够
	 * 
	 * @param dest
	 *            byte[]
	 * @param src
	 *            byte[]
	 * @param pos
	 *            int
	 * @param length
	 *            int
	 * @return byte[]
	 */
	public static byte[] copyfrom(byte[] dest, byte[] src, int pos, int length) {
		if (length > src.length) {
			length = src.length;
		}

		if (dest == null || dest.length < length) { // 只能通过返回参数返回，无法通过入参返回，与C语言不同。
			dest = new byte[length];
		}

		for (int i = 0; i < length; i++) {
			dest[i] = src[pos + i];
		}

		return copy(dest, 0, length); // 返回一个新建数组
	}

	/**
	 * 删除字节内容，返回删除后的内容
	 * 
	 * @param src
	 *            byte[]
	 * @param pos
	 *            int
	 * @param length
	 *            int
	 * @return byte[] 删除一些字节的新数组
	 */
	public static byte[] delete(byte src[], int pos, int length) {
		if (src == null) {
			return null;
		}

		if (pos > src.length || pos < 0 || length < 0) { // 非法参数
			return src;
		}

		if (pos + length > src.length) {
			length = src.length - pos;
		}

		byte[] dest = new byte[src.length - length];
		for (int i = 0; i < src.length; i++) {
			if (i - pos >= 0 && i - pos < length) {
				continue;
			}
			dest[i] = src[i];
		}
		return dest;
	}

	/**
	 * 字节拷贝，返回拷贝内容
	 * 
	 * @param src
	 *            byte[]
	 * @param pos
	 *            int
	 * @param size
	 *            int
	 * @return byte[]
	 */
	public static byte[] copy(byte[] src, int pos, int size) {
		if (pos < 0 || pos > src.length || size <= 0) { // 非法参数
			return null;
		}

		byte[] dest = new byte[src.length - pos < size ? src.length - pos
				: size];
		for (int i = 0; i < size && pos + i < src.length; i++) {
			dest[i] = src[pos + i];
		}

		return dest;
	}

	/**
	 * 转换成16进制的格式
	 * 
	 * @param length
	 *            int
	 * @param hexlen
	 *            int
	 * @param fillhead
	 *            byte
	 * @return byte[]
	 */
	public static byte[] toHexBytes(int length, int hexlen, byte fillhead) {
		if (hexlen <= 0) {
			return null;
		}

		byte dest[] = new byte[hexlen];
		ByteUtil.memset(dest, fillhead, 0, hexlen);
		byte[] src = Integer.toHexString(length).getBytes();
		System.arraycopy(src, 0, dest, hexlen - src.length > 0 ? hexlen
				- src.length : 0, src.length);

		return dest;
	}

	/**
	 * 
	 * @param b
	 *            byte[]
	 * @return int
	 */
	public static int bytes2int(byte[] b) {
		// byte[] b=new byte[]{1,2,3,4};
		int mask = 0xff;
		int temp = 0;
		int res = 0;
		for (int i = 0; i < 4; i++) {
			res <<= 8;
			temp = b[i] & mask;
			res |= temp;
		}
		return res;
	}

	/**
	 * 
	 * @param b
	 *            byte
	 * @return String
	 */
	public static String byte2hex(byte b) {
		String hex = Integer.toHexString((int) b & 0xff);
		if (hex.length() == 1) {
			hex = "0" + hex;
		}
		return hex;
	}

	/**
	 * 
	 * @param hex
	 *            String
	 * @return byte
	 */
	public static byte hex2byte(String hex) {
		return (byte) (Integer.parseInt(hex, 16) & 0xff);
	}

	/**
	 * 
	 * @param hex
	 *            String
	 * @return byte[]
	 */
	public static byte[] hex2bytes(String hex) {
		if (hex == null || hex.trim().length() == 0) {
			return null;
		}

		byte[] dest = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length() / 2; i++) {
			String h = hex.substring(2 * i, 2 * i + 2);
			dest[i] = hex2byte(h);
		}

		return dest;
	}

	/**
	 * 
	 * @param bytes
	 *            byte[]
	 * @return String
	 */
	public static String bytes2hex(byte[] bytes) {
		StringBuffer sb = null;
		if (bytes == null || bytes.length == 0) {
			return null;
		}

		sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(byte2hex(bytes[i]));
		}

		return sb.toString();
	}

	/**
	 * 
	 * @param num
	 *            int
	 * @return byte[]
	 */
	public static byte[] int2bytes(int num) {
		byte[] b = new byte[4];
		// int mask=0xff;
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
			// System.out.print(Integer.toHexString((int)b[i]));
		}
		return b;
	}

	/**
	 * 
	 * @param num
	 *            int
	 * @return byte[]
	 */
	public static byte[] short2bytes(int num) {
		num &= 0xffff;
		byte[] b = new byte[2];
		// int mask=0xff;
		for (int i = 0; i < 2; i++) {
			b[i] = (byte) (num >>> (8 - i * 8));
			// System.out.print(Integer.toHexString((int)b[i]));
		}
		return b;
	}

	/**
	 * byte2short
	 * 
	 * @param len
	 *            byte[]
	 * @return int
	 */
	public static int byte2short(byte[] b) {
		int mask = 0xff;
		int temp = 0;
		int res = 0;
		for (int i = 0; i < 2; i++) {
			res <<= 8;
			temp = b[i] & mask;
			res |= temp;
		}
		return res;
	}

	/**
	 * 
	 * @param h
	 *            int
	 * @return int
	 */
	public static int htonl(int h) {
		int nl = 0;
		int b = 0;

		for (int i = 0; i < 4; i++) {
			b = (h << (24 - i * 8)) & 0xff000000;
			// System.out.println(Integer.toHexString(b));
			// System.out.println(Integer.toHexString(b >> (i * 8)));
			nl = nl + (b >> (i * 8));
		}

		// /System.out.println(Integer.toHexString(nl));
		return nl;
	}

	public static byte[] htonlbytes(int h) {
		// return int2bytes(htonl(h));
		return int2bytes(h);
	}

	/**
	 * 
	 * @param h
	 *            int
	 * @return int
	 */
	public static int htons(int h) {
		int ns = 0;
		int b = 0;
		for (int i = 0; i < 2; i++) {
			b = (h << (8 - i * 8)) & 0xff00;
			// System.out.println(Integer.toHexString(b));
			// System.out.println(Integer.toHexString(b >> 8));
			ns = ns + (b >> (i * 8));
		}

		// System.out.println(Integer.toHexString(ns));
		return ns;
	}

	public static byte[] htonsbytes(int h) {
		// return short2bytes(htons(h));
		return short2bytes(h);
	}

	/**
	 * 
	 * @param n
	 *            int
	 * @return int
	 */
	public static int ntohl(int n) {
		return htonl(n);
	}

	public static int nbytestohl(byte[] n) {
		// return ntohl(bytes2int(n));
		return bytes2int(n);
	}

	public static int ntohs(int n) {
		return htons(n);
	}

	public static int nbytestohs(byte[] n) {
		// return ntohs(byte2short(n));
		return byte2short(n);
	}

	// /**
	// * getZteIHLRChecksum
	// * @param msg char[]
	// * @return char[]
	// */
	// public static char[] getZteIHLRChecksum(char[] msg) {
	// int i = 0, j = 0;
	// char[] checksum = new char[4];
	//
	// for (i = 0; i < msg.length / 4; i++) {
	// for (j = 0; j < 4; j++) {
	// checksum[j] ^= msg[i * 4 + j];
	// }
	// }
	//
	// for (j = 0; j < msg.length % 4; j++) {
	// checksum[j] ^= msg[i * 4 + j];
	// }
	//
	// for (i = 0; i < 4; i++) {
	// int k = ~checksum[i] & 0xff;
	// checksum[i] = (char) k;
	// }
	//
	// StringBuffer sb = new StringBuffer();
	// for (i = 0; i < 4; i++) {
	// String s = Integer.toHexString(checksum[i] & 0xff).toUpperCase();
	// if (s.length() < 2) {
	// sb.append("0").append(s);
	// }
	// else {
	// sb.append(s);
	// }
	// }
	//
	// return sb.toString().toCharArray();
	// }

	// /**
	// * getZtePCSChecksum
	// * @param msg char[]
	// * @return char[]
	// */
	// public static char[] getZtePCSChecksum(char[] msg) {
	// int i = 0, j = 0;
	// char[] checksum = new char[4];
	//
	// for (i = 2; i < (msg.length - 8) / 4; i++) {
	// for (j = 0; j < 4; j++) {
	// checksum[j] ^= msg[i * 4 + j];
	// }
	// }
	//
	// for (i = 0; i < 4; i++) {
	// int k = ~checksum[i] & 0xff;
	// checksum[i] = (char) k;
	// }
	//
	// StringBuffer sb = new StringBuffer();
	// for (i = 0; i < 4; i++) {
	// String s = Integer.toHexString(checksum[i] & 0xff).toUpperCase();
	// if (s.length() < 2) {
	// sb.append("0").append(s);
	// }
	// else {
	// sb.append(s);
	// }
	// }
	//
	// return sb.toString().toCharArray();
	// }

	/**
	 * 为比对字节好用，显示输出格尺
	 */
	public static void showGrid() {
		for (int i = 0; i < 10; i++) {
			System.out.print("0123456789");
		}
		System.out.println();
	}

	/**
	 * 
	 * @param buf
	 *            byte[]
	 */
	public static void showBytes(byte[] buf) {
		if (buf == null) {
			buf = new byte[0];
		}
		String hex = "";
		byte[] s = new byte[16];
		System.out.println("[BYTES INFO] : LENGTH = " + buf.length + "(0x"
				+ Integer.toHexString(buf.length) + ")");
		for (int i = 0; i < buf.length; i++) {
			if (i % 16 == 0) { // 行号
				hex = Integer.toHexString((i & 0xff) / 16); // hex 字符
				if (hex.length() == 1) {
					hex = "0" + hex;
				}
				System.out.print("[" + hex + "]  ");
			}

			hex = Integer.toHexString(((byte) buf[i]) & 0xff); // hex 字符
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			System.out.print(hex + " "); // 输出HEX
			s[i % 16] = buf[i];

			if (i != 0 && i % 8 == 7 && i % 16 != 15) { // 8 字节
				System.out.print("  ");
			}

			if (i != 0 && (i % 16 == 15 || i == buf.length - 1)) { // 16字节或结束
				if (i % 16 != 15) { // 补空格
					for (int k = i % 16; k < 16; k++) {
						if (k == 7) {
							System.out.print("  ");
						} else {
							System.out.print("   ");
						}
					}
				}

				System.out.print("  "); // 输出可打印字符
				for (int j = 0; j < i % 16 + 1; j++) {
					if (s[j] == 0 || s[j] <= ' ' || s[j] > 127) {
						s[j] = '.';
					}
					System.out.print((char) s[j]);
				}

				System.out.print("\n"); // 输出换行
			}
		}

		System.out.println("");
	}

	public static String showBytes4Log4j(byte[] buf) {
		StringBuffer sb = new StringBuffer();
		if (buf == null) {
			buf = new byte[0];
		}
		String hex = "";
		byte[] s = new byte[16];
		sb.append("[BYTES INFO] : LENGTH = " + buf.length + "(0x"
				+ Integer.toHexString(buf.length) + ")" + "\n");
		for (int i = 0; i < buf.length; i++) {
			if (i % 16 == 0) { // 行号
				hex = Integer.toHexString((i & 0xff) / 16); // hex 字符
				if (hex.length() == 1) {
					hex = "0" + hex;
				}
				sb.append("[" + hex + "]  ");
			}

			hex = Integer.toHexString(((byte) buf[i]) & 0xff); // hex 字符
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			sb.append(hex + " "); // 输出HEX
			s[i % 16] = buf[i];

			if (i != 0 && i % 8 == 7 && i % 16 != 15) { // 8 字节
				sb.append("  ");
			}

			if (i != 0 && (i % 16 == 15 || i == buf.length - 1)) { // 16字节或结束
				if (i % 16 != 15) { // 补空格
					for (int k = i % 16; k < 16; k++) {
						if (k == 7) {
							sb.append("  ");
						} else {
							sb.append("   ");
						}
					}
				}

				sb.append("  "); // 输出可打印字符
				for (int j = 0; j < i % 16 + 1; j++) {
					if (s[j] == 0 || s[j] <= ' ' || s[j] > 127) {
						s[j] = '.';
					}
					sb.append((char) s[j]);
				}

				sb.append("\n"); // 输出换行
			}
		}

		return sb.append("\n").toString();
	}

	/**
	 * 
	 * @param args
	 *            String[]
	 */
	public static void main(String[] args) {
		byte[] hex = hex2bytes("013233");
		String hhh = bytes2hex(hex);

		String dd = "SCSC003C1.00JS123456UASUAS  00000000DLGCON    00000001TXBEG     HBHB";
		// C5F6F7B2
		char[] s = dd.toCharArray();
		showBytes(dd.getBytes());
		// System.out.println(ByteUtil.getZteIHLRChecksum("HBHB".toCharArray()));
		// // it is ok. B7BDB7BD
		// System.out.println(ByteUtil.getZteIHLRChecksum(s)); // it is ok.
		// System.out.println(ByteUtil.getZtePCSChecksum(s));

		byte[] length = new byte[20];
		byte[] to = new byte[10];
		ByteUtil.memset(length, (byte) '0', 0, 20);
		ByteUtil.memcpy(to, 0, length, 20);
		System.out.println(new String(ByteUtil.toHexBytes(255, 2, (byte) '0')));
		System.out.println(new String(ByteUtil.fillHead(to, 20, (byte) '1')));
		System.out.println(new String(ByteUtil.fillTail(to, 15, (byte) '9')));
		System.out.println(new String(ByteUtil.copyto(to, "hello!".getBytes(),
				0, 10)));
		System.out.println(new String(ByteUtil.copyto(to, "Hello world!"
				.getBytes(), 2, 15)));

		System.out.println(new String(ByteUtil.copy(to, 5, 6)));
		System.out.println(new String(ByteUtil.delete("Hello world".getBytes(),
				5, 6)));
		System.out.println(new String(ByteUtil.insert(to, "Gogogo!".getBytes(),
				5, 5)));
		ByteUtil.memset(length, (byte) '5', 10, 10);
		System.out.println(new String(length));
		System.out.println(new String(ByteUtil.insert(length, "Gogogo!"
				.getBytes(), 8, 8)));

		ByteUtil.showGrid();

		System.out.println(new String(int2bytes(0x1c1d1e1f)));

		int n = htonl(0x12345678);
		System.out.println(Integer.toHexString(n));
		n = ntohl(n);
		System.out.println(Integer.toHexString(n));

		n = htons(0x1234);
		System.out.println(Integer.toHexString(n));
		n = htons(n);
		System.out.println(Integer.toHexString(n));

		byte[] z = htonlbytes(0x12345678);
		System.out.println(new String(z));
		z = htonsbytes(0x1234);
		System.out.println(new String(z));
	}
}
