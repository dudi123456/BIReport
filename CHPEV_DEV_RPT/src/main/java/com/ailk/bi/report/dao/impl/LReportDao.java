package com.ailk.bi.report.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.ailk.bi.base.struct.InfoResStruct;
import com.ailk.bi.base.util.CObjKnd;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.dao.ILReportDao;
import com.ailk.bi.report.domain.RptChartTable;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.system.facade.impl.CommonFacade;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class LReportDao implements ILReportDao {

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.report.dao.ILReportDao#getSelfReportID()
	 */
	public String getSelfReportID() throws AppException {
		String res_id = "";
		// 获取编码
		String strSql = SQLGenator.genSQL("Q3400");
		String[][] svces = WebDBUtil.execQryArray(strSql, "");
		if (svces != null && svces.length > 0)
			res_id = svces[0][0];
		return res_id;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.report.dao.ILReportDao#getReport(java.lang.String)
	 */
	public Object getReport(String rpt_id) throws AppException {
		if (rpt_id == null || "".equals(rpt_id))
			throw new AppException();
		RptResourceTable rptTable = null;
		String whereStr = "AND B.RPT_ID='" + rpt_id + "'";
		List reports = getReports(whereStr);
		if (reports != null && reports.size() > 0) {
			rptTable = (RptResourceTable) reports.toArray()[0];
		}
		return rptTable;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.report.dao.ILReportDao#getReports(java.lang.String)
	 */
	public List getReports(String whereStr) throws AppException {
		List reports = null;
		String strSql = SQLGenator.genSQL("Q3000", whereStr);
		System.out.println("report list=" + strSql);
		String[][] svces = WebDBUtil.execQryArray(strSql, "");

		String groupSql = SQLGenator.genSQL("Q3001");
		String[][] arrGroup = WebDBUtil.execQryArray(groupSql, "");
		if (null != svces) {
			reports = new ArrayList();
			for (int i = 0; i < svces.length; i++) {
				RptResourceTable report = RptResourceTable.genReportFromArray(svces[i], arrGroup);
				reports.add(report);
			}
		}
		return reports;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.report.dao.ILReportDao#getRoleReports(java.lang.String,
	 * java.lang.String)
	 */
	public List getRoleReports(String whereStr, String role_id) throws AppException {
		List reports = null;
		String strSql = SQLGenator.genSQL("Q3000", whereStr);
		strSql = "SELECT * FROM (" + strSql
				+ ") A LEFT JOIN UI_RPT_INFO_DISPENSE B ON A.RES_ID = B.RPT_ID";
		strSql += " AND B.R_ROLE_ID = '" + role_id + "'";
		// System.out.println("report list=" + strSql);
		String[][] svces = WebDBUtil.execQryArray(strSql, "");
		if (null != svces) {
			reports = new ArrayList();
			for (int i = 0; i < svces.length; i++) {
				RptResourceTable report = RptResourceTable.genReportFromArray(svces[i], null);
				reports.add(report);
			}
		}
		return reports;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.report.dao.ILReportDao#updateReport(java.lang.Object)
	 */
	public void insertReport(Object rptTable) throws AppException {
		RptResourceTable report = (RptResourceTable) rptTable;
		List svc = new ArrayList();
		svc.add(report.rpt_id);
		svc.add(report.local_res_code);
		svc.add(report.name);
		svc.add(report.cycle);
		svc.add(report.start_date);
		svc.add(report.title_note);
		svc.add(report.rpt_type);
		svc.add(report.pagecount);
		svc.add(report.ishead);
		svc.add(report.isleft);
		svc.add(report.dispenseflag);
		svc.add(report.metaflag);
		svc.add(report.privateflag);
		svc.add(report.d_user_id);
		svc.add(report.data_table);
		// 转换单引号
		String data_where = report.data_where;
		data_where = StringB.replace(data_where, "'", "'||chr(39)||'");
		svc.add(data_where);
		svc.add(report.data_date);
		svc.add(report.needdept);
		svc.add(report.needperson);
		svc.add(report.needreason);
		svc.add(report.inputnote);
		svc.add(report.filldept);
		svc.add(report.rp_status);
		svc.add(report.rp_engine);
		String[] paramReport = null;
		if (svc != null && svc.size() >= 0) {
			paramReport = (String[]) svc.toArray(new String[svc.size()]);
		}

		svc = new ArrayList();
		svc.add(report.rpt_id);
		svc.add(report.parent_id);
		svc.add(CObjKnd.REPORT_CENTER);
		svc.add(report.name);
		svc.add(report.cycle);
		svc.add("../report/ReportView.rptdo?rpt_id=" + report.rpt_id);
		String[] paramResource = null;
		if (svc != null && svc.size() >= 0) {
			paramResource = (String[]) svc.toArray(new String[svc.size()]);
		}

		String[] strSql = new String[2];
		strSql[0] = SQLGenator.genSQL("I3450", paramResource);
		System.out.println("Sql I3450==>" + strSql[0]);
		strSql[1] = SQLGenator.genSQL("I3452", paramReport);
		System.out.println("Sql I3452==>" + strSql[1]);
		WebDBUtil.execTransUpdate(strSql);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.report.dao.ILReportDao#updateReport(java.lang.Object)
	 */
	public void updateReport(Object rptTable) throws AppException {
		RptResourceTable report = (RptResourceTable) rptTable;
		List svc = new ArrayList();
		svc.add(report.local_res_code);
		svc.add(report.name);
		svc.add(report.cycle);
		svc.add(report.start_date);
		svc.add(report.title_note);
		svc.add(report.rpt_type);
		svc.add(report.pagecount);
		svc.add(report.ishead);
		svc.add(report.isleft);
		svc.add(report.startcol);
		svc.add(report.dispenseflag);
		svc.add(report.metaflag);
		svc.add(report.privateflag);
		svc.add(report.data_table);
		// 转换单引号
		String data_where = report.data_where;
		data_where = StringB.replace(data_where, "'", "'||chr(39)||'");
		svc.add(data_where);
		svc.add(report.data_date);
		svc.add(report.needdept);
		svc.add(report.needperson);
		svc.add(report.needreason);
		svc.add(report.inputnote);
		svc.add(report.status);
		svc.add(report.filldept);
		svc.add(report.rp_status);
		svc.add(report.rp_engine);
		svc.add(report.rpt_id);

		String[] paramReport = null;
		if (svc != null && svc.size() >= 0) {
			paramReport = (String[]) svc.toArray(new String[svc.size()]);
		}

		svc = new ArrayList();
		svc.add(report.parent_id);
		svc.add(report.name);
		svc.add(report.cycle);
		svc.add(report.status);
		svc.add(report.rpt_id);
		String[] paramResource = null;
		if (svc != null && svc.size() >= 0) {
			paramResource = (String[]) svc.toArray(new String[svc.size()]);
		}

		String[] strSql = new String[2];
		strSql[0] = SQLGenator.genSQL("C3451", paramResource);
		System.out.println("Sql C3451==>" + strSql[0]);
		strSql[1] = SQLGenator.genSQL("C3453", paramReport);
		System.out.println("Sql C3453==>" + strSql[1]);
		WebDBUtil.execTransUpdate(strSql);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.report.dao.ILReportDao#delReport(java.lang.String)
	 */
	public void delReport(String rpt_id) throws AppException {
		String[] strSql = new String[7];
		strSql[0] = SQLGenator.genSQL("D3454", rpt_id);
		strSql[1] = SQLGenator.genSQL("D3455", rpt_id);
		strSql[2] = SQLGenator.genSQL("D3463", rpt_id);
		strSql[3] = SQLGenator.genSQL("D3468", rpt_id);
		strSql[4] = SQLGenator.genSQL("D3469", rpt_id);
		strSql[5] = SQLGenator.genSQL("D3472", rpt_id);
		strSql[6] = SQLGenator.genSQL("D3473", rpt_id);
		WebDBUtil.execTransUpdate(strSql);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.dao.ILReportDao#getConditionReports(java.lang.String)
	 */
	public List getConditionReports(String con_id, String whereSql) throws AppException {
		List reports = null;
		String strSql = SQLGenator.genSQL("MSTR0016", con_id, whereSql);
		String[][] svces = WebDBUtil.execQryArray(strSql, "");

		String groupSql = SQLGenator.genSQL("Q3001");
		String[][] arrGroup = WebDBUtil.execQryArray(groupSql, "");
		if (null != svces) {
			reports = new ArrayList();
			for (int i = 0; i < svces.length; i++) {
				RptResourceTable report = RptResourceTable.genReportFromArray(svces[i], arrGroup);
				reports.add(report);
			}
		}
		return reports;
	}

	public String[][] getViewTreeList(HttpSession session, String resId, String resType,
			String rptId, String rptName, String tabType) throws AppException {

		String[] str = new String[3];

		if ("OLAP".equals(resType.toUpperCase())) {
			str[0] = " A3.menuitemtitle||'_'||A2.menuitemtitle||'_'||A1.menuitemtitle ";
			str[1] = ",sys_menu_item A3 ";
			str[2] = " AND A3.menuitemid = A2.PARENTID ";
		} else if ("RPT".equals(resType.toUpperCase())) {
			str[0] = " A2.menuitemtitle||'_'||A1.menuitemtitle ";
			str[1] = " ";
			str[2] = " ";
		}

		if (rptId != null && !"".equals(rptId)) {
			str[2] += " and A.url like '%" + rptId + "%' ";
		}
		if (rptName != null && !"".equals(rptName)) {
			str[2] += " and A.menuitemtitle like '%" + rptName + "%' ";
		}

		String userId = CommonFacade.getLoginId(session);
		String group_id = CommTool.getLoginGroup(session);

		System.out.println("group_id================" + group_id);
		String where1 = "";
		if (tabType != null && !"".equals(tabType)) {
			where1 = " INTERSECT              select distinct A.*              from sys_menu_item  A              start with A.resid in (select res_id from ui_pub_info_resource where hot_type='"
					+ tabType + "')              connect by A.menuitemid = prior A.parentid  ";
		} else {
			where1 = "";
		}
		String strSql = SQLGenator.genSQL("viewList", str[0], resId, where1, str[1], str[2]);
		// 资源权限
		// 不是系统管理员就需要过滤
		if (group_id != null && !"".equals(group_id) && !"1".equals(group_id)) {
			strSql += " and to_char(A.menuitemid) in(select resourceid  from sys_menuitem_right  a , user_role_map b where a.operatorid = b.role_id and  b.userid = '"
					+ userId
					+ "' union select distinct to_char(d.menuitemid) as resourceid from sys_menuitem_right  a , user_group_map b  ,  group_role_map c , sys_menu_item d where b.userid = '"
					+ userId
					+ "' and b.group_id = c.group_id and c.role_id = a.operatorid and a.resourceid = d.menuitemid"
					+ "  union  select distinct menuitemid||'' as resourceid from sys_menu_item where accesstoken = 0 )";

		}
		System.out.println("strSql================" + strSql);
		//

		String[][] rs = WebDBUtil.execQryArray(strSql, "");
		String[][] data = null;
		if (rs != null) {
			data = new String[rs.length][4];

			for (int i = 0; rs.length > 0 && i < rs.length; i++) {
				// InfoResStruct obj = new InfoResStruct();
				data[i][0] = rs[i][0];
				if ("OLAP".equals(resType.toUpperCase())) {
					data[i][1] = "主题分析";
					data[i][3] = "OlapMstr.rptdo?rpt_id=" + rs[i][0];
				} else {
					data[i][1] = "固定报表";
					data[i][3] = rs[i][3];
				}
				data[i][2] = rs[i][1] + "_" + rs[i][2];
			}

		}

		return data;
	}

	public String[][] getViewTreeList(String resId, String resType, String rptId, String rptName)
			throws AppException {

		return null;
	}

	/**
	 * 返回图形对象列表
	 *
	 * @param rpt_id
	 * @return
	 * @throws AppException
	 */
	public List getReportCharts(String rpt_id) throws Exception {
		List parameter = new ArrayList();
		parameter.add(rpt_id);
		String strSql = SQLGenator.getOrignalSQL("Q3175");
		// System.out.println("reportcharts list=" + strSql);

		return WebDBUtil.find(RptChartTable.class, strSql, parameter);
	}

}
