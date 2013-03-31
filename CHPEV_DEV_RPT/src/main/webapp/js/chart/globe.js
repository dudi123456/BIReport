function getXmlHttp() {
	var xmlHttp = null;
	try {
    // Firefox, Opera 8.0+, Safari
		xmlHttp = new XMLHttpRequest();
	}
	catch (e) {
    // Internet Explorer
		try {
			xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
		}
		catch (e) {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
	}
	return xmlHttp;
}

function PostInfo(url, xml) {
	var xmlHttp = getXmlHttp();
	if (xmlHttp == null) {
		alert("您的浏览器不支持AJAX！");
		return;
	}
	xmlHttp.open("POST", url, false);
	xmlHttp.setRequestHeader("Content-Type", "multipart/form-data");
	xmlHttp.send(xml);
	return xmlHttp.responseText;
}