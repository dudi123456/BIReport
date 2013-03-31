package com.ailk.bi.map.entity;

import com.ailk.bi.common.event.JBTableBase;

public class ColorInfo extends JBTableBase {

	private static final long serialVersionUID = 1L;
	public String color_name;
	public String color_rgb;
	public String sta_Num;
	public String end_Num;
	public String group_Name;

	public ColorInfo()
	{
	}
	public ColorInfo(String name,String rgb,String num1,String num2)
	{
		this.color_name = name;
		this.color_rgb =rgb;
		this.sta_Num = num1;
		this.end_Num = num2;
	}
	public String getColor_name() {
		return color_name;
	}
	public void setColor_name(String color_name) {
		this.color_name = color_name;
	}
	public String getColor_rgb() {
		return color_rgb;
	}
	public void setColor_rgb(String color_rgb) {
		this.color_rgb = color_rgb;
	}
	public String getSta_Num() {
		return sta_Num;
	}
	public void setSta_Num(String sta_Num) {
		this.sta_Num = sta_Num;
	}
	public String getEnd_Num() {
		return end_Num;
	}
	public void setEnd_Num(String end_Num) {
		this.end_Num = end_Num;
	}
	public String getGroup_Name() {
		return group_Name;
	}
	public void setGroup_Name(String group_Name) {
		this.group_Name = group_Name;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
