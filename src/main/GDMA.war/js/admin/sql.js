// Create a namespace for this code
YAHOO.namespace("GDMA.sql");
 
// layout the page
YAHOO.util.Event.onDOMReady( function(){
    //this is a bit dodgey! - look for something better than yui-gen8
    YAHOO.util.Event.onAvailable("yui-gen8", function() {
        var center = YAHOO.GDMA.layout.getUnitByPosition('center').body;
        YAHOO.GDMA.sql.layout = new YAHOO.widget.Layout(center,{
            units: [
                { position: 'top', height: '28px', resize: false, body: 'divToolbar',gutter: '0px 0px 0px 0px',scroll: false },
                { position: 'bottom', height: '30px', resize: false, body: 'divPager', gutter: '0px 0px 0px 0px', scroll: false},
                { position: 'center', body: 'divDataTable', gutter: '0px 0px 0px 0px', scroll: true }
            ]
        });
        YAHOO.GDMA.sql.layout.render();
    });
});
// end layout

// Error handlers for DWR
YAHOO.GDMA.sql.dwrErrorHandler = function(message, exception) {
    YAHOO.GDMA.sql.dwrLastException = exception;
    if (exception && exception.mostSpecificCause) {
        YAHOO.GDMA.dialog.showInfoDialog("Error!", "1. An error occured<br/>" + exception.mostSpecificCause.message,
                YAHOO.widget.SimpleDialog.ICON_BLOCK);
    } else {
        YAHOO.GDMA.dialog.showInfoDialog("Error!", "2. An error occured<br/>" + message, YAHOO.widget.SimpleDialog.ICON_BLOCK);
    }
}

// Refresh the current records
YAHOO.GDMA.sql.refreshRecords = function() {
    YAHOO.GDMA.sql.dataTable.cancelCellEditor();
    YAHOO.GDMA.sql.refreshFunction();
};

// This is the method responsible for drawing the table
YAHOO.GDMA.sql.buildTable = function(datasource) {
    // setup the datatable
    YAHOO.GDMA.sql.dataTable = new YAHOO.widget.DataTable(
            "divServerTable", 
            datasource.columnDefs,
            datasource, 
            {
                    initialRequest         : YAHOO.GDMA.sql.paginatedSqlRequest,
                    paginationEventHandler : YAHOO.GDMA.sql.handlePagination,
                    paginator              : YAHOO.GDMA.sql.paginator
            });

    YAHOO.GDMA.dialog.loading.hide();
};


YAHOO.GDMA.sql.onExecute = function(){
    
    var selServers = YAHOO.util.Dom.get("selServers");
    var txtSqlEditor = YAHOO.util.Dom.get("txtSqlEditor");
    var serverId = selServers.value;
    var sql =  YAHOO.lang.trim(txtSqlEditor.value);
    
    if(!selServers.selectedIndex > 0){
        YAHOO.GDMA.dialog.showInfoDialog("Error!", "Please select a server first",YAHOO.widget.SimpleDialog.ICON_BLOCK);
        return;
    }
    
    if(sql == ""){
        YAHOO.GDMA.dialog.showInfoDialog("Error!", "Please enter a valid select query",YAHOO.widget.SimpleDialog.ICON_BLOCK);
        return;
    }
    
    if(sql.substring(0,1).toUpperCase() != 'S'){
    	sql = sql.substring(1);
    }else{
    	sql = sql.substring(0);
    }
    
    if(sql.substring(0,6).toUpperCase() != "SELECT")
	{	
        	YAHOO.GDMA.dialog.showInfoDialog("Invalid operation!", "This console is designed for SQL SELECT statements. " +
    			"Please use the GUI on the GDMA tab for SQL INSERTS, UPDATES or DELETES.",YAHOO.widget.SimpleDialog.ICON_BLOCK);
		return;
	}
	
    
    if(YAHOO.GDMA.sql.dataTable){
        YAHOO.GDMA.sql.dataTable.destroy();
    }
    if(YAHOO.GDMA.sql.paginator){
        YAHOO.GDMA.sql.paginator.destroy();
    }
    
    YAHOO.GDMA.sql.paginatedSqlRequest.sql = sql;
    YAHOO.GDMA.sql.paginatedSqlRequest.serverId = serverId;
    YAHOO.GDMA.sql.createDatasource(serverId, sql);
}

