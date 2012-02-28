// Create a namespace for this code
YAHOO.namespace("GDMA.admin");
 
// Error handlers for DWR
YAHOO.GDMA.admin.dwrErrorHandler = function(message, exception) {
    YAHOO.GDMA.admin.dwrLastException = exception;
    if (exception && exception.mostSpecificCause) {
        YAHOO.GDMA.dialog.showInfoDialog("Error!", "1. An error occured<br/>" + exception.mostSpecificCause.message,
                YAHOO.widget.SimpleDialog.ICON_BLOCK);
    } else {
        YAHOO.GDMA.dialog.showInfoDialog("Error!", "2. An error occured<br/>" + message, YAHOO.widget.SimpleDialog.ICON_BLOCK);
    }
}

// Function to refresh the list of servers
YAHOO.GDMA.admin.refreshServers = function() {
    YAHOO.GDMA.admin.dataTable.cancelCellEditor();
    YAHOO.GDMA.admin.updateList = [];
    YAHOO.GDMA.admin.selectedServerID = null;
    YAHOO.GDMA.admin.servers.loadServers(YAHOO.GDMA.admin.buildTable);
    YAHOO.GDMA.toolbar.changeState("defaultMode");  
    YAHOO.GDMA.admin.refreshFunction = YAHOO.GDMA.admin.refreshServers;
    YAHOO.GDMA.layout.getUnitByPosition('center').set('header','Admin - Servers');
}


// Function to refresh the list of tables
YAHOO.GDMA.admin.refreshTables = function() {
    YAHOO.GDMA.admin.dataTable.cancelCellEditor();
    YAHOO.GDMA.admin.updateList = []; 
    YAHOO.GDMA.admin.selectedTableID = null;    
    YAHOO.GDMA.admin.tables.loadTables(YAHOO.GDMA.admin.selectedServerID, YAHOO.GDMA.admin.buildTable);
    YAHOO.GDMA.toolbar.changeState("tablesView");
    //uncomment the next line and comment the line after if moving resynch functionality to the Refresh button
    //YAHOO.GDMA.admin.refreshFunction = YAHOO.GDMA.admin.resynchTables;
    YAHOO.GDMA.admin.refreshFunction = YAHOO.GDMA.admin.refreshTables;
    YAHOO.GDMA.layout.getUnitByPosition('center').set('header','Admin - Tables for Server [' + YAHOO.GDMA.admin.selectedServerName + ']');
};

//Uncomment this method if moving resynch functionality to the Refresh button
//Function to resynch the list of tables with the DB
/*YAHOO.GDMA.admin.resynchTables = function() {
    YAHOO.GDMA.admin.dataTable.cancelCellEditor();
    YAHOO.GDMA.admin.updateList = []; 
    YAHOO.GDMA.admin.selectedTableID = null;    
    YAHOO.GDMA.admin.tables.resynchTables(YAHOO.GDMA.admin.selectedServerID, YAHOO.GDMA.admin.buildTable);
    YAHOO.GDMA.toolbar.changeState("tablesView");
    YAHOO.GDMA.admin.refreshFunction = YAHOO.GDMA.admin.resynchTables;
    YAHOO.GDMA.layout.getUnitByPosition('center').set('header','Admin - Tables for Server [' + YAHOO.GDMA.admin.selectedServerName + ']');
};*/


// Function to refresh the list of columns
YAHOO.GDMA.admin.refreshColumns = function() {
    YAHOO.GDMA.admin.dataTable.cancelCellEditor();
    YAHOO.GDMA.admin.updateList = [];   
    YAHOO.GDMA.admin.columns.loadColumns(YAHOO.GDMA.admin.selectedServerID, YAHOO.GDMA.admin.selectedTableID, YAHOO.GDMA.admin.buildTable);
    YAHOO.GDMA.toolbar.changeState("columnsView"); 
    YAHOO.GDMA.admin.refreshFunction = YAHOO.GDMA.admin.refreshColumns;
    YAHOO.GDMA.layout.getUnitByPosition('center').set('header','Admin - Columns for table [' + YAHOO.GDMA.admin.selectedTableName + ']');
};

