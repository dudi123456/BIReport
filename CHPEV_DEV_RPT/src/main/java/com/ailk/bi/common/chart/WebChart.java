package com.ailk.bi.common.chart;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;

//基本
import org.jfree.ui.*;
import org.jfree.chart.*;
import org.jfree.chart.title.*;
import org.jfree.chart.labels.*;
import org.jfree.util.Rotation;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.text.TextBlockAnchor;

//数据集
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

//自定义数据集
import org.jfree.data.xy.ChartXYZDataset;
import org.jfree.data.xy.XYZserie;

//图集
import org.jfree.chart.plot.*;

//表现形式集
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.StackedAreaRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.LineRenderer3D;
import org.jfree.chart.renderer.xy.XYItemRenderer;

//标签
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;

//刻度
import org.jfree.chart.axis.*;

//通用
//import org.jfree.data.general.DatasetUtilities;

//常量
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.util.TableOrder;

/**
 * <p>
 * Title: BI System
 * </p>
 * <p>
 * Description: 生成图形接口 jfreechart-1.0.0
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author renhui
 * @version 1.0
 */

public class WebChart {
	/* 定义数组 */
	private String[] category;

	private String[] series;

	private double[][] value;

	private double[][] xValue;

	private double[][] yValue;

	private double[][] zValue;

	/* 定义图形 */
	private JFreeChart jfreechart = null;

	private JFreeChart jfreechartExtend = null;

	/* 其他变量 */
	private double valuepositions = 0;// 值显示的角度

