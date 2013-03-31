package com.ailk.bi.subject.action;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;

/**
 * 
 * 
 * 
 * 
 * 
 */
public class ExportTools extends HTMLActionSupport {

	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response)) {
			return;
		}
		response.setContentType("text/html; charset=GBK");
		String[] sqlArgs = null;
		if (request.getParameter("sqlArgs") != null)
			sqlArgs = request.getParameter("sqlArgs").split(",");
		String exportId = StringB.NulltoBlank(request.getParameter("exportId"));
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
		String acctMonth = request.getParameter("acct_month");
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
		if (!StringUtils.isBlank(acctMonth)) {
			tableName = tableName+"("+acctMonth+")";
		}
		OutputStream os = null;
		try {
			os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-disposition", "attachment; filename="
					+ URLEncoder.encode(tableName, "UTF-8") + ".xls");// 设定输出文件头
			response.setContentType("application/msexcel");// 定义输出类型

			ChnlCommonTools.dataToExcle(os, exportId, sqlArgs, tableName);
			// os.flush();
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