//Function to refresh the access rights for a table
YAHOO.GDMA.admin.refreshAccess = function() {
    YAHOO.GDMA.admin.dataTable.cancelCellEditor();
    YAHOO.GDMA.admin.updateList = [];
    YAHOO.GDMA.admin.access.loadList(YAHOO.GDMA.admin.selectedTableID, YAHOO.GDMA.admin.selectedTableName, YAHOO.GDMA.admin.buildTable);
};    

//Function to refresh the list of users
YAHOO.GDMA.admin.refreshUsers = function() {
    YAHOO.GDMA.admin.dataTable.cancelCellEditor();
    YAHOO.GDMA.admin.updateList = [];
    YAHOO.GDMA.admin.users.loadUsers(YAHOO.GDMA.admin.buildTable);
    YAHOO.GDMA.toolbar.changeState("usersView");  
    YAHOO.GDMA.admin.refreshFunction = YAHOO.GDMA.admin.refreshUsers;    
}

// set up inline editing
YAHOO.GDMA.admin.highlightEditableCell = function(oArgs) {
    var elCell = oArgs.target;
    if (YAHOO.util.Dom.hasClass(elCell, "yui-dt-editable")) {
        this.highlightCell(elCell);
    }
};

// used to hold the rows that need to be updated or added
YAHOO.GDMA.admin.updateList = [];

// save the record
YAHOO.GDMA.admin.saveRecord = function() {
    YAHOO.GDMA.admin.dataTable.cancelCellEditor();
    // do we have items to save
    
    if (!YAHOO.GDMA.admin.updateList || YAHOO.GDMA.admin.updateList.length > 0) {
        // yes, so set up the callback function for the yes/no dialog
        var handleYes = function() {
            YAHOO.GDMA.admin.saveFunction(YAHOO.GDMA.admin.updateList, function() {
                YAHOO.GDMA.dialog.showInfoDialog("Saved!", YAHOO.GDMA.admin.updateList.length + " Record(s) saved!");
                    YAHOO.GDMA.admin.refreshFunction();
                });
        };
        YAHOO.GDMA.dialog.showYesNoDialog(handleYes, "Please confirm save", "Are you sure you wish to save the "
                + YAHOO.GDMA.admin.updateList.length
                + " changed record(s)?<br/> This action will commit your changes to the database.");
    } else {
        YAHOO.GDMA.dialog.showInfoDialog("Save Records", "No changed have been made- there is nothing to save!");
    }    
};

// Delete a record
YAHOO.GDMA.admin.deleteRecord = function() {
    YAHOO.GDMA.admin.dataTable.cancelCellEditor();
    var selectedRows = YAHOO.GDMA.admin.dataTable.getSelectedRows();

    if (!selectedRows || selectedRows.length == 0) {
        YAHOO.GDMA.dialog.showInfoDialog("No record selected ...", "Please select the record you wish to delete.")
    } else if (selectedRows.length > 1) {
        YAHOO.GDMA.dialog.showInfoDialog("Select a single record ...", "Please select only ONE record for deletion.")
    }else{
        var handleYes = function() {
            var deletedRecord =          YAHOO.GDMA.admin.dataTable.getRecord(selectedRows[0]).getData();
            YAHOO.GDMA.admin.deleteFunction(deletedRecord, function() {
                YAHOO.GDMA.dialog.showInfoDialog("Deleted!", "Record deleted!");
                YAHOO.GDMA.admin.refreshFunction();
            });
        };

        YAHOO.GDMA.dialog.showYesNoDialog(handleYes, "Please confirm delete",
                "Are you sure you wish to delete the selected record?"
                        + "<br/> This action will commit your changes to the database.");
    }
};

// Add a new server record
YAHOO.GDMA.admin.addRecord = function() {
    YAHOO.GDMA.admin.dataTable.cancelCellEditor();
    var selectedRows = YAHOO.GDMA.admin.dataTable.getSelectedRows();
    if (selectedRows && selectedRows.length != 0) {
        var idx = YAHOO.GDMA.admin.dataTable.getRecordIndex(YAHOO.GDMA.admin.dataTable
                .getRecord(YAHOO.GDMA.admin.dataTable.getRecord(selectedRows[0])));
        YAHOO.GDMA.admin.dataTable.addRow( {}, idx);
    } else {
        YAHOO.GDMA.admin.dataTable.addRow( {}, 0);
    }
    YAHOO.GDMA.toolbar.changeState("editmode");
};

