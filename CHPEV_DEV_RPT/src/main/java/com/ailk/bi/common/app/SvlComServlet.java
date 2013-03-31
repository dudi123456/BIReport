package com.ailk.bi.common.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.common.sysconfig.GetSystemConfig;

/**
 * <p>
 * Title: iBusiness
 * </p>
 * <p>
 * Description: 通用的Servlet底层，你的servlet可以继承该servlet很方便的
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class SvlComServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 610416501627098356L;

	/** 系统是否需要记录日志信息，TRUE需，False不需 */
	public static boolean LOGFLAG = true;

	public void init() throws ServletException {
	}

	// Process the HTTP Get request
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	// Process the HTTP Post request
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * 转换页面
	 * 
	 * @param strJspID
	 *            转向页面的ID（在页面配置文件中)
	 */
	public void ReqDispatch(HttpServletRequest request,
			HttpServletResponse response, String strJspID) {
		try {
			getServletContext().getRequestDispatcher(
					(String) GetSystemConfig.getJspURL(strJspID)).forward(
					request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 转换页面
	 * 
	 * @param URL
	 *            要转向的url
	 */
	public void DirectReqDispatch(HttpServletRequest request,
			HttpServletResponse response, String URL) {
		try {
			getServletContext().getRequestDispatcher(URL).forward(request,
					response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据错误ID取得错误信息 changed by wiseking 1129
	 */
	public String getErrMsg(String strErrID) {
		return GetSystemConfig.getErrMsg(strErrID);
	}

	/**
	 * 设置成通用的成功提示页面
	 * 
	 * @param strMsg
	 *            提示信息
	 */
	public void setSucessPageMsg(HttpServletRequest request,
			HttpServletResponse response, String strMsg) {
		// 读取提示信息，放入session中，然后转到提示页面
		request.getSession(true).setAttribute("INFOMESSAGE", strMsg);
		this.ReqDispatch(request, response, "P1003");
	}

	/**
	 * 设置成通用的成功提示页面
	 * 
	 * @param strErrID
	 *            提示信息ID
	 */
	public void setSucessPage(HttpServletRequest request,
			HttpServletResponse response, String strErrID) {
		// 读取提示信息，放入session中，然后转到提示页面
		request.getSession(true).setAttribute("INFOMESSAGE",
				getErrMsg(strErrID));
		this.ReqDispatch(request, response, "P1003");
	}

	/**
	 * 设置成通用的错误提示页面
	 * 
	 * @param strErrID
	 *            提示信息ID
	 */
	public void setErrPage(HttpServletRequest request,
			HttpServletResponse response, String strErrID) {
		// 读取提示信息，放入session中，然后转到提示页面
		request.getSession(true).setAttribute("INFOMESSAGE",
				getErrMsg(strErrID));
		this.ReqDispatch(request, response, "P1002");
	}

	/**
	 * 设置成通用的错误提示页面
	 * 
	 * @param strErrMsg
	 *            提示信息
	 */
	public void setErrPageMsg(HttpServletRequest request,
			HttpServletResponse response, String strErrMsg) {
		// 读取提示信息，放入session中，然后转到提示页面
		request.getSession(true).setAttribute("INFOMESSAGE", strErrMsg);
		this.ReqDispatch(request, response, "P1002");
	}

	/**
	 * 设置成通用的警告提示页面
	 * 
	 * @param strErrID
	 *            提示信息ID
	 */
	public void setWarnPage(HttpServletRequest request,
			HttpServletResponse response, String strErrID) {
		// 读取提示信息，放入session中，然后转到提示页面
		request.getSession(true).setAttribute("INFOMESSAGE",
				getErrMsg(strErrID));
		this.ReqDispatch(request, response, "P1004");
	}

	/** 设置系统的错误提示页面 */
	public void setSystemErrPage(HttpServletRequest request,
			HttpServletResponse response) {
		// 读取提示信息，放入session中，然后转到提示页面
		request.getSession(true)
				.setAttribute("INFOMESSAGE", getErrMsg("E1002"));
		this.ReqDispatch(request, response, "P1002");
	}

	/** 设置系统的错误提示页面和信息 */
	public void setSystemErrPageMsg(HttpServletRequest request,
			HttpServletResponse response, String strMsg) {
		// 读取提示信息，放入session中，然后转到提示页面
		request.getSession(true).setAttribute("INFOMESSAGE", strMsg);
		this.ReqDispatch(request, response, "P1002");
	}

	/**
	 * 系统记录日志信息
	 * 
	 * @param request
	 * @param strLogIn
	 */
	public void BIBMLog(HttpServletRequest request, String strLogIn) {
		// 如果系统需要记录信息
	}

	/**
	 * 生成令牌串
	 */
	private String genToken() {
		String strToken = null;
		strToken = System.currentTimeMillis() + "||" + "ASFSAFasflkas";
		// 做一下加密就更酷了
		return strToken;
	}

	/**
	 * 用令牌方式解决页面重复提交的问题
	 * 
	 * @param request
	 * @param response
	 * @return 如果为false则说明该页面重复提交，为true则不必进行处理
	 */
	public boolean checkToken(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (session == null)
			return true;

		String tokenString = (String) session
				.getAttribute(AppConst.TOKEN_STRING);
		if (tokenString == null || "".equals(tokenString)) {
			session.setAttribute(AppConst.TOKEN_STRING, genToken());
			return true;
		}
		// 重新生成
		session.setAttribute(AppConst.TOKEN_STRING, genToken());
		// 如果session中存在Token信息，那么读取页面的hidden信息
		String hidenString = request.getParameter(AppConst.TOKEN_STRING);
		if (hidenString == null || "".equals(hidenString)) {
			// 不需要判断重复提交的页面
			return true;
		}

		// 重复提交
		if (!hidenString.equals(tokenString)) {
			return false;
		}

		return true;
	}
}