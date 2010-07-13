// Create a namespace for this code
YAHOO.namespace("GDMA.datagrid");
 
// layout the page
YAHOO.util.Event.onDOMReady( function(){
    //this is a bit dodgey! - look for something better than yui-gen8
    YAHOO.util.Event.onAvailable("yui-gen8", function() {
        var center = YAHOO.GDMA.layout.getUnitByPosition('center').body;
        YAHOO.GDMA.datagrid.layout = new YAHOO.widget.Layout(center,{
            units: [
                { position: 'top', height: '28px', resize: false, body: 'divToolbar',gutter: '0px 0px 0px 0px',scroll: false },
                { position: 'bottom', height: '30px', resize: false, body: 'divPager', gutter: '0px 0px 0px 0px', scroll: false},
                { position: 'center', body: 'divDataTable', gutter: '0px 0px 0px 0px', scroll: true }
            ]
        });
        YAHOO.GDMA.datagrid.layout.render();
    });
});
// end layout

//Error handlers for DWR
YAHOO.GDMA.datagrid.dwrErrorHandler = function(message, exception) {
    YAHOO.GDMA.dialog.loading.hide();
    YAHOO.GDMA.datagrid.dwrLastException = exception;
    if (exception && exception.mostSpecificCause) {
        YAHOO.GDMA.dialog.showInfoDialog("Error!", "An error occured.<br/>Exception: " + exception.mostSpecificCause.message,YAHOO.widget.SimpleDialog.ICON_BLOCK);
    } else if (exception) {
        YAHOO.GDMA.dialog.showInfoDialog("Error!", "An error occured<br/>Exception: " + exception.message, YAHOO.widget.SimpleDialog.ICON_BLOCK);
    }else {
        YAHOO.GDMA.dialog.showInfoDialog("Error!", "An error occured<br/>Message: " + message, YAHOO.widget.SimpleDialog.ICON_BLOCK);
    }
};

// set up inline editing
YAHOO.GDMA.datagrid.highlightEditableCell = function(oArgs) {
    var elCell = oArgs.target;
    if (YAHOO.util.Dom.hasClass(elCell, "yui-dt-editable")) {
        this.highlightCell(elCell);
    }
};

// replace DropDown id's with values
YAHOO.GDMA.datagrid.formatDropdown = function(elCell, oRecord, oColumn, oData) {
    var columnId = oColumn.getKey();
    
    var lookups = YAHOO.GDMA.datagrid.ddColumns[columnId];
    
    if (lookups && oData) {
         //in-efficient but will do for now
         for(var i = 0; i < lookups.length; i++){
             if(lookups[i][1] == oData){
                 elCell.innerHTML = lookups[i][2];
                 break;
             }
         }
     } else {
         elCell.innerHTML = "...";
     }
};

//Need a custom edit drop down to deal with referenced values
YAHOO.GDMA.datagrid.editDropdown = function(oEditor, oSelf) {
    var elCell = oEditor.cell;
    var oRecord = oEditor.record;
    var oColumn = oEditor.column;
    var elContainer = oEditor.container;
    var value = oEditor.value ? oEditor.value : -1;
    var columnId = oColumn.getKey();

    var elDropdown = elContainer.appendChild(document.createElement("select"));
    YAHOO.GDMA.utilities.populateDropDown(
            elDropdown, 
            YAHOO.GDMA.datagrid.ddColumns[columnId], 
            1, 
            2, 
            value, 
            true);
        
    // set up a listener to track the input value
    YAHOO.util.Event.addListener(elDropdown, "change", function() {
        oSelf._oCellEditor.value = elDropdown.options[elDropdown.selectedIndex].value;
        oSelf.fireEvent("editorUpdateEvent", {
            editor :oSelf._oCellEditor
        });
    });

    // Focus the dropdown
    oSelf._focusEl(elDropdown);
};

YAHOO.GDMA.datagrid.doFilter = function(){
    YAHOO.GDMA.datagrid.paginatedRequest.filters = [];
    var columns = YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
    var idx = 0;
    for(var i = 0; i < columns.length; i++){
        if(columns[i].displayed == true){
            var columnValue;
            
            var chkIsNull = document.getElementById("chkIsNull" + columns[i].id);
            var chkIsBlank = document.getElementById("chkIsBlank" + columns[i].id);
            
            if(columns[i].dropDownColumnDisplay && columns[i].dropDownColumnStore){
                var selFilter = document.getElementById("selFilter" + columns[i].id);
                var selectedIndex = selFilter.selectedIndex;
                if(selectedIndex < 1){                     
                    columnValue = null;
                }else{
                    columnValue = selFilter.options[selFilter.selectedIndex].value;
                }
            }else{
                var txtFilter = document.getElementById("txtFilter" + columns[i].id);
                columnValue = txtFilter.value;
            }
   
            if(  (columnValue && columnValue != '') || chkIsNull.checked || chkIsBlank.checked){
                YAHOO.GDMA.datagrid.paginatedRequest.filters[idx] = {};
                YAHOO.GDMA.datagrid.paginatedRequest.filters[idx].columnId = columns[i].id;
                YAHOO.GDMA.datagrid.paginatedRequest.filters[idx].columnName = columns[i].name;
                YAHOO.GDMA.datagrid.paginatedRequest.filters[idx].columnType = columns[i].columnType;
                YAHOO.GDMA.datagrid.paginatedRequest.filters[idx].filterValue = columnValue;
                YAHOO.GDMA.datagrid.paginatedRequest.filters[idx].nullValue = chkIsNull.checked;
                YAHOO.GDMA.datagrid.paginatedRequest.filters[idx].blank = chkIsBlank.checked;
                idx++;
            }
        }
    }
    YAHOO.GDMA.datagrid.paginatedRequest.recordOffset = 0;
    YAHOO.GDMA.datagrid.popFormPanel.destroy();    
    YAHOO.GDMA.datagrid.refreshData();
};


