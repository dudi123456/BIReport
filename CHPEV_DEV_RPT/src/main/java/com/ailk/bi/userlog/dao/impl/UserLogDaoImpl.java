package com.ailk.bi.userlog.dao.impl;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.userlog.dao.IUserLogDao;
import com.ailk.bi.userlog.entity.UiInfoUserLog;

public class UserLogDaoImpl implements IUserLogDao {

	public void excuteSql(String sql) {

		try {
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}
	}

	public String[][] qryObjectInfoList(String sql) {

		String[][] value = null;

		try {

			value = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {

			e.printStackTrace();
		}

		return value;
	}

	public void insert(UiInfoUserLog obj) {

		String sql = "insert into UI_SYS_LOG(service_sn,LOG_SEQ,LOG_TYPE,LOG_OPER_NO,LOG_OPER_TIME,LOG_IP,LOG_OPER_NAME,OBJ_ID,OBJ_NAME) "
				+ "values(SEQ_USER_LOG.NEXTVAL,'"
				+ obj.getSessionId()
				+ "','"
				+ obj.getOperation()
				+ "','"
				+ obj.getUserId()
				+ "',sysdate,'"
				+ obj.getClientAddress()
				+ "','"
				+ obj.getUserName()
				+ "','"
				+ obj.getResourceId() + "','" + obj.getMsg() + "')";

		try {
			// System.out.println(sql);
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}
	}

	public void update(UiInfoUserLog obj) {

		String sql = "update UI_SYS_LOG set leave_time=sysdate where LOG_SEQ='"
				+ obj.getSessionId() + "' and log_type='1' and LOG_OPER_NO='"
				+ obj.getUserId() + "'";

		try {

			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}
	}

}
