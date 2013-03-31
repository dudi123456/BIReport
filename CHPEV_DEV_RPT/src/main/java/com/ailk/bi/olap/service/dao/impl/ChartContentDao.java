package com.ailk.bi.olap.service.dao.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.table.RptOlapDimTable;
import com.ailk.bi.base.table.RptOlapMsuTable;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.olap.domain.RptOlapChartAttrStruct;
import com.ailk.bi.olap.domain.RptOlapDateStruct;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;
import com.ailk.bi.olap.service.dao.IChartContentDao;
import com.ailk.bi.olap.util.RptOlapConsts;
import com.ailk.bi.olap.util.RptOlapDataRight;
import com.ailk.bi.olap.util.RptOlapDimUtil;
import com.ailk.bi.olap.util.RptOlapStringUtil;

@SuppressWarnings({ "rawtypes" })
public class ChartContentDao implements IChartContentDao {

	public String[][] getChartContent(List structs,
			PubInfoResourceTable report, RptOlapFuncStruct olapFun,
			UserCtlRegionStruct userCtl, String svckndRight,
			RptOlapDateStruct ds) throws ReportOlapException {
		if (null == structs || 0 >= structs.size() || null == report
				|| null == olapFun || null == ds)
			throw new ReportOlapException("获取图形分析数据时输入的参数为空");
		String[][] svces = null;
		try {
			long startTime = System.currentTimeMillis();
			String select = genChartSelect(structs, report, olapFun, userCtl,
					svckndRight, ds);
			System.out.println("组装图形SQL用时："
					+ (System.currentTimeMillis() - startTime) + "ms");
			System.out.println("oalp chart=" + select);
			startTime = System.currentTimeMillis();
			svces = WebDBUtil.execQryArray(select, "");
			System.out.println("图形SQL查询用时："
					+ (System.currentTimeMillis() - startTime) + "ms");
		} catch (AppException e) {
			throw new ReportOlapException("获取图形分析数据时发生数据库错误", e);
		}
		return svces;
	}

