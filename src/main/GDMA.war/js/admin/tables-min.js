YAHOO.namespace("GDMA.admin.tables");
YAHOO.GDMA.admin.tables.fields={fields:[{key:"id",parser:YAHOO.util.DataSource.parseNumber},{key:"name",parser:YAHOO.util.DataSource.parseString},{key:"displayed"},{key:"allowDelete"}]};
YAHOO.GDMA.admin.tables.columnDefs=[{label:"",formatter:YAHOO.GDMA.utilities.rownumFormatter,minWidth:20},{key:"name",label:"Name",sortable:true,resizeable:true,width:200},{key:"displayed",label:YAHOO.GDMA.utilities.createCheckAllHeader("Displayed"),formatter:"checkbox",resizeable:true,width:85},{key:"allowDelete",label:YAHOO.GDMA.utilities.createCheckAllHeader("Allow Delete"),formatter:"checkbox",resizeable:true,width:85}];
YAHOO.GDMA.admin.tables.doubleClick=function(B){var A=YAHOO.GDMA.admin.dataTable.getTrEl(B.target);
YAHOO.GDMA.admin.dataTable.selectRow(A);
YAHOO.GDMA.admin.dataTable.focus();
YAHOO.GDMA.admin.viewColumns()
};
YAHOO.GDMA.admin.tables.deleteRecord=function(){};
YAHOO.GDMA.admin.tables.loadTables=function(A,B){GdmaAdmin.getTablesForServer(A,function(D){var C=new YAHOO.util.DataSource(D);
C.responseType=YAHOO.util.DataSource.TYPE_JSARRAY;
C.responseSchema=YAHOO.GDMA.admin.tables.fields;
C.columnDefs=YAHOO.GDMA.admin.tables.columnDefs;
B(C)
});
YAHOO.GDMA.admin.saveFunction=GdmaAdmin.saveTables;
YAHOO.GDMA.admin.deleteFunction=YAHOO.GDMA.admin.tables.deleteRecord;
YAHOO.GDMA.admin.doubleClick=YAHOO.GDMA.admin.tables.doubleClick
};