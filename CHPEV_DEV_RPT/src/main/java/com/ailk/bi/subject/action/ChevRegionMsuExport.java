package com.ailk.bi.subject.action;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class ChevRegionMsuExport extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6657767856368061696L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response)) {
			return;
		}
		response.setContentType("text/html; charset=GBK");
		String tableName = request.getParameter("tableName");
		if (!StringUtils.isBlank(tableName)) {
			try {
				tableName = URLDecoder.decode(tableName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		} else {
			tableName = "数据下载";
		}
		String provinceCode = request.getParameter("province_code");
		String cityCode = request.getParameter("city_code");
		String provinceName = null;
		String cityName = null;
		if (!StringUtils.isBlank(provinceCode)) {
			try {
				String[][] svces = WebDBUtil
						.execQryArray("SELECT PROVINCE_NAME FROM ST_DIM_AREA_PROVINCE T WHERE T.PROVINCE_CODE ='"
								+ provinceCode + "'");
				if (null != svces && svces.length >= 1) {
					provinceName = svces[0][0];
				}
			} catch (AppException e) {
				e.printStackTrace();
			}
		}
		if (!StringUtils.isBlank(cityCode)) {
			try {
				String[][] svces = WebDBUtil
						.execQryArray(" SELECT CITY_NAME FROM DIM_PUB_CITY WHERE CITY_CODE='"
								+ cityCode + "'");
				if (null != svces && svces.length >= 1) {
					cityName = svces[0][0];
				}
			} catch (AppException e) {
				e.printStackTrace();
			}
		}
		if (!StringUtils.isBlank(provinceName)) {
			tableName = tableName.replaceAll("::province_code::", provinceName);
		} else {
			tableName = tableName.replaceAll("::province_code::", "");
		}
		if (!StringUtils.isBlank(cityName)) {
			tableName = tableName.replaceAll("::city_code::", cityName);
		} else {
			tableName = tableName.replaceAll("::city_code::", "");
		}
		PrintWriter os = null;
		try {
			
			response.reset();// 清空输出流
			response.setHeader("Content-disposition", "attachment; filename="
					+ URLEncoder.encode(tableName, "UTF-8") + ".xls");// 设定输出文件头
			response.setContentType("application/msexcel;charset=UTF-8");// 定义输出类型
			request.setCharacterEncoding("UTF-8");
			os = response.getWriter();// 取得输出流
			os.write("<HTML>");
			os.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
			os.write("<body>");
			HttpSession session = request.getSession();
			String content = (String) session
					.getAttribute(WebKeys.ATTR_ST_AREA_SEGMENT_REFLECT);
			if (null != content && !"".equals(content)) {
				os.write("<table width='100%' border='1' cellpadding='0' cellspacing='0' "
						+ ">\n");
				os.write(content);
				os.write("</table>");
				os.write("<BR/>");
			}
			os.write("</body>");
			os.write("</HTML>");
			os.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// finally{
		// if(null!=os)
		// try {
		// os.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		setNvlNextScreen(request);
	}
}
