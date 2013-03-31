package com.ailk.bi.common.taglib;

/**
 * <p>Title: SelfRefresh</p>
 * <p>Description:通用的页面自回显tag</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author WiseKing
 * @version 1.0
 */
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.event.JBTable;

/**
 * 通用的页面自回显tag（内部机制使用java.lang.reflect的基础上完成)
 * <p>
 * Title: asiabi J2EE Web 程序的框架和底层的定义
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: BeiJing asiabi Info System Co,Ltd.
 * </p>
 *
 * @author WISEKING
 * @version 1.0 目前的使用格式为：
 *
 *          <BIBM:SelfRefreshTag pageNames =
 *          "from1.cust,from1.user,from2.account,..."
 *          [必选][回显对象在jsp中的名字，通常要指定form名称,如果前面为!则是对DIV类型的内容进行回显] attrNames =
 *          "attr.CUST,attr.USER,attr.ACCT,..." [必选][对象在session中存放的属性名] hint =
 *          "R=from1.cust_sex,....;C=from1.cust_hpy;N=from1.xxx;P=from1.passwd;...."
 *          [可选][对于radio,checkbox,N不显示,password(P)
 *          类型的页面输入域进行提示，如果没有特殊说明则可以不设置此属性] warn = "1"
 *          [可选][如果在tag处理过程中发生错误，如何告警][1进行告警提示] />
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SelfRefresh extends BodyTagSupport {
	/**
	 *
	 */
	private static final long serialVersionUID = 4024382654045353285L;

	private final static String PWD_RPT = "_RPT";

	/**
	 * 定义选择框属性
	 */
	private String pageNames = "";

	private String attrNames = "";

	private String warn = "0";

	private String hint = "";

	HashMap hmRadioHints = new HashMap();

	HashMap hmCheckHints = new HashMap();

	HashMap hmNoDispHints = new HashMap();

	HashMap hmPwdDispHints = new HashMap();

	public int doEndTag() throws JspException {
		Vector vPageNames = StringB.parseStringTokenizer(pageNames, ",");
		Vector vAttrNames = StringB.parseStringTokenizer(attrNames, ",");
		Vector vHints = StringB.parseStringTokenizer(hint, ";");

		for (int i = 0; i < vHints.size(); i++) {
			String s = (String) vHints.elementAt(i);
			if (s.startsWith("R=")) // Radio Types
			{
				Vector v = StringB.parseStringTokenizer(s.substring(2), ",");
				for (int j = 0; j < v.size(); j++) {
					hmRadioHints.put((String) v.elementAt(j), "1");
				}
			}
			if (s.startsWith("C=")) // Checkbox Types
			{
				Vector v = StringB.parseStringTokenizer(s.substring(2), ",");
				for (int j = 0; j < v.size(); j++) {
					hmCheckHints.put((String) v.elementAt(j), "1");
				}
			}
			if (s.startsWith("N=")) // Not Display Attributes
			{
				Vector v = StringB.parseStringTokenizer(s.substring(2), ",");
				for (int j = 0; j < v.size(); j++) {
					hmNoDispHints.put((String) v.elementAt(j), "1");
				}
			}
			if (s.startsWith("P=")) // password Attributes
			{
				Vector v = StringB.parseStringTokenizer(s.substring(2), ",");
				for (int j = 0; j < v.size(); j++) {
					hmPwdDispHints.put((String) v.elementAt(j), "1");
				}
			}

		}

		JspWriter out = pageContext.getOut();
		try {
			out.print("<script language='JavaScript'>\n");
			// out.print("var reloadFlag=0;\n");
			out.print("function selfDisp()\n");
			out.print("{\n");

			//out.print(" if( SYS_ManAlwaysForm.ReLove.value==\"1\" )\n");
			//out.print("  return;\n");
			//out.print("\n  SYS_ManAlwaysForm.ReLove.value=\"1\";\n");

			for (int i = 0; i < vPageNames.size(); i++) {
				if (i >= vAttrNames.size()) {
					continue;
				}
				out.print("\n  //set " + (String) vPageNames.elementAt(i)
						+ " \n");
				printAClass(out, (String) vPageNames.elementAt(i),
						(String) vAttrNames.elementAt(i));
			}
			out.print("}\n");
			out.print("</script>\n");
			out.print("\n");
			out.print("<form name=\"SYS_ManAlwaysForm\">\n");
			out.print("<input type=\"hidden\" name=\"ReLove\">\n");
			out.print("</form>\n\n");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return (SKIP_BODY);
	}

	private void printAObject(JspWriter out, Object obj, String pName)
			throws IOException {
		if (obj == null)
			return;
		Field[] fields = obj.getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				if (fields[i].get(obj) instanceof JBTable) {
					continue;
				}
			} catch (Exception ex1) {
			}

			String v = "";
			try {
				v = StringB.getFieldValueByStr(fields[i], obj);
			} catch (Exception ex) {
				continue;
			}
			if ("null".equals(v))
				v = "";
			// xulei modify 2004-12-29 value值改为双引号表示
			v = "\"" + v + "\"";
			out.print("  try{\n");
			// 对radio和check进行特殊处理
			//String htmlInput = pName + "__" + fields[i].getName();
			String tmpPname = pName.substring(pName.indexOf("."));
			if(tmpPname.indexOf(".")==0){
				tmpPname = tmpPname.substring(1) + "__";
			}
			String htmlInput = "document.getElementById(\"" + tmpPname + fields[i].getName() + "\")";
			if (htmlInput.startsWith("!")) {
				htmlInput = htmlInput.substring(1);
				if (htmlInput.indexOf(".") > -1)
					htmlInput = htmlInput.substring(htmlInput.indexOf(".") + 1);
				out.print("    " + htmlInput + ".innerHTML=" + v + ";\n\n");
				;
			} else if (htmlInput.indexOf("multi_text") > 0) {// 多行文本
				// System.out.println("innerHTML:" + v);
				String[] noSplit = v.split("\r\n");
				String strOut = "";
				for (int kk = 0; kk < noSplit.length; kk++) {

					String num = noSplit[kk].trim();
					if (num.length() > 0) {
						if (strOut.length() == 0) {
							strOut = num;
						} else {

							strOut += "\\r\\n" + num;
							// System.out.println("strOut111:" + strOut);
						}
					}

				}
				// System.out.println("strOut:" + strOut);
				out.print("    " + htmlInput + ".innerText=" + strOut + ";\n\n");
			} else if (hmRadioHints.containsKey(htmlInput)) {
				out.print("    for(i=0; i<" + htmlInput + ".length; i++) {\n");
				out.print("      if( " + htmlInput + "[i].value==" + v
						+ " ) {\n");
				out.print("        " + htmlInput + "[i].checked=true;\n");
				out.print("        break; \n");
				out.print("      }\n");
				out.print("    }\n");
			} else if (hmCheckHints.containsKey(htmlInput)) {
				out.print("    if( " + htmlInput + ".value==" + v + " ) {\n");
				out.print("      " + htmlInput + ".checked=true;\n");
				out.print("    }\n");
			} else if (hmNoDispHints.containsKey(htmlInput)) {
				out.print("    //htmlInput no auto display. \n\n");
			} else if (hmPwdDispHints.containsKey(htmlInput)) {
				out.print("    " + htmlInput + ".value = " + v + ";\n");
				out.print("    " + htmlInput + PWD_RPT + ".value = " + v
						+ ";\n");
			} else {
				out.print("    var vObj = "+ htmlInput +";\n");
				out.print("    vObj.value = " + v + ";\n");
			}
			out.print("  }catch(e){;}\n\n");
		}

	}

	private void printAClass(JspWriter out, String pName, String aName)
			throws IOException {
		HttpSession session = pageContext.getSession();

		Object obj = session.getAttribute(aName);
		// 如果session里面的值为空，则直接返回（以后可以考虑：是否在页面设置类的初始值）
		if (obj == null)
			return;
		Field[] fields = obj.getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				if (fields[i].get(obj) instanceof JBTable) {
					printAObject(out, fields[i].get(obj), pName + "__"
							+ fields[i].getName());
				}
			} catch (Exception e) {
				continue;
			}
		}
		printAObject(out, obj, pName);
	}

	/*
	 * private void printAClass(JspWriter out, String pName, String aName)
	 * throws IOException { HttpSession session = pageContext.getSession();
	 *
	 * Object obj = session.getAttribute(aName);
	 * //如果session里面的值为空，则直接返回（以后可以考虑：是否在页面设置类的初始值） if (obj == null) return;
	 * Field[] fields = obj.getClass().getFields(); for (int i = 0; i <
	 * fields.length; i++) { String v = ""; try {
	 * v=StringB.getFieldValueByStr(fields[i], obj); } catch (Exception ex) {
	 * continue; } if( "null".equals(v) ) v = ""; v = "'"+v+"'"; out.print("
	 * try{\n"); //对radio和check进行特殊处理 String htmlInput = pName + "__" +
	 * fields[i].getName(); if( htmlInput.startsWith("!") ) { htmlInput =
	 * htmlInput.substring(1); if( htmlInput.indexOf(".")>-1 ) htmlInput =
	 * htmlInput.substring(htmlInput.indexOf(".")+1); out.print("
	 * "+htmlInput+".innerHTML="+v+";\n\n");; }else if(
	 * hmRadioHints.containsKey(htmlInput) ) {
	 * out.print(" for(i=0; i<"+htmlInput+".length; i++) {\n"); out.print(" if(
	 * "+htmlInput + "[i].value=="+v+" ) {\n"); out.print(" "+htmlInput
	 * +"[i].checked=true;\n"); out.print(" break; \n"); out.print(" }\n");
	 * out.print(" }\n"); }else if( hmCheckHints.containsKey(htmlInput) ) {
	 * out.print(" if( "+htmlInput + ".value=="+v+" ) {\n");
	 * out.print(" "+htmlInput +".checked=true;\n"); out.print(" }\n"); }else
	 * if( hmNoDispHints.containsKey(htmlInput) ) {
	 * out.print(" //htmlInput no auto display. \n\n"); }else if(
	 * hmPwdDispHints.containsKey(htmlInput) ) { out.print(" "+htmlInput +
	 * ".value = " + v + ";\n"); out.print(" "+htmlInput + PWD_RPT +".value = "
	 * + v + ";\n"); } else{ out.print(" "+htmlInput + ".value = " + v + ";\n");
	 * } out.print(" }catch(e){;}\n\n"); } }
	 */
	public void setAttrNames(String attrNames) {
		this.attrNames = attrNames;
	}

	public void setPageNames(String pageNames) {
		this.pageNames = pageNames;
	}

	public void setWarn(String warn) {
		this.warn = warn;
	}

	public String getAttrNames() {
		return attrNames;
	}

	public String getPageNames() {
		return pageNames;
	}

	public String getWarn() {
		return warn;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}
}