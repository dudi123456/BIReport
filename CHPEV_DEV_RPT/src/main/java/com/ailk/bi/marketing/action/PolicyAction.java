package com.ailk.bi.marketing.action;
/**
 *实现对营销目标增删改查的控制
 * 【action控制层】活动目标控制层
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.ailk.bi.marketing.entity.PolicyInfo;
import com.ailk.bi.marketing.service.IPolicyService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.system.facade.impl.CommonFacade;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class PolicyAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	@Resource(name = "policyService")
	private IPolicyService policyService;

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
		String policyName = request.getParameter("qry__policy_name");
		String policyType = request.getParameter("qry__policy_type");
		String policyState = request.getParameter("qry__policy_state");
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
		if (StringTool.checkEmptyString(qryStruct.policy_name)) {
			qryStruct.policy_name = policyName;
		}
		if (StringTool.checkEmptyString(qryStruct.policy_state)) {
			qryStruct.policy_state = policyState;
		}
		if (StringTool.checkEmptyString(qryStruct.policy_type)) {
			qryStruct.policy_type = policyType;
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
		InitServlet.init(super.config, this, "policyService");
		if (null != policyService) {
			PolicyInfo entity = new PolicyInfo();
			if (!StringTool.checkEmptyString(policyName)) {
				entity.setPolicyName(policyName);
			}
			if (!StringTool.checkEmptyString(policyType)) {
				entity.setPolicyType(Integer.parseInt(policyType));
			}else{
				entity.setPolicyType(-999);
			}
			if (!StringTool.checkEmptyString(policyState)) {
				entity.setState(Integer.parseInt(policyState));
			}else{
				entity.setState(-999);
			}
			if ("search".equals(doType)) {
				List<PolicyInfo> list = policyService.getAll(entity, 0);
				request.setAttribute("policyList", list);
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
				policyService.delete(ids);
				List<PolicyInfo> list = policyService.getAll(entity, 0);
				request.setAttribute("policyList", list);
			}else if("add".equals(doType)) {
				PolicyInfo poInfo = new PolicyInfo();
				Date date = new Date();
				poInfo.setCreateDate(date);
				poInfo.setCreator(loginUser.user_id);
				session.setAttribute("policyList", poInfo);
			}else if("modify".equals(doType)) {
				PolicyInfo ppnfo =null;
				String msgId = request.getParameter("policyId");
				if (!StringTool.checkEmptyString(msgId)) {
					ppnfo = policyService.getById(Integer.parseInt(msgId));
				}
				request.setAttribute("policy", ppnfo);
				session.setAttribute("policyList", ppnfo);
			}else if("save".equals(doType)) {
				PolicyInfo ppp = (PolicyInfo) session.getAttribute("policyList");
				String type = request.getParameter("txt__policy_type");
				String content = request.getParameter("txt__policy_content");
				String name = request.getParameter("txt__policy_name");
				String d1 = request.getParameter("txt_date01");
				String d2 = request.getParameter("txt_date02");

				if (!StringTool.checkEmptyString(type)) {
					ppp.setPolicyType(Integer.parseInt(type));
				}
				if (!StringTool.checkEmptyString(content)) {
					ppp.setContent(content);
				}
				if (!StringTool.checkEmptyString(name)) {
					ppp.setPolicyName(name);
				}

				if (!StringTool.checkEmptyString(d1)) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date dd = new Date();
					try {
						dd = df.parse(d1);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					ppp.setEffectDate(dd);
				}

				if (!StringTool.checkEmptyString(d2)) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date dd = new Date();
					try {
						dd = df.parse(d2);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					ppp.setInvaildDate(dd);
				}
				ppp.setState(0);//默认状态为等待审批
				policyService.save(ppp);
				//保存后重新刷新
				List<PolicyInfo> list = policyService.getAll(null, 0);
				request.setAttribute("policyList", list);
				request.setAttribute("addResult", "Refresh");
			}
		}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
