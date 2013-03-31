package com.ailk.bi.content.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ailk.bi.SysConsts;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.Arith;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.app.ReflectUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.content.dao.ICustContentDao;
import com.ailk.bi.content.dao.impl.CustContentDao;
import com.ailk.bi.content.domain.UiCustcontentCalmeasure;
import com.ailk.bi.content.domain.UiCustcontentTemplateTable;
import com.ailk.bi.content.domain.UiKpiData;
import com.ailk.bi.content.service.ICustContentHTML;
import com.ailk.bi.content.util.ContentConsts;
import com.ailk.bi.report.struct.ReportQryStruct;

@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class CustContentHTML implements ICustContentHTML {

	private ICustContentDao contentDao = new CustContentDao();

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.ailk.bi.content.service.ICustContentHTML#getContentDesc(java.lang
	 * .String)
	 */
	public String getContentDesc(String contentID, String whereSql, String whereSqlnoDate,
			ReportQryStruct qryStruct) {
		String result = getContentHTML(contentID, whereSql, whereSqlnoDate, qryStruct);
		return result;
	}

	/**
	 * 获取描述信息
	 *
	 * @param kpiData
	 * @return
	 */
	private String getContentHTML(String contentID, String whereSql, String whereSqlnoDate,
			ReportQryStruct qryStruct) {
		StringBuffer desc = new StringBuffer();

		// 获取模板定义
		UiCustcontentTemplateTable templateTable = getContent(contentID);
		// 获取模板指标定义
		UiCustcontentCalmeasure[] measureTables = getContentCalMeasure(contentID);
		// 获取数据
		boolean isSelfDef = true;// 判断是否全部自定义SQL
		for (int i = 0; measureTables != null && i < measureTables.length; i++) {
			UiCustcontentCalmeasure msu = measureTables[i];
			if (!ContentConsts.DEFAULT_MSU_CAL_TYPE4.equals(msu.cal_msu_type)) {
				isSelfDef = false;
				break;
			}
		}
		UiKpiData[] kpiData = null;
		if (!isSelfDef) {
			kpiData = getContentData(templateTable, measureTables, whereSql);
		}
		String strDate = "";
		if (SysConsts.STAT_PERIOD_DAY.equals(qryStruct.rpt_cycle)) {
			strDate = qryStruct.gather_day;
		}
		if (SysConsts.STAT_PERIOD_MONTH.equals(qryStruct.rpt_cycle)) {
			strDate = qryStruct.gather_month;
		}

		// 数据封装

		Map param = getDataResult(measureTables, kpiData, strDate, whereSql, whereSqlnoDate);
		// 内容模板
		String contentT = templateTable.custcontent;
		String[] ruleIdArr = StringTool.replaceArrValue(contentT, "#");
		for (int i = 0; ruleIdArr != null && i < ruleIdArr.length; i++) {
			String replaceStr = "#" + ruleIdArr[i] + "#";
			Object keyValue = param.get(ruleIdArr[i]);
			String strValue = "--";
			if (keyValue != null) {
				strValue = keyValue.toString();
			}
			if (ContentConsts.CHANGE_DATEDESC.equals(ruleIdArr[i])) {
				if (SysConsts.STAT_PERIOD_DAY.equals(qryStruct.rpt_cycle)) {
					strValue = StringTool.getCnDate(strDate, SysConsts.STAT_PERIOD_DAY);
				} else if (SysConsts.STAT_PERIOD_MONTH.equals(qryStruct.rpt_cycle)) {
					strValue = StringTool.getCnDate(strDate, SysConsts.STAT_PERIOD_MONTH);
				}
			}
			contentT = StringB.replace(contentT, replaceStr, strValue);
		}

		return contentT;
	}

	/**
	 * 获取数据
	 *
	 * @param strSql
	 * @return
	 */
	private UiKpiData[] getContentData(UiCustcontentTemplateTable templateTable,
			UiCustcontentCalmeasure[] measureTables, String whereSql) {
		UiKpiData[] kpiData = null;
		String strSql = getDataSql(templateTable, measureTables, whereSql);
		List listData = contentDao.getContentData(strSql);
		if (listData != null && listData.size() > 0) {
			kpiData = (UiKpiData[]) listData.toArray(new UiKpiData[listData.size()]);
		}

		return kpiData;
	}

	/**
	 * 获取数据SQL
	 *
	 * @return
	 */
	private String getDataSql(UiCustcontentTemplateTable templateTable,
			UiCustcontentCalmeasure[] measureTables, String whereSql) {
		StringBuffer strSql = new StringBuffer();

		// 如果定义为空返回
		if (templateTable == null || measureTables == null || measureTables.length == 0) {
			return "";
		}
		// 指标ID列表
		String msuID = "";
		// 维度字段定义
		String dimCodeSql = "";
		// 指标字段定义
		String msuCodeSql = "";
		for (int i = 0; i < measureTables.length; i++) {
			// 指标ID
			if (measureTables[i].msu_id != null
					&& msuID.indexOf("'" + measureTables[i].msu_id + "'") < 0) {
				if (msuID.length() > 0) {
					msuID += ",";
				}
				msuID += "'" + measureTables[i].msu_id + "'";
			}
			// 维度
			if (measureTables[i].dim_code1 != null
					&& dimCodeSql.indexOf(" " + measureTables[i].dim_code1 + " ") < 0) {
				dimCodeSql += ", " + measureTables[i].dim_code1 + " ";
			}
			if (measureTables[i].dim_code2 != null
					&& dimCodeSql.indexOf(" " + measureTables[i].dim_code2 + " ") < 0) {
				dimCodeSql += ", " + measureTables[i].dim_code2 + " ";
			}
			if (measureTables[i].dim_code3 != null
					&& dimCodeSql.indexOf(" " + measureTables[i].dim_code3 + " ") < 0) {
				dimCodeSql += ", " + measureTables[i].dim_code3 + " ";
			}
			// 指标
			if (measureTables[i].msu_fld != null
					&& msuCodeSql.indexOf(" " + measureTables[i].msu_fld + " ") < 0) {
				msuCodeSql += "," + measureTables[i].real_fld + " AS " + measureTables[i].msu_fld
						+ " ";
			}
			if (measureTables[i].cal_real_fld != null
					&& msuCodeSql.toUpperCase().indexOf(
							" " + measureTables[i].cal_real_fld.toUpperCase() + " ") < 0) {
				msuCodeSql += "," + measureTables[i].cal_real_fld + " AS "
						+ ContentConsts.DEFAULT_MSU_CAL + " ";
			}
		}

		// 拼接数据SQL
		strSql.append("select " + templateTable.date_code + "," + templateTable.msu_code);
		strSql.append(dimCodeSql);
		strSql.append(msuCodeSql);
		strSql.append(" from " + templateTable.data_table + " a where 1=1 ");
		strSql.append(whereSql);
		strSql.append(" and " + templateTable.msu_code + " in (" + msuID + ")");
		strSql.append(" group by " + templateTable.date_code + "," + templateTable.msu_code);
		strSql.append(dimCodeSql);
		System.out.println(strSql.toString());
		return strSql.toString();
	}

	/**
	 * 根据ID获取模板信息
	 *
	 * @param contentID
	 * @return
	 */
	private UiCustcontentTemplateTable getContent(String contentID) {
		UiCustcontentTemplateTable templateTable = null;
		String whereStr = "";
		if (!StringTool.checkEmptyString(contentID)) {
			whereStr = " and CUSTCONTENT_ID='" + contentID + "'";
		}
		List listTemplates = contentDao.getContent(whereStr);
		if (listTemplates != null && listTemplates.size() > 0) {
			templateTable = (UiCustcontentTemplateTable) listTemplates.get(0);
		}

		return templateTable;
	}

	/**
	 * 根据ID获取模板信息
	 *
	 * @param contentID
	 * @return
	 */
	private UiCustcontentCalmeasure[] getContentCalMeasure(String contentID) {
		UiCustcontentCalmeasure[] measureTables = null;
		String whereStr = "";
		if (!StringTool.checkEmptyString(contentID)) {
			whereStr = " and CUSTCONTENT_ID='" + contentID + "'";
		}
		List listTemplates = contentDao.getContentCalMsu(whereStr);
		if (listTemplates != null && listTemplates.size() > 0) {
			measureTables = (UiCustcontentCalmeasure[]) listTemplates
					.toArray(new UiCustcontentCalmeasure[listTemplates.size()]);
		}

		return measureTables;
	}

	private Map getDataResult(UiCustcontentCalmeasure[] msuArr, UiKpiData[] kpiData,
			String strDate, String whereSql, String whereSqlnoDate) {
		Map param = new HashMap();

		for (int k = 0; msuArr != null && k < msuArr.length; k++) {
			UiCustcontentCalmeasure msu = msuArr[k];

			// 自定义SQL内容
			if (ContentConsts.DEFAULT_MSU_CAL_TYPE4.equals(msu.cal_msu_type)) {
				String dataSql = msu.self_sql;
				dataSql = StringB.replace(dataSql, "#date#", strDate);
				dataSql = StringB.replace(dataSql, "#condition#", whereSql);
				dataSql = StringB.replace(dataSql, "#condition_nodate#", whereSqlnoDate);
				System.out.println("dataSql=" + dataSql);
				String[][] tmpData = contentDao.getData(dataSql);
				String tmpResult = "";
				for (int m = 0; tmpData != null && m < tmpData.length; m++) {
					for (int n = 0; tmpData[m] != null && n < tmpData[m].length; n++) {
						tmpResult += tmpData[m][n];
					}
				}
				if (tmpData == null) {
					tmpResult = "--";
				}
				param.put(msu.rule_id, tmpResult);
			} else {
				// 维度数量
				int dimCount = 0;
				boolean dim1 = false;
				boolean dim2 = false;
				boolean dim3 = false;
				if (!StringTool.checkEmptyString(msu.dim_code1)) {
					dimCount++;
					dim1 = true;
				}
				if (!StringTool.checkEmptyString(msu.dim_code2)) {
					dimCount++;
					dim2 = true;
				}
				if (!StringTool.checkEmptyString(msu.dim_code3)) {
					dimCount++;
					dim3 = true;
				}
				// 指标数量
				int msuCount = 1;
				if (!ContentConsts.DEFAULT_MSU_CAL_TYPE0.equals(msu.cal_msu_type)) {
					msuCount++;
				}
				List valueArr = new ArrayList();
				String[] value = new String[dimCount + msuCount];

				// 加和指标
				String valueSum = "0";
				String value1 = "0";
				String value2 = "0";
				for (int i = 0; kpiData != null && i < kpiData.length; i++) {
					UiKpiData kdata = (UiKpiData) kpiData[i];
					String data_msu_id = ReflectUtil.getStringFromObj(kdata,
							ContentConsts.DEFAUTL_MSU_CODE);
					// 根据指标ID定位数据
					if (msu.msu_id.equals(data_msu_id)) {
						// 根据维度1判断
						if (dim1) {
							String data_dim1_value = ReflectUtil.getStringFromObj(kdata,
									msu.dim_code1.toLowerCase());
							if (msu.dim_value1 != null && !msu.dim_value1.equals(data_dim1_value)) {
								valueSum = Arith.add(
										valueSum,
										ReflectUtil.getStringFromObj(kdata,
												msu.msu_fld.toLowerCase()));
								continue;
							}
						}
						// 根据维度2判断
						if (dim2) {
							String data_dim2_value = ReflectUtil.getStringFromObj(kdata,
									msu.dim_code2.toLowerCase());
							if (msu.dim_value2 != null && !msu.dim_value2.equals(data_dim2_value)) {
								valueSum = Arith.add(
										valueSum,
										ReflectUtil.getStringFromObj(kdata,
												msu.msu_fld.toLowerCase()));
								continue;
							}
						}
						// 根据维度1判断
						if (dim3) {
							String data_dim3_value = ReflectUtil.getStringFromObj(kdata,
									msu.dim_code3.toLowerCase());
							if (msu.dim_value3 != null && !msu.dim_value3.equals(data_dim3_value)) {
								valueSum = Arith.add(
										valueSum,
										ReflectUtil.getStringFromObj(kdata,
												msu.msu_fld.toLowerCase()));
								continue;
							}
						}
						valueSum = Arith.add(valueSum,
								ReflectUtil.getStringFromObj(kdata, msu.msu_fld.toLowerCase()));
						value1 = Arith.add(value1,
								ReflectUtil.getStringFromObj(kdata, msu.msu_fld.toLowerCase()));
						value2 = Arith.add(value2,
								ReflectUtil.getStringFromObj(kdata, ContentConsts.DEFAULT_MSU_CAL));
					}
				}

				// 返回指标
				if (ContentConsts.DEFAULT_MSU_CAL_TYPE0.equals(msu.cal_msu_type)) {
					value1 = FormatUtil.formatStr(value1, -9, true);
					param.put(msu.rule_id, value1);
				}
				// 返回比率
				if (ContentConsts.DEFAULT_MSU_CAL_TYPE1.equals(msu.cal_msu_type)) {
					String per = Arith.divPer(value1, value2, 2);
					param.put(msu.rule_id, per);
				}
				// 返回占比
				if (ContentConsts.DEFAULT_MSU_CAL_TYPE2.equals(msu.cal_msu_type)) {
					String per = Arith.divPer(value1, valueSum, 2);
					param.put(msu.rule_id, per);
				}
				// 返回同比环比
				if (ContentConsts.DEFAULT_MSU_CAL_TYPE3.equals(msu.cal_msu_type)) {
					String sum = Arith.add(value1, "-" + value2);
					String per = Arith.divPer(sum, value2, 2);
					if (StringTool.checkEmptyString(per)) {
						per = "--";
					}
					if (SysConsts.YES.equals(msu.show_changeinfo)) {
						Double aa = StringB.toDouble(per, 0);
						if (aa > 0) {
							per = "上升" + per;
						} else if (aa < 0) {
							per = "下降" + per;
						}
					}
					param.put(msu.rule_id, per);
				}
			}
		}

		return param;
	}

}
