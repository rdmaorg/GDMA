YAHOO.namespace("GDMA.datagrid");
YAHOO.util.Event.onDOMReady(function(){YAHOO.util.Event.onAvailable("yui-gen8",function(){var A=YAHOO.GDMA.layout.getUnitByPosition("center").body;
YAHOO.GDMA.datagrid.layout=new YAHOO.widget.Layout(A,{units:[{position:"top",height:"28px",resize:false,body:"divToolbar",gutter:"0px 0px 0px 0px",scroll:false},{position:"bottom",height:"30px",resize:false,body:"divPager",gutter:"0px 0px 0px 0px",scroll:false},{position:"center",body:"divDataTable",gutter:"0px 0px 0px 0px",scroll:true}]});
YAHOO.GDMA.datagrid.layout.render()
})
});
YAHOO.GDMA.datagrid.dwrErrorHandler=function(B,A){YAHOO.GDMA.dialog.loading.hide();
YAHOO.GDMA.datagrid.dwrLastException=A;
if(A&&A.mostSpecificCause){YAHOO.GDMA.dialog.showInfoDialog("Error!","An error occured.<br/>Exception: "+A.mostSpecificCause.message,YAHOO.widget.SimpleDialog.ICON_BLOCK)
}else{if(A){YAHOO.GDMA.dialog.showInfoDialog("Error!","An error occured<br/>Exception: "+A.message,YAHOO.widget.SimpleDialog.ICON_BLOCK)
}else{YAHOO.GDMA.dialog.showInfoDialog("Error!","An error occured<br/>Message: "+B,YAHOO.widget.SimpleDialog.ICON_BLOCK)
}}};
YAHOO.GDMA.datagrid.highlightEditableCell=function(A){var B=A.target;
if(YAHOO.util.Dom.hasClass(B,"yui-dt-editable")){this.highlightCell(B)
}};
YAHOO.GDMA.datagrid.formatDropdown=function(E,C,F,G){var D=F.getKey();
var B=YAHOO.GDMA.datagrid.ddColumns[D];
if(B&&G){for(var A=0;
A<B.length;
A++){if(B[A][1]==G){E.innerHTML=B[A][2];
break
}}}else{E.innerHTML="..."
}};
YAHOO.GDMA.datagrid.editDropdown=function(E,D){var F=E.cell;
var I=E.record;
var C=E.column;
var B=E.container;
var G=E.value?E.value:-1;
var A=C.getKey();
var H=B.appendChild(document.createElement("select"));
YAHOO.GDMA.utilities.populateDropDown(H,YAHOO.GDMA.datagrid.ddColumns[A],1,2,G,true);
YAHOO.util.Event.addListener(H,"change",function(){D._oCellEditor.value=H.options[H.selectedIndex].value;
D.fireEvent("editorUpdateEvent",{editor:D._oCellEditor})
});
D._focusEl(H)
};
YAHOO.GDMA.datagrid.doFilter=function(){YAHOO.GDMA.datagrid.paginatedRequest.filters=[];
var A=YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
var H=0;
for(var E=0;
E<A.length;
E++){if(A[E].displayed==true){var I;
var D=document.getElementById("chkIsNull"+A[E].id);
var F=document.getElementById("chkIsBlank"+A[E].id);
if(A[E].dropDownColumnDisplay&&A[E].dropDownColumnStore){var C=document.getElementById("selFilter"+A[E].id);
var B=C.selectedIndex;
if(B<1){I=null
}else{I=C.options[C.selectedIndex].value
}}else{var G=document.getElementById("txtFilter"+A[E].id);
I=G.value
}if((I&&I!="")||D.checked||F.checked){YAHOO.GDMA.datagrid.paginatedRequest.filters[H]={};
YAHOO.GDMA.datagrid.paginatedRequest.filters[H].columnId=A[E].id;
YAHOO.GDMA.datagrid.paginatedRequest.filters[H].columnName=A[E].name;
YAHOO.GDMA.datagrid.paginatedRequest.filters[H].columnType=A[E].columnType;
YAHOO.GDMA.datagrid.paginatedRequest.filters[H].filterValue=I;
YAHOO.GDMA.datagrid.paginatedRequest.filters[H].nullValue=D.checked;
YAHOO.GDMA.datagrid.paginatedRequest.filters[H].blank=F.checked;
H++
}}}YAHOO.GDMA.datagrid.paginatedRequest.recordOffset=0;
YAHOO.GDMA.datagrid.popFormPanel.destroy();
YAHOO.GDMA.datagrid.refreshData()
};
YAHOO.GDMA.datagrid.updateRecords=function(){YAHOO.GDMA.datagrid.dataTable.cancelCellEditor();
if(!YAHOO.GDMA.datagrid.updateList||YAHOO.GDMA.datagrid.updateList.length>0){var A=YAHOO.GDMA.datagrid.updateList.length;
var B=function(){var H={serverId:YAHOO.GDMA.datagrid.paginatedRequest.serverId,tableId:YAHOO.GDMA.datagrid.paginatedRequest.tableId,updates:[]};
var G=YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
for(var F=0;
F<A;
F++){var E=YAHOO.GDMA.datagrid.updateList[F].newValues;
var D=YAHOO.GDMA.datagrid.updateList[F].oldValues;
var C=[];
for(var I in E){if(I!="rowid"){if(YAHOO.lang.isUndefined(D[I])){C.push({columnId:I,oldColumnValue:E[I]})
}else{C.push({columnId:I,newColumnValue:E[I],oldColumnValue:D[I]})
}}}H.updates.push(C)
}YAHOO.GDMA.datagrid.updateRequest=H;
GdmaAjax.updateRecords(H,function(J){YAHOO.GDMA.dialog.showInfoDialog("Updated!",J+" Record"+(J>1?"s":"")+" updated");
YAHOO.GDMA.datagrid.refreshData()
})
};
YAHOO.GDMA.dialog.showYesNoDialog(B,"Please confirm save","Are you sure you wish to save the "+A+" changed record(s)?<br/> This action will happen immediately and is non reversible.")
}else{YAHOO.GDMA.dialog.showInfoDialog("Updated Records","No changed have been made- there is nothing to save!")
}};
YAHOO.GDMA.datagrid.deleteRecord=function(){YAHOO.GDMA.datagrid.dataTable.cancelCellEditor();
var B=YAHOO.GDMA.datagrid.dataTable.getSelectedRows();
if(!B||B.length==0){YAHOO.GDMA.dialog.showInfoDialog("No record selected ...","Please select the record you wish to delete.")
}else{var A=function(){var F={serverId:YAHOO.GDMA.datagrid.paginatedRequest.serverId,tableId:YAHOO.GDMA.datagrid.paginatedRequest.tableId,updates:[]};
var E=YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
for(var C=0;
C<B.length;
C++){var H=YAHOO.GDMA.datagrid.dataTable.getRecord(B[C]).getData();
var G=[];
for(var D=0;
D<E.length;
D++){if(E[D].primarykey==true){G.push({columnId:E[D].id,oldColumnValue:H[E[D].id]})
}}F.updates.push(G)
}YAHOO.GDMA.datagrid.updateRequest=F;
GdmaAjax.deleteRecords(F,function(I){YAHOO.GDMA.dialog.showInfoDialog("Deleted!",I+" Record"+(I>1?"s":"")+" deleted");
YAHOO.GDMA.datagrid.refreshData()
})
};
YAHOO.GDMA.dialog.showYesNoDialog(A,"Please confirm delete","Are you sure you wish to delete the selected "+B.length+" record(s)?<br/> This action will happen immediately and is non reversible.")
}};
YAHOO.GDMA.datagrid.handlePagination=function(B,A){if(B.rowsPerPage==1){B.rowsPerPage=B.totalRecords+100;
B.recordOffset=0
}YAHOO.GDMA.datagrid.paginatedRequest.rowsPerPage=B.rowsPerPage;
YAHOO.GDMA.datagrid.paginatedRequest.recordOffset=B.recordOffset;
YAHOO.GDMA.datagrid.refreshData(B)
};
YAHOO.GDMA.datagrid.resetSortTitle=function(D,B){try{var E=D.getThEl();
var A=E.getElementsByTagName("a")[0];
A.title="Click to sort "+(B==YAHOO.widget.DataTable.CLASS_ASC?"descending":"ascending")
}catch(C){}};
YAHOO.GDMA.datagrid.handleSorting=function(B){var A=YAHOO.widget.DataTable.CLASS_DESC;
if(B.key===this.get("sortedBy").key){A=(this.get("sortedBy").dir===YAHOO.widget.DataTable.CLASS_ASC?YAHOO.widget.DataTable.CLASS_DESC:YAHOO.widget.DataTable.CLASS_ASC)
}YAHOO.GDMA.datagrid.paginatedRequest.sortedByColumnId=""+B.key;
YAHOO.GDMA.datagrid.paginatedRequest.dir=A;
YAHOO.GDMA.datagrid.refreshData();
YAHOO.GDMA.datagrid.resetSortTitle(B,A)
};
YAHOO.GDMA.datagrid.onDataReturnInitializeTable=function(A,B,D){try{YAHOO.GDMA.datagrid.dataTable.onDataReturnInitializeTable(A,B,D)
}catch(C){}YAHOO.GDMA.dialog.loading.hide()
};
YAHOO.GDMA.datagrid.refreshData=function(A){YAHOO.GDMA.dialog.loading.show();
YAHOO.GDMA.datagrid.updateList=[];
var B={success:YAHOO.GDMA.datagrid.onDataReturnInitializeTable,failure:YAHOO.GDMA.datagrid.onDataReturnInitializeTable,scope:YAHOO.GDMA.datagrid.dataTable,argument:{startIndex:YAHOO.GDMA.datagrid.paginatedRequest.recordOffset,pagination:YAHOO.GDMA.datagrid.currentState,sorting:{key:YAHOO.GDMA.datagrid.paginatedRequest.sortedByColumnId,dir:YAHOO.GDMA.datagrid.paginatedRequest.dir}}};
if(A){B.argument.pagination=A
}YAHOO.GDMA.datagrid.datasource.sendRequest(YAHOO.GDMA.datagrid.paginatedRequest,B);
YAHOO.GDMA.toolbar.changeState("defaultMode")
};
YAHOO.GDMA.datagrid.buildTable=function(A){YAHOO.GDMA.datagrid.dataTable=new YAHOO.widget.DataTable("divDataTable",A.columnDefs,A,{initialRequest:YAHOO.GDMA.datagrid.paginatedRequest,paginationEventHandler:YAHOO.GDMA.datagrid.handlePagination,paginator:YAHOO.GDMA.datagrid.paginator,sortedBy:{key:(""+YAHOO.GDMA.datagrid.paginatedRequest.sortedByColumnId),dir:YAHOO.widget.DataTable.CLASS_ASC}});
YAHOO.GDMA.datagrid.dataTable.subscribe("theadCellClickEvent",YAHOO.GDMA.datagrid.dataTable.onEventSortColumn);
YAHOO.GDMA.datagrid.dataTable.sortColumn=YAHOO.GDMA.datagrid.handleSorting;
YAHOO.GDMA.datagrid.dataTable.subscribe("cellMouseoverEvent",YAHOO.GDMA.datagrid.highlightEditableCell);
YAHOO.GDMA.datagrid.dataTable.subscribe("cellMouseoutEvent",YAHOO.GDMA.datagrid.dataTable.onEventUnhighlightCell);
YAHOO.GDMA.datagrid.dataTable.subscribe("cellClickEvent",YAHOO.GDMA.datagrid.dataTable.onEventShowCellEditor);
YAHOO.GDMA.datagrid.dataTable.subscribe("rowMouseoverEvent",YAHOO.GDMA.datagrid.dataTable.onEventHighlightRow);
YAHOO.GDMA.datagrid.dataTable.subscribe("rowMouseoutEvent",YAHOO.GDMA.datagrid.dataTable.onEventUnhighlightRow);
YAHOO.GDMA.datagrid.dataTable.subscribe("rowClickEvent",YAHOO.GDMA.datagrid.dataTable.onEventSelectRow);
YAHOO.GDMA.datagrid.dataTable.subscribe("columnResizeEvent",function(E){var F=this.getColumn(E.target);
var D=E.width;
var C=YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
var B=F.getIndex()-1;
if(C[B].maxWidth&&C[B].maxWidth>0&&D>C[B].maxWidth){this.setColumnWidth(F,C[B].maxWidth)
}});
YAHOO.GDMA.datagrid.dataTable.selectRow(YAHOO.GDMA.datagrid.dataTable.getTrEl(0));
YAHOO.GDMA.datagrid.dataTable.focus();
YAHOO.GDMA.datagrid.addToUpdateList=function(C){var H=C.record.getData();
var E=C.key;
var B=C.oldData;
var F=C.newData;
for(var D=0;
D<YAHOO.GDMA.datagrid.updateList.length;
D++){if(YAHOO.GDMA.datagrid.updateList[D].newValues===H){if(!YAHOO.GDMA.datagrid.updateList[D].oldValues[E]){YAHOO.GDMA.datagrid.updateList[D].oldValues[E]=B
}return 
}}var G={newValues:H,oldValues:{}};
G.oldValues[E]=B;
YAHOO.GDMA.datagrid.updateList.push(G);
YAHOO.GDMA.toolbar.changeState("editmode")
};
YAHOO.GDMA.datagrid.dataTable.subscribe("checkboxClickEvent",function(D){var C=D.target;
var B=this.getRecord(C);
var E=this.getColumn(C);
YAHOO.GDMA.datagrid.getRecordSet().updateRecordValue(B,E.key,C.checked)
});
YAHOO.GDMA.datagrid.dataTable.subscribe("dropdownChangeEvent",function(C){var E=C.target;
var B=this.getRecord(E);
var D=this.getColumn(E);
YAHOO.GDMA.datagrid.dataTable.getRecordSet().updateRecordValue(B,D.key,E.value)
});
YAHOO.GDMA.datagrid.dataTable.subscribe("editorUpdateEvent",function(B){if(B.editor.column.key==="active"){this.saveCellEditor()
}});
YAHOO.GDMA.datagrid.dataTable.subscribe("editorBlurEvent",function(B){this.cancelCellEditor()
});
YAHOO.GDMA.datagrid.dataTable.getRecordSet().subscribe("recordValueUpdateEvent",YAHOO.GDMA.datagrid.addToUpdateList);
YAHOO.GDMA.dialog.loading.hide()
};
YAHOO.GDMA.datagrid.createDatasource=function(){GdmaAjax.getTableDetails(YAHOO.GDMA.datagrid.paginatedRequest.serverId,YAHOO.GDMA.datagrid.paginatedRequest.tableId,function(F){YAHOO.GDMA.datagrid.currentDataDescription=F[0];
YAHOO.GDMA.datagrid.responseSchema={resultsList:"records",fields:[],metaFields:{totalRecords:"totalRecords",paginationRecordOffset:"startIndex",sortDir:"sortDir",sortKey:"key"}};
YAHOO.GDMA.datagrid.columnDefs=[];
var B=F[0].tables[0].columns;
var E=false;
YAHOO.GDMA.datagrid.responseSchema.fields.push({key:"rowid"});
for(var A=0;
A<B.length;
A++){if(B[A].displayed==true){YAHOO.GDMA.datagrid.responseSchema.fields.push({key:""+B[A].id,parser:YAHOO.GDMA.utilities.getParser(B[A].columnType),primarykey:B[A].primarykey});
if(B[A].primarykey==true){E=true
}}}YAHOO.GDMA.datagrid.columnDefs.push({key:"rowid",label:"",sortable:false,resizeable:false,minWidth:20});
for(var A=0;
A<B.length;
A++){if(B[A].displayed==true){var D=""+B[A].id;
var C={key:D,label:B[A].name,formatter:YAHOO.GDMA.utilities.getFormatter(B[A].columnType),sortable:true,sortOptions:{defaultOrder:"asc"},resizeable:true};
if(B[A].minWidth&&B[A].minWidth>0){C.minWidth=B[A].minWidth
}if(B[A].maxWidth&&B[A].maxWidth>0){C.width=B[A].maxWidth
}if(B[A].dropDownColumnDisplay&&B[A].dropDownColumnStore){if(!YAHOO.GDMA.datagrid.ddColumns){YAHOO.GDMA.datagrid.ddColumns={}
}(function(){var G=D;
GdmaAjax.getDropDownData(B[A].dropDownColumnDisplay,B[A].dropDownColumnStore,function(H){YAHOO.GDMA.datagrid.ddColumns[G]=H
})
})();
C.editor=YAHOO.GDMA.datagrid.editDropdown;
C.formatter=YAHOO.GDMA.datagrid.formatDropdown
}else{if(E&&B[A].primarykey!=true&&B[A].allowUpdate==true){C.editor=YAHOO.GDMA.utilities.getEditor(B[A].columnType);
C.editorOptions={validator:YAHOO.GDMA.utilities.getValidator(B[A].columnType)}
}}YAHOO.GDMA.datagrid.columnDefs.push(C)
}}if(B[0]){YAHOO.GDMA.datagrid.paginatedRequest.sortedByColumnId=B[0].id;
YAHOO.GDMA.datagrid.paginatedRequest.dir=YAHOO.widget.DataTable.CLASS_ASC
}if(F[0].tables[0].allowDelete!=true||!E){YAHOO.GDMA.toolbar.updateToolBarButton("btnDelete",{name:"Delete",fn:YAHOO.GDMA.datagrid.deleteRecord,defaultMode:"noshow",tooltip:"delete selected record"})
}YAHOO.GDMA.datagrid.datasource=new YAHOO.util.DataSource(GdmaAjax.getData);
YAHOO.GDMA.datagrid.datasource.responseType=YAHOO.util.DataSource.TYPE_JSON;
YAHOO.GDMA.datagrid.datasource.responseSchema=YAHOO.GDMA.datagrid.responseSchema;
YAHOO.GDMA.datagrid.datasource.columnDefs=YAHOO.GDMA.datagrid.columnDefs;
YAHOO.GDMA.datagrid.buildTable(YAHOO.GDMA.datagrid.datasource)
})
};
YAHOO.GDMA.datagrid.init=function(A,B){YAHOO.GDMA.dialog.loading.show();
YAHOO.GDMA.datagrid.paginatedRequest={serverId:"",tableId:"",rowsPerPage:25,recordOffset:0,sortedByColumnId:"",dir:"asc",filters:[]};
YAHOO.GDMA.datagrid.updateRequest={serverId:"",tableId:"",primarykey:[],updates:[]};
YAHOO.GDMA.datagrid.updateList=[];
if(YAHOO.GDMA.datagrid.paginator){YAHOO.GDMA.datagrid.paginator.destroy()
}YAHOO.GDMA.datagrid.paginator=new YAHOO.widget.Paginator({containers:["divPager"],pageLinks:10,rowsPerPage:25,rowsPerPageOptions:[25,50,100,{value:1,text:"All"}],template:"<strong>{CurrentPageReport}</strong>{PreviousPageLink} {PageLinks} {NextPageLink} {RowsPerPageDropdown}",pageReportTemplate:"(page {currentPage} of {totalPages}, total records {totalRecords})"});
dwr.engine.setErrorHandler(YAHOO.GDMA.datagrid.dwrErrorHandler);
dwr.engine.setPreHook(function(){YAHOO.GDMA.dialog.loading.show()
});
dwr.engine.setPostHook(function(){setTimeout("YAHOO.GDMA.dialog.loading.hide();",5000)
});
YAHOO.GDMA.datagrid.paginatedRequest.serverId=A;
YAHOO.GDMA.datagrid.paginatedRequest.tableId=B;
YAHOO.GDMA.datagrid.paginatedRequest.filters=[];
YAHOO.GDMA.datagrid.updateRequest.tableId=B;
YAHOO.GDMA.datagrid.updateRequest.updates=[];
YAHOO.GDMA.datagrid.createDatasource();
YAHOO.GDMA.toolbar.createToolbar(YAHOO.GDMA.datagrid.toolbarButtons)
};
YAHOO.GDMA.datagrid.clearFilter=function(){YAHOO.GDMA.datagrid.paginatedRequest.filters=[];
YAHOO.GDMA.datagrid.refreshData()
};
YAHOO.GDMA.datagrid.filterRecords=function(){YAHOO.GDMA.datagrid.dataTable.cancelCellEditor();
var A={title:"Create a filter for table "+YAHOO.GDMA.datagrid.currentDataDescription.tables[0].name,submit:YAHOO.GDMA.datagrid.doFilter,submitLabel:"Filter",reset:YAHOO.GDMA.datagrid.resetPopupForm,cancel:YAHOO.GDMA.datagrid.cancelPopupForm,body:function(C){YAHOO.GDMA.utilities.createElement("br",null,C);
YAHOO.GDMA.utilities.createElement("br",null,C);
var I=YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
for(var K=0;
K<I.length;
K++){if(I[K].displayed==true){var D=I[K].id;
var E=YAHOO.GDMA.utilities.createElement("p","p"+D,C);
var B=YAHOO.GDMA.utilities.createElement("label","lblFilter"+D,E);
B.htmlFor="txtFilter"+D;
B.innerHTML=I[K].name;
if(I[K].dropDownColumnDisplay&&I[K].dropDownColumnStore){var J=YAHOO.GDMA.utilities.createElement("select","selFilter"+D,E);
YAHOO.GDMA.utilities.populateDropDown(J,YAHOO.GDMA.datagrid.ddColumns[D],1,2,-1,true)
}else{var L=YAHOO.GDMA.utilities.createElement("input","txtFilter"+D,E);
L.type="text"
}var H=YAHOO.GDMA.utilities.createElement("span","spanIsNull"+D,E);
H.innerHTML="Is Null";
E.innerHTML+='<INPUT type="checkbox" id="chkIsNull'+D+'" />';
var G=YAHOO.GDMA.utilities.createElement("span","spanIsBlank"+D,E);
G.innerHTML="Is Blank";
E.innerHTML+='<INPUT type="checkbox" id="chkIsBlank'+D+'" />'
}}for(var K=0;
K<YAHOO.GDMA.datagrid.paginatedRequest.filters.length;
K++){var D=YAHOO.GDMA.datagrid.paginatedRequest.filters[K].columnId;
var F=document.getElementById("txtFilter"+D);
if(!F){F=document.getElementById("selFilter"+D)
}if(F){F.value=YAHOO.GDMA.datagrid.paginatedRequest.filters[K].filterValue;
document.getElementById("chkIsNull"+D).checked=(YAHOO.GDMA.datagrid.paginatedRequest.filters[K].nullValue==true);
document.getElementById("chkIsBlank"+D).checked=(YAHOO.GDMA.datagrid.paginatedRequest.filters[K].blank==true)
}}}};
YAHOO.GDMA.datagrid.createPopupform(A)
};
YAHOO.GDMA.datagrid.doDownload=function(){var E=YAHOO.util.Dom.get("txtPaginatedRequest");
var F=YAHOO.util.Dom.get("frmDownload");
var B=YAHOO.util.Dom.get("rdDownloadAll");
var D=YAHOO.util.Dom.get("rdDownloadExcel");
if(B.checked){var C=YAHOO.GDMA.datagrid.paginatedRequest.rowsPerPage;
var A=YAHOO.GDMA.datagrid.paginatedRequest.recordOffset;
YAHOO.GDMA.datagrid.paginatedRequest.rowsPerPage=YAHOO.GDMA.datagrid.paginator.get("totalRecords")+100;
YAHOO.GDMA.datagrid.paginatedRequest.recordOffset=0;
E.value=YAHOO.lang.JSON.stringify(YAHOO.GDMA.datagrid.paginatedRequest);
YAHOO.GDMA.datagrid.paginatedRequest.rowsPerPage=C;
YAHOO.GDMA.datagrid.paginatedRequest.recordOffset=A
}else{E.value=YAHOO.lang.JSON.stringify(YAHOO.GDMA.datagrid.paginatedRequest)
}if(D.checked){F.action="download.xls"
}else{F.action="download.csv"
}F.submit()
};
YAHOO.GDMA.datagrid.downloadRecords=function(){YAHOO.GDMA.datagrid.dataTable.cancelCellEditor();
var A={width:300,height:200,title:"Download records for table "+YAHOO.GDMA.datagrid.currentDataDescription.tables[0].name,submit:YAHOO.GDMA.datagrid.doDownload,submitLabel:"Download",cancel:YAHOO.GDMA.datagrid.cancelPopupForm,body:function(B){var C=YAHOO.GDMA.utilities.createElement("h1",null,B);
C.innerHTML="Download";
var E=YAHOO.GDMA.utilities.createElement("p",null,B);
E.innerHTML='<label for="rdDownloadExcel">Excel</label> <input id="rdDownloadExcel" name="rdDownloadFormat" type="radio" value="xls" checked="true"> ';
E=YAHOO.GDMA.utilities.createElement("p",null,B);
E.innerHTML='<label for="rdDownloadCsv">CSV</label> <input id="rdDownloadCsv" name="rdDownloadFormat" type="radio" value="csv" > ';
C=YAHOO.GDMA.utilities.createElement("h1",null,B);
C.innerHTML="Records";
E=YAHOO.GDMA.utilities.createElement("p",null,B);
E.innerHTML='<label for="rdDownloadAll">All</label> <input id="rdDownloadAll" name="rdDownloadAmount" type="radio" value="all" checked="true"> ';
E=YAHOO.GDMA.utilities.createElement("p",null,B);
E.innerHTML='<label for="rdCurrentPage">Current Page</label> <input id="rdCurrentPage" name="rdDownloadAmount" type="radio" value="page"> ';
frmDownload=YAHOO.GDMA.utilities.createElement("form","frmDownload",B);
frmDownload.method="post";
var D=YAHOO.GDMA.utilities.createElement("input","txtPaginatedRequest",frmDownload,"hidden");
D.name="txtPaginatedRequest"
}};
YAHOO.GDMA.datagrid.createPopupform(A)
};
YAHOO.GDMA.datagrid.addRecordSave=function(){var F={serverId:YAHOO.GDMA.datagrid.paginatedRequest.serverId,tableId:YAHOO.GDMA.datagrid.paginatedRequest.tableId,updates:[]};
var C=YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
var I=0;
var B=[];
for(var E=0;
E<C.length;
E++){if(C[E].displayed==true&&C[E].allowInsert){var J;
if(C[E].dropDownColumnDisplay&&C[E].dropDownColumnStore){var G=document.getElementById("selColumnValue"+C[E].id);
var D=G.selectedIndex;
if(D<1){if(C[E].nullable){J=null
}else{YAHOO.GDMA.dialog.showInfoDialog("Validation Error!","You must select an item from the "+C[E].name+" dropdown");
return 
}}J=G.options[G.selectedIndex].value
}else{var H=document.getElementById("txtColumnValue"+C[E].id);
J=H.value;
var A=YAHOO.GDMA.utilities.getValidator(C[E].columnType);
if(A){if(A(J)==null){H.style.color="red";
YAHOO.GDMA.dialog.showInfoDialog("Validation Error!","Field values in red are not valid.");
return 
}}H.style.color="black"
}B.push({columnId:C[E].id,newColumnValue:J})
}}F.updates.push(B);
GdmaAjax.addRecord(F,function(){YAHOO.GDMA.dialog.showInfoDialog("Saved!","Record added");
YAHOO.GDMA.datagrid.refreshData();
YAHOO.GDMA.datagrid.popFormPanel.destroy()
})
};
YAHOO.GDMA.datagrid.addRecord=function(){YAHOO.GDMA.datagrid.dataTable.cancelCellEditor();
var A={title:"Primary Key Values",submit:YAHOO.GDMA.datagrid.addRecordSave,submitLabel:"Add",reset:YAHOO.GDMA.datagrid.resetPopupForm,cancel:YAHOO.GDMA.datagrid.cancelPopupForm,body:function(D){YAHOO.GDMA.utilities.createElement("br",null,D);
YAHOO.GDMA.utilities.createElement("br",null,D);
var F=YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
for(var E=0;
E<F.length;
E++){if(F[E].displayed==true&&F[E].allowInsert==true){var I=F[E].id;
var H=YAHOO.GDMA.utilities.createElement("p","p"+I,D);
var C=YAHOO.GDMA.utilities.createElement("label","lblColumnName"+I,H);
C.htmlFor="txtColumnValue"+I;
C.innerHTML=F[E].name;
if(F[E].dropDownColumnDisplay&&F[E].dropDownColumnStore){var G=YAHOO.GDMA.utilities.createElement("select","selColumnValue"+I,H);
YAHOO.GDMA.utilities.populateDropDown(G,YAHOO.GDMA.datagrid.ddColumns[I],1,2,-1,true)
}else{var B=YAHOO.GDMA.utilities.createElement("input","txtColumnValue"+I,H);
B.type="text"
}}}}};
YAHOO.GDMA.datagrid.createPopupform(A)
};
YAHOO.GDMA.datagrid.multiUpdateSave=function(){var I={serverId:YAHOO.GDMA.datagrid.paginatedRequest.serverId,tableId:YAHOO.GDMA.datagrid.paginatedRequest.tableId,updates:[]};
var G=YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
var L=0;
var P={};
for(var H=0;
H<G.length;
H++){if(G[H].displayed==true&&G[H].allowUpdate==true){var D=document.getElementById("chkInclude"+G[H].id);
if(D.checked==true){var N;
if(G[H].dropDownColumnDisplay&&G[H].dropDownColumnStore){var J=document.getElementById("txtColumnValue"+G[H].id);
var F=J.selectedIndex;
if(F<1){if(G[H].nullable){N=null
}else{YAHOO.GDMA.dialog.showInfoDialog("Validation Error!","You must select an item from the "+G[H].name+" dropdown");
return 
}}N=J.options[J.selectedIndex].value
}else{var K=document.getElementById("txtColumnValue"+G[H].id);
N=K.value;
var A=YAHOO.GDMA.utilities.getValidator(G[H].columnType);
if(A){if(A(N)==null){K.style.color="red";
YAHOO.GDMA.dialog.showInfoDialog("Validation Error!","Field values in red are not valid.");
return 
}}K.style.color="black"
}P[G[H].id]=N
}}}var E=YAHOO.GDMA.datagrid.dataTable.getSelectedRows();
for(var H=0;
H<E.length;
H++){var C=YAHOO.GDMA.datagrid.dataTable.getRecord(E[H]).getData();
var M=C;
var O=[];
for(var B in M){if(B!="rowid"){if(YAHOO.lang.isUndefined(P[B])){O.push({columnId:B,oldColumnValue:M[B]})
}else{O.push({columnId:B,newColumnValue:P[B],oldColumnValue:M[B]})
}}}I.updates.push(O)
}YAHOO.GDMA.datagrid.updateRequest=I;
GdmaAjax.updateRecords(I,function(Q){YAHOO.GDMA.dialog.showInfoDialog("Saved!",Q+" Record"+(Q>1?"s":"")+" updated");
YAHOO.GDMA.datagrid.refreshData();
YAHOO.GDMA.datagrid.popFormPanel.destroy()
})
};
YAHOO.GDMA.datagrid.multiUpdate=function(){YAHOO.GDMA.datagrid.dataTable.cancelCellEditor();
var B=YAHOO.GDMA.datagrid.dataTable.getSelectedRows();
if(!B||B.length<2){YAHOO.GDMA.dialog.showInfoDialog("No record selected ...","Please select the multiple records you wish to update.")
}else{var A={title:"Update All Selected Rows",submit:YAHOO.GDMA.datagrid.multiUpdateSave,submitLabel:"Update All",reset:YAHOO.GDMA.datagrid.resetPopupForm,cancel:YAHOO.GDMA.datagrid.cancelPopupForm,body:function(C){YAHOO.GDMA.utilities.createElement("br",null,C);
var E=YAHOO.GDMA.utilities.createElement("p",null,C);
E.innerHTML+="Use the checkbox to enable the columns you wish to update";
YAHOO.GDMA.utilities.createElement("br",null,C);
var H=YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
for(var I=0;
I<H.length;
I++){if(H[I].displayed==true&&H[I].allowUpdate==true){var D=H[I].id;
var E=YAHOO.GDMA.utilities.createElement("p","p"+D,C);
var F=YAHOO.GDMA.utilities.createElement("label","lblColumnName"+D,E);
F.htmlFor="txtColumnValue"+D;
F.innerHTML=H[I].name;
var K="chkInclude"+D;
var G=YAHOO.GDMA.utilities.createElement("input",K,E,"checkbox");
G.value=H[I].id;
if(H[I].dropDownColumnDisplay&&H[I].dropDownColumnStore){var J=YAHOO.GDMA.utilities.createElement("select","txtColumnValue"+D,E);
YAHOO.GDMA.utilities.populateDropDown(J,YAHOO.GDMA.datagrid.ddColumns[D],1,2,-1,true);
J.disabled=true
}else{var J=YAHOO.GDMA.utilities.createElement("input","txtColumnValue"+D,E);
J.type="text";
J.disabled=true
}YAHOO.util.Event.addListener(K,"click",function(N,O){var M=YAHOO.util.Dom.get("chkInclude"+O);
var L=YAHOO.util.Dom.get("txtColumnValue"+O);
if(M.checked==true){L.disabled=false
}else{L.value="";
L.disabled=true
}},D)
}}}};
YAHOO.GDMA.datagrid.createPopupform(A)
}};
YAHOO.GDMA.datagrid.cancelPopupForm=function(){YAHOO.GDMA.datagrid.popFormPanel.destroy()
};
YAHOO.GDMA.datagrid.resetPopupForm=function(){var B=YAHOO.util.Dom.get("divDlgPopupFormCenter");
var A=B.getElementsByTagName("input");
for(var C=0;
C<A.length;
C++){if(A[C].type=="text"){A[C].value=""
}else{if(A[C].type=="checkbox"){A[C].checked=false
}}}};
YAHOO.GDMA.datagrid.createPopupform=function(B){var D=B.submitLabel?B.submitLabel:"Save";
var C=B.width?B.width:500;
var A=B.height?B.height:400;
var E=YAHOO.util.Dom.get("divDlgPopupForm");
if(!E){E=document.createElement("div");
E.id="divDlgPopupForm";
document.body.insertBefore(E,document.body.firstChild)
}YAHOO.GDMA.datagrid.popFormPanel=new YAHOO.widget.Panel("divDlgPopupForm",{draggable:true,close:false,modal:true,width:(C+8)+"px",xy:[140,100]});
YAHOO.GDMA.datagrid.popFormPanel.setHeader(B.title);
YAHOO.GDMA.datagrid.popFormPanel.setBody('<div id="divDlgPopupFormLayout"></div>');
YAHOO.GDMA.datagrid.popFormPanel.beforeRenderEvent.subscribe(function(){YAHOO.util.Event.onAvailable("divDlgPopupFormLayout",function(){var H=YAHOO.util.Dom.get("divDlgPopupFormLayout");
var F=YAHOO.GDMA.utilities.createElement("div","divDlgPopupFormCenter",H);
var G=YAHOO.GDMA.utilities.createElement("div","divDlgPopupFormBottom",H);
YAHOO.util.Dom.addClass(G,"gdma-toolbar");
YAHOO.GDMA.datagrid.popFormLayout=new YAHOO.widget.Layout("divDlgPopupFormLayout",{height:A,width:C,units:[{position:"center",width:100,height:60,body:"divDlgPopupFormCenter",gutter:"0px",scroll:true},{position:"bottom",height:40,body:"divDlgPopupFormBottom"}]});
YAHOO.GDMA.datagrid.popFormLayout.render();
if(B.reset){YAHOO.GDMA.toolbar.createToolBarButton("Reset","btnPopupFormClear",G,B.reset,"Reset the form","show")
}YAHOO.GDMA.toolbar.createToolBarButton("Cancel","btnPopupFormCancel",G,B.cancel,"Close this dialog without applying changes","show");
YAHOO.GDMA.toolbar.createToolBarButton("Save","btnPopupFormSave",G,B.submit,"Apply changes","show").set("label",D);
B.body(F)
})
});
YAHOO.GDMA.datagrid.popFormPanel.render()
};
YAHOO.GDMA.datagrid.toolbarButtons=[{name:"Add",fn:YAHOO.GDMA.datagrid.addRecord,defaultMode:"show",tooltip:"add a new record"},{name:"Delete",fn:YAHOO.GDMA.datagrid.deleteRecord,defaultMode:"show",tooltip:"delete selected record"},{name:"Save",fn:YAHOO.GDMA.datagrid.updateRecords,editmode:"show",tooltip:"save updates and/or additions of records"},{name:"Cancel",fn:YAHOO.GDMA.datagrid.refreshData,editmode:"show",tooltip:"cancel edit and refresh the data"},{name:"Refresh",fn:YAHOO.GDMA.datagrid.refreshData,defaultMode:"show",tooltip:"refresh the data"},{name:"Filter",fn:YAHOO.GDMA.datagrid.filterRecords,defaultMode:"show",tooltip:"filter the data"},{name:"Clear",fn:YAHOO.GDMA.datagrid.clearFilter,defaultMode:"show",tooltip:"clear the filter"},{name:"Multi",fn:YAHOO.GDMA.datagrid.multiUpdate,defaultMode:"show",tooltip:"update multiple records"},{name:"Download",fn:YAHOO.GDMA.datagrid.downloadRecords,defaultMode:"show",tooltip:"download records"}];