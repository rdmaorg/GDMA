// Create a namespace for this code
YAHOO.namespace("GDMA.admin.access");

// Load the list of tables
YAHOO.GDMA.admin.access.loadList = function(tableId, tableName ) {
	YAHOO.GDMA.admin.access.tableId = tableId;
    YAHOO.GDMA.admin.access.tableName = tableName;
    YAHOO.GDMA.admin.access.initLayout();
};


YAHOO.GDMA.admin.access.displayLists = function(userAccessList){
	YAHOO.GDMA.admin.access.userAccessList =  userAccessList;
	
	var divDlgAccessCenter = YAHOO.util.Dom.get("divDlgAccessCenter");
    
    divDlgAccessCenter.innerHTML += "<BR>&nbsp;<BR><BR>&nbsp;<BR>&nbsp;<BR>";
    //create a new table with the 
	var userAccessTable = "<div align=\"center\" id=\"userAccessDiv\" class=\"height: 100px; overflow-y: scroll;  padding: 30px; width: 100%;\"><table id=\"userAccessTable\" overflow-y=\"scroll\"  cellpadding=\"10\" width=\"90%\">";
	userAccessTable += "<tr bgcolor=\"#C0C0C0\"><th><b>User</b></th><th><b>Display</b></th><th><b>Update</b></th><th><b>Insert</b></th><th><b>Delete</b></th></tr>";
	
	YAHOO.GDMA.admin.access.userIdList = new Array();
	//loop through the users and get the access levels for the table
	for(var i = 0; i < userAccessList.length; i++)
	{
		YAHOO.GDMA.admin.access.userIdList[i] = userAccessList[i]["user"]["id"]
		var userName = userAccessList[i]["user"]["userName"];
		
		var allowDisplayId = "chkDisplay" + userAccessList[i]["user"]["id"];
		var allowUpdateId = "chkUpdate" + userAccessList[i]["user"]["id"];
		var allowInsertId = "chkInsert" + userAccessList[i]["user"]["id"];
		var allowDeleteId = "chkDelete" + userAccessList[i]["user"]["id"];
		
		if((i % 2) == 0)
    	{
			userAccessTable += "<tr bgcolor=\"#EDF5FF\"><td align=\"left\" >" + userName + "</td>";
			if(userAccessList[i]["allowDisplay"])
				userAccessTable += "<td align=\"left\" ><INPUT type=\"checkbox\"  id=\"" + allowDisplayId + "\" checked /></td>";
			else
				userAccessTable += "<td align=\"left\" ><INPUT type=\"checkbox\"  id=\"" + allowDisplayId + "\" /></td>";
			if(userAccessList[i]["allowUpdate"])
				userAccessTable += "<td align=\"left\" ><INPUT type=\"checkbox\" id=\"" + allowUpdateId + "\" checked /></td>";
			else
				userAccessTable += "<td align=\"left\" ><INPUT type=\"checkbox\" id=\"" + allowUpdateId + "\" /></td>";
			if(userAccessList[i]["allowInsert"])
				userAccessTable += "<td align=\"left\" ><INPUT type=\"checkbox\" id=\"" + allowInsertId + "\" checked /></td>";
			else
				userAccessTable += "<td align=\"left\" ><INPUT type=\"checkbox\" id=\"" + allowInsertId + "\" /></td>";
			if(userAccessList[i]["allowDelete"])
				userAccessTable += "<td align=\"left\" ><INPUT type=\"checkbox\" id=\"" + allowDeleteId + "\" checked /></td>";
			else
				userAccessTable += "<td align=\"left\" ><INPUT type=\"checkbox\" id=\"" + allowDeleteId + "\" /></td>";																								                                  														
    	
    	}
    	else
    	{
			userAccessTable += "<tr bgcolor=\"#FFFFFF\"><td align=\"left\" >" + userName + "</td>";
    		if(userAccessList[i]["allowDisplay"])
    			userAccessTable += "<td align=\"left\" ><INPUT type=\"checkbox\"  id=\"" + allowDisplayId + "\" checked /></td>";
    		else
    			userAccessTable += "<td align=\"left\" ><INPUT type=\"checkbox\"  id=\"" + allowDisplayId + "\" /></td>";
    		if(userAccessList[i]["allowUpdate"])
    			userAccessTable += "<td align=\"left\" ><INPUT type=\"checkbox\" id=\"" + allowUpdateId + "\" checked /></td>";
    		else
    			userAccessTable += "<td align=\"left\" ><INPUT type=\"checkbox\" id=\"" + allowUpdateId + "\" /></td>";
    		if(userAccessList[i]["allowInsert"])
    			userAccessTable += "<td align=\"left\" ><INPUT type=\"checkbox\" id=\"" + allowInsertId + "\" checked /></td>";
    		else
    			userAccessTable += "<td align=\"left\" ><INPUT type=\"checkbox\" id=\"" + allowInsertId + "\" /></td>";
    		if(userAccessList[i]["allowDelete"])
    			userAccessTable += "<td align=\"left\" ><INPUT type=\"checkbox\" id=\"" + allowDeleteId + "\" checked /></td>";
    		else
    			userAccessTable += "<td align=\"left\" ><INPUT type=\"checkbox\" id=\"" + allowDeleteId + "\" /></td>";	
    	}
	}
	userAccessTable += "</table></div>";                    	
	divDlgAccessCenter.innerHTML += userAccessTable;
	
	
	divDlgAccessCenter.innerHTML += "<BR>&nbsp;<BR><BR>&nbsp;<BR>&nbsp;<BR>";    
    
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
    	var users = GdmaAdmin.getUserList();
    	 var userIdListLength = YAHOO.GDMA.admin.access.userIdList.length;
        var userAccess = {};
        for ( var i = 0; i < userIdListLength; i++) {
        	
        	
        	userAccess["tableId"] = YAHOO.GDMA.admin.access.tableId;
        	userAccess["userId"] = YAHOO.GDMA.admin.access.userIdList[i];   
            userAccess["allowDisplay"] = document.getElementById("chkDisplay" + YAHOO.GDMA.admin.access.userIdList[i]).checked;//"chkDisplay" + YAHOO.GDMA.admin.access.userIdList[i];
            userAccess["allowUpdate"] = document.getElementById("chkUpdate" + YAHOO.GDMA.admin.access.userIdList[i]).checked; 
            userAccess["allowInsert"] = document.getElementById("chkInsert" + YAHOO.GDMA.admin.access.userIdList[i]).checked; 
            userAccess["allowDelete"] = document.getElementById("chkDelete" + YAHOO.GDMA.admin.access.userIdList[i]).checked; 
            
            GdmaAdmin.saveAccessList(userAccess, function() {
            });
        }
        YAHOO.GDMA.dialog.showInfoDialog("Saved!", "Record(s) saved!");
        YAHOO.GDMA.admin.access.cancel();
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
    
    YAHOO.GDMA.admin.access.panel.setHeader('User Access for table ' + YAHOO.GDMA.admin.access.tableName );
    YAHOO.GDMA.admin.access.panel.setBody('<div id="divDlgAccessLayout"></div>');
    YAHOO.GDMA.admin.access.panel.beforeRenderEvent.subscribe(function() {
        try{
            YAHOO.util.Event.onAvailable('divDlgAccessLayout', function() {
                var divDlgAccessLayout = YAHOO.util.Dom.get("divDlgAccessLayout");
                
                /*var divDlgAccessLeft = YAHOO.GDMA.utilities.createElement("div","divDlgAccessLeft",divDlgAccessLayout);
                var divDlgAccessRight = YAHOO.GDMA.utilities.createElement("div","divDlgAccessRight",divDlgAccessLayout);*/
                var divDlgAccessCenter = YAHOO.GDMA.utilities.createElement("div","divDlgAccessCenter",divDlgAccessLayout);
                var divDlgAccessBottom = YAHOO.GDMA.utilities.createElement("div","divDlgAccessBottom",divDlgAccessLayout);
                
                
            YAHOO.GDMA.admin.access.layout = new YAHOO.widget.Layout('divDlgAccessLayout', {
                height: 300,
                width: 430,
                units: [
                    //{ position: 'left', width: 150, height: 60, body: 'divDlgAccessLeft', header: 'User Without Access' },
                    { position: 'center', width: 100, height: 60, body: 'divDlgAccessCenter' },
                    //{ position: 'right', width: 150, height: 60, body: 'divDlgAccessRight', header: 'User With Access'  },
                    { position: 'bottom', height: 40, body: 'divDlgAccessBottom' }
                ]
            });

            YAHOO.GDMA.admin.access.layout.render();
            
            /*var divButtons = YAHOO.util.Dom.get("divDlgAccessCenter");
            
            var btnMoveRight = YAHOO.GDMA.toolbar.createToolBarButton("Grant All","btnAllMoveRight",divButtons, YAHOO.GDMA.admin.access.moveAllItemToRight, "Grant access to all users", "show" );            
            var btnMoveRight = YAHOO.GDMA.toolbar.createToolBarButton("Grant","btnMoveRight",divButtons, YAHOO.GDMA.admin.access.moveItemToRight, "Grant access to selected user", "show" );            
            var btnMoveLeft = YAHOO.GDMA.toolbar.createToolBarButton("Revoke", "btnMoveLeft", divButtons, YAHOO.GDMA.admin.access.moveItemToLeft, "Revoke access to selected user", "show" );
            var btnMoveRight = YAHOO.GDMA.toolbar.createToolBarButton("Revoke All","btnAllMoveLeft",divButtons, YAHOO.GDMA.admin.access.moveAllItemToLeft, "Revoke access to all users", "show" );
            */
            divButtons = YAHOO.util.Dom.get("divDlgAccessBottom");
            var btnSaveAccessList = YAHOO.GDMA.toolbar.createToolBarButton("Save", "btnAccessListSave", divButtons, YAHOO.GDMA.admin.access.save, "Save changes to the access list", "show" );
            var btnCancelAccessList = YAHOO.GDMA.toolbar.createToolBarButton("Cancel","btnAccessListCancel", divButtons, YAHOO.GDMA.admin.access.cancel, "Close this dialog without saving changes", "show" );
            
            GdmaAdmin.getAccessListForTable(YAHOO.GDMA.admin.access.tableId,  function(userAccessList) {
                YAHOO.GDMA.admin.access.displayLists(userAccessList);                
            });  
        });
        }catch(e){alert(e)}
    });    
    YAHOO.GDMA.admin.access.panel.render();
};


