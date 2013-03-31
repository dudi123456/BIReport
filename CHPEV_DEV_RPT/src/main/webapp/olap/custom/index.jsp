<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	测试
	<s:actionmessage />
	<table>
		<tr>
			<th>定制 ID</th>
			<th>定制名称</th>
			<th>报表标识</th>
			<th>显示顺序</th>
			<th>是否有效</th>
			<th>创建人</th>
			<th>操作</th>
		</tr>
		<s:iterator value="model">
			<tr>
				<td><s:property value="customRptId" />
				</td>
				<td><s:property value="customRptName" />
				</td>
				<td><s:property value="reportId" />
				</td>
				<td><s:property value="displayMode" />
				</td>
				<td><s:property value="isValid" />
				</td>
				<td><s:property value="userId" />
				</td>
				<td><a href="book/${customRptId}"> 查看 </a> | <a
					href="book/${customRptId}/edit"> 编辑 </a> | <s:url action="custom/%{customRptId}/remove"
						var="delUrl">
					</s:url> <a href='<s:property value="delUrl" />'> 删除 </a>
				</td>
			</tr>
		</s:iterator>
	</table>
	<a href="<%=request.getContextPath()%>/book/new"> 创建新图书 </a>
</body>
</html>