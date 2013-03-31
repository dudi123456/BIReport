package com.fins.gt.util;

public class BytesUtils {

	public static final int BYTE_LEN = 1;
	public static final int SHORT_LEN = 2;
	public static final int INT_LEN = 4;
	public static final int FLOAT_LEN = 4;
	public static final int LONG_LEN = 8;
	public static final int DOUBLE_LEN = 8;

	public static byte[] shortToBytes(short num) {
		byte[] bytes = new byte[SHORT_LEN];
		int startIndex = 0;
		bytes[startIndex] = (byte) (num & 0xff);
		bytes[startIndex + 1] = (byte) ((num >> 8) & 0xff);
		return bytes;
	}

	public static byte[] intToBytes(int num) {
		byte[] bytes = new byte[INT_LEN];
		int startIndex = 0;
		bytes[startIndex + 0] = (byte) (num & 0xff);
		bytes[startIndex + 1] = (byte) ((num >> 8) & 0xff);
		bytes[startIndex + 2] = (byte) ((num >> 16) & 0xff);
		bytes[startIndex + 3] = (byte) ((num >> 24) & 0xff);
		return bytes;
	}

	public static byte[] floatToBytes(float fnum) {
		return intToBytes(Float.floatToIntBits(fnum));
	}

	public static byte[] longToBytes(long lnum) {
		byte[] bytes = new byte[LONG_LEN];
		int startIndex = 0;
		for (int i = 0; i < 8; i++)
			bytes[startIndex + i] = (byte) ((lnum >> (i * 8)) & 0xff);
		return bytes;
	}

	public static byte[] doubleToBytes(double dnum) {
		return longToBytes(Double.doubleToLongBits(dnum));
	}

	// base64 encoder
	public static char[] toBase64Chars(byte[] in) {
		int inLen = in.length;
		int outLen = ((inLen + 2) / 3) * 4;
		int outDataLen = (inLen * 4 + 2) / 3;
		char[] out = new char[outLen];
		int ip = 0;
		int op = 0;
		while (ip < inLen) {
			int i0 = in[ip++] & 0xff;
			int i1 = ip < inLen ? in[ip++] & 0xff : 0;
			int i2 = ip < inLen ? in[ip++] & 0xff : 0;
			int o0 = i0 >>> 2;
			int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
			int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
			int o3 = i2 & 0x3F;
			out[op++] = nibblesMap[o0];
			out[op++] = nibblesMap[o1];
			out[op] = op < outDataLen ? nibblesMap[o2] : '=';
			op++;
			out[op] = op < outDataLen ? nibblesMap[o3] : '=';
			op++;
		}
		return out;
	}

	private static char[] nibblesMap = new char[64];
	static {
		int i = 0;
		for (char c = 'A'; c <= 'Z'; c++)
			nibblesMap[i++] = c;
		for (char c = 'a'; c <= 'z'; c++)
			nibblesMap[i++] = c;
		for (char c = '0'; c <= '9'; c++)
			nibblesMap[i++] = c;
		nibblesMap[i++] = '+';
		nibblesMap[i++] = '/';
	}
}
