<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<html>
  <head>
  <title>评分标准及权重</title>
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/newmain.css">
  </head>
<style>
body{padding:0px;margin:0px;overflow:auto;}
html{overflow:auto;}
</style>

  <body>
   	<div class="result_title">
		<span >评分标准及权重</span>		
	</div>
     <table width="100%" cellpadding="0" cellspacing="0" >
      <tr>
        <td class="tdrow3" colspan="2">分区域评价指标</td>
        <td class="tdrow1">北方权重</td>
        <td class="tdrow1">南方权重</td>
      </tr> 
      <tr>
        <td class="tdrow2" rowspan='4'>销售绩效(80%)</td>
        <td class="tdrow">月均3G用户发展量（户）</td>
        <td class="tdrow" align="right">40%</td>
        <td class="tdrow" align="right">50%</td>
      </tr> 
      <tr>        
        <td class="tdrow">北方单厅月均宽带及沃家庭用户发展量（户）</td>
        <td class="tdrow" align="right">20%</td>
        <td class="tdrow" align="right">0%</td>
      </tr> 
      <tr>        
        <td class="tdrow">月均3G合约发展量占该厅3G用户发展量比例</td>
        <td class="tdrow" align="right">10%</td>
        <td class="tdrow" align="right">10%</td>
      </tr> 
      <tr>       
        <td class="tdrow">月均3G用户发展坪效（户/平米）</td>
        <td class="tdrow" align="right">10%</td>
        <td class="tdrow" align="right">20%</td>
      </tr> 
      <tr>
        <td class="tdrow2">硬件资源(20%)</td>
        <td class="tdrow">地理位置</td>
        <td class="tdrow" align="right">20%</td>
        <td class="tdrow" align="right">20%</td>
      </tr> 
     </table>
  </body>
</html>

