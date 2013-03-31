<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>

<%
String rootPath = request.getContextPath();
%>
<%@ include file="/base/commonMeta.jsp"%>
<link rel="stylesheet" href="<%=context%>/css/ilayout.css"
	type="text/css" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=context%>/js/jquery.min.js"></script>
<script src="<%=context%>/js/jquery.bi.js"></script>
<script languange="javascript">

	function submit1() {
		var frm = document.addmenu;
		if(frm.elements("oper_no").value==""){
    			alert("对不起，请输入账号！");
				document.all.oper_no.focus();
    			return false;
    		}
	   if(frm.elements("question").value==""){
    			alert("对不起，请输入问题！");
				document.all.question.focus();
    			return false;
    		}
		 if(frm.elements("answer").value==""){
    			alert("对不起，请输入答案！");
				document.all.answer.focus();
    			return false;
    		}

		 if(frm.elements("authCode").value==""){
    			alert("对不起，请输入验证码！");
				document.all.authCode.focus();
    			return false;
    		}


	}

</script>

<html>
<TITLE>获取密码</TITLE>
<%
String strMsg = (String)request.getAttribute("COMMON_MSG");
if (strMsg==null){
	strMsg = "";
}

%>
<BODY  onLoad="document.addmenu.oper_no.focus();" style="background-color:#fff" >
<form name="addmenu" method="POST" action="login.rptdo?opType=getpwd" onSubmit="return submit1();">
  <table  align="center" border="0" class="form" width="70%" id="forgetPw">
    <tr class="add_title" >
      <td height="26"  colspan="2" style="padding-left:50px">【获取密码】<font color="red"><%=strMsg%></font></td>
    </tr>

	   <TR bgColor="#ffffff">
      <td width="145" height="30" align="right"  class="left">账号：</td>
      <td align="left"　width="518" class="right"> <input type="text" name="oper_no" maxlength="40"  id="oper_no" tabindex="1" class="txtinput" >
     </td>
    </tr>

   <TR bgColor="#ffffff">
      <td width="145" height="30" align="right"  class="left">问题：</td>
      <td align="left"　width="518" class="right"> <input type="text" name="question" maxlength="40"  id="question" tabindex="2" class="txtinput" >
     </td>
    </tr>
  <TR bgColor="#ffffff">
      <td  width="145" height="30" align="right" class="left">答案：</td>
      <td align="left" class="right"> <input type="text" name="answer" maxlength="40"  id="answer" tabindex="3"  class="txtinput" >      </td>
    </tr>

      <TR bgColor="#ffffff">
      <td  width="145" height="30" align="right" class="left">验证码：</td>
      <td align="left" class="right"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="30%"><input type="text" id="authCode" name="authCode" size="10" maxsize='50' tabindex="4" class="txtinput"></td>
                  <td width="70%">&nbsp;<a href=""><img border=0 src="<%=rootPath%>/pwdImages" align="absmiddle"></a></td>
                </tr>
            </table></td>
    </tr>

    <tr class="add_bottom">
      <td  align="center" class="right" colspan="2">
	  <input type="submit" value="获取密码" name="submit" class="btn">&nbsp;<input type="reset" value="重 置" name="btnreset" class="btn">&nbsp;&nbsp;<input type="button" value="关 闭" name="btnclose" class="btn" onclick="javascript:window.close();">		  </td>
    </tr>
  </table>
</form>
<script type="text/javascript">
		$(document).ready(function() {
			domHover(".btn", "btn_hover");
		});
	</script>
</BODY>

</html>

