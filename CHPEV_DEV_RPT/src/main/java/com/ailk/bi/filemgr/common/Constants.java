package com.ailk.bi.filemgr.common;

import com.ailk.bi.common.sysconfig.GetSystemConfig;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: 2010-1-18 Time: 16:15:45
 * To change this template use File | Settings | File Templates.
 */
public interface Constants {

	// public final static String FILE_PATH_PREFIX = CommTool.getWebInfPath()
	// + GetSystemConfig.getBIBMConfig().getExtParam("upload_fold");

	public final static String FILE_PATH_PREFIX = GetSystemConfig.getBIBMConfig().getExtParam(
			"upload_fold");
}
