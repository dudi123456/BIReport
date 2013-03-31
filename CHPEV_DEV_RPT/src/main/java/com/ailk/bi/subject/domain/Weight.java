package com.ailk.bi.subject.domain;

import java.util.List;
import java.util.Map;

public class Weight
{
	//表名称
	public String tableName;
	
	//首列是否需要显示汇总数
	public String neadSum;
	
	//表头名称     占用列数
	public List<Map<String, Integer>> heads;
	
	//数据列     数据类型 1:字符串，2:百分号
	public List<Map<String, Integer>> cols;
	
	//此list中包含的Map有  
	//geoFlag 1:按用户级别显示南北权重列，2：显示北方权重，3：显示南方权重，4：显示南北方权限
	//geo_n 北方权重表中字段
	//geo_s 南方权重表中字段
	//geo_poi 南北权重显示的起始列数
	public List<Map<String, String>> geo;

	public Weight()
	{
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public String getNeadSum()
	{
		return neadSum;
	}

	public void setNeadSum(String neadSum)
	{
		this.neadSum = neadSum;
	}

	public List<Map<String, Integer>> getHeads()
	{
		return heads;
	}

	public void setHeads(List<Map<String, Integer>> heads)
	{
		this.heads = heads;
	}

	public List<Map<String, Integer>> getCols()
	{
		return cols;
	}

	public void setCols(List<Map<String, Integer>> cols)
	{
		this.cols = cols;
	}

	public List<Map<String, String>> getGeo()
	{
		return geo;
	}

	public void setGeo(List<Map<String, String>> geo)
	{
		this.geo = geo;
	}
	
}
