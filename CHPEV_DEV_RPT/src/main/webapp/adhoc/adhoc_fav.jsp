<%@page import="com.ailk.bi.base.util.WebConstKeys"%>
<%@page import="com.ailk.bi.base.table.InfoOperTable"%>
<%@page import="com.ailk.bi.system.facade.impl.CommonFacade"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.common.app.StringB"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>经营分析系统</title>
		<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
		<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
		<script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jQuery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jQuery.blockUI.js"></script>

		<script language=javascript>
	    domHover(".btn", "btn3_hover");
        function switchClass2(theObj)
		{
			if(theObj.className.indexOf("_hover")<0)
			{
				theObj.className=theObj.className+"_hover";
			}
			else
			{
				theObj.className=theObj.className.replace("_hover","");
			}
		}
		</script>
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/css/other/css.css"
			type="text/css">
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/css/other/kw.css" type="text/css">
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/css/icontent.css" type="text/css">
	</head>
	<%
		String type = StringB.NulltoBlank(request.getParameter("type"));
	%>
	<body bgcolor="white" >
		<div id="maincontent">

			<div class="alert_box_container" id="alert_box_container"
				style="top: 5%; height: 100%;">
				<!-- div class="alert_box" style="width:443px;height:297px" -->
				<div class="alert_box flowbox3">
					<div class="layerbox">
						<form name="Form1">

							<input type="hidden" name="favorite_id">
							<div class="fv_box">
								<span class="fvinput"><input value="我的收藏夹1"
										class="txtinput" id="favorite" name="favorite" />
								</span>

								<div class="fvtree" style="overflow:hidden">
									<div class="fvtreebox" >
										<!-- <p class="icon">选择文件夹</p> -->
										<iframe id="favor_frame" name="favor_frame" width="100%"
											height="100%" frameborder="0" marginwidth="0"
											marginheight="5" scrolling="auto"
											src="<%=request.getContextPath()%>/system/favorTreeAdd.jsp">
										</iframe>
									</div>
								</div>

							</div>

							<div class="co_btn" id="co_btn"
								style="top: 240px; left: 0px; width: 711px;">
								<input type="button" name="save" class="btn"
									onMouseOver="switchClass2(this)"
									onMouseOut="switchClass2(this)" value="保 存"
									onclick="doSave('<%=type%>')">
								<!--
								<input type="button" name="new" class="btn"
									onMouseOver="switchClass2(this)"
									onMouseOut="switchClass2(this)" value="新 建" onclick="openAdd()">
								 -->
								<input type="button" name="close" class="btn"
									onMouseOver="switchClass2(this)"
									onMouseOut="switchClass2(this)" value="取 消"
									onclick="closeAdd()">
							</div>
							<div id="newFav" style="display: none;">
								<iframe id="helps" src="<%=request.getContextPath()%>/system/folderAdd.jsp" width="500px" height="200px"
									frameborder="0" scrolling="no"> </iframe>
						    </div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>

<script>
function setFavor(id,name) {
	Form1.favorite_id.value=id;
}
function doSave(type){
	if(Form1.favorite_id.value=="") {
		alert("请选择文件夹！");
		return false;
	}
	window.parent.tableQryForm.fav_name.value = Form1.favorite.value;  //self.opener
	window.parent.tableQryForm.myFavor_id.value = Form1.favorite_id.value;
	window.parent.tableQryForm.addFavor.onclick();
	window.parent.close();
}
//function openAdd() {
//	window.open('../system/folderAdd.jsp?gotoUrl=adhocFav.screen',"newFloder","width=500,height=200,top=200, left=400,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=yes,resizable=0");
//}
function closeAdd() {
	parent.closeBlock();
}
function openAdd()
{
	 $.blockUI(
	 {
		 message:$('#newFav'),
         css: {
	             top:  ($(window).height() - 310)/2 + 'px',
	             left: ($(window).width() - 711) /2 + 'px',
	             height: '300px',
	        	 width : '700px',
	        	 border : '0',
	        	 padding:	0,
	     		 margin:	0,
	        	 cursor : 'default'
        		}
    	});
}
function closeBlock()
{
	$.unblockUI();
}
</script>