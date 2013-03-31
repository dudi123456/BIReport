package com.ailk.bi.olap.domain;

import java.io.Serializable;
import java.util.Map;

import com.ailk.bi.olap.util.RptOlapConsts;

@SuppressWarnings({ "rawtypes" })
public class RptOlapFuncStruct implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7270834932761867591L;
	/**
	 * 是否可以使用自定义表头
	 */
	private boolean canUseCustomHead = true;

	/**
	 * 默认展现方式，层层钻取方式
	 */
	private String displayMode = RptOlapConsts.RESET_MODE_DIG;

	/**
	 * 当前功能，默认数据，占比、同比、环比等
	 */
	private String curFunc = null;

	/**
	 * 当前条件是否能够钻取到用户级
	 */
	private boolean toUserLevel = false;

	/**
	 * 是否有累计值
	 */
	private boolean hasSum = false;

	/**
	 * 是否有预警
	 */
	private boolean hasAlert = false;

	/**
	 * 有同比值预警
	 */
	private boolean hasSameRatioAlert = false;

	/**
	 * 有环比值预警
	 */
	private boolean hasLastRatioAlert = false;

	/**
	 * 计算同比时的统计周期设置
	 */
	private String sameRatioPeriod = RptOlapConsts.SAME_RATIO_MONTH_PERIOD;

	/**
	 * 排序的列索引
	 */
	private String sort = null;

	/**
	 * 排序本期值、比率值、还是周同比值，目前只有三个
	 */
	private String sortThis = null;

	/**
	 * 排序方向
	 */
	private String sortOrder = RptOlapConsts.SORT_ASC;

	/**
	 * 钻取的指标索引
	 */
	private String clickMsu = null;

	/**
	 * 所单击的指标标识
	 */
	private String clickMusId = null;

	/**
	 * 是否是单维度展开
	 */
	private boolean singleDimExpand = false;

	/**
	 * 当前的展开层次
	 */
	private int curExpandLevel = 0;

	/**
	 * 折叠展开的最大层次
	 */
	private int maxExpandLevel;

	/**
	 * 折叠展开第一次展示，绝对是否显示全部表格内容
	 */
	private boolean firstExpand = false;

	/**
	 * 当前的折叠行的标识
	 */
	private String collapseRowId = null;

	/**
	 * 记录当前层次前各层次的值
	 */
	private Map expandedValues = null;

	/**
	 * 是否有新的折叠和展开
	 */
	private boolean hasNewExpand = false;

	public String getCurFunc() {
		return curFunc;
	}

	public void setCurFunc(String curFunc) {
		this.curFunc = curFunc;
	}

	public boolean isHasAlert() {
		return hasAlert;
	}

	public void setHasAlert(boolean hasAlert) {
		this.hasAlert = hasAlert;
	}

	public boolean isHasSum() {
		return hasSum;
	}

	public void setHasSum(boolean hasSum) {
		this.hasSum = hasSum;
	}

	public String getSameRatioPeriod() {
		return sameRatioPeriod;
	}

	public void setSameRatioPeriod(String sameRatioPeriod) {
		this.sameRatioPeriod = sameRatioPeriod;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSortThis() {
		return sortThis;
	}

	public void setSortThis(String sortThis) {
		this.sortThis = sortThis;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getClickMsu() {
		return clickMsu;
	}

	public void setClickMsu(String clickMsu) {
		this.clickMsu = clickMsu;
	}

	public String getClickMusId() {
		return clickMusId;
	}

	public void setClickMusId(String clickMusId) {
		this.clickMusId = clickMusId;
	}

	public String getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(String displayMode) {
		this.displayMode = displayMode;
	}

	public int getMaxExpandLevel() {
		return maxExpandLevel;
	}

	public void setMaxExpandLevel(int maxExpandLevel) {
		this.maxExpandLevel = maxExpandLevel;
	}

	public boolean isFirstExpand() {
		return firstExpand;
	}

	public void setFirstExpand(boolean firstExpand) {
		this.firstExpand = firstExpand;
	}

	public Map getExpandedValues() {
		return expandedValues;
	}

	public void setExpandedValues(Map expandedValues) {
		this.expandedValues = expandedValues;
	}

	public int getCurExpandLevel() {
		return curExpandLevel;
	}

	public void setCurExpandLevel(int curExpandLevel) {
		this.curExpandLevel = curExpandLevel;
	}

	public boolean isSingleDimExpand() {
		return singleDimExpand;
	}

	public void setSingleDimExpand(boolean singleDimExpand) {
		this.singleDimExpand = singleDimExpand;
	}

	public String getCollapseRowId() {
		return collapseRowId;
	}

	public void setCollapseRowId(String collapseRowId) {
		this.collapseRowId = collapseRowId;
	}

	public boolean isToUserLevel() {
		return toUserLevel;
	}

	public void setToUserLevel(boolean toUserLevel) {
		this.toUserLevel = toUserLevel;
	}

	public boolean isHasLastRatioAlert() {
		return hasLastRatioAlert;
	}

	public void setHasLastRatioAlert(boolean hasLastRatioAlert) {
		this.hasLastRatioAlert = hasLastRatioAlert;
	}

	public boolean isHasSameRatioAlert() {
		return hasSameRatioAlert;
	}

	public void setHasSameRatioAlert(boolean hasSameRatioAlert) {
		this.hasSameRatioAlert = hasSameRatioAlert;
	}

	public boolean isCanUseCustomHead() {
		return canUseCustomHead;
	}

	public void setCanUseCustomHead(boolean canUseCustomHead) {
		this.canUseCustomHead = canUseCustomHead;
	}

	public boolean isHasNewExpand() {
		return hasNewExpand;
	}

	public void setHasNewExpand(boolean hasNewExpand) {
		this.hasNewExpand = hasNewExpand;
	}

}
