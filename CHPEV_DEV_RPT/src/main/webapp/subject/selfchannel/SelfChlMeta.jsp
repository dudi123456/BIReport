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
        <td class="tdrow2" rowspan='4'>销售绩效</td>
        <td class="tdrow">月均3G用户发展量（户）</td>
        <td align="right" class="tdrow">20%</td>
        <td align="right" class="tdrow">30%</td>
      </tr> 
      <tr>        
        <td class="tdrow">北方单厅月均宽带及沃家庭用户发展量（户）</td>
        <td align="right" class="tdrow">10%</td>
        <td align="right" class="tdrow">0%</td>
      </tr> 
      <tr>        
        <td class="tdrow">月均3G合约发展量占该厅3G用户发展量比例</td>
        <td align="right" class="tdrow">10%</td>
        <td align="right" class="tdrow">10%</td>
      </tr> 
      <tr>       
        <td class="tdrow">月均3G用户发展坪效（户/平米）</td>
        <td align="right" class="tdrow">5%</td>
        <td align="right" class="tdrow">5%</td>
      </tr> 
      <tr>
        <td class="tdrow2" rowspan='2'>渠道效益</td>
        <td class="tdrow">3G业务收入</td>
        <td align="right" class="tdrow">10%</td>
        <td align="right" class="tdrow">10%</td>
      </tr> 
      <tr>
        <td class="tdrow">全业务出账收入</td>
        <td align="right" class="tdrow">10%</td>
        <td align="right" class="tdrow">10%</td>
      </tr>
      <tr>
        <td class="tdrow2" rowspan='3'>渠道质量</td>
        <td class="tdrow">3G有效发展率</td>
        <td align="right" class="tdrow">20%</td>
        <td align="right" class="tdrow">20%</td>
      </tr>     
      <tr>
        <td class="tdrow">3G三无、极低使用量用户占比（无语音无短信无流量）</td>
        <td align="right" class="tdrow">10%</td>
        <td align="right" class="tdrow">10%</td>
      </tr>
      <tr>
        <td class="tdrow">3G用户ARPU值</td>
        <td align="right" class="tdrow">5%</td>
        <td align="right" class="tdrow">5%</td>
      </tr>     
</table>
</body>
</html>