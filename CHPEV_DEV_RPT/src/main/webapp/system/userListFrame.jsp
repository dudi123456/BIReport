<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<HTML  XMLNS:IE>
<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/tabpane.js"></script>

<TABLE cellspacing="0" cellpadding="0" WIDTH="100%" HEIGHT="100%">
  <TR><TD width="100%">
    <iframe id="userTree" name="userTree"  width="100%" height="100%" frameborder="0"  marginwidth="0" marginheight="0" scrolling="auto" src="usertree.jsp?treeType=Type_DeptUser" ></iframe>
  </TD></TR>
  <TR bgColor="#deebff" width="100%" height="20">
  <TD valign="top">
	<div class="tab-pane" id="tabPane1" width="100%">
	
	<script type="text/javascript">
	tp1 = new WebFXTabPane( document.getElementById( "tabPane1" ) );
	</script><!--
	  <div class="tab-page" id="Type_DeptUser" width="100%"><h2 class="tab">按部门</h2>
		<script type="text/javascript">
			var label1 = tp1.addTabPage( document.getElementById( "Type_DeptUser" ));
			label1.aElement.onclick = function (){parent._treeRedirect("Type_DeptUser"); return false; };
			label1.tab.onclick = function () { label1.select();parent._treeRedirect("Type_DeptUser"); return false;};
			label1.select();
		</script>
	  </div>
	  
	 
	--></div>

  </TD>
  </TR>
  </TABLE>