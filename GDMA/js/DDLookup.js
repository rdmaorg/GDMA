var serverid = null;
var tableid = null;
var row = null;

function doOnLoad(){
	serverid = document.getElementById("server_id").value;	
	row = dialogArguments;
	document.getElementById("server_id").value = row.server_id.value;
	doRefreshTableList();
	document.getElementById("table_id").value = row.table_id.value;
	doRefreshColumnList();
	document.getElementById("column_display_id").value = row.columnDisplay_id.value;
	document.getElementById("column_store_id").value = row.columnStored_id.value;
}

function doOK(){
	
	var idx, obj;
	
	obj = document.getElementById("server_id");
    if(validateSelect(obj,"server")){
        idx =  obj.selectedIndex;       
    	row.server_id.value = obj.options[idx].value;
        row.server_name.value =  obj.options[idx].text;
    }else{
        return;
    }
   
    obj = document.getElementById("table_id");
    if(validateSelect(obj,"table")){
    	idx =  obj.selectedIndex;	
        row.table_id.value = obj.options[idx].value;
        row.table_name.value = obj.options[idx].text;
    }else{
        return;
    }
   
    obj = document.getElementById("column_display_id");
    if(validateSelect(obj,"display column")){
    	idx =  obj.selectedIndex;	
    	row.columnDisplay_id.value = obj.options[idx].value;
        row.columnDisplay_name.value = obj.options[idx].text;
    }else{
        return;
    }
	
	obj = document.getElementById("column_store_id");
    if(validateSelect(obj,"store column")){    
        idx =  obj.selectedIndex;	
        row.columnStored_id.value = obj.options[idx].value;
        row.columnStored_name.value = obj.options[idx].text;
    }else{
        return;
    }
   
   returnValue=true;
   window.close();
}

function validateSelect(obj,prompt){
    if(obj.selectedIndex > 0 && 
       obj.options[obj.selectedIndex].value > 0){
        return true;
    }else{
        alert("Please select a " + prompt);
        obj.focus();
        return false;
    }
}

function doCancel(){
   
   returnValue=false;
   window.close();
}


function doRefreshTableList(){
	serverid = document.getElementById("server_id").value;
	var selTable = document.getElementById("table_id");
	removeAllOptions(selTable);
	selTable.add( new Option("----",-1));
	for( i = 0; i < arrTables.length ; i++){
		if(arrTables[i].parentid == serverid){
			selTable.add( new Option(arrTables[i].name,arrTables[i].id));  
		}
	}	
	tableid = document.getElementById("table_id").value;	
	doRefreshColumnList();
}


function doRefreshColumnList(){
	tableid = document.getElementById("table_id").value;
	var selColDisplay = document.getElementById("column_display_id");
	var selColStore = document.getElementById("column_store_id");
	removeAllOptions(selColDisplay);
	removeAllOptions(selColStore);
	selColDisplay.add( new Option("----",-1));
	selColStore.add( new Option("----",-1));
	for( i = 0; i < arrColumns.length ; i++){
		if(arrColumns[i].parentid == tableid){
			selColDisplay.add( new Option(arrColumns[i].name,arrColumns[i].id));  
			selColStore.add( new Option(arrColumns[i].name,arrColumns[i].id));  
		}
	}	
}

 function removeAllOptions(obj)
 {
    var idx;
    obj.length = 0;
    //for(idx = 0; idx < obj.options.length; idx++)
        obj.options[idx] = null;
}