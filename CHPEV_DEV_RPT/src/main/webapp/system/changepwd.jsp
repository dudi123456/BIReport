<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.base.util.CommTool"%>
<%@ page import="com.ailk.bi.base.util.WebConstKeys"%>
<%@ page import="com.ailk.bi.base.table.InfoOperTable"%>
<%@ page import="com.ailk.bi.system.common.*"%>

<%
	String rootPath = request.getContextPath();
	InfoOperTable loginUser = (InfoOperTable) session
			.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
%>
<link rel="stylesheet" href="<%=rootPath%>/css/ilayout.css"
	type="text/css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/XmlRPC.js"></script>
<script src="<%=rootPath%>/js/jquery.min.js"></script>
<script src="<%=rootPath%>/js/jquery.bi.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
#passwordStrength {
	font-size:0;
	height: 6px;
	display: block;
	float: left;
}

.strength0 {
	font-size:0;
	height: 6px;
	width: 120px;
	background: #cccccc;
}

.strength1 {
	font-size:0;
	height: 6px;
	width: 24px;
	background: #ff0000;
}

.strength2 {
	font-size:0;
	height: 6px;
	width: 48px;
	background: #ff5f5f;
}

.strength3 {
	font-size:0;
	height: 6px;
	width: 72px;
	background: #56e500;
}

.strength4 {
	font-size:0;
	height: 6px;
	background: #4dcd00;
	width: 96px;
}

.strength5 {
	font-size:0;
	height: 6px;
	background: #399800;
	width: 120px;
}
</style>
<script languange="javascript">
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
	//生成工具条
	function AJAXPWD(pwd) {
		if (pwd == null || pwd == "") {
			alert("没有 输入密码!");
			return;
		}

		var bar = baseXmlSubmit.callAction("../system/SystemAjax.jsp?user_id=<%=loginUser.user_id%>&pwd="+ pwd);
				bar = bar.replace(/^\s+|\n+$/g, '');
		//
		if (bar != null && bar != "") {
			alert(bar);
			document.addmenu.password.value = "";
			document.addmenu.password.focus();
			return;
		}

	}

	function passwordStrength(password) {
		var desc = new Array();
		desc[0] = "非常弱";
		desc[1] = "弱";
		desc[2] = "一般";
		desc[3] = "中等";
		desc[4] = "强";
		desc[5] = "非常强";

		var score = 0;

		//if password bigger than 6 give 1 point
		if (password.length >= 6)
			score++;

		//if password has both lower and uppercase characters give 1 point
		if ((password.match(/[a-z]/)) && (password.match(/[A-Z]/)))
			score++;

		//if password has at least one number give 1 point
		if (password.match(/\d+/))
			score++;

		//if password has at least one special caracther give 1 point
		if (password.match(/.[!,@,#,$,%,^,&,*,?,_,~,-,(,)]/))
			score++;

		//if password bigger than 12 give another 1 point
		if (password.length > 12)
			score++;

		document.getElementById("passwordDescription").innerHTML = desc[score];
		document.getElementById("passwordStrength").className = "strength"
				+ score;
	}

	function submit1() {
		var frm = document.addmenu;
		if (frm.elements("oldpwd").value == "") {
			alert("对不起，请输入原密码！");
			frm.elements("oldpwd").focus();
			return false;
		}
		if (frm.elements("password").value == "") {
			alert("请输入 '新密码'！");
			frm.elements("password").focus();
			return false;
		}
		if (frm.elements("repassword").value == "") {
			alert("请输入 '新密码确认'！");
			frm.elements("repassword").focus();
			return false;
		}

		//校验操作员密码
		if(document.addmenu.password.value!=null ||document.addmenu.password.value!="")
		{

			 //密码强度
			 var pwdLevel = document.addmenu.password.value;//document.getElementById("passwordStrength").value;//className;
		   	 if(pwdLevel.length<8){
		    			alert("密码不符合强度要求，密码长度不低于8位。");
		    			document.addmenu.password.focus();
		    			return;
		      }
		   	 else{
				 var sc = 0;
				 if ((pwdLevel.match(/[a-z]/)!=null) || ( pwdLevel.match(/[A-Z]/)!=null ) ) sc++;
				 if (pwdLevel.match(/.[!,@,#,$,%,^,&,*,?,_,~,-,(,)]/) != null)	sc++;
				 if (pwdLevel.match(/.[0-9]/) != null)	sc++;
				 if(sc < 2)
					 {
					 	alert("密码不符合强度要求，需要由字母、数字及下划线、@等特殊字符至少两种组成。");
					 	document.addmenu.password.focus();
		    			return;
					 }
	     	 }
 		}

		//
		if (frm.elements("password").value != frm.elements("repassword").value) {
			alert("'新密码'与'确认密码'不符");
			return false;
		}

		frm.submit();

	}

</script>
<!DOCTYPE html>
<html>
<TITLE>修改密码</TITLE>

<BODY>

	<form name="addmenu" method="POST"
		action="<%=rootPath%>/login/login.rptdo?opType=changpwd">
		<div class="alert_box_bg" id="alert_box_bg">
			<iframe></iframe>
		</div>
		<div class="alert_box_container" id="alert_box_container">
			<div class="alert_box flowbox2">
				<div class="layerbox dialog1">
					<div class="co_box">
						<table width="100%">
							<tr></tr>
							<tr>
								<td align="right" width="80px" style="font-size:12px;">原密码：</td>
								<td>
								<input
										type="password" name="oldpwd" maxlength="40" id="oldpwd"
										class="txtinput">
								</td>
							</tr>
							<tr>
								<td align="right" width="80px" style="font-size:12px;">新密码：</td>
								<td><input type="password"
									name="password" maxlength="40" id="password"
									onkeyup="passwordStrength(this.value)" onBlur="AJAXPWD(this.value)"
									class="txtinput">
								</td>
							</tr>
							<tr>
								<td align="right" width="80px" style="font-size:12px;">密码强度：</td>
								<td><div id="passwordDescription" style="font-size:12px;"></div>
									<div id="passwordStrength" class="strength0"></div>
								</td>
							</tr>
							<tr>
								<td align="right" width="80px" style="font-size:12px;">确认密码：</td>
								<td><input type="password"
									name="repassword" maxlength="40" id="repassword" class="txtinput">
								</td>
							</tr>
						</table>
					</div>
					<div class="co_btn" id="fv_btn">
						<input
						type="button" value="修改密码" name="btnChange" class="btn"
						onclick="submit1()"">&nbsp;<input type="reset"
						value="重置" name="btnreset" class="btn">&nbsp;&nbsp;<input
						type="button" value="关闭" name="btnclose" class="btn"
						onclick="javascript:top.closeBlock();">
					</div
				</div>
			</div>
		</div>
	</form>
	<script type="text/javascript">
		$(document).ready(function() {
			domHover(".btn", "btn_hover");
		});
	</script>
</BODY>

</html>

