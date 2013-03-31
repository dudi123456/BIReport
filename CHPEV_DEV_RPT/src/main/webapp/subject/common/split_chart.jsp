<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.base.struct.*"%>
<%@ page import="com.ailk.bi.common.app.Arith"%>
<%@ page import="com.ailk.bi.report.struct.ReportQryStruct" %>
<%@ page import="com.ailk.bi.subject.util.SplitChartOpt"%>
<html>
<meta http-equiv="Content-Language" content="zh-cn">
<link rel="stylesheet" href="../<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<%
//查询结构
ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
if(qryStruct == null){
	qryStruct = new ReportQryStruct();
}
qryStruct.splity_one="0.1";
qryStruct.splity_two="0.2";
qryStruct.splity_three="0.4";
qryStruct.splity_four="0.8";
qryStruct.splitx_one="0.1";
qryStruct.splitx_two="0.2";
qryStruct.splitx_three="0.4";
qryStruct.splitx_four="0.6";

SplitChartOpt s_chart= new SplitChartOpt("FUI_MONITOR_PRODUCT_D");
s_chart.setCountMsu("PRODUCT_ID");
s_chart.setMsuX("ACCT_NUM");
s_chart.setMsuY("FEE");
s_chart.setArith_msuX("SUM(ACCT_NUM)");
s_chart.setArith_msuY("SUM(FEE)");
s_chart.setWhereSql(" where 1=1 "+CommConditionUtil.getPubWhere("SC_C_132",request,qryStruct));
%>
<%
String wheresql = " where 1=1 ";
	
String y1 = qryStruct.splity_one;
String y2 = qryStruct.splity_two;
String y3 = qryStruct.splity_three;
String y4 = qryStruct.splity_four;
String x1 = qryStruct.splitx_one;
String x2 = qryStruct.splitx_two;
String x3 = qryStruct.splitx_three;
String x4 = qryStruct.splitx_four;
String[] arrX = new String[4];
arrX[0] = x1;
arrX[1] = x2;
arrX[2] = x3;
arrX[3] = x4;
String[] arrY = new String[4];
arrY[0] = y1;
arrY[1] = y2;
arrY[2] = y3;
arrY[3] = y4;
String[] Rates = new String[18];
String[] RatesX = s_chart.getRate(arrX, s_chart.getMsuX());
String[] RatesY = s_chart.getRate(arrX, s_chart.getMsuY());
		
