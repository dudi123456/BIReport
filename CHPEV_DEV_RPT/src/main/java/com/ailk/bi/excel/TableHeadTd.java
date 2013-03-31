package com.ailk.bi.excel;

import java.io.Serializable;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFFont;

/**
 * 
 * 用于Excel工具的表头单元格对象 注意：单元格的跨行和跨列是和HTML定义类似，即某个单元格占两个单元格，则 colspan=2
 * 
 */
public class TableHeadTd implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2181751427635791363L;
	/**
	 * 跨行数
	 */
	private int rowSpan = 0;
	/**
	 * 跨列数
	 */
	private int colSpan = 0;
	/**
	 * 单元格文本
	 */
	private String text = "";

	/**
	 * 设置字体颜色
	 */
	private short fontColor = HSSFFont.COLOR_NORMAL;

	/**
	 * 数据对齐方式
	 */
	private String dataAlign = null;

	/**
	 * 数据格式
	 */
	private String dataFormat = null;

	/**
	 * 最后一列时获取数据的方法
	 */
	private String dataMethod = null;

	/**
	 * 最后一列是获取数据的方法所需要的参数类型
	 */
	private Class<?>[] dataMethodParameterTypes = null;
	/**
	 * 最后一列是获取数据的方法所需要的参数
	 */
	private Object[] dataMethodParameters = null;

	/**
	 * 是否需要转换
	 */
	private boolean needConvert = false;

	/**
	 * 转换是要用到的键-值对
	 */
	private Map<String, String> transSet = null;

	/**
	 * 某列单元格的下来选择框
	 */
	private String listBoxStr = null;

	/**
	 * 获取跨行数
	 * 
	 * @return
	 */
	public int getRowSpan() {
		return rowSpan;
	}

	/**
	 * 设置跨行数
	 * 
	 * @param rowSpan
	 */
	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	/**
	 * 获取跨列数
	 * 
	 * @return
	 */
	public int getColSpan() {
		return colSpan;
	}

	/**
	 * 设置跨列数
	 * 
	 * @param colSpan
	 */
	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	/**
	 * 获取单元格文本
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * 设置单元格文本
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * 获取单元格对齐方式
	 * 
	 * @return
	 */
	public String getDataAlign() {
		return dataAlign;
	}

	/**
	 * 设置单元格对齐方式,字符串为left/center/right
	 * 
	 * @param dataAlign
	 */
	public void setDataAlign(String dataAlign) {
		this.dataAlign = dataAlign;
	}

	/**
	 * 获取数据格式
	 * 
	 * @return
	 */
	public String getDataFormat() {
		return dataFormat;
	}

	/**
	 * 设置单元格格式
	 * 
	 * @param dataFormat
	 */
	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	/**
	 * 是否需要转换
	 * 
	 * @return
	 */
	public boolean isNeedConvert() {
		return needConvert;
	}

	/**
	 * 设置转换
	 * 
	 * @param needConvert
	 */
	public void setNeedConvert(boolean needConvert) {
		this.needConvert = needConvert;
	}

	/**
	 * 转换对象集
	 * 
	 * @return
	 */
	public Map<String, String> getTransSet() {
		return transSet;
	}

	/**
	 * 设置转换对象集
	 * 
	 * @param transSet
	 */
	public void setTransSet(Map<String, String> transSet) {
		this.transSet = transSet;
	}

	public short getFontColor() {
		return fontColor;
	}

	/**
	 * 设置某列单元格的颜色，取值目前仅为两个，为
	 * ExcelUtil.FONT_COLOR_RED,ExcelUtil.FONT_COLOR_NORMAL
	 * 
	 * @param fontColor
	 */
	public void setFontColor(short fontColor) {
		this.fontColor = fontColor;
	}

	public String getListBoxStr() {
		return listBoxStr;
	}

	/**
	 * 设置某列下拉输入框的可选值，形式为由逗号分隔的带双引号字符串 "\"100, 200, 300, 400, 500\""
	 * 
	 * @param listBoxStr
	 */
	public void setListBoxStr(String listBoxStr) {
		this.listBoxStr = listBoxStr;
	}

	public String getDataMethod() {
		return dataMethod;
	}

	/**
	 * 表头最后一行设置列的获取数据方法，直接为 getXXX形式
	 * 
	 * @param dataMethod
	 */
	public void setDataMethod(String dataMethod) {
		this.dataMethod = dataMethod;
	}

	public Object[] getDataMethodParameters() {
		return dataMethodParameters;
	}

	/**
	 * 表头最后一行设置列的获取数据方法的参数，如果没有，可以不设置
	 * 
	 * 调用方式为 (Object[0],Object[1].......)
	 * 
	 * @param dataMethodParameters
	 * 
	 */
	public void setDataMethodParameters(Object[] dataMethodParameters) {
		this.dataMethodParameters = dataMethodParameters;
	}

	public Class<?>[] getDataMethodParameterTypes() {
		return dataMethodParameterTypes;
	}

	/**
	 * 表头最后一行设置列的获取数据方法的参数类型，如果没有，可以不设置 如果设置
	 * 
	 * @param dataMethodParameterTypes
	 */
	public void setDataMethodParameterTypes(Class<?>[] dataMethodParameterTypes) {
		this.dataMethodParameterTypes = dataMethodParameterTypes;
	}

}
