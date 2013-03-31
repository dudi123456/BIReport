<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.subject.admin.entity.UiSubjectCommonRptHead"%>
<%@page import="com.ailk.bi.subject.admin.SubjectCommonConst"%>

<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
//报表列表
UiSubjectCommonRptHead rptHeadObj = (UiSubjectCommonRptHead)request.getAttribute(SubjectCommonConst.CONST_SUBJECT_TABLE_DEF);

	String context = request.getContextPath();
	String rpthead = context + "/subject/admin/rpthead";
	String report_id = rptHeadObj.getTableId();
	if (report_id.length()==0){
		report_id = (String)request.getAttribute("table_id");
	}
	String row_span = "";
	if (rptHeadObj.getRowSpan()!=null){
		row_span = rptHeadObj.getRowSpan() + "";
	}

	String head = rptHeadObj.getTableHeader();
	
	String tmpStr = "<table cellspacing='1' cellpadding='1' width='100%' border='1'>";									
	head = tmpStr + head + "</table>";
	head = head.replaceAll("\"","'");
	head = head.replaceAll("\r\n","");


%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.util.ReportHeadUtil"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>定制表头</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="robots" content="noindex, nofollow" />
<link rel="stylesheet" type="text/css" href="<%=context%>/css/css.css" />
<link rel="stylesheet" href="<%=context%>/css/tab_css.css" type="text/css" />
<script type="text/javascript" src="<%=context%>/report/rpthead/fckeditor.js"></script>
<script type="text/javascript" src="<%=context%>/js/tab_js.js"></script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
  	<form name="mainFrm" action="SubjectCommonTblDef.rptdo" method="post"
          onsubmit="return validate();"><input type="hidden"
          name="table_id" value="<%=report_id %>" /><input type="hidden"
          name="opt_type" value="" />
    <td valign="top" class="squareB">
    <table width="100%" border="0" cellpadding="0"
      cellspacing="0">
        <tr>
        <td width="5" height="7"
          background="<%=context%>/images/square_line_2.gif"></td>
        <td width="100%" colspan="2"></td>
        <td><img
          src="<%=context%>/images/tab/field_upline_right.gif" width="5"
          height="7" /></td>
      </tr>
	    <tr>
        <td colspan=3>合并行(请输入数字):<input type="text" name="row_span" value="<%=row_span%>"></td>
       
      </tr>
      <tr>
        <td height="7"
          background="<%=context%>/images/square_line_2.gif">&nbsp;</td>
          <td height="100%" colspan="2" valign="top"> <input type="hidden" name="content" value="<%=head%>">
			<iframe src='<%=context%>/subject/admin/rpthead/eWebEditor/ewebeditor.htm?id=content' frameborder=0 scrolling=no width='550' HEIGHT='350'></iframe>

              <table width="100%" cellpadding="0" cellspacing="3">
                <tr> 
                  <td width="2%" align="right" valign="bottom"><img src="<%=context%>/images/title_draw.gif" width="18" height="16"></td>
                  <td width="98%" height="20" valign="bottom" class="mobileblackbold">定制表头支持三种方式来画出表头：</td>
                </tr>
                <tr> 
                  <td height="2" colspan="2"><img src="<%=context%>/images/title_line.gif" width="250" height="1"></td>
                </tr>
                <tr> 
                  <td align="center"><img src="<%=context%>/images/list_order.gif" width="9" height="6"></td>
                  <td>从MS Word 粘贴、从 MS Excel 中粘贴、在本页编辑器中直接编辑</td>
                </tr>
                <tr> 
                  <td align="center"><img src="<%=context%>/images/list_order.gif" width="9" height="6"></td>
                  <td> 前两种方式粘贴到本页编辑器后，用户可以继续编辑。</td>
                </tr>
                <tr> 
                  <td height="20" valign="bottom"><img src="<%=context%>/images/title_edit.gif" width="15" height="14"></td>
                  <td valign="bottom" class="mobileblackbold">下面简要说明如何在编辑器中编辑表格：</td>
                </tr>
                <tr> 
                  <td height="2" colspan="2"><img src="<%=context%>/images/title_line.gif" width="250" height="1"></td>
                </tr>
                <tr> 
                  <td align="center"><img src="<%=context%>/images/list_order.gif" width="9" height="6"></td>
                  <td>单击编辑器工具栏的制作表格图标<img
              src="<%=rpthead%>/editor/images/create_table.jpg"
              border="0" />,出现表格的属性对话框，用户可以根据需要进行编辑 单击确定按钮</td>
                </tr>
                <tr> 
                  <td align="center"><img src="<%=context%>/images/list_order.gif" width="9" height="6"></td>
                  <td>编辑器出现表格的大致形式，用户可以进行定制</td>
                </tr>
                <tr> 
                  <td align="center"><img src="<%=context%>/images/list_order.gif" width="9" height="6"></td>
                  <td>如果需要横向合并单元格，需要选中单元格(单元格必须有内容)，</td>
                </tr>
                <tr>
                  <td align="center">&nbsp;</td>
                  <td>然后通过右键菜单选择“单元格”－“合并单元格”或直接选择工具栏的合并按钮</td>
                </tr>
                <tr> 
                  <td align="center"><img src="<%=context%>/images/list_order.gif" width="9" height="6"></td>
                  <td>如果需要纵向合并单元格，单击定位到第一格单元格,然后通过右键菜单选择“单元格”－“单元格属性”</td>
                </tr>
                <tr> 
                  <td align="center"><img src="<%=context%>/images/list_order.gif" width="9" height="6"></td>
                  <td>在弹出的对话框中输入要跨的行数，单击确定，然后删除后面的多出的列</td>
                </tr>
              </table>
              <script type="text/javascript">
<!--
  function validate(){
    //到目前为止不知道如何获得用户的输入，放到服务器去检验
    return true;
  }
  function _submit(opt){
	document.getElementById("opt_type").value=opt;  
	document.mainFrm.submit();
	//window.close();
  }
  function _delete(opt){
 if(confirm("您确定要删除吗？此操作不可恢复!")){
 	document.getElementById("opt_type").value=opt;  
	document.mainFrm.submit();
}
	
	//window.close();
  
  }
//-->
</script>
        </td>
        <td background="<%=context%>/images/square_line_3.gif">&nbsp;</td>
      </tr>
      <tr>
        <td height="5"><img
          src="<%=context%>/images/square_corner_3.gif" width="5"
          height="5" /></td>
        <td colspan="2"
          background="<%=context%>/images/square_line_4.gif"></td>
        <td><img src="<%=context%>/images/square_corner_4.gif"
          width="5" height="5" /></td>
      </tr>
    </table>	
    </td>
	</form>
  </tr>
  <tr>
    <td height="30" align="center" valign="bottom">
      <input name="bc"
      type="button" class="button" onmouseover="switchClass(this)"
      onmouseout="switchClass(this)" value="保存"
      onclick="_submit('saveHead');" /> <input name="bc" type="button"
      class="button2" onmouseover="switchClass(this)"
      onmouseout="switchClass(this)" value="删除表头"
      onclick="_delete('deleHead');" /> <input name="关闭" type="button"
      class="button" onmouseover="switchClass(this)"
      onmouseout="switchClass(this)" value="关闭"
      onclick="window.close();" /></td>
  </tr>
</table>
</body>
</html>
