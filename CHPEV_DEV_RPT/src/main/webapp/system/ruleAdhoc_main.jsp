<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.system.common.LSInfoAdhocRole"%>
<%
String role_id = request.getParameter("role_id");
String adhoc_id = request.getParameter("adhoc_id");
String info = LSInfoAdhocRole.getAdhocRole(adhoc_id,role_id);
%>
<html>

<head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统一经营分析系统</title>
</head>
<body>
<form name="ListForm"  method="post" action="adhocRole.rptdo" target="ruleAdhoc_mainFrame">
<input type="hidden" name="role_id" value="<%=role_id %>">
<input type="hidden" name="adhoc_id" value="<%=adhoc_id %>">
<input type="hidden" name="submitType" value="save">

<%=info %>

</table>
</form>
</body>

</html>
<script>
		function hiddenData(titelID,contentID){
			 if (document.getElementById(contentID).style.display == "none"){
			       document.getElementById(contentID).style.display = "block";
			       document.getElementById(titelID).src="../images/common/system/sign_show.gif";
			 }
			 else{
				 document.getElementById(contentID).style.display = "none";
				 document.getElementById(titelID).src="../images/common/system/sign_collect.gif";
			 }
		 }

		function formPost(bt){
			bt.disabled = true;
			ListForm.submit();

		 }

		function checkEvent(name, allCheckId)
		{
		    var allCk = document.getElementById(allCheckId);

		    if (allCk.checked == true)
		    	{
		    		checkAll(name);
		    	}
		    else
		    	{
		    		checkAllNo(name);
		    	}
		 }
		//全选
		function checkAll(name) {
		    var names = document.getElementsByName(name);
		    var len = names.length;
		    if (len > 0) {
		        var i = 0;
		        for (i = 0; i < len; i++)
		         names[i].checked = true;

		     }
		    seleAllNo();
		 }

		//全不选
		function checkAllNo(name) {
		    var names = document.getElementsByName(name);
		    var all = document.getElementById("all");
		    var len = names.length;
		    if (len > 0) {
		        var i = 0;
		        for (i = 0; i < len; i++)
		         names[i].checked = false;
		         all.checked = false;
		     }
		    seleAllNo();
		 }

		//标题下的全选。
		function seleAll()
		{
			var els = document.forms[0].elements;
			var allCk = document.getElementById("all");
			var i = 0;
		    if (allCk.checked == true) {
				for(i=0;i<els.length;i++){
					if(els[i].type == "checkbox"){
						els[i].checked = true;
					}
				}
		    }else {
				for(i=0;i<els.length;i++){
					if(els[i].type == "checkbox"){
						els[i].checked = false;
					}
				}
		    }

		}
		//标题下的单个判断,不选
		function seleAllNo()
		{
			var els = document.forms[0].elements;
			var allCk = document.getElementById("all");
			var flag = true;

			for(var i=1;i<els.length;i++)
			{
				if(els[i].type == "checkbox" && els[i].checked == false && els[i].name != "all")
				{
					flag = false;
				}
			}
			if(flag == true)
			{
				allCk.checked = true;
			}
			else
			{
				allCk.checked = false;
			}
		}
		//单个是否选中来判断全选。
		function checkSingle(name,allCheckId)
		{
			var names = document.getElementsByName(name);
			var allCk = document.getElementById(allCheckId);
			var all = document.getElementById("all");
		    var len = names.length;
		    var flag = true;
	        for (var i = 0; i < len; i++)
        	{
        		if(names[i].checked == false)
       			{
       				flag = false;
       			}
        	}
	        allCk.checked = flag;
	        if(allCk.checked == false)
        	{
        		all.checked = false;
        	}
	        seleAllNo();
		}
</script>