<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<jsp:directive.page import="com.ailk.bi.adhoc.domain.UiAdhocUserListTable"/>
<jsp:directive.page import="com.ailk.bi.adhoc.service.impl.AdhocFacade"/>
<jsp:directive.page import="com.ailk.bi.adhoc.dao.impl.AdhocDao"/>
<jsp:directive.page import="com.ailk.bi.base.util.CommTool"/>
<%@ page import="com.ailk.bi.adhoc.util.*"%>
<%@ page import="java.lang.StringBuffer"%>
<%@ page import="java.util.*"%>


<jsp:directive.page import="com.ailk.bi.adhoc.domain.UiAdhocInfoDefTable"/>
<jsp:directive.page import="com.ailk.bi.base.struct.UserCtlRegionStruct"/>
<jsp:directive.page import="com.ailk.bi.base.util.WebConstKeys"/>
<jsp:directive.page import="java.util.HashMap"/>
<jsp:directive.page import="com.ailk.bi.system.facade.impl.CommonFacade"/>

<html>
<head>


    <script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
	<script language=javascript>
	    domHover(".btn3", "btn3_hover");
        
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


<title>用户清单定制</title>
<%@ include file="/base/commonHtml.jsp"%>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/dojo.js"></SCRIPT>	
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/wait.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/kw.css" type="text/css">
<link href="<%=request.getContextPath()%>/css/other/bimain.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/icontent.css" type="text/css">

<style>
.open{background-position:0 -26px;}
</style>
</head>

<%

	String adhoc = request.getParameter(AdhocConstant.ADHOC_ROOT);
	if (adhoc == null || "".equals(adhoc)) {
		adhoc = (String) session.getAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS);
		if (adhoc == null || "".equals(adhoc)) {
			adhoc = AdhocConstant.ADHOC_ROOT_DEFAULT_VALUE;
		}
	}

	//分组属性列表"a","b","c","d"
	//此列表初始化javascript数组,作为分组应用
	String groupTag = AdhocHelper.getAdhocTag(adhoc);

	//默认分组属性(树型选中的分组标记，便于切换)
	String group_tag = request.getParameter(AdhocConstant.ADHOC_TABLE_WEBKEYS);
	if (group_tag == null || "".equals(group_tag)) {
		group_tag = (String) session
		.getAttribute(AdhocConstant.ADHOC_TABLE_WEBKEYS);
		if (group_tag == null || "".equals(group_tag)) {
			group_tag = AdhocHelper.getAdhocDefaultTag(adhoc);
		}
	}
	
	//指标定制页默认选中的属性类页
	String group_msu_tag = request.getParameter(AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS);
	if (group_msu_tag == null || "".equals(group_msu_tag)) {
		group_msu_tag = (String) session
		.getAttribute(AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS);
		if (group_msu_tag == null || "".equals(group_msu_tag)) {
			group_msu_tag = AdhocHelper.getAdhocMsuDefaultTag(adhoc);
		}

	}
	


//操作员
String oper_no = CommonFacade.getLoginId(session);
//清单配置信息	
String adhoc_root = CommTool.getParameterGB(request, AdhocConstant.ADHOC_ROOT);

/*
AdhocFacade facade = new AdhocFacade(new AdhocDao());
UiAdhocInfoDefTable hocInfo  = facade.getAdhocInfo(adhoc_root);
UiAdhocUserListTable[] defineInfo = facade.getAdhocUserListDefine(hocInfo.getAdhoc_id(), oper_no);
if(defineInfo == null || defineInfo.length <=0){//没有用户自定义清单配置则采用系统默认配置
	defineInfo  = facade.getAdhocUserListDefineDefault(hocInfo.getAdhoc_id());
	if(defineInfo == null || defineInfo.length <=0){
		out.print( "当前用户没有自定义清单以及当前即席查询也没有默认自定义清单，请通知相关配置人员！");
		return;
	}
}
*/
UiAdhocUserListTable[] defineInfo = (UiAdhocUserListTable[])session.getAttribute(AdhocConstant.ADHOC_USER_LIST_DEFINE_INFO);
String[][] groupName = (String[][])request.getAttribute("LIST_GROUP_NAME");

//增加发展人部门控制
UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct)session.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
String tmpKey = "";
if(ctlStruct!=null&&ctlStruct.ctl_county_str!=null&&!"".equals(ctlStruct.ctl_county_str)){
	tmpKey=ctlStruct.ctl_county_str;
}
String tmpMap_code = "USER_DEVELOP_DEPART_ID4";
String tmpValue = "";
ServletContext servletContext = request.getSession().getServletContext();
HashMap codeMap = (HashMap)servletContext.getAttribute(WebConstKeys.ATTR_C_CODE_LIST);
		if(codeMap!=null){
			HashMap map = (HashMap)codeMap.get(tmpMap_code.trim().toUpperCase());
			if(map!=null){
				if(map.get(tmpKey) == null){
					tmpValue = "";
				}else{
					tmpValue = (String)map.get(tmpKey);
				}
				
			}
		}
