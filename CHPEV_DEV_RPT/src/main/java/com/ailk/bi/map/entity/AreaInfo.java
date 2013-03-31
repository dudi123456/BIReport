package com.ailk.bi.map.entity;

import com.ailk.bi.common.event.JBTableBase;

public class AreaInfo extends JBTableBase {

	private static final long serialVersionUID = 1L;
	public String area_Id;
	public String area_Name;
	public String code_Id;
	public String bgColor;
	public String getArea_Id() {
		return area_Id;
	}
	public void setArea_Id(String area_Id) {
		this.area_Id = area_Id;
	}
	public String getArea_Name() {
		return area_Name;
	}
	public void setArea_Name(String area_Name) {
		this.area_Name = area_Name;
	}
	public String getCode_Id() {
		return code_Id;
	}
	public void setCode_Id(String code_Id) {
		this.code_Id = code_Id;
	}
	public String getBgColor() {
		return bgColor;
	}
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
