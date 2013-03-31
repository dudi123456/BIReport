package com.ailk.bi.common.image;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserForgotPwdImg extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = -7692030067518385728L;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("image/jpeg");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		HttpSession session = request.getSession();
		session.setAttribute("forgetPwdCode",
				AuthCoder.genAuthCode(response.getOutputStream()));

	}
}