//刻度
String measure_col = "G6,G2";
String value11="0,0,0,0",value12="0,0,0,0",value13="0,0,0,0",value14="0,0,0,0",value15="0,0,0,0";
String value21="0,0,0,0",value22="0,0,0,0",value23="0,0,0,0",value24="0,0,0,0",value25="0,0,0,0",value26="0,0,0,0";
String value31="0,0,0,0",value32="0,0,0,0",value33="0,0,0,0",value34="0,0,0,0",value35="0,0,0,0",value36="0,0,0,0";
String value41="0,0,0,0",value42="0,0,0,0",value43="0,0,0,0",value44="0,0,0,0",value45="0,0,0,0",value46="0,0,0,0";
String value51="0,0,0,0",value52="0,0,0,0",value53="0,0,0,0",value54="0,0,0,0",value55="0,0,0,0",value56="0,0,0,0";
String value61="0,0,0,0",value62="0,0,0,0",value63="0,0,0,0",value64="0,0,0,0",value65="0,0,0,0",value66="0,0,0,0";
String desc11="",desc12="",desc13="",desc14="",desc15="",desc16="";
String desc21="",desc22="",desc23="",desc24="",desc25="",desc26="";
String desc31="",desc32="",desc33="",desc34="",desc35="",desc36="";
String desc41="",desc42="",desc43="",desc44="",desc45="",desc46="";
String desc51="",desc52="",desc53="",desc54="",desc55="",desc56="";
String desc61="",desc62="",desc63="",desc64="",desc65="",desc66="";
if(Rates!=null&&Rates.length>0){
	value11 = ">="+Rates[0]+",<="+Rates[5]+",>="+Rates[12]+",<"+Rates[13];
	value12 = ">="+Rates[0]+",<="+Rates[5]+",>="+Rates[13]+",<"+Rates[14];
	value13 = ">="+Rates[0]+",<="+Rates[5]+",>="+Rates[14]+",<"+Rates[15];
	value14 = ">="+Rates[0]+",<="+Rates[5]+",>="+Rates[15]+",<"+Rates[16];
	value15 = ">="+Rates[0]+",<="+Rates[5]+",>="+Rates[16]+",<="+Rates[17];
	desc11="发展用户数占比："+Rates[7]+"至"+Rates[6]+" 流失/发展："+Rates[0]+"至"+Rates[5];
	desc12="发展用户数占比："+Rates[8]+"至"+Rates[7]+" 流失/发展："+Rates[0]+"至"+Rates[5];
	desc13="发展用户数占比："+Rates[9]+"至"+Rates[8]+" 流失/发展："+Rates[0]+"至"+Rates[5];
	desc14="发展用户数占比："+Rates[10]+"至"+Rates[9]+" 流失/发展："+Rates[0]+"至"+Rates[5];
	desc15="发展用户数占比："+Rates[11]+"至"+Rates[10]+" 流失/发展："+Rates[0]+"至"+Rates[5];
	desc16="发展用户数占比："+Rates[11]+"至"+Rates[6]+" 流失/发展："+Rates[0]+"至"+Rates[5];
	
	value21 = ">="+Rates[4]+",<="+Rates[5]+",>="+Rates[12]+",<"+Rates[13];
	value22 = ">="+Rates[4]+",<="+Rates[5]+",>="+Rates[13]+",<"+Rates[14];
	value23 = ">="+Rates[4]+",<="+Rates[5]+",>="+Rates[14]+",<"+Rates[15];
	value24 = ">="+Rates[4]+",<="+Rates[5]+",>="+Rates[15]+",<"+Rates[16];
	value25 = ">="+Rates[4]+",<="+Rates[5]+",>="+Rates[16]+",<="+Rates[17];
	value26 = ">="+Rates[4]+",<="+Rates[5]+",>="+Rates[12]+",<="+Rates[17];
	desc21="发展用户数占比："+Rates[7]+"至"+Rates[6]+" 流失/发展："+Rates[4]+"至"+Rates[5];
	desc22="发展用户数占比："+Rates[8]+"至"+Rates[7]+" 流失/发展："+Rates[4]+"至"+Rates[5];
	desc23="发展用户数占比："+Rates[9]+"至"+Rates[8]+" 流失/发展："+Rates[4]+"至"+Rates[5];
	desc24="发展用户数占比："+Rates[10]+"至"+Rates[9]+" 流失/发展："+Rates[4]+"至"+Rates[5];
	desc25="发展用户数占比："+Rates[11]+"至"+Rates[10]+" 流失/发展："+Rates[4]+"至"+Rates[5];
	desc26="发展用户数占比："+Rates[11]+"至"+Rates[6]+" 流失/发展："+Rates[4]+"至"+Rates[5];
		
	value31 = ">="+Rates[3]+",<"+Rates[4]+",>="+Rates[12]+",<"+Rates[13];
	value32 = ">="+Rates[3]+",<"+Rates[4]+",>="+Rates[13]+",<"+Rates[14];
	value33 = ">="+Rates[3]+",<"+Rates[4]+",>="+Rates[14]+",<"+Rates[15];
	value34 = ">="+Rates[3]+",<"+Rates[4]+",>="+Rates[15]+",<"+Rates[16];
	value35 = ">="+Rates[3]+",<"+Rates[4]+",>="+Rates[16]+",<="+Rates[17];
	value36 = ">="+Rates[3]+",<"+Rates[4]+",>="+Rates[12]+",<="+Rates[17];
	desc31="发展用户数占比："+Rates[7]+"至"+Rates[6]+" 流失/发展："+Rates[3]+"至"+Rates[4];
	desc32="发展用户数占比："+Rates[8]+"至"+Rates[7]+" 流失/发展："+Rates[3]+"至"+Rates[4];
	desc33="发展用户数占比："+Rates[9]+"至"+Rates[8]+" 流失/发展："+Rates[3]+"至"+Rates[4];
	desc34="发展用户数占比："+Rates[10]+"至"+Rates[9]+" 流失/发展："+Rates[3]+"至"+Rates[4];
	desc35="发展用户数占比："+Rates[11]+"至"+Rates[10]+" 流失/发展："+Rates[3]+"至"+Rates[4];
	desc36="发展用户数占比："+Rates[11]+"至"+Rates[6]+" 流失/发展："+Rates[3]+"至"+Rates[4];
	
	value41 = ">="+Rates[2]+",<"+Rates[3]+",>="+Rates[12]+",<"+Rates[13];
	value42 = ">="+Rates[2]+",<"+Rates[3]+",>="+Rates[13]+",<"+Rates[14];
	value43 = ">="+Rates[2]+",<"+Rates[3]+",>="+Rates[14]+",<"+Rates[15];
	value44 = ">="+Rates[2]+",<"+Rates[3]+",>="+Rates[15]+",<"+Rates[16];
	value45 = ">="+Rates[2]+",<"+Rates[3]+",>="+Rates[16]+",<="+Rates[17];
	value46 = ">="+Rates[2]+",<"+Rates[3]+",>="+Rates[12]+",<="+Rates[17];
	desc41="发展用户数占比："+Rates[7]+"至"+Rates[6]+" 流失/发展："+Rates[2]+"至"+Rates[3];
	desc42="发展用户数占比："+Rates[8]+"至"+Rates[7]+" 流失/发展："+Rates[2]+"至"+Rates[3];
	desc43="发展用户数占比："+Rates[9]+"至"+Rates[8]+" 流失/发展："+Rates[2]+"至"+Rates[3];
	desc44="发展用户数占比："+Rates[10]+"至"+Rates[9]+" 流失/发展："+Rates[2]+"至"+Rates[3];
	desc45="发展用户数占比："+Rates[11]+"至"+Rates[10]+" 流失/发展："+Rates[2]+"至"+Rates[3];
	desc46="发展用户数占比："+Rates[11]+"至"+Rates[6]+" 流失/发展："+Rates[2]+"至"+Rates[3];
	
	value51 = ">="+Rates[1]+",<"+Rates[2]+",>="+Rates[12]+",<"+Rates[13];
	value52 = ">="+Rates[1]+",<"+Rates[2]+",>="+Rates[13]+",<"+Rates[14];
	value53 = ">="+Rates[1]+",<"+Rates[2]+",>="+Rates[14]+",<"+Rates[15];
	value54 = ">="+Rates[1]+",<"+Rates[2]+",>="+Rates[15]+",<"+Rates[16];
	value55 = ">="+Rates[1]+",<"+Rates[2]+",>="+Rates[16]+",<="+Rates[17];
	value56 = ">="+Rates[1]+",<"+Rates[2]+",>="+Rates[12]+",<="+Rates[17];
	desc51="发展用户数占比："+Rates[7]+"至"+Rates[6]+" 流失/发展："+Rates[1]+"至"+Rates[2];
	desc52="发展用户数占比："+Rates[8]+"至"+Rates[7]+" 流失/发展："+Rates[1]+"至"+Rates[2];
	desc53="发展用户数占比："+Rates[9]+"至"+Rates[8]+" 流失/发展："+Rates[1]+"至"+Rates[2];
	desc54="发展用户数占比："+Rates[10]+"至"+Rates[9]+" 流失/发展："+Rates[1]+"至"+Rates[2];
	desc55="发展用户数占比："+Rates[11]+"至"+Rates[10]+" 流失/发展："+Rates[1]+"至"+Rates[2];
	desc56="发展用户数占比："+Rates[11]+"至"+Rates[6]+" 流失/发展："+Rates[1]+"至"+Rates[2];
		
	value61 = ">="+Rates[0]+",<"+Rates[1]+",>="+Rates[12]+",<"+Rates[13];
	value62 = ">="+Rates[0]+",<"+Rates[1]+",>="+Rates[13]+",<"+Rates[14];
	value63 = ">="+Rates[0]+",<"+Rates[1]+",>="+Rates[14]+",<"+Rates[15];
	value64 = ">="+Rates[0]+",<"+Rates[1]+",>="+Rates[15]+",<"+Rates[16];
	value65 = ">="+Rates[0]+",<"+Rates[1]+",>="+Rates[16]+",<="+Rates[17];
	value66 = ">="+Rates[0]+",<"+Rates[1]+",>="+Rates[12]+",<="+Rates[17];
	desc61="发展用户数占比："+Rates[7]+"至"+Rates[6]+" 流失/发展："+Rates[0]+"至"+Rates[1];
	desc62="发展用户数占比："+Rates[8]+"至"+Rates[7]+" 流失/发展："+Rates[0]+"至"+Rates[1];
	desc63="发展用户数占比："+Rates[9]+"至"+Rates[8]+" 流失/发展："+Rates[0]+"至"+Rates[1];
	desc64="发展用户数占比："+Rates[10]+"至"+Rates[9]+" 流失/发展："+Rates[0]+"至"+Rates[1];
	desc65="发展用户数占比："+Rates[11]+"至"+Rates[10]+" 流失/发展："+Rates[0]+"至"+Rates[1];
	desc66="发展用户数占比："+Rates[11]+"至"+Rates[6]+" 流失/发展："+Rates[0]+"至"+Rates[1];
}
		
