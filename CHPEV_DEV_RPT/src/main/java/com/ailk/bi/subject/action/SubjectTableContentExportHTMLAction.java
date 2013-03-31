package com.ailk.bi.subject.action;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.ailk.bi.base.table.SubjectCommTabCol;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.subject.domain.TableCurFunc;
import com.ailk.bi.subject.service.dao.ITableHeadHTMLDAO;
import com.ailk.bi.subject.service.dao.impl.TableHeadHTMLDAO;
import com.ailk.bi.subject.util.SubjectConst;
import com.ailk.bi.subject.util.SubjectStringUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class SubjectTableContentExportHTMLAction extends HTMLActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4136724045303597427L;

	@SuppressWarnings("rawtypes")
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response)) {
			return;
		}
		response.setContentType("text/html; charset=GBK");
		String tableName = request.getParameter("table_name");
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
		PrintWriter out = null;
		try {

			response.reset();// 清空输出流
			response.setHeader("Content-disposition", "attachment; filename="
					+ URLEncoder.encode(tableName, "UTF-8") + ".xls");// 设定输出文件头
			response.setContentType("application/msexcel;charset=UTF-8");// 定义输出类型
			request.setCharacterEncoding("UTF-8");
			out = response.getWriter();// 取得输出流
			out.write("<HTML>");
			out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
			out.write("<body>");
			HttpSession session = request.getSession();
			request.setCharacterEncoding("UTF-8");
			String tables = CommTool.getParameterGB(request, "table_id");

			if (null != tables && !"".equals(tables)) {
				int pos = tables.indexOf(",");
				if (pos >= 0) {
					String[] table = tables.split(",");
					String[] name = tableName.split(",");
					for (int i = 0; i < table.length; i++) {
						Object content = session
								.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_EXPORT
										+ "_" + table[i]);
						SubjectCommTabDef subTable = null;
						Object tmpObj = session
								.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_TABLE_OBJ
										+ "_" + table[i]);
						if (null != tmpObj)
							subTable = (SubjectCommTabDef) tmpObj;
						TableCurFunc curFunc = null;
						tmpObj = session
								.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_FUNC_OBJ
										+ "_" + table[i]);
						if (null != tmpObj) {
							curFunc = (TableCurFunc) tmpObj;
						}
						if (null != subTable
								&& SubjectConst.NO
										.equalsIgnoreCase(subTable.has_expand)
								&& SubjectConst.YES
										.equalsIgnoreCase(subTable.has_paging)) {
							// 分页方式，需要全部重新生成
							String[][] svces = null;
							tmpObj = session
									.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_BODY_SVCES
											+ "_" + table[i]);
							if (null != tmpObj) {
								svces = (String[][]) tmpObj;
							}
							if (null != svces) {
								// 这里是重新生成，还是调用一遍呢，重新生成吧
								out.write("<table width='100%' border='1' cellpadding='0' cellspacing='0' "
										+ ">\n");
								ITableHeadHTMLDAO tableHeadDao = new TableHeadHTMLDAO();
								tableHeadDao.getTableHead(subTable, curFunc,
										svces);
								StringBuffer sbHead = tableHeadDao
										.getTableExportHead();
								out.write(sbHead.toString());
								List tabCols = subTable.tableCols;
								int dimCount = 0;
								for (int ii = 0; ii < svces.length; ii++) {
									out.write("<tr>");
									int index = -1;
									Iterator iter = tabCols.iterator();
									while (iter.hasNext()) {
										SubjectCommTabCol tabCol = (SubjectCommTabCol) iter
												.next();
										if (SubjectConst.YES
												.equalsIgnoreCase(tabCol.default_display)
												&& SubjectConst.NO
														.equalsIgnoreCase(tabCol.is_measure)) {
											// 维度
											if (ii == svces.length - 1) {
												out.write("<td nowrap align=\"center\" colspan=\""
														+ dimCount + "\">");
												out.write("<strong>合计</strong>");
												index = 2 * dimCount - 1;
											} else {
												index++;
												out.write("<td nowrap align=\"left\">");
												index++;
												out.write(svces[ii][index]);
											}
											out.write("</td>");
										} else if (SubjectConst.YES
												.equalsIgnoreCase(tabCol.default_display)) {
											// 指标
											dimCount = (index + 1) / 2;
											out.write("<td nowrap align=\"right\">");
											index++;
											out.write(SubjectStringUtil
													.formatColValue(tabCol,
															svces[ii][index]));
											out.write("</td>");
										}
									}
									out.write("</tr>\n");
								}
								out.write("</table>");
								out.write("<BR/>");
							}
						} else {
							// 非分页
							if (null != content) {
								out.write("<center><h3>" + name[i]
										+ "</h3></center>");
								String[] real_content = (String[]) content;
								for (int j = 0; j < real_content.length; j++) {
									out.write(real_content[j]);
								}
							}
						}
						out.write("<br>");
						out.write("<br>");
					}
				} else {
					out.write("<center><h3>" + tableName + "</h3></center>");
					SubjectCommTabDef subTable = null;
					Object tmpObj = session
							.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_TABLE_OBJ
									+ "_" + tables);
					if (null != tmpObj)
						subTable = (SubjectCommTabDef) tmpObj;
					TableCurFunc curFunc = null;
					tmpObj = session
							.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_FUNC_OBJ
									+ "_" + tables);
					if (null != tmpObj) {
						curFunc = (TableCurFunc) tmpObj;
					}
					if (null != subTable
							&& SubjectConst.NO
									.equalsIgnoreCase(subTable.has_expand)
							&& SubjectConst.YES
									.equalsIgnoreCase(subTable.has_paging)) {
						// 分页方式，需要全部重新生成
						String[][] svces = null;
						tmpObj = session
								.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_BODY_SVCES
										+ "_" + tables);
						if (null != tmpObj) {
							svces = (String[][]) tmpObj;
						}
						if (null != svces) {
							// 这里是重新生成，还是调用一遍呢，重新生成吧
							out.write("<table width='100%' border='1' cellpadding='0' cellspacing='0' "
									+ ">\n");
							ITableHeadHTMLDAO tableHeadDao = new TableHeadHTMLDAO();
							tableHeadDao.getTableHead(subTable, curFunc, svces);
							StringBuffer sbHead = tableHeadDao
									.getTableExportHead();
							// System.out.write("head==========="+sbHead.toString());
							out.write(sbHead.toString());
							List tabCols = subTable.tableCols;
							// int dimCount=0;
							for (int ii = 0; ii < svces.length; ii++) {
								out.write("<tr>");
								int index = -1;
								int dimCount = 0;
								Iterator iter = tabCols.iterator();
								while (iter.hasNext()) {
									SubjectCommTabCol tabCol = (SubjectCommTabCol) iter
											.next();
									if (SubjectConst.YES
											.equalsIgnoreCase(tabCol.default_display)
											&& SubjectConst.NO
													.equalsIgnoreCase(tabCol.is_measure)) {
										// 维度
										if ((ii == svces.length - 1)
												&& SubjectConst.YES
														.equalsIgnoreCase(subTable.sum_display)) {
											out.write("<td nowrap align=\"center\" colspan=\""
													+ dimCount + "\">");
											out.write("<strong>合计</strong>");
											index = 2 * dimCount - 1;
										} else {
											index++;
											out.write("<td nowrap align=\"left\">");
											index++;
											out.write(svces[ii][index]);
										}
										out.write("</td>");
									} else if (SubjectConst.YES
											.equalsIgnoreCase(tabCol.default_display)) {
										if ((ii == svces.length - 1)
												&& SubjectConst.NO
														.equalsIgnoreCase(subTable.sum_display)) {

										} else {
											// 指标
											dimCount = (index + 1) / 2;
											out.write("<td nowrap align=\"right\">");
											index++;
											out.write(SubjectStringUtil
													.formatColValue(tabCol,
															svces[ii][index]));
											out.write("</td>");
										}
									}
								}
								out.write("</tr>\n");
							}
							out.write("</table>");
							out.write("<BR/>");
						}
					} else {
						Object content = session
								.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_EXPORT
										+ "_" + tables);
						if (null != content) {
							if (null != content) {
								String[] real_content = (String[]) content;
								for (int i = 0; i < real_content.length; i++) {
									out.write(real_content[i].replaceAll(
											"&nbsp;", ""));
								}
							}
						}
					}
				}
			}
			out.write("</body>");
			out.write("</HTML>");
			out.flush();
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
