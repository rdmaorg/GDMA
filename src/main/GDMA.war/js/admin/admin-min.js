YAHOO.namespace("GDMA.admin");
YAHOO.GDMA.admin.dwrErrorHandler=function(B,A){YAHOO.GDMA.admin.dwrLastException=A;
if(A&&A.mostSpecificCause){YAHOO.GDMA.dialog.showInfoDialog("Error!","1. An error occured<br/>"+A.mostSpecificCause.message,YAHOO.widget.SimpleDialog.ICON_BLOCK)
}else{YAHOO.GDMA.dialog.showInfoDialog("Error!","2. An error occured<br/>"+B,YAHOO.widget.SimpleDialog.ICON_BLOCK)
}};
YAHOO.GDMA.admin.refreshServers=function(){YAHOO.GDMA.admin.dataTable.cancelCellEditor();
YAHOO.GDMA.admin.updateList=[];
YAHOO.GDMA.admin.selectedServerID=null;
YAHOO.GDMA.admin.servers.loadServers(YAHOO.GDMA.admin.buildTable);
YAHOO.GDMA.toolbar.changeState("defaultMode");
YAHOO.GDMA.admin.refreshFunction=YAHOO.GDMA.admin.refreshServers;
YAHOO.GDMA.layout.getUnitByPosition("center").set("header","Admin - Servers")
};
YAHOO.GDMA.admin.refreshTables=function(){YAHOO.GDMA.admin.dataTable.cancelCellEditor();
YAHOO.GDMA.admin.updateList=[];
YAHOO.GDMA.admin.selectedTableID=null;
YAHOO.GDMA.admin.tables.loadTables(YAHOO.GDMA.admin.selectedServerID,YAHOO.GDMA.admin.buildTable);
YAHOO.GDMA.toolbar.changeState("tablesView");
YAHOO.GDMA.admin.refreshFunction=YAHOO.GDMA.admin.refreshTables;
YAHOO.GDMA.layout.getUnitByPosition("center").set("header","Admin - Tables for Server ["+YAHOO.GDMA.admin.selectedServerName+"]")
};
YAHOO.GDMA.admin.refreshColumns=function(){YAHOO.GDMA.admin.dataTable.cancelCellEditor();
YAHOO.GDMA.admin.updateList=[];
YAHOO.GDMA.admin.columns.loadColumns(YAHOO.GDMA.admin.selectedServerID,YAHOO.GDMA.admin.selectedTableID,YAHOO.GDMA.admin.buildTable);
YAHOO.GDMA.toolbar.changeState("columnsView");
YAHOO.GDMA.admin.refreshFunction=YAHOO.GDMA.admin.refreshColumns;
YAHOO.GDMA.layout.getUnitByPosition("center").set("header","Admin - Columns for table ["+YAHOO.GDMA.admin.selectedTableName+"]")
};
YAHOO.GDMA.admin.refreshAccess=function(){YAHOO.GDMA.admin.dataTable.cancelCellEditor();
YAHOO.GDMA.admin.updateList=[];
YAHOO.GDMA.admin.access.loadList(YAHOO.GDMA.admin.selectedTableID,YAHOO.GDMA.admin.selectedTableName,YAHOO.GDMA.admin.buildTable)
};
YAHOO.GDMA.admin.refreshUsers=function(){YAHOO.GDMA.admin.dataTable.cancelCellEditor();
YAHOO.GDMA.admin.updateList=[];
YAHOO.GDMA.admin.users.loadUsers(YAHOO.GDMA.admin.buildTable);
YAHOO.GDMA.toolbar.changeState("usersView");
YAHOO.GDMA.admin.refreshFunction=YAHOO.GDMA.admin.refreshUsers
};
YAHOO.GDMA.admin.highlightEditableCell=function(A){var B=A.target;
if(YAHOO.util.Dom.hasClass(B,"yui-dt-editable")){this.highlightCell(B)
}};
YAHOO.GDMA.admin.updateList=[];
YAHOO.GDMA.admin.saveRecord=function(){YAHOO.GDMA.admin.dataTable.cancelCellEditor();
if(!YAHOO.GDMA.admin.updateList||YAHOO.GDMA.admin.updateList.length>0){var A=function(){YAHOO.GDMA.admin.saveFunction(YAHOO.GDMA.admin.updateList,function(){YAHOO.GDMA.dialog.showInfoDialog("Saved!",YAHOO.GDMA.admin.updateList.length+" Record(s) saved!");
YAHOO.GDMA.admin.refreshFunction()
})
};
YAHOO.GDMA.dialog.showYesNoDialog(A,"Please confirm save","Are you sure you wish to save the "+YAHOO.GDMA.admin.updateList.length+" changed record(s)?<br/> This action will commit your changes to the database.")
}else{YAHOO.GDMA.dialog.showInfoDialog("Save Records","No changed have been made- there is nothing to save!")
}};
YAHOO.GDMA.admin.deleteRecord=function(){YAHOO.GDMA.admin.dataTable.cancelCellEditor();
var B=YAHOO.GDMA.admin.dataTable.getSelectedRows();
if(!B||B.length==0){YAHOO.GDMA.dialog.showInfoDialog("No record selected ...","Please select the record you wish to delete.")
}else{if(B.length>1){YAHOO.GDMA.dialog.showInfoDialog("Select a single record ...","Please select only ONE record for deletion.")
}else{var A=function(){var C=YAHOO.GDMA.admin.dataTable.getRecord(B[0]).getData();
YAHOO.GDMA.admin.deleteFunction(C,function(){YAHOO.GDMA.dialog.showInfoDialog("Deleted!","Record deleted!");
YAHOO.GDMA.admin.refreshFunction()
})
};
YAHOO.GDMA.dialog.showYesNoDialog(A,"Please confirm delete","Are you sure you wish to delete the selected record?<br/> This action will commit your changes to the database.")
}}};
YAHOO.GDMA.admin.addRecord=function(){YAHOO.GDMA.admin.dataTable.cancelCellEditor();
var B=YAHOO.GDMA.admin.dataTable.getSelectedRows();
if(B&&B.length!=0){var A=YAHOO.GDMA.admin.dataTable.getRecordIndex(YAHOO.GDMA.admin.dataTable.getRecord(YAHOO.GDMA.admin.dataTable.getRecord(B[0])));
YAHOO.GDMA.admin.dataTable.addRow({},A)
}else{YAHOO.GDMA.admin.dataTable.addRow({},0)
}YAHOO.GDMA.toolbar.changeState("editmode")
};
YAHOO.GDMA.admin.refreshRecords=function(){YAHOO.GDMA.admin.dataTable.cancelCellEditor();
YAHOO.GDMA.admin.refreshFunction()
};
YAHOO.GDMA.admin.buildTable=function(A){YAHOO.GDMA.admin.dataTable=new YAHOO.widget.DataTable("divServerTable",A.columnDefs,A);
YAHOO.GDMA.admin.dataTable.subscribe("cellMouseoverEvent",YAHOO.GDMA.admin.highlightEditableCell);
YAHOO.GDMA.admin.dataTable.subscribe("cellMouseoutEvent",YAHOO.GDMA.admin.dataTable.onEventUnhighlightCell);
YAHOO.GDMA.admin.dataTable.subscribe("cellClickEvent",YAHOO.GDMA.admin.dataTable.onEventShowCellEditor);
YAHOO.GDMA.admin.dataTable.subscribe("rowMouseoverEvent",YAHOO.GDMA.admin.dataTable.onEventHighlightRow);
YAHOO.GDMA.admin.dataTable.subscribe("rowMouseoutEvent",YAHOO.GDMA.admin.dataTable.onEventUnhighlightRow);
YAHOO.GDMA.admin.dataTable.subscribe("rowClickEvent",YAHOO.GDMA.admin.dataTable.onEventSelectRow);
YAHOO.GDMA.admin.dataTable.subscribe("rowDblclickEvent",YAHOO.GDMA.admin.doubleClick);
YAHOO.GDMA.admin.dataTable.selectRow(YAHOO.GDMA.admin.dataTable.getTrEl(0));
YAHOO.GDMA.admin.dataTable.focus();
YAHOO.GDMA.admin.addToUpdateList=function(D){for(var E=0;
E<YAHOO.GDMA.admin.updateList.length;
E++){if(YAHOO.GDMA.admin.updateList[E]===D.record.getData()){return 
}}YAHOO.GDMA.admin.updateList.push(D.record.getData());
YAHOO.GDMA.toolbar.changeState("editmode")
};
YAHOO.GDMA.admin.dataTable.subscribe("checkboxClickEvent",function(G){var F=G.target;
var E=this.getRecord(F);
var H=this.getColumn(F);
E.setData(H.key,F.checked);
var D={};
D.record=E;
YAHOO.GDMA.admin.addToUpdateList(D)
});
YAHOO.GDMA.admin.dataTable.subscribe("dropdownChangeEvent",function(F){var H=F.target;
var E=this.getRecord(H);
var G=this.getColumn(H);
E.setData(G.key,H.value);
var D={};
D.record=E;
YAHOO.GDMA.admin.addToUpdateList(D)
});
YAHOO.GDMA.admin.dataTable.subscribe("theadCheckboxClickEvent",function(E){var D=E.target;
if(D&&D.type=="checkbox"){if(D.checked){YAHOO.GDMA.admin.dataTable.selectAll(E)
}else{YAHOO.GDMA.admin.dataTable.unSelectAll(E)
}}});
YAHOO.GDMA.admin.dataTable.selectAll=function(E){var F=this.getColumn(E.target);
var D=YAHOO.GDMA.admin.dataTable.getRecordSet().getRecords();
for(C=0;
C<D.length;
C++){YAHOO.GDMA.admin.dataTable.getRecordSet().updateKey(D[C],F.key,true)
}YAHOO.GDMA.admin.dataTable.refreshView()
};
YAHOO.GDMA.admin.dataTable.unSelectAll=function(E){var F=this.getColumn(E.target);
var D=YAHOO.GDMA.admin.dataTable.getRecordSet().getRecords();
for(C=0;
C<D.length;
C++){YAHOO.GDMA.admin.dataTable.getRecordSet().updateKey(D[C],F.key,false)
}YAHOO.GDMA.admin.dataTable.refreshView()
};
YAHOO.GDMA.admin.dataTable.subscribe("editorUpdateEvent",function(D){if(D.editor.column.key==="active"){this.saveCellEditor()
}});
YAHOO.GDMA.admin.dataTable.subscribe("editorBlurEvent",function(D){this.cancelCellEditor()
});
YAHOO.GDMA.admin.dataTable.getRecordSet().subscribe("recordValueUpdateEvent",YAHOO.GDMA.admin.addToUpdateList);
YAHOO.GDMA.admin.tep=[];
var B=YAHOO.GDMA.admin.dataTable.getColumnSet().tree[0].length;
for(var C=0;
C<B;
C++){if(YAHOO.GDMA.admin.dataTable.getColumn(C).formatter=="checkbox"){YAHOO.GDMA.admin.tep[C]=YAHOO.GDMA.admin.dataTable.getColumn(C).getThEl()
}}YAHOO.GDMA.dialog.loading.hide()
};
YAHOO.GDMA.admin.viewTables=function(){YAHOO.GDMA.admin.dataTable.cancelCellEditor();
var A=YAHOO.GDMA.admin.dataTable.getSelectedRows();
if(YAHOO.GDMA.admin.selectedServerID){YAHOO.GDMA.admin.refreshTables()
}else{if(!A||A.length==0){YAHOO.GDMA.dialog.showInfoDialog("No server selected ...","Please select the server you wish view the tables for.")
}else{YAHOO.GDMA.admin.selectedServerID=YAHOO.GDMA.admin.dataTable.getRecord(A[0]).getData().id;
YAHOO.GDMA.admin.selectedServerName=YAHOO.GDMA.admin.dataTable.getRecord(A[0]).getData().name;
YAHOO.GDMA.admin.refreshTables()
}}};
YAHOO.GDMA.admin.viewColumns=function(){YAHOO.GDMA.admin.dataTable.cancelCellEditor();
var A=YAHOO.GDMA.admin.dataTable.getSelectedRows();
if(!A||A.length==0){YAHOO.GDMA.dialog.showInfoDialog("No table selected ...","Please select the table you wish view the columns for.")
}else{YAHOO.GDMA.admin.selectedTableID=YAHOO.GDMA.admin.dataTable.getRecord(A[0]).getData().id;
YAHOO.GDMA.admin.selectedTableName=YAHOO.GDMA.admin.dataTable.getRecord(A[0]).getData().name;
YAHOO.GDMA.admin.refreshColumns()
}};
YAHOO.GDMA.admin.viewPermissions=function(){YAHOO.GDMA.admin.dataTable.cancelCellEditor();
var A=YAHOO.GDMA.admin.dataTable.getSelectedRows();
if(!A||A.length==0){YAHOO.GDMA.dialog.showInfoDialog("No table selected ...","Please select the table you wish view the access rights  for.")
}else{YAHOO.GDMA.admin.selectedTableID=YAHOO.GDMA.admin.dataTable.getRecord(A[0]).getData().id;
YAHOO.GDMA.admin.selectedTableName=YAHOO.GDMA.admin.dataTable.getRecord(A[0]).getData().name;
YAHOO.GDMA.admin.refreshAccess()
}};
YAHOO.GDMA.admin.viewColumnConfig=function(){YAHOO.GDMA.admin.dataTable.cancelCellEditor();
YAHOO.GDMA.admin.updateList=[];
YAHOO.GDMA.admin.columnconfig.loadList(YAHOO.GDMA.admin.selectedServerID,YAHOO.GDMA.admin.selectedTableID,YAHOO.GDMA.admin.selectedTableName)
};
YAHOO.GDMA.admin.viewUsers=function(){YAHOO.GDMA.admin.dataTable.cancelCellEditor();
YAHOO.GDMA.admin.refreshUsers()
};
YAHOO.GDMA.admin.toolbarButtons=[{name:"Add",fn:YAHOO.GDMA.admin.addRecord,defaultMode:"show",usersView:"show",tooltip:"add a new server registration"},{name:"Delete",fn:YAHOO.GDMA.admin.deleteRecord,defaultMode:"hide",usersView:"hide",tooltip:"delete selected server registration"},{name:"Save",fn:YAHOO.GDMA.admin.saveRecord,editmode:"show",tooltip:"save updates or additions of server records"},{name:"Cancel",fn:YAHOO.GDMA.admin.refreshRecords,editmode:"show",tooltip:"cancel and discard current changes"},{name:"Refresh",fn:YAHOO.GDMA.admin.refreshRecords,defaultMode:"show",tablesView:"show",columnsView:"show",usersView:"show",tooltip:"refresh the list of servers"},{name:"Servers",fn:YAHOO.GDMA.admin.refreshServers,tablesView:"show",columnsView:"show",usersView:"show",tooltip:"view the list of server registrations"},{name:"Tables",fn:YAHOO.GDMA.admin.viewTables,defaultMode:"show",columnsView:"show",tooltip:"view the tables for the selected server"},{name:"Columns",fn:YAHOO.GDMA.admin.viewColumns,tablesView:"show",tooltip:"view the columns for the selected table"},{name:"Access",fn:YAHOO.GDMA.admin.viewPermissions,tablesView:"show",tooltip:"view the permissions for the selected table"},{name:"Users",fn:YAHOO.GDMA.admin.viewUsers,defaultMode:"show",tooltip:"view the list of users"},{name:"Config",fn:YAHOO.GDMA.admin.viewColumnConfig,columnsView:"show",tooltip:"Edit columns config"}];
YAHOO.GDMA.admin.init=function(){dwr.engine.setErrorHandler(YAHOO.GDMA.admin.dwrErrorHandler);
dwr.engine.setPreHook(function(){YAHOO.GDMA.dialog.loading.show()
});
dwr.engine.setPostHook(function(){setTimeout("YAHOO.GDMA.dialog.loading.hide();",5000)
});
YAHOO.GDMA.admin.servers.loadConnectionTypes();
YAHOO.GDMA.admin.servers.loadServers(YAHOO.GDMA.admin.buildTable);
YAHOO.GDMA.toolbar.createToolbar(YAHOO.GDMA.admin.toolbarButtons);
YAHOO.GDMA.admin.refreshFunction=YAHOO.GDMA.admin.refreshServers
};
YAHOO.util.Event.onDOMReady(function(){YAHOO.util.Event.onAvailable("divServerTable",YAHOO.GDMA.admin.init)
});