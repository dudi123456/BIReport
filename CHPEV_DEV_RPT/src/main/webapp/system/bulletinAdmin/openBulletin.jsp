<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>

<%@ page import="com.ailk.bi.base.util.*"%>

<%@ page import="com.ailk.bi.report.struct.*"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"%>
<%@ page import="com.ailk.bi.bulletin.entity.MartInfoBulletin"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>公告信息</title>
<%
String rootPath = request.getContextPath();
	//列表数据
MartInfoBulletin bulletin = (MartInfoBulletin)session.getAttribute("BULLETIN_DETAIL");
	//查询条件
	if(bulletin == null){
	bulletin = new MartInfoBulletin();
}

	//查询条件
	ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
	if(qryStruct == null){
		qryStruct = new ReportQryStruct();
	}


%>

<link rel="stylesheet" href="<%=rootPath%>/css/icontent.css" type="text/css">

<script src="<%=rootPath%>/js/jquery.min.js"></script>

<script src="<%=rootPath%>/js/jquery.bi.js"></script>

<script type='text/javascript' src='<%=rootPath%>/js/date.js'> </script>


<body>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
  <td valign="top">
		<div class="out">
			<div class="in ltin tpin">
				<table style="width: 100%" height="325" class="kuangContent" cellspacing="0" cellpadding="0">
					<tr>
						<td valign="top" style="padding: 10px;">
						<table style="width: 100%">
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td>
								<hr noshade="noshade" style="height: 1px; color: #caddeb">
								</td>
							</tr>
							<tr>
								<td align="center"><font class="newTitle" ><%=bulletin.getTitle()%></font></td>
							</tr>
							<tr>
								<td>
								<div style="width: 100%; height: 350px; overflow-y: auto;">
									<table style="width: 100%">
										<tr>
											<td class="newContent">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<%=bulletin.getNewsMsg()%>
											</td>
										</tr>
									</table>
								</div>
								</td>
							</tr>

						</table>
						</td>
					</tr>
				</table>
			</div>
		</div>
    </td>
  </tr>

  <tr>
    <td height="35" >
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>
        	<table width="100%" border="0" cellspacing="0" cellpadding="0">
         	 <tr>
               <td  align="center"><input type="button" value="关闭" name="btnclose" class="btn3" onclick="javascript:window.close();">	</td>
             </tr>
            </table>
            </td>
          </tr>
        </table>
        </td>

      </tr>

</table>
    <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
</body>
</html>