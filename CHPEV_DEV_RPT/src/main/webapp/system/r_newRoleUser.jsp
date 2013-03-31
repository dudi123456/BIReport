<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.util.*" %>
<%@ page import="com.ailk.bi.base.table.*" %>
<%@ page import="com.ailk.bi.system.common.*" %>
<%
if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>
<%

    String role_code = request.getParameter("role_code");//角色ID
    String role_name = CommTool.getParameterGB(request,"role_name");//角色名称
    String region_id = CommTool.getParameterGB(request,"region_id"); //选择区域
    String region_name =LSInfoRegion.getRegionName(region_id);
    String submitType = request.getParameter("submitType");
    if(submitType==null||"".equals(submitType)){		//判断提交类型
		submitType = "0";
    }

    //数据库处理
    int ErrNo = -1;
    if("1".equals(submitType)){
	String []addArr = request.getParameterValues("userSelect");

	if(addArr!=null && addArr.length > 0){
		String role_bak=role_code;
		ErrNo = 0;//LSInfoRole.setRoleUsers(request,role_code, addArr);
		if(ErrNo < 0){
			if(ErrNo==-10){
			%>
			<script type="text/javascript">
				alert(" 违反角色互斥:<%=request.getAttribute("roleRepel")%>");
			</script>
			<%
			role_code=role_bak;
			}		
			out.println("<table width='100%'><tr><td width='100%' align='center'><font color=red>修改失败！</font></td></tr></table>");
		}else{
			out.println("<table width='100%'><tr><td width='100%' align='center'><font color=red>修改成功！</font></td></tr></table>");
		}
       }
    }

    //取出一个区域下的没有分配给此角色的所有操作员信息
   InfoOperTable sstUsers[] = null;//LSInfoRole.getUsersByRegionNotBelongToRole(role_code, region_id);
  %>
<HTML>
<HEAD>
  <TITLE>角色设置用户</TITLE>
  <LINK REL="stylesheet" TYPE="text/css" HREF="<%=request.getContextPath()%>/css/other/syscss.css">
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <script language="javascript">
  function Direct()
  {
	if((<%=ErrNo%> > 0)&&(<%=submitType%>=="1")){
	  parent.opener.location="userlist.jsp?role_code=<%=role_code%>&role_name=<%=role_name%>&region_id=<%=region_id%>&region_name=<%=region_name%>";
	  parent.winClose();
        }
  }
  function _addRoleUser(){
	var selectFlag = false;
	var add = document.frmEdit.elements.tags("input");
	for(i = 0; i < add.length; i++){
		if(add[i].type == "checkbox" && add[i].checked == true){
			selectFlag = true;
			break;
		}
	}
	if(!selectFlag){
		alert("您没有选中要添加的操作员！");
		return;
	}

	window.frmEdit.submitType.value = 1;
	window.frmEdit.submit();
  }

  function _frmReset(){
	document.frmEdit.reset();
  }

  function _selectAll(){
	var obj = document.frmEdit.elements.tags("input")

	if (document.all.SelectAll.checked){
		for (i=0; i < obj.length; i++){
			var e = obj[i];
			if(e.type == "checkbox" && !e.disabled){
    				e.checked = true;
    			}
		}
	}
	else{
		for (i=0; i < obj.length; i++){
			var e =obj[i];
			if(e.type == "checkbox"){
    				e.checked = false;
    			}
		}
	}
	return false;
  }
  function _isSelectAll(){
	var obj = document.frmEdit.elements.tags("input")

	for (i=0; i < obj.length; i++){
		var e = obj[i];
		if(e.name == "userSelect" && !e.disabled && !e.checked){
			document.all.SelectAll.checked = false;
			return;
		}
	}

	document.all.SelectAll.checked = true;
  }
</script>

</HEAD>

<BODY onload="javascript:Direct();">
  <br>
  <TABLE WIDTH="95%" align="center">
    <TR>
      <TD>
        <FORM name="frmEdit" action="r_newRoleUser.jsp" METHOD="POST" onsubmit="return false;">
          <INPUT TYPE="hidden" id="submitType" name="submitType" value="0"/>
          <INPUT TYPE="hidden" id="role_code" name="role_code" value="<%=role_code%>"/>
          <INPUT TYPE="hidden" id="role_name" name="role_name" value="<%=role_name%>"/>
          <INPUT TYPE="hidden" id="region_id" name="region_id" value="<%=region_id%>"/>
          <TABLE width="100%" border="1" cellspacing="1" cellpadding="1" style="border-collapse: collapse">
            <COLGROUP/><COLGROUP/><COLGROUP/><COLGROUP/><COLGROUP/><COLGROUP width="40"/>
            <TR bgColor="#deebff">
              <TD align="center">姓名</TD>
              <TD align="center">登录工号</TD>
              <TD align="center">性别</TD>
              <TD align="center">区域</TD>
              <TD align="center">选择</TD>
            </TR>
            <%
            if(sstUsers == null){
            %>
            <TR bgColor="whitesmoke">
              <TD colspan="6" ALIGN="CENTER">此区域下还没有设置操作员</TD>
            </TR>
            <%
            }
            else{
              for(int i=0;i<sstUsers.length;i++){
            %>
            <TR bgColor="#ffffff">
              <TD><%=sstUsers[i].oper_name%></TD>
              <TD><%=sstUsers[i].user_id%></TD>
              <TD><% if(sstUsers[i].gender.equals("1")) out.println("男"); else if(sstUsers[i].gender.equals("2")) out.println("女");  %></TD>
              <TD><%=sstUsers[i].region_name%></TD>
              <TD>
                <INPUT id="userSelect" type='checkbox' name="userSelect" value="<%=sstUsers[i].user_id%>" onclick='_isSelectAll();' />
              </TD>
            </TR>
            <%
              }
            }
            %>
          </TABLE>
        </FORM>
        <table width="100%" align="center">
          <tr>
            <td align="right">全选</td>
            <td width="40">
              <INPUT id="SelectAll" type='checkbox' name="SelectAll" value=""onclick="_selectAll();" />
            </td>
          </tr>
        </table>
      </TD>
    </TR>
  </TABLE>
</BODY>
</HTML>


