<?xml version="1.0" encoding="UTF-8"?>
<AreaData>
<DataInfo>
	 <#if mapInfo.dataList?exists>
	  <#list mapInfo.dataList as data>
		  <DataItem  AreaID="${(data.areaId)!0}" value1="${(data.value1)!0}" value2="${(data.value2)!0}" value3="${(data.value3)!0}" value4="${(data.value4)!0}"></DataItem>
	  </#list>
	 </#if>
</DataInfo>
<MapInfo Title ="${(mapInfo.title)!0}" DateTime="${(mapInfo.dateTime)!0}" Drift="${(mapInfo.drift)!0}" FatherID = "${(mapInfo.fatherID)!0}" FatherName="${(mapInfo.fatherName)!0}" parameName="${(mapInfo.parameName)!0}" showNum="${(mapInfo.showNum)!0}" showTitle="${(mapInfo.showTitle)!0}" dataField ="${(mapInfo.dataField)!0}"
TopCode="${(mapInfo.topCode)!0}" LowCode="${(mapInfo.lowCode)!0}" BGColor="${(mapInfo.bgColor)!0}" IsButton="${(mapInfo.isButton)!0}"></MapInfo>
<ColorInfo>
<#if mapInfo.colorList?exists>
	  <#list mapInfo.colorList as c>
		<ColorItem  colorName="${(c.color_name)!0}" colorRGB="${(c.color_rgb)!0}" staNum="${(c.sta_Num)!0}" endNum="${(c.end_Num)!0}"></ColorItem>
	  </#list>
	 </#if>
</ColorInfo>
<AreaInfo>
<#if mapInfo.areaList?exists>
	  <#list mapInfo.areaList as a>
		<AreaItem  AreaID="${(a.area_Id)!0}" AreaName="${(a.area_Name)!0}" CodeId="${(a.code_Id)!0}" BgColor="${(a.bgColor)}"></AreaItem>
	  </#list>
	 </#if>
</AreaInfo>
<RoleInfo>
<#if mapInfo.roles?exists>
  <#list mapInfo.roles as a>
	<RoleItem  CityID="${(a)!0}" ></RoleItem>
		  </#list>
	 </#if>
</RoleInfo>
</AreaData>