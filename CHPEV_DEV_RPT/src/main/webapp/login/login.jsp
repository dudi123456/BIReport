<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>
<html>
<head>

<%@ include file="/base/commonMeta.jsp"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>中国联通经营分析系统</title>
<link rel="stylesheet" href="<%=context%>/css/ilayout.css"
	type="text/css" />

<script src="<%=context%>/js/jquery.min.js"></script>

<script src="<%=context%>/js/jquery.bi.js"></script>

</head>
<body onload="document.form1.oper_no.focus();">
	<form METHOD="POST" name="form1"
		action="<%=context%>/login/login.rptdo">
		<INPUT TYPE="hidden" id="opType" name="opType" value="LOGIN" /> <INPUT
			TYPE="hidden" id="system_id" name="system_id" value="1" />
		<%
			String strMsg = (String) request.getAttribute("COMMON_MSG");
			if (strMsg == null) {
				strMsg = "";
			}
		%>
		<div id="login_box">
			<div class="login_wrap">
				<div class="login_tb">
					<form>
						<table>
							<tr>
								<td width="100px" align="right">用户名：</td>
								<td><input type="text" name="oper_no" tabindex="1"
									onkeyup="tabj()" class="txtinput" />
								</td>
								<td>
								</td>
							</tr>
							<tr>
								<td align="right">密&nbsp;&nbsp;码：</td>
								<td><input type="password" name="txtPassword"
									type="password" tabindex="2" onkeyup="tabAuth()"
									class="txtinput" />
								</td>
								<td>
								</td>
							</tr>
							<tr>
								<td align="right">验证码：</td>
								<td><input type="text" id="authCode" name="authCode"
									tabindex="3" onkeydown="keyProcess()" class="txtinput round" />
									<a href="javascript:refreshImages();" title="点击后刷新" class="autcode"><img
										id="imgVerfiyCode" border=0 src="<%=context%>/images" valign="absmiddle"></a>
								</td>
								<td></td>
							</tr>
							<tr>
								<td></td>
								<td style="height: 60px"><input type="submit" class="btn"
									id="btn_reset" value="登 录" onclick="javascript:onSubmit();" />
									<input type="reset" class="btn" id="btn_submit" value="重 填" />
								</td>
								<td><a href="javascript:;"
									onclick="javascript:forgetpwd();" class="forgetpw">忘记密码</a>
								</td>
							</tr>
							<tr>
								<td></td>
								<td colspan="2">
								<% if(!"".equals(strMsg)){%>
								<span class="icon errortip"><%=strMsg%></span>
								<%} %>
								</td>
							</tr>

						</table>
					</form>
				</div>
			</div>
		</div>


</body>
</html>
<script type="text/javascript">
		$(document).ready(function() {
			domHover("#btn_reset", "btn_hover");
			domHover("#btn_submit", "btn_hover");
		});
</script>
<script language="JavaScript" type="text/JavaScript">function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}
//-->

String.prototype.trim = function()
{
return this.replace(/(^\s*)|(\s*$)/g, "");
}

	function forgetpwd(){

    var h = "400";
	var w = "600";
	var top=(screen.availHeight-h)/2;
	var left=(screen.availWidth-w)/2;
	  var optstr = "height=" + h +",width=" + w + ",left=" + left + ",top=" + top + ",status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
	  var strUrl = "<%=context%>/login/login.rptdo?opType=forgetpwd";
		newsWin = window.open(strUrl, "editRptHead", optstr);
		if (newsWin != null) {
			newsWin.focus();
		}

	}

	function onSubmit() {

		var loginName = document.all.oper_no.value;
		var objLP = document.getElementById("txtPassword");
		if (loginName.trim().length == 0) {
			alert("用户名不能为空，请重新输入！");
			document.all.oper_no.value = "";
			objLP.value = "";
			document.all.oper_no.focus();
			return false;
		}

		if (objLP.value.trim().length == 0) {
			alert("密码不能为空，请重新输入！");
			objLP.value = "";
			objLP.focus();
			return false;
		}

		var authCode = document.all.authCode.value.trim();
		if (authCode.length == 0) {
			alert("验证码不能为空！");
			document.all.authCode.value = "";
			document.all.authCode.focus();
			return false;
		}

		var str_name;
		var oHidden1 = window.document.createElement("INPUT");
		oHidden1.type = "hidden";
		str_name = "screenXY";
		oHidden1.name = str_name;
		oHidden1.value = "" + window.screen.availWidth + "_"
				+ window.screen.availHeight + "";
		window.document.form1.appendChild(oHidden1);
		return true;
	}

	function tabj() {
		if (event.keyCode == 13)
			form1.txtPassword.focus();
	}

	function tabAuth() {
		if (event.keyCode == 13)
			form1.authCode.focus();
	}

	function keyProcess() {
		//alert(event.keyCode);
		if (event.keyCode == 13)
			onSubmit();
	}

	//对象
	function BaseXmlSubmit() {
	}
	//动作
	BaseXmlSubmit.prototype.callAction = function f_callAction(url) {
		var dom = null;
		try {
			var rpc = new XmlRPC(url);
			rpc.send();
			dom = rpc.getText();
		} catch (e) {
			alert(e.message);
		}
		return dom;
	}

	//实例
	var baseXmlSubmit = new BaseXmlSubmit();

	function genUserSystem(obj) {
		var user_id = obj.value;
		if (user_id == null || user_id == "") {
			//alert("没有用户ID!");
			return;
		}

		var bar = baseXmlSubmit.callAction("../login/login_ajax.jsp?user_id="
				+ user_id);
		bar = bar.replace(/^\s+|\n+$/g, '');
		//
		document.getElementById("bar").innerHTML = bar;
	}

	function refreshImages(){
		$("#imgVerfiyCode").attr("src","<%=context%>/images?time="+new Date());
	}
</script>