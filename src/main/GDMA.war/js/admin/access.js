// Create a namespace for this code
YAHOO.namespace("GDMA.admin.access");

// Load the list of tables
YAHOO.GDMA.admin.access.loadList = function(tableId, tableName ) {
    YAHOO.GDMA.admin.access.tableId = tableId;
    YAHOO.GDMA.admin.access.tableName = tableName;
    YAHOO.GDMA.admin.access.initLayout();
};


YAHOO.GDMA.admin.access.displayLists = function(accessList){
    YAHOO.GDMA.admin.access.accessList =  accessList;
    var usersWithAccess = accessList["canAccess"];
    var usersWithOutAccess = accessList["canNotAccess"];
    var divDlgAccessRight = YAHOO.util.Dom.get("divDlgAccessRight");
    var divDlgAccessLeft = YAHOO.util.Dom.get("divDlgAccessLeft");
    
    divDlgAccessRight.innerHTML += "<SELECT size=\"20\" id=\"lstAccess\" multiple=\"multiple\" />";
    divDlgAccessLeft.innerHTML += "<SELECT size=\"20\" id=\"lstNoAccess\" multiple=\"multiple\" />";
    
    var lstAccess  = YAHOO.util.Dom.get("lstAccess");
    var lstNoAccess  = YAHOO.util.Dom.get("lstNoAccess");
   //var lstAccess = YAHOO.GDMA.utilities.createElement("select","lstAccess",YAHOO.util.Dom.get("divDlgAccessRight"));
    //var lstNoAccess = YAHOO.GDMA.utilities.createElement("select","lstNoAccess",YAHOO.util.Dom.get("divDlgAccessLeft"));
    
    lstAccess.size = 20;
    lstNoAccess.size = 20;
    
    for ( var i = 0; i < usersWithAccess.length; i++) {
        var elOption = document.createElement("option");
        elOption.value = usersWithAccess[i]["id"];
        elOption.text = usersWithAccess[i]["userName"];
        lstAccess.options[i] = elOption;
    }

    for ( var i = 0; i < usersWithOutAccess.length; i++) {
        var elOption = document.createElement("option");
        elOption.value = usersWithOutAccess[i]["id"];
        elOption.text = usersWithOutAccess[i]["userName"];
        lstNoAccess.options[i] = elOption;
    }
    YAHOO.GDMA.dialog.loading.hide();
};

YAHOO.GDMA.admin.access.moveItemToLeft = function(){
    YAHOO.GDMA.admin.access.moveItem("lstAccess","lstNoAccess");    
};

YAHOO.GDMA.admin.access.moveItemToRight = function(){
    YAHOO.GDMA.admin.access.moveItem("lstNoAccess","lstAccess");
};

YAHOO.GDMA.admin.access.moveAllItemToRight = function(){
    YAHOO.GDMA.admin.access.moveAllItems("lstNoAccess","lstAccess");
};

YAHOO.GDMA.admin.access.moveAllItemToLeft = function(){
    YAHOO.GDMA.admin.access.moveAllItems("lstAccess","lstNoAccess");
};

YAHOO.GDMA.admin.access.moveAllItems = function(lstFrom, lstTo){
    var lstFrom  = YAHOO.util.Dom.get(lstFrom);
    var lstTo  = YAHOO.util.Dom.get(lstTo);
    while( lstFrom.options.length > 0){
        var option = lstFrom.options[0]; 
        var elOption = document.createElement("option");
        elOption.value = option.value;
        elOption.text = option.text;
        lstTo.options[lstTo.options.length] = elOption;
        lstFrom.options[0] = null;
    }
};

YAHOO.GDMA.admin.access.moveItem = function(lstFrom, lstTo){
    var lstFrom  = YAHOO.util.Dom.get(lstFrom);
    var lstTo  = YAHOO.util.Dom.get(lstTo);
    
    if(lstFrom.selectedIndex == -1){
        YAHOO.GDMA.dialog.showInfoDialog("No record selected ...", "Please select a user");
    } else{
        //move the item(s)
        while(lstFrom.selectedIndex != -1){
            var option = lstFrom.options[lstFrom.selectedIndex]; 
            var elOption = document.createElement("option");
            elOption.value = option.value;
            elOption.text = option.text;
            lstTo.options[lstTo.options.length] = elOption;
            lstFrom.options[lstFrom.selectedIndex] = null;
        }
    }
};

YAHOO.GDMA.admin.access.save = function(){
    var handleYes = function() {
        var lstAccess  = YAHOO.util.Dom.get("lstAccess");
        var table = {};
        table["id"] = YAHOO.GDMA.admin.access.tableId;
        table["name"] = YAHOO.GDMA.admin.access.tableName;
        table["users"] = [];
        for ( var i = 0; i < lstAccess.options.length; i++) {
            var option = lstAccess.options[i];
            table["users"][i] = {} ;       
            table["users"][i]["id"] = option.value;
            table["users"][i]["userName"] = option.text;
        }
        GdmaAdmin.saveAccessList(table, function() {
            YAHOO.GDMA.dialog.showInfoDialog("Saved!", "Record(s) saved!");
            YAHOO.GDMA.admin.access.cancel();
            });
    };
    YAHOO.GDMA.dialog.showYesNoDialog(handleYes, "Please confirm save", "Are you sure you wish to save the changes?");
};

