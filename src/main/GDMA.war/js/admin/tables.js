// Create a namespace for this code
YAHOO.namespace("GDMA.admin.tables");

// define the datasource fields
YAHOO.GDMA.admin.tables.fields = {
      fields : [ 
                 {key:"id", parser:YAHOO.util.DataSource.parseNumber},
                 {key:"name", parser:YAHOO.util.DataSource.parseString}/*,
                 {key:"displayed"},
                 {key:"allowDelete"}*/
                 ]
};


// define the tables columns
YAHOO.GDMA.admin.tables.columnDefs = [ {
        label :"",
        formatter : YAHOO.GDMA.utilities.rownumFormatter,
        minWidth: 20
    },{
        key :"name",
        label :"Name",
        sortable:true,
        resizeable:true, 
        width:200
}];

YAHOO.GDMA.admin.tables.doubleClick = function(oArgs){
    var elTargetRow = YAHOO.GDMA.admin.dataTable.getTrEl(oArgs.target);
    YAHOO.GDMA.admin.dataTable.selectRow(elTargetRow);
    YAHOO.GDMA.admin.dataTable.focus();
    YAHOO.GDMA.admin.viewColumns();
};

YAHOO.GDMA.admin.tables.deleteRecord = function(){    
}

//Uncomment this method if moving resynch functionality to the Refresh button
//Resync and load the list of tables
/*YAHOO.GDMA.admin.tables.resynchTables = function(serverId, fnCallback ) {
  GdmaAdmin.resyncTableList( serverId, function(tables) {
      // setup the object backing the table
      var datasource = new YAHOO.util.DataSource(tables);
      datasource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;        
      // field names
      datasource.responseSchema = YAHOO.GDMA.admin.tables.fields;        
      datasource.columnDefs = YAHOO.GDMA.admin.tables.columnDefs;
      fnCallback(datasource);
  });
  // setup the functions need
  YAHOO.GDMA.admin.saveFunction =  GdmaAdmin.saveTables;
  YAHOO.GDMA.admin.deleteFunction = YAHOO.GDMA.admin.tables.deleteRecord;
  YAHOO.GDMA.admin.doubleClick = YAHOO.GDMA.admin.tables.doubleClick;
}*/

// Load the list of tables
YAHOO.GDMA.admin.tables.loadTables = function(serverId, fnCallback ) {
  GdmaAdmin.getTablesForServer( serverId, function(tables) {
      // setup the object backing the table
      var datasource = new YAHOO.util.DataSource(tables);
      datasource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;        
      // field names
      datasource.responseSchema = YAHOO.GDMA.admin.tables.fields;        
      datasource.columnDefs = YAHOO.GDMA.admin.tables.columnDefs;
      fnCallback(datasource);
  });
  // setup the functions need
  YAHOO.GDMA.admin.saveFunction =  GdmaAdmin.saveTables;
  YAHOO.GDMA.admin.deleteFunction = YAHOO.GDMA.admin.tables.deleteRecord;
  YAHOO.GDMA.admin.doubleClick = YAHOO.GDMA.admin.tables.doubleClick;
}

