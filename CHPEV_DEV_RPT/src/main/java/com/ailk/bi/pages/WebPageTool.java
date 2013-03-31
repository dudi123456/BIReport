package com.ailk.bi.pages;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.event.DataSet;
import com.ailk.bi.common.event.JBTable;

import java.util.Vector;
import java.util.HashMap;

/**
 *
 * <p>
 * Title:翻页的处理类
 * </p>
 * <p>
 * Description:
 *
 * <pre>
 *   例子（在一jsp文件中）
 *   .......
 *   《html》
 *   《head》
 *    //输出javascript部分
 *   《%=WebPageTool.pageScript(&quot;&lt;b&gt;pageForm&lt;/b&gt;&quot;)%》[此参数是翻页内容所在的form的名字]
 *   《/head》
 *    ....
 *    《%
 *       ...
 *       //获取页面信息
 *       PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, cdmaNos.length, 40 );
 *       ...
 *     %》
 *     《FORM name=&quot;&lt;b&gt;pageForm&lt;/b&gt;&quot; action=&quot;本jsp文件&quot; method=&quot;post&quot;》
 *      //设置隐藏区
 *     《%=WebPageTool.pageHidden(pageInfo)%》
 *      ......
 *      //显示页面内容
 *      《TR》
 *       设置显示每列标题
 *      《/TR》
 *       for( int i=0 ; i&lt;pageInfo.iLinesPerPage; i++ )
 *       {
 *         if( (i + pageInfo.absRowNoCurPage()) &gt; pageInfo.iLines )
 *             break;
 *          //一行一行地输出
 *          out.println(&quot;
 * <TR>
 *  &quot;);
 *            ....
 *            [《TD》设置单选/复选按钮《%=WebPageTool.tdCheckBox(pageInfo,theKeyValue)%》/《%=WebPageTool.tdRadio(pageInfo,theKeyValue)%》《/TD》]
 *            《TD》..《/TD》
 *             ....
 *          out.println(&quot;
 * </TR>
 *  &quot;);
 *       }
 *      //输出页面导航条信息
 *     《%=WebPageTool.pagePolit(pageInfo)%》
 *     《/FORM》
 * </pre>
 *
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author WISEKING
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes" })
public class WebPageTool implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -14920124598849172L;

	// 这个值的定义要和waf框架里面的
	// waf.controller.web.util.WebKeys.PREVIOUS_REQUEST_PARAMETERS 值定义相同
	final static String PREVIOUS_REQUEST_PARAMETERS = "waf.PREVIOUS_REQUEST_PARAMETERS";

	final static String ROWID = "rowid"; // 显示dataset的特殊字段，以序号为值
	final static String CONST_CHOSE_VALUE = "CUR_CHOOSE_CHECKBOX_VALUE";
	final static String CONST_CHOSE_CAPTION = "CUR_CHOOSE_CHECKBOX_CAPTION";

	/**
	 * 页面翻页所需要的script
	 *
	 * @param pageForm
	 *            翻页区域所在的窗口
	 * @return
	 */
	public static String pageScript(String pageForm) {
		return pageScript(pageForm, null, "page");
	}

	public static String pageScript(String pageForm, String action) {
		return pageScript(pageForm, action, "page");
	}

	/**
	 * 页面翻页所需要的script
	 *
	 * @param pageForm
	 *            翻页区域所在的窗口
	 * @param action
	 *            翻页时候调用的form的action窗口
	 * @return
	 */
	public static String pageScript(String pageForm, String action,
			String pagePrefix) {
		StringBuffer sbScript = new StringBuffer();
		String formAction = "";
		if (action != null) {
			formAction = "  document.forms[\"" + pageForm + "\"].action='" + action + "'\n";
		}

		sbScript.append("<script language=\"JavaScript\">\n");
		sbScript.append("\n");
		sbScript.append("function firstPage()\n");
		sbScript.append("{\n");
		sbScript.append("  document.forms[\"" + pageForm + "\"]." + pagePrefix
				+ "__iCurPage.value = \"0\";\n");
		sbScript.append(formAction);
		sbScript.append("  document.forms[\"" + pageForm + "\"].submit();\n");
		sbScript.append("}\n");
		sbScript.append("\n");
		sbScript.append("function changestyle(type){\n");
		sbScript.append("  var a = event.srcElement;\n");
		sbScript.append("  if( type==0 )\n");
		sbScript.append("    a.runtimeStyle.borderStyle = \"outset\";\n");
		sbScript.append("  else\n");
		sbScript.append("    a.runtimeStyle.borderStyle = a.style.borderStyle;\n");
		sbScript.append("}\n");
		sbScript.append("\n");

		sbScript.append("function prePage()\n");
		sbScript.append("{\n");
		sbScript.append("  page=parseInt(document.forms[\"" + pageForm + "\"]." + pagePrefix
				+ "__iCurPage.value);\n");
		sbScript.append("  page--;\n");
		sbScript.append("  document.forms[\"" + pageForm + "\"]." + pagePrefix
				+ "__iCurPage.value=page;\n");
		sbScript.append(formAction);
		sbScript.append("  document.forms[\"" + pageForm + "\"].submit();\n");
		sbScript.append("}\n");

		sbScript.append("\n");

		sbScript.append("function nextPage()\n");
		sbScript.append("{\n");
		sbScript.append("  page=parseInt(document.forms[\"" + pageForm + "\"]." + pagePrefix
				+ "__iCurPage.value);\n");
		sbScript.append("  page++;\n");
		sbScript.append("  document.forms[\"" + pageForm + "\"]." + pagePrefix
				+ "__iCurPage.value=page;\n");
		sbScript.append(formAction);
		sbScript.append("  document.forms[\"" + pageForm + "\"].submit();\n");
		sbScript.append("}\n");

		sbScript.append("function lastPage()\n");
		sbScript.append("{\n");
		sbScript.append("  document.forms[\"" + pageForm + "\"]." + pagePrefix
				+ "__iCurPage.value = document.forms[\"" + pageForm + "\"]." + pagePrefix
				+ "__iPages.value;\n");
		sbScript.append(formAction);
		sbScript.append("  document.forms[\"" + pageForm + "\"].submit();\n");
		sbScript.append("}\n");

		sbScript.append("\n");

		sbScript.append("function goFixPage()\n");
		sbScript.append("{\n");
		sbScript.append("  var page = document.forms[\"" + pageForm + "\"]."
				+ "SYS_GoPage.value;\n");		
		//这里需要判断如果不是数字报错
		sbScript.append("  var totolPage= parseInt(document.forms[\"" + pageForm + "\"]." + pagePrefix
				+ "__iPages.value);\n");
		sbScript.append("  if(parseInt(page) > totolPage) \n");
		sbScript.append("     lastPage();\n");
		sbScript.append("  else \n");
		sbScript.append("     goPage(page);\n");
		sbScript.append("}\n");

		sbScript.append("\n");

		sbScript.append("function goPage(pageV)\n");
		sbScript.append("{\n");
		sbScript.append("  var page;\n");
		sbScript.append("\n");
		sbScript.append("  //是否输入合法\n");
		sbScript.append("  page=parseInt(pageV);\n");
//		sbScript.append("  if( page==\"NaN\" || page>parseInt(document.forms[\"" + pageForm
//				+ "\"]." + pagePrefix + "__iPages.value)+1 || page<1 )\n");
		sbScript.append("  if( page!=pageV || page<1 )\n");
		sbScript.append("  {\n");
		sbScript.append("   alert(\"跳转页输入错误!\");\n");
		sbScript.append("   document.forms[\"" + pageForm + "\"].SYS_GoPage.focus();\n");
		sbScript.append("   return;\n");
		sbScript.append("  }\n");
		sbScript.append("  document.forms[\"" + pageForm + "\"]." + pagePrefix
				+ "__iCurPage.value = page-1;\n");
		sbScript.append(formAction);
		sbScript.append("  document.forms[\"" + pageForm + "\"].submit();\n");
		sbScript.append("}\n");
		sbScript.append("\n");

		sbScript.append(printFunCheckID(pageForm, pagePrefix).toString());

		sbScript.append("function checkAll(checkFlag)\n");
		sbScript.append("{\n");
		sbScript.append("    for( i=0; i<document.forms[\"" + pageForm
				+ "\"].elements.length; i++)\n");
		sbScript.append("    {\n");
		sbScript.append("      if( document.forms[\"" + pageForm
				+ "\"].elements[i].name==\"pageChkBox\") \n");
		sbScript.append("      {\n");
		sbScript.append("        document.forms[\"" + pageForm
				+ "\"].elements[i].checked = checkFlag;\n");
		sbScript.append("        checkID( document.forms[\"" + pageForm + "\"].elements[i] );\n");
		sbScript.append("      }\n");
		sbScript.append("    }\n");
		sbScript.append("}\n");
		sbScript.append("\n");

		sbScript.append("function radioID(radioInput)\n");
		sbScript.append("{\n");
		sbScript.append("  var ID_DELI=\"" + CommKeys.ID_DELI + "\";\n");
		sbScript.append("\n");
		sbScript.append("      document.forms[\"" + pageForm + "\"]." + pagePrefix
				+ "__checkIDs.value=ID_DELI+radioInput.value+ID_DELI;\n");
		sbScript.append("}\n");
		sbScript.append("\n");
		sbScript.append("</script>\n");
		sbScript.append("\n");
		return sbScript.toString();
	}

	private static StringBuffer printFunCheckID(String pageForm,
			String pagePrefix) {
		StringBuffer sbScript = new StringBuffer();
		sbScript.append("function checkID(chkInput)\n");
		sbScript.append("{\n");
		sbScript.append("  var ID_DELI=\"" + CommKeys.ID_DELI + "\";\n");
		sbScript.append("  var checkIDs = document.forms[\"" + pageForm + "\"]." + pagePrefix
				+ "__checkIDs.value+\"\";\n");
		sbScript.append("\n");
		sbScript.append("  //判断是true还是false\n");
		sbScript.append("  if( chkInput.checked )\n");
		sbScript.append("  {  //++\n");
		sbScript.append("    if( checkIDs.indexOf(ID_DELI+chkInput.value+ID_DELI)==-1 )\n");
		sbScript.append("    {\n");
		sbScript.append("       if( document.forms[\"" + pageForm + "\"]." + pagePrefix
				+ "__checkIDs.value ==\"\" )\n");
		sbScript.append("         document.forms[\"" + pageForm + "\"]." + pagePrefix
				+ "__checkIDs.value += ID_DELI;\n");
		sbScript.append("       document.forms[\"" + pageForm + "\"]." + pagePrefix
				+ "__checkIDs.value += chkInput.value+ID_DELI;\n");
		sbScript.append("    }\n");
		sbScript.append("  }\n");
		sbScript.append("  else\n");
		sbScript.append("  {\n");
		sbScript.append("    //--\n");
		sbScript.append("    iPos = checkIDs.indexOf(ID_DELI+chkInput.value+ID_DELI);\n");
		sbScript.append("    if( iPos >-1 )\n");
		sbScript.append("    {\n");
		sbScript.append("      tmpID = ID_DELI+chkInput.value+ID_DELI;\n");
		sbScript.append("      document.forms[\""
				+ pageForm
				+ "\"]."
				+ pagePrefix
				+ "__checkIDs.value = checkIDs.substr(0, iPos)+checkIDs.substr(iPos+tmpID.length-1);\n");
		sbScript.append("    }\n");
		sbScript.append("  }\n");
		sbScript.append("}\n");
		return sbScript;
	}

	/**
	 * 页面导航信息，如果有多页，则显示页面导航条信息
	 *
	 * @param pageForm
	 * @return
	 */
	public static String pagePolit(PagesInfoStruct pageInfo) {
		return pagePolit(pageInfo, false);
	}

	/**
	 * 页面导航信息，如果有多页，则显示页面导航条信息
	 *
	 * @param pageForm
	 * @return
	 */
	public static String pagePolit(PagesInfoStruct pageInfo, String rootPath) {

		return pagePolit(pageInfo, false, rootPath);
	}

	/**
	 * 页面导航信息，如果有多页，则显示页面导航条信息
	 *
	 * @param pageForm
	 * @return
	 */
	public static String pagePolit(PagesInfoStruct pageInfo,
			boolean allCheckFlag) {
		return pagePolit(pageInfo, allCheckFlag, false);
	}

	public static String pagePolit(PagesInfoStruct pageInfo,
			boolean allCheckFlag, String rootPath) {
		return pagePolit(pageInfo, allCheckFlag, false, rootPath);
	}

	/**
	 * 页面导航信息，如果有多页，则显示页面导航条信息
	 *
	 * @param pageInfo
	 * @param allCheckFlag
	 *            如果导航条上显示全选按钮，则true
	 * @return
	 */
	public static String pagePolit(PagesInfoStruct pageInfo,
			boolean allCheckFlag, boolean simpleFlag) {
		StringBuffer sbPagePolit = new StringBuffer();
		// 如果有多页，则显示翻页导航条信息
		if (pageInfo.iPages > 0) {
			sbPagePolit.append("<table width=\"300px\">\n<tr>\n");
			if (allCheckFlag) {
				sbPagePolit
						.append("<TD>[<a href=\"javascript:;\" onClick=\"checkAll(true)\">当页全选</a>]</TD>\n");
				sbPagePolit
						.append("<TD>[<a href=\"javascript:;\" onClick=\"checkAll(false)\">空选</a>]</TD>\n");
			}
			if (simpleFlag)
				sbPagePolit
						.append("<TD nowrap class=\"report-desc\" align=\"left\">当前是第"
								+ (pageInfo.iCurPage + 1)
								+ "/"
								+ (pageInfo.iPages + 1)
								+ "页&nbsp;&nbsp;共"
								+ (pageInfo.iLines) + "条&nbsp;&nbsp;</TD>\n");
			else
				sbPagePolit
						.append("<TD nowrap class=\"report-desc\" align=\"left\">当前是第"
								+ (pageInfo.iCurPage + 1)
								+ "/"
								+ (pageInfo.iPages + 1)
								+ "页&nbsp;&nbsp;共"
								+ (pageInfo.iLines) + "条&nbsp;&nbsp;</TD>\n");

			if (pageInfo.iCurPage == 0) {

				sbPagePolit
						.append("<TD  align=\"right\" nowrap=\"nowrap\"><a href=\"javascript:;\" class=\"ash\"><img src=\"../images/common/system/arrow3.gif\" title=\"第一页\" width=\"16\" height=\"16\"></a>\n");
				sbPagePolit
						.append("<a href=\"javascript:;\" class=\"ash\"><img src=\"../images/common/system/arrow1.gif\" title=\"前一页\" width=\"16\" height=\"16\"></a>\n");
				sbPagePolit
						.append("<a href=\"javascript:;\" class=\"ash\" onclick=\"javascript:nextPage();\" ><img src=\"../images/common/system/arrow2.gif\" title=\"下一页\" width=\"16\" height=\"16\"></a>\n");
				sbPagePolit
						.append("<a href=\"javascript:;\" class=\"ash\" onclick=\"lastPage()\"><img src=\"../images/common/system/arrow4.gif\"  title=\"最后一页\" width=\"16\" height=\"16\"></a></TD>\n");

			} else if (pageInfo.iCurPage == pageInfo.iPages) {
				sbPagePolit
						.append("<TD  align=\"right\" nowrap=\"nowrap\"><a href=\"javascript:;\" class=\"ash\" onclick=\"firstPage()\"><img src=\"../images/common/system/arrow3.gif\" title=\"第一页\"  width=\"16\" height=\"16\"></a>\n");
				sbPagePolit
						.append("<a href=\"javascript:;\" class=\"ash\" onclick=\"prePage()\"><img src=\"../images/common/system/arrow1.gif\" title=\"前一页\" width=\"16\" height=\"16\"></a>\n");
				sbPagePolit
						.append("<a href=\"javascript:;\" class=\"ash\" ><img src=\"../images/common/system/arrow2.gif\" title=\"下一页\" width=\"16\" height=\"16\"></a>\n");
				sbPagePolit
						.append("<a href=\"javascript:;\" class=\"ash\"><img src=\"../images/common/system/arrow4.gif\" title=\"最后一页\" width=\"16\" height=\"16\"></a></TD>\n");

			} else {
				sbPagePolit
						.append("<TD  align=\"right\" nowrap=\"nowrap\"><a href=\"javascript:;\" class=\"ash\" onclick=\"firstPage()\"><img src=\"../images/common/system/arrow3.gif\" title=\"第一页\"  width=\"16\" height=\"16\"></a>\n");
				sbPagePolit
						.append("<a href=\"javascript:;\" class=\"ash\" onclick=\"prePage()\"><img src=\"../images/common/system/arrow1.gif\" title=\"前一页\" width=\"16\" height=\"16\"></a>\n");
				sbPagePolit
						.append("<a href=\"javascript:;\" class=\"ash\" onclick=\"nextPage()\" ><img src=\"../images/common/system/arrow2.gif\" title=\"下一页\"  width=\"16\" height=\"16\"></a>\n");
				sbPagePolit
						.append("<a href=\"javascript:;\" class=\"ash\" onclick=\"lastPage()\"><img src=\"../images/common/system/arrow4.gif\" title=\"最后一页\"  width=\"16\" height=\"16\"></a></TD>\n");

			}
			if (!simpleFlag)
				// maxlength=\"3\"  分页中跳转的页数不仅仅为三位数。
				sbPagePolit
						.append("<TD nowrap align=\"right\">转到<input type=\"text\" name=\"SYS_GoPage\" style=\"width:50px\">页&nbsp;<input name=\"gobt\" type=\"image\" src=\"../images/common/system/go.gif\" width=\"18\" height=\"18\" border=\"0\" onclick=\"goFixPage()\"></TD>\n");
			sbPagePolit.append("</tr></table>\n");
		} else {
			sbPagePolit.append("<table width=\"100%\">\n<tr>\n");
			if (allCheckFlag) {
				sbPagePolit
						.append("<TD>[<a href=\"javascript:;\" onClick=\"checkAll(true)\">全选</a>]</TD>\n");
				sbPagePolit
						.append("<TD>[<a href=\"javascript:;\" onClick=\"checkAll(false)\">空选</a>]</TD>\n");

			}

			if (simpleFlag)
				sbPagePolit.append("<TD align=\"right\">" + (pageInfo.iCurPage + 1) + "/"
						+ (pageInfo.iPages + 1) + "页;" + (pageInfo.iLines)
						+ "条</TD>\n");
			else
				sbPagePolit.append("<TD align=\"right\">" + (pageInfo.iCurPage + 1) + "/"
						+ (pageInfo.iPages + 1) + "页&nbsp;共"
						+ (pageInfo.iLines) + "条</TD>\n");

			sbPagePolit.append("</tr></table>\n");
		}
		return sbPagePolit.toString();
	}

	public static String pagePolit(PagesInfoStruct pageInfo,
			boolean allCheckFlag, boolean simpleFlag, String rootPath) {
		StringBuffer sbPagePolit = new StringBuffer();
		// 如果有多页，则显示翻页导航条信息
		if (pageInfo.iPages > 0) {
			sbPagePolit.append("<table>\n");
			if (allCheckFlag) {
				sbPagePolit
						.append("<TD>[<a href=\"javascript:;\" onClick=\"checkAll(true)\">当页全选</a>]</TD>\n");
				sbPagePolit
						.append("<TD>[<a href=\"javascript:;\" onClick=\"checkAll(false)\">空选</a>]</TD>\n");
			}
			if (simpleFlag)
				sbPagePolit
						.append("<TD nowrap class=\"report-desc\" align=\"right\">当前是第"
								+ (pageInfo.iCurPage + 1)
								+ "/"
								+ (pageInfo.iPages + 1)
								+ "页&nbsp;&nbsp;共"
								+ (pageInfo.iLines) + "条&nbsp;&nbsp;</TD>\n");
			else
				sbPagePolit
						.append("<TD nowrap class=\"report-desc\" align=\"right\">当前是第"
								+ (pageInfo.iCurPage + 1)
								+ "/"
								+ (pageInfo.iPages + 1)
								+ "页&nbsp;&nbsp;共"
								+ (pageInfo.iLines) + "条&nbsp;&nbsp;</TD>\n");

			if (pageInfo.iCurPage == 0) {

				sbPagePolit
						.append("<TD  align=\"right\" nowrap=\"nowrap\"><a href=\"javascript:;\" class=\"ash\"><img src=\""
								+ rootPath
								+ "/images/common/system/arrow3.gif\" title=\"第一页\" width=\"16\" height=\"16\"></a>\n");
				sbPagePolit
						.append("<a href=\"javascript:;\" class=\"ash\">上一页</a><img src=\""
								+ rootPath
								+ "/images/common/system/arrow1.gif\" width=\"16\" height=\"16\">\n");
				sbPagePolit
						.append("<img src=\""
								+ rootPath
								+ "/images/common/system/arrow2.gif\" width=\"16\" height=\"16\"><a href=\"javascript:;\" class=\"ash\" onclick=\"nextPage()\" >下一页</a>\n");
				sbPagePolit
						.append("<img src=\""
								+ rootPath
								+ "/images/common/system/arrow4.gif\" width=\"16\" height=\"16\"><a href=\"javascript:;\" class=\"ash\" onclick=\"lastPage()\">末页</a>\n");

				// sbPagePolit.append("<TD><span style=\"font-family:webdings;cursor:hand\" title=\"第一页\">7</span></TD><TD><span style=\"font-family:webdings;cursor:hand\" title=\"上一页\">3</span></TD>\n");
				// sbPagePolit.append("<TD><span style=\"font-family:webdings;cursor:hand\" title=\"下一页\" onclick=\"nextPage()\" onmouseover=\"changestyle(0)\"  onmouseout=\"changestyle(1)\">4</span></TD>\n");
				// sbPagePolit.append("<TD><span style=\"font-family:webdings;cursor:hand\" title=\"末页\" onclick=\"lastPage()\" onmouseover=\"changestyle(0)\"  onmouseout=\"changestyle(1)\">8</span></TD>\n");
			} else if (pageInfo.iCurPage == pageInfo.iPages) {
				sbPagePolit
						.append("<TD  align=\"right\" nowrap=\"nowrap\"><a href=\"javascript:;\" class=\"ash\" onclick=\"firstPage()\">首页</a><img src=\""
								+ rootPath
								+ "/images/common/system/arrow3.gif\" width=\"13\" height=\"11\">\n");
				sbPagePolit
						.append("<a href=\"javascript:;\" class=\"ash\" onclick=\"prePage()\">上一页</a><img src=\""
								+ rootPath
								+ "/images/common/system/arrow1.gif\" width=\"6\" height=\"11\">\n");
				sbPagePolit
						.append("<img src=\""
								+ rootPath
								+ "/images/common/system/arrow2.gif\" width=\"6\" height=\"11\"><a href=\"javascript:;\" class=\"ash\" >下一页</a>\n");
				sbPagePolit
						.append("<img src=\""
								+ rootPath
								+ "/images/common/system/arrow4.gif\" width=\"13\" height=\"11\"><a href=\"javascript:;\" class=\"ash\">末页</a>\n");

				// sbPagePolit.append("<TD><span style=\"font-family:webdings;cursor:hand\" title=\"第一页\" onclick=\"firstPage()\" onmouseover=\"changestyle(0)\"  onmouseout=\"changestyle(1)\">7</span></TD>\n");
				// sbPagePolit
				// .append("<TD><span style=\"font-family:webdings;cursor:hand\" title=\"上一页\" onclick=\"prePage()\" onmouseover=\"changestyle(0)\"  onmouseout=\"changestyle(1)\">3</span></TD>\n");
				// sbPagePolit
				// .append("<TD><span style=\"font-family:webdings;cursor:hand\" title=\"下一页\">4</span></TD><TD><span style=\"font-family:webdings;cursor:hand\" title=\"末页\">8</span></TD>\n");
			} else {
				sbPagePolit
						.append("<TD  align=\"right\" nowrap=\"nowrap\"><a href=\"javascript:;\" class=\"ash\" onclick=\"firstPage()\">首页</a><img src=\""
								+ rootPath
								+ "/images/common/system/arrow3.gif\" width=\"13\" height=\"11\">\n");
				sbPagePolit
						.append("<a href=\"javascript:;\" class=\"ash\" onclick=\"prePage()\">上一页</a><img src=\""
								+ rootPath
								+ "/images/common/system/arrow1.gif\" width=\"6\" height=\"11\">\n");
				sbPagePolit
						.append("<img src=\""
								+ rootPath
								+ "/images/common/system/arrow2.gif\" width=\"6\" height=\"11\"><a href=\"javascript:;\" class=\"ash\" onclick=\"nextPage()\" >下一页</a>\n");
				sbPagePolit
						.append("<img src=\""
								+ rootPath
								+ "/images/common/system/arrow4.gif\" width=\"13\" height=\"11\"><a href=\"javascript:;\" class=\"ash\" onclick=\"lastPage()\">末页</a>\n");

				// sbPagePolit
				// .append("<TD><span style=\"font-family:webdings;cursor:hand\" title=\"第一页\" onclick=\"firstPage()\" onmouseover=\"changestyle(0)\"  onmouseout=\"changestyle(1)\">7</span></TD>\n");
				// sbPagePolit
				// .append("<TD><span style=\"font-family:webdings;cursor:hand\" title=\"上一页\" onclick=\"prePage()\" onmouseover=\"changestyle(0)\"  onmouseout=\"changestyle(1)\">3</span></TD>\n");
				// sbPagePolit
				// .append("<TD><span style=\"font-family:webdings;cursor:hand\" title=\"下一页\" onclick=\"nextPage()\" onmouseover=\"changestyle(0)\"  onmouseout=\"changestyle(1)\">4</span></TD>\n");
				// sbPagePolit
				// .append("<TD><span style=\"font-family:webdings;cursor:hand\" title=\"末页\" onclick=\"lastPage()\" onmouseover=\"changestyle(0)\"  onmouseout=\"changestyle(1)\">8</span></TD>\n");
			}
			if (!simpleFlag)
				sbPagePolit
						.append("转到<input type=\"text\" name=\"SYS_GoPage\" style=\"width:50px\">页&nbsp;<input name=\"gobt\" type=\"image\" src=\""
								+ rootPath
								+ "/images/common/system/go.gif\" width=\"18\" height=\"18\" border=\"0\" onclick=\"goFixPage()\"></TD>\n");
			sbPagePolit.append("</table>\n");
		} else {
			sbPagePolit.append("<table>\n");
			if (allCheckFlag) {
				sbPagePolit
						.append("<TD>[<a href=\"javascript:;\" onClick=\"checkAll(true)\">全选</a>]</TD>\n");
				sbPagePolit
						.append("<TD>[<a href=\"javascript:;\" onClick=\"checkAll(false)\">空选</a>]</TD>\n");

			}

			if (simpleFlag)
				sbPagePolit.append("<TD nowrap class=\"report-desc\" align=\"right\" valign=\"bottom\">" + (pageInfo.iCurPage + 1) + "/"
						+ (pageInfo.iPages + 1) + "页;" + (pageInfo.iLines)
						+ "条</TD>\n");
			else
				sbPagePolit.append("<TD align=\"right\">" + (pageInfo.iCurPage + 1) + "/"
						+ (pageInfo.iPages + 1) + "页&nbsp;共"
						+ (pageInfo.iLines) + "条</TD>\n");

			sbPagePolit.append("</table>\n");
		}

		return sbPagePolit.toString();
	}

	/**
	 * 以图标的方式显示翻页信息
	 *
	 * @param pageInfo
	 * @param allCheckFlag
	 * @return
	 */
	public static void pagePolitIcon(PagesInfoStruct pageInfo) {

	}

	/**
	 * 获取页面选定的id值
	 *
	 * @param request
	 * @return
	 */
	public static String getCheckIDs(HttpServletRequest request) {
		return getCheckIDs(request, "page");
	}

	/**
	 * 指定特殊的页信息前缀，默认为"page",取得选择checkIDs,如果失败则返回为null
	 *
	 * @param request
	 * @param pageTag
	 * @return
	 */
	public static String[] getCheckIDs2Array(HttpServletRequest request,
			String pageTag) {
		// return getCheckIDs2Array(request, pageTag);
		String checks = getCheckIDs(request, pageTag);
		if (checks == null)
			return null;
		checks = StringB.trim(checks, ",");
		Vector v = StringB.parseStringTokenizer(checks, ",");
		if (v == null)
			return null;
		String retStr[] = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			retStr[i] = (String) v.elementAt(i);
		}
		return retStr;

	}

	/**
	 * 以数组的方式取得checkIDs,如果失败则返回为null
	 *
	 * @param request
	 * @return
	 */
	public static String[] getCheckIDs2Array(HttpServletRequest request) {
		String checks = getCheckIDs(request);
		if (checks == null)
			return null;
		checks = StringB.trim(checks, ",");
		Vector v = StringB.parseStringTokenizer(checks, ",");
		if (v == null)
			return null;
		String retStr[] = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			retStr[i] = (String) v.elementAt(i);
		}
		return retStr;
	}

	/**
	 * 获取页面选定的id值
	 *
	 * @param request
	 *            http请求参数
	 * @param pagePrefix
	 *            页面的前缀
	 * @return 如果没有取得则返回null
	 */
	public static String getCheckIDs(HttpServletRequest request,
			String pagePrefix) {
		if (request.getParameterMap().containsKey(pagePrefix + "__checkIDs")) {
			System.out.println("from request!!! checkID=" + pagePrefix
					+ "__checkIDs"
					+ request.getParameter(pagePrefix + "__checkIDs"));
			return (String) request.getParameter(pagePrefix + "__checkIDs");
		} else {
			// 从请求参数缓存中获取page属性信息
			HashMap newParams = (HashMap) request.getSession().getAttribute(
					PREVIOUS_REQUEST_PARAMETERS);
			if (newParams != null
					&& newParams.containsKey(pagePrefix + "__checkIDs")) {
				System.out.println("from cache!!! checkID=" + pagePrefix
						+ "__checkIDs"
						+ newParams.get(pagePrefix + "__checkIDs"));
				return (String) newParams.get(pagePrefix + "__checkIDs");
			}
		}
		return null;
	}

	public static PagesInfoStruct getPageInfo(HttpServletRequest request,
			int iLines, int iLinesPerPage) {
		return getPageInfo(request, iLines, iLinesPerPage, "page");
	}

	/**
	 * 取得页面翻页相关的信息
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param iLines
	 *            翻页内容的总行数
	 * @param iLinesPerPage
	 *            每页的行数
	 * @param pagePrefix
	 *            page信息在页面中的前缀【可选，默认为page】
	 * @return 设置后的页面信息
	 */
	public static PagesInfoStruct getPageInfo(HttpServletRequest request,
			int iLines, int iLinesPerPage, String pagePrefix) {
		// 取得页面配置信息
		PagesInfoStruct pageInfo = new PagesInfoStruct();
		// 首先查看是否在页面中有相关属性
		if (request.getParameterMap().containsKey(pagePrefix + "__checkIDs")) {
			try {
				AppWebUtil.getHtmlObject(request, pagePrefix, pageInfo);
			} catch (AppException ex) {
				pageInfo = new PagesInfoStruct();
			}
		} else {
			// 从请求参数缓存中获取page属性信息
			HashMap newParams = (HashMap) request.getSession().getAttribute(
					PREVIOUS_REQUEST_PARAMETERS);
			if (newParams != null
					&& newParams.containsKey(pagePrefix + "__checkIDs")) {
				pageInfo.checkIDs = (String) newParams.get(pagePrefix
						+ "__checkIDs");
				pageInfo.pagePrefix = (String) newParams.get(pagePrefix
						+ "__pagePrefix");
				pageInfo.iCurPage = StringB.toInt(
						(String) newParams.get(pagePrefix + "__iCurPage"), -1);
				pageInfo.iLines = StringB.toInt(
						(String) newParams.get(pagePrefix + "__iLines"), -1);
				pageInfo.iLinesPerPage = StringB.toInt(
						(String) newParams.get(pagePrefix + "__iLinesPerPage"),
						-1);
				pageInfo.iPages = StringB.toInt(
						(String) newParams.get(pagePrefix + "__iPages"), -1);
			}
		}
		if (pageInfo.iCurPage == -1) {
			// 初次设置
			pageInfo.iCurPage = 0;
			pageInfo.checkIDs = "";
		}
		// 对于总长度变化时，要重置总长度
		pageInfo.iLinesPerPage = iLinesPerPage;
		pageInfo.iLines = iLines;
		pageInfo.calcPages();
		pageInfo.pagePrefix = pagePrefix;
		return pageInfo;
	}

	/**
	 * 设置不显示的条目
	 *
	 * @param pageInfo
	 * @param ids
	 */
	public static void setBlankIDs(PagesInfoStruct pageInfo, String ids) {
		if (ids != null)
			pageInfo.blankIDs = ids.split(",");
	}

	/**
	 * 设置已经选择的条目
	 *
	 * @param pageInfo
	 * @param ids
	 */
	public static void setUsedIDs(PagesInfoStruct pageInfo, String ids)
			throws AppException {
		if (ids == null)
			return;

		if (!ids.startsWith(CommKeys.ID_DELI))
			ids = CommKeys.ID_DELI + ids;
		if (!ids.endsWith(CommKeys.ID_DELI))
			ids = ids + CommKeys.ID_DELI;

		if (pageInfo.checkIDs == null || "".equals(pageInfo.checkIDs))
			pageInfo.checkIDs = ids;
		else {
			throw new AppException("已经定义选择的IDs了,setUsedIDs只能作用于翻页处理的初始部分!");
		}
	}

	/**
	 * 存储页面信息的一些隐藏输入域
	 *
	 * @param pageInfo
	 *            页面信息
	 * @return hidden的页面的输出内容
	 */
	public static String pageHidden(PagesInfoStruct pageInfo) {
		return pageHidden(pageInfo, "page");
	}

	public static String pageHidden(PagesInfoStruct pageInfo, String pagePrefix) {
		StringBuffer sbPageHidden = new StringBuffer();

		// sbPageHidden.append("<!--存储页面相关信息-->\n");
		sbPageHidden.append("");
		sbPageHidden.append("<input type=\"hidden\" name=\"" + pagePrefix
				+ "__iCurPage\" id=\""+pagePrefix+"__iCurPage\" value=" + pageInfo.iCurPage + ">\n");
		sbPageHidden.append("<input type=\"hidden\" name=\"" + pagePrefix
				+ "__iLinesPerPage\" id=\""+pagePrefix+"__iLinesPerPage\" value=" + pageInfo.iLinesPerPage + ">\n");
		sbPageHidden.append("<input type=\"hidden\" name=\"" + pagePrefix
				+ "__iLines\"  id=\""+pagePrefix+"__iLines\" value=" + pageInfo.iLines + ">\n");
		sbPageHidden.append("<input type=\"hidden\" name=\"" + pagePrefix
				+ "__iPages\"  id=\""+pagePrefix+"__iPages\" value=" + pageInfo.iPages + ">\n");
		sbPageHidden.append("<input type=\"hidden\" name=\"" + pagePrefix
				+ "__checkIDs\"  id=\""+pagePrefix+"__checkIDs\" value=" + pageInfo.checkIDs + ">\n");

		return sbPageHidden.toString();
	}

	/**
	 * 一行信息中的checkbox域的输出
	 *
	 * @param pageInfo
	 *            页信息
	 * @param checkValue
	 *            该行check的值，通常这个值要求是该行记录的key值
	 * @return
	 */
	public static String tdCheckBox(PagesInfoStruct pageInfo, String checkValue) {
		// 不显示选择框的条目
		if (isExist(pageInfo.blankIDs, checkValue))
			return "";

		StringBuffer sbCheck = new StringBuffer();

		sbCheck.append("<input type=\"checkbox\" name=\"pageChkBox\" value=\""
				+ checkValue + "\" onClick=\"checkID(this)\" ");
		if (pageInfo.isInIDs(checkValue))
			sbCheck.append(" checked ");
		sbCheck.append(">\n");
		return sbCheck.toString();
	}

	/**
	 * 一行信息中的checkbox域的输出
	 *
	 * @param pageInfo
	 *            页信息
	 * @param checkValue
	 *            该行check的值，通常这个值要求是该行记录的key值
	 * @param selfFunc
	 *            自定义的函数设置 注意，这里面如果有串参数要用单引号，否则可能有问题
	 * @return
	 */
	public static String tdCheckBox(PagesInfoStruct pageInfo,
			String checkValue, String selfFunc) {
		// 不显示选择框的条目
		if (isExist(pageInfo.blankIDs, checkValue))
			return "";
		StringBuffer sbCheck = new StringBuffer();
		if (selfFunc == null)
			selfFunc = "";

		sbCheck.append("<input type=\"checkbox\" name=\"pageChkBox\" value=\""
				+ checkValue + "\" onClick=\"checkID(this);" + selfFunc + "\" ");
		if (pageInfo.isInIDs(checkValue))
			sbCheck.append(" checked ");
		sbCheck.append(">\n");
		return sbCheck.toString();
	}

	/**
	 * 一行信息中的radio域的输出
	 *
	 * @param pageInfo
	 *            页信息
	 * @param radioValue
	 *            该行radio的值，通常这个值要求是该行记录的key值
	 * @return
	 */
	public static String tdRadio(PagesInfoStruct pageInfo, String radioValue) {
		// 不显示选择框的条目
		if (isExist(pageInfo.blankIDs, radioValue))
			return "";

		StringBuffer sbRadio = new StringBuffer();

		sbRadio.append("<input type=\"radio\" name=\"wkRadio\" value=\""
				+ radioValue + "\" onClick=\"radioID(this)\" ");
		if (pageInfo.isInIDs(radioValue))
			sbRadio.append(" checked ");
		sbRadio.append(">\n");
		return sbRadio.toString();
	}

	/**
	 * 一行信息中的radio域的输出
	 *
	 * @param pageInfo
	 *            页信息
	 * @param radioValue
	 *            该行radio的值，通常这个值要求是该行记录的key值
	 * @param selfFunc
	 *            自定义的函数设置,注意，这里面如果有串参数要用单引号，否则可能有问题
	 * @return
	 */
	public static String tdRadio(PagesInfoStruct pageInfo, String radioValue,
			String selfFunc) {
		// 不显示选择框的条目
		if (isExist(pageInfo.blankIDs, radioValue))
			return "";

		StringBuffer sbRadio = new StringBuffer();

		sbRadio.append("<input type=\"radio\" name=\"wkRadio\" value=\""
				+ radioValue + "\" onClick=\"radioID(this);" + selfFunc + "\" ");
		if (pageInfo.isInIDs(radioValue))
			sbRadio.append(" checked ");
		sbRadio.append(">\n");
		return sbRadio.toString();
	}

	/**
	 * 以Table的方式显示结果集合内的数据
	 *
	 * @param ds
	 *            结果集合
	 * @param pageInfo
	 *            翻页信息
	 * @param titles
	 *            table的标题
	 * @param fields
	 *            table要显示的列
	 * @param iColumns
	 *            每一页要显示的行数
	 * @param thClass
	 *            th的显示格式
	 * @param iColumns
	 *            td的显示格式
	 * @return 显示的table html内容
	 */
	public static String tableContent(DataSet ds, PagesInfoStruct pageInfo,
			String titles, String fields, int iColumns, String thClass,
			String tdClass) {
		if (thClass == null)
			thClass = "";
		if (tdClass == null)
			tdClass = "";
		StringBuffer sbTable = new StringBuffer();
		Vector vFields = StringB.parseStringTokenizer(fields, ",");
		Vector vTitles = StringB.parseStringTokenizer(titles, ",");
		/* 徐磊 2004-12-16 修改 增加table的style */
		sbTable.append(" <table align=\"center\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"list-table\">\n");
		sbTable.append(" <tr>\n");
		// 设置table标题区域
		String c = " align=\"center\" ";
		String f = "";
		for (int l = 0; l < iColumns; l++)
			for (int i = 0; i < vTitles.size(); i++) {
				if (vFields.size() > i)
					f = vFields.elementAt(i) + "";
				else
					f = "";
				if (f.startsWith("!") || f.startsWith("?"))
					c = " align=\"center\" ";
				else
					c = "";
				sbTable.append("   <td " + c + "nowrap " + thClass + ">"
						+ vTitles.elementAt(i) + "</td>\n");
			}
		sbTable.append(" </TR>\n");

		for (int i = 0; i < pageInfo.iLinesPerPage
				&& ((i + pageInfo.absRowNoCurPage()) < pageInfo.iLines);) {
			sbTable.append(" <TR>\n");
			int j = -1;
			while ((i + pageInfo.absRowNoCurPage()) < pageInfo.iLines
					&& j++ < (iColumns - 1)) {
				int iRow = (i++) + pageInfo.absRowNoCurPage();
				for (int k = 0; k < vFields.size(); k++) {
					sbTable.append(" <TD nowrap " + tdClass + ">\n");
					String field = (String) vFields.elementAt(k);
					field = field.trim();
					String fieldName = field;
					int iType = 0;
					if (field.startsWith("!")) {
						iType = 1;
						fieldName = field.substring(1);
					}
					if (field.startsWith("?")) {
						iType = 2;
						fieldName = field.substring(1);
					}
					String fieldValue = null;
					if (ROWID.equalsIgnoreCase(fieldName))
						fieldValue = iRow + "";
					else
						fieldValue = ds
								.elementAt(iRow, fieldName.toLowerCase());

					if (fieldValue == null)
						fieldValue = fieldName;

					if (iType == 0)
						sbTable.append(" " + fieldValue);
					else if (iType == 1)
						sbTable.append(" "
								+ WebPageTool.tdRadio(pageInfo, fieldValue));
					else if (iType == 2)
						sbTable.append(" "
								+ WebPageTool.tdCheckBox(pageInfo, fieldValue));
					sbTable.append(" </TD>\n");
				}
			}
			sbTable.append("</TR>\n");
		}
		sbTable.append(" </table>\n");

		return sbTable.toString();
	}

	/**
	 * 以Table的方式显示结果集合内的数据
	 *
	 * @param ds
	 *            结果集合
	 * @param pageInfo
	 *            翻页信息
	 * @param titles
	 *            table的标题
	 * @param fields
	 *            table要显示的列
	 * @param iColumns
	 *            每一页要显示的行数
	 * @param thClass
	 *            th的显示格式
	 * @param iColumns
	 *            td的显示格式
	 * @return 显示的table html内容
	 */
	public static String tableContent(DataSet ds, PagesInfoStruct pageInfo,
			String titles, String fields, int iColumns) {
		/* xulei 修改 2004-12-16 改变缺省style */
		String th_class = " class=\"list-label\" ";
		String td_class = " class=\"list-value\" ";
		return tableContent(ds, pageInfo, titles, fields, iColumns, th_class,
				td_class);
	}

	/**
	 * 以Table方式显示tables或者combTables的数据
	 *
	 * @param tabInfo
	 *            存储table或者combTable的数组
	 * @param pageInfo
	 *            翻页信息
	 * @param titles
	 *            table的标题
	 * @param fields
	 *            table要显示的列
	 * @param iColumns
	 *            每一页要显示的行数
	 * @return
	 */
	public static String tableContent(JBTable tabInfo[],
			PagesInfoStruct pageInfo, String titles, String fields, int iColumns) {
		DataSet ds = DataSet.tabs2DS(tabInfo);
		return tableContent(ds, pageInfo, titles, fields, iColumns);
	}

	/**
	 * 以Table方式显示tables或者combTables的数据
	 *
	 * @param tabInfo
	 *            存储table或者combTable的数组
	 * @param pageInfo
	 *            翻页信息
	 * @param titles
	 *            table的标题
	 * @param fields
	 *            table要显示的列
	 * @param iColumns
	 *            每一页要显示的行数
	 * @return
	 */
	public static String tableContent(JBTable tabInfo[],
			PagesInfoStruct pageInfo, String titles, String fields,
			int iColumns, String thClass, String tdClass) {
		DataSet ds = DataSet.tabs2DS(tabInfo);
		return tableContent(ds, pageInfo, titles, fields, iColumns, thClass,
				tdClass);
	}

	private static boolean isExist(String[] ids, String id) {
		if (id == null)
			return false;
		for (int i = 0; ids != null && i < ids.length; i++) {
			if (id.equals(ids[i]))
				return true;
		}
		return false;
	}

	public static void main(String[] args) {
		Vector v = StringB.parseStringTokenizer("123,456,789,", ",");
		for (int i = 0; i < v.size(); i++) {
			System.out.println(v.elementAt(i));
		}
		System.out.println("asfdfsasafd");

	}
}