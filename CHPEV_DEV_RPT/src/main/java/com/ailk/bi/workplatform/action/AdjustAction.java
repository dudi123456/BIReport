package com.ailk.bi.workplatform.action;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.ailk.bi.base.common.InitServlet;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.workplatform.entity.OrderAdjustInfo;
import com.ailk.bi.workplatform.entity.UserInfo;
import com.ailk.bi.workplatform.service.IAdjustService;
import com.ailk.bi.workplatform.service.IUserService;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;
/**
 *实现对营销目标增删改查的控制
 * 【action控制层】活动目标控制层
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AdjustAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	@Resource(name = "adjustService")
	private IAdjustService adjustService;

	@Resource(name = "userService")
	private IUserService userService;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 判断用户是否有效登陆
		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
			return;
		HttpSession session = request.getSession();
		//InfoOperTable loginUser = CommonFacade.getLoginUser(session);
		// 获取页面screen标示
		String optype = request.getParameter("optype");//获得制定的的JSP页面
		String doType = request.getParameter("doType");//获得具体的操作
		if (optype == null || "".equals(optype)) {
			throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "未知界面查询操作，请检查！");
		}
		InitServlet.init(super.config, this, "userService");
		//以下是获得查询类表页面的查询条件//
		String adjustUser = request.getParameter("adjustUser");
		String adjustCreator = request.getParameter("adjustCreator");
		String adjustState = request.getParameter("adjustState");
		//创建查询条件
		OrderAdjustInfo entity = new OrderAdjustInfo();
		if (!StringTool.checkEmptyString(adjustUser)) {
			UserInfo uu = userService.getById(adjustUser);
			entity.setPerformer(uu);
		}
		if (!StringTool.checkEmptyString(adjustCreator)) {
			UserInfo uu = userService.getById(adjustCreator);
			entity.setUpdatePersonal(uu);
		}
		if (!StringTool.checkEmptyString(adjustState)) {
			entity.setStatus(Integer.parseInt(adjustState));
		}else	{
			entity.setStatus(-999);
		}
		// 查询结构，接受界面条件值
		ReportQryStruct qryStruct = new ReportQryStruct();
		// 判断是否有外部传入条件，p_condition约定名称
		String p_condition = request.getParameter("p_condition");
		if (StringTool.checkEmptyString(p_condition)) {
			p_condition = ReportConsts.NO;
		}
		try {
			if (ReportConsts.YES.equals(p_condition)) {
				qryStruct = (ReportQryStruct) session
						.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
			} else {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			}
		} catch (AppException ex) {
			throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
					"提取界面查询信息失败，请注意是否登陆超时！");
		}

		// 加入权限控制条件-用户控制区域
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
				.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		if (ctlStruct == null) {
			ctlStruct = new UserCtlRegionStruct();
		}
		if (StringTool.checkEmptyString(qryStruct.adjustUser)) {
			qryStruct.adjustUser = adjustUser;
		}
		if (StringTool.checkEmptyString(qryStruct.adjustCreator)) {
			qryStruct.adjustCreator = adjustCreator;
		}
		if (StringTool.checkEmptyString(qryStruct.adjustState)) {
			qryStruct.adjustState = adjustState;
		}
		/**
		 * 业务开始
		 *@author f00211612
		 * */
		InitServlet.init(super.config, this, "adjustService");

		if (null != adjustService) {
			 if("search".equals(doType)) {
				List<OrderAdjustInfo> list = adjustService.getAll(entity, 0);
				request.setAttribute("adjustList", list);
			}else if("newActivity".equals(doType)) {
			}
		}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
