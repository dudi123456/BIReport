package com.ailk.bi.olap.util;

import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.DimLevelTable;
import com.ailk.bi.base.table.RptOlapDimTable;
import com.ailk.bi.base.util.WebConstKeys;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RptOlapDataRight {

	public static String genEvalDeptJoinSQL(RptOlapDimTable rptDim,
			UserCtlRegionStruct userCtl, String dataTabVirName)
			throws ReportOlapException {
		if (null == dataTabVirName)
			throw new ReportOlapException("生成分析型报表的数据权限时输入的参数为空");
		// 由于数据权限复杂，要考虑多重情况
		// 如果这个维度进行展现的话，还得考虑显示哪层,
		// 还是再关联一遍呢，再关联简单些，也分开了
		// 关联两次，
		StringBuffer joinSQL = new StringBuffer();
		if (null == userCtl || null == rptDim)
			return joinSQL.toString();
		if (null == userCtl.ctl_lvl || "".equals(userCtl.ctl_lvl))
			throw new ReportOlapException("生成分析型报表的数据权限时输入的参数为空");
		joinSQL.delete(0, joinSQL.length());
		joinSQL.append(RptOlapDimUtil.getDimRightSQL(rptDim, dataTabVirName,
				userCtl));
		return joinSQL.toString();
	}

	public static String genSvcKndJoinSQL(RptOlapDimTable rptDim,
			String svckndRight, String dataTabVirName)
			throws ReportOlapException {
		StringBuffer joinSQL = new StringBuffer();
		if (null == rptDim
				|| null == svckndRight
				|| "".equals(svckndRight)
				|| null == dataTabVirName
				|| svckndRight
						.equalsIgnoreCase(WebConstKeys.RIGHT_G_C_VALUES_ALL)) {
			return null;
		}
		String virTabNamePrefix = "S_R_";
		// 业务类型维可能是多层次或者一个层次
		List levels = rptDim.dimInfo.dim_levels;
		if (null == levels || 0 == levels.size()) {
			// 单层次，
			String codeFld = rptDim.dimInfo.code_idfld;
			codeFld = RptOlapStringUtil.clearVirTabName(codeFld);
			joinSQL.append(" JOIN (SELECT ");
			joinSQL.append(codeFld).append(" FROM ");
			joinSQL.append(rptDim.dimInfo.dim_table).append(" ");
			joinSQL.append("WHERE ").append(RptOlapConsts.SVC_KND_LVL_FLD)
					.append("=");
			String value = svckndRight;
			if (svckndRight.indexOf("'") >= 0)
				svckndRight = svckndRight.replaceAll("'", "");
			if (RptOlapConsts.FLD_STRING_TYPE.equals(rptDim.dimInfo.idfld_type)) {
				value = "'" + value + "'";
			}
			joinSQL.append(value);
			joinSQL.append(") ")
					.append(virTabNamePrefix + RptOlapConsts.ZERO_STR)
					.append(" ON ");
			joinSQL.append(virTabNamePrefix + RptOlapConsts.ZERO_STR)
					.append(".").append(codeFld).append("=");
			joinSQL.append(dataTabVirName).append(".").append(codeFld)
					.append(" ");
		} else {
			// 多层次
			// 找到最细层，关联上来，然后在关联
			String maxLevel = rptDim.max_level;
			StringBuffer from = new StringBuffer();
			StringBuffer where = new StringBuffer(" WHERE ");
			StringBuffer on = new StringBuffer(" ON ");
			joinSQL.append(" JOIN (SELECT ");
			String initCodeFld = rptDim.dimInfo.code_idfld;
			initCodeFld = RptOlapStringUtil.clearVirTabName(initCodeFld);
			from.append(" FROM ").append(rptDim.dimInfo.dim_table).append(" ")
					.append(virTabNamePrefix + RptOlapConsts.ZERO_STR)
					.append(",");
			where.append(virTabNamePrefix + RptOlapConsts.ZERO_STR + ".")
					.append(RptOlapConsts.SVC_KND_LVL_FLD).append("=");
			String value = svckndRight;
			if (svckndRight.indexOf("'") >= 0)
				svckndRight = svckndRight.replaceAll("'", "");
			if (RptOlapConsts.FLD_STRING_TYPE.equals(rptDim.dimInfo.idfld_type)) {
				value = "'" + value + "'";
			}
			where.append(value);
			DimLevelTable[] levelObjs = (DimLevelTable[]) levels
					.toArray(new DimLevelTable[levels.size()]);
			int init = 0;
			for (int i = 0; i < levelObjs.length; i++) {
				if (maxLevel.equals(levelObjs[i].lvl_id)) {
					init = 0;
					break;
				}
			}
			if (0 == init)
				init = levelObjs.length - 1;
			for (int i = init; i >= 0; i--) {
				String codeFld = levelObjs[i].code_idfld;
				codeFld = RptOlapStringUtil.clearVirTabName(codeFld);
				if (i == init) {
					joinSQL.append(virTabNamePrefix + levelObjs[i].lvl_id + ".")
							.append(codeFld).append(" ");
					from.append(levelObjs[i].lvl_table).append(" ")
							.append(virTabNamePrefix + levelObjs[i].lvl_id)
							.append(",");
					on.append(virTabNamePrefix + RptOlapConsts.ZERO_STR + ".")
							.append(codeFld).append("=");
					on.append(dataTabVirName + ".").append(codeFld);
				} else {
					from.append(levelObjs[i].lvl_table).append(" ")
							.append(virTabNamePrefix + levelObjs[i].lvl_id)
							.append(",");
					String parentFld = levelObjs[i].parent_idfld;
					parentFld = RptOlapStringUtil.clearVirTabName(parentFld);
					where.append(" AND ")
							.append(virTabNamePrefix + levelObjs[i].lvl_id
									+ ".").append(parentFld).append("=");
					codeFld = levelObjs[i + 1].code_idfld;
					codeFld = RptOlapStringUtil.clearVirTabName(codeFld);
					where.append(
							virTabNamePrefix + levelObjs[i + 1].lvl_id + ".")
							.append(codeFld);
				}
				if (0 == i) {
					String parentFld = levelObjs[i].parent_idfld;
					parentFld = RptOlapStringUtil.clearVirTabName(parentFld);
					where.append(" AND ")
							.append(virTabNamePrefix + levelObjs[i].lvl_id
									+ ".").append(parentFld).append("=");
					where.append(
							virTabNamePrefix + RptOlapConsts.ZERO_STR + ".")
							.append(initCodeFld);
				}
			}
			if (from.lastIndexOf(",") >= 0) {
				from = new StringBuffer(
						from.substring(0, from.lastIndexOf(",")));
			}
			joinSQL.append(from).append(where);
			joinSQL.append(" ) ").append(
					virTabNamePrefix + RptOlapConsts.ZERO_STR);
			joinSQL.append(on);
		}
		System.out.println(joinSQL.toString());
		return joinSQL.toString();
	}

	public static String genDevDeptJoinSQL(RptOlapDimTable rptDim,
			UserCtlRegionStruct userCtl) throws ReportOlapException {
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
