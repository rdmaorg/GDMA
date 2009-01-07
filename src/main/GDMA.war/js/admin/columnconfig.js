// Create a namespace for this code
YAHOO.namespace("GDMA.admin.columnconfig");

// Load the list of tables
YAHOO.GDMA.admin.columnconfig.loadList = function(serverid, tableId, tableName ) {
    YAHOO.GDMA.admin.columnconfig.serverid = serverid;
    YAHOO.GDMA.admin.columnconfig.tableId = tableId;
    YAHOO.GDMA.admin.columnconfig.tableName = tableName;
    YAHOO.GDMA.admin.columnconfig.initLayout();
};


YAHOO.GDMA.admin.columnconfig.displayList = function(columns){
    YAHOO.GDMA.admin.columnconfig.columns =  columns;
    var divDlgPopupFormLeft = YAHOO.util.Dom.get("divDlgPopupFormLeft");
    divDlgPopupFormLeft.innerHTML += "<SELECT size=\"20\" id=\"lstColumns\" />";
    var lstColumns  = YAHOO.util.Dom.get("lstColumns");
    for ( var i = 0; i < columns.length; i++) {
        var elOption = document.createElement("option");
        elOption.value = columns[i]["id"];
        elOption.text = columns[i]["name"];
        lstColumns.options[i] = elOption;
    }
};

YAHOO.GDMA.admin.columnconfig.moveItemUp = function(){
    var lstColumns  = YAHOO.util.Dom.get("lstColumns");
    
    if(lstColumns.selectedIndex == -1 ){
        YAHOO.GDMA.dialog.showInfoDialog("No column selected ...", "Please select the a column");
    } else{
        YAHOO.GDMA.admin.columnconfig.moveItem(lstColumns, lstColumns.selectedIndex, lstColumns.selectedIndex - 1 );
    }
};

YAHOO.GDMA.admin.columnconfig.moveItemDown = function(){
    var lstColumns  = YAHOO.util.Dom.get("lstColumns");
    
    if(lstColumns.selectedIndex == -1 ){
        YAHOO.GDMA.dialog.showInfoDialog("No column selected ...", "Please select the a column");
    } else{
        YAHOO.GDMA.admin.columnconfig.moveItem(lstColumns, lstColumns.selectedIndex, lstColumns.selectedIndex + 1 );
    }
};

YAHOO.GDMA.admin.columnconfig.moveItemTop = function(){
    var lstColumns  = YAHOO.util.Dom.get("lstColumns");
    
    if(lstColumns.selectedIndex == -1 ){
        YAHOO.GDMA.dialog.showInfoDialog("No column selected ...", "Please select the a column");
    } else{
        while(lstColumns.selectedIndex > 0){
            YAHOO.GDMA.admin.columnconfig.moveItem(lstColumns, lstColumns.selectedIndex, lstColumns.selectedIndex - 1 );
        }
    }
};

YAHOO.GDMA.admin.columnconfig.moveItemBottom = function(){
    var lstColumns  = YAHOO.util.Dom.get("lstColumns");
    
    if(lstColumns.selectedIndex == -1 ){
        YAHOO.GDMA.dialog.showInfoDialog("No column selected ...", "Please select the a column");
    } else{
        while(lstColumns.selectedIndex <  (lstColumns.options.length -1)){
            YAHOO.GDMA.admin.columnconfig.moveItem(lstColumns, lstColumns.selectedIndex, lstColumns.selectedIndex + 1 );
        }
    }
};

YAHOO.GDMA.admin.columnconfig.moveItem = function(lstColumns, oldIdx, newIdx){
    try{
    if(newIdx < lstColumns.options.length && newIdx >= 0){
        var tmpValue = lstColumns.options[oldIdx].value;
        var tmpText = lstColumns.options[oldIdx].text;
        lstColumns.options[oldIdx].value =  lstColumns.options[newIdx].value;
        lstColumns.options[oldIdx].text =  lstColumns.options[newIdx].text;
        lstColumns.options[newIdx].value = tmpValue;
        lstColumns.options[newIdx].text = tmpText;
        lstColumns.selectedIndex = newIdx;
    }
    }catch(e){alert(e)}
};

YAHOO.GDMA.admin.columnconfig.save = function(){
    var handleYes = function() {
        var lstColumns  = YAHOO.util.Dom.get("lstColumns");
        
        var columns = YAHOO.GDMA.admin.columnconfig.columns;
        
        for ( var i = 0; i < lstColumns.options.length; i++) {
            var id = lstColumns.options[i].value;
            for(var j = 0; j < columns.length; j++){
                if(columns[j].id == id ){
                    columns[j].orderby = i;
                    break;
                }
            }
        }

        GdmaAdmin.saveColumns(columns, function() {
            YAHOO.GDMA.dialog.showInfoDialog("Saved!", "Record(s) saved!");
            YAHOO.GDMA.admin.refreshColumns();
            YAHOO.GDMA.admin.columnconfig.cancel();
            });
    };
    YAHOO.GDMA.dialog.showYesNoDialog(handleYes, "Please confirm save", "Are you sure you wish to save the changes?");
};

