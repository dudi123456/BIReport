package com.ailk.bi.subject.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ailk.bi.subject.dao.IOppDao;
import com.ailk.bi.subject.service.IOppService;
import com.ailk.bi.subject.struct.OppParamStruct;
import com.ailk.bi.subject.struct.OppParamValueStruct;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class OppService implements IOppService {

	public IOppDao getDao() {
		return dao;
	}

	public void setDao(IOppDao dao) {
		this.dao = dao;
	}

	private IOppDao dao;

	public List getOppParamList() {
		return dao.getOppParamList();
	}

	public int addNewParam(String[] value) {

		return dao.addNewParam(value);
	}

	public int updateParam(String[] value) {

		return dao.updateParam(value);
	}

	public String genParamTableHtml() {

		StringBuffer sb = new StringBuffer("");

		ArrayList paramList = (ArrayList) dao.getOppParamList();
		if (paramList != null && paramList.size() > 0) {
			sb.append("<table style=\"width: 100%\" border=\"0\" bgcolor=\"#CFCFCF\" cellpadding=\"1\" cellspacing=\"1\"  style=\"margin:0;\">");
			sb.append(" <tr class=\"celtitle FixedTitleRow\">");
			sb.append("	<td align=\"center\"  bgcolor=\"E3E3E3\" width=\"5%\">操作</td> ");
			sb.append("	<td align=\"center\"  bgcolor=\"E3E3E3\" width=\"5%\">标识</td> ");
			sb.append("	<td align=\"center\"  bgcolor=\"E3E3E3\" width=\"20%\">名称</td> ");
			sb.append("	<td align=\"center\"  bgcolor=\"E3E3E3\" width=\"5%\">类型</td> ");
			sb.append("	<td align=\"center\"  bgcolor=\"E3E3E3\" width=\"20%\">规则</td> ");
			sb.append("	<td align=\"center\"  bgcolor=\"E3E3E3\" width=\"5%\">权重</td> ");
			sb.append("	<td align=\"center\"  bgcolor=\"E3E3E3\" width=\"5%\">状态</td> ");
			sb.append("	<td align=\"center\"  bgcolor=\"E3E3E3\" width=\"25%\">描述</td> ");
			sb.append("	<td align=\"center\"  bgcolor=\"E3E3E3\" width=\"5%\">编辑</td> ");
			sb.append("</tr>");

			for (int i = 0; i < paramList.size(); i++) {
				OppParamStruct struct = (OppParamStruct) paramList.get(i);
				String radioValue = struct.getParam_id() + "$$"
						+ struct.getParam_name() + "$$"
						+ struct.getParam_type() + "$$"
						+ struct.getParam_rule() + "$$"
						+ struct.getParam_weight() + "$$"
						+ struct.getParam_status() + "$$"
						+ struct.getParam_desc();

				sb.append("<tr class=\"celdata\" onmouseover=\"this.className='mouseM'\" onmouseout=\"this.className='celdata'\">");
				sb.append("<td align=\"center\"><input type=\"radio\"  name=\"param\" value=\""
						+ radioValue + "\" onclick=\"selectValue(this)\"></td>");
				sb.append("<td align=\"center\" >" + struct.getParam_id()
						+ "</td>");
				sb.append("<td align=\"left\" >" + struct.getParam_name()
						+ "</td>");
				sb.append("<td align=\"center\" >" + struct.getParam_type()
						+ "</td>");
				sb.append("<td align=\"left\" >" + struct.getParam_rule()
						+ "</td>");
				sb.append("<td align=\"center\" >" + struct.getParam_weight()
						+ "</td>");
				sb.append("<td align=\"center\" >" + struct.getParam_status()
						+ "</td>");
				sb.append("<td align=\"left\" >" + struct.getParam_desc()
						+ "</td>");
				sb.append("<td align=\"center\" ><a href=\"javascript:editParamValue('"
						+ struct.getParam_id() + "');\">编辑</a></td>");
				sb.append("</tr>");

			}

			sb.append("</table>");
		}

		return sb.toString();

	}

	public int deleteParam(String param_id) {

		return dao.deleteParam(param_id);
	}

	public List getOppParamValueList(String param_id) {
		return dao.getOppParamValueList(param_id);
	}

	public List getOppParam(String param_id) {
		return dao.getOppParam(param_id);
	}

	public int addNewParamLvl(String[] value) {

		return dao.addNewParamLvl(value);
	}

	public int deleteParamLvl(String whereStr) {

		return dao.deleteParamLvl(whereStr);
	}

	public int updateParamLvl(String[] value) {

		return dao.updateParamLvl(value);
	}

	public String genParamLvlTableHtml(String param_id) {

		StringBuffer sb = new StringBuffer("");

		ArrayList paramList = (ArrayList) dao.getOppParam(param_id);
		if (paramList != null && paramList.size() > 0) {
			sb.append("<table style=\"width: 100%\" border=\"0\" bgcolor=\"#CFCFCF\" cellpadding=\"1\" cellspacing=\"1\"  style=\"margin:0;\">");
			sb.append(" <tr class=\"celtitle FixedTitleRow\">");
			sb.append("	<td align=\"center\"  bgcolor=\"E3E3E3\" width=\"15%\">操作</td> ");
			sb.append("	<td align=\"center\"  bgcolor=\"E3E3E3\" width=\"20%\">标识</td> ");
			sb.append("	<td align=\"center\"  bgcolor=\"E3E3E3\" width=\"20%\">名称</td> ");
			sb.append("	<td align=\"center\"  bgcolor=\"E3E3E3\" width=\"20%\">值上域（闭）</td> ");
			sb.append("	<td align=\"center\"  bgcolor=\"E3E3E3\" width=\"20%\">值下域（闭）</td> ");

			sb.append("</tr>");
			ArrayList lvlList = (ArrayList) ((OppParamStruct) paramList.get(0))
					.getValueObjs();
			for (int i = 0; lvlList != null && i < lvlList.size(); i++) {
				OppParamValueStruct struct = (OppParamValueStruct) lvlList
						.get(i);
				String radioValue = struct.getParam_id() + "$$"
						+ struct.getParam_type() + "$$" + struct.getLvl_id()
						+ "$$" + struct.getLvl_name() + "$$"
						+ struct.getStart_val() + "$$" + struct.getEnd_val();

				sb.append("<tr class=\"celdata\" onmouseover=\"this.className='mouseM'\" onmouseout=\"this.className='celdata'\">");
				sb.append("<td align=\"center\"><input type=\"radio\"  name=\"param\" value=\""
						+ radioValue + "\" onclick=\"selectValue(this)\"></td>");

				sb.append("<td align=\"left\" >" + struct.getLvl_id() + "</td>");
				sb.append("<td align=\"center\" >" + struct.getLvl_name()
						+ "</td>");
				sb.append("<td align=\"center\" >" + struct.getStart_val()
						+ "</td>");
				sb.append("<td align=\"left\" >" + struct.getEnd_val()
						+ "</td>");
				sb.append("</tr>");

			}

			sb.append("</table>");
		}

		return sb.toString();

	}

	public List getOppParamList(String param_type) {

		return dao.getOppParamList(param_type);
	}

	public Map getOppParamWeightMap(String param_type) {
		Map map = new HashMap();
		ArrayList list = (ArrayList) dao.getOppParamList(param_type);
		for (int i = 0; list != null && list.size() > 0 && i < list.size(); i++) {
			OppParamStruct struct = (OppParamStruct) list.get(i);
			map.put(struct.getParam_rule().trim(), struct.getParam_weight()
					.trim());
		}
		return map;

	}
}
