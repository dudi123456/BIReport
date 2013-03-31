<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%
String msu_id = request.getParameter("msu_id");
%>
<html xmlns:v="urn:schemas-microsoft-com:vml">
<HEAD>
<TITLE>WebFlow 2005 Designer</TITLE>
<link href="../inc/style.css" type=text/css rel=stylesheet>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
<script language=jscript src="../inc/contextMenu/context.js"></script>
<script language=jscript src="../inc/webflow.js"></script>
<script language=jscript src="../inc/function.js"></script>
<script language=jscript src="../inc/shiftlang.js"></script>
<script language=jscript src="../inc/movestep.js"></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'> </script>
<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'> </script>
<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/SourceFlow.js'></script>
	<style>
		.tab-button-on {
		    background-image: url(<%=request.getContextPath()%>/images/common/taber/tab_bg_on.gif);
		    vertical-align: bottom;
		    font-size: 12px;
		    line-height: 22px;
		    padding-left: 10px;
		    padding-right: 10px;
		    color: #9f4700;
		}
		
		.tab-button-off {
		    background-image: url(<%=request.getContextPath()%>/images/common/taber/tab_bg_off.gif);
		    vertical-align: bottom;
		    line-height: 22px;
		    padding-left: 10px;
		    padding-right: 10px;
		    color: #666666;
		}
	</style> 
<SCRIPT>

var msu_id = '<%=msu_id%>';
SourceFlow.getSourceFlow(msu_id,setFlowXML);//读取流程图数据

//设置FlowXML控件的值
function setFlowXML(xmlDoc){ 
	document.all.FlowXML.value = xmlDoc;
}


function menuChanger(contentID,obj){		
	if(contentID==1) {
		if(msu_id == "") {
		alert("请选择指标!");
		return;
		}		
		obj.href="msuDet.rptdo?msu_id="+msu_id;
	}
	if(contentID==3) {
		if(msu_id == "") {
			alert("请选择指标!");
			return;
		}		
		obj.href="<%=request.getContextPath()%>/metamanage/msu/impactFlow.jsp?msu_id="+msu_id;
	}
}

</SCRIPT>
<STYLE>
v\: * {
	Behavior: url(#default#VML)
}
</STYLE>
</HEAD>

<BODY>
<table width="99" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td valign="bottom"><img src="<%=request.getContextPath()%>/images/common/taber/tab_line.gif" width="16" height="5"></td>
    <td><table border="0" cellspacing="0" cellpadding="0">
    <tr> 
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_left_off.gif" width="5" height="22"></td>
          <td nowrap class="tab-button-off"><a href="javascript:void(0)" onclick="menuChanger(1,this)" class="tab-button-off-link">指标定义</a></td>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_right_off.gif" width="5" height="22"></td>
        </tr>
       
      </table></td>
    <td><img src="<%=request.getContextPath()%>/images/common/system/size.gif" width="1"></td>
    <td><table border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_left_off.gif" width="5" height="22"></td>
          <td nowrap class="tab-button-on"><a href="javascript:void(0)" onclick="menuChanger(2,this)" class="tab-button-off-link">血缘分析</a></td>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_right_off.gif" width="5" height="22"></td>
        </tr>
      </table></td>
    <td><img src="<%=request.getContextPath()%>/biimages/size.gif" width="1"></td>
    <td><table border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_left_off.gif" width="5" height="22"></td>
          <td nowrap class="tab-button-off"><a href="javascript:void(0)" onclick="menuChanger(3,this)" class="tab-button-off-link">影响分析</a></td>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_right_off.gif" width="5" height="22"></td>
        </tr>
      </table></td>
    <td valign="bottom"><img src="<%=request.getContextPath()%>/images/common/taber/tab_right_line.gif" width="280" height="5"></td>
  </tr>
</table>
<INPUT TYPE="hidden" name="FlowXML"	onpropertychange='if(AUTODRAW) redrawVML();'>
		<TABLE cellspacing="0" cellpadding="0" class="panel_style">
			<TR>
				<TD width="3000" height="1000"
				    valign=top
					align=left>
					<v:group ID="FlowVML"
					style="left:10;top:40;width:800px;height:500px;position:absolute;"
					coordsize="2000,2000">
				</v:group>
				</TD>
			</TR>
		</TABLE>

</BODY>
</HTML>
