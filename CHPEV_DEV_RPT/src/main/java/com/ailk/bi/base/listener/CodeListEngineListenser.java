package com.ailk.bi.base.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.ailk.bi.base.table.UiCodeListTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CodeListEngineListenser extends HttpServlet implements
		ServletContextListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(CodeListEngineListenser.class);

	private HashMap codemap = new HashMap();

	public void contextDestroyed(ServletContextEvent arg0) {

	}

	public void contextInitialized(ServletContextEvent arg0) {

		UiCodeListTable codeList[] = null;

		ArrayList list_type_1 = new ArrayList();
		ArrayList list_type_2 = new ArrayList();

		ServletContext context = arg0.getServletContext();

		String sql = "";
		try {
			// 提取配置表
			sql = SQLGenator.genSQL("QS001");
			// System.out.println("QS001=================" + sql);
			String[][] arr = WebDBUtil.execQryArray(sql, "");
			if (arr != null && arr.length > 0) {
				codeList = new UiCodeListTable[arr.length];
				for (int i = 0; i < arr.length; i++) {
					codeList[i] = new UiCodeListTable();
					codeList[i].type_code = arr[i][0];
					codeList[i].type_name = arr[i][1];
					codeList[i].rule_type = arr[i][2];
					codeList[i].code_id = arr[i][3];
					codeList[i].code_name = arr[i][4];
					codeList[i].code_filed = arr[i][5];
					codeList[i].code_datasource = arr[i][6];
					codeList[i].code_condition = arr[i][7];
					codeList[i].code_order = arr[i][8];
					codeList[i].code_seq = arr[i][9];
					codeList[i].status = arr[i][10];

					if ("1".equals(codeList[i].rule_type)) {
						list_type_1.add(codeList[i]);
					} else {
						list_type_2.add(codeList[i]);
					}

				}
			}
			// 处理1
			process1(list_type_1);
			// 处理2
			process2(list_type_2);
			//

		} catch (AppException e) {

			e.printStackTrace();
		}

		// 装入MAP
		context.setAttribute(WebConstKeys.ATTR_C_CODE_LIST, codemap);
	}

	public void process1(ArrayList list_type_1) {

		if (list_type_1 == null || list_type_1.isEmpty()) {
			return;
		}
		//
		UiCodeListTable codeList[] = (UiCodeListTable[]) list_type_1
				.toArray(new UiCodeListTable[list_type_1.size()]);
		for (int i = 0; i < codeList.length; i++) {
			HashMap map = new LinkedHashMap();
			String sql = "select " + codeList[i].code_filed + " from "
					+ codeList[i].code_datasource + " where "
					+ codeList[i].code_condition + " " + codeList[i].code_order;
			logger.debug("sql=================" + sql);
			try {
				String[][] arr = WebDBUtil.execQryArray(sql, "");
				for (int j = 0; arr != null && j < arr.length; j++) {
					map.put(arr[j][0], arr[j][1]);
				}
			} catch (AppException e) {

				e.printStackTrace();
			}

			codemap.put(codeList[i].type_code.trim().toUpperCase(), map);
		}

	}

	public void process2(ArrayList list_type_2) {

		if (list_type_2 == null || list_type_2.isEmpty()) {
			return;
		}
		HashSet set = new HashSet();
		//
		UiCodeListTable codeList[] = (UiCodeListTable[]) list_type_2
				.toArray(new UiCodeListTable[list_type_2.size()]);
		//
		for (int i = 0; codeList != null && i < codeList.length; i++) {
			set.add(codeList[i].type_code.trim().toUpperCase());
		}
		//
		Iterator it = set.iterator();
		while (it.hasNext()) {

			String key = it.next().toString();
			HashMap map = new LinkedHashMap();
			for (int i = 0; codeList != null && i < codeList.length; i++) {
				if (codeList[i].type_code.toUpperCase().equals(key)) {
					map.put(codeList[i].code_id, codeList[i].code_name);
				}
			}
			codemap.put(key, map);
		}

	}

}