// save the record
YAHOO.GDMA.datagrid.updateRecords = function() {
    YAHOO.GDMA.datagrid.dataTable.cancelCellEditor();
    // do we have items to save
    
    if (!YAHOO.GDMA.datagrid.updateList || YAHOO.GDMA.datagrid.updateList.length > 0) {
        // yes, so set up the callback function for the yes/no dialog
        var updateCount = YAHOO.GDMA.datagrid.updateList.length;
        var handleYes = function() {
            
            var updateRequest = {
                    serverId: YAHOO.GDMA.datagrid.paginatedRequest.serverId,
                    tableId: YAHOO.GDMA.datagrid.paginatedRequest.tableId,
                    updates: []
            }
            var columns = YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
            for(var j = 0; j <  updateCount; j++){
                var newValues = YAHOO.GDMA.datagrid.updateList[j].newValues;
                var oldValues = YAHOO.GDMA.datagrid.updateList[j].oldValues;
                
                var updateRecord = []; 
                
                for(var id in newValues){
                    if(id != "rowid"){ //everything except dummy rowid
                        if(YAHOO.lang.isUndefined(oldValues[id])){
                            updateRecord.push({
                                columnId: id, 
                                oldColumnValue: newValues[id] 
                            });
                        }else{
                            updateRecord.push({
                                columnId: id, 
                                newColumnValue: newValues[id], 
                                oldColumnValue: oldValues[id] 
                            }); 
                        }
                    }
                }
                updateRequest.updates.push(updateRecord);
            }
            YAHOO.GDMA.datagrid.updateRequest = updateRequest;
            GdmaAjax.updateRecords(updateRequest, function(countUpdated) {
                YAHOO.GDMA.dialog.showInfoDialog("Updated!", countUpdated + " Record" +(countUpdated>1?"s":"") +" updated");
                YAHOO.GDMA.datagrid.refreshData();
            });
        };
        YAHOO.GDMA.dialog.showYesNoDialog(handleYes, "Please confirm save", "Are you sure you wish to save the "
                + updateCount
                + " changed record(s)?<br/> This action will commit your changes to the database.");
    } else {
        YAHOO.GDMA.dialog.showInfoDialog("Updated Records", "No changed have been made- there is nothing to save!");
    }    
};

// Delete a record
YAHOO.GDMA.datagrid.deleteRecord = function() {
    YAHOO.GDMA.datagrid.dataTable.cancelCellEditor();
    var selectedRows = YAHOO.GDMA.datagrid.dataTable.getSelectedRows();

    if (!selectedRows || selectedRows.length == 0) {
        YAHOO.GDMA.dialog.showInfoDialog("No record selected ...", "Please select the record you wish to delete.")
    } else{
        var handleYes = function() {
            var updateRequest = {
                    serverId: YAHOO.GDMA.datagrid.paginatedRequest.serverId,
                    tableId: YAHOO.GDMA.datagrid.paginatedRequest.tableId,
                    updates: []
            }
            var columns = YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
            for(var j = 0; j <  selectedRows.length; j++){
                var oData = YAHOO.GDMA.datagrid.dataTable.getRecord(selectedRows[j]).getData();            
                var deleteRecord = []; 
                
                // find all the primary key values
                for(var i = 0; i < columns.length; i++){
                    if(columns[i].primarykey == true){
                        deleteRecord.push({columnId: columns[i].id, oldColumnValue: oData[columns[i].id]});
                    }
                }
                updateRequest.updates.push(deleteRecord);
            }
            YAHOO.GDMA.datagrid.updateRequest = updateRequest;
            GdmaAjax.deleteRecords(updateRequest, function(countUpdated) {
                YAHOO.GDMA.dialog.showInfoDialog("Deleted!", countUpdated + " Record" +(countUpdated>1?"s":"") +" deleted");
                YAHOO.GDMA.datagrid.refreshData();
            });
        };

        YAHOO.GDMA.dialog.showYesNoDialog(handleYes, "Please confirm delete",
                "Are you sure you wish to delete the selected " + selectedRows.length + " record(s)?"
                        + "<br/> This action will commit your changes to the database.");
    }
     
};

// function used to intercept pagination requests
YAHOO.GDMA.datagrid.handlePagination = function (state,datatable) {
    if(state.rowsPerPage == 1){
        state.rowsPerPage = state.totalRecords + 100;
        state.recordOffset = 0;
    }
    YAHOO.GDMA.datagrid.paginatedRequest.rowsPerPage = state.rowsPerPage;
    YAHOO.GDMA.datagrid.paginatedRequest.recordOffset = state.recordOffset;
    
    YAHOO.GDMA.datagrid.refreshData(state);
};

YAHOO.GDMA.datagrid.resetSortTitle = function(oColumn,dir){
    try{
        var elTh = oColumn.getThEl();
        var a = elTh.getElementsByTagName("a")[0];
        a.title = "Click to sort " + (dir == YAHOO.widget.DataTable.CLASS_ASC ? "descending":"ascending");
    }catch(e){}
};

// function used to intercept sorting requests
YAHOO.GDMA.datagrid.handleSorting = function (oColumn) {
    var dir = YAHOO.widget.DataTable.CLASS_DESC;
    // If already sorted, sort in opposite direction
    if(oColumn.key === this.get("sortedBy").key) {
        dir = (this.get("sortedBy").dir === YAHOO.widget.DataTable.CLASS_ASC  ?  YAHOO.widget.DataTable.CLASS_DESC : YAHOO.widget.DataTable.CLASS_ASC); 
    }
    YAHOO.GDMA.datagrid.paginatedRequest.sortedByColumnId = "" + oColumn.key;
    YAHOO.GDMA.datagrid.paginatedRequest.dir = dir;

    YAHOO.GDMA.datagrid.refreshData();

    // reset title - seems to be a bug in YUI
    YAHOO.GDMA.datagrid.resetSortTitle(oColumn, dir);
};

YAHOO.GDMA.datagrid.onDataReturnInitializeTable = function(sRequest, oResponse, oPayload) {
    try{
        YAHOO.GDMA.datagrid.dataTable.onDataReturnInitializeTable(sRequest, oResponse, oPayload);
    }catch(e){}
    YAHOO.GDMA.dialog.loading.hide();
}

YAHOO.GDMA.datagrid.refreshData = function (state) {
    YAHOO.GDMA.dialog.loading.show();
    YAHOO.GDMA.datagrid.updateList = [];
    var callback = {
            success  : YAHOO.GDMA.datagrid.onDataReturnInitializeTable,
            failure  : YAHOO.GDMA.datagrid.onDataReturnInitializeTable,
            scope: YAHOO.GDMA.datagrid.dataTable,
            argument: 
            {
                startIndex : YAHOO.GDMA.datagrid.paginatedRequest.recordOffset,
                pagination: YAHOO.GDMA.datagrid.currentState,
                sorting: 
                {
                    key: YAHOO.GDMA.datagrid.paginatedRequest.sortedByColumnId,
                    dir: YAHOO.GDMA.datagrid.paginatedRequest.dir
                }
            }
    };
    if(state){
        callback.argument.pagination = state;
    }
    
    YAHOO.GDMA.datagrid.datasource.sendRequest(YAHOO.GDMA.datagrid.paginatedRequest,callback);
    YAHOO.GDMA.toolbar.changeState("defaultMode");
};

