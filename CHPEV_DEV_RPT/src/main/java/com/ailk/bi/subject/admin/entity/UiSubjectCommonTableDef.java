package com.ailk.bi.subject.admin.entity;

/**
 * UiSubjectCommonTableDef entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings({ "serial" })
public class UiSubjectCommonTableDef implements java.io.Serializable {

	// Fields

	private String tableId = "";
	private String tableName = "";
	private String tableDesc = "";
	private String dataTable = "";
	private String dataWhere = "";
	private String hasExpand = "";
	private String hasPaging = "";
	private String timeField = "";
	private String fieldType = "1";
	private String timeLevel = "4";
	private String rowClickedChartChange = "";
	private String rltChartId = "";
	private String hasHead = "";
	private String sumDisplay = "";
	private String dimAscol = "";
	private String customMsu = "";
	private String throwOld = "N";
	private String hasExpandall = "";
	private String chartChangeJs = "";
	private String tableType = "";

	// Constructors

	/** default constructor */
	public UiSubjectCommonTableDef() {
	}

	/** minimal constructor */
	public UiSubjectCommonTableDef(String tableId, String tableName,
			String dataTable, String dataWhere, String hasExpand,
			String hasPaging, String timeField, String fieldType,
			String timeLevel, String rowClickedChartChange, String hasHead,
			String sumDisplay, String dimAscol, String customMsu,
			String throwOld, String hasExpandall) {
		this.tableId = tableId;
		this.tableName = tableName;
		this.dataTable = dataTable;
		this.dataWhere = dataWhere;
		this.hasExpand = hasExpand;
		this.hasPaging = hasPaging;
		this.timeField = timeField;
		this.fieldType = fieldType;
		this.timeLevel = timeLevel;
		this.rowClickedChartChange = rowClickedChartChange;
		this.hasHead = hasHead;
		this.sumDisplay = sumDisplay;
		this.dimAscol = dimAscol;
		this.customMsu = customMsu;
		this.throwOld = throwOld;
		this.hasExpandall = hasExpandall;
	}

	/** full constructor */
	public UiSubjectCommonTableDef(String tableId, String tableName,
			String tableDesc, String dataTable, String dataWhere,
			String hasExpand, String hasPaging, String timeField,
			String fieldType, String timeLevel, String rowClickedChartChange,
			String rltChartId, String hasHead, String sumDisplay,
			String dimAscol, String customMsu, String throwOld,
			String hasExpandall, String chartChangeJs, String tableType) {
		this.tableId = tableId;
		this.tableName = tableName;
		this.tableDesc = tableDesc;
		this.dataTable = dataTable;
		this.dataWhere = dataWhere;
		this.hasExpand = hasExpand;
		this.hasPaging = hasPaging;
		this.timeField = timeField;
		this.fieldType = fieldType;
		this.timeLevel = timeLevel;
		this.rowClickedChartChange = rowClickedChartChange;
		this.rltChartId = rltChartId;
		this.hasHead = hasHead;
		this.sumDisplay = sumDisplay;
		this.dimAscol = dimAscol;
		this.customMsu = customMsu;
		this.throwOld = throwOld;
		this.hasExpandall = hasExpandall;
		this.chartChangeJs = chartChangeJs;
		this.tableType = tableType;
	}

	// Property accessors

	public String getTableId() {
		return this.tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableDesc() {
		return this.tableDesc;
	}

	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}

	public String getDataTable() {
		return this.dataTable;
	}

	public void setDataTable(String dataTable) {
		this.dataTable = dataTable;
	}

	public String getDataWhere() {
		return this.dataWhere;
	}

	public void setDataWhere(String dataWhere) {
		this.dataWhere = dataWhere;
	}

	public String getHasExpand() {
		return this.hasExpand;
	}

	public void setHasExpand(String hasExpand) {
		this.hasExpand = hasExpand;
	}

	public String getHasPaging() {
		return this.hasPaging;
	}

	public void setHasPaging(String hasPaging) {
		this.hasPaging = hasPaging;
	}

	public String getTimeField() {
		return this.timeField;
	}

	public void setTimeField(String timeField) {
		this.timeField = timeField;
	}

	public String getFieldType() {
		return this.fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getTimeLevel() {
		return this.timeLevel;
	}

	public void setTimeLevel(String timeLevel) {
		this.timeLevel = timeLevel;
	}

	public String getRowClickedChartChange() {
		return this.rowClickedChartChange;
	}

	public void setRowClickedChartChange(String rowClickedChartChange) {
		this.rowClickedChartChange = rowClickedChartChange;
	}

	public String getRltChartId() {
		return this.rltChartId;
	}

	public void setRltChartId(String rltChartId) {
		this.rltChartId = rltChartId;
	}

	public String getHasHead() {
		return this.hasHead;
	}

	public void setHasHead(String hasHead) {
		this.hasHead = hasHead;
	}

	public String getSumDisplay() {
		return this.sumDisplay;
	}

	public void setSumDisplay(String sumDisplay) {
		this.sumDisplay = sumDisplay;
	}

	public String getDimAscol() {
		return this.dimAscol;
	}

	public void setDimAscol(String dimAscol) {
		this.dimAscol = dimAscol;
	}

	public String getCustomMsu() {
		return this.customMsu;
	}

	public void setCustomMsu(String customMsu) {
		this.customMsu = customMsu;
	}

	public String getThrowOld() {
		return this.throwOld;
	}

	public void setThrowOld(String throwOld) {
		this.throwOld = throwOld;
	}

	public String getHasExpandall() {
		return this.hasExpandall;
	}

	public void setHasExpandall(String hasExpandall) {
		this.hasExpandall = hasExpandall;
	}

	public String getChartChangeJs() {
		return this.chartChangeJs;
	}

	public void setChartChangeJs(String chartChangeJs) {
		this.chartChangeJs = chartChangeJs;
	}

	public String getTableType() {
		return this.tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

}