package com.ailk.bi.system.action;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.struct.InfoResStruct;
import com.ailk.bi.base.struct.ParamMonitorStruct;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.SecurityTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "rawtypes" })
public class ParamMonitorHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2018249568562978108L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		// 变量申明区
		HttpSession session = request.getSession();
		ParamMonitorStruct params[] = null;

		String sql = "";
		// 程序判断区
		String operType = request.getParameter("oper_type");
		if (operType == null || "".equals(operType)) {
			operType = "0";
		}
		if ("0".equals(operType)) {
			// 列出参数列表
			try {
				sql = SQLGenator.genSQL("Q0036");
				System.out.println("Q0036=========" + sql);
				Vector v = WebDBUtil.execQryVector(sql, "");
				if (v != null && v.size() > 0) {
					params = new ParamMonitorStruct[v.size()];
				}
				for (int i = 0; v != null && i < v.size(); i++) {
					Vector temp = (Vector) v.get(i);
					params[i] = new ParamMonitorStruct();
					params[i].task_id = (String) temp.get(0);
					params[i].task_name = (String) temp.get(1);
					params[i].task_desc = (String) temp.get(2);
					params[i].task_status = (String) temp.get(3);

				}
			} catch (AppException ex) {
				ex.printStackTrace();
			}
			session.removeAttribute(WebKeys.ATTR_ParamMonitorStruct);
			session.setAttribute(WebKeys.ATTR_ParamMonitorStruct, params);
			setNextScreen(request, "ParamMonitor.screen");

		} else {
			// 刷新参数
			String param_type = request.getParameter("param_type");
			if (param_type == null || "".equals(param_type)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "缺失参数类型！");
			} else {
				if ("REGION".equals(param_type.toUpperCase())) {

					HashMap rgmap = SecurityTool.getConstMap("REGION");
					context.removeAttribute(WebConstKeys.ATTR_C_REGION_ID_VS_NAME);
					context.setAttribute(WebConstKeys.ATTR_C_REGION_ID_VS_NAME,
							rgmap);

				} else if ("DEPT".equals(param_type.toUpperCase())) {

					HashMap dpmap = SecurityTool.getConstMap("DEPT");
					context.removeAttribute(WebConstKeys.ATTR_C_DEPT_ID_VS_NAME);
					context.setAttribute(WebConstKeys.ATTR_C_DEPT_ID_VS_NAME,
							dpmap);

				} else if ("BRAND".equals(param_type.toUpperCase())) {

					HashMap brandmap = CommTool.getConstMap("BRAND");
					context.removeAttribute(WebConstKeys.ATTR_C_BRAND_ID_VS_NAME);
					context.setAttribute(WebConstKeys.ATTR_C_BRAND_ID_VS_NAME,
							brandmap);

				} else if ("PRODUCT".equals(param_type.toUpperCase())) {

					HashMap productmap = CommTool.getConstMap("PRODUCT");
					context.removeAttribute(WebConstKeys.ATTR_C_PRODUCT_ID_VS_NAME);
					context.setAttribute(
							WebConstKeys.ATTR_C_PRODUCT_ID_VS_NAME, productmap);

				} else if ("SVC_KND".equals(param_type.toUpperCase())) {

					HashMap svcmap = CommTool.getConstMap("SVC_KND");
					context.removeAttribute(WebConstKeys.ATTR_C_SVC_ID_VS_NAME);
					context.setAttribute(WebConstKeys.ATTR_C_SVC_ID_VS_NAME,
							svcmap);

				} else if ("MAC".equals(param_type.toUpperCase())) {

					HashMap macmap = CommTool.getConstMap("MAC_TYPE");
					context.removeAttribute(WebConstKeys.ATTR_C_MAC_ID_VS_NAME);
					context.setAttribute(WebConstKeys.ATTR_C_MAC_ID_VS_NAME,
							macmap);

				} else if ("SALE".equals(param_type.toUpperCase())) {

					HashMap salemap = CommTool.getConstMap("SALE_TYPE");
					context.removeAttribute(WebConstKeys.ATTR_C_SALE_ID_VS_NAME);
					context.setAttribute(WebConstKeys.ATTR_C_SALE_ID_VS_NAME,
							salemap);

				} else if ("VIP".equals(param_type.toUpperCase())) {

					HashMap vipmap = CommTool.getConstMap("VIP");
					context.removeAttribute(WebConstKeys.ATTR_C_VIP_ID_VS_NAME);
					context.setAttribute(WebConstKeys.ATTR_C_VIP_ID_VS_NAME,
							vipmap);

				} else if ("DURA".equals(param_type.toUpperCase())) {

					HashMap duramap = CommTool.getConstMap("DURA");
					context.removeAttribute(WebConstKeys.ATTR_C_DURA_ID_VS_NAME);
					context.setAttribute(WebConstKeys.ATTR_C_DURA_ID_VS_NAME,
							duramap);

				} else if ("CHANNEL".equals(param_type.toUpperCase())) {

					HashMap channelmap = CommTool.getConstMap("CHANNEL");
					context.removeAttribute(WebConstKeys.ATTR_C_CHANNEL_ID_VS_NAME);
					context.setAttribute(
							WebConstKeys.ATTR_C_CHANNEL_ID_VS_NAME, channelmap);

				} else if ("SECURITY".equals(param_type.toUpperCase())) {

					// 统一资源表信息资源ID和描述初始化MAP*
					SecurityTool.setAllResConstsKeyValue(context);
					// 系统所有资源信息*
					InfoResStruct[] infoRes = CommTool.getInfoResStruct();
					context.removeAttribute(WebConstKeys.ATTR_C_INFORESSTRUCT);
					context.setAttribute(WebConstKeys.ATTR_C_INFORESSTRUCT,
							infoRes);

				}

			}

			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "刷新参数成功！", "ParamMonitor.do");

		}

	}

}
