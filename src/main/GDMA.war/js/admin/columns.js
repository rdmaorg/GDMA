// Create a namespace for this code
YAHOO.namespace("GDMA.admin.columns");
YAHOO.namespace("GDMA.admin.columns.serverbrowser");

// define the datasource fields
YAHOO.GDMA.admin.columns.fields = {
        fields : [ 
                  {key:"id", parser:YAHOO.util.DataSource.parseNumber},
                  {key:"name", parser:YAHOO.util.DataSource.parseString},
                  {key:"columnTypeString", parser:YAHOO.util.DataSource.parseString},
                  {key:"primarykey"},
                  {key:"displayed"},
                  {key:"allowInsert"},
                  {key:"allowUpdate"},
                  {key:"nullable"},
                  {key:"dropDownColumnDisplay"},
                  {key:"dropDownColumnStore"},
                  {key:"special"},
                  {key:"minWidth"},
                  {key:"maxWidth"},
                  {key:"orderby"},
                  {key:"columnSize"},
                  {key:"active"}
                  ]
};

YAHOO.GDMA.admin.columns.yesnoFormatter = function(elCell, oRecord, oColumn, oData) {
    if (oData == true) {
        elCell.innerHTML = "Yes";
    } else {
        elCell.innerHTML = "No";
    }
};

YAHOO.GDMA.admin.columns.ddTypeFormatter = function(elCell, oRecord, oColumn, oData) {
    if (oData) {
        elCell.innerHTML = oData.name;
    }else{
        elCell.innerHTML = "[NULL]";
    }
};

