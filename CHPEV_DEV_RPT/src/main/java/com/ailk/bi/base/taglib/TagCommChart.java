package com.ailk.bi.base.taglib;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.ailk.bi.base.table.PubInfoChartDefTable;
import com.ailk.bi.base.taglib.flashchart.ChartConsts;
import com.ailk.bi.base.taglib.flashchart.TagFlashChartUtil;
import com.ailk.bi.base.util.CommChartUtil;
import com.ailk.bi.common.app.Arith;
import com.ailk.bi.common.app.SortUtils;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.chart.WebChart;

/**
 * BI基础通用图形标签
 * 
 * @author huir01
 * 
 */
@SuppressWarnings({ "unused" })
public class TagCommChart extends BodyTagSupport {
	/**
	 *
	 */
	private static final long serialVersionUID = 4045019501137210203L;

	private String chartId = null;// 图形对象标识

	private String chartName = null;// 图形标题名称

	private String chartSubName = null;// 图形子标题名称

	private String chartType = null;// 图形类型

	private String categoryIndex = null;// 图形分组索引

	private String categoryDesc = null;// 图形分组描述

	private String valueIndex = null;// 图形值所在索引

	private String selfSQL = null;// 图形传入SQL

	private String whereSQL = null;// 图形传入条件

	private Object qryStruct = null;// 查询结构对象

	private String[][] dataset; // 传入数据

	private String width; // 输出宽度

	private String height; // 高度

	private String configId; // 后台配置信息ID

	private String jsfunc_name; // 生成Javascript函数名称

	private String visible = "true"; // 是否默认显示图形;true显示 false不显示

	private String render; // 图形显示的层div id

	private String path; // 系统路径

	private String replace; // 替换标志

	public String getChartId() {
		return chartId;
	}

	public void setChartId(String chartId) {
		this.chartId = chartId;
	}

	public String getChartName() {
		return chartName;
	}

	public void setChartName(String chartName) {
		this.chartName = chartName;
	}

	public String getChartSubName() {
		return chartSubName;
	}

