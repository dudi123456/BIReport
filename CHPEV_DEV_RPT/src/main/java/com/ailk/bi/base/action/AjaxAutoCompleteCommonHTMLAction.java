package com.ailk.bi.base.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class AjaxAutoCompleteCommonHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2055287089379606016L;

	@Override
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response)) {
			return;
		}
		// 获取参数
		String serchSqlId = request.getParameter("serchSqlId");
		String searchKey = request.getParameter("searchKey");
		if (StringUtils.isBlank(serchSqlId) || StringUtils.isBlank(searchKey))
			return;
		// 获取SQL定义
		try {
			searchKey = URLDecoder.decode(searchKey, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		try {
			// 生成SQL
			String query = SQLGenator.genSQL(serchSqlId, searchKey);
			// 变成只取前20
			query += " AND ROWNUM<=20 ";
			// 查询结果
			System.out.println(query);
			String[][] svces = WebDBUtil.execQryArray(query);
			if (null != svces) {
				// 转换成json
				StringBuilder sb = new StringBuilder();
				sb.append("[");
				for (int i = 0; i < svces.length; i++) {
					sb.append("\"").append(svces[i][0]).append("\",");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append("]");
				System.out.println(sb);
				// 输出到客户端
				response.reset();// 清空输出流
				response.setContentType("text/html; charset=UTF-8");
				out = response.getWriter();// 取得输出流
				out.write(sb.toString());
				out.flush();
			}
		} catch (AppException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setNvlNextScreen(request);
	}
}