YAHOO.GDMA.admin.columns.editDDLookup = function (oEditor, oSelf){
    YAHOO.GDMA.admin.dataTable.cancelCellEditor();
    
    //create the container element for the panel
    var divDlgAccess = document.createElement("div")
    divDlgAccess.id = "divDlgColumns";
    document.body.insertBefore(divDlgAccess, document.body.firstChild);
    
    // the actual panel
    YAHOO.GDMA.admin.columns.panel = new YAHOO.widget.Panel('divDlgColumns', {
        draggable: true,
        close: false,
        modal: true,
        width: '438px',
        height: '400px',
        xy: [220, 100]
    });
    
    YAHOO.GDMA.admin.columns.clear  = function(){
        var elCell = oEditor.cell;
        var oRecord = oEditor.record;
        var oColumn = oEditor.column;
        
        YAHOO.GDMA.admin.dataTable.getRecordSet().updateRecordValue(oRecord, "dropDownColumnDisplay", null);
        YAHOO.GDMA.admin.dataTable.getRecordSet().updateRecordValue(oRecord, "dropDownColumnStore", null);
        YAHOO.GDMA.admin.dataTable.render();
        YAHOO.GDMA.admin.columns.cancel();
    };
    
    YAHOO.GDMA.admin.columns.cancel = function(){
        YAHOO.GDMA.admin.columns.panel.destroy();
    };

    YAHOO.GDMA.admin.columns.save = function(){
        //validation
        var ddDisplay = YAHOO.util.Dom.get("ddDisplay")
        var ddStore = YAHOO.util.Dom.get("ddStore")
        
        if(ddDisplay.selectedIndex < 1 || ddStore.selectedIndex < 1){
            YAHOO.GDMA.dialog.showInfoDialog("Error!", "Please select both a column to store and a column to display" , YAHOO.widget.SimpleDialog.ICON_BLOCK);
        }
        
        var elCell = oEditor.cell;
        var oRecord = oEditor.record;
        var oColumn = oEditor.column;
        
        YAHOO.GDMA.admin.dataTable.getRecordSet().updateRecordValue(oRecord, "dropDownColumnDisplay", YAHOO.GDMA.admin.columns.ddColumnsList[ddDisplay.options[ddDisplay.selectedIndex].value]);
        YAHOO.GDMA.admin.dataTable.getRecordSet().updateRecordValue(oRecord, "dropDownColumnStore", YAHOO.GDMA.admin.columns.ddColumnsList[ddStore.options[ddStore.selectedIndex].value]);
        YAHOO.GDMA.admin.dataTable.render();
        YAHOO.GDMA.admin.columns.cancel();
        
    };
    
    YAHOO.GDMA.admin.columns.panel.setHeader('Configure Dropdown for Column - ' + oEditor.record.getData("name") );
    YAHOO.GDMA.admin.columns.panel.setBody('<div id="divDlgColumnsLayout"></div>');
    YAHOO.GDMA.admin.columns.panel.beforeRenderEvent.subscribe(function() {
        try{
            YAHOO.util.Event.onAvailable('divDlgColumnsLayout', function() {
                
                var divDlgColumnsLayout = YAHOO.util.Dom.get("divDlgColumnsLayout");
                
                var divDlgColumnsLayoutTreeview = YAHOO.GDMA.utilities.createElement("div","divDlgColumnsLayoutTreeview",divDlgColumnsLayout);
                var divDlgColumnsLayoutDropdowns = YAHOO.GDMA.utilities.createElement("div","divDlgColumnsLayoutDropdowns",divDlgColumnsLayout);
                var divDlgColumnsLayoutButtons = YAHOO.GDMA.utilities.createElement("div","divDlgColumnsLayoutButtons",divDlgColumnsLayout);
                YAHOO.util.Dom.addClass(divDlgColumnsLayoutButtons, "gdma-toolbar");
                
                GdmaAdmin.getServerTableColumnList(function(serverList) {
                    YAHOO.GDMA.admin.columns.serverbrowser.serverList = serverList;
                    YAHOO.GDMA.admin.columns.serverbrowser.tree = new YAHOO.widget.TreeView("divDlgColumnsLayoutTreeview");
                    var root = YAHOO.GDMA.admin.columns.serverbrowser.tree.getRoot();
                    for(i=0;i<serverList.length;i++){
                        var serverNode = new YAHOO.widget.TextNode(serverList[i].name, root, false);
                        serverNode.labelStyle = "icon-server"; 
                         
                        for(j=0;j<serverList[i].tables.length;j++){
                            var nodeObject = { 
                                        label:      serverList[i].tables[j].name, 
                                        serveridx:  i,
                                        tableidx:   j
                            };
                            var tableNode = new YAHOO.widget.TextNode(nodeObject, serverNode, false);
                            tableNode.labelStyle = "icon-table"; 
                            for(k=0;k<serverList[i].tables[j].columns.length;k++){
                                var nodeObject = { 
                                        label:      serverList[i].tables[j].columns[k].name, 
                                        serveridx:  i,
                                        tableidx:   j,
                                        columnidx:  k
                                };
                                var columnNode = new YAHOO.widget.TextNode(nodeObject, tableNode, false);
                                columnNode.labelStyle = "icon-column"; 
                            }
                        }
                    }
                    YAHOO.GDMA.admin.columns.serverbrowser.tree.subscribe("labelClick", function(node) {
                        if(!isNaN(node.data.tableidx)){
                            var ddDisplay = YAHOO.util.Dom.get("ddDisplay")
                            var ddStore = YAHOO.util.Dom.get("ddStore")
                            
                            ddDisplay.options.length = 0;
                            ddStore.options.length = 0;
                            
                            var elOption = document.createElement("option");
                            elOption.value = -1;
                            elOption.text = "Column To Display";
                            ddDisplay.options[0] = elOption;
                            
                            elOption = document.createElement("option");
                            elOption.text = "Column To Store";
                            elOption.value = -1;
                            ddStore.options[0] = elOption;
                            
                            // get columns for selected table
                            var columns = YAHOO.GDMA.admin.columns.serverbrowser.serverList[node.data.serveridx].tables[node.data.tableidx].columns;
                            YAHOO.GDMA.admin.columns.ddColumnsList = {};
                            //first record is null - makes thing handy!!
                            YAHOO.GDMA.admin.columns.ddColumnsList[-1] = null;
                            for ( var i = 0; i < columns.length; i++) {
                                var elOption = document.createElement("option");
                                elOption.value = columns[i].id;
                                elOption.text = columns[i].name;
                                ddDisplay.options[i + 1 ] = elOption;
                                
                                elOption = document.createElement("option");
                                elOption.value = columns[i].id;
                                elOption.text = columns[i].name;
                                ddStore.options[i + 1] = elOption;
                                YAHOO.GDMA.admin.columns.ddColumnsList[columns[i].id] = columns[i];
                            }
                        }else{
                            var ddDisplay = YAHOO.util.Dom.get("ddDisplay")
                            var ddStore = YAHOO.util.Dom.get("ddStore")
                            
                            ddDisplay.options.length = 0;
                            ddStore.options.length = 0;
                            
                            var elOption = document.createElement("option");
                            elOption.text = "Click a Table";
                            ddDisplay.options[0] = elOption;
                            
                            elOption = document.createElement("option");
                            elOption.text = "Click a Table";
                            ddStore.options[0] = elOption;

                        }
                    }); 
                    YAHOO.GDMA.admin.columns.serverbrowser.tree.draw();
                });

                //create empty drop downs for server browser
                
                var ddDisplay = YAHOO.GDMA.utilities.createElement("select","ddDisplay",divDlgColumnsLayoutDropdowns);
                var elOption = document.createElement("option");
                elOption.text = "Click a Table";
                ddDisplay.appendChild(elOption)
                
                var ddStore = YAHOO.GDMA.utilities.createElement("select","ddStore",divDlgColumnsLayoutDropdowns);
                elOption = document.createElement("option");
                elOption.text = "Click a Table";
                ddStore.appendChild(elOption);
                
                // add save and cancel buttons to the server browser
                YAHOO.GDMA.toolbar.createToolBarButton("Clear", "btnColumnServerBrowserClear", divDlgColumnsLayoutButtons, YAHOO.GDMA.admin.columns.clear, "Remove columns from DD cells", "show" );                
                YAHOO.GDMA.toolbar.createToolBarButton("Cancel","btnColumnServerBrowserCancel", divDlgColumnsLayoutButtons, YAHOO.GDMA.admin.columns.cancel, "Close this dialog without saving changes", "show" );
                YAHOO.GDMA.toolbar.createToolBarButton("Ok", "btnColumnServerBrowserSave", divDlgColumnsLayoutButtons, YAHOO.GDMA.admin.columns.save, "Update record with selected columns", "show" );
        });
        }catch(e){alert(e)}
    });    
    YAHOO.GDMA.admin.columns.panel.render();
};