	private ItemLabelPosition pos_o_b_c = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
			TextAnchor.BASELINE_CENTER, TextAnchor.CENTER, this.valuepositions);

	private ItemLabelPosition pos_o_b_l = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
			TextAnchor.BOTTOM_LEFT, TextAnchor.CENTER, this.valuepositions);

	private ItemLabelPosition pos_o_t_r = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
			TextAnchor.TOP_RIGHT, TextAnchor.CENTER, this.valuepositions);

	private ItemLabelPosition pos_i_b_c = new ItemLabelPosition(ItemLabelAnchor.INSIDE12,
			TextAnchor.BASELINE_CENTER, TextAnchor.CENTER, this.valuepositions);

	private ItemLabelPosition pos_i_c_c = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE11,
			TextAnchor.CENTER_RIGHT, TextAnchor.CENTER_LEFT, this.valuepositions);

	public void setValuePositions(double p0) {
		this.valuepositions = p0;
		pos_o_b_c = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER,
				TextAnchor.CENTER, this.valuepositions);

		pos_o_b_l = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_LEFT,
				TextAnchor.CENTER, this.valuepositions);

		pos_o_t_r = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.TOP_RIGHT,
				TextAnchor.CENTER, this.valuepositions);

		pos_i_b_c = new ItemLabelPosition(ItemLabelAnchor.INSIDE12, TextAnchor.BASELINE_CENTER,
				TextAnchor.CENTER, this.valuepositions);
	}

	private ItemLabelPosition pos_o_pos = pos_o_b_c;

	public void setValueLabelPos(int p0) {
		if (p0 == 1) {
			pos_o_pos = pos_o_b_c;
		} else if (p0 == 2) {
			pos_o_pos = pos_o_b_l;
		} else if (p0 == 3) {
			pos_o_pos = pos_o_t_r;
		} else if (p0 == 4) {
			pos_o_pos = pos_i_b_c;
		} else if (p0 == 5) {
			pos_o_pos = pos_i_c_c;
		}
	}

	/* 定义属性 */
	/**
	 * 图形标题
	 */
	private String title = "";

	/**
	 * 图形子标题
	 */
	private String subtitle = "";

	/**
	 * 图形标题字体
	 */
	private Font titlefont = new Font("宋体", 1, 20);

	/**
	 * 没有数据时候的提示
	 */
	private String nodatamessage = "没有数据";

	/**
	 * 图形宽度
	 */
	private int width = 350;

	/**
	 * 图形高度
	 */
	private int height = 200;

	/**
	 * 图形背景色
	 */
	private Paint backgroundcolor = Color.white;

	/**
	 * 图形边框是否可见
	 */
	private boolean bordervisible = false;

	/**
	 * 图形边框颜色
	 */
	private Paint bordercolor = Color.lightGray;

	/**
	 * 图形是否显示图例
	 */
	private boolean legend = true;

	/**
	 * 图形图例字体
	 */
	private Font legendfont = new Font("宋体", 0, 12);

	/**
	 * 图形图例边框宽度
	 */
	private double legendborder = 0;

	/**
	 * 图形图例的位置，设置的值为top,bottom,left,right
	 */
	private RectangleEdge legendpos = RectangleEdge.BOTTOM;

	/**
	 * 图表区域背景色
	 */
	private Paint backgroundcolor_plot = Color.white;

	/**
	 * 图表区域透明度
	 */
	private float alpha = 0.9F;

	/**
	 * 图表区域水平或垂直显示模式，0水平显示，1垂直显示
	 */
	private PlotOrientation plotorientation = PlotOrientation.VERTICAL;

	/**
	 * 图表区域提示数值字体
	 */
	private Font labelfont = new Font("宋体", 0, 12);

	/**
	 * 图表区域X轴刻度值显示角度
	 */
	private double categorylabelpositions = 0;

	/**
	 * 图表区域X轴刻度值字体
	 */
	private Font categoryaxisfont = new Font("宋体", 0, 12);

	/**
	 * 图表区域Y轴刻度值字体
	 */
	private Font numberaxisfont = new Font("SansSerif", 0, 12);

	/**
	 * 图表区域刻度线颜色
	 */
	private Paint gridlinecolor = Color.lightGray;

	/**
	 * 横坐标刻度线是否显示
	 */
	private boolean gridlinesdomain = false;

	/**
	 * 纵坐标刻度线是否显示
	 */
	private boolean gridlinesrange = true;

	/**
	 * 图表区域X轴描述
	 */
	private String xAxisInfo = null;

	/**
	 * 图表区域Y轴描述
	 */
	private String yAxisInfo = null;

	/**
	 * 图表区域X轴描述
	 */
	private String xAxisName = null;

	/**
	 * 图表区域Y轴描述
	 */
	private String yAxisName = null;

	public String getYAxisName() {
		return yAxisName;
	}

	public void setYAxisName(String axisName) {
		yAxisName = axisName;
	}

	/**
	 * 图表区域X轴描述
	 */
	private String xAxis = null;

	/**
	 * 图表区域Y轴描述
	 */
	private String yAxis = null;

	/**
	 * 图表区域第二Y轴描述
	 */
	private String yAxisInfo_extend = null;

	/**
	 * 饼状图形边框颜色
	 */
	private Paint piebordercolor = Color.lightGray;

	/**
	 * 饼状图形是否为圆形
	 */
	private boolean piecircular = false;

	/**
	 * 饼状图形图例显示，{0}显示名称，{1}显示数值，{2}显示百分比
	 */
	private String pielegende = "{0}";

	/**
	 * 是否显示饼状图形提示连线
	 */
	private boolean pielinks = true;

	/**
	 * 饼状图形提示连线颜色
	 */
	private Paint pielinkscolor = Color.black;

	/**
	 * 饼状图形提示连线数值显示，{0}显示名称，{1}显示数值，{2}显示百分比
	 */
	private String pielinkslegend = "{1}";

	/**
	 * 柱状图形数值提示显示{0}显示名称，{2}显示数值，{3}显示百分比
	 */
	private String barlegend = "";

	/**
	 * 柱状图柱子间的间距
	 */
	private double baritemmargin = 0.1D;

	/**
	 * 柱状图柱子的最大宽度
	 */
	private double barmaxwidth = 0.075D;

	/**
	 * 柱状图是否显示边框
	 */
	private boolean baroutline = false;

	/**
	 * 柱状图形渐变颜色方向，1垂直，2水平，3垂直居中，4水平居中
	 */
	private GradientPaintTransformType bartranstype = GradientPaintTransformType.VERTICAL;

	/**
	 * 线性图是否用虚线显示
	 */
	private boolean linestroke0 = false;

	private boolean linestroke1 = false;

	private boolean linestroke2 = false;

	private boolean linestroke3 = false;

	private boolean linestroke4 = false;

	private boolean linestroke5 = false;

	private boolean linestroke6 = false;

	private boolean linestroke7 = false;

	private boolean linestroke8 = false;

	private boolean linestroke9 = false;

	/**
	 * 线性图是否显示0值点
	 */
	private boolean includeszero = true;

	/**
	 * 双轴图是否同步Y轴最大值高度
	 */
	private boolean synchroaxis = false;

	/**
	 * 对比图上图形占比
	 */
	private int upwight = 1;

	/**
	 * 对比图下图形占比
	 */
	private int downwight = 1;

	/**
	 * 特殊设定第一座标起始刻度值
	 */
	private double rangeStart = 0;

	/**
	 * 特殊设定第一座标终止刻度值
	 */
	private double rangeEnd = 0;

	/**
	 * 特殊设定第二座标起始刻度值
	 */
	private double rangeStart_extend = 0;

	/**
	 * 特殊设定第二座标终止刻度值
	 */
	private double rangeEnd_extend = 0;

	/**
	 * 特殊设定目标名称
	 */
	private String rangename = "";

	/**
	 * 特殊设定目标上限值
	 */
	private double toprange = 0;

	/**
	 * 特殊设定目标下限值
	 */
	private double bottomrange = 0;

	/**
	 * 是否用渐变色
	 */
	private boolean usetranscolor = true;

	// 第一序列色彩
	private Paint color0 = new Color(255, 64, 87);

	private Paint transcolor0 = new Color(255, 100, 100);

	private GradientPaint gradientpaint0 = new GradientPaint(0.0F, 0.0F, (Color) transcolor0, 0.0F,
			0.0F, (Color) color0);

	// 第二序列色彩
	private Paint color1 = new Color(30, 169, 254);

	private Paint transcolor1 = new Color(90, 192, 254);

	private GradientPaint gradientpaint1 = new GradientPaint(0.0F, 0.0F, (Color) transcolor1, 0.0F,
			0.0F, (Color) color1);

	// 第三序列色彩
	private Paint color2 = new Color(50, 214, 29);

	private Paint transcolor2 = new Color(25, 255, 25);

	private GradientPaint gradientpaint2 = new GradientPaint(0.0F, 0.0F, (Color) transcolor2, 0.0F,
			0.0F, (Color) color2);

	// 第四序列色彩
	private Paint color3 = new Color(255, 102, 0);

	private Paint transcolor3 = new Color(255, 120, 30);

	private GradientPaint gradientpaint3 = new GradientPaint(0.0F, 0.0F, (Color) transcolor3, 0.0F,
			0.0F, (Color) color3);

	// 第五序列色彩
	private Paint color4 = new Color(202, 105, 174);

	private Paint transcolor4 = new Color(218, 150, 198);

	private GradientPaint gradientpaint4 = new GradientPaint(0.0F, 0.0F, (Color) transcolor4, 0.0F,
			0.0F, (Color) color4);

	// 第六序列色彩
	private Paint color5 = new Color(250, 165, 36);

	private Paint transcolor5 = new Color(252, 198, 116);

	private GradientPaint gradientpaint5 = new GradientPaint(0.0F, 0.0F, (Color) transcolor5, 0.0F,
			0.0F, (Color) color5);

	// 第七序列色彩
	private Paint color6 = new Color(25, 25, 255);

	private Paint transcolor6 = new Color(130, 130, 255);

	private GradientPaint gradientpaint6 = new GradientPaint(0.0F, 0.0F, (Color) transcolor6, 0.0F,
			0.0F, (Color) color6);

	// 第八序列色彩
	private Paint color7 = new Color(255, 25, 255);

	private Paint transcolor7 = new Color(255, 100, 255);

	private GradientPaint gradientpaint7 = new GradientPaint(0.0F, 0.0F, (Color) transcolor7, 0.0F,
			0.0F, (Color) color7);

	// 第九序列色彩
	private Paint color8 = new Color(25, 255, 25);

	private Paint transcolor8 = new Color(100, 255, 100);

	private GradientPaint gradientpaint8 = new GradientPaint(0.0F, 0.0F, (Color) transcolor8, 0.0F,
			0.0F, (Color) color8);

	// 第十序列色彩
	private Paint color9 = new Color(255, 140, 25);

	private Paint transcolor9 = new Color(255, 157, 60);

	private GradientPaint gradientpaint9 = new GradientPaint(0.0F, 0.0F, (Color) transcolor9, 0.0F,
			0.0F, (Color) color9);

	// fusionchart 模板ID
	private String configId = "";

	/**
	 * 构造函数，初始化数据
	 *
	 * @param category
	 * @param series
	 * @param value
	 */
	public WebChart(String[] category, String[] series, double[][] value) {
		this.category = category;
		if (this.category == null) {
			this.category = new String[1];
			this.category[0] = "";
		}
		this.series = series;
		this.value = value;
	}

	/**
	 * 构造极面图数据
	 *
	 * @param category
	 * @param xValue
	 * @param yValue
	 */
	public WebChart(String[] category, double[][] xValue, double[][] yValue) {
		this.category = category;
		this.xValue = xValue;
		this.yValue = yValue;
	}

	/**
	 * 构造函数，初始化气泡数据
	 *
	 * @param series
	 * @param xValue
	 * @param yValue
	 * @param zValue
	 */
	public WebChart(String[] series, double[][] xValue, double[][] yValue, double[][] zValue) {
		this.series = series;
		this.xValue = xValue;
		this.yValue = yValue;
		this.zValue = zValue;
	}

	/**
	 * 获取图形的标题
	 *
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * 设置图形的标题
	 *
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取图形的子标题
	 *
	 */
	public String getSubtitle() {
		return subtitle;
	}

	/**
	 * 设置图形的子标题
	 *
	 * @param title
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	/**
	 * 设置图形的标题字体
	 *
	 * @param font
	 */
	public void setTitleFont(Font font) {
		this.titlefont = font;
	}

	/**
	 * 设置图形没有数据的提示信息
	 *
	 * @param message
	 */
	public void setNoDataMessage(String message) {
		this.nodatamessage = message;
	}

	/**
	 * 获取图形的宽度
	 *
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * 设置图形的宽度
	 *
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * 获取图形的高度
	 *
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * 设置图形的高度
	 *
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * 获取图形背景色
	 *
	 * @return
	 */
	public Paint getBackgroundColor() {
		return this.backgroundcolor;
	}

	/**
	 * 设置图形背景色
	 *
	 * @param color
	 */
	public void setBackgroundColor(Paint color) {
		this.backgroundcolor = color;
	}

	/**
	 * 设置图形是否显示边框
	 *
	 * @param isLegend
	 */
	public void setBorderVisible(boolean p0) {
		this.bordervisible = p0;
	}

	/**
	 * 获取图形外框颜色
	 *
	 * @return
	 */
	public Paint getBorderColor() {
		return this.bordercolor;
	}

	/**
	 * 设置图形外框颜色
	 *
	 * @param color
	 */
	public void setBorderColor(Paint color) {
		this.bordercolor = color;
	}

	/**
	 * 设置图形是否显示图例
	 *
	 * @param isLegend
	 */
	public void setLegendVisible(boolean isLegend) {
		this.legend = isLegend;
	}

	/**
	 * 设置图形图例字体
	 *
	 * @param font
	 */
	public void setLegendFont(Font font) {
		this.legendfont = font;
	}

	/**
	 * 设置图形图例边框宽度
	 *
	 * @param p0
	 */
	public void setLegendBorder(double p0) {
		this.legendborder = p0;
	}

	/**
	 * 设置图形图例的位置
	 *
	 * @param p0
	 *            设置的值为top,bottom,left,right
	 */
	public void setLegendPos(String p0) {
		if ("top".equals(p0.toLowerCase()))
			this.legendpos = RectangleEdge.TOP;
		else if ("bottom".equals(p0.toLowerCase()))
			this.legendpos = RectangleEdge.BOTTOM;
		else if ("left".equals(p0.toLowerCase()))
			this.legendpos = RectangleEdge.LEFT;
		else if ("right".equals(p0.toLowerCase()))
			this.legendpos = RectangleEdge.RIGHT;
	}

	/**
	 * 获取图表区域背景色
	 *
	 * @return
	 */
	public Paint getBackgroundColor_plot() {
		return this.backgroundcolor_plot;
	}

	/**
	 * 设置图表区域背景色
	 *
	 * @param color
	 */
	public void setBackgroundColor_plot(Paint color) {
		this.backgroundcolor_plot = color;
	}

	/**
	 * 设置图例的透明度
	 *
	 * @param p0
	 */
	public void setAlpha(float p0) {
		this.alpha = p0;
	}

	/**
	 * 设置图表区域水平或垂直显示模式
	 *
	 * @param iPlot
	 *            0水平显示，1垂直显示
	 */
	public void setChartPlotOrientation(int iPlot) {
		if (iPlot == 1)
			this.plotorientation = PlotOrientation.HORIZONTAL;
		else
			this.plotorientation = PlotOrientation.VERTICAL;
	}

	/**
	 * 设置图表区域提示数值字体
	 *
	 * @param p0pielinksLabelFont
	 */
	public void setLableFont(Font font) {
		this.labelfont = font;
	}

	/**
	 * 设置图表区域X轴刻度值显示角度
	 *
	 * @param p0
	 */
	public void setCateGoryLabelPositions(double p0) {
		this.categorylabelpositions = p0;
	}

	/**
	 * 设置图表区域X轴刻度值字体
	 *
	 * @param font
	 */
	public void setCategoryAxisFont(Font font) {
		this.categoryaxisfont = font;
	}

	/**
	 * 设置图表区域Y轴刻度值字体
	 *
	 * @param font
	 */
	public void setNumberAxisFont(Font font) {
		this.numberaxisfont = font;
	}

	/**
	 * 获取图表区域刻度线颜色
	 *
	 * @return
	 */
	public Paint getGridlineColor() {
		return this.gridlinecolor;
	}

	/**
	 * 设置图表区域刻度线颜色
	 *
	 * @param color
	 */
	public void setGridlineColor(Paint color) {
		this.gridlinecolor = color;
	}

	/**
	 * 设置是否显示横坐标刻度线
	 *
	 * @param p0
	 */
	public void setGridlinesDomainVisible(boolean p0) {
		this.gridlinesdomain = p0;
	}

	/**
	 * 设置是否显示纵坐标刻度线
	 *
	 * @param p0
	 */
	public void setGridlinesRangeVisible(boolean p0) {
		this.gridlinesrange = p0;
	}

	/**
	 * 设置图表区域X轴描述
	 *
	 * @param info
	 */
	public void setChartXInfo(String info) {
		this.xAxisInfo = info;
	}

	/**
	 * 设置图表区域Y轴描述
	 *
	 * @param info
	 */
	public void setChartYInfo(String info) {
		this.yAxisInfo = info;
	}

	/**
	 * 设置图表区域扩展Y轴描述
	 *
	 * @param info
	 */
	public void setChartYInfoExtend(String info) {
		this.yAxisInfo_extend = info;
	}

	/**
	 * 获取饼状图形外框颜色
	 *
	 * @return
	 */
	public Paint getPieBorderColor() {
		return this.piebordercolor;
	}

	/**
	 * 设置饼状图形外框颜色
	 *
	 * @param color
	 */
	public void setPieBorderColor(Paint color) {
		this.piebordercolor = color;
	}

	/**
	 * 设置饼状图形是否为圆形
	 *
	 * @param isCircular
	 */
	public void setPieCircular(boolean isCircular) {
		this.piecircular = isCircular;
	}

	/**
	 * 设置饼状图形的图例显示
	 *
	 * @param pieLegend
	 *            {0}显示名称，{1}显示数值，{2}显示百分比
	 */
	public void setPieLegend(String pieLegend) {
		this.pielegende = pieLegend;
	}

	/**
	 * 设置是否显示饼状图形提示连线
	 *
	 * @param p0
	 */
	public void setPieLinksVisible(boolean p0) {
		this.pielinks = p0;
	}

	/**
	 * 获取饼状图形提示连线颜色
	 *
	 * @return
	 */
	public Paint getPieLinksColor() {
		return this.pielinkscolor;
	}

	/**
	 * 设置饼状图形提示连线颜色
	 *
	 * @param color
	 */
	public void setPieLinksColor(Paint color) {
		this.pielinkscolor = color;
	}

	/**
	 * 设置饼状图形提示连线数值显示
	 *
	 * @param pieLegend
	 *            {0}显示名称，{1}显示数值，{2}显示百分比
	 */
	public void setPieLinksLegend(String pieLegend) {
		this.pielinkslegend = pieLegend;
	}

	/**
	 * 设置柱图的图例显示
	 *
	 * @param barLegend
	 *            {0}显示名称，{2}显示数值，{3}显示百分比
	 */
	public void setBarLegend(String barLegend) {
		this.barlegend = barLegend;
	}

	/**
	 * 设置柱状图柱子间的间距
	 *
	 * @param p0
	 */
	public void setBarItemMargin(double itemmargin) {
		this.baritemmargin = itemmargin;
	}

	/**
	 * 设置柱状图柱子的最大宽度
	 *
	 * @param p0
	 */
	public void setBarMaxWidth(double maxwidth) {
		this.barmaxwidth = maxwidth;
	}

	/**
	 * 设置柱状图是否显示边框
	 *
	 * @param p0
	 */
	public void setBarOutlineVisible(boolean p0) {
		this.baroutline = p0;
	}

	/**
	 * 设置柱图渐变颜色方向
	 *
	 * @param barTransType
	 *            1垂直，2水平，3垂直居中，4水平居中
	 */
	public void setBarTransType(int barTransType) {
		if (barTransType == 1)
			this.bartranstype = GradientPaintTransformType.VERTICAL;
		if (barTransType == 2)
			this.bartranstype = GradientPaintTransformType.HORIZONTAL;
		if (barTransType == 3)
			this.bartranstype = GradientPaintTransformType.CENTER_VERTICAL;
		if (barTransType == 4)
			this.bartranstype = GradientPaintTransformType.CENTER_HORIZONTAL;
	}

	/**
	 * 设置线性图是否用虚线显示
	 *
	 * @param p0
	 */
	public void setLineStroke0(boolean p0) {
		this.linestroke0 = p0;
	}

	public void setLineStroke1(boolean p0) {
		this.linestroke1 = p0;
	}

	public void setLineStroke2(boolean p0) {
		this.linestroke2 = p0;
	}

	public void setLineStroke3(boolean p0) {
		this.linestroke3 = p0;
	}

	public void setLineStroke4(boolean p0) {
		this.linestroke4 = p0;
	}

	public void setLineStroke5(boolean p0) {
		this.linestroke5 = p0;
	}

	public void setLineStroke6(boolean p0) {
		this.linestroke6 = p0;
	}

	public void setLineStroke7(boolean p0) {
		this.linestroke7 = p0;
	}

	public void setLineStroke8(boolean p0) {
		this.linestroke8 = p0;
	}

	public void setLineStroke9(boolean p0) {
		this.linestroke9 = p0;
	}

	/**
	 * 获取线性图是否显示0值点
	 *
	 * @return
	 */
	public boolean getIncludesZero() {
		return this.includeszero;
	}

	/**
	 * 设置线性图是否显示0值点
	 *
	 * @param isIncludesZero
	 */
	public void setIncludesZero(boolean p0) {
		this.includeszero = p0;
	}

	/**
	 * 设置双轴图是否同步Y轴最大值高度
	 *
	 * @param isSynchro
	 */
	public void setSynchroAxis(boolean isSynchro) {
		this.synchroaxis = isSynchro;
	}

	/**
	 * 获取双轴图是否同步Y轴最大值高度
	 *
	 * @return
	 */
	public boolean getSynchroAxis() {
		return this.synchroaxis;
	}

	/**
	 * 设置对比图上图形占比
	 *
	 * @param p0
	 */
	public void setUpWight(int p0) {
		if (p0 >= 1)
			this.upwight = p0;
	}

	/**
	 * 设置对比图下图形占比
	 *
	 * @param p0
	 */
	public void setDownWight(int p0) {
		if (p0 >= 1)
			this.downwight = p0;
	}

	/**
	 * 设置特殊设定第一座标起始刻度值
	 *
	 * @param p0
	 */
	public void setRangeStart(double p0) {
		this.rangeStart = p0;
	}

	/**
	 * 设置特殊设定第一座标终止刻度值
	 *
	 * @param p0
	 */
	public void setRangeEnd(double p0) {
		this.rangeEnd = p0;
	}

	/**
	 * 设置特殊设定第二座标起始刻度值
	 *
	 * @param p0
	 */
	public void setRangeStartExtend(double p0) {
		this.rangeStart_extend = p0;
	}

	/**
	 * 设置特殊设定第二座标终止刻度值
	 *
	 * @param dRange
	 */
	public void setRangeEndExtend(double dRange) {
		this.rangeEnd_extend = dRange;
	}

	/**
	 * 设置特殊设定目标名称
	 *
	 * @param p0
	 */
	public void setRangeName(String p0) {
		this.rangename = p0;
	}

	/**
	 * 设置特殊设定目标上限值
	 *
	 * @param p0
	 */
	public void setTopRange(double p0) {
		this.toprange = p0;
	}

	/**
	 * 设置特殊设定目标下限值
	 *
	 * @param p0
	 */
	public void setBottomRange(double p0) {
		this.bottomrange = p0;
	}

	/**
	 * 是否渐变色
	 *
	 * @param color
	 */
	public void setUseTransColor(boolean isColorSwoon) {
		this.usetranscolor = isColorSwoon;
	}

	/**
	 * 第一序列色彩
	 *
	 */
	public Paint getChartColor0() {
		return this.color0;
	}

	public void setChartColor0(Paint color) {
		this.color0 = color;
		this.gradientpaint0 = new GradientPaint(0.0F, 0.0F, (Color) transcolor0, 0.0F, 0.0F,
				(Color) color0);
	}

	public Paint getChartTransColor0() {
		return this.transcolor0;
	}

	public void setChartTransColor0(Paint color) {
		this.transcolor0 = color;
		this.gradientpaint0 = new GradientPaint(0.0F, 0.0F, (Color) transcolor0, 0.0F, 0.0F,
				(Color) color0);
	}

	/**
	 * 第二序列色彩
	 *
	 */
	public Paint getChartColor1() {
		return this.color1;
	}

	public void setChartColor1(Paint color) {
		this.color1 = color;
		this.gradientpaint1 = new GradientPaint(0.0F, 0.0F, (Color) transcolor1, 0.0F, 0.0F,
				(Color) color1);
	}

	public Paint getChartTransColor1() {
		return this.transcolor1;
	}

	public void setChartTransColor1(Paint chartcolor) {
		this.transcolor1 = chartcolor;
		this.gradientpaint1 = new GradientPaint(0.0F, 0.0F, (Color) transcolor1, 0.0F, 0.0F,
				(Color) color1);
	}

	/**
	 * 第三序列色彩
	 *
	 */
	public Paint getChartColor2() {
		return this.color2;
	}

	public void setChartColor2(Paint color) {
		this.color2 = color;
		this.gradientpaint2 = new GradientPaint(0.0F, 0.0F, (Color) transcolor2, 0.0F, 0.0F,
				(Color) color2);
	}

	public Paint getChartTransColor2() {
		return this.transcolor2;
	}

	public void setChartTransColor2(Paint chartcolor) {
		this.transcolor2 = chartcolor;
		this.gradientpaint2 = new GradientPaint(0.0F, 0.0F, (Color) transcolor2, 0.0F, 0.0F,
				(Color) color2);
	}

	/**
	 * 第四序列色彩
	 *
	 * @param color
	 */
	public Paint getChartColor3() {
		return this.color3;
	}

	public void setChartColor3(Paint color) {
		this.color3 = color;
		this.gradientpaint3 = new GradientPaint(0.0F, 0.0F, (Color) transcolor3, 0.0F, 0.0F,
				(Color) color3);
	}

	public Paint getChartTransColor3() {
		return this.transcolor3;
	}

	public void setChartTransColor3(Paint chartcolor) {
		this.transcolor3 = chartcolor;
		this.gradientpaint3 = new GradientPaint(0.0F, 0.0F, (Color) transcolor3, 0.0F, 0.0F,
				(Color) color3);
	}

	/**
	 * 第五序列色彩
	 *
	 * @param color
	 */
	public Paint getChartColor4() {
		return this.color4;
	}

	public void setChartColor4(Paint color) {
		this.color4 = color;
		this.gradientpaint4 = new GradientPaint(0.0F, 0.0F, (Color) transcolor4, 0.0F, 0.0F,
				(Color) color4);
	}

	public Paint getChartTransColor4() {
		return this.transcolor4;
	}

	public void setChartTransColor4(Paint chartcolor) {
		this.transcolor4 = chartcolor;
		this.gradientpaint4 = new GradientPaint(0.0F, 0.0F, (Color) transcolor4, 0.0F, 0.0F,
				(Color) color4);
	}

	/**
	 * 第六序列色彩
	 *
	 * @param color
	 */
	public Paint getChartColor5() {
		return this.color5;
	}

	public void setChartColor5(Paint color) {
		this.color5 = color;
		this.gradientpaint5 = new GradientPaint(0.0F, 0.0F, (Color) transcolor5, 0.0F, 0.0F,
				(Color) color5);
	}

	public Paint getChartTransColor5() {
		return this.transcolor5;
	}

	public void setChartTransColor5(Paint chartcolor) {
		this.transcolor5 = chartcolor;
		this.gradientpaint5 = new GradientPaint(0.0F, 0.0F, (Color) transcolor5, 0.0F, 0.0F,
				(Color) color5);
	}

	/**
	 * 第七序列色彩
	 *
	 * @param color
	 */
	public Paint getChartColor6() {
		return this.color6;
	}

	public void setChartColor6(Paint color) {
		this.color6 = color;
		this.gradientpaint6 = new GradientPaint(0.0F, 0.0F, (Color) transcolor6, 0.0F, 0.0F,
				(Color) color6);
	}

	public Paint getChartTransColor6() {
		return this.transcolor6;
	}

	public void setChartTransColor6(Paint chartcolor) {
		this.transcolor6 = chartcolor;
		this.gradientpaint6 = new GradientPaint(0.0F, 0.0F, (Color) transcolor6, 0.0F, 0.0F,
				(Color) color6);
	}

	/**
	 * 第八序列色彩
	 *
	 * @param color
	 */
	public Paint getChartColor7() {
		return this.color7;
	}

	public void setChartColor7(Paint color) {
		this.color7 = color;
		this.gradientpaint7 = new GradientPaint(0.0F, 0.0F, (Color) transcolor7, 0.0F, 0.0F,
				(Color) color7);
	}

	public Paint getChartTransColor7() {
		return this.transcolor7;
	}

	public void setChartTransColor7(Paint chartcolor) {
		this.transcolor7 = chartcolor;
		this.gradientpaint7 = new GradientPaint(0.0F, 0.0F, (Color) transcolor7, 0.0F, 0.0F,
				(Color) color7);
	}

	/**
	 * 第九序列色彩
	 *
	 * @param color
	 */
	public Paint getChartColor8() {
		return this.color8;
	}

	public void setChartColor8(Paint color) {
		this.color8 = color;
		this.gradientpaint8 = new GradientPaint(0.0F, 0.0F, (Color) transcolor8, 0.0F, 0.0F,
				(Color) color8);
	}

	public Paint getChartTransColor8() {
		return this.transcolor8;
	}

	public void setChartTransColor8(Paint chartcolor) {
		this.transcolor8 = chartcolor;
		this.gradientpaint8 = new GradientPaint(0.0F, 0.0F, (Color) transcolor8, 0.0F, 0.0F,
				(Color) color8);
	}

	/**
	 * 第十序列色彩
	 *
	 * @param color
	 */
	public Paint getChartColor9() {
		return this.color9;
	}

	public void setChartColor9(Paint color) {
		this.color9 = color;
		this.gradientpaint9 = new GradientPaint(0.0F, 0.0F, (Color) transcolor9, 0.0F, 0.0F,
				(Color) color9);
	}

	public Paint getChartTransColor9() {
		return this.transcolor9;
	}

	public void setChartTransColor9(Paint chartcolor) {
		this.transcolor9 = chartcolor;
		this.gradientpaint9 = new GradientPaint(0.0F, 0.0F, (Color) transcolor9, 0.0F, 0.0F,
				(Color) color9);
	}

	/** ************************生成图形的方法开始********************************* */
	/**
	 * 生成平面饼状图形generatePieChart
	 *
	 * @param p0
	 *            需要显示的数据索引值
	 * @param session
	 * @param pw
	 * @return 生成图片名称
	 */
	public String generatePieChart(int p0, HttpSession session, PrintWriter pw) {
		/* 图形定义 */
		jfreechart = ChartFactory.createPieChart(this.title, createPieDataset(p0), this.legend,
				true, false);
		setChartInfo();
		/* 图表区域对象 */
		PiePlot pieplot = (PiePlot) jfreechart.getPlot();
		pieplot.setOutlinePaint(this.piebordercolor);// 图形边框颜色
		pieplot.setSectionOutlinePaint(Color.gray);// 饼状图形圆形边框颜色
		pieplot.setStartAngle(90D);// 数值显示的起始角度
		pieplot.setDirection(Rotation.CLOCKWISE);// 数据显示的方向
		pieplot.setCircular(this.piecircular);// 图表是否为圆形
		pieplot.setLabelLinksVisible(this.pielinks);// 提示连线可见
		pieplot.setLabelLinkPaint(this.pielinkscolor);// 提示连线颜色
		pieplot.setLabelOutlinePaint(this.pielinkscolor);// 提示连线数据外框颜色
		pieplot.setLabelBackgroundPaint(Color.white);// 提示连线数据背景颜色
		pieplot.setLabelShadowPaint(Color.white);// 提示连线数据阴影颜色
		pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator(this.pielinkslegend));// 提示连线数据标签格式
		pieplot.setLabelFont(this.labelfont);// 提示连线数据标签字体
		pieplot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(this.pielegende));// 设置图例数据标签格式
		pieplot.setNoDataMessage(this.nodatamessage); // 空数据显示的信息
		pieplot.setNoDataMessageFont(this.titlefont);
		/* 生成图片和提示信息 */
		return generateFilename(session, pw);
	}

	/**
	 * 生成立体饼状图形generatePie3DChart
	 *
	 * @param p0
	 *            需要显示的数据索引值
	 * @param session
	 * @param pw
	 * @return 生成图片名称
	 */
	public String generatePie3DChart(int p0, HttpSession session, PrintWriter pw) {
		/* 图形定义 */
		jfreechart = ChartFactory.createPieChart3D(this.title, createPieDataset(p0), this.legend,
				true, false);
		setChartInfo();
		/* 图表区域对象 */
		PiePlot3D pieplot = (PiePlot3D) jfreechart.getPlot();
		pieplot.setForegroundAlpha(this.alpha);// 设置图表的透明度
		pieplot.setOutlinePaint(this.piebordercolor);// 图形边框颜色
		pieplot.setSectionOutlinePaint(Color.gray);// 饼状图形圆形边框颜色
		pieplot.setStartAngle(90D);// 数值显示的起始角度
		pieplot.setDirection(Rotation.CLOCKWISE);// 数据显示的方向
		pieplot.setCircular(this.piecircular);// 图表是否为圆形
		pieplot.setLabelLinksVisible(this.pielinks);// 提示连线可见
		pieplot.setLabelLinkPaint(this.pielinkscolor);// 提示连线颜色
		pieplot.setLabelOutlinePaint(this.pielinkscolor);// 提示连线数据外框颜色
		pieplot.setLabelBackgroundPaint(Color.white);// 提示连线数据背景颜色
		pieplot.setLabelShadowPaint(Color.white);// 提示连线数据阴影颜色
		pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator(this.pielinkslegend));// 提示连线数据标签格式
		pieplot.setLabelFont(this.labelfont);// 提示连线数据标签字体
		pieplot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(this.pielegende));// 设置图例数据标签格式
		pieplot.setNoDataMessage(this.nodatamessage); // 空数据显示的信息
		pieplot.setNoDataMessageFont(this.titlefont);
		/* 生成图片和提示信息 */
		return generateFilename(session, pw);
	}

	/**
	 * 生成指环状饼图形generatePieRingChart
	 *
	 * @param p0
	 *            需要显示的数据索引值
	 * @param session
	 * @param pw
	 * @return 生成图片名称
	 */
	public String generatePieRingChart(int p0, HttpSession session, PrintWriter pw) {
		/* 图形定义 */
		jfreechart = ChartFactory.createRingChart(this.title, createPieDataset(p0), this.legend,
				true, false);
		setChartInfo();
		/* 图表区域对象 */
		RingPlot pieplot = (RingPlot) jfreechart.getPlot();
		pieplot.setOutlinePaint(this.piebordercolor);// 图形边框颜色
		pieplot.setSectionOutlinePaint(Color.gray);// 饼状图形圆形边框颜色
		pieplot.setStartAngle(90D);// 数值显示的起始角度
		pieplot.setDirection(Rotation.CLOCKWISE);// 数据显示的方向
		pieplot.setCircular(this.piecircular);// 图表是否为圆形
		pieplot.setLabelLinksVisible(this.pielinks);// 提示连线可见
		pieplot.setLabelLinkPaint(this.pielinkscolor);// 提示连线颜色
		pieplot.setLabelOutlinePaint(this.pielinkscolor);// 提示连线数据外框颜色
		pieplot.setLabelBackgroundPaint(Color.white);// 提示连线数据背景颜色
		pieplot.setLabelShadowPaint(Color.white);// 提示连线数据阴影颜色
		pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator(this.pielinkslegend));// 提示连线数据标签格式
		pieplot.setLabelFont(this.labelfont);// 提示连线数据标签字体
		pieplot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(this.pielegende));// 设置图例数据标签格式
		pieplot.setNoDataMessage(this.nodatamessage); // 空数据显示的信息
		pieplot.setNoDataMessageFont(this.titlefont);
		/* 生成图片和提示信息 */
		return generateFilename(session, pw);
	}

	/**
	 * 生成平面组合饼状图形generateMultiplePieChart
	 *
	 * @param session
	 * @param pw
	 * @return 生成图片名称
	 */
	public String generateMultiplePieChart(HttpSession session, PrintWriter pw) {
		/* 图形定义 */
		jfreechart = ChartFactory.createMultiplePieChart(this.title, createCategoryDataset(),
				TableOrder.BY_ROW, this.legend, true, false);
		setChartInfo();
		/* 图表区域对象 */
		MultiplePiePlot multiplepieplot = (MultiplePiePlot) jfreechart.getPlot();
		jfreechartExtend = multiplepieplot.getPieChart();// 获得所有图表对象
		PiePlot pieplot = (PiePlot) jfreechartExtend.getPlot();// 图表区域对象
		pieplot.setOutlinePaint(this.piebordercolor);// 图形边框颜色
		pieplot.setSectionOutlinePaint(Color.gray);// 饼状图形圆形边框颜色
		pieplot.setStartAngle(90D);// 数值显示的起始角度
		pieplot.setDirection(Rotation.CLOCKWISE);// 数据显示的方向
		pieplot.setCircular(this.piecircular);// 图表是否为圆形
		pieplot.setLabelLinksVisible(this.pielinks);// 提示连线可见
		pieplot.setLabelLinkPaint(this.pielinkscolor);// 提示连线颜色
		pieplot.setLabelOutlinePaint(this.pielinkscolor);// 提示连线数据外框颜色
		pieplot.setLabelBackgroundPaint(Color.white);// 提示连线数据背景颜色
		pieplot.setLabelShadowPaint(Color.white);// 提示连线数据阴影颜色
		pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator(this.pielinkslegend));// 提示连线数据标签格式
		pieplot.setLabelFont(this.labelfont);// 提示连线数据标签字体
		pieplot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(this.pielegende));// 设置图例数据标签格式
		multiplepieplot.setBackgroundPaint(this.backgroundcolor);// 图形背景色
		multiplepieplot.setNoDataMessage(this.nodatamessage);// 空数据显示的信息
		multiplepieplot.setNoDataMessageFont(this.titlefont);
		/* 生成图片和提示信息 */
		return generateFilename(session, pw);
	}

	/**
	 * 生成立体组合饼状图形generateMultiplePie3DChart
	 *
	 * @param session
	 * @param pw
	 * @return 生成图片名称
	 */
	public String generateMultiplePie3DChart(HttpSession session, PrintWriter pw) {
		/* 图形定义 */
		jfreechart = ChartFactory.createMultiplePieChart3D(this.title, createCategoryDataset(),
				TableOrder.BY_ROW, this.legend, true, false);
		setChartInfo();
		/* 图表区域对象 */
		MultiplePiePlot multiplepieplot = (MultiplePiePlot) jfreechart.getPlot();
		jfreechartExtend = multiplepieplot.getPieChart();// 获得所有图表对象
		PiePlot pieplot = (PiePlot) jfreechartExtend.getPlot();// 图表区域对象
		pieplot.setForegroundAlpha(this.alpha);// 设置图表的透明度
		pieplot.setOutlinePaint(this.piebordercolor);// 图形边框颜色
		pieplot.setSectionOutlinePaint(Color.gray);// 饼状图形圆形边框颜色
		pieplot.setStartAngle(90D);// 数值显示的起始角度
		pieplot.setDirection(Rotation.CLOCKWISE);// 数据显示的方向
		pieplot.setCircular(this.piecircular);// 图表是否为圆形
		pieplot.setLabelLinksVisible(this.pielinks);// 提示连线可见
		pieplot.setLabelLinkPaint(this.pielinkscolor);// 提示连线颜色
		pieplot.setLabelOutlinePaint(this.pielinkscolor);// 提示连线数据外框颜色
		pieplot.setLabelBackgroundPaint(Color.white);// 提示连线数据背景颜色
		pieplot.setLabelShadowPaint(Color.white);// 提示连线数据阴影颜色
		pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator(this.pielinkslegend));// 提示连线数据标签格式
		pieplot.setLabelFont(this.labelfont);// 提示连线数据标签字体
		pieplot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(this.pielegende));// 设置图例数据标签格式
		multiplepieplot.setBackgroundPaint(this.backgroundcolor);// 图形背景色
		multiplepieplot.setNoDataMessage(this.nodatamessage);// 空数据显示的信息
		/* 生成图片和提示信息 */
		return generateFilename(session, pw);
	}

	/**
	 * 生成平面簇状柱形图generateBarChart
	 *
	 * @param session
	 * @param pw
	 * @return 生成图片名称
	 */
	public String generateBarChart(HttpSession session, PrintWriter pw) {
		/* 图形定义 */
		jfreechart = ChartFactory.createBarChart(this.title, this.xAxisInfo, this.yAxisInfo,
				createCategoryDataset(), this.plotorientation, this.legend, true, false);
		setChartInfo();
		/* 图表区域对象 */
		CategoryPlot plot = jfreechart.getCategoryPlot();
		plot.setOutlinePaint(this.piebordercolor);// 图形边框颜色
		plot.setBackgroundPaint(this.backgroundcolor_plot);// 设置图表区域背景色
		plot.setDomainGridlinesVisible(this.gridlinesdomain);// 设置横坐标刻度线
		plot.setDomainGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setRangeGridlinesVisible(this.gridlinesrange);// 设置纵坐标刻度线
		plot.setRangeGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setNoDataMessage(this.nodatamessage);// 空数据显示的信息
		setMarker(plot);
		/* 设置图表表现形式 */
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		setBarRenderer(renderer, 0);
		/* 设置X轴坐标 */
		CategoryAxis domainAxis = plot.getDomainAxis();
		setCategoryAxis(domainAxis);
		/* 设置Y轴 */
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		setNumberAxis(numberAxis);
		/* 生成图片和提示信息 */
		return generateFilename(session, pw);
	}

	/**
	 * 生成立体簇状柱形图generateBar3DChart
	 *
	 * @param session
	 * @param pw
	 * @return 生成图片名称
	 */
	public String generateBar3DChart(HttpSession session, PrintWriter pw) {
		/* 图形定义 */
		jfreechart = ChartFactory.createBarChart3D(this.title, this.xAxisInfo, this.yAxisInfo,
				createCategoryDataset(), this.plotorientation, this.legend, true, false);
		setChartInfo();
		/* 图表区域对象 */
		CategoryPlot plot = jfreechart.getCategoryPlot();
		plot.setOutlinePaint(this.piebordercolor);// 图形边框颜色
		plot.setForegroundAlpha(this.alpha);// 图表区域透明度
		plot.setBackgroundPaint(this.backgroundcolor_plot);// 设置图表区域背景色
		plot.setDomainGridlinesVisible(this.gridlinesdomain);// 设置横坐标刻度线
		plot.setDomainGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setRangeGridlinesVisible(this.gridlinesrange);// 设置纵坐标刻度线
		plot.setRangeGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setNoDataMessage(this.nodatamessage);// 空数据显示的信息
		setMarker(plot);
		/* 设置图表表现形式 */
		BarRenderer3D renderer = (BarRenderer3D) plot.getRenderer();
		setBarRenderer3D(renderer, 0);
		/* 设置X轴 */
		CategoryAxis domainAxis = plot.getDomainAxis();
		setCategoryAxis(domainAxis);
		/* 设置Y轴 */
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		setNumberAxis(numberAxis);
		/* 生成图片和提示信息 */
		return generateFilename(session, pw);
	}

	/**
	 * 生成平面堆积柱形图generateBarChartStacked
	 *
	 * @param session
	 * @param pw
	 * @return
	 */
	public String generateBarChartStacked(HttpSession session, PrintWriter pw) {
		/* 图形定义 */
		jfreechart = ChartFactory.createStackedBarChart(this.title, this.xAxisInfo, this.yAxisInfo,
				createCategoryDataset(), this.plotorientation, this.legend, true, false);
		setChartInfo();
		/* 图表区域对象 */
		CategoryPlot plot = jfreechart.getCategoryPlot();
		plot.setOutlinePaint(this.piebordercolor);// 图形边框颜色
		plot.setBackgroundPaint(this.backgroundcolor_plot);// 设置图表区域背景色
		plot.setDomainGridlinesVisible(this.gridlinesdomain);// 设置横坐标刻度线
		plot.setDomainGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setRangeGridlinesVisible(this.gridlinesrange);// 设置纵坐标刻度线
		plot.setRangeGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setNoDataMessage(this.nodatamessage);// 空数据显示的信息
		setMarker(plot);
		/* 设置图表表现形式 */
		StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
		setStackedBarRenderer(renderer, 0);
		/* 设置X轴 */
		CategoryAxis domainAxis = plot.getDomainAxis();
		setCategoryAxis(domainAxis);
		/* 设置Y轴 */
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		setNumberAxis(numberAxis);
		/* 生成图片和提示信息 */
		return generateFilename(session, pw);
	}

	/**
	 * 生成立体堆积柱形图generateBar3DChartStacked
	 *
	 * @param session
	 * @param pw
	 * @return
	 */
	public String generateBar3DChartStacked(HttpSession session, PrintWriter pw) {
		/* 图形定义 */
		jfreechart = ChartFactory.createStackedBarChart3D(this.title, this.xAxisInfo,
				this.yAxisInfo, createCategoryDataset(), this.plotorientation, this.legend, true,
				false);
		setChartInfo();
		/* 图表区域对象 */
		CategoryPlot plot = jfreechart.getCategoryPlot();
		plot.setOutlinePaint(this.piebordercolor);// 图形边框颜色
		plot.setForegroundAlpha(this.alpha);// 图表区域透明度
		plot.setBackgroundPaint(this.backgroundcolor_plot);// 设置图表区域背景色
		plot.setDomainGridlinesVisible(this.gridlinesdomain);// 设置横坐标刻度线
		plot.setDomainGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setRangeGridlinesVisible(this.gridlinesrange);// 设置纵坐标刻度线
		plot.setRangeGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setNoDataMessage(this.nodatamessage);// 空数据显示的信息
		setMarker(plot);
		/* 设置图表对象的显示 */
		StackedBarRenderer3D renderer = (StackedBarRenderer3D) plot.getRenderer();
		setStackedBarRenderer3D(renderer, 0);
		/* 设置X轴 */
		CategoryAxis domainAxis = plot.getDomainAxis();
		setCategoryAxis(domainAxis);
		/* 设置Y轴 */
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		setNumberAxis(numberAxis);
		/* 生成图片和提示信息 */
		return generateFilename(session, pw);
	}

	/**
	 * 生成平面面积图generateAreaChart
	 *
	 * @param session
	 * @param pw
	 * @return 生成图片名称
	 */
	public String generateAreaChart(HttpSession session, PrintWriter pw) {
		/* 图形定义 */
		jfreechart = ChartFactory.createAreaChart(this.title, this.xAxisInfo, this.yAxisInfo,
				createCategoryDataset(), this.plotorientation, this.legend, true, false);
		setChartInfo();
		/* 图表区域对象 */
		CategoryPlot plot = jfreechart.getCategoryPlot();
		plot.setOutlinePaint(this.piebordercolor);// 图形边框颜色
		plot.setForegroundAlpha(this.alpha);// 图表区域透明度
		plot.setBackgroundPaint(this.backgroundcolor_plot);// 设置图表区域背景色
		plot.setDomainGridlinesVisible(this.gridlinesdomain);// 设置横坐标刻度线
		plot.setDomainGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setRangeGridlinesVisible(this.gridlinesrange);// 设置纵坐标刻度线
		plot.setRangeGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setNoDataMessage(this.nodatamessage);// 空数据显示的信息
		setMarker(plot);
		/* 设置图表对象的显示 */
		AreaRenderer renderer = (AreaRenderer) plot.getRenderer();
		setAreaRenderer(renderer, 0);
		/* 设置X轴 */
		CategoryAxis domainAxis = plot.getDomainAxis();
		setCategoryAxis(domainAxis);
		/* 设置Y轴 */
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		setNumberAxis(numberAxis);
		/* 生成图片和提示信息 */
		return generateFilename(session, pw);
	}

	/**
	 * 生成堆积面积图generateAreaChartStacked
	 *
	 * @param session
	 * @param pw
	 * @return 生成图片名称
	 */
	public String generateAreaChartStacked(HttpSession session, PrintWriter pw) {
		/* 图形定义 */
		jfreechart = ChartFactory.createStackedAreaChart(this.title, this.xAxisInfo,
				this.yAxisInfo, createCategoryDataset(), this.plotorientation, this.legend, true,
				false);
		setChartInfo();
		/* 图表区域对象 */
		CategoryPlot plot = jfreechart.getCategoryPlot();
		plot.setOutlinePaint(this.piebordercolor);// 图形边框颜色
		plot.setForegroundAlpha(this.alpha);// 图表区域透明度
		plot.setBackgroundPaint(this.backgroundcolor_plot);// 设置图表区域背景色
		plot.setDomainGridlinesVisible(this.gridlinesdomain);// 设置横坐标刻度线
		plot.setDomainGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setRangeGridlinesVisible(this.gridlinesrange);// 设置纵坐标刻度线
		plot.setRangeGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setNoDataMessage(this.nodatamessage);// 空数据显示的信息
		setMarker(plot);
		/* 设置图表对象的显示 */
		StackedAreaRenderer renderer = (StackedAreaRenderer) plot.getRenderer();
		setStackedAreaRenderer(renderer, 0);
		/* 设置X轴 */
		CategoryAxis domainAxis = plot.getDomainAxis();
		setCategoryAxis(domainAxis);
		/* 设置Y轴 */
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		setNumberAxis(numberAxis);
		/* 生成图片和提示信息 */
		return generateFilename(session, pw);
	}

	/**
	 * 生成平面折线图generateLineChart
	 *
	 * @param session
	 * @param pw
	 * @return 生成图片名称
	 */
	public String generateLineChart(HttpSession session, PrintWriter pw) {
		/* 图形定义 */
		jfreechart = ChartFactory.createLineChart(this.title, this.xAxisInfo, this.yAxisInfo,
				createCategoryDataset(), this.plotorientation, this.legend, true, false);
		setChartInfo();
		/* 图表区域对象 */
		CategoryPlot plot = (CategoryPlot) jfreechart.getPlot();
		plot.setOutlinePaint(this.piebordercolor);// 图形边框颜色
		plot.setBackgroundPaint(this.backgroundcolor_plot);// 设置图表区域背景色
		plot.setDomainGridlinesVisible(this.gridlinesdomain);// 设置横坐标刻度线
		plot.setDomainGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setRangeGridlinesVisible(this.gridlinesrange);// 设置纵坐标刻度线
		plot.setRangeGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setNoDataMessage(this.nodatamessage);// 空数据显示的信息
		setMarker(plot);
		/* 图表表现形式 */
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		setLineAndShapeRenderer(renderer, 0);
		/* 设置X轴 */
		CategoryAxis domainAxis = plot.getDomainAxis();
		setCategoryAxis(domainAxis);
		plot.setDomainAxis(domainAxis);
		/* 设置Y轴 */
		NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
		setNumberAxis(numberaxis);
		// 生成图片和提示信息
		return generateFilename(session, pw);
	}

	/**
	 * 生成立体折线图generateLine3DChart
	 *
	 * @param session
	 * @param pw
	 * @return 生成图片名称
	 */
	public String generateLine3DChart(HttpSession session, PrintWriter pw) {
		/* 图形定义 */
		jfreechart = ChartFactory.createLineChart3D(this.title, this.xAxisInfo, this.yAxisInfo,
				createCategoryDataset(), this.plotorientation, this.legend, true, false);
		setChartInfo();
		/* 图表区域对象 */
		CategoryPlot plot = (CategoryPlot) jfreechart.getPlot();
		plot.setOutlinePaint(this.piebordercolor);// 图形边框颜色
		plot.setBackgroundPaint(this.backgroundcolor_plot);// 设置图表区域背景色
		plot.setDomainGridlinesVisible(this.gridlinesdomain);// 设置横坐标刻度线
		plot.setDomainGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setRangeGridlinesVisible(this.gridlinesrange);// 设置纵坐标刻度线
		plot.setRangeGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setNoDataMessage(this.nodatamessage);// 空数据显示的信息
		plot.setNoDataMessageFont(this.titlefont);
		setMarker(plot);
		/* 图表表现形式 */
		LineRenderer3D linerenderer3D = (LineRenderer3D) plot.getRenderer();
		setLineRenderer3D(linerenderer3D, 0);
		/* 设置X轴 */
		CategoryAxis domainAxis = plot.getDomainAxis();
		setCategoryAxis(domainAxis);
		plot.setDomainAxis(domainAxis);
		/* 设置Y轴 */
		NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
		setNumberAxis(numberaxis);
		// 生成图片和提示信息
		return generateFilename(session, pw);
	}

	/**
	 * 生成雷达图generateSpiderChart
	 *
	 * @param session
	 * @param pw
	 * @return
	 */
	public String generateSpiderChart(HttpSession session, PrintWriter pw) {
		/* 图形定义 */
		SpiderWebPlot spiderwebplot = new SpiderWebPlot(createCategoryDataset());
		jfreechart = new JFreeChart(title, TextTitle.DEFAULT_FONT, spiderwebplot, this.legend);
		setChartInfo();
		/* 图表区域对象 */
		spiderwebplot.setOutlinePaint(this.piebordercolor);// 图形边框颜色
		spiderwebplot.setLabelFont(this.legendfont);
		spiderwebplot.setNoDataMessage(this.nodatamessage);
		spiderwebplot.setNoDataMessageFont(this.titlefont);
		/* 图表表现形式 */
		StandardCategoryToolTipGenerator tooltip = new StandardCategoryToolTipGenerator();
		spiderwebplot.setToolTipGenerator(tooltip);
		/* 生成图片和提示信息 */
		return generateFilename(session, pw);
	}

	/**
	 * 生成极面图generatePolarChart
	 *
	 * @param session
	 * @param pw
	 * @return 生成图片名称
	 */
	public String generatePolarChart(HttpSession session, PrintWriter pw) {
		/* 图形定义 */
		jfreechart = ChartFactory.createPolarChart(this.title, createXYDataset(), this.legend,
				true, false);
		setChartInfo();
		/* 图表区域对象 */
		PolarPlot plot = (PolarPlot) jfreechart.getPlot();
		plot.setOutlinePaint(this.piebordercolor);// 图形边框颜色
		plot.setBackgroundPaint(this.backgroundcolor_plot);// 设置图表区域背景色
		plot.setAngleLabelFont(new Font("宋体", 0, 12));
		plot.setNoDataMessage(this.nodatamessage);// 空数据显示的信息
		plot.setNoDataMessageFont(this.titlefont);
		// 生成图片和提示信息
		return generateFilename(session, pw);
	}

	/**
	 * 生成瀑布图generateWaterfallChart
	 *
	 * @param session
	 * @param pw
	 * @return 生成图片名称
	 */
	public String generateWaterfallChart(HttpSession session, PrintWriter pw) {
		/* 图形定义 */
		jfreechart = ChartFactory.createWaterfallChart(this.title, this.xAxisInfo, this.yAxisInfo,
				createCategoryDataset(), this.plotorientation, this.legend, true, false);
		setChartInfo();
		/* 图表区域对象 */
		CategoryPlot plot = (CategoryPlot) jfreechart.getPlot();
		plot.setOutlinePaint(this.piebordercolor);// 图形边框颜色
		plot.setForegroundAlpha(this.alpha);// 图表区域透明度
		plot.setBackgroundPaint(this.backgroundcolor_plot);// 设置图表区域背景色
		plot.setDomainGridlinesVisible(this.gridlinesdomain);// 设置横坐标刻度线
		plot.setDomainGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setRangeGridlinesVisible(this.gridlinesrange);// 设置纵坐标刻度线
		plot.setRangeGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		plot.setNoDataMessage(this.nodatamessage);// 空数据显示的信息
		plot.setNoDataMessageFont(this.titlefont);
		/* 设置图表表现形式 */
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		setBarRenderer(renderer, 0);
		/* 设置X轴 */
		CategoryAxis domainAxis = plot.getDomainAxis();
		setCategoryAxis(domainAxis);
		/* 设置Y轴 */
		NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
		setNumberAxis(numberaxis);
		/* 生成图片和提示信息 */
		return generateFilename(session, pw);
	}

	/**
	 * 生成气泡图generateBubbleChart
	 *
	 * @param session
	 * @param pw
	 * @return 生成图片名称
	 */
	public String generateBubbleChart(HttpSession session, PrintWriter pw) {
		/* 图形定义 */
		jfreechart = ChartFactory.createBubbleChart(this.title, this.xAxisInfo, this.yAxisInfo,
				createXYZDataset(), this.plotorientation, this.legend, true, false);
		setChartInfo();
		/* 图表区域对象 */
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setOutlinePaint(this.piebordercolor);// 图形边框颜色
		xyplot.setForegroundAlpha(this.alpha);
		xyplot.setNoDataMessage(this.nodatamessage);// 空数据显示的信息
		xyplot.setNoDataMessageFont(this.titlefont);

		/* 设置图表表现形式 */
		XYItemRenderer renderer = (XYItemRenderer) xyplot.getRenderer();
		setXYItemRenderer(renderer, 0);
		/* 设置X轴 */
		ValueAxis domainAxis = xyplot.getDomainAxis();
		setValueAxis(domainAxis);
		/* 设置Y轴 */
		NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
		setNumberAxis(numberaxis);
		// 生成图片和提示信息
		return generateFilename(session, pw);
	}

	/**
	 * 生成多重图形平面柱加折线图generateDualAxisChart
	 *
	 * @param p0
	 *            需要生成Line图形数据所在的索引
	 * @param session
	 * @param pw
	 * @return
	 */
	public String generateDualAxisChart(int p0, int pType, HttpSession session, PrintWriter pw) {
		/* 图表对应数据集对象 */
		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
		createCategoryDataset(p0, dataset1, dataset2);

		/* 建立平面柱状0,立体柱状1,平面堆积柱状2,立体堆积柱状3 */
		if (pType == 0 || pType == 10) {
			jfreechart = ChartFactory.createBarChart(this.title, this.xAxisInfo, this.yAxisInfo,
					dataset1, this.plotorientation, this.legend, true, false);
		}
		if (pType == 1 || pType == 11) {
			jfreechart = ChartFactory.createLineChart(this.title, this.xAxisInfo, this.yAxisInfo,
					dataset1, this.plotorientation, this.legend, true, false);
		}
		if (pType == 2 || pType == 12) {
			jfreechart = ChartFactory.createStackedBarChart(this.title, this.xAxisInfo,
					this.yAxisInfo, dataset1, this.plotorientation, this.legend, true, false);
		}
		if (pType == 3 || pType == 13) {
			jfreechart = ChartFactory.createStackedBarChart3D(this.title, this.xAxisInfo,
					this.yAxisInfo, dataset1, this.plotorientation, this.legend, true, false);
		}
		setChartInfo();

		/* 图表1区域对象柱状图 */
		CategoryPlot categoryplot = jfreechart.getCategoryPlot();
		categoryplot.setBackgroundPaint(this.backgroundcolor_plot);// 设置图表区域背景色
		categoryplot.setDomainGridlinesVisible(this.gridlinesdomain);// 设置横坐标刻度线
		categoryplot.setDomainGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		categoryplot.setRangeGridlinesVisible(this.gridlinesrange);// 设置纵坐标刻度线
		categoryplot.setRangeGridlinePaint(this.gridlinecolor);// 坐标刻度线颜色
		categoryplot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
		categoryplot.setForegroundAlpha(this.alpha);// 图表区域透明度
		/* 设置图表1表现形式 */
		if (pType == 1 || pType == 11) {
			LineAndShapeRenderer renderer = (LineAndShapeRenderer) categoryplot.getRenderer();
			setLineAndShapeRenderer(renderer, 0);
		} else {
			BarRenderer barrenderer = (BarRenderer) categoryplot.getRenderer();
			setBarRenderer(barrenderer, 0);
		}
		/* 设置设置第一Y轴定义 */
		NumberAxis numberAxis1 = (NumberAxis) categoryplot.getRangeAxis();
		setNumberAxis(numberAxis1);
		double maxvlaue = numberAxis1.getUpperBound();
		/* 图表2区域对象线形图 */
		categoryplot.setDataset(1, dataset2);
		categoryplot.mapDatasetToRangeAxis(1, 1);
		/* 设置X轴坐标 */
		CategoryAxis domainAxis = categoryplot.getDomainAxis();
		setCategoryAxis(domainAxis);
		/* 设置设置第二Y轴定义 */
		NumberAxis numberAxis = new NumberAxis(this.yAxisInfo_extend);
		categoryplot.setRangeAxis(1, numberAxis);
		if (this.rangeEnd_extend > 0)
			numberAxis.setRange(this.rangeStart_extend, this.rangeEnd_extend);
		if (synchroaxis)
			numberAxis.setUpperBound(maxvlaue);

		/* 设置图表2表现形式 */
		if (pType == 10 || pType == 11 || pType == 12 || pType == 13) {
			BarRenderer barrenderer1 = new BarRenderer();
			setBarRenderer(barrenderer1, p0);
			categoryplot.setRenderer(1, barrenderer1);
		} else {
			LineAndShapeRenderer lineandshaperenderer = new LineAndShapeRenderer();
			setLineAndShapeRenderer(lineandshaperenderer, p0);
			categoryplot.setRenderer(1, lineandshaperenderer);
		}
		categoryplot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		categoryplot.setNoDataMessage(this.nodatamessage);// 空数据显示的信息
		categoryplot.setNoDataMessageFont(this.titlefont);
		setMarker(categoryplot);
		/* 生成图片和提示信息 */
		return generateFilename(session, pw);
	}

	/**
	 * 生成多重图形对比图generateCombinedChart
	 *
	 * @param p0
	 *            需要生成对比图形数据所在的索引
	 * @param session
	 * @param pw
	 * @return
	 */
	public String generateCombinedChart(int p0, int pType, int pType2, HttpSession session,
			PrintWriter pw) {
		/* 图表对应数据集对象 */
		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
		createCategoryDataset(p0, dataset1, dataset2);

		/* 平面折线 */
		LineAndShapeRenderer renderer1 = new LineAndShapeRenderer();
		setLineAndShapeRenderer(renderer1, 0);
		LineAndShapeRenderer renderer1_ext = new LineAndShapeRenderer();
		setLineAndShapeRenderer(renderer1_ext, p0);
		/* 平面柱状 */
		BarRenderer renderer2 = new BarRenderer();
		setBarRenderer(renderer2, 0);
		BarRenderer renderer2_ext = new BarRenderer();
		setBarRenderer(renderer2_ext, p0);
		/* 立体柱状 */
		BarRenderer3D renderer3 = new BarRenderer3D();
		setBarRenderer3D(renderer3, 0);
		BarRenderer3D renderer3_ext = new BarRenderer3D();
		setBarRenderer3D(renderer3_ext, p0);
		/* 平面堆积柱状 */
		StackedBarRenderer renderer4 = new StackedBarRenderer();
		setStackedBarRenderer(renderer4, 0);
		StackedBarRenderer renderer4_ext = new StackedBarRenderer();
		setStackedBarRenderer(renderer4_ext, p0);
		/* 立体堆积柱状 */
		StackedBarRenderer3D renderer5 = new StackedBarRenderer3D();
		setStackedBarRenderer3D(renderer5, 0);
		StackedBarRenderer3D renderer5_ext = new StackedBarRenderer3D();
		setStackedBarRenderer3D(renderer5_ext, p0);

		/* 设置图表1Y轴坐标 */
		NumberAxis numberAxis1 = new NumberAxis(this.yAxisInfo);
		setNumberAxis(numberAxis1);
		/* 设置图表1表现形式 */
		CategoryPlot categoryplot1 = null;
		if (pType == 1) {
			categoryplot1 = new CategoryPlot(dataset1, null, numberAxis1, renderer1);
		}
		if (pType == 2) {
			categoryplot1 = new CategoryPlot(dataset1, null, numberAxis1, renderer2);
		}
		if (pType == 3) {
			categoryplot1 = new CategoryPlot(dataset1, null, numberAxis1, renderer3);
		}
		if (pType == 4) {
			categoryplot1 = new CategoryPlot(dataset1, null, numberAxis1, renderer4);
		}
		if (pType == 5) {
			categoryplot1 = new CategoryPlot(dataset1, null, numberAxis1, renderer5);
		}
		/* 图表1区域对象 */
		categoryplot1.setBackgroundPaint(backgroundcolor_plot); // 设置图表区域背景色
		categoryplot1.setDomainGridlinesVisible(this.gridlinesdomain); // 设置横坐标刻度线
		categoryplot1.setDomainGridlinePaint(gridlinecolor); // 坐标刻度线颜色
		categoryplot1.setRangeGridlinesVisible(this.gridlinesrange); // 设置纵坐标刻度线
		categoryplot1.setRangeGridlinePaint(gridlinecolor); // 坐标刻度线颜色
		categoryplot1.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
		categoryplot1.setForegroundAlpha(this.alpha); // 图表区域透明度
		categoryplot1.setNoDataMessage(this.nodatamessage);// 空数据显示的信息
		categoryplot1.setNoDataMessageFont(this.titlefont);

		/* 设置图表2Y轴坐标 */
		NumberAxis numberAxis2 = new NumberAxis(this.yAxisInfo_extend);
		setNumberAxis(numberAxis2);
		/* 设置图表2表现形式 */
		CategoryPlot categoryplot2 = null;
		if (pType2 == 1) {
			categoryplot2 = new CategoryPlot(dataset2, null, numberAxis2, renderer1_ext);
		}
		if (pType2 == 2) {
			categoryplot2 = new CategoryPlot(dataset2, null, numberAxis2, renderer2_ext);
		}
		if (pType2 == 3) {
			categoryplot2 = new CategoryPlot(dataset2, null, numberAxis2, renderer3_ext);
		}
		if (pType2 == 4) {
			categoryplot2 = new CategoryPlot(dataset2, null, numberAxis2, renderer4_ext);
		}
		if (pType2 == 5) {
			categoryplot2 = new CategoryPlot(dataset2, null, numberAxis2, renderer5_ext);
		}
		categoryplot2.setDomainGridlinesVisible(true);

		/* 设置X轴坐标 */
		CategoryAxis domainAxis = new CategoryAxis(this.xAxisInfo);
		setCategoryAxis(domainAxis);
		/* 图表区域对象 */
		CombinedDomainCategoryPlot plot = new CombinedDomainCategoryPlot(domainAxis);
		plot.add(categoryplot1, this.upwight);
		plot.add(categoryplot2, this.downwight);
		plot.setOrientation(this.plotorientation);
		plot.setNoDataMessage(this.nodatamessage);// 空数据显示的信息
		plot.setNoDataMessageFont(this.titlefont);
		/* 图形定义 */
		jfreechart = new JFreeChart(title, new Font("宋体", 0, 12), plot, true);
		setChartInfo();
		/* 生成图片和提示信息 */
		return generateFilename(session, pw);
	}

	/** ************************生成图形的方法结束********************************* */

	/** ************************生成图形表现方式开始********************************* */
	/**
	 * 设置图形基本属性
	 */
	private void setChartInfo() {
		// 设置图例
		if (this.legend) {
			LegendTitle legendtitle = (LegendTitle) jfreechart.getSubtitle(0);
			legendtitle.setItemFont(this.legendfont);
			legendtitle.setBorder(legendborder, legendborder, legendborder, legendborder);
			legendtitle.setPosition(this.legendpos);
		}
		// 设置背景色
		jfreechart.setBackgroundPaint(this.backgroundcolor);
		// 设置边框线可见
		jfreechart.setBorderVisible(this.bordervisible);
		jfreechart.setBorderPaint(this.bordercolor);
		// 设置标题字体
		jfreechart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		TextTitle textTitle = new TextTitle(this.title, this.titlefont);
		jfreechart.setTitle(textTitle);
	}

	/**
	 * 设置X轴属性
	 *
	 * @param domainAxis
	 */
	private void setCategoryAxis(CategoryAxis domainAxis) {
		domainAxis.setTickLabelFont(this.categoryaxisfont);
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(this.categorylabelpositions));
		CategoryLabelPositions categorylabelpositions = domainAxis.getCategoryLabelPositions();
		CategoryLabelPosition categorylabelposition = new CategoryLabelPosition(
				RectangleAnchor.LEFT, TextBlockAnchor.CENTER_LEFT, TextAnchor.CENTER_LEFT, 0.0D,
				CategoryLabelWidthType.RANGE, 0.3F);
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.replaceLeftPosition(
				categorylabelpositions, categorylabelposition));
	}

	/**
	 * 设置Y轴属性
	 *
	 * @param numberaxis
	 */
	private void setNumberAxis(NumberAxis numberaxis) {
		numberaxis.setTickLabelFont(this.numberaxisfont);
		numberaxis.setUpperMargin(0.1D);
		numberaxis.setLowerMargin(0.1D);
		numberaxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
		numberaxis.setAutoRangeIncludesZero(this.includeszero);
		if (this.rangeEnd > 0)
			numberaxis.setRange(this.rangeStart, this.rangeEnd);
	}

	/**
	 * 设置X轴属性
	 *
	 * @param domainAxis
	 */
	private void setValueAxis(ValueAxis domainAxis) {
		domainAxis.setTickLabelFont(this.categoryaxisfont);
		domainAxis.setLabelAngle(this.categorylabelpositions);
	}

	/**
	 * 生成图片和提示信息
	 *
	 * @param session
	 * @param pw
	 * @return
	 */
	private String generateFilename(HttpSession session, PrintWriter pw) {
		String filename = "";
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		try {
			filename = ServletUtilities.saveChartAsPNG(jfreechart, width, height, info, session);
			ChartUtilities.writeImageMap(pw, filename, info, false);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace(System.out);
			filename = "public_error_500x300.png";
		}
		return filename;
	}

	/**
	 * 设置目标值
	 *
	 * @param categoryplot
	 */
	private void setMarker(CategoryPlot categoryplot) {
		if (bottomrange > toprange)
			bottomrange = toprange;

		IntervalMarker intervalmarker = new IntervalMarker(bottomrange, toprange);
		intervalmarker.setLabel(rangename);
		intervalmarker.setLabelFont(new Font("宋体", 2, 11));
		intervalmarker.setLabelAnchor(RectangleAnchor.LEFT);
		intervalmarker.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
		intervalmarker.setPaint(new Color(222, 222, 255, 128));
		categoryplot.addRangeMarker(intervalmarker, Layer.BACKGROUND);
	}

	/**
	 * 设置Bar图表表现形式
	 *
	 * @param render
	 * @param p0
	 */
	private void setBarRenderer(BarRenderer render, int p0) {
		render.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		render.setItemMargin(this.baritemmargin);// 柱子间的间距
		render.setMinimumBarLength(0.5D);// 最小的柱子长度
		render.setMaximumBarWidth(this.barmaxwidth);// 柱子的最大宽度
		render.setDrawBarOutline(this.baroutline);// 是否显示边框
		render.setBaseOutlinePaint(Color.gray);// 边框颜色
		// 设置数值显示在图表上
		render.setItemLabelsVisible(true);
		render.setItemLabelFont(this.labelfont);
		render.setPositiveItemLabelPosition(this.pos_o_pos);
		render.setPositiveItemLabelPositionFallback(this.pos_o_pos);
		DecimalFormat decimalformat = new DecimalFormat();
		render.setItemLabelGenerator(new StandardCategoryItemLabelGenerator(this.barlegend,
				decimalformat));
		// 设置渐变色的方式
		render.setGradientPaintTransformer(new StandardGradientPaintTransformer(this.bartranstype));
		if (this.usetranscolor) {
			// 渐变颜色
			int m = 0;
			if (p0 <= 0)
				render.setSeriesPaint(m++, gradientpaint0);
			if (p0 <= 1)
				render.setSeriesPaint(m++, gradientpaint1);
			if (p0 <= 2)
				render.setSeriesPaint(m++, gradientpaint2);
			if (p0 <= 3)
				render.setSeriesPaint(m++, gradientpaint3);
			if (p0 <= 4)
				render.setSeriesPaint(m++, gradientpaint4);
			if (p0 <= 5)
				render.setSeriesPaint(m++, gradientpaint5);
			if (p0 <= 6)
				render.setSeriesPaint(m++, gradientpaint6);
			if (p0 <= 7)
				render.setSeriesPaint(m++, gradientpaint7);
			if (p0 <= 8)
				render.setSeriesPaint(m++, gradientpaint8);
			if (p0 <= 9)
				render.setSeriesPaint(m++, gradientpaint9);
		} else {
			// 基本颜色
			int m = 0;
			if (p0 <= 0)
				render.setSeriesPaint(m++, color0);
			if (p0 <= 1)
				render.setSeriesPaint(m++, color1);
			if (p0 <= 2)
				render.setSeriesPaint(m++, color2);
			if (p0 <= 3)
				render.setSeriesPaint(m++, color3);
			if (p0 <= 4)
				render.setSeriesPaint(m++, color4);
			if (p0 <= 5)
				render.setSeriesPaint(m++, color5);
			if (p0 <= 6)
				render.setSeriesPaint(m++, color6);
			if (p0 <= 7)
				render.setSeriesPaint(m++, color7);
			if (p0 <= 8)
				render.setSeriesPaint(m++, color8);
			if (p0 <= 9)
				render.setSeriesPaint(m++, color9);
		}
	}

	/**
	 * 设置Bar3D图表表现形式
	 *
	 * @param render
	 * @param p0
	 */
	private void setBarRenderer3D(BarRenderer3D render, int p0) {
		render.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		render.setItemMargin(this.baritemmargin);// 柱子间的间距
		render.setMinimumBarLength(0.5D);// 最小的柱子长度
		render.setMaximumBarWidth(this.barmaxwidth);// 柱子的最大宽度
		render.setDrawBarOutline(this.baroutline);// 是否显示边框
		render.setBaseOutlinePaint(Color.gray);// 边框颜色
		// 设置数值显示在图表上
		render.setItemLabelsVisible(true);
		render.setItemLabelFont(this.labelfont);
		render.setPositiveItemLabelPosition(this.pos_o_b_l);
		render.setPositiveItemLabelPositionFallback(this.pos_o_b_l);
		DecimalFormat decimalformat = new DecimalFormat();
		render.setItemLabelGenerator(new StandardCategoryItemLabelGenerator(this.barlegend,
				decimalformat));
		// 基本颜色
		int m = 0;
		if (p0 <= 0)
			render.setSeriesPaint(m++, color0);
		if (p0 <= 1)
			render.setSeriesPaint(m++, color1);
		if (p0 <= 2)
			render.setSeriesPaint(m++, color2);
		if (p0 <= 3)
			render.setSeriesPaint(m++, color3);
		if (p0 <= 4)
			render.setSeriesPaint(m++, color4);
		if (p0 <= 5)
			render.setSeriesPaint(m++, color5);
		if (p0 <= 6)
			render.setSeriesPaint(m++, color6);
		if (p0 <= 7)
			render.setSeriesPaint(m++, color7);
		if (p0 <= 8)
			render.setSeriesPaint(m++, color8);
		if (p0 <= 9)
			render.setSeriesPaint(m++, color9);
	}

	/**
	 * 设置StackedBar图表表现形式
	 *
	 * @param render
	 * @param p0
	 */
	private void setStackedBarRenderer(StackedBarRenderer render, int p0) {
		render.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		render.setItemMargin(0.5D);// 柱子间的间距
		render.setMinimumBarLength(0.5D);// 最小的柱子长度
		render.setMaximumBarWidth(this.barmaxwidth);// 柱子的最大宽度
		render.setDrawBarOutline(this.baroutline);// 是否显示边框
		render.setBaseOutlinePaint(Color.gray);// 边框颜色
		// 设置数值显示在图表上
		render.setItemLabelsVisible(true);
		render.setItemLabelFont(this.labelfont);
		render.setPositiveItemLabelPosition(this.pos_o_t_r);
		render.setPositiveItemLabelPositionFallback(this.pos_o_t_r);
		DecimalFormat decimalformat = new DecimalFormat();
		render.setItemLabelGenerator(new StandardCategoryItemLabelGenerator(this.barlegend,
				decimalformat));
		// 设置渐变色的方式
		render.setGradientPaintTransformer(new StandardGradientPaintTransformer(this.bartranstype));
		if (this.usetranscolor) {
			// 渐变颜色
			int m = 0;
			if (p0 <= 0)
				render.setSeriesPaint(m++, gradientpaint0);
			if (p0 <= 1)
				render.setSeriesPaint(m++, gradientpaint1);
			if (p0 <= 2)
				render.setSeriesPaint(m++, gradientpaint2);
			if (p0 <= 3)
				render.setSeriesPaint(m++, gradientpaint3);
			if (p0 <= 4)
				render.setSeriesPaint(m++, gradientpaint4);
			if (p0 <= 5)
				render.setSeriesPaint(m++, gradientpaint5);
			if (p0 <= 6)
				render.setSeriesPaint(m++, gradientpaint6);
			if (p0 <= 7)
				render.setSeriesPaint(m++, gradientpaint7);
			if (p0 <= 8)
				render.setSeriesPaint(m++, gradientpaint8);
			if (p0 <= 9)
				render.setSeriesPaint(m++, gradientpaint9);
		} else {
			// 基本颜色
			int m = 0;
			if (p0 <= 0)
				render.setSeriesPaint(m++, color0);
			if (p0 <= 1)
				render.setSeriesPaint(m++, color1);
			if (p0 <= 2)
				render.setSeriesPaint(m++, color2);
			if (p0 <= 3)
				render.setSeriesPaint(m++, color3);
			if (p0 <= 4)
				render.setSeriesPaint(m++, color4);
			if (p0 <= 5)
				render.setSeriesPaint(m++, color5);
			if (p0 <= 6)
				render.setSeriesPaint(m++, color6);
			if (p0 <= 7)
				render.setSeriesPaint(m++, color7);
			if (p0 <= 8)
				render.setSeriesPaint(m++, color8);
			if (p0 <= 9)
				render.setSeriesPaint(m++, color9);
		}
	}

	/**
	 * 设置StackedBar3D图表表现形式
	 *
	 * @param render
	 * @param p0
	 */
	private void setStackedBarRenderer3D(StackedBarRenderer3D render, int p0) {
		render.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		render.setItemMargin(0.5D);// 柱子间的间距
		render.setMinimumBarLength(0.5D);// 最小的柱子长度
		render.setMaximumBarWidth(this.barmaxwidth);// 柱子的最大宽度
		render.setDrawBarOutline(this.baroutline);// 是否显示边框
		render.setBaseOutlinePaint(Color.gray);// 边框颜色
		// 设置数值显示在图表上
		render.setItemLabelsVisible(true);
		render.setItemLabelFont(this.labelfont);
		render.setPositiveItemLabelPosition(this.pos_o_t_r);
		render.setPositiveItemLabelPositionFallback(this.pos_o_t_r);
		DecimalFormat decimalformat = new DecimalFormat();
		render.setItemLabelGenerator(new StandardCategoryItemLabelGenerator(this.barlegend,
				decimalformat));
		// 基本颜色
		int m = 0;
		if (p0 <= 0)
			render.setSeriesPaint(m++, color0);
		if (p0 <= 1)
			render.setSeriesPaint(m++, color1);
		if (p0 <= 2)
			render.setSeriesPaint(m++, color2);
		if (p0 <= 3)
			render.setSeriesPaint(m++, color3);
		if (p0 <= 4)
			render.setSeriesPaint(m++, color4);
		if (p0 <= 5)
			render.setSeriesPaint(m++, color5);
		if (p0 <= 6)
			render.setSeriesPaint(m++, color6);
		if (p0 <= 7)
			render.setSeriesPaint(m++, color7);
		if (p0 <= 8)
			render.setSeriesPaint(m++, color8);
		if (p0 <= 9)
			render.setSeriesPaint(m++, color9);
	}

	/**
	 * 设置Area图表表现形式
	 *
	 * @param render
	 * @param p0
	 */
	private void setAreaRenderer(AreaRenderer render, int p0) {
		render.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		// 设置数值显示在图表上
		render.setItemLabelsVisible(true);
		render.setItemLabelFont(this.labelfont);
		render.setPositiveItemLabelPosition(this.pos_i_b_c);
		DecimalFormat decimalformat = new DecimalFormat();
		render.setItemLabelGenerator(new StandardCategoryItemLabelGenerator(this.barlegend,
				decimalformat));
		// 基本颜色
		int m = 0;
		if (p0 <= 0)
			render.setSeriesPaint(m++, color0);
		if (p0 <= 1)
			render.setSeriesPaint(m++, color1);
		if (p0 <= 2)
			render.setSeriesPaint(m++, color2);
		if (p0 <= 3)
			render.setSeriesPaint(m++, color3);
		if (p0 <= 4)
			render.setSeriesPaint(m++, color4);
		if (p0 <= 5)
			render.setSeriesPaint(m++, color5);
		if (p0 <= 6)
			render.setSeriesPaint(m++, color6);
		if (p0 <= 7)
			render.setSeriesPaint(m++, color7);
		if (p0 <= 8)
			render.setSeriesPaint(m++, color8);
		if (p0 <= 9)
			render.setSeriesPaint(m++, color9);
	}

	/**
	 * 设置StackedArea图表表现形式
	 *
	 * @param render
	 * @param p0
	 */
	private void setStackedAreaRenderer(StackedAreaRenderer render, int p0) {
		render.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		render.setBaseOutlinePaint(Color.GRAY);
		// 设置数值显示在图表上
		render.setItemLabelsVisible(true);
		render.setItemLabelFont(this.labelfont);
		render.setPositiveItemLabelPosition(this.pos_i_b_c);
		DecimalFormat decimalformat = new DecimalFormat();
		render.setItemLabelGenerator(new StandardCategoryItemLabelGenerator(this.barlegend,
				decimalformat));
		// 基本颜色
		int m = 0;
		if (p0 <= 0)
			render.setSeriesPaint(m++, color0);
		if (p0 <= 1)
			render.setSeriesPaint(m++, color1);
		if (p0 <= 2)
			render.setSeriesPaint(m++, color2);
		if (p0 <= 3)
			render.setSeriesPaint(m++, color3);
		if (p0 <= 4)
			render.setSeriesPaint(m++, color4);
		if (p0 <= 5)
			render.setSeriesPaint(m++, color5);
		if (p0 <= 6)
			render.setSeriesPaint(m++, color6);
		if (p0 <= 7)
			render.setSeriesPaint(m++, color7);
		if (p0 <= 8)
			render.setSeriesPaint(m++, color8);
		if (p0 <= 9)
			render.setSeriesPaint(m++, color9);
	}

	/**
	 * 设置Line图表表现形式
	 *
	 * @param render
	 * @param p0
	 */
	private void setLineAndShapeRenderer(LineAndShapeRenderer render, int p0) {
		render.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		render.setShapesVisible(true);// 序列线基本形状
		render.setLinesVisible(true);// 线的可见性
		render.setDrawOutlines(false);// 是否有外边框
		// 设置数值显示在图表上
		render.setItemLabelsVisible(true);
		render.setItemLabelFont(this.labelfont);
		render.setPositiveItemLabelPosition(this.pos_i_b_c);
		DecimalFormat decimalformat = new DecimalFormat();
		render.setItemLabelGenerator(new StandardCategoryItemLabelGenerator(this.barlegend,
				decimalformat));
		// 基本颜色
		int m = 0;
		if (p0 <= 0)
			render.setSeriesPaint(m++, color0);
		if (p0 <= 1)
			render.setSeriesPaint(m++, color1);
		if (p0 <= 2)
			render.setSeriesPaint(m++, color2);
		if (p0 <= 3)
			render.setSeriesPaint(m++, color3);
		if (p0 <= 4)
			render.setSeriesPaint(m++, color4);
		if (p0 <= 5)
			render.setSeriesPaint(m++, color5);
		if (p0 <= 6)
			render.setSeriesPaint(m++, color6);
		if (p0 <= 7)
			render.setSeriesPaint(m++, color7);
		if (p0 <= 8)
			render.setSeriesPaint(m++, color8);
		if (p0 <= 9)
			render.setSeriesPaint(m++, color9);
		// 是否显示为虚线
		m = 0;
		if (this.linestroke0)
			render.setSeriesStroke(m++, new BasicStroke(1.0F, 1, 1, 1.0F, new float[] { 10F, 6F },
					0.0F));
		if (this.linestroke1)
			render.setSeriesStroke(m++, new BasicStroke(1.0F, 1, 1, 1.0F, new float[] { 10F, 6F },
					0.0F));
		if (this.linestroke2)
			render.setSeriesStroke(m++, new BasicStroke(1.0F, 1, 1, 1.0F, new float[] { 10F, 6F },
					0.0F));
		if (this.linestroke3)
			render.setSeriesStroke(m++, new BasicStroke(1.0F, 1, 1, 1.0F, new float[] { 10F, 6F },
					0.0F));
		if (this.linestroke4)
			render.setSeriesStroke(m++, new BasicStroke(1.0F, 1, 1, 1.0F, new float[] { 10F, 6F },
					0.0F));
		if (this.linestroke5)
			render.setSeriesStroke(m++, new BasicStroke(1.0F, 1, 1, 1.0F, new float[] { 10F, 6F },
					0.0F));
		if (this.linestroke6)
			render.setSeriesStroke(m++, new BasicStroke(1.0F, 1, 1, 1.0F, new float[] { 10F, 6F },
					0.0F));
		if (this.linestroke7)
			render.setSeriesStroke(m++, new BasicStroke(1.0F, 1, 1, 1.0F, new float[] { 10F, 6F },
					0.0F));
		if (this.linestroke8)
			render.setSeriesStroke(m++, new BasicStroke(1.0F, 1, 1, 1.0F, new float[] { 10F, 6F },
					0.0F));
		if (this.linestroke9)
			render.setSeriesStroke(m++, new BasicStroke(1.0F, 1, 1, 1.0F, new float[] { 10F, 6F },
					0.0F));
	}

	/**
	 * 设置Line3D图表表现形式
	 *
	 * @param render
	 * @param p0
	 */
	private void setLineRenderer3D(LineRenderer3D render, int p0) {
		render.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		// 设置数值显示在图表上
		render.setItemLabelsVisible(true);
		render.setItemLabelFont(this.labelfont);
		render.setPositiveItemLabelPosition(this.pos_i_b_c);
		DecimalFormat decimalformat = new DecimalFormat();
		render.setItemLabelGenerator(new StandardCategoryItemLabelGenerator(this.barlegend,
				decimalformat));
		// 基本颜色
		int m = 0;
		if (p0 <= 0)
			render.setSeriesPaint(m++, color0);
		if (p0 <= 1)
			render.setSeriesPaint(m++, color1);
		if (p0 <= 2)
			render.setSeriesPaint(m++, color2);
		if (p0 <= 3)
			render.setSeriesPaint(m++, color3);
		if (p0 <= 4)
			render.setSeriesPaint(m++, color4);
		if (p0 <= 5)
			render.setSeriesPaint(m++, color5);
		if (p0 <= 6)
			render.setSeriesPaint(m++, color6);
		if (p0 <= 7)
			render.setSeriesPaint(m++, color7);
		if (p0 <= 8)
			render.setSeriesPaint(m++, color8);
		if (p0 <= 9)
			render.setSeriesPaint(m++, color9);
	}

	/**
	 * 设置XYItem图表表现形式
	 *
	 * @param render
	 * @param p0
	 */
	private void setXYItemRenderer(XYItemRenderer render, int p0) {
		// 基本颜色
		int m = 0;
		if (p0 <= 0)
			render.setSeriesPaint(m++, color0);
		if (p0 <= 1)
			render.setSeriesPaint(m++, color1);
		if (p0 <= 2)
			render.setSeriesPaint(m++, color2);
		if (p0 <= 3)
			render.setSeriesPaint(m++, color3);
		if (p0 <= 4)
			render.setSeriesPaint(m++, color4);
		if (p0 <= 5)
			render.setSeriesPaint(m++, color5);
		if (p0 <= 6)
			render.setSeriesPaint(m++, color6);
		if (p0 <= 7)
			render.setSeriesPaint(m++, color7);
		if (p0 <= 8)
			render.setSeriesPaint(m++, color8);
		if (p0 <= 9)
			render.setSeriesPaint(m++, color9);
	}

	/** ************************生成图形表现方式结束********************************* */

	/** ***************************通用图形数据集方法开始*************************** */
	/**
	 * 生成饼状图形数据createPieDataset(int p0)
	 *
	 * @param p0
	 * @return
	 */
	private PieDataset createPieDataset(int p0) {
		DefaultPieDataset defaultpiedataset = new DefaultPieDataset();
		if (value == null || value.length == 0)
			return defaultpiedataset;

		for (int i = 0; p0 < value.length && i < series.length; i++) {
			if (series[i] == null)
				series[i] = "未知" + i;
			defaultpiedataset.setValue(series[i], value[p0][i]);
		}
		return defaultpiedataset;
	}

	/**
	 * 返回通用分类图形数据createCategoryDataset()
	 *
	 * @return
	 */
	// private CategoryDataset createCategoryDataset() {
	// if (value == null || value.length == 0)
	// return null;
	// CategoryDataset categorydataset = DatasetUtilities
	// .createCategoryDataset(category, series, value);
	// return categorydataset;
	// }
	private CategoryDataset createCategoryDataset() {
		if (value == null || value.length == 0)
			return null;

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; category != null && i < category.length; i++) {
			for (int j = 0; series != null && j < series.length; j++) {
				if (value[i][j] != -999999999) {
					dataset.addValue(value[i][j], category[i], series[j]);
				}
			}
		}
		return dataset;
	}

	/**
	 * 返回对比分类图形数据
	 *
	 * @param p0
	 * @param dataset1
	 * @param dataset2
	 */
	private void createCategoryDataset(int p0, DefaultCategoryDataset dataset1,
			DefaultCategoryDataset dataset2) {
		for (int i = 0; category != null && i < category.length; i++) {
			for (int j = 0; series != null && j < series.length; j++) {
				if (i < p0) {
					try {
						if (value[i][j] != -999999999) {
							dataset1.addValue(value[i][j], category[i], series[j]);
						}
					} catch (Exception e) {
					}
				} else {
					try {
						if (value[i][j] != -999999999) {
							dataset2.addValue(value[i][j], category[i], series[j]);
						}
					} catch (Exception e) {
					}
				}
			}
		}
	}

	/**
	 * 返回XY数据类型createXYDataset()
	 *
	 * @return
	 */
	private XYDataset createXYDataset() {
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		for (int i = 0; i < category.length; i++) {
			XYSeries xyseries = new XYSeries(category[i]);
			for (int j = 0; j < xValue[i].length; j++) {
				try {
					if (xValue[i][j] != -999999999) {
						xyseries.add(xValue[i][j], yValue[i][j]);
					}
				} catch (Exception e) {
				}
			}
			xyseriescollection.addSeries(xyseries);
		}
		return xyseriescollection;
	}

	/**
	 * 返回XYZ数据类型createXYZDataset()
	 *
	 * @return
	 */
	private XYZDataset createXYZDataset() {
		XYZDataset dataset = new ChartXYZDataset();
		if (series == null || series.length == 0)
			return null;
		for (int i = 0; series != null && i < series.length; i++) {
			if (series[i] == null)
				series[i] = "未知" + i;
			XYZserie serie = new XYZserie(series[i]);
			for (int j = 0; zValue != null && j < zValue[i].length; j++) {
				if (xValue[i][j] != -999999999) {
					serie.addData(xValue[i][j], yValue[i][j], zValue[i][j]);
				}
			}
			((ChartXYZDataset) dataset).addSeries(serie);
		}
		return dataset;
	}

	/** ***************************通用图形数据集方法结束*************************** */

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public void setXAxis(String xAxis) {
		this.xAxis = xAxis;
	}

	public String getXAxis() {
		return xAxis;
	}

	public void setYAxis(String yAxis) {
		this.yAxis = yAxis;
	}

	public String getYAxis() {
		return yAxis;
	}

	public void setXAxisName(String xAxisName) {
		this.xAxisName = xAxisName;
	}

	public String getXAxisName() {
		return xAxisName;
	}

	/**
	 * 图形的超链接
	 */
	private String lablink = "";

	public String getLablink() {
		return lablink;
	}

	public void setLablink(String lablink) {
		this.lablink = lablink;
	}
}
