<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import = "java.io.PrintWriter" %>
<%@ page import = "com.ailk.bi.base.util.*" %>
<%@ page import = "com.ailk.bi.common.dbtools.*" %>
<%@ page import = "com.ailk.bi.report.struct.ReportQryStruct" %>
<%@ page import="com.ailk.bi.common.app.AppException"%>
<%@ page import="com.ailk.bi.base.common.*"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Content-Language" content="zh-cn">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/bimain.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/css.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/newmain.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/titleStyle.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/Scroll.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dd99.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart.js" ></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/net.js"></SCRIPT>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/scw.js"></script>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/scwM.js"></script>
<title><%=CSysCode.SYS_TITLE%></title>


<%

ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
if(qryStruct == null){
	qryStruct = new ReportQryStruct();
}

%>
<BIBM:SelfRefreshTag pageNames="TableQryForm.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1"/>
<body onLoad="selfDisp()">
<script>
document.body.onmousemove=quickalt;
document.body.onmouseover=getalt;
document.body.onmouseout=restorealt;
var tempalt='';

function export_table_content(tables,names){
	window.open("SubjectTableContentExport.screen?table_id="+tables+"&table_name="+names,"数据导出","");
}
function open_metaExplain(adhoc_id)
    {
        var h = "500";
        var w = "750";
        var top = (screen.availHeight - h) / 2;
        var left = (screen.availWidth - w) / 2;
        var optstr = "height=" + h + ",width=" + w + ",left=" + left + ",top=" + top + ",status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
        var strUrl = "../adhoc/adhocMetaExplain.rptdo?adhoc_id=" + adhoc_id;
        var newsWin = window.open(strUrl, "editRptHead", optstr);
        if (newsWin != null) {
            newsWin.focus();
        }
    }


<%=JSTool.printDeviceModel()%>

function changelocation(locationid)
　　{
　　document.TableQryForm.qry__dim16.length = 0;
　　var locationid=locationid;
　　var i;

	document.TableQryForm.qry__dim16.options[document.TableQryForm.qry__dim16.length] = new Option("全部", "");

　　for (i=0;i < onecount; i++)
　　　　{
　　　　　　if (subcat[i][2] == locationid)
　　　　　　{
　　　　　　　　document.TableQryForm.qry__dim16.options[document.TableQryForm.qry__dim16.length] = new Option(subcat[i][0], subcat[i][1]);
　　　　　　}
　　　　}
　　}

</script>
</head>
<%
int screenx = Integer.parseInt(session.getAttribute(WebKeys.Screenx)==null?"1280":(String)session.getAttribute(WebKeys.Screenx));
int chartWidthTmp = (screenx-250)/2;

%>
<FORM name="TableQryForm" action="OppSubject.rptdo">
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">
<div style="display:none;position:absolute;" id=altlayer></div>


<table style="width: 100%;font-weight:bold" align="center" cellspacing="0" cellpadding="0">
	<tr class="tools">
		<td><font class="tooltitle">专题分析>>增值聚焦专题>>增值业务套餐分析>>短信套餐推荐</font></td><td><a href='javascript:;' onclick="open_metaExplain('SUBJECT')"><img src="<%=request.getContextPath()%>/biimages/home/nav_icon3.gif" width="14" height="14" border="0" hspace="10">指标说明</a></td>


		<td align="center" width="10%"><input type="button" id="button_outputExcel" value="导出EXCEL" onclick="javascript:export_table_content('product_t_001','竞争预警')"></td>
	</tr>
</table>
<table width="100%" height="3" border="0" cellpadding="0" cellspacing="0" class="squareB" >

      <tr>
        <td background="<%=request.getContextPath()%>/biimages/square_line_2.gif"></td>
        <td width="100%" height="100%" valign="top">

        <table width="100%" border="0">
          <tr>

			  <td width="80%" align="left">&nbsp;&nbsp;账期:<input style='input-text' value='<%=qryStruct.gather_month%>' type='text' name='qry__gather_month' id="qry__gather_month" readonly onClick="scwShowM(this,this);">
			  &nbsp;&nbsp;饱和度:<BIBM:TagSelectList listName="qry__dim2"
									focusID="<%=qryStruct.dim2%>" listID="0"
									selfSQL="select lvl_id,lvl_name from d_adv_param_lvl where param_id=106 order by lvl_id" />&nbsp;&nbsp;资费名称:<BIBM:TagSelectList listName="qry__dim3"
									focusID="<%=qryStruct.dim3%>" listID="0"
									selfSQL="select x.price_plan_id,x.price_plan_name from new_bi_ui.d_adv_focus_pp x where x.price_plan_cat_id=1 order by price_plan_id" />&nbsp;&nbsp;手机品牌:<BIBM:TagSelectList listName="qry__dim15"
									focusID="<%=qryStruct.dim15%>" listID="0" allFlag="" script="onChange='changelocation(document.TableQryForm.qry__dim15.options[document.TableQryForm.qry__dim15.selectedIndex].value)'"
									selfSQL="select distinct device_brand_id x1,device_brand_id x2 from D_DEVICE_BRAND_MODEL order by x1" />&nbsp;&nbsp;机型:<SELECT name='qry__dim16' id="qry__dim16">
								　　　　　<OPTION selected value="">全部</OPTION>

								　　　　</SELECT>

			  </td>


            <td  width="20%" align="left">


               <input id="button_submit" type="submit" value="确认"/>



            </td>
          </tr>


        </table>

        </td>
        <td background="<%=request.getContextPath()%>/biimages/square_line_3.gif"></td>
      </tr>

    </table>

<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td align="center" colspan="2"><iframe id="table_product_t_001" width="100%"
			height="430"
			src="SubjectCommTable.rptdo?table_id=add_macro_005&first=Y&table_height=425"
			frameborder="0" scrolling="no"></iframe></td>
	</tr>
</table>
<table cellspacing="0" cellpaddingx="0" style="width: 100%;">

		<tr>
		<td><%=CommTool.getEditorHtml("feeCommendShortMsg","0")%></td>
	</tr>
</table>

</FORM>
</body>
</html>