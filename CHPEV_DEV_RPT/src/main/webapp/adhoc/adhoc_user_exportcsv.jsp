<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<jsp:directive.page import="com.ailk.bi.adhoc.domain.UiAdhocUserListTable"/>
<jsp:directive.page import="com.ailk.bi.adhoc.service.impl.AdhocFacade"/>
<jsp:directive.page import="com.ailk.bi.adhoc.dao.impl.AdhocDao"/>
<jsp:directive.page import="com.ailk.bi.base.util.CommTool"/>
<jsp:directive.page import="com.ailk.bi.adhoc.util.AdhocUtil"/>
<jsp:directive.page import="com.ailk.bi.adhoc.util.AdhocConstant"/>
<jsp:directive.page import="com.ailk.bi.adhoc.domain.UiAdhocInfoDefTable"/>
<jsp:directive.page import="com.ailk.bi.base.struct.UserCtlRegionStruct"/>
<jsp:directive.page import="com.ailk.bi.base.util.WebConstKeys"/>
<jsp:directive.page import="java.util.HashMap"/>
<jsp:directive.page import="com.ailk.bi.common.dbtools.DAOFactory"/>

<%
//增加发展人部门控制
UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct)session.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
String tmpKey = "";
if(ctlStruct!=null&&ctlStruct.ctl_county_str!=null&&!"".equals(ctlStruct.ctl_county_str)){
	tmpKey=ctlStruct.ctl_county_str;
}
String tmpMap_code = "USER_DEVELOP_DEPART_ID4";
String tmpValue = "";
ServletContext servletContext = request.getSession().getServletContext();
HashMap codeMap = (HashMap)servletContext.getAttribute(WebConstKeys.ATTR_C_CODE_LIST);
		if(codeMap!=null){
			HashMap map = (HashMap)codeMap.get(tmpMap_code.trim().toUpperCase());
			if(map!=null){
				if(map.get(tmpKey) == null){
					tmpValue = "";
				}else{
					tmpValue = (String)map.get(tmpKey);
				}

			}
		}
if(!"".equals(tmpValue)){
	tmpValue = "(受限发展渠道查询："+tmpValue+")";
}
String rowcnt = (String)session.getAttribute(AdhocConstant.ADHOC_EXPORT_ROWCNT);


//String grp_id = DAOFactory.getCommonFac().getLoginUser(session).group_id;

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>清单下载选择</title>
<%@ include file="/base/commonHtml.jsp"%>
</head>
<body bgcolor="CDD5EC">
<!--<form name="tableQryForm" method="post" action="/servlet/adhocFileDownload">-->
<form name="tableQryForm" method="post" action="AdhocInfoExport.rptdo?oper_type=doexportcsv">

          <table width="100%" height="125" border="0" align="center" cellpadding="1" cellspacing="1">
            <tr>
              <td align="center" class="note-side">
                <table width="100%">
                  <tr>
                    <td width="80" align="center"><img src="<%=request.getContextPath()%>/images/system/icon-locked.gif" ></td>
                    <td ><table width="100%" border="0">
                        <tr height="25">
                          <td>如果你的记录数小于10000条,请选择第一个选项,很快就会生成你要的文件</td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                        </tr>
						<!--
                        <tr>
                          <td><input type="radio" name="row" value="2000" <%if (rowcnt.equals("2000")) out.print("checked");%>><=2000条</td>
                        </tr>
                        <tr>
                          <td><input type="radio" name="row" value="5000" <%if (rowcnt.equals("5000")) out.print("checked");%>><=5000条</td>
                        </tr>
						-->
                        <tr height="25">
                          <td><input type="radio" name="row" value="10000" <%if (rowcnt.equals("10000")) out.print("checked");%>><=10000条</td>
                        </tr>
                        <tr height="25">
                          <td><input type="radio" name="row" value="50000" <%if (rowcnt.equals("50000")) out.print("checked");%>><=50000条</td>
                        </tr>
						<tr height="25">
                          <td><input type="radio" name="row" value="-1">全部</td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td><input name="btnexport" type="button" class="btn3" value="确定" onclick="javascript:doExportCsv(this);">&nbsp;<input type="button" name="btn_close" value="关闭" class="btn3" onclick="window.close();"></td>
                        </tr>
                    </table></td>
                  </tr>
              </table></td>
            </tr>
  </table>

</form>
</body>
</html>
<script>

function doExportCsv(bt){
	bt.disabled = true;
	var choseValue = 0;
	var a = document.getElementsByName("row");
	for(i=0;i<a.length;i++)
	{
		if(a[i].checked)
			{
			choseValue = a[i].value;
		break;
		}
	}

	if (choseValue==10000)
	{
		bt.value='请稍等...';
//		tableQryForm.action="AdhocUserListToExcel.screen";
	}

//	alert(tableQryForm.row.value());

    tableQryForm.submit();
//	window.close();
}

domHover(".btn3", "btn3_hover");

function switchClass2(theObj)
{
	if(theObj.className.indexOf("_hover")<0)
{
	theObj.className=theObj.className+"_hover";
}
else
{
	theObj.className=theObj.className.replace("_hover","");
}
}
</script>