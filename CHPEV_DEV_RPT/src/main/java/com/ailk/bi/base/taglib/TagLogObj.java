package com.ailk.bi.base.taglib;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

@SuppressWarnings({ "rawtypes" })
public class TagLogObj extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String objID = null;

	private String logType = null;

	private String defaultValue = null;

	protected String tmpValue = null;

	public String getObjID() {
		return objID;
	}

	public String getLogType() {
		return logType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setObjID(String obj_id) {
		this.objID = obj_id;
	}

	public void setLogType(String log_type) {
		this.logType = log_type;
	}

	public void setDefaultValue(String value) {
		this.defaultValue = value;
	}

	public int doStartTag() throws JspException {

		try {
			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			if (request != null) {
				tmpValue = request.getParameter("SysMenuCode");
				if (tmpValue == null || "".equals(tmpValue)) {
					tmpValue = defaultValue;
				}
				objID = tmpValue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (SKIP_BODY);
	}

	public int doEndTag() throws JspException {
		try {
			HttpSession session = pageContext.getSession();
			if (session != null) {
				setLogAndObjInfo(session, logType, objID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}

	/**
	 * 插入日志函数 现在只插入登录日志和功能点的访问日志
	 * 
	 * @param session
	 * @param log_type
	 * @param obj_id
	 * @return
	 * @author jcm
	 */
	public static int dbSetLog(HttpSession session, String log_type,
			String obj_id) {
		int flag = -1;
		String sessionID = session.getId();
		InfoOperTable loginUser = CommonFacade.getLoginUser(session);
		String user_id = loginUser.user_id;
		String user_name = loginUser.user_name;
		String login_ip = loginUser.login_ip;
		String sql = "";
		if ("".equals(log_type) || "1".equals(log_type)) {
			try {
				sql = SQLGenator.genSQL("I0006", sessionID, "1", user_id,
						user_name, login_ip);
			} catch (AppException e) {

				e.printStackTrace();
			}
		} else {
			String obj_name = getObjName(session, obj_id);
			try {
				sql = SQLGenator.genSQL("I0007", sessionID, "2", user_id,
						user_name, login_ip, obj_id, obj_name);
			} catch (AppException e) {

				e.printStackTrace();
			}
		}
		// 执行
		// System.out.println(sql);
		try {
			flag = WebDBUtil.execUpdate(sql);
		} catch (AppException ex) {
			return -1;
		}
		return flag;
	}

	/**
	 * 系统日志及反馈参数设置
	 * 
	 * @param session
	 * @param log_type
	 *            1:登录日志;2:访问日志;
	 * @param obj_id
	 *            访问对象标识
	 * @return
	 * @author jcm
	 */
	public static void setLogAndObjInfo(HttpSession session, String log_type,
			String obj_id) {

		String obj = CommTool.getBackObjFromSession(session);
		//
		if (obj == null || !obj.equals(obj_id)) {
			// 登陆日志
			if ("1".equals(CommTool.getLoginStatus(session))) {
				CommTool.dbSetLog(session, "1", "");
				//
				InfoOperTable loginUser = CommonFacade.getLoginUser(session);
				if (loginUser != null) {
					loginUser.login_status = "2";
					session.setAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE,
							loginUser);
				}
			}
			// 系统访问日志
			CommTool.dbSetLog(session, log_type, obj_id);
			// 设置访问对象
			CommTool.setBackObjToSession(session, obj_id);
		}

	}

	/**
	 * 从会话取得对象名称
	 * 
	 * @param session
	 * @param obj_id
	 * @return
	 */
	public static String getObjName(HttpSession session, String obj_id) {
		ServletContext context = session.getServletContext();
		HashMap mapres = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_RES_ID_VS_NAME);
		String obj_name = (String) mapres.get(obj_id);
		return obj_name;
	}

}