// Refresh the current records
YAHOO.GDMA.admin.refreshRecords = function() {
    YAHOO.GDMA.admin.dataTable.cancelCellEditor();
    YAHOO.GDMA.admin.refreshFunction();
};


// This is the method responsible for drawing the table
YAHOO.GDMA.admin.buildTable = function(datasource) {

    // setup the datatable
    YAHOO.GDMA.admin.dataTable = new YAHOO.widget.DataTable("divServerTable", datasource.columnDefs,
            datasource);

    //CEll highlighting
    YAHOO.GDMA.admin.dataTable.subscribe("cellMouseoverEvent", YAHOO.GDMA.admin.highlightEditableCell);
    YAHOO.GDMA.admin.dataTable.subscribe("cellMouseoutEvent", YAHOO.GDMA.admin.dataTable.onEventUnhighlightCell);
    YAHOO.GDMA.admin.dataTable.subscribe("cellClickEvent", YAHOO.GDMA.admin.dataTable.onEventShowCellEditor);
    
    // Subscribe to events for row selection
    YAHOO.GDMA.admin.dataTable.subscribe("rowMouseoverEvent", YAHOO.GDMA.admin.dataTable.onEventHighlightRow);
    YAHOO.GDMA.admin.dataTable.subscribe("rowMouseoutEvent", YAHOO.GDMA.admin.dataTable.onEventUnhighlightRow);
    YAHOO.GDMA.admin.dataTable.subscribe("rowClickEvent", YAHOO.GDMA.admin.dataTable.onEventSelectRow);
    
    YAHOO.GDMA.admin.dataTable.subscribe("rowDblclickEvent", YAHOO.GDMA.admin.doubleClick);

    // Programmatically select the first row
    YAHOO.GDMA.admin.dataTable.selectRow(YAHOO.GDMA.admin.dataTable.getTrEl(0));
    // Programmatically bring focus to the instance so arrow selection works
    // immediately
    YAHOO.GDMA.admin.dataTable.focus();

    // this method will add a record to the list of updated records
    // "YAHOO.GDMA.admin.updateList" if necessary - updated
    YAHOO.GDMA.admin.addToUpdateList = function(args) {
        for ( var i = 0; i < YAHOO.GDMA.admin.updateList.length; i++) {
            if (YAHOO.GDMA.admin.updateList[i] === args.record.getData()) {
                return;
            }
        }
        YAHOO.GDMA.admin.updateList.push(args.record.getData());
        YAHOO.GDMA.toolbar.changeState("editmode");
    }
    
    YAHOO.GDMA.admin.dataTable.subscribe("checkboxClickEvent", function(oArgs){ 
        
        var elCheckbox = oArgs.target; 
        var oRecord = this.getRecord(elCheckbox);
        var oColumn = this.getColumn(elCheckbox);
        oRecord.setData(oColumn.key,elCheckbox.checked);
        var args = {};
        args.record = oRecord;
        YAHOO.GDMA.admin.addToUpdateList(args);
        
    }); 
    
    YAHOO.GDMA.admin.dataTable.subscribe("dropdownChangeEvent", function(oArgs){ 
        
        var elDropdown = oArgs.target; 
        var oRecord = this.getRecord(elDropdown);
        var oColumn = this.getColumn(elDropdown);
        oRecord.setData(oColumn.key,elDropdown.value);
        var args = {};
        args.record = oRecord;
        YAHOO.GDMA.admin.addToUpdateList(args);
        
    }); 
    
    YAHOO.GDMA.admin.dataTable.subscribe('theadCheckboxClickEvent', function(oArgs) {
        var elCheckBox = oArgs.target;
        if(elCheckBox && elCheckBox.type == "checkbox"){
           if(elCheckBox.checked){
               YAHOO.GDMA.admin.dataTable.selectAll(oArgs);
           }else{
               YAHOO.GDMA.admin.dataTable.unSelectAll(oArgs);
           }
        }
    });
      
    YAHOO.GDMA.admin.dataTable.selectAll = function (oArgs) {
        var oColumn = this.getColumn(oArgs.target);
        var records = YAHOO.GDMA.admin.dataTable.getRecordSet().getRecords();
        for (i=0; i < records.length; i++) {
           YAHOO.GDMA.admin.dataTable.getRecordSet().updateKey(records[i], oColumn.key, true);
        }
        YAHOO.GDMA.admin.dataTable.refreshView();
    }
   
    YAHOO.GDMA.admin.dataTable.unSelectAll = function (oArgs) {
        var oColumn = this.getColumn(oArgs.target);
        var records = YAHOO.GDMA.admin.dataTable.getRecordSet().getRecords();
        for (i=0; i < records.length; i++) {
           YAHOO.GDMA.admin.dataTable.getRecordSet().updateKey(records[i], oColumn.key, false);
        }
        YAHOO.GDMA.admin.dataTable.refreshView();
    }
    
    // Hook into custom event to customize save-flow of "radio" editor
    YAHOO.GDMA.admin.dataTable.subscribe("editorUpdateEvent", function(oArgs) {
        if(oArgs.editor.column.key === "active") {
            this.saveCellEditor();
        }
    });
    
    YAHOO.GDMA.admin.dataTable.subscribe("editorBlurEvent", function(oArgs) {
        this.cancelCellEditor();
    });

    YAHOO.GDMA.admin.dataTable.getRecordSet().subscribe("recordValueUpdateEvent", YAHOO.GDMA.admin.addToUpdateList);
    YAHOO.GDMA.admin.tep=[]
    var colLength = YAHOO.GDMA.admin.dataTable.getColumnSet().tree[0].length;
    for(var i =0; i < colLength; i++){
        if(YAHOO.GDMA.admin.dataTable.getColumn(i).formatter == "checkbox"){
            YAHOO.GDMA.admin.tep[i] = YAHOO.GDMA.admin.dataTable.getColumn(i).getThEl();
        }
    }
    YAHOO.GDMA.dialog.loading.hide();
};