YAHOO.GDMA.sql.createServerDropDown = function(servers){
    var selServers = YAHOO.util.Dom.get("selServers");
    YAHOO.GDMA.utilities.populateDropDown(selServers, servers, "id", "name", -1, true);
}

YAHOO.GDMA.sql.createDatasource = function(serverId, sql){
    GdmaAdmin.executeSelectGetColumns(serverId, sql, function(columns) {
        //build the column defs & fields structure
        YAHOO.GDMA.sql.responseSchema = {
            resultsList : 'records',
            fields      : []
        };        
        
        YAHOO.GDMA.sql.columnDefs = [];
        
        YAHOO.GDMA.sql.responseSchema.fields.push({ key: "rowid" });
        for(var i = 0; i < columns.length; i++){            
            YAHOO.GDMA.sql.responseSchema.fields.push({key: ""+columns[i].id});
        }
        
        YAHOO.GDMA.sql.columnDefs.push({
            key :           "rowid",
            label :         "",
            minWidth: 20
        });

        // now build the table structure
        for(var i = 0; i < columns.length; i++){
                var columnId = "" + columns[i].id;
                var def = {
                        key :           ""+columns[i].id,
                        label :         columns[i].name,
                        resizeable:     true
                };
                YAHOO.GDMA.sql.columnDefs.push(def);
        }
    
        //set up the data source - passing the DWR fucntion as the source
        YAHOO.GDMA.sql.datasource = new YAHOO.util.DataSource(GdmaAdmin.executeSelect);
        YAHOO.GDMA.sql.datasource.responseType = YAHOO.util.DataSource.TYPE_JSON;        
        YAHOO.GDMA.sql.datasource.responseSchema = YAHOO.GDMA.sql.responseSchema;        
        YAHOO.GDMA.sql.datasource.columnDefs = YAHOO.GDMA.sql.columnDefs;
        YAHOO.GDMA.sql.buildTable(YAHOO.GDMA.sql.datasource);
    });
};

// Init functon for this page
YAHOO.GDMA.sql.init = function() {

    dwr.engine.setErrorHandler(YAHOO.GDMA.sql.dwrErrorHandler);

    YAHOO.GDMA.toolbar.createToolbar(YAHOO.GDMA.sql.toolbarButtons);
    YAHOO.GDMA.sql.refreshFunction = YAHOO.GDMA.sql.refreshServers;
    
    GdmaAdmin.getServers( function(servers) {
        YAHOO.GDMA.sql.createServerDropDown(servers);
    });
    
    // default structure for making data requests
    YAHOO.GDMA.sql.paginatedSqlRequest = {
        serverId: "",
        rowsPerPage: 1000,
        recordOffset:0,
        sql: ""
    };

    if(YAHOO.GDMA.sql.paginator)
        YAHOO.GDMA.sql.paginator.destroy();
    
    // create the paginator
    YAHOO.GDMA.sql.paginator = new YAHOO.widget.Paginator({
        containers         : ['divPager'],
        pageLinks          : 10,
        rowsPerPage        : 25,
        rowsPerPageOptions : [25,50,100, 1000 ],
        template           : "<strong>{CurrentPageReport}</strong>{PreviousPageLink} {PageLinks} {NextPageLink} {RowsPerPageDropdown}",
        pageReportTemplate : "(page {currentPage} of {totalPages}, total records {totalRecords})"
    });

    YAHOO.GDMA.toolbar.createToolbar(YAHOO.GDMA.sql.toolbarButtons);
}

// event which kicks it all off
YAHOO.util.Event.onDOMReady(function() {
    YAHOO.util.Event.onAvailable('divServerTable', YAHOO.GDMA.sql.init);
});

YAHOO.GDMA.sql.toolbarButtons =  [{
    name: "Execute",
    fn: YAHOO.GDMA.sql.onExecute ,
    defaultMode: "show",
    usersView: "show",
    tooltip: "execute the SQL in the console"
}];