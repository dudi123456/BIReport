//add by renhui change chart


var tableScreen = "SubjectCommTable.screen";
var chartScreen = "SubjectCommChart.screen";
var tableFrame = "table_";
var chartFrame = "chart_";
var target = "";

function subjectchartchange(chart_id,where,framename,isChildCall){

		if(chart_id){
			//chart and table Number
			var pos = chart_id.indexOf(",");
			if(pos>0){
				//multi-table and multi-chart
				var charts = chart_id.split(",");
				var frames;
				if(framename!=null && framename!=""){
					//multi-chart just one frame
					frames = framename.split(",");
				}
				for(var i=0;i<charts.length;i++){
					if(charts[i].indexOf("table")>=0){
						var tid = charts[i].replace("table","");
						var link = tableScreen+"?table_id="+tid+"&first=Y"+where;
						target = tableFrame+tid;
						//var target_obj=document.getElementById(target);
						//loadNewContent(link,null,target_obj);
						if(isChildCall){
							target="parent."+target;
						}
						target=eval(target);
					  	target.location=link;
					}else{
						var link = chartScreen+"?chart_id="+charts[i]+where;
						target = chartFrame+charts[i];
						if(frames!=null&&frames[i]!=null&&frames[i]!=""){
							link = chartScreen+"?flag=Y&chart_id="+charts[i]+where;
							target = chartFrame+frames[i];
						}
						//alert("link="+link);
						if(isChildCall){
							target="parent."+target;
						}

						target=eval(target);
					  	target.location=link;
					}
				}
			}else{
				//just one table or chart
				if(chart_id.indexOf("table")>=0){
					var tid = chart_id.replace("table","");
					var link = tableScreen+"?table_id="+tid+"&first=Y"+where;
					target = tableFrame+tid;
					if(isChildCall){
							target="parent."+target;
					}
					target=eval(target);
					target.location=link;
				}else{
					var link = chartScreen+"?chart_id="+chart_id+where;
					target = chartFrame+chart_id;
					if(framename!=null && framename!=""){
						link = chartScreen+"?flag=Y&chart_id="+chart_id+where;
						target = chartFrame+framename;
					}
					if(isChildCall){
						target="parent."+target;
					}

					target=eval(target);
					target.location=link;
				}
			}
		}
}


function subjecttablechange(chart_id,where,framename,isChildCall){

		if(chart_id){
			//chart and table Number
			var pos = chart_id.indexOf(",");
			if(pos>0){
				//multi-table and multi-chart
				var charts = chart_id.split(",");
				var frames;
				if(framename!=null && framename!=""){
					//multi-chart just one frame
					frames = framename.split(",");
				}
				for(var i=0;i<charts.length;i++){
						var tid = charts[i];
						var link = tableScreen+"?table_id="+tid+"&first=Y"+where;
						target = tableFrame+tid;
						//var target_obj=document.getElementById(target);
						//loadNewContent(link,null,target_obj);
						if(isChildCall){
							target="parent."+target;
						}
						target=eval(target);
					  	target.location=link;
				}
			}else{
				//just one table or chart
				    var tid = chart_id;
					var link = tableScreen+"?table_id="+tid+"&first=Y"+where;
					target = tableFrame+tid;

					if(isChildCall){
							target="parent."+target;
					}
					//alert(target);
					target=eval(target);
					target.location=link;

			}
		}

}




