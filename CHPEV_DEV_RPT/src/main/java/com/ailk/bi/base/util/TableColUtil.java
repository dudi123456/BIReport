package com.ailk.bi.base.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.ailk.bi.base.common.*;
import com.ailk.bi.base.table.UiCommonDimhierarchyTable;
import com.ailk.bi.base.table.UiCommonMsucalTable;
import com.ailk.bi.base.table.UiCommonRtpheadTable;
import com.ailk.bi.base.table.UiCommonTableDefTable;
import com.ailk.bi.base.table.UiCommonTabledictTable;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

/**
 * 用于生成表格的列对象工具和其他辅助操作
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TableColUtil {

	/**
	 * 获取表格的所有显示列定义和计算公式
	 *
	 * @param reportID
	 *            表格标识
	 * @return 返回列对象列表
	 */
	public static List getColObjs(String reportID) {
		List cols = null;
		if (null != reportID) {
			try {
				// 查询所有列定义，包括计算用的字段
				String sql = SQLGenator.genSQL("Q6200", reportID);
				String[][] svces = WebDBUtil.execQryArray(sql, "");
				if (null != svces) {
					cols = new ArrayList();
					for (int i = 0; i < svces.length; i++) {
						UiCommonTabledictTable colObj = new UiCommonTabledictTable();
						colObj.table_id = svces[i][0];
						colObj.col_id = svces[i][1];
						colObj.col_name = svces[i][2];
						colObj.col_sequence = svces[i][3];
						colObj.code_field = svces[i][4];
						colObj.code_field_clone = svces[i][4];
						colObj.desc_field = svces[i][5];
						colObj.is_display = svces[i][6];
						colObj.is_calByOther = svces[i][7];
						colObj.data_type = svces[i][8];
						if (null == colObj.data_type
								|| "".equals(colObj.data_type)) {
							colObj.data_type = ConstantKeys.DATA_TYPE_STRING;
						}
						colObj.digit_length = svces[i][9];
						colObj.is_measure = svces[i][10];
						colObj.isExpandCol = svces[i][11];
						colObj.defalut_drilled = svces[i][12];
						colObj.init_level = svces[i][13];
						colObj.colsrch_where = svces[i][14];
						colObj.has_link = svces[i][15];
						colObj.link_url = svces[i][16];
						colObj.link_target = svces[i][17];
						colObj.has_last = svces[i][18];
						colObj.last_field = svces[i][19];
						colObj.last_display = svces[i][20];
						colObj.rise_arrow_color = svces[i][21];
						colObj.has_last_link = svces[i][22];
						colObj.last_url = svces[i][23];
						colObj.last_target = svces[i][24];
						colObj.has_loop = svces[i][25];
						colObj.loop_field = svces[i][26];
						colObj.loop_display = svces[i][27];
						colObj.has_loop_link = svces[i][28];
						colObj.loop_url = svces[i][29];
						colObj.loop_target = svces[i][30];
						colObj.has_comratio = svces[i][31];
						colObj.is_colclk = svces[i][32];
						colObj.col_chart_ids = svces[i][33];
						colObj.is_cellclk = svces[i][34];
						colObj.cell_chart_ids = svces[i][35];
						colObj.status = svces[i][36];
						// 判断是否有计算公式，如果有，设置
						if (null != svces[i][37]
								&& !"".equalsIgnoreCase(svces[i][37])) {
							UiCommonMsucalTable calObj = new UiCommonMsucalTable();
							calObj.table_id = svces[i][37];
							calObj.col_id = svces[i][38];
							calObj.cal_cmd = svces[i][39];
							calObj.cal_memo = svces[i][40];
							calObj.zero_null = svces[i][41];
							calObj.rsv_fld1 = svces[i][42];
							calObj.rsv_fld2 = svces[i][43];
							calObj.rsv_fld3 = svces[i][44];
							colObj.calObj = calObj;
						}
						colObj.col_desc = svces[i][45];
						colObj.is_ratio = svces[i][46];
						colObj.blank_num = svces[i][47];
						colObj.is_sumdisplay = svces[i][48];
						colObj.col_rlt_chart_target = svces[i][49];
						// 如果是默认就展开的列
						if (ConstantKeys.YES
								.equalsIgnoreCase(colObj.defalut_drilled)
								&& ConstantKeys.YES
										.equalsIgnoreCase(colObj.isExpandCol)) {
							// 将从没有钻取属性设置为FALSE
							colObj.neverDrilled = false;
						} else if (ConstantKeys.YES
								.equalsIgnoreCase(colObj.isExpandCol)) {
							// 如果是需要展开列，设置为TRUE
							colObj.neverDrilled = true;
						} else {
							// 如果不需要展开，设置为FAlSE
							colObj.neverDrilled = false;
						}
						// 如果是默认就展开列
						if (ConstantKeys.YES
								.equalsIgnoreCase(colObj.defalut_drilled)) {
							// 设置初始层次为定义
							colObj.level = colObj.init_level;
						} else {
							// 否则设置为0
							colObj.level = ConstantKeys.ZERO;
						}
						cols.add(colObj);
					}
				}
			} catch (AppException ae) {
				ae.printStackTrace();
				cols = null;
			}
		}
		return cols;
	}

	/**
	 * 获取表格的所有的列层次定义对象
	 *
	 * @param tableID
	 *            表格标识
	 * @return 以列标识为KEY，层次对象列表为值的MAP 集合
	 */
	public static Map getColsLevels(String tableID) {
		Map colsLevels = null;
		if (null != tableID) {
			try {
				// 层次查询
				String sql = SQLGenator.genSQL("Q6300", tableID);
				String[][] svces = WebDBUtil.execQryArray(sql, "");
				if (null != svces) {
					colsLevels = new HashMap();
					List levels = new ArrayList();
					String key = "";
					for (int i = 0; i < svces.length; i++) {
						UiCommonDimhierarchyTable levObj = new UiCommonDimhierarchyTable();
						levObj.table_id = svces[i][0];
						levObj.col_id = svces[i][1];
						levObj.lev_id = svces[i][2];
						levObj.lev_name = svces[i][3];
						levObj.lev_memo = svces[i][4];
						levObj.src_idfld = svces[i][5];
						levObj.idfld_type = svces[i][6];
						levObj.src_namefld = svces[i][7];
						levObj.dimsrch_where = svces[i][8];
						levObj.is_valid = svces[i][9];
						levObj.rsv_fld1 = svces[i][10];
						levObj.rsv_fld2 = svces[i][11];
						levObj.rsv_fld3 = svces[i][12];
						if (!key.equalsIgnoreCase(levObj.col_id)) {
							if (!"".equalsIgnoreCase(key)) {
								colsLevels.put(key, levels);
								levels = new ArrayList();
							}
							levels.clear();
							levels.add(levObj);
							key = levObj.col_id;
						} else {
							levels.add(levObj);
						}
					}
					colsLevels.put(key, levels);
				}
			} catch (AppException ae) {
				ae.printStackTrace();
				colsLevels = null;
			}
		}
		return colsLevels;
	}

	/**
	 * 根据当前层次获取当前层次的对象
	 *
	 * @param levels
	 *            层次对象集合
	 * @param curLevel
	 *            当前层次
	 * @return 当前层次对象
	 */
	public static UiCommonDimhierarchyTable getCurLevelObj(List levels,
			String curLevel) {
		UiCommonDimhierarchyTable levObj = null;
		if (null != levels && null != curLevel) {
			Iterator iter = levels.iterator();
			while (iter.hasNext()) {
				UiCommonDimhierarchyTable tmpLevObj = (UiCommonDimhierarchyTable) iter
						.next();
				if (tmpLevObj.lev_id.equals(curLevel)) {
					levObj = tmpLevObj;
					break;
				}
			}
		}
		return levObj;
	}

	/**
	 * 根据当前层次获取下一层次的对象
	 *
	 * @param levels
	 *            层次对象集合
	 * @param curLevel
	 *            当前层次
	 * @return 下一层次对象
	 */
	public static UiCommonDimhierarchyTable getNextLevelObj(List levels,
			String curLevel) {
		UiCommonDimhierarchyTable levObj = null;
		if (null != levels) {
			int index = -1;
			int curLev = -1;
			Iterator iter = levels.iterator();
			while (iter.hasNext()) {
				index++;
				UiCommonDimhierarchyTable tmpLevObj = (UiCommonDimhierarchyTable) iter
						.next();
				if (null == curLevel || "".equals(curLevel)
						|| ConstantKeys.ZERO.equals(curLevel)) {
					// 如果当前层次为空、或者为零则返回第一层
					levObj = tmpLevObj;
					break;
				} else if (tmpLevObj.lev_id.equals(curLevel)) {
					// 找到当前层
					curLev = index;
				}
				if (curLev != -1 && (curLev + 1) == index) {
					// 找到下一层
					levObj = tmpLevObj;
					break;
				}
			}
		}
		return levObj;
	}

	/**
	 * 根据当前层次获取上一层次层次的对象
	 *
	 * @param levels
	 *            所有层次集合
	 * @param curLevel
	 *            当前层
	 * @return 上一层对象
	 */
	public static UiCommonDimhierarchyTable getPreLevelObj(List levels,
			String curLevel) {
		UiCommonDimhierarchyTable levObj = null;
		if (null != levels && null != curLevel) {
			int cur_index = -1;
			Object[] objs = levels.toArray();
			// 从最底层开始找起
			for (int i = objs.length - 1; i >= 0; i--) {
				UiCommonDimhierarchyTable tmpLevObj = (UiCommonDimhierarchyTable) objs[i];
				if (tmpLevObj.lev_id.equals(curLevel)) {
					// 定位到当前层
					cur_index = i - 1;
				}
				if (cur_index != -1 && cur_index == i) {
					// 得到上一层
					levObj = tmpLevObj;
					break;
				}
			}
		}
		return levObj;
	}

	/**
	 * 获取表格的定义
	 *
	 * @param reportID
	 *            表格标识
	 * @return 表格对象,如果有，带有表头
	 */
	public static UiCommonTableDefTable getTabObj(String reportID) {
		UiCommonTableDefTable tabObj = null;
		if (null != reportID) {
			try {
				// 查询表格
				String sql = SQLGenator.genSQL("Q6100", reportID);
				String[][] svces = WebDBUtil.execQryArray(sql, "");
				if (null != svces) {
					for (int i = 0; i < svces.length; i++) {
						tabObj = new UiCommonTableDefTable();
						tabObj.table_id = svces[i][0];
						tabObj.obj_type_id = svces[i][1];
						tabObj.table_desc = svces[i][2];
						tabObj.data_table = svces[i][3];
						tabObj.sql_where = svces[i][4];
						tabObj.sql_order = svces[i][5];
						tabObj.isexpand = svces[i][6];
						tabObj.time_field = svces[i][7];
						tabObj.field_type = svces[i][8];
						tabObj.time_level = svces[i][9];
						tabObj.cal_ratio = svces[i][10];
						tabObj.is_rowclk = svces[i][11];
						tabObj.chart_ids = svces[i][12];
						tabObj.has_head = svces[i][13];
						// 如果有表头对象
						if (null != svces[i][14] && !"".equals(svces[i][14])) {
							UiCommonRtpheadTable tabHead = new UiCommonRtpheadTable();
							tabHead.table_id = svces[i][14];
							tabHead.table_header = svces[i][15];
							tabObj.tabHead = tabHead;
						}
						tabObj.hasSubpage = svces[i][16];
						tabObj.subpage_id = svces[i][17];
						tabObj.subpage_url = svces[i][18];
					}
				}
			} catch (AppException ae) {
				ae.printStackTrace();
				tabObj = null;
			}
		}
		return tabObj;
	}

	/**
	 * 将计算公式转换成实际字段的计算 注意：计算公式必须是合法的公式，如括号必须成对。 每个计算因子使用MSU123形式，123表示指标的标识
	 * 除MSU、数字、 ＋、－、*、/、（）外， 不应出现其他符号 所有符号必须是英文字符，不能使用中文字符
	 *
	 * @param colObj
	 *            列对象
	 * @return 替换成::msu::形式的公式
	 */
	public static UiCommonTabledictTable parseCmd(UiCommonTabledictTable colObj) {
		UiCommonTabledictTable retColObj = null;
		retColObj = colObj;
		// 将计算公式转换成实际字段的计算，
		if (null != colObj && null != colObj.calObj
				&& null != colObj.calObj.cal_cmd) {
			String sqlStr = "(";
			String cmd = retColObj.calObj.cal_cmd.toLowerCase();
			List calMsus = new ArrayList();
			String fnd = "msu";
			int pos = 0;
			int begin = 0;
			while (pos < cmd.length()) {
				pos = cmd.indexOf(fnd, pos);
				if (pos >= 0) {
					// 先把前面的东西存到里面去
					sqlStr = sqlStr + cmd.substring(begin, pos);
					// 向前移动三个字符
					pos = pos + 3;
					boolean over = false;
					String tmpStr = "";
					// 获取因子
					String tmp = "";
					while (pos < cmd.length() && !over) {
						tmp = cmd.substring(pos, pos + 1);
						if (!tmp.trim().equalsIgnoreCase("+")
								&& !tmp.trim().equalsIgnoreCase("-")
								&& !tmp.trim().equalsIgnoreCase("*")
								&& !tmp.trim().equalsIgnoreCase("/")
								&& !tmp.trim().equalsIgnoreCase("(")
								&& !tmp.trim().equalsIgnoreCase(")"))
							tmpStr = tmpStr + tmp;
						else
							over = true;
						pos++;
					}
					if (pos == cmd.length() && !over) {
						tmp = tmp.substring(0, tmp.length() - 1);
					}
					begin = pos;
					calMsus.add(tmpStr);
					// 加入左右大括号，以便后来替换为SUM( 和 )
					sqlStr = sqlStr + "::msu" + tmpStr + "::" + tmp;
				} else {
					// 没有指标了
					sqlStr = sqlStr + cmd.substring(begin);
					break;
				}
			}
			sqlStr = sqlStr + ")";
			retColObj.calObj.cal_cmd = sqlStr;
			retColObj.msuFactors = calMsus;
		}
		return retColObj;
	}

	/**
	 * 处理计算公式中分母可能为0的情况 case when id=0 then null else id end
	 *
	 * @param cmd
	 *            计算公式
	 * @return 返回处理分母为零情况下的公式
	 */
	public static String processDenoZero(String cmd) {
		StringBuffer retCMD = null;
		if (null != cmd) {
			int begin = -1;
			int found = 0;
			String rest = "";
			begin = cmd.indexOf("/", begin);
			if (begin >= 0 && cmd.indexOf("::msu", begin) >= 0) {
				retCMD = new StringBuffer("");
				while (begin >= 0 && cmd.indexOf("::msu", begin) >= 0) {
					retCMD.append(cmd.substring(found, begin + 1));
					rest = cmd.substring(begin + 1, cmd.length());
					char tmpChar = rest.charAt(0);
					if (tmpChar == '(') {
						// 可能有多个因子,分母中含有括号啊，真复杂，这里不考虑公式非法情况，如括号不匹配
						// 使用堆栈吧，先将最先这个压入，当最后弹出时，也就是这个分母完了
						Stack stack = new Stack();
						stack.push("(");
						int pos = 1;// 已经压入了一个
						String denoStr = "";
						@SuppressWarnings("unused")
						int count = 0;
						while (pos < rest.length() && !stack.isEmpty()) {
							String tmp = rest.substring(pos, pos + 1);
							if (!tmp.equalsIgnoreCase(")"))
								// 不是右括号，就压入
								stack.push(tmp);
							else {
								// 开始出栈知道遇到一个左括号
								String str = "";
								String tmpStr = (String) stack.pop();

								while (!tmpStr.equalsIgnoreCase("(")
										&& !stack.isEmpty()) {
									str = tmpStr + str;
									tmpStr = (String) stack.pop();
								}
								denoStr += str;
								denoStr = "(" + denoStr + ")";
								count++;
							}
							pos++;
						}
						found = begin + pos + 1;
						rest = rest.substring(pos, rest.length());
						denoStr = "(CASE WHEN " + denoStr
								+ "=0 THEN NULL ELSE " + denoStr + " END)";
						retCMD.append(denoStr);

					} else {
						// 就一个因子
						String tmpStr = "";
						String tmp;
						int pos = 0;
						boolean over = false;
						while (pos < rest.length() && !over) {
							tmp = rest.substring(pos, pos + 1);
							if (!tmp.trim().equalsIgnoreCase("+")
									&& !tmp.trim().equalsIgnoreCase("-")
									&& !tmp.trim().equalsIgnoreCase("*")
									&& !tmp.trim().equalsIgnoreCase("/")
									&& !tmp.trim().equalsIgnoreCase("(")
									&& !tmp.trim().equalsIgnoreCase(")"))
								tmpStr = tmpStr + tmp;
							else
								over = true;
							pos++;
						}
						found = begin + pos + 1;
						String denoStr = "(CASE WHEN " + tmpStr
								+ "=0 THEN NULL ELSE " + tmpStr + " END)";
						retCMD.append(denoStr);
						rest = rest.substring(pos - 1, rest.length());
					}

					begin = cmd.indexOf("/", found);
				}
				retCMD.append(rest);
			} else
				retCMD = new StringBuffer(cmd);
		}
		return retCMD.toString();
	}

	/**
	 * 对列表的内容进行排序
	 *
	 * @param index
	 *            要排序的列下标
	 * @param order
	 *            排序顺序
	 * @param dimsCount
	 *            维度数量
	 * @param content
	 *            内容列表
	 * @return 排序后的内容
	 */
	public static List sortContent(int index, String order, String dataType,
			int dimsCount, List content) {
		List ret = null;
		if (Integer.MIN_VALUE != index && null != order && null != content) {
			if (index > dimsCount) {
				// 指标数据进行排序，按数值进行排序
				Collections.sort(content, new NumberComparator(index, order));
				ret = content;
			} else {
				// 维度数据进行排序，这里可能要判断是否进行数值或字符排序，北分目前都是字符型
				if (ConstantKeys.DATA_TYPE_NUMBER.equalsIgnoreCase(dataType)) {
					Collections.sort(content,
							new NumberComparator(index, order));
					ret = content;
				} else {
					// 按字符型排序
					Collections.sort(content, new AlphabeticComparator(index,
							order));
					ret = content;
				}
			}
		}
		return ret;
	}
}
