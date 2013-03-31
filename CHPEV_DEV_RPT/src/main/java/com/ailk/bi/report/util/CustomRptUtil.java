package com.ailk.bi.report.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.ailk.bi.base.struct.SimpleDispense;
import com.ailk.bi.base.table.DimensionTable;
import com.ailk.bi.base.util.CObjKnd;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.domain.RptResourceTable;

import org.apache.log4j.Logger;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CustomRptUtil {
	private static Logger logger = Logger.getLogger(CustomRptUtil.class);

	/**
	 * 添加自定义报表基本信息
	 * 
	 * @param rptInfo
	 * @param tmp_rpt_id
	 * @param user_id
	 * @throws AppException
	 */
	public static void insertReport(RptResourceTable rptInfo,
			String tmp_rpt_id, String user_id) throws AppException {
		String[] paramStr = new String[15];
		int m = 0;
		rptInfo.rpt_id = "RPTZ" + tmp_rpt_id;
		paramStr[m++] = rptInfo.rpt_id;
		paramStr[m++] = rptInfo.parent_id;
		paramStr[m++] = CObjKnd.REPORT_CUSTOM;
		paramStr[m++] = rptInfo.rpt_id;
		paramStr[m++] = rptInfo.name;
		paramStr[m++] = rptInfo.cycle;
		paramStr[m++] = "../report/ReportView.rptdo?rpt_id=" + rptInfo.rpt_id;
		paramStr[m++] = rptInfo.makedept;
		paramStr[m++] = rptInfo.makeperson;
		paramStr[m++] = rptInfo.needdept;
		paramStr[m++] = rptInfo.needperson;
		paramStr[m++] = rptInfo.needreason;
		paramStr[m++] = rptInfo.inputnote;
		paramStr[m++] = rptInfo.isleft;
		paramStr[m++] = user_id;

		String strSql = SQLGenator.genSQL("I3450", paramStr);
		// logger.debug("Sql I3450==>" + strSql);
		int icount = WebDBUtil.execUpdate(strSql);
		if (icount <= 0) {
			throw new AppException("添加报表信息失败!");
		}
	}

	/**
	 * 修改自定义报表基本信息
	 * 
	 * @param rptInfo
	 * @throws AppException
	 */
	public static void updateReport(RptResourceTable rptInfo)
			throws AppException {
		String[] paramStr = new String[9];
		int m = 0;
		paramStr[m++] = rptInfo.parent_id;
		paramStr[m++] = rptInfo.name;
		paramStr[m++] = rptInfo.cycle;
		paramStr[m++] = rptInfo.needdept;
		paramStr[m++] = rptInfo.needperson;
		paramStr[m++] = rptInfo.needreason;
		paramStr[m++] = rptInfo.inputnote;
		paramStr[m++] = rptInfo.isleft;
		paramStr[m++] = rptInfo.rpt_id;

		String strSql = SQLGenator.genSQL("C3451", paramStr);
		// logger.debug("Sql C3451==>" + strSql);
		int icount = WebDBUtil.execUpdate(strSql);
		if (icount <= 0) {
			throw new AppException("修改报表信息失败!");
		}
	}

	/**
	 * 删除报表(同时删除该报表相关信息)
	 * 
	 * @param rpt_id
	 * @param rpt_status
	 * @throws AppException
	 */
	public static void deleteReport(String rpt_id, String rpt_status)
			throws AppException {
		if ("Y".equals(rpt_status)) {
			String sql = SQLGenator.genSQL("C3476", "D");
			int icount = WebDBUtil.execUpdate(sql);
			if (icount <= 0) {
				throw new AppException("删除报表信息失败!");
			}
		} else {
			String[] strSql = new String[9];
			strSql[0] = SQLGenator.genSQL("D3460", rpt_id);
			strSql[1] = SQLGenator.genSQL("D3461", rpt_id);
			strSql[2] = SQLGenator.genSQL("D3462", rpt_id);
			strSql[3] = SQLGenator.genSQL("D3463", rpt_id);
			strSql[4] = SQLGenator.genSQL("D3464", rpt_id);
			strSql[5] = SQLGenator.genSQL("D3465", rpt_id);
			strSql[6] = SQLGenator.genSQL("D3466", rpt_id);
			strSql[7] = SQLGenator.genSQL("D3467", rpt_id);
			strSql[8] = SQLGenator.genSQL("D3468", rpt_id);

			int icount = WebDBUtil.execTransUpdate(strSql);
			if (icount <= 0) {
				throw new AppException("删除报表信息失败!");
			}
		}
	}

	/**
	 * 获取指标体系定义的纬度信息
	 * 
	 * @param dim_id
	 * @return
	 * @throws AppException
	 */
	public static DimensionTable[] getMeteDimTable(String dim_id)
			throws AppException {
		if (dim_id == null || "".equals(dim_id) || "0".equals(dim_id))
			return null;

		DimensionTable[] customRptDims = null;
		String strSql = SQLGenator.genSQL("Q3472", dim_id);
		// logger.debug("Sql Q3472==>" + strSql);
		Vector temp = WebDBUtil.execQryVector(strSql, "");
		if (temp != null && temp.size() > 0) {
			customRptDims = new DimensionTable[temp.size()];
			for (int i = 0; i < temp.size(); i++) {
				Vector tempv = (Vector) temp.get(i);
				customRptDims[i] = new DimensionTable();
				int m = 0;
				customRptDims[i].dim_id = (String) tempv.get(m++);
				customRptDims[i].dim_name = (String) tempv.get(m++);
				customRptDims[i].dim_desc = (String) tempv.get(m++);
				customRptDims[i].dim_type = (String) tempv.get(m++);
				customRptDims[i].dim_table = (String) tempv.get(m++);
				customRptDims[i].code_idfld = (String) tempv.get(m++);
				customRptDims[i].idfld_type = (String) tempv.get(m++);
				customRptDims[i].code_descfld = (String) tempv.get(m++);
				customRptDims[i].dim_unit = (String) tempv.get(m++);
				customRptDims[i].is_deptdim = (String) tempv.get(m++);
				customRptDims[i].to_userlvl = (String) tempv.get(m++);
			}
		}
		return customRptDims;
	}

	/**
	 * 更新报表状态
	 * 
	 * @param rpt_id
	 * @param status
	 * @throws AppException
	 */
	public static void updateReportStatus(String rpt_id, String status)
			throws AppException {
		// 输入值不合法返回null值
		if (rpt_id == null || "".equals(rpt_id) || "0".equals(rpt_id))
			throw new AppException("非法的操作，请检查！");
		// 输入值不合法返回null值
		if (status == null || "".equals(status))
			throw new AppException("非法的操作，请检查！");

		String strSql = SQLGenator.genSQL("C3476", status, rpt_id);
		// logger.debug("Sql C3476==>" + strSql);
		int icount = WebDBUtil.execUpdate(strSql);
		if ("Y".equals(status)) {
			strSql = SQLGenator.genSQL("I3478", rpt_id, "1", "2");
			WebDBUtil.execUpdate(strSql);
			strSql = SQLGenator.genSQL("I3478", rpt_id, "BI2020", "2");
			WebDBUtil.execUpdate(strSql);
		}
		if (icount <= 0) {
			throw new AppException("修改报表信息失败!");
		}
	}

	/**
	 * 根据角色获取自定义报表列表
	 * 
	 * @param rpt_type
	 * @param cycle
	 * @param rpt_name
	 * @param role_code
	 * @param user_id
	 * @return
	 */
	public static SimpleDispense[] getDispenseReportByRight(String rpt_type,
			String cycle, String rpt_name, String role_code, String user_id) {
		SimpleDispense[] rptInfo = null;
		String whereStr = " 1=1 ";
		if (null != rpt_type && !"".equals(rpt_type)) {
			whereStr += " AND A.PARENT_ID = '" + rpt_type + "'";
		}
		if (null != cycle && !"".equals(cycle)) {
			whereStr += " AND A.CYCLE = " + cycle;
		}
		if (null != rpt_name && !"".equals(rpt_name)) {
			whereStr += " AND A.NAME like '%" + rpt_name + "%'";
		}
		String strSql = "";
		try {
			strSql = SQLGenator.genSQL("Q3490", user_id, whereStr, role_code);
			logger.debug("Sql Q3490==>" + strSql);
			String[][] arr = WebDBUtil.execQryArray(strSql, "");
			if (arr != null && arr.length > 0) {
				rptInfo = new SimpleDispense[arr.length];
				for (int i = 0; i < arr.length; i++) {
					rptInfo[i] = new SimpleDispense();
					rptInfo[i].res_id = arr[i][0];
					rptInfo[i].res_name = arr[i][1];
					rptInfo[i].cycle = arr[i][2];
					if ("1".equals(rptInfo[i].cycle)) {
						rptInfo[i].cycle_desc = "年";
					} else if ("2".equals(rptInfo[i].cycle)) {
						rptInfo[i].cycle_desc = "半年";
					} else if ("3".equals(rptInfo[i].cycle)) {
						rptInfo[i].cycle_desc = "季报";
					} else if ("4".equals(rptInfo[i].cycle)) {
						rptInfo[i].cycle_desc = "月报";
					} else if ("5".equals(rptInfo[i].cycle)) {
						rptInfo[i].cycle_desc = "旬报";
					} else if ("6".equals(rptInfo[i].cycle)) {
						rptInfo[i].cycle_desc = "日报";
					}
					rptInfo[i].parent_id = arr[i][3];
					rptInfo[i].parent_name = arr[i][4];
					rptInfo[i].role_code = arr[i][5];
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return rptInfo;
	}

	/**
	 * <p>
	 * 报表范围--相关角色权限保存处理
	 * </p>
	 * 通过取得报表的ID以及需要更改的角色权限进行保存、修改操作
	 * 
	 * @param role_code
	 * @param reports
	 * @param rptList
	 * @return
	 */
	public static int saveRptRoleByRoleIdList(String role_code,
			SimpleDispense[] reports, String[] rptList) {
		int renum = -1;
		List delSql = new ArrayList();
		List addSql = new ArrayList();
		try {
			// tmp
			String strSql = "";
			// 删除
			for (int i = 0; reports != null && i < reports.length; i++) {
				strSql = SQLGenator.genSQL("D3491", role_code,
						reports[i].res_id);
				delSql.add(strSql);
			}
			// 添加
			for (int i = 0; rptList != null && i < rptList.length; i++) {
				strSql = SQLGenator.genSQL("I3492", rptList[i], role_code);
				addSql.add(strSql);
			}
		} catch (AppException ex) {
			ex.printStackTrace();
			return -1;
		}
		// 转化
		String[] sqlDel = (String[]) delSql.toArray(new String[delSql.size()]);
		String[] sqlAdd = (String[]) addSql.toArray(new String[addSql.size()]);
		// 执行
		try {
			if (sqlDel != null && sqlDel.length > 0)
				renum = WebDBUtil.execTransUpdate(sqlDel);
			if (sqlAdd != null && sqlAdd.length > 0)
				renum = WebDBUtil.execTransUpdate(sqlAdd);
		} catch (AppException e) {
			e.printStackTrace();
			renum = -1;
		}
		return renum;
	}

	public static int saveRptToMRCondition(String con_id, String[] rpt_id,
			String whereStr) {
		int renum = -1;
		List sqlList = new ArrayList();

		List addrpt = new ArrayList();
		List deleterpt = new ArrayList();

		try {
			HashMap mapa = new HashMap();
			HashMap mapb = new HashMap();
			// 查询当前条件报表关系
			String sql = SQLGenator.genSQL("MSTR0017", whereStr);
			// logger.debug("MSTR0017==========" + sql);
			String[][] value = WebDBUtil.execQryArray(sql, "");
			if (value == null || value.length <= 0) {
				for (int i = 0; rpt_id != null && i < rpt_id.length; i++) {
					addrpt.add(rpt_id[i]);
				}
			} else {
				for (int i = 0; value != null && i < value.length; i++) {
					mapa.put(value[i][0], value[i][0]);
				}
				for (int i = 0; rpt_id != null && i < rpt_id.length; i++) {
					mapb.put(con_id + "$" + rpt_id[i], con_id + "$" + rpt_id[i]);
				}
				logger.debug("mapa==========" + mapa.size());
				logger.debug("mapb==========" + mapb.size());
				// 增加
				for (int i = 0; rpt_id != null && i < rpt_id.length; i++) {
					if (mapa.get(con_id + "$" + rpt_id[i]) == null) {
						addrpt.add(rpt_id[i]);
					}
				}
				// 删除
				for (int i = 0; value != null && i < value.length; i++) {
					if (mapb.get(value[i][0]) == null) {
						deleterpt.add(value[i][0]);
					}
				}
			}
			logger.debug("deleterpt==========" + deleterpt.size());
			logger.debug("addrpt==========" + addrpt.size());
			// tmp
			String strSql = "";
			// 删除
			for (int i = 0; deleterpt != null && i < deleterpt.size(); i++) {
				String[] arr = deleterpt.get(i).toString().split("$");
				strSql = SQLGenator.genSQL("MSTR0014", con_id, arr[1]);
				logger.debug(strSql);
				sqlList.add(strSql);
			}
			// 添加
			for (int i = 0; addrpt != null && i < addrpt.size(); i++) {
				// String[] arr = addrpt.get(i).toString().split("$");
				strSql = SQLGenator.genSQL("MSTR0015", con_id, addrpt.get(i)
						.toString());
				logger.debug(strSql);
				sqlList.add(strSql);
			}
		} catch (AppException ex) {
			ex.printStackTrace();
			return -1;
		}
		// 转化
		String[] sqlArr = (String[]) sqlList
				.toArray(new String[sqlList.size()]);

		// 执行
		try {
			if (sqlArr != null && sqlArr.length > 0) {
				renum = WebDBUtil.execTransUpdate(sqlArr);
			}
		} catch (AppException e) {
			e.printStackTrace();
			renum = -1;
		}
		return renum;
	}

	/**
	 * 判断报表权限是否存在
	 * 
	 * @param role_id
	 * @param report_id
	 * @throws AppException
	 */
	public static boolean IsRoleRpt(String role_code, String report_str) {
		boolean flag = false;
		try {
			String strSql = SQLGenator.genSQL("Q3493", role_code, report_str);
			// logger.debug("Q3493===========" + strSql);
			String[][] arr = WebDBUtil.execQryArray(strSql, "");
			if (arr != null && arr.length > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;

	}
}