function subjectchartchangenoflag(chart_id,where,framename,isChildCall){
	//alert(where);
		if(chart_id){
			//chart and table Number
			var pos = chart_id.indexOf(",");
			if(pos>0){
				//multi-table and multi-chart
				var charts = chart_id.split(",");
				var frames;
				if(framename!=null && framename!=""){
					//multi-chart just one frame
					frames = framename.split(",");
				}
				for(var i=0;i<charts.length;i++){
					if(charts[i].indexOf("table")>=0){
						var tid = charts[i].replace("table","");
						var link = tableScreen+"?table_id="+tid+"&first=Y"+encodeURI(encodeURI(where));
						target = tableFrame+tid;
						//var target_obj=document.getElementById(target);
						//loadNewContent(link,null,target_obj);
						if(isChildCall){
							target="parent."+target;
						}
						target=eval(target);
					  	target.location=link;
					}else{
						var link = chartScreen+"?chart_id="+charts[i]+encodeURI(encodeURI(where));
						target = chartFrame+charts[i];
						if(frames!=null&&frames[i]!=null&&frames[i]!=""){
							link = chartScreen+"?chart_id="+charts[i]+encodeURI(encodeURI(where));
							target = chartFrame+frames[i];
						}
						//alert("link="+link);
						if(isChildCall){
							target="parent."+target;
						}
						target=eval(target);
					  	target.location=link;
					}
				}
			}else{
				//just one table or chart
				if(chart_id.indexOf("table")>=0){
					var tid = charts[i].replace("table","");
					var link = tableScreen+"?table_id="+tid+"&first=Y"+where;
					target = tableFrame+tid;
					if(isChildCall){
							target="parent."+target;
					}
					target=eval(target);
					target.location=link;
				}else{
					var link = chartScreen+"?chart_id="+chart_id+where;
					target = chartFrame+chart_id;
					if(framename!=null && framename!=""){
						link = chartScreen+"?chart_id="+chart_id+encodeURI(encodeURI(where));
						target = chartFrame+framename;
					}
					if(isChildCall){
						target="parent."+target;
					}
					target=eval(target);
					//alert(link);
					target.location=link;
				}
			}
		}
}

function selectChange(value){
	var callscript = value;
	eval(callscript);
}

function selectChangeChartType(chart_id,value,framename){
	var link = chartScreen+"?chart_id="+chart_id+"&chartType="+value;
	target = chartFrame+chart_id;
	if(framename!=null && framename!=""){
		link = chartScreen+"?chart_id="+chart_id+"&chartType="+value+"&flag=Y";
		target = chartFrame+framename;
	}
	target=eval(target);
	//alert(link);
	target.location=link;
}

function checkboxChange(framename,chartid,chartframe){
	var obj = eval(framename).elements.tags("input");
	var cname = "";
	var desc = "";
	var id = "";
	var wherename = "";
	var wherevalue = "";
	for (i=0; i < obj.length; i++){
		var e = obj[i];
		if(e.name == "checkbox"+chartid){
    		if(e.checked){
    			var values = e.value.split(";");
    			cname = cname + values[0] + " ";
    			if(desc.length>0){
    				desc = desc+","
    			}
    			desc = desc+values[0];
    			if(id.length>0){
    				id = id+",";
    			}
    			id = id+values[1];
    			var wherrArr = values[2].split("=");
    			if(wherevalue.length>0){
    				wherevalue = wherevalue+",";
    			}
    			if(wherrArr!=null){
    				wherename = wherrArr[0];
    				wherevalue = wherevalue+wherrArr[1];
    			}
    		}
    	}
	}
	if(desc.length>0&&id.length>0){
		var where = "&chart_name_r="+cname+"&category_desc="+desc+"&category_index="+id+"&"+wherename+"="+wherevalue;
		//var where = "&category_desc="+desc+"&category_index="+id+"&"+wherename+"="+wherevalue;
		var eee = "eval(subjectchartchangenoflag('"+chartid+"','"+where+"','"+chartframe+"'))";
		eval(eee);
	}
}


function checkboxChange_title(framename,chartid,chartframe,title){
	var obj = eval(framename).elements.tags("input");
	var cname = "";
	var desc = "";
	var id = "";
	var wherename = "";
	var wherevalue = "";
	for (i=0; i < obj.length; i++){
		var e = obj[i];
		if(e.name == "checkbox"+chartid){
    		if(e.checked){
    			var values = e.value.split(";");
    			cname = cname + values[0] + " ";
    			if(desc.length>0){
    				desc = desc+","
    			}
    			desc = desc+values[0];
    			if(id.length>0){
    				id = id+",";
    			}
    			id = id+values[1];
    			var wherrArr = values[2].split("=");
    			if(wherevalue.length>0){
    				wherevalue = wherevalue+",";
    			}
    			if(wherrArr!=null){
    				wherename = wherrArr[0];
    				wherevalue = wherevalue+wherrArr[1];
    			}
    		}
    	}
	}
	if(desc.length>0&&id.length>0){
		var where = "&chart_name_r="+title+"&category_desc="+desc+"&category_index="+id+"&"+wherename+"="+wherevalue;
		//var where = "&category_desc="+desc+"&category_index="+id+"&"+wherename+"="+wherevalue;
		var eee = "eval(subjectchartchangenoflag('"+chartid+"','"+where+"','"+chartframe+"'))";
		eval(eee);
	}
}