// This is the method responsible for drawing the table
YAHOO.GDMA.datagrid.buildTable = function(datasource) {

	// Update the table Title
    if (null != YAHOO.GDMA.datagrid.currentDataDescription)
    {
	    var server = YAHOO.GDMA.datagrid.currentDataDescription.name;
	    var selectedTable = "";
	    
	    if (YAHOO.GDMA.datagrid.currentDataDescription.tables.length >= 1 && 
	    	null != YAHOO.GDMA.datagrid.currentDataDescription.tables[0].name)
	    	selectedTable = YAHOO.lang.trim(YAHOO.GDMA.datagrid.currentDataDescription.tables[0].name);
	    
	    YAHOO.GDMA.layout.getUnitByPosition('center').set('header','Database[ ' + server + ' ]  -  Table[ ' + selectedTable + ' ]');
    }
    
    // setup the datatable
    YAHOO.GDMA.datagrid.dataTable = new YAHOO.widget.DataTable(
            "divDataTable", 
            datasource.columnDefs,
            datasource, 
            {
                    initialRequest         : YAHOO.GDMA.datagrid.paginatedRequest,
                    paginationEventHandler : YAHOO.GDMA.datagrid.handlePagination,
                    paginator              : YAHOO.GDMA.datagrid.paginator,
                    sortedBy               : {
                                                key:    ("" + YAHOO.GDMA.datagrid.paginatedRequest.sortedByColumnId), 
                                                dir:    YAHOO.widget.DataTable.CLASS_DESC
                                            }
            });
    
    YAHOO.GDMA.datagrid.dataTable.subscribe('theadCellClickEvent', YAHOO.GDMA.datagrid.dataTable.onEventSortColumn);
    YAHOO.GDMA.datagrid.dataTable.sortColumn = YAHOO.GDMA.datagrid.handleSorting;


    //CEll highlighting
    YAHOO.GDMA.datagrid.dataTable.subscribe("cellMouseoverEvent", YAHOO.GDMA.datagrid.highlightEditableCell);
    YAHOO.GDMA.datagrid.dataTable.subscribe("cellMouseoutEvent", YAHOO.GDMA.datagrid.dataTable.onEventUnhighlightCell);
    YAHOO.GDMA.datagrid.dataTable.subscribe("cellClickEvent", YAHOO.GDMA.datagrid.dataTable.onEventShowCellEditor);
    
    // Subscribe to events for row selection
    YAHOO.GDMA.datagrid.dataTable.subscribe("rowMouseoverEvent", YAHOO.GDMA.datagrid.dataTable.onEventHighlightRow);
    YAHOO.GDMA.datagrid.dataTable.subscribe("rowMouseoutEvent", YAHOO.GDMA.datagrid.dataTable.onEventUnhighlightRow);
    YAHOO.GDMA.datagrid.dataTable.subscribe("rowClickEvent", YAHOO.GDMA.datagrid.dataTable.onEventSelectRow);
    
    
    YAHOO.GDMA.datagrid.dataTable.subscribe("columnResizeEvent", function(oArgs){
        var oColumn = this.getColumn(oArgs.target);
        var newWidth = oArgs.width;
        var columns = YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
        var idx = oColumn.getIndex() - 1;
        if(columns[idx].maxWidth && columns[idx].maxWidth > 0 && newWidth > columns[idx].maxWidth ){
            this.setColumnWidth(oColumn, columns[idx].maxWidth);
        }
    });
    
    // select the first row and
    // bring focus to the instance so arrow selection works immediately    
    YAHOO.GDMA.datagrid.dataTable.selectRow(YAHOO.GDMA.datagrid.dataTable.getTrEl(0));
    YAHOO.GDMA.datagrid.dataTable.focus();

    // this method will add a record to the list of updated records
    // "YAHOO.GDMA.datagrid.updateList" if necessary - updated
    YAHOO.GDMA.datagrid.addToUpdateList = function(args) {
        var oData = args.record.getData();
        var key = args.key;
        var oldValue = args.oldData;
        var newValue = args.newData;
        
        for ( var i = 0; i < YAHOO.GDMA.datagrid.updateList.length; i++) {
            if (YAHOO.GDMA.datagrid.updateList[i].newValues === oData) {
                //just need to add old value if needed
                if(!YAHOO.GDMA.datagrid.updateList[i].oldValues[key]){
                    YAHOO.GDMA.datagrid.updateList[i].oldValues[key] = oldValue;
                }
                return;
            }
        }
        
        // adding a new record to the update list
        var  update = {
                newValues: oData,
                oldValues: {}
            };
        update.oldValues[key] =  oldValue;
        
        YAHOO.GDMA.datagrid.updateList.push(update);
        YAHOO.GDMA.toolbar.changeState("editmode");
    }
    
    YAHOO.GDMA.datagrid.dataTable.subscribe("checkboxClickEvent", function(oArgs){ 
        var elCheckbox = oArgs.target; 
        var oRecord = this.getRecord(elCheckbox);
        var oColumn = this.getColumn(elCheckbox);
        YAHOO.GDMA.datagrid.getRecordSet().updateRecordValue(oRecord, oColumn.key, elCheckbox.checked);
    }); 
    
    YAHOO.GDMA.datagrid.dataTable.subscribe("dropdownChangeEvent", function(oArgs){ 
        var elDropdown = oArgs.target; 
        var oRecord = this.getRecord(elDropdown);
        var oColumn = this.getColumn(elDropdown);
        YAHOO.GDMA.datagrid.dataTable.getRecordSet().updateRecordValue(oRecord, oColumn.key,elDropdown.value);
    }); 
    
    // Hook into custom event to customize save-flow of "radio" editor
    YAHOO.GDMA.datagrid.dataTable.subscribe("editorUpdateEvent", function(oArgs) {
        if(oArgs.editor.column.key === "active") {
        	this.saveCellEditor();
        }
    });
    
    YAHOO.GDMA.datagrid.dataTable.subscribe("editorBlurEvent", function(oArgs) {
        this.cancelCellEditor();
    });

    YAHOO.GDMA.datagrid.dataTable.getRecordSet().subscribe("recordValueUpdateEvent", YAHOO.GDMA.datagrid.addToUpdateList);
    YAHOO.GDMA.dialog.loading.hide();
};


