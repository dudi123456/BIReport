// Set the default language
FCKConfig.AutoDetectLanguage = false ;
FCKConfig.DefaultLanguage = "zh-cn" ;

// Set our sample toolbar.
FCKConfig.ToolbarSets['PluginTable'] = [
    ['Source'],
	['Cut','Copy','Paste','PasteText','PasteWord'],
	['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
	['Table','-','TableInsertRow','TableDeleteRows','TableInsertColumn','TableDeleteColumns','TableInsertCell','TableDeleteCells','TableMergeCells','TableSplitCell'],
	'/',
	['Bold','Italic','Underline','StrikeThrough','-','Subscript','Superscript'],
	['OrderedList','UnorderedList','-','Outdent','Indent'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyFull'],
	['Link','Unlink','Anchor'],
	'/',
	['Style','FontFormat','FontName','FontSize'],
	['TextColor','BGColor']
		
] ;

// If you want to use plugins found on other directories, just use the third parameter.
var sOtherPluginPath = FCKConfig.BasePath.substr(0, FCKConfig.BasePath.length - 7) + 'editor/plugins/' ;

FCKConfig.Plugins.Add( 'tablecommands', null, sOtherPluginPath ) ;

