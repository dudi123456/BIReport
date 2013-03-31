package com.ailk.bi.subject.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.util.SQLGenator;
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
public class SelfChnlQryTools extends HTMLActionSupport {

	private static final long serialVersionUID = 1L;
	
	private static String getQryData(String sql) {
		String roleStr = "";
		try {
			String[][] arr = WebDBUtil.execQryArray(sql, "");
			for (int i = 0; arr != null && i < arr.length; i++) {
				if (roleStr.length() > 0) {
					roleStr += ",";
				}
				roleStr += "{\"argsCode\":\"" + arr[i][0] + "\",\"argsName\":\"" + arr[i][1] + "\"}";
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return roleStr;
	}
	
	private static String getQryData(String sql,String[] ret_data) {
		String roleStr = "";
		try {
			String[][] arr = WebDBUtil.execQryArray(sql, "");
			for (int i = 0; arr != null && i < arr.length; i++) {
				if (roleStr.length() > 0) {
					roleStr += ",";
				}
				roleStr += "{\"argsCode\":\"" + arr[i][0] + "\",\"argsName\":\"" + arr[i][1] + "\"}";
			}
			ret_data[0]=arr[0][0];
		} catch (AppException e) {
			e.printStackTrace();
		}
		
		return roleStr;
	}

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response)) {
			return;
		}
		response.setContentType("text/html; charset=GBK");
		
		PrintWriter out = null;
		try 
		{
			out = response.getWriter();
		} catch (IOException ioe) {
			//
		}

		String sql = "";
		String roleStr = "";
		
		String qryType = StringB.NulltoBlank(request.getParameter("qryType"));
		String argsCode = StringB.NulltoBlank(request.getParameter("argsCode"));
		String focusCode = StringB.NulltoBlank(request.getParameter("provCode"));
		try {
			System.out.println(qryType);
			System.out.println(argsCode);
			if(qryType.equals("AREA")){
				sql = SQLGenator.genSQL("LoadProv", argsCode);
				System.out.println("LoadSQL==:" + sql);
				roleStr=getQryData(sql);
				roleStr= "[" +roleStr + "]";
			}
			if(qryType.equals("PROV")){
				sql = SQLGenator.genSQL("LoadDist", argsCode);
				System.out.println("LoadSQL==:" + sql);
				roleStr=getQryData(sql);
				roleStr= "[" +roleStr + "]";
			}
			if(qryType.equals("ALL")){
				String Str_1="";
				String Str_2="";
				sql = SQLGenator.genSQL("LoadProv", argsCode);
				System.out.println("ALL==LoadSQL_1==:" + sql);
				String[] provCode = new String[1];
				provCode[0]="";
				Str_1 = getQryData(sql,provCode);
				
				if(focusCode != null && !focusCode.equals("")){
					provCode[0]=focusCode;
				}
				sql = SQLGenator.genSQL("LoadDist", provCode[0]);
				System.out.println("ALL==LoadSQL_2==:" + sql);
				Str_2=getQryData(sql);
				
				roleStr="{\"ProvList\":[" + Str_1 + "],\"DistList\":[" + Str_2 + "]}";
				
			}
			
			roleStr = "{\"RESULT\":" + roleStr + "}";
		} catch (AppException e) {
			e.printStackTrace();
		}
		System.out.println("search over!!!");
		
		out.write(roleStr);
		out.flush();
		setNvlNextScreen(request);
	}

}