if(!"".equals(tmpValue)){
	tmpValue = "(受限发展渠道查询："+tmpValue+")";
}
%>

<body class="main-body_old" onunload="closeWaitWin()">
<div id="maincontent">
<form name="tableQryForm" method="post" action="">
<table width="100%" border="0" cellpadding="5" cellspacing="1" align="center">
  <tr> 
    <td class="td-info_old"> 
    <div class="topsearch">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
            <td >请选择相应的字段&nbsp;&nbsp;
            <input id="all" type="button" onMouseOver="switchClass2(this)"  onMouseOut="switchClass2(this)" class="btn3" value="全 选" onclick="all_Click()"/>&nbsp;&nbsp;
            <input id="not" type="button" onMouseOver="switchClass2(this)"  onMouseOut="switchClass2(this)"  class="btn3" value="反 选" onclick="not_Click()" />&nbsp;&nbsp;
            <input type="button" onMouseOver="switchClass2(this)"  onMouseOut="switchClass2(this)" name="btnsearch" value="搜索" class="btn3" onclick="searchUserListDefine('<%=adhoc%>')" />
            </td>  
          </tr>
        </table>
     </div>
  </td>
  </tr>
    <tr> 
    <td height="8"></td>
  </tr>
  </table>
  
<div class="formbox">  
<%
out.print("<table width=\"98%\" border=\"0\" cellpadding=\"0\" align=\"center\">\n");
		
for (int kk=0;kk<groupName.length;kk++){
	StringBuffer sb = new StringBuffer("");
	String strShowBln = "";
	String strImg = "_.gif";

				if (kk!=0){
					strShowBln="none";
					strImg = "show.gif";
				}
	sb.append("<tr><td colspan=\"3\">\n");
				sb.append("<table width=\"100%\">\n");
				sb.append("<tr onclick=\"openMsuTree('Tbl" + kk + "')\" style=\"cursor:hand\">\n");
				sb.append("<td width=\"15\" height=\"25\"><img  src=\"../images/sh/" + strImg + "\"  id=\"iconTbl"+kk+"\"></td>\n");
				sb.append("<td  nowrap><a href=\"javascript:;\" >"+groupName[kk][0]+"</a></td>\n");
				sb.append("</tr>\n");
				sb.append("</table>\n");				
				sb.append("</td></tr>\n");
				sb.append("<tr id=\"treeTbl"+kk+"\" style=\"display:" + strShowBln + "\">\n");
				sb.append("<td colspan=\"3\" class=\"dot-area\">\n");
				sb.append("<div class=\"fd_content\">");//js-add
				sb.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\">\n");
				
				out.print(sb.toString());
				List listTmp = new ArrayList();
				for(int j=0;defineInfo!=null&&j<defineInfo.length;j++){
					if (groupName[kk][0].equals(defineInfo[j].getGroup_Name())){
						listTmp.add(defineInfo[j]);
					}
				}
				
				UiAdhocUserListTable[] defineInfoTmp = (UiAdhocUserListTable[]) listTmp
						.toArray(new UiAdhocUserListTable[listTmp.size()]);

				
				for(int j=0;defineInfoTmp!=null&&j<defineInfoTmp.length;j++){
					if(j%5 == 0){
						out.print("<tr>\n");
					}
					
					String msuField = defineInfoTmp[j].getMsu_field();
					if("Y".equals(defineInfoTmp[j].getDefault_view())){
						out.print("<td height=\"18\" title=\""+defineInfoTmp[j].getMsu_name() + "\"><input type=\"checkbox\" name=\"msuSelected\" value=\""+msuField+"\" checked>"+defineInfoTmp[j].getMsu_name()+"</td>\n");
					}else{//默认既是选中
						out.print("<td height=\"18\" title=\""+defineInfoTmp[j].getMsu_name()+"\"><input type=\"checkbox\" name=\"msuSelected\" value=\""+msuField+"\">"+defineInfoTmp[j].getMsu_name()+"</td>\n");
					}
										
					if((j%5 == 4)||(j==defineInfoTmp.length-1)){
						if((j==defineInfoTmp.length-1)&&(j%5 != 4)){
							int count = 5-((defineInfoTmp.length)%5);
							out.print(AdhocUtil.getNbspTdInnerHtml(count));
						}
						out.print("</tr>\n");
					}

				
				}
				out.print("</table>\n");
				out.print("</div>\n");
				out.print("</td>\n");
				out.print("</tr>\n");
}

%>  

