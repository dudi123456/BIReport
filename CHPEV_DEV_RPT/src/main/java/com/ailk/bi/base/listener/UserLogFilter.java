package com.ailk.bi.base.listener;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.DAOFactory;
import com.ailk.bi.userlog.entity.UiInfoUserLog;

public class UserLogFilter implements Filter {

	FilterConfig config;

	public FilterConfig getConfig() {
		return config;
	}

	public void setConfig(FilterConfig config) {
		this.config = config;
	}

	public void destroy() {

	}

	@SuppressWarnings("unused")
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		// ServletContext context = getFilterConfig().getServletContext();
		long bef = System.currentTimeMillis();
		long aft = System.currentTimeMillis();
		// String strURI = ((HttpServletRequest) req).getRequestURI();
		if (req instanceof HttpServletRequest) {
			HttpSession session = ((HttpServletRequest) req).getSession();
			if (res instanceof HttpServletResponse) {
				insertUserBrowserLog((HttpServletRequest) req,
						(HttpServletResponse) res, session);
			}
		}
		// config.getServletContext().log("Request to " + ((HttpServletRequest)
		// req).getRequestURI() + ": " + (aft-bef));
		// String strURI = StringB.NulltoBlank(((HttpServletRequest)
		// req).getQueryString());
		chain.doFilter(req, res); // no chain parameter needed here
	}

	public void init(FilterConfig arg0) throws ServletException {
		this.config = arg0;
	}

	/**
	 * 插入用户浏览记录
	 *
	 * @param request
	 * @param response
	 * @param session
	 */
	private void insertUserBrowserLog(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		// if (com.asiabi.common.app.WebChecker.isLoginUser(request, response))
		// {
		InfoOperTable loginUser = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
		if (loginUser != null) {
			String res_id = StringB.NulltoBlank(request
					.getParameter("SysMenuCode"));
			// String strURI = StringB.NulltoBlank(((HttpServletRequest)
			// request).getQueryString());
			// request.getRequestURI()
			// if (strURI.toLowerCase().indexOf("dolog")>=0){
			if (res_id.length() > 0) {
				String sessionId = session.getId();
				if (res_id.length() > 0) {
					String proResId = StringB.NulltoBlank((String) session
							.getAttribute("DOLOG_RESOUCE_ID"));// 先前的资源ID
					// String sqlExist =
					// "select SERVICE_SN from UI_SYS_LOG where LOG_SEQ='" +
					// sessionId + "' and OBJ_ID='" + res_id + "'";
					// String resultExist[][] =
					// DAOFactory.getUserLogDao().qryObjectInfoList(sqlExist);
					// if (resultExist!=null &&
					// resultExist.length>0){//查询当前session是否存在该资源的访问资料
					if (res_id.equals(proResId) && proResId.length() > 0) {// 查询当前session是否存在该资源的访问资料
						// todo:xxxx
					} else {
						session.setAttribute("DOLOG_RESOUCE_ID", res_id);
						String msg = "";
						String sql = "select name from ui_pub_info_resource where res_id='"
								+ res_id + "'";
						String result[][] = DAOFactory.getUserLogDao()
								.qryObjectInfoList(sql);
						if (result != null && result.length > 0) {
							msg = result[0][0];
						} else {
							sql = "select menu_name from ui_info_menu where menu_id='"
									+ res_id + "'";
							result = DAOFactory.getUserLogDao()
									.qryObjectInfoList(sql);
							if (result != null && result.length > 0) {
								msg = result[0][0];
							}
						}
						UiInfoUserLog obj = new UiInfoUserLog();
						obj.setSessionId(sessionId);
						obj.setUserId(loginUser.oper_no);
						obj.setUserName(loginUser.user_name);
						obj.setClientAddress(request.getRemoteAddr());
						obj.setOperation("2");
						obj.setResourceId(res_id);
						obj.setMsg(msg);
						DAOFactory.getUserLogDao().insert(obj);
					}
				}
			}
		}
		// }
	}
}