YAHOO.GDMA.datagrid.createDatasource = function(){
    GdmaAjax.getTableDetails(YAHOO.GDMA.datagrid.paginatedRequest.serverId, YAHOO.GDMA.datagrid.paginatedRequest.tableId, function(servers) {

    	if (null == servers || 0 == servers.length)
    	{
    		YAHOO.GDMA.dialog.showInfoDialog("Error", "Table Columns haven't been setup correctly.  Please contact your administrator.");
    		return;
    	}
    	
        YAHOO.GDMA.datagrid.currentDataDescription = servers[0];

        //build the column defs & fields structure
        YAHOO.GDMA.datagrid.responseSchema = {
            resultsList : 'records',
            fields      : [],
            metaFields : { 
                totalRecords: 'totalRecords',
                paginationRecordOffset : "startIndex",
                sortDir : "sortDir",
                sortKey : "key"
            }
        };        
        
        YAHOO.GDMA.datagrid.columnDefs = [];
        var columns = servers[0].tables[0].columns;
        
        // need to scan the columns first to make sure at least one primary key is set
        var primarykeyfound = false;
        
        // Build the datasource schema
        // first item is rowid
        YAHOO.GDMA.datagrid.responseSchema.fields.push({ key: "rowid" });
        for(var i = 0; i < columns.length; i++){            
            if(columns[i].displayed == true){
                YAHOO.GDMA.datagrid.responseSchema.fields.push({
                            key: ""+columns[i].id,                            
                            parser: YAHOO.GDMA.utilities.getParser(columns[i].columnType),
                            primarykey: columns[i].primarykey });
                
                if(columns[i].primarykey == true){
                    primarykeyfound = true;                
                }
            }
        }

        // now build the table structure
        // First row is rowid
        YAHOO.GDMA.datagrid.columnDefs.push({
            key :           "rowid",
            label :         "",
            sortable:       false,
            resizeable:     false,
            minWidth: 20
        });

        for(var i = 0; i < columns.length; i++){
            if(columns[i].displayed == true){
                var columnId = "" + columns[i].id;
                var def = {
                        key :           columnId,
                        label :         columns[i].name,
                        formatter:      YAHOO.GDMA.utilities.getFormatter(columns[i].columnType),
                        sortable:       true,
                        sortOptions: {
                            defaultOrder: "desc"
                        },
                        resizeable:     true
                };
                
                if(columns[i].minWidth && columns[i].minWidth > 0){
                    def.minWidth = columns[i].minWidth;
                }
                
                if(columns[i].maxWidth && columns[i].maxWidth > 0){
                    def.width = columns[i].maxWidth;
                }
                
                if(columns[i].dropDownColumnDisplay && columns[i].dropDownColumnStore){
                    if(!YAHOO.GDMA.datagrid.ddColumns){
                        YAHOO.GDMA.datagrid.ddColumns = {}
                    }
                    
                    // need to proxy the call to GdmaAjax, so that we
                    // pass in the columnId in the correct scope
                    
                    (function() {
                        var scopedColumnId = columnId;
                        GdmaAjax.getDropDownData(
                                columns[i].dropDownColumnDisplay, 
                                columns[i].dropDownColumnStore,
                                function (ddData){
                                    YAHOO.GDMA.datagrid.ddColumns[scopedColumnId] = ddData;
                                });
                    })();

                    def.editor = YAHOO.GDMA.datagrid.editDropdown;
                    def.formatter = YAHOO.GDMA.datagrid.formatDropdown;
                    
                    
                }else{
                    // can only edit non primary key fields
                    if(primarykeyfound && columns[i].primarykey != true && columns[i].allowUpdate == true){
                        def.editor = YAHOO.GDMA.utilities.getEditor(columns[i].columnType);
                        def.editorOptions = {validator:YAHOO.GDMA.utilities.getValidator(columns[i].columnType)};
                    }
                }
                
                YAHOO.GDMA.datagrid.columnDefs.push(def);
            }
        }
    
        //set the first column as the sort column for the initial request
        if(columns[0]){
            YAHOO.GDMA.datagrid.paginatedRequest.sortedByColumnId = columns[0].id;
            YAHOO.GDMA.datagrid.paginatedRequest.dir = YAHOO.widget.DataTable.CLASS_DESC ;
        }
        

        if(servers[0].tables[0].allowDelete != true || !primarykeyfound ){
            YAHOO.GDMA.toolbar.updateToolBarButton("btnDelete",{
                name: "Delete",
                fn: YAHOO.GDMA.datagrid.deleteRecord,
                defaultMode: "noshow",
                tooltip: "delete selected record"
            });
        }
        //set up the data source - passing the DWR fucntion as the source
        YAHOO.GDMA.datagrid.datasource = new YAHOO.util.DataSource(GdmaAjax.getData);
        YAHOO.GDMA.datagrid.datasource.responseType = YAHOO.util.DataSource.TYPE_JSON;        
        YAHOO.GDMA.datagrid.datasource.responseSchema = YAHOO.GDMA.datagrid.responseSchema;        
        YAHOO.GDMA.datagrid.datasource.columnDefs = YAHOO.GDMA.datagrid.columnDefs;
        YAHOO.GDMA.datagrid.buildTable(YAHOO.GDMA.datagrid.datasource);
    });
};

// Init function for this page
YAHOO.GDMA.datagrid.init = function(serverId, tableId) {
    
//    YAHOO.GDMA.datagrid.dataTable.oldRender = YAHOO.GDMA.datagrid.dataTable.render;
//    YAHOO.GDMA.datagrid.dataTable.render = function(){
//        YAHOO.GDMA.datagrid.dataTable.oldRender();
//        YAHOO.GDMA.dialog.loading.hide();
//    }
    
    YAHOO.GDMA.dialog.loading.show();
    
    // default structure for making data requests
    YAHOO.GDMA.datagrid.paginatedRequest = {
        serverId: "",
        tableId: "",
        rowsPerPage: 25,
        recordOffset:0,
        sortedByColumnId: "",
        dir: "desc",
        filters: []
    };

    // list which stores changed records
    YAHOO.GDMA.datagrid.updateRequest = {
            serverId: "",
            tableId: "",
            primarykey: [],
            updates: []        
    };

    // list to hold 'dirty' records
    YAHOO.GDMA.datagrid.updateList = [];
    
    if(YAHOO.GDMA.datagrid.paginator)
        YAHOO.GDMA.datagrid.paginator.destroy();
    // create the paginator
    YAHOO.GDMA.datagrid.paginator = new YAHOO.widget.Paginator({
        containers         : ['divPager'],
        pageLinks          : 10,
        rowsPerPage        : 25,
        rowsPerPageOptions : [25,50,100, { value : 1, text : "All" } ],
        template           : "<strong>{CurrentPageReport}</strong>{PreviousPageLink} {PageLinks} {NextPageLink} {RowsPerPageDropdown}",
        pageReportTemplate : "(page {currentPage} of {totalPages}, total records {totalRecords})"
    });


    
    dwr.engine.setErrorHandler(YAHOO.GDMA.datagrid.dwrErrorHandler);
    dwr.engine.setPreHook(function() {
    	YAHOO.GDMA.dialog.loading.show();
	});
	dwr.engine.setPostHook(function() {
		setTimeout("YAHOO.GDMA.dialog.loading.hide();",5000);
	});

    YAHOO.GDMA.datagrid.paginatedRequest.serverId = serverId;
    YAHOO.GDMA.datagrid.paginatedRequest.tableId = tableId;
    YAHOO.GDMA.datagrid.paginatedRequest.filters = [];    
    YAHOO.GDMA.datagrid.updateRequest.tableId = tableId;
    YAHOO.GDMA.datagrid.updateRequest.updates = [];
    
    YAHOO.GDMA.datagrid.createDatasource();
    
    // Enable optional features
    if (!enabledFeatures['ie.clients.gdma.BulkImport']) {
    	YAHOO.GDMA.datagrid.toolbarButtons[9].defaultMode = "hide";
    }
    
    YAHOO.GDMA.toolbar.createToolbar(YAHOO.GDMA.datagrid.toolbarButtons);
};

