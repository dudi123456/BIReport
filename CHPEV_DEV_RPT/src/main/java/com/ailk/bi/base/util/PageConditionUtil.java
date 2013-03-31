package com.ailk.bi.base.util;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.common.CSysCode;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.PubInfoChartFunTable;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;

public class PageConditionUtil {
	/**
	 * 区域条件（需加上数据权限控制）
	 *
	 * @param request
	 * @param area_id
	 * @param qry_id
	 * @param qry_name
	 * @param colnum
	 * @return
	 */
	public static String getRegionDesc(HttpServletRequest request, String area_id, String qry_id,
			String qry_name, int colnum) {
		// 数据权限
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) request.getSession().getAttribute(
				WebConstKeys.ATTR_C_UserCtlStruct);
		if (ctlStruct == null) {
			ctlStruct = new UserCtlRegionStruct();
		}

		//
		String[][] dataArr = null;
		try {
			String regionSql = "";
			if ("".equals(ctlStruct.ctl_city_str)) {
				regionSql = "select distinct '' as city_id,'" + CSysCode.CITY_DESC
						+ "' as city_desc,0 as sequence from D_CITY union ";
				regionSql += " select city_id, city_desc, sequence from D_CITY ";
				regionSql += " order by sequence";
			} else {
				regionSql = "";
				regionSql += " select city_id, city_desc, sequence from D_CITY where city_id in("
						+ ctlStruct.ctl_city_str_add + ")";
				regionSql += " order by sequence";
			}

			dataArr = WebDBUtil.execQryArray(regionSql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}

		return getCommonMColTable(dataArr, area_id, qry_id, qry_name, colnum);
	}

