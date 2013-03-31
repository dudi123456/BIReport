<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>深度运营支撑平台</title>
<%
String rootPath = request.getContextPath();
%>

<script type='text/javascript' src='<%=rootPath%>/js/date.js'> </script>
<SCRIPT language=javascript src="<%=rootPath%>/js/date/YMDTIME.js"></SCRIPT>


<script lanaguage="javascript">
function isEmpty(s){
  return ((s == null) || (s.length == 0))
}
function isSpace(content2){
  for(i=0;i<content2.length;i++){
    var c=content2.charAt(i);
    if(c!=" ") return false;
  }
  return true;
}
function getStringLength(contact){
  var num=0;
  if (contact!=""){
    var i;
    var s;
    for(i=0;i<contact.length;i++){
      s=contact.charCodeAt(i);
      if(s-128<0) num=num+1;
      else num=num+2;
    }
  }
  return num;
}
function getValue(){

 if(isEmpty(document.form1.starttime.value)){
     alert("请输入生效时间，不能为空！");
     document.form1.starttime.focus();
     return false;
  }
    if(isSpace(document.form1.starttime.value)){
      alert("请输入生效时间，不能全部为空格！");
      document.form1.starttime.focus();
      return false;
    }
 if(isEmpty(document.form1.endtime.value)){
     alert("请输入失效时间，不能为空！");
     document.form1.starttime.focus();
     return false;
  }
    if(isSpace(document.form1.endtime.value)){
      alert("请输入失效时间，不能全部为空格！");
      document.form1.starttime.focus();
      return false;
    }

 if(isEmpty(document.form1.title.value)){
     alert("请输入公告标题，不能为空！");
     document.form1.title.focus();
     return false;
  }
    if(isSpace(document.form1.title.value)){
      alert("请输入公告标题，不能全部为空格！");
      document.form1.title.focus();
      return false;
    }
if(getStringLength(document.form1.title.value)>200)
       {
         alert("你输入的公告标题长度过长！");
         document.form1.title.focus();
         return false;
       } 
  if(isEmpty(document.form1.content.value)){
     alert("请输入公告内容，不能为空！");
     document.form1.content.focus();
     return false;
  }
    if(isSpace(document.form1.content.value)){
      alert("请输入公告内容，不能全部为空格！");
      document.form1.content.focus();
      return false;
    }
if(getStringLength(document.form1.content.value)>2000)
       {
         alert("你输入的公告内容长度过长！");
         document.form1.content.focus();
         return false;
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
                <td width="95%" class="STYLE1"><span class="STYLE03">你当前的位置</span>：[系统管理]-[公告管理]-[公告添加]&nbsp;&nbsp;标记<font color="red">*</font>的为必填项</td>
              </tr>
            </table></td>
            <td width="54%">　</td>
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
        <td><form name="form1" action="bulletinAdmin.rptdo?opt_type=doadd" method="post" onSubmit="return getValue()">
<table width="100%" border="0" cellpadding="5" cellspacing="1" id="bgm">
		<tr>
              <td width="43%" height="-1" align="right" valign="middle">公告范围：</td>
              <td width="56%" height="20"><span class="STYLE19"> <BIBM:TagSelectList listName="city"
									allFlag="" focusID="" listID="0"
									selfSQL="select region_id,region_name from ui_info_region t" />
                
            
              </span></td>
                  </tr>
				  <tr>
              <td width="43%" height="-1" align="right" valign="middle">类型：</td>
              <td width="56%" height="20"><span class="STYLE19"> <SELECT ID="type_id" name="type_id" >
			  <OPTION value='1'>公告</OPTION>
			  <OPTION value='2'>新闻</OPTION>
			  <OPTION value='3'>其它</OPTION>
</SELECT>
                
            
              </span></td>
                  </tr>
                   <tr>
              <td width="43%" align="right">是否用于营业前台：</td>
              <td width="56%"><input type=radio checked name="tp" value="0" >&nbsp;否<input type=radio name="tp" value="1">&nbsp;是</td>            </tr>
			<tr>
              <td width="43%" align="right">生效时间：</td>
              <td width="56%"><INPUT TYPE="text" NAME="starttime" id="starttime" onfocus="SelectDate(this,'yyyy-MM-dd')"  >			  
               &nbsp;&nbsp;<font color="red">*(yyyy-MM-dd)</font></td>
                   </tr>

            <tr>
              <td width="43%" align="right">失效时间：</td>
              <td width="56%"><INPUT TYPE="text" NAME="endtime" id="endtime" onfocus="SelectDate(this,'yyyy-MM-dd')"  >	
						  			
               &nbsp;&nbsp;<font color="red">*(yyyy-MM-dd)</font></td>
                   </tr>
<tr>
              <td width="43%" height="0" align="right">公告标题：</td>
              <td width="56%" height="20"><textarea name="title" rows="4" cols="32"></textarea>&nbsp;&nbsp;<font color="red">*</font></td>
             
            </tr>
<tr>
              <td width="43%" align="right">公告内容：</td>
              <td width="56%"><textarea name="content" rows="10" cols="32"></textarea>&nbsp;&nbsp;<font color="red">*</font></td>            </tr>

                       <tr>
              <td height="20" colspan="4" align="center" valign="middle"><input type="submit" name="Submit" value="提  交"  class="an11"/></td>
            </tr>
            
            
          </table></form></td>
        <td width="8" class="bb15">　</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td height="35"  class="bb19"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="12" height="35" class="bb18"><img src="<%=rootPath%>/biimages/kong.gif" width="12" height="35" /></td>
        <td><img src="<%=rootPath%>/biimages/kong.gif"/></td>
        <td width="16" class="bb20"><img src="<%=rootPath%>/biimages/kong.gif" width="16" height="35" /></td>
      </tr>
    </table></td>
  </tr>
</table>
</body>
</html>