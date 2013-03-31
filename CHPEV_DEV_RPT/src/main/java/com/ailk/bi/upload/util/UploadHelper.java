package com.ailk.bi.upload.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.ailk.bi.upload.domain.UiMsuUpReportMetaInfoTable;

/**
 * 上传展现业务辅助工具，只包含业务实现逻辑，而非工具类
 * 
 * 
 * @author chunming
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UploadHelper {

	/**
	 * 提取界面配置条件元数据的下拉列表值
	 * 
	 * @param metaInfo
	 * @return
	 * @throws AppException
	 */
	public static String[] getBIBMListValue(
			UiMsuUpReportMetaInfoTable[] metaList) {

		// 元数据数据
		String[] valueSet = null;
		if (metaList != null) {
			valueSet = new String[metaList.length];
		}

		for (int i = 0; metaList != null && i < metaList.length; i++) {
			String sql = "SELECT " + metaList[i].getRef_field()
					+ " FROM UI_MSU_INFO_DIMDEF ";
			sql += metaList[i].getRef_con();
			sql += " ORDER BY MAP_SEQUENCE";

			String[][] arr = UploadUtil.queryArrayFacade(sql);
			valueSet[i] = UploadUtil.changArrsToStr(arr, 2);
		}
		return valueSet;
	}

	/**
	 * 
	 * @param rpttype
	 * @param vdefine
	 * @param disp_col
	 * @param param
	 * @param report_id
	 * @param record_code
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws AppException
	 */
	public static String[][] getSumDataList(String rpttype,
			UiMsuUpReportMetaInfoTable[] dimMeta,
			UiMsuUpReportMetaInfoTable[] msuMeta, String[] param,
			String report_id, String record_code, String beginDate,
			String endDate) {

		String tableName = "";
		String dateFiled = "";

		if (rpttype.equals("D")) {
			tableName = "UI_MSU_UP_INFO_DAY_DATA";
			dateFiled = "DAY_ID";
		} else if (rpttype.equals("M")) {
			tableName = "UI_MSU_UP_INFO_MONTH_DATA";
			dateFiled = "MONTH_ID";
		} else {
			tableName = "UI_MSU_UP_INFO_DAY_DATA";
			dateFiled = "DAY_ID";
		}

		// 组SQL
		String sql = "SELECT";

		String msuList = "";
		for (int i = 0; msuMeta != null && i < msuMeta.length; i++) {
			if (msuList.length() > 0)
				msuList += ",";
			msuList += "COALESCE(SUM(" + msuMeta[i].getCol_code() + "),0)";
		}

		sql += " " + dateFiled;
		if (msuList != null && msuList.length() > 0) {
			sql += "," + msuList;
		}
		sql += " FROM " + tableName + " WHERE RPT_ID='" + report_id + "'"
				+ " AND RECORD_CODE='" + record_code + "' ";

		for (int i = 0; param != null && i < param.length; i++) {
			if (!"".equals(param[i])) {
				sql += " AND " + dimMeta[i].getCol_code() + "='" + param[i]
						+ "' ";
			}
		}
		//
		sql += "   AND " + dateFiled + ">='" + beginDate + "' and " + dateFiled
				+ "<='" + endDate + "' GROUP BY " + dateFiled + " ORDER BY "
				+ dateFiled + " ASC ";

		System.out.println("getSumDataList==========================" + sql);

		String[][] result = UploadUtil.queryArrayFacade(sql);
		return result;
	}

	/**
	 * 取得当前报表的所有记录类型数组
	 * 
	 * @param list
	 * @return
	 * @throws AppException
	 */
	public static String[] getRecordCode(UiMsuUpReportMetaInfoTable[] list) {
		String[] arr = null;
		//
		ArrayList tmpList = new ArrayList();
		for (int i = 0; i < list.length; i++) {
			if (!tmpList.contains(list[i].getRecord_code())) {
				tmpList.add(list[i].getRecord_code());
			}
		}
		arr = (String[]) tmpList.toArray(new String[tmpList.size()]);

		return arr;
	}

	/**
	 * 统计报表纬度元数据个数
	 * 
	 * @param metaInfo
	 * @return
	 */
	public static int getDimCount(UiMsuUpReportMetaInfoTable[] metaInfo,
			String record) {

		int reI = 0;
		for (int i = 0; metaInfo != null && i < metaInfo.length; i++) {
			if ("1".equals(metaInfo[i].getFiled_type())
					&& record.equals(metaInfo[i].getRecord_code())) {
				reI++;
			}
		}

		return reI;
	}

	/**
	 * 当前列表表头
	 * 
	 * @param list
	 * @return
	 */
	public static HashMap getTitleMap(UiMsuUpReportMetaInfoTable[] list) {
		HashMap titleMap = new HashMap();

		// 寻找关键字
		HashSet recordCodeSet = new HashSet();

		for (int i = 0; i < list.length; i++) {
			recordCodeSet.add(list[i].getRecord_code());
		}
		//
		Iterator iter = recordCodeSet.iterator();
		while (iter.hasNext()) {
			String tmpCode = (String) iter.next();
			String tmpDesc = "";
			for (int i = 0; i < list.length; i++) {
				if (tmpCode.equals(list[i].getRecord_code())) {
					if (tmpDesc.length() > 0) {
						tmpDesc += ",";
					}
					tmpDesc += list[i].getNew_title();
				}
			}
			titleMap.put(tmpCode, tmpDesc);
		}

		return titleMap;
	}

	/**
	 * 当前列表字段
	 * 
	 * @param list
	 * @return
	 */
	public static HashMap getFiledMap(UiMsuUpReportMetaInfoTable[] list,
			String type) {
		HashMap fieldMap = new HashMap();

		// 寻找关键字
		HashSet recordCodeSet = new HashSet();

		for (int i = 0; i < list.length; i++) {
			recordCodeSet.add(list[i].getRecord_code());
		}
		//
		Iterator iter = recordCodeSet.iterator();
		while (iter.hasNext()) {
			String tmpCode = (String) iter.next();
			String tmpDesc = "";
			for (int i = 0; i < list.length; i++) {
				if (tmpCode.equals(list[i].getRecord_code())) {
					if (tmpDesc.length() > 0) {
						tmpDesc += ",";
					}
					if (list[i].getFiled_type().equals("1") && "0".equals(type)) {
						tmpDesc += list[i].getCol_code() + ","
								+ list[i].getCol_code().substring(9);
					} else {
						tmpDesc += list[i].getCol_code();
					}

				}
			}
			fieldMap.put(tmpCode, tmpDesc);
		}

		return fieldMap;
	}

	/**
	 * 当前列表字段精度
	 * 
	 * @param list
	 * @return
	 */
	public static HashMap getDigitMap(UiMsuUpReportMetaInfoTable[] list) {
		HashMap digitMap = new HashMap();

		// 寻找关键字
		HashSet recordCodeSet = new HashSet();

		for (int i = 0; i < list.length; i++) {
			recordCodeSet.add(list[i].getRecord_code());
		}
		//
		Iterator iter = recordCodeSet.iterator();
		while (iter.hasNext()) {
			String tmpCode = (String) iter.next();
			String tmpDesc = "";
			for (int i = 0; i < list.length; i++) {
				if (tmpCode.equals(list[i].getRecord_code())) {
					if (tmpDesc.length() > 0) {
						tmpDesc += ",";
					}
					if (list[i].getDigit() == null
							|| "".equals(list[i].getDigit())) {
						tmpDesc += "0";
					} else {
						tmpDesc += list[i].getDigit();
					}

				}
			}
			digitMap.put(tmpCode, tmpDesc);
		}

		return digitMap;
	}

	/**
	 * 查询纬度和指标具体值
	 * 
	 * @param rpttype
	 *            报表类型
	 * @param fileId
	 *            报表ID
	 * @param dataTime
	 *            日期
	 * @param recordId
	 *            记录类型
	 * @param fields
	 * @return
	 * @throws AppException
	 */
	public static String[][] getReportDetailList(String rpttype,
			String report_id, String date, String record_code, String fields,
			int dimCount, String[] list) {
		String[][] result = null;
		String[] tableInfoa = new String[2];
		// 上传日报
		if (rpttype.equals("D")) {
			tableInfoa[0] = "UI_MSU_UP_INFO_DAY_DATA";
			tableInfoa[1] = "DAY_ID";
		}
		// 上传月报
		if (rpttype.equals("M")) {
			tableInfoa[0] = "UI_MSU_UP_INFO_MONTH_DATA";
			tableInfoa[1] = "MONTH_ID";
		}

		String sql = "SELECT " + fields + " from " + tableInfoa[0] + " where "
				+ tableInfoa[1] + "='" + date;
		sql += "' and RPT_ID='" + report_id + "' and " + " record_code='"
				+ record_code + "' ";
		sql += " order by " + fields;
		System.out.println("getReportDetailList==============" + record_code
				+ "==" + sql);
		result = UploadUtil.queryArrayFacade(sql);
		// 填充值
		HashMap dimDescMap = getDimDescMap(report_id, record_code);

		for (int i = 0; result != null && i < result.length; i++) {
			for (int j = 0; j < dimCount; j++) {
				String key = list[j].trim();
				// System.out.println("key=========="+key);
				HashMap map = (HashMap) dimDescMap.get(key.toUpperCase());

				if (map.get(result[i][2 * j].trim()) == null) {
					result[i][2 * j + 1] = "没有纬度描述";
				} else {
					result[i][2 * j + 1] = map.get(result[i][2 * j].trim())
							.toString();
				}

			}
		}
		//
		return result;
	}

	/**
	 * 查询纬度和指标具体值
	 * 
	 * @param rpttype
	 *            报表类型
	 * @param fileId
	 *            报表ID
	 * @param dataTime
	 *            日期
	 * @param recordId
	 *            记录类型
	 * @param fields
	 * @return
	 * @throws AppException
	 */
	public static String[][] getReportDetailList(String rpttype,
			String report_id, String date, String record_code, String fields) {
		String[][] result = null;
		String[] tableInfoa = new String[2];
		// 上传日报
		if (rpttype.equals("D")) {
			tableInfoa[0] = "UI_MSU_UP_INFO_DAY_DATA";
			tableInfoa[1] = "DAY_ID";
		}
		// 上传月报
		if (rpttype.equals("M")) {
			tableInfoa[0] = "UI_MSU_UP_INFO_MONTH_DATA";
			tableInfoa[1] = "MONTH_ID";
		}

		String sql = "SELECT " + fields + " from " + tableInfoa[0] + " where "
				+ tableInfoa[1] + "='" + date;
		sql += "' and RPT_ID='" + report_id + "' and " + " record_code='"
				+ record_code + "' ";
		sql += " order by " + fields;
		System.out.println("getReportDetailList==============" + record_code
				+ "==" + sql);
		result = UploadUtil.queryArrayFacade(sql);

		//
		return result;
	}

	/**
	 * 纬度描述 MAP
	 * 
	 * @param report_id
	 * @param record_code
	 * @return
	 */
	public static HashMap getDimDescMap(String report_id, String record_code) {

		HashMap map = new HashMap();

		String sql = "";
		sql += "Select a.col_code,a.ref_map_type,b.map_id,b.map_name from ui_msu_up_report_meta_info a,UI_MSU_INFO_DIMDEF b";
		sql += " where a.field_type = '1' and a.report_id='" + report_id + "'"
				+ " and a.ref_map_type=b.map_type and a.record_code='"
				+ record_code + "'" + "order by a.col_sequence,a.ref_map_type";

		String[][] result = UploadUtil.queryArrayFacade(sql);
		//
		HashSet mapSet = new HashSet();
		//
		for (int i = 0; result != null && i < result.length; i++) {
			mapSet.add(result[i][0].toUpperCase());
		}
		//
		Iterator iter = mapSet.iterator();
		while (iter.hasNext()) {
			String tmpCode = (String) iter.next();
			HashMap tmpMap = new HashMap();
			for (int i = 0; i < result.length; i++) {

				if (tmpCode.equals(result[i][0].toUpperCase())) {
					tmpMap.put(result[i][2], result[i][3]);
				}
			}
			map.put(tmpCode, tmpMap);
		}

		//
		return map;
	}

}
