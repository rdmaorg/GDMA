function doEdit(serverID){
	if(selectedID == -1 ){
		alert('Please save the list of tables first');
	}else{
		if(selectedID){
			var server_id = document.forms['frmMain'].elements['server_id'].value;
			window.location.href='ListColumns.jsp?server_id='+server_id+'&table_id='+selectedID;
		}else{
			alert('Please select a table');
	 	}
	 }
} 
