package com.ailk.bi.leader.action;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;
import com.ailk.bi.SysConsts;
import com.ailk.bi.base.struct.LeaderQryStruct;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.leader.struct.LeaderKpiInfoStruct;
import com.ailk.bi.leader.util.LeaderKpiUtil;

/**
 * 领导首页处理
 *
 * @author Renhui
 *
 */
public class LeaderKpiHTMLAction extends HTMLActionSupport {

	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 检查登陆信息
		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
			return;

		// 获取用户session
		HttpSession session = request.getSession(true);
		// 数据权限
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
				.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		if (ctlStruct == null) {
			ctlStruct = new UserCtlRegionStruct();
		}
		// 提取参数条件
		LeaderQryStruct qryStruct = new LeaderQryStruct();

		// 分析周期
		String data_cycle = request.getParameter("qry__data_cycle");
		if (data_cycle == null || "".equals(data_cycle)) {
			data_cycle = SysConsts.STAT_PERIOD_DAY;
		}
		// 日期取值
		String gather_mon = request.getParameter("qry__gather_mon");
		String gather_day = request.getParameter("qry__gather_day");
		if (SysConsts.STAT_PERIOD_DAY.equals(data_cycle)) {
			if (StringTool.checkEmptyString(gather_day)) {
				gather_day = DateUtil.getDiffDay(-1, new Date());
				gather_mon = DateUtil.getDiffMonth(-1, new Date());
			}else{
				gather_mon = DateUtil.getDiffMonth(0, gather_day);
				String now_mon = DateUtil.getDiffMonth(0, new Date());
				if(now_mon.equals(gather_mon)){
					gather_mon = DateUtil.getDiffMonth(-1, new Date());
				}
			}
		} else if (SysConsts.STAT_PERIOD_MONTH.equals(data_cycle)) {
			if (StringTool.checkEmptyString(gather_mon)) {
				gather_mon = DateUtil.getDiffMonth(-1, new Date());
				gather_day = DateUtil.getDiffDay(-1, new Date());
			}
		}

		// 客户群
		String cust_group_id = request.getParameter("qry__cust_group_id");
		if (StringTool.checkEmptyString(cust_group_id)) {
			cust_group_id = "";
		}
		String cust_group_name = request.getParameter("qry__cust_group_name");
		if (StringTool.checkEmptyString(cust_group_name)) {
			cust_group_name = "全部";
		}

		// 分析区域
		String city_id = request.getParameter("qry__city_id");
		if (StringTool.checkEmptyString(city_id)) {
			city_id = ctlStruct.ctl_city_str;
			if (SysConsts.RIGHT_LVL_METRO.equals(ctlStruct.ctl_lvl)) {
				city_id = "";
			}
		}
		String city_name = request.getParameter("qry__city_name");
		if (StringTool.checkEmptyString(city_name)) {
			if ("".equals(city_id)) {
				city_name = "全部";
			} else {
				city_name = CommTool.getResName(session, "region", city_id);
			}
		}

		// 客户部门角色
		String group_type = request.getParameter("qry__group_type");
		if (group_type == null || "".equals(group_type)) {
			group_type = "0";// 老总门户
		}

		// 分析指标数据源
		String data_source = request.getParameter("qry__data_source");
		if (data_source == null || "".equals(data_source)) {
			data_source = LeaderKpiUtil.getCustGroupDataSource(group_type, data_cycle);
		}

		qryStruct.svc_id = group_type;
		qryStruct.city_id = city_id;
		qryStruct.city_name = city_name;
		qryStruct.gather_day = gather_day;
		qryStruct.cust_group_id = cust_group_id;
		qryStruct.cust_group_name = cust_group_name;
		qryStruct.data_cycle = data_cycle;
		qryStruct.group_type = group_type;
		qryStruct.gather_mon = gather_mon;
		qryStruct.data_source = data_source;

		// 会话
		session.setAttribute(WebKeys.ATTR_ailk_QRY_STRUCT, qryStruct);
		// 查询KPI数据
		try {
			System.out.println(qryStruct.toXml());
		} catch (AppException e) {
			e.printStackTrace();
		}
		LeaderKpiInfoStruct[] kpiStruct = LeaderKpiUtil.getKpiInfoStruct(qryStruct, ctlStruct);
		if (kpiStruct == null || kpiStruct.length <= 0) {
			throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
					"当前分析看板没有配置KPI指标信息，请通知系统维护人员！");
		}
		session.setAttribute(WebKeys.ATTR_LEADER_KPI_INFO_STRUCT, kpiStruct);
		// 确认第一个分析指标
		session.setAttribute(WebKeys.ATTR_LEADER_KPI_INFO_STRUCT_FIRST, kpiStruct[0]);

		this.setNextScreen(request, "LeaderKpiView.screen");
	}

}