// Drill down to the tables
YAHOO.GDMA.admin.viewTables = function() {
    YAHOO.GDMA.admin.dataTable.cancelCellEditor();
    var selectedRows = YAHOO.GDMA.admin.dataTable.getSelectedRows();
    if(YAHOO.GDMA.admin.selectedServerID){ // server already selected
        YAHOO.GDMA.admin.refreshTables();        
    }else{
        if (!selectedRows || selectedRows.length == 0) {
            YAHOO.GDMA.dialog.showInfoDialog("No server selected ...", "Please select the server you wish view the tables for.")
        } else {
            YAHOO.GDMA.admin.selectedServerID = YAHOO.GDMA.admin.dataTable.getRecord(selectedRows[0]).getData().id;        
            YAHOO.GDMA.admin.selectedServerName = YAHOO.GDMA.admin.dataTable.getRecord(selectedRows[0]).getData().name;
            YAHOO.GDMA.admin.refreshTables();            
        }
    }
};

// Drill down to the columns
YAHOO.GDMA.admin.viewColumns = function() {
    YAHOO.GDMA.admin.dataTable.cancelCellEditor();
    var selectedRows = YAHOO.GDMA.admin.dataTable.getSelectedRows();

    if (!selectedRows || selectedRows.length == 0) {
        YAHOO.GDMA.dialog.showInfoDialog("No table selected ...", "Please select the table you wish view the columns for.")
    } else {
        YAHOO.GDMA.admin.selectedTableID = YAHOO.GDMA.admin.dataTable.getRecord(selectedRows[0]).getData().id; 
        YAHOO.GDMA.admin.selectedTableName = YAHOO.GDMA.admin.dataTable.getRecord(selectedRows[0]).getData().name;
        YAHOO.GDMA.admin.refreshColumns();  
    }
};

// View permissions for selected table
YAHOO.GDMA.admin.viewPermissions = function() {
    YAHOO.GDMA.admin.dataTable.cancelCellEditor();
    var selectedRows = YAHOO.GDMA.admin.dataTable.getSelectedRows();

    if (!selectedRows || selectedRows.length == 0) {
        YAHOO.GDMA.dialog.showInfoDialog("No table selected ...", "Please select the table you wish view the access rights  for.")
    } else {
        YAHOO.GDMA.admin.selectedTableID = YAHOO.GDMA.admin.dataTable.getRecord(selectedRows[0]).getData().id;        
        YAHOO.GDMA.admin.selectedTableName = YAHOO.GDMA.admin.dataTable.getRecord(selectedRows[0]).getData().name;
        YAHOO.GDMA.admin.refreshAccess();        
    }
};

