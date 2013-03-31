function assembleTime(){
	var start=$("#start_date").val();
	  var end=$("#end_date").val();
	    if(start && end){
			if(parseInt(start)>parseInt(end)){
				 alert("您选择起始日期大于截至日期，请重新选择");
				 document.getElementById("start_date").focus();
				 return false;
			}
	   }else{
		   alert("请选择日期!");
		   document.getElementById("start_date").focus();
		   return false;
	   }
	    return "&start_date="+start+"&end_date="+end;
}

function loadWithTime(chartUrl){
	    var time=assembleTime();
		if(chartUrl && time){
			ShowWait();
			chartUrl +=time;
			window.location.href=chartUrl;
		}else{
			return;
		}
}