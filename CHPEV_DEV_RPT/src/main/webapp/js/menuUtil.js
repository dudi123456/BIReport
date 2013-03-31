function window.onload() {

	var oTable = document.getElementById("menuTable");
	var oTr = oTable.rows(0);
	if(oTr!=null){
		var oTd = oTr.cells(0);	
		if (oTd){
			display(oTd,1);
			setColor(oTable.rows(0).firstChild);
		}
	}
}

function display(obj,titleNo){
	var oTr = menuTable.firstChild.children;
	for(var iNum=0; iNum<menuTable.rows.length; iNum++){

		var oTd = oTr[iNum].firstChild;
		var oID = oTd.id;
		var idName = "sub_title" + titleNo;
 //       oTd.outerText.color = "white";
        
		if(oID == idName){
			if(oTr[iNum].style.display == "none")
				oTr[iNum].style.display = "block";
				
			else
				oTr[iNum].style.display = "none";
		}
		if(oID != idName&&oID!="menuButton"){
			oTr[iNum].style.display = "none";
		}
	}

}

var preClickAnchor = null;
var preChild = null;
function setColor(oa){
	var anchor = oa.parentElement;
	
	if(preClickAnchor != null){
		preClickAnchor.className="submenu_l1";
	}
	if(preChild != null){
		preChild.className="submenu_l2";
	}
	anchor.className="submenu_l1_act";
	preClickAnchor = anchor;
}

function setChildColor(oa){
	var anchor = oa.parentElement;
	
	if(preClickAnchor != null){
		preClickAnchor.className="submenu_l1";
	}
	if(preChild != null){
		preChild.className="submenu_l2";
	}
	anchor.className="submenu_l2_act";
	preChild = anchor;
}
