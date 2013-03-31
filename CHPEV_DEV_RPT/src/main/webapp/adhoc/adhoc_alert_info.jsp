<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>经营分析系统</title>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/kw.css" type="text/css">
</head>

<body bgcolor="#E3E3E3">
<form name="Form1" >

          <table width="320" height="125" border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="CDD5EC">
            <tr>
              <td align="center" class="note-side">
                <table width="100%">
                  <tr>
                    <td width="80" align="center"><img src="../biimages/icon-locked.gif" ></td>
                    <td ><table width="100%" border="0">
                        <tr>
                          <td>由于查询提取数据量过于庞大,为了提高查询效率,请选则查询提取记录条数!</td>
                        </tr>
                        <tr>
                          <td><input type="radio" name="row" value="2000" checked><=2000条</td>
                        </tr>
                        <tr>
                          <td><input type="radio" name="row" value="5000"><=5000条</td>
                        </tr>
                        <tr>
                          <td><input type="radio" name="row" value="10000"><=10000条</td>
                        </tr>
                        <tr>
                          <td><input type="radio" name="row" value="50000"><=50000条</td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td><input name="Submit2" type="button" class="button" value="确定" onClick="save(1)" >
                            <input name="Submit3" type="button" class="button" value="取消" onClick="save(0)" ></td>
                        </tr>
                    </table></td>
                  </tr>
              </table></td>
            </tr>
  </table>
</form>  
</body>
</html>

<script>
  <!--
function save(tag){
    if(tag==0){
      window.returnValue="";
    }else{
	    for(i=0;i<document.Form1.row.length;i++){
				if(document.Form1.row[i].checked == true){
					window.returnValue=document.Form1.row[i].value;
				}
		}
	
    }    
    window.close();
    //-->
}

</script>