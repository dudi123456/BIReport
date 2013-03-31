package com.ailk.bi.report.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import com.ailk.bi.base.exception.CustomMsuException;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.TableConsts;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.MsuDimStruct;
import com.ailk.bi.report.struct.MsuTreeNode;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class CustomMsuUtil {
	private static Logger logger = Logger.getLogger(CustomMsuUtil.class);

	/**
	 * 判断是否有除自己外和自己相同名称的自定义指标
	 *
	 * @param msuId
	 *            自定义指标标识
	 * @param msuName
	 *            自定义指标名称
	 * @return 有或没有
	 * @throws CustomMsuException
	 */
	public static boolean hasSameNamedCustomMsu(String msuId, String msuName)
			throws CustomMsuException {
		// 加严条件
		boolean hasSame = true;
		if (null != msuId && null != msuName) {
			try {
				String select = SQLGenator.genSQL("Q5220", msuId, "'" + msuName
						+ "'");
				hasSame = hasSelectResult(select, false);
			} catch (AppException ape) {
				// 这里不再抛出异常
				hasSame = true;
			}
		}
		return hasSame;
	}

	/**
	 * 判断查询语句是否有结果，并根据默认值设置返回值
	 *
	 * @param sql
	 *            要查询的语句
	 * @param defaultValue
	 *            默认没有结果返回的值
	 * @return 查询结果
	 */
	private static boolean hasSelectResult(String sql, boolean defaultValue) {
		boolean hasResult = defaultValue;
		if (null != sql && null != sql) {
			try {
				String[][] svces = WebDBUtil.execQryArray(sql, "");
				if (null != svces && svces.length > 0
						&& !"".equals(svces[0][0])) {
					hasResult = !defaultValue;
				} else {
					hasResult = defaultValue;
				}
			} catch (AppException ape) {
				// 这里不再抛出异常
				hasResult = defaultValue;
			}
		}
		return hasResult;
	}

	/**
	 * 删除自定义指标
	 *
	 * @param msuId
	 *            自定义指标标识
	 * @throws CustomMsuException
	 */
	public static void deleteCustomMsu(String msuId) throws CustomMsuException {
		if (null != msuId) {
			try {
				String[] deleteSql = new String[2];
				deleteSql[0] = SQLGenator.genSQL("D5180", msuId);
				deleteSql[1] = SQLGenator.genSQL("D5190", msuId);
				executeBatchSQL(deleteSql);
			} catch (AppException ape) {
				throw new CustomMsuException("删除自定义指标生成SQL语句失败", ape);
			}
		}
	}

	/**
	 * 判断自定义指标是否已经被报表使用
	 *
	 * @param msuId
	 *            自定义指标标识
	 * @return 是否已经被使用
	 * @throws CustomMsuException
	 */
	public static boolean customMsuUsedForReport(String msuId)
			throws CustomMsuException {
		boolean used = true;
		if (null != msuId) {
			try {
				String select = SQLGenator.genSQL("Q5210", "'" + msuId + "'");
				used = hasSelectResult(select, false);
			} catch (AppException ape) {
				used = true;
			}
		}
		return used;
	}

	/**
	 * 生成自定义指标更新SQL语句
	 *
	 * @param msuId
	 *            自定义指标标识
	 * @param msuName
	 *            自定义指标名称
	 * @return 更新语句
	 */
	public static String genUpdateCustomMsuSQL(String msuId, String msuName)
			throws CustomMsuException {
		String ret = null;
		if (null != msuId && null != msuName) {
			try {
				ret = SQLGenator.genSQL("C5200", "'" + msuName + "'", msuId);
			} catch (AppException ape) {
				throw new CustomMsuException("自定义指标更新语句生成失败", ape);
			}
		}
		return ret;
	}

	/**
	 * 更新自定义指标
	 *
	 * @param msuId
	 *            自定义指标标识
	 * @param sql
	 *            要执行的SQL数组
	 * @throws CustomMsuException
	 */
	public static void modifyCustomMsu(String msuId, String msuName,
			HttpServletRequest request) throws CustomMsuException {
		// 先删除自定义指标的维度
		deleteCustomMsuDims(msuId);
		String sql = CustomMsuUtil.genUpdateCustomMsuSQL(msuId, msuName);
		List insert = new java.util.ArrayList();
		insert.add(sql);
		// 获取用户选择的维度
		List dimsAndValues = CustomMsuUtil.getCustomMsuDims(msuId);
		if (null != dimsAndValues && dimsAndValues.size() >= 2) {
			List dims = (List) dimsAndValues.get(0);
			Iterator iter = dims.iterator();
			while (iter.hasNext()) {
				MsuDimStruct dim = (MsuDimStruct) iter.next();
				String[] values = request.getParameterValues(dim.getDim_id());
				List tmp = CustomMsuUtil.genMsuDimSQL(msuId, dim.getDim_id(),
						dim.getDim_level(), values);
				if (null != tmp)
					insert.addAll(tmp);
			}
		}
		String[] sqlAry = (String[]) insert.toArray(new String[insert.size()]);
		executeBatchSQL(sqlAry);
	}

	/**
	 * 删除自定义指标的维度记录
	 *
	 * @param msuId
	 *            自定义指标的标识
	 * @throws CustomMsuException
	 */
	private static void deleteCustomMsuDims(String msuId)
			throws CustomMsuException {
		if (null != msuId) {
			try {
				String delete = SQLGenator.genSQL("D5190", msuId);
				WebDBUtil.execUpdate(delete);
			} catch (AppException ape) {
				throw new CustomMsuException("删除自定义指标维度值失败", ape);
			}

		}
	}

	/**
	 * 生成日和月的树节点
	 *
	 * @param userId
	 *            用户标识
	 * @return 节点对象列表
	 */
	public static List getDayAndMonthNodes(String userId) {
		List ret = null;
		ret = new ArrayList();
		// 生成月节点
		MsuTreeNode monthNode = new MsuTreeNode();
		monthNode.setIndex(0);
		monthNode.setMsu_desc("所有的月自定义指标");
		monthNode.setMsu_rule("");
		monthNode.setObjectId(TableConsts.CUSTOM_MSU_VIR_MONTH_ID);
		monthNode.setRule_desc("");
		monthNode.setSrctab_id(TableConsts.CUSTOM_MSU_VIR_MONTH_ID);
		monthNode.setSrctab_name("");
		monthNode.setTitle("月指标");
		monthNode.setWidgetId(TableConsts.CUSTOM_MSU_VIR_MONTH_ID);
		monthNode.setPrecision("");
		monthNode.setIsFolder(hasCustomMus(userId,
				TableConsts.STAT_PERIOD_MONTH));
		ret.add(monthNode);
		// 生成日指标节点
		MsuTreeNode dayNode = new MsuTreeNode();
		dayNode.setIndex(1);
		dayNode.setMsu_desc("所有的日自定义指标");
		dayNode.setMsu_rule("");
		dayNode.setObjectId(TableConsts.CUSTOM_MSU_VIR_DAY_ID);
		dayNode.setRule_desc("");
		dayNode.setSrctab_id(TableConsts.CUSTOM_MSU_VIR_DAY_ID);
		dayNode.setSrctab_name("");
		dayNode.setTitle("日指标");
		dayNode.setWidgetId(TableConsts.CUSTOM_MSU_VIR_DAY_ID);
		dayNode.setPrecision("");
		dayNode.setIsFolder(hasCustomMus(userId, TableConsts.STAT_PERIOD_DAY));
		ret.add(dayNode);
		return ret;
	}

	/**
	 * 判断用户是否有日指标或月指标
	 *
	 * @param userId
	 *            用户标识
	 * @param stat_period
	 *            统计周期
	 * @return 是否有指标
	 */
	private static boolean hasCustomMus(String userId, String stat_period) {
		boolean has = false;
		if (null != userId && null != stat_period) {
			try {
				String select = SQLGenator.genSQL("Q5150", "'" + userId + "'",
						stat_period);
				has = hasSelectResult(select, false);
			} catch (AppException ape) {
				// 这里不再抛出异常
				has = false;
			}
		}
		return has;
	}

	/**
	 * 为防止用户多次提交设置不同的令牌
	 *
	 * @return 返回当前的时间字符串
	 */
	public static String getToken() {
		String ret = null;
		Date now = new Date();
		ret = "" + now.getTime();
		return ret;
	}

	/**
	 * 判断是否存在同名的自定义指标
	 *
	 * @param userId
	 *            用户标识
	 * @param msu_name
	 *            自定义指标名称
	 * @param stat_period
	 *            统计周期
	 * @return 是否有相同名称
	 */
	public static boolean hasOtherSameNamedCustomMsu(String userId,
			String msu_name, String stat_period) {
		boolean hasSame = true;
		if (null != userId && null != msu_name && null != stat_period) {
			try {
				String select = SQLGenator.genSQL("Q5140", "'" + userId + "'",
						"'" + msu_name + "'", stat_period);
				hasSame = hasSelectResult(select, false);
			} catch (AppException ape) {
				// 这里不再抛出异常
				hasSame = true;
			}
		}
		return hasSame;
	}

	/**
	 * 生成自定义指标插入语句
	 *
	 * @param userId
	 *            用户标识
	 * @param msu_name
	 *            指标名称
	 * @param base_msu
	 *            基本指标标识
	 * @param srctab_id
	 *            数据源标识
	 * @param stat_period
	 *            统计周期
	 * @return INSERT SQL
	 * @throws CustomMsuException
	 */
	public static String genCustomMsuSQL(String userId, String msu_name,
			String base_msu, String srctab_id, String stat_period)
			throws CustomMsuException {
		String sql = null;
		if (null != userId && null != msu_name && null != base_msu
				&& null != srctab_id && null != stat_period) {
			try {
				List list = new ArrayList();
				list.add("'" + userId + "'");
				// 去序列的值
				list.add(TableConsts.CUSTOM_MSU_SEQUENCE + ".NEXTVAL");
				list.add("'" + msu_name + "'");
				list.add("'" + base_msu + "'");
				list.add("'" + srctab_id + "'");
				list.add(stat_period);
				list.add("'Y'");
				String[] strIdxValue = (String[]) list.toArray(new String[list
						.size()]);
				sql = SQLGenator.genSQL("I5120", strIdxValue);
			} catch (AppException ae) {
				throw new CustomMsuException("生成自定义指标插入语句失败", ae);
			}
		}
		return sql;
	}

	/**
	 * 生成自定义指标维度值插入语句
	 *
	 * @param msuId
	 *            自定义指标标识
	 * @param dim_id
	 *            维度标识
	 * @param dim_level
	 *            数据粒度，层次标识
	 * @param values
	 *            维度值数组
	 * @return 返回维度值插入语句列表
	 * @throws CustomMsuException
	 */
	public static List genMsuDimSQL(String msuId, String dim_id,
			String dim_level, String[] values) throws CustomMsuException {
		List ret = null;
		if (null != dim_id && null != dim_level && null != values) {
			ret = new ArrayList();
			try {
				for (int i = 0; i < values.length; i++) {
					// 取序列的当前值
					String msu_id = TableConsts.CUSTOM_MSU_SEQUENCE
							+ ".CURRVAL";
					if (null != msuId)
						msu_id = msuId;
					String sql = SQLGenator.genSQL("I5130", msu_id, "'"
							+ dim_id + "'", dim_level, "'" + values[i] + "'");
					ret.add(sql);
				}
			} catch (AppException ae) {
				throw new CustomMsuException("生成自定义指标维度值插入语句失败", ae);
			}
		}
		return ret;
	}

	/**
	 * 使用二相提交执行批量SQL语句
	 *
	 * @param sqlAry
	 *            SQL语句数组
	 * @throws CustomMsuException
	 */
	private static void executeBatchSQL(String[] sqlAry)
			throws CustomMsuException {
		if (null != sqlAry) {
			// 这里使用二相提交
			Connection conn = null;
			Statement stmt = null;
			try {
				conn = WebDBUtil.getConn();
				conn.setAutoCommit(false);
				stmt = conn.createStatement();
				for (int i = 0; i < sqlAry.length; i++) {
					stmt.addBatch(sqlAry[i]);
				}
				stmt.executeBatch();
				conn.commit();
				conn.setAutoCommit(true);
			} catch (AppException ape) {
				if (null != conn) {
					try {
						conn.rollback();
					} catch (SQLException se) {
						throw new CustomMsuException("执行批量SQL语句操作回滚失败", se);
					}
				}
				throw new CustomMsuException("执行批量SQL语句操作失败", ape);
			} catch (SQLException se) {
				if (null != stmt) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (null != conn) {
					try {
						conn.rollback();
					} catch (SQLException se1) {
						throw new CustomMsuException("执行批量SQL语句操作回滚失败", se1);
					}
				}
				throw new CustomMsuException("执行批量SQL语句操作失败", se);
			} finally {
				if (null != conn) {
					try {
						conn.close();
					} catch (SQLException se) {
						throw new CustomMsuException("执行批量SQL语句操作关闭连接失败", se);
					}
				}
			}
		}
	}

	/**
	 * 取出自定义指标的维度列表
	 *
	 * @param msuId
	 *            自定义指标标识
	 * @return 列表包含两个对象，一个dim对象的列表，一个是MAP对象，里面是维度值
	 */
	public static List getCustomMsuDims(String msuId) throws CustomMsuException {
		List dimsAndValues = null;
		if (null != msuId) {
			try {
				String select = SQLGenator.genSQL("Q5170", "'" + msuId + "'");
				String[][] svces = WebDBUtil.execQryArray(select, "");
				if (null != svces && svces.length > 0) {
					dimsAndValues = new ArrayList();
					List dims = new ArrayList(2);
					// 声明临时维度标识
					String tmpDimId = "";
					MsuDimStruct dimObj = null;
					Map values = new HashMap();
					List dimValues = new ArrayList();
					for (int i = 0; i < svces.length; i++) {
						String dimId = svces[i][0];
						if (!tmpDimId.equals(dimId)) {
							if (!"".equals(tmpDimId)) {// 不是第一次
								dims.add(dimObj);
								dimObj = new MsuDimStruct();
								dimObj.setDim_id(svces[i][0]);
								dimObj.setDim_level(svces[i][1]);
								dimObj.setHlvl_id(svces[i][2]);
								dimObj.setDim_name(svces[i][3]);
								dimObj.setDim_table(svces[i][4]);
								dimObj.setCode_fld(svces[i][5]);
								dimObj.setCode_desc(svces[i][6]);
								// 值放进去,这里转换为字符串，因为直接对象的化是引用
								values.put(tmpDimId, dimValues.toString());
								dimValues.clear();
								dimValues.add(svces[i][7]);
								tmpDimId = dimId;
							} else {
								dimObj = new MsuDimStruct();
								dimObj.setDim_id(svces[i][0]);
								dimObj.setDim_level(svces[i][1]);
								dimObj.setHlvl_id(svces[i][2]);
								dimObj.setDim_name(svces[i][3]);
								dimObj.setDim_table(svces[i][4]);
								dimObj.setCode_fld(svces[i][5]);
								dimObj.setCode_desc(svces[i][6]);
								dimValues.add(svces[i][7]);
								tmpDimId = dimId;
							}
						} else {
							tmpDimId = dimId;
							// 去维度值放进去
							dimValues.add(svces[i][7]);
						}
					}
					// 最后一个放进去
					if (null != dimObj)
						dims.add(dimObj);
					if (null != dimValues && dimValues.size() > 0)
						values.put(tmpDimId, dimValues.toString());
					dimsAndValues.add(0, dims);
					dimsAndValues.add(1, values);
				}
			} catch (AppException ae) {
				throw new CustomMsuException("获取自定义指标的相关维度和值出错", ae);
			}
		}
		return dimsAndValues;
	}

	/**
	 * 由自定义指标的维度和所选值列表生成JSON对象列表
	 *
	 * @param dimsAndValues
	 *            自定义指标的维度和所选值
	 * @return 返回维度的JSON对象列表
	 */
	public static List getCustomMsuDimsValue(List dimsAndValues)
			throws CustomMsuException {
		List dimsValue = null;
		if (null != dimsAndValues && !"".equals(dimsAndValues)) {
			List dims = null;
			Map values = null;
			// 取出维度对象
			dims = (List) dimsAndValues.get(0);
			// 取出值列表
			values = (Map) dimsAndValues.get(1);
			if (null != dims) {
				// 组装成一个union所有的语句
				Map tmpDims = convertDimsListToMap(dims);
				String select = genDimsSelectSQL(dims);
				dimsValue = genDimsValueJSonString(select, tmpDims, values);
			}
		}
		return dimsValue;
	}

	/**
	 * 获取自定义指标的维度JSON字符串
	 *
	 * @param dimsValue
	 *            维度的JSON字符串
	 * @return 整个JSON字符串
	 */
	public static String genDimValueJSonString(List dimsValue) {
		String ret = null;
		if (null != dimsValue) {
			StringBuffer sb = new StringBuffer("");
			Iterator iter = dimsValue.iterator();
			while (iter.hasNext()) {
				String st = (String) iter.next();
				sb.append(st);
			}
			if (null != sb && sb.length() > 0) {
				ret = sb.toString();
				if (null != ret && ret.length() > 0) {
					ret = ret.substring(0, ret.length() - 1);
					ret = "{\"dims\":[" + ret + "]}";
				}
			}
		}
		return ret;
	}

	/**
	 * 获取基本指标分类
	 *
	 * @return
	 */
	public static List getBaseMsuType(String period) throws CustomMsuException {
		if (null == period || "".equals(period))
			throw new CustomMsuException("获取基本指标的分类时输入的参数为空");
		List types = new ArrayList();
		try {
			String select = SQLGenator.genSQL("Q5105",
					TableConsts.DATA_USED_FOR_RPT_CUSTOM,
					TableConsts.DATA_USED_FOR_GENERAL, period);
			logger.debug(select);
			String[][] svces = WebDBUtil.execQryArray(select, "");
			if (null != svces) {
				for (int i = 0; i < svces.length; i++) {
					MsuTreeNode node = new MsuTreeNode();
					node.setIndex(i);
					node.setSrctab_id("");
					node.setSrctab_name("");
					node.setWidgetId(svces[i][0]);
					node.setObjectId(svces[i][0]);
					node.setTitle(svces[i][1]);
					node.setMsu_rule("");
					node.setRule_desc("");
					node.setMsu_desc("");
					node.setPrecision("");
					node.setIsFolder(true);
					types.add(node);
				}
			}
		} catch (AppException e) {
			throw new CustomMsuException("查询基本指标的类型时发生错误", e);
		}
		return types;
	}

	/**
	 * 取出基本指标的维度列表
	 *
	 * @param msuId
	 *            基本指标的标识
	 * @param srctabId
	 *            数据源标识
	 * @return 维度对象的列表
	 */
	public static List getBaseMsuDims(String msuId, String srctabId)
			throws CustomMsuException {
		List dims = null;
		if (null != msuId && !"".equals(msuId) && null != srctabId
				&& !"".equals(srctabId)) {
			try {
				String select = SQLGenator.genSQL("Q5110", "'" + msuId + "'",
						"'" + srctabId + "'");
				String[][] svces = WebDBUtil.execQryArray(select, "");
				if (null != svces && svces.length > 0) {
					dims = new ArrayList();
					for (int i = 0; i < svces.length; i++) {
						MsuDimStruct dimObj = new MsuDimStruct();
						dimObj.setDim_id(svces[i][0]);
						dimObj.setDim_level(svces[i][1]);
						dimObj.setHlvl_id(svces[i][2]);
						dimObj.setDim_name(svces[i][3]);
						dimObj.setDim_table(svces[i][4]);
						dimObj.setCode_fld(svces[i][5]);
						dimObj.setCode_desc(svces[i][6]);
						dims.add(dimObj);
					}
				}
			} catch (AppException ae) {
				throw new CustomMsuException("获取基本指标的相关维度出错");
			}
		}
		return dims;
	}

	/**
	 * 生成所有维度值查询的语句，语句用UNION联合
	 *
	 * @param dims
	 *            维度对象列表
	 * @return 查询语句
	 */
	private static String genDimsSelectSQL(List dims) {
		String sql = null;
		if (null != dims && !"".equals(dims)) {
			StringBuffer sb = new StringBuffer("");
			Iterator iter = dims.iterator();
			int count = 0;
			while (iter.hasNext()) {
				MsuDimStruct dimObj = (MsuDimStruct) iter.next();
				if (count > 0) {
					sb.append(" UNION ");
				}
				sb.append("SELECT '").append(dimObj.getDim_id()).append("',");// 作为常量
				sb.append("'").append(dimObj.getHlvl_id())
						.append("' AS LEVEL_ID,");// 作为常量
				sb.append("to_char(" + dimObj.getCode_fld() + ")").append(
						" AS CODE_ID,");
				sb.append(dimObj.getCode_desc());
				sb.append(" FROM ").append(dimObj.getDim_table());
				count++;
			}
			if (null != sb && sb.length() > 0) {
				sb.append(" ORDER BY ").append(" LEVEL_ID,CODE_ID ASC");
			}
			sql = sb.toString();
		}
		// logger.debug("genDimsSelectSQL="+sql);
		return sql;
	}

	/**
	 * 将维度对象列表转换为维度对象MAP
	 *
	 * @param dims
	 *            维度对象列表
	 * @return 转换后的MAP
	 */
	private static Map convertDimsListToMap(List dims) {
		Map dimsMap = null;
		if (null != dims) {
			dimsMap = new HashMap();
			Iterator iter = dims.iterator();
			while (iter.hasNext()) {
				MsuDimStruct dimObj = (MsuDimStruct) iter.next();
				dimsMap.put(dimObj.getDim_id(), dimObj);
			}
		}
		return dimsMap;
	}

	/**
	 * 生成维度值的JSON列表
	 *
	 * @param select
	 *            查询语句
	 * @param dimsMap
	 *            维度对象MAP
	 * @param selectedValues
	 *            用户选择的值
	 * @return JSON 维度值列表
	 */
	private static List genDimsValueJSonString(String select, Map dimsMap,
			Map selectedValues) {
		List dimsValue = null;
		if (null != select && !"".equals(select)) {
			try {
				String[][] svces = WebDBUtil.execQryArray(select, "");
				if (null != svces && svces.length > 0) {
					dimsValue = new ArrayList();
					String tmpId = "";
					StringBuffer value = new StringBuffer("");
					for (int i = 0; i < svces.length; i++) {
						String dimId = svces[i][0];
						if (!tmpId.equals(dimId)) {
							if (null != value && value.length() > 0) {
								// 放到列表中
								String tmpStr = value.toString();
								tmpStr = tmpStr.substring(0,
										tmpStr.length() - 1);// 去掉最后一个逗号
								tmpStr = "{\"dimName\":\""
										+ ((MsuDimStruct) dimsMap.get(tmpId))
												.getDim_name()
										+ "\",\"dimId\":\"" + tmpId
										+ "\",\"dimValues\":[" + tmpStr + "]},";
								dimsValue.add(tmpStr);
								value.delete(0, value.length());
							} else {
								// 第一次访问
								tmpId = dimId;
								value.append("{\"codeId\":\"")
										.append(svces[i][2]).append("\",");
								String tmpStr = svces[i][3];
								tmpStr = tmpStr.replaceAll("\"", "");
								value.append("\"codeValue\":\"").append(tmpStr)
										.append("\",\"selected\":\"");
								// 判断是否选中
								if (null != selectedValues) {
									Object tmpObj = selectedValues.get(tmpId);
									if (null != tmpObj) {
										tmpStr = (String) tmpObj;
										int pos = tmpStr.indexOf(svces[i][2]);
										if (pos >= 0) {
											value.append("true\"");
										} else {
											value.append("false\"");
										}
									} else {
										value.append("false\"");
									}
								} else {
									value.append("false\"");
								}
								value.append("},");
							}
						} else {
							tmpId = dimId;
							value.append("{\"codeId\":\"").append(svces[i][2])
									.append("\",");
							String tmpStr = svces[i][3];
							tmpStr = tmpStr.replaceAll("\"", "");
							value.append("\"codeValue\":\"").append(tmpStr)
									.append("\",\"selected\":\"");
							// 判断是否选中
							if (null != selectedValues) {
								Object tmpObj = selectedValues.get(tmpId);
								if (null != tmpObj) {
									tmpStr = (String) tmpObj;
									int pos = tmpStr.indexOf(svces[i][2]);
									if (pos >= 0) {
										value.append("true\"");
									} else {
										value.append("false\"");
									}
								} else {
									value.append("false\"");
								}
							} else {
								value.append("false\"");
							}
							value.append("},");
						}
					}
					// 最后一个还没有放进去
					if (null != value && value.length() > 0) {
						// 放到列表中
						String tmpStr = value.toString();
						tmpStr = tmpStr.substring(0, tmpStr.length() - 1);// 去掉最后一个逗号
						tmpStr = "{\"dimName\":\""
								+ ((MsuDimStruct) dimsMap.get(tmpId))
										.getDim_name() + "\",\"dimId\":\""
								+ tmpId + "\",\"dimValues\":[" + tmpStr + "]},";
						dimsValue.add(tmpStr);
						value.delete(0, value.length());
					}
				}
			} catch (AppException ae) {
				throw new CustomMsuException("获取指标的各维度值出错");
			}
		}
		return dimsValue;
	}

	/**
	 * 由基本指标的维度列表取出维度值列表
	 *
	 * @param dims
	 *            维度对象列表
	 * @return 返回维度值的JSON列表
	 */
	public static List getBaseMsuDimsValue(List dims) throws CustomMsuException {
		List dimsValue = null;
		if (null != dims && !"".equals(dims)) {
			Map dimsMap = convertDimsListToMap(dims);
			String select = genDimsSelectSQL(dims);
			dimsValue = genDimsValueJSonString(select, dimsMap, null);
		}
		return dimsValue;
	}

	/**
	 * 获取第一级树节点
	 *
	 * @return 第一级节点
	 */
	public static List getFirLvlMsuNodes(String period)
			throws CustomMsuException {
		List nodes = null;
		nodes = getChildrenNodes(TableConsts.MSU_FIRST_LEVEL_PARENT_ID, period);
		return nodes;
	}

	/**
	 * 获取某个父亲的孩子节点
	 *
	 * @param parentId
	 *            父节点标识
	 * @return 孩子树节点列表
	 */
	public static List getChildrenNodes(String parentId, String period)
			throws CustomMsuException {
		List nodes = null;
		if (null != parentId && null != period) {
			try {
				String select = "";
				select = SQLGenator.genSQL("Q5100", "'" + parentId + "'",
						TableConsts.DATA_USED_FOR_RPT_CUSTOM,
						TableConsts.DATA_USED_FOR_GENERAL, period);
				logger.debug(select);
				String[][] svces = WebDBUtil.execQryArray(select, "");
				if (null != svces && svces.length > 0) {
					nodes = new ArrayList();
					for (int i = 0; i < svces.length; i++) {
						MsuTreeNode node = new MsuTreeNode();
						node.setIndex(i);
						node.setSrctab_id(svces[i][0]);
						node.setSrctab_name(svces[i][1]);
						node.setWidgetId(svces[i][2]);
						node.setObjectId(svces[i][0] + "|" + svces[i][7]);
						node.setTitle(svces[i][3]);
						node.setMsu_rule(svces[i][4]);
						node.setRule_desc(svces[i][5]);
						node.setMsu_desc(svces[i][6]);
						node.setPrecision(svces[i][7]);
						if (null == svces[i][8] || "".equals(svces[i][8])) {
							node.setIsFolder(false);
						} else {
							node.setIsFolder(true);
						}
						nodes.add(node);
					}
				}
			} catch (AppException ae) {
				throw new CustomMsuException("获取基本指标定义失败");
			}
		}
		return nodes;
	}

	/**
	 * 获取自定义指标的孩子树节点
	 *
	 * @param userId
	 *            用户标识
	 * @param period
	 *            统计周期
	 * @return 树孩子节点
	 * @throws CustomMsuException
	 */
	public static List genCustomChildrenNodes(String userId, String period)
			throws CustomMsuException {
		List nodes = null;
		if (null != userId && null != period) {
			try {
				String select = "";
				select = SQLGenator.genSQL("Q5155", "'" + userId + "'", period);
				String[][] svces = WebDBUtil.execQryArray(select, "");
				if (null != svces && svces.length > 0) {
					nodes = new ArrayList();
					for (int i = 0; i < svces.length; i++) {
						MsuTreeNode node = new MsuTreeNode();
						node.setIndex(i);
						node.setSrctab_id(svces[i][3]);
						node.setSrctab_name("");
						node.setWidgetId(svces[i][0]);
						node.setObjectId(svces[i][3]);
						node.setTitle(svces[i][1]);
						node.setMsu_rule("");
						node.setRule_desc("");
						node.setMsu_desc("");
						node.setPrecision(svces[i][4]);
						node.setIsFolder(false);
						nodes.add(node);
					}
				}
			} catch (AppException ae) {
				throw new CustomMsuException("获取基本指标定义失败");
			}
		}
		return nodes;
	}

	public static List getCustomChildrenNodes(String userId, String baseMsuId,
			String period) throws CustomMsuException {
		if (null == userId || "".equals(userId) || null == baseMsuId
				|| "".equals(baseMsuId) || null == period || "".equals(period))
			throw new CustomMsuException("获取某个基本指标的自定义指标时输入的参数为空");
		List nodes = null;
		try {
			String select = "";
			select = SQLGenator.genSQL("Q5165", "'" + baseMsuId + "'", "'"
					+ userId + "'", period);
			logger.debug("Q5165=" + select);
			String[][] svces = WebDBUtil.execQryArray(select, "");
			if (null != svces && svces.length > 0) {
				nodes = new ArrayList();
				for (int i = 0; i < svces.length; i++) {
					MsuTreeNode node = new MsuTreeNode();
					node.setIndex(i);
					node.setSrctab_id(svces[i][2]);
					node.setSrctab_name("");
					node.setWidgetId(svces[i][0]);
					node.setObjectId(svces[i][2] + "|" + svces[i][3]);
					node.setTitle(svces[i][1]);
					node.setMsu_rule("");
					node.setRule_desc("");
					node.setMsu_desc(svces[i][4]);
					node.setPrecision(svces[i][3]);
					node.setIsFolder(false);
					nodes.add(node);
				}
			}
		} catch (AppException ae) {
			throw new CustomMsuException("获取基本指标定义失败");
		}
		return nodes;
	}

	/**
	 * 获取自定义指标的孩子树节点
	 *
	 * @param userId
	 *            用户标识
	 * @param period
	 *            统计周期
	 * @return 树孩子节点
	 * @throws CustomMsuException
	 */
	public static List getCustomChildrenNodes(String userId, String period)
			throws CustomMsuException {
		List nodes = null;
		if (null != userId && null != period) {
			try {
				String select = "";
				select = SQLGenator.genSQL("Q5160", "'" + userId + "'", period);
				logger.debug("Q5160=" + select);
				String[][] svces = WebDBUtil.execQryArray(select, "");
				if (null != svces && svces.length > 0) {
					nodes = new ArrayList();
					for (int i = 0; i < svces.length; i++) {
						MsuTreeNode node = new MsuTreeNode();
						node.setIndex(i);
						node.setSrctab_id(period);
						node.setSrctab_name("");
						node.setWidgetId("C_" + svces[i][0]);
						node.setObjectId(period);
						node.setTitle(svces[i][1]);
						node.setMsu_rule("");
						node.setRule_desc("");
						node.setMsu_desc(svces[i][4]);
						node.setPrecision(svces[i][3]);
						node.setIsFolder(true);
						nodes.add(node);
					}
				}
			} catch (AppException ae) {
				throw new CustomMsuException("获取基本指标定义失败");
			}
		}
		return nodes;
	}

	/**
	 * 将树节点转换成JSon模式
	 *
	 * @param nodes
	 *            树节点列表
	 * @return JSON 字符串
	 */
	public static String convertTreeNodesToJSon(List nodes) {
		String treeNodes = null;
		if (null != nodes && nodes.size() > 0) {
			JSONArray jsonObj = JSONArray.fromObject(nodes);
			treeNodes = jsonObj.toString();
		}
		return treeNodes;
	}

	/**
	 * 将JSon字符串转换成单个树节点
	 *
	 * @param node
	 *            JSon字符串
	 * @return 树节点
	 */
	public static MsuTreeNode convertJSonToTreeNode(String node) {
		MsuTreeNode ret = null;
		if (null != node) {
			// 由于json库不进行层次解析，自己做吧
			JSONObject jsonObject = JSONObject.fromObject(node);
			ret = (MsuTreeNode) JSONObject.toBean(
					jsonObject.getJSONObject("node"), MsuTreeNode.class);
		}
		return ret;
	}

	/**
	 * 将树节点组装成dojo识别的div模式
	 *
	 * @param nodes
	 *            树节点列表
	 * @return 转换后的 HTML字符串
	 */
	public static String assembleTreeNodes(List nodes, boolean addDesc) {
		String treeNodes = null;
		if (null != nodes) {
			StringBuffer sb = new StringBuffer("");
			Iterator iter = nodes.iterator();
			while (iter.hasNext()) {
				MsuTreeNode msuObj = (MsuTreeNode) iter.next();
				sb.append(genTreeNodeHtml(msuObj, addDesc));
			}
			treeNodes = sb.toString();
		}
		return treeNodes;
	}

	/**
	 * 生成单个树节点HTML
	 *
	 * @param node
	 *            树节点对象
	 * @return HTML字符串
	 */
	private static String genTreeNodeHtml(MsuTreeNode node, boolean addDesc) {
		String ret = null;
		if (null != node) {
			StringBuffer sb = new StringBuffer("");
			sb.append("<div dojoType=\"TreeNode\" ");
			sb.append("title=\"").append(node.getTitle());
			if (addDesc)
				sb.append("-" + node.getMsu_desc());
			sb.append("\" ");
			sb.append("widgetId=\"").append(node.getWidgetId()).append("\" ");
			sb.append("objectId=\"")
					.append(node.getSrctab_id() + "|" + node.getPrecision())
					.append("\" ");
			sb.append("stat_period=\"4\" ");
			sb.append("isFolder=\"").append(node.isIsFolder())
					.append("\"></div>");
			ret = sb.toString();
		}
		return ret;
	}

	/**
	 * 保存自定义指标
	 *
	 * @param sqlAry
	 *            保存指标的语句数组
	 * @throws CustomMsuException
	 */
	public static void saveCustomMsu(String userId, String msu_name,
			String base_msu, String srctab_id, String stat_period,
			HttpServletRequest request) throws CustomMsuException {
		String sql = CustomMsuUtil.genCustomMsuSQL(userId, msu_name, base_msu,
				srctab_id, stat_period);
		List insert = new java.util.ArrayList();
		insert.add(sql);
		// 获取用户选择的维度
		List dims = CustomMsuUtil.getBaseMsuDims(base_msu, srctab_id);
		if (null != dims) {
			Iterator iter = dims.iterator();
			while (iter.hasNext()) {
				MsuDimStruct dim = (MsuDimStruct) iter.next();
				String[] values = request.getParameterValues(dim.getDim_id());
				List tmpSQL = CustomMsuUtil.genMsuDimSQL(null, dim.getDim_id(),
						dim.getDim_level(), values);
				if (null != tmpSQL)
					insert.addAll(tmpSQL);
			}
		}
		try {
			String[] insrtSQL = (String[]) insert.toArray(new String[insert
					.size()]);
			executeBatchSQL(insrtSQL);
		} catch (CustomMsuException cme) {
			throw new CustomMsuException("保存自定义指标是失败", cme);
		}

	}
}
