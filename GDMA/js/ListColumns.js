function ObjectDDLookup(){}

function toggleColumnLookup(id){
	var o = document.getElementById("trLookup" + id);
	if(o){
		if(o.style.display == "none")
			o.style.display = "block";
		else
			o.style.display = "none";
	}
}

function doDDLookup(id) {
                         
   var propDialog = "dialogHeight:227px;" +
                    "dialogWidth:500px;" +
                    "dialogTop:240;" +
                    "dialogLeft:270;" +
                    "titlebar:no;" +
                    "help:no;" +
                    "toolbars:no;" +
                    "scrollbars:no;" +
                    "status:no;" +
                    "resizable:no;";
                    
   var row = new ObjectDDLookup();
   
   row.server_id = document.getElementById("fldDropDownColumnDisplayServerID" + id);
   row.server_name = document.getElementById("fldDropDownColumnDisplayServerName" + id);
   row.table_id = document.getElementById("fldDropDownColumnDisplayTableID" + id);
   row.table_name = document.getElementById("fldDropDownColumnDisplayTableName" + id);
	row.columnDisplay_id = document.getElementById("fldDropDownColumnDisplayID" + id);
	row.columnDisplay_name = document.getElementById("fldDropDownColumnDisplayName" + id);
   row.columnStored_id = document.getElementById("fldDropDownColumnStoreID" + id);
   row.columnStored_name = document.getElementById("fldDropDownColumnStoreName" + id);
   
	window.showModalDialog('DDLookup.jsp',row,propDialog);
}
