function scwShow(scwEle,format){
		Calendar.setup({
			inputField     :    scwEle,      // id of the input field
			ifFormat       :    format,       // format of the input field
			showsTime      :    true,            // will display a time selector
			button         :    scwEle,   // trigger for the calendar (button ID)
			singleClick    :    true,           
			step           :    1                // show all years in drop-down boxes (instead of every other year as default)
		});
	}

function formatNumber(num,dec,thou,pnt,curr1,curr2,n1,n2) {
  var x = Math.round(num * Math.pow(10,dec));
  if (x >= 0) n1=n2='';
  var y = (''+Math.abs(x)).split('');
  var z = y.length - dec;
  if (z<0) z--;
  for(var i = z; i < 0; i++) y.unshift('0');
  if (z<0) z = 1;
  y.splice(z, 0, pnt);
  if(y[0] == pnt) y.unshift('0');
  while (z > 3) {
    z-=3; y.splice(z,0,thou);
  }

  sign = (num == (num = Math.abs(num)));
var tmpVar = y.join('');

 if (dec==0)
  {
	//var tmpVar = y.join('');
	tmpVar = tmpVar.substring(0,tmpVar.indexOf('.'));
  }

  var r = ((sign)?'':'-') + curr1+n1+tmpVar+n2+curr2;
 // var r = curr1+n1+y.join('')+n2+curr2;
  return r;
}