	public static String getCondtion_CommonDim(HttpServletRequest request, String value_id,
			String qry_id, String qry_name, int colnum, String typeId, String appTypeCode) {

		String[][] dataArr = null;
		try {
			String regionSql = "SELECT  '' as DIM_ID,'全部' as DIM_NAME  from dual union "
					+ "select DIM_ID,DIM_NAME FROM FUI_PUB_INFO_DIM_DEF WHERE TYPE_ID='" + typeId
					+ "' AND APP_TYPE_CODE='" + appTypeCode + "' order by DIM_ID";
			// System.out.println(regionSql);
			dataArr = WebDBUtil.execQryArray(regionSql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}

		return getCommonMColTable(dataArr, value_id, qry_id, qry_name, colnum);
	}

	public static String[][] getCondtion_CommonDim(String typeId, String appTypeCode) {
		String[][] dataArr = null;

		try {
			String regionSql = "SELECT  '' as DIM_ID,'全部' as DIM_NAME  from dual union "
					+ "select DIM_ID,DIM_NAME FROM FUI_PUB_INFO_DIM_DEF WHERE TYPE_ID='" + typeId
					+ "' AND APP_TYPE_CODE='" + appTypeCode + "' order by DIM_ID";
			// System.out.println(regionSql);
			dataArr = WebDBUtil.execQryArray(regionSql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}
		return dataArr;
	}

	/**
	 *
	 * @param request
	 * @param area_id
	 * @param qry_id
	 * @param qry_name
	 * @param colnum
	 * @param flag
	 * @return
	 * @获取资费名称
	 *
	 */
	public static String getPrice_plan_Desc(HttpServletRequest request, String price_plan_id,
			String qry_id, String qry_name, int colnum, int flag) {

		String[][] dataArr = null;
		try {
			String regionSql = "select '' as price_plan_id,'全部' as price_plan_name from dual union "
					+ "select x.price_plan_id || '',x.price_plan_name from new_bi_ui.d_adv_focus_pp x where x.price_plan_cat_id="
					+ flag + " order by price_plan_id";
			System.out.println(regionSql);
			dataArr = WebDBUtil.execQryArray(regionSql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}

		return getCommonMColTable(dataArr, price_plan_id, qry_id, qry_name, colnum);
	}

	/**
	 * 业务类型
	 *
	 * @param request
	 * @param svc_id
	 * @param qry_id
	 * @param qry_name
	 * @return
	 */
	public static String getSvcDesc(HttpServletRequest request, String svc_id, String qry_id,
			String qry_name, String all_flag_svc) {
		String[][] dataArr = null;
		try {
			String svcSql = "";
			if (!"none".equals(all_flag_svc) && !"lvl1_only".equals(all_flag_svc)) {
				svcSql += "select distinct '' as svc_kind_id,'全部' as svc_kind_name,'0' as lvl,0 as sequence from D_SVC_KIND union";
			}
			svcSql += " select svc_kind_id,svc_kind_name,case when svc_kind_lv1_id='' then '1' else '2' end as lvl,sequence from D_SVC_KIND";
			if ("lvl1".equals(all_flag_svc) || "lvl1_only".equals(all_flag_svc)) {
				svcSql += " where svc_kind_lv1_id=''";
			}
			svcSql += " order by sequence";
			// System.out.println(svcSql);
			dataArr = WebDBUtil.execQryArray(svcSql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}

		return getCommonRowTable(dataArr, svc_id, qry_id, qry_name);
	}

	/**
	 * 业务类型
	 *
	 * @param request
	 * @param svc_id
	 * @param qry_id
	 * @param qry_name
	 * @return
	 */
	public static String getSvcDesc(HttpServletRequest request, String svc_id, String qry_id,
			String qry_name) {
		String[][] dataArr = null;
		ReportQryStruct qryStruct = (ReportQryStruct) request.getSession().getAttribute(
				WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		try {
			String svcSql = "";
			if (!"none".equals(qryStruct.all_flag_svc)
					&& !"lvl1_only".equals(qryStruct.all_flag_svc)) {
				svcSql += "select distinct '' as svc_kind_id,'全部' as svc_kind_name,'0' as lvl,0 as sequence from D_SVC_KIND union";
			}
			svcSql += " select svc_kind_id,svc_kind_name,case when svc_kind_lv1_id='' then '1' else '2' end as lvl,sequence from D_SVC_KIND";
			if ("lvl1".equals(qryStruct.all_flag_svc) || "lvl1_only".equals(qryStruct.all_flag_svc)) {
				svcSql += " where svc_kind_lv1_id=''";
			}
			svcSql += " order by sequence";
			// System.out.println(svcSql);
			dataArr = WebDBUtil.execQryArray(svcSql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}

		return getCommonRowTable(dataArr, svc_id, qry_id, qry_name);
	}

	/**
	 * 客户分群
	 *
	 * @param request
	 * @param svc_id
	 * @param qry_id
	 * @param qry_name
	 * @return
	 */
	public static String getCustGroupDesc(HttpServletRequest request, String cust_group_id,
			String qry_id, String qry_name) {
		String[][] dataArr = null;
		try {
			String svcSql = "select distinct '' as cust_group_id,'全部' as cust_group_desc,'0' as lvl from D_CUST_GROUP union";
			svcSql += " select cust_group_id,cust_group_desc,'1' as lvl from D_CUST_GROUP";
			svcSql += " order by lvl,cust_group_id";
			// System.out.println(svcSql);
			dataArr = WebDBUtil.execQryArray(svcSql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}

		return getCommonRowTable(dataArr, cust_group_id, qry_id, qry_name);
	}

	/**
	 * 品牌
	 *
	 * @param request
	 * @param cust_group_id
	 * @param qry_id
	 * @param qry_name
	 * @return
	 */
	public static String getBrandDesc(HttpServletRequest request, String brand_id, String qry_id,
			String qry_name) {
		String[][] dataArr = null;
		try {
			String svcSql = "select distinct '' as brand_id,'全部' as brand_name,'0' as lvl,0 as sequence from D_BRAND union";
			svcSql += " select brand_id,brand_name,'1' as lvl,sequence from D_BRAND";
			svcSql += " order by sequence";
			// System.out.println(svcSql);
			dataArr = WebDBUtil.execQryArray(svcSql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}

		return getCommonRowTable(dataArr, brand_id, qry_id, qry_name);
	}

	/**
	 * 品牌
	 *
	 * @param request
	 * @param cust_group_id
	 * @param qry_id
	 * @param qry_name
	 * @return
	 */
	public static String getCarrierDesc(HttpServletRequest request, String carrier_id,
			String qry_id, String qry_name, String value) {
		String[][] dataArr = null;
		try {

			String sql = " select carrier_id,carrier_name,'1' as lvl,1 from D_OPP_CARRIER where carrier_id in("
					+ value + ")";
			// System.out.println(svcSql);
			dataArr = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}

		return getCommonRowTable(dataArr, carrier_id, qry_id, qry_name);
	}

	/**
	 * 产品大类
	 *
	 * @param request
	 * @param svc_id
	 * @param qry_id
	 * @param qry_name
	 * @param callScript
	 * @return
	 */
	public static String getProductDesc(HttpServletRequest request, String product_lv1_id,
			String qry_id, String qry_name, String callScript) {
		String[][] dataArr = null;
		ReportQryStruct qryStruct = (ReportQryStruct) request.getSession().getAttribute(
				WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		try {
			String svcSql = "";
			if (!"none".equals(qryStruct.all_flag_product_lv1)) {
				svcSql += "select distinct '' as product_lv1_id,'全部' as product_lv1_name,'0' as lvl from d_product union";
			}
			svcSql += " select distinct product_lv1_id,product_lv1_name,'1' as lvl from d_product";
			svcSql += " order by product_lv1_id";
			// System.out.println(svcSql);
			dataArr = WebDBUtil.execQryArray(svcSql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}

		return getCommonRowTable(dataArr, product_lv1_id, qry_id, qry_name, callScript);
	}

	/**
	 * 在网时长分档
	 *
	 * @param request
	 * @param lvl_id
	 * @param qry_id
	 * @param qry_name
	 * @return
	 */
	public static String getOnlineLvl(HttpServletRequest request, String lvl_id, String qry_id,
			String qry_name, String param_id, int colnum) {
		String[][] dataArr = null;
		try {
			String svcSql = "select distinct '' as lvl_id,'全部' as lvl_name,'0' as lvl,0 as sequence from d_cch_param_lvl union";
			svcSql += " select to_char(lvl_id),lvl_name,'1' as lvl,lvl_id as sequence from d_cch_param_lvl where param_id='"
					+ param_id + "'";
			svcSql += " order by sequence";
			// System.out.println(svcSql);
			dataArr = WebDBUtil.execQryArray(svcSql, "");
		} catch (AppException e) {
			e.printStackTrace();
		}

		return getCommonMColTable(dataArr, lvl_id, qry_id, qry_name, colnum);
	}

	/**
	 * 通用的多行多列显示
	 *
	 * @param dataArr
	 * @param value_id
	 * @param qry_id
	 * @param qry_name
	 * @param colnum
	 * @return
	 */
	public static String getCommonMColTable(String[][] dataArr, String value_id, String qry_id,
			String qry_name, int colnum) {
		String ret = "";
		String id1 = "qry__" + qry_id;
		String id2 = "_" + qry_id;
		String name1 = "qry__" + qry_name;

		ret += "<TABLE class=d9_pop_table cellSpacing=1 cellPadding=1 width=100% border=0>";
		ret += "<TBODY>";
		String classStr = "";
		String idStr = "";
		for (int i = 0; dataArr != null && i < dataArr.length; i++) {
			if (value_id.equals(dataArr[i][0])) {
				classStr = "d9_pop_td_sel";
				idStr = "id='" + id2 + "'  sel='true'";
			} else {
				classStr = "d9_pop_td";
				idStr = "";
			}

			if (i % colnum == 0) {
				ret += "<tr>\n";
			}
			ret += "<TD class=" + classStr + " " + idStr
					+ " onmouseover=d9tdov(this) style='TEXT-ALIGN: left'";
			ret += " onclick=d9clk(this) onmouseout=d9tdou(this)";
			ret += " vvalue='" + dataArr[i][0] + "' vvaluedesc='" + dataArr[i][1] + "' vname='"
					+ id1 + "'  vdescname='" + name1 + "'>";
			if (value_id.equals(dataArr[i][0])) {
				ret += "<SCRIPT>__ctrlMapD9.put('" + id1 + "',document.getElementById('" + id2
						+ "'));</SCRIPT>";
			}
			ret += dataArr[i][1] + "</td>\n";
			if (i % colnum == (colnum - 1) || i == (dataArr.length - 1)) {
				ret += "</tr>\n";
			}
		}
		ret += "</TBODY>";
		ret += "</TABLE>";

		return ret;
	}

	/**
	 * 获取单列排列表格，id,描述,层次三列
	 *
	 * @param dataArr
	 * @param value_id
	 * @param qry_id
	 * @param qry_name
	 * @return
	 */
	public static String getCommonRowTable(String[][] dataArr, String value_id, String qry_id,
			String qry_name) {
		return getCommonRowTable(dataArr, value_id, qry_id, qry_name, "");
	}

	/**
	 * 获取单列排列表格，id,描述,层次三列
	 *
	 * @param dataArr
	 * @param value_id
	 * @param qry_id
	 * @param qry_name
	 * @return
	 */
	public static String getCommonRowTable(String[][] dataArr, String value_id, String qry_id,
			String qry_name, String callScript) {
		String ret = "";
		String id1 = "qry__" + qry_id;
		String id2 = "_" + qry_id;
		String name1 = "qry__" + qry_name;

		ret += "<TABLE class=d9_pop_table cellSpacing=1 cellPadding=1 width=100% border=0>";
		ret += "<TBODY>";
		String classStr = "";
		String idStr = "";
		for (int i = 0; dataArr != null && i < dataArr.length; i++) {
			if (value_id.equals(dataArr[i][0])) {
				classStr = "d9_pop_td_sel";
				idStr = "id='" + id2 + "'  sel='true'";
			} else {
				classStr = "d9_pop_td";
				idStr = "";
			}

			ret += "<tr>\n";
			ret += "<TD class=" + classStr + " " + idStr
					+ " onmouseover=d9tdov(this) style='TEXT-ALIGN: left'";
			ret += " onclick='d9clk(this)";
			if (callScript != null && !"".equals(callScript.trim())) {
				ret += ";" + callScript;
			}
			ret += "'";
			ret += " onmouseout=d9tdou(this)";
			ret += " vvalue='" + dataArr[i][0] + "' vvaluedesc='" + dataArr[i][1] + "' vname='"
					+ id1 + "'  vdescname='" + name1 + "'>";
			if (value_id.equals(dataArr[i][0])) {
				ret += "<SCRIPT>__ctrlMapD9.put('" + id1 + "',document.getElementById('" + id2
						+ "'));</SCRIPT>";
			}
			if ("1".equals(dataArr[i][2])) {
				ret += "&nbsp;&nbsp;";
			} else if ("2".equals(dataArr[i][2])) {
				ret += "&nbsp;&nbsp;&nbsp;&nbsp;";
			}
			ret += dataArr[i][1] + "</td>\n";
			ret += "</tr>\n";
		}
		ret += "</TBODY>";
		ret += "</TABLE>";

		return ret;
	}

	public static String getDayDesc() {
		String ret = "";
		ret += "<TABLE>";
		ret += "<TBODY>";
		ret += "<TR>";
		ret += "<TD vAlign=center align=middle>";
		ret += "<IMG id=\"__gind971_5_yeardecr\" onclick=\"javascript:d9calclk();\" height=\"11\" hspace=\"5\" src=\"../images/common/x/left_arrow.gif\" width=\"6\" border=\"0\">";
		ret += "<SPAN id=__gind971_5_year></SPAN>";
		ret += "<IMG id=__gind971_5_yearincr onclick=d9calclk() height=11 hspace=5 src=../images/common/x/right_arrow.gif width=6 border=0></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD>";
		ret += "<TABLE class=d9_pop_table cellSpacing=1 cellPadding=1 width=100% border=0>";
		ret += "<TBODY id=__gind971_5_month>";
		ret += "<TR>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>1月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>2月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>3月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>4月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>5月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>6月</TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>7月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>8月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>9月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>10月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>11月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>12月</TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "</TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD>";
		ret += "<TABLE class=d9_pop_table id=__gind971_5_month cellSpacing=1 cellPadding=1 width=100% border=0>";
		ret += "<TBODY>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td><B>一</B></TD>";
		ret += "<TD class=d9_pop_td><B>二</B></TD>";
		ret += "<TD class=d9_pop_td><B>三</B></TD>";
		ret += "<TD class=d9_pop_td><B>四</B></TD>";
		ret += "<TD class=d9_pop_td><B>五</B></TD>";
		ret += "<TD class=d9_pop_td><B>六</B></TD>";
		ret += "<TD class=d9_pop_td><B>日</B></TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "<TBODY id=__gind971_5_date>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "</TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "<SCRIPT defer>__initCalendar('__gind971_5');</SCRIPT>";
		return ret;
	}

	public static String getDayDescByOne() {
		String ret = "";
		ret += "<TABLE>";
		ret += "<TBODY>";
		ret += "<TR>";
		ret += "<TD vAlign=center align=middle>";
		ret += "<IMG id=__gind971_5_yeardecr onclick=d9calclk() height=11 hspace=5 src=../images/common/x/left_arrow.gif width=6 border=0>";
		ret += "<SPAN id=__gind971_5_year></SPAN>";
		ret += "<IMG id=__gind971_5_yearincr onclick=d9calclk() height=11 hspace=5 src=../images/common/x/right_arrow.gif width=6 border=0></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD>";
		ret += "<TABLE class=d9_pop_table_a cellSpacing=1 cellPadding=1 width=100% border=0>";
		ret += "<TBODY id=__gind971_5_month>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>1月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>2月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>3月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>4月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>5月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>6月</TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>7月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>8月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>9月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>10月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>11月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>12月</TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "</TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD>";
		ret += "<TABLE class=d9_pop_table_a id=__gind971_5_month cellSpacing=1 cellPadding=1 width=100% border=0>";
		ret += "<TBODY>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td_a><B>一</B></TD>";
		ret += "<TD class=d9_pop_td_a><B>二</B></TD>";
		ret += "<TD class=d9_pop_td_a><B>三</B></TD>";
		ret += "<TD class=d9_pop_td_a><B>四</B></TD>";
		ret += "<TD class=d9_pop_td_a><B>五</B></TD>";
		ret += "<TD class=d9_pop_td_a><B>六</B></TD>";
		ret += "<TD class=d9_pop_td_a><B>日</B></TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "<TBODY id=__gind971_5_date>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "</TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "<SCRIPT defer>__initCalendar('__gind971_5');</SCRIPT>";
		return ret;
	}

	public static String getDayDesc(HttpServletRequest request) {
		String rtPath = request.getContextPath();

		String ret = "";
		ret += "<TABLE>";
		ret += "<TBODY>";
		ret += "<TR>";
		ret += "<TD vAlign=center align=middle>";
		ret += "<IMG id=__gind971_5_yeardecr onclick=\"d9calclk()\" height=11 hspace=5 src=" + rtPath
				+ "/images/common//x/left_arrow.gif width=6 border=0>";
		ret += "<SPAN id=__gind971_5_year></SPAN>";
		ret += "<IMG id=__gind971_5_yearincr onclick=d9calclk() height=11 hspace=5 src=" + rtPath
				+ "/images/common/x/right_arrow.gif width=6 border=0></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD>";
		ret += "<TABLE class=d9_pop_table cellSpacing=1 cellPadding=1 width=100% border=0>";
		ret += "<TBODY id=__gind971_5_month>";
		ret += "<TR>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick='d9calclk()' onmouseout=d9calout()>1月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>2月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>3月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>4月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>5月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>6月</TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>7月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>8月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>9月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>10月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>11月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>12月</TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "</TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD>";
		ret += "<TABLE class=d9_pop_table id=__gind971_5_month cellSpacing=1 cellPadding=1 width=100% border=0>";
		ret += "<TBODY>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td><B>一</B></TD>";
		ret += "<TD class=d9_pop_td><B>二</B></TD>";
		ret += "<TD class=d9_pop_td><B>三</B></TD>";
		ret += "<TD class=d9_pop_td><B>四</B></TD>";
		ret += "<TD class=d9_pop_td><B>五</B></TD>";
		ret += "<TD class=d9_pop_td><B>六</B></TD>";
		ret += "<TD class=d9_pop_td><B>日</B></TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "<TBODY id=__gind971_5_date>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "</TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "<SCRIPT defer>__initCalendar('__gind971_5');</SCRIPT>";
		return ret;
	}

	public static String getDayDescByOne(HttpServletRequest request) {
		String rtPath = request.getContextPath();

		String ret = "";
		ret += "<TABLE width=99%>";
		ret += "<TBODY>";
		ret += "<TR>";
		ret += "<TD vAlign=center align=middle>";
		ret += "<IMG id=__gind971_5_yeardecr onclick=d9calclk() height=11 hspace=5 src=" + rtPath
				+ "/images/common/x/left_arrow.gif width=6 border=0>";
		ret += "<SPAN id=__gind971_5_year></SPAN>";
		ret += "<IMG id=__gind971_5_yearincr onclick=d9calclk() height=11 hspace=5 src=" + rtPath
				+ "/images/common/x/right_arrow.gif width=6 border=0></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD>";
		ret += "<TABLE class=d9_pop_table_a cellSpacing=1 cellPadding=1 width=100% border=0>";
		ret += "<TBODY id=__gind971_5_month>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>1月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>2月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>3月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>4月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>5月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>6月</TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>7月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>8月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>9月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>10月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>11月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>12月</TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "</TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD>";
		ret += "<TABLE class=d9_pop_table_a id=__gind971_5_month cellSpacing=1 cellPadding=1 width=100% border=0>";
		ret += "<TBODY>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td_a><B>一</B></TD>";
		ret += "<TD class=d9_pop_td_a><B>二</B></TD>";
		ret += "<TD class=d9_pop_td_a><B>三</B></TD>";
		ret += "<TD class=d9_pop_td_a><B>四</B></TD>";
		ret += "<TD class=d9_pop_td_a><B>五</B></TD>";
		ret += "<TD class=d9_pop_td_a><B>六</B></TD>";
		ret += "<TD class=d9_pop_td_a><B>日</B></TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "<TBODY id=__gind971_5_date>";
		ret += "<TR>";

		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "<TD class=d9_pop_td_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()></TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "</TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "<SCRIPT defer>__initCalendar('__gind971_5');</SCRIPT>";
		return ret;
	}

	public static String getMonthDesc(HttpServletRequest request) {
		String rtPath = request.getContextPath();

		String ret = "";
		ret += "<TABLE>";
		ret += "<TBODY>";
		ret += "<TR>";
		ret += "<TD vAlign=center align=middle>";
		ret += "<IMG id=__gind971_50_yeardecr onclick=d9calclk() height=11 hspace=5 src=" + rtPath
				+ "/images/common/x/left_arrow.gif width=6 border=0>";
		ret += "<SPAN id=__gind971_50_year></SPAN>";
		ret += "<IMG id=__gind971_50_yearincr onclick=d9calclk() height=11 hspace=5 src=" + rtPath
				+ "/images/common/x/right_arrow.gif width=6 border=0></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD>";
		ret += "<TABLE class=d9_pop_table cellSpacing=1 cellPadding=1 width=100% border=0>";
		ret += "<TBODY id=__gind971_50_month>";
		ret += "<TR>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>1月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>2月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>3月</TD></TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>4月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>5月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>6月</TD></TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>7月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>8月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>9月</TD></TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>10月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>11月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>12月</TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "</TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "<SCRIPT defer>__initCalendar('__gind971_50');</SCRIPT>";
		return ret;
	}

	public static String getMonthDescByOne(HttpServletRequest request) {
		String rtPath = request.getContextPath();

		String ret = "";
		ret += "<TABLE width=99%>";
		ret += "<TBODY>";
		ret += "<TR>";
		ret += "<TD vAlign=center align=middle>";
		ret += "<IMG id=__gind971_50_yeardecr onclick=d9calclk() height=11 hspace=5 src=" + rtPath
				+ "/images/common/x/left_arrow.gif width=6 border=0>";
		ret += "<SPAN id=__gind971_50_year></SPAN>";
		ret += "<IMG id=__gind971_50_yearincr onclick=d9calclk() height=11 hspace=5 src=" + rtPath
				+ "/images/common/x/right_arrow.gif width=6 border=0></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD>";
		ret += "<TABLE class=d9_pop_table_a cellSpacing=1 cellPadding=1 width=100% border=0>";
		ret += "<TBODY id=__gind971_50_month>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()>1月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()>2月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()>3月</TD></TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()>4月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()>5月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()>6月</TD></TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()>7月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()>8月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()>9月</TD></TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()>10月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()>11月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick='d9calclk();changDate();' onmouseout=d9calout()>12月</TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "</TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "<SCRIPT defer>__initCalendar('__gind971_50');</SCRIPT>";
		return ret;
	}

	public static String getMonthDesc() {
		String ret = "";
		ret += "<TABLE>";
		ret += "<TBODY>";
		ret += "<TR>";
		ret += "<TD vAlign=center align=middle>";
		ret += "<IMG id=__gind971_50_yeardecr onclick=d9calclk() height=11 hspace=5 src=../images/common/x/left_arrow.gif width=6 border=0>";
		ret += "<SPAN id=__gind971_50_year></SPAN>";
		ret += "<IMG id=__gind971_50_yearincr onclick=d9calclk() height=11 hspace=5 src=../images/common/x/right_arrow.gif width=6 border=0></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD>";
		ret += "<TABLE class=d9_pop_table cellSpacing=1 cellPadding=1 width=100% border=0>";
		ret += "<TBODY id=__gind971_50_month>";
		ret += "<TR>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>1月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>2月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>3月</TD></TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>4月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>5月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>6月</TD></TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>7月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>8月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>9月</TD></TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>10月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>11月</TD>";
		ret += "<TD class=d9_pop onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>12月</TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "</TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "<SCRIPT defer>__initCalendar('__gind971_50');</SCRIPT>";
		return ret;
	}

	public static String getMonthDescByOne() {
		String ret = "";
		ret += "<TABLE>";
		ret += "<TBODY>";
		ret += "<TR>";
		ret += "<TD vAlign=center align=middle>";
		ret += "<IMG id=__gind971_50_yeardecr onclick=d9calclk() height=11 hspace=5 src=../images/common/x/left_arrow.gif width=6 border=0>";
		ret += "<SPAN id=__gind971_50_year></SPAN>";
		ret += "<IMG id=__gind971_50_yearincr onclick=d9calclk() height=11 hspace=5 src=../images/common/x/right_arrow.gif width=6 border=0></TD>";
		ret += "</TR>";
		ret += "<TR>";
		ret += "<TD>";
		ret += "<TABLE class=d9_pop_table_a cellSpacing=1 cellPadding=1 width=100% border=0>";
		ret += "<TBODY id=__gind971_50_month>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>1月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>2月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>3月</TD></TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>4月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>5月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>6月</TD></TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>7月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>8月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>9月</TD></TR>";
		ret += "<TR>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>10月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>11月</TD>";
		ret += "<TD class=d9_pop_a onmouseover=d9calover() onclick=d9calclk() onmouseout=d9calout()>12月</TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "</TD>";
		ret += "</TR>";
		ret += "</TBODY>";
		ret += "</TABLE>";
		ret += "<SCRIPT defer>__initCalendar('__gind971_50');</SCRIPT>";
		return ret;
	}

	/**
	 * @param chart_id
	 * @param distype
	 *            radio单选 checkbox多选 select下拉框
	 * @param table_direction
	 *            1纵向 2横向
	 * @param colnum
	 *            每行显示个数
	 * @return
	 */
	public static String getChartFunction(String chart_id, String distype, String table_direction,
			String framename, int colnum) {
		String ret = "";
		if (colnum <= 0) {
			colnum = 1;
		}
		PubInfoChartFunTable[] chartDef = null;
		try {
			chartDef = CommChartUtil.getChartFunDef(chart_id);
		} catch (AppException e) {
			e.printStackTrace();
		}
		if (chartDef == null || chartDef.length == 0) {
			return ret;
		}

		ret += "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">";
		if ("radio".equals(distype)) {
			if ("2".equals(table_direction)) {
				for (int i = 0; chartDef != null && i < chartDef.length; i++) {
					PubInfoChartFunTable chart = chartDef[i];
					if (i % colnum == 0) {
						ret += "<tr>\n";
					}
					ret += "<td>";
					ret += "<input type=\"radio\" name=\"radio" + chart.chart_id + "\" onclick="
							+ getClickScript(chart);
					if ("Y".equals(chart.is_checked)) {
						ret += " checked";
					}
					ret += ">" + chart.chart_desc;
					ret += "</td>\n";
					if (i % colnum == (colnum - 1) || i == (chartDef.length - 1)) {
						ret += "</tr>\n";
					}
				}
			} else if ("1".equals(table_direction)) {
				for (int i = 0; chartDef != null && i < chartDef.length; i++) {
					PubInfoChartFunTable chart = chartDef[i];
					ret += "<tr>\n";
					ret += "<td>";
					ret += "<input type=\"radio\" name=\"radio" + chart.chart_id + "\" onclick="
							+ getClickScript(chart);
					if ("Y".equals(chart.is_checked)) {
						ret += " checked";
					}
					ret += ">" + chart.chart_desc;
					ret += "</td>\n";
					ret += "</tr>\n";
				}
			}
		}
		if ("checkbox".equals(distype)) {
			if ("2".equals(table_direction)) {
				for (int i = 0; chartDef != null && i < chartDef.length; i++) {
					PubInfoChartFunTable chart = chartDef[i];
					if (i % colnum == 0) {
						ret += "<tr>\n";
					}
					ret += "<td>";
					ret += "<input type=\"checkbox\" name=\"checkbox" + chart.chart_id
							+ "\" onclick=checkboxChange('" + framename + "','" + chart.chart_id
							+ "','" + chart.frame_name + "') value='" + chart.category_desc + ";"
							+ chart.category_desc_index + ";" + chart.wheresql + "'";
					if ("Y".equals(chart.is_checked)) {
						ret += " checked";
					}
					ret += ">" + chart.chart_desc;
					ret += "</td>\n";
					if (i % colnum == (colnum - 1) || i == (chartDef.length - 1)) {
						ret += "</tr>\n";
					}
				}
			} else if ("1".equals(table_direction)) {
				for (int i = 0; chartDef != null && i < chartDef.length; i++) {
					PubInfoChartFunTable chart = chartDef[i];
					ret += "<tr>\n";
					ret += "<td>";
					ret += "<input type=\"checkbox\" name=\"checkbox" + chart.chart_id
							+ "\" onclick=checkboxChange('" + framename + "','" + chart.chart_id
							+ "','" + chart.frame_name + "') value='" + chart.category_desc + ";"
							+ chart.category_desc_index + ";" + chart.wheresql + "'";
					if ("Y".equals(chart.is_checked)) {
						ret += " checked";
					}
					ret += ">" + chart.chart_desc;
					ret += "</td>\n";
					ret += "</tr>\n";
				}
			}
		}
		if ("select".equals(distype)) {
			ret += "<tr>\n";
			ret += "<td>";
			for (int i = 0; chartDef != null && i < chartDef.length; i++) {
				PubInfoChartFunTable chart = chartDef[i];
				if (i == 0) {
					ret += "<SELECT ID=\"select" + chart.chart_id + "\" name=\"select"
							+ chart.chart_id + "\" onChange=\"selectChange(this.value)\">";
				}
				ret += "<OPTION value=\"" + getClickScript(chart) + "\"";
				if ("Y".equals(chart.is_checked)) {
					ret += " selected";
				}
				ret += ">" + chart.chart_desc + "</OPTION>";
			}
			ret += "</td>\n";
			ret += "</tr>\n";
		}
		ret += "</table>\n";

		return ret;
	}

	/**
	 * 获取替换内容
	 *
	 * @param chart_id
	 * @return
	 */
	public static String getChartDefaultCheck(String chart_id) {
		String ret = "";

		PubInfoChartFunTable[] chartDef = null;
		try {
			chartDef = CommChartUtil.getChartFunDef(chart_id);
		} catch (AppException e) {
			e.printStackTrace();
		}
		if (chartDef == null || chartDef.length == 0) {
			return ret;
		}

		for (int i = 0; chartDef != null && i < chartDef.length; i++) {
			PubInfoChartFunTable chart = chartDef[i];
			if (ReportConsts.YES.equals(chart.is_checked)
					&& !StringTool.checkEmptyString(chart.replace_content)) {
				ret += "&replace=" + chart.replace_content;
			}
		}

		return ret;
	}

	private static String getClickScript(PubInfoChartFunTable chart) {
		String ret = "subjectchartchangenoflag('" + chart.chart_id + "','";
		if (!StringTool.checkEmptyString(chart.chart_desc)) {
			ret += "&chart_name_r=" + chart.chart_desc;
		}
		if (!StringTool.checkEmptyString(chart.category_desc)) {
			ret += "&category_desc=" + chart.category_desc;
		}
		if (!StringTool.checkEmptyString(chart.category_desc_index)) {
			ret += "&category_index=" + chart.category_desc_index;
		}
		if (!StringTool.checkEmptyString(chart.value_index)) {
			ret += "&value_index=" + chart.value_index;
		}
		if (!StringTool.checkEmptyString(chart.wheresql)) {
			ret += chart.wheresql;
		}
		if (!StringTool.checkEmptyString(chart.replace_content)) {
			ret += "&replace=" + chart.replace_content;
		}
		ret += "','" + chart.frame_name + "')";
		return ret;
	}

}
