package com.ailk.bi.connect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import com.jcraft.jzlib.*;

abstract class AbstractConnector implements BackConnector {

	protected DataPackage inDataBuffer = new DataPackage(); // 连接输入缓冲区

	protected DataPackage outDataBuffer = new DataPackage(); // 连接输出缓冲区

	protected boolean bDevMode = false; // 连接开发状态

	/*
	 * 构造器设定默认为非调试、运行模式
	 */
	AbstractConnector() {
		bDevMode = false;
	}

	/*
	 * 获得当前连接是否处于调试状态
	 */
	public final void setDevelopMode(boolean devMode) {
		bDevMode = devMode;
	}

	/*
	 * 获取当前连接是否处于开发状态
	 */
	public final boolean isDevelopMode() {
		return bDevMode;
	}

	/**
	 * 设置发送缓冲区，返回原发送缓冲区的引用
	 */
	public DataPackage setInDataBuffer(DataPackage inBuf) {
		if (inBuf == null)
			return inDataBuffer;

		DataPackage tmp = inDataBuffer;
		inDataBuffer = inBuf;

		return tmp;
	}

	/*
	 * 返回输入缓冲区
	 */
	public final DataPackage getInDataBuffer() {
		return inDataBuffer;
	}

	/*
	 * 返回输出缓冲区
	 */
	public final DataPackage getOutDataBuffer() {
		return outDataBuffer;
	}

	/*
	 * 本方法实现将inBuffer写入XML文件，并从XML文件中读入outBuffer
	 */
	protected DataPackage callService0(String servName, String serviceName)
			throws DataException {
		outDataBuffer.clear();
		// 以后再这儿实现与XML文件的交互操作

		String xmlPfx = System.getProperty("connect.xmlpfx");
		if (xmlPfx == null)
			xmlPfx = "";

		inDataBuffer.saveToXml(xmlPfx + serviceName + "_out.xml");
		outDataBuffer.loadFromXml(xmlPfx + serviceName + "_in.xml");

		return outDataBuffer;
	}

	/*
	 * 发起调用后台交易
	 */
	protected int compress(byte[] src, int src_offset, byte[] dest,
			int dest_offset, int length) throws ConnectException {
		int ret;

		/*
		 * Zlib 压缩和解压类.
		 */
		ZStream zlibStream = new ZStream();

		ret = zlibStream.deflateInit(JZlib.Z_DEFAULT_COMPRESSION);
		if (ret != JZlib.Z_OK) {
			throw new ConnectException("deflatinit error:" + ret);
		}

		/*
		 * 输入将要压缩的数据类.
		 */
		zlibStream.next_in = src;
		zlibStream.next_in_index = src_offset;

		/*
		 * 压缩后输出结果.
		 */
		zlibStream.next_out = dest;
		zlibStream.next_out_index = dest_offset;
		int remained = dest.length - dest_offset;

		/*
		 * 开始实行压缩 Compress.
		 */
		while (zlibStream.total_in != length && zlibStream.total_out < remained) {
			/*
			 * Small buffers.
			 */
			zlibStream.avail_in = zlibStream.avail_out = 1;
			ret = zlibStream.deflate(JZlib.Z_NO_FLUSH);
			if (ret != JZlib.Z_OK) {
				throw new ConnectException("deflate error :" + ret);
			}
		}

		while (true) {
			zlibStream.avail_out = 1;
			ret = zlibStream.deflate(JZlib.Z_FINISH);
			if (ret == JZlib.Z_STREAM_END) {
				break;
			} else if (ret != JZlib.Z_OK) {
				throw new ConnectException("deflate error :" + ret);
			}
		}

		/*
		 * 压缩结束.
		 */

		ret = zlibStream.deflateEnd();
		if (ret != JZlib.Z_OK) {
			throw new ConnectException("deflateEnd error :" + ret);
		}

		return (int) zlibStream.total_out;
	}

	/*
	 * 
	 */
	protected int uncompress(byte[] src, int src_offset, byte[] dest,
			int dest_offset, int length) throws ConnectException {
		int ret;

		ZStream zlibStream = new ZStream();

		zlibStream.next_in = src;
		zlibStream.next_in_index = src_offset;

		zlibStream.next_out = dest;
		zlibStream.next_out_index = dest_offset;

		ret = zlibStream.inflateInit();
		if (ret != JZlib.Z_OK) {
			throw new ConnectException("inflateInit error :" + ret);
		}

		while (true) {
			zlibStream.avail_in = zlibStream.avail_out = 1;
			ret = zlibStream.inflate(JZlib.Z_NO_FLUSH);

			if (ret == JZlib.Z_STREAM_END) {
				break;
			} else if (ret != JZlib.Z_OK) {
				// wiseking add here can not parse error at 2004 5
				// 12**********************
				// throw new ConnectException("inflate error :" + ret+"
				// "+zlibStream.msg);
				break;
			}
		}

		ret = zlibStream.inflateEnd();
		if (ret != JZlib.Z_OK) {
			throw new ConnectException("inflateEnd error :" + ret);
		}

		return (int) zlibStream.total_out;
	}

	/*
	 * 
	 */
	public abstract DataPackage callService(String serverName,
			String serviceName) throws ConnectException, DataException;

	/*
	 * 
	 */
	public abstract DataPackage callService(String serverName,
			String serviceName, int flag) throws ConnectException,
			DataException;

}