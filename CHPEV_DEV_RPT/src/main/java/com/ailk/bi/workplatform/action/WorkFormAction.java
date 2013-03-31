package com.ailk.bi.workplatform.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.ailk.bi.base.common.InitServlet;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.bulletin.dao.IBulletinDao;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.dbtools.DAOFactory;
import com.ailk.bi.marketing.service.IActivityService;
import com.ailk.bi.marketing.service.IProjectService;
import com.ailk.bi.marketing.service.ITacticService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.system.facade.impl.CommonFacade;
import com.ailk.bi.workplatform.entity.OrderInfo;
import com.ailk.bi.workplatform.service.IOrderInfoService;
import com.ailk.bi.workplatform.service.ITransService;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;
public class WorkFormAction extends HTMLActionSupport {

	/**
	 * 工作平台入口
	 */
	private static final long serialVersionUID = 1L;
	@Resource(name = "orderInfoService")
	private IOrderInfoService orderInfoService;
	@Resource(name = "tacticService")
	private ITacticService tacticService;
	@Resource(name = "projectService")
	private IProjectService projectService;
	@Resource(name = "activityService")
	private IActivityService activityService;
	@Resource(name = "transService")
	private ITransService transService;


	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 判断用户是否有效登陆
		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
			return;
		HttpSession session = request.getSession();
		InfoOperTable loginUser = CommonFacade.getLoginUser(session);

		// 业务对象
		InitServlet.init(super.config, this, "orderInfoService");
		InitServlet.init(super.config, this, "tacticService");
		InitServlet.init(super.config, this, "projectService");
		InitServlet.init(super.config, this, "activityService");
		InitServlet.init(super.config, this, "transService");


		// 获取今日工单内容
		if (orderInfoService != null) {
			OrderInfo entity = new OrderInfo();

			// 执行日期
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dt = format.format(DateUtil.getNowDate());
			Date dd = java.sql.Date.valueOf(dt);
			entity.setContact_start_date(dd);
			// 工单状态，待办
			entity.setOrder_state(1);

			List<OrderInfo> orderInfo = orderInfoService.getTodayOrderList(entity, 5);
			request.setAttribute("platformOrderInfo", orderInfo);
		}

		// 获取协同工单内容
		if (orderInfoService != null) {
			String[][] orderInfo = orderInfoService.getOutChannelOrderList(5);
			request.setAttribute("platformOutChannelOrderInfo", orderInfo);
		}

		// 获取二次回访工单内容
		if (orderInfoService != null) {
			OrderInfo entity = new OrderInfo();

			// 再次执行日期
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dt = format.format(DateUtil.getNowDate());
			Date dd = java.sql.Date.valueOf(dt);
			entity.setNext_contact_date(dd);
			// 工单状态，待办
			entity.setOrder_state(1);

			List<OrderInfo> orderInfo = orderInfoService.getTwiceTodayOrderList(entity, 5);
			request.setAttribute("platformTwiceTodayOrderInfo", orderInfo);
		}

		// 获取所有活动工单汇总
		if (orderInfoService != null) {
			String[][] orderInfo = orderInfoService.getAllOrderList();
			request.setAttribute("platformAllOrderInfo", orderInfo);
		}

		/**
		 * 获取待办任务列表
		 **/
		int tacticcount= tacticService.getCountForPlat(loginUser.user_id);
		request.setAttribute("tacticCount", tacticcount+"");
			/**
			 * 获取待办方案列表
			 **/
		int projctcount= projectService.getCountForPlat(loginUser.user_id);
		request.setAttribute("projectCount", projctcount+"");
			/**
			 * 获取待办活动列表
			 **/
		int activityCount= activityService.getCountForPlat(loginUser.user_id);
		request.setAttribute("activityCount", activityCount+"");
		/**
		 * 获取渠道转派列表(我创建)
		 **/
    	int creatorCount= transService.getCountForCreator(loginUser.user_id);
	    request.setAttribute("creatorCount", creatorCount+"");
		/**
		 * 获取渠道转派列表(我审批)
		 **/
    	int transCount= transService.getCountForTrans(loginUser.user_id);
	    request.setAttribute("transCount", transCount+"");

		// 获取公告内容
		ReportQryStruct qryStruct = new ReportQryStruct();
		qryStruct.dim3 = loginUser.user_id;
		qryStruct.dim4 = loginUser.group_id;
		qryStruct.dim5 = "1";
		IBulletinDao sysDao = DAOFactory.getBulletinDao();
		String[][] newsArr = sysDao.qryMartBulletinInfoList(qryStruct, true);
		session.setAttribute("WorkPlatFormNews", newsArr);
		setNextScreen(request, "workPlatForm.screen");
	}
}