String[][] RateDatas = s_chart.getRateData(RatesX,RatesY);
int m=0;
%>
<body onload="setfrist('<%=desc16%>')">
<table border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td colspan="3" align="center" valign="bottom"><img src="../biimages/ctable/arrow2.gif" width="8" height="7"></td>
    <td colspan="7" valign="bottom"></td>
  </tr>
  <tr>
    <td height="10" align="right" valign="top"></td>
    <td width="2" rowspan="7" align="center" valign="bottom" background="../biimages/ctable/line1.gif"></td>
    <td colspan="8" align="center"></td>
  </tr>
  <tr>
    <td height="50" valign="bottom"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="30" align="right">列合计</td>
      </tr>
      <tr>
        <td align="right">MAX:<%=Rates[5]%></td>
      </tr>
    </table></td>
    <td colspan="6" rowspan="6"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="80" height="50" align="center" bgcolor="#FFFF99" id="a1" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value11%>','F','F','<%=desc11%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td width="1" background="../biimages/ctable/line1.gif"></td>
        <td width="80" align="center" bgcolor="#FFFF99" id="a2" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value12%>','F','F','<%=desc12%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td width="1" background="../biimages/ctable/line1.gif"></td>
        <td width="80" align="center" bgcolor="#FFFF99" id="a3" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value13%>','F','F','<%=desc13%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td width="1" background="../biimages/ctable/line1.gif"></td>
        <td width="80" align="center" bgcolor="#FFFF99" id="a4" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value14%>','F','F','<%=desc14%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td width="1" background="../biimages/ctable/line1.gif"></td>
        <td width="80" align="center" bgcolor="#FFFF99" id="a5" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value15%>','F','F','<%=desc15%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td width="1" background="../biimages/ctable/line1.gif"></td>
        <td width="80" align="center" bgcolor="#FFFF99" id="a6" onclick="tableori(this,20201,1201,'<%=desc16%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
      </tr>
      <tr background="../biimages/ctable/line2.gif">
        <td height="1" colspan="11" background="../biimages/ctable/line2.gif"></td>
      </tr>
      <tr align="center">
        <td height="50" bgcolor="#FFFF00" id="b1" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value21%>','T','F','<%=desc21%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#FFDF46" id="b2" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value22%>','T','F','<%=desc22%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#FFBC23" id="b3" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value23%>','T','F','<%=desc23%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#FF8923" id="b4" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value24%>','T','F','<%=desc24%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#FF2323" id="b5" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value25%>','T','F','<%=desc25%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#FFFF99" id="b6" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value26%>','T','F','<%=desc26%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
      </tr>
      <tr align="center" background="../biimages/ctable/line2.gif">
        <td height="1" colspan="11" background="../biimages/ctable/line2.gif"></td>
      </tr>
      <tr align="center">
        <td height="50" bgcolor="#EFEF56" id="c1" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value31%>','F','F','<%=desc31%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#EFD0B3" id="c2" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value32%>','F','F','<%=desc32%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#EFB1FF" id="c3" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value33%>','F','F','<%=desc33%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#EF936B" id="c4" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value34%>','F','F','<%=desc34%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#EF5623" id="c5" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value35%>','F','F','<%=desc35%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#FFFF99" id="c6" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value36%>','F','F','<%=desc36%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
      </tr>
      <tr align="center" background="../biimages/ctable/line2.gif">
        <td height="1" colspan="11" background="../biimages/ctable/line2.gif"></td>
      </tr>
      <tr align="center">
        <td height="50" bgcolor="#CACA88" id="d1" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value41%>','F','F','<%=desc41%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#C6B46A" id="d2" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value42%>','F','F','<%=desc42%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#D5C0FF" id="d3" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value43%>','F','F','<%=desc43%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#D5B6CC" id="d4" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value44%>','F','F','<%=desc44%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#D5A23C" id="d5" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value45%>','F','F','<%=desc45%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#FFFF99" id="d6" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value46%>','F','F','<%=desc46%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
      </tr>
      <tr align="center" background="../biimages/ctable/line2.gif">
        <td height="1" colspan="11" background="../biimages/ctable/line2.gif"></td>
      </tr>
      <tr align="center">
        <td height="50" bgcolor="#7373A8" id="e1" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value51%>','F','F','<%=desc51%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#7985CA" id="e2" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value52%>','F','F','<%=desc52%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#95A8E8" id="e3" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value53%>','F','F','<%=desc53%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#3DC2D1" id="e4" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value54%>','F','F','<%=desc54%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#89BC23" id="e5" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value55%>','F','F','<%=desc55%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#FFFF99" id="e6" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value56%>','F','F','<%=desc56%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
      </tr>
      <tr align="center" background="../biimages/ctable/line2.gif">
        <td height="1" colspan="11" background="../biimages/ctable/line2.gif"></td>
      </tr>
      <tr align="center">
        <td height="50" bgcolor="#3C3CFF" id="f1" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value61%>','F','T','<%=desc61%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#3C6FFF" id="f2" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value62%>','F','T','<%=desc62%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#3CA2D5" id="f3" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value63%>','F','T','<%=desc63%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#3CD5A2" id="f4" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value64%>','F','T','<%=desc64%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#36A36C" id="f5" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value65%>','F','T','<%=desc65%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
        <td background="../biimages/ctable/line1.gif"></td>
        <td bgcolor="#FFFF99" id="f6" onclick="tablec(this,20201,1201,'<%=measure_col%>','<%=value66%>','F','T','<%=desc66%>')" style="cursor:hand">
        共<%=RateDatas[0][m++]%>个<br>
      (<%=Arith.divPerZero(RateDatas[0][m++],"1",2)%>)</td>
      </tr>
    </table></td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
  </tr>
  <tr>
    <td height="51" valign="bottom"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="30" align="left">高</td>
      </tr>
      <tr>
        <td align="right"><%=Rates[4]%></td>
      </tr>
    </table></td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
  </tr>
  <tr>
    <td height="51" valign="bottom"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="30" align="right">&nbsp;</td>
      </tr>
      <tr>
        <td align="right"><%=Rates[3]%></td>
      </tr>
    </table></td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
  </tr>
  <tr>
    <td height="51" valign="bottom"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="30" align="left">离网/发展</td>
      </tr>
      <tr>
        <td align="right"><%=Rates[2]%></td>
      </tr>
    </table></td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
  </tr>
  <tr>
    <td height="51" valign="bottom"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="30" align="right">&nbsp;</td>
      </tr>
      <tr>
        <td align="right"><%=Rates[1]%></td>
      </tr>
    </table></td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
  </tr>
  <tr>
    <td height="51" valign="bottom"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="30" align="left">低</td>
      </tr>
      <tr>
        <td align="right"><%=Rates[0]%></td>
      </tr>
    </table></td>
    <td align="center">&nbsp;</td>
	<td rowspan="4" align="center"><img src="../biimages/ctable/arrow.gif" width="7" height="9"></td>
  </tr>
  <tr>
    <td height="2" colspan="9" align="right"><img src="../biimages/ctable/line2.gif" width="550" height="2"></td>
  </tr>
  <tr>
    <td height="25" align="right"><%=Rates[6]%></td>
	<td width="2" background="../biimages/ctable/line1.gif"></td>
    <td align="right"><%=Rates[7]%></td>
    <td align="right"><%=Rates[8]%></td>
    <td align="right"><%=Rates[9]%></td>
    <td align="right"><%=Rates[10]%></td>
    <td align="right"><%=Rates[11]%></td>
	<td align="center">行合计</td>
    <td align="center">&nbsp;</td>
  </tr>
  <tr>
    <td width="81" height="25" align="center">&nbsp;</td>
	<td width="2"></td>
    <td width="81">低:<%=Rates[12]%></td>
    <td width="81">&nbsp;</td>
    <td width="81">发展用户数</td>
    <td width="81">&nbsp;</td>
    <td width="81">&nbsp;</td>
	<td width="80">高:<%=Rates[17]%></td>
    <td align="center">&nbsp;</td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="100%"><span id="desc"></span></td>
  </tr>
