<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.ailk.bi.system.common.LSInfoFavorite"%>

<%
	String[][] rs = LSInfoFavorite.getFavorTree(request);
	String _base = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<!--
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 -->

<title></title>

<link rel="stylesheet" href="<%=_base%>/css/icontent.css"
	type="text/css">
<script src="<%=_base%>/js/jquery.min.js"></script>

<link rel="StyleSheet"  href="common/dtree.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<script type="text/javascript" src="common/dtree.js"></script>

<script type="text/javascript">
function openManager() {

//	window.open('../system/favoriteMain.jsp',"新建文件夹","width=600,height=500,top=100, left=100,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=yes,resizable=0");

	parent.document.getElementsByTagName('frame')[1].src="../system/favoriteMain.jsp";
}

</script>
</head>

<body scroll="no">
	<div id="treebox">
		<div class="childtreenode mcset" style="font-weight:bold;text-align: center;">收藏夹</div>
		<div name="childBox">
			<div class="childtreenode mcset">
				<a href="javascript:openManager();" class="icon">整理收藏夹</a>
			</div>
			<div class="mcbox">
				<a href="javascript:;" class="icon">我的收藏夹</a>
			</div>
			<!-- 此处需要显示我的列表 -->
			<div class="childnodebox childBox">
				<div id="Layer1" style="position:absolute; width:100%; height:100%; z-index:1; overflow: auto;">
					<script type="text/javascript">
						d = new dTree('d');

						d.add(0,-1,'我的收藏夹','','test','bodyFrame');
						<%for (int i = 0; rs != null && i < rs.length; i++) {
										out.print("d.add("
												+ rs[i][0]
												+ ","
												+ rs[i][1]
												+ ",'"
												+ rs[i][2]
												+ "','"
												+ (rs[i][3] == null || "".equals(rs[i][3].trim())
														? ""
														: ".." + rs[i][3]) + "','test','bodyFrame');\n");

						}%>
					d.add(-100, 0, '即席查询清单', '', '即席查询清单', 'bodyFrame');
					d.add(-101, -100, '清单运行情况','../adhoc/AdhocInfoExport.rptdo?oper_type=qryTaskSts','清单运行情况','bodyFrame');
					document.write(d);
					</script>
				</div>
			</div>
		</div>
		<div id="manageFav" style="display: none;">
			<iframe id="helps" src="../system/favoriteMain.jsp" width="711px" height="310px"
				frameborder="0" scrolling="no"> </iframe>
	    </div>


	</div>
	<div id="treetoggle">
		<a href="javascript:;" onclick="toggleTree()">
		<img width="11px"height="47px" src="<%=_base%>/images/common/com_1.png" />
		</a>
	</div>
	<script type="text/javascript">
        function layout1(){
            var clientHeight = document.documentElement.clientHeight;
            var leftHeight=clientHeight+70;
            $("#treebox,#treetoggle").css("height", leftHeight + "px");
            $("#treetoggle a").css("top", leftHeight / 2 - 50 + "px");
            var treenodnum=$("#treebox").children(".treenode").length;
            if (treenodnum > 1){
                $("div[name='childBox']").css("height", clientHeight - treenodnum*30 + "px");
            }

        }

        layout1();
        $(".node1 a").click(
        function()
        {
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
</html>
