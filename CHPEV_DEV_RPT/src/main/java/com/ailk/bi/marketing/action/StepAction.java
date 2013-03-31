package com.ailk.bi.marketing.action;
import java.util.ArrayList;
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
import com.ailk.bi.marketing.entity.QuestionInfo;
import com.ailk.bi.marketing.entity.SurveyInfo;
import com.ailk.bi.marketing.service.IQuestionService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.system.facade.impl.CommonFacade;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

/**
 * 实现对营销目标增删改查的控制 【action控制层】活动目标控制层
 *
 * @author 方慧
 * @version [版本号, 2012-04-10]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class StepAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;

	@Resource(name = "questionService")
	private IQuestionService questionService;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 判断用户是否有效登陆
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();
		InfoOperTable loginUser = CommonFacade.getLoginUser(session);
		// 获取页面screen标示
		String optype = request.getParameter("optype");// 获得制定的的JSP页面
		String doType = request.getParameter("doType");// 获得具体的操作
		String doStep = request.getParameter("doStep");// 获得创造作步骤

		// 获得所有的查询条件（保存的之在save方法中）
		String[] arr = request.getParameterValues("kexuan");
		if (optype == null || "".equals(optype)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "未知界面查询操作，请检查！");
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
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "提取界面查询信息失败，请注意是否登陆超时！");
		}
		// 加入权限控制条件-用户控制区域
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
				.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		if (ctlStruct == null) {
			ctlStruct = new UserCtlRegionStruct();
		}
		/**
		 * 业务开始
		 *
		 * @author f00211612
		 * */
		@SuppressWarnings("unchecked")
		List<QuestionInfo> list1 = (List<QuestionInfo>) session.getAttribute("questionList1");
		@SuppressWarnings("unchecked")
		List<QuestionInfo> list2 = (List<QuestionInfo>) session.getAttribute("questionList2");
		InitServlet.init(super.config, this, "questionService");
		if(null!=questionService){
			if("step1".equals(doStep)){
				if (list1 == null) {
					list1 = new ArrayList<QuestionInfo>();
				}
				if (list2 == null) {
					list2 = new ArrayList<QuestionInfo>();
				}
				if (null != arr) {
					for (int i = 0; i < arr.length; i++) {
						if (!StringTool.checkEmptyString(arr[i])) {
							QuestionInfo info = questionService.getById(Integer
									.parseInt(arr[i]));

							if (!StringTool.checkEmptyString(doType)) {
								if ("toAdd".equals(doType)) {
									int index = IsExist(list1, info.getQuestionId());
									if(index>=0){
										list1.remove(index);
									}
									list2.add(info);
								} else {
									int index = IsExist(list2, info.getQuestionId());
									if(index>=0){
										list2.remove(index);
									}
									list1.add(info);
								}
							}
						}
					}
				}
				session.setAttribute("questionList1", list1);
				session.setAttribute("questionList2", list2);
			}else if("step2".equals(optype)){
				String  survey_name  = request.getParameter("txt_survey_name");
				String  survey_type  = request.getParameter("txt_survey_type");
				 SurveyInfo info =(SurveyInfo) session.getAttribute("addSurvey");
				 if(null==info){
						 info = new SurveyInfo();
				 }


				if (!StringTool.checkEmptyString(survey_name)) {
					qryStruct.step1_survey_name=survey_name;
					info.setSurveyName(survey_name);
				}
				if (!StringTool.checkEmptyString(survey_type)) {
					qryStruct.step1_survey_type=survey_type;
					info.setSurveyType(Integer.parseInt(survey_type));
				}
				Date date = new Date();
				info.setCreateDate(date);
				info.setCreator(loginUser.user_id);
				info.setState(0);
				session.setAttribute("addSurvey", info);
		  	}
		}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype + ".screen");
	}
	public int IsExist(List<QuestionInfo> list, int id){
		for(int i=0;i<list.size();i++){
			if(list.get(i).getQuestionId()==id){
				return i;
			}
		}
		return -1;
	}
}
