<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import="com.ailk.bi.sigma.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>

<%
String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>清单情况</title>

	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<SCRIPT language="javascript" src="<%=path%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language="javascript" src="<%=path%>/js/js.js"></SCRIPT>
<script type="text/javascript" src="<%=path%>/js/chart.js" ></script>
<script type="text/JavaScript" src="<%=path%>/js/date/scw.js"></script>
<script type="text/JavaScript" src="<%=path%>/js/date/scwM.js"></script>
<link rel="stylesheet" href="<%=path%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=path%>/css/other/tab_css.css" type="text/css">

  </head>
  <%
        //int sigmaId = 3;
		int sigmaId = Integer.parseInt((String)request.getAttribute("sigmaId"));
	    SigmaGridUtil util = new SigmaGridUtil(sigmaId,path);
		util.initConditonInfo();		
		//out.print(util.showSigmaScriptLanguage().toString());
		%>
<body>
	<form name="frmConditon" method="post" target="dataShowFrame">
    <table width="100%" height="5" border="0" cellpadding="0" cellspacing="0" class="squareB" >
      
      <tr> 
        <td background="<%=path%>biimages/square_line_2.gif"></td>
        <td width="100%" height="100%" valign="top">
        
        <table width="100%" border="0">
          <tr>
		  	<%
	List<SigmaGridConditionShowBean> list = util.getSigmaGridDisplayCondition();
		//System.out.println(list.size());
		int qryBtnShow = 1;
		if (list.size()==0){
			qryBtnShow = 0;
		}

		for (SigmaGridConditionShowBean bean:list){
			//StringBuffer sb = list.get(i).getShowHtmlString();
			%>
			  <td width="5%" nowrap><%=bean.getShowConName()%></td>
			<td nowrap><%=bean.getShowHtmlString()%></td>

			<%
			//out.println(bean.getShowConName()+bean.getShowHtmlString());
			
		}


		%>
          
			
            <td width="20%" colspan="3" nowrap><INPUT TYPE="hidden" NAME="sigmaId" id="sigmaId" value="<%=sigmaId%>"> </td>
			
			
            <td colspan="3" width="5%" nowrap>
          
            <%if (qryBtnShow==1){%>
               <input type="button" name="search" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="查询" onclick="javascript:doQrySearchTask();"> 
              <%}%>
                <input type="button" name="dc" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="导出" onclick="javascript:doDownLoad('<%=sigmaId%>');"> 
              
            
            </td>
          </tr>
          
	
        </table>
        
        </td>
        <td background="<%=path%>biimages/square_line_3.gif"></td>
      </tr>
  
    </table>
<%
	list = util.getSigmaGridHiddenCondition();
    
	List<SigmaPassParamBean> listParam = (ArrayList<SigmaPassParamBean>)request.getAttribute(SigmaGridConstant.GRID_CON_PARAM_REQ);
	String conValueDesc = "";
	for (SigmaPassParamBean beanParam:listParam){
		conValueDesc += beanParam.getPassParamDesc();
		
	}
//	List<SigmaPassParamBean> listParam = ;
		
		for (SigmaGridConditionShowBean bean:list){
			//StringBuffer sb = list.get(i).getShowHtmlString();
			if (bean.getPassType()==0){
				String value = "";
				String rConName = bean.getReqConName();
				for (SigmaPassParamBean beanParam:listParam){
					if (bean.getPassParam().equals(beanParam.getPassParamName())){
						value = beanParam.getPassParamValue();
						break;
					}
				}
				String strTxt = "<input style='"
					+ SigmaGridConstant.GRID_TXT_CLASS + "' value='"+ value + "' type='hidden' name='"
					+ rConName + "'>";
				out.println(strTxt);
			}else{
				out.println(bean.getShowHtmlString());
			}
		}
%>
		
	</form>
<table id="AutoNumber1" width="100%"  border="0" cellpadding="0" cellspacing="0">
      <TR><TD colspan="2" class="tab-boldtitle"><%=util.getSigmaReportInfo(sigmaId).getReportName()%>&nbsp;[<%=conValueDesc%>]</TD></TR>
</TABLE>
<iframe id="dataShowFrame" name = "dataShowFrame" scrolling="no" width="100%" height="500" border="0" frameborder="0" marginwidth="0" marginheight="0" src=""></iframe>

</html>

<script type="text/javascript" >

function doQrySearchTask(){
	frmConditon.action = "<%=path%>/gridReport/showData.jsp";
	//frmConditon.target="";
    frmConditon.submit();

}

 function doDownLoad(sigmaId)
    {
        var h = "350";
        var w = "900";
        var top = (screen.availHeight - h) / 2;
        var left = (screen.availWidth - w) / 2;
        var optstr = "height=" + h + ",width=" + w + ",left=" + left + ",top=" + top + ",status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
        var strUrl = "<%=path%>/gridReport/sigmaRptBuild.rptdo?sigmaId=" + sigmaId;
        var newsWin = window.open(strUrl, "editRptHead", optstr);
        if (newsWin != null) {
            newsWin.focus();
        }
    }

doQrySearchTask();

</script> 