YAHOO.GDMA.datagrid.clearFilter = function() {
    YAHOO.GDMA.datagrid.paginatedRequest.filters = [];
    YAHOO.GDMA.datagrid.refreshData();
};

YAHOO.GDMA.datagrid.filterRecords = function() {
    YAHOO.GDMA.datagrid.dataTable.cancelCellEditor();
    var popupformConfig = {
            title: 'Create a filter for table ' + YAHOO.GDMA.datagrid.currentDataDescription.tables[0].name ,
            submit: YAHOO.GDMA.datagrid.doFilter,
            submitLabel: 'Filter',
            reset: YAHOO.GDMA.datagrid.resetPopupForm,
            cancel: YAHOO.GDMA.datagrid.cancelPopupForm,
            body: function(container){
                        YAHOO.GDMA.utilities.createElement("br",null, container);
                        YAHOO.GDMA.utilities.createElement("br",null, container);
                        
                        var columns = YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
                        for(var i = 0; i < columns.length; i++){
                            if(columns[i].displayed == true){
                                var id = columns[i].id
                                var p = YAHOO.GDMA.utilities.createElement("p","p" + id, container);
                                
                                var lblFilter = YAHOO.GDMA.utilities.createElement("label","lblFilter" + id, p);
                                lblFilter.htmlFor = "txtFilter" + id;
                                lblFilter.innerHTML = columns[i].name;
                                if(columns[i].dropDownColumnDisplay && columns[i].dropDownColumnStore){
                                    var selFilter = YAHOO.GDMA.utilities.createElement("select","selFilter" + id, p);
                                    YAHOO.GDMA.utilities.populateDropDown(
                                            selFilter, 
                                            YAHOO.GDMA.datagrid.ddColumns[id], 
                                            1, 
                                            2, 
                                            -1, 
                                            true);                        
                                }else{
                                    var txtColumnValue = YAHOO.GDMA.utilities.createElement("input","txtFilter" + id, p);
                                    txtColumnValue.type = "text";
                                }
                                
                                var spanIsNull = YAHOO.GDMA.utilities.createElement("span","spanIsNull" + id, p);
                                spanIsNull.innerHTML = "Is Null";                     
                                p.innerHTML += "<INPUT type=\"checkbox\" id=\"chkIsNull" + id + "\" />";
            
                                var spanIsBlank = YAHOO.GDMA.utilities.createElement("span","spanIsBlank" + id, p);
                                spanIsBlank.innerHTML = "Is Blank";
                                p.innerHTML += "<INPUT type=\"checkbox\" id=\"chkIsBlank" + id + "\" />";
                                
                            }
                        }
                        
                        //now go through the filters a set values
                        for(var i = 0; i <  YAHOO.GDMA.datagrid.paginatedRequest.filters.length; i++){
                            var id = YAHOO.GDMA.datagrid.paginatedRequest.filters[i].columnId;
                            var el = document.getElementById("txtFilter" + id);
                            if(!el)
                                el = document.getElementById("selFilter" + id);
                            if(el){
                                el.value =  YAHOO.GDMA.datagrid.paginatedRequest.filters[i].filterValue;
                                document.getElementById("chkIsNull" + id).checked = (YAHOO.GDMA.datagrid.paginatedRequest.filters[i].nullValue == true);
                                document.getElementById("chkIsBlank" + id).checked = (YAHOO.GDMA.datagrid.paginatedRequest.filters[i].blank == true);
                            }
                        }
                }
    };
    YAHOO.GDMA.datagrid.createPopupform( popupformConfig );
};

YAHOO.GDMA.datagrid.doDownload = function(){
    
    var txtPaginatedRequest = YAHOO.util.Dom.get("txtPaginatedRequest");
    var frmDownload = YAHOO.util.Dom.get("frmDownload");
    var rdDownloadAll = YAHOO.util.Dom.get("rdDownloadAll");
    var rdDownloadExcel = YAHOO.util.Dom.get("rdDownloadExcel");
    
    if(rdDownloadAll.checked){
        //temporally change the request record and change it back again
        var rowsPerPage= YAHOO.GDMA.datagrid.paginatedRequest.rowsPerPage;
        var recordOffset= YAHOO.GDMA.datagrid.paginatedRequest.recordOffset;
        
        YAHOO.GDMA.datagrid.paginatedRequest.rowsPerPage = YAHOO.GDMA.datagrid.paginator.get('totalRecords') + 100;
        YAHOO.GDMA.datagrid.paginatedRequest.recordOffset = 0;
        
        txtPaginatedRequest.value = YAHOO.lang.JSON.stringify(YAHOO.GDMA.datagrid.paginatedRequest);
        
        YAHOO.GDMA.datagrid.paginatedRequest.rowsPerPage = rowsPerPage;
        YAHOO.GDMA.datagrid.paginatedRequest.recordOffset = recordOffset;
    }else{
        txtPaginatedRequest.value = YAHOO.lang.JSON.stringify(YAHOO.GDMA.datagrid.paginatedRequest);
    }
    
    
    if(rdDownloadExcel.checked){
        frmDownload.action = "download.xls";
    }else{
        frmDownload.action = "download.csv";
    }
    frmDownload.submit();

};