//View permissions for selected table
YAHOO.GDMA.admin.viewColumnConfig = function() {
    YAHOO.GDMA.admin.dataTable.cancelCellEditor();
    YAHOO.GDMA.admin.updateList = [];
    YAHOO.GDMA.admin.columnconfig.loadList(YAHOO.GDMA.admin.selectedServerID, YAHOO.GDMA.admin.selectedTableID, YAHOO.GDMA.admin.selectedTableName);        
};

//View teh list of users
YAHOO.GDMA.admin.viewUsers = function() {
    YAHOO.GDMA.admin.dataTable.cancelCellEditor();
    YAHOO.GDMA.admin.refreshUsers();        
};

//each button has a view and an edit mode
//the default mode ofr each button is hide
// set up the default set of buttons - the other script files may update this
YAHOO.GDMA.admin.toolbarButtons =  [{
            name: "Add",
            fn: YAHOO.GDMA.admin.addRecord,
            defaultMode: "show",
            usersView: "show",
            tooltip: "add a new server registration"
        },{
            name: "Delete",
            fn: YAHOO.GDMA.admin.deleteRecord,
            defaultMode: "hide",
            usersView: "hide",
            tooltip: "delete selected server registration"
        },{
            name: "Save",
            fn: YAHOO.GDMA.admin.saveRecord,
            editmode: "show",
            tooltip: "save updates or additions of server records"
        },{
            name: "Cancel",
            fn: YAHOO.GDMA.admin.refreshRecords,
            editmode: "show",
            tooltip: "cancel and discard current changes"
        },{
            name: "Refresh",
            fn: YAHOO.GDMA.admin.refreshRecords,
            defaultMode: "show",
            tablesView: "show",
            columnsView: "show",
            usersView: "show",
            tooltip: "refresh the list of records"
        },{
            name: "Servers",
            fn: YAHOO.GDMA.admin.refreshServers, 
            tablesView: "show",
            columnsView: "show",
            usersView: "show",
            tooltip: "view the list of server registrations"
        },{
            name: "Tables",
            fn: YAHOO.GDMA.admin.viewTables, 
            defaultMode: "show",
            columnsView: "show",
            tooltip: "view the tables for the selected server"
        },{
            name: "Columns",
            fn: YAHOO.GDMA.admin.viewColumns, 
            tablesView: "show",
            tooltip: "view the columns for the selected table"
        },{
            name: "Access",
            fn: YAHOO.GDMA.admin.viewPermissions, 
            tablesView: "show",
            tooltip: "view the permissions for the selected table"
        },{
            name: "Users",
            fn: YAHOO.GDMA.admin.viewUsers, 
            defaultMode: "show",
            tooltip: "view the list of users"
        },{
            name: "Config",
            fn: YAHOO.GDMA.admin.viewColumnConfig, 
            columnsView: "show",
            tooltip: "Edit columns config"
        }];     

// Init functon for this page
YAHOO.GDMA.admin.init = function() {
    //YAHOO.GDMA.dialog.loading.show();
    dwr.engine.setErrorHandler(YAHOO.GDMA.admin.dwrErrorHandler);
    dwr.engine.setPreHook(function() {
    	YAHOO.GDMA.dialog.loading.show();
	});
	dwr.engine.setPostHook(function() {
		setTimeout("YAHOO.GDMA.dialog.loading.hide();",5000);
	});
    YAHOO.GDMA.admin.servers.loadConnectionTypes();
    YAHOO.GDMA.admin.servers.loadServers(YAHOO.GDMA.admin.buildTable);
    YAHOO.GDMA.toolbar.createToolbar(YAHOO.GDMA.admin.toolbarButtons);
    YAHOO.GDMA.admin.refreshFunction = YAHOO.GDMA.admin.refreshServers;
}

// event which kicks it all off
YAHOO.util.Event.onDOMReady(function() {
    YAHOO.util.Event.onAvailable('divServerTable', YAHOO.GDMA.admin.init);
});
