<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<HTML  XMLNS:IE>
<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/tabpane.js"></script>
</head>
<body style="background-color: White" bgcolor="white">
<TABLE cellspacing="0" cellpadding="0" WIDTH="100%" HEIGHT="100%">
  <TR><TD width="100%">
    <iframe id="groupTree" name="groupTree"  width="100%" height="100%" frameborder="0"  marginwidth="0" marginheight="0" scrolling="auto" src="favoriteTree.jsp?treeType=Type_Level" ></iframe>
  </TD></TR>
</TABLE>
</body>
</html>