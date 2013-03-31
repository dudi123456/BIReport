package com.ailk.bi.subject.admin.entity;

/**
 * UiSubjectCommonColDefId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings({ "serial" })
public class UiSubjectCommonColDef implements java.io.Serializable {

	// Fields
	private String rowId;

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	private String tableId;
	private String colId;
	private String colName;
	private String colDesc;
	private String colSequence;
	private String isMeasure;
	private String dimAswhere;
	private String defaultDisplay;
	private String isExpandCol;
	private String defaultDrilled;
	private String initLevel;
	private String dimAscol;
	private String codeField;
	private String descField;
	private String isRatio;
	private String dataType;
	private String digitLength;
	private String hasComratio;
	private String hasLink;
	private String linkUrl;
	private String linkTarget;
	private String hasLast;
	private String lastDisplay;
	private String riseArrowColor;
	private String hasLastLink;
	private String lastUrl;
	private String lastTarget;
	private String hasLoop;
	private String loopDisplay;
	private String hasLoopLink;
	private String loopUrl;
	private String loopTarget;
	private String isColClickChartChange;
	private String colRltChartId;
	private String isCellClickChartChange;
	private String cellRltChartId;
	private String status;
	private String descAstitle;
	private String lastVarDisplay;
	private String loopVarDisplay;
	private String totalDisplayed;

	// Constructors

	/** default constructor */
	public UiSubjectCommonColDef() {
	}

	/** minimal constructor */
	public UiSubjectCommonColDef(String tableId, String colId, String colName,
			String colSequence, String isMeasure, String dimAswhere,
			String defaultDisplay, String isExpandCol, String defaultDrilled,
			String dimAscol, String codeField, String isRatio,
			String hasComratio, String hasLink, String hasLast,
			String lastDisplay, String hasLastLink, String hasLoop,
			String loopDisplay, String hasLoopLink,
			String isColClickChartChange, String isCellClickChartChange,
			String status, String descAstitle, String lastVarDisplay,
			String loopVarDisplay) {
		this.tableId = tableId;
		this.colId = colId;
		this.colName = colName;
		this.colSequence = colSequence;
		this.isMeasure = isMeasure;
		this.dimAswhere = dimAswhere;
		this.defaultDisplay = defaultDisplay;
		this.isExpandCol = isExpandCol;
		this.defaultDrilled = defaultDrilled;
		this.dimAscol = dimAscol;
		this.codeField = codeField;
		this.isRatio = isRatio;
		this.hasComratio = hasComratio;
		this.hasLink = hasLink;
		this.hasLast = hasLast;
		this.lastDisplay = lastDisplay;
		this.hasLastLink = hasLastLink;
		this.hasLoop = hasLoop;
		this.loopDisplay = loopDisplay;
		this.hasLoopLink = hasLoopLink;
		this.isColClickChartChange = isColClickChartChange;
		this.isCellClickChartChange = isCellClickChartChange;
		this.status = status;
		this.descAstitle = descAstitle;
		this.lastVarDisplay = lastVarDisplay;
		this.loopVarDisplay = loopVarDisplay;
	}

	/** full constructor */
	public UiSubjectCommonColDef(String tableId, String colId, String colName,
			String colDesc, String colSequence, String isMeasure,
			String dimAswhere, String defaultDisplay, String isExpandCol,
			String defaultDrilled, String initLevel, String dimAscol,
			String codeField, String descField, String isRatio,
			String dataType, String digitLength, String hasComratio,
			String hasLink, String linkUrl, String linkTarget, String hasLast,
			String lastDisplay, String riseArrowColor, String hasLastLink,
			String lastUrl, String lastTarget, String hasLoop,
			String loopDisplay, String hasLoopLink, String loopUrl,
			String loopTarget, String isColClickChartChange,
			String colRltChartId, String isCellClickChartChange,
			String cellRltChartId, String status, String descAstitle,
			String lastVarDisplay, String loopVarDisplay, String totalDisplayed) {
		this.tableId = tableId;
		this.colId = colId;
		this.colName = colName;
		this.colDesc = colDesc;
		this.colSequence = colSequence;
		this.isMeasure = isMeasure;
		this.dimAswhere = dimAswhere;
		this.defaultDisplay = defaultDisplay;
		this.isExpandCol = isExpandCol;
		this.defaultDrilled = defaultDrilled;
		this.initLevel = initLevel;
		this.dimAscol = dimAscol;
		this.codeField = codeField;
		this.descField = descField;
		this.isRatio = isRatio;
		this.dataType = dataType;
		this.digitLength = digitLength;
		this.hasComratio = hasComratio;
		this.hasLink = hasLink;
		this.linkUrl = linkUrl;
		this.linkTarget = linkTarget;
		this.hasLast = hasLast;
		this.lastDisplay = lastDisplay;
		this.riseArrowColor = riseArrowColor;
		this.hasLastLink = hasLastLink;
		this.lastUrl = lastUrl;
		this.lastTarget = lastTarget;
		this.hasLoop = hasLoop;
		this.loopDisplay = loopDisplay;
		this.hasLoopLink = hasLoopLink;
		this.loopUrl = loopUrl;
		this.loopTarget = loopTarget;
		this.isColClickChartChange = isColClickChartChange;
		this.colRltChartId = colRltChartId;
		this.isCellClickChartChange = isCellClickChartChange;
		this.cellRltChartId = cellRltChartId;
		this.status = status;
		this.descAstitle = descAstitle;
		this.lastVarDisplay = lastVarDisplay;
		this.loopVarDisplay = loopVarDisplay;
		this.totalDisplayed = totalDisplayed;
	}

	// Property accessors

	public String getTableId() {
		return this.tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getColId() {
		return this.colId;
	}

	public void setColId(String colId) {
		this.colId = colId;
	}

	public String getColName() {
		return this.colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getColDesc() {
		return this.colDesc;
	}

	public void setColDesc(String colDesc) {
		this.colDesc = colDesc;
	}

	public String getColSequence() {
		return this.colSequence;
	}

	public void setColSequence(String colSequence) {
		this.colSequence = colSequence;
	}

	public String getIsMeasure() {
		return this.isMeasure;
	}

	public void setIsMeasure(String isMeasure) {
		this.isMeasure = isMeasure;
	}

	public String getDimAswhere() {
		return this.dimAswhere;
	}

	public void setDimAswhere(String dimAswhere) {
		this.dimAswhere = dimAswhere;
	}

	public String getDefaultDisplay() {
		return this.defaultDisplay;
	}

	public void setDefaultDisplay(String defaultDisplay) {
		this.defaultDisplay = defaultDisplay;
	}

	public String getIsExpandCol() {
		return this.isExpandCol;
	}

	public void setIsExpandCol(String isExpandCol) {
		this.isExpandCol = isExpandCol;
	}

	public String getDefaultDrilled() {
		return this.defaultDrilled;
	}

	public void setDefaultDrilled(String defaultDrilled) {
		this.defaultDrilled = defaultDrilled;
	}

	public String getInitLevel() {
		return this.initLevel;
	}

	public void setInitLevel(String initLevel) {
		this.initLevel = initLevel;
	}

	public String getDimAscol() {
		return this.dimAscol;
	}

	public void setDimAscol(String dimAscol) {
		this.dimAscol = dimAscol;
	}

	public String getCodeField() {
		return this.codeField;
	}

	public void setCodeField(String codeField) {
		this.codeField = codeField;
	}

	public String getDescField() {
		return this.descField;
	}

	public void setDescField(String descField) {
		this.descField = descField;
	}

	public String getIsRatio() {
		return this.isRatio;
	}

	public void setIsRatio(String isRatio) {
		this.isRatio = isRatio;
	}

	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDigitLength() {
		return this.digitLength;
	}

	public void setDigitLength(String digitLength) {
		this.digitLength = digitLength;
	}

	public String getHasComratio() {
		return this.hasComratio;
	}

	public void setHasComratio(String hasComratio) {
		this.hasComratio = hasComratio;
	}

	public String getHasLink() {
		return this.hasLink;
	}

	public void setHasLink(String hasLink) {
		this.hasLink = hasLink;
	}

	public String getLinkUrl() {
		return this.linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getLinkTarget() {
		return this.linkTarget;
	}

	public void setLinkTarget(String linkTarget) {
		this.linkTarget = linkTarget;
	}

	public String getHasLast() {
		return this.hasLast;
	}

	public void setHasLast(String hasLast) {
		this.hasLast = hasLast;
	}

	public String getLastDisplay() {
		return this.lastDisplay;
	}

	public void setLastDisplay(String lastDisplay) {
		this.lastDisplay = lastDisplay;
	}

	public String getRiseArrowColor() {
		return this.riseArrowColor;
	}

	public void setRiseArrowColor(String riseArrowColor) {
		this.riseArrowColor = riseArrowColor;
	}

	public String getHasLastLink() {
		return this.hasLastLink;
	}

	public void setHasLastLink(String hasLastLink) {
		this.hasLastLink = hasLastLink;
	}

	public String getLastUrl() {
		return this.lastUrl;
	}

	public void setLastUrl(String lastUrl) {
		this.lastUrl = lastUrl;
	}

	public String getLastTarget() {
		return this.lastTarget;
	}

	public void setLastTarget(String lastTarget) {
		this.lastTarget = lastTarget;
	}

	public String getHasLoop() {
		return this.hasLoop;
	}

	public void setHasLoop(String hasLoop) {
		this.hasLoop = hasLoop;
	}

	public String getLoopDisplay() {
		return this.loopDisplay;
	}

	public void setLoopDisplay(String loopDisplay) {
		this.loopDisplay = loopDisplay;
	}

	public String getHasLoopLink() {
		return this.hasLoopLink;
	}

	public void setHasLoopLink(String hasLoopLink) {
		this.hasLoopLink = hasLoopLink;
	}

	public String getLoopUrl() {
		return this.loopUrl;
	}

	public void setLoopUrl(String loopUrl) {
		this.loopUrl = loopUrl;
	}

	public String getLoopTarget() {
		return this.loopTarget;
	}

	public void setLoopTarget(String loopTarget) {
		this.loopTarget = loopTarget;
	}

	public String getIsColClickChartChange() {
		return this.isColClickChartChange;
	}

	public void setIsColClickChartChange(String isColClickChartChange) {
		this.isColClickChartChange = isColClickChartChange;
	}

	public String getColRltChartId() {
		return this.colRltChartId;
	}

	public void setColRltChartId(String colRltChartId) {
		this.colRltChartId = colRltChartId;
	}

	public String getIsCellClickChartChange() {
		return this.isCellClickChartChange;
	}

	public void setIsCellClickChartChange(String isCellClickChartChange) {
		this.isCellClickChartChange = isCellClickChartChange;
	}

	public String getCellRltChartId() {
		return this.cellRltChartId;
	}

	public void setCellRltChartId(String cellRltChartId) {
		this.cellRltChartId = cellRltChartId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescAstitle() {
		return this.descAstitle;
	}

	public void setDescAstitle(String descAstitle) {
		this.descAstitle = descAstitle;
	}

	public String getLastVarDisplay() {
		return this.lastVarDisplay;
	}

	public void setLastVarDisplay(String lastVarDisplay) {
		this.lastVarDisplay = lastVarDisplay;
	}

	public String getLoopVarDisplay() {
		return this.loopVarDisplay;
	}

	public void setLoopVarDisplay(String loopVarDisplay) {
		this.loopVarDisplay = loopVarDisplay;
	}

	public String getTotalDisplayed() {
		return this.totalDisplayed;
	}

	public void setTotalDisplayed(String totalDisplayed) {
		this.totalDisplayed = totalDisplayed;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UiSubjectCommonColDef))
			return false;
		UiSubjectCommonColDef castOther = (UiSubjectCommonColDef) other;

		return ((this.getTableId() == castOther.getTableId()) || (this
				.getTableId() != null && castOther.getTableId() != null && this
				.getTableId().equals(castOther.getTableId())))
				&& ((this.getColId() == castOther.getColId()) || (this
						.getColId() != null && castOther.getColId() != null && this
						.getColId().equals(castOther.getColId())))
				&& ((this.getColName() == castOther.getColName()) || (this
						.getColName() != null && castOther.getColName() != null && this
						.getColName().equals(castOther.getColName())))
				&& ((this.getColDesc() == castOther.getColDesc()) || (this
						.getColDesc() != null && castOther.getColDesc() != null && this
						.getColDesc().equals(castOther.getColDesc())))
				&& ((this.getColSequence() == castOther.getColSequence()) || (this
						.getColSequence() != null
						&& castOther.getColSequence() != null && this
						.getColSequence().equals(castOther.getColSequence())))
				&& ((this.getIsMeasure() == castOther.getIsMeasure()) || (this
						.getIsMeasure() != null
						&& castOther.getIsMeasure() != null && this
						.getIsMeasure().equals(castOther.getIsMeasure())))
				&& ((this.getDimAswhere() == castOther.getDimAswhere()) || (this
						.getDimAswhere() != null
						&& castOther.getDimAswhere() != null && this
						.getDimAswhere().equals(castOther.getDimAswhere())))
				&& ((this.getDefaultDisplay() == castOther.getDefaultDisplay()) || (this
						.getDefaultDisplay() != null
						&& castOther.getDefaultDisplay() != null && this
						.getDefaultDisplay().equals(
								castOther.getDefaultDisplay())))
				&& ((this.getIsExpandCol() == castOther.getIsExpandCol()) || (this
						.getIsExpandCol() != null
						&& castOther.getIsExpandCol() != null && this
						.getIsExpandCol().equals(castOther.getIsExpandCol())))
				&& ((this.getDefaultDrilled() == castOther.getDefaultDrilled()) || (this
						.getDefaultDrilled() != null
						&& castOther.getDefaultDrilled() != null && this
						.getDefaultDrilled().equals(
								castOther.getDefaultDrilled())))
				&& ((this.getInitLevel() == castOther.getInitLevel()) || (this
						.getInitLevel() != null
						&& castOther.getInitLevel() != null && this
						.getInitLevel().equals(castOther.getInitLevel())))
				&& ((this.getDimAscol() == castOther.getDimAscol()) || (this
						.getDimAscol() != null
						&& castOther.getDimAscol() != null && this
						.getDimAscol().equals(castOther.getDimAscol())))
				&& ((this.getCodeField() == castOther.getCodeField()) || (this
						.getCodeField() != null
						&& castOther.getCodeField() != null && this
						.getCodeField().equals(castOther.getCodeField())))
				&& ((this.getDescField() == castOther.getDescField()) || (this
						.getDescField() != null
						&& castOther.getDescField() != null && this
						.getDescField().equals(castOther.getDescField())))
				&& ((this.getIsRatio() == castOther.getIsRatio()) || (this
						.getIsRatio() != null && castOther.getIsRatio() != null && this
						.getIsRatio().equals(castOther.getIsRatio())))
				&& ((this.getDataType() == castOther.getDataType()) || (this
						.getDataType() != null
						&& castOther.getDataType() != null && this
						.getDataType().equals(castOther.getDataType())))
				&& ((this.getDigitLength() == castOther.getDigitLength()) || (this
						.getDigitLength() != null
						&& castOther.getDigitLength() != null && this
						.getDigitLength().equals(castOther.getDigitLength())))
				&& ((this.getHasComratio() == castOther.getHasComratio()) || (this
						.getHasComratio() != null
						&& castOther.getHasComratio() != null && this
						.getHasComratio().equals(castOther.getHasComratio())))
				&& ((this.getHasLink() == castOther.getHasLink()) || (this
						.getHasLink() != null && castOther.getHasLink() != null && this
						.getHasLink().equals(castOther.getHasLink())))
				&& ((this.getLinkUrl() == castOther.getLinkUrl()) || (this
						.getLinkUrl() != null && castOther.getLinkUrl() != null && this
						.getLinkUrl().equals(castOther.getLinkUrl())))
				&& ((this.getLinkTarget() == castOther.getLinkTarget()) || (this
						.getLinkTarget() != null
						&& castOther.getLinkTarget() != null && this
						.getLinkTarget().equals(castOther.getLinkTarget())))
				&& ((this.getHasLast() == castOther.getHasLast()) || (this
						.getHasLast() != null && castOther.getHasLast() != null && this
						.getHasLast().equals(castOther.getHasLast())))
				&& ((this.getLastDisplay() == castOther.getLastDisplay()) || (this
						.getLastDisplay() != null
						&& castOther.getLastDisplay() != null && this
						.getLastDisplay().equals(castOther.getLastDisplay())))
				&& ((this.getRiseArrowColor() == castOther.getRiseArrowColor()) || (this
						.getRiseArrowColor() != null
						&& castOther.getRiseArrowColor() != null && this
						.getRiseArrowColor().equals(
								castOther.getRiseArrowColor())))
				&& ((this.getHasLastLink() == castOther.getHasLastLink()) || (this
						.getHasLastLink() != null
						&& castOther.getHasLastLink() != null && this
						.getHasLastLink().equals(castOther.getHasLastLink())))
				&& ((this.getLastUrl() == castOther.getLastUrl()) || (this
						.getLastUrl() != null && castOther.getLastUrl() != null && this
						.getLastUrl().equals(castOther.getLastUrl())))
				&& ((this.getLastTarget() == castOther.getLastTarget()) || (this
						.getLastTarget() != null
						&& castOther.getLastTarget() != null && this
						.getLastTarget().equals(castOther.getLastTarget())))
				&& ((this.getHasLoop() == castOther.getHasLoop()) || (this
						.getHasLoop() != null && castOther.getHasLoop() != null && this
						.getHasLoop().equals(castOther.getHasLoop())))
				&& ((this.getLoopDisplay() == castOther.getLoopDisplay()) || (this
						.getLoopDisplay() != null
						&& castOther.getLoopDisplay() != null && this
						.getLoopDisplay().equals(castOther.getLoopDisplay())))
				&& ((this.getHasLoopLink() == castOther.getHasLoopLink()) || (this
						.getHasLoopLink() != null
						&& castOther.getHasLoopLink() != null && this
						.getHasLoopLink().equals(castOther.getHasLoopLink())))
				&& ((this.getLoopUrl() == castOther.getLoopUrl()) || (this
						.getLoopUrl() != null && castOther.getLoopUrl() != null && this
						.getLoopUrl().equals(castOther.getLoopUrl())))
				&& ((this.getLoopTarget() == castOther.getLoopTarget()) || (this
						.getLoopTarget() != null
						&& castOther.getLoopTarget() != null && this
						.getLoopTarget().equals(castOther.getLoopTarget())))
				&& ((this.getIsColClickChartChange() == castOther
						.getIsColClickChartChange()) || (this
						.getIsColClickChartChange() != null
						&& castOther.getIsColClickChartChange() != null && this
						.getIsColClickChartChange().equals(
								castOther.getIsColClickChartChange())))
				&& ((this.getColRltChartId() == castOther.getColRltChartId()) || (this
						.getColRltChartId() != null
						&& castOther.getColRltChartId() != null && this
						.getColRltChartId()
						.equals(castOther.getColRltChartId())))
				&& ((this.getIsCellClickChartChange() == castOther
						.getIsCellClickChartChange()) || (this
						.getIsCellClickChartChange() != null
						&& castOther.getIsCellClickChartChange() != null && this
						.getIsCellClickChartChange().equals(
								castOther.getIsCellClickChartChange())))
				&& ((this.getCellRltChartId() == castOther.getCellRltChartId()) || (this
						.getCellRltChartId() != null
						&& castOther.getCellRltChartId() != null && this
						.getCellRltChartId().equals(
								castOther.getCellRltChartId())))
				&& ((this.getStatus() == castOther.getStatus()) || (this
						.getStatus() != null && castOther.getStatus() != null && this
						.getStatus().equals(castOther.getStatus())))
				&& ((this.getDescAstitle() == castOther.getDescAstitle()) || (this
						.getDescAstitle() != null
						&& castOther.getDescAstitle() != null && this
						.getDescAstitle().equals(castOther.getDescAstitle())))
				&& ((this.getLastVarDisplay() == castOther.getLastVarDisplay()) || (this
						.getLastVarDisplay() != null
						&& castOther.getLastVarDisplay() != null && this
						.getLastVarDisplay().equals(
								castOther.getLastVarDisplay())))
				&& ((this.getLoopVarDisplay() == castOther.getLoopVarDisplay()) || (this
						.getLoopVarDisplay() != null
						&& castOther.getLoopVarDisplay() != null && this
						.getLoopVarDisplay().equals(
								castOther.getLoopVarDisplay())))
				&& ((this.getTotalDisplayed() == castOther.getTotalDisplayed()) || (this
						.getTotalDisplayed() != null
						&& castOther.getTotalDisplayed() != null && this
						.getTotalDisplayed().equals(
								castOther.getTotalDisplayed())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTableId() == null ? 0 : this.getTableId().hashCode());
		result = 37 * result
				+ (getColId() == null ? 0 : this.getColId().hashCode());
		result = 37 * result
				+ (getColName() == null ? 0 : this.getColName().hashCode());
		result = 37 * result
				+ (getColDesc() == null ? 0 : this.getColDesc().hashCode());
		result = 37
				* result
				+ (getColSequence() == null ? 0 : this.getColSequence()
						.hashCode());
		result = 37 * result
				+ (getIsMeasure() == null ? 0 : this.getIsMeasure().hashCode());
		result = 37
				* result
				+ (getDimAswhere() == null ? 0 : this.getDimAswhere()
						.hashCode());
		result = 37
				* result
				+ (getDefaultDisplay() == null ? 0 : this.getDefaultDisplay()
						.hashCode());
		result = 37
				* result
				+ (getIsExpandCol() == null ? 0 : this.getIsExpandCol()
						.hashCode());
		result = 37
				* result
				+ (getDefaultDrilled() == null ? 0 : this.getDefaultDrilled()
						.hashCode());
		result = 37 * result
				+ (getInitLevel() == null ? 0 : this.getInitLevel().hashCode());
		result = 37 * result
				+ (getDimAscol() == null ? 0 : this.getDimAscol().hashCode());
		result = 37 * result
				+ (getCodeField() == null ? 0 : this.getCodeField().hashCode());
		result = 37 * result
				+ (getDescField() == null ? 0 : this.getDescField().hashCode());
		result = 37 * result
				+ (getIsRatio() == null ? 0 : this.getIsRatio().hashCode());
		result = 37 * result
				+ (getDataType() == null ? 0 : this.getDataType().hashCode());
		result = 37
				* result
				+ (getDigitLength() == null ? 0 : this.getDigitLength()
						.hashCode());
		result = 37
				* result
				+ (getHasComratio() == null ? 0 : this.getHasComratio()
						.hashCode());
		result = 37 * result
				+ (getHasLink() == null ? 0 : this.getHasLink().hashCode());
		result = 37 * result
				+ (getLinkUrl() == null ? 0 : this.getLinkUrl().hashCode());
		result = 37
				* result
				+ (getLinkTarget() == null ? 0 : this.getLinkTarget()
						.hashCode());
		result = 37 * result
				+ (getHasLast() == null ? 0 : this.getHasLast().hashCode());
		result = 37
				* result
				+ (getLastDisplay() == null ? 0 : this.getLastDisplay()
						.hashCode());
		result = 37
				* result
				+ (getRiseArrowColor() == null ? 0 : this.getRiseArrowColor()
						.hashCode());
		result = 37
				* result
				+ (getHasLastLink() == null ? 0 : this.getHasLastLink()
						.hashCode());
		result = 37 * result
				+ (getLastUrl() == null ? 0 : this.getLastUrl().hashCode());
		result = 37
				* result
				+ (getLastTarget() == null ? 0 : this.getLastTarget()
						.hashCode());
		result = 37 * result
				+ (getHasLoop() == null ? 0 : this.getHasLoop().hashCode());
		result = 37
				* result
				+ (getLoopDisplay() == null ? 0 : this.getLoopDisplay()
						.hashCode());
		result = 37
				* result
				+ (getHasLoopLink() == null ? 0 : this.getHasLoopLink()
						.hashCode());
		result = 37 * result
				+ (getLoopUrl() == null ? 0 : this.getLoopUrl().hashCode());
		result = 37
				* result
				+ (getLoopTarget() == null ? 0 : this.getLoopTarget()
						.hashCode());
		result = 37
				* result
				+ (getIsColClickChartChange() == null ? 0 : this
						.getIsColClickChartChange().hashCode());
		result = 37
				* result
				+ (getColRltChartId() == null ? 0 : this.getColRltChartId()
						.hashCode());
		result = 37
				* result
				+ (getIsCellClickChartChange() == null ? 0 : this
						.getIsCellClickChartChange().hashCode());
		result = 37
				* result
				+ (getCellRltChartId() == null ? 0 : this.getCellRltChartId()
						.hashCode());
		result = 37 * result
				+ (getStatus() == null ? 0 : this.getStatus().hashCode());
		result = 37
				* result
				+ (getDescAstitle() == null ? 0 : this.getDescAstitle()
						.hashCode());
		result = 37
				* result
				+ (getLastVarDisplay() == null ? 0 : this.getLastVarDisplay()
						.hashCode());
		result = 37
				* result
				+ (getLoopVarDisplay() == null ? 0 : this.getLoopVarDisplay()
						.hashCode());
		result = 37
				* result
				+ (getTotalDisplayed() == null ? 0 : this.getTotalDisplayed()
						.hashCode());
		return result;
	}

}