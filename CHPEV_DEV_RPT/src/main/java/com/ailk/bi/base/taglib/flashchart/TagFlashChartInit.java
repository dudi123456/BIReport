package com.ailk.bi.base.taglib.flashchart;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import java.io.IOException;

@SuppressWarnings({ "unused" })
public class TagFlashChartInit extends TagSupport {

	private static final long serialVersionUID = 1L;

	private String caption; // 图形标题
	private String subcaption; // 图形子标题
	private String[] categories; // 图形X轴数据
	private String[] seriesname; // 图例
	private String[][] dataset; // 数据
	private String width; // 输出宽度
	private String height; // 高度
	private String configId; // 后台配置信息ID
	private String jsfunc_name; // 生成Javascript函数名称
	private String visible; // 是否默认显示图形;true显示 false不显示,需要其他js调用jsfunc_name
	private String render; // 图形显示的div层id
	private String chartType; // 图形类型
	private String path; // 系统路径

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRender() {
		return render;
	}

	public void setRender(String render) {
		this.render = render;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getSubcaption() {
		return subcaption;
	}

	public void setSubcaption(String subcaption) {
		this.subcaption = subcaption;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}

	public String[] getSeriesname() {
		return seriesname;
	}

	public void setSeriesname(String[] seriesname) {
		this.seriesname = seriesname;
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

	public int doEndTag() throws JspException {
		TagFlashChartUtil chartutil = new TagFlashChartUtil();

		StringBuffer chart = new StringBuffer();
		chart.append("<script defer=\"true\" type=\"text/javascript\">\n");
		if (visible.equals("true")) {
			chart.append(jsfunc_name + "();\n");
		}
		chart.append("function " + jsfunc_name + "(){\n");
		String chartXmlString = "";
		try {
			chartXmlString = TagFlashChartUtil.buildChart(chartType, caption,
					subcaption, categories, seriesname, dataset, configId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		chart.append("var chartDataXml =  '")
				.append(chartXmlString.replaceAll("\n", "")).append(" ';\n")
				.append("var chart = new FusionCharts(\"").append(path)
				.append("/swf/").append(chartType)
				.append(".swf\", \"ChartId\", \"").append(width)
				.append("\", \"").append(height).append("\");\n")
				.append("chart.setDataXML(chartDataXml);\n")
				.append("chart.setTransparent (true);\n")
				.append("chart.render(\"").append(render).append("\");\n")
				.append("}\n").append("</script>\n");
		try {
			pageContext.getOut().println(chart.toString());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return super.doEndTag();
	}
}
