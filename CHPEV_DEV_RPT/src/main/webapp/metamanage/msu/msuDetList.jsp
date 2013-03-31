<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.report.util.DateUtil" %>
<%@ page import="com.ailk.bi.metamanage.model.Measure" %>
<%
    response.setHeader("Cache-Control", "no-stored");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    Measure info = (Measure)request.getAttribute("list");
    String msg = (String)request.getAttribute("msg");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache, must-revalidate"/>
    <meta http-equiv="expires" content="0"/>
    <title>指标详情</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
    <script type="text/javascript" src="<c:url value="/js/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/date/scw.js"/>"></script>
	<style>
		.tab-button-on {
		    background-image: url(<%=request.getContextPath()%>/images/common/taber/tab_bg_on.gif);
		    vertical-align: bottom;
		    font-size: 12px;
		    line-height: 22px;
		    padding-left: 10px;
		    padding-right: 10px;
		    color: #9f4700;
		}

		.tab-button-off {
		    background-image: url(<%=request.getContextPath()%>/images/common/taber/tab_bg_off.gif);
		    vertical-align: bottom;
		    line-height: 22px;
		    padding-left: 10px;
		    padding-right: 10px;
		    color: #666666;
		}
	</style>
	<script>
		function menuChanger(contentID,obj){
			if(contentID==2) {
				<c:if test="${list.msu_id == null || empty list.msu_id}">
				alert("请选择指标!");
				return;
				</c:if>
				obj.href="<%=request.getContextPath()%>/metamanage/msu/sourceFlow.jsp?msu_id=<c:out value='${list.msu_id}'/>&msu_name=<c:out value='${list.msu_name}'/>";
			}
			if(contentID==3) {
				<c:if test="${list.msu_id == null || empty list.msu_id}">
				alert("请选择指标!");
				return;
				</c:if>
				obj.href="<%=request.getContextPath()%>/metamanage/msu/impactFlow.jsp?msu_id=<c:out value='${list.msu_id}'/>&msu_name=<c:out value='${list.msu_name}'/>";
			}
		}
	</script>
</head>
<body class="side-7">
<form name="form" method="post" action="msuDet.rptdo">

<table width="99" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="bottom"><img src="<%=request.getContextPath()%>/images/common/taber/tab_line.gif" width="16" height="5"></td>
    <td><table border="0" cellspacing="0" cellpadding="0">
    <tr>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_left_off.gif" width="5" height="22"></td>
          <td nowrap class="tab-button-on"><a href="javascript:void(0)" onclick="menuChanger(1,this)" class="tab-button-off-link">指标定义</a></td>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_right_off.gif" width="5" height="22"></td>
        </tr>

      </table></td>
    <td><img src="../../biimages/size.gif" width="1"></td>
    <td><table border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_left_off.gif" width="5" height="22"></td>
          <td nowrap class="tab-button-off"><a href="javascript:void(0)" onclick="menuChanger(2,this)" class="tab-button-off-link">血缘分析</a></td>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_right_off.gif" width="5" height="22"></td>
        </tr>
      </table></td>
    <td><img src="<%=request.getContextPath()%>/images/common/system/size.gif" width="1"></td>
    <td><table border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_left_off.gif" width="5" height="22"></td>
          <td nowrap class="tab-button-off"><a href="javascript:void(0)" onclick="menuChanger(3,this)" class="tab-button-off-link">影响分析</a></td>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_right_off.gif" width="5" height="22"></td>
        </tr>
      </table></td>
    <td valign="bottom"><img src="<%=request.getContextPath()%>/images/common/taber/tab_right_line.gif" width="280" height="5"></td>
  </tr>
</table>
<table width="100%">

    <tr>
        <td>
            <TABLE bgColor="#999999" WIDTH="100%" CELLPADDING="4" border="0" cellspacing="1">
               <TR bgColor="#ffffff">
                   <td align="right" width="14%">指标标识</td>
                   <td width="40%"><c:out value="${list.msu_id}"/></td>
                   <td align="right" width="12%">指标名称</td>
                   <td width="40%"><c:out value="${list.msu_name}"/></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">指标类型</td>
                   <td><c:out value="${list.msu_type_name}"/></td>
                   <td align="right">指标单位</td>
                   <td><c:out value="${list.msu_unit}"/></td>
               </TR>
               <tr bgColor="#ffffff">
                   <td align="right">指标编码</td>
                   <td><c:out value="${list.msu_code}"/></td>
                   <td align="right">指标字段</td>
                   <td><c:out value="${list.msu_field}"/></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">上级指标标识</td>
                   <td><c:out value="${list.parent_id}"/></td>
                   <td align="right">指标精度</td>
                   <td><c:out value="${list.msu_digit}"/></td>
              </TR>
               <TR bgColor="#ffffff">
                   <td align="right">是否计算指标</td>
                   <td>

						    <c:if test="${list.is_calmsu == 'N'}">
						    	否
						    </c:if>
						    <c:if test="${list.is_calmsu == 'Y'}">
						    	是
						    </c:if>
					</td>
                   <td align="right">是否时点指标</td>
                   <td>
					    <c:choose>
						    <c:when test="${list.is_timemsu == 'N'}">
						    	时期
						    </c:when>
						    <c:when test="${list.is_timemsu == 'Y'}">
						    	时点
						    </c:when>
					    </c:choose>
					</td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">是否衍生指标</td>
                   <td colspan="3">
					    <c:choose>
						    <c:when test="${list.is_deri == 'N'}">
						    	否
						    </c:when>
						    <c:when test="${list.is_deri == 'Y'}">
						    	是
						    </c:when>
					    </c:choose>
                   </td>
               </tr>
               <tr bgColor="#ffffff">
                   <td align="right">指标描述</td>
                   <td colspan="3"><c:out value="${list.msu_desc}"/></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">数据口径规则</td>
                   <td colspan="3"><c:out value="${list.msu_rule}"/></td>
              </TR>
               <TR bgColor="#ffffff">
                   <td align="right">数据口径规则描述</td>
                   <td colspan="3"><c:out value="${list.msu_rule_desc}"/></td>
              </TR>
               <TR bgColor="#ffffff">
                   <td align="right">数据应用规则</td>
                   <td colspan="3"><c:out value="${list.msu_app_rule}"/></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">数据应用规则描述</td>
                   <td colspan="3"><c:out value="${list.msu_app_rule_desc}"/></td>
               </TR>
            </table>
        </td>
    </tr>
</table>
</form>
</body>
</html>
