package com.ailk.bi.sigma;

public class SigmaGridEntityBean {

	private String reportName;

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportDesc() {
		return reportDesc;
	}

	public void setReportDesc(String reportDesc) {
		this.reportDesc = reportDesc;
	}

	private String reportDesc;

	private String sigmaId;

	private StringBuffer colJs;

	public StringBuffer getColJs() {
		return colJs;
	}

	public void setColJs(StringBuffer colJs) {
		this.colJs = colJs;
	}

	public String getSigmaId() {
		return sigmaId;
	}

	public void setSigmaId(String sigmaId) {
		this.sigmaId = sigmaId;
	}

	private String grid_demo_id;
	private String var_grid_demo_id;

	public String getVar_grid_demo_id() {
		return var_grid_demo_id;
	}

	public void setVar_grid_demo_id(String varGridDemoId) {
		var_grid_demo_id = varGridDemoId;
	}

	private StringBuffer dsOption;

	private StringBuffer colsOption;

	private StringBuffer gridOption;

	private String gridContainerBox;

	public String getGridContainerBox() {
		return gridContainerBox;
	}

	public void setGridContainerBox(String gridContainerBox) {
		this.gridContainerBox = gridContainerBox;
	}

	public String getGrid_demo_id() {
		return grid_demo_id;
	}

	public void setGrid_demo_id(String gridDemoId) {
		grid_demo_id = gridDemoId;
	}

	public StringBuffer getDsOption() {
		return dsOption;
	}

	public void setDsOption(StringBuffer dsOption) {
		this.dsOption = dsOption;
	}

	public StringBuffer getColsOption() {
		return colsOption;
	}

	public void setColsOption(StringBuffer colsOption) {
		this.colsOption = colsOption;
	}

	public StringBuffer getGridOption() {
		return gridOption;
	}

	public void setGridOption(StringBuffer gridOption) {
		this.gridOption = gridOption;
	}

}