	public void setChartSubName(String chartSubName) {
		this.chartSubName = chartSubName;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String getCategoryIndex() {
		return categoryIndex;
	}

	public void setCategoryIndex(String categoryIndex) {
		this.categoryIndex = categoryIndex;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public String getValueIndex() {
		return valueIndex;
	}

	public void setValueIndex(String valueIndex) {
		this.valueIndex = valueIndex;
	}

	public String getSelfSQL() {
		return selfSQL;
	}

	public void setSelfSQL(String selfSQL) {
		this.selfSQL = selfSQL;
	}

	public String getWhereSQL() {
		return whereSQL;
	}

	public void setWhereSQL(String whereSQL) {
		this.whereSQL = whereSQL;
	}

	public Object getQryStruct() {
		return qryStruct;
	}

	public void setQryStruct(Object qry_struct) {
		this.qryStruct = qry_struct;
	}

	public String[][] getDataset() {
		return dataset;
	}

	public void setDataset(String[][] dataset) {
		this.dataset = dataset;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getJsfunc_name() {
		return jsfunc_name;
	}

	public void setJsfunc_name(String jsfunc_name) {
		this.jsfunc_name = jsfunc_name;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getRender() {
		return render;
	}

	public void setRender(String render) {
		this.render = render;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getReplace() {
		return replace;
	}

	public void setReplace(String replace) {
		this.replace = replace;
	}

	// 界面传递参数
	// protected String category_index = null; // 图形分组名称索引

	// protected String category_desc = null; // 图形分组名称

	// protected String value_index = null; // 图形分组名称索引

	public int doStartTag() throws JspException {
		// 提取界面传递参数
		try {
			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
		} catch (Exception e) {
			System.out.println("初始化图形标签出现错误，有可能参数传递失败！");
		}
		return (SKIP_BODY);
	}

	public int doEndTag() throws JspException {
		String outStr = "";

		try {
			HttpServletResponse response = (HttpServletResponse) pageContext
					.getResponse();
			response.setContentType("charset=utf-8");
			JspWriter out = pageContext.getOut();
			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			HttpSession session = pageContext.getSession();
			if (session != null) {
				// 获取图形定义
				PubInfoChartDefTable chartDef = CommChartUtil
						.getChartDef(chartId);
				if (chartDef == null) {
					outStr = "<font color=red>未定义</font>";
				} else {
					// 替换SQL
					if (selfSQL != null && !"".equals(selfSQL)) {
						chartDef.sql_main = selfSQL;
						chartDef.sql_where = "";
						chartDef.sql_order = "";
					}
					// 附加的数据条件
					if (whereSQL != null && !"".equals(whereSQL)) {
						chartDef.sql_where += " " + whereSQL;
					}
					// 特殊替换处理
					if (replace != null && !"".equals(replace)) {
						chartDef.sql_main = StringB.replace(chartDef.sql_main,
								"#", replace);
						chartDef.sql_order = StringB.replace(
								chartDef.sql_order, "#", replace);
					}
					// 生成图形数据
					String[][] arrObj = null;
					if (dataset != null) {
						arrObj = dataset;
					} else {
						arrObj = CommChartUtil.genSubjectChartData(chartDef,
								request, qryStruct, session);
					}
					// TOP N排名
					int isorNum = StringB.toInt(chartDef.chart_index, 0);
					if (isorNum > 0) {
						int iValueIndex = StringB
								.toInt(chartDef.value_index, 0);
						arrObj = SortUtils.sortValuesStrNum(arrObj,
								iValueIndex, false, isorNum);
					}

					// 设置图形类型
					if (chartType != null && !"".equals(chartType)) {
						chartDef.chart_type = chartType;
					}
					// 重置图形分组名称索引
					if (categoryIndex != null && !"".equals(categoryIndex)) {
						chartDef.category_desc_index = categoryIndex;
					}
					// 重置图形分组名称
					if (categoryDesc != null && !"".equals(categoryDesc)) {
						chartDef.category_desc = categoryDesc;
					}
					// 重置图形值所在索引
					if (valueIndex != null && !"".equals(valueIndex)) {
						chartDef.value_index = valueIndex;
					}

					if (ChartConsts.CHART_TYPE_JFREECHART
							.equals(chartDef.chart_distype)) {
						// 生成图形对象
						WebChart chart = CommChartUtil.genChartObj(chartDef,
								arrObj);
						// 设置图形属性
						CommChartUtil.setChartAttribute(chart,
								chartDef.chart_attribute);
						// 设置图形标题
						if (chartName == null || "".equals(chartName)) {
							chartName = "";
						}
						// 设置图形子标题
						if (chartSubName == null || "".equals(chartSubName)) {
							chartSubName = "";
						} else {
							chartName = chartName + " " + chartSubName;
						}
						chart.setTitle(chartName + chart.getTitle());
						// 设置图形宽度
						if (width != null && !"".equals(width.trim())) {
							int iWidth = StringB.toInt(width, 500);
							chart.setWidth(iWidth);
						}
						// 设置图形高度
						if (height != null && !"".equals(height.trim())) {
							int iHeight = StringB.toInt(height, 260);
							chart.setHeight(iHeight);
						}
						// 生成图形代码
						outStr = CommChartUtil.genChartHTML(chartDef, chart,
								request, response.getWriter());
						// 打印出图形
						// out.print(outStr);
					} else if (ChartConsts.CHART_TYPE_FUSION_MULTI
							.equals(chartDef.chart_distype)
							|| ChartConsts.CHART_TYPE_FUSION_SINGLE
									.equals(chartDef.chart_distype)
							|| ChartConsts.CHART_TYPE_FUSION_SCATTER
									.equals(chartDef.chart_distype)
							|| ChartConsts.CHART_TYPE_FUSION_BUBBLE
									.equals(chartDef.chart_distype)) {
						// 生成图形对象，统一属性设置
						String[] tmpC = null;
						String[] tmpS = null;
						double[][] tmpD = null;
						WebChart chartObj = new WebChart(tmpC, tmpS, tmpD);
						// 设置图形属性
						CommChartUtil.setChartAttribute(chartObj,
								chartDef.chart_attribute);
						// 设置图形标题
						if (chartName == null || "".equals(chartName)) {
							chartName = "";
						}
						chartObj.setTitle(chartName + " " + chartObj.getTitle());
						// 设置图形子标题
						if (chartSubName == null || "".equals(chartSubName)) {
							chartSubName = "";
						}
						chartObj.setSubtitle(chartSubName);
						// 设置图形宽度
						if (width != null && !"".equals(width.trim())) {
							if (width.indexOf(".") > 0) {
								width = Arith.round(width, 0);
							}
							int iWidth = StringB.toInt(width, 500);
							chartObj.setWidth(iWidth);
						}
						// 设置图形高度
						if (height != null && !"".equals(height.trim())) {
							int iHeight = StringB.toInt(height, 260);
							chartObj.setHeight(iHeight);
						}
						// 图形样式配置信息ID
						if (configId != null && !"".equals(configId.trim())) {
							chartObj.setConfigId(configId);
						}
						if (chartObj.getConfigId() == null) {
							chartObj.setConfigId("");
						}

						// 分组描述数组
						String[] arrCategory = null;
						// X轴数组
						String[] arrSeries = null;
						// 数值
						String[][] arrValue = null;

						if (ChartConsts.CHART_TYPE_FUSION_MULTI
								.equals(chartDef.chart_distype)
								|| ChartConsts.CHART_TYPE_FUSION_SINGLE
										.equals(chartDef.chart_distype)) {
							arrCategory = CommChartUtil.genFlashChartObj(
									chartDef, arrObj, "category");
							arrSeries = CommChartUtil.genFlashChartObj(
									chartDef, arrObj, "series");
							arrValue = CommChartUtil.genFlashChartObjValue(
									chartDef, arrObj);
						} else if (ChartConsts.CHART_TYPE_FUSION_SCATTER
								.equals(chartDef.chart_distype)
								|| ChartConsts.CHART_TYPE_FUSION_BUBBLE
										.equals(chartDef.chart_distype)) {
							arrCategory = null;
							arrSeries = null;
							arrValue = arrObj;
						}

						// 获取funsionchart的图形对应类型
						PubInfoChartDefTable funsionchartDef = CommChartUtil
								.getFusionChartDef(chartId,
										chartDef.chart_distype);
						if (chartType != null && !"".equals(chartType)) {
							chartDef.chart_type = chartType;
						} else {
							chartDef.chart_type = funsionchartDef.chart_code;
						}
						if (chartDef.chart_type == null
								|| "".equals(chartDef.chart_type)) {
							chartDef.chart_type = "MSLine";// 线
						}

						// 图形显示div名称
						if (render == null || "".equals(render.trim())) {
							render = "chartdiv" + chartDef.chart_id;
						}
						// 图形js名称
						if (jsfunc_name == null
								|| "".equals(jsfunc_name.trim())) {
							jsfunc_name = "showChart" + chartDef.chart_id;
						}
						// 系统路径
						if (path == null || "".equals(path.trim())) {
							path = request.getContextPath();
						}
						StringBuffer chart = new StringBuffer();
						chart.append("<div id=\"" + render
								+ "\" style=\"padding-top:0px;\"></div>");
						chart.append("<script type=\"text/javascript\">\n");
						if (visible.equals("true")) {
							chart.append(jsfunc_name + "();\n");
						}
						chart.append("function " + jsfunc_name + "(){\n");
						String chartXmlString = "";
						try {
							chartXmlString = TagFlashChartUtil.buildChartByObj(
									chartDef, chartObj, arrSeries, arrCategory,
									arrValue);
						} catch (Exception ex) {
							System.out.println("构造图形出错" + ex);
						}
						chart.append("var chartDataXml =  '"
								+ chartXmlString.replaceAll("\n", "") + " ';\n");
						// chart.append("alert(chartDataXml)\n");
						chart.append("var chart = new FusionCharts(\"" + path
								+ "/swf/" + chartDef.chart_type
								+ ".swf\", \"ChartId\", \""
								+ chartObj.getWidth() + "\", \""
								+ chartObj.getHeight() + "\");\n");
						chart.append("chart.setDataXML(chartDataXml);\n");
						chart.append("chart.render(\"" + render + "\");\n");
						chart.append("}\n");
						chart.append("</script>\n");

						outStr = chart.toString();
					}

					// 打印出图形
					out.println(outStr);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (SKIP_BODY);
	}

}
