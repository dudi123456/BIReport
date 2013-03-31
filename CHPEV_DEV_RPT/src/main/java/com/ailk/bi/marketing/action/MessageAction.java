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
import com.ailk.bi.marketing.entity.MessageInfo;
import com.ailk.bi.marketing.service.IMsgService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class MessageAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	@Resource(name = "msgService")
	private IMsgService msgService;

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
		String msgContent = request.getParameter("qry__msg_content");
		String msgType = request.getParameter("qry__msg_type");
		String msgState = request.getParameter("qry__msg_state");
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
		if (StringTool.checkEmptyString(qryStruct.msg_content)) {
			qryStruct.msg_content = msgContent;
		}
		if (StringTool.checkEmptyString(qryStruct.msg_state)) {
			qryStruct.msg_state = msgState;
		}
		if (StringTool.checkEmptyString(qryStruct.msg_type)) {
			qryStruct.msg_type = msgType;
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
		InitServlet.init(super.config, this, "msgService");
		if (null != msgService) {
			MessageInfo entity = new MessageInfo();
			if (!StringTool.checkEmptyString(msgContent)) {
				entity.setContent(msgContent);
			}
			if (!StringTool.checkEmptyString(msgType)) {
				entity.setMsgType(Integer.parseInt(msgType));
			}else{
				entity.setMsgType(-999);
			}
			if (!StringTool.checkEmptyString(msgState)) {
				entity.setMsgState(Integer.parseInt(msgState));
			}else{
				entity.setMsgState(-999);
			}
			if ("search".equals(doType)) {
				List<MessageInfo> list = msgService.getAll(entity, 0);
				request.setAttribute("msgList", list);
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
				msgService.delete(ids);
				List<MessageInfo> list = msgService.getAll(entity, 0);
				request.setAttribute("msgList", list);
			}else if("add".equals(doType)) {
				MessageInfo msgInfo = new MessageInfo();
				Date date = new Date();
				msgInfo.setCreateTime(date);
				msgInfo.setCreator(loginUser.user_id);
				session.setAttribute("msgList", msgInfo);
			}else if("modify".equals(doType)) {
				MessageInfo msgInfo =null;
				String msgId = request.getParameter("msgId");
				if (!StringTool.checkEmptyString(msgId)) {
					msgInfo = msgService.getById(Integer.parseInt(msgId));
				}
				request.setAttribute("msgInfo", msgInfo);
				session.setAttribute("msgList", msgInfo);
			}else if("save".equals(doType)) {
				MessageInfo msgInfo = (MessageInfo) session.getAttribute("msgList");
				String type = request.getParameter("txt__msg_type");
				String content = request.getParameter("txt__msg_content");

				if (!StringTool.checkEmptyString(type)) {
					msgInfo.setMsgType(Integer.parseInt(type));
				}
				if (!StringTool.checkEmptyString(content)) {
					msgInfo.setContent(content);
				}
				msgInfo.setMsgState(0);//默认状态为等待审批
				msgService.save(msgInfo);
				//保存后重新刷新
				List<MessageInfo> list = msgService.getAll(msgInfo, 0);
				request.setAttribute("msgList", list);
				request.setAttribute("addResult", "Refresh");
			}
		}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
