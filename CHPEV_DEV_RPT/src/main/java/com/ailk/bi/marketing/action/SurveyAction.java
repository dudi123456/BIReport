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
import com.ailk.bi.marketing.entity.QuestionRelationInfo;
import com.ailk.bi.marketing.entity.SurveyInfo;
import com.ailk.bi.marketing.service.IQuestionRelationService;
import com.ailk.bi.marketing.service.IQuestionService;
import com.ailk.bi.marketing.service.ISurveyService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.system.facade.impl.CommonFacade;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;
/**
 *
 * 【action控制层】调查问卷
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SurveyAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	@Resource(name = "surveyService")
	private ISurveyService surveyService;

	@Resource(name = "questionService")
	private IQuestionService questionService;

	@Resource(name = "questionRelationService")
	private IQuestionRelationService questionRelationService;

	public int IsExist(List<QuestionInfo> list, int id){
		for(int i=0;i<list.size();i++){
			if(list.get(i).getQuestionId()==id){
				return i;
			}
		}
		return -1;
	}

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
		String doStep = request.getParameter("doStep");//获得具体的操作
		String surveyName = request.getParameter("qry__survey_name");
		String surveyType = request.getParameter("qry__survey_type");
		String surveyState = request.getParameter("qry__survey_state");
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
		if (StringTool.checkEmptyString(qryStruct.survey_name)) {
			qryStruct.survey_name = surveyName;
		}
		if (StringTool.checkEmptyString(qryStruct.survey_state)) {
			qryStruct.survey_state = surveyState;
		}
		if (StringTool.checkEmptyString(qryStruct.policy_type)) {
			qryStruct.survey_type = surveyType;
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

		InitServlet.init(super.config, this, "surveyService");
		InitServlet.init(super.config, this, "questionService");
		InitServlet.init(super.config, this, "questionRelationService");


		if (null != surveyService) {
			SurveyInfo entity = new SurveyInfo();
			if (!StringTool.checkEmptyString(surveyName)) {
				entity.setSurveyName(surveyName);
			}
			if (!StringTool.checkEmptyString(surveyType)) {
				entity.setSurveyType(Integer.parseInt(surveyType));
			}else{
				entity.setSurveyType(-999);
			}
			if (!StringTool.checkEmptyString(surveyState)) {
				entity.setState(Integer.parseInt(surveyState));
			}else{
				entity.setState(-999);
			}
			if ("search".equals(doType)) {
				List<SurveyInfo> list = surveyService.getAll(entity, 0);
				request.setAttribute("surveyList", list);
			}else if("delect".equals(doType)) {
				String ids = "";
				String[] arr = request.getParameterValues("checkbox");
				if(null!=arr){
					for(int i=0;i<arr.length;i++){
						ids+=arr[i]+",";
					}
				}
				char lastChar =',';
				if(ids.charAt(ids.length()-1)==lastChar)
				{
					ids = ids.substring(0, ids.length()-1);
				}
				//删除后重新刷新
				surveyService.delete(ids);
				List<SurveyInfo> list = surveyService.getAll(entity, 0);
				request.setAttribute("surveyList", list);
			}else if("add".equals(doType)) {
				if (!StringTool.checkEmptyString(doStep)) {
					if("step1".equals(doStep))
					{
						//步骤一中查出所思有审批通过的问题
						QuestionInfo qq = new QuestionInfo();
						qq.setQuestionType(-999);
						qq.setState(1);
						List<QuestionInfo>list = 	questionService.getAll(qq, 0);
						session.setAttribute("questionList1", list);
						session.setAttribute("questionList2", null);
					}
			}
				SurveyInfo poInfo = new SurveyInfo();
				Date date = new Date();
				poInfo.setCreateDate(date);
				poInfo.setCreator(loginUser.user_id);
				session.setAttribute("addSurvey", poInfo);
			}else if("modify".equals(doType)) {
				SurveyInfo ssInfo =null;
				String Id = request.getParameter("surveyId");
				if (!StringTool.checkEmptyString(Id)) {
					ssInfo = surveyService.getById(Integer.parseInt(Id));
				}
				List<QuestionRelationInfo> list = questionRelationService.getAllBySurveyId(Integer.parseInt(Id));
				List<QuestionInfo>list2= 	new ArrayList<QuestionInfo>();
				for(int i=0;i<list.size();i++){
					list2.add(list.get(i).getQuestionInfo());
				}
				session.setAttribute("questionList2", list2);
				//步骤一中查出所思有审批通过的问题
				QuestionInfo qq = new QuestionInfo();
				qq.setQuestionType(-999);
				qq.setState(1);
				List<QuestionInfo>list1 = 	questionService.getAll(qq, 0);
				 for(int i=0;i<list2.size();i++){
						int index = IsExist(list1, list2.get(i).getQuestionId());
						if(index>=0){
							list1.remove(index);
						}
				 }
				session.setAttribute("questionList1", list1);
				qryStruct.step1_survey_name=ssInfo.getSurveyName();
				qryStruct.step1_survey_type = String.valueOf(ssInfo.getSurveyType());
				session.setAttribute("addSurvey", ssInfo);
			}else if("save".equals(doType)) {
			 SurveyInfo info =(SurveyInfo) session.getAttribute("addSurvey");
			 if(null!=info){
				 surveyService.save(info);
				 int id =  info.getSurveyId();
				@SuppressWarnings("unchecked")
				List<QuestionInfo> list2 = (List<QuestionInfo>) session.getAttribute("questionList2");
				questionRelationService.delete(String.valueOf(info.getSurveyId()));
				for (int i=0;i<list2.size();i++){
					QuestionRelationInfo qr = new  QuestionRelationInfo();
					qr.setOrder(i);
					qr.setQuestionInfo(list2.get(i));
					qr.setSurveyId(id);
					questionRelationService.save(qr);
				}
			 }
			 session.setAttribute("questionList1",null);
			 session.setAttribute("questionList2",null);
			 List<SurveyInfo> list = surveyService.getAll(entity, 0);
		     request.setAttribute("surveyList", list);
			}
		}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
