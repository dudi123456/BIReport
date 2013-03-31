function showcat(ID)
{
    if (document.getElementById(ID).style.pixelHeight == 1) {
        if (ID == 'selectIndex') {
            document.getElementById(ID).style.left = 0;
        }
        else {
        	document.getElementById(ID).style.pixelHeight = "auto";
        }
        
    } else {
        document.getElementById(ID).style.pixelHeight = "auto";
    }
    
}

function hiddencat(ID)
{
    document.getElementById(ID).style.pixelHeight = 1;    
}




