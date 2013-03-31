<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page contentType="application/vnd.ms-excel;charset=UTF-8"%>
<%
request.setCharacterEncoding("GBK");
response.setContentType("application/vnd.ms-excel;charset=UTF-8");
%>
<%@page import="com.ailk.bi.base.table.SubjectCommTabDef"%>
<%@page import="com.ailk.bi.subject.domain.TableCurFunc"%>
<%@page import="com.ailk.bi.subject.util.SubjectConst"%>
<%@page import="com.ailk.bi.subject.service.dao.ITableHeadHTMLDAO"%>
<%@page import="com.ailk.bi.subject.service.dao.impl.TableHeadHTMLDAO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.ailk.bi.base.table.SubjectCommTabCol"%>
<%@page import="com.ailk.bi.subject.util.SubjectStringUtil"%>
<HTML>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<body>
<%
	request.setCharacterEncoding("UTF-8");
	String tables = CommTool.getParameterGB(request, "table_id");
	String names = request.getParameter("table_name");
	names = java.net.URLDecoder.decode(names,"UTF-8"); 
	
	if (null != tables && !"".equals(tables)) {
		int pos = tables.indexOf(",");
		if (pos >= 0) {
			String[] table = tables.split(",");
			String[] name = names.split(",");
			for (int i = 0; i < table.length; i++) {
				Object content = session
						.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_EXPORT
						+ "_" + table[i]);
				SubjectCommTabDef subTable = null;
				Object tmpObj = session.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_TABLE_OBJ
						+ "_" + table[i]);
				if (null != tmpObj)
					subTable = (SubjectCommTabDef) tmpObj;
				TableCurFunc curFunc = null;
				tmpObj = session.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_FUNC_OBJ
						+ "_" + table[i]);
				if (null != tmpObj) {
					curFunc = (TableCurFunc) tmpObj;
				}
				if (null != subTable && SubjectConst.NO.equalsIgnoreCase(subTable.has_expand)
			 			&& SubjectConst.YES.equalsIgnoreCase(subTable.has_paging)) {
					//分页方式，需要全部重新生成
					String[][] svces = null;
					tmpObj = session.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_BODY_SVCES
 						+ "_" + table[i]);
			 		if (null != tmpObj) {
			 			svces = (String[][]) tmpObj;
			 		}
			 		if (null != svces) {
			 			//这里是重新生成，还是调用一遍呢，重新生成吧
			 			out.println("<table width='100%' border='1' cellpadding='0' cellspacing='0' "
							+ ">\n");
			 			ITableHeadHTMLDAO tableHeadDao = new TableHeadHTMLDAO();
 						tableHeadDao.getTableHead(subTable, curFunc,svces);
			 			StringBuffer sbHead=tableHeadDao.getTableExportHead();
			 			out.println(sbHead);
			 			List tabCols = subTable.tableCols;
			 			int dimCount=0;
			 			for(int ii=0;ii<svces.length;ii++){
			 				out.println("<tr>");
			 				int index=-1;
			 				Iterator iter=tabCols.iterator();
			 				while(iter.hasNext()){
			 					SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			 					if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)
			 							&& SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
			 						//维度			 						
			 						if(ii==svces.length-1){
			 							out.println("<td nowrap align=\"center\" colspan=\""+dimCount+"\">");
			 							out.println("<strong>合计</strong>");
			 							index=2*dimCount-1;
			 						}else{
			 							index++;
				 						out.println("<td nowrap align=\"left\">");
				 						index++;
			 							out.println(svces[ii][index]);
			 						}
			 						out.println("</td>");
			 					}else if(SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)){
			 						//指标
			 						dimCount=(index+1)/2;
			 						out.println("<td nowrap align=\"right\">");
			 						index++;
			 						System.out.println("index============================"+index);
			 						out.println(SubjectStringUtil.formatColValue(tabCol,svces[ii][index]));
			 						out.println("</td>");
			 					}
			 				}
			 				out.println("</tr>\n");
			 			}                                
			 			out.println("</table>");
			 			out.println("<BR/>");
			 		}
				}else{
					//非分页
					if (null != content) {
						out.println("<center><h3>" + name[i]
						+ "</h3></center>");
						String[] real_content = (String[]) content;
						for(int j=0;j<real_content.length;j++) {
							out.println(real_content[j]);
						}
					}
				}
				out.println("<br>");
				out.println("<br>");
			}
		} else {
				out.println("<center><h3>" + names + "</h3></center>");
				SubjectCommTabDef subTable = null;
				Object tmpObj = session.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_TABLE_OBJ
						+ "_" + tables);
				if (null != tmpObj)
					subTable = (SubjectCommTabDef) tmpObj;
				TableCurFunc curFunc = null;
				tmpObj = session.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_FUNC_OBJ	+ "_" + tables);
				if (null != tmpObj) {
					curFunc = (TableCurFunc) tmpObj;
				}
				if (null != subTable && SubjectConst.NO.equalsIgnoreCase(subTable.has_expand)
			 			&& SubjectConst.YES.equalsIgnoreCase(subTable.has_paging)) {
					//分页方式，需要全部重新生成
					String[][] svces = null;
					tmpObj = session.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_BODY_SVCES+ "_" + tables);
			 		if (null != tmpObj) {
			 			svces = (String[][]) tmpObj;
			 		}
			 		if (null != svces) {
			 			//这里是重新生成，还是调用一遍呢，重新生成吧
			 			out.println("<table width='100%' border='1' cellpadding='0' cellspacing='0' "+ ">\n");
			 			ITableHeadHTMLDAO tableHeadDao = new TableHeadHTMLDAO();
 						tableHeadDao.getTableHead(subTable, curFunc,svces);
			 			StringBuffer sbHead=tableHeadDao.getTableExportHead();
			 			//System.out.println("head==========="+sbHead.toString());
			 			out.println(sbHead);
			 			List tabCols = subTable.tableCols;
			 			//int dimCount=0;
			 			for(int ii=0;ii<svces.length;ii++){
			 				out.println("<tr>");
			 				int index=-1;
			 				int dimCount=0;
			 				Iterator iter=tabCols.iterator();
			 				while(iter.hasNext()){
			 					SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			 					if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)
			 							&& SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
			 						//维度			 						
			 						if((ii==svces.length-1)&&SubjectConst.YES.equalsIgnoreCase(subTable.sum_display)){
			 							out.println("<td nowrap align=\"center\" colspan=\""+dimCount+"\">");
			 							out.println("<strong>合计</strong>");			 							
			 							index=2*dimCount-1;			 							
			 						}else{
			 							index++;
				 						out.println("<td nowrap align=\"left\">");
				 						index++;
			 							out.println(svces[ii][index]);
			 						}
			 						out.println("</td>");
			 					}else if(SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)){
			 						if((ii==svces.length-1)&&SubjectConst.NO.equalsIgnoreCase(subTable.sum_display)){
			 							
			 						}else{
			 							//指标
				 						dimCount=(index+1)/2;
				 						out.println("<td nowrap align=\"right\">");
				 						index++;			 					
				 						out.println(SubjectStringUtil.formatColValue(tabCol,svces[ii][index]));
				 						out.println("</td>");
			 						}
			 					}
			 				}
			 				out.println("</tr>\n");
			 			}                                
			 			out.println("</table>");
			 			out.println("<BR/>");
			 		}
				}else{					
					Object content = session.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_EXPORT+ "_" + tables);
					if (null != content) {
						if (null != content) {
							String[] real_content = (String[]) content;
							for(int i=0;i<real_content.length;i++) {
								out.println(real_content[i].replaceAll("&nbsp;",""));
							}
						}
					}
			}
		}
	}
%>
</body>
</HTML>