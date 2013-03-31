<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%
	Log log = LogFactory.getLog(ReportHeadUtil.class);
	String context = request.getContextPath();
	String rpthead = context + "/report/rpthead";
	String report_id = CommTool.getParameterGB(request, "report_id");
	if (null == report_id || "".equals(report_id)) {
		log.error("没有提供表报标识");
		return;
	}
	String head = "";
	Object tmpObj = session
			.getAttribute(WebKeys.ATTR_REPORT_HEAD_CONTENT);
	if (null != tmpObj) {
		head = (String) tmpObj;
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="com.ailk.bi.report.util.ReportHeadUtil"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>定制表头</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="robots" content="noindex, nofollow" />
<link rel="stylesheet" type="text/css" href="<%=context%>/css/other/css.css" />
<link rel="stylesheet" href="<%=context%>/css/other/tab_css.css" type="text/css" />
<script type="text/javascript" src="<%=context%>/report/rpthead/fckeditor.js"></script>
<script type="text/javascript" src="<%=context%>/js/tab_js.js"></script>
</head>
<body onUnload="opener.location.reload()">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
  	<form name="mainFrm" action="editReportHead.rptdo" method="post"
          onsubmit="return validate();"><input type="hidden"
          name="report_id" value="<%=report_id %>" /><input
          type="hidden" name="opSubmit" value="current" /><input
          type="hidden" name="delete" value="" />
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
        <td height="7"
          background="<%=context%>/images/square_line_2.gif">&nbsp;</td>
          <td height="100%" colspan="2" valign="top"> 
          <script
          type="text/javascript">
<!--
// Automatically calculates the editor base path based on the _samples directory.
// This is usefull only for these samples. A real application should use something like this:
// oFCKeditor.BasePath = '/fckeditor/' ;	// '/fckeditor/' is the default value.

var sBasePath ='<%=rpthead%>/';

var oFCKeditor = new FCKeditor( 'report_head' ) ;
oFCKeditor.Height	= 200 ;
oFCKeditor.BasePath	= sBasePath ;

// Set the custom configurations file path (in this way the original file is mantained).
oFCKeditor.Config['CustomConfigurationsPath'] = '<%=rpthead%>/config.js';

// Let's use a custom toolbar for this sample.
oFCKeditor.ToolbarSet	= 'PluginTable' ;

oFCKeditor.Value		= '<%=head%>' ;
oFCKeditor.Create();
//-->
		</script>
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
  function _submit(next){
  //  var e=document.getElementById("opSubmit");
    var e=document.mainFrm.opSubmit;
    if(e){
      e.value=next;
      document.mainFrm.submit();
    }    
  }
  function _delete(next){
   var e=document.getElementById("opSubmit");
    if(e){
      e.value=next;
      var head=document.getElementById("delete");
      head.value="true";
      document.mainFrm.submit();
    }    
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
      onclick="_submit('current');" /> <input name="bc" type="button"
      class="button2" onmouseover="switchClass(this)"
      onmouseout="switchClass(this)" value="删除表头"
      onclick="_delete('current');" /> <input name="关闭" type="button"
      class="button" onmouseover="switchClass(this)"
      onmouseout="switchClass(this)" value="关闭"
      onclick="window.close();javascript:opener.location.reload();" /></td>
  </tr>
</table>
</body>
</html>