YAHOO.GDMA.datagrid.downloadRecords = function() {
    YAHOO.GDMA.datagrid.dataTable.cancelCellEditor();
    var popupformConfig = {
            width: 300,
            height: 200,
            title: 'Download records for table ' + YAHOO.GDMA.datagrid.currentDataDescription.tables[0].name,
            submit: YAHOO.GDMA.datagrid.doDownload,
            submitLabel: 'Download',
            cancel: YAHOO.GDMA.datagrid.cancelPopupForm,
            body: function(container){
                        
                var h = YAHOO.GDMA.utilities.createElement("h1", null, container);
                h.innerHTML = "Download"
                    
                var p = YAHOO.GDMA.utilities.createElement("p", null, container);
                
                p.innerHTML = "<label for=\"rdDownloadExcel\">Excel</label> <input id=\"rdDownloadExcel\" name=\"rdDownloadFormat\" type=\"radio\" value=\"xls\" checked=\"true\"> "; 
                    
                p = YAHOO.GDMA.utilities.createElement("p", null, container);
                p.innerHTML = "<label for=\"rdDownloadCsv\">CSV</label> <input id=\"rdDownloadCsv\" name=\"rdDownloadFormat\" type=\"radio\" value=\"csv\" > ";
                
                h = YAHOO.GDMA.utilities.createElement("h1", null, container);
                h.innerHTML = "Records"
                    
                p = YAHOO.GDMA.utilities.createElement("p", null, container);
                p.innerHTML = "<label for=\"rdDownloadAll\">All</label> <input id=\"rdDownloadAll\" name=\"rdDownloadAmount\" type=\"radio\" value=\"all\" checked=\"true\"> ";
                    
                p = YAHOO.GDMA.utilities.createElement("p", null, container);
                p.innerHTML = "<label for=\"rdCurrentPage\">Current Page</label> <input id=\"rdCurrentPage\" name=\"rdDownloadAmount\" type=\"radio\" value=\"page\"> ";

                frmDownload = YAHOO.GDMA.utilities.createElement("form", "frmDownload", container);
                frmDownload.method = "post";
                
                var txtPaginatedRequest = YAHOO.GDMA.utilities.createElement("input","txtPaginatedRequest", frmDownload, "hidden");
                txtPaginatedRequest.name = "txtPaginatedRequest";
                        
            }
    
    };
    YAHOO.GDMA.datagrid.createPopupform( popupformConfig );
};

YAHOO.GDMA.datagrid.doBulkImport = function(){

        var theFile = document.getElementById("fiDataFile");
        var fileParent = theFile.parentNode;
        var theDiv = document.createElement('div');
        theDiv.style.display = 'none';
        theDiv.innerHTML =
            '<iframe id="hidden_frame" name="hidden_frame" src=""></iframe>' +
            '<form id="hidden_form" target="hidden_frame" action="bulkImport.htm" enctype="multipart/form-data" method="post"></form>';

        document.body.appendChild(theDiv);
        var hiddenForm = document.getElementById("hidden_form");
        fileParent.removeChild(theFile);
        hiddenForm.appendChild(theFile);
        
        var txtPaginatedRequest = YAHOO.GDMA.utilities.createElement("input","txtPaginatedRequest", hiddenForm, "hidden");
        txtPaginatedRequest.name = "txtPaginatedRequest";
        txtPaginatedRequest.value = YAHOO.lang.JSON.stringify(YAHOO.GDMA.datagrid.paginatedRequest);
        
        hiddenForm.submit();
        hiddenForm.removeChild(theFile);
        fileParent.appendChild(theFile);
        
};

YAHOO.GDMA.datagrid.doBulkImportDone = function(error, numRecords){
	if (error == '') {
        YAHOO.GDMA.dialog.showInfoDialog("Saved!", "" + numRecords + " record(s) imported successfully");
        YAHOO.GDMA.datagrid.refreshData();
	} else {
        YAHOO.GDMA.dialog.showInfoDialog("Error!", "Import error: " + error);
	}
    YAHOO.GDMA.datagrid.popFormPanel.destroy();
};

YAHOO.GDMA.datagrid.bulkImport = function() {
    YAHOO.GDMA.datagrid.dataTable.cancelCellEditor();
    var popupformBulkImport = {
            width: 300,
            height: 200,
            title: 'Bulk import for table ' + YAHOO.GDMA.datagrid.currentDataDescription.tables[0].name,
            submit: YAHOO.GDMA.datagrid.doBulkImport,
            submitLabel: 'Import',
            cancel: YAHOO.GDMA.datagrid.cancelPopupForm,
            body: function(container){
                        
                var h = YAHOO.GDMA.utilities.createElement("h1", null, container);
                h.innerHTML = "Bulk Import"
                    
                var p = YAHOO.GDMA.utilities.createElement("p", null, container);
                
                p.innerHTML = "<label for=\"fiDataFile\">File:</label> <input id=\"fiDataFile\" name=\"fiDataFile\" type=\"file\"> "; 
            }
    
    };
    YAHOO.GDMA.datagrid.createPopupform( popupformBulkImport );
};

YAHOO.GDMA.datagrid.addRecordSave = function() {
    var updateRequest = {
            serverId: YAHOO.GDMA.datagrid.paginatedRequest.serverId,
            tableId: YAHOO.GDMA.datagrid.paginatedRequest.tableId,
            updates: []
    }
    
     var columns = YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
     var idx = 0;
     var addRecords = []; 
     for(var i = 0; i < columns.length; i++){
         if(columns[i].displayed == true && columns[i].allowInsert){
             var columnValue;
             if(columns[i].dropDownColumnDisplay && columns[i].dropDownColumnStore){
                 var selColumnValue = document.getElementById("selColumnValue" + columns[i].id);
                 var selectedIndex = selColumnValue.selectedIndex;
                 if(selectedIndex < 1){                     
                     //check that nulls are allowed
                     if(columns[i].nullable){
                         columnValue = null;
                     }else{
                         YAHOO.GDMA.dialog.showInfoDialog("Validation Error!", "You must select an item from the " + columns[i].name + " dropdown");
                         return;
                     }
                 }
                 columnValue = selColumnValue.options[selColumnValue.selectedIndex].value;
             }else{
                 var txtColumnValue = document.getElementById("txtColumnValue" + columns[i].id);
                 columnValue = txtColumnValue.value;
                 var validator = YAHOO.GDMA.utilities.getValidator(columns[i].columnType);
                 if(validator){
                     if(validator(columnValue, i) == null){
                    	 txtColumnValue.style.color = "red";
                    	 if(columnValue.length > columns[i].columnSize)
                         {
                        	 //YAHOO.GDMA.dialog.showInfoDialog("Validation Error", "The maximum length allowed for this field is " + YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns[i].columnSize);
                         }
                         else
                         {
                        	 YAHOO.GDMA.dialog.showInfoDialog("Validation Error!", "Field values in red are not valid.");
                         }
                         return;
                     }
                 }
                 //reset color
                 txtColumnValue.style.color = "black";    
             }
             addRecords.push({columnId: columns[i].id, newColumnValue: columnValue});
         }
     }
     updateRequest.updates.push(addRecords);
    
     GdmaAjax.addRecord(updateRequest, function() {
         YAHOO.GDMA.dialog.showInfoDialog("Saved!", "Record added");
         YAHOO.GDMA.datagrid.refreshData();         
         YAHOO.GDMA.datagrid.popFormPanel.destroy();
     });
 
};

