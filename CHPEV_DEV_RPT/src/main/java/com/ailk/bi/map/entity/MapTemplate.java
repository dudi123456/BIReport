package com.ailk.bi.map.entity;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.common.event.JBTableBase;

public class MapTemplate extends JBTableBase {

	private static final long serialVersionUID = 1L;
	private String fatherID;
	private String fatherName;
	private String parameName;
	private String showNum;
	private String showTitle;
	private String dataField;
	private List<ColorInfo> colorList = new ArrayList<ColorInfo>();
	private List<DataInfo> dataList = new ArrayList<DataInfo>();
	private List<AreaInfo> areaList = new ArrayList<AreaInfo>();
	private String drift;
	private String topCode;
	private String lowCode;
	private List<String> roles;
	private String title;
	private String dateTime;
	private String isButton;



	public String getIsButton() {
		return isButton;
	}
	public void setIsButton(String isButton) {
		this.isButton = isButton;
	}
	public MapTemplate(String isButton,String title,String time ,String topCode,String lowCode ,List<String> roles, String Drift, String FatherID,
			String FatherName, List<ColorInfo> colors, List<DataInfo> datas,
			List<AreaInfo> areas, String parameName, String num,
			String showTitle, String dataField) {
		this.isButton = isButton;
		this.title = title;
		this.dateTime = time;
		this.topCode = topCode;
		this.lowCode = lowCode;
		this.roles = roles;
		this.drift = Drift;
		this.fatherID = FatherID;
		this.fatherName = FatherName;
		this.colorList = colors;
		this.dataList = datas;
		this.areaList = areas;
		this.parameName = parameName;
		this.showNum = num;
		this.showTitle = showTitle;
		this.dataField = dataField;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getFatherID() {
		return fatherID;
	}
	public void setFatherID(String fatherID) {
		this.fatherID = fatherID;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getParameName() {
		return parameName;
	}
	public void setParameName(String parameName) {
		this.parameName = parameName;
	}
	public String getShowNum() {
		return showNum;
	}
	public void setShowNum(String showNum) {
		this.showNum = showNum;
	}
	public String getShowTitle() {
		return showTitle;
	}
	public void setShowTitle(String showTitle) {
		this.showTitle = showTitle;
	}
	public String getDataField() {
		return dataField;
	}
	public void setDataField(String dataField) {
		this.dataField = dataField;
	}
	public List<ColorInfo> getColorList() {
		return colorList;
	}
	public void setColorList(List<ColorInfo> colorList) {
		this.colorList = colorList;
	}
	public List<DataInfo> getDataList() {
		return dataList;
	}
	public void setDataList(List<DataInfo> dataList) {
		this.dataList = dataList;
	}
	public List<AreaInfo> getAreaList() {
		return areaList;
	}
	public void setAreaList(List<AreaInfo> areaList) {
		this.areaList = areaList;
	}
	public String getDrift() {
		return drift;
	}
	public void setDrift(String drift) {
		this.drift = drift;
	}
	public String getTopCode() {
		return topCode;
	}
	public void setTopCode(String topCode) {
		this.topCode = topCode;
	}
	public String getLowCode() {
		return lowCode;
	}
	public void setLowCode(String lowCode) {
		this.lowCode = lowCode;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
