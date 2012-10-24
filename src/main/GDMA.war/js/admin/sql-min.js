YAHOO.namespace("GDMA.sql");
YAHOO.util.Event.onDOMReady(function(){YAHOO.util.Event.onAvailable("yui-gen8",function(){var A=YAHOO.GDMA.layout.getUnitByPosition("center").body;
YAHOO.GDMA.sql.layout=new YAHOO.widget.Layout(A,{units:[{position:"top",height:"28px",resize:false,body:"divToolbar",gutter:"0px 0px 0px 0px",scroll:false},{position:"bottom",height:"30px",resize:false,body:"divPager",gutter:"0px 0px 0px 0px",scroll:false},{position:"center",body:"divDataTable",gutter:"0px 0px 0px 0px",scroll:true}]});
YAHOO.GDMA.sql.layout.render()
})
});
YAHOO.GDMA.sql.dwrErrorHandler=function(B,A){YAHOO.GDMA.sql.dwrLastException=A;
if(A&&A.mostSpecificCause){YAHOO.GDMA.dialog.showInfoDialog("Error!","1. An error occured<br/>"+A.mostSpecificCause.message,YAHOO.widget.SimpleDialog.ICON_BLOCK)
}else{YAHOO.GDMA.dialog.showInfoDialog("Error!","2. An error occured<br/>"+B,YAHOO.widget.SimpleDialog.ICON_BLOCK)
}};
YAHOO.GDMA.sql.refreshRecords=function(){YAHOO.GDMA.sql.dataTable.cancelCellEditor();
YAHOO.GDMA.sql.refreshFunction()
};
YAHOO.GDMA.sql.buildTable=function(A){YAHOO.GDMA.sql.dataTable=new YAHOO.widget.DataTable("divServerTable",A.columnDefs,A,{initialRequest:YAHOO.GDMA.sql.paginatedSqlRequest,paginationEventHandler:YAHOO.GDMA.sql.handlePagination,paginator:YAHOO.GDMA.sql.paginator});
YAHOO.GDMA.dialog.loading.hide()
};
YAHOO.GDMA.sql.onExecute=function(){var B=YAHOO.util.Dom.get("selServers");
var C=YAHOO.util.Dom.get("txtSqlEditor");
var A=B.value;
var D=YAHOO.lang.trim(C.value);
if(!B.selectedIndex>0){YAHOO.GDMA.dialog.showInfoDialog("Error!","Please select a server first",YAHOO.widget.SimpleDialog.ICON_BLOCK);
return 
}if(D==""){YAHOO.GDMA.dialog.showInfoDialog("Error!","Please enter a valid select query",YAHOO.widget.SimpleDialog.ICON_BLOCK);
return 
}if(D.substring(0,6).toUpperCase()!="SELECT"){YAHOO.GDMA.dialog.showInfoDialog("Invalid operation!","This console is designed for SQL SELECT statements. Please use the GUI on the GDMA tab for SQL INSERTS, UPDATES or DELETES.",YAHOO.widget.SimpleDialog.ICON_BLOCK);
return 
}if(YAHOO.GDMA.sql.dataTable){YAHOO.GDMA.sql.dataTable.destroy()
}if(YAHOO.GDMA.sql.paginator){YAHOO.GDMA.sql.paginator.destroy()
}YAHOO.GDMA.sql.paginatedSqlRequest.sql=D;
YAHOO.GDMA.sql.paginatedSqlRequest.serverId=A;
YAHOO.GDMA.sql.createDatasource(A,D)
};
YAHOO.GDMA.sql.createServerDropDown=function(B){var A=YAHOO.util.Dom.get("selServers");
YAHOO.GDMA.utilities.populateDropDown(A,B,"id","name",-1,true)
};
YAHOO.GDMA.sql.createDatasource=function(A,B){GdmaAdmin.executeSelectGetColumns(A,B,function(D){YAHOO.GDMA.sql.responseSchema={resultsList:"records",fields:[]};
YAHOO.GDMA.sql.columnDefs=[];
YAHOO.GDMA.sql.responseSchema.fields.push({key:"rowid"});
for(var C=0;
C<D.length;
C++){YAHOO.GDMA.sql.responseSchema.fields.push({key:""+D[C].id})
}YAHOO.GDMA.sql.columnDefs.push({key:"rowid",label:"",minWidth:20});
for(var C=0;
C<D.length;
C++){var F=""+D[C].id;
var E={key:""+D[C].id,label:D[C].name,resizeable:true};
YAHOO.GDMA.sql.columnDefs.push(E)
}YAHOO.GDMA.sql.datasource=new YAHOO.util.DataSource(GdmaAdmin.executeSelect);
YAHOO.GDMA.sql.datasource.responseType=YAHOO.util.DataSource.TYPE_JSON;
YAHOO.GDMA.sql.datasource.responseSchema=YAHOO.GDMA.sql.responseSchema;
YAHOO.GDMA.sql.datasource.columnDefs=YAHOO.GDMA.sql.columnDefs;
YAHOO.GDMA.sql.buildTable(YAHOO.GDMA.sql.datasource)
})
};
YAHOO.GDMA.sql.init=function(){dwr.engine.setErrorHandler(YAHOO.GDMA.sql.dwrErrorHandler);
YAHOO.GDMA.toolbar.createToolbar(YAHOO.GDMA.sql.toolbarButtons);
YAHOO.GDMA.sql.refreshFunction=YAHOO.GDMA.sql.refreshServers;
GdmaAdmin.getServers(function(A){YAHOO.GDMA.sql.createServerDropDown(A)
});
YAHOO.GDMA.sql.paginatedSqlRequest={serverId:"",rowsPerPage:1000,recordOffset:0,sql:""};
if(YAHOO.GDMA.sql.paginator){YAHOO.GDMA.sql.paginator.destroy()
}YAHOO.GDMA.sql.paginator=new YAHOO.widget.Paginator({containers:["divPager"],pageLinks:10,rowsPerPage:25,rowsPerPageOptions:[25,50,100,1000],template:"<strong>{CurrentPageReport}</strong>{PreviousPageLink} {PageLinks} {NextPageLink} {RowsPerPageDropdown}",pageReportTemplate:"(page {currentPage} of {totalPages}, total records {totalRecords})"});
YAHOO.GDMA.toolbar.createToolbar(YAHOO.GDMA.sql.toolbarButtons)
};
YAHOO.util.Event.onDOMReady(function(){YAHOO.util.Event.onAvailable("divServerTable",YAHOO.GDMA.sql.init)
});
YAHOO.GDMA.sql.toolbarButtons=[{name:"Execute",fn:YAHOO.GDMA.sql.onExecute,defaultMode:"show",usersView:"show",tooltip:"execute the SQL in the console"}];