YAHOO.GDMA.datagrid.addRecord = function() {
    YAHOO.GDMA.datagrid.dataTable.cancelCellEditor();
    var popupformConfig = {
            title: 'Primary Key Values',
            submit: YAHOO.GDMA.datagrid.addRecordSave,
            submitLabel: 'Add',
            reset: YAHOO.GDMA.datagrid.resetPopupForm,
            cancel: YAHOO.GDMA.datagrid.cancelPopupForm,
            body: function(container){
        
                    YAHOO.GDMA.utilities.createElement("br",null, container);
                    YAHOO.GDMA.utilities.createElement("br",null, container);
                    
                    var columns = YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
                    for(var i = 0; i < columns.length; i++){
                        if(columns[i].displayed == true && columns[i].allowInsert == true ){
                            var id = columns[i].id
                            var p = YAHOO.GDMA.utilities.createElement("p","p" + id, container);
                            
                            var lblColumnName = YAHOO.GDMA.utilities.createElement("label","lblColumnName" + id, p);
                            lblColumnName.htmlFor = "txtColumnValue" + id;
                            lblColumnName.innerHTML = columns[i].name;
                            if(columns[i].dropDownColumnDisplay && columns[i].dropDownColumnStore){
                                var selColumnValue = YAHOO.GDMA.utilities.createElement("select","selColumnValue" + id, p);
                                YAHOO.GDMA.utilities.populateDropDown(
                                        selColumnValue, 
                                        YAHOO.GDMA.datagrid.ddColumns[id], 
                                        1, 
                                        2, 
                                        -1, 
                                        true);                        
                            }else{
                                var txtColumnValue = YAHOO.GDMA.utilities.createElement("input","txtColumnValue" + id, p);
                                txtColumnValue.type = "text";
                            }
                            
                        }
                    }
            
                }
    };
    YAHOO.GDMA.datagrid.createPopupform( popupformConfig ); 
};

YAHOO.GDMA.datagrid.multiUpdateSave = function(){
    var updateRequest = {
            serverId: YAHOO.GDMA.datagrid.paginatedRequest.serverId,
            tableId: YAHOO.GDMA.datagrid.paginatedRequest.tableId,
            updates: []
    }
     var columns = YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
     var idx = 0;
     var newValues = {}; 
     for(var i = 0; i < columns.length; i++){
         if(columns[i].displayed == true && columns[i].allowUpdate == true){
             var chkInclude = document.getElementById("chkInclude" + columns[i].id);
             if(chkInclude.checked == true){
                 var columnValue;
                 if(columns[i].dropDownColumnDisplay && columns[i].dropDownColumnStore){
                     var selColumnValue = document.getElementById("txtColumnValue" + columns[i].id);
                     var selectedIndex = selColumnValue.selectedIndex;
                     if(selectedIndex < 1){                     
                         //check that nulls are allowed
                         if(columns[i].nullable){
                             columnValue = null;
                         }else{
                             YAHOO.GDMA.dialog.showInfoDialog("Validation Error!", "You must select an item from the " + columns[i].name + " dropdown");
                             return;
                         }
                     }
                     columnValue = selColumnValue.options[selColumnValue.selectedIndex].value;
                 }else{
                     var txtColumnValue = document.getElementById("txtColumnValue" + columns[i].id);
                     columnValue = txtColumnValue.value;
                     var validator = YAHOO.GDMA.utilities.getValidator(columns[i].columnType);
                     if(validator){
                         if(validator(columnValue, i) == null){
                        	 txtColumnValue.style.color = "red";
                        	 if(columnValue.length > columns[i].columnSize)
                             {
                            	 //YAHOO.GDMA.dialog.showInfoDialog("Validation Error", "The maximum length allowed for this field is " + YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns[i].columnSize);
                             }
                             else
                             {
                            	 YAHOO.GDMA.dialog.showInfoDialog("Validation Error!", "Field values in red are not valid.");
                             }
                             return;
                         }
                     }
                     //reset color
                     txtColumnValue.style.color = "black";    
                 }
                 newValues[ columns[i].id] = columnValue;
             }
         }
     }
     
     var selectedRows = YAHOO.GDMA.datagrid.dataTable.getSelectedRows();
     for(var i = 0; i < selectedRows.length; i++){
         var oData = YAHOO.GDMA.datagrid.dataTable.getRecord(selectedRows[i]).getData(); 
         
             var oldValues = oData;
             
             var updateRecord = []; 
             
             for(var id in oldValues){
                 if(id != "rowid"){ //everything except dummy rowid
                     if(YAHOO.lang.isUndefined(newValues[id])){
                         updateRecord.push({
                             columnId: id, 
                             oldColumnValue: oldValues[id] 
                         });
                     }else{
                         updateRecord.push({
                             columnId: id, 
                             newColumnValue: newValues[id], 
                             oldColumnValue: oldValues[id] 
                         });
                     }
                 }
             }
             updateRequest.updates.push(updateRecord);
     }
         
    YAHOO.GDMA.datagrid.updateRequest = updateRequest;
     GdmaAjax.updateRecords(updateRequest, function(countUpdated) {
         YAHOO.GDMA.dialog.showInfoDialog("Saved!", countUpdated + " Record" +(countUpdated>1?"s":"") +" updated");
         YAHOO.GDMA.datagrid.refreshData();         
         YAHOO.GDMA.datagrid.popFormPanel.destroy();
     });
};

YAHOO.GDMA.datagrid.multiUpdate = function() {
    YAHOO.GDMA.datagrid.dataTable.cancelCellEditor();
    var selectedRows = YAHOO.GDMA.datagrid.dataTable.getSelectedRows();

    if (!selectedRows || selectedRows.length < 2) {
        YAHOO.GDMA.dialog.showInfoDialog("No record selected ...", "Please select the multiple records you wish to update.")
    } else{
        var popupformConfig = {
                title: 'Update All Selected Rows',
                submit: YAHOO.GDMA.datagrid.multiUpdateSave,
                submitLabel: 'Update All',
                reset: YAHOO.GDMA.datagrid.resetPopupForm,
                cancel: YAHOO.GDMA.datagrid.cancelPopupForm,
                body: function(container){
                
                            YAHOO.GDMA.utilities.createElement("br",null, container);
                            
                            var p = YAHOO.GDMA.utilities.createElement("p",null, container);
                            p.innerHTML += "Use the checkbox to enable the columns you wish to update";
                            YAHOO.GDMA.utilities.createElement("br",null, container);
                            
                            var columns = YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns;
                            for(var i = 0; i < columns.length; i++){
                                if(columns[i].displayed == true && columns[i].allowUpdate == true ){
                                    var id = columns[i].id
                                    var p = YAHOO.GDMA.utilities.createElement("p","p" + id, container);
                                    var lblColumnName = YAHOO.GDMA.utilities.createElement("label","lblColumnName" + id, p);
                                    lblColumnName.htmlFor = "txtColumnValue" + id;
                                    lblColumnName.innerHTML = columns[i].name;
                                    
                                    var chkId = "chkInclude" + id;
                                    var chkInclude = YAHOO.GDMA.utilities.createElement("input",chkId, p, "checkbox");
                                    chkInclude.value = columns[i].id;
                
                                    if(columns[i].dropDownColumnDisplay && columns[i].dropDownColumnStore){
                                        var txtColumnValue = YAHOO.GDMA.utilities.createElement("select","txtColumnValue" + id, p);
                                        YAHOO.GDMA.utilities.populateDropDown(
                                                txtColumnValue, 
                                                YAHOO.GDMA.datagrid.ddColumns[id], 
                                                1, 
                                                2, 
                                                -1, 
                                                true);    
                                        txtColumnValue.disabled = true;
                                    }else{
                                        var txtColumnValue = YAHOO.GDMA.utilities.createElement("input","txtColumnValue" + id, p);
                                        txtColumnValue.type = "text";
                                        txtColumnValue.disabled = true;
                                    }
                                    
                                    YAHOO.util.Event.addListener(chkId, "click", function(e, id){
                                        var chkInclude = YAHOO.util.Dom.get("chkInclude" + id);
                                        var txtColumnValue = YAHOO.util.Dom.get("txtColumnValue" + id);
                                        if(chkInclude.checked == true){
                                            txtColumnValue.disabled = false;                            
                                        }else{
                                            txtColumnValue.value = "";
                                            txtColumnValue.disabled = true;
                                        }
                                    }, id); 
                                    
                                }
                            }
                    }
        };
        YAHOO.GDMA.datagrid.createPopupform( popupformConfig );
    }
};

