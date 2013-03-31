if ($.fn.pagination){
	$.fn.pagination.defaults.beforePageText = 'P脿gina';
	$.fn.pagination.defaults.afterPageText = 'de {pages}';
	$.fn.pagination.defaults.displayMsg = "Veient {from} a {to} de {total} d'articles";
}
if ($.fn.datagrid){
	$.fn.datagrid.defaults.loadMsg = 'Elaboraci贸, si us plau esperi ...';
}
if ($.messager){
	$.messager.defaults.ok = 'Ok';
	$.messager.defaults.cancel = 'Cancel';
}
if ($.fn.validatebox){
	$.fn.validatebox.defaults.missingMessage = 'Aquest camp 茅s obligatori.';
	$.fn.validatebox.defaults.rules.email.message = 'Introdu茂u una adre莽a de correu electr貌nic v脿lida.';
	$.fn.validatebox.defaults.rules.url.message = 'Si us plau, introdu茂u un URL v脿lida.';
	$.fn.validatebox.defaults.rules.length.message = 'Si us plau, introdu茂u un valor entre {0} i {1}.';
}
if ($.fn.numberbox){
	$.fn.numberbox.defaults.missingMessage = 'Aquest camp 茅s obligatori.';
}
if ($.fn.combobox){
	$.fn.combobox.defaults.missingMessage = 'Aquest camp 茅s obligatori.';
}
if ($.fn.combotree){
	$.fn.combotree.defaults.missingMessage = 'Aquest camp 茅s obligatori.';
}
if ($.fn.calendar){
	$.fn.calendar.defaults.weeks = ['S','M','T','W','T','F','S'];
	$.fn.calendar.defaults.months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
}
if ($.fn.datebox){
	$.fn.datebox.defaults.currentText = 'Avui';
	$.fn.datebox.defaults.closeText = 'Tancar';
	$.fn.datebox.defaults.missingMessage = 'Aquest camp 茅s obligatori.';
}
