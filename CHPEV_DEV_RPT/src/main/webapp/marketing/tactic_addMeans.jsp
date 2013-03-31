<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.TargetInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>北京联通统一经营分析系统</title>
<BIBM:SelfRefreshTag pageNames="frmEdit.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart.js" ></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/dojo.js"></SCRIPT>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/XmlRPC.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/jquery.easyui.min.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/patch.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/date/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/icon.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/icontent.css">

</head>
<body style="background-color:#f9f9f9">
<form name="form1" method="post" action="">

<!-- 营销手段开始-->
<div class="list_content">
<table width="94%" >
            <tr width="100%">
              <th width="15%" align="center">接触方式</th>
              <th width="15%" align="center"> 接触约束 </th>
              <th width="17%" align="center"> 建议接触时间 </th>
              <th width="15%" align="center"> 建议接触周期</th>
              <th width="15%" align="center"> 短信模板</th>
              <th width="23%" align="center"> 运营脚本</th>
              </tr>

           <tr>
              <td align="center"><label>
              <select name="select5" style="width:120px">
                <option>外乎</option>
                <option>短信</option>
              </select>
              </label></td>
              <td align="center"><select name="select6" style="width:120px">
                <option>一月不超过2次</option>
                <option>一月不超过3次</option>
              </select></td>
              <td align="center"><input name="Input4" class="txtinput"  style="width:120px"/></td>
              <td align="center"><select name="select7" style="width:120px">
                <option>1周</option>
                <option>2周</option>
                            </select></td>
              <td align="center"><select name="select8" style="width:120px">
                <option>模板一</option>
                <option>模板二</option>
              </select></td>
              <td align="left"><input name="Input5" class="txtinput"  style="width:120px"/>
                <input type="button" class="public-btn2" value="选择" /></td>
              </tr>
          </table >
          <table width="94%">
           <tr class="jg">
              <td align="center"><label></label></td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
               <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              </tr>
           </table>
          </div><br>
          <input type="button" class="public-btn2" value="添加营销手段" />
            <input type="button" class="public-btn2" value="删除营销手段" />
<!-- 营销手段结束 -->

</form>
</body>
</html>
