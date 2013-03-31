package com.ailk.bi.common.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * Title: asiabi J2EE Web 程序的框架和底层的定义
 * </p>
 * <p>
 * Description: Web操作的一些常用的判断 判断是否为登陆用户
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: BeiJing asiabi Info System Co,Ltd.
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class WebChecker {
	public final static String INVALID_LOGIN_SCREEN = "INVALID_LOGIN.screen";

	public final static String LOGIN_FLAG = "LS_CHK_LOGIN_FLAG"; // 在session中存放是否登陆的标志

	public static boolean isLoginUser(HttpServletRequest request,
			HttpServletResponse response) {
		if (request == null)
			return false;
		HttpSession session = request.getSession();
		if (session != null) {
			String v = (String) session.getAttribute(LOGIN_FLAG);
			if (v != null && "1".equals(v)) {

				return true;
			}
			// session.setAttribute(WebKeys.HANDLER_SCREEN_KEY,
			// INVALID_LOGIN_SCREEN);
		}
		// 登录信息有问题有问题
		try {
			request.getRequestDispatcher(INVALID_LOGIN_SCREEN).forward(request,
					response);
		} catch (IOException ex) {
			System.err.println("**FATAL ERROR you must define the screen:"
					+ INVALID_LOGIN_SCREEN + " in your screendefine.xml");
			ex.printStackTrace();
		} catch (ServletException ex) {
			System.err.println("**FATAL ERROR you must define the screen:"
					+ INVALID_LOGIN_SCREEN + " in your screendefine.xml");
			ex.printStackTrace();
		} catch (Exception e) {
			System.err.println("**FATAL ERROR you must define the screen:"
					+ INVALID_LOGIN_SCREEN + " in your screendefine.xml");
			e.printStackTrace();
		}
		return false;
	}

}