</table>
</body>
</html>
<script>
var clickOBJ=null;
</script>
<script for="d" event="onclick">
this.style.backgroundColor="#ffffff";
if(clickOBJ!=null)
clickOBJ.style.backgroundColor="";
clickOBJ=this;
</script>
<script language="javascript">
  function tablec(obj,tableID,chart_id,index,value,isMax,isMin,desc){
	var url="SubjectCommTable.do?table_id=SC_T_132&first=Y&table_height=240&filter_level=2&filter_indexs="+index+"&filter_values="+value+"&isMax="+isMax+"&isMin="+isMin;
	var target="parent.table_SC_T_132";
	target=eval(target);
	target.location=url;
	
	document.all.desc.innerText=desc;
	setcolor();	
	obj.bgColor="#ffffff";
  }
  function tableori(obj,tableID,chart_id,desc){
	var url="SubjectCommTable.do?table_id=SC_T_132&first=Y&table_height=240";
	var target="parent.table_SC_T_132";
	target=eval(target);
	target.location=url;
	
	document.all.desc.innerText=desc;
	setcolor();
	obj.bgColor="#ffffff";
  }
  function setfrist(desc){
    document.all.desc.innerText=desc;
    setcolor();
    document.all.a6.bgColor="#ffffff";
  }
  function setcolor(){
	document.all.a1.bgColor="#FFFF99";
	document.all.a2.bgColor="#FFFF99";
	document.all.a3.bgColor="#FFFF99";
	document.all.a4.bgColor="#FFFF99";
	document.all.a5.bgColor="#FFFF99";
	document.all.a6.bgColor="#FFFF99";
	document.all.b1.bgColor="#FFFF00";
	document.all.b2.bgColor="#FFDF46";
	document.all.b3.bgColor="#FFBC23";
	document.all.b4.bgColor="#FF8923";
	document.all.b5.bgColor="#FF2323";
	document.all.b6.bgColor="#FFFF99";
	document.all.c1.bgColor="#EFEF56";
	document.all.c2.bgColor="#EFD0B3";
	document.all.c3.bgColor="#EFB1FF";
	document.all.c4.bgColor="#EF936B";
	document.all.c5.bgColor="#EF5623";
	document.all.c6.bgColor="#FFFF99";
	document.all.d1.bgColor="#CACA88";
	document.all.d2.bgColor="#C6B46A";
	document.all.d3.bgColor="#D5C0FF";
	document.all.d4.bgColor="#D5B6CC";
	document.all.d5.bgColor="#D5A23C";
	document.all.d6.bgColor="#FFFF99";
	document.all.e1.bgColor="#7373A8";
	document.all.e2.bgColor="#7985CA";
	document.all.e3.bgColor="#95A8E8";
	document.all.e4.bgColor="#3DC2D1";
	document.all.e5.bgColor="#89BC23";
	document.all.e6.bgColor="#FFFF99";
	document.all.f1.bgColor="#3C3CFF";
	document.all.f2.bgColor="#3C6FFF";
	document.all.f3.bgColor="#3CA2D5";
	document.all.f4.bgColor="#3CD5A2";
	document.all.f5.bgColor="#36A36C";
	document.all.f6.bgColor="#FFFF99";
  }
</script>