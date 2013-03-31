package com.ailk.bi.base.util;

import javax.servlet.ServletContext;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import com.ailk.bi.base.struct.InfoResStruct;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.table.InfoPageItemTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.Arith;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.app.ReflectUtil;
import com.ailk.bi.common.dbtools.DBUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

import java.util.HashMap;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CommTool {
	private static final String WEBINF = "WEB-INF";

	/**
	 * 获取WEB-INF目录
	 * 
	 * @return
	 */
	public static String getWebInfPath() {

		String filePath = "";

		URL url = CommTool.class.getResource("CommTool.class");
		String className = url.getFile();

		filePath = className.substring(0,
				className.indexOf(WEBINF) + WEBINF.length());
		return filePath;
	}

	/**
	 * 取得BI系统 所有定义的界面元素
	 * 
	 * @param oper_no
	 * @return
	 * @author jcm
	 */

	public static InfoPageItemTable[] getAllPageItem() {
		return SecurityTool.getBIPageItemFromULP();
	}

	/**
	 * 取得登录所有系统资源
	 * 
	 * @author jcm
	 * @return
	 */
	public static InfoResStruct[] getInfoResStruct() {

		InfoResStruct infores[] = null;

		String sql = "";
		try {
			sql = SQLGenator.genSQL("Q0028");
			System.out.println("Q0028=================" + sql);
			String[][] result = WebDBUtil.execQryArray(sql, "");
			if (result != null && result.length > 0) {
				infores = new InfoResStruct[result.length];
				for (int i = 0; i < result.length; i++) {
					infores[i] = new InfoResStruct();
					infores[i].res_id = result[i][0];
					infores[i].parent_id = result[i][1];
					infores[i].is_res = result[i][2];
					infores[i].res_name = result[i][3];
					infores[i].res_url = result[i][4];
					//
					int count = 0;
					for (int m = 0; m < result.length; m++) {
						if (result[m][1].equals(infores[i].res_id)) {
							count++;
						}
					}
					//
					// System.out.println(count);
					//
					if (count > 0) {
						infores[i].submenu = new InfoResStruct[count];
						int num = 0;
						for (int m = 0; m < result.length; m++) {
							if (result[m][1].equals(infores[i].res_id)) {
								infores[i].submenu[num] = new InfoResStruct();
								infores[i].submenu[num].res_id = result[i][0];
								infores[i].submenu[num].parent_id = result[i][1];
								infores[i].submenu[num].is_res = result[i][2];
								infores[i].submenu[num].res_name = result[i][3];
								infores[i].submenu[num].res_url = result[i][4];
								num++;
							}
						}
					}

				}
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return infores;
	}

	/**
	 * 查询维表数据
	 * 
	 * @param type
	 * @return
	 * @author jcm
	 */
	public static HashMap getConstMap(String type) {
		HashMap map = new HashMap();
		String sql = "";
		//
		if ("BRAND".equals(type.toUpperCase())) {
			sql = "SELECT BRAND_KND,BRANDKND_DESC FROM D_BRAND_KND";
		}
		//
		if ("PRODUCT".equals(type.toUpperCase())) {
			sql = "select distinct product as id ,comments as name from  D_CODE_PRODUCT ";
			sql += " union ";
			sql += "select distinct sub_brand_knd as id,subbrandknd_desc as name from D_SUB_BRAND_KND ";
			sql += " union ";
			sql += "select distinct brand_code as id ,brandcode_desc  as name from D_BRAND_CODE ";

		}
		//
		if ("CHANNEL".equals(type.toUpperCase())) {
			sql = "select  channel_id as id , channel_desc as name from  d_channel ";
			sql += " union ";
			sql += " select sub_channel_type as id ,subchanneltype_desc as name from  d_sub_channel_type";
			sql += " union ";
			sql += "select channel_type as id ,channeltype_desc  as name from d_channel_type";
		}
		if ("SALE_TYPE".equals(type.toUpperCase())) {
			sql = "SELECT SALE_TYPE,SALETYPE_DESC FROM D_SALE_TYPE";
		}
		if ("VIP".equals(type.toUpperCase())) {
			sql = "SELECT VIP_LEVEL,VIPLEVEL_DESC FROM D_VIP_LEVEL";
		}
		if ("DURA".equals(type.toUpperCase())) {
			sql = "SELECT ONL_DURA,ONLDURA_DESC FROM D_ONL_DURA";
		}
		if ("MAC_TYPE".equals(type.toUpperCase())) {
			sql = "SELECT MAC_TYPE,MACTYPE_DESC FROM D_MAC_TYPE";
		}
		if ("SVC_KND".equals(type.toUpperCase())) {
			sql = "select distinct svc_knd as id, svcknd_desc as name from d_svc_knd  where svc_knd_lvl in('10','20')";
			sql += " union ";
			sql += "select  distinct  vprepay_knd as id, vprepayknd_desc as name  from d_svc_knd  where svc_knd_lvl in('10','20')";
			sql += " union ";
			sql += "select  distinct  svc_knd_lvl as id, svckndlvl_desc as name from d_svc_knd  where svc_knd_lvl in('10','20')";
		}
		//
		// System.out.println("sql============"+sql);
		String[][] arr = null;
		try {
			arr = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				map.put(arr[i][0], arr[i][1]);
			}

		}
		//
		return map;
	}

	/**
	 * 取得数据库表的序列号
	 * 
	 * @param seqTabName
	 * @return
	 */
	public static String dbGetMaxIDBySeq(String seqTabName) {

		String maxID = "1";
		try {
			String sql = "";
			if ("INFO_MENU".equals(seqTabName.toUpperCase())) {
				sql = SQLGenator.genSQL("Q2703");
			} else if ("INFO_ROLE".equals(seqTabName.toUpperCase())) {
				sql = SQLGenator.genSQL("Q2706");
			} else if ("INFO_OPER".equals(seqTabName.toUpperCase())) {
				sql = SQLGenator.genSQL("Q2707");
			} else if ("INFO_DEPT".equals(seqTabName.toUpperCase())) {
				sql = SQLGenator.genSQL("Q2704");
			} else if ("INFO_REGION".equals(seqTabName.toUpperCase())) {
				sql = SQLGenator.genSQL("Q2705");
			} else if ("INFO_PAGE_ITEM".equals(seqTabName.toUpperCase())) {
				sql = SQLGenator.genSQL("Q2708");
			}
			String res[][] = WebDBUtil.execQryArray(sql, "");
			if (res != null) {
				maxID = res[0][0];
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
		return maxID;
	}

	/**
	 * 取得登陆用户角色信息列表1，2，3，4
	 * 
	 * @param oper_no
	 * @return
	 * @author jcm
	 */
	public static String getRoleStringByOper(String oper_no) {
		return SecurityTool.getOperRoleStrFromULP(oper_no);
	}

	/**
	 * 登录用户组
	 * 
	 * @param session
	 * @return
	 */
	public static String getLoginGroup(HttpSession session) {
		if (session == null) {
			return null;
		}
		String group_id = (String) session
				.getAttribute(WebConstKeys.ATTR_C_SST_USER_GROUP);
		if (group_id == null) {
			return null;
		}
		return group_id;
	}

	/**
	 * 以中文码方式获得串
	 * 
	 * @param request
	 * @param paraName
	 * @return jcm confirm
	 */
	public static String getParameter8895_1(HttpServletRequest request,
			String paraName) {
		String encodeing = request.getCharacterEncoding();
		String charset = "gb2312";
		System.out.println("encodeing=" + encodeing);
		if (encodeing != null && "GBK".equals(encodeing)) {
			encodeing = "GBK";
			charset = "GBK";
		} else if (encodeing != null && "GB2312".equals(encodeing)) {
			charset = "GBK";
		} else {
			encodeing = "8859_1";
		}

		String tmpStr = request.getParameter(paraName);
		if (tmpStr == null || "".equals(tmpStr)) {
			return "";
		}
		try {
			tmpStr = new String(tmpStr.getBytes(encodeing), charset);
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
			tmpStr = request.getParameter(paraName);
		}
		return tmpStr;
	}

	/**
	 * @param strSql
	 * @return
	 * @throws AppException
	 */
	public static String[][] getDataArr(String strSql) throws AppException {
		return WebDBUtil.execQryArray(strSql, "");
	}

	/**
	 * 取得数据库表的序列号
	 * 
	 * @param seqTabName
	 * @return
	 * @author jcm
	 */
	public static String dbGetMaxID(String seqTabName) {
		String maxID = "1";
		Connection conn = null;
		try {
			conn = WebDBUtil.getConn();
			conn.setAutoCommit(false);
			String sql = SQLGenator.genSQL("Q1005", seqTabName);
			String res[][] = WebDBUtil.execQryArray(conn, sql, "");
			if (res == null) {
				sql = SQLGenator.genSQL("I1006", seqTabName);
			} else {
				maxID = res[0][0];
				sql = SQLGenator.genSQL("C1007", maxID, seqTabName);
			}

			WebDBUtil.execUpdate(conn, sql);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
				}
			}
			return "0";
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex1) {
				}
			}
		}
		return maxID;
	}

	/**
	 * 以中文码方式获得串
	 * 
	 * @param request
	 * @param paraName
	 * @return jcm confirm
	 */
	public static String getParameterGB(HttpServletRequest request,
			String paraName) {
		// String encodeing = request.getCharacterEncoding();
		// if (null == encodeing) {
		// encodeing = "UTF-8";
		// }
		String tmpStr = request.getParameter(paraName);
		// if (tmpStr == null || "".equals(tmpStr)) {
		// return "";
		// }
		// try {
		// tmpStr = new String(tmpStr.getBytes(encodeing), "GBK");
		// } catch (UnsupportedEncodingException uee) {
		// uee.printStackTrace();
		// tmpStr = request.getParameter(paraName);
		// }
		return tmpStr;
	}

	/**
	 * 返回UTF-8编码的字符串
	 * 
	 * @param request
	 * @param paraName
	 * @return
	 */
	public static String getParameterUTF(HttpServletRequest request,
			String paraName) {
		String encodeing = request.getCharacterEncoding();
		if (null == encodeing) {
			encodeing = "UTF-8";
		}
		String tmpStr = request.getParameter(paraName);
		try {
			if (null != tmpStr) {
				tmpStr = new String(tmpStr.getBytes(encodeing), "UTF-8");
				tmpStr = URLDecoder.decode(tmpStr, "UTF-8");
			}
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
			tmpStr = request.getParameter(paraName);
		}
		return tmpStr;
	}

	/**
	 * 函数名 :isset 功能描述:检查一个字符哈是否为空 参数 :str 字符串 返回值 :boolean
	 */
	public static boolean isset(String str) {
		if (str == null) {
			return false;
		} else if (str.equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * 通用的对象为空时抛出异常,注意:此方法只在HTMLAction中使用!
	 * 
	 * @param session
	 * @param obj
	 * @param msg
	 * @throws HTMLActionException
	 */
	public static void nullObj(HttpSession session, Object obj, String msg)
			throws HTMLActionException {
		if (obj == null)
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, msg);
	}

	/**
	 * 获取表中最大字段值
	 * 
	 * @param tablename
	 * @param field_name
	 * @param where
	 *            ｛以 and 开头的where条件｝
	 * @return
	 * @throws AppException
	 */
	public static String getMaxField(String tablename, String field_name,
			String where) {
		String[][] result = null;
		String sql = "";
		sql += "select max(" + field_name + ") from " + tablename
				+ " where 1=1 " + where;
		System.out.println(sql);
		try {

			result = WebDBUtil.execQryArray(sql, "");
			if (result != null && result.length > 0) {
				return result[0][0];
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * getLeftTree 获取左边的权限树（针对单结点情况） chent
	 * 
	 * @param parentID
	 *            要取资源表中记录集所对应的parentid值
	 * @param right_rptid
	 *            访问权限列表
	 * @param title
	 *            单节点显示的名称
	 * @return
	 * @author chent
	 */
	public static String getLeftTree(String parentID, String right_rptid,
			String title) {
		String result = "";
		try {
			result = getLeftTree(getTreeArr(parentID, right_rptid), title);
		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param parentID
	 * @param right_rptid
	 * @return
	 * @throws AppException
	 * @author chent
	 */
	public static String[][] getTreeArr(String parentID, String right_rptid)
			throws AppException {
		String[][] dictory = null;
		String sql = "";

		sql += "select res_id,name,url from UI_PUB_INFO_RESOURCE where status='Y' and parent_id="
				+ parentID;
		sql += " and res_id in (" + right_rptid + ") order by sequence";

		dictory = WebDBUtil.execQryArray(sql, "");
		return dictory;
	}

	/**
	 * 
	 * @param dataArr
	 * @param title
	 * @return
	 * @author chent
	 */
	public static String getLeftTree(String[][] dataArr, String title) {
		String result = "<script>  var t ;";
		result += " t=outlookbar.addtitle('" + title + "');";
		for (int i = 0; i < dataArr.length; i++) {
			result += " outlookbar.additem('" + dataArr[i][1] + "',t,'"
					+ dataArr[i][2] + "');";
		}
		return result + " </script>";
	}

	/**
	 * 获取多层结构的数据 chent
	 * 
	 * @param parentID
	 * @param right_rptid
	 * @return
	 * @throws AppException
	 * @author chent
	 */
	public static String[][] getCenTreeArr(String parentID, String right_rptid)
			throws AppException {
		String[][] dictory = null;
		String sql = "";
		sql += " select b.name,b.url,parent_id,b.res_id,b.sequence from ui_pub_info_resource b ";
		sql += " where b.status='Y' and b.parent_id= "
				+ parentID
				+ " and exists (select a.parent_id from ui_pub_info_resource  a ";
		sql += " where a.parent_id=b.res_id " +
		// "and rpt_id in (" + right_rptid + ")" +
				") ";
		sql += " union all  ";
		sql += " select b.name,b.url,parent_id,b.res_id,b.sequence from ui_pub_info_resource b ";
		sql += " where b.status='Y' and b.parent_id in (select a.res_id from ui_pub_info_resource  a ";
		sql += " where a.parent_id=" + parentID + ") ";

		sql += " union all ";
		sql += " select b.name,b.url,parent_id,b.res_id,b.sequence ";
		sql += " from ui_pub_info_resource b ";
		sql += "where b.status='Y' and b.parent_id in (select res_id from ui_pub_info_resource where parent_id in(select a.res_id from ui_pub_info_resource  a  where a.parent_id="
				+ parentID + "))";
		sql += "order by sequence,res_id ";
		System.out.println("----getCenTreeArr====" + sql);
		dictory = WebDBUtil.execQryArray(sql, "");
		return dictory;
	}

	/**
	 * getLeftCenTree获取左边的权限树（针对多结点情况）
	 * 
	 * @param dataArr
	 *            展现数据集合：dataArr[i][0]存放名称；dataArr[i][1]存放URL连接；dataArr[i][2]
	 *            存放parentID ；
	 * @param parentID
	 *            第一层目录中对应的parent_id
	 * @return
	 * @author chent
	 */
	public static String getLeftCenTree(String[][] dataArr, String parentID) {
		String result = "<script>  var t ;";
		for (int i = 0; i < dataArr.length; i++) {
			if (dataArr[i][2].equals(parentID)) {
				result += " t=outlookbar.addtitle('" + dataArr[i][0] + "');\n";
			} else {
				result += " outlookbar.additem('" + dataArr[i][0] + "',t,'"
						+ dataArr[i][1] + "');\n";
			}
		}
		return result + " </script>";
	}

	/**
	 * getLeftCenTree获取左边的权限树（针对多结点情况
	 * 
	 * @param dataArr
	 *            展现数据集合：dataArr[i][0]存放名称；dataArr[i][1]存放URL连接；dataArr[i][2]
	 *            存放parentID ；
	 * @param parentID
	 *            第一层目录中对应的parent_id
	 * @return
	 * @author jcm
	 */
	public static String getLeftCenterTree(String[][] dataArr, String parentID) {
		String result = "<script>  var t ,son_t;";
		for (int i = 0; i < dataArr.length; i++) {
			if (dataArr[i][2].equals(parentID)) {
				result += " t=outlookbar.addtitle('" + dataArr[i][0] + "');\n";
				for (int j = 0; j < dataArr.length; j++) {
					if (dataArr[j][2].equals(dataArr[i][3])) {
						result += " son_t = outlookbar.additem('"
								+ dataArr[j][0] + "',t,'" + dataArr[j][1]
								+ "');\n";
						for (int m = 0; m < dataArr.length; m++) {
							if (dataArr[m][2].equals(dataArr[j][3])) {
								result += " outlookbar.addsubitem('"
										+ dataArr[m][0] + "',t,son_t,'"
										+ dataArr[m][1] + "');\n";
							}
						}
					}
				}
			}
		}
		return result + " </script>";
	}

	/**
	 * 格式化输出数字
	 * 
	 * @param value
	 * @param len
	 * @param flag
	 * @return
	 */
	public static String formatNullEmpty(String value, int len, boolean flag) {
		return NullProcFactory.transNullToEmptyString(FormatUtil.formatStr(
				value, len, flag));
	}

	/**
	 * 格式化输出数字
	 * 
	 * @param value
	 * @param len
	 * @param flag
	 * @return
	 */
	public static String formatNullZero(String value, int len, boolean flag) {
		return NullProcFactory.transNullToZero(FormatUtil.formatStr(value, len,
				flag));
	}

	/**
	 * 获得登录用户对象名称
	 * 
	 * @param session
	 * @return
	 * @author jcm
	 */
	public static String getLoginStatus(HttpSession session) {
		if (session == null) {
			return null;
		}
		InfoOperTable loginUser = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
		if (loginUser == null) {
			return null;
		}
		return loginUser.login_status;
	}

	/**
	 * 获得登录用户归属区域
	 * 
	 * @param session
	 * @return
	 * @author jcm
	 */
	public static String getLoginUserRegion(HttpSession session) {
		if (session == null) {
			return null;
		}
		InfoOperTable loginUser = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
		if (loginUser == null) {
			return null;
		}
		return loginUser.region_id;
	}

	/**
	 * 从对象集合中得到其中符合条件的对象
	 */
	public static Object[] getObjSet(Object[] obj, String fieldName,
			String fieldValue) {
		int objSum = obj.length;
		int dataSum = 0;

		// 符合条件的对象数目
		for (int i = 0; i < objSum; i++) {
			Object temp = obj[i];
			String tempVal = ReflectUtil.getStringFromObj(temp, fieldName);
			if (fieldValue.equals(tempVal))
				dataSum++;
		}

		// 得到集合
		Object[] resultObj = new Object[dataSum];
		for (int i = 0, j = 0; i < objSum; i++) {
			Object temp = obj[i];
			String tempVal = ReflectUtil.getStringFromObj(temp, fieldName);
			if (fieldValue.equals(tempVal)) {
				resultObj[j] = temp;
				j++;
			}
		}

		return resultObj;
	}

	/**
	 * 简单解密（临时）
	 * 
	 * @param code
	 * @return
	 * @author jcm
	 */
	public static String getdesecret(String code) {

		String value;
		char[] A1 = new char[40];
		char[] fname1 = new char[40];
		char[] A2 = new char[40];

		int t2[] = { 5, 14, 12, 9, 11, 2, 0, 15, 7, 3, 4, 13, 1, 6, 10, 8 };
		char t3[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };

		int i = 0, j = 0, k, p, q;
		int key = 32;
		int temp;
		fname1 = code.toCharArray();
		k = code.length();
		while ((i < k) && (fname1[i] != '\0')) {
			A1[j] = fname1[i];
			p = 0;
			while ((p < 16) && (A1[j] != t3[p++]))
				;
			A2[j] = fname1[i + 1];
			q = 0;
			while ((q < 16) && (A2[j] != t3[q++]))
				;
			A1[j] = (char) (t2[p - 1] * 16 + t2[q - 1]);
			temp = (int) A1[j];
			temp = temp ^ key;
			A1[j] = (char) temp;
			i = i + 2;
			j++;
		}
		A1[j] = 0;
		value = String.valueOf(A1);
		return (value);

	}

	/**
	 * 简单加密（临时）
	 * 
	 * @param code
	 * @return
	 * @author jcm
	 */
	public static String getsecret(String code) {
		String value;
		char[] A1 = new char[40];
		char[] A2 = new char[40];
		int t1[] = { 6, 12, 5, 9, 10, 0, 13, 8, 15, 3, 14, 4, 2, 11, 1, 7 };
		char t3[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		int i = 0, j = 0, x, y, k;
		int key = 32;
		byte temp;

		A1 = code.toCharArray();
		k = code.length();
		while (j < k) {
			temp = (byte) A1[j];
			temp = (byte) (((int) temp) ^ key);
			A1[j] = (char) temp;

			x = temp / 16;
			y = temp % 16;
			A2[i++] = t3[t1[x]];

			A2[i++] = t3[t1[y]];

			j++;
		}
		A2[i] = '\0';
		value = String.valueOf(A2);
		return (value);
	}

	/**
	 * 解密
	 * 
	 * @param passwd
	 * @return
	 * @author jcm
	 */
	public static String getDecrypt(String passwd) {
		int key[] = { 0x4A, 0x50, 0x4C, 0x53, 0x43, 0x57, 0x43, 0x44 };
		int key_len = 8;
		StringBuffer msg = new StringBuffer();
		int len = passwd.length() / 2;
		int iTmp, i;
		for (i = 0; i < len; i++) {
			iTmp = (passwd.charAt(i * 2) - 'a') << 4;
			iTmp |= passwd.charAt(i * 2 + 1) - 'a';
			msg.append((char) (iTmp ^ key[i % key_len]));
		}
		return msg.toString();
	}

	/**
	 * 加密
	 * 
	 * @param passwd
	 * @return
	 * @author jcm
	 */
	public static String getEncrypt(String passwd) {
		StringBuffer msg = new StringBuffer();
		// String aa = null;
		int key_len = 8;
		int iTmp = 0;
		int key[] = { 0x4A, 0x50, 0x4C, 0x53, 0x43, 0x57, 0x43, 0x44 };
		for (int i = 0; i < passwd.length(); i++) {
			iTmp = (int) passwd.charAt(i) ^ key[i % key_len];
			msg.append((char) (((iTmp >> 4) & 0x0f) + 'a'));
			msg.append((char) ((iTmp & 0x0f) + 'a'));
		}
		return msg.toString();
	}

	/**
	 * 插入日志函数 现在只插入登录日志和功能点的访问日志
	 * 
	 * @param session
	 * @param log_type
	 * @param obj_id
	 * @return
	 * @author jcm
	 */
	public static int dbSetLog_bak_20070313(HttpSession session,
			String log_type, String obj_id) {
		int flag = -1;
		String sessionID = session.getId();
		InfoOperTable loginUser = CommonFacade.getLoginUser(session);
		String user_id = loginUser.user_id;
		String user_name = loginUser.user_name;
		String login_ip = loginUser.login_ip;
		String sql = "";
		if ("".equals(log_type) || "1".equals(log_type)) {
			try {
				sql = SQLGenator.genSQL("I0006", sessionID, "1", user_id,
						user_name, login_ip);
			} catch (AppException e) {

				e.printStackTrace();
			}
		} else {
			String obj_name = getObjName(session, obj_id);
			try {
				sql = SQLGenator.genSQL("I0007", sessionID, "2", user_id,
						user_name, login_ip, obj_id, obj_name);
			} catch (AppException e) {

				e.printStackTrace();
			}
		}
		// 执行
		System.out.println(sql);
		try {
			flag = WebDBUtil.execUpdate(sql);
		} catch (AppException ex) {
			return -1;
		}
		return flag;
	}

	public static void dbSetLog(HttpSession session, String log_type,
			String obj_id) {

		String sessionID = session.getId();
		InfoOperTable loginUser = CommonFacade.getLoginUser(session);
		String user_id = loginUser.user_id;
		String user_name = loginUser.user_name;
		String login_ip = loginUser.login_ip;

		if ("".equals(log_type) || "1".equals(log_type)) {
			String param[] = { sessionID, "1", user_id, user_name, login_ip,
					"", "", "0" };
			try {
				setLogByProcedure(param);
			} catch (AppException e) {

				e.printStackTrace();
			}
		} else {
			String obj_name = getObjName(session, obj_id);
			String param[] = { sessionID, "2", user_id, user_name, login_ip,
					obj_id, obj_name, "1" };
			try {
				setLogByProcedure(param);
			} catch (AppException e) {

				e.printStackTrace();
			}
		}

	}

	/**
	 * 系统日志及反馈参数设置
	 * 
	 * @param session
	 * @param log_type
	 *            1:登录日志;2:访问日志;
	 * @param obj_id
	 *            访问对象标识
	 * @return
	 * @author jcm
	 */
	public static void setLogAndObjInfo(HttpSession session, String log_type,
			String obj_id) {

		String obj = getBackObjFromSession(session);
		//
		if (obj == null || !obj.equals(obj_id)) {

			dbSetLog(session, log_type, obj_id);

			setBackObjToSession(session, obj_id);
		}

	}

	/**
	 * 从会话取得对象名称
	 * 
	 * @param session
	 * @param obj_id
	 * @return
	 * @author jcm
	 */
	public static String getObjName(HttpSession session, String obj_id) {
		ServletContext context = session.getServletContext();
		HashMap mapres = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_RES_ID_VS_NAME);
		String obj_name = (String) mapres.get(obj_id);
		return obj_name;
	}

	/**
	 * 将串转为数组
	 * 
	 * @param delStr
	 * @return
	 * @throws AppException
	 */
	public static String[] strToArray(String delStr) throws AppException {
		String[] delArr = null;
		if (!delStr.equals("") && delStr.length() > 0) {
			delArr = delStr.split(";,");
		}
		return delArr;
	}

	/**
	 * 显示说明编辑文字信息
	 * 
	 * @param obj_id
	 * @param obj_date
	 * @return
	 */
	public static StringBuffer getEditorHtml(String obj_id, String obj_date) {
		return getEditorHtml(obj_id, obj_date, " [ 编辑说明 ] ");
	}

	/**
	 * 显示说明编辑文字信息
	 * 
	 * @param obj_id
	 * @param obj_date
	 * @return
	 */
	public static StringBuffer getEditorHtml(String obj_id, String obj_date,
			String editInfo) {
		StringBuffer editout = new StringBuffer("");
		String editor = getEditorInfo(obj_id, obj_date);
		editout.append("<table width=\"100%\" align=\"center\">");
		editout.append("<tr height=\"30px;\">");
		editout.append("<td><div class=\"tipbox_new\"><b>说明&nbsp;&nbsp;</b><span style=\"cursor: pointer;\" class=\"blue\" onClick='window.open(\"EditorView.screen?id="
				+ obj_id
				+ "&date="
				+ obj_date
				+ "\", \"\", \"left=100,top=100,width=710,height=450\")'>"
				+ editInfo + "</span></div></td>");
		editout.append("</tr>");
		editout.append("<tr>");
		editout.append("<td class=\"row_linexx\" id=\"report_msu_memo\" >"
				+ editor + "</td>");
		editout.append("</tr>");
		editout.append("</table>");
		return editout;
	}

	/**
	 * 显示说明编辑文字信息
	 * 
	 * @param obj_id
	 * @param obj_date
	 * @return
	 */
	public static StringBuffer getEditorHtmlPrint(String obj_id, String obj_date) {
		StringBuffer editout = new StringBuffer("");
		String editor = getEditorInfo(obj_id, obj_date);
		if (editor == null || "".equals(editor.trim())) {
			return editout;
		}
		editout.append("<table width=\"100%\" align=\"center\">");
		editout.append("<tr>");
		editout.append("<td><img src=\"../biimages/arrow6.gif\" width=\"14\" height=\"7\">");
		editout.append("说　明：");
		editout.append("</td>");
		editout.append("</tr>");
		editout.append("<tr>");
		editout.append("<td height=\"1\" background=\"../biimages/black-dot.gif\"></td>");
		editout.append("</tr>");
		editout.append("<tr>");
		editout.append("<td id=\"report_msu_memo\" width=\19%\">" + editor
				+ "</td>");
		editout.append("</tr>");
		editout.append("<tr>");
		editout.append("<td height=\"1\" background=\"../biimages/black-dot.gif\"></td>");
		editout.append("</tr>");
		editout.append("</table>");
		return editout;
	}

	/**
	 * 获取评述信息
	 * 
	 * @param obj_id
	 *            资源ID
	 * @param obj_date
	 *            日期
	 * @author renhui
	 * @return
	 */
	public static String getEditorInfo(String obj_id, String obj_date) {
		if (obj_id == null || "".equals(obj_id))
			return "";
		if (obj_date == null || "".equals(obj_date))
			obj_date = "0";

		String[][] tmp = null;
		try {
			String sql = SQLGenator.genSQL("TQ3002", obj_id, obj_date);
			// System.out.println("TQ3002 sql=" + sql);
			tmp = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}
		if (tmp == null || tmp.length == 0)
			return "";
		return tmp[0][0];
	}

	/**
	 * 取得指定表的最大月份
	 * 
	 * @param tablename
	 * @return
	 * @throws AppException
	 * @throws AppException
	 * @author jcm
	 */
	public static String getMaxMon(String tablename) {
		String[][] result = null;

		String sql;
		try {
			sql = "select max(gather_month) from " + tablename;
			// System.out.println("Q0015=="+sql);
			result = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}
		if (result != null && result.length > 0) {
			return result[0][0];
		}
		return null;
	}

	/**
	 * 取得指定表的最大日期
	 * 
	 * @param tablename
	 * @return
	 * @throws AppException
	 * @throws AppException
	 * @author jcm
	 */
	public static String getMaxDay(String tablename) {
		String[][] result = null;

		String sql;
		try {
			sql = "select max(gather_day) from " + tablename;
			result = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}
		if (result != null && result.length > 0) {
			return result[0][0];
		}
		return null;
	}

	/**
	 * 取得指定表的最大日期
	 * 
	 * @param tablename
	 * @return
	 * @throws AppException
	 * @throws AppException
	 * @author jcm
	 */
	public static String getMaxDate(String tablename) {
		String[][] result = null;

		String sql;
		try {
			sql = "SELECT MAX(GATHER_DATE) FROM " + tablename;
			result = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}
		if (result != null && result.length > 0) {
			return result[0][0];
		}
		return null;
	}

	/**
	 * 当页面LOAD过程时，设置反馈对象
	 * 
	 * @param session
	 * @param obj_id
	 * @author jcm
	 */
	public static void setBackObjToSession(HttpSession session, String obj_id) {
		session.removeAttribute(WebConstKeys.ATTR_C_RES_BACK_ID);
		session.setAttribute(WebConstKeys.ATTR_C_RES_BACK_ID, obj_id);
	}

	/**
	 * 取出反馈对象标识
	 * 
	 * @param session
	 * @param obj_id
	 * @author jcm
	 */
	public static String getBackObjFromSession(HttpSession session) {
		String obj = (String) session
				.getAttribute(WebConstKeys.ATTR_C_RES_BACK_ID);
		return obj;
	}

	/**
	 * 从二维数组提取指定的值
	 * 
	 * @param arr
	 *            二维数组
	 * @param index
	 *            关键字索引
	 * @param comp
	 *            比较值
	 * @param value
	 *            提取值索引
	 * @return 字符串
	 * @author jcm
	 */
	public static String getValueFromArr(String[][] arr, int index,
			String comp, int value) {
		String retStr = "";

		for (int i = 0; arr != null && i < arr.length; i++) {
			if (arr[i][index].equals(comp)) {
				if (retStr.length() > 0) {
					retStr += " , ";
				}
				retStr += "'" + arr[i][value] + "'";
			}
		}
		return retStr;

	}

	/**
	 * 从二维数组提取指定的值
	 * 
	 * @param arr
	 *            二维数组
	 * @param index
	 *            关键字索引
	 * @param comp
	 *            比较值
	 * @param value
	 *            提取值索引
	 * @return 字符串
	 * @author jcm
	 */
	public static String getValueFromArrAdd(String[][] arr, int index,
			String comp, int value) {
		String retStr = "";

		for (int i = 0; arr != null && i < arr.length; i++) {
			if (arr[i][index].equals(comp)) {
				if (retStr.length() > 0) {
					retStr += " , ";
				}
				retStr += arr[i][value];
			}
		}
		return retStr;

	}

	/**
	 * 取得登录用户的控制区域
	 * 
	 * @param session
	 * @return
	 */
	public static UserCtlRegionStruct getUserCtlRegionStructFromSession(
			HttpSession session) {
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
				.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		return ctlStruct;
	}

	/**
	 * 调用日志存储过程
	 * 
	 * @param conn
	 * @param procedure
	 * @param param
	 * @throws AppException
	 */
	public static void setLogByProcedure(String param[]) throws AppException {

		try {
			Connection connection = null;
			CallableStatement cstmt = null;
			;
			try {
				connection = WebDBUtil.getConn();
				DBUtil dbutil = new DBUtil(connection);

				cstmt = connection
						.prepareCall("{call P_UI_LOG_PROCEDURE (?,?,?,?,?,?,?,?) }");
				cstmt.setString(1, param[0]);
				cstmt.setString(2, param[1]);
				cstmt.setString(3, param[2]);
				cstmt.setString(4, param[3]);
				cstmt.setString(5, param[4]);
				cstmt.setString(6, param[5]);
				cstmt.setString(7, param[6]);
				cstmt.setString(8, param[7]);
				cstmt.execute();
				cstmt.close();
				dbutil.closeConnection();

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				if (null != cstmt) {
					cstmt.close();
				}
				try {
					if (connection != null) {
						if (!connection.isClosed())
							connection.close();
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
				} finally {
					if (null != connection) {
						try {
							if (!connection.isClosed())
								connection.close();
						} catch (SQLException se) {
							se.printStackTrace();
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AppException(ex.getMessage());
		}
	}

	/**
	 * 填空格（总账报表）
	 * 
	 * @param value
	 * @param len
	 * @return
	 */

	public static String insertBlank(String value, String len) {
		if (len == null || "".equals(len)) {
			return value;
		}
		String retStr = "";
		//
		int length = Integer.parseInt(len);
		String str = "&nbsp;&nbsp;";
		for (int i = 0; i < length; i++) {
			retStr += str;
		}
		retStr += value;
		return retStr;

	}

	/**
	 * 求环比
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 * @return
	 */
	public static String computeLoopBit(String v1, String v2, int scale) {
		String vTemp = Arith.sub(v1, v2);
		return Arith.divPerNot(vTemp, v2, scale);
	}

	/**
	 * 返回数据类型
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 * @param flag
	 * @param type
	 * @return
	 */
	public static String computePer(String v1, String v2, int scale, String type) {

		String vTemp = Arith.sub(v1, v2);
		return NullProcFactory.transNullToFixedRate(
				Arith.divPer(vTemp, v2, scale), type);

	}

	public static String getSweek(String date) {
		SimpleDateFormat sf = new SimpleDateFormat("E");
		return sf.format(DateUtil.stringToDate(date));
	}

	/**
	 * 判断字符串是为null,或是全由空格组成
	 * 
	 * @param testStr
	 * @return
	 */
	public static boolean isEmpty(String testStr) {
		if (testStr != null && testStr.trim().length() > 0)
			return false;
		else
			return true;

	}

	/**
	 * 打印出二维数组
	 * 
	 * @param testStr
	 * @return
	 */
	public static void printArray(String[][] testArray) {
		if (testArray != null && testArray.length > 0) {
			String temp = "";
			for (int i = 0; i < testArray.length; i++) {
				temp += "[";
				for (int j = 0; j < testArray[i].length; j++) {
					temp += "," + testArray[i][j];
				}
				temp += "]\r\n";
			}
			System.out.println(temp);
			return;
		}
		System.out.println("A Null Array!");
	}

	/**
	 * 打印出二维数组
	 * 
	 * @param testStr
	 * @return
	 */
	public static void printArray(int[][] testArray) {
		if (testArray != null && testArray.length > 0) {
			String temp = "";
			for (int i = 0; i < testArray.length; i++) {
				temp += "[";
				for (int j = 0; j < testArray[i].length; j++) {
					temp += "," + testArray[i][j];
				}
				temp += "]\r\n";
			}
			System.out.println(temp);
			return;
		}
		System.out.println("A Null Array!");
	}

	/**
	 * 查询区域名称
	 * 
	 * @param args
	 */

	/**
	 * 从会话取得对象名称
	 * 
	 * @param session
	 * @param obj_id
	 * @return
	 * @author jcm
	 */
	public static String getResName(HttpSession session, String res_type,
			String res_id) {
		ServletContext context = session.getServletContext();
		HashMap map = new HashMap();
		if ("region".equalsIgnoreCase(res_type)) {
			map = (HashMap) context
					.getAttribute(WebConstKeys.ATTR_C_REGION_ID_VS_NAME);
		} else {
			map = (HashMap) context
					.getAttribute(WebConstKeys.ATTR_C_REGION_ID_VS_NAME);
		}

		String res_name = (String) map.get(res_id);
		if (res_name == null) {
			res_name = "";
		}
		return res_name;
	}

	/**
	 * 获取客户端ip 如果为集群的尝试从weblogic参数中取得 其他从request.getRemoteAddr()中取得
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientIP(HttpServletRequest request) {
		String ip = request.getHeader("WL-Proxy-Client-IP");// weblogic proxy's
		// ip head
		if (ip == null || "unknown".equals(ip)) {
			ip = request.getRemoteAddr();
		}
		ip = ip == null ? "" : ip;
		HttpSession session = request.getSession(false);
		if (session != null) {
			String jsip = (String) session.getAttribute("front.const.clientip"); // 通过javascript获取的ip
			if (jsip != null && !ip.equals(jsip)) {
				ip += "[" + jsip + "]";
			}
		}
		if (ip.length() > 63)
			ip = ip.substring(0, 63);
		return ip;
	}

	public static String replace(String strExp, String strFind, String strRep) {
		int intFrom, intTo;
		String strHead, strTail;

		if (strFind == null || strFind.length() == 0)
			return strExp;

		intFrom = strExp.indexOf(strFind, 0);
		while (intFrom >= 0) {
			intTo = intFrom + strFind.length();
			strHead = strExp.substring(0, intFrom);
			strTail = strExp.substring(intTo);
			strExp = strHead + strRep + strTail;
			System.out.println(strExp);
			System.out.println(strHead + strRep + strTail);
			intTo = intFrom + strRep.length();
			intFrom = strExp.indexOf(strFind, intTo);
		}

		return strExp;
	}

	/**
	 * 根据序列名称返回序列值
	 * 
	 * @param seqName
	 * @return
	 */
	public static String dbGetMaxIDBySeqName(String seqName) {

		String maxID = "1";
		try {
			String sql = "SELECT " + seqName + ".NEXTVAL FROM DUAL";
			String res[][] = WebDBUtil.execQryArray(sql, "");
			if (res != null) {
				maxID = res[0][0];
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
		return maxID;
	}

	public static String genModValue(String value) {
		String tmpStr = value.substring(0, value.indexOf("."));
		String reStr = "";
		for (int i = 1; i < tmpStr.length(); i++) {
			reStr += "0";
		}

		reStr = tmpStr.substring(0, 1) + reStr;
		return reStr;

	}

	/**
	 * 查询数组
	 * 
	 * @param sqlId
	 * @param whereStr
	 * @return
	 */
	public static String[][] queryArrayFacade(String sqlId, String whereStr) {
		String sql = "";
		String arr[][] = null;
		try {
			sql = SQLGenator.genSQL(sqlId, whereStr);
			System.out.println(sqlId + "================" + sql);
			arr = WebDBUtil.execQryArray(sql, "");

		} catch (AppException ex1) {
			//
			ex1.printStackTrace();
		}
		return arr;
	}

	/**
	 * 查询数组
	 * 
	 * @param sqlId
	 * @param whereStr
	 * @return
	 */
	public static String[][] queryArrayFacade(String sqlId, String paramA,
			String paramB) {
		String sql = "";
		String arr[][] = null;
		try {
			sql = SQLGenator.genSQL(sqlId, paramA, paramB);
			System.out.println(sqlId + "================" + sql);
			arr = WebDBUtil.execQryArray(sql, "");

		} catch (AppException ex1) {
			//
			ex1.printStackTrace();
		}
		return arr;
	}

	/**
	 * 查询数组
	 * 
	 * @param sqlId
	 * @param whereStr
	 * @return
	 */
	public static String[][] queryArrayFacade(String sqlId, String paramA,
			String paramB, String paramC) {
		String sql = "";
		String arr[][] = null;
		try {
			sql = SQLGenator.genSQL(sqlId, paramA, paramB, paramC);
			System.out.println(sqlId + "================" + sql);
			arr = WebDBUtil.execQryArray(sql, "");

		} catch (AppException ex1) {
			//
			ex1.printStackTrace();
		}
		return arr;
	}

	public static String[][] queryArrayFacade(String sqlId) {
		String sql = "";
		String arr[][] = null;
		try {
			sql = SQLGenator.genSQL(sqlId);
			System.out.println(sqlId + "================" + sql);
			arr = WebDBUtil.execQryArray(sql, "");

		} catch (AppException ex1) {
			//
			ex1.printStackTrace();
		}
		return arr;
	}

	/**
	 * 查询数组
	 * 
	 * @param sql
	 * @return
	 */
	public static String[][] queryArrayFacade(String sqlId, String[] strWhere) {
		String arr[][] = null;
		try {
			String sql = SQLGenator.genSQL(sqlId, strWhere);
			System.out.println(sqlId + "================" + sql);
			arr = WebDBUtil.execQryArray(sql, "");
		} catch (AppException ex1) {
			//
			ex1.printStackTrace();
		}
		return arr;
	}

	public static String[][] queryZiFeeInfo(String typeId) {

		String arr[][] = null;
		String sql = "select x.price_plan_id,x.price_plan_name from new_bi_ui.d_adv_focus_pp x where x.price_plan_cat_id="
				+ typeId + " order by price_plan_id";
		try {

			// String sql = SQLGenator.genSQL(sqlId,strWhere);
			// System.out.println(sqlId + "================" + sql);
			arr = WebDBUtil.execQryArray(sql, "");
		} catch (AppException ex1) {
			//
			ex1.printStackTrace();
		}
		return arr;
	}

	public static void main(String[] args) throws IOException {

		System.out.println(genModValue("123.45"));

		System.out.println(Boolean.TRUE);

		String str = "-1|0|2|3|4|5$6$7$8$9$10$11$12";

		System.out.println(str.split("[|]").length);

	}

}