</table>
<table width="98%" height="40" border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="CDD5EC">
     <tr>
       <td align="center" class="note-side">
       <table width="100%" border="0">
          <tr>
            <td align="center" width="15%" nowrap>请选择要导出的清单字段! <%=tmpValue %></td>
            <td align="left">
			<INPUT TYPE="hidden" NAME="row" value="10000">
			<input name="Submit2" type="button" class="btn3" onMouseOver="switchClass2(this)"  onMouseOut="switchClass2(this)" value="确  定" onClick="save(1)">&nbsp;&nbsp;
			<input name="btnSaveDtl" type="button" class="btn4" onMouseOver="switchClass2(this)"  onMouseOut="switchClass2(this)" value="添加收藏" onClick="AddFav()"><input type="hidden" name="addFavor" onclick="doAddFavor()"><input	type="hidden" name="fav_name" value="" />
			<input type="hidden" name="myFavor_id">
			<input	type="hidden" name="<%=AdhocConstant.ADHOC_ROOT%>" value="<%=adhoc%>" /> 
			<input	type="hidden" name="<%=AdhocConstant.ADHOC_TABLE_WEBKEYS%>" value="<%=group_tag%>" /> 
			<input	type="hidden" name="<%=AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS%>" value="<%=group_msu_tag%>" />	
			</td>
          </tr>
        </table>
        </td>
     </tr>
</table>
  
  </div>
</form>  
</div>
</body>
</html>

<script>
//搜索指标、条件、维度
function searchUserListDefine(adhocId){    
    //
    var returnValue;
	var acptsite;
   	var time = new Date();
	  var h = "650";
        var w = "600";
        var top = (screen.availHeight - h) / 2;
        var left = (screen.availWidth - w) / 2;

   returnValue=window.showModalDialog("adhocSearchInfo.rptdo?opType=openUserList&adhocId="+adhocId+"&time=" +time,"搜索","dialogWidth:" + w + "px; dialogHeight:" + h + "px; dialogLeft:" + left + "px; dialogTop:" + top + "px; status:no; directories:yes;scrollbars:yes;Resizable=no;help:no");

 //  window.open("adhocSearchInfo.rptdo?opType=openUserList&adhocId="+adhocId + "&time=" +time)
    if(returnValue == null || returnValue == "" || returnValue == "undefined"){	    
        return;
    }else{
		
		acptsite=returnValue.split(",");
		for(var i=0;i<acptsite.length;i++){
			var oCk = document.all.msuSelected;
			for(var j=0;j<oCk.length;j++){
				if (oCk[j].value==acptsite[i])
				{
					//alert(oCk[j].value);
					oCk[j].checked = true;
					break;
				}
			}
		}

	}

}


//增加收藏夹信息
function AddFav(){
	var obj = document.getElementsByName("msuSelected");

var almlev = "";
for(var i=0;i<obj.length;i++){
  if(obj[i].checked){
   almlev = almlev+"#"+obj[i].value;
  }
 }
 if (almlev.length==0)
 {
	 alert("你没有选择字段，请选择");
	 return;
 }

	window.open('adhoc_fav.jsp?type=1',"addFavor","width=711,height=310,top=250, left=250,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=yes,resizable=0");
	
}

function doAddFavor() {
	tableQryForm.target="_self";//&fav_name="+favName+"
	tableQryForm.action="../AdhocFav.rptdo?oper_type=addqrydtl";
	tableQryForm.submit();

}


function save(tag){

var obj = document.getElementsByName("msuSelected");
var almlev = "";
for(var i=0;i<obj.length;i++){
  if(obj[i].checked){
   almlev = almlev+"#"+obj[i].value;
  }
 }
 if (almlev.length==0)
 {
	 alert("你没有选择字段，请选择");
	 return;
 }


    tableQryForm.action = "AdhocUserList.rptdo?adhoc_root=<%=adhoc_root%>&oper_type=init";
    ShowWait();
    tableQryForm.submit();
}

function openMsuTree(n) 
{
	var t1 = "tree" + n;
	var t2 = "icon" + n;
	
	
	obj = document.getElementById(t1);
	sIcon = document.getElementById(t2);
	if (obj.style.display == "none") 
	{
		sIcon.src="../images/sh/_.gif";
		obj.style.display = "block";//??????????
		//sIcon.setClass('icon1');	
	}
	else 
	{	
		sIcon.src="../images/sh/show.gif";
		obj.style.display = "none";	
		//sIcon.setClass('icon1 open');
		
	}
}


 function all_Click()   
       {   
          var chk=document.getElementsByName("msuSelected");   
         for(var i=0;i<chk.length;i++)   
          {   
            chk(i).checked=true;   
          }   
       }   
     //反选   
     function not_Click()   
     {   
        var chk=document.getElementsByName("msuSelected");   
        for(var i=0;i<chk.length;i++)   
        {   
			if(chk(i).checked)   
              chk(i).checked=false;   
            else  
             chk(i).checked=true;                 
        }   
     }   


</script>