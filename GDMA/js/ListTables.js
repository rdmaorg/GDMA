function doColumns(serverID){
	if(selectedID == -1 ){
		alert('Please save the list of tables first');
	}else{
		if(selectedID){
			window.location.href='ListColumns.jsp?server_id='+serverID+'&table_id='+selectedID;
		}else{
			alert('Please select a table');
	 	}
	 }
} 
