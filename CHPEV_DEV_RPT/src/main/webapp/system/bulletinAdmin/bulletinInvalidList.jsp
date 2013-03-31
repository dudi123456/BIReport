<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>

<%@ page import="com.ailk.bi.base.util.*"%>

<%@ page import="com.ailk.bi.report.struct.*"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>深度运营支撑平台</title>
<%
String rootPath = request.getContextPath();
	//列表数据
String[][] list = (String[][])session.getAttribute("VIEW_TREE_LIST");
	//查询条件
	if(list == null){
	list = new String[0][0];
}

	//查询条件
	ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
	if(qryStruct == null){
		qryStruct = new ReportQryStruct();
	}


%>

<script type='text/javascript' src='<%=rootPath%>/js/date.js'> </script>
<script type="text/javascript" src="<%=rootPath%>/js/prototype.lite.js"></script>

<script  language=javascript> 
function  mm(o) 
{ 
     var  a  =  document.getElementsByName("chxSong"); 
     for  (var  i=0;  i<a.length;  i++){ 
         a[i].checked  =  o.checked; 
     } 
      
} 

function _delete(id){
 if(confirm("您确定要删除吗？此操作不可恢复!")){
 	
 	var strUrl = "bulletinAdmin.rptdo?opt_type=delBulletin&id=" + id;
	//alert(strUrl);
 	document.form2.action=strUrl;
	document.form2.submit();
}
}

function _deleteAllSel(){
 if(confirm("您确定要删除选中项吗？此操作不可恢复!")){
 	
 	var strUrl = "bulletinAdmin.rptdo?opt_type=delBulletinSel";
	//alert(strUrl);
 	document.form2.action=strUrl;
	document.form2.submit();
}
}

</script>

</head>

<body>
<table style="margin:auto 3px;" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="30"  class="bb05"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="12" height="30" class="bb03"><img src="<%=rootPath%>/biimages/kong.gif" width="12" height="30" /></td>
        <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="46%" valign="middle"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="5%"><div align="center"><img src="<%=rootPath%>/biimages/tb2.gif" width="16" height="16" /></div></td>
                <td width="95%" class="STYLE1"><span class="STYLE03">你当前的位置</span>：[系统管理]-[公告管理]-[过期公告维护]</td>
              </tr>
            </table></td>
            <form name="form2" action="" method="post">
						<!--显示script部分-->
<%=WebPageTool.pageScript("form1","bulletinList.screen")%>
<% 
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list.length, 30 );
String init = request.getParameter("init");
if("true".equals(init)) {
	pageInfo.iCurPage=0;	
} 
%>

<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>

            <td width="54%"><table border="0" align="right" cellpadding="0" cellspacing="0">
              <tr>
                <td width="60"><table width="87%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td class="STYLE1"><div align="center">
                          <input type="checkbox" value="Check  All"  onclick="mm(this)">                       </div></td>
                      <td class="STYLE1"><div align="center">全选</div></td>
                    </tr>
                </table></td>
                <td width="52"><table width="88%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td class="STYLE1"><div align="center"><input type="image" name="button" onclick="javascript:_deleteAllSel();" src="<%=rootPath%>/biimages/11.gif"  width="14" height="14" border="0"></div></td>
                      <td class="STYLE1"><div align="center">删除</div></td>
                    </tr>
                </table></td>
              </tr>
            </table></td>
          </tr>
        </table></td>
        <td width="16" class="bb07"><img src="<%=rootPath%>/biimages/kong.gif" width="16" height="30" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="8" class="bb12">　</td>
        <td><table width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#B4D3E7" onmouseover="changeto()"  onmouseout="changeback()">
            <tr>
              <td width="30" height="22" align="center" bgcolor="#FFFFFF"  class="bbbg">　</td>
              <td height="22" align="center" bgcolor="#FFFFFF" class="bbbg">公告标题</td>
              <td height="22" align="center" bgcolor="#FFFFFF" class="bbbg">公告内容</td>
              <td height="22" align="center" bgcolor="#FFFFFF" class="bbbg">过期时间</td>
              <td align="center" bgcolor="#FFFFFF" class="bbbg">删除操作</td>
            </tr>
                                        <%if(list==null||list.length==0){ %>
  <tr class="table-white-bg">
    <td colspan="4" nowrap align="center">该条件下没有符合要求的数据</td>
  </tr>
  <%}else{ %>

  <%for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
		String[] value = list[i+pageInfo.absRowNoCurPage()];
  %>  
            <tr>
			 <td height="20" align="center" bgcolor="#FFFFFF"><input type="checkbox" name="chxSong" value="<%=value[3]%>" ></td>
              <td height="20" align="center" bgcolor="#FFFFFF"><%=value[0]%></td>
              <td height="20" align="center" bgcolor="#FFFFFF"><%=value[1]%></td>
              <td height="20" align="center" bgcolor="#FFFFFF"><%=value[2]%></td>
              <td align="center" bgcolor="#FFFFFF"><img src="<%=rootPath%>/biimages/del.gif" width="16" height="16" /><a href="javascript:;" onclick="javascript:_delete('<%=value[3]%>');">删除</a></td>
            </tr>
            
               <%} %>
  <%} %>


                    </table></td>
        <td width="8" class="bb15">　</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td height="35"  class="bb19"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="12" height="35" class="bb18"><img src="<%=rootPath%>/biimages/kong.gif" width="12" height="35" /></td>
        <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td class="STYLE4"><%=WebPageTool.pagePolit(pageInfo)%></td>
         
                </tr>
            </table></td>
          </tr>                            
        </table></td>
        <td width="16" class="bb20"><img src="<%=rootPath%>/biimages/kong.gif" width="16" height="35" /></td>
      </tr>
    </table></td>
  </tr>
  </form>
       
        </table></td>
        <td width="16" class="bb20"><img src="<%=rootPath%>/biimages/kong.gif" width="16" height="35" /></td>
      </tr>
    </table></td>
  </tr>
</table>
</body>
</html>
