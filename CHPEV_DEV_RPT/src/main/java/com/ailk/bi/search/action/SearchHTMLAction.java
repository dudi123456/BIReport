package com.ailk.bi.search.action;

import com.ailk.bi.base.common.CSysCode;
import com.ailk.bi.base.struct.LeaderQryStruct;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.leader.struct.LeaderKpiInfoStruct;
import com.ailk.bi.leader.util.LeaderKpiUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: 2010-1-25 Time: 13:54:05
 * To change this template use File | Settings | File Templates.
 */
public class SearchHTMLAction extends HTMLActionSupport {

	/**
     *
     */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		String keyword = "";
		try {
			keyword = CommTool.getParameterGB(request, "keyword");
			HttpSession session = request.getSession(true);
			// 数据权限
			UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
					.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
			// 从领导看板相关表里查出指标的信息
			LeaderQryStruct qryStruct = (LeaderQryStruct) session
					.getAttribute(WebKeys.ATTR_ailk_QRY_STRUCT);
			if (qryStruct == null) {
				qryStruct = new LeaderQryStruct();
				qryStruct.data_cycle = "2";// 日分析
				qryStruct.group_type = "0";// 老总门户
				qryStruct.cust_group_id = "";
				qryStruct.cust_group_name = "全部";
				qryStruct.city_id = ctlStruct.ctl_city_str;
			}
			qryStruct.msu_name = keyword;

			if ("".equals(ctlStruct.ctl_city_str)) {
				qryStruct.city_name = CSysCode.CITY_DESC;
			} else {
				qryStruct.city_name = CommTool.getResName(session, "region",
						ctlStruct.ctl_city_str);
			}
			if ("".equals(qryStruct.gather_mon)) {
				qryStruct.gather_mon = DateUtil.getDiffMonth(-1, new Date());
			}
			if ("".equals(qryStruct.gather_day)) {
				qryStruct.gather_day = DateUtil.getDiffDay(-1, new Date());
			}
			qryStruct.data_source = LeaderKpiUtil.getCustGroupDataSource(
					qryStruct.group_type, qryStruct.data_cycle);
			LeaderKpiInfoStruct[] kpiStruct = LeaderKpiUtil
					.findKpiInfoStruct(qryStruct);

			request.setAttribute("list", kpiStruct);// 指标数据

			// 报表类资源搜索
			String sql = "SELECT T1.MENU_NAME,\n"
					+ "       T1.MENU_URL,\n"
					+ "       CASE\n"
					+ "         WHEN T2.CYCLE = '1' THEN\n"
					+ "          '年'\n"
					+ "         WHEN T2.CYCLE = '2' THEN\n"
					+ "          '半年'\n"
					+ "         WHEN T2.CYCLE = '3' THEN\n"
					+ "          '季报'\n"
					+ "         WHEN T2.CYCLE = '4' THEN\n"
					+ "          '月报'\n"
					+ "         WHEN T2.CYCLE = '5' THEN\n"
					+ "          '旬报'\n"
					+ "         WHEN T2.CYCLE = '6' THEN\n"
					+ "          '日报'\n"
					+ "         WHEN T2.CYCLE = '7' THEN\n"
					+ "          '周报'\n"
					+ "         ELSE\n"
					+ "          '月报'\n"
					+ "       END AS CYCLE,\n"
					+ "       SUBSTR(SYS_CONNECT_BY_PATH(T1.MENU_NAME, '->'), 3) PATH\n"
					+ "  FROM UI_INFO_MENU T1\n"
					+ "  LEFT JOIN UI_RPT_INFO_REPORT T2 ON T1.RES_CHAR1 = T2.RPT_ID\n"
					+ " WHERE T1.MENU_NAME LIKE '%" + keyword + "%'\n"
					+ "   AND T1.RES_CHAR1 IS NOT NULL\n"
					+ " START WITH T1.MENU_ID IN (3, 60, 901096)\n"
					+ "CONNECT BY PRIOR T1.MENU_ID = T1.PARENT_ID\n"
					+ " ORDER SIBLINGS BY T1.MENU_ID";
			System.out.println("rptsql==============================" + sql);
			request.setAttribute("list1", WebDBUtil.execQryArray(sql, ""));// 指标数据
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setNextScreen(request, "searchIndex.screen");
	}
}
