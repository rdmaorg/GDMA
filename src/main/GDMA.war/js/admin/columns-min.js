YAHOO.namespace("GDMA.admin.columns");
YAHOO.namespace("GDMA.admin.columns.serverbrowser");
YAHOO.GDMA.admin.columns.fields={fields:[{key:"id",parser:YAHOO.util.DataSource.parseNumber},{key:"name",parser:YAHOO.util.DataSource.parseString},{key:"columnTypeString",parser:YAHOO.util.DataSource.parseString},{key:"primarykey"},{key:"displayed"},{key:"allowInsert"},{key:"allowUpdate"},{key:"nullable"},{key:"dropDownColumnDisplay"},{key:"dropDownColumnStore"},{key:"special"},{key:"minWidth"},{key:"maxWidth"},{key:"orderby"}]};
YAHOO.GDMA.admin.columns.yesnoFormatter=function(B,A,C,D){if(D==true){B.innerHTML="Yes"
}else{B.innerHTML="No"
}};
YAHOO.GDMA.admin.columns.ddTypeFormatter=function(B,A,C,D){if(D){B.innerHTML=D.name
}else{B.innerHTML="[NULL]"
}};
YAHOO.GDMA.admin.columns.editDDLookup=function(C,B){YAHOO.GDMA.admin.dataTable.cancelCellEditor();
var A=document.createElement("div");
A.id="divDlgColumns";
document.body.insertBefore(A,document.body.firstChild);
YAHOO.GDMA.admin.columns.panel=new YAHOO.widget.Panel("divDlgColumns",{draggable:true,close:false,modal:true,width:"438px",height:"400px",xy:[220,100]});
YAHOO.GDMA.admin.columns.clear=function(){var E=C.cell;
var D=C.record;
var F=C.column;
YAHOO.GDMA.admin.dataTable.getRecordSet().updateRecordValue(D,"dropDownColumnDisplay",null);
YAHOO.GDMA.admin.dataTable.getRecordSet().updateRecordValue(D,"dropDownColumnStore",null);
YAHOO.GDMA.admin.dataTable.render();
YAHOO.GDMA.admin.columns.cancel()
};
YAHOO.GDMA.admin.columns.cancel=function(){YAHOO.GDMA.admin.columns.panel.destroy()
};
YAHOO.GDMA.admin.columns.save=function(){var H=YAHOO.util.Dom.get("ddDisplay");
var D=YAHOO.util.Dom.get("ddStore");
if(H.selectedIndex<1||D.selectedIndex<1){YAHOO.GDMA.dialog.showInfoDialog("Error!","Please select both a column to store and a column to display",YAHOO.widget.SimpleDialog.ICON_BLOCK)
}var F=C.cell;
var E=C.record;
var G=C.column;
YAHOO.GDMA.admin.dataTable.getRecordSet().updateRecordValue(E,"dropDownColumnDisplay",YAHOO.GDMA.admin.columns.ddColumnsList[H.options[H.selectedIndex].value]);
YAHOO.GDMA.admin.dataTable.getRecordSet().updateRecordValue(E,"dropDownColumnStore",YAHOO.GDMA.admin.columns.ddColumnsList[D.options[D.selectedIndex].value]);
YAHOO.GDMA.admin.dataTable.render();
YAHOO.GDMA.admin.columns.cancel()
};
YAHOO.GDMA.admin.columns.panel.setHeader("Configure Dropdown for Column - "+C.record.getData("name"));
YAHOO.GDMA.admin.columns.panel.setBody('<div id="divDlgColumnsLayout"></div>');
YAHOO.GDMA.admin.columns.panel.beforeRenderEvent.subscribe(function(){try{YAHOO.util.Event.onAvailable("divDlgColumnsLayout",function(){var E=YAHOO.util.Dom.get("divDlgColumnsLayout");
var G=YAHOO.GDMA.utilities.createElement("div","divDlgColumnsLayoutTreeview",E);
var F=YAHOO.GDMA.utilities.createElement("div","divDlgColumnsLayoutDropdowns",E);
var J=YAHOO.GDMA.utilities.createElement("div","divDlgColumnsLayoutButtons",E);
YAHOO.util.Dom.addClass(J,"gdma-toolbar");
GdmaAdmin.getServerTableColumnList(function(N){YAHOO.GDMA.admin.columns.serverbrowser.serverList=N;
YAHOO.GDMA.admin.columns.serverbrowser.tree=new YAHOO.widget.TreeView("divDlgColumnsLayoutTreeview");
var L=YAHOO.GDMA.admin.columns.serverbrowser.tree.getRoot();
for(i=0;
i<N.length;
i++){var M=new YAHOO.widget.TextNode(N[i].name,L,false);
M.labelStyle="icon-server";
for(j=0;
j<N[i].tables.length;
j++){var Q={label:N[i].tables[j].name,serveridx:i,tableidx:j};
var P=new YAHOO.widget.TextNode(Q,M,false);
P.labelStyle="icon-table";
for(k=0;
k<N[i].tables[j].columns.length;
k++){var Q={label:N[i].tables[j].columns[k].name,serveridx:i,tableidx:j,columnidx:k};
var O=new YAHOO.widget.TextNode(Q,P,false);
O.labelStyle="icon-column"
}}}YAHOO.GDMA.admin.columns.serverbrowser.tree.subscribe("labelClick",function(V){if(!isNaN(V.data.tableidx)){var W=YAHOO.util.Dom.get("ddDisplay");
var T=YAHOO.util.Dom.get("ddStore");
W.options.length=0;
T.options.length=0;
var U=document.createElement("option");
U.value=-1;
U.text="Column To Display";
W.options[0]=U;
U=document.createElement("option");
U.text="Column To Store";
U.value=-1;
T.options[0]=U;
var S=YAHOO.GDMA.admin.columns.serverbrowser.serverList[V.data.serveridx].tables[V.data.tableidx].columns;
YAHOO.GDMA.admin.columns.ddColumnsList={};
YAHOO.GDMA.admin.columns.ddColumnsList[-1]=null;
for(var R=0;
R<S.length;
R++){var U=document.createElement("option");
U.value=S[R].id;
U.text=S[R].name;
W.options[R+1]=U;
U=document.createElement("option");
U.value=S[R].id;
U.text=S[R].name;
T.options[R+1]=U;
YAHOO.GDMA.admin.columns.ddColumnsList[S[R].id]=S[R]
}}else{var W=YAHOO.util.Dom.get("ddDisplay");
var T=YAHOO.util.Dom.get("ddStore");
W.options.length=0;
T.options.length=0;
var U=document.createElement("option");
U.text="Click a Table";
W.options[0]=U;
U=document.createElement("option");
U.text="Click a Table";
T.options[0]=U
}});
YAHOO.GDMA.admin.columns.serverbrowser.tree.draw()
});
var K=YAHOO.GDMA.utilities.createElement("select","ddDisplay",F);
var I=document.createElement("option");
I.text="Click a Table";
K.appendChild(I);
var H=YAHOO.GDMA.utilities.createElement("select","ddStore",F);
I=document.createElement("option");
I.text="Click a Table";
H.appendChild(I);
YAHOO.GDMA.toolbar.createToolBarButton("Clear","btnColumnServerBrowserClear",J,YAHOO.GDMA.admin.columns.clear,"Remove columns from DD cells","show");
YAHOO.GDMA.toolbar.createToolBarButton("Cancel","btnColumnServerBrowserCancel",J,YAHOO.GDMA.admin.columns.cancel,"Close this dialog without saving changes","show");
YAHOO.GDMA.toolbar.createToolBarButton("Ok","btnColumnServerBrowserSave",J,YAHOO.GDMA.admin.columns.save,"Update record with selected columns","show")
})
}catch(D){alert(D)
}});
YAHOO.GDMA.admin.columns.panel.render()
};
YAHOO.GDMA.admin.columns.columnDefs=[{label:"",formatter:YAHOO.GDMA.utilities.rownumFormatter,minWidth:20},{key:"name",label:"Name",sortable:true,resizeable:true,width:170},{key:"columnTypeString",label:"Type",sortable:true,resizeable:true,width:65},{key:"primarykey",label:"Primary Key",formatter:"checkbox",resizeable:true,width:75},{key:"displayed",label:YAHOO.GDMA.utilities.createCheckAllHeader("Displayed"),formatter:"checkbox",resizeable:true,width:85},{key:"allowInsert",label:YAHOO.GDMA.utilities.createCheckAllHeader("Allow Insert"),formatter:"checkbox",resizeable:true,width:85},{key:"allowUpdate",label:YAHOO.GDMA.utilities.createCheckAllHeader("Allow Update"),formatter:"checkbox",resizeable:true,width:85},{key:"nullable",label:"Allow Null",formatter:YAHOO.GDMA.admin.columns.yesnoFormatter,resizeable:true,width:60},{key:"dropDownColumnDisplay",label:"DD Display",formatter:YAHOO.GDMA.admin.columns.ddTypeFormatter,editor:YAHOO.GDMA.admin.columns.editDDLookup,editorOptions:{disableBtns:true},resizeable:true},{key:"dropDownColumnStore",formatter:YAHOO.GDMA.admin.columns.ddTypeFormatter,editor:YAHOO.GDMA.admin.columns.editDDLookup,editorOptions:{disableBtns:true},label:"DD Store",resizeable:true},{key:"special",label:"Special",resizeable:true,formatter:"dropdown",dropdownOptions:[{value:"N",text:"No"},{value:"U",text:"User"},{value:"D",text:"Date"}],width:75},{key:"minWidth",label:"Min Width",editor:"textbox",editorOptions:{validator:YAHOO.GDMA.utilities.validateNumber},width:60},{key:"maxWidth",label:"Max Width",editor:"textbox",editorOptions:{validator:YAHOO.GDMA.utilities.validateNumber},width:60}];
YAHOO.GDMA.admin.columns.deleteRecord=function(){};
YAHOO.GDMA.admin.columns.saveRecord=function(A,C){for(var B=0;
B<A.length;
B++){if(A[B].minWidth>A[B].maxWidth){YAHOO.GDMA.dialog.showInfoDialog("Validation Error!","Please ensure that all minWidths are less than maxWidths.",YAHOO.widget.SimpleDialog.ICON_BLOCK);
return 
}if(YAHOO.lang.trim(A[B].minWidth)==""){A[B].minWidth=null
}if(YAHOO.lang.trim(A[B].maxWidth)==""){A[B].maxWidth=null
}}GdmaAdmin.saveColumns(A,C)
};
YAHOO.GDMA.admin.columns.doubleClick=function(){};
YAHOO.GDMA.admin.columns.loadColumns=function(A,B,C){GdmaAdmin.getColumnsForTable(A,B,function(E){var D=new YAHOO.util.DataSource(E);
D.responseType=YAHOO.util.DataSource.TYPE_JSARRAY;
D.responseSchema=YAHOO.GDMA.admin.columns.fields;
D.columnDefs=YAHOO.GDMA.admin.columns.columnDefs;
C(D)
});
YAHOO.GDMA.admin.saveFunction=YAHOO.GDMA.admin.columns.saveRecord;
YAHOO.GDMA.admin.deleteFunction=YAHOO.GDMA.admin.columns.deleteRecord;
YAHOO.GDMA.admin.doubleClick=YAHOO.GDMA.admin.columns.doubleClick
};