YAHOO.GDMA.admin.access.cancel = function(){
    YAHOO.GDMA.admin.access.panel.destroy();
};

YAHOO.GDMA.admin.access.initLayout = function (){
    //get or create the container element for the panel
    var divDlgAccess = YAHOO.util.Dom.get("divDlgAccess");
    if(!divDlgAccess){
        divDlgAccess = document.createElement("div")    
        divDlgAccess.id = "divDlgAccess";
        document.body.insertBefore(divDlgAccess, document.body.firstChild);
    }
    
    // the actual panel
    YAHOO.GDMA.admin.access.panel = new YAHOO.widget.Panel('divDlgAccess', {
        draggable: true,
        close: false,
        modal: true,
        width: '438px',
        xy: [140, 100]
    });
    
    YAHOO.GDMA.admin.access.panel.setHeader('User Acess for table ' + YAHOO.GDMA.admin.access.tableName );
    YAHOO.GDMA.admin.access.panel.setBody('<div id="divDlgAccessLayout"></div>');
    YAHOO.GDMA.admin.access.panel.beforeRenderEvent.subscribe(function() {
        try{
            YAHOO.util.Event.onAvailable('divDlgAccessLayout', function() {
                var divDlgAccessLayout = YAHOO.util.Dom.get("divDlgAccessLayout");
                
                var divDlgAccessLeft = YAHOO.GDMA.utilities.createElement("div","divDlgAccessLeft",divDlgAccessLayout);
                var divDlgAccessRight = YAHOO.GDMA.utilities.createElement("div","divDlgAccessRight",divDlgAccessLayout);
                var divDlgAccessCenter = YAHOO.GDMA.utilities.createElement("div","divDlgAccessCenter",divDlgAccessLayout);
                var divDlgAccessBottom = YAHOO.GDMA.utilities.createElement("div","divDlgAccessBottom",divDlgAccessLayout);
                
                
            YAHOO.GDMA.admin.access.layout = new YAHOO.widget.Layout('divDlgAccessLayout', {
                height: 300,
                width: 430,
                units: [
                    { position: 'left', width: 150, height: 60, body: 'divDlgAccessLeft', header: 'User Without Access' },
                    { position: 'center', width: 100, height: 60, body: 'divDlgAccessCenter' },
                    { position: 'right', width: 150, height: 60, body: 'divDlgAccessRight', header: 'User With Access'  },
                    { position: 'bottom', height: 40, body: 'divDlgAccessBottom' }
                ]
            });

            YAHOO.GDMA.admin.access.layout.render();
            
            var divButtons = YAHOO.util.Dom.get("divDlgAccessCenter");
            
            var btnMoveRight = YAHOO.GDMA.toolbar.createToolBarButton("Grant All","btnAllMoveRight",divButtons, YAHOO.GDMA.admin.access.moveAllItemToRight, "Grant access to all users", "show" );            
            var btnMoveRight = YAHOO.GDMA.toolbar.createToolBarButton("Grant","btnMoveRight",divButtons, YAHOO.GDMA.admin.access.moveItemToRight, "Grant access to selected user", "show" );            
            var btnMoveLeft = YAHOO.GDMA.toolbar.createToolBarButton("Revoke", "btnMoveLeft", divButtons, YAHOO.GDMA.admin.access.moveItemToLeft, "Revoke access to selected user", "show" );
            var btnMoveRight = YAHOO.GDMA.toolbar.createToolBarButton("Revoke All","btnAllMoveLeft",divButtons, YAHOO.GDMA.admin.access.moveAllItemToLeft, "Revoke access to all users", "show" );
            
            divButtons = YAHOO.util.Dom.get("divDlgAccessBottom");
            var btnSaveAccessList = YAHOO.GDMA.toolbar.createToolBarButton("Save", "btnAccessListSave", divButtons, YAHOO.GDMA.admin.access.save, "Save changes to the access list", "show" );
            var btnCancelAccessList = YAHOO.GDMA.toolbar.createToolBarButton("Cancel","btnAccessListCancel", divButtons, YAHOO.GDMA.admin.access.cancel, "Close this dialog without saving changes", "show" );
            GdmaAdmin.getAccessListForTable( YAHOO.GDMA.admin.access.tableId, function(accessList) {
                YAHOO.GDMA.admin.access.displayLists(accessList);
            });  
        });
        }catch(e){alert(e)}
    });    
    YAHOO.GDMA.admin.access.panel.render();
};


