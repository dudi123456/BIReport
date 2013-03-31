function getalt(){
 if(event.srcElement.title && (event.srcElement.title!='' || (event.srcElement.title=='' && tempalt!=''))){
  altlayer.style.left=event.x;
  altlayer.style.top=event.y+20;
  altlayer.style.display='';
  tempalt=event.srcElement.title;
  tempbg=event.srcElement.altbg;
  tempcolor=event.srcElement.altcolor;
  tempborder=event.srcElement.altborder;
  event.srcElement.title='';
  altlayer.innerHTML=tempalt;
  if (typeof(tempbg)!="undefined"){altlayer.style.background=tempbg}else{altlayer.style.background="infobackground"}
  if (typeof(tempcolor)!="undefined"){altlayer.style.color=tempcolor}else{altlayer.style.color=tempcolor="infotext"}
  if (typeof(tempborder)!="undefined"){altlayer.style.border='1px solid '+tempborder;}else{altlayer.style.border='1px solid #000000';}
 }
}
function quickalt(){
 if(altlayer.style.display==''){
  altlayer.style.left=event.x+15;
  altlayer.style.top=event.y+10;
 }
}
function restorealt(){
 event.srcElement.title=tempalt;
 tempalt='';
 altlayer.style.display='none';
}
