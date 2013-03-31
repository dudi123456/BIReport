package com.ailk.bi.olap.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ailk.bi.olap.util.RptOlapConsts;

@SuppressWarnings({ "rawtypes" })
public class RptOlapChartAttrStruct {

	/**
	 * 索引标识
	 */
	private String index = "";

	/**
	 * 是否是维度
	 */
	private boolean isDim = false;

	/**
	 * 是时间维
	 */
	private boolean isTime = false;

	/**
	 * 该维度是否显示
	 */
	private boolean display = false;

	/**
	 * 当前维度的层次水平
	 */
	private String level = RptOlapConsts.NO_DIGGED_LEVEL;

	/**
	 * 该维度用于分组
	 */
	private boolean usedForGroup = false;

	/**
	 * 使用所有的维度值
	 */
	private boolean useAllValues = false;

	/**
	 * 维度值是包含，还是排出关系
	 */
	private boolean includeValues = true;

	/**
	 * 当前功能下的给维度的选值
	 */
	private List curValues = new ArrayList();

	/**
	 * 维度的值，键为图形显示方式，趋势、对比、占比，值为List列表
	 */
	private Map values = null;

	/**
	 * 维度或指标对象
	 */
	private Object rptStruct = null;

	/**
	 * 图形的属性设置对象,键为图形显示方式，趋势、对比、占比，值为属性对象
	 */
	private Map sets = null;

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public boolean isDim() {
		return isDim;
	}

	public void setDim(boolean isDim) {
		this.isDim = isDim;
	}

	public boolean isTime() {
		return isTime;
	}

	public void setTime(boolean isTime) {
		this.isTime = isTime;
	}

	public Object getRptStruct() {
		return rptStruct;
	}

	public void setRptStruct(Object rptStruct) {
		this.rptStruct = rptStruct;
	}

	public Map getSets() {
		return sets;
	}

	public void setSets(Map sets) {
		this.sets = sets;
	}

	public boolean isUsedForGroup() {
		return usedForGroup;
	}

	public void setUsedForGroup(boolean usedForGroup) {
		this.usedForGroup = usedForGroup;
	}

	public Map getValues() {
		return values;
	}

	public void setValues(Map values) {
		this.values = values;
	}

	public boolean isUseAllValues() {
		return useAllValues;
	}

	public void setUseAllValues(boolean useAllValues) {
		this.useAllValues = useAllValues;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public List getCurValues() {
		return curValues;
	}

	public void setCurValues(List curValues) {
		this.curValues = curValues;
	}

	public boolean isIncludeValues() {
		return includeValues;
	}

	public void setIncludeValues(boolean includeValues) {
		this.includeValues = includeValues;
	}

}
