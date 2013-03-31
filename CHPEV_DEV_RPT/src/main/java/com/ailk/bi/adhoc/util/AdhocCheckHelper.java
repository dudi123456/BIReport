package com.ailk.bi.adhoc.util;

import javax.servlet.http.HttpSession;

import com.ailk.bi.adhoc.dao.impl.AdhocCheckDao;
import com.ailk.bi.adhoc.service.impl.AdhocCheckFacade;
import com.ailk.bi.adhoc.struct.AdhocCheckStruct;

import waf.controller.web.action.HTMLActionException;

public class AdhocCheckHelper {
	//
	private static AdhocCheckFacade facade = new AdhocCheckFacade(
			new AdhocCheckDao());

	/**
	 * 渠道类型过滤资渠道类型
	 * 
	 * @param session
	 * @param
	 * @param
	 * @return
	 */
	public synchronized static String ParentValueVsSubValue(
			HttpSession session, String objType, String subObjType,
			String parentObj, String sonObj, String depart_id)
			throws HTMLActionException {
		// 判断对象类型，如果为空，则返回！
		if (objType == null || "".equals(objType)) {
			return "";
		}

		AdhocCheckStruct parentList[] = facade.getCheckSQLInfo(objType);
		StringBuffer js = new StringBuffer();
		if (parentList == null) {
			js.append("<Script>function createParentValue(){}\nfunction filterSubValue(parent){}\n</Script>");
			return js.toString();
		}
		int j;
		js.append("<Script language=\"Javascript\">\n");
		// 生成OperType数据的JS函数
		js.append("function createParentValue() {\n");
		js.append("  var theObj=" + parentObj + ";\n");
		js.append("  var formObj=" + parentObj + ";\n");
		js.append("  //获得func_oper_type对象所属的form对象\n");
		js.append("  while (formObj.tagName!=\"FORM\"){\n");
		js.append("    formObj=formObj.parentElement;\n");
		js.append("  }\n");
		js.append("  //获得记录func_oper_type对象所选项的隐含对象\n");
		js.append("  for (i=0;i<formObj.elements.length;i++){\n");
		js.append("  if (formObj.elements[i].name==theObj.name){\n");
		js.append("    hiObj=formObj.elements[i-1];\n");
		js.append("    break;\n");
		js.append("    }\n");
		js.append("  }\n");
		js.append("  //获得下拉框的所选的id\n");
		js.append("  oldIdx=hiObj.value;\n");
		js.append("  if (oldIdx==null || oldIdx==-1 || oldIdx=='') oldIdx=0;\n");
		js.append("  //重新设置下拉框的值\n");
		js.append("  if(theObj.tagName==\"SELECT\"){\n");
		js.append("    cleanSelect(theObj);\n");

		js.append("  theObj.options[theObj.length]=new Option('全部','');\n");
		for (j = 0; j < parentList.length; j++) {
			js.append("    theObj.options[theObj.length]=new Option('"
					+ parentList[j].getDesc().trim() + "','"
					+ parentList[j].getKey().trim() + "');\n");
		}

		js.append("  }\n");
		js.append("  //重新设置下拉框为已选的id\n");
		js.append("  theObj.options[oldIdx].selected=true;\n");
		js.append("}\n");
		js.append("</Script>\n");
		// 产生地市过滤区县的脚本
		if (!"".equals(sonObj))
			js.append(setSubValue(session, subObjType, sonObj, parentList,
					depart_id));
		return js.toString();
	}

	/**
	 * 过滤子渠道类型
	 * 
	 * @param session
	 * @param operTypeObj
	 * @param teleTypeObj
	 * @return
	 */
	public synchronized static String setSubValue(HttpSession session,
			String sonObjType, String subObj, AdhocCheckStruct parentList[],
			String depart_id) {
		AdhocCheckStruct sonList[] = facade.getCheckSQLInfo(sonObjType);
		StringBuffer js = new StringBuffer();
		if (subObj == null) {
			js.append("<Script>function filterSubValue(parent){}\n</Script>");
			return js.toString();
		}
		int i, j;

		js.append("<Script language=\"Javascript\">\n");
		// 生成按operType过滤teleType的JS函数
		js.append("function filterSubValue(parent) {\n");
		js.append("try{\n");
		js.append("  var theObj=" + subObj + ";\n");
		js.append("  var formObj=" + subObj + ";\n");
		js.append("  while (formObj.tagName!=\"FORM\"){\n");
		js.append("    formObj=formObj.parentElement;\n");
		js.append("  }\n");
		js.append("  for (i=0;i<formObj.elements.length;i++){\n");
		js.append("  if (formObj.elements[i].name==theObj.name){\n");
		js.append("    hiObj=formObj.elements[i-1];\n");
		js.append("    break;\n");
		js.append("    }\n");
		js.append("  }\n");
		js.append("  oldIdx=hiObj.value;\n");
		js.append("  if (oldIdx==null || oldIdx==-1 || oldIdx=='') oldIdx=0;\n");
		js.append("  cleanSelect(theObj);\n");
		js.append("  theObj.options[theObj.length]=new Option('全部','');\n");

		for (i = 0; i < parentList.length; i++) {
			js.append("  if (parent=='" + parentList[i].getKey() + "'){\n");
			for (j = 0; j < sonList.length; j++) {
				if (sonList[j].getParent_key().equals(parentList[i].getKey())) {
					js.append("    theObj.options[theObj.length]=new Option('"
							+ sonList[j].getDesc().trim() + "','"
							+ sonList[j].getKey().trim() + "');\n");

				}
			}
			js.append("  }\n");

		}
		js.append("  if (parent==''){\n");
		for (j = 0; j < sonList.length; j++) {
			js.append("    theObj.options[theObj.length]=new Option('"
					+ sonList[j].getDesc().trim() + "','"
					+ sonList[j].getKey().trim() + "');\n");
		}
		js.append("  }\n");

		js.append("  theObj.options[oldIdx].selected=true;\n");
		js.append("}catch(e){;}\n");
		js.append("}\n");
		js.append("</Script>\n");
		return js.toString();
	}
}
