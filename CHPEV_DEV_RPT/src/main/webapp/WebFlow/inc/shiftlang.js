//**************************************************************************************
//名称： HTML语言切换函数集
//开发者： 冯春
//撰写时间： 2005年1月
//说明：著作权归开发者拥有，保留一切商业权利。
//使用前请保留这段注释，欢迎使用。
//**************************************************************************************


//-------------------语言切换函数集----------------------------------------------------------------------------

function shiftLanguage(lang,xmlName){
  if(lang=='' || xmlName=='') return false;
  var file = 'langs/'+xmlName+'_'+lang+'.xml';

  var xmlDoc = new ActiveXObject('MSXML2.DOMDocument');
  xmlDoc.async = false;
  var flag = xmlDoc.load(file);
  if(!flag){alert('mutil-Language function fail!');return false;}
  var xmlRoot = xmlDoc.documentElement;

  var Title = xmlRoot.getElementsByTagName("Title").item(0);
  if(Title!=null) {document.title=Title.getAttribute("text");}
  
  var Pros = xmlRoot.getElementsByTagName("Properties").item(0);
  if(Pros!=null){
	  for (var i = 0;i < Pros.childNodes.length;i++){
		Item = Pros.childNodes.item(i);
		id = Item.getAttribute("id");
		property = Item.getAttribute("property");
		text = Item.getAttribute("text");
		eval('document.all.'+id+'.'+property+'="'+text+'"');
	  }
  }

  var Sels = xmlRoot.getElementsByTagName("Selects").item(0);
  if(Sels!=null){
	  for (var i = 0;i < Sels.childNodes.length;i++){
		Item = Sels.childNodes.item(i);
		id = Item.getAttribute("id");
		document.getElementById(id).innerHTML = '';
		
		for (var j = 0;j < Item.childNodes.length;j++){
		  Opt = Item.childNodes.item(j);
		  optValue = Opt.getAttribute("value");
		  optText = Opt.getAttribute("text");

		  var oOption = document.createElement("OPTION");
		  document.getElementById(id).options.add(oOption);
		  oOption.innerText = optText
		  oOption.value = optValue
		}
	  }
  }
}

//-------------------------------------------------------------------------------------------------------------