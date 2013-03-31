package com.ailk.bi.metamanage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.ailk.bi.metamanage.dao.IImpactAnalyseDao;
import com.ailk.bi.metamanage.dao.impl.ImpactAnalyseDaoImpl;
import com.ailk.bi.metamanage.model.JobTableFlow;
import com.ailk.bi.metamanage.service.IImpactAnalyseService;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ImpactAnalyseServiceImpl implements IImpactAnalyseService {
	private HashMap map = null; // 重复节点的个数
	private HashMap stepMap = null; // 节点的x、y坐标
	private String step = null; // 所有节点的字符串
	private HashMap lvlMap = null; // 各节点对应的下级子节点
	private int count = 0; // 节点层级数
	private int sn = 0;
	private Object[] obj = new Object[10];
	private HashMap colorMap = null; // 数据分层对应的颜色
	private IImpactAnalyseDao dao = new ImpactAnalyseDaoImpl();

	// 生成流程图的xml
	public String getImpactFlow(String msu_id) {

		Document document1 = DocumentHelper.createDocument();
		document1.setXMLEncoding("GBK");
		Element webFlow = document1.addElement("WebFlow");
		Element FlowConfig = webFlow.addElement("FlowConfig");
		Element steps = webFlow.addElement("Steps");
		Element actions = webFlow.addElement("Actions");

		// 添加流程图基础数据
		Element BaseProperties = FlowConfig.addElement("BaseProperties");
		BaseProperties.addAttribute("flowId", "jobTable").addAttribute(
				"flowText", "影响分析");
		Element VMLProperties = FlowConfig.addElement("VMLProperties");
		VMLProperties.addAttribute("stepTextColor", "blue")
				.addAttribute("stepStrokeColor", "green")
				.addAttribute("stepShadowColor", "#b3b3b3")
				.addAttribute("stepFocusedStrokeColor", "yellow")
				.addAttribute("isStepShadow", "T")
				.addAttribute("actionStrokeColor", "green")
				.addAttribute("actionTextColor", "")
				.addAttribute("actionFocusedStrokeColor", "yellow")
				.addAttribute("sStepTextColor", "green")
				.addAttribute("sStepStrokeColor", "green")
				.addAttribute("stepColor1", "green")
				.addAttribute("stepColor2", "white")
				.addAttribute("isStep3D", "true")
				.addAttribute("step3DDepth", "20");

		String[][] rs = dao.getMsuTable(msu_id);
		String table_id = "";
		String msu_name = "";
		if (rs != null && rs.length > 0) {
			msu_name = rs[0][0];
			msu_id = rs[0][1];
			for (int j = 0; j < rs.length; j++) {
				table_id += rs[j][2] + ",";
			}
			JobTableFlow tableFlow = getImpactInfo(msu_id, msu_name,
					table_id.substring(0, table_id.length() - 1));
			tableFlow.setIn_table_id(msu_id);

			String[][] color = dao.getLayerColor();
			colorMap = new HashMap();
			colorMap.put(msu_id, "#CCCCFF");
			int x = 0;
			for (int i = 0; color != null && i < color.length; i++) {
				colorMap.put(color[i][0], color[i][2]);
				x += 250;
				Element step = steps.addElement("Step");
				Element BasePropertiess = step.addElement("BaseProperties");
				Element VMLPropertiess = step.addElement("VMLProperties");
				BasePropertiess.addAttribute("stepType", "NormalStep")
						.addAttribute("id", color[i][0])
						.addAttribute("text", color[i][1])
						.addAttribute("title", color[i][1]);

				VMLPropertiess.addAttribute("width", "150")
						.addAttribute("height", "100")
						.addAttribute("x", x + "").addAttribute("y", "4000")
						.addAttribute("textWeight", "")
						.addAttribute("strokeWeight", "")
						.addAttribute("hidden", "") // 层次隐藏 0为显示，1为隐藏
						.addAttribute("zIndex", "");
				VMLPropertiess.addAttribute("stepColor1", color[i][2])
						.addAttribute("stepColor2", "white");
			}

			if (tableFlow != null) {
				// getMutiStep(tableFlow.getIn_table_id());

				Element[] element = new Element[2];
				element[0] = steps;
				element[1] = actions;
				element = getElement(element, tableFlow);
				steps = element[0];
				actions = element[1];
			}
		}
		return document1.asXML();
	}

	private Element[] getElement(Element[] stepAction, JobTableFlow flow) {
		// 添加环节数据
		int c = 0;
		String[] xy = new String[2];
		if (map != null && map.get(flow.getIn_table_id()) != null) {
			c = Integer.parseInt(map.get(flow.getIn_table_id()).toString());
			--c;
			map.put(flow.getIn_table_id(), c + "");
		}
		if (c == 0) {
			String tempId = flow.getIn_table_id().replace(".", "");
			Element step = stepAction[0].addElement("Step");
			Element BasePropertiess = step.addElement("BaseProperties");
			Element VMLPropertiess = step.addElement("VMLProperties");
			BasePropertiess.addAttribute("stepType", "NormalStep")
					.addAttribute("id", tempId)
					.addAttribute("text", flow.getIn_table_name())
					.addAttribute("title", flow.getIn_table_id());

			if (stepMap.get(flow.getIn_table_id()) != null) {
				xy = stepMap.get(flow.getIn_table_id()).toString().split(",");
			}
			VMLPropertiess.addAttribute("width", "300")
					.addAttribute("height", "150").addAttribute("x", xy[0])
					.addAttribute("y", xy[1]).addAttribute("textWeight", "")
					.addAttribute("strokeWeight", "")
					.addAttribute("hidden", "") // 层次隐藏 0为显示，1为隐藏
					.addAttribute("zIndex", "");
			if (colorMap.get(flow.getLayer_id()) != null) {
				VMLPropertiess.addAttribute("stepColor1",
						colorMap.get(flow.getLayer_id()).toString())
						.addAttribute("stepColor2", "white");
			} else {
				VMLPropertiess.addAttribute("stepColor1", "#CCFFFF")
						.addAttribute("stepColor2", "white");
			}
		}
		List list = flow.getChildTable();
		String str = "";
		int i = 0;
		// 添加步骤
		for (i = 0; list != null && list.size() > 0 && i < list.size(); i++) {
			JobTableFlow tableFlow = (JobTableFlow) list.get(i);
			String fromId = flow.getIn_table_id().replace(".", "");
			String toId = tableFlow.getIn_table_id().replace(".", "");

			Element action = stepAction[1].addElement("Action");
			Element actionsBaseProperties = action.addElement("BaseProperties");
			Element actionsVMLProperties = action.addElement("VMLProperties");
			if (map.get(tableFlow.getIn_table_id()) != null) {
				c = Integer.parseInt(map.get(tableFlow.getIn_table_id())
						.toString());
				str = c + "";
			}
			actionsBaseProperties.addAttribute("id", "act_" + toId + str)
					.addAttribute("actionType", "PolyLine")
					.addAttribute("from", fromId).addAttribute("to", toId);

			actionsVMLProperties.addAttribute("startArrow", " ")
					.addAttribute("endArrow", "Classic")
					.addAttribute("strokeWeight", "1")
					.addAttribute("zIndex", "");

		}

		for (i = 0; list != null && list.size() > 0 && i < list.size(); i++) {
			JobTableFlow tableFlow = (JobTableFlow) list.get(i);
			stepAction = getElement(stepAction, tableFlow);
		}
		str = "";
		return stepAction;
	}

	// 封装数据
	private JobTableFlow getImpactInfo(String msu_id, String msu_name,
			String table_id) {

		step = "";
		lvlMap = new HashMap();
		stepMap = new HashMap();
		JobTableFlow flow = null;

		flow = new JobTableFlow();
		flow.setIn_table_id(table_id);
		flow.setIn_table_name(msu_name);
		flow.setLayer_id(msu_id);
		stepMap.put(msu_id, "10,10");
		lvlMap.put(msu_id, table_id);
		getChildInfo(flow);
		getMutiStep();
		getImpactStepMap(msu_id);

		return flow;
	}

	private JobTableFlow getChildInfo(JobTableFlow flow) {
		List list = new ArrayList();
		String str = "";
		String[][] rs = null;
		if (sn == 0) {
			rs = dao.getTableInfo(flow.getIn_table_id());
			++sn;
		} else {
			rs = dao.getImpactInfo(flow.getIn_table_id());
		}

		if (rs != null && rs.length > 0) {
			for (int i = 0; i < rs.length; i++) {
				JobTableFlow jobFlow = new JobTableFlow();

				jobFlow.setIn_table_id(rs[i][0]);
				jobFlow.setIn_table_name(rs[i][1]);
				jobFlow.setLayer_id(rs[i][2]);
				str += rs[i][0] + ",";
				list.add(jobFlow);
			}
			step += str;
			str = str.substring(0, str.length() - 1);

			flow.setOut_table_id(str);
			flow.setChildTable(list);

			if (!flow.getIn_table_id().equals(str)) {
				lvlMap.put(flow.getIn_table_id(), str);
			}
			for (int j = 0; j < list.size(); j++) {
				JobTableFlow jobFlow = (JobTableFlow) list.get(j);
				if (jobFlow.getIn_table_id() != null
						&& !"".equals(jobFlow.getIn_table_id()))
					jobFlow = getChildInfo(jobFlow);
			}
		}

		return flow;
	}

	// 取得重复的节点个数放入map
	public void getMutiStep() {
		map = new HashMap();
		if (!"".equals(step)) {
			String[] arr = step.split(",");
			int c = 0;
			String temp = "";
			for (int i = 0; i < arr.length; i++) {
				int m = 0;
				if (i == 0)
					temp = arr[i];
				else
					temp = "," + arr[i] + ",";
				m = step.indexOf(arr[i]);
				if (m >= 0) {
					c = 0;
					while (m >= 0) {
						m = step.indexOf(temp, m + 1);
						c++;
					}
					step = step.replace(temp, "," + temp + ",");
					step = step.replace(temp, "");
					if (c > 1)
						map.put(arr[i], c + "");
				}
			}
		}
	}

	// 取得所有节点的层级关系，定义x、y轴坐标
	private void getImpactStepMap(String table_id) {
		List list = new ArrayList();

		int i = 0;
		int x = 0;
		if (lvlMap.get(table_id) != null) {
			String[] child = lvlMap.get(table_id).toString().split(",");
			for (i = 0; i < child.length; i++) {
				list.add(child[i]);
			}
		}
		obj[count] = list;
		++count;
		getChildLvl(list);
		for (i = 0; i < count + 1; i++) {
			List temp = (List) obj[i];
			x = 400 * (i + 1);
			int y = 10;
			if (temp != null && temp.size() > 0) {
				for (int j = 0; j < temp.size(); j++) {

					if (temp.get(j) != null) {
						String str = (String) temp.get(j);
						stepMap.put(str, x + "," + y);
					}
					y += 250;
				}
			}
		}
	}

	private void getChildLvl(List parent) {
		if (parent != null && parent.size() > 0) {
			List ls = new ArrayList();
			for (int i = 0; i < parent.size(); i++) {
				if (parent.get(i) != null) {
					String temp = (String) parent.get(i);
					if (lvlMap.get(temp) != null) {
						String[] child = lvlMap.get(temp).toString().split(",");
						for (int j = 0; j < child.length; j++) {
							ls.add(child[j]);
						}
					} else {
						ls.add(null);
					}
				}
			}
			obj[count] = ls;
			++count;
			getChildLvl(ls);
		}
	}

}
