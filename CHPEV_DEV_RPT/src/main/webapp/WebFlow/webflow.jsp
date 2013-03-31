<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<html xmlns:v="urn:schemas-microsoft-com:vml">
<HEAD>
<TITLE>WebFlow 2005 Designer</TITLE>
<link href="inc/style.css" type=text/css rel=stylesheet>
<script language=jscript src="inc/contextMenu/context.js"></script>
<script language=jscript src="inc/webflow.js"></script>
<script language=jscript src="inc/function.js"></script>
<script language=jscript src="inc/shiftlang.js"></script>
<script language=jscript src="inc/movestep.js"></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'> </script>
<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'> </script>
<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/JobTable.js'></script>

<SCRIPT>

/**
function loadFromXML(filename){
  var xmlDoc = new ActiveXObject('MSXML2.DOMDocument');
  xmlDoc.async = false;
  var flag = xmlDoc.load('flows/'+filename);
  if (!flag) {
    alert('文件导入失败！请先检查: flows/'+filename+' 是否存在！');
    return;
  }
  var xmlRoot = xmlDoc.documentElement;  
  FlowXML.value = xmlRoot.xml;
  drawTreeView();
}
*/

	JobTable.getJobTableFlow(setFlowXML);//读取流程图数据



//设置FlowXML控件的值
function setFlowXML(xmlDoc){
//	alert(xmlDoc);
	document.all.FlowXML.value = xmlDoc;
}

</SCRIPT>
<STYLE>
v\: * {
	Behavior: url(#default#VML)
}
</STYLE>
</HEAD>

<BODY>
<INPUT TYPE="hidden" name="FlowXML"	onpropertychange='if(AUTODRAW) redrawVML();'>
		<TABLE cellspacing="0" cellpadding="0" class="panel_style">
			<TR>
				<TD width="800" height="500"
				    valign=top
					align=left>
					<v:group ID="FlowVML"
					style="left:10;top:56;width:800px;height:500px;position:absolute;"
					coordsize="2000,2000">
				</v:group>
				</TD>
			</TR>
		</TABLE>
</BODY>
</HTML>
