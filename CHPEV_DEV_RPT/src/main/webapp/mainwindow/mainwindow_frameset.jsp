<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String context = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>中国联通经营分析系统</title>
    <link rel="stylesheet" href="<%=context%>/css/ilayout.css" type="text/css" />

    <script src="<%=context%>/js/jquery.min.js"></script>
	<script src="<%=context%>/js/jquery.blockUI.js"></script>

    <script src="<%=context%>/js/jquery.bi.js"></script>
	<script type="text/javascript">
		window.onresize = function(){
		    layout();
		    widgetWidth();
		}
	</script>
</head>
<body>
	<div id="bulletinBorad" style="display: none;">
			<iframe id="bulletins" src="../system/bulletinAdmin.rptdo?opt_type=bshow" width="702px" height="252px"
				frameborder="0" scrolling="no"> </iframe>
	</div>
	<div id="helpBorad" style="display: none;">
			<iframe id="helps" src="help_list.jsp" width="702px" height="252px"
				frameborder="0" scrolling="no"> </iframe>
	</div>
	<div id="pwdBorad" style="display: none;">
			<iframe id="pwd" src="../system/changepwd.jsp" width="442px" height="295px"
				frameborder="0" scrolling="no"> </iframe>
	</div>
	<script type="text/javascript">
		function showBulletin(){
			 $.blockUI({
				 message:$('#bulletinBorad'),
		         css: {
		             top:  ($(window).height() - 252)/2 + 'px',
		             left: ($(window).width() - 702) /2 + 'px',
		             height: '252px',
		        	 width : '702px',
		        	 border : '0',
		        	 padding:	0,
		     		 margin:	0,
		        	 cursor : 'default'
		         }
		     });
		}
		function closeBlock(){
			$.unblockUI();
		}
		function showHelp(){
			 $.blockUI({
				 message:$('#helpBorad'),
		         css: {
		             top:  ($(window).height() - 252)/2 + 'px',
		             left: ($(window).width() - 702) /2 + 'px',
		             height: '252px',
		        	 width : '702px',
		        	 border : '0',
		        	 padding:	0,
		     		 margin:	0,
		        	 cursor : 'default'
		         }
		     });
		}
		function showPwd(){
			 $.blockUI({
				 message:$('#pwdBorad'),
		         css: {
		             top:  ($(window).height() - 295)/2 + 'px',
		             left: ($(window).width() - 442) /2 + 'px',
		             height: '295px',
		        	 width : '442px',
		        	 border : '0',
		        	 padding:	0,
		     		 margin:	0,
		        	 cursor : 'default'
		         }
		     });
		}
	</script>
    <div id="mainbox">
        <div id="header" style="height:107px;">
            <iframe name="topFrame" scrolling="no" frameborder="0" marginwidth="0" marginheight="0" src="<%=context%>/mainwindow/top.jsp" style="width:100%;height:100%;"></iframe>
        </div>
        <div id="content">
            <iframe id="mainFrame" name="mainFrame" scrolling="auto" class="content_iframe" frameborder="no" marginwidth="0" marginheight="0" src="<%=context%>/mainwindow/panel_frameset.jsp" style="width:100%;height:100%;"></iframe>
            <div id="scriptbox">

                <script>
                	layout();
                </script>

            </div>
        </div>
        <div id="foot">
        	<iframe name="bottomFrame" scrolling="no" frameborder="no" marginwidth="0" marginheight="0" src="<%=context%>/mainwindow/bottom.jsp" style="width:100%;height:100%;"></iframe>
        </div>
    </div>
</body>
</html>





