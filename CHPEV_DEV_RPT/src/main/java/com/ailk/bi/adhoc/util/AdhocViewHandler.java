package com.ailk.bi.adhoc.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.HashMap;
//import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ailk.bi.adhoc.dao.impl.AdhocDao;
import com.ailk.bi.adhoc.domain.UiAdhocConditionMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocDimMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocInfoDefTable;
import com.ailk.bi.adhoc.domain.UiAdhocMsuMetaTable;
//import com.ailk.bi.adhoc.domain.UiAdhocRuleUserDimTable;
import com.ailk.bi.adhoc.domain.UiAdhocUserListTable;
import com.ailk.bi.adhoc.service.impl.AdhocFacade;
import com.ailk.bi.adhoc.struct.AdhocViewQryStruct;
import com.ailk.bi.adhoc.struct.AdhocViewStruct;
import com.ailk.bi.base.util.*;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.pages.CommKeys;
import com.ailk.bi.system.facade.impl.CommonFacade;

/**
 * 业务逻辑动作类
 * 
 * @author Chunming
 * 
 */
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class AdhocViewHandler {

	/**
	 * 提取有默认值的条件对象数组
	 * 
	 * @param session
	 * @param hoc_id
	 * @param qryStruct
	 * @return
	 */
	public static UiAdhocConditionMetaTable[] getConditionValueArray(
			HttpSession session, String hoc_id, AdhocViewQryStruct qryStruct) {

		UiAdhocConditionMetaTable[] reCon = null;

		// 获取条件结构体序列(全部)
		UiAdhocConditionMetaTable[] con_meta = getConditionMetaBySession(
				session, hoc_id);
		//
		ArrayList tmpList = new ArrayList();
		// 结构字段数组
		Field[] tmpStuct = qryStruct.getClass().getFields();
		// Arrays.sort(tmpStuct);
		//
		if (con_meta != null) {
			for (int j = 0; j < tmpStuct.length; j++) {
				// 取输入条件中的值放入tmpList中
				try {
					String tmpValue = tmpStuct[j].get(qryStruct).toString();
					if (!"".equals(tmpValue)) {
						// 处理范围的条件组织成一行
						if (tmpStuct[j].getName().trim().indexOf("_A_22") > -1) {// 区间值
							if (tmpList != null
									&& tmpList.size() >= 1
									&& tmpStuct[j - 1].getName().trim()
											.indexOf("_A_11") > -1) {
								// 范围
								UiAdhocConditionMetaTable tmpTable = (UiAdhocConditionMetaTable) tmpList
										.get(tmpList.size() - 1);
								String tmpQryName = tmpTable.getQry_name();
								if (tmpQryName != null
										&& tmpQryName.length() > 5) {
									tmpQryName = tmpQryName.substring(5,
											tmpQryName.length());
								}
								String tmpQryStuct = tmpStuct[j].getName();
								if (tmpQryStuct != null
										&& tmpQryStuct.length() > 5) {
									tmpQryStuct = tmpQryStuct.substring(0,
											tmpQryStuct.length() - 5);
								}
								if (tmpQryName.equals(tmpQryStuct)) {
									tmpTable.setValueb(tmpValue);
								} else {
									UiAdhocConditionMetaTable parseTable = setConditionMetaValue(
											con_meta, tmpStuct[j].getName(),
											tmpValue);
									parseTable.setValueb(tmpValue);
									parseTable.setValuea("");
									tmpList.add(parseTable);
								}
								System.out.println("tmpTable null=" + tmpTable);
								System.out.println("tmpTable=" + tmpQryName);
								System.out
										.println("tmpStuct[j]=" + tmpQryStuct);
								// ???????有问题
							} else {
								// 实际项
								UiAdhocConditionMetaTable parseTable = setConditionMetaValue(
										con_meta, tmpStuct[j].getName(),
										tmpValue);
								parseTable.setValueb(tmpValue);
								parseTable.setValuea("");
								tmpList.add(parseTable);
							}
						} else {
							UiAdhocConditionMetaTable parseTable = setConditionMetaValue(
									con_meta, tmpStuct[j].getName(), tmpValue);
							if (parseTable != null) {
								// 如果带描述则value存id
								// value2存描述；在维护时一定id在前；描述在后。
								if (parseTable.getCon_type().equals(
										AdhocConstant.ADHOC_CONDITION_TYPE_6)) {
									parseTable.setValueb(tmpStuct[j + 1].get(
											qryStruct).toString());
								}
								// 对多选的处理
								if (parseTable.getCon_type().equals(
										AdhocConstant.ADHOC_CONDITION_TYPE_5)) {
									parseTable.setValueb(tmpStuct[j + 1].get(
											qryStruct).toString());
								}
								tmpList.add(parseTable);
							}
						}
					}
				} catch (IllegalArgumentException e) {

					e.printStackTrace();
				} catch (IllegalAccessException e) {

					e.printStackTrace();
				}

			}
		}
		// 抽取条件对象数组（已经拼好值）
		if (tmpList != null && tmpList.size() > 0) {
			reCon = (UiAdhocConditionMetaTable[]) tmpList
					.toArray(new UiAdhocConditionMetaTable[tmpList.size()]);
		}
		return reCon;
	}

	/**
	 * 提取当前即席查询已选条件配置元数据信息（包含默认条件）
	 * 
	 * @param session
	 * @param adhoc
	 * @return
	 */
	public static UiAdhocConditionMetaTable[] getConditionMetaBySession(
			HttpSession session, String adhoc) {

		UiAdhocConditionMetaTable[] con_meta = null;

		String[] conArr = (String[]) session
				.getAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS);
		if (conArr == null) {
			conArr = new String[0];
		}
		String conStr = "";
		for (int i = 0; conArr != null && i < conArr.length; i++) {
			if (conStr.length() > 0) {
				conStr += ",";
			}
			conStr += "'" + conArr[i] + "'";
		}
		AdhocFacade facade = new AdhocFacade(new AdhocDao());
		ArrayList selectedList = null;
		// 已经选中
		if (!"".equals(conStr)) {
			selectedList = (ArrayList) facade.getConListByString(conStr);
		}

		// 默认选择纬度
		ArrayList conList = (ArrayList) facade.getDefaultConListByHocId(adhoc);
		// 融和
		ArrayList list = new ArrayList();
		if (conList != null && !conList.isEmpty()) {
			for (int i = 0; i < conList.size(); i++) {
				list.add(conList.get(i));
			}
		}

		//
		if (selectedList != null && !selectedList.isEmpty()) {
			for (int i = 0; i < selectedList.size(); i++) {
				list.add(selectedList.get(i));
			}
		}

		//
		if (list != null && !list.isEmpty()) {
			//
			ArrayList arr = (ArrayList) list;
			con_meta = (UiAdhocConditionMetaTable[]) arr
					.toArray(new UiAdhocConditionMetaTable[arr.size()]);
		}
		return con_meta;
	}

	/**
	 * 根据条件元数据名称设置值
	 * 
	 * @param metaList
	 * @param name
	 * @param value
	 * @return
	 */
	public static UiAdhocConditionMetaTable setConditionMetaValue(
			UiAdhocConditionMetaTable[] metaList, String name, String value) {
		UiAdhocConditionMetaTable con = null;
		// 提取比较名称
		if (name.indexOf("_A_11") > -1 || name.indexOf("_A_22") > -1) {
			name = name.substring(0, name.length() - 5);
		}
		name = "QRY__" + name;
		//
		for (int i = 0; metaList != null && i < metaList.length; i++) {
			if (name.trim().toUpperCase()
					.equals(metaList[i].getQry_name().trim().toUpperCase())) {
				con = metaList[i];
				break;
			}
		}
		if (con != null) {
			con.setValuea(value);
		}
		return con;
	}

	/**
	 * 根据条件元数据名称设置值B
	 * 
	 * @param metaList
	 * @param name
	 * @param value
	 * @return
	 */
	public static UiAdhocConditionMetaTable setConditionMetaDuraValue(
			UiAdhocConditionMetaTable[] metaList, String name, String value) {
		UiAdhocConditionMetaTable con = null;

		// 提取比较名称
		if (name.indexOf("_A_11") > -1 || name.indexOf("_A_22") > -1) {
			name = name.substring(0, name.length() - 5);
		}
		//
		for (int i = 0; metaList != null && i < metaList.length; i++) {
			if (name.trim().toUpperCase()
					.equals(metaList[i].getFiled_name().trim().toUpperCase())) {
				con = metaList[i];
				con.setValueb(value);
				// metaList[i].setValueb(value);
				break;
			}
		}

		return con;
	}

	/**
	 * 
	 * @param conParam
	 * @param facade
	 * @param adhoc_root
	 * @return
	 */
	public static UiAdhocConditionMetaTable[] getConditionSelectedArray(
			String[] conParam, AdhocFacade facade, String adhoc_root,
			String[] hconParam) {
		UiAdhocConditionMetaTable[] parseCondition = null;
		// 当前选中的条件
		String conditionStr = "";
		for (int i = 0; conParam != null && i < conParam.length; i++) {
			if (conditionStr.length() > 0) {
				conditionStr += ",";
			}
			conditionStr += "'" + conParam[i] + "'";
		}
		// 默认选择纬度
		ArrayList conList = (ArrayList) facade
				.getDefaultConListByHocId(adhoc_root);
		ArrayList conditionList = null;
		ArrayList list = new ArrayList();

		// 已经选中
		if (!"".equals(conditionStr)) {
			conditionList = (ArrayList) facade.getConListByString(conditionStr);
		}
		if (conList != null && !conList.isEmpty()) {
			for (int i = 0; i < conList.size(); i++) {
				list.add(conList.get(i));
			}
		}
		if (conditionList != null && !conditionList.isEmpty()) {
			for (int i = 0; i < conditionList.size(); i++) {
				list.add(conditionList.get(i));
			}
		}
		//
		UiAdhocConditionMetaTable[] tmpparseCON = (UiAdhocConditionMetaTable[]) list
				.toArray(new UiAdhocConditionMetaTable[list.size()]);
		ArrayList orderList = new ArrayList();
		for (int i = 0; hconParam != null && i < hconParam.length; i++) {
			for (int j = 0; tmpparseCON != null && j < tmpparseCON.length; j++) {
				if (tmpparseCON[j].getCon_id().equalsIgnoreCase(hconParam[i])) {
					orderList.add(tmpparseCON[j]);
					break;
				}
			}
		}
		parseCondition = (UiAdhocConditionMetaTable[]) orderList
				.toArray(new UiAdhocConditionMetaTable[orderList.size()]);
		return parseCondition;
	}

	/**
	 * 
	 * @param dimParam
	 * @param facade
	 * @param adhoc_root
	 * @return
	 */
	public static UiAdhocDimMetaTable[] getDimSelectedArray(String[] dimParam,
			AdhocFacade facade, String adhoc_root, String[] hdimParam) {
		UiAdhocDimMetaTable[] parseDim = null;
		// 当前选中的纬度条件
		String dimStr = "";
		for (int i = 0; dimParam != null && i < dimParam.length; i++) {
			if (dimStr.length() > 0) {
				dimStr += ",";
			}
			dimStr += "'" + dimParam[i] + "'";
		}

		ArrayList tempList = null;
		ArrayList list = new ArrayList();
		// 已经选中
		if (!"".equals(dimStr)) {
			tempList = (ArrayList) facade.getDimListByString(dimStr);
		}
		// 默认选择纬度
		ArrayList dimList = (ArrayList) facade
				.getDefaultDimListByHocId(adhoc_root);

		if (dimList != null && !dimList.isEmpty()) {
			for (int i = 0; i < dimList.size(); i++) {
				list.add(dimList.get(i));
			}
		}
		if (tempList != null && !tempList.isEmpty()) {
			for (int i = 0; i < tempList.size(); i++) {
				list.add(tempList.get(i));
			}
		}
		UiAdhocDimMetaTable[] tmpparseDim = (UiAdhocDimMetaTable[]) list
				.toArray(new UiAdhocDimMetaTable[list.size()]);
		ArrayList orderList = new ArrayList();
		for (int i = 0; hdimParam != null && i < hdimParam.length; i++) {
			for (int j = 0; tmpparseDim != null && j < tmpparseDim.length; j++) {
				if (tmpparseDim[j].getDim_id().equalsIgnoreCase(hdimParam[i])) {
					orderList.add(tmpparseDim[j]);
					break;
				}
			}
		}
		parseDim = (UiAdhocDimMetaTable[]) orderList
				.toArray(new UiAdhocDimMetaTable[orderList.size()]);

		return parseDim;
	}

	public static UiAdhocDimMetaTable[] getDimSelectedArray(AdhocFacade facade,
			String whereStr) {
		UiAdhocDimMetaTable[] parseDim = null;
		if (whereStr == null || "".equals(whereStr))
			return null;

		String whereSql = " WHERE 1=1";
		if (whereStr != null && whereStr.trim().length() > 0) {
			whereSql += whereStr;
		}

		ArrayList list = null;
		if (!"".equals(whereSql)) {
			list = (ArrayList) facade.getDimListByWhereString(whereSql);
		}
		parseDim = (UiAdhocDimMetaTable[]) list
				.toArray(new UiAdhocDimMetaTable[list.size()]);

		return parseDim;
	}

	/**
	 * 
	 * @param dimParam
	 * @param facade
	 * @param adhoc_root
	 * @return
	 */
	public static UiAdhocMsuMetaTable[] getMsuSelectedArray(String[] msuParam,
			AdhocFacade facade, String adhoc_root, String[] hmsuParam) {
		UiAdhocMsuMetaTable[] parseMsu = null;
		// 当前选中的纬度条件
		String msuStr = "";
		for (int i = 0; msuParam != null && i < msuParam.length; i++) {
			if (msuStr.length() > 0) {
				msuStr += ",";
			}
			msuStr += "'" + msuParam[i] + "'";
		}

		ArrayList tempList = null;
		ArrayList list = new ArrayList();
		// 已经选中
		if (!"".equals(msuStr)) {
			tempList = (ArrayList) facade.getMsuListByString(msuStr);
		}
		// 默认选择纬度
		ArrayList msuList = (ArrayList) facade
				.getDefaultMsuListByHocId(adhoc_root);
		if (msuList != null && !msuList.isEmpty()) {
			for (int i = 0; i < msuList.size(); i++) {
				list.add(msuList.get(i));
			}
		}
		if (tempList != null && !tempList.isEmpty()) {
			for (int i = 0; i < tempList.size(); i++) {
				list.add(tempList.get(i));
			}
		}
		//
		UiAdhocMsuMetaTable[] tmpparseMSU = (UiAdhocMsuMetaTable[]) list
				.toArray(new UiAdhocMsuMetaTable[list.size()]);
		ArrayList orderList = new ArrayList();
		for (int i = 0; hmsuParam != null && i < hmsuParam.length; i++) {
			for (int j = 0; tmpparseMSU != null && j < tmpparseMSU.length; j++) {
				if (tmpparseMSU[j].getMsu_id().equalsIgnoreCase(hmsuParam[i])) {
					orderList.add(tmpparseMSU[j]);
					break;
				}
			}
		}
		//
		parseMsu = (UiAdhocMsuMetaTable[]) orderList
				.toArray(new UiAdhocMsuMetaTable[orderList.size()]);
		return parseMsu;
	}

	/**
	 * 初始化查询视图
	 * 
	 * @param session
	 * @param qryStruct
	 *            查询结构
	 * @param parseCondition
	 *            条件
	 * @param parseDim
	 *            纬度
	 * @param parseMsu
	 *            度量
	 * @return
	 */
	public static AdhocViewStruct init(HttpServletRequest request,
			AdhocViewQryStruct qryStruct,
			UiAdhocConditionMetaTable[] parseCondition,
			UiAdhocDimMetaTable parseDim[], UiAdhocMsuMetaTable parseMsu[],
			UiAdhocInfoDefTable hocInfo) {

		HttpSession session = request.getSession();
		String headDesc = "";
		// 定义表头汉语描述的HTML格式的内容
		String headStr = "";
		// 定义查询内容的标准查询，例如：SELECT AA FROM
		String sqlStd = "";
		// 定义查询条件的组合，
		String sqlCon = "WHERE 1=1 ";
		// 定义查询条件的分组组合，例如：GROUP BY
		String sqlGrp = "";
		// 定义查询条件的排序内容，例如：ORDER BY AA
		String sqlOdr = "";
		// 过滤SQL
		String sqlHaving_1 = "";
		String sqlHaving_2 = "";
		// 指标汇总
		String sumStr = "";
		// 解析条件

		String leftOuterJoin = "";

		if (parseCondition != null) {
			for (int i = 0; parseCondition != null && i < parseCondition.length; i++) {
				// 分不同类型条件提取拼装SQL
				// System.out.println("zxj:kkkkkkkkkkkkkkkkkkkk1111" +
				// parseCondition[i].getQry_name());

				if (parseCondition[i].getCon_type().equals(
						AdhocConstant.ADHOC_CONDITION_TYPE_17)) {// 上传文件类型
					String tmpValue = StringB.NulltoBlank(request
							.getParameter(parseCondition[i].getQry_name()));

					if (tmpValue.length() > 0) {
						String strTmp1 = parseCondition[i].getFiled_name()
								+ "," + tmpValue;
						if (leftOuterJoin.length() == 0) {
							leftOuterJoin = strTmp1;
						} else {
							leftOuterJoin += "|" + strTmp1;
						}
					}

				}

				sqlCon += "  " + getSqlWhereString(parseCondition[i], request);

			}
		}

		// 进行数据权限设置(保留)

		// 拼装纬度
		if (parseDim != null) {
			for (int i = 0; parseDim != null && i < parseDim.length; i++) {
				// group
				if (sqlGrp.length() > 0)
					sqlGrp += ",";
				if (AdhocHelper.hasUserSelfDim(parseDim[i], hocInfo,
						CommonFacade.getLoginId(session))) {
					sqlGrp += AdhocHelper.genCaseWhenString(parseDim[i],
							hocInfo, CommonFacade.getLoginId(session));
				} else {
					sqlGrp += parseDim[i].getDim_field_name();
				}
				// having_1

				if (sqlHaving_1.length() > 0)
					sqlHaving_1 += " AND ";
				if (AdhocHelper.hasUserSelfDim(parseDim[i], hocInfo,
						CommonFacade.getLoginId(session))) {
					sqlHaving_1 += AdhocHelper.genCaseWhenString(parseDim[i],
							hocInfo, CommonFacade.getLoginId(session))
							+ " IS NOT NULL ";
				} else {
					sqlHaving_1 += "GROUPING("
							+ parseDim[i].getDim_field_name() + ")=0";
				}

				// having_2
				if (sqlHaving_2.length() > 0)
					sqlHaving_2 += " AND ";
				if (AdhocHelper.hasUserSelfDim(parseDim[i], hocInfo,
						CommonFacade.getLoginId(session))) {
					sqlHaving_2 += AdhocHelper.genCaseWhenString(parseDim[i],
							hocInfo, CommonFacade.getLoginId(session))
							+ " IS NULL ";
				} else {
					sqlHaving_2 += parseDim[i].getDim_field_name() + " IS NULL";
				}

				// order
				if (sqlOdr.length() > 0)
					sqlOdr += ",";
				sqlOdr += parseDim[i].getDim_field_name();
				// select
				if (sqlStd.length() > 0)
					sqlStd += ",";
				if (AdhocHelper.hasUserSelfDim(parseDim[i], hocInfo,
						CommonFacade.getLoginId(session))) {
					sqlStd += AdhocHelper.genCaseWhenString(parseDim[i],
							hocInfo, CommonFacade.getLoginId(session))
							+ " "
							+ parseDim[i].getDim_field_name();
					/*
					 * sqlStd += AdhocHelper.genCaseWhenString(parseDim[i],
					 * hocInfo,CommonFacade.getLoginId(session)) + " " +
					 * parseDim[i].getDim_field_name() + "  ,  " + i;
					 */
				} else {
					// sqlStd += parseDim[i].getDim_field_name() + " ,  " + i;
					sqlStd += parseDim[i].getDim_field_name();
				}
				// head
				if (headStr.length() > 0)
					headStr += ",";
				headStr += parseDim[i].getDim_name();

				if (headDesc.length() > 0) {
					headDesc += ",";
				}
				if (parseDim[i].getDim_desc().equals("")) {
					headDesc += " ";
				} else {
					headDesc += parseDim[i].getDim_desc();// 这里是新加的
				}

			}
		}

		// 提取纬度（查询和描述）
		if (parseMsu != null) {
			for (int i = 0; parseMsu != null && i < parseMsu.length; i++) {
				//
				if (sumStr.length() > 0)
					sumStr += ",";
				if ("S".equals(parseMsu[i].getCal_rule())) {
					sumStr += "COALESCE(SUM(" + parseMsu[i].getMsu_field()
							+ "),0)";
				} else if ("C".equals(parseMsu[i].getCal_rule())) {
					sumStr += "COUNT(" + parseMsu[i].getMsu_field() + ")";
				} else if ("Z".equals(parseMsu[i].getCal_rule())) {
					sumStr += parseMsu[i].getCal_msu();
					if (sumStr.indexOf("#op_time#") >= 0) {
						sumStr = StringB.replace(sumStr, "#op_time#",
								qryStruct.op_time);
					}
				} else {
					sumStr += "COALESCE(SUM(" + parseMsu[i].getMsu_field()
							+ "),0)";
				}

				if (headStr.length() > 0)
					headStr += ",";
				headStr += parseMsu[i].getMsu_name();

				if (headDesc.length() > 0) {
					headDesc += ",";
				}
				if (parseMsu[i].getMsu_desc().equals("")) {
					headDesc += " ";
				} else {
					headDesc += parseMsu[i].getMsu_desc();// 这里是新加的
				}

			}
		} else {
			sumStr += "";
			headStr += "";
		}
		System.out.println("headStr=====" + headStr);
		// 拼上select 和数据表
		if (sqlStd != null && sqlStd.length() > 0) {
			sqlStd += ",";
		}
		sqlStd += sumStr;
		// sqlStd += " from " + hocInfo.getData_table();

		System.out.println("sqlStd=====" + sqlStd);
		System.out.println("sqlGrp=====" + sqlGrp);
		System.out.println("sqlOdr=====" + sqlOdr);
		System.out.println("headStr=====" + headStr);
		System.out.println("sqlHaving_1=====" + sqlHaving_1);
		System.out.println("sqlHaving_2=====" + sqlHaving_2);
		System.out.println("leftOuterJoin=====" + leftOuterJoin);
		/**
		 * *********************************************************************
		 * *
		 */
		//
		AdhocViewStruct adhocView = new AdhocViewStruct();
		adhocView.headStr = headStr; // "日期,客户姓名,手机号码,状态,本月通话,本月应收入";
		adhocView.headDesc = headDesc;// 表头描述
		adhocView.sqlStd = sqlStd; // "select
		adhocView.sqlCon = sqlCon; // " where GATHER_MON=200507";
		adhocView.sqlGrp = sqlGrp; // " group by
		adhocView.sqlOdr = sqlOdr; // " order by
		adhocView.sqlHaving_1 = sqlHaving_1; // " order by
		adhocView.sqlHaving_2 = sqlHaving_2; // " order by
		adhocView.sqlHaving = "(" + sqlHaving_1 + ") OR (" + sqlHaving_2 + ")"; // "

		adhocView.leftOuterJoinTag = leftOuterJoin;
		// order
		// by

		return adhocView;

	}

	/**
	 * 初始化查询视图
	 * 
	 * @param session
	 * @param qryStruct
	 *            查询结构
	 * @param parseCondition
	 *            条件
	 * @param parseDim
	 *            纬度
	 * @param parseMsu
	 *            度量
	 * @return
	 */
	public static AdhocViewStruct init(HttpSession session,
			AdhocViewQryStruct qryStruct,
			UiAdhocConditionMetaTable[] parseCondition,
			UiAdhocDimMetaTable parseDim[], UiAdhocMsuMetaTable parseMsu[],
			UiAdhocConditionMetaTable[] valueCondition,
			UiAdhocInfoDefTable hocInfo) {
		String headDesc = "";
		// 定义表头汉语描述的HTML格式的内容
		String headStr = "";
		// 定义查询内容的标准查询，例如：SELECT AA FROM
		String sqlStd = "";
		// 定义查询条件的组合，
		String sqlCon = "WHERE 1=1 ";
		// 定义查询条件的分组组合，例如：GROUP BY
		String sqlGrp = "";
		// 定义查询条件的排序内容，例如：ORDER BY AA
		String sqlOdr = "";
		// 过滤SQL
		String sqlHaving_1 = "";
		String sqlHaving_2 = "";
		// 指标汇总
		String sumStr = "";
		// 解析条件

		String leftOuterJoin = "";

		if (parseCondition != null) {
			for (int i = 0; parseCondition != null && i < parseCondition.length; i++) {
				// 分不同类型条件提取拼装SQL

				if (parseCondition[i].getCon_type().equals(
						AdhocConstant.ADHOC_CONDITION_TYPE_17)) {// 上传文件类型

					for (int yy = 0; valueCondition != null
							&& yy < valueCondition.length; yy++) {

						if (parseCondition[i].getCon_id().equals(
								valueCondition[yy].getCon_id())) {

							if (StringB.NulltoBlank(
									valueCondition[yy].getValuea()).length() > 0) {
								String strTmp1 = valueCondition[yy]
										.getFiled_name()
										+ ","
										+ valueCondition[yy].getValuea();
								if (leftOuterJoin.length() == 0) {
									leftOuterJoin = strTmp1;
								} else {
									leftOuterJoin += "|" + strTmp1;
								}
							}
						}
					}

				}

				sqlCon += "  "
						+ getSqlWhereString(valueCondition,
								parseCondition[i].getCon_id());

			}
		}

		// 进行数据权限设置(保留)

		// 拼装纬度
		if (parseDim != null) {
			for (int i = 0; parseDim != null && i < parseDim.length; i++) {
				// group
				if (sqlGrp.length() > 0)
					sqlGrp += ",";
				if (AdhocHelper.hasUserSelfDim(parseDim[i], hocInfo,
						CommonFacade.getLoginId(session))) {
					sqlGrp += AdhocHelper.genCaseWhenString(parseDim[i],
							hocInfo, CommonFacade.getLoginId(session));
				} else {
					sqlGrp += parseDim[i].getDim_field_name();
				}
				// having_1

				if (sqlHaving_1.length() > 0)
					sqlHaving_1 += " AND ";
				if (AdhocHelper.hasUserSelfDim(parseDim[i], hocInfo,
						CommonFacade.getLoginId(session))) {
					sqlHaving_1 += AdhocHelper.genCaseWhenString(parseDim[i],
							hocInfo, CommonFacade.getLoginId(session))
							+ " IS NOT NULL ";
				} else {
					sqlHaving_1 += "GROUPING("
							+ parseDim[i].getDim_field_name() + ")=0";
				}

				// having_2
				if (sqlHaving_2.length() > 0)
					sqlHaving_2 += " AND ";
				if (AdhocHelper.hasUserSelfDim(parseDim[i], hocInfo,
						CommonFacade.getLoginId(session))) {
					sqlHaving_2 += AdhocHelper.genCaseWhenString(parseDim[i],
							hocInfo, CommonFacade.getLoginId(session))
							+ " IS NULL ";
				} else {
					sqlHaving_2 += parseDim[i].getDim_field_name() + " IS NULL";
				}

				// order
				if (sqlOdr.length() > 0)
					sqlOdr += ",";
				sqlOdr += parseDim[i].getDim_field_name();
				// select
				if (sqlStd.length() > 0)
					sqlStd += ",";
				if (AdhocHelper.hasUserSelfDim(parseDim[i], hocInfo,
						CommonFacade.getLoginId(session))) {
					sqlStd += AdhocHelper.genCaseWhenString(parseDim[i],
							hocInfo, CommonFacade.getLoginId(session))
							+ " "
							+ parseDim[i].getDim_field_name() + "  ,  " + i;
				} else {
					sqlStd += parseDim[i].getDim_field_name() + " ,  " + i;
				}
				// head
				if (headStr.length() > 0)
					headStr += ",";
				headStr += parseDim[i].getDim_name();

				if (headDesc.length() > 0) {
					headDesc += ",";
				}
				if (parseDim[i].getDim_desc().equals("")) {
					headDesc += " ";
				} else {
					headDesc += parseDim[i].getDim_desc();// 这里是新加的
				}

			}
		}

		// 提取纬度（查询和描述）
		if (parseMsu != null) {
			for (int i = 0; parseMsu != null && i < parseMsu.length; i++) {
				//
				if (sumStr.length() > 0)
					sumStr += ",";
				if ("S".equals(parseMsu[i].getCal_rule())) {
					sumStr += "COALESCE(SUM(" + parseMsu[i].getMsu_field()
							+ "),0)";
				} else if ("C".equals(parseMsu[i].getCal_rule())) {
					sumStr += "COUNT(" + parseMsu[i].getMsu_field() + ")";
				} else if ("Z".equals(parseMsu[i].getCal_rule())) {
					sumStr += parseMsu[i].getCal_msu();
					if (sumStr.indexOf("#op_time#") >= 0) {
						sumStr = StringB.replace(sumStr, "#op_time#",
								qryStruct.op_time);
					}
				} else {
					sumStr += "COALESCE(SUM(" + parseMsu[i].getMsu_field()
							+ "),0)";
				}

				if (headStr.length() > 0)
					headStr += ",";
				headStr += parseMsu[i].getMsu_name();

				if (headDesc.length() > 0) {
					headDesc += ",";
				}
				if (parseMsu[i].getMsu_desc().equals("")) {
					headDesc += " ";
				} else {
					headDesc += parseMsu[i].getMsu_desc();// 这里是新加的
				}

			}
		} else {
			sumStr += "";
			headStr += "";
		}
		System.out.println("headStr=====" + headStr);
		// 拼上select 和数据表
		if (sqlStd != null && sqlStd.length() > 0) {
			sqlStd += ",";
		}
		sqlStd += sumStr;
		// sqlStd += " from " + hocInfo.getData_table();

		System.out.println("sqlStd=====" + sqlStd);
		System.out.println("sqlGrp=====" + sqlGrp);
		System.out.println("sqlOdr=====" + sqlOdr);
		System.out.println("headStr=====" + headStr);
		System.out.println("sqlHaving_1=====" + sqlHaving_1);
		System.out.println("sqlHaving_2=====" + sqlHaving_2);
		System.out.println("leftOuterJoin=====" + leftOuterJoin);
		/**
		 * *********************************************************************
		 * *
		 */
		//
		AdhocViewStruct adhocView = new AdhocViewStruct();
		adhocView.headStr = headStr; // "日期,客户姓名,手机号码,状态,本月通话,本月应收入";
		adhocView.headDesc = headDesc;// 表头描述
		adhocView.sqlStd = sqlStd; // "select
		adhocView.sqlCon = sqlCon; // " where GATHER_MON=200507";
		adhocView.sqlGrp = sqlGrp; // " group by
		adhocView.sqlOdr = sqlOdr; // " order by
		adhocView.sqlHaving_1 = sqlHaving_1; // " order by
		adhocView.sqlHaving_2 = sqlHaving_2; // " order by
		adhocView.sqlHaving = "(" + sqlHaving_1 + ") OR (" + sqlHaving_2 + ")"; // "

		adhocView.leftOuterJoinTag = leftOuterJoin;
		// order
		// by

		return adhocView;

	}

	/**
	 * 提取条件SQL
	 * 
	 * @param valueCondition
	 * @param con_id
	 * @return
	 */
	public static String getSqlWhereString(
			UiAdhocConditionMetaTable[] valueCondition, String con_id) {
		String reWhere = "";
		for (int i = 0; valueCondition != null && i < valueCondition.length; i++) {
			if (con_id.equals(valueCondition[i].getCon_id())) {

				String tmp = valueCondition[i].getFiled_name();
				String fieldType = valueCondition[i].getCon_tag();
				String showType = valueCondition[i].getCon_type();
				String validator = valueCondition[i].getValidator();
				// 对范围的处理
				if (showType.equals(AdhocConstant.ADHOC_CONDITION_TYPE_2)
						|| showType
								.equals(AdhocConstant.ADHOC_CONDITION_TYPE_9)
						|| showType
								.equals(AdhocConstant.ADHOC_CONDITION_TYPE_10)) {
					if (fieldType.equals(AdhocConstant.ADHOC_CONDITION_TAG_1)) {
						if (!valueCondition[i].getValuea().equals((""))) {
							reWhere = " AND " + tmp + " >=";
							reWhere += valueCondition[i].getValuea();
						}
						if (!valueCondition[i].getValueb().equals((""))) {
							reWhere += " AND " + tmp + "<=";
							reWhere += valueCondition[i].getValueb();
						}
					} else {
						if (!valueCondition[i].getValuea().equals((""))) {
							reWhere = " AND " + tmp + ">='";
							reWhere += valueCondition[i].getValuea() + "'";
						}
						if (!valueCondition[i].getValueb().equals((""))) {
							reWhere += " AND " + tmp + "<='";
							reWhere += valueCondition[i].getValueb() + "'";
						}
					}
				}
				// 对多选条件的处理
				else if (showType.equals(AdhocConstant.ADHOC_CONDITION_TYPE_5)
						|| showType
								.equals(AdhocConstant.ADHOC_CONDITION_TYPE_15)) {
					if (fieldType.equals(AdhocConstant.ADHOC_CONDITION_TAG_1)) {
						// 数值
						reWhere = "AND " + tmp + " in ( ";
						reWhere += valueCondition[i].getValuea().replaceAll(
								"|", ",");
						reWhere += " ) ";
					} else {
						// 数值
						reWhere = "AND " + tmp + " in ( ";
						reWhere += AdhocUtil.fillCharString(valueCondition[i]
								.getValuea());
						reWhere += " ) ";
					}

				} else if (showType
						.equals(AdhocConstant.ADHOC_CONDITION_TYPE_16)) {// TEXTAREA
					// System.out.println("TEXTAREA:" + tmp + ":" +
					// valueCondition[i].getValuea() + ":" +
					// valueCondition[i].getCon_tag());

					String tempNo = valueCondition[i].getValuea().trim();
					String[] noSplit = tempNo.split("\r\n");
					String strTmpWhere = "";
					for (int kk = 0; kk < noSplit.length; kk++) {
						String num = noSplit[kk].trim();
						if (num.length() > 0) {
							if (fieldType.trim().equals(
									AdhocConstant.ADHOC_CONDITION_TAG_1)) {// 数值条件
								if (strTmpWhere.length() == 0) {
									strTmpWhere = " in (" + num;
								} else {
									strTmpWhere += "," + num;
								}
							} else if (fieldType.trim().equals(
									AdhocConstant.ADHOC_CONDITION_TAG_2)
									|| fieldType
											.trim()
											.equals(AdhocConstant.ADHOC_CONDITION_TAG_3)) {// 字符条件
								if (strTmpWhere.length() == 0) {
									strTmpWhere = " in ('" + num;
								} else {
									strTmpWhere += "','" + num;
								}
							}

						}
					}
					if (fieldType.trim().equals(
							AdhocConstant.ADHOC_CONDITION_TAG_1)) {// 数值条件
						strTmpWhere += ")";
					} else {
						strTmpWhere += "')";
					}
					reWhere += " AND " + tmp + strTmpWhere;
					// System.out.println("strTmpWhere:" + strTmpWhere);
				} else if (showType
						.equals(AdhocConstant.ADHOC_CONDITION_TYPE_17)) {
					// 上传文件类型在and里忽略，

				} else { // 对简单条件的处理
					if (fieldType.equals(AdhocConstant.ADHOC_CONDITION_TAG_1)) {
						reWhere = " AND " + tmp + " =";
						reWhere += valueCondition[i].getValuea();
					} else {
						if (fieldType.trim().equals(
								AdhocConstant.ADHOC_CONDITION_TAG_3)) {
							reWhere = " AND " + tmp + " like '%";
							reWhere += valueCondition[i].getValuea().trim()
									+ "%'";

						} else {
							reWhere = " AND " + tmp + "='";
							reWhere += valueCondition[i].getValuea() + "'";
						}
					}
				}

				// 附加条件
				if (!"".equals(valueCondition[i].getValuea())
						|| !"".equals(valueCondition[i].getValueb())) {
					reWhere += " " + validator;
				}
			}
		}
		return reWhere;

	}

	/**
	 * 提取条件SQL
	 * 
	 * @param valueCondition
	 * @param con_id
	 * @return
	 */
	public static String getSqlWhereString(
			UiAdhocConditionMetaTable parseCondition, HttpServletRequest request) {
		String reWhere = "";

		String tmp = parseCondition.getFiled_name();
		String fieldType = parseCondition.getCon_tag();
		String showType = parseCondition.getCon_type();
		String validator = parseCondition.getValidator();
		// 对范围的处理
		String tmpValue = StringB.NulltoBlank(request
				.getParameter(parseCondition.getQry_name()));

		if (showType.equals(AdhocConstant.ADHOC_CONDITION_TYPE_2)
				|| showType.equals(AdhocConstant.ADHOC_CONDITION_TYPE_9)
				|| showType.equals(AdhocConstant.ADHOC_CONDITION_TYPE_10)) {

		} else {
			if (tmpValue.length() == 0) {
				return "";
			}
		}

		// System.out.println("zxj:kkkkkkkkkkkkkkkkkkkk" +
		// parseCondition.getQry_name() + ":" + tmpValue + ":" + showType);

		if (showType.equals(AdhocConstant.ADHOC_CONDITION_TYPE_2)
				|| showType.equals(AdhocConstant.ADHOC_CONDITION_TYPE_9)
				|| showType.equals(AdhocConstant.ADHOC_CONDITION_TYPE_10)) {

			String tmpA = StringB.NulltoBlank(request
					.getParameter(parseCondition.getQry_name() + "_A_11"));
			String tmpB = StringB.NulltoBlank(request
					.getParameter(parseCondition.getQry_name() + "_A_22"));

			if (fieldType.equals(AdhocConstant.ADHOC_CONDITION_TAG_1)) {

				if (!tmpA.equals((""))) {
					reWhere = " AND " + tmp + " >=";
					reWhere += tmpA;
				}

				if (!tmpB.equals((""))) {
					reWhere += " AND " + tmp + "<=";
					reWhere += tmpB;
				}
			} else {
				if (!tmpA.equals((""))) {
					reWhere = " AND " + tmp + ">='";
					reWhere += tmpA + "'";
				}
				if (!tmpB.equals((""))) {
					reWhere += " AND " + tmp + "<='";
					reWhere += tmpB + "'";
				}
			}
		}
		// 对多选条件的处理
		else if (showType.equals(AdhocConstant.ADHOC_CONDITION_TYPE_5)
				|| showType.equals(AdhocConstant.ADHOC_CONDITION_TYPE_15)) {

			if (fieldType.equals(AdhocConstant.ADHOC_CONDITION_TAG_1)) {
				// 数值
				if (tmpValue.indexOf(CommKeys.ID_DELI) >= 0) {
					reWhere = "AND " + tmp + " in ( ";
					// reWhere += tmpValue.replaceAll("\\|", ",");
					reWhere += tmpValue;
					reWhere += " ) ";
				} else {
					reWhere = "AND " + tmp + " =";
					reWhere += tmpValue + " ";
				}

			} else {
				// 字符串
				if (tmpValue.indexOf(CommKeys.ID_DELI) >= 0) {
					reWhere = "AND " + tmp + " in ( ";
					reWhere += AdhocUtil.fillCharString(tmpValue);
					reWhere += " ) ";
				} else {
					reWhere = "AND " + tmp + " ='";
					reWhere += tmpValue + "' ";
				}

			}

		} else if (showType.equals(AdhocConstant.ADHOC_CONDITION_TYPE_16)) {// TEXTAREA
			// System.out.println("TEXTAREA:" + tmp + ":" +
			// valueCondition[i].getValuea() + ":" +
			// valueCondition[i].getCon_tag());

			String tempNo = tmpValue.trim();
			String[] noSplit = tempNo.split("\r\n");
			String strTmpWhere = "";
			for (int kk = 0; kk < noSplit.length; kk++) {
				String num = noSplit[kk].trim();
				if (num.length() > 0) {
					if (fieldType.trim().equals(
							AdhocConstant.ADHOC_CONDITION_TAG_1)) {// 数值条件
						if (strTmpWhere.length() == 0) {
							strTmpWhere = " in (" + num;
						} else {
							strTmpWhere += "," + num;
						}
					} else if (fieldType.trim().equals(
							AdhocConstant.ADHOC_CONDITION_TAG_2)
							|| fieldType.trim().equals(
									AdhocConstant.ADHOC_CONDITION_TAG_3)) {// 字符条件
						if (strTmpWhere.length() == 0) {
							strTmpWhere = " in ('" + num;
						} else {
							strTmpWhere += "','" + num;
						}
					}

				}
			}
			if (fieldType.trim().equals(AdhocConstant.ADHOC_CONDITION_TAG_1)) {// 数值条件
				strTmpWhere += ")";
			} else {
				strTmpWhere += "')";
			}
			reWhere += " AND " + tmp + strTmpWhere;
			// System.out.println("strTmpWhere:" + strTmpWhere);
		} else if (showType.equals(AdhocConstant.ADHOC_CONDITION_TYPE_17)) {
			// 上传文件类型在and里忽略，

		} else if (showType.equals(AdhocConstant.ADHOC_CONDITION_TYPE_18)) {
			// IS NULL
			reWhere = " AND " + tmp + " IS NULL";
		} else if (showType.equals(AdhocConstant.ADHOC_CONDITION_TYPE_19)) {
			// IS NOT NULL
			reWhere = " AND " + tmp + " IS NOT NULL";

		} else if (showType.equals(AdhocConstant.ADHOC_CONDITION_TYPE_20)) {
			// !=的情况
			if (fieldType.equals(AdhocConstant.ADHOC_CONDITION_TAG_1)) {
				reWhere = " AND " + tmp + " <>";
				reWhere += tmpValue;
			} else {
				reWhere = " AND " + tmp + " <>'";
				reWhere += tmpValue + "'";
			}

		} else { // 对简单条件的处理
			if (fieldType.equals(AdhocConstant.ADHOC_CONDITION_TAG_1)) {
				reWhere = " AND " + tmp + " =";
				reWhere += tmpValue;
			} else {
				if (fieldType.trim()
						.equals(AdhocConstant.ADHOC_CONDITION_TAG_3)) {

					// 如果tmpValue有%，则用like查询，否则用"="查询
					if (tmpValue.indexOf("%") >= 0) {
						reWhere = " AND " + tmp + " like '" + tmpValue + "'";
					} else {
						reWhere = " AND " + tmp + "='" + tmpValue + "'";
					}

					// reWhere = " AND " + tmp + " like '%";
					// reWhere += tmpValue
					// + "%'";

				} else {
					reWhere = " AND " + tmp + "='";
					reWhere += tmpValue + "'";
				}
			}
		}

		// 附加条件
		/*
		 * if (!"".equals(valueCondition[i].getValuea()) ||
		 * !"".equals(valueCondition[i].getValueb())) { reWhere += " " +
		 * validator; }
		 */

		return reWhere;

	}

	/**
	 * 填从纬度描述
	 * 
	 * @param list
	 * @param parseDim
	 */
	/*
	 * public static void fillDimDescByCodeParam(HttpServletRequest request,
	 * String[][] list, UiAdhocDimMetaTable[] parseDim, String hoc_id) { if
	 * (parseDim == null || list == null) { return; } String oper_no =
	 * CommonFacade.getLoginId(request.getSession());
	 * 
	 * for (int i = 0; i < parseDim.length; i++) { String map_code =
	 * parseDim[i].getDim_map_code().toUpperCase();
	 * System.out.println("=map_code=========" + map_code); //
	 * System.out.println("=i=========" + i); if (map_code == null ||
	 * "".equals(map_code)) { for (int j = 0; j < list.length; j++) { //
	 * System.out.println("=list[j][2 * i]=========" + list[j][2 // * i]);
	 * list[j][2 * i + 1] = list[j][2 * i]; } continue; } String relation_field
	 * = parseDim[i].getDim_relation_field(); AdhocFacade facade = new
	 * AdhocFacade(new AdhocDao()); if (!"".equals(relation_field)) { HashMap
	 * map = new HashMap(); UiAdhocRuleUserDimTable[] arr =
	 * facade.getAdhocUserDimList( hoc_id, oper_no, relation_field); if (arr !=
	 * null && arr.length > 0) { for (int a = 0; a < arr.length; a++) {
	 * map.put(arr[a].getSelf_dim_id(), arr[a] .getSelf_dim_name()); } for (int
	 * j = 0; j < list.length; j++) { if (map.get(list[j][2 * i]) == null) {
	 * list[j][2 * i + 1] = ""; } else { list[j][2 * i + 1] = map.get(list[j][2
	 * * i]) .toString(); }
	 * 
	 * } } else { for (int j = 0; j < list.length; j++) { list[j][2 * i + 1] =
	 * CodeParamUtil .codeListParamFetcher(request, map_code, list[j][2 * i]); }
	 * } } else {
	 * 
	 * for (int j = 0; j < list.length; j++) { list[j][2 * i + 1] =
	 * CodeParamUtil.codeListParamFetcher( request, map_code, list[j][2 * i]); }
	 * }
	 * 
	 * } }
	 */
	/**
	 * 填从纬度描述
	 * 
	 * @param list
	 * @param parseDim
	 */
	public static void fillDimDescByCodeParam(HttpServletRequest request,
			String[][] list, UiAdhocDimMetaTable[] parseDim, String hoc_id) {
		if (parseDim == null || list == null) {
			return;
		}

		for (int i = 0; i < parseDim.length; i++) {
			if (!"".equals(parseDim[i].getDim_map_code())) {
				String map_code = parseDim[i].getDim_map_code().toUpperCase();

				for (int j = 0; j < list.length; j++) {
					// System.out.println("fillDescByMapCode========1============"+
					// map_code+"========="+list[j][i]);
					list[j][i] = CodeParamUtil.codeListParamFetcher(request,
							map_code, list[j][i]);
					// System.out.println("fillDescByMapCode========2============"+
					// list[j][i]);

				}
			}
		}
	}

	/**
	 * 填从纬度描述
	 * 
	 * @param list
	 * @param parseDim
	 */
	public static void fillDescByMapCode(HttpServletRequest request,
			String[][] list, UiAdhocUserListTable[] userList) {

		System.out.println("fillDescByMapCode========1============");
		if (userList == null || list == null) {
			return;
		}
		ServletContext context = request.getSession().getServletContext();
		HashMap codeMap = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_CODE_LIST);

		for (int i = 0; i < userList.length; i++) {
			if (!"".equals(userList[i].getMap_code())) {
				String map_code = userList[i].getMap_code().toUpperCase();
				// System.out.println(map_code + ":map_code" );
				// System.out.println(list.length + ":list.length" );
				HashMap map = (HashMap) codeMap.get(map_code.trim()
						.toUpperCase());

				for (int j = 0; j < list.length; j++) {
					// System.out.println("fillDescByMapCode========1============"+
					// map_code+"========="+list[j][i]);
					list[j][i] = CodeParamUtil.codeListParamFetcher(request,
							map, list[j][i]);
					// System.out.println("fillDescByMapCode========2============"+
					// list[j][i]);

				}
			}
		}
	}

}