YAHOO.GDMA.datagrid.cancelPopupForm = function(){
    YAHOO.GDMA.datagrid.popFormPanel.destroy();
};

YAHOO.GDMA.datagrid.resetPopupForm = function(){
    var divDlgPopupFormCenter = YAHOO.util.Dom.get("divDlgPopupFormCenter");
    var inputs = divDlgPopupFormCenter.getElementsByTagName("input");
    for ( var i = 0; i < inputs.length; i++) {
        if(inputs[i].type == 'text'){
            inputs[i].value = "";
        }else if(inputs[i].type == 'checkbox'){
            inputs[i].checked = false;
        }
    }    
};

//used to collect primary key values
YAHOO.GDMA.datagrid.createPopupform = function(config) {
    
    var btnSaveText = config.submitLabel ? config.submitLabel : 'Save';
    var width = config.width ? config.width : 500;
    var height = config.height ? config.height : 400;
    
    //get or create the container element for the panel
    var divDlgPopupForm = YAHOO.util.Dom.get("divDlgPopupForm");
    if(!divDlgPopupForm){
        divDlgPopupForm = document.createElement("div")    
        divDlgPopupForm.id = "divDlgPopupForm";
        document.body.insertBefore(divDlgPopupForm, document.body.firstChild);
    }
    
    // the actual panel
    YAHOO.GDMA.datagrid.popFormPanel = new YAHOO.widget.Panel('divDlgPopupForm', {
        draggable: true,
        close: false,
        modal: true,
        width: (width + 8) + 'px',
        xy: [140, 100]
    });
    
    YAHOO.GDMA.datagrid.popFormPanel.setHeader(config.title);
    YAHOO.GDMA.datagrid.popFormPanel.setBody('<div id="divDlgPopupFormLayout"></div>');
    YAHOO.GDMA.datagrid.popFormPanel.beforeRenderEvent.subscribe(function() {
        YAHOO.util.Event.onAvailable('divDlgPopupFormLayout', function() {
            var divDlgPopupFormLayout = YAHOO.util.Dom.get("divDlgPopupFormLayout");
            
            var divDlgPopupFormCenter = YAHOO.GDMA.utilities.createElement("div","divDlgPopupFormCenter",divDlgPopupFormLayout);
            var divDlgPopupFormBottom = YAHOO.GDMA.utilities.createElement("div","divDlgPopupFormBottom",divDlgPopupFormLayout);
            YAHOO.util.Dom.addClass(divDlgPopupFormBottom, "gdma-toolbar");
            
            YAHOO.GDMA.datagrid.popFormLayout = new YAHOO.widget.Layout('divDlgPopupFormLayout', {
                height: height,
                width: width,
                units: [
                    { position: 'center', width: 100, height: 60, body: 'divDlgPopupFormCenter', gutter: '0px',  scroll: true  },
                    { position: 'bottom', height: 40, body: 'divDlgPopupFormBottom' }
                ]
            });
    
            YAHOO.GDMA.datagrid.popFormLayout.render();            
                          
            if(config.reset){            
                YAHOO.GDMA.toolbar.createToolBarButton("Reset","btnPopupFormClear", divDlgPopupFormBottom, config.reset, "Reset the form", "show" );
            }
            YAHOO.GDMA.toolbar.createToolBarButton("Cancel","btnPopupFormCancel", divDlgPopupFormBottom, config.cancel, "Close this dialog without applying changes", "show" );
            YAHOO.GDMA.toolbar.createToolBarButton("Save", "btnPopupFormSave", divDlgPopupFormBottom, config.submit, "Apply changes", "show" ).set("label",btnSaveText);
     
            config.body(divDlgPopupFormCenter);
                    
        });
    });
    YAHOO.GDMA.datagrid.popFormPanel.render();
};

//define the buttons for the page (2 modes default and edit)
YAHOO.GDMA.datagrid.toolbarButtons =  [
    {
        name: "Add",
        fn: YAHOO.GDMA.datagrid.addRecord,
        defaultMode: "show",
        tooltip: "add a new record"
    },{
        name: "Delete",
        fn: YAHOO.GDMA.datagrid.deleteRecord,
        defaultMode: "show",
        tooltip: "delete selected record"
    },{
        name: "Save",
        fn: YAHOO.GDMA.datagrid.updateRecords,
        editmode: "show",
        tooltip: "save updates and/or additions of records"
    },{
        name: "Cancel",
        fn: YAHOO.GDMA.datagrid.refreshData,
        editmode: "show",
        tooltip: "cancel edit and refresh the data"
    },{
        name: "Refresh",
        fn: YAHOO.GDMA.datagrid.refreshData,
        defaultMode: "show",
        tooltip: "refresh the data"
    },{
        name: "Filter",
        fn: YAHOO.GDMA.datagrid.filterRecords,
        defaultMode: "show",
        tooltip: "filter the data"
    },{
        name: "Clear",
        fn: YAHOO.GDMA.datagrid.clearFilter,
        defaultMode: "show",
        tooltip: "clear the filter"
    },{
        name: "Multi",
        fn: YAHOO.GDMA.datagrid.multiUpdate,
        defaultMode: "show",
        tooltip: "update multiple records"
    },{
        name: "Download",
        fn: YAHOO.GDMA.datagrid.downloadRecords,
        defaultMode: "show",
        tooltip: "download records"
    },{
        name: "Import",
        fn: YAHOO.GDMA.datagrid.bulkImport,
        defaultMode: "show",
        tooltip: "import data from csv file"
    }
];
