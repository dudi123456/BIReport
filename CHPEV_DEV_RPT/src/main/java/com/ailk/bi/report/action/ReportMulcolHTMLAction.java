package com.ailk.bi.report.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.struct.LsbiQryStruct;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.MultiColDataStruct;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

/**
 * 
 * @author jincm
 * 
 */
public class ReportMulcolHTMLAction extends HTMLActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3583492342986992918L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();
		// 提取条件
		String report_id = CommTool.getParameterGB(request, "rpt_id");
		if (null == report_id) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "报表标识参数没有配置！");
		}
		//
		String report_name = getReportName(report_id);
		// 提取报表信息

		if (report_name == null || "".equals(report_name)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "该报表不存在！");
		}

		LsbiQryStruct qryStruct = new LsbiQryStruct();
		try {
			AppWebUtil.getHtmlObject(request, "qry", qryStruct);

		} catch (AppException ex) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "提取界面查询信息失败，请注意是否登陆超时！");
		}
		qryStruct.obj_id = report_id;
		qryStruct.obj_name = report_name;

		if (qryStruct.gather_mon == null || "".equals(qryStruct.gather_mon)) {
			qryStruct.gather_mon = DateUtil.getDiffMonth(-1,
					DateUtil.getNowDate());
		}

		// 对于双列报表数据权限目前只到地市，不到区县！！！！
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
				.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		// if ("2".equals(ctlStruct.ctl_lvl)) { //地市用户
		if (qryStruct.city_id == null || "".equals(qryStruct.city_id)) {
			qryStruct.city_id = ctlStruct.ctl_city_str_add;// 单一
		}

		if (qryStruct.city_id == null || "".equals(qryStruct.city_id)) {
			qryStruct.city_id = "999";
		}
		if ("999".equals(qryStruct.city_id)) {
			qryStruct.region_name = "全省";
		} else {
			qryStruct.region_name = getCityName(qryStruct.city_id);
		}

		// 提取数据
		MultiColDataStruct[] data = genData(qryStruct);
		if (data == null) {
			data = new MultiColDataStruct[0];
		}
		// 组织报表结构
		session.setAttribute(WebKeys.ATTR_LsbiQryStruct, qryStruct);
		session.setAttribute("MultiColDataStruct_DATA", data);
		//
		setNextScreen(request, "RptMultiView.screen");
	}

	public static MultiColDataStruct[] genData(LsbiQryStruct qryStruct) {
		MultiColDataStruct[] data = null;

		try {
			String sql = SQLGenator.genSQL("Q3590", qryStruct.obj_id,
					qryStruct.city_id, qryStruct.gather_mon, qryStruct.obj_id,
					qryStruct.city_id, qryStruct.gather_mon);
			// logcommon.debug("Q3590=========>" + sql);
			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				data = new MultiColDataStruct[rs.length];
			}
			for (int i = 0; rs != null && i < rs.length; i++) {
				data[i] = new MultiColDataStruct();
				data[i].setRow_no(rs[i][2]);
				//
				data[i].setItem_desc(rs[i][3]);
				if (null == rs[i][3] || "".equals(rs[i][3])) {
					data[i].setItem_value("");
				} else {
					if (null == rs[i][4] || "".equals(rs[i][4])) {
						data[i].setItem_value("0");
					} else {
						data[i].setItem_value(FormatUtil.formatStr(rs[i][4], 2,
								true));
					}
				}

				//
				data[i].setItem_2_desc(rs[i][8]);
				if (null == rs[i][8] || "".equals(rs[i][8])) {
					data[i].setItem_2_value("");
				} else {
					if (null == rs[i][9] || "".equals(rs[i][9])) {
						data[i].setItem_2_value("0");
					} else {
						data[i].setItem_2_value(FormatUtil.formatStr(rs[i][9],
								2, true));
					}
				}

			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}

		return data;
	}

	public static String getReportName(String report_id) {
		String rptName = "";
		try {
			String sql = "select name from ui_rpt_info_report where rpt_id = '"
					+ report_id + "'";

			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				rptName = rs[0][0];
			}

		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return rptName;

	}

	public static String getCityName(String city_id) {
		String cityName = "";
		try {
			String sql = "select city_desc from bi_mid.d_city  where city_id =  "
					+ city_id;

			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				cityName = rs[0][0];
			}

		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return cityName;

	}
}