	private String genChartSelect(List structs, PubInfoResourceTable report,
			RptOlapFuncStruct olapFun, UserCtlRegionStruct userCtl,
			String svckndRight, RptOlapDateStruct ds)
			throws ReportOlapException {
		if (null == structs || 0 >= structs.size() || null == report
				|| null == olapFun || null == ds)
			throw new ReportOlapException("获取图形分析数据时输入的参数为空");
		String selectSQL = null;

		StringBuffer select = new StringBuffer();
		StringBuffer subSelect = new StringBuffer();
		StringBuffer from = new StringBuffer(" FROM ");
		StringBuffer where = new StringBuffer(" WHERE 1=1 ");
		StringBuffer groupby = new StringBuffer();
		StringBuffer mainGroupby = new StringBuffer();
		StringBuffer joinSQL = new StringBuffer();
		StringBuffer orderby = new StringBuffer();

		String dataVirTabName = RptOlapConsts.THIS_VIR_TAB_NAME;
		from.append(report.data_table).append(" ").append(dataVirTabName)
				.append(",");
		String dataWhere = report.data_where;
		if (null != dataWhere && !"".equals(dataWhere)) {
			dataWhere = dataWhere.toUpperCase();
			dataWhere = dataWhere.replaceAll("WHERE 1=1", "");
			dataWhere = dataWhere.replaceAll("WHERE", "");
			dataWhere = RptOlapStringUtil.replaceVirTabName(dataWhere,
					dataVirTabName);
			if (null != dataWhere && !"".equals(dataWhere))
				where.append(" AND ").append(dataWhere);
		}

		RptOlapDimTable rptEvalDept = null;
		RptOlapDimTable rptSvcKnd = null;
		// 仅用到此功能
		String func = olapFun.getCurFunc();
		String timePrefix = null;
		RptOlapDimTable rptTime = null;
		Iterator iter = structs.iterator();
		while (iter.hasNext()) {
			RptOlapChartAttrStruct chartStruct = (RptOlapChartAttrStruct) iter
					.next();
			if (chartStruct.isDim()) {
				RptOlapDimTable tmpDim = (RptOlapDimTable) chartStruct
						.getRptStruct();
				if (RptOlapConsts.YES.equalsIgnoreCase(tmpDim.is_evaldept)) {
					rptEvalDept = tmpDim;
				}
				if (RptOlapConsts.YES.equalsIgnoreCase(tmpDim.is_svcknd))
					rptSvcKnd = tmpDim;
			}
			if (chartStruct.isDim() && chartStruct.isTime()) {
				rptTime = (RptOlapDimTable) chartStruct.getRptStruct();
				timePrefix = "D" + chartStruct.getIndex() + "_0";
			}
			// 需要按当前功能找到查询部分,这个应该仍给另外一个类
			// 这个类应该按当前功能设置好，哪些维度显示，显示的都进行分组，
			if (chartStruct.isDisplay()) {
				if (chartStruct.isDim()) {
					if (!chartStruct.isTime()) {
						// 显示维
						Map parts = RptOlapDimUtil.genChartSelectParts(
								chartStruct, dataVirTabName);
						select.append(parts.get("select"));
						subSelect.append(parts.get("subselect"));
						joinSQL.append(parts.get("join"));
						from.append(parts.get("from"));
						where.append(parts.get("where"));
						groupby.append(parts.get("groupby"));
						orderby.append(parts.get("orderby"));
						mainGroupby.append(parts.get("select"));
					}
				} else {
					// 指标，只要简单选择出来即可
					RptOlapMsuTable rptMsu = (RptOlapMsuTable) chartStruct
							.getRptStruct();
					String realFld = rptMsu.msuInfo.real_fld;
					realFld = RptOlapStringUtil.replaceVirTabName(realFld,
							dataVirTabName);
					String msuFld = rptMsu.msuInfo.msu_fld;
					msuFld = RptOlapStringUtil.replaceVirTabName(msuFld,
							dataVirTabName);
					String sumMsuFld = "SUM(" + msuFld + ")";
					sumMsuFld = RptOlapConsts.NVL_PROC.replaceAll("::NULL::",
							sumMsuFld);
					select.append(sumMsuFld).append(",");
					String regular = "\\w*\\.?(\\w*)";
					String replace = "$1";
					msuFld = RptOlapStringUtil.regReplace(msuFld, regular,
							replace, true);
					realFld = RptOlapConsts.NVL_PROC.replaceAll("::NULL::",
							realFld);
					subSelect.append(realFld).append(" AS ").append(msuFld)
							.append(",");

				}
			}
		}
		// 趋势图加上时间分组
		if (RptOlapConsts.OLAP_FUN_LINE.equals(func)) {
			String tmpStr = joinSQL.toString();
			joinSQL.delete(0, joinSQL.length());
			joinSQL.append(" LEFT OUTER JOIN (SELECT ");
			joinSQL.append(timePrefix).append(".")
					.append(rptTime.dimInfo.code_idfld).append(",");
			joinSQL.append(timePrefix).append(".")
					.append(rptTime.dimInfo.code_descfld).append(" ");
			joinSQL.append(" FROM ");
			joinSQL.append(rptTime.dimInfo.dim_table).append(" ")
					.append(timePrefix).append(" ");
			joinSQL.append(" WHERE ").append(timePrefix).append(".")
					.append(rptTime.dimInfo.code_idfld).append(">=")
					.append(ds.getStart());
			joinSQL.append(" AND ").append(timePrefix).append(".")
					.append(rptTime.dimInfo.code_idfld).append("<=")
					.append(ds.getEnd());
			joinSQL.append(") ").append(timePrefix).append(" ");
			joinSQL.append(" ON ").append(dataVirTabName).append(".")
					.append(rptTime.dimInfo.code_idfld).append("=")
					.append(timePrefix).append(".")
					.append(rptTime.dimInfo.code_idfld);
			joinSQL.append(tmpStr);

			// 时间
			tmpStr = select.toString();
			select.delete(0, select.length());
			select.append(dataVirTabName).append(".")
					.append(rptTime.dimInfo.code_idfld).append(",");
			select.append(timePrefix).append(".")
					.append(rptTime.dimInfo.code_descfld).append(",");
			select.append(tmpStr);

			String parts = mainGroupby.toString();
			mainGroupby.delete(0, mainGroupby.length());
			mainGroupby.append(dataVirTabName).append(".")
					.append(rptTime.dimInfo.code_idfld).append(",");
			mainGroupby.append(timePrefix).append(".")
					.append(rptTime.dimInfo.code_descfld).append(",");
			mainGroupby.append(parts);

			// 字查询
			tmpStr = subSelect.toString();
			subSelect.delete(0, subSelect.length());
			subSelect.append(dataVirTabName).append(".")
					.append(rptTime.dimInfo.code_idfld).append(",");
			subSelect.append(tmpStr);

			tmpStr = groupby.toString();
			groupby.delete(0, groupby.length());
			groupby.append(dataVirTabName).append(".")
					.append(rptTime.dimInfo.code_idfld).append(",");
			groupby.append(tmpStr);

			tmpStr = orderby.toString();
			orderby.delete(0, orderby.length());
			orderby.append(dataVirTabName).append(".")
					.append(rptTime.dimInfo.code_idfld).append(",");
			orderby.append(tmpStr);
		}
		// 加上时间限制
		where.append(" AND ").append(dataVirTabName).append(".")
				.append(rptTime.dimInfo.code_idfld).append(">=");
		where.append(ds.getStart());
		where.append(" AND ").append(dataVirTabName).append(".")
				.append(rptTime.dimInfo.code_idfld).append("<=");
		where.append(ds.getEnd());
		// 至此组装完毕
		String rightJoinSQL = RptOlapDataRight.genEvalDeptJoinSQL(rptEvalDept,
				userCtl, dataVirTabName);
		boolean addRightFld = (null == rightJoinSQL || "".equals(rightJoinSQL) ? false
				: true);
		String rightSvcKndSQL = RptOlapDataRight.genSvcKndJoinSQL(rptSvcKnd,
				svckndRight, RptOlapConsts.THIS_VIR_TAB_NAME);
		selectSQL = "";
		// 最外层
		String tmpStr = select.toString();
		tmpStr = RptOlapStringUtil.removeLastSubStr(tmpStr, ",");
		selectSQL += " SELECT " + tmpStr;
		//
		selectSQL += " FROM (SELECT ";
		String coldFld = null;
		if (null != rptEvalDept && null != userCtl && addRightFld) {
			// 加上权限过虑
			coldFld = RptOlapDimUtil.getDimDataFld(rptEvalDept);
			coldFld = RptOlapStringUtil.replaceVirTabName(coldFld,
					RptOlapConsts.THIS_VIR_TAB_NAME);
			if (subSelect.indexOf(coldFld) < 0)
				subSelect.append(coldFld).append(",");
		}
		tmpStr = subSelect.toString();
		tmpStr = RptOlapStringUtil.removeLastSubStr(tmpStr, ",");
		selectSQL += tmpStr;
		tmpStr = from.toString();
		tmpStr = RptOlapStringUtil.removeLastSubStr(tmpStr, ",");
		selectSQL += tmpStr;
		if (null != rightSvcKndSQL && !"".equals(rightSvcKndSQL))
			selectSQL += rightSvcKndSQL;
		tmpStr = where.toString();
		selectSQL += tmpStr;
		if (null != rptEvalDept && null != userCtl && addRightFld) {
			// 加上权限过虑
			coldFld = RptOlapDimUtil.getDimDataFld(rptEvalDept);
			coldFld = RptOlapStringUtil.replaceVirTabName(coldFld,
					RptOlapConsts.THIS_VIR_TAB_NAME);
			if (groupby.indexOf(coldFld) < 0)
				groupby.append(coldFld).append(",");
		}
		tmpStr = groupby.toString();
		tmpStr = RptOlapStringUtil.removeLastSubStr(tmpStr, ",");
		if (tmpStr.length() > 0)
			selectSQL += " GROUP BY " + tmpStr;
		selectSQL += ") " + dataVirTabName;
		// 加上数据权限
		joinSQL.append(rightJoinSQL);

		selectSQL += joinSQL.toString();

		// group by
		tmpStr = mainGroupby.toString();
		tmpStr = RptOlapStringUtil.removeLastSubStr(tmpStr, ",");
		if (tmpStr.length() > 0)
			selectSQL += " GROUP BY " + tmpStr;

		tmpStr = mainGroupby.toString();
		tmpStr = RptOlapStringUtil.removeLastSubStr(tmpStr, ",");
		if (tmpStr.length() > 0)
			selectSQL += " ORDER BY " + tmpStr;
		return selectSQL;
	}
}