// define the columns columns
YAHOO.GDMA.admin.columns.columnDefs = [ {
        label :"",
        formatter : YAHOO.GDMA.utilities.rownumFormatter,
        minWidth: 20
    },{
            key :"name",
            label :"Name",
            sortable:true,
            resizeable:true, 
            width:170
        }, {
            key :"columnTypeString",
            label :"Type",
            sortable:true,
            resizeable:true, 
            width:65
        }, {
            key :"primarykey",                
            label : "Primary Key",
            formatter:"checkbox",
            resizeable:true, 
            width:75
        }, {
            key :"displayed",                
            label : YAHOO.GDMA.utilities.createCheckAllHeader("Displayed"),
            formatter:"checkbox",
            resizeable:true, 
            width:85
        }, {
            key :"allowInsert",
            label : YAHOO.GDMA.utilities.createCheckAllHeader("Allow Insert"),
            formatter:"checkbox",
            resizeable:true, 
            width:85
        }, {
            key :"allowUpdate",
            label : YAHOO.GDMA.utilities.createCheckAllHeader("Allow Update"),
            formatter:"checkbox",
            resizeable:true, 
            width:85
        }, {
            key :"nullable",
            label :"Allow Null",
            formatter:YAHOO.GDMA.admin.columns.yesnoFormatter,
            resizeable:true, 
            width:60
        }, {
            key :"dropDownColumnDisplay",
            label :"DD Display",
            formatter:YAHOO.GDMA.admin.columns.ddTypeFormatter,
            editor : YAHOO.GDMA.admin.columns.editDDLookup,
            editorOptions : { disableBtns: true },
            resizeable:true
        }, {
            key :"dropDownColumnStore",
            formatter:YAHOO.GDMA.admin.columns.ddTypeFormatter,
            editor : YAHOO.GDMA.admin.columns.editDDLookup,
            editorOptions : { disableBtns: true },
            label :"DD Store",
            resizeable:true
        }, {
            key :"special",
            label :"Special",
            resizeable:true,
            formatter:"dropdown", 
            dropdownOptions:[
                             {value: "N", text: "No"},
                             {value: "U", text: "User"},
                             {value: "D", text: "Date"}],
            width:75
        },{
            key :"minWidth",
            label :"Min Width",
            editor :"textbox",
            editorOptions: {
                validator: YAHOO.GDMA.utilities.validateNumber
            },
            width:60
        },{
            key :"maxWidth",
            label :"Max Width",
            editor :"textbox",
            editorOptions: {
                validator: YAHOO.GDMA.utilities.validateNumber
            },
            width:60
        }
        
        ,{
            key :"columnSize",
            label :"Column Size",
            editor :"textbox",
            editorOptions: {
                validator: YAHOO.GDMA.utilities.validateNumber
            },
            width:60
        }
        
        ];


YAHOO.GDMA.admin.columns.deleteRecord = function(){
    //not allow to delete columns
}

YAHOO.GDMA.admin.columns.saveRecord = function(updateList, callback){
    //do validation
    for(var i = 0; i < updateList.length ; i++){
        if(updateList[i].minWidth > updateList[i].maxWidth){
            YAHOO.GDMA.dialog.showInfoDialog("Validation Error!", "Please ensure that all minWidths are less than maxWidths.",YAHOO.widget.SimpleDialog.ICON_BLOCK);
            return;
        }
        
        if(YAHOO.lang.trim(updateList[i].minWidth) == ""){
            updateList[i].minWidth = null;
        }
        
        if(YAHOO.lang.trim(updateList[i].maxWidth) == ""){
            updateList[i].maxWidth = null;
        }
    }
    GdmaAdmin.saveColumns(updateList, callback);
};

YAHOO.GDMA.admin.columns.doubleClick = function(){
    
};


// Load the list of columns
YAHOO.GDMA.admin.columns.loadColumns = function(serverId, tableId, fnCallback ) {
  GdmaAdmin.getColumnsForTable( serverId, tableId, function(columns) {
      // setup the object backing the column
      var datasource = new YAHOO.util.DataSource(columns);
      datasource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;        
      // field names
      datasource.responseSchema = YAHOO.GDMA.admin.columns.fields;        
      datasource.columnDefs = YAHOO.GDMA.admin.columns.columnDefs;
      fnCallback(datasource);
  });
  // setup the functions need
  YAHOO.GDMA.admin.saveFunction =  YAHOO.GDMA.admin.columns.saveRecord;
  YAHOO.GDMA.admin.deleteFunction = YAHOO.GDMA.admin.columns.deleteRecord;
  YAHOO.GDMA.admin.doubleClick = YAHOO.GDMA.admin.columns.doubleClick;
}


