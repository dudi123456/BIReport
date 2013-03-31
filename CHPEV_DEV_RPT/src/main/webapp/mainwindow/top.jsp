<%@ include file="/base/controlHead.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.system.common.CommonUtil"%>
<%@page import="com.ailk.bi.base.util.WebConstKeys"%>
<%@page import="com.ailk.bi.common.dbtools.DAOFactory"%>
<%@ page import="com.ailk.bi.base.table.*"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%
	List menuList = (ArrayList) session
			.getAttribute(WebConstKeys.LOGIN_USER_MENU_LIST);
	String[][] bulletin = CommonUtil.getBulletin(request.getSession());
	InfoOperTable loginUser = CommonFacade.getLoginUser(session);
%>
<link href="<%=_base%>/css/ilayout.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=_base%>/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=_base%>/js/thickbox.js"></script>
<script type="text/javascript" src="<%=_base%>/js/MsnPup.js"></script>


<script type="text/javascript">
    function logon_out()
    {
        if (!confirm("您确认要退出系统么？"))
            return;
        window.location.href = "../exitsys.jsp?TYPE=1";
    }

    function change_pwd()
    {
    	parent.showPwd();
    }

    function openHelp()
    {
    	parent.showHelp();
    }

    function goForum() {
        var forumUrl = "<%=request.getContextPath()%>/bibbs/index";
        window.open(forumUrl);
    }

    function my_bulletinShow(){
    	parent.showBulletin();
    }

    <%if (bulletin != null && bulletin.length > 0) {%>
    var info = "";
    var id = "";
    var c = 1;
    var hg = 150;
    //仅取两条
    <%for (int i = 0; bulletin != null && i < bulletin.length && i<2; i++) {%>
    info += '<%=bulletin[i][1]%>$';
    id += '<%=bulletin[i][0]%>$';
    ++c;
    <%}%>
    if (c > 5) {
        hg = 25 * c;
    }
    var MSG = new MessShow(id.substring(0, id.length - 1), 250, hg, "公告", info.substring(0, info.length - 1), '', '');
    MSG.show();
    <%}%>
</script>
<body>
	<%
		String sysId = (String) session.getAttribute("system_id");
		if (sysId == null) {
			sysId = "3";
		}
	%>

	<div id="head">
		<div class="head_box">
			<div class="mt_logo float-l">
				<a href="javascript:;" title="中国联通经营分析系统"> <img
					src="../images/common/logo.png" alt="中国联通经营分析系统" />
				</a>
			</div>
			<div class="mt_info">
				<div class="mywork">
					<span class="icon w1"><a href="javascript:;"
						onclick="goForum();">经营论坛</a>
					</span> <span class="icon w2"><a href="javascript:;"
						onclick="my_bulletinShow();">系统公告</a>
					</span><span class="icon w3"><a href="javascript:;"
						onclick="change_pwd();">修改密码</a>
					</span> <span class="icon w4"><a href="javascript:;"
						onclick="openHelp();">帮助文档</a>
					</span>
				</div>
				<div class="myinfo">
					<%=loginUser.user_name%>，你好！ <a href="javascript:;" class="blue"
						onclick="logon_out();">[ 退出 ]</a>
				</div>
			</div>
		</div>
	</div>
	<div class="head_tag">
		<ul id="headtabbox">
			<script type="text/javascript">
                          //定义Taber标签数组
                          <%=DAOFactory.getCommonFac().getFirstUserHeadMenu(session)%>
                      </script>
			<script type="text/javascript">
                          buildTaberArea('headtabbox', taberArray[1][0], taberArray);
                      </script>
			<!--/taber标签切换区结束-->
		</ul>
		<a href="javascript:;" class="goup" onclick="toggleHead(this)"> <img
			src="<%=_base%>/images/common/com_2.gif" />
		</a>
	</div>
</body>
</html>
<script type="text/javascript">
		function switchTab(i){
			var splits=taberArray[i][2].split("\"");
			if(null!=splits && splits.length>=2){
				if(i==10){
					window.open(splits[1]);
				}else{
					parent.mainFrame.location.href=splits[1];
				}
			}
		}
		//默认首页显示
		switchTab(1);

		$("#headtabbox a").click(function(){
            $("#headtabbox li").removeClass("active");
            $(this).parent().removeClass("hover").addClass("active");
        });

        $("#headtabbox li").hover(function(){
            if (!$(this).hasClass("active")){
                $(this).addClass("hover");
            }
        }, function(){
            $(this).removeClass("hover");
        });

        function toggleHead(obj){
            $('#head').toggle();
            top_layout();
            if ($("#head").css("display") == "none")
            {
                $(obj).children().attr("src", "<%=_base%>/images/common/com_6.gif");

            }
            else
            {
                $(obj).children().attr("src", "<%=_base%>/images/common/com_2.gif");
			//还得设置
		}
	}

	function top_layout() {//让中间层的高度随屏幕变化而变化
		var clientHeight = window.parent.document.documentElement.clientHeight; //屏幕高度
		var headHeight = 107; //页头高度
		if ($("#head").css("display") == "none") {
			headHeight = 37;
		}
		var footHeight = 28; //页脚高度
		var contentHeight = clientHeight - headHeight - footHeight;
		$('#header', window.parent.document).css("height", headHeight + "px");
		$("#content", window.parent.document).css("height",
				contentHeight + "px");
		//这里还得设置一下当前的菜单
		//相当于每个未打开的菜单加上37，或者减37
		//先找到
		var left = window.top.frames["mainFrame"].frames["leftFrame"];
		if (left) {
			left.layout1();
		}
	}
</script>
