package com.ailk.bi.bulletin.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.bulletin.dao.IBulletinDao;
import com.ailk.bi.bulletin.entity.MartInfoBulletin;
import com.ailk.bi.bulletin.util.BulletinRoleGrp;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.ReportQryStruct;

//import com.ailk.bi.system.common.CommonUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class BulletinDaoImpl implements IBulletinDao {
	private static Log logger = LogFactory.getLog(BulletinDaoImpl.class);

	public void deleteMartBulletinInfo(String id) {

		String[] sql = new String[2];
		sql[0] = "delete from UI_INFO_BULLETIN where id=" + id;
		sql[1] = "delete from ui_rule_bulletin_usergrp where bulletin_id=" + id;

		logger.debug(sql[0]);
		logger.debug(sql[1]);

		try {

			// logger.info("sql-" + sql);
			WebDBUtil.execTransUpdate(sql);

		} catch (AppException e) {

			e.printStackTrace();
		}
	}

	public void excuteSql(String sql) {

		try {
			logger.info("sql-" + sql);
			WebDBUtil.execUpdate(sql);

		} catch (AppException e) {

			e.printStackTrace();
		}
	}

	public List qryMartBulletinInfoList(ReportQryStruct qryStruct) {

		// CommonUtil.getBulletin();

		String sql = "select title,news_msg,to_char(valid_begin_date,'yyyy-mm-dd'),to_char(valid_end_date,'yyyy-mm-dd'),id,city_id,type_id,FIELD_BAK_01,system_id from UI_INFO_BULLETIN a WHERE"
				+ " 1=1";

		// String sql =
		// "select title,news_msg,valid_end_datec from  UI_INFO_BULLETIN a WHERE"
		// + " 1=1";
		if (qryStruct.city_id.length() > 0) {
			sql += " and city_id='" + qryStruct.city_id + "'";

		}
		if (qryStruct.dim1.length() > 0) {
			sql += " and valid_begin_date=to_date('" + qryStruct.dim1
					+ "','yyyy-mm-dd')";

		}
		if (qryStruct.dim2.length() > 0) {
			sql += " and title like '%" + qryStruct.dim2 + "%'";

		}

		if (qryStruct.dim3.length() > 0) {
			sql += " and type_id='" + qryStruct.dim3 + "'";

		}

		if (qryStruct.dim5.length() > 0) {
			sql += " and system_id in(0," + qryStruct.dim5 + ")";

		}

		String[][] value = null;
		sql += " order by a.id desc";
		List list = new ArrayList();

		try {
			logger.info("sql-" + sql);
			value = WebDBUtil.execQryArray(sql, "");

			if (value != null && value.length > 0) {
				for (int k = 0; k < value.length; k++) {
					MartInfoBulletin obj = new MartInfoBulletin();
					obj.setTitle(value[k][0]);
					obj.setNewsMsg(value[k][1]);
					obj.setValidBeginDate(value[k][2]);
					obj.setValidEndDate(value[k][3]);
					obj.setId(value[k][4]);
					obj.setCityId(value[k][5]);
					obj.setTypeId(value[k][6]);
					obj.setFieldBak01(StringB.NulltoBlank(value[k][7]));
					obj.setSystemId(StringB.NulltoBlank(value[k][8]));

					String sqlTmp = "select case when link_type='1' then 'U_' || link_id else 'G_' || link_id end from ui_rule_bulletin_usergrp "
							+ "where bulletin_id=" + obj.getId();
					String[][] arrTmp = WebDBUtil.execQryArray(sqlTmp, "");
					String strGrp = "";
					if (arrTmp != null && arrTmp.length > 0) {
						for (int m = 0; m < arrTmp.length; m++) {
							if (strGrp.length() == 0) {
								strGrp = arrTmp[m][0];
							} else {
								strGrp += "," + arrTmp[m][0];
							}
						}
					}
					obj.setGroupId(strGrp);
					list.add(obj);
				}

			}

		} catch (AppException e) {

			e.printStackTrace();
		}

		return list;
	}

	public String[][] qryMartBulletinInfoList(ReportQryStruct qryStruct,
			boolean isValid) {

		String sqlBul = "select distinct bulletin_id from UI_RULE_BULLETIN_USERGRP t where  "
				+ "(t.link_type='1' and link_id='"
				+ qryStruct.dim3
				+ "') or ( t.link_type='2' and link_id='"
				+ qryStruct.dim4
				+ "')";

		String sql = "select title,news_msg,to_char(valid_begin_date,'yyyy-mm-dd'),to_char(valid_end_date,'yyyy-mm-dd'),id,city_id,type_id,system_id  from  UI_INFO_BULLETIN a WHERE"

				+ " ( ID IN(" + sqlBul + ") OR FIELD_BAK_01='0')";

		sql += " and system_id in(0," + qryStruct.dim5 + ")";

		// String sql =
		// "select title,news_msg,valid_end_datec from  UI_INFO_BULLETIN a WHERE"
		// + " 1=1";
		if (isValid == true) {
			Calendar ca = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String strNow = formatter.format(ca.getTime());
			sql += " and valid_end_date>=to_date('" + strNow
					+ "','yyyy-mm-dd')";
		}
		if (qryStruct.city_id.length() > 0) {
			sql += " and city_id='" + qryStruct.city_id + "'";

		}
		if (qryStruct.dim1.length() > 0) {
			sql += " and valid_begin_date=to_date('" + qryStruct.dim1
					+ "','yyyy-mm-dd')";

		}
		if (qryStruct.dim2.length() > 0) {
			sql += " and title like '%" + qryStruct.dim2 + "%'";

		}

		String[][] value = null;
		sql += " order by a.id desc";

		try {
			logger.info("sql-" + sql);
			value = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {

			e.printStackTrace();
		}

		return value;
	}

	public String[][] qryObjectInfoList(String sql) {

		String[][] value = null;

		try {
			logger.info("sql-" + sql);
			value = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {

			e.printStackTrace();
		}

		return value;
	}

	public void insertMartBulletinInfo(MartInfoBulletin obj) {

		String sql = "";
		String mainID = "";
		if (!"".equals(obj.getId())) {
			mainID = obj.getId();
			sql = "update UI_INFO_BULLETIN set title='" + obj.getTitle()
					+ "',news_msg='" + obj.getNewsMsg() + "',city_id='"
					+ obj.getCityId() + "'," + "valid_begin_date=to_date('"
					+ obj.getValidBeginDate()
					+ "','yyyy-mm-dd'),valid_end_date=to_date('"
					+ obj.getValidEndDate() + "','yyyy-mm-dd')," + "type_id="
					+ obj.getTypeId() + ",type_name='" + obj.getTypeName()
					+ "',FIELD_BAK_01='" + obj.getFieldBak01() + "',system_id="
					+ obj.getSystemId() + " where id=" + obj.getId();
		} else {

			mainID = CommTool.dbGetMaxIDBySeqName("SEQ_BULLETIN");

			sql = "insert into UI_INFO_BULLETIN(id,title,news_msg,CITY_ID,valid_begin_date,valid_end_date,creator,ADD_DATE,type_id,type_name,FIELD_BAK_01,system_id) "
					+ "values("
					+ mainID
					+ ",'"
					+ obj.getTitle()
					+ "','"
					+ obj.getNewsMsg()
					+ "','"
					+ obj.getCityId()
					+ "',to_date('"
					+ obj.getValidBeginDate()
					+ "','yyyy-mm-dd'),to_date('"
					+ obj.getValidEndDate()
					+ "','yyyy-mm-dd'),'"
					+ obj.getCreator()
					+ "',to_date('"
					+ obj.getAddDt()
					+ "','yyyy-mm-dd'),'"
					+ obj.getTypeId()
					+ "','"
					+ obj.getTypeName()
					+ "','"
					+ obj.getFieldBak01()
					+ "',"
					+ obj.getSystemId() + ")";
		}
		try {
			// String sql = SQLGenator.genSQL("SYSADMIN_003_I", paramReport);
			logger.info("sql-" + sql);
			if (!"".equals(sql)) {
				WebDBUtil.execUpdate(sql);
				WebDBUtil
						.execUpdate("delete from ui_rule_bulletin_usergrp where bulletin_id="
								+ mainID);

				if (obj.getFieldBak01().equals("1")) {// 有权限控制
					List list = BulletinRoleGrp.transferSelectIdToType(obj
							.getGroupId());
					String[] sqlDim = new String[list.size()];
					for (int k = 0; k < list.size(); k++) {
						MartInfoBulletin tmp = (MartInfoBulletin) list.get(k);
						sqlDim[k] = "insert into ui_rule_bulletin_usergrp values("
								+ mainID
								+ ",'"
								+ tmp.getId()
								+ "','"
								+ tmp.getTypeId() + "')";
					}
					WebDBUtil.execTransUpdate(sqlDim);
				}
			}

		} catch (AppException e) {

			e.printStackTrace();
		}
	}

}
