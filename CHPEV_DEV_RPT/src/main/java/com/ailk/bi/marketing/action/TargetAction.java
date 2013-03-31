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
import com.ailk.bi.marketing.entity.TargetInfo;
import com.ailk.bi.marketing.service.ITargetService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.system.facade.impl.CommonFacade;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class TargetAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	@Resource(name = "targetService")
	private ITargetService targetService;

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
		String targetNmae = request.getParameter("qry__target_name");
		String targetType = request.getParameter("qry__target_type");
		String targetState = request.getParameter("qry__target_state");
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
		if (StringTool.checkEmptyString(qryStruct.target_name)) {
			qryStruct.target_name = targetNmae;
		}
		if (StringTool.checkEmptyString(qryStruct.target_state)) {
			qryStruct.target_state = targetState;
		}
		if (StringTool.checkEmptyString(qryStruct.target_type)) {
			qryStruct.target_type = targetType;
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
		InitServlet.init(super.config, this, "targetService");
		if (null != targetService) {
			TargetInfo entity = new TargetInfo();
			if (!StringTool.checkEmptyString(targetNmae)) {
				entity.setTargetName(targetNmae);
			}
			if (!StringTool.checkEmptyString(targetType)) {
				entity.setTargetType(Integer.parseInt(targetType));
			}else{
				entity.setTargetType(-999);
			}
			if (!StringTool.checkEmptyString(targetState)) {
				entity.setState(Integer.parseInt(targetState));
			}else{
				entity.setState(-999);
			}
			if ("search".equals(doType)) {
				List<TargetInfo> list = targetService.getAll(entity, 0);
				request.setAttribute("targetList", list);
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
				boolean delResult = targetService.delete(ids);
				//删除后重新刷新
				List<TargetInfo> list = targetService.getAll(entity, 0);
				request.setAttribute("targetList", list);
				request.setAttribute("delResult", delResult);
			}else if("add".equals(doType)) {
				TargetInfo targetInfo = new TargetInfo();
				Date date = new Date();
				targetInfo.setCreateDate(date);
				targetInfo.setCreator(loginUser.user_id);
				session.setAttribute("targetInfo", targetInfo);
			}else if("modify".equals(doType)) {
				TargetInfo targetInfo =null;
				String targetId = request.getParameter("targetId");
				if (!StringTool.checkEmptyString(targetId)) {
					targetInfo = targetService.getById(Integer.parseInt(targetId));
				}
				session.setAttribute("targetInfo", targetInfo);
			}else if("save".equals(doType)) {
				TargetInfo targetInfo = (TargetInfo) session.getAttribute("targetInfo");
				String name = request.getParameter("txt_targetName");
				String typeId = request.getParameter("target_type");
				String creator = request.getParameter("txt_creator");
				String content = request.getParameter("txt_content");
				String unit = request.getParameter("txt_nuit");
				if (!StringTool.checkEmptyString(name)) {
					targetInfo.setTargetName(name);
				}
				if (!StringTool.checkEmptyString(unit)) {
					targetInfo.setUnit(Integer.parseInt(unit));
				}
				if (!StringTool.checkEmptyString(typeId)) {
					targetInfo.setTargetType(Integer.parseInt(typeId));
				}
				if (!StringTool.checkEmptyString(creator)) {
					targetInfo.setContent(content);
				}
				if (!StringTool.checkEmptyString(content)) {
					targetInfo.setCreator(creator);
				}
				targetInfo.setState(2);//默认状态为通过审批（指标不做审批处理）
				boolean  addResult = targetService.save(targetInfo);
				//保存后重新刷新
				List<TargetInfo> list = targetService.getAll(entity, 0);
				request.setAttribute("targetList", list);
				request.setAttribute("addResult", addResult);
			}
		}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
