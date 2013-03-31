package com.ailk.bi.favor.dao.impl;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.favor.dao.IFavorDao;
import com.ailk.bi.favor.entity.UserFavor;

public class FavorDaoImpl implements IFavorDao {

	public void delete(UserFavor favor) {

		String sql = "delete from UI_INFO_USER_FAVOR where FAVOR_ID="
				+ favor.getFavorId();
		excuteSql(sql);
	}

	public void excuteSql(String sql) {

		try {
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}
	}

	public void insert(UserFavor favor) {

		StringBuffer sb = new StringBuffer();
		sb.append("insert into UI_INFO_USER_FAVOR(FAVOR_ID,PARENT_ID,USER_ID,FAVOR_NAME,FAVOR_DESC,"
				+ "SORT_NUM,STATUS,ADD_DATE,LAST_UPD,URL,RESOURCE_TYPE,RESOURCE_ID,TARGET,FAVOR_ATTR)");
		sb.append(" values(" + favor.getFavorId() + "," + favor.getParentId()
				+ ",'" + favor.getUserId() + "','");
		sb.append(favor.getFavorName() + "','" + favor.getFavorDesc() + "',"
				+ favor.getStatus() + ",to_date('" + favor.getAddDate()
				+ "','yyyy-mm-dd hh24:mi:ss')");
		sb.append("to_date('" + favor.getAddDate()
				+ "','yyyy-mm-dd hh24:mi:ss'),'" + favor.getUrl() + "','"
				+ favor.getResourceType());
		sb.append("','" + favor.getResourceId() + "','" + favor.getTarget()
				+ "," + favor.getFavorAttr() + ")");
		System.out.println(sb.toString());
		excuteSql(sb.toString());
	}

	public void update(UserFavor favor) {

		delete(favor);
		insert(favor);
	}

}
