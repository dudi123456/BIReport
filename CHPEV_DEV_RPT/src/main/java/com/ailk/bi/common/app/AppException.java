package com.ailk.bi.common.app;

/**
 * <p>
 * Title: iBusiness
 * </p>
 * <p>
 * Description: 应用程序的异常基本类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author wiseking
 * @version 1.0
 */

public class AppException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8329936322853816744L;

	String strErrorCode = null;

	public AppException() {
	}

	public AppException(String str) {
		super(str);
	}

	public AppException(Exception e) {
		super(e.toString());
	}

	/**
	 * 构造函数
	 * 
	 * @param strErrCode
	 *            错误码
	 * @param strMsg
	 *            出错信息
	 */
	public AppException(String strErrCode, String strMsg) {
		super(strMsg);
		this.strErrorCode = strErrCode;
	}

	/**
	 * 返回错误码
	 * 
	 * @return 错误码
	 */
	public String getErrorCode() {
		return strErrorCode;
	}
}