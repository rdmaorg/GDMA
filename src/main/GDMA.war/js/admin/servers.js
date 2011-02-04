// Create a namespace for this code
YAHOO.namespace("GDMA.admin.servers");

// Get all possible connection types for drop down
YAHOO.GDMA.admin.servers.loadConnectionTypes = function() {
    GdmaAdmin.getConnectionTypes( function(connectionTypes) {
        YAHOO.GDMA.admin.servers.connectionTypes = connectionTypes;
    });
}

//define the datasource fields
YAHOO.GDMA.admin.servers.fields = {
        fields : [ 
                  {key:"id", parser:YAHOO.util.DataSource.parseNumber},
                  {key:"name", parser:YAHOO.util.DataSource.parseString},
                  {key:"username", parser:YAHOO.util.DataSource.parseString},
                  {key:"password", parser:YAHOO.util.DataSource.parseString},
                  {key:"connectionUrl", parser:YAHOO.util.DataSource.parseString},
                  {key:"connectionType"},
                  {key:"prefix", parser:YAHOO.util.DataSource.parseString},
                  {key:"active"}
        ]
};

YAHOO.GDMA.admin.servers.doubleClick = function(oArgs){
    var elTargetRow = YAHOO.GDMA.admin.dataTable.getTrEl(oArgs.target);
    YAHOO.GDMA.admin.dataTable.selectRow(elTargetRow);
    YAHOO.GDMA.admin.dataTable.focus();
    YAHOO.GDMA.admin.viewTables();
};

// Load the list of servers
YAHOO.GDMA.admin.servers.loadServers = function(fnCallback) {
    GdmaAdmin.getServers( function(servers) {
        // setup the object backing the table
        YAHOO.GDMA.admin.servers.servers = servers;
        var datasource = new YAHOO.util.DataSource(servers);
        datasource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;        
        // field names
        datasource.responseSchema = YAHOO.GDMA.admin.servers.fields;        
        datasource.columnDefs = YAHOO.GDMA.admin.servers.columnDefs;
        fnCallback(datasource);
    });
    //setup the functions need
    YAHOO.GDMA.admin.saveFunction =  GdmaAdmin.saveServers;
    YAHOO.GDMA.admin.deleteFunction = GdmaAdmin.deleteServer; 
    YAHOO.GDMA.admin.doubleClick = YAHOO.GDMA.admin.servers.doubleClick;
}

// need some custom methods for the datatable

// connectionType is an object. We need to display the
// name attribute of this object
YAHOO.GDMA.admin.servers.connectionTypeFormatter = function(elCell, oRecord, oColumn, oData) {
    YAHOO.GDMA.admin.servers.oRecord = oRecord;
    YAHOO.GDMA.admin.servers.elCell = elCell;
    if (oData) {
        elCell.innerHTML = oData.name;
    } else {
        elCell.innerHTML = "NOT SELECTED";
    }
};

// Need a custom edit drop down to deal with referenced table
YAHOO.GDMA.admin.servers.editDropdown = function(oEditor, oSelf) {
    var elCell = oEditor.cell;
    var oRecord = oEditor.record;
    var oColumn = oEditor.column;
    var elContainer = oEditor.container;
    var value = oEditor.value ? oEditor.value.id : -1;

    var elDropdown = elContainer.appendChild(document.createElement("select"));
    
    YAHOO.GDMA.utilities.populateDropDown(
            elDropdown, 
            YAHOO.GDMA.admin.servers.connectionTypes, 
            "id", 
            "name", 
            value, 
            true);
    
    // set up a listener to track the input value
    YAHOO.util.Event.addListener(elDropdown, "change", function() {
        oSelf._oCellEditor.value = YAHOO.GDMA.admin.servers.connectionTypes[elDropdown.selectedIndex - 1];
        oSelf.fireEvent("editorUpdateEvent", {
            editor :oSelf._oCellEditor
        });
    });

    // Focus the dropdown
    oSelf._focusEl(elDropdown);
};





//Need a custom edit drop down to deal with referenced table
YAHOO.GDMA.admin.servers.editActiveServerDropdown = function(oEditor, oSelf) {
    var elCell = oEditor.cell;
    var oRecord = oEditor.record;
    var oColumn = oEditor.column;
    var elContainer = oEditor.container;
    //var value = oEditor.value ? oEditor.value.id : -1;

    var elDropdown2 = elContainer.appendChild(document.createElement("select"));
    
    var activeId = new Array();
    var activeValue = new Array();
    activeId[0] = 1;
    activeId[1] = 0;
    activeValue[0] = "true";
    activeValue[1] = "false";
    YAHOO.GDMA.utilities.populateDropDown2(
            elDropdown2, 
            activeId, 
            activeValue, 
            1,
            2,
            -1, 
            true);
    
    // set up a listener to track the input value
    YAHOO.util.Event.addListener(elDropdown2, "change", function() {
        oSelf._oCellEditor.value = activeValue[elDropdown2.selectedIndex - 1];//elDropdown2.options[elDropdown2.selectedIndex].value;
        oSelf.fireEvent("editorUpdateEvent", {
            editor :oSelf._oCellEditor
        });
    });

    // Focus the dropdown
    oSelf._focusEl(elDropdown2);
};


//define the tables columns
YAHOO.GDMA.admin.servers.columnDefs = [ {
        label :"",
        formatter : YAHOO.GDMA.utilities.rownumFormatter,
        minWidth: 20
    },{
        key :"name",
        label :"Name",
        editor :"textbox",
        sortable:true,
        resizeable:true, 
        width:75
    }, {
        key :"username",
        label :"Username",
        editor :"textbox",
        sortable:true,
        resizeable:true, 
        width:75
    }, {
        key :"password",
        label :"Password",
        editor :"textbox",
        formatter : YAHOO.GDMA.utilities.passwordFormatter,
        resizeable:true, 
        width:75
    }, {
        key :"connectionUrl",
        label :"Connection URL",
        editor :"textbox",
        resizeable:true, 
        width:200
    }, {
        key :"connectionType",
        label :"Connection Type",
        formatter : YAHOO.GDMA.admin.servers.connectionTypeFormatter,
        editor : YAHOO.GDMA.admin.servers.editDropdown,
        resizeable:true, 
        width:100
    }, {
        key :"prefix",
        label :"Prefix",
        editor :"textbox",
        resizeable:true, 
        width:50
    }, {
        key :"active",
        label :"Active",
        formatter : YAHOO.GDMA.admin.servers.activeFormatter,
        editor : YAHOO.GDMA.admin.servers.editActiveServerDropdown,
        resizeable:true, 
        width:50
} ];
