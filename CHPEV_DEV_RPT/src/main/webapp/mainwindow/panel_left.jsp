<%@ include file="/base/controlHead.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="com.ailk.bi.common.dbtools.DAOFactory"%>
<%
    String menuId = request.getParameter("menuId");
%>
<link rel="stylesheet" href="<%=_base %>/css/icontent.css" type="text/css">
<script src="<%=_base %>/js/jquery.min.js"></script>
<body>
<div id="treebox">
    <SCRIPT type="text/javascript">
        <%DAOFactory.getCommonFac().getUserChildMenuDetail(session,menuId,out,_base);%>
    </script>
</div>
<div id="treetoggle">
    <a href="javascript:;" onclick="toggleTree()">
        <img width="11px" height="47px" src="<%=_base %>/images/common/com_1.png" /></a>
</div>
<script type="text/javascript">
        function layout1(){
            var clientHeight = document.documentElement.clientHeight;
            var leftHeight=clientHeight+0;
            $("#treebox,#treetoggle").css("height", leftHeight + "px");
            $("#treetoggle a").css("top", leftHeight / 2 - 50 + "px");
            var treenodnum=$("#treebox").children(".treenode").length;
            if (treenodnum > 1){
                $("div[name='childBox']").css("height", clientHeight - treenodnum*30 + "px");
            }
            $("div[name='childBox']").css("style","overflow-y:auto");

        }

        layout1();
        $(".node1 a").click(
        function()
        {
        	$(".node1_box").not($(this).parent().next(".childBox")).hide();
            $(".node1 a").not($(this)).removeClass("open");

        	$(this).parent().next(".childBox").toggle();
            $(this).toggleClass("open");
        });
        $(".treenode").click(
        function()
        {
            $("div[name='childBox']").hide();
            $(this).next("div[name='childBox']").show();
        });
        function toggleTree()
        {

            $("#treebox").toggle();
            if ($("#treebox").css("display") == "none")
            {
                parent.document.body.cols = "12,*";
            }
            else
            {
                parent.document.body.cols = "190,*";
            }
        }
    </script>
</body>
