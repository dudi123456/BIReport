<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/base/controlHead.jsp" %>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%@ page import="com.ailk.bi.base.table.*"%>

<%
	String context = request.getContextPath();

    InfoOperTable loginUser = CommonFacade.getLoginUser(session);
%>
    <link rel="stylesheet" href="<%=context%>/css/ilayout.css" type="text/css" />
<SCRIPT language=javascript src="<%=context%>/js/dojo.js"></SCRIPT>
<script type="text/javascript" src="<%=context%>/js/XmlRPC.js"></script>

<body>
      <div id="foot">
            <div class="foot_bg">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				    <tr>
				        <td height="20" align="center" nowrap>
				            <table width="98%" border="0" cellspacing="0" cellpadding="0">
				                <tr>
				                    <td valign="middle" height="20" width="30%">欢迎你,<%=null!=loginUser?loginUser.user_name:""%>
				                        <a href="javascript:;" class="font-bule" onclick="javascript:logon_out();">【退出】</a>
				                        <span id="Clock"></span>
				                    </td>
				                    <td valign="middle" align="center"></td>
									<td valign="middle" align="right" width="30%" id="tdUserCount">当前在线用户数</td>
				                </tr>
				            </table>
				        </td>
				    </tr>
				</table>
			</div>
	</div>

</body>
</html>
<script type="text/javascript">

//对象
function BaseXmlSubmit(){
}
//动作
BaseXmlSubmit.prototype.callAction = function f_callAction(url)
{
  var dom = null;
  try{
    var rpc = new XmlRPC(url);
    rpc.send();
    dom = rpc.getText();
  }
  catch(e){
    alert(e.message);
  }
  return dom;
}

//实例
var baseXmlSubmit =new BaseXmlSubmit();


function genUserSystemCount(){

    var bar = baseXmlSubmit.callAction("../mainwindow/bottom_ajax.jsp");
    bar=bar.replace(/^\s+|\n+$/g,'');
    //
    document.getElementById("tdUserCount").innerHTML = bar;
   window.setTimeout("genUserSystemCount();", 1000 * 60 * 5);
}



    function tick() {
        var hours, minutes, seconds, xfile;
        var intHours, intMinutes, intSeconds;
        var today, theday;
        today = new Date();
        function initArray() {
            this.length = initArray.arguments.length;
            for (var i = 0; i < this.length; i++)
                this[i + 1] = initArray.arguments[i]
        }
        var d = new initArray(
                "星期日",
                "星期一",
                "星期二",
                "星期三",
                "星期四",
                "星期五",
                "星期六");
        theday = today.getYear() + "年" + [today.getMonth() + 1] + "月" + today.getDate() + d[today.getDay() + 1];
        intHours = today.getHours();
        intMinutes = today.getMinutes();
        intSeconds = today.getSeconds();
        if (intHours == 0) {
            hours = "12:";
            xfile = "午夜";
        } else if (intHours < 12) {
            hours = intHours + ":";
            xfile = "上午";
        } else if (intHours == 12) {
            hours = "12:";
            xfile = "正午";
        } else {
            intHours = intHours - 12
            hours = intHours + ":";
            xfile = "下午";
        }
        if (intMinutes < 10) {
            minutes = "0" + intMinutes + ":";
        } else {
            minutes = intMinutes + ":";
        }
        if (intSeconds < 10) {
            seconds = "0" + intSeconds + " ";
        } else {
            seconds = intSeconds + " ";
        }
        timeString = theday + xfile + hours + minutes + seconds;
        Clock.innerHTML = timeString;
        window.setTimeout("tick();", 100);
    }

    window.onload = genUserSystemCount;

    var strRootPathTemp = "<%=_base%>";
    window.onbeforeunload = function() {
        var n = window.event.screenX - window.screenLeft;
        var b = n > document.documentElement.scrollWidth - 20;
        if (b && window.event.clientY < 0 || window.event.altKey) {
            var aj = new ajax();
         //   window.event.returnValue = "确定要退出系统吗？";
            aj.get(strRootPathTemp + "/login/login.rptdo?operation_type=logout", function (obj) {});
        }
    }

    function logon_out()
    {
        if (!confirm("您确认要退出系统么？"))
            return;
        window.location.href = "../exitsys.jsp?TYPE=1";
    }

	function current_online_user(){
		var h = "300";
        var w = "400";
        var top = (screen.availHeight - h) / 2;
        var left = (screen.availWidth - w) / 2;
        var optstr = "height=" + h + ",width=" + w + ",left=" + left + ",top=" + top + ",status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
        var strUrl = "./current_online_show.jsp";
        var newsWin = window.open(strUrl, "editRptHead", optstr);
        if (newsWin != null) {
            newsWin.focus();
        }

	}
</script>