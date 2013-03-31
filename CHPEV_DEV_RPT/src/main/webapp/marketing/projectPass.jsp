<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.ProjectInfo"%>
<%@page import="com.ailk.bi.marketing.entity.TacticInfo"%>
<%@page import="com.ailk.bi.marketing.entity.ChannleInfo"%>
<%@page import="com.ailk.bi.marketing.entity.PassInfo"%>
 <%@page import="com.ailk.bi.marketing.entity.FileInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="com.ailk.bi.common.sysconfig.GetSystemConfig"%>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/date/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/icon.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/icontent.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/pubilicButton.css" />

	<%
	  String fileUrl =request.getContextPath()+GetSystemConfig.getBIBMConfig().getUploadFolder();//获得文件路径
			List<FileInfo> list = (List<FileInfo>) session.getAttribute("files");
	        List<PassInfo> passlist = ( List<PassInfo>)	session.getAttribute("projectPassList");
			String imgUrl =request.getContextPath()+"/images/validatebox_warning.gif";
			SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String selectCss = "class='easyui-combobox' style='width:180px;'";
			//获得审批人
			String personName = "select t.user_id,t.user_name  from ui_info_user t";


			ProjectInfo pinfo = (ProjectInfo)session.getAttribute("ProjectInfo");
			String name="";
			String type="";
			String level="";
			String priority="";
			String channle="";
			String tactic="";
			int tacticId=0;
			String content="";
			String date01=sdf.format(date);
			String date02=sdf.format(date);
			if(null!=pinfo){

				type=pinfo.getProjectType()+"";
				level=pinfo.getProjectLevel()+"";
				priority=pinfo.getPriority()+"";
				if (null!=pinfo.getEffectDate()) {
					date01=sdf.format(pinfo.getEffectDate());
				}
				if (null!=pinfo.getInvaildDate()) {
					date02=sdf.format(pinfo.getInvaildDate());
				}
				if (!StringTool.checkEmptyString(pinfo.getProjectContent())) {
					content=pinfo.getProjectContent();
				}
				if (!StringTool.checkEmptyString(pinfo.getProjectName())) {
					name=pinfo.getProjectName();
				}
					type=String.valueOf(pinfo.getProjectType());
					level=String.valueOf(pinfo.getProjectLevel());
					priority=String.valueOf(pinfo.getPriority());
				ChannleInfo CInfo = pinfo.getChannleInfo();
				if (null!=CInfo) {

					channle=CInfo.getChannleName();
				}
				TacticInfo TInfo = pinfo.getTacticInfo();
				if (null!=TInfo) {
					tactic = TInfo.getTacticName();
					tacticId = TInfo.getTacticId();
				}
			}
	%>


	<script language="javascript">

	function mySubmit(op) {
		form1.action="projectAction.rptdo?optype=projectList&doType="+op;
		form1.submit();
	}
	function openFile(fileUrl){
		window.open(fileUrl);
	}
	function opTactictWindow(){
		var url = "tacticAction.rptdo?optype=tacticPass&doType=modify&tactictId="+<%=tacticId%>;
		window.open(url,'newwindow','height=600,width=1000,top=60,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no, status=no') ;

	}
</script>
</head>
<body>
<form action="">
	<div class="validatebox-tabTD-title">方案基本信息<div id="div1" style="display: none;">
 <input type="button"  class="public-btn2" value="方案基本信息" onclick="javascript:document.getElementById('div11').style.display='block';document.getElementById('div1').style.display='none'" />
  </div></div>

<!-- 第一个层开始 -->
  <div id="div11" style="display: block">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
        <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />方案名称：</td>
          <td width="30%" class="validatebox-tabTD-right"><%=name %></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />方案类型：</td>
          <td class="validatebox-tabTD-right"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_PROJECT_TYPE",type)  %></td>
        </tr>
        <tr>
          <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />方案级别：</td>
          <td class="validatebox-tabTD-right"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_PROJECT_LEVEL",level)  %></td>
          <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />方案优先级：</td>
          <td class="validatebox-tabTD-right"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_PROJECT_PRIORITY",priority)  %></td>
        </tr>
        <tr>
          <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />渠道ID：</td>
          <td class="validatebox-tabTD-right"><%=channle %></td>
          <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />营销策略：</td>
          <td class="validatebox-tabTD-right"><%=tactic %>&nbsp;&nbsp;&nbsp;&nbsp;<input  type="button" class="public-btn2" value="查 看 详 细 信 息" onclick="opTactictWindow()" /></td>
        </tr>
        <tr>
          <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />开始时间：</td>
          <td class="validatebox-tabTD-right"><%=date01 %></td>
          <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />结束时间：</td>
          <td class="validatebox-tabTD-right"><%=date02 %></td>
        </tr>
         <tr>
          <td height="80" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />详细描述：</td>
          <td colspan="3" class="validatebox-tabTD-right"><%=content %><br></td>

        </tr>
	</table>
</div><!-- 第一个层结束 -->
		<div class="validatebox-tabTD-title">方案附件列表</div>
 <div class="list_content">
   <table >
     <tr width="100%">
       <th width="8%" align="center"> 序  号 </th>
       <th width="12%" align="center">文件编码</th>
       <th width="12%" align="center">文件名称</th>
       <th width="10%" align="center">文件大小 </th>
     </tr>
<%
 	if(null!=list){
       		for(int i = 0 ;i<list.size();i++){
       			if(i%2!=0){
       				%>
       				<tr class="jg">
       				<%
       			}else{
       			%>
       			 <tr><%} %>
          <td align="center">附件<%=i+1 %></td>
          <td align="center">
           <a style="color: blue" href="javascript:openFile('<%=fileUrl+list.get(i).getFileUrl()%>')">
          <%=list.get(i).getFileCode()%></a></td>
      	  <td align="center"><%=list.get(i).getFileName()%></td>
          <td align="center"><%=list.get(i).getFileSize()%>KB</td>
       </tr>	<%
       		}
       	}%>

       	 <tr class="jg">
              <td align="center"><label></label></td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              </tr>
     </table>
   </div>
   </form>
    <iframe id="ff" scrolling="0"width="100%" height="500px"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="<%=request.getContextPath()%>/marketing/projectPassList.jsp"></iframe><br>
</body>
</html>