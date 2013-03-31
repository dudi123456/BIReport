package com.ailk.bi.subject.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.ailk.bi.subject.util.SubjectConst;

/**
 * @author DXF 当前用户状态域对象 此处放置了一些信息是为了对于不同的表格可以进行动态设置 如果写在常量里全部都一样
 */
@SuppressWarnings({ "rawtypes" })
public class TableCurFunc implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3982283347832944371L;

	/**
	 * 是否扔掉以前扩展的内容,需要判断表格是否由扩展，还要条件是否到了下以层次
	 */
	public boolean throwOldExpandContent = false;

	/**
	 * 收缩后扔掉的行标识
	 */
	public List removedRowIds = new ArrayList();
	/**
	 * 展开后的保存内容链列表,最新展开放在最后
	 */
	public LinkedList expandedContent = new LinkedList();

	/**
	 * 扔掉以后的具体到那个层次
	 */
	public String expandLevel = "";
	/**
	 * 展开后的保存行标识链列表,最新展开放在最后
	 */
	public LinkedList expandedRowIDs = new LinkedList();

	/**
	 * 是否有维度不作为条件
	 */
	public boolean hasDimNotAsWhere = false;

	/**
	 * 为单元格行合并而保存扩展列的临时值
	 */
	public String rowSpanDimValue = null;

	/**
	 * 合并行数计数器
	 */
	public int rowSpanNum = 1;

	/**
	 * 合并行数
	 */
	public int rowspan = 0;

	/**
	 * 是否需要行合并
	 */
	public boolean needRowSpan = false;

	/**
	 * 是否有维度作为列
	 */
	public boolean hasDimCol = false;

	/**
	 * 保存维度作为列的结构体
	 */
	public List tabColDimStructs = null;

	/**
	 * 表格的当前状态
	 */
	public String curUserFunc = SubjectConst.TABLE_FUNC_INIT;

	/**
	 * 排序行
	 */
	public int sortedColum = Integer.MIN_VALUE;

	/**
	 * 当前的排序值，是升序还是降序
	 */
	public String sortOrder = SubjectConst.SORT_ASC;

	/**
	 * 列按升序排序后的图片
	 */
	public String sortAscImgName = "menu_up.gif";

	/**
	 * 排序列的数据类型，数值或字符型
	 */
	public String sortDataType = SubjectConst.DATA_TYPE_NUMBER;

	/**
	 * 列按降序排序显示的图片
	 */
	public String sortDescImgName = "menu_down.gif";

	/**
	 * 列没有进行排序时的图片
	 */
	public String sortGenImgName = "menu_init.gif";

	/**
	 * 展开列的行标识
	 */
	public String expandRwoId = null;

	/**
	 * 折叠时的行标识
	 */
	public String collpaseRowId = null;

	/**
	 * 是否当前状态下禁止折叠和展开了
	 */
	public boolean banExpanded = false;

	/**
	 * 是否有环比或者同比
	 */
	public boolean hasRatio = false;

	/**
	 * 是否有同比
	 */
	public boolean hasLastRaito = false;

	/**
	 * 是否有环比
	 */
	public boolean hasLoopRatio = false;

	/**
	 * 要过虑的列对象标识串，以逗号分隔
	 */
	public String filterIndexs = null;

	/**
	 * 要过虑的层次
	 */
	public String filterLevel = null;

	/**
	 * 用于过虑的百分比值，所有值必须两两成对，且左闭右开
	 */
	public String filterValues = null;

	/**
	 * 过虑内容缓存
	 */
	public List filteredContent = null;

	/**
	 * 第一次过虑，或者是过虑后排序
	 */
	public boolean firstFiltered = false;

	/**
	 * 是否是最大区间过滤
	 */
	public boolean isFilterMax = false;

	/**
	 * 最小区间过滤
	 */
	public boolean isFilterMin = false;

	/**
	 * 表格中是否有显示占比列
	 */
	public boolean hasComRatio = false;

	/**
	 * 是否有查询结果
	 */
	public boolean hasCotent = true;
	
	
	/**
	 * 分页情况下当前要访问的页号
	 */
	public int pageNum=0;

	/**
	 * 数据过虑条件
	 */
	public String dataWhere = null;

	/**
	 * 指标过滤条件
	 */
	public String dataHaving = null;

	/**
	 * 分析型报表的表格的偶数行的HTML样式
	 */
	public String tabEvenTRClass = "table-tr";
	public String tabEvenTRClass2 = "jg";

	/**
	 * 表格的奇数行的HTML样式
	 */
	public String tabOddTRClass = "table-trb";
	public String tabOddTRClass2 = "bgwl";

	/**
	 * 表格的表头行HTML样式
	 */
	public String tabHeadTRClass = "table-th";

	/**
	 * 表格的偶数行非最后列的HTML样式
	 */
	public String tabTDEvenClass = "table-td-withbg";

	/**
	 * 表格的奇数数行非最后列的HTML样式
	 */
	public String tabTDOddClass = "table-tdb-withbg";

	/**
	 * 表格的表头行的非最后列HTML样式
	 */
	public String tabHeadTDClass = "table-item";

	/**
	 * 表格的行的其他HTML定义
	 */
	public String trRest = "";

	/**
	 * 链接样式
	 */
	public String hrefLinkClass = "bule";

	/**
	 * 图片所在路径
	 */
	public String imagePath = "../images/";

	/**
	 * 钻取折叠时图片
	 */
	public String drillCollapseGif = "drill-position-collapse.gif";

	/**
	 * 钻取展开时图片
	 */
	public String drillExpandGif = "drill-position-expand.gif";

	/**
	 * 比率下降变好箭头
	 */
	public String ratioDownGreenGif = "down_green.gif";

	/**
	 * 比率下降变坏箭头
	 */
	public String ratioDownRedGif = "down_red.gif";

	/**
	 * 比率上升变好箭头
	 */
	public String ratioRiseGreenGif = "rise_green.gif";

	/**
	 * 比率上升变坏箭头
	 */
	public String ratioRiseRedGif = "rise_red.gif";

	/**
	 * 比率不变图片
	 */
	public String ratioZeroGif = "ratio_zero.gif";

	/**
	 * 表头链接
	 */
	public String tabHeadTDProHTML = "<a href=\"::link::\"><img " + "src=\""
			+ imagePath + "::img::\" " + "border=\"0\" width=\"9\"></a>";

	/**
	 * 表格的高度
	 */
	public String tableHeight = "300";
	
	/**
	 * 表格是否有底边BAR
	 */
	public boolean withBar=true;
}
