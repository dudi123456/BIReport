<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<jsp:directive.page import="com.ailk.bi.adhoc.util.AdhocConstant"/>


<%

String rootPath = request.getContextPath();
String flag = (String)request.getAttribute("flag");
String infoMsg = "你的导出任务已添加到队列中，请耐心等待！";
String infoMsg2 = "你可以去‘我的收藏夹’中查看清单生成情况";

if (flag.equals("0")){
	
}else if(flag.equals("1")){
	infoMsg = "不能重复添加相同的任务【名称相同】！";
	infoMsg2 = "检查你的任务名称是否相同?";
}else if(flag.equals("2")){
	infoMsg = "选择的字段太多";
	infoMsg2 = "请精确选择你要导出的字段";
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link href="<%=rootPath%>/css/tablecss/main.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=rootPath%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/css/other/tab_css.css" type="text/css">

<title>清单导出</title>
</head>
<body class="main-body">


<table style="width: 100%" height="100%" class="kuangContent" cellspacing="0" cellpadding="0">
					<tr>
						<td valign="top" style="padding: 10px;">
						<table style="width: 100%">
							<tr>
								<td><font class="title1"><%=infoMsg%></font></td>
							</tr>
							 
<tr>
								<td>
								<hr noshade="noshade" style="height: 1px; color: #caddeb">
								</td>
							</tr>
							<tr>
								 <td class="toolbg"><%=infoMsg2%></td>
							</tr>
							<tr>
								 <td class="toolbg"></td>
							</tr>
							
							 <tr>
                          <td class="toolbg" align="center"><input type="button" name="btn_close" value="关闭" class="button-add" onclick="window.close();"></td>
                        </tr>
						</table>
						</td>
					</tr>
					<tr>
					<td><table>
<TD></TD>
</table>

					</td>
					</tr>
				</table>

</body>
</html>
