<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<HTML  XMLNS:IE>
<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/tabpane.js"></script>
</head>
<body>
<TABLE cellspacing="0" cellpadding="0" WIDTH="100%" HEIGHT="100%">
  <TR><TD width="100%">
    <iframe id="roleTree" name="roleTree"  width="100%" height="100%" frameborder="0"  marginwidth="0" marginheight="0" scrolling="auto" src="roles.jsp?treeType=Type_Level" ></iframe>
  </TD></TR>
  <TR bgColor="#deebff" width="100%" height="25"><TD valign="top">
	<div class="tab-pane" id="tabPane1">
	<script type="text/javascript">
		tp1 = new WebFXTabPane( document.getElementById( "tabPane1" ) );
	</script><!--
	
	  <div class="tab-page" id="Type_Level"><h2 class="tab">按类型</h2>
		<script type="text/javascript">
			var label1 = tp1.addTabPage( document.getElementById( "Type_Level" ));
			label1.aElement.onclick = function (){parent._treeRedirect("Type_Level"); return false; };
			label1.tab.onclick = function () { label1.select();parent._treeRedirect("Type_Level"); return false;};
			label1.select();
		</script>
	  </div>
	 
	
	--></div>

  </TD></TR></TABLE>
</body>
</html>