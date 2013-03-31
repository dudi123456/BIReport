package com.ailk.bi.connect;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public interface BackConnector {
	/*
	 * 获取当前连接是否处于开发状态
	 */
	public boolean isDevelopMode();

	/*
	 * 获得当前连接是否处于调试状态
	 */
	public void setDevelopMode(boolean devMode);

	/**
	 * 设置发送缓冲区，返回原发送缓冲区的引用
	 */
	public DataPackage setInDataBuffer(DataPackage inBuf);

	/*
	 * 返回输入缓冲区
	 */
	DataPackage getInDataBuffer();

	/*
	 * 返回输出缓冲区
	 */
	DataPackage getOutDataBuffer();

	/*
	 * 发起调用后台交易
	 */
	DataPackage callService(String serverName, String serviceName)
			throws ConnectException, DataException;

	/*
	 * 发起调用后台交易
	 */
	DataPackage callService(String serverName, String serviceName, int flag)
			throws ConnectException, DataException;

}
