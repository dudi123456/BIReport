package com.ailk.bi.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpSession;

/* 
 import lsbi.base.tools.NullProcFactory;
 import lsbi.subject.common.RowStruct;
 import lsbi.subject.common.SubjectConst;*/

import com.ailk.bi.base.struct.RowStruct;
import com.ailk.bi.base.table.UiCommonDimhierarchyTable;
import com.ailk.bi.base.table.UiCommonTableDefTable;
import com.ailk.bi.base.table.UiCommonTabledictTable;
import com.ailk.bi.base.util.ConstantKeys;
import com.ailk.bi.base.util.TableColUtil;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.Arith;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;

/**
 * 主要生成折叠、展开型表格的工具
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TableUtil {
	/**
	 * 是否禁止整个报表的展开功能
	 */
	private boolean banExpand = false;

	/**
	 * 全部折叠的javascript函数
	 */
	private String collapseAllJSFun = "collapseAll";

	/**
	 * 维度折叠的javascript函数
	 */
	private String collapseJSFun = "collapseDim";

	/**
	 * 代表表格列的列对象
	 */
	private List cols = null;

	/**
	 * 表格的所有层次对象，列标识为 KEY,值为层次对象列表
	 */
	private Map colsLevels = null;

	/**
	 * 按列标识存储的列对象集合,列标识为KEY，值为列对象
	 */
	private Map colsMap = null;

	/**
	 * 缓存上次的结果,存储内容为行对象
	 */
	private List content = null;

	/**
	 * 维度的数量
	 */
	private int dimsCount = 0;

	/**
	 * 钻取折叠时图片
	 */
	private String drill_collapse_gif = "icon_spread.gif";

	/**
	 * 钻取展开时图片
	 */
	private String drill_expand_gif = "icon_accept.gif";

	/**
	 * 全部展开javascript函数
	 */
	private String drillAllJSFun = "drillAll";

	/**
	 * 维度展开的javascript函数
	 */
	private String drillJSFun = "drillDim";

	/**
	 * 分析型报表的表格的偶数行的HTML样式
	 */
	private String evenTRClass = "table-tr";

	/**
	 * 是否全部展开
	 */
	private boolean expandedAll = false;

	/**
	 * 展开后的保存内容链列表,最新展开放在最后
	 */
	private LinkedList expandedContent = new LinkedList();

	/**
	 * 展开后的保存行标识链列表,最新展开放在最后
	 */
	private LinkedList expandedRowIDs = new LinkedList();

	/**
	 * 展开的列标识，从0开始
	 */
	private int expandIndex = Integer.MIN_VALUE;

	/**
	 * 要导出的表格内容，只记录当前情况下内容
	 */
	private List exportHTML = new ArrayList();

	/**
	 * 表格的导出基本定义，包括样式等
	 */
	private String exportTableProperties = "<table id=\"table_content\" width=\"100%\" "
			+ "border=\"1\" cellpadding=\"0\" "
			+ "cellspacing=\"0\" class=\"table\">";

	/**
	 * 要过虑的列对象标识串，以逗号分隔
	 */
	private String filter_indexs = null;

	/**
	 * 要过虑的层次
	 */
	private String filter_level = null;

	/**
	 * 用于过虑的百分比值，所有值必须两两成对，且左闭右开
	 */
	private String filter_values = null;

	/**
	 * 过虑内容缓存
	 */
	private List filteredContent = null;

	/**
	 * 第一次过虑，或者是过虑后排序
	 */
	private boolean firstFiltered = false;

	/**
	 * 表格中是否有显示占比列
	 */
	private boolean hasComRatio = false;

	/**
	 * 是否有查询结果
	 */
	private boolean hasCotent = true;

	/**
	 * 表格高度
	 */
	private String height = "200";

	/**
	 * 链接的样式
	 */
	private String hrefLinkClass = "bule";

	/**
	 * 图片所在路径
	 */
	private String imagePath = "../biimages/";

	/**
	 * 行缩进空格数
	 */
	private String indentSpace = "&nbsp;&nbsp;";

	/**
	 * 是最大的过虑值，用于将右包括
	 */
	private boolean maxFilterValue = false;

	/**
	 * 是最小过虑的单元格，用于对有除法时对分母为0的剔除
	 */
	private boolean minFilterValue = false;

	/**
	 * 是否需要全部展开
	 */
	private boolean needAllExpand = true;

	/**
	 * 非显示的列数，即统计计算用的字段数，主要为样式服务
	 */
	private int notDisplayCols = 0;

	/**
	 * 表格的奇数行的HTML样式
	 */
	private String oddTRClass = "table-trb";

	/**
	 * 排序列序号
	 */
	private int orderIndex = Integer.MIN_VALUE;

	/**
	 * 比率下降变好箭头
	 */
	private String ratioDownGreenGif = "down_green.gif";

	/**
	 * 比率下降变坏箭头
	 */
	private String ratioDownRedGif = "down_red.gif";

	/**
	 * 比率上升变好箭头
	 */
	private String ratioRiseGreenGif = "rise_green.gif";

	/**
	 * 比率上升变坏箭头
	 */
	private String ratioRiseRedGif = "rise_red.gif";

	/**
	 * 比率不变图片
	 */
	private String ratioZeroGif = "ratio_zero.gif";

	/**
	 * 实际的表格列数
	 */
	private int realColNum = 0;

	/**
	 * 标识展开行唯一标识的临时变量
	 */
	private String row_id = null;

	/**
	 * 列按升序排序后的图片
	 */
	private String sortAscImgName = "menu_up.gif";

	/**
	 * 排序列的数据类型，数值或字符型
	 */
	private String sortDataType = ConstantKeys.DATA_TYPE_NUMBER;

	/**
	 * 列按降序排序显示的图片
	 */
	private String sortDescmgName = "menu_down.gif";

	/**
	 * 用户所选择的排序列序号
	 */
	private int sortedColum = Integer.MIN_VALUE;

	/**
	 * 列没有进行排序时的图片
	 */
	private String sortGenImgName = "menu_init.gif";

	/**
	 * 进行排序时的javascript函数
	 */
	private String sortJSFun = "sortCol";

	/**
	 * 当前的排序值，是升序还是降序
	 */
	private String sortOrder = ConstantKeys.SORT_ASC;

	/**
	 * 查询过虑条件
	 */
	private String sqlHaving = "";

	/**
	 * 第一层的所有数据的合计值
	 */
	private String[] sumValues = null;

	/**
	 * 表格的表头行的非最后列HTML样式
	 */
	private String tabHeadTDClass = "table-item";

	/**
	 * 表格的表头行的最后列HTML样式
	 */
	private String tabHeadTDEndClass = "table-item2";

	/**
	 * 表格表头的列具体内容后面的部分
	 */
	private String tabHeadTDProHTML = "<a href=\"javascript:::link::\"><img "
			+ "src=\"" + imagePath + "::img::\" " + "border=\"0\"></a>";

	/**
	 * 表格的表头行HTML样式
	 */
	private String tabHeadTRClass = "table-th";

	/**
	 * 表格的标识
	 */
	private String tableID = null;

	/**
	 * 表格的基本定义，包括样式等
	 */
	private String tableProperties = "<table id=\"table_content\" width=\"100%\" "
			+ "border=\"0\" cellpadding=\"0\" "
			+ "cellspacing=\"0\" class=\"table\">";

	/**
	 * 表格对象
	 */
	private UiCommonTableDefTable tabObj = null;

	/**
	 * 表格的偶数行非最后列的HTML样式
	 */
	private String tabTDEvenClass = "table-td";

	/**
	 * 表格的偶数行最后列的HTML样式
	 */
	private String tabTDEvenEndClass = "table-td2";

	/**
	 * 表格的奇数数行非最后列的HTML样式
	 */
	private String tabTDOddClass = "table-tdb";

	/**
	 * 表格的奇数行最后列的HTML样式
	 */
	private String tabTDOddEndClass = "table-tdb2";

	/**
	 * 表格的行的其他HTML定义
	 */
	private String trRest = "";

	/**
	 * 传进查询条件，如时间,业务类型等
	 */
	private String where = null;

	public void setTableProperties(String tableProperties) {
		this.tableProperties = tableProperties;
	}

	public void setTabObj(UiCommonTableDefTable tabObj) {
		this.tabObj = tabObj;
	}

	public void setTabTDEvenClass(String tabTDEvenClass) {
		this.tabTDEvenClass = tabTDEvenClass;
	}

	public void setTabTDEvenEndClass(String tabTDEvenEndClass) {
		this.tabTDEvenEndClass = tabTDEvenEndClass;
	}

	public void setTabTDOddClass(String tabTDOddClass) {
		this.tabTDOddClass = tabTDOddClass;
	}

	public void setTabTDOddEndClass(String tabTDOddEndClass) {
		this.tabTDOddEndClass = tabTDOddEndClass;
	}

	public void setTrRest(String trRest) {
		this.trRest = trRest;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public List getContent() {
		return content;
	}

	public int getDimsCount() {
		return dimsCount;
	}

	public String getDrill_collapse_gif() {
		return drill_collapse_gif;
	}

	public String getDrill_expand_gif() {
		return drill_expand_gif;
	}

	public String getDrillAllJSFun() {
		return drillAllJSFun;
	}

	public String getDrillJSFun() {
		return drillJSFun;
	}

	public String getEvenTRClass() {
		return evenTRClass;
	}

	public int getExpandIndex() {
		return expandIndex;
	}

	public List getExportHTML() {
		return exportHTML;
	}

	public String getFilter_indexs() {
		return filter_indexs;
	}

	public String getFilter_level() {
		return filter_level;
	}

	public String getFilter_values() {
		return filter_values;
	}

	public String getCollapseJSFun() {
		return collapseJSFun;
	}

	public List getCols() {
		return cols;
	}

	public Map getColsLevels() {
		return colsLevels;
	}

	public Map getColsMap() {
		return colsMap;
	}

	public String getTableID() {
		return tableID;
	}

	public String getTableProperties() {
		return tableProperties;
	}

	public UiCommonTableDefTable getTabObj() {
		return tabObj;
	}

	public String getTabTDEvenClass() {
		return tabTDEvenClass;
	}

	public String getTabTDEvenEndClass() {
		return tabTDEvenEndClass;
	}

	public String getTabTDOddClass() {
		return tabTDOddClass;
	}

	public String getTabTDOddEndClass() {
		return tabTDOddEndClass;
	}

	public String getTrRest() {
		return trRest;
	}

	public String getWhere() {
		return where;
	}

	public boolean isBanExpand() {
		return banExpand;
	}

	public boolean isFirstFiltered() {
		return firstFiltered;
	}

	public boolean isHasCotent() {
		return hasCotent;
	}

	public boolean isMaxFilterValue() {
		return maxFilterValue;
	}

	public boolean isMinFilterValue() {
		return minFilterValue;
	}

	public boolean isNeedAllExpand() {
		return needAllExpand;
	}

	public void setBanExpand(boolean banExpand) {
		this.banExpand = banExpand;
	}

	public void setCollapseJSFun(String collapseJSFun) {
		this.collapseJSFun = collapseJSFun;
	}

	public void setCols(List cols) {
		this.cols = cols;
	}

	public void setColsLevels(Map colsLevels) {
		this.colsLevels = colsLevels;
	}

	public void setColsMap(Map colsMap) {
		this.colsMap = colsMap;
	}

	public void setContent(List content) {
		this.content = content;
	}

	public void setDimsCount(int dimsCount) {
		this.dimsCount = dimsCount;
	}

	public void setDrill_collapse_gif(String drill_collapse_gif) {
		this.drill_collapse_gif = drill_collapse_gif;
	}

	public void setDrill_expand_gif(String drill_expand_gif) {
		this.drill_expand_gif = drill_expand_gif;
	}

	public void setDrillAllJSFun(String drillAllJSFun) {
		this.drillAllJSFun = drillAllJSFun;
	}

	public void setDrillJSFun(String drillJSFun) {
		this.drillJSFun = drillJSFun;
	}

	public void setEvenTRClass(String evenTRClass) {
		this.evenTRClass = evenTRClass;
	}

	public void setExpandIndex(int expandIndex) {
		this.expandIndex = expandIndex;
	}

	public void setExportHTML(List exportHTML) {
		this.exportHTML = exportHTML;
	}

	public void setFilter_indexs(String filter_indexs) {
		this.filter_indexs = filter_indexs;
	}

	public void setFilter_level(String filter_level) {
		this.filter_level = filter_level;
	}

	public void setFilter_values(String filter_values) {
		this.filter_values = filter_values;
	}

	public void setFirstFiltered(boolean firstFiltered) {
		this.firstFiltered = firstFiltered;
	}

	public void setHasCotent(boolean hasCotent) {
		this.hasCotent = hasCotent;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setHrefLinkClass(String hrefLinkClass) {
		this.hrefLinkClass = hrefLinkClass;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public void setIndentSpace(String indentSpace) {
		this.indentSpace = indentSpace;
	}

	public void setMaxFilterValue(boolean maxFilterValue) {
		this.maxFilterValue = maxFilterValue;
	}

	public void setMinFilterValue(boolean minFilterValue) {
		this.minFilterValue = minFilterValue;
	}

	public void setNeedAllExpand(boolean needAllExpand) {
		this.needAllExpand = needAllExpand;
	}

	public void setOddTRClass(String oddTRClass) {
		this.oddTRClass = oddTRClass;
	}

	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}

	public void setRatioDownGreenGif(String ratioDownGreenGif) {
		this.ratioDownGreenGif = ratioDownGreenGif;
	}

	public void setRatioDownRedGif(String ratioDownRedGif) {
		this.ratioDownRedGif = ratioDownRedGif;
	}

	public void setRatioRiseGreenGif(String ratioRiseGreenGif) {
		this.ratioRiseGreenGif = ratioRiseGreenGif;
	}

	public void setRatioRiseRedGif(String ratioRiseRedGif) {
		this.ratioRiseRedGif = ratioRiseRedGif;
	}

	public void setRow_id(String row_id) {
		this.row_id = row_id;
	}

	public void setSortAscImgName(String sortAscImgName) {
		this.sortAscImgName = sortAscImgName;
	}

	public void setSortDataType(String sortDataType) {
		this.sortDataType = sortDataType;
	}

	public void setSortDescmgName(String sortDescmgName) {
		this.sortDescmgName = sortDescmgName;
	}

	public void setSortedColum(int sortedColum) {
		this.sortedColum = sortedColum;
	}

	public void setSortGenImgName(String sortGenImgName) {
		this.sortGenImgName = sortGenImgName;
	}

	public void setSortJSFun(String sortJSFun) {
		this.sortJSFun = sortJSFun;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public void setSqlHaving(String sqlHaving) {
		this.sqlHaving = sqlHaving;
	}

	public void setTabHeadTDClass(String tabHeadTDClass) {
		this.tabHeadTDClass = tabHeadTDClass;
	}

	public void setTabHeadTDEndClass(String tabHeadTDEndClass) {
		this.tabHeadTDEndClass = tabHeadTDEndClass;
	}

	public void setTabHeadTDProHTML(String tabHeadTDProHTML) {
		this.tabHeadTDProHTML = tabHeadTDProHTML;
	}

	public void setTabHeadTRClass(String tabHeadTRClass) {
		this.tabHeadTRClass = tabHeadTRClass;
	}

	/**
	 * 将新查询的数组，变成行对象后， 放入到正确的位置处
	 * 
	 * @param content
	 *            已有的行对象集合
	 * @param svces
	 *            新查询出来的数组
	 * @return 放入正确位置后的行对象集合
	 */
	private List assembleArray(List content, String[][] svces) {
		List ret = null;
		if (null != svces) {
			// 如果查询数组不为空
			// 转换为行对象
			List insert = convertToRowStructs(svces);
			if (Integer.MIN_VALUE != expandIndex) {
				// 是展开情况，将这些行对象放入到缓存链表
				expandedContent.addLast(insert);
				expandedRowIDs.add(row_id);
			}
			// 如果是第一次查询，直接返回查询结果
			if (null == content) {
				ret = insert;
			} else {
				// 声明新的列表对象
				ret = new ArrayList();
				// 开始循环原来的行对象集合
				Iterator iter = content.iterator();
				while (iter.hasNext()) {
					// 取出行对象
					RowStruct rowObj = (RowStruct) iter.next();
					if (null != rowObj && null != rowObj.row_id
							&& null != row_id && row_id.equals(rowObj.row_id)) {
						// 如果是从该行展开的，先加入该行对象
						ret.add(rowObj);
						if (hasComRatio) {
							insert = genSubComRatio(insert, rowObj);
						}
						// 然后加入所有展开后的行对象
						ret.addAll(insert);
					} else {
						// 对于其他行对象，也加入
						ret.add(rowObj);
					}
				}
			}
		} else {
			// 如果数组为空，直接返回原来的
			ret = content;
		}
		return ret;
	}

	/**
	 * 将缓存的展开链表的最后一个行对象集合 排序后正确插入到原来的位置处
	 * 
	 * @return 插入后的行对象集合
	 */
	private List assembleList() {
		List ret = null;
		if (null != expandedContent) {
			// 如果缓存的链表不为空
			if (null == content) {
				// 如果要插入的集合为空,直接取缓存的最后一个
				ret = (List) expandedContent.getLast();
			} else {
				// 插入额集合不为空
				List tmpCon = new ArrayList();
				// 循环插入集合
				Iterator iter = content.iterator();
				// 获取被插入集合
				List tempList = (List) expandedContent.getLast();
				// 获取插入位置的行的唯一标识
				String temp_row_id = (String) expandedRowIDs.getLast();
				// 要插入的行对象数目
				int count = tempList.size();
				// 行的序号
				int index = -1;
				// 插入位置处
				int from = -1;
				// 去除原来的行对象
				while (iter.hasNext()) {
					index++;
					// 获取行对象
					RowStruct rowObj = (RowStruct) iter.next();
					if (null != rowObj && null != rowObj.row_id
							&& null != temp_row_id
							&& temp_row_id.equals(rowObj.row_id)) {
						// 判断是否是插入位置处，是
						// 将源行对象插入
						tmpCon.add(rowObj);
						// 设置插入位置
						from = index;
					} else if (from == -1 || index > (from + count)) {
						// 如果不是插入位置，则添加进，先将插入处的行对象去掉
						tmpCon.add(rowObj);
					}
				}
				ret = new ArrayList();
				iter = tmpCon.iterator();
				// 插入新的排序后的对象
				while (iter.hasNext()) {
					RowStruct rowObj = (RowStruct) iter.next();
					if (null != rowObj && null != rowObj.row_id
							&& null != temp_row_id
							&& temp_row_id.equals(rowObj.row_id)) {
						// 判断是否是插入位置处，是
						// 将源行对象插入
						ret.add(rowObj);
						// 插入排序后的行对象
						ret.addAll(tempList);
					} else {
						// 插入其他对象
						ret.add(rowObj);
					}
				}
			}
		} else {
			// 如果要插入的对象为空，直接返回原来的
			ret = content;
		}
		return ret;
	}

	/**
	 * 计算带括号的计算公式
	 * 
	 * @param calEQ
	 *            计算公式
	 * @param sumValues
	 *            指标合计值
	 * @return 计算后的值
	 */
	private String calComEQ(String calEQ, String[] sumValues) {
		String result = null;
		if (null != calEQ && null != sumValues) {
			String sb = calEQ;
			int pos = sb.lastIndexOf("(");
			while (pos >= 0) {
				int end = sb.indexOf(")", pos);
				if (end >= 0) {
					String temp = sb.substring(pos + 1, end);
					String value = calSimpleEQ(temp, sumValues);
					// sb = sb.replace("(" + temp + ")", value);
					sb = StringB.replace(sb, "(" + temp + ")", value);
					pos = sb.lastIndexOf("(");
				} else {
					break;
				}
			}
			result = sb;
		}
		return result;
	}

	/**
	 * 计算不带括号的公式的值
	 * 
	 * @param calEQ
	 *            公式
	 * @param sumValues
	 *            合计值
	 * @return 计算后的值
	 */
	private String calSimpleEQ(String calEQ, String[] sumValues) {
		String result = null;
		if (null != calEQ && null != sumValues) {
			String sb = calEQ.toUpperCase();
			// 开始扫描,这里不能直接使用indexOf方法，要不不会有优先顺序
			// 先乘除
			int index = 0;
			while (index < sb.length()) {
				char temp_char = sb.charAt(index);
				if (temp_char == '*' || temp_char == '/') {
					// 找到乘或除
					// 取出两个因子
					String first = sb.substring(0, index);
					String second = sb.substring(index + 1);
					String firstValue = "";
					String secondValue = "";
					int pos = -1;
					// 先看看前面部分是否有加号或者减号
					pos = first.lastIndexOf("+");
					if (pos >= 0) {
						// 缩小范围
						first = first.substring(pos + 1);
					}
					// 再看看有没有减号
					pos = first.lastIndexOf("-");
					if (pos >= 0) {
						// 缩小范围
						first = first.substring(pos + 1);
					}
					// 这是应该是该因子了
					// 判断是数值还是指标变量
					String firstPart = first;
					pos = first.lastIndexOf("::MSU");
					if (pos >= 0) {
						first = first.substring(pos + "::MSU".length());
						pos = first.indexOf("::");
						if (pos >= 0) {
							first = first.substring(0, pos);
							firstValue = getSumColValue(first, sumValues);
						}
					} else {
						firstValue = first;
					}
					// 后面部分
					pos = second.indexOf("+");
					if (pos >= 0) {
						second = second.substring(0, pos);
					}
					pos = second.indexOf("-");
					if (pos >= 0) {
						second = second.substring(0, pos);
					}
					pos = second.indexOf("*");
					if (pos >= 0) {
						second = second.substring(0, pos);
					}
					pos = second.indexOf("/");
					if (pos >= 0) {
						second = second.substring(0, pos);
					}
					String secondPart = second;
					pos = second.indexOf("::MSU");
					if (pos >= 0) {
						second = second.substring(pos + "::MSU".length());
						pos = second.indexOf("::");
						if (pos >= 0) {
							second = second.substring(0, pos);
							secondValue = getSumColValue(second, sumValues);
						}
					} else {
						secondValue = second;
					}
					// 至此已经获得了两个列对象得列标识
					// 取值，进行计算
					String replaced = "";
					if (firstPart.indexOf("::MSU") < 0) {
						replaced += first;
					} else {
						replaced += "::MSU" + first + "::";
					}
					replaced += temp_char;
					if (secondPart.indexOf("::MSU") < 0) {
						replaced += second;
					} else {
						replaced += "::MSU" + second + "::";
					}
					String replace = "";
					if (temp_char == '*') {
						replace = Arith.mul(firstValue, secondValue, 4);
					}
					if (temp_char == '/') {
						replace = NullProcFactory.transNullToZero(Arith.divs(
								firstValue, secondValue, 4));
						if (null == replace)
							replace = ConstantKeys.ZERO;
					}
					// sb = sb.replace(replaced, replace);
					sb = StringB.replace(sb, replaced, replace);
					// 从新开始扫描
					index = -1;
				}
				index++;
			}
			index = 0;
			while (index < sb.length()) {
				char temp_char = sb.charAt(index);
				if (temp_char == '+' || temp_char == '-') {
					// 找到乘或除
					// 取出两个因子
					String first = sb.substring(0, index);
					if ("".equals(first)) {
						index++;
						continue;
					}
					String second = sb.substring(index + 1);
					String firstValue = "";
					String secondValue = "";
					int pos = -1;
					// 先看看前面部分是否有加号或者减号
					pos = first.lastIndexOf("+");
					if (pos >= 0) {
						// 缩小范围
						first = first.substring(pos + 1);
					}
					// 再看看有没有减号
					pos = first.lastIndexOf("-");
					if (pos >= 0) {
						// 缩小范围
						first = first.substring(pos + 1);
					}
					// 这是应该是该因子了
					// 判断是数值还是指标变量
					pos = first.lastIndexOf("::MSU");
					if (pos >= 0) {
						first = first.substring(pos + "::MSU".length());
						pos = first.indexOf("::");
						if (pos >= 0) {
							first = first.substring(0, pos);
							firstValue = getSumColValue(first, sumValues);
						}
					} else {
						firstValue = first;
					}
					// 后面部分
					pos = second.indexOf("+");
					if (pos >= 0) {
						second = second.substring(0, pos);
					}
					pos = second.indexOf("-");
					if (pos >= 0) {
						second = second.substring(0, pos);
					}

					pos = second.indexOf("::MSU");
					if (pos >= 0) {
						second = second.substring(pos + "::MSU".length());
						pos = second.indexOf("::");
						if (pos >= 0) {
							second = second.substring(0, pos);
							secondValue = getSumColValue(second, sumValues);
						}
					} else {
						secondValue = second;
					}
					// 至此已经获得了两个列对象得列标识
					// 取值，进行计算
					String replaced = "";
					if (first.equals(firstValue)) {
						replaced += first;
					} else {
						replaced += "::MSU" + first + "::";
					}
					replaced += temp_char;
					if (second.equals(secondValue)) {
						replaced += second;
					} else {
						replaced += "::MSU" + second + "::";
					}
					String replace = "";
					if (temp_char == '+') {
						replace = NullProcFactory.transNullToZero(Arith.add(
								firstValue, secondValue));
					}
					if (temp_char == '-') {
						replace = NullProcFactory.transNullToZero(Arith.sub(
								firstValue, secondValue));
					}
					// sb = sb.replace(replaced, replace);
					sb = StringB.replace(sb, replaced, replace);
					// 从新开始扫描
					index = -1;
				}
				index++;
			}
			result = sb;
		}
		return result;
	}

	/**
	 * 清除本类的一些临时变量为初始值
	 */
	private void clearTempValue() {
		// 展开列序号
		expandIndex = Integer.MIN_VALUE;
		// 展开的唯一行标识
		row_id = null;
		try {
			// 去除最后一次的缓存
			expandedContent.removeLast();
			// 去除最后的行的缓存
			expandedRowIDs.removeLast();
			sortOrder = null;
			sortedColum = Integer.MIN_VALUE;
		} catch (NoSuchElementException nee) {
		}
	}

	/**
	 * 将列表中的对象进行克隆
	 * 
	 * @param colsObj
	 *            要克隆的列表
	 * @return 克隆后的列表
	 */
	private List cloneListColsObj(List colsObj) {
		List ret = null;
		if (null != colsObj) {
			ret = new ArrayList();
			Iterator iter = colsObj.iterator();
			while (iter.hasNext()) {
				UiCommonTabledictTable intiColObj = (UiCommonTabledictTable) iter
						.next();
				UiCommonTabledictTable colObj = (UiCommonTabledictTable) intiColObj
						.clone();
				ret.add(colObj);
			}
		}
		return ret;
	}

	/**
	 * 将所有展开全部折叠起来，恢复到最初始状态
	 * 
	 * @param expandIndex
	 */
	public void collapseAll(String expandIndex, HttpSession session) {
		if (null != expandIndex) {
			// 将标志设置为假
			expandedAll = false;
			// 禁止展开标志设置为假
			banExpand = false;
			// 缓存内容清空
			content = null;
			// 重新设置表格标识，以便重置列对象状态
			resetCols();
			// 产生默认内容
			genDefalutContent(session);
		}
	}

	/**
	 * 将指定的列标识，从指定的行处折叠
	 * 
	 * @param expandIndex
	 *            要折叠的列标识
	 * @param row_id
	 *            要折叠的行标识
	 */
	public void collapseTable(String expandIndex, String row_id) {
		if (null != expandIndex && null != row_id) {
			// 如果这两个标识不为空
			try {
				// 设置类的展开列标识
				this.expandIndex = Integer.parseInt(expandIndex);
				// 设置缓存行标识
				this.row_id = row_id;
				// 定位这个行，取出行对象，取出层次水平，然后删除直到通层次水平的出现
				if (null != content) {
					int index = -1;
					int from = -1;
					List temp = new ArrayList();
					RowStruct fromObj = null;
					Iterator iter = content.iterator();
					// 循环所有行对象
					while (iter.hasNext()) {
						index++;
						RowStruct rowObj = (RowStruct) iter.next();
						// 定位到该行先
						if (row_id.equalsIgnoreCase(rowObj.row_id)) {
							from = index + 1;
							fromObj = rowObj;
							rowObj.expanded = false;
						}
						// 判断找到的位置,以及大于这个层次的对象
						if (from != -1 && index >= from
								&& rowObj.expand_index == this.expandIndex
								&& rowObj.level > fromObj.level) {
							// 该行下面的所有大于本层的行对象去掉
						} else {
							// 其他对象加上
							temp.add(rowObj);
						}
						if (from != -1 && index >= from
								&& rowObj.level <= fromObj.level) {
							// 到了其他层次对象处
							from = -1;
						}
					}
					// 先清除缓存
					content.clear();
					// 缓存折叠后的行对象
					content.addAll(temp);
				}
			} catch (NumberFormatException nfe) {
			}
			// 清空一些临时变量
			clearTempValue();
		}
	}

	/**
	 * 将二维数组转换为行对象
	 * 
	 * @param svces
	 *            要转换的二维数组
	 */
	private List convertToRowStructs(String[][] svces) {
		List rows = null;
		if (null != svces) {
			// 如果数组不为空
			rows = new ArrayList();
			for (int i = 0; i < svces.length; i++) {
				// 开始循环
				RowStruct rowObj = new RowStruct();
				// 行对象的列对象
				List colObjs = new ArrayList();
				colObjs.clear();
				// 行的个列值
				String[] tmp = new String[svces[i].length];
				// 复制列值
				System.arraycopy(svces[i], 0, tmp, 0, svces[i].length);
				// 设置行对象的列值
				rowObj.cols_value = tmp;
				// 设置行对象的展开列为缓存的列标识
				rowObj.expand_index = expandIndex;
				// 生成唯一行标识
				StringBuffer row_id = new StringBuffer("");
				if (null == rowObj.row_id) {
					// 重新生成所有维度的值，每个维度间用
					Iterator iter = cols.iterator();
					int index = -1;
					int dims_count = 0;
					while (iter.hasNext()) {
						index++;
						UiCommonTabledictTable initColObj = (UiCommonTabledictTable) iter
								.next();
						// 克隆对象
						UiCommonTabledictTable colObj = (UiCommonTabledictTable) initColObj
								.clone();
						if (null != colObj
								&& ConstantKeys.NO
										.equalsIgnoreCase(colObj.is_measure)) {
							dims_count++;
							// 主要考虑维度
							if (index == expandIndex) {
								String level = colObj.level;
								try {
									int lev = Integer.parseInt(level);
									// 设置行对象的层次水平
									rowObj.level = lev;
								} catch (NumberFormatException nfe) {
								}
							}
							// 生成唯一标识,唯一标识为维度编码字段的组合和层次的组合
							int real_index = index + dims_count - 1;
							row_id.append(tmp[real_index]).append("_")
									.append(colObj.level);
							if (expandedAll
									&& ConstantKeys.YES
											.equalsIgnoreCase(colObj.isExpandCol)) {
								// 全部展开了
								List levels = null;
								if (null != colsLevels)
									levels = (List) colsLevels
											.get(colObj.col_id);
								if (null != levels) {
									Iterator temp_iter = levels.iterator();
									int tmp_index = real_index;
									while (temp_iter.hasNext()) {
										UiCommonDimhierarchyTable levObj = (UiCommonDimhierarchyTable) temp_iter
												.next();
										tmp_index++;
										tmp_index++;
										row_id.append("_" + levObj.lev_id + "_"
												+ tmp[tmp_index]);
									}
								}
							}

							// 如果列从没有钻取过
							if (!colObj.neverDrilled) {
								// 设置列对象的值
								if (null == colObj.value)
									colObj.value = new ArrayList();
								colObj.value.add(tmp[real_index]);
								row_id.append("_" + colObj.value.toString());
							}
							row_id.append("_");
							row_id.append(ConstantKeys.MAX_SPLIT);
						}
						colObjs.add(index, colObj);
					}
					rowObj.row_id = row_id.toString();
					rowObj.colObjs = colObjs;
				}
				rows.add(i, rowObj);
			}
		}

		return rows;
	}

	/**
	 * 将查询的查询条件转换为URL模式
	 * 
	 * @return 返回链接串
	 */
	private String convertWhereToUrlMode() {
		String ret = "";
		if (null != where && !"".equals(where)) {
			ret = where;
			if (ret.indexOf("a.") >= 0) {
				// 替换掉所有的A.
				ret = ret.replaceAll("a\\.", "");
			}
			if (ret.indexOf("A.") >= 0) {
				// 替换掉所有的A.
				ret = ret.replaceAll("A\\.", "");
			}
			if (ret.indexOf("and") >= 0) {
				ret = ret.replaceAll("and", "&");
			}
			ret = ret.replaceAll(" ", "");
			ret = ret.replaceAll("'", "");
		}
		return ret;
	}

	/**
	 * 将表格全部展开，则其下的素有层次将转换为列显示
	 * 
	 * @param expandIndex
	 *            要展开的列标识
	 */
	public void expandAll(String expandIndex, HttpSession session) {
		if (null != expandIndex) {
			// 设置全部展开标志为真
			expandedAll = true;
			// 设置禁止展开标志为真，因为这是已经全部展开了
			banExpand = true;
			this.expandIndex = Integer.MIN_VALUE;
			expandedContent.clear();
			content = null;
			genDefalutContent(session);
			/*
			 * try { // 生成查询语句 String sql = genSQL(); System.out.println(sql);
			 * if (null != sql) { long start = System.currentTimeMillis(); //
			 * 开始查询 String[][] svces = WebDBUtil.execQryArray(sql, "");
			 * System.out.println("数据库查询用时:" + (System.currentTimeMillis() -
			 * start) + "ms"); if (null != svces && null != cols) { //
			 * 查询结果不为空，且缓存列对象集合不为空 // 清空内容缓存 content = null; // 组装查询结果 List temp
			 * = assembleArray(content, svces); if (null != content) {
			 * content.clear(); } else { content = new ArrayList(); }
			 * content.addAll(temp); } } } catch (AppException ae) {
			 * ae.printStackTrace(); }
			 */}
	}

	/**
	 * 
	 * 展开指定的列和指定的行
	 * 
	 * @param expandIndex
	 *            要展开的列标识
	 * @param row_id
	 *            要展开的行标识
	 */
	public void expandTable(String expandIndex, String row_id,
			HttpSession session) {
		// 要设置所有的列对象的状态
		if (null != expandIndex && null != row_id) {
			// 如果参数不为空
			try {
				// 设置缓存的列标识和行标识
				this.expandIndex = Integer.parseInt(expandIndex);
				this.row_id = row_id;
				int index = -1;
				if (null != content) {
					index++;
					// 将该行的列对象状态取出来，并放到全局中
					Iterator iter = content.iterator();
					while (iter.hasNext()) {
						RowStruct rowObj = (RowStruct) iter.next();
						if (null != rowObj && null != rowObj.colObjs
								&& null != rowObj.row_id
								&& row_id.equals(rowObj.row_id)) {
							// 找到展开行对象
							rowObj.expanded = true;
							if (null == cols) {
								cols = new ArrayList();
							}
							// 清空缓存的列对象
							cols.clear();
							// 克隆了对象
							cols.addAll(cloneListColsObj(rowObj.colObjs));
							break;
						}
					}
				}
				if (null != cols) {
					// 设置展开列为下一层次
					Iterator iter = cols.iterator();
					index = -1;
					while (iter.hasNext()) {
						index++;
						UiCommonTabledictTable colObj = (UiCommonTabledictTable) iter
								.next();
						if (index == this.expandIndex) {
							// 找到要展开的列对象
							if (colObj.neverDrilled) {
								// 如果该列对象还没有展开过，不动
							} else {
								// 如果已经展开过
								// 取出所有层次
								List levels = null;
								if (null != colsLevels)
									levels = (List) colsLevels
											.get(colObj.col_id);
								// 获取下一层此对象
								UiCommonDimhierarchyTable nextLevObj = TableColUtil
										.getNextLevelObj(levels, colObj.level);
								if (null != nextLevObj) {
									// 设置为新的层次
									colObj.level = nextLevObj.lev_id;
									// 可以继续展开
									colObj.canDrill = true;
								} else {
									// 不能展开了
									colObj.canDrill = false;
								}
							}
							break;
						}
					}
				}
			} catch (NumberFormatException nfe) {

			}
			genDefalutContent(session);
		}
	}

	/**
	 * 看值是否满足过虑条件
	 * 
	 * @param index
	 *            第几个过虑条件
	 * @param value
	 *            被过虑的值
	 * @param filter_values
	 *            过虑条件
	 * @return
	 */
	private boolean fitFilterCon(int index, String value, String[] filter_values) {
		boolean fitted = false;
		if (null != value && null != filter_values) {
			int low = 2 * index;
			int high = low + 1;
			String low_value = filter_values[low];
			String high_value = filter_values[high];
			try {
				int pos = low_value.indexOf(">=");
				if (pos >= 0) {
					low_value = low_value.substring(pos + 2);
				}
				pos = low_value.indexOf(">");
				if (pos >= 0) {
					low_value = low_value.substring(pos + 1);
				}
				boolean hasEqual = false;
				pos = high_value.indexOf("<=");
				if (pos >= 0) {
					high_value = high_value.substring(pos + 2);
					hasEqual = true;
				}
				pos = high_value.indexOf("<");
				if (pos >= 0) {
					high_value = high_value.substring(pos + 1);
				}
				double ld = Double.parseDouble(low_value);
				double hd = Double.parseDouble(high_value);
				double dv = Double.parseDouble(value);
				if (hasEqual) {
					if (dv >= ld && dv <= hd) {
						fitted = true;
					} else {
						fitted = false;
					}
				} else {
					if (dv >= ld && dv < hd) {
						fitted = true;
					} else {
						fitted = false;
					}
				}
				if (isMinFilterValue()
						&& value.equalsIgnoreCase(Integer.MIN_VALUE + "")) {
					fitted = false;
				}
				if (isMaxFilterValue()
						&& value.equalsIgnoreCase(Integer.MAX_VALUE + "")) {
					fitted = true;
				}
			} catch (NumberFormatException nfe) {
				fitted = false;
			}
		}
		return fitted;
	}

	/**
	 * 根据列对象定义格式化单元格值
	 * 
	 * @param colObj
	 *            列对象
	 * @param value
	 *            要格式化的值
	 * @return 格式化后的值
	 */
	private String formatColValue(UiCommonTabledictTable colObj, String value) {
		String retValue = null;
		if (null != colObj && null != value) {
			if (ConstantKeys.NO.equalsIgnoreCase(colObj.is_ratio)) {
				// 二者皆不为空
				int fraction_num = 2;// 设置默认小数位数为2
				try {
					// 获取列对象定义的小数位数
					fraction_num = Integer.parseInt(colObj.digit_length);
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
				}
				// 按小数位数格式化数值
				retValue = FormatUtil.formatStr(value, fraction_num, true);
			} else {
				retValue = FormatUtil.formatPercent(value, 2, true);
			}
		}
		return retValue;
	}

	/**
	 * 生成全部折叠的链接
	 * 
	 * @param index
	 *            要折叠的列标识
	 * @return 链接字符串
	 */
	private String genAllCollapseLink(int index) {
		String link = null;
		if (index >= 0) {
			StringBuffer sb = new StringBuffer("");
			// 参数变量，先将要展开的参数加上
			StringBuffer params = new StringBuffer("");
			params.append("'").append(tableID);
			params.append("',");
			params.append("'").append(index);
			params.append("'");
			sb.append("<a href=\"javascript:").append(collapseAllJSFun);
			sb.append("(").append(params).append(")\">")
					.append(genHtmlImg(drill_collapse_gif))
					.append("</a>&nbsp;");
			link = sb.toString();
		}
		return link;
	}

	/**
	 * 生成全部展开的链接
	 * 
	 * @param index
	 *            要展开的列标识
	 * @return 展开链接
	 */
	private String genAllExpandLink(int index) {
		String link = null;
		if (index >= 0) {
			StringBuffer sb = new StringBuffer("");
			// 参数变量，先将要展开的参数加上
			StringBuffer params = new StringBuffer("");
			params.append("'").append(tableID);
			params.append("',");
			params.append("'").append(index);
			params.append("'");
			sb.append("<a href=\"javascript:").append(drillAllJSFun);
			sb.append("(").append(params).append(")\">")
					.append(genHtmlImg(drill_expand_gif)).append("</a>&nbsp;");
			link = sb.toString();
		}
		return link;
	}

	/**
	 * 生成计算型指标的汇总计算值
	 * 
	 * @param colObj
	 *            列对象
	 * @param sumValues
	 *            指标合计值
	 * @return
	 */
	private String genCalMsuSumValue(UiCommonTabledictTable colObj,
			String[] sumValues) {
		String html = "";
		if (null != colObj && null != sumValues) {
			if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_calByOther)) {
				// 取出计算公式
				String calCMD = colObj.calObj.cal_cmd;
				// 判断是否带有括号
				int pos = calCMD.indexOf("(");
				if (pos >= 0) {
					// 有括号，先从最里层开始计算
					html = formatColValue(colObj, calComEQ(calCMD, sumValues));
				} else {
					// 没有括号
					// 从左至右扫描,找出成或除，然后加和减
					html = formatColValue(colObj,
							calSimpleEQ(calCMD, sumValues));
				}
			}
		}
		return html;
	}

	/**
	 * 生成由第一层到相关层的条件
	 * 
	 * @param colObj
	 * @param virDotTabName
	 * @param levels
	 * @return
	 */
	private String genChartColSearchWhere(RowStruct rowObj, String col_id,
			String virDotTabName, List levels) {
		String where = null;
		if (null != rowObj && null != col_id) {
			UiCommonTabledictTable colObj = null;
			List tmpList = rowObj.colObjs;
			Iterator iter = tmpList.iterator();
			while (iter.hasNext()) {
				UiCommonTabledictTable tmpObj = (UiCommonTabledictTable) iter
						.next();
				if (tmpObj.col_id.equals(col_id)) {
					colObj = tmpObj;
					break;
				}
			}
			if (null != colObj) {
				StringBuffer WHERE = new StringBuffer("");
				Iterator tmp_iter = colObj.value.iterator();
				int lev_index = -1;
				while (tmp_iter.hasNext()) {
					// 开始循环,取每一层的的代码
					lev_index++;
					String value = (String) tmp_iter.next();
					if (lev_index == 0) {
						WHERE.append("&").append(virDotTabName)
								.append(colObj.code_field.toLowerCase());
						WHERE.append("=");
						WHERE.append(value);
					} else {
						Iterator lev_iter = levels.iterator();
						UiCommonDimhierarchyTable tmp_levObj = null;
						int val_index = 0;
						while (lev_iter.hasNext()) {
							val_index++;
							tmp_levObj = (UiCommonDimhierarchyTable) lev_iter
									.next();
							if (val_index == lev_index) {
								break;
							}
						}
						if (null != tmp_levObj) {
							WHERE.append("&").append(virDotTabName)
									.append(tmp_levObj.src_idfld.toLowerCase());
							WHERE.append("=");
							WHERE.append(value);
						}
					}
				}
				where = WHERE.toString();
			}
		}
		return where;
	}

	/**
	 * 生成图形所用的JavaScript函数
	 * 
	 * @return
	 */
	private String genChartJSHtml() {
		StringBuffer html = new StringBuffer("");
		html.append("<script type='text/javascript'>\n");
		// html.append("setParentHeight();\n");
		html.append("window.status=\"完毕\";\n");
		html.append("</script>\n");
		return html.toString();
	}

	/**
	 * 生成折叠的链接
	 * 
	 * @param index
	 *            要折叠的列标识
	 * @param row_id
	 *            要折叠的行标识
	 * @return 生成的链接
	 */
	private String genCollapseLink(int index, String row_id) {
		String link = null;
		if (index >= 0 && null != row_id) {
			StringBuffer sb = new StringBuffer("");
			// 参数变量，先将要展开的参数加上
			StringBuffer params = new StringBuffer("");
			params.append("'").append(tableID);
			params.append("','").append(index);
			params.append("','").append(row_id).append("'");
			sb.append("<a href=\"javascript:").append(collapseJSFun);
			sb.append("(").append(params).append(")\">")
					.append(genHtmlImg(drill_collapse_gif))
					.append("</a>&nbsp;");
			link = sb.toString();
		}
		return link;
	}

	/**
	 * 生成由第一层到相关层的条件
	 * 
	 * @param colObj
	 * @param virDotTabName
	 * @param levels
	 * @return
	 */
	private String genColSearchWhere(UiCommonTabledictTable colObj,
			String virDotTabName, List levels) {
		String where = null;
		if (null != colObj) {
			StringBuffer WHERE = new StringBuffer("");
			Iterator tmp_iter = colObj.value.iterator();
			int lev_index = -1;
			while (tmp_iter.hasNext()) {
				// 开始循环,取每一层的的代码
				lev_index++;
				String value = (String) tmp_iter.next();
				if (lev_index == 0) {
					WHERE.append(" AND ").append(virDotTabName)
							.append(colObj.code_field.toLowerCase());
					WHERE.append("=");
					if (colObj.data_type
							.equalsIgnoreCase(ConstantKeys.DATA_TYPE_NUMBER))
						WHERE.append(value);
					if (colObj.data_type
							.equalsIgnoreCase(ConstantKeys.DATA_TYPE_STRING))
						WHERE.append("'").append(value).append("'");
				} else {
					Iterator lev_iter = levels.iterator();
					UiCommonDimhierarchyTable tmp_levObj = null;
					int val_index = 0;
					while (lev_iter.hasNext()) {
						val_index++;
						tmp_levObj = (UiCommonDimhierarchyTable) lev_iter
								.next();
						if (val_index == lev_index) {
							break;
						}
					}
					if (null != tmp_levObj) {
						WHERE.append(" AND ").append(virDotTabName)
								.append(tmp_levObj.src_idfld.toLowerCase());
						WHERE.append("=");
						if (tmp_levObj.idfld_type
								.equalsIgnoreCase(ConstantKeys.DATA_TYPE_NUMBER))
							WHERE.append(value);
						if (tmp_levObj.idfld_type
								.equalsIgnoreCase(ConstantKeys.DATA_TYPE_STRING))
							WHERE.append("'").append(value).append("'");
					}
				}
			}
			where = WHERE.toString();
		}
		return where;
	}

	/**
	 * 生成最初始的表格内容
	 */
	public void genDefalutContent(HttpSession session) {
		filteredContent = null;
		try {
			// 生成查询语句
			String sql = genSQL(session);
			// System.out.println(sql);
			// if(true) return;
			if (null != sql) {
				long start = System.currentTimeMillis();
				// System.out.println("genDefalutContent() sql "+sql);
				// if (true) return;
				String[][] svces = WebDBUtil.execQryArray(sql, "");
				System.out.println("数据库查询用时:"
						+ (System.currentTimeMillis() - start) + "ms");
				// 第一次执行
				if (null == content) {
					// 没有缓存内容
					if (null != svces) {
						// 开始进行各列合计
						// 声明为列的合计
						sumValues = new String[svces[0].length];
						// 所有维度合并为合计列
						// 循环行
						for (int i = 0; i < svces.length; i++) {
							Iterator iter = cols.iterator();
							// 维度数目
							int dim_index = -1;
							int dims_count = 0;
							// 指标下标
							int mus_index = -1;
							// 合计的下标
							int sum_index = -1;
							// 列的数目
							int index = -1;
							// 循环列对象
							while (iter.hasNext()) {
								index++;
								UiCommonTabledictTable colObj = (UiCommonTabledictTable) iter
										.next();
								if (ConstantKeys.NO
										.equalsIgnoreCase(colObj.is_measure)) {
									// 是维度列
									// 维度列增加
									dims_count++;
									// 编码字段
									sum_index++;
									dim_index++;
									sumValues[sum_index] = svces[i][dim_index];
									// 描述字段
									sum_index++;
									dim_index++;
									sumValues[sum_index] = svces[i][dim_index];
									if (null != filter_indexs
											&& null != filter_level
											&& null != filter_values) {
										index++;
										dims_count++;
										sum_index++;
										dim_index++;
										sumValues[sum_index] = svces[i][dim_index];
										sum_index++;
										dim_index++;
										sumValues[sum_index] = svces[i][dim_index];
									}
									if (expandedAll) {
										List levels = null;
										if (null != colsLevels)
											levels = (List) colsLevels
													.get(colObj.col_id);
										if (null != levels) {
											Iterator temp_iter = levels
													.iterator();
											while (temp_iter.hasNext()) {
												temp_iter.next();
												index++;
												dims_count++;
												sum_index++;
												dim_index++;
												sumValues[sum_index] = svces[i][dim_index];
												sum_index++;
												dim_index++;
												sumValues[sum_index] = svces[i][dim_index];
											}
										}
									}
									// 指标下标重新设置
									mus_index = index + dims_count;
								} else {
									// 指标列，且为显示列
									// 指标下标增加
									mus_index++;
									// 合计下标增加
									sum_index++;
									// 取出当前行、当前列值
									String colValue = svces[i][mus_index];
									// 设置合计值，
									if (null == sumValues[sum_index])
										sumValues[sum_index] = ConstantKeys.ZERO;
									// 累加当前值
									sumValues[sum_index] = NullProcFactory
											.transNullToZero(Arith.add(
													sumValues[sum_index],
													colValue));
									// 这里还没有算占比,所以不考虑
									if (ConstantKeys.YES
											.equalsIgnoreCase(colObj.has_last)) {
										// 如果有同比，则会有同比上期、环比上期
										mus_index++;
										sum_index++;
										colValue = svces[i][mus_index];
										// 设置合计值，
										if (null == sumValues[sum_index])
											sumValues[sum_index] = ConstantKeys.ZERO;
										// 累加当前值
										sumValues[sum_index] = NullProcFactory
												.transNullToZero(Arith.add(
														sumValues[sum_index],
														colValue));
										if (ConstantKeys.YES
												.equalsIgnoreCase(colObj.has_loop)) {
											mus_index++;
											sum_index++;
											colValue = svces[i][mus_index];
											// 设置合计值，
											if (null == sumValues[sum_index])
												sumValues[sum_index] = ConstantKeys.ZERO;
											// 累加当前值
											sumValues[sum_index] = NullProcFactory
													.transNullToZero(Arith
															.add(sumValues[sum_index],
																	colValue));
										}
										mus_index++;
										sum_index++;
										// 环比设置为0
										sumValues[sum_index] = ConstantKeys.ZERO;
									}
									if (ConstantKeys.YES
											.equalsIgnoreCase(colObj.has_loop)) {
										if (ConstantKeys.NO
												.equalsIgnoreCase(colObj.has_last)) {
											mus_index++;
											sum_index++;
											colValue = svces[i][mus_index];
											// 设置合计值，
											if (null == sumValues[sum_index])
												sumValues[sum_index] = ConstantKeys.ZERO;
											// 累加当前值
											sumValues[sum_index] = NullProcFactory
													.transNullToZero(Arith
															.add(sumValues[sum_index],
																	colValue));
										}
										mus_index++;
										sum_index++;
										// 环比设置为0
										sumValues[sum_index] = ConstantKeys.ZERO;
									}
								}
							}
						}

					}
				}
				if (null != svces && null != cols) {
					List temp = assembleArray(content, svces);
					if (null != content) {
						content.clear();
						// 先计算占比
						content.addAll(temp);
					} else {
						content = new ArrayList();
						content.addAll(temp);
						// 计算占比
						if (hasComRatio) {
							temp = genTotalComRatio(content, sumValues);
							content.clear();
							content.addAll(temp);
						}
					}

				}

			}
		} catch (AppException ae) {
			ae.printStackTrace();
		}
	}

	/**
	 * 生成某行的扩展链接
	 * 
	 * @param index
	 *            指定的扩展列
	 * @param row_id
	 *            指定的扩展行
	 * @return 扩展链接串
	 */
	private String genExpandLink(int index, String row_id) {
		String link = null;
		if (index >= 0 && null != row_id) {
			StringBuffer sb = new StringBuffer("");
			// 参数变量，先将要展开的参数加上
			StringBuffer params = new StringBuffer("");
			params.append("'").append(tableID);
			params.append("','").append(index);
			params.append("','").append(row_id).append("'");
			sb.append("<a href=\"javascript:").append(drillJSFun);
			sb.append("(").append(params).append(")\">")
					.append(genHtmlImg(drill_expand_gif)).append("</a>&nbsp;");
			link = sb.toString();
		}
		return link;
	}

	/**
	 * 按指定的图像名称生成HTML图像
	 * 
	 * @param imgName
	 *            图像的名称
	 * @return 生成后的图像HTML
	 */
	private String genHtmlImg(String imgName) {
		String img = null;
		if (null != imgName) {
			StringBuffer sb = new StringBuffer("<img src=\"");
			sb.append(imagePath).append(imgName).append("\" border=\"0\">");
			img = sb.toString();
		}
		return img;
	}

	/**
	 * 对指定的值根据列定义生成HTML链接
	 * 
	 * @param colObj
	 *            列对象
	 * @param value
	 *            值
	 * @return 生成的HMTL链接
	 */
	private String genHtmlLink(UiCommonTabledictTable colObj, String value) {
		String html = value;
		if (null != colObj && null != value && null != colObj.link_url
				&& !"".equals(colObj.link_url)) {
			String temp_where = convertWhereToUrlMode();
			// 如果由其他指标计算
			if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_calByOther)) {
				html = "<a href=\"" + colObj.link_url + temp_where
						+ "::state::&table_id=" + tableID + "&msu_fld="
						+ colObj.col_id.toLowerCase() + "&msu_name="
						+ colObj.col_name + "\"";
			} else {
				html = "<a href=\"" + colObj.link_url + temp_where
						+ "::state::&table_id=" + tableID + "&msu_fld="
						+ colObj.code_field.toLowerCase() + "&msu_name="
						+ colObj.col_name + "\"";
			}
			if (null != colObj.link_target && !"".equals(colObj.link_target)) {
				html += " target=\"" + colObj.link_target + "\"";
			}
			html += " class=\"" + this.hrefLinkClass + "\">" + value + "</a>";
		}
		return html;
	}

	/**
	 * 生成缩进空格
	 * 
	 * @param colObj
	 *            列对象
	 * @param expandCol
	 *            是否扩展列
	 * @return 生成后的缩进空格
	 */
	private String genIndentSpace(UiCommonTabledictTable colObj,
			boolean expandCol) {
		String space = null;
		if (null != colObj) {
			try {
				int count = Integer.parseInt(colObj.level);
				if (expandCol
						&& ConstantKeys.NO
								.equalsIgnoreCase(colObj.defalut_drilled))
					count++;
				StringBuffer sb = new StringBuffer("");
				for (int i = 0; i < count; i++) {
					sb.append(indentSpace);
				}
				space = sb.toString();
			} catch (NumberFormatException nfe) {

			}
		}
		return space;
	}

	/**
	 * 生成同比图像的链接
	 * 
	 * @param colObj
	 *            列对象
	 * @param img
	 *            图象名称
	 * @param alt
	 *            替换显示串
	 * @return 返回生成后的图像链接
	 */
	private String genLastImgHtml(UiCommonTabledictTable colObj, String img,
			String alt) {
		String ret = "";
		if (null != img) {
			ret = "<img src=\"" + imagePath + img + "\" alt=\"" + alt
					+ "\"border=\"0\">";
			String tmp = null;
			if (null != colObj && null != colObj.has_last
					&& !"".equals(colObj.has_last) && null != colObj.last_url
					&& !"".equalsIgnoreCase(colObj.last_url)) {
				String temp_where = convertWhereToUrlMode();
				if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_calByOther)) {
					tmp = "<a href=\"" + colObj.last_url + temp_where
							+ "::state::&table_id=" + tableID + "&msu_fld="
							+ colObj.col_id.toLowerCase() + "&msu_name="
							+ colObj.col_name + "\"";
				} else {
					tmp = "<a href=\"" + colObj.last_url + temp_where
							+ "::state::&table_id=" + tableID + "&msu_fld="
							+ colObj.code_field.toLowerCase() + "&msu_name="
							+ colObj.col_name + "\"";
				}
				if (null != colObj.last_target
						&& !"".equalsIgnoreCase(colObj.last_target)) {
					// 有目标
					tmp += " target=\"" + colObj.last_target + "\"";
				}
				tmp += ">";
			}
			if (null != tmp)
				ret = tmp + ret + "</a>";
		}
		return ret;
	}

	/**
	 * 生成环比链接图像
	 * 
	 * @param colObj
	 *            列对象
	 * @param img
	 *            图像名称
	 * @param alt
	 *            替换显示字符串
	 * @return 返回生成后的图形链接
	 */
	private String genLoopImgHtml(UiCommonTabledictTable colObj, String img,
			String alt) {
		String ret = "";
		if (null != img) {
			ret = "<img src=\"" + imagePath + img + "\" alt=\"" + alt
					+ "\"border=\"0\">";
			String tmp = null;
			if (null != colObj && null != colObj.has_loop
					&& !"".equals(colObj.has_loop) && null != colObj.loop_url
					&& !"".equalsIgnoreCase(colObj.loop_url)) {
				String temp_where = convertWhereToUrlMode();
				if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_calByOther)) {
					tmp = "<a href=\"" + colObj.loop_url + temp_where
							+ "::state::&table_id=" + tableID + "&msu_fld="
							+ colObj.col_id.toLowerCase() + "&msu_name="
							+ colObj.col_name + "\"";
				} else {
					tmp = "<a href=\"" + colObj.loop_url + temp_where
							+ "::state::&table_id=" + tableID + "&msu_fld="
							+ colObj.code_field.toLowerCase() + "&msu_name="
							+ colObj.col_name + "\"";
				}
				if (null != colObj.loop_target
						&& !"".equalsIgnoreCase(colObj.loop_target)) {

					// 有目标
					tmp += " target=\"" + colObj.loop_target + "\"";
				}
				tmp += ">";
			}
			if (null != tmp)
				ret = tmp + ret + "</a>";
		}
		return ret;
	}

	/**
	 * 生成对齐列的补充空格数
	 * 
	 * @param colObj
	 * @return
	 */
	private String genMsuSupSpace(String colName, String value) {
		String retStr = "";
		if (null != colName && null != value) {
			try {
				int name_len = colName.getBytes("GBK").length + 3;
				int val_len = value.getBytes("GBK").length;
				if (val_len < name_len) {
					int total = name_len - val_len;
					for (int i = 0; i < total; i++) {
						retStr += "&nbsp;";
					}
				}
			} catch (Exception e) {
				retStr = "";
			}
		}
		return retStr;
	}

	/**
	 * 将给定的值格式化为百分比显示
	 * 
	 * @param value
	 * @return
	 */
	public String genMsuTDPercentHtml(String value) {
		String retStr = "";
		if (null != value) {
			retStr = NullProcFactory.transNullToFixedRate(
					FormatUtil.formatPercent(value, 2, true), "2");
		}
		return retStr;
	}

	/**
	 * 根据给定序号生成排序的HTML代码
	 * 
	 * @param index
	 * @return
	 */
	public String genSortStr(int index, UiCommonTabledictTable colObj) {
		String sortStr = "";
		// 当前点击列就是本列
		String data_type = ConstantKeys.DATA_TYPE_NUMBER;
		if (null != colObj) {
			if (ConstantKeys.NO.equalsIgnoreCase(colObj.is_measure)
					&& ConstantKeys.DATA_TYPE_STRING
							.equalsIgnoreCase(colObj.data_type))
				data_type = ConstantKeys.DATA_TYPE_STRING;
		}
		if (sortedColum == index) {
			// 将前面条件加上，然后加上排序字段
			StringBuffer tmpStr1 = new StringBuffer(sortJSFun);
			String str = "";
			if (ConstantKeys.SORT_ASC.equalsIgnoreCase(sortOrder))
				str = ConstantKeys.SORT_DESC;
			else
				str = ConstantKeys.SORT_ASC;
			tmpStr1.append("('").append(tableID).append("','").append(index)
					.append("','").append(str).append("','").append(data_type)
					.append("')");
			String tmpImgStr = null;
			if (sortOrder.equals(ConstantKeys.SORT_ASC))
				tmpImgStr = sortAscImgName;
			else
				tmpImgStr = sortDescmgName;
			String tmpStr2 = tabHeadTDProHTML.replaceAll("::link::",
					tmpStr1.toString());
			tmpStr2 = tmpStr2.replaceAll("::img::", tmpImgStr);
			sortStr = tmpStr2;
		} else {
			StringBuffer tmpStr1 = new StringBuffer(sortJSFun);
			tmpStr1.append("('").append(tableID).append("','").append(index)
					.append("','").append(ConstantKeys.SORT_ASC).append("','")
					.append(data_type).append("')");
			String tmpImgStr = sortGenImgName;
			String tmpStr2 = tabHeadTDProHTML.replaceAll("::link::",
					tmpStr1.toString());
			tmpStr2 = tmpStr2.replaceAll("::img::", tmpImgStr);
			sortStr = tmpStr2;
		}
		return sortStr;
	}

	/**
	 * 生成查询的SQL
	 * 
	 * @return 返回组装后的SQL
	 */
	public String genSQL(HttpSession session) throws AppException {
		long start = System.currentTimeMillis();
		String retSQL = null;
		if (null != tableID) {
			if (null != cols && null != tabObj) {
				// 默认情况下第一个维度展开
				// 初始化
				dimsCount = 0;
				// 主表虚拟名
				String virTabName = "A";
				String virDotTabName = "A.";
				// 初始化SQL的各部分
				StringBuffer SELECT = new StringBuffer("SELECT ");
				StringBuffer dimSELECT = new StringBuffer("");
				StringBuffer FROM = new StringBuffer(" FROM ");
				StringBuffer WHERE = new StringBuffer(" WHERE 1=1 ");
				StringBuffer GROUPBY = new StringBuffer("");
				StringBuffer ORDERBY = new StringBuffer("");
				// 如果有条件，加上。注意必须是A.什么，这里不作解析了
				if (null != tabObj.sql_where
						&& !"".equals(tabObj.sql_where.trim())) {
					WHERE.append(tabObj.sql_where);
				}
				// System.out.println("where string middle is "+WHERE);
				// System.out.println("where string postfix is "+where);
				// 加上设置的条件，注意和上面的情况相同
				if (null != this.where && !"".equalsIgnoreCase(where)) {
					WHERE.append(where);
				}
				// System.out.println("where string is "+WHERE);
				// 加进主表
				FROM.append(tabObj.data_table).append(" ").append(virTabName);
				int index = -1;
				if (null != filter_indexs && null != filter_level
						&& null != filter_values) {
					banExpand = true;
				}
				// 默认认为没有比率计算
				boolean hasRatio = false;
				// 此处先要判断一下整个表格是否带有比率
				Iterator iter = cols.iterator();
				while (iter.hasNext()) {
					UiCommonTabledictTable colObj = (UiCommonTabledictTable) iter
							.next();
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_measure)) {
						if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_last)
								|| ConstantKeys.YES
										.equalsIgnoreCase(colObj.has_loop)) {
							// 有一个就可以了
							hasRatio = true;
							break;
						}
					}
				}
				// 声明一个新的列对象列表
				List tmpCols = new ArrayList();
				iter = cols.iterator();
				// 不显示的列数为0
				notDisplayCols = 0;
				while (iter.hasNext()) {
					// 取出列对象
					UiCommonTabledictTable colObj = (UiCommonTabledictTable) iter
							.next();
					index++;
					// 重新设置一下
					colObj.index = index;
					if (ConstantKeys.NO.equalsIgnoreCase(colObj.is_display)) {
						// 不显示列
						notDisplayCols++;
					}
					// 是维度
					if (ConstantKeys.NO.equalsIgnoreCase(colObj.is_measure)
							&& ConstantKeys.YES
									.equalsIgnoreCase(colObj.is_display)) {
						// 是维度，且显示
						dimsCount++;
						List levels = null;
						// 取出所有的维度层次
						if (null != colsLevels)
							levels = (List) colsLevels.get(colObj.col_id);
						if (!banExpand) {
							// 如果没有禁止扩展
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.isExpandCol)) {
								// 如果维度是扩展列
								if (hasRatio) {
									// 如果有比率计算
									// 取出列对象的当前层次
									String level = colObj.level;

									if (ConstantKeys.ZERO
											.equalsIgnoreCase(level.trim())) {
										// 第0层
										if (!"".equalsIgnoreCase(colObj.colsrch_where
												.trim()))
											// 如果列查询还有条件，注意要满足的条件
											WHERE.append(" AND ").append(
													colObj.colsrch_where);
									} else {
										// 不是第0层，取出当前层对象
										UiCommonDimhierarchyTable curObj = TableColUtil
												.getCurLevelObj(levels, level);
										if (null != curObj) {
											if (!"".equalsIgnoreCase(curObj.dimsrch_where
													.trim()))
												// 加上查询条件
												WHERE.append(" AND ").append(
														curObj.dimsrch_where);

										}
									}
								}

								// 钻取该列
								if (Integer.MIN_VALUE != expandIndex
										&& index == expandIndex) {
									// 是展开该列
									if (colObj.neverDrilled) {
										// 如果从来没有钻取过，类似全部情况
										// 设置已经钻取过
										colObj.neverDrilled = false;
										// 展开第0层
										SELECT.append(virDotTabName)
												.append(colObj.code_field)
												.append(",");
										SELECT.append(virDotTabName)
												.append(colObj.desc_field)
												.append(",");
										dimSELECT.append(virDotTabName)
												.append(colObj.code_field)
												.append(",");
										dimSELECT.append(virDotTabName)
												.append(colObj.desc_field)
												.append(",");
										GROUPBY.append(virDotTabName)
												.append(colObj.code_field)
												.append(",");
										GROUPBY.append(virDotTabName)
												.append(colObj.desc_field)
												.append(",");
										ORDERBY.append(virDotTabName)
												.append(colObj.code_field)
												.append(",");
									} else {
										// 如果展开过
										if (null != levels) {
											// 如果有层次结构
											// 找到上一层对象
											UiCommonDimhierarchyTable preLevObj = TableColUtil
													.getPreLevelObj(levels,
															colObj.level);
											// 找到当前层对象
											UiCommonDimhierarchyTable curLevObj = TableColUtil
													.getCurLevelObj(levels,
															colObj.level);
											if (null != curLevObj) {
												// 如果当前层对象不为空
												if (null != preLevObj) {
													// 非第一层
													SELECT.append(virDotTabName)
															.append(curLevObj.src_idfld)
															.append(",");
													SELECT.append(virDotTabName)
															.append(curLevObj.src_namefld)
															.append(",");
													dimSELECT
															.append(virDotTabName)
															.append(curLevObj.src_idfld)
															.append(",");
													dimSELECT
															.append(virDotTabName)
															.append(curLevObj.src_namefld)
															.append(",");
													GROUPBY.append(
															virDotTabName)
															.append(curLevObj.src_idfld)
															.append(",");
													GROUPBY.append(
															virDotTabName)
															.append(curLevObj.src_namefld)
															.append(",");
													ORDERBY.append(
															virDotTabName)
															.append(curLevObj.src_idfld)
															.append(",");
													// 如果有值
													if (null != colObj.value) {
														String where = genColSearchWhere(
																colObj,
																virDotTabName,
																levels);
														if (null != where)
															WHERE.append(where);
													}
												} else {
													// 第0层占到第一层
													SELECT.append(virDotTabName)
															.append(curLevObj.src_idfld)
															.append(",");
													SELECT.append(virDotTabName)
															.append(curLevObj.src_namefld)
															.append(",");
													dimSELECT
															.append(virDotTabName)
															.append(curLevObj.src_idfld)
															.append(",");
													dimSELECT
															.append(virDotTabName)
															.append(curLevObj.src_namefld)
															.append(",");
													GROUPBY.append(
															virDotTabName)
															.append(curLevObj.src_idfld)
															.append(",");
													GROUPBY.append(
															virDotTabName)
															.append(curLevObj.src_namefld)
															.append(",");
													ORDERBY.append(
															virDotTabName)
															.append(curLevObj.src_idfld)
															.append(",");
													// 如果有值
													if (null != colObj.value) {
														String where = genColSearchWhere(
																colObj,
																virDotTabName,
																levels);
														if (null != where)
															WHERE.append(where);
													}
												}
											}

										} else { // 即使一层也不应该为零
											colObj.canDrill = false;

											SELECT.append(virDotTabName)
													.append(colObj.code_field)
													.append(",");
											SELECT.append(virDotTabName)
													.append(colObj.desc_field)
													.append(",");
											dimSELECT.append(virDotTabName)
													.append(colObj.code_field)
													.append(",");
											dimSELECT.append(virDotTabName)
													.append(colObj.desc_field)
													.append(",");
											GROUPBY.append(virDotTabName)
													.append(colObj.code_field)
													.append(",");
											GROUPBY.append(virDotTabName)
													.append(colObj.desc_field)
													.append(",");
											ORDERBY.append(virDotTabName)
													.append(colObj.code_field)
													.append(",");
										}
									}
								} else {
									// 第一次显示，还没有钻取
									colObj.canDrill = true;

									if (!colObj.neverDrilled) {
										// 也即默认展示列
										UiCommonDimhierarchyTable curLevObj = TableColUtil
												.getCurLevelObj(levels,
														colObj.level);
										if (null == curLevObj) {
											SELECT.append(virDotTabName)
													.append(colObj.code_field)
													.append(",");
											SELECT.append(virDotTabName)
													.append(colObj.desc_field)
													.append(",");
											dimSELECT.append(virDotTabName)
													.append(colObj.code_field)
													.append(",");
											dimSELECT.append(virDotTabName)
													.append(colObj.desc_field)
													.append(",");
											GROUPBY.append(virDotTabName)
													.append(colObj.code_field)
													.append(",");
											GROUPBY.append(virDotTabName)
													.append(colObj.desc_field)
													.append(",");
											ORDERBY.append(virDotTabName)
													.append(colObj.code_field)
													.append(",");
											if (null != colObj.value) {
												String where = genColSearchWhere(
														colObj, virDotTabName,
														levels);
												if (null != where)
													WHERE.append(where);
											}
										} else {
											SELECT.append(virDotTabName)
													.append(colObj.code_field)
													.append(",");
											SELECT.append(virDotTabName)
													.append(colObj.desc_field)
													.append(",");
											dimSELECT.append(virDotTabName)
													.append(colObj.code_field)
													.append(",");
											dimSELECT.append(virDotTabName)
													.append(colObj.desc_field)
													.append(",");
											GROUPBY.append(virDotTabName)
													.append(colObj.code_field)
													.append(",");
											GROUPBY.append(virDotTabName)
													.append(colObj.desc_field)
													.append(",");
											ORDERBY.append(virDotTabName)
													.append(colObj.code_field)
													.append(",");
											if (null != colObj.value) {
												String where = genColSearchWhere(
														colObj, virDotTabName,
														levels);
												if (null != where)
													WHERE.append(where);
											}
										}
										if (null == levels)
											colObj.canDrill = false;
									} else {
										// 非默认展示列
										SELECT.append("'").append(index)
												.append("',");
										SELECT.append("'").append(index)
												.append("',");
									}
								}
							} else {
								SELECT.append(virDotTabName)
										.append(colObj.code_field).append(",");
								SELECT.append(virDotTabName)
										.append(colObj.desc_field).append(",");
								dimSELECT.append(virDotTabName)
										.append(colObj.code_field).append(",");
								dimSELECT.append(virDotTabName)
										.append(colObj.desc_field).append(",");
								GROUPBY.append(virDotTabName)
										.append(colObj.code_field).append(",");
								GROUPBY.append(virDotTabName)
										.append(colObj.desc_field).append(",");
								ORDERBY.append(virDotTabName)
										.append(colObj.code_field).append(",");
								if (null != colObj.value) {
									String where = genColSearchWhere(colObj,
											virDotTabName, levels);
									if (null != where)
										WHERE.append(where);
								}
							}
						} else {
							if (null != filter_indexs && null != filter_level
									&& null != filter_values) {

								// 定义为不能钻取
								colObj.canDrill = false;
								SELECT.append(virDotTabName)
										.append(colObj.code_field).append(",");
								SELECT.append(virDotTabName)
										.append(colObj.desc_field).append(",");
								dimSELECT.append(virDotTabName)
										.append(colObj.code_field).append(",");
								dimSELECT.append(virDotTabName)
										.append(colObj.desc_field).append(",");
								GROUPBY.append(virDotTabName)
										.append(colObj.code_field).append(",");
								GROUPBY.append(virDotTabName)
										.append(colObj.desc_field).append(",");
								ORDERBY.append(virDotTabName)
										.append(colObj.code_field).append(",");
								// 全部展开
								if (null != levels) {
									// 将所有的层次全选出来
									Iterator temp_iter = levels.iterator();
									while (temp_iter.hasNext()) {
										dimsCount++;
										UiCommonDimhierarchyTable levObj = (UiCommonDimhierarchyTable) temp_iter
												.next();
										SELECT.append(virDotTabName)
												.append(levObj.src_idfld)
												.append(",");
										SELECT.append(virDotTabName)
												.append(levObj.src_namefld)
												.append(",");
										dimSELECT.append(virDotTabName)
												.append(levObj.src_idfld)
												.append(",");
										dimSELECT.append(virDotTabName)
												.append(levObj.src_namefld)
												.append(",");
										GROUPBY.append(virDotTabName)
												.append(levObj.src_idfld)
												.append(",");
										GROUPBY.append(virDotTabName)
												.append(levObj.src_namefld)
												.append(",");
										break;
									}
								}
							}// 这里不进行过虑，在组成表格时再过虑掉
							if (expandedAll) {
								// 定义为不能钻取
								colObj.canDrill = false;
								SELECT.append(virDotTabName)
										.append(colObj.code_field).append(",");
								SELECT.append(virDotTabName)
										.append(colObj.desc_field).append(",");
								dimSELECT.append(virDotTabName)
										.append(colObj.code_field).append(",");
								dimSELECT.append(virDotTabName)
										.append(colObj.desc_field).append(",");
								GROUPBY.append(virDotTabName)
										.append(colObj.code_field).append(",");
								GROUPBY.append(virDotTabName)
										.append(colObj.desc_field).append(",");
								ORDERBY.append(virDotTabName)
										.append(colObj.code_field).append(",");
								// 全部展开
								if (null != levels) {
									// 将所有的层次全选出来
									Iterator temp_iter = levels.iterator();
									while (temp_iter.hasNext()) {
										dimsCount++;
										UiCommonDimhierarchyTable levObj = (UiCommonDimhierarchyTable) temp_iter
												.next();
										SELECT.append(virDotTabName)
												.append(levObj.src_idfld)
												.append(",");
										SELECT.append(virDotTabName)
												.append(levObj.src_namefld)
												.append(",");
										dimSELECT.append(virDotTabName)
												.append(levObj.src_idfld)
												.append(",");
										dimSELECT.append(virDotTabName)
												.append(levObj.src_namefld)
												.append(",");
										GROUPBY.append(virDotTabName)
												.append(levObj.src_idfld)
												.append(",");
										GROUPBY.append(virDotTabName)
												.append(levObj.src_namefld)
												.append(",");
									}
								}
							}
						}
					} else {
						if (ConstantKeys.YES
								.equalsIgnoreCase(colObj.has_comratio)) {
							hasComRatio = true;
						}
						// 指标且能显示
						// 指标，直接SUM
						if (!hasRatio) {
							// 这里是不求其他比率，直接SUM
							StringBuffer temp = new StringBuffer("");
							if (ConstantKeys.NO
									.equalsIgnoreCase(colObj.is_calByOther)) {
								String temp_fld = colObj.code_field;
								temp_fld = temp_fld.toUpperCase();
								if (temp_fld.indexOf("COUNT(") >= 0) {
									// 是统计行数的
									String col_where = colObj.colsrch_where;
									if (null != col_where
											&& !"".equals(col_where)) {
										// 还有条件,去where中找到
										List parsedWhere = parseSpecialWhere(
												WHERE.toString(), col_where);
										if (null != parsedWhere) {
											Iterator temp_iter = parsedWhere
													.iterator();
											int count = -1;
											while (temp_iter.hasNext()) {
												count++;
												String temp_str = (String) temp_iter
														.next();
												if (0 == count) {
													WHERE.delete(0,
															WHERE.length());
													WHERE.append(" " + temp_str);
												} else {
													temp_fld = temp_fld
															.replaceAll("\\?",
																	temp_str);
												}
											}
										}
										SELECT.append(temp_fld).append(",");
									} else {
										// 没有条件
										SELECT.append(temp_fld).append(",");
									}
								} else {
									// 非计算型指标
									temp.append("SUM(A.")
											.append(colObj.code_field)
											.append(")");
									SELECT.append("NVL(").append(temp)
											.append(",0),");
								}
							} else {
								// 计算型指标
								temp.delete(0, temp.length());
								String cmd = colObj.code_field;
								List factors = colObj.msuFactors;
								if (null != factors) {
									Iterator tmp_iter = factors.iterator();
									while (tmp_iter.hasNext()) {
										String msuID = (String) tmp_iter.next();
										UiCommonTabledictTable tmpColObj = (UiCommonTabledictTable) colsMap
												.get(msuID);
										if (null != tmpColObj
												&& null != tmpColObj.code_field) {
											// 开始替换成实际的
											String replace = "::msu" + msuID
													+ "::";
											String replaced = "SUM(A."
													+ tmpColObj.code_field
													+ ")";
											cmd = cmd.replaceAll(replace,
													replaced);
										}
									}
								}
								temp.append(cmd);
								SELECT.append("NVL(").append(temp)
										.append(",0),");
							}
						} else {
							// 如果要计算比率，比如同比或环比
							// 先将本期值加上
							StringBuffer temp = new StringBuffer("");
							if (ConstantKeys.NO
									.equalsIgnoreCase(colObj.is_calByOther)) {
								temp.append("SUM(A.").append(colObj.code_field)
										.append(")");
							} else {
								temp.delete(0, temp.length());
								String cmd = colObj.code_field;
								List factors = colObj.msuFactors;
								if (null != factors) {
									Iterator tmp_iter = factors.iterator();
									while (tmp_iter.hasNext()) {
										String msuID = (String) tmp_iter.next();
										UiCommonTabledictTable tmpColObj = (UiCommonTabledictTable) colsMap
												.get(msuID);
										if (null != tmpColObj
												&& null != tmpColObj.code_field) {
											// 开始替换成实际的
											String replace = "::msu" + msuID
													+ "::";
											String replaced = "SUM(A."
													+ tmpColObj.code_field
													+ ")";
											cmd = cmd.replaceAll(replace,
													replaced);
										}
									}
								}
								temp.append(cmd);
							}
							// 查询本期值
							SELECT.append("NVL(").append(temp).append(",0),");
							// //查询同比上期值
							String temp_str = temp.toString();
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_last)) {
								temp_str = temp.toString();
								temp_str = temp_str.replaceAll("A\\.", "B.");
								SELECT.append("NVL(").append(temp_str)
										.append(",0),");
							}
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_loop)) {
								// 查询环比上期值
								temp_str = temp.toString();
								temp_str = temp_str.replaceAll("A\\.", "C.");
								SELECT.append("NVL(").append(temp_str)
										.append(",0),");
							}
							// 下面开始判断是否有同比或环比，计算出来
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_last)) {
								// 有同比，判断是否有字段值
								if ("".equals(colObj.last_field.trim())) {
									// 计算关联的值
									String tempStr = colObj.code_field;
									if (ConstantKeys.NO
											.equalsIgnoreCase(colObj.is_calByOther)) {
										StringBuffer this_period = new StringBuffer(
												"SUM(A.").append(tempStr)
												.append(")");
										StringBuffer last_period = new StringBuffer(
												"SUM(B.").append(tempStr)
												.append(")");
										StringBuffer ratio = new StringBuffer(
												"NVL((").append(this_period)
												.append("-")
												.append(last_period)
												.append(")/");
										ratio.append("(CASE WHEN ")
												.append(last_period)
												.append("=0")
												.append(" THEN NULL ELSE ");
										ratio.append(last_period).append(
												" END),0)");
										SELECT.append(ratio).append(",");
									} else {
										// 计算型的指标的比率
										// 本期值
										StringBuffer this_period = new StringBuffer(
												"");
										String cmd = colObj.code_field;
										List factors = colObj.msuFactors;
										if (null != factors) {
											Iterator tmp_iter = factors
													.iterator();
											while (tmp_iter.hasNext()) {
												String msuID = (String) tmp_iter
														.next();
												UiCommonTabledictTable tmpColObj = (UiCommonTabledictTable) colsMap
														.get(msuID);
												if (null != tmpColObj
														&& null != tmpColObj.code_field) {
													// 开始替换成实际的
													String replace = "::msu"
															+ msuID + "::";
													String replaced = "SUM(A."
															+ tmpColObj.code_field
															+ ")";
													cmd = cmd.replaceAll(
															replace, replaced);
												}
											}
										}
										this_period.append(cmd);
										// 上期值
										StringBuffer last_period = new StringBuffer(
												"");
										last_period.append(this_period
												.toString().replaceAll("A\\.",
														"B."));
										StringBuffer ratio = new StringBuffer(
												"");
										ratio.append("NVL((")
												.append(this_period)
												.append("-");
										ratio.append(last_period).append(
												")/(CASE WHEN ");
										ratio.append(last_period)
												.append("=0 THEN NULL ELSE ")
												.append(last_period);
										ratio.append(" END),0)");
										SELECT.append(ratio).append(",");
									}
								} else {
									SELECT.append("NVL(")
											.append(colObj.last_field)
											.append(",0),");
								}
							}
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_loop)) {
								if ("".equals(colObj.last_field.trim())) {
									// 计算关联的值
									String tempStr = colObj.code_field;
									if (ConstantKeys.NO
											.equalsIgnoreCase(colObj.is_calByOther)) {
										StringBuffer this_period = new StringBuffer(
												"SUM(A.").append(tempStr)
												.append(")");
										StringBuffer last_period = new StringBuffer(
												"SUM(C.").append(tempStr)
												.append(")");
										StringBuffer ratio = new StringBuffer(
												"NVL((").append(this_period)
												.append("-")
												.append(last_period)
												.append(")/");
										ratio.append("(CASE WHEN ")
												.append(last_period)
												.append("=0")
												.append(" THEN NULL ELSE ");
										ratio.append(last_period).append(
												" END),0)");
										SELECT.append(ratio).append(",");
									} else {
										// 计算型的指标的比率
										// 本期值
										StringBuffer this_period = new StringBuffer(
												"");
										String cmd = colObj.code_field;
										List factors = colObj.msuFactors;
										if (null != factors) {
											Iterator tmp_iter = factors
													.iterator();
											while (tmp_iter.hasNext()) {
												String msuID = (String) tmp_iter
														.next();
												UiCommonTabledictTable tmpColObj = (UiCommonTabledictTable) colsMap
														.get(msuID);
												if (null != tmpColObj
														&& null != tmpColObj.code_field) {
													// 开始替换成实际的
													String replace = "::msu"
															+ msuID + "::";
													String replaced = "SUM(A."
															+ tmpColObj.code_field
															+ ")";
													cmd = cmd.replaceAll(
															replace, replaced);
												}
											}
										}
										this_period.append(cmd);
										// 上期值
										StringBuffer last_period = new StringBuffer(
												"");
										last_period.append(this_period
												.toString().replaceAll("A\\.",
														"C."));
										StringBuffer ratio = new StringBuffer(
												"");
										ratio.append("NVL((")
												.append(this_period)
												.append("-");
										ratio.append(last_period).append(
												")/(CASE WHEN ");
										ratio.append(last_period)
												.append("=0 THEN NULL ELSE ")
												.append(last_period);
										ratio.append(" END),0)");
										SELECT.append(ratio).append(",");
									}
								} else {
									SELECT.append("NVL(")
											.append(colObj.last_field)
											.append(",0),");
								}
							}
						}
					}
					tmpCols.add(index, colObj);
				}

				// 开始除去多余的,
				if (SELECT.lastIndexOf(",") >= 0) {
					SELECT.deleteCharAt(SELECT.lastIndexOf(","));
				}
				if (GROUPBY.lastIndexOf(",") >= 0) {
					GROUPBY.deleteCharAt(GROUPBY.lastIndexOf(","));
				}
				if (ORDERBY.lastIndexOf(",") >= 0) {
					ORDERBY.deleteCharAt(ORDERBY.lastIndexOf(","));
				}

				//
				if (hasRatio
						&& ConstantKeys.YES.equalsIgnoreCase(tabObj.cal_ratio)) {
					String tempStr = joinRatioTable(tabObj, cols,
							dimSELECT.toString(), WHERE.toString(),
							GROUPBY.toString(), "A", "B", "C", false, false);
					FROM.delete(0, FROM.length());
					FROM.append(tempStr);
					SELECT.append(FROM);
					if (GROUPBY.length() > 0) {
						SELECT.append(" GROUP BY ").append(GROUPBY);
					}
					SELECT.append(sqlHaving);
					if (ORDERBY.length() > 0) {
						SELECT.append(" ORDER BY ").append(ORDERBY);
					}
				} else {
					// 开始组装
					SELECT.append(FROM).append(WHERE);
					if (GROUPBY.length() > 0) {
						SELECT.append(" GROUP BY ").append(GROUPBY);
					}
					SELECT.append(sqlHaving);
					if (ORDERBY.length() > 0) {
						SELECT.append(" ORDER BY ").append(ORDERBY);
					}
				}
				cols.clear();
				cols.addAll(tmpCols);
				retSQL = SELECT.toString();
			}
		}
		System.out.println("the sql is======================= " + retSQL);
		System.out.println("组装SQL用时:" + (System.currentTimeMillis() - start)
				+ "ms");
		return retSQL;
	}

	/**
	 * 生成展开行的占比
	 * 
	 * @param subContent
	 *            展开行的列表
	 * @param sumRowObj
	 *            汇总行对象
	 * @return 计算占比后的行列表
	 */
	private List genSubComRatio(List subContent, RowStruct sumRowObj) {
		List retContent = null;
		if (null != subContent && null != sumRowObj) {
			String[] sumRowValues = sumRowObj.cols_value;
			retContent = new ArrayList();
			Iterator iter = subContent.iterator();
			// 循环行
			while (iter.hasNext()) {
				RowStruct rowObj = (RowStruct) iter.next();
				String[] colsValues = rowObj.cols_value;
				// 声明一个LIST来保存值，因为数组长度扩展且不定
				List temp_cols_value = new ArrayList();
				int col_index = -1;
				int dimsNum = 0;
				int msu_index = -1;
				Iterator col_iter = rowObj.colObjs.iterator();
				while (col_iter.hasNext()) {
					UiCommonTabledictTable colObj = (UiCommonTabledictTable) col_iter
							.next();
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_display)) {
						col_index++;
						if (ConstantKeys.NO.equalsIgnoreCase(colObj.is_measure)) {
							// 维度列
							int aryIndex = col_index + dimsNum + 1;
							dimsNum++;
							String colValue = null;
							if (colObj.neverDrilled) {
								colValue = colObj.col_id;
								colValue = colValue.trim();
							} else {
								colValue = colsValues[aryIndex - 1];
								colValue = colValue.trim();
							}
							temp_cols_value.add(colValue);
							if (colObj.neverDrilled) {
								colValue = colObj.col_name;
								colValue = colValue.trim();
							} else {
								colValue = colsValues[aryIndex];
								colValue = colValue.trim();
							}
							temp_cols_value.add(colValue);
							if (expandedAll) {
								List levels = null;
								if (null != colsLevels)
									levels = (List) colsLevels
											.get(colObj.col_id);
								if (null != levels) {
									Iterator temp_iter = levels.iterator();
									while (temp_iter.hasNext()) {
										temp_iter.next();
										col_index++;
										dimsNum++;
									}
								}
							}
							msu_index = col_index + dimsNum;
						} else {
							// 指标列
							// 获取列值
							msu_index++;
							String colValue = colsValues[msu_index];
							colValue = colValue.trim();
							temp_cols_value.add(colValue);
							// 开始加上占比
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_comratio)) {
								String ratio = NullProcFactory
										.transNullToZero(Arith.div(colValue,
												sumRowValues[msu_index], 4));
								temp_cols_value.add(ratio);
							}
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_last)) {
								// 同比上期值
								msu_index++;
								colValue = colsValues[msu_index];
								if (ConstantKeys.YES
										.equalsIgnoreCase(colObj.has_loop)) {
									// 环比上期值
									msu_index++;
									colValue = colsValues[msu_index];
								}
								// 同比值
								msu_index++;
								colValue = colsValues[msu_index];
							}
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_loop)) {
								if (ConstantKeys.YES
										.equalsIgnoreCase(colObj.has_last)) {
									// 前面已经加上了
									// 环比值
									msu_index++;
									colValue = colsValues[msu_index];
								} else {
									// 环比上期值
									msu_index++;
									colValue = colsValues[msu_index];
									temp_cols_value.add(colValue);
									// 环比
									msu_index++;
									colValue = colsValues[msu_index];
									temp_cols_value.add(colValue);
								}
							}
						}
					}
				}
				Object[] tmpObj = temp_cols_value.toArray();
				if (null != tmpObj) {
					colsValues = new String[tmpObj.length];
					for (int i = 0; i < tmpObj.length; i++) {
						colsValues[i] = (String) tmpObj[i];
					}
				}
				rowObj.cols_value = colsValues;
				retContent.add(rowObj);
			}
		}
		return retContent;
	}

	/**
	 * 生成汇总行的HTML部分
	 * 
	 * @return
	 */
	private String[] genSumHtml() {
		String[] sumHtml = new String[2];
		for (int i = 0; i < sumHtml.length; i++) {
			sumHtml[i] = "";
		}
		if (null != sumValues) {
			StringBuffer tableBodyLeft = new StringBuffer("");
			StringBuffer tableBodyRight = new StringBuffer("");
			tableBodyLeft.append("<tr id=\"L_sum_value\" class=\"")
					.append("table-total").append("\" ").append(trRest)
					.append(">\n");
			tableBodyRight.append("<tr id=\"R_sum_value\" class=\"")
					.append("table-total").append("\" ").append(trRest)
					.append(">\n");
			String tdBeginClass = "table-tdd";
			tableBodyLeft.append("<td nowrap colspan=\"").append(dimsCount)
					.append("\" align=\"center\" class=\"")
					.append(tdBeginClass).append("\">\n");

			UiCommonTabledictTable temp_colObj = null;
			Iterator iter = cols.iterator();
			while (iter.hasNext()) {
				UiCommonTabledictTable colObj = (UiCommonTabledictTable) iter
						.next();
				if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_display)
						&& ConstantKeys.NO.equalsIgnoreCase(colObj.is_measure)) {
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_cellclk)) {
						temp_colObj = colObj;
						break;
					}
				}
			}
			if (null != temp_colObj) {
				// 点击行图形变化
				/*
				 * String chartLink = "chartchange('" +
				 * temp_colObj.cell_chart_ids + "','',null,'sum_value')\"";
				 * chartLink = "<a href=\"javascript:" + chartLink + " class=\""
				 * + hrefLinkClass + "\">"; tableBodyLeft.append(chartLink);
				 */
				tableBodyLeft.append("合计");
				// tableBodyLeft.append("</a>");
			} else {
				tableBodyLeft.append("合计");
			}
			// 加上名称

			tableBodyLeft.append("</td>\n");
			iter = cols.iterator();
			String tdClass = "";
			String align = "right";
			int index = 2 * dimsCount - 1;
			while (iter.hasNext()) {
				UiCommonTabledictTable colObj = (UiCommonTabledictTable) iter
						.next();
				if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_display)
						&& ConstantKeys.YES.equalsIgnoreCase(colObj.is_measure)) {
					index++;
					int this_period = index;

					tdClass = tdBeginClass;
					align = "right";
					tableBodyRight.append("<td nowrap align=\"").append(align)
							.append("\" class=\"").append(tdClass)
							.append("\">\n");
					// 加上名称
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_sumdisplay)) {
						if (ConstantKeys.YES
								.equalsIgnoreCase(colObj.is_calByOther)) {
							tableBodyRight.append(genCalMsuSumValue(colObj,
									sumValues));
						} else
							tableBodyRight.append(formatColValue(colObj,
									sumValues[index]));
					} else {
						tableBodyRight
								.append("<img src=\"../images/msu_nosum.gif\" border=0>");
					}
					tableBodyRight.append("</td>\n");
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_comratio)) {

						tdClass = tdBeginClass;
						tableBodyRight
								.append("<td nowrap align=\"right\" class=\"")
								.append(tdClass).append("\">\n");
						tableBodyRight.append("100%");
						tableBodyRight.append("</td>\n");
					}
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_last)) {
						index++;
						if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_loop)) {
							index++;
						}
						index++;
						if (ConstantKeys.YES
								.equalsIgnoreCase(colObj.last_display)) {

							tdClass = tdBeginClass;
							tableBodyRight
									.append("<td nowrap align=\"right\" class=\"")
									.append(tdClass).append("\">\n");
							// 返回到本期值
							String colvalue = sumValues[this_period];
							String lcolvalue = sumValues[this_period + 1];
							String lratio = NullProcFactory
									.transNullToZero(Arith.divs(
											Arith.sub(colvalue, lcolvalue),
											lcolvalue, 4));
							if (null == lratio) {
								lratio = ConstantKeys.ZERO;
							}
							tableBodyRight.append(FormatUtil.formatPercent(
									lratio, 2, true));
							tableBodyRight.append("</td>\n");
						}
					}
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_loop)) {
						if (ConstantKeys.NO.equalsIgnoreCase(colObj.has_last)) {
							index++;
						}
						index++;
						if (ConstantKeys.YES
								.equalsIgnoreCase(colObj.loop_display)) {

							tdClass = tdBeginClass;
							tableBodyRight
									.append("<td nowrap align=\"right\" class=\"")
									.append(tdClass).append("\">\n");
							int loop_index = 0;
							if (ConstantKeys.NO
									.equalsIgnoreCase(colObj.has_last)) {
								loop_index = this_period + 1;
							} else {
								loop_index = this_period + 2;
							}
							String colvalue = sumValues[this_period];
							String lcolvalue = sumValues[loop_index];
							String lratio = NullProcFactory
									.transNullToZero(Arith.divs(
											Arith.sub(colvalue, lcolvalue),
											lcolvalue, 4));
							if (null == lratio) {
								lratio = ConstantKeys.ZERO;
							}
							tableBodyRight.append(FormatUtil.formatPercent(
									lratio, 2, true));
							tableBodyRight.append("</td>\n");
						}
					}
				}
			}
			tableBodyRight.append("</tr>");
			tableBodyLeft.append("</tr>");
			sumHtml[0] = tableBodyLeft.toString();
			sumHtml[1] = tableBodyRight.toString();
		}
		return sumHtml;
	}

	/**
	 * 生成过虑后的合计值
	 * 
	 * @param tempContent
	 * @return
	 */
	private String[] genTempSumHtml(List tempContent) {

		int length = 0;
		Iterator row_iter = tempContent.iterator();
		while (row_iter.hasNext()) {
			RowStruct rowObj = (RowStruct) row_iter.next();
			String[] cols_value = rowObj.cols_value;
			length = cols_value.length;
			break;
		}
		String[] sumHtml = new String[2];
		for (int i = 0; i < sumHtml.length; i++) {
			sumHtml[i] = "";
		}
		String[] tmpSumValues = new String[length];
		// 所有维度合并为合计列
		// tmpSumValues[0] = "合计";
		// 循环行
		row_iter = tempContent.iterator();
		while (row_iter.hasNext()) {
			RowStruct rowObj = (RowStruct) row_iter.next();
			String[] cols_value = rowObj.cols_value;
			Iterator iter = cols.iterator();
			// 维度数目
			int dims_count = 0;
			// 指标下标
			int mus_index = -1;
			// 合计的下标
			int sum_index = -1;
			// 列的数目
			int index = -1;
			int dim_index = -1;
			// 循环列对象
			while (iter.hasNext()) {
				index++;
				UiCommonTabledictTable colObj = (UiCommonTabledictTable) iter
						.next();
				if (ConstantKeys.NO.equalsIgnoreCase(colObj.is_measure)) {
					// 是维度列
					// 维度列增加
					dims_count++;
					if (null != filter_indexs && null != filter_level
							&& null != filter_values) {
						index++;
						dims_count++;
						sum_index++;
						dim_index++;
						sumValues[sum_index] = cols_value[dim_index];
						// 描述字段
						sum_index++;
						dim_index++;
						sumValues[sum_index] = cols_value[dim_index];
					}
					sum_index++;
					dim_index++;
					sumValues[sum_index] = cols_value[dim_index];
					// 描述字段
					sum_index++;
					dim_index++;
					sumValues[sum_index] = cols_value[dim_index];
					// 指标下标重新设置
					mus_index = index + dims_count;
				} else if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_display)) {
					// 指标列，且为显示列
					// 指标下标增加
					mus_index++;
					// 合计下标增加
					sum_index++;
					// 取出当前行、当前列值
					String colValue = cols_value[mus_index];
					// 设置合计值，
					if (null == tmpSumValues[sum_index])
						tmpSumValues[sum_index] = ConstantKeys.ZERO;
					// 累加当前值
					tmpSumValues[sum_index] = NullProcFactory
							.transNullToZero(Arith.add(tmpSumValues[sum_index],
									colValue));
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_comratio)) {
						mus_index++;
						// 合计下标增加
						sum_index++;
						// 取出当前行、当前列值
						colValue = cols_value[mus_index];
						// 设置合计值，
						if (null == tmpSumValues[sum_index])
							tmpSumValues[sum_index] = ConstantKeys.ZERO;
						// 累加当前值
						tmpSumValues[sum_index] = NullProcFactory
								.transNullToZero(Arith.add(
										tmpSumValues[sum_index], colValue));
					}
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_last)) {
						// 如果有
						mus_index++;
						if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_loop)) {
							mus_index++;
							// 合计下标增加
							sum_index++;
							// 取出当前行、当前列值
							colValue = cols_value[mus_index];
							// 设置合计值，
							if (null == tmpSumValues[sum_index])
								tmpSumValues[sum_index] = ConstantKeys.ZERO;
							// 累加当前值
							tmpSumValues[sum_index] = NullProcFactory
									.transNullToZero(Arith.add(
											tmpSumValues[sum_index], colValue));
						}
						mus_index++;
						// 合计下标增加
						sum_index++;
						// 取出当前行、当前列值
						colValue = cols_value[mus_index];
						// 设置合计值，
						if (null == tmpSumValues[sum_index])
							tmpSumValues[sum_index] = ConstantKeys.ZERO;
						// 累加当前值
						tmpSumValues[sum_index] = ConstantKeys.ZERO;
					}
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_loop)) {
						if (ConstantKeys.NO.equalsIgnoreCase(colObj.has_last)) {
							mus_index++;
							// 合计下标增加
							sum_index++;
							// 取出当前行、当前列值
							colValue = cols_value[mus_index];
							// 设置合计值，
							if (null == tmpSumValues[sum_index])
								tmpSumValues[sum_index] = ConstantKeys.ZERO;
							// 累加当前值
							tmpSumValues[sum_index] = NullProcFactory
									.transNullToZero(Arith.add(
											tmpSumValues[sum_index], colValue));
						}
						mus_index++;
						// 合计下标增加
						sum_index++;
						// 取出当前行、当前列值
						colValue = cols_value[mus_index];
						// 设置合计值，
						if (null == tmpSumValues[sum_index])
							tmpSumValues[sum_index] = ConstantKeys.ZERO;
						// 累加当前值
						tmpSumValues[sum_index] = ConstantKeys.ZERO;
					}
				}
			}
		}

		if (null != tmpSumValues && tmpSumValues.length > 0) {
			StringBuffer tableBodyLeft = new StringBuffer("");
			StringBuffer tableBodyRight = new StringBuffer("");
			tableBodyLeft.append("<tr id=\"L_sum_value\" class=\"")
					.append("table-total").append("\" ").append(trRest)
					.append(">\n");
			tableBodyRight.append("<tr id=\"R_sum_value\" class=\"")
					.append("table-total").append("\" ").append(trRest)
					.append(">\n");
			String tdBeginClass = "table-tdd";
			;
			tableBodyLeft.append("<td nowrap colspan=\"").append(dimsCount)
					.append("\" align=\"center\" class=\"")
					.append(tdBeginClass).append("\">\n");
			UiCommonTabledictTable temp_colObj = null;
			Iterator iter = cols.iterator();
			while (iter.hasNext()) {
				UiCommonTabledictTable colObj = (UiCommonTabledictTable) iter
						.next();
				if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_display)
						&& ConstantKeys.NO.equalsIgnoreCase(colObj.is_measure)) {
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_cellclk)) {
						temp_colObj = colObj;
						break;
					}
				}
			}
			if (null != temp_colObj) {
				// 点击行图形变化
				/*
				 * String chartLink = "chartchange('" +
				 * temp_colObj.cell_chart_ids + "','',null,'sum_value')\"";
				 * chartLink = "<a href=\"javascript:" + chartLink + " class=\""
				 * + hrefLinkClass + "\">";
				 */
				// tableBodyLeft.append(chartLink);
				tableBodyLeft.append("合计");
				// tableBodyLeft.append("</a>");
			} else {
				tableBodyLeft.append("合计");
			}
			// 加上名称

			tableBodyLeft.append("</td>\n");
			iter = cols.iterator();
			String tdClass = "";
			String align = "right";
			int index = 2 * dimsCount - 1;
			while (iter.hasNext()) {
				UiCommonTabledictTable colObj = (UiCommonTabledictTable) iter
						.next();
				if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_display)
						&& ConstantKeys.YES.equalsIgnoreCase(colObj.is_measure)) {
					index++;
					int this_period = index;

					tdClass = tdBeginClass;
					align = "right";
					tableBodyRight.append("<td nowrap align=\"").append(align)
							.append("\" class=\"").append(tdClass)
							.append("\">\n");
					// 加上名称
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_sumdisplay)) {
						if (ConstantKeys.YES
								.equalsIgnoreCase(colObj.is_calByOther)) {
							tableBodyRight.append(genCalMsuSumValue(colObj,
									tmpSumValues));
						} else
							tableBodyRight.append(formatColValue(colObj,
									tmpSumValues[index]));
					} else {
						tableBodyRight
								.append("<img src=\"../images/msu_nosum.gif\" border=0>");
					}
					tableBodyRight.append("</td>\n");
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_comratio)) {

						tdClass = tdBeginClass;
						tableBodyRight
								.append("<td nowrap align=\"right\" class=\"")
								.append(tdClass).append("\">\n");
						tableBodyRight.append("100%");
						tableBodyRight.append("</td>\n");
					}
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_last)) {
						index++;
						if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_loop)) {
							index++;
						}
						index++;
						if (ConstantKeys.YES
								.equalsIgnoreCase(colObj.last_display)) {

							tdClass = tdBeginClass;
							tableBodyRight
									.append("<td nowrap align=\"right\" class=\"")
									.append(tdClass).append("\">\n");
							// 返回到本期值
							String colvalue = tmpSumValues[this_period];
							String lcolvalue = tmpSumValues[this_period + 1];
							String lratio = NullProcFactory
									.transNullToZero(Arith.divs(
											Arith.sub(colvalue, lcolvalue),
											lcolvalue, 4));
							if (null == lratio) {
								lratio = ConstantKeys.ZERO;
							}
							tableBodyRight.append(FormatUtil.formatPercent(
									lratio, 2, true));
							tableBodyRight.append("</td>\n");
						}
					}
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_loop)) {
						if (ConstantKeys.NO.equalsIgnoreCase(colObj.has_last)) {
							index++;
						}
						index++;
						if (ConstantKeys.YES
								.equalsIgnoreCase(colObj.loop_display)) {

							tdClass = tdBeginClass;
							tableBodyRight
									.append("<td nowrap align=\"center\" class=\"")
									.append(tdClass).append("\">\n");
							int loop_index = 0;
							if (ConstantKeys.NO
									.equalsIgnoreCase(colObj.has_last)) {
								loop_index = this_period + 1;
							} else {
								loop_index = this_period + 2;
							}
							String colvalue = tmpSumValues[this_period];
							String lcolvalue = tmpSumValues[loop_index];
							String lratio = NullProcFactory
									.transNullToZero(Arith.divs(
											Arith.sub(colvalue, lcolvalue),
											lcolvalue, 4));
							if (null == lratio) {
								lratio = ConstantKeys.ZERO;
							}
							tableBodyRight.append(FormatUtil.formatPercent(
									lratio, 2, true));
							tableBodyRight.append("</td>\n");
						}
					}
				}
			}
			tableBodyLeft.append("</tr>");
			tableBodyRight.append("</tr>");
			sumHtml[0] = tableBodyLeft.toString();
			sumHtml[1] = tableBodyRight.toString();
		} else {
			sumHtml = null;
		}
		return sumHtml;
	}

	private String genTip(UiCommonTabledictTable colObj) {
		String tip = "";
		if (null != colObj) {
			tip = "维度:::DIM_NAME::\n";
			tip += "指标:" + colObj.col_desc;
		}
		return tip;
	}

	/**
	 * 生成占比后的行对象列表
	 * 
	 * @param content
	 *            行对象列表
	 * @param sumValues
	 *            合计值
	 * @return 返回追加占比列后的行对象集合
	 */
	private List genTotalComRatio(List content, String[] sumValues) {
		List retContent = null;
		if (null != content && null != sumValues) {
			retContent = new ArrayList();
			Iterator iter = content.iterator();
			// 循环行
			while (iter.hasNext()) {
				RowStruct rowObj = (RowStruct) iter.next();
				String[] colsValues = rowObj.cols_value;
				// 声明一个LIST来保存值，因为数组长度扩展且不定
				List temp_cols_value = new ArrayList();
				int col_index = -1;
				int dimsNum = 0;
				int msu_index = -1;
				int sum_index = 0;
				Iterator col_iter = rowObj.colObjs.iterator();
				while (col_iter.hasNext()) {
					UiCommonTabledictTable colObj = (UiCommonTabledictTable) col_iter
							.next();
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_display)) {
						col_index++;
						// 维度列
						if (ConstantKeys.NO.equalsIgnoreCase(colObj.is_measure)) {
							int aryIndex = col_index + dimsNum + 1;
							dimsNum++;

							String colValue = null;
							if (colObj.neverDrilled) {
								colValue = colObj.col_id;
								colValue = colValue.trim();
							} else {
								colValue = colsValues[aryIndex - 1];
								colValue = colValue.trim();
							}
							temp_cols_value.add(colValue);
							if (colObj.neverDrilled) {
								colValue = colObj.col_name;
								colValue = colValue.trim();
							} else {
								colValue = colsValues[aryIndex];
								colValue = colValue.trim();
							}
							temp_cols_value.add(colValue);
							if (null != filter_indexs && null != filter_level
									&& null != filter_values) {
								col_index++;
								aryIndex = col_index + dimsNum + 1;
								dimsNum++;
								msu_index = col_index + dimsNum;
								colValue = null;
								if (colObj.neverDrilled) {
									colValue = colObj.col_id;
									colValue = colValue.trim();
								} else {
									colValue = colsValues[aryIndex - 1];
									colValue = colValue.trim();
								}
								temp_cols_value.add(colValue);
								if (colObj.neverDrilled) {
									colValue = colObj.col_name;
									colValue = colValue.trim();
								} else {
									colValue = colsValues[aryIndex];
									colValue = colValue.trim();
								}
								temp_cols_value.add(colValue);
							}
							if (expandedAll) {
								List levels = null;
								if (null != colsLevels)
									levels = (List) colsLevels
											.get(colObj.col_id);
								if (null != levels) {
									Iterator temp_iter = levels.iterator();
									while (temp_iter.hasNext()) {
										temp_iter.next();
										col_index++;
										aryIndex = col_index + dimsNum + 1;
										dimsNum++;
										sum_index++;
										colValue = colsValues[aryIndex - 1];
										colValue = colValue.trim();
										temp_cols_value.add(colValue);
										colValue = colsValues[aryIndex];
										colValue = colValue.trim();
										temp_cols_value.add(colValue);
									}
								}
							}
							msu_index = col_index + dimsNum;
							sum_index = col_index + dimsNum;
						} else {
							// 指标列
							// 获取列值
							msu_index++;
							sum_index++;
							String colValue = colsValues[msu_index];
							colValue = colValue.trim();
							temp_cols_value.add(colValue);
							// 开始加上占比
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_comratio)) {
								String ratio = NullProcFactory
										.transNullToZero(Arith.div(colValue,
												sumValues[sum_index], 4));
								temp_cols_value.add(ratio);
							}
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_last)) {
								// 同比上期值
								msu_index++;
								colValue = colsValues[msu_index];
								sum_index++;
								if (ConstantKeys.YES
										.equalsIgnoreCase(colObj.has_loop)) {
									// 环比上期值
									msu_index++;
									colValue = colsValues[msu_index];
									sum_index++;
								}
								// 同比值
								msu_index++;
								colValue = colsValues[msu_index];
								sum_index++;
							}
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_loop)) {
								if (ConstantKeys.YES
										.equalsIgnoreCase(colObj.has_last)) {
									// 前面已经加上了
									// 环比值
									msu_index++;
									colValue = colsValues[msu_index];
									sum_index++;
								} else {
									// 环比上期值
									msu_index++;
									colValue = colsValues[msu_index];
									temp_cols_value.add(colValue);
									sum_index++;
									// 环比
									msu_index++;
									colValue = colsValues[msu_index];
									temp_cols_value.add(colValue);
									sum_index++;
								}
							}
						}
					}
				}
				Object[] tmpObj = temp_cols_value.toArray();
				colsValues = new String[tmpObj.length];
				for (int i = 0; i < tmpObj.length; i++) {
					colsValues[i] = (String) tmpObj[i];
				}
				rowObj.cols_value = colsValues;
				retContent.add(rowObj);
			}
		}
		return retContent;
	}

	/**
	 * 取列值的某一列的值 如果是ismin,且分母为0时，返回无穷小，如果isMax且分母为0时返回无穷大,
	 * 其他情况正常返回，如果返回null，直接不能显示
	 * 
	 * @param cols_value
	 *            所有列值
	 * @param index
	 *            列的索引
	 * @return 列值
	 */
	private String getColValue(String[] cols_value, String index,
			boolean isCamRatio) {
		String value = null;
		if (null != cols_value && null != index) {
			int real_index = -1;
			int msu_index = -1;
			int dims_count = 0;
			Iterator iter = cols.iterator();
			String temp_value = null;
			while (iter.hasNext()) {
				real_index++;
				UiCommonTabledictTable colObj = (UiCommonTabledictTable) iter
						.next();
				if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_display)) {
					// 首先是显示列
					if (ConstantKeys.NO.equalsIgnoreCase(colObj.is_measure)) {
						// 维度值
						if (ConstantKeys.YES
								.equalsIgnoreCase(colObj.isExpandCol)) {
							dims_count++;
							real_index++;
							dims_count++;
							msu_index = real_index + dims_count;
						} else {
							dims_count++;
							msu_index = real_index + dims_count;
						}
					} else {
						// 指标
						msu_index++;
						// 本期值
						temp_value = cols_value[msu_index];
						if (!isCamRatio && (colObj.col_id).equals(index)) {
							value = temp_value;
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.is_calByOther)) {
								String cmd = colObj.calObj.cal_cmd;
								int pos = cmd.indexOf("/");
								if (pos >= 0) {
									// 要找出分母，这里很难办，如果分母带有括号怎么办
									// 不考虑了，太复杂
									String rest = cmd.substring(pos + 1);
									// 判断是否有其他符号
									pos = rest.indexOf("*");
									if (pos >= 0) {
										rest = rest.substring(0, pos);
									}
									pos = rest.indexOf("+");
									if (pos >= 0) {
										rest = rest.substring(0, pos);
									}
									pos = rest.indexOf("-");
									if (pos >= 0) {
										rest = rest.substring(0, pos);
									}
									pos = rest.indexOf("/");
									if (pos >= 0) {
										rest = rest.substring(0, pos);
									}
									rest = rest.toUpperCase();
									pos = rest.indexOf("::MSU");
									if (pos >= 0) {
										rest = rest.substring(pos
												+ "::MSU".length());
										pos = rest.indexOf("::");
										if (pos >= 0) {
											// 下标
											rest = rest.substring(0, pos);
											if (isMinFilterValue()
													&& ConstantKeys.ZERO
															.equals(getColValue(
																	cols_value,
																	rest, false))
													&& ConstantKeys.ZERO
															.equals(value)) {
												value = Integer.MIN_VALUE + "";
											}
											if (isMaxFilterValue()
													&& ConstantKeys.ZERO
															.equals(getColValue(
																	cols_value,
																	rest, false))
													&& ConstantKeys.ZERO
															.equals(value)) {
												value = Integer.MAX_VALUE + "";
											}
										}
									}
								}
							}
							break;
						}
						if (ConstantKeys.YES
								.equalsIgnoreCase(colObj.has_comratio)) {
							msu_index++;
							// 占总比
							temp_value = cols_value[msu_index];
							if (isCamRatio && (colObj.col_id).equals(index)) {
								value = temp_value;
								break;
							}
						}
						if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_last)) {
							msu_index++;
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_loop)) {
								msu_index++;
							}
							msu_index++;
						}
						if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_loop)) {
							if (ConstantKeys.NO
									.equalsIgnoreCase(colObj.has_last)) {
								msu_index++;
							}
							msu_index++;
						}
					}
				}
			}
		}
		return value;
	}

	/**
	 * 获取实际的过虑值
	 * 
	 * @param cols_value
	 *            列值列表
	 * @param oper_index
	 *            操作串
	 * @return 值
	 */
	private String getFilterValue(String[] cols_value, String oper_index) {
		String value = null;
		if (null != cols_value && null != oper_index) {
			String firOp = null;
			String secOp = null;
			char oper = oper_index.charAt(0);
			switch (oper) {
			case 'C':
				// 取占比
				firOp = oper_index.substring(1);
				secOp = null;
				value = getColValue(cols_value, firOp, true);
				break;
			case 'D':
				// 进行除法运算
				String temp = oper_index.substring(1);
				int pos = temp.indexOf("/");
				if (pos >= 0) {
					firOp = temp.substring(0, pos);
					secOp = temp.substring(pos + 1);
					value = NullProcFactory.transNullToZero(Arith.divs(
							getColValue(cols_value, firOp, false),
							getColValue(cols_value, secOp, false), 4));
				}
				break;
			case 'G':
				// 取某个值
				firOp = oper_index.substring(1);
				value = getColValue(cols_value, firOp, false);
				break;
			default:
				break;
			}
		}
		return value;
	}

	public String getHeight() {
		return height;
	}

	public String getHrefLinkClass() {
		return hrefLinkClass;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getIndentSpace() {
		return indentSpace;
	}

	public String getOddTRClass() {
		return oddTRClass;
	}

	public int getOrderIndex() {
		return orderIndex;
	}

	public String getRatioDownGreenGif() {
		return ratioDownGreenGif;
	}

	public String getRatioDownRedGif() {
		return ratioDownRedGif;
	}

	public String getRatioRiseGreenGif() {
		return ratioRiseGreenGif;
	}

	public String getRatioRiseRedGif() {
		return ratioRiseRedGif;
	}

	public String getRow_id() {
		return row_id;
	}

	public String getSortAscImgName() {
		return sortAscImgName;
	}

	public String getSortDataType() {
		return sortDataType;
	}

	public String getSortDescmgName() {
		return sortDescmgName;
	}

	public int getSortedColum() {
		return sortedColum;
	}

	public String getSortGenImgName() {
		return sortGenImgName;
	}

	public String getSortJSFun() {
		return sortJSFun;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public String getSqlHaving() {
		return sqlHaving;
	}

	private String getSumColValue(String index, String[] sumValues) {
		String value = null;
		if (null != index && null != sumValues) {
			Iterator iter = cols.iterator();
			int msu_index = dimsCount * 2 - 1;
			while (iter.hasNext()) {
				UiCommonTabledictTable colObj = (UiCommonTabledictTable) iter
						.next();
				if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_measure)) {
					// 是指标
					msu_index++;
					// 开始判断是否是要取得值
					if (index.equals(colObj.col_id)) {
						value = sumValues[msu_index];
						break;
					}
					// 开始处理下标移动,上面求和时只移动了比率
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_last)) {
						msu_index++;
						if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_loop)) {
							msu_index++;
						}
						msu_index++;
					}
					if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_loop)) {
						if (ConstantKeys.NO.equalsIgnoreCase(colObj.has_last)) {
							msu_index++;
						}
						msu_index++;
					}
				}
			}
		}
		return value;
	}

	public String getTabHeadTDClass() {
		return tabHeadTDClass;
	}

	public String getTabHeadTDEndClass() {
		return tabHeadTDEndClass;
	}

	public String getTabHeadTDProHTML() {
		return tabHeadTDProHTML;
	}

	public String getTabHeadTRClass() {
		return tabHeadTRClass;
	}

	/**
	 * 获取表体
	 * 
	 * @return
	 */
	public String[] getTableBody(HttpSession session) {
		long start = System.currentTimeMillis();
		String[] body = null;
		List bodyContent = null;
		List temp_content = null;
		if (null != filter_indexs && null != filter_level
				&& null != filter_values) {
			if (!firstFiltered && null != filteredContent) {
				content = filteredContent;
			} else {
				content = null;
			}
			temp_content = new ArrayList();
		}
		if (null == content) {
			// 既有不过虑情况，又有过虑条件
			genDefalutContent(session);
		}
		if (null != content) {
			bodyContent = content;
		}
		if (null != bodyContent) {
			List tempBodyLeft = new ArrayList();
			List tempBodyRight = new ArrayList();
			StringBuffer tableBody = new StringBuffer("");
			StringBuffer tableBodyLeft = new StringBuffer("");
			StringBuffer tableBodyRight = new StringBuffer("");
			tableBodyLeft.append("<tr valign=\"top\">\n");
			tableBodyLeft.append("<td height=\"100%\">\n");
			tableBodyLeft
					.append("<div id=\"LayerLeft1\"")
					.append("style=\"position:absolute; width:100%; "
							+ "z-index:1; overflow: hidden; height: 100%;\">\n");
			tableBodyLeft.append("<table width=\"100%\" border=\"0\" ")
					.append("cellpadding=\"0\" cellspacing=\"0\"")
					.append("class=\"iTable\" id=\"iTable_LeftTable1\">\n");
			tableBodyRight.append("</table>\n");
			tableBodyRight.append("</div>");
			tableBodyRight.append("</td>");
			tableBodyRight.append("<td align=\"left\">");
			tableBodyRight.append("<div id=\"LayerRight1\" "
					+ "style=\"position:absolute; width:100%; "
					+ "z-index:1; overflow: auto; height: 100%;\" "
					+ "onscroll=\"syncScroll()\">");
			tableBodyRight.append("<table width=\"100%\" border=\"0\" "
					+ "cellpadding=\"0\" cellspacing=\"0\" "
					+ "class=\"iTable\" id=\"iTable_ContentTable1\">");
			StringBuffer exportBody = new StringBuffer("");
			Iterator iter = content.iterator();
			int index = -1;
			int filter_index = -1;
			while (iter.hasNext()) {
				index++;
				RowStruct rowObj = (RowStruct) iter.next();
				if (null != filter_indexs && null != filter_level
						&& null != filter_values) {
					if (null == filteredContent) {
						boolean displayed = rowCanDisplayed(rowObj);
						if (!displayed) {
							// 继续
							continue;
						} else {
							filter_index++;
							temp_content.add(rowObj);
							index = filter_index;
						}
					}
				}
				String[] cols_value = rowObj.cols_value;
				// 开始奇数、偶数行样式
				String tdBeginClass = "";
				String tdEndClass = "";
				String chartStr = "";
				String chartStrRight = "";
				if (ConstantKeys.YES.equalsIgnoreCase(tabObj.is_rowclk)) {
					// 点击行图形变化
					chartStrRight = "chartchange('"
							+ tabObj.chart_ids
							+ "','&chart_name=::chart_name::::state::',jin,'::ROW_ID::');";
					chartStr = " onClick=\"" + chartStrRight + "\"";
				}
				// 点击行打开子页面
				if (ConstantKeys.YES.equalsIgnoreCase(tabObj.hasSubpage)) {
					chartStrRight += "subpage('" + tabObj.table_id + "','"
							+ tabObj.subpage_id + "','" + tabObj.subpage_url
							+ "','" + cols_value[0] + "');";// 将第一行分析角度的id值传过去；
					chartStrRight = " onClick=\"" + chartStrRight + "\"";
				}

				if (index % 2 == 0) {
					tableBodyLeft.append("<tr id=\"::L_ROW_ID::\" class=\"")
							.append(oddTRClass).append("\" ").append(chartStr)
							.append(trRest).append(">\n");
					tableBodyRight
							.append("<tr id=\"::R_ROW_ID::\"  onMouseOver=\"switchClass(this)\" onMouseOut=\"switchClass(this)\" style=\"cursor:hand\" class=\"")
							.append(oddTRClass).append("\" ")// .append(chartStrRight)
							.append(trRest).append(">\n");
					tdBeginClass = tabTDOddClass;
					tdEndClass = tabTDOddEndClass;
				} else {
					tableBodyLeft.append("<tr id=\"::L_ROW_ID::\" class=\"")
							.append(evenTRClass).append("\" ").append(chartStr)
							.append(trRest).append(">\n");
					tableBodyRight
							.append("<tr id=\"::R_ROW_ID::\" onMouseOver=\"switchClass(this)\" onMouseOut=\"switchClass(this)\" style=\"cursor:hand\" class=\"")
							.append(evenTRClass).append("\" ")// .append(chartStrRight)
							.append(trRest).append(">\n");
					tdBeginClass = tabTDEvenClass;
					tdEndClass = tabTDEvenEndClass;
				}
				exportBody.delete(0, exportBody.length());
				exportBody.append("<tr>\n");
				// 单元格样式
				String tdClass = "";

				int col_index = -1;
				int dimsNum = 0;
				int msu_index = -1;
				StringBuffer dimWhere = new StringBuffer("");
				String dim_tip = "";
				String chartName = "";
				String chart_name = "";
				Iterator col_iter = rowObj.colObjs.iterator();
				while (col_iter.hasNext()) {
					col_index++;
					// 开始
					UiCommonTabledictTable colObj = (UiCommonTabledictTable) col_iter
							.next();
					String chartLink = "";
					// 单元格样式
					if (col_index == (rowObj.colObjs.size() - 1 - notDisplayCols))
						tdClass = tdEndClass;
					else
						tdClass = tdBeginClass;
					// 判断是否为NULL
					if (null != colObj
							&& ConstantKeys.YES
									.equalsIgnoreCase(colObj.is_display)) {
						if (ConstantKeys.NO.equalsIgnoreCase(colObj.is_measure)) {

							tdClass = tdBeginClass;
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.is_cellclk)) {
								chartLink += "javascript:subpage('"
										+ tabObj.table_id + "','"
										+ tabObj.subpage_id + "','"
										+ tabObj.subpage_url + "','"
										+ cols_value[0] + "');\"";// 将第一行分析角度的id值传过去；
								/*
								 * chartLink = " javascript:chartchange('" +
								 * colObj.cell_chart_ids +
								 * "','&chart_name=::chart_name::::state::" +
								 * "',chun,'::ROW_ID::')\"";
								 */
								chartLink = "<a href=\"" + chartLink
										+ " class=\"" + hrefLinkClass + "\">";
							}
							String temp_value = "";
							// 维度列
							// 在数组中的实际下标
							if (expandedAll) {

								// 全部展开
								int aryIndex = col_index + dimsNum + 1;
								dimsNum++;

								if (ConstantKeys.YES
										.equalsIgnoreCase(colObj.isExpandCol)) {
									String colValue = cols_value[aryIndex];
									dim_tip += colValue + ",";
									colValue = colValue.trim();
									if ("".equals(colValue))
										colValue = "&nbsp;";
									temp_value = colValue;
									String dim_value = cols_value[aryIndex - 1];
									String temp_state = "&"
											+ colObj.code_field.toLowerCase()
											+ "=" + dim_value;
									List levels = null;
									if (null != colsLevels)
										levels = (List) colsLevels
												.get(colObj.col_id);
									if (null != levels) {
										Iterator temp_iter = levels.iterator();
										while (temp_iter.hasNext()) {
											dimsNum++;
											UiCommonDimhierarchyTable levObj = (UiCommonDimhierarchyTable) temp_iter
													.next();
											aryIndex = col_index + dimsNum + 1;
											String lev_value = cols_value[aryIndex - 1];
											temp_state += "&"
													+ levObj.src_idfld
															.toLowerCase()
													+ "=" + lev_value;
											dimsNum++;
										}
										dimsNum = dimsNum - 2 * levels.size();
									}
									tableBodyLeft
											.append("<td nowrap align=\"left\" class=\"")
											.append(tdClass).append("\">\n");
									exportBody.append(
											"<td nowrap align=\"left\"")
											.append(">\n");
									// 加上名称
									if (null != chartLink
											&& !"".equals(chartLink)) {
										temp_state = temp_state + "::state::";
										// System.out.println("temp_state======="+temp_state);
										String temp_chartLink = chartLink
												.replaceAll("::state::",
														temp_state);
										temp_chartLink = temp_chartLink
												.replaceAll("::chart_name::",
														colValue);
										tableBodyLeft
												.append((temp_chartLink == null ? ""
														: temp_chartLink));
										tableBodyLeft.append(colValue);
										tableBodyLeft.append("</a>");
										tableBodyLeft.append(genMsuSupSpace(
												colObj.col_name + "   ",
												temp_value));
										tableBodyLeft.append("</td>\n");
										exportBody.append(colValue);
										exportBody.append("</td>\n");
									} else {
										if (ConstantKeys.YES
												.equalsIgnoreCase(colObj.has_link)
												&& colObj.level
														.equals(ConstantKeys.ZERO)) {
											// 数字有链接
											colValue = genHtmlLink(colObj,
													colValue);
										}
										tableBodyLeft.append(colValue);
										tableBodyLeft.append(genMsuSupSpace(
												colObj.col_name, temp_value));
										tableBodyLeft.append("</td>\n");
										exportBody.append(colValue);
										exportBody.append("</td>\n");
									}
									// 先将最上层显示
									levels = null;
									if (null != colsLevels)
										levels = (List) colsLevels
												.get(colObj.col_id);
									if (null != levels) {
										Iterator temp_iter = levels.iterator();
										while (temp_iter.hasNext()) {
											dimsNum++;
											temp_iter.next();
											aryIndex = col_index + dimsNum + 1;
											colValue = cols_value[aryIndex];
											colValue = colValue.trim();
											dim_tip += colValue + ",";
											if ("".equals(colValue))
												colValue = "&nbsp;";
											temp_value = colValue;
											tableBodyLeft
													.append("<td nowrap align=\"left\" class=\"")
													.append(tdClass)
													.append("\">\n");
											exportBody
													.append("<td nowrap align=\"left\"")
													.append(">\n");
											// 加上名称

											tableBodyLeft.append(colValue);
											tableBodyLeft
													.append(genMsuSupSpace(
															colObj.col_name,
															temp_value));
											tableBodyLeft.append("</td>\n");
											exportBody.append(colValue);
											exportBody.append("</td>\n");
											dimsNum++;
										}
									}
								} else {
									String colValue = cols_value[aryIndex];
									colValue = colValue.trim();
									dim_tip += colValue + ",";
									if ("".equals(colValue))
										colValue = "&nbsp;";
									temp_value = colValue;
									String dimValue = cols_value[aryIndex - 1];
									tableBodyLeft
											.append("<td nowrap align=\"left\" class=\"")
											.append(tdClass).append("\">\n");
									exportBody
											.append("<td nowrap align=\"left\">\n");
									dimWhere.append("&").append(
											colObj.code_field.toLowerCase());
									dimWhere.append("=").append(dimValue);
									// 加上名称
									if (null != chartLink
											&& !"".equals(chartLink)) {
										chartName = colValue;
										chartLink = chartLink.replaceAll(
												"::chart_name::", chartName);
										tableBodyLeft
												.append((chartLink == null ? ""
														: chartLink));
										tableBodyLeft.append(colValue);
										tableBodyLeft.append("</a>");
										tableBodyLeft.append(genMsuSupSpace(
												colObj.col_name, temp_value));
										tableBodyLeft.append("</td>\n");
										exportBody.append(colValue);
										exportBody.append("</td>\n");
									} else {
										if (ConstantKeys.YES
												.equalsIgnoreCase(colObj.has_link)
												&& colObj.level
														.equals(ConstantKeys.ZERO)) {
											// 数字有链接
											colValue = genHtmlLink(colObj,
													colValue);
										}
										tableBodyLeft.append(colValue);
										tableBodyLeft.append(genMsuSupSpace(
												colObj.col_name, temp_value));
										tableBodyLeft.append("</td>\n");
										exportBody.append(colValue);
										exportBody.append("</td>\n");
									}
								}
								msu_index = col_index + dimsNum;
							} else if (null != filter_indexs
									&& null != filter_level
									&& null != filter_values) {
								// 过虑情况
								int aryIndex = col_index + dimsNum + 1;
								dimsNum++;

								if (ConstantKeys.YES
										.equalsIgnoreCase(colObj.isExpandCol)) {
									String colValue = cols_value[aryIndex];
									colValue = colValue.trim();
									dim_tip += colValue + ",";
									if ("".equals(colValue))
										colValue = "&nbsp;";
									temp_value = colValue;
									String dim_value = cols_value[aryIndex - 1];
									String temp_state = "";
									List levels = null;
									if (null != colsLevels)
										levels = (List) colsLevels
												.get(colObj.col_id);
									if (null != levels) {
										String where = genChartColSearchWhere(
												rowObj, colObj.col_id, "",
												levels);
										if (null != where)
											temp_state += where;
									} else {
										temp_state = "&"
												+ colObj.code_field
														.toLowerCase() + "="
												+ dim_value;
									}
									tableBodyLeft
											.append("<td nowrap align=\"left\" class=\"")
											.append(tdClass).append("\">\n");
									exportBody.append(
											"<td nowrap align=\"left\"")
											.append(">\n");
									// 加上名称
									if (null != chartLink
											&& !"".equals(chartLink)) {
										temp_state = temp_state + "::state::";
										String temp_chartLink = chartLink
												.replaceAll("::state::",
														temp_state);
										temp_chartLink = temp_chartLink
												.replaceAll("::chart_name::",
														colValue);
										tableBodyLeft
												.append((temp_chartLink == null ? ""
														: temp_chartLink));
										tableBodyLeft.append(colValue);
										tableBodyLeft.append("</a>");
										tableBodyLeft.append(genMsuSupSpace(
												colObj.col_name, temp_value));
										tableBodyLeft.append("</td>\n");
										exportBody.append(colValue);
										exportBody.append("</td>\n");
									} else {
										if (ConstantKeys.YES
												.equalsIgnoreCase(colObj.has_link)
												&& colObj.level
														.equals(ConstantKeys.ZERO)) {
											// 数字有链接
											colValue = genHtmlLink(colObj,
													colValue);
										}
										tableBodyLeft.append(colValue);
										tableBodyLeft.append(genMsuSupSpace(
												colObj.col_name, temp_value));
										tableBodyLeft.append("</td>\n");
										exportBody.append(colValue);
										exportBody.append("</td>\n");
									}
									// 先将最上层显示
									levels = null;
									if (null != colsLevels)
										levels = (List) colsLevels
												.get(colObj.col_id);
									if (null != levels) {
										Iterator temp_iter = levels.iterator();
										while (temp_iter.hasNext()) {
											dimsNum++;
											UiCommonDimhierarchyTable levObj = (UiCommonDimhierarchyTable) temp_iter
													.next();
											aryIndex = col_index + dimsNum + 1;
											colValue = cols_value[aryIndex];
											colValue = colValue.trim();
											dim_tip += colValue + ",";
											if ("".equals(colValue))
												colValue = "&nbsp;";
											temp_value = colValue;
											String lev_value = cols_value[aryIndex - 1];
											String lev_state = "&"
													+ levObj.src_idfld
															.toLowerCase()
													+ "=" + lev_value;
											tableBodyLeft
													.append("<td nowrap align=\"left\" class=\"")
													.append(tdClass)
													.append("\">\n");
											exportBody
													.append("<td nowrap align=\"left\"")
													.append(">\n");
											// 加上名称
											if (null != chartLink
													&& !"".equals(chartLink)) {
												lev_state = lev_state
														+ "::state::";
												String temp_chartLink = chartLink
														.replaceAll(
																"::state::",
																lev_state);
												chartName = colValue;
												temp_chartLink = temp_chartLink
														.replaceAll(
																"::chart_name::",
																chartName);
												tableBodyLeft
														.append((temp_chartLink == null ? ""
																: temp_chartLink));
												tableBodyLeft.append(colValue);
												tableBody.append("</a>");
												tableBodyLeft
														.append(genMsuSupSpace(
																colObj.col_name,
																temp_value));
												tableBody.append("</td>\n");
												exportBody.append(colValue);
												exportBody.append("</td>\n");
											} else {
												tableBodyLeft.append(colValue);
												tableBodyLeft
														.append(genMsuSupSpace(
																colObj.col_name,
																temp_value));
												tableBodyLeft.append("</td>\n");
												exportBody.append(colValue);
												exportBody.append("</td>\n");
											}
											dimsNum++;
											break;
										}
									}
								} else {
									String colValue = cols_value[aryIndex];
									colValue = colValue.trim();
									dim_tip += colValue + ",";
									if ("".equals(colValue))
										colValue = "&nbsp;";
									temp_value = colValue;
									String dimValue = cols_value[aryIndex - 1];
									tableBodyLeft
											.append("<td nowrap align=\"left\" class=\"")
											.append(tdClass).append("\">\n");
									exportBody.append(
											"<td nowrap align=\"left\"")
											.append(">\n");
									dimWhere.append("&").append(
											colObj.code_field.toLowerCase());
									dimWhere.append("=").append(dimValue);
									// 加上名称
									if (null != chartLink
											&& !"".equals(chartLink)) {
										chartName = colValue;
										chartLink = chartLink.replaceAll(
												"::chart_name::", chartName);
										tableBodyLeft
												.append((chartLink == null ? ""
														: chartLink));
										tableBodyLeft.append(colValue);
										tableBodyLeft.append("</a>");
										tableBodyLeft.append(genMsuSupSpace(
												colObj.col_name, temp_value));
										tableBodyLeft.append("</td>\n");
										exportBody.append(colValue);
										exportBody.append("</td>\n");
									} else {
										if (ConstantKeys.YES
												.equalsIgnoreCase(colObj.has_link)
												&& colObj.level
														.equals(ConstantKeys.ZERO)) {
											// 数字有链接
											colValue = genHtmlLink(colObj,
													colValue);
										}
										tableBodyLeft.append(colValue);
										tableBodyLeft.append(genMsuSupSpace(
												colObj.col_name, temp_value));
										tableBodyLeft.append("</td>\n");
										exportBody.append(colValue);
										exportBody.append("</td>\n");
									}
								}
								msu_index = col_index + dimsNum;

							} else {

								int aryIndex = col_index + dimsNum + 1;
								dimsNum++;
								msu_index = col_index + dimsNum;
								String colValue = null;
								String exportValue = null;
								String dimValue = null;
								if (colObj.neverDrilled) {
									colValue = colObj.col_name;
									colValue = colValue.trim();
									dim_tip += colValue + ",";
									if ("".equals(colValue))
										colValue = "&nbsp;";
								} else {
									colValue = cols_value[aryIndex];
									colValue = colValue.trim();
									dim_tip += colValue + ",";
									if ("".equals(colValue))
										colValue = "&nbsp;";
									dimValue = cols_value[aryIndex - 1];
								}
								temp_value = colValue;
								exportValue = colValue;
								if (ConstantKeys.YES
										.equalsIgnoreCase(colObj.isExpandCol)) {
									dimWhere.append("&level=").append(
											colObj.level);
									chart_name = colValue;
									// 生成所有的条件值，要有个临时变量
									// 当遇到相同水平的值时要去掉上次的
									// 这样就在行替换时可以了
									List levels = null;
									if (null != colsLevels)
										levels = (List) colsLevels
												.get(colObj.col_id);
									if (null != levels) {
										String where = genChartColSearchWhere(
												rowObj, colObj.col_id, "",
												levels);
										if (null != where)
											dimWhere.append(where);
									} else {
										dimWhere.append("&")
												.append(colObj.code_field
														.toLowerCase())
												.append("=").append(dimValue);
									}
									if (null != chartLink
											&& !"".equals(chartLink)) {
										chartName = colValue;
										chartLink = chartLink.replaceAll(
												"::chart_name::", chartName);
										colValue = (chartLink == null ? ""
												: chartLink) + colValue;
										colValue += "</a>";
									} else {
										if (ConstantKeys.YES
												.equalsIgnoreCase(colObj.has_link)
												&& colObj.level
														.equals(ConstantKeys.ZERO)) {
											// 数字有链接
											colValue = genHtmlLink(colObj,
													colValue);
										}
									}
								} else {
									dimWhere.append("&")
											.append(colObj.code_field
													.toLowerCase()).append("=")
											.append(dimValue);
									if (null != chartLink
											&& !"".equals(chartLink)) {
										chartName = colValue;
										chartLink = chartLink.replaceAll(
												"::chart_name::", chartName);
										colValue = (chartLink == null ? ""
												: chartLink) + colValue;
										colValue += "</a>";
									} else {
										if (ConstantKeys.YES
												.equalsIgnoreCase(colObj.has_link)
												&& colObj.level
														.equals(ConstantKeys.ZERO)) {
											// 数字有链接
											colValue = genHtmlLink(colObj,
													colValue);
										}
									}
								}
								String index_space = "";
								if ((col_index) == expandIndex) {
									index_space = genIndentSpace(colObj, true);
								} else {
									index_space = genIndentSpace(colObj, false);
								}
								if (col_index == expandIndex
										&& null != row_id
										&& null != rowObj.row_id
										&& row_id
												.equalsIgnoreCase(rowObj.row_id)) {

									// 刚刚的展开行的列单元，变为折叠形式
									if (colObj.neverDrilled) {
										index_space = "";
									}
									if (!banExpand) {
										if (rowObj.expanded) {
											colValue = index_space
													+ genCollapseLink(
															expandIndex,
															rowObj.row_id)
													+ colValue;
										} else {
											colValue = index_space
													+ genExpandLink(
															expandIndex,
															rowObj.row_id)
													+ colValue;
										}
									}
								} else {
									if (colObj.neverDrilled) {
										index_space = "";
									}
									if (!banExpand) {
										if (colObj.neverDrilled) {
											colValue = index_space
													+ genExpandLink(col_index,
															rowObj.row_id)
													+ colValue;
										} else {
											List levels = null;
											if (null != colsLevels)
												levels = (List) colsLevels
														.get(colObj.col_id);
											if (null != levels) {
												UiCommonDimhierarchyTable nextLevObj = TableColUtil
														.getNextLevelObj(
																levels,
																colObj.level);
												if (null != nextLevObj) {
													// 获取展开层次
													if (rowObj.expanded) {
														colValue = index_space
																+ genCollapseLink(
																		col_index,
																		rowObj.row_id)
																+ colValue;
													} else {
														colValue = index_space
																+ genExpandLink(
																		col_index,
																		rowObj.row_id)
																+ colValue;
													}
												} else {
													colValue = indentSpace
															+ index_space
															+ colValue;
												}

											}
										}
									}
								}

								tableBodyLeft
										.append("<td nowrap align=\"left\" class=\"")
										.append(tdClass).append("\">\n");
								exportBody
										.append("<td nowrap align=\"left\">\n");
								// 加上名称
								tableBodyLeft.append(colValue);
								tableBodyLeft.append(genMsuSupSpace(
										colObj.col_name, temp_value));
								tableBodyLeft.append("</td>\n");
								exportValue = index_space + exportValue;
								exportBody.append(exportValue);
								exportBody.append("</td>\n");
							}
						} else {
							// 指标列
							tdClass = tdBeginClass;
							tableBodyRight
									.append("<td nowrap align=\"right\" class=\"")
									.append(tdClass).append("\" title=\"")
									.append(genTip(colObj)).append("\">\n");
							exportBody.append("<td nowrap align=\"right\">\n");
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.is_cellclk)) {
								chartLink = "chartchange('"
										+ colObj.cell_chart_ids
										+ "','&chart_name=::chart_name::::state::&msu_fld="
										+ colObj.code_field.toLowerCase()
										+ "','" + colObj.col_rlt_chart_target
										+ "','::ROW_ID::')\"";
								chartLink = "<a href=\"javascript:" + chartLink
										+ " class=\"" + hrefLinkClass + "\">";
							}
							// 获取列值
							msu_index++;
							String colValue = cols_value[msu_index];
							colValue = colValue.trim();

							colValue = formatColValue(colObj, colValue);
							String exportValue = colValue;
							if (null != chartLink && !"".equals(chartLink)) {
								chartName = colObj.col_name;
								chartLink = chartLink.replaceAll(
										"::chart_name::", chartName);
								colValue = chartLink + colValue;
								colValue += "</a>";
							}
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_link)) {
								// 数字有链接
								colValue = genHtmlLink(colObj, colValue);
							}
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_comratio)) {
								// 有占比
								msu_index++;
							}
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_last)
									&& ConstantKeys.NO
											.equalsIgnoreCase(colObj.loop_display)) {
								// 具有同比
								// 定位到同比上期值
								msu_index++;
								if (ConstantKeys.YES
										.equalsIgnoreCase(colObj.has_loop)) {
									// 定位到环比上期值
									msu_index++;
								}
								// 定位到同比
								msu_index++;
								String lastRatio = cols_value[msu_index];
								try {
									double ratio = Double
											.parseDouble(lastRatio);
									if (ratio > 0
											&& ConstantKeys.RATIO_RISE_GREEN
													.equalsIgnoreCase(colObj.rise_arrow_color)) {
										colValue += genLastImgHtml(colObj,
												ratioRiseGreenGif, "同比上升变好");
									}
									if (ratio > 0
											&& ConstantKeys.RATIO_RISE_RED
													.equalsIgnoreCase(colObj.rise_arrow_color)) {
										colValue += genLastImgHtml(colObj,
												ratioRiseRedGif, "同比上升变差");
									}
									if (ratio < 0
											&& ConstantKeys.RATIO_RISE_GREEN
													.equalsIgnoreCase(colObj.rise_arrow_color)) {
										colValue += genLastImgHtml(colObj,
												ratioDownRedGif, "同比下降变差");
									}
									if (ratio < 0
											&& ConstantKeys.RATIO_RISE_RED
													.equalsIgnoreCase(colObj.rise_arrow_color)) {
										colValue += genLastImgHtml(colObj,
												ratioDownGreenGif, "同比下降变好");
									}
									if (ratio == 0.0) {
										colValue += genLastImgHtml(colObj,
												ratioZeroGif, "同比不变");
									}
								} catch (NumberFormatException nfe) {

								}
							}
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_loop)
									&& ConstantKeys.NO
											.equalsIgnoreCase(colObj.loop_display)) {
								if (ConstantKeys.NO
										.equalsIgnoreCase(colObj.has_last)) {
									msu_index++;
								}
								// 具有同比
								msu_index++;
								String loopRatio = cols_value[msu_index];
								try {
									double ratio = Double
											.parseDouble(loopRatio);
									if (ratio > 0
											&& ConstantKeys.RATIO_RISE_GREEN
													.equalsIgnoreCase(colObj.rise_arrow_color)) {
										colValue += genLoopImgHtml(colObj,
												ratioRiseGreenGif, "环比上升变好");
									}
									if (ratio > 0
											&& ConstantKeys.RATIO_RISE_RED
													.equalsIgnoreCase(colObj.rise_arrow_color)) {
										colValue += genLoopImgHtml(colObj,
												ratioRiseRedGif, "环比上升变差");
									}
									if (ratio < 0
											&& ConstantKeys.RATIO_RISE_GREEN
													.equalsIgnoreCase(colObj.rise_arrow_color)) {
										colValue += genLoopImgHtml(colObj,
												ratioDownRedGif, "环比下降变差");
									}
									if (ratio < 0
											&& ConstantKeys.RATIO_RISE_RED
													.equalsIgnoreCase(colObj.rise_arrow_color)) {
										colValue += genLoopImgHtml(colObj,
												ratioDownGreenGif, "环比下降变好");
									}
									if (ratio == 0.0) {
										colValue += genLoopImgHtml(colObj,
												ratioZeroGif, "环比不变");
									}
								} catch (NumberFormatException nfe) {

								}
							}

							tableBodyRight.append(genMsuSupSpace(
									colObj.col_name, exportValue));
							tableBodyRight.append(colValue);
							tableBodyRight.append("</td>\n");
							exportBody.append(exportValue);
							exportBody.append("</td>\n");
							// 本期值完成，如果这时有同比或环比，下标已经移动了，先返回来
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_comratio)) {
								if (ConstantKeys.YES
										.equalsIgnoreCase(colObj.has_comratio)) {
									msu_index--;
								}
								if (ConstantKeys.YES
										.equalsIgnoreCase(colObj.has_last)
										&& ConstantKeys.NO
												.equalsIgnoreCase(colObj.loop_display)) {
									msu_index--;
									if (ConstantKeys.YES
											.equalsIgnoreCase(colObj.has_loop)
											&& ConstantKeys.NO
													.equalsIgnoreCase(colObj.loop_display)) {
										msu_index--;
									}
									msu_index--;
								}
								if (ConstantKeys.YES
										.equalsIgnoreCase(colObj.has_loop)
										&& ConstantKeys.NO
												.equalsIgnoreCase(colObj.loop_display)) {
									if (ConstantKeys.NO
											.equalsIgnoreCase(colObj.has_last)
											&& ConstantKeys.NO
													.equalsIgnoreCase(colObj.loop_display)) {
										msu_index--;
									}
									msu_index--;
								}
							}
							// 现在下标已经是本期值了
							// 判断是否有占比
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_comratio)) {

								tdClass = tdBeginClass;
								// 有占比
								msu_index++;
								String ratio = cols_value[msu_index];
								ratio = genMsuTDPercentHtml(ratio);
								// 开始显示
								tableBodyRight
										.append("<td nowrap align=\"right\" class=\"")
										.append(tdClass).append("\"  title=\"")
										.append(genTip(colObj)).append("\">\n");
								tableBodyRight.append(genMsuSupSpace("占总比",
										ratio));
								tableBodyRight.append(ratio);
								tableBodyRight.append("</td>\n");
								exportBody
										.append("<td nowrap align=\"right\">\n");
								exportBody.append(ratio);
								exportBody.append("</td>\n");
							}
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_last)
									&& ConstantKeys.YES
											.equalsIgnoreCase(colObj.last_display)) {

								tdClass = tdBeginClass;
								// 有占比
								msu_index++;
								if (ConstantKeys.YES
										.equalsIgnoreCase(colObj.has_loop)
										&& ConstantKeys.YES
												.equalsIgnoreCase(colObj.loop_display)) {
									msu_index++;
								}
								msu_index++;
								String ratio = cols_value[msu_index];
								ratio = genMsuTDPercentHtml(ratio);
								// 开始显示
								tableBodyRight
										.append("<td nowrap align=\"right\" class=\"")
										.append(tdClass).append("\" title=\"")
										.append(genTip(colObj)).append("\">\n");
								tableBodyRight.append(genMsuSupSpace("同比",
										ratio));
								tableBodyRight.append(ratio);
								tableBodyRight.append("</td>\n");
								exportBody
										.append("<td nowrap align=\"right\">\n");
								exportBody.append(ratio);
								exportBody.append("</td>\n");
							}
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_loop)
									&& ConstantKeys.YES
											.equalsIgnoreCase(colObj.loop_display)) {

								tdClass = tdBeginClass;
								if (ConstantKeys.NO
										.equalsIgnoreCase(colObj.has_last)
										&& ConstantKeys.NO
												.equalsIgnoreCase(colObj.last_display)) {
									msu_index++;
								}
								// 有占比
								msu_index++;
								String ratio = cols_value[msu_index];
								ratio = genMsuTDPercentHtml(ratio);
								// 开始显示
								tableBodyRight
										.append("<td nowrap align=\"right\" class=\"")
										.append(tdClass).append("\" title=\"")
										.append(genTip(colObj)).append("\">\n");
								tableBodyRight.append(genMsuSupSpace("环比",
										ratio));
								tableBodyRight.append(ratio);
								tableBodyRight.append("</td>\n");
								exportBody
										.append("<td nowrap align=\"right\">\n");
								exportBody.append(ratio);
								exportBody.append("</td>\n");
							}
						}
					}
				}
				// 到最后了
				tableBodyRight.append("</tr>");
				tableBodyLeft.append("</tr>");
				exportBody.append("</tr>");
				String temp_str = tableBodyLeft.toString();
				temp_str = temp_str
						.replaceAll("::state::", dimWhere.toString());
				temp_str = temp_str.replaceAll("::chart_name::", chart_name);
				temp_str = temp_str.replaceAll("::ROW_ID::", rowObj.row_id);
				temp_str = temp_str.replaceAll("::L_ROW_ID::", "L_"
						+ rowObj.row_id);
				int pos = dim_tip.lastIndexOf(",");
				if (pos >= 0) {
					dim_tip = dim_tip.substring(0, pos);
				}
				temp_str = temp_str.replaceAll("::DIM_NAME::", dim_tip);
				tableBodyLeft.delete(0, tableBodyLeft.length());
				tableBodyLeft.append(temp_str);

				temp_str = tableBodyRight.toString();
				temp_str = temp_str
						.replaceAll("::state::", dimWhere.toString());
				temp_str = temp_str.replaceAll("::chart_name::", chart_name);
				temp_str = temp_str.replaceAll("::ROW_ID::", rowObj.row_id);
				temp_str = temp_str.replaceAll("::R_ROW_ID::", "R_"
						+ rowObj.row_id);
				pos = dim_tip.lastIndexOf(",");
				if (pos >= 0) {
					dim_tip = dim_tip.substring(0, pos);
				}
				temp_str = temp_str.replaceAll("::DIM_NAME::", dim_tip);
				tableBodyRight.delete(0, tableBodyRight.length());
				tableBodyRight.append(temp_str);

				tempBodyLeft.add(tableBodyLeft.toString());
				tempBodyRight.add(tableBodyRight.toString());
				exportHTML.add(exportBody.toString());
				tableBodyRight = new StringBuffer("");
				tableBodyLeft = new StringBuffer("");
				exportBody = new StringBuffer("");
			}// 行循环结束
			if (null != filter_indexs && null != filter_level
					&& null != filter_values) {
				if (firstFiltered) {
					filteredContent = temp_content;
					String[] temp = genTempSumHtml(temp_content);
					if (null != temp && temp.length >= 2) {
						tableBodyLeft.append(temp[0]);
						tableBodyRight.append(temp[1]);
						hasCotent = true;
					} else {
						hasCotent = false;
					}
				} else {
					String[] temp = genTempSumHtml(filteredContent);
					if (null != temp && temp.length >= 2) {
						String temp1 = temp[0];
						String temp2 = temp[1];
						temp1 = processExportSumTR(temp1);
						temp2 = processExportSumTR(temp2);
						exportBody.append("<tr nowrap>");
						exportBody.append(temp1).append(temp2);
						exportBody.append("</tr>");
						tableBodyLeft.append(temp[0]);
						tableBodyRight.append(temp[1]);
						hasCotent = true;
					} else {
						hasCotent = false;
					}
				}
			} else {
				String[] temp = genSumHtml();
				if (null != temp && temp.length >= 2) {
					String temp1 = temp[0];
					String temp2 = temp[1];
					temp1 = processExportSumTR(temp1);
					temp2 = processExportSumTR(temp2);
					exportBody.append("<tr nowrap>");
					exportBody.append(temp1).append(temp2);
					exportBody.append("</tr>");
					tableBodyLeft.append(temp[0]);
					tableBodyRight.append(temp[1]);
				}
			}

			exportHTML.add(exportBody.toString());
			// 左边要加上一行新的用于调整高度的
			tableBodyLeft.append("<tr>");
			for (int i = 0; i < dimsCount; i++) {
				tableBodyLeft.append("<td>&nbsp;</td>");
			}
			tableBodyLeft.append("</tr>");
			tableBodyRight.append("</table></div></td></tr>");
			tempBodyLeft.add(tableBodyLeft.toString());
			tempBodyRight.add(tableBodyRight.toString());
			Object[] tempObj1 = tempBodyLeft.toArray();
			Object[] tempObj2 = tempBodyRight.toArray();
			body = new String[tempObj1.length + tempObj2.length];
			for (int i = 0; i < tempObj1.length; i++) {
				body[i] = (String) tempObj1[i];
			}
			for (int i = 0; i < tempObj2.length; i++) {
				body[i + tempObj1.length] = (String) tempObj2[i];
			}
		}
		if (!hasCotent) {
			body = null;
		}
		System.out.println("组装表体用时:" + (System.currentTimeMillis() - start)
				+ "ms");
		return body;
	}

	/**
	 * 生成表格表头
	 * 
	 * @return 返回表头
	 */
	public String[] getTableHead(HttpSession session) {

		String temp_head = null;
		if (null != tabObj) {
			if (tabObj.has_head.equalsIgnoreCase(ConstantKeys.YES)
					&& null != tabObj.tabHead
					&& null != tabObj.tabHead.table_header) {
				temp_head = tabObj.tabHead.table_header;
			}
		}

		// ----- 以上部分是具有定义表头的情况

		String[] head = null;
		if (null != cols) {
			StringBuffer exportHead = new StringBuffer("");
			StringBuffer exportTmpHead = new StringBuffer("");
			StringBuffer tableHead = new StringBuffer("");
			StringBuffer tableHeadLeft = new StringBuffer("");
			StringBuffer tableHeadRight = new StringBuffer("");

			tableHeadLeft.append("<table id=\"iTable_LeftHeadTable1\" "
					+ "width=\"100%\" border=\"0\" "
					+ "cellpadding=\"0\" cellspacing=\"0\" "
					+ "class=\"table\">\n");
			tableHeadLeft.append("<tr class=\"").append(tabHeadTRClass)
					.append("\">\n");

			tableHeadRight.append("</td>\n");
			tableHeadRight
					.append("<td width=\"100%\" align=\"left\" valign=\"top\">\n");
			tableHeadRight.append("	<div id=\"Layer1\" "
					+ "style=\"position:absolute; width:100%; z-index:1; "
					+ "overflow: hidden;\">\n");
			tableHeadRight.append("	<table width=\"100%\" border=\"0\" "
					+ "cellpadding=\"0\" cellspacing=\"0\" "
					+ "class=\"iTable\" id=\"iTable_HeadTable1\">\n");
			tableHeadRight.append("<tr class=\"").append(tabHeadTRClass)
					.append("\">\n");
			if (null != temp_head) {
				tableHeadRight.append(temp_head);
				tableHeadRight.append("</tr>\n");
				tableHeadRight.append("<tr class=\"").append(tabHeadTRClass)
						.append("\">\n");
			}
			exportHead.append("<tr>\n");
			exportTmpHead.append("<tr>\n");
			int index = -1;
			String tdClass = "";
			Iterator iter = cols.iterator();
			int real_index = -1;
			int dims_count = 0;
			while (iter.hasNext()) {
				index++;
				UiCommonTabledictTable colObj = (UiCommonTabledictTable) iter
						.next();
				String chartLink = "";
				if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_colclk)) {
					chartLink = " onClick=\"chartchange('"
							+ colObj.col_chart_ids + "','&msu_fld="
							+ colObj.code_field.toLowerCase() + "','"
							+ colObj.col_rlt_chart_target + "')\"";
				}
				String hrefLink = "javascript:chartchange('"
						+ colObj.col_chart_ids + "','&msu_fld="
						+ colObj.code_field_clone.toLowerCase() + "','"
						+ colObj.col_rlt_chart_target + "')";

				if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_display)) {
					realColNum++;
					if (index == (cols.size() - 1 - notDisplayCols))
						tdClass = tabHeadTDEndClass;
					else
						tdClass = tabHeadTDClass;
					// begin
					if (ConstantKeys.NO.equalsIgnoreCase(colObj.is_measure)) {
						real_index = index + dims_count;
						dims_count++;
						if (needAllExpand
								&& ConstantKeys.YES
										.equalsIgnoreCase(colObj.isExpandCol)) {
							// 如果是展开列,生成全部展开按钮
							// 这里暂时考虑两层
							if (!expandedAll) {
								if (null != filter_indexs
										&& null != filter_level
										&& null != filter_values) {
									if (null != temp_head) {
										tableHeadLeft
												.append("<td nowrap rowspan=\"2\" align=\"center\" class=\"")
												.append(tdClass).append("\" ")
												.append(">\n");
										tableHeadLeft.append(colObj.col_name);
										tableHeadLeft.append(genSortStr(
												real_index, colObj));
										tableHeadLeft.append("</td>\n");
										exportTmpHead
												.append("<td nowrap rowspan=\"2\" align=\"center\"")
												.append(">\n");
										exportTmpHead.append(colObj.col_name);
										exportTmpHead.append("</td>\n");
									} else {
										tableHeadLeft
												.append("<td nowrap align=\"center\" class=\"")
												.append(tdClass).append("\" ")
												.append(">\n");
										tableHeadLeft.append(colObj.col_name);
										tableHeadLeft.append(genSortStr(
												real_index, colObj));
										tableHeadLeft.append("</td>\n");
										exportHead.append(
												"<td nowrap align=\"center\"")
												.append(">\n");
										exportHead.append(colObj.col_name);
										exportHead.append("</td>\n");
									}
									List levels = null;
									if (null != colsLevels)
										levels = (List) colsLevels
												.get(colObj.col_id);
									if (null != levels) {
										Iterator temp_iter = levels.iterator();
										while (temp_iter.hasNext()) {
											UiCommonDimhierarchyTable levObj = (UiCommonDimhierarchyTable) temp_iter
													.next();
											dims_count++;
											real_index = index + dims_count - 1;
											if (null != temp_head) {

												tableHeadLeft
														.append("<td nowrap rowspan=\"2\" align=\"center\" class=\"")
														.append(tdClass)
														.append("\" ")
														.append(chartLink)
														.append(">\n");
												tableHeadLeft
														.append(levObj.lev_name);
												tableHeadLeft
														.append(genSortStr(
																real_index,
																colObj));
												tableHeadLeft.append("</td>\n");
												exportTmpHead
														.append("<td nowrap rowspan=\"2\" align=\"center\"")
														.append(">\n");
												exportTmpHead
														.append(levObj.lev_name);
												exportTmpHead.append("</td>\n");
											} else {
												tableHeadLeft
														.append("<td nowrap align=\"center\" class=\"")
														.append(tdClass)
														.append("\" ")
														.append(chartLink)
														.append(">\n");
												tableHeadLeft
														.append(levObj.lev_name);
												tableHeadLeft
														.append(genSortStr(
																real_index,
																colObj));
												tableHeadLeft.append("</td>\n");
												exportHead
														.append("<td nowrap align=\"center\"")
														.append(">\n");
												exportHead
														.append(levObj.lev_name);
												exportHead.append("</td>\n");
											}
											dims_count++;
											break;
										}
									}
								} else {
									String link = genAllExpandLink(index);
									if (null != temp_head) {
										tableHeadLeft
												.append("<td nowrap rowspan=\"2\" align=\"center\" class=\"")
												.append(tdClass).append("\" ")
												.append(chartLink)
												.append(">\n");
										tableHeadLeft.append(link);
										tableHeadLeft.append(colObj.col_name);
										tableHeadLeft.append(genSortStr(
												real_index, colObj));
										tableHeadLeft.append("</td>\n");
										exportTmpHead
												.append("<td nowrap rowspan=\"2\" align=\"center\"")
												.append(">\n");
										exportTmpHead.append(colObj.col_name);
										exportTmpHead.append("</td>\n");
									} else {
										tableHeadLeft
												.append("<td nowrap align=\"center\" class=\"")
												.append(tdClass).append("\" ")
												.append(chartLink)
												.append(">\n");
										tableHeadLeft.append(link);
										tableHeadLeft.append(colObj.col_name);
										tableHeadLeft.append(genSortStr(
												real_index, colObj));
										tableHeadLeft.append("</td>\n");
										exportHead.append(
												"<td nowrap align=\"center\"")
												.append(">\n");
										exportHead.append(colObj.col_name);
										exportHead.append("</td>\n");
									}
								}
							} else {
								String link = genAllCollapseLink(index);
								if (null != temp_head) {
									tableHeadLeft
											.append("<td nowrap rowspan=\"2\" align=\"center\" class=\"")
											.append(tdClass).append("\" ")
											.append(chartLink).append(">\n");
									tableHeadLeft.append(link);
									tableHeadLeft.append(colObj.col_name);
									tableHeadLeft.append(genSortStr(real_index,
											colObj));
									tableHeadLeft.append("</td>\n");
									exportTmpHead
											.append("<td nowrap rowspan=\"2\" align=\"center\"")
											.append(">\n");
									exportTmpHead.append(colObj.col_name);
									exportTmpHead.append("</td>\n");
								} else {
									tableHeadLeft
											.append("<td nowrap align=\"center\" class=\"")
											.append(tdClass).append("\" ")
											.append(chartLink).append(">\n");
									tableHeadLeft.append(link);
									tableHeadLeft.append(colObj.col_name);
									tableHeadLeft.append(genSortStr(real_index,
											colObj));
									tableHeadLeft.append("</td>\n");
									exportHead.append(
											"<td nowrap align=\"center\"")
											.append(">\n");
									exportHead.append(colObj.col_name);
									exportHead.append("</td>\n");
								}
								List levels = null;
								if (null != colsLevels)
									levels = (List) colsLevels
											.get(colObj.col_id);
								if (null != levels) {
									Iterator temp_iter = levels.iterator();
									while (temp_iter.hasNext()) {
										UiCommonDimhierarchyTable levObj = (UiCommonDimhierarchyTable) temp_iter
												.next();
										dims_count++;
										real_index = index + dims_count - 1;
										if (null != temp_head) {

											tableHeadLeft
													.append("<td nowrap rowspan=\"2\" align=\"center\" class=\"")
													.append(tdClass)
													.append("\" ")
													.append(chartLink)
													.append(">\n");
											tableHeadLeft
													.append(levObj.lev_name);
											tableHeadLeft.append(genSortStr(
													real_index, colObj));
											tableHeadLeft.append("</td>\n");
											exportTmpHead
													.append("<td nowrap rowspan=\"2\" align=\"center\"")
													.append(">\n");
											exportTmpHead
													.append(levObj.lev_name);
											exportTmpHead.append("</td>\n");
										} else {
											tableHeadLeft
													.append("<td nowrap align=\"center\" class=\"")
													.append(tdClass)
													.append("\" ")
													.append(chartLink)
													.append(">\n");
											tableHeadLeft
													.append(levObj.lev_name);
											tableHeadLeft.append(genSortStr(
													real_index, colObj));
											tableHeadLeft.append("</td>\n");
											exportHead
													.append("<td nowrap align=\"center\"")
													.append(">\n");
											exportHead.append(levObj.lev_name);
											exportHead.append("</td>\n");
										}
										dims_count++;
									}
								}
							}
						} else {
							if (null != temp_head) {
								tableHeadLeft
										.append("<td nowrap rowspan=\"2\" align=\"left\" class=\"")
										.append(tdClass).append("\" ")
										.append(chartLink).append(">\n");
								tableHeadLeft.append(colObj.col_name);
								tableHeadLeft.append(genSortStr(real_index,
										colObj));
								tableHeadLeft.append("</td>\n");
								exportTmpHead
										.append("<td nowrap rowspan=\"2\" align=\"left\"")
										.append(">\n");
								exportTmpHead.append(colObj.col_name);
								exportTmpHead.append("</td>\n");
							} else {
								tableHeadLeft
										.append("<td nowrap align=\"center\" class=\"")
										.append(tdClass).append("\" ")
										.append(chartLink).append(">\n");
								tableHeadLeft.append(colObj.col_name);
								tableHeadLeft.append(genSortStr(real_index,
										colObj));
								tableHeadLeft.append("</td>\n");
								exportHead
										.append("<td nowrap align=\"center\"")
										.append(">\n");
								exportHead.append(colObj.col_name);
								exportHead.append("</td>\n");
							}
						}
						real_index = index + dims_count;
					} else {
						System.out
								.print("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&指标开始");
						// 指标开始
						real_index++;
						tdClass = tabHeadTDClass;
						tableHeadRight
								.append("<td nowrap align=\"center\" class=\"")
								.append(tdClass).append("\" ").append(">\n");

						// add by jcm
						if (colObj.is_calByOther.equals("Y")) {
							tableHeadRight.append("<a href=" + hrefLink + ">");
							tableHeadRight.append(colObj.col_name);
							tableHeadRight.append("</a>");
						} else {
							tableHeadRight.append("<a href=" + hrefLink + ">");
							tableHeadRight.append(colObj.col_name);
							tableHeadRight.append("</a>");
						}

						//
						tableHeadRight.append(genSortStr(real_index, colObj));
						tableHeadRight.append("</td>\n");
						exportHead.append("<td nowrap align=\"center\"")
								.append(">\n");
						exportHead.append(colObj.col_name);
						exportHead.append("</td>\n");
						// 占比
						if (ConstantKeys.YES
								.equalsIgnoreCase(colObj.has_comratio)) {
							realColNum++;
							// 有占比，

							tdClass = tabHeadTDClass;
							real_index++;
							tableHeadRight
									.append("<td nowrap align=\"center\" class=\"")
									.append(tdClass).append("\" ")
									.append(chartLink).append(">\n");
							tableHeadRight.append("占总比");
							tableHeadRight
									.append(genSortStr(real_index, colObj));
							tableHeadRight.append("</td>\n");
							exportHead.append("<td nowrap align=\"center\"")
									.append(">\n");
							exportHead.append("占总比");
							exportHead.append("</td>\n");
						}// end ratio
							// 同比
						if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_last)) {
							real_index++;
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.last_display)) {
								realColNum++;

								tdClass = tabHeadTDClass;
								tableHeadRight
										.append("<td nowrap align=\"center\" class=\"")
										.append(tdClass).append("\" ")
										.append(chartLink).append(">\n");
								tableHeadRight.append("同比");
								tableHeadRight.append(genSortStr(real_index,
										colObj));
								tableHeadRight.append("</td>\n");
								exportHead
										.append("<td nowrap align=\"center\"")
										.append(">\n");
								exportHead.append("同比");
								exportHead.append("</td>\n");
							}
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_loop)) {
								real_index++;
							}
							real_index++;
						}// end last
							// //占比
						if (ConstantKeys.YES.equalsIgnoreCase(colObj.has_loop)) {
							if (ConstantKeys.YES
									.equalsIgnoreCase(colObj.loop_display)) {
								realColNum++;

								tdClass = tabHeadTDClass;
								tableHeadRight
										.append("<td nowrap align=\"center\" class=\"")
										.append(tdClass).append("\" ")
										.append(chartLink).append(">\n");
								tableHeadRight.append("环比");
								tableHeadRight.append(genSortStr(real_index,
										colObj));
								tableHead.append("</td>\n");
								exportHead
										.append("<td nowrap align=\"center\"")
										.append(">\n");
								exportHead.append("环比");
								exportHead.append("</td>\n");
							}
							if (ConstantKeys.NO
									.equalsIgnoreCase(colObj.has_last)) {
								real_index++;
							}
							real_index++;
						}// end loop
					}// end else 是否指标
				}// 是否显示
			}// 遍历表头

			if (null != temp_head) {
				exportTmpHead.append(temp_head);
				exportTmpHead.append("</tr>\n");
			}
			tableHeadLeft.append("</tr></table>\n");
			tableHeadRight.append("</tr></table></div></td></tr>\n");
			tableHead = new StringBuffer("");
			tableHead.append("<tr>\n");
			tableHead.append("		<td align=center valign='top'>\n");
			tableHead.append(tableHeadLeft).append(tableHeadRight);
			if (null != temp_head) {
				exportTmpHead.append(exportHead);
				exportHead.delete(0, exportHead.length());
				exportHead.append(exportTmpHead);
			}
			head = new String[1];
			head[0] = tableHead.toString();
			exportHTML.add(exportHead.toString());
		}// end has head
		return head;
	}

	/**
	 * 生成表格HTML
	 * 
	 * @return
	 */
	public String[] getTableHTML(HttpSession session) {

		String[] html = null;
		exportHTML.clear();
		exportHTML.add(exportTableProperties);
		String[] head = getTableHead(session);

		String[] body = getTableBody(session);

		String tmpHtml = "<table width='100%' border='0' cellpadding='0' cellspacing='0' "
				+ ">\n<tr><td class=\"side-left\">";
		tmpHtml += "<table width='100%' border='0' height=\"" + height + "\" "
				+ "cellpadding='0' cellspacing='0' "
				+ "id=\"iTable_TableContainer\">\n";
		if (null != head) {
			if (null == body) {
				hasCotent = false;
				html = null;
				StringBuffer sb = new StringBuffer("");
				sb.append(
						"<tr><td colspan=\"" + realColNum
								+ "\"align=\"center\"><strong>").append(
						"当前查询条件下没有查询结果，请重新选择条件");
				sb.append("</strong></td></tr>");
				exportHTML.add(sb.toString());
			} else {
				String[] tableHtml = new String[body.length + head.length + 2];
				tableHtml[0] = tmpHtml;
				System.arraycopy(head, 0, tableHtml, 1, head.length);
				System.arraycopy(body, 0, tableHtml, 1 + head.length,
						body.length);
				tableHtml[tableHtml.length - 1] = "</table>\n</td></tr></table>";
				tableHtml[tableHtml.length - 1] += genChartJSHtml();
				html = tableHtml;
				hasCotent = true;
			}
			exportHTML.add("</table>\n");
		}
		if (filter_indexs != null && firstFiltered) {
			firstFiltered = false;
		}

		// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
		// System.out.println(html);
		// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		return html;

	}

	/**
	 * 用于将查询本期、上期同比、上期环比的查询语句外关联起来
	 * 
	 * @param report
	 *            报表对象
	 * @param rptDims
	 *            报表的维度定义
	 * @param rptMsus
	 *            报表的指标定义
	 * @param rptMsuDatas
	 *            报表指标的数据字段
	 * @param mainTabVirName
	 *            主表的虚名称
	 * @param sameRatioTabVirName
	 *            同比查询的虚名称
	 * @param lastRatioTabVirName
	 *            环比查询的虚名称
	 * @param needSameRatio
	 *            是否关联同比查询
	 * @param needLastRatio
	 *            是否关联环比查询
	 * @return
	 */
	public String joinRatioTable(UiCommonTableDefTable table, List cols,
			String mainDimSelect, String where, String group_by,
			String mainTabVirName, String sameRatioTabVirName,
			String lastRatioTabVirName, boolean needSameRatio,
			boolean needLastRatio) {
		StringBuffer FROM = null;
		if (null != table && null != cols && null != mainTabVirName
				&& null != sameRatioTabVirName && null != lastRatioTabVirName) {
			StringBuffer subSelect = new StringBuffer("");
			// 如果任意一个主要参数为NULL，则返回NULL
			// 默认认为没有同比
			boolean hasSameRatio = false;
			// 默认认为没有环比
			boolean hasLastRatio = false;
			// 先看看有没有要求计算环比和同期比的
			Iterator iter = cols.iterator();
			while (iter.hasNext()) {
				UiCommonTabledictTable colObj = (UiCommonTabledictTable) iter
						.next();
				if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_measure)) {
					if (null != colObj.has_last
							&& ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_last))
						hasSameRatio = true;
					if (null != colObj.has_loop
							&& ConstantKeys.YES
									.equalsIgnoreCase(colObj.has_loop))
						hasLastRatio = true;
				}
			}
			// 如果有计算同比或环比，初始化FROM部分
			if (hasLastRatio || hasSameRatio)
				FROM = new StringBuffer("");

			StringBuffer select = new StringBuffer("");
			StringBuffer mainSelect = new StringBuffer("SELECT ");
			StringBuffer groupby = new StringBuffer(" GROUP BY ");
			StringBuffer on = new StringBuffer("");
			on.append(parseDimSelectAsOnWhere(mainDimSelect));
			// 由于在关联环比查询时，对于数据粒度到日的查询，
			// ORACLE不能正确找到对应日期，因此自己写一个函数，
			// 利用子查询来关联
			if (table.time_level.equalsIgnoreCase(ConstantKeys.DATA_DAY_LEVEL)) {
				groupby.append("T.").append(ConstantKeys.TIME_DIM_DAY_FLD)
						.append(",");
			} else {
				groupby.append("T.").append(ConstantKeys.TIME_DIM_MONTH_FLD)
						.append(",");
			}

			String dim_select = mainDimSelect;
			dim_select = dim_select.replaceAll("A\\.", "M.");
			mainSelect.append(dim_select);
			select.append(dim_select);
			// 将指标查询出来,对于计算型指标不用查询，所有指标需要SUM
			iter = cols.iterator();
			while (iter.hasNext()) {
				UiCommonTabledictTable colObj = (UiCommonTabledictTable) iter
						.next();
				if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_measure)
						&& ConstantKeys.NO
								.equalsIgnoreCase(colObj.is_calByOther)) {
					select.append("SUM(").append(colObj.code_field)
							.append(") AS ").append(colObj.code_field)
							.append(",");
					mainSelect.append("SUM(").append(colObj.code_field)
							.append(") AS ").append(colObj.code_field)
							.append(",");
				}
			}
			String time_fld = null;
			if (table.time_level.equalsIgnoreCase(ConstantKeys.DATA_DAY_LEVEL)) {
				subSelect.append("(SELECT T.")
						.append(ConstantKeys.TIME_DIM_DAY_FLD).append(" AS ")
						.append(table.time_field).append(",");
				time_fld = ConstantKeys.TIME_DIM_DAY_FLD;
			} else {
				subSelect.append("(SELECT T.")
						.append(ConstantKeys.TIME_DIM_MONTH_FLD).append(" AS ")
						.append(table.time_field).append(",");
				time_fld = ConstantKeys.TIME_DIM_MONTH_FLD;
			}
			mainSelect.append("M.").append(table.time_field).append(",");
			// 由于多加了一个逗号，去掉
			if (null != select && select.lastIndexOf(",") >= 0) {
				select = new StringBuffer(select.substring(0,
						select.lastIndexOf(",")));
			}
			if (null != mainSelect && mainSelect.lastIndexOf(",") >= 0) {
				mainSelect = new StringBuffer(mainSelect.substring(0,
						mainSelect.lastIndexOf(",")));
			}
			subSelect.append(select);
			subSelect.append(" FROM ").append(table.data_table).append(" M,");
			mainSelect.append(" FROM ").append(table.data_table).append(" A0");
			if (table.time_level.equalsIgnoreCase(ConstantKeys.DATA_DAY_LEVEL)) {
				subSelect.append(ConstantKeys.TIME_DIM_DAY_TABLE).append(" T ");
			} else {
				subSelect.append(ConstantKeys.TIME_DIM_MONTH_TABLE).append(
						" T ");
			}
			String sub_where = where;
			sub_where = sub_where.replaceAll("A\\.", "M.");
			mainSelect.append(sub_where);
			sub_where = sub_where.replaceAll("M\\." + table.time_field, "T."
					+ time_fld);
			sub_where = sub_where.replaceAll(
					"M\\." + table.time_field.toLowerCase(), "T." + time_fld);
			subSelect.append(sub_where);

			subSelect.append(" AND M.").append(table.time_field).append("=")
					.append("T.::RATIO_FLD::");

			if (group_by.length() > 0) {
				String temp_group = group_by;
				temp_group = temp_group.replaceAll("A\\.", "M.");
				mainSelect.append(" GROUP BY ").append(temp_group);
				groupby.append(temp_group);
			}
			subSelect.append(groupby).append(")");

			mainSelect.append(",M.").append(table.time_field);
			if (hasSameRatio || needSameRatio) {
				if (table.time_level
						.equalsIgnoreCase(ConstantKeys.DATA_DAY_LEVEL)) {
					if (FROM.length() <= 0) {
						String temp = mainSelect.toString();
						temp = temp.replaceAll("M\\.", "A0.");
						FROM = new StringBuffer(" FROM (").append(temp)
								.append(") ").append(mainTabVirName);
					}
					String temp = subSelect.toString();
					temp = temp.replaceAll("::RATIO_FLD::",
							ConstantKeys.TIME_DIM_SAME_DAY_FLD);
					temp = temp.replaceAll("M\\.", "A1.");
					temp = temp.replaceAll(" M,", " A1,");
					FROM.append(" LEFT JOIN ").append(temp);

					// 这里的::otherSelectFlds要替换成主表不包括比率的除时间字段外的字段
					FROM.append(sameRatioTabVirName);
					FROM.append(" ON");
					String dateStr = mainTabVirName + "." + table.time_field;
					FROM.append(" ").append(dateStr).append("=")
							.append(sameRatioTabVirName).append(".")
							.append(table.time_field);
				}
				// 如果数据粒度到月份，则直接关联去年数据
				if (table.time_level
						.equalsIgnoreCase(ConstantKeys.DATA_MONTH_LEVEL)) {
					if (FROM.length() <= 0) {
						String temp = mainSelect.toString();
						temp = temp.replaceAll("M\\.", "A0.");
						FROM = new StringBuffer(" FROM (").append(temp)
								.append(") ").append(mainTabVirName);
					}
					String temp = subSelect.toString();
					temp = temp.replaceAll("::RATIO_FLD::",
							ConstantKeys.TIME_DIM_SAME_MONTH_FLD);
					temp = temp.replaceAll("M\\.", "A1.");
					temp = temp.replaceAll(" M,", " A1,");
					FROM.append(" LEFT JOIN ").append(temp);
					FROM.append(" ").append(sameRatioTabVirName);
					FROM.append(" ON");
					// 粒度到日期
					String dateStr = mainTabVirName + "." + table.time_field;
					FROM.append(" ").append(dateStr).append("=")
							.append(sameRatioTabVirName).append(".")
							.append(table.time_field);

				}
				FROM.append(on);
			}
			// 关联环比
			if (hasLastRatio || needLastRatio) {
				if (table.time_level
						.equalsIgnoreCase(ConstantKeys.DATA_DAY_LEVEL)) {
					if (FROM.length() <= 0) {
						String temp = mainSelect.toString();
						temp = temp.replaceAll("M\\.", "A0.");
						FROM = new StringBuffer(" FROM (").append(temp)
								.append(") ").append(mainTabVirName);
					}
					String temp = subSelect.toString();
					temp = temp.replaceAll("::RATIO_FLD::",
							ConstantKeys.TIME_DIM_LAST_DAY_FLD);
					temp = temp.replaceAll("M\\.", "A2.");
					temp = temp.replaceAll(" M,", " A2,");
					FROM.append(" LEFT JOIN ").append(temp);

					// 这里的::otherSelectFlds要替换成主表不包括比率的除时间字段外的字段
					FROM.append(lastRatioTabVirName);
					FROM.append(" ON");
					String dateStr = mainTabVirName + "." + table.time_field;
					FROM.append(" ").append(dateStr).append("=")
							.append(lastRatioTabVirName).append(".")
							.append(table.time_field);
				}
				// 如果数据粒度到月份，则直接关联去年数据
				if (table.time_level
						.equalsIgnoreCase(ConstantKeys.DATA_MONTH_LEVEL)) {
					if (FROM.length() <= 0) {
						String temp = mainSelect.toString();
						temp = temp.replaceAll("M\\.", "A0.");
						FROM = new StringBuffer(" FROM (").append(temp)
								.append(") ").append(mainTabVirName);
					}
					String temp = subSelect.toString();
					temp = temp.replaceAll("::RATIO_FLD::",
							ConstantKeys.TIME_DIM_LAST_MONTH_FLD);
					temp = temp.replaceAll("M\\.", "A2.");
					temp = temp.replaceAll(" M,", " A2,");
					FROM.append(" LEFT JOIN ").append(temp);
					FROM.append(" ").append(lastRatioTabVirName);
					FROM.append(" ON");
					// 粒度到日期
					String dateStr = mainTabVirName + "." + table.time_field;
					FROM.append(" ").append(dateStr).append("=")
							.append(lastRatioTabVirName).append(".")
							.append(table.time_field);

				}
				String temp = on.toString();
				temp = temp.replaceAll("B\\.", "C.");
				FROM.append(temp);
			}
		}
		return FROM.toString();
	}

	/**
	 * 分析进来维度选择串，生成左连接时的条件
	 * 
	 * @param dimSelect
	 *            维度选择字符串
	 * @return on 条件
	 */
	private String parseDimSelectAsOnWhere(String dimSelect) {
		String onWhere = null;
		if (null != dimSelect) {
			// 即使
			StringBuffer sb = new StringBuffer("");
			if (dimSelect.indexOf(",") >= 0) {
				String[] temp = dimSelect.split(",");
				if (null != temp) {
					// 每次两个走
					for (int i = 0; i < temp.length; i = i + 2) {
						sb.append(" AND ").append(temp[i]).append("=");
						String temp_str = temp[i];
						temp_str = temp_str.replaceAll("A\\.", "B.");
						sb.append(temp_str);
					}
				}
				onWhere = sb.toString();
			}
		}
		return onWhere;
	}

	/**
	 * 分析条件中的特殊字段，并取出其值，用于该列的CASE WHEN 条件，并在WHERE中去除该条件
	 * 
	 * @param where
	 *            传入的条件
	 * @param col_fld
	 *            字段名称
	 * @return 返回处理后WHERE和字段的值
	 */
	private List parseSpecialWhere(String where, String col_fld) {
		List parsedWhere = null;
		if (null != where && null != col_fld) {
			parsedWhere = new ArrayList();
			String temp_str = where.toUpperCase();
			String fld = col_fld.toUpperCase();
			int pos = temp_str.indexOf(fld);
			if (pos >= 0) {
				String first = temp_str.substring(0, pos);
				first = first.trim();
				if (first.lastIndexOf("AND") >= 0) {
					first = first.substring(0, first.lastIndexOf("AND"));
				}
				String second = temp_str.substring(pos + fld.length(),
						temp_str.length());
				second = second.trim();
				String value = null;
				pos = second.indexOf(" ");
				if (pos >= 0) {
					value = second.substring(0, pos);
				} else {
					value = second;
				}
				pos = second.indexOf(value);
				if (pos >= 0) {
					second = second.substring(pos + value.length());
				} else {
					second = "";
				}
				parsedWhere.add(first + second);
				if (null != value) {
					pos = value.indexOf("=");
					if (pos >= 0) {
						value = value.substring(pos + 1, value.length());
						parsedWhere.add(value);
					} else {
						value = value.substring(1, value.length());
						parsedWhere.add(value);
					}
				}
			} else {
				parsedWhere.add(temp_str);
				parsedWhere.add("");
			}
		}
		return parsedWhere;
	}

	/**
	 * 处理合计的单元格
	 * 
	 * @param sumTR
	 * @return
	 */
	private String processExportSumTR(String sumTR) {
		String ret = "";
		if (null != sumTR) {
			ret = sumTR;
			ret = ret.replaceAll("</tr>", "");
			ret = ret.replaceAll("</TR>", "");
			int pos = ret.indexOf("<tr");
			if (pos >= 0) {
				pos = ret.indexOf(">", pos + 1);
				if (pos > 0) {
					ret = ret.substring(pos + 1);
				}
			} else {
				pos = ret.indexOf("<TR");
				if (pos >= 0) {
					pos = ret.indexOf(">", pos + 1);
					if (pos > 0) {
						ret = ret.substring(pos + 1);
					}
				}
			}
			String first = "";
			pos = ret.indexOf("<a");
			if (pos >= 0) {
				first = ret.substring(0, pos);
				pos = ret.indexOf(">", pos + 1);
				if (pos > 0) {
					ret = ret.substring(pos + 1);
				}
				ret.replaceAll("</a>", "");
				ret = first + ret;
			} else {
				pos = ret.indexOf("<A");
				if (pos >= 0) {
					first = ret.substring(0, pos);
					pos = ret.indexOf(">", pos + 1);
					if (pos > 0) {
						ret = ret.substring(pos + 1);
					}
					ret.replaceAll("</A>", "");
					ret = first + ret;
				}
			}
		}
		return ret;
	}

	/**
	 * 重置列对象的状态
	 */
	private void resetCols() {
		clearTempValue();
		cols = TableColUtil.getColObjs(tableID);
		colsLevels = TableColUtil.getColsLevels(tableID);
		if (null != cols) {
			colsMap = new HashMap();
			Iterator iter = cols.iterator();
			while (iter.hasNext()) {
				UiCommonTabledictTable colObj = (UiCommonTabledictTable) iter
						.next();
				if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_calByOther)) {
					colObj = TableColUtil.parseCmd(colObj);
					// 如果有公式，则设置其因子
					// 处理一下分母情况
					if (null != colObj.calObj && null != colObj.calObj.cal_cmd
							&& !"".equalsIgnoreCase(colObj.calObj.cal_cmd))
						colObj.code_field = TableColUtil
								.processDenoZero(colObj.calObj.cal_cmd);
					// 这是已经设置好了,就看如何求和了
				}
				colsMap.put(colObj.col_id, colObj);
			}
		}
	}

	/**
	 * 判断一个行对象是否可以显示 filter_indexs=C1,C2全部时是占比 filter_indexs=G1,C2全部时是占比
	 * G表示普通某个列字段 filter_indexs=D1/2,C2全部时是占比 表示列1的值除以列2的值
	 * filter_values【】必须两两成对，且占比用小数表示
	 * 
	 * @param rowObj
	 * @return
	 */
	private boolean rowCanDisplayed(RowStruct rowObj) {
		boolean canDisplayed = false;
		if (null != rowObj && null != filter_indexs && null != filter_level
				&& null != filter_values) {
			// 判断只有完全满足条件的行，这样应该快一些
			String[] values = rowObj.cols_value;
			if (null != values) {
				String[] split_indexs = filter_indexs.split(",");
				String[] split_values = filter_values.split(",");
				for (int i = 0; i < split_indexs.length; i++) {
					String value = getFilterValue(values, split_indexs[i]);
					canDisplayed = fitFilterCon(i, value, split_values);
					if (!canDisplayed) {
						// 只要一个不满足，退出
						break;
					}
				}
			}
		}
		return canDisplayed;
	}

	/**
	 * 设置表格标识，同时取出表格的定义和列对象
	 * 
	 * @param tableID
	 */
	public void setTableID(String tableID) {
		this.tableID = tableID;
		clearTempValue();
		tabObj = TableColUtil.getTabObj(tableID);
		cols = TableColUtil.getColObjs(tableID);
		colsLevels = TableColUtil.getColsLevels(tableID);
		// System.out.println("setTableID() goes here."+tabObj);
		if (null != cols) {
			colsMap = new HashMap();
			Iterator iter = cols.iterator();
			while (iter.hasNext()) {
				UiCommonTabledictTable colObj = (UiCommonTabledictTable) iter
						.next();
				if (ConstantKeys.YES.equalsIgnoreCase(colObj.is_calByOther)) {
					colObj = TableColUtil.parseCmd(colObj);
					// 如果有公式，则设置其因子
					// 处理一下分母情况
					if (null != colObj.calObj && null != colObj.calObj.cal_cmd
							&& !"".equalsIgnoreCase(colObj.calObj.cal_cmd))
						colObj.code_field = TableColUtil
								.processDenoZero(colObj.calObj.cal_cmd);
					// 这是已经设置好了,就看如何求和了
				}
				colsMap.put(colObj.col_id, colObj);
			}
		}
	}

	/**
	 * 对表格的内容进行排序
	 */
	public void sortTable() {
		long start = System.currentTimeMillis();
		if (Integer.MIN_VALUE != sortedColum && null != sortOrder
				&& null != content) {
			if (Integer.MIN_VALUE == expandIndex && 0 == expandedContent.size()) {
				if (null != filteredContent && null != filter_indexs)
					filteredContent = TableColUtil
							.sortContent(sortedColum, sortOrder, sortDataType,
									dimsCount, filteredContent);
				else
					content = TableColUtil.sortContent(sortedColum, sortOrder,
							sortDataType, dimsCount, content);
			} else {
				List tempList = TableColUtil.sortContent(sortedColum,
						sortOrder, sortDataType, dimsCount,
						(List) expandedContent.getLast());
				try {
					expandedContent.removeLast();
				} catch (NoSuchElementException nee) {

				}
				expandedContent.addLast(tempList);
				content = assembleList();
			}
		}
		System.out.println("排序用时：" + (System.currentTimeMillis() - start)
				+ "ms");
	}
}