YAHOO.GDMA.admin.columnconfig.cancel = function(){
    YAHOO.GDMA.admin.columnconfig.panel.destroy();
};

YAHOO.GDMA.admin.columnconfig.initLayout = function (){
    //get or create the container element for the panel
    var divDlgPopupForm = YAHOO.util.Dom.get("divDlgPopupForm");
    if(!divDlgPopupForm){
        divDlgPopupForm = document.createElement("div")    
        divDlgPopupForm.id = "divDlgPopupForm";
        document.body.insertBefore(divDlgPopupForm, document.body.firstChild);
    }
    
    // the actual panel
    YAHOO.GDMA.admin.columnconfig.panel = new YAHOO.widget.Panel('divDlgPopupForm', {
        draggable: true,
        close: false,
        modal: true,
        width: '308px',
        xy: [140, 100]
    });
    
    YAHOO.GDMA.admin.columnconfig.panel.setHeader('Column Order for table ' + YAHOO.GDMA.admin.columnconfig.tableName );
    YAHOO.GDMA.admin.columnconfig.panel.setBody('<div id="divDlgPopupFormLayout"></div>');
    YAHOO.GDMA.admin.columnconfig.panel.beforeRenderEvent.subscribe(function() {
        try{
            YAHOO.util.Event.onAvailable('divDlgPopupFormLayout', function() {
                var divDlgPopupFormLayout = YAHOO.util.Dom.get("divDlgPopupFormLayout");
                
                var divDlgPopupFormLeft = YAHOO.GDMA.utilities.createElement("div","divDlgPopupFormLeft",divDlgPopupFormLayout);
                var divDlgPopupFormCenterButtons = YAHOO.GDMA.utilities.createElement("div","divDlgPopupFormCenterButtons",divDlgPopupFormLayout);
                var divDlgPopupFormBottom = YAHOO.GDMA.utilities.createElement("div","divDlgPopupFormBottom",divDlgPopupFormLayout);
                
                
            YAHOO.GDMA.admin.columnconfig.layout = new YAHOO.widget.Layout('divDlgPopupFormLayout', {
                height: 400,
                width: 300,
                units: [
                    { position: 'left', width: 150, height: 60, body: 'divDlgPopupFormLeft', header: 'User Without Access' },
                    { position: 'center', width: 100, height: 60, body: 'divDlgPopupFormCenterButtons' },
                    { position: 'bottom', height: 40, body: 'divDlgPopupFormBottom' }
                ]
            });

            YAHOO.GDMA.admin.columnconfig.layout.render();
            
            var divButtons = YAHOO.util.Dom.get("divDlgPopupFormCenterButtons");
            
            var btnMoveTop = YAHOO.GDMA.toolbar.createToolBarButton("Top","btnMoveTop",divButtons, YAHOO.GDMA.admin.columnconfig.moveItemTop, "Move column to top", "show" );            
            var btnMoveUp = YAHOO.GDMA.toolbar.createToolBarButton("Up","btnMoveUp",divButtons, YAHOO.GDMA.admin.columnconfig.moveItemUp, "Move column up", "show" );            
            var btnMoveDown = YAHOO.GDMA.toolbar.createToolBarButton("Down", "btnMoveDown", divButtons, YAHOO.GDMA.admin.columnconfig.moveItemDown, "Move column down", "show" );
            var btnMoveBottom = YAHOO.GDMA.toolbar.createToolBarButton("Bottom","btnMoveBottom",divButtons, YAHOO.GDMA.admin.columnconfig.moveItemBottom, "Move column to bottom", "show" );
            
            divButtons = YAHOO.util.Dom.get("divDlgPopupFormBottom");
            var btnPopupFormSave = YAHOO.GDMA.toolbar.createToolBarButton("Save", "btnPopupFormSave", divButtons, YAHOO.GDMA.admin.columnconfig.save, "Save changes to the column order", "show" );
            var btnPopupFormCancel = YAHOO.GDMA.toolbar.createToolBarButton("Cancel","btnPopupFormCancel", divButtons, YAHOO.GDMA.admin.columnconfig.cancel, "Close this dialog without saving changes to the column order", "show" );
            GdmaAdmin.getColumnsForTable( YAHOO.GDMA.admin.columnconfig.serverid, YAHOO.GDMA.admin.columnconfig.tableId, function(columns) {
                YAHOO.GDMA.admin.columnconfig.displayList(columns);
            });  
        });
        }catch(e){alert(e)}
    });    
    YAHOO.GDMA.admin.columnconfig.panel.render();
};


