package com.ailk.bi.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.ReflectUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.report.domain.RptColDictTable;

/**
 * 对数组和字符串操作
 *
 * @author renhui
 *
 */
/**
 * @author Administrator
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StringTool {

	/**
	 * 检查字符串是否为空。空返回true,非空返回false
	 *
	 * @param ret
	 * @return
	 */
	public static boolean checkEmptyString(String ret) {
		boolean isTrue = false;
		if (ret == null || "".equals(ret.trim())) {
			isTrue = true;
		}
		return isTrue;
	}

	public static String nullStringToEmp(String ret) {
		if (checkEmptyString(ret)) {
			return "";
		}
		return ret;
	}

	/**
	 * 功能描述:将字符串数组练成字符串(不去除重复数据用逗号分隔)
	 *
	 * @param arr
	 *            字符串数组
	 * @return 返回值 :String 字符串
	 */
	public static String changArrToStr(String[] arr) {
		String ret = "";
		for (int i = 0; arr != null && i < arr.length; i++) {
			if (ret.length() > 0)
				ret += ",";
			ret += arr[i].trim();
		}
		return ret;
	}

	/**
	 * 将计算公式转换成实际字段的计算 注意：计算公式必须是合法的公式，如括号必须成对。 每个计算因子使用MSU123形式，123表示指标的标识
	 * 除MSU、数字、 ＋、－、*、/、（）外，不应出现其他符号 所有符号必须是英文字符，不能使用中文字符
	 *
	 * @param rptMsu
	 * @param fnd
	 * @param fndLen
	 * @param p0
	 * @return
	 */
	public static String parseCmdNew(String rptMsu, String fnd, int fndLen,
			String[][] tmpArr, HashMap colMap) {
		// 将计算公式转换成实际字段的计算，
		if (null != rptMsu && !"".equals(rptMsu)) {

			String sqlStr = "";
			String cmd = rptMsu.toUpperCase();
			if (cmd.indexOf("NVL(") >= 0) {
				cmd = StringB.replace(cmd, "NVL(", "");
				cmd = StringB.replace(cmd, ",0)", "");
			}
			if (cmd.indexOf("COALESCE(") >= 0) {
				cmd = StringB.replace(cmd, "COALESCE(", "");
				cmd = StringB.replace(cmd, ",0)", "");
			}
			fnd = fnd.toUpperCase();

			int pos = 0;
			int begin = 0;

			while (pos < cmd.length()) {
				pos = cmd.indexOf(fnd, pos);

				if (pos >= 0) {
					// 先把前面的东西存到里面去
					sqlStr = sqlStr + cmd.substring(begin, pos);

					// 向前移动fndLen个字符
					pos = pos + fndLen;
					boolean over = false;
					String tmpStr = "";
					// 获取因子
					String tmp = "";
					while (pos < cmd.length() && !over) {
						// tmp = cmd.substring(pos, pos + 1);
						if (pos < cmd.length()
								&& Character.isDigit(cmd.charAt(pos))) {
							if ((pos + 1) < cmd.length()
									&& Character.isDigit(cmd.charAt(pos + 1))) {
								tmp = cmd.substring(pos, pos + 2);
								pos = pos + 2;
							} else {
								tmp = cmd.substring(pos, pos + 1);
								pos++;
							}
						} else {
							tmp = cmd.substring(pos, pos + 1);
							pos++;
						}
						if (!tmp.trim().equalsIgnoreCase("+")
								&& !tmp.trim().equalsIgnoreCase("-")
								&& !tmp.trim().equalsIgnoreCase("*")
								&& !tmp.trim().equalsIgnoreCase("/")
								&& !tmp.trim().equalsIgnoreCase("(")
								&& !tmp.trim().equalsIgnoreCase(")")) {
							tmpStr = tmpStr + tmp;

						} else
							over = true;
						// pos++;
					}
					//
					if (pos == cmd.length() && !over) {
						tmp = tmp.substring(0, tmp.length() - 1);
					}
					//
					/*
					 * for (int i = 0; tmpArr != null && i < tmpArr.length; i++)
					 * { if (tmpStr.equals(tmpArr[i][0])) { tmpStr =
					 * tmpArr[i][1]; break; } }
					 */
					tmpStr = (String) colMap.get(tmpStr);

					begin = pos;
					// 加入左右大括号，以便后来替换为SUM( 和 )
					if (!tmp.trim().equalsIgnoreCase("+")
							&& !tmp.trim().equalsIgnoreCase("-")
							&& !tmp.trim().equalsIgnoreCase("*")
							&& !tmp.trim().equalsIgnoreCase("/")
							&& !tmp.trim().equalsIgnoreCase("(")
							&& !tmp.trim().equalsIgnoreCase(")")) {
						sqlStr += "COALESCE(SUM(A." + fnd + tmpStr + "),0)";
					} else {
						sqlStr += "COALESCE(SUM(A." + fnd + tmpStr + "),0)"
								+ tmp;
					}
				} else {
					sqlStr += cmd.substring(begin);
					break;
				}
			}
			if (sqlStr.indexOf("/") >= 0) {
				String tmps = sqlStr.substring(sqlStr.indexOf("/") + 1,
						sqlStr.length());
				sqlStr = sqlStr.substring(0, sqlStr.indexOf("/"))
						+ "/(CASE WHEN ";
				sqlStr += tmps + "=0 THEN NULL ELSE " + tmps + " END)";
			}
			// rptMsu = sqlStr + " AS " + fnd + p0;
			rptMsu = sqlStr;
		}
		return rptMsu;
	}

	/**
	 * 功能描述:将字符串数组练成字符串(不去除重复数据用\n分隔)
	 *
	 * @param arr
	 *            字符串数组
	 * @return 返回值 :String 字符串
	 */
	public static String changArrToStrWithLn(String[] arr) {
		String ret = "";
		for (int i = 0; arr != null && i < arr.length; i++) {
			if (ret.length() > 0)
				ret += "\n";
			ret += arr[i].trim();
		}
		return ret;
	}

	/**
	 * 功能描述:将字符串数组前后添加特定字符练成字符串(不去除重复数据用逗号分隔)
	 *
	 * @param arr
	 *            字符串数组
	 * @param addStr
	 *            添加指定的字符
	 * @return 返回值 :String 字符串
	 */
	public static String changArrToStr(String[] arr, String addStr) {
		String ret = "";
		for (int i = 0; arr != null && i < arr.length; i++) {
			if (ret.length() > 0)
				ret += ",";
			ret += addStr + arr[i].trim() + addStr;
		}
		return ret;
	}

	/**
	 * 功能描述:将二纬字符串数组某列练成字符串(去除重复数据用逗号分隔)
	 *
	 * @param arr
	 *            字符串数组
	 * @param iIndex
	 *            需要转换的列的索引
	 * @return 返回值 :String 字符串
	 */
	public static String changArrToStr(String[][] arr, int iIndex) {
		String ret = ",";
		for (int i = 0; arr != null && i < arr.length; i++) {
			if (ret.indexOf("," + arr[i][iIndex] + ",") < 0)
				ret += arr[i][iIndex] + ",";
		}
		if (!"".equals(ret) && ret.length() > 1)
			ret = StringB.trim(ret, ",");
		return ret;
	}

	/**
	 * 功能描述:将二纬字符串数组某列练成字符串(去除重复数据用逗号分隔，并用单引号括住)，
	 *
	 * @param arr
	 *            字符串数组
	 * @param iIndex
	 *            需要转换的列的索引
	 * @return 返回值 :String 字符串
	 */
	public static String changArrToStrComma(String[][] arr, int iIndex) {
		String ret = ",'";
		for (int i = 0; arr != null && i < arr.length; i++) {
			if (ret.indexOf(",'" + arr[i][iIndex] + "',") < 0)
				ret += arr[i][iIndex] + "','";
		}
		if (!"".equals(ret) && ret.length() > 2) {
			ret = ret.substring(1, ret.length() - 2);
		}
		return ret;
	}

	/**
	 * 功能描述:将二纬字符串数组某列练成字符串(不去除重复数据用逗号分隔)
	 *
	 * @param arr
	 *            字符串数组
	 * @param iIndex
	 *            需要转换的列的索引
	 * @return 返回值 :String 字符串
	 */
	public static String changArrToStrAll(String[][] arr, int iIndex) {
		String ret = "";
		for (int i = 0; arr != null && i < arr.length; i++) {
			if (ret.length() > 0)
				ret += ",";
			ret += arr[i][iIndex];
		}
		return ret;
	}

	/**
	 * 功能描述:将二纬字符串数组某列练成字符串(不去除重复数据用逗号分隔)
	 *
	 * @param arr
	 *            对象数组
	 * @param p1
	 *            需要转换的列的名称
	 * @return 返回值 :String 字符串
	 */
	public static String changArrToStrAll(Object[] arr, String p1) {
		String ret = "";
		for (int i = 0; arr != null && i < arr.length; i++) {
			if (ret.length() > 0)
				ret += ",";
			ret += ReflectUtil.getStringFromObj(arr[i], p1);
		}
		return ret;
	}

	/**
	 * 功能描述:将二纬字符串数组某列练成字符串(除重复数据用逗号分隔)
	 *
	 * @param arr
	 *            对象数组
	 * @param p1
	 *            需要转换的列的名称
	 * @return 返回值 :String 字符串
	 */
	public static String changArrToStrComma(Object[] arr, String p1) {
		String ret = ",'";
		for (int i = 0; arr != null && i < arr.length; i++) {
			String tmp = ReflectUtil.getStringFromObj(arr[i], p1);
			if (ret.indexOf(",'" + tmp + "',") < 0)
				ret += tmp + "','";
		}
		if (!"".equals(ret) && ret.length() > 2) {
			ret = ret.substring(1, ret.length() - 2);
		}
		return ret;
	}

	/**
	 * 功能描述:将二纬字符串数组转成下拉列表字符串
	 *
	 * @param arr
	 *            字符串数组
	 * @return 返回值 :String 字符串
	 */
	public static String changArrsToStrAll(String[][] arr) {
		String ret = "";
		for (int i = 0; arr != null && i < arr.length; i++) {
			ret += arr[i][0] + "," + arr[i][1] + ";";
		}
		if (ret.length() > 1)
			ret = ret.substring(0, ret.length() - 1);
		return ret;
	}

	/**
	 * 获取字符串指定参数值
	 *
	 * @param str
	 * @param splitChar
	 * @param findParm
	 * @return
	 */
	public static String findStrValue(String str, String splitChar,
			String findParm) {
		if (str == null || "".equals(str)) {
			return "";
		}
		String ret = "";
		String[] arr1 = str.split(splitChar);
		for (int i = 0; arr1 != null && i < arr1.length; i++) {
			String[] arr2 = arr1[i].split("=");
			if (arr2 != null && arr2.length > 1 && findParm.equals(arr2[0])) {
				ret = arr2[1];
			}
		}
		return ret;
	}

	/**
	 * 获得日期的中文描述
	 *
	 * @param n
	 *            负向前，正向后
	 * @return
	 */
	public static String getCNPrivousDate(int n) {
		String sDate = DateUtil.getDiffDay(n, DateUtil.getNowDate());
		String resCN = sDate.substring(0, 4) + "年" + sDate.substring(4, 6)
				+ "月" + sDate.substring(6, 8) + "日";
		return resCN;
	}

	/**
	 * 获得月份的中文描述
	 *
	 * @param n
	 *            负向前，正向后
	 * @return
	 */
	public static String getPrivousMonthCn(int n) {
		String sMonth = DateUtil.getDiffMonth(n, DateUtil.getNowDate());
		String resCN = sMonth.substring(0, 4) + "年" + sMonth.substring(4, 6)
				+ "月";
		return resCN;
	}

	/**
	 * 获得日期的中文描述
	 *
	 * @param sDate
	 *            日期值
	 * @param cycle
	 *            日期周期
	 * @return
	 */
	public static String getCnDate(String sDate, String cycle) {
		if (sDate == null || "".equals(sDate))
			return "";

		String cnDate = sDate;
		if ("4".equals(cycle)) {
			// 月
			if (sDate.length() != 6)
				return sDate;

			cnDate = sDate.substring(0, 4) + "年" + sDate.substring(4, 6) + "月";

		} else if ("6".equals(cycle)) {
			// 日
			if (sDate.length() != 8)
				return sDate;

			cnDate = sDate.substring(0, 4) + "年" + sDate.substring(4, 6) + "月"
					+ sDate.substring(6, 8) + "日";
		}

		return cnDate;
	}

	public static HashMap colDefMap(List rptColTable) {

		int dataStrLen = 8;
		String dataStr = "DATA_NUM";

		HashMap map = new HashMap();
		for (int i = 0; rptColTable != null && i < rptColTable.size(); i++) {
			RptColDictTable dict = (RptColDictTable) rptColTable.get(i);
			String fieldCode = dict.field_code.trim();
			String colSeq = dict.col_sequence.trim();
			int pos = 0;
			boolean flag = false;
			if (fieldCode.indexOf("+") >= 0 || fieldCode.indexOf("-") >= 0
					|| fieldCode.indexOf("*") >= 0
					|| fieldCode.indexOf("/") >= 0) {
				flag = true;
			}
			while (pos < fieldCode.length()) {
				pos = fieldCode.indexOf(dataStr, pos);

				if (pos >= 0) {

					// 向前移动dataStrLen个字符
					pos = pos + dataStrLen;
					boolean over = false;
					// 获取因子
					String tmp = "";
					int sc = 0;
					while (pos < fieldCode.length() && !over) {
						if (pos < fieldCode.length()
								&& Character.isDigit(fieldCode.charAt(pos))) {
							if ((pos + 1) < fieldCode.length()
									&& Character.isDigit(fieldCode
											.charAt(pos + 1))) {
								tmp = fieldCode.substring(pos, pos + 2);
								pos = pos + 2;
							} else {
								tmp = fieldCode.substring(pos, pos + 1);
								pos++;
							}
						} else {
							tmp = fieldCode.substring(pos, pos + 1);
							pos++;
						}

						if (tmp.trim().equalsIgnoreCase("+")
								|| tmp.trim().equalsIgnoreCase("-")
								|| tmp.trim().equalsIgnoreCase("*")
								|| tmp.trim().equalsIgnoreCase("/")
								|| tmp.trim().equalsIgnoreCase("(")
								|| tmp.trim().equalsIgnoreCase(")")) {
							over = true;
							continue;
						}

						if (flag) {
							if (map.get(tmp) == null) {
								sc++;
								map.put(tmp, colSeq + sc);
							}
						} else {
							if (map.get(tmp) == null) {
								map.put(tmp, colSeq);
							}
						}
					}

				} else {
					break;
				}
			}
		}
		return map;
	}

	/**
	 * 将计算公式转换成实际字段的计算 注意：计算公式必须是合法的公式，如括号必须成对。 每个计算因子使用MSU123形式，123表示指标的标识
	 * 除MSU、数字、 ＋、－、*、/、（）外，不应出现其他符号 所有符号必须是英文字符，不能使用中文字符
	 *
	 * @param rptMsu
	 * @param fnd
	 * @param fndLen
	 * @param p0
	 * @return
	 */
	public static String parseCmd(String rptMsu, String fnd, int fndLen, int p0) {
		// 将计算公式转换成实际字段的计算，
		if (null != rptMsu && !"".equals(rptMsu)) {
			String sqlStr = "";
			String cmd = rptMsu.toUpperCase();
			if (cmd.indexOf("NVL(") >= 0) {
				cmd = StringB.replace(cmd, "NVL(", "");
				cmd = StringB.replace(cmd, ",0)", "");
			}
			if (cmd.indexOf("COALESCE(") >= 0) {
				cmd = StringB.replace(cmd, "COALESCE(", "");
				cmd = StringB.replace(cmd, ",0)", "");
			}
			fnd = fnd.toUpperCase();

			int pos = 0;
			int begin = 0;
			while (pos < cmd.length()) {
				pos = cmd.indexOf(fnd, pos);
				if (pos >= 0) {
					// 先把前面的东西存到里面去
					sqlStr = sqlStr + cmd.substring(begin, pos);
					// 向前移动fndLen个字符
					pos = pos + fndLen;
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
					// 加入左右大括号，以便后来替换为SUM( 和 )
					sqlStr += "COALESCE(SUM(" + fnd + tmpStr + "),0)" + tmp;
				} else {
					sqlStr += cmd.substring(begin);
					break;
				}
			}
			if (sqlStr.indexOf("/") >= 0) {
				String tmps = sqlStr.substring(sqlStr.indexOf("/") + 1,
						sqlStr.length());
				sqlStr = sqlStr.substring(0, sqlStr.indexOf("/"))
						+ "/(CASE WHEN ";
				sqlStr += tmps + "=0 THEN NULL ELSE " + tmps + " END)";
			}
			// rptMsu = sqlStr + " AS " + fnd + p0;
			rptMsu = sqlStr;
		}
		return rptMsu;
	}

	/**
	 *
	 * @param menuUrl
	 * @param menuId
	 * @desc:从menu_url中获取是否有SysMenuCode,返回带SysMenuCode的url
	 */
	public static String getSysMenuCodeFromUrl(String menuUrl, String menuId) {
		String strRetn = "";
		menuUrl = StringB.NulltoBlank(menuUrl);
		if (menuUrl.length() > 0) {
			// System.out.println(menuUrl + ":menuUrl");
			if (menuUrl.indexOf("SysMenuCode") != -1) {
				return menuUrl;
			}

			if (menuUrl.indexOf("?") == -1) {
				strRetn = menuUrl + "?SysMenuCode=" + menuId;
			} else {
				// url中带问号
				strRetn = menuUrl + "&SysMenuCode=" + menuId;
			}

		}
		return strRetn;
	}

	/**
	 * 得到替换字符串数组
	 *
	 * @param source
	 * @param regex
	 * @return
	 */
	public static String[] replaceArrValue(String source, String regex) {
		Matcher matcher = Pattern.compile(regex).matcher(source);
		List dd = new ArrayList();
		for (@SuppressWarnings("unused")
		int counter = 0; matcher.find(); counter++) {
			int aa = matcher.start();
			dd.add(Integer.toString(aa));
		}
		List ee = new ArrayList();
		for (int i = 0; i < dd.size(); i++) {
			String aa = (String) dd.get(i);
			String bb = (String) dd.get(i + 1);
			ee.add(source.substring(Integer.parseInt(aa) + 1,
					Integer.parseInt(bb)));
			i++;
		}
		String[] sourceArr = (String[]) ee.toArray(new String[ee.size()]);
		return sourceArr;
	}

	public static void main(String[] args) {
		String tmp = "NVL(DATA_NUM1,0)/NVL(DATA_NUM2,0)";
		tmp = "DATA_NUM2/(DATA_NUM2+DATA_NUM3+DATA_NUM4)";
		tmp = StringB.replace(tmp, "NVL(", "");
		tmp = StringB.replace(tmp, ",0)", "");
		System.out.println(parseCmd(tmp, "DATA_NUM", 8, 3));
	}
}