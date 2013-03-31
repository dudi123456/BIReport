package com.ailk.bi.olap.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.ailk.bi.olap.util.RptOlapConsts;

@SuppressWarnings({ "rawtypes" })
public class RptOlapTableColumnStruct implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2902052014522982024L;

	/**
	 * 列的排列索引，可以不连续，按从小到大排序
	 */
	private int index = RptOlapConsts.NO_INDEX;

	/**
	 * 维度或指标标识
	 */
	private String dimOrMsuId;

	/**
	 * 列显示名称
	 */
	private String colName;

	/**
	 * 数据最细粒度字段
	 */
	private String colFld;

	/**
	 * 每个列的显示顺序，可变
	 */
	private int displayOrder = RptOlapConsts.NO_INDEX;

	/**
	 * 是否是本期排序
	 */
	private boolean isSortThis = true;

	/**
	 * 当前的排序状态
	 */
	private String sortOrder = RptOlapConsts.NO_SORT;

	/**
	 * 是否需要显示
	 */
	private boolean display = false;

	/**
	 * 是否是维度
	 */
	private boolean isDim = false;

	/**
	 * 是否是时间维度，由于其层次特殊
	 */
	private boolean isTimeDim = false;

	/**
	 * 是否是合计指标列对象
	 */
	private boolean isSumMsu = false;

	/**
	 * 需要钻取
	 */
	private boolean needDig = false;

	/**
	 * 当前层次，生成全链接时也是指当前层次
	 */
	private String digLevel = RptOlapConsts.NO_DIGGED_LEVEL;

	/**
	 * 当前层次下是否允许到用户级,注意当没有展开时时可以的
	 */
	private boolean toUserLevel = false;

	/**
	 * 维度值
	 */
	private String dimValue;

	/**
	 * 保存各个层次的维度值,以支持单层收缩
	 */
	private Map dimValues = new HashMap();

	/**
	 * 存储报表维度对象或者报表指标对象
	 */
	private Object rptStruct;

	/**
	 * 列的表头的HTML代码
	 */
	private String colHeadHTML1 = "";

	/**
	 * 列的表头的HTML代码
	 */
	private String colHeadHTML2 = "";

	/**
	 * 列的表头的HTML代码
	 */
	private String colHeadHTML3 = "";

	/**
	 * 是否钻取维度
	 */
	private boolean isClickDim = false;

	public Object getStruct() {
		return rptStruct;
	}

	public void setStruct(Object struct) {
		this.rptStruct = struct;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getDigLevel() {
		return digLevel;
	}

	public void setDigLevel(String digLevel) {
		this.digLevel = digLevel;
	}

	public String getDimOrMsuId() {
		return dimOrMsuId;
	}

	public void setDimOrMsuId(String dimOrMsuId) {
		this.dimOrMsuId = dimOrMsuId;
	}

	public String getDimValue() {
		return dimValue;
	}

	public void setDimValue(String dimValue) {
		this.dimValue = dimValue;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isDim() {
		return isDim;
	}

	public void setDim(boolean isDim) {
		this.isDim = isDim;
	}

	public boolean isTimeDim() {
		return isTimeDim;
	}

	public void setTimeDim(boolean isTimeDim) {
		this.isTimeDim = isTimeDim;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public boolean isNeedDig() {
		return needDig;
	}

	public void setNeedDig(boolean needDig) {
		this.needDig = needDig;
	}

	public String getColFld() {
		return colFld;
	}

	public void setColFld(String colFld) {
		this.colFld = colFld;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public boolean isSortThis() {
		return isSortThis;
	}

	public void setSortThis(boolean isSortThis) {
		this.isSortThis = isSortThis;
	}

	public boolean isToUserLevel() {
		return toUserLevel;
	}

	public void setToUserLevel(boolean toUserLevel) {
		this.toUserLevel = toUserLevel;
	}

	public String getColHeadHTML1() {
		return colHeadHTML1;
	}

	public void setColHeadHTML1(String colHeadHTML1) {
		this.colHeadHTML1 = colHeadHTML1;
	}

	public String getColHeadHTML2() {
		return colHeadHTML2;
	}

	public void setColHeadHTML2(String colHeadHTML2) {
		this.colHeadHTML2 = colHeadHTML2;
	}

	public String getColHeadHTML3() {
		return colHeadHTML3;
	}

	public void setColHeadHTML3(String colHeadHTML3) {
		this.colHeadHTML3 = colHeadHTML3;
	}

	public Map getDimValues() {
		return dimValues;
	}

	public void setDimValues(Map dimValues) {
		this.dimValues = dimValues;
	}

	public boolean isClickDim() {
		return isClickDim;
	}

	public void setClickDim(boolean isClickDim) {
		this.isClickDim = isClickDim;
	}

	public boolean isSumMsu() {
		return isSumMsu;
	}

	public void setSumMsu(boolean isSumMsu) {
		this.isSumMsu = isSumMsu;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
