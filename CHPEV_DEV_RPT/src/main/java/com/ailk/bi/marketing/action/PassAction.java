package com.ailk.bi.marketing.action;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.ailk.bi.base.common.InitServlet;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.marketing.entity.ActivityInfo;
import com.ailk.bi.marketing.entity.PassInfo;
import com.ailk.bi.marketing.entity.ProjectInfo;
import com.ailk.bi.marketing.entity.TacticInfo;
import com.ailk.bi.marketing.service.IActivityService;
import com.ailk.bi.marketing.service.IPassService;
import com.ailk.bi.marketing.service.IProjectService;
import com.ailk.bi.marketing.service.ITacticService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.system.facade.impl.CommonFacade;
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
public class PassAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	@Resource(name = "passService")
	private IPassService passService;
	@Resource(name = "projectService")
	private IProjectService projectService;
	@Resource(name = "activityService")
	private IActivityService activityService;
	@Resource(name = "tacticService")
	private ITacticService tacticService;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 判断用户是否有效登陆
		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
			return;
		HttpSession session = request.getSession();
		InfoOperTable loginUser = CommonFacade.getLoginUser(session);
		// 获取页面screen标示
		String optype = request.getParameter("optype");//获得制定的的JSP页面
		String doType = request.getParameter("doType");//获得具体的操作
		String step = request.getParameter("step");//获得具体的操作
		//获得所有的查询条件（保存的之在save方法中）
		String objectId = request.getParameter("object_id");
		String warnName = request.getParameter("warn_name");

		if (optype == null || "".equals(optype)) {
			throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "未知界面查询操作，请检查！");
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
		/**
		 * 业务开始
		 *@author f00211612
		 * */
		InitServlet.init(super.config, this, "passService");
		InitServlet.init(super.config, this, "projectService");
		InitServlet.init(super.config, this, "activityService");
		if (null != passService) {

			//策略审批开始
			if ("tactic".equals(doType)) {
				if (!StringTool.checkEmptyString(objectId)) {
					if("step1".equals(step)){
						TacticInfo info = tacticService.getById(Integer.parseInt(objectId));
						PassInfo passInfo = new PassInfo();
						if(null!=info){
							passInfo.setAdvice("暂无");
							Date date = new Date();
							passInfo.setCreateDate(date);
							passInfo.setCreator(loginUser.user_id);
							passInfo.setWarnName(warnName);
							passInfo.setStep(2);
							passInfo.setTypeId(info.getTacticId());
							passService.save(passInfo);
							List<PassInfo> passlist =  passService.getAllByTypeId(info.getTacticId());
							session.setAttribute("tacticPassList", passlist);
							//同时修改project的状态将0改为1
							info.setState(1);
							info.setDecider(warnName);
							tacticService.save(info);
							TacticInfo newinfo = tacticService.getById(Integer.parseInt(objectId));
							session.setAttribute("TacticInfo",newinfo);
						}
					}else if("step2".equals(step)){
						TacticInfo info = tacticService.getById(Integer.parseInt(objectId));
						PassInfo passInfo = new PassInfo();
						if(null!=info){
							String advice = request.getParameter("advice");
							String passState = request.getParameter("state");
							passInfo.setAdvice(advice);
							Date date = new Date();
							passInfo.setCreateDate(date);
							passInfo.setCreator(loginUser.user_id);
							passInfo.setWarnName(loginUser.user_id);
							if("2".equals(passState)){
								passInfo.setStep(3);
							}else{
								passInfo.setStep(4);
							}
							passInfo.setTypeId(info.getTacticId());
							passService.save(passInfo);
							List<PassInfo> passlist =  passService.getAllByTypeId(info.getTacticId());
							session.setAttribute("tacticPassList", passlist);
							//同时修改project的状态将1改为2或者-1
							if(null!=passState){
								info.setState(Integer.parseInt(passState));
							}
							tacticService.save(info);
							TacticInfo newinfo = tacticService.getById(info.getTacticId());
							session.setAttribute("TacticInfo",newinfo);
						}
					}
				}
			}
			//活动审批结束

			//活动审批开始
			if ("activity".equals(doType)) {
				if (!StringTool.checkEmptyString(objectId)) {
					if("step1".equals(step)){
						ActivityInfo info = activityService.getById(Integer.parseInt(objectId));
						PassInfo passInfo = new PassInfo();
						if(null!=info){
							passInfo.setAdvice("暂无");
							Date date = new Date();
							passInfo.setCreateDate(date);
							passInfo.setCreator(loginUser.user_id);
							passInfo.setWarnName(warnName);
							passInfo.setStep(2);
							passInfo.setTypeId(info.getActivityId());
							passService.save(passInfo);
							List<PassInfo> passlist =  passService.getAllByTypeId(info.getActivityId());
							session.setAttribute("activityPassList", passlist);
							//同时修改project的状态将0改为1
							info.setState(1);
							info.setDecider(warnName);
							activityService.save(info);
							ActivityInfo newinfo = activityService.getById(info.getActivityId());
							session.setAttribute("ActivityInfo",newinfo);
						}
					}else if("step2".equals(step)){
						ActivityInfo info = activityService.getById(Integer.parseInt(objectId));
						PassInfo passInfo = new PassInfo();
						if(null!=info){
							String advice = request.getParameter("advice");
							String passState = request.getParameter("state");
							passInfo.setAdvice(advice);
							Date date = new Date();
							passInfo.setCreateDate(date);
							passInfo.setCreator(loginUser.user_id);
							passInfo.setWarnName(loginUser.user_id);
							if("2".equals(passState)){
								passInfo.setStep(3);
							}else{
								passInfo.setStep(4);
							}
							passInfo.setTypeId(info.getActivityId());
							passService.save(passInfo);
							List<PassInfo> passlist =  passService.getAllByTypeId(info.getActivityId());
							session.setAttribute("activityPassList", passlist);
							//同时修改project的状态将1改为2或者-1
							if(null!=passState){
								info.setState(Integer.parseInt(passState));
							}
							activityService.save(info);
							ActivityInfo newinfo = activityService.getById(info.getActivityId());
							session.setAttribute("ActivityInfo",newinfo);
						}
					}
				}
			}
			//活动审批结束

			//方案审批开始
			if ("project".equals(doType)) {
				if (!StringTool.checkEmptyString(objectId)) {
					if("step1".equals(step)){
						ProjectInfo info = projectService.getById(Integer.parseInt(objectId));
						PassInfo passInfo = new PassInfo();
						if(null!=info){
							passInfo.setAdvice("暂无");
							Date date = new Date();
							passInfo.setCreateDate(date);
							passInfo.setCreator(loginUser.user_id);
							passInfo.setWarnName(warnName);
							passInfo.setStep(2);
							passInfo.setTypeId(info.getProjectId());
							passService.save(passInfo);
							List<PassInfo> passlist =  passService.getAllByTypeId(info.getProjectId());
							session.setAttribute("projectPassList", passlist);
							//同时修改project的状态将0改为1
							info.setState(1);
							info.setWarnName(warnName);
							projectService.save(info);
							ProjectInfo newinfo = projectService.getById(info.getProjectId());
							session.setAttribute("ProjectInfo",newinfo);
						}
					}else if("step2".equals(step)){
						ProjectInfo info = projectService.getById(Integer.parseInt(objectId));
						PassInfo passInfo = new PassInfo();
						if(null!=info){
							String advice = request.getParameter("advice");
							String passState = request.getParameter("state");
							passInfo.setAdvice(advice);
							Date date = new Date();
							passInfo.setCreateDate(date);
							passInfo.setCreator(loginUser.user_id);
							passInfo.setWarnName(loginUser.user_id);
							if("2".equals(passState)){
								passInfo.setStep(3);
							}else{
								passInfo.setStep(4);
							}
							passInfo.setTypeId(info.getProjectId());
							passService.save(passInfo);
							List<PassInfo> passlist =  passService.getAllByTypeId(info.getProjectId());
							session.setAttribute("projectPassList", passlist);
							//同时修改project的状态将1改为2或者-1
							if(null!=passState){
								info.setState(Integer.parseInt(passState));
							}
							projectService.save(info);
							ProjectInfo newinfo = projectService.getById(info.getProjectId());
							session.setAttribute("ProjectInfo",newinfo);
						}
					}
				}
			}
			//方案审批结束

		}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
