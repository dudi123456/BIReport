package com.ailk.bi.adhoc.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.common.sysconfig.GetSystemConfig;

public class AdhocSaveTaskInJob {
	private AdhocBuildXlsBean bean;

	public AdhocSaveTaskInJob(AdhocBuildXlsBean bean) {
		this.bean = bean;

	}

	public void doSaveTask() {

		String mainID = bean.getMainID();

		String cntSql = bean.getCntSql();
		double dblLimit = AdhocConstant.CONST_EXCEL_RECORDS_LIMITS;
		double dbl47Limit = AdhocConstant.CONST_EXCEL_RUN_47_LIMITS;
		boolean isLimit = true;
		try {

			String sqlLimit = "select RECORD_LIMIT from ui_adhoc_info_def where adhoc_id in"
					+ "(select adhoc_id from ui_adhoc_buildxls_task where id="
					+ mainID + ")";

			String[][] resLimit = WebDBUtil.execQryArray(sqlLimit, "");
			if (resLimit != null && resLimit.length > 0) {
				dblLimit = Double.parseDouble(resLimit[0][0]);
			} else {
				isLimit = false;
			}

			String[][] recordCnt = WebDBUtil.execQryArray(cntSql, "");
			double dblCnt = Double.parseDouble(recordCnt[0][0]);
			String strLimit = GetSystemConfig.getBIBMConfig().getExtParam(
					"CONST_EXCEL_RUN_47_LIMITS");

			if (strLimit != null && strLimit.length() > 0) {
				dbl47Limit = Double.parseDouble(strLimit);
			}

			String serverIp = "172.19.31.47";

			String serverConfig47 = GetSystemConfig.getBIBMConfig()
					.getExtParam("CONST_47_MACHINE_IP");
			if (serverConfig47 != null && serverConfig47.length() > 0) {
				serverIp = serverConfig47;
			}

			String runMachine = serverIp
					.substring(serverIp.lastIndexOf(".") + 1);

			String flag = "0";
			String msg = "";
			boolean sendFlag = true;

			if (dblCnt > dbl47Limit) {

				serverIp = "172.19.31.45";
				String serverConfig45 = GetSystemConfig.getBIBMConfig()
						.getExtParam("CONST_45_MACHINE_IP");

				if (serverConfig45 != null && serverConfig45.length() > 0) {
					serverIp = serverConfig45;
				}
				runMachine = serverIp.substring(serverIp.lastIndexOf(".") + 1);

			}

			if (dblCnt >= dblLimit) {
				if (isLimit) {
					if (bean.getRcnt().equals("-1")) {
						flag = "3";
						sendFlag = false;
						msg = "超过" + dblLimit + "条,禁止导出";
					}
				}

			}

			String strUpdate = "update ui_adhoc_buildxls_task set flag=" + flag
					+ ",bak_fld_01='" + msg + "',RECORDCNT=" + recordCnt[0][0]
					+ ",RUNMACHINE=" + runMachine + " where id=" + mainID;

			WebDBUtil.execUpdate(strUpdate);

			if (sendFlag) {
				sendInfoToSocket(mainID, serverIp);
			}

		} catch (AppException e) {

			e.printStackTrace();
		}

		// sendInfoToSocket(mainID);

	}

	private void sendInfoToSocket(String id, String serverIp) {
		try {
			Socket socket = new Socket(serverIp, 10984);
			System.out.println("send request to server" + serverIp);

			PrintWriter out = null;
			try {

				out = new PrintWriter(socket.getOutputStream());
				System.out.println("send task id:" + id);
				out.println(id);
				out.flush();

			} finally {
				System.out.println("Finally");
				socket.close();
				out.close();

			}

		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}
