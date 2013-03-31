package com.ailk.bi.marketing.action;
/**
 *实现对营销目标增删改查的控制
 * 【action控制层】活动目标控制层
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
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
import com.ailk.bi.marketing.service.IQuestionService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class QuestionAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	@Resource(name = "questionService")
	private IQuestionService questionService;

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
		String questionContent = request.getParameter("qry__question_content");
		String questionType = request.getParameter("qry__question_type");
		String questionState = request.getParameter("qry__question_state");
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
		if (StringTool.checkEmptyString(qryStruct.question_content)) {
			qryStruct.question_content = questionContent;
		}
		if (StringTool.checkEmptyString(qryStruct.question_state)) {
			qryStruct.question_state = questionState;
		}
		if (StringTool.checkEmptyString(qryStruct.question_type)) {
			qryStruct.question_type =questionType;
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
		InitServlet.init(super.config, this, "questionService");
		if (null != questionService) {
			QuestionInfo entity = new QuestionInfo();
			if (!StringTool.checkEmptyString(questionContent)) {
				entity.setContent(questionContent);
			}
			if (!StringTool.checkEmptyString(questionType)) {
				entity.setQuestionType(Integer.parseInt(questionType));
			}else{
				entity.setQuestionType(-999);
			}
			if (!StringTool.checkEmptyString(questionState)) {
				entity.setState(Integer.parseInt(questionState));
			}else{
				entity.setState(-999);
			}
			if ("search".equals(doType)) {
				List<QuestionInfo> list = questionService.getAll(entity, 0);
				request.setAttribute("questionList", list);
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
				boolean delResult = questionService.delete(ids);
				//删除后重新刷新
				List<QuestionInfo> list = questionService.getAll(entity, 0);
				request.setAttribute("questionList", list);
				request.setAttribute("delResult", delResult);
			}else if("add".equals(doType)) {
				QuestionInfo questionInfo = new QuestionInfo();
				Date date = new Date();
				questionInfo.setCreateDate(date);
				questionInfo.setCreator(loginUser.user_id);
				session.setAttribute("QuestionInfo", questionInfo);
			}else if("modify".equals(doType)) {
				QuestionInfo questionInfo =null;
				String questionId = request.getParameter("questiontId");
				if (!StringTool.checkEmptyString(questionId)) {
					questionInfo = questionService.getById(Integer.parseInt(questionId));
				}
				session.setAttribute("QuestionInfo", questionInfo);
			}else if("save".equals(doType)) {
				QuestionInfo questionInfo = (QuestionInfo) session.getAttribute("QuestionInfo");
				String content = request.getParameter("txt_questionContnt");
				String type = request.getParameter("txt_question_type");
				String desc = request.getParameter("txt_desc");
				if (!StringTool.checkEmptyString(content)) {
					questionInfo.setContent(content);
				}
				if (!StringTool.checkEmptyString(type)) {
					questionInfo.setQuestionType(Integer.parseInt(type));
				}
				if (!StringTool.checkEmptyString(desc)) {
					questionInfo.setDesc(desc);
				}
				questionInfo.setState(0);//默认状态为等待审批
				boolean  addResult = questionService.save(questionInfo);
				//保存后重新刷新
				List<QuestionInfo> list = questionService.getAll(entity, 0);
				request.setAttribute("questionList", list);
				request.setAttribute("addResult", addResult);
			}
		}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
