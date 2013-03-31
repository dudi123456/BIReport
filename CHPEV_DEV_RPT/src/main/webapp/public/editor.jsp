<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.util.CommTool"%>
<%
	String sid = CommTool.getParameterGB(request, "id");
	String sdate = CommTool.getParameterGB(request, "date");
	String memo = CommTool.getEditorInfo(sid, sdate);
%>
<%@ include file="/base/commonHtml.jsp"%>
<!doctype html>
<html>
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=context%>/css/icontent.css" type="text/css">
<title>文字编辑</title>
<style>
form {
	margin: 0;
}

textarea {
	display: block;
}
</style>
<script charset="utf-8" src="<%=context%>/js/kindeditor-min.js"></script>
<script>
	KE.show({
		id : 'memo',
		allowUpload : false,
		minWidth : 500,
		items : [
		 		'source', '|', 'fullscreen', 'undo', 'redo', 'cut', 'copy', 'paste',
		 		'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
		 		'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent',
		 		'|', 'selectall', '-','subscript',	'superscript',
		 		'title', 'fontname', 'fontsize', '|', 'textcolor', 'bgcolor', 'bold',
		 		'italic', 'underline', 'strikethrough', 'removeformat', '|', 'advtable', 'hr', 'emoticons', 'link', 'unlink',
		 	],
		afterCreate : function(id) {

			KE.event.ctrl(document, 13, function() {
				KE.sync(id);
				document.forms['mainFrm'].submit();
			});
			KE.event.ctrl(KE.g[id].iframeDoc, 13, function() {
				KE.sync(id);
				document.forms['mainFrm'].submit();
			});
		}
	});
</script>
<script language="javascript">
	function synContent(){
		KE.sync("memo");
	    window.opener.document.getElementById("report_msu_memo").innerHTML = document.getElementById("memo").value;
	 	document.mainFrm.action = "editor.rptdo";
	 	document.mainFrm.submit();
 	}
</script>
</head>
<body>
	<form name="mainFrm" method="post" action="editor.rptdo">
		<input type="hidden" name="id" value="<%=sid%>">
		<input type="hidden" name="date" value="<%=sdate%>">
		<textarea id="memo" name="memo"
			style="width: 700px; height: 300px; visibility: hidden;">
		<%=memo %>
			</textarea>
		<br />
		<table>
			<tr>
				<td width="100%" align="right"><input
					type="button" value="确 定" name="Ok" onclick="synContent();" class="btn3"> <input type="button"
					value="取 消" name="Cancel" onclick="window.close();" class="btn3">
				</td>
			</tr>
		</table>
	</form>
<script type="text/javascript">
        domHover(".btn3", "btn3_hover");
</script>
</body>
</html>
