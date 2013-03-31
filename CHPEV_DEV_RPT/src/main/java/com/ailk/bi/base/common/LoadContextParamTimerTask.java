package com.ailk.bi.base.common;

//import java.util.HashMap;
import java.util.TimerTask;

import javax.servlet.ServletContext;

//import com.ailk.bi.base.struct.InfoResStruct;
//import com.ailk.bi.base.table.InfoPageItemTable;
//import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SecurityTool;
//import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

public class LoadContextParamTimerTask extends TimerTask {

	private static boolean isRunning = false;
	private ServletContext context = null;

	// 构造Context
	public LoadContextParamTimerTask(ServletContext context) {
		this.context = context;
	}

	public void run() {
		if (!isRunning) {
			isRunning = true;

			// 执行任务
			if (hasSetParamTrue()) {
				//
				// 统一资源表信息资源ID和描述初始化MAP*
				SecurityTool.setAllResConstsKeyValue(context);
				// 系统所有资源信息*

				// BI系统控件信息

				// 区域

				// 部门

				// 品牌*

				// 子品牌
				// HashMap subbrandmap = getConstMap("SUB_BRAND_KND");
				// session.setAttribute(WebConstKeys.ATTR_C_SUBBRAND_ID_VS_NAME,
				// subbrandmap);
				// 产品*

				//
				updateParamValue();
			}

			//
			isRunning = false;
		}

	}

	/**
	 * 判断刷新参数
	 * 
	 * @return
	 */
	private static boolean hasSetParamTrue() {
		boolean flag = false;
		String sql = "select task_status from ui_sys_timer_config where task_id = 'CTX' and task_type='1'";
		String[][] arr = null;
		try {
			arr = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		if (arr != null && arr.length > 0) {

			if ("0".equals(arr[0][0])) {
				flag = false;
			} else {
				flag = true;
			}

		}

		return flag;
	}

	/**
	 * 判断刷新参数
	 * 
	 * @return
	 */
	private static void updateParamValue() {

		String sql = "update  ui_sys_timer_config  set task_status = '0' where task_id = 'CTX' and task_type='1'";

		try {
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}